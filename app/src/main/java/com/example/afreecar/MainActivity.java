package com.example.afreecar;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

import java.io.IOException;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    SurfaceView cameraPreview;
    TextView txtResult;
    BarcodeDetector barcodeDetector;
    CameraSource cameraSource;
    Button confirmResult;
    Button goToSpaceDetector;

    public String[] kitOneRequirements; // This is retrieved from the database.
    public final String kitOneId = "assembly-requirements-one"; // This value will be used to query the database.
    String[] scannedValues = new String[2];
    final int RequestCameraPermissionID = 1001;

    // Database initialization.
    DatabaseReference kitIdRef = FirebaseDatabase.getInstance().getReference("kitId");
    DatabaseReference kitListRef = kitIdRef.child(kitOneId); // Querying the database for this particular kit ID.

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cameraPreview = findViewById(R.id.cameraPreview);
        txtResult = findViewById(R.id.txtResult);
        confirmResult = findViewById(R.id.confirmResult);
        goToSpaceDetector = findViewById(R.id.openCv);

        final Intent intent = new Intent(MainActivity.this, RequirementsActivity.class);
        final Intent spaceDetectorIntent = new Intent(MainActivity.this, OpenCV.class);
        final Intent assembleIntent = getIntent();
        final boolean isAssembling = assembleIntent.getBooleanExtra("isAssembling", false);

        // RETRIEVE KIT ONE VALUES FROM THE DATABASE
        kitListRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String databaseValue = dataSnapshot.getValue(String.class);
                kitOneRequirements = databaseValue.split("/");
                Log.i("KIT", kitOneRequirements[0]);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("ERROR", "onCancelled", databaseError.toException());
            }
        });

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

                                //Create vibrate
                                Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                                vibrator.vibrate(1000);

                                String qrValue = qrcodes.valueAt(0).displayValue;      //THIS IS WHERE THE QR CODE IS BEING READ AND TRANSLATED

                                if(qrValue.equals(stepsArray[0]) || qrValue.equals(stepsArray[1])) {

                                    if(qrValue.equals(stepsArray[0])) {

                                        scannedValues[0] = valueScanned;

                                        if((Arrays.toString(scannedValues)).equals("[x, x]")) {

                                            spaceDetectorIntent.putExtra("listPosition", listPosition);
                                            spaceDetectorIntent.putExtra("kitId", kitOneId);                      //Sending the kit ID value to RequirementsActivity
                                            spaceDetectorIntent.putExtra("requirementsList", requirementsList);

                                            goToSpaceDetector.setVisibility(View.VISIBLE);
                                            goToSpaceDetector.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    MainActivity.this.startActivity(spaceDetectorIntent);
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

                                            spaceDetectorIntent.putExtra("listPosition", listPosition);
                                            spaceDetectorIntent.putExtra("kitId", kitOneId);                      //Sending the kit ID value to RequirementsActivity
                                            spaceDetectorIntent.putExtra("requirementsList", requirementsList);

                                            goToSpaceDetector.setVisibility(View.VISIBLE);
                                            goToSpaceDetector.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    MainActivity.this.startActivity(spaceDetectorIntent);
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
