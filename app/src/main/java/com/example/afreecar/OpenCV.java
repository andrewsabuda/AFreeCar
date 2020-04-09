package com.example.afreecar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class OpenCV extends AppCompatActivity {

    ImageView im; //Your image View
    double distanceBetween;
    Button launchOpenCv;
    Button backToGuide;
    public final String kitOneId = "assembly-requirements-one"; // This also needs to be stored in the database. Still need to implement compatibility for multiple different types of Kits. Right now, only one kit is being used.

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.opencv);
        im = findViewById(R.id.imageView);
        launchOpenCv = findViewById(R.id.launchOpenCv);
        backToGuide = findViewById(R.id.backToGuide);

        launchOpenCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent img = new Intent (); //Your Intent
                img.setAction(MediaStore.ACTION_IMAGE_CAPTURE); //the intents action to capture the image
                startActivityForResult(img,1);//start the activity adding any code .1 in this example
            }
        });
    }

    public double euclideanDistance(Point a, Point b){
        double distance = 0.0;
        try{
            if(a != null && b != null){
                double xDiff = a.x - b.x;
                double yDiff = a.y - b.y;
                distance = Math.sqrt(Math.pow(xDiff,2) + Math.pow(yDiff, 2));
            }
        }catch(Exception e){
            System.err.println("Something went wrong in euclideanDistance function in  "+e.getMessage());
        }
        return distance;
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        List<RotatedRect> boxes = new ArrayList<>();

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

            //sort the contours from left-to-right and, then initialize the
            //distance colors and reference object
            sortContours(contours);

            //loop over the contours individually
            for (MatOfPoint contour : contours) {
                //compute the rotated bounding box of the contour
                RotatedRect box = Imgproc.minAreaRect(new MatOfPoint2f(contour.toArray()));
                boxes.add(box);
            }

            Scalar yellow = new Scalar(255, 255, 0);
            Scalar blue = new Scalar(0, 0, 255);

            Imgproc.circle(img, boxes.get(0).center, 2, yellow);

            if(boxes.size() > 1) {
                Imgproc.circle(img, boxes.get(1).center, 2, blue);
                Imgproc.line(img, boxes.get(0).center, boxes.get(1).center, yellow, 2);
                distanceBetween = Math.ceil(euclideanDistance(boxes.get(0).center, boxes.get(1).center));
            }

            Utils.matToBitmap(img, photo);

            im.setImageBitmap(photo);// set photo to imageView
        }

        if(distanceBetween < 90 && distanceBetween > 80) {
            final Intent assembleIntent = getIntent();
            final String[] requirementsList = assembleIntent.getStringArrayExtra("requirementsList");
            final int listPosition = assembleIntent.getIntExtra("listPosition", 0);

            final Toast toast1 = Toast.makeText(getApplicationContext(), "List item completed", Toast.LENGTH_SHORT);
            toast1.show();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    toast1.cancel();
                }
            }, 800);

            final Intent intent = new Intent(OpenCV.this, RequirementsActivity.class);

            requirementsList[listPosition] = "completed";
            intent.putExtra("kitId", kitOneId);                      //Sending the kit ID value to RequirementsActivity
            intent.putExtra("kitRequirements", requirementsList);

            backToGuide.setVisibility(View.VISIBLE);
            backToGuide.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OpenCV.this.startActivity(intent);
                }
            });
        } else {
            final Toast toast1 = Toast.makeText(getApplicationContext(), "Insufficient connection, please try again. Distance: " + distanceBetween, Toast.LENGTH_SHORT);
            toast1.show();
        }
    }
}