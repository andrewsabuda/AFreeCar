package com.example.afreecar;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//import com.google.firebase.firestore.FirebaseFirestore;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;
import java.util.Arrays;

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

    protected void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);//method retrieves the requestCode , its result and the data containing the pic from system
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data"); //get data and casts it into Bitmap photo

            Mat img1 = new Mat();
            Utils.bitmapToMat(photo, img1);

            Mat imageGray1 = new Mat();

            Imgproc.cvtColor(img1, imageGray1, Imgproc.COLOR_RGB2GRAY);

            Utils.matToBitmap(imageGray1, photo);

            im.setImageBitmap(photo);// set photo to imageView
        }
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
