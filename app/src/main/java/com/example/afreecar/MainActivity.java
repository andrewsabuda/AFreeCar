package com.example.afreecar;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    SurfaceView cameraPreview;
    TextView txtResult;
    BarcodeDetector barcodeDetector;
    CameraSource cameraSource;
    Button confirmResult;
    Button backToGuide;
    Button openCv;
    ImageView im; //Your image View

    public final String[] kitOneRequirements = { "1,2", "3,4", "5,6" }; // These need to be stored in the database.
    public final String kitOneId = "assembly-requirements-one"; // This also needs to be stored in the database. Still need to implement compatibility for multiple different types of Kits. Right now, only one kit is being used.
    String[] scannedValues = new String[2];
    final int RequestCameraPermissionID = 1001;
    //Sean test push

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RequestCameraPermissionID: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    try {
                        cameraSource.start(cameraPreview.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            break;
        }
    }

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {

                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };

    @Override
    public void onResume()
    {
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, this, mLoaderCallback);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);//method retrieves the requestCode , its result and the data containing the pic from system
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data"); //get data and casts it into Bitmap photo

            Mat img = new Mat();
            Utils.bitmapToMat(photo, img);

            Mat imageGray = new Mat();

            //load the image, convert it to grayscale, and blur it slightly
            Imgproc.cvtColor(img, imageGray, Imgproc.COLOR_RGB2GRAY);
            Imgproc.GaussianBlur(imageGray, imageGray, new Size(7, 7), 0);

            //perform edge detection, then perform a dilation + erosion to
            //close gaps in between object edges
            Mat edged = new Mat();
            Imgproc.Canny(imageGray, edged, 50, 100);
            Imgproc.dilate(edged, edged, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(2,2)));
            Imgproc.erode(edged, edged, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(2,2)));

            //find contours in the edge map
            ArrayList<MatOfPoint> contours = new ArrayList<MatOfPoint>();
            Mat hierarchy = new Mat();
            Imgproc.findContours(edged, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
            hierarchy.release();

            //sort the contours from left-to-right and, then initialize the
            //distance colors and reference object
            sortContours(contours);
            int[] arrayColors ={ Color.BLUE, Color.RED, Color.CYAN, Color.YELLOW, Color.MAGENTA};
            Object refObj = new Object();

            //loop over the contours individually
            for (MatOfPoint contour : contours) {
                List<RotatedRect> boxes = new ArrayList<>();

                //if the contour is not sufficiently large, ignore it
                if(Imgproc.contourArea(contour) < 100) {
                    continue;
                }

                //compute the rotated bounding box of the contour
                RotatedRect box = Imgproc.minAreaRect(new MatOfPoint2f(contour.toArray()));
                boxes.add(box);

                // Getting the vertices of the rectangle
                List<Point[]> vertices = initialiseWithDefaultPointInstances(boxes.size(), 4);
                for(int i=0; i<boxes.size(); i++){
                    boxes.get(i).points(vertices.get(i));
                }

                /** With the vertices, the object size is calculated.
                 *  The object size is calculated through pythagoras theorm. In addition, it gives
                 *  the distance between 2 points in a bi-dimensional space.
                 *
                 *  For a rectangle, considering any vertex V, its two sizes (width and height) can
                 *  be calculated by calculating the distance of V from the previous vertex and
                 *  calculating the distance of V from the next vertex. This is the reason why I
                 *  calculate the distance between vertici[0]/vertici[3] and vertici[0]/vertici[1]
                 */
                double conversionFactor = 1.0;
                List<Integer> objectWidth = new ArrayList<>();
                List<Integer> objectHeight = new ArrayList<>();
                for(Point[] points : vertices){
                    int width = (int) (conversionFactor * Math.sqrt((points[0].x - points[3].x) * (points[0].x - points[3].x) + (points[0].y - points[3].y) * (points[0].y - points[3].y)));
                    int height = (int) (conversionFactor * Math.sqrt((points[0].x - points[1].x) * (points[0].x - points[1].x) + (points[0].y - points[1].y) * (points[0].y - points[1].y)));
                    objectWidth.add(width);
                    objectHeight.add(height);
                }

                /** Draw the rectangle containing the contours. The line method draws a line from 1
                 *  point to the next, and accepts only integer coordinates; for this reason, 2
                 *  temporary Points have been created and why I used Math.round method.
                 */

                Scalar blue = new Scalar(0, 0, 255);
                for (int i=0; i<vertices.size(); i++){
                    Point pt1 = new Point();
                    Point pt2 = new Point();
                    for (int j = 0; j < 4; j++) {
                        pt1.x = Math.round(vertices.get(i)[j].x);
                        pt1.y = Math.round(vertices.get(i)[j].y);
                        pt2.x = Math.round(vertices.get(i)[(j + 1) % 4].x);
                        pt2.y = Math.round(vertices.get(i)[(j + 1) % 4].y);
                        Imgproc.line(img, pt1, pt2, blue, 3);
                    }
                    if (objectWidth.get(i) != 0 && objectHeight.get(i) != 0){
                        Imgproc.putText(img, "width: " + objectWidth + ", height: " + objectHeight, new Point(Math.round(vertices.get(i)[1].x), Math.round(vertices.get(i)[1].y)), 1, 1, blue);
                    }
                }

            }
            Utils.matToBitmap(img, photo);

            im.setImageBitmap(photo);// set photo to imageView
        }
    }

    // Initialising an array of points
    public static List<Point[]> initialiseWithDefaultPointInstances(int n_Contours, int n_Points) {
        List<Point[]> pointsList = new ArrayList<>();
        for(int i=0; i<n_Contours; i++){
            Point[] array = new Point[n_Points];
            for (int j = 0; j < n_Points; j++) {
                array[j] = new Point();
            }
            pointsList.add(array);
        }
        return pointsList;
    }

    /**
     * Sorts contours from left to right (assumes a length of 2)
     *
     * @param contours
     *            List of contours to sort
     */
    public ArrayList sortContours(ArrayList<MatOfPoint> contours) {
        if (getLeftMostX(contours.get(0)) > getLeftMostX(contours.get(1))) {
            contours.add(contours.remove(0));
        }

        return contours;
    }

    /**
     * @param points
     *            List of points representing a contour
     * @return The X coordinate of the left-most point in {@code points}
     */
    public double getLeftMostX(MatOfPoint points) {
        List<Point> coords = points.toList();
        double leftMostX = coords.get(0).x;
        for (int i = 0; i < coords.size(); i++) {
            if (coords.get(i).x < leftMostX) {
                leftMostX = coords.get(i).x;
            }
        }
        return leftMostX;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cameraPreview = findViewById(R.id.cameraPreview);
        txtResult = findViewById(R.id.txtResult);
        confirmResult = findViewById(R.id.confirmResult);
        backToGuide = findViewById(R.id.backToGuide);
        openCv = findViewById(R.id.openCv);
        im =(ImageView)findViewById(R.id.imageView);


        final Intent intent = new Intent(MainActivity.this, OpenCV.class);

        openCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent img = new Intent (); //Your Intent
                img.setAction(MediaStore.ACTION_IMAGE_CAPTURE); //the intents action to capture the image
                startActivityForResult(img,1);//start the activity adding any code .1 in this example
            }
        });

        final Intent assembleIntent = getIntent();
        final boolean isAssembling = assembleIntent.getBooleanExtra("isAssembling", false);

        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.QR_CODE)
                .build();
        cameraSource = new CameraSource
                .Builder(this, barcodeDetector)
                .setRequestedPreviewSize(640, 640)
                .build();
        //Add Event
        cameraPreview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    //Request permission
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.CAMERA},RequestCameraPermissionID);
                    return;
                }
                try {
                    cameraSource.start(cameraPreview.getHolder());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                cameraSource.stop();

            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> qrcodes = detections.getDetectedItems();

                if(qrcodes.size() != 0) {

                    if(!isAssembling) {

                        txtResult.post(new Runnable() {
                            @Override
                            public void run() {
                                final Intent intent = new Intent(MainActivity.this, RequirementsActivity.class);

                                //Create vibrate
                                Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                                vibrator.vibrate(1000);

                                String qrValue = qrcodes.valueAt(0).displayValue;      //THIS IS WHERE THE QR CODE IS BEING READ AND TRANSLATED

                                if (qrValue.equals(kitOneId)) {

                                    txtResult.setText(qrValue);
                                    intent.putExtra("kitId", qrValue);                      //Sending the kit ID value to RequirementsActivity
                                    intent.putExtra("kitRequirements", kitOneRequirements);    //Sending the kit requirements array

                                    confirmResult.setVisibility(View.VISIBLE);
                                    confirmResult.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            MainActivity.this.startActivity(intent);
                                        }
                                    });
                                }
                            }
                        });
                    } else {

                        final String stepValue = assembleIntent.getStringExtra("stepValue");
                        final String[] requirementsList = assembleIntent.getStringArrayExtra("requirementsList");
                        final int listPosition = assembleIntent.getIntExtra("listPosition", 0);

                        txtResult.post(new Runnable() {
                            @Override
                            public void run() {
                                String[] stepsArray = stepValue.split(",");
                                String valueScanned = String.valueOf('x');

                                final Intent intent = new Intent(MainActivity.this, RequirementsActivity.class);

                                //Create vibrate
                                Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                                vibrator.vibrate(1000);

                                String qrValue = qrcodes.valueAt(0).displayValue;      //THIS IS WHERE THE QR CODE IS BEING READ AND TRANSLATED

                                if(qrValue.equals(stepsArray[0]) || qrValue.equals(stepsArray[1])) {

                                    if(qrValue.equals(stepsArray[0])) {

                                        scannedValues[0] = valueScanned;

                                        if((Arrays.toString(scannedValues)).equals("[x, x]")) {

                                            final Toast toast = Toast.makeText(getApplicationContext(), "List item completed", Toast.LENGTH_SHORT);
                                            toast.show();
                                            Handler handler = new Handler();
                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    toast.cancel();
                                                }
                                            }, 800);

                                            requirementsList[listPosition] = "completed";
                                            intent.putExtra("kitId", kitOneId);                      //Sending the kit ID value to RequirementsActivity
                                            intent.putExtra("kitRequirements", requirementsList);

                                            backToGuide.setVisibility(View.VISIBLE);
                                            backToGuide.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    MainActivity.this.startActivity(intent);
                                                }
                                            });
                                        } else {

                                            final Toast toast = Toast.makeText(getApplicationContext(), "CORRECT SCAN! PLEASE SCAN THE NEXT CONNECTION", Toast.LENGTH_SHORT);
                                            toast.show();
                                            Handler handler = new Handler();
                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    toast.cancel();
                                                }
                                            }, 800);
                                        }
                                    } else {

                                        scannedValues[1] = valueScanned;

                                        if((Arrays.toString(scannedValues)).equals("[x, x]")) {
                                            final Toast toast = Toast.makeText(getApplicationContext(), "List item completed", Toast.LENGTH_SHORT);
                                            toast.show();
                                            Handler handler = new Handler();
                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    toast.cancel();
                                                }
                                            }, 800);

                                            requirementsList[listPosition] = "completed";
                                            intent.putExtra("kitId", kitOneId);                      //Sending the kit ID value to RequirementsActivity
                                            intent.putExtra("kitRequirements", requirementsList);

                                            backToGuide.setVisibility(View.VISIBLE);
                                            backToGuide.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    MainActivity.this.startActivity(intent);
                                                }
                                            });
                                        } else {
                                            final Toast toast = Toast.makeText(getApplicationContext(), "CORRECT SCAN! PLEASE SCAN THE NEXT CONNECTION", Toast.LENGTH_SHORT);
                                            toast.show();
                                            Handler handler = new Handler();
                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    toast.cancel();
                                                }
                                            }, 800);
                                        }
                                    }
                                } else {
                                    final Toast toast = Toast.makeText(getApplicationContext(), "WRONG SCAN", Toast.LENGTH_SHORT);
                                    toast.show();
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            toast.cancel();
                                        }
                                    }, 800);
                                }
                            }
                        });
                    }
                }
            }
        });
    }
}
