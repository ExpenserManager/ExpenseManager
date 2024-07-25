package com.example.expensermanager;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.expensermanager.databinding.ActivityQrReaderBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

// https://www.centron.de/tutorial/java-qr-code-generator-leitfaden/
// https://blog.aspose.com/de/barcode/scan-qr-code-in-java/
// https://www.youtube.com/watch?v=bWEt-_z7BOY
// https://developer.android.com/media/camera/camerax/mlkitanalyzer
// https://github.com/android/camera-samples/blob/main/CameraX-MLKit/app/src/main/java/com/example/camerax_mlkit/MainActivity.kt
// https://developer.android.com/jetpack/androidx/releases/camera#kts
// https://github.com/Scandit/datacapture-android-samples/blob/master/BarcodeCaptureSimpleSample/src/main/java/com/scandit/datacapture/barcodecapturesimplesample/CameraPermissionActivity.java
// https://stackoverflow.com/questions/75199929/how-can-i-request-camera-permission-in-android-studio-new-version
// https://developer.android.com/media/camera/camerax/analyze

/**
 * QR_Reader is an Activity that sets up and uses the device camera to scan QR codes.
 * It uses CameraX for camera management and ML Kit for QR code scanning.
 */

public class QR_Reader extends AppCompatActivity {
    TextView qrTextView;
    TextView infoText;
    private PreviewView previewView;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;
    ImageButton backButtonQR;
    FloatingActionButton infoButton;
    private Animation scaleAnimation;
    private Animation bounceAnimation;
    @SuppressLint({"MissingInflatedId", "ResourceType"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_qr_reader);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // values in the xmls from ChatGPT
        scaleAnimation = AnimationUtils.loadAnimation(this, R.transition.scale);
        bounceAnimation = AnimationUtils.loadAnimation(this, R.transition.bounce);
        backButtonQR = findViewById(R.id.backButtonQR);
        doAnimation(backButtonQR);
        backButtonQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("QR_Reader", "Back button clicked");

                Intent intent = new Intent(QR_Reader.this, HomeScreenActivity.class);
                startActivity(intent);
                finish();
            }
        });
        infoButton = findViewById(R.id.infoButton);
        doAnimation(infoButton);
        infoButton.setOnClickListener(v -> openInfoDialog());

        qrTextView = findViewById(R.id.qrText);

        // check if the app is allowed to use the camera
        if (hasCameraPermission()) {
            previewView = findViewById(R.id.previewView);
            startCamera();
        } else {
            requestCameraPermission();
        }


    }
    private void doAnimation(View button){
        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        v.startAnimation(scaleAnimation);
                        break;
                    case MotionEvent.ACTION_UP:
                        v.startAnimation(bounceAnimation);
                        break;
                }
                return false;
            }
        });
    }

    private void openInfoDialog() {
        Log.d("QR_Reader", "Opening info dialog");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_info, null);
        infoText = dialogView.findViewById(R.id.infotext);

        infoText.setText("What can the QR-Code tell? " + "\n" + "\n" +
                "- Date in format YYYY-MM-DD" + "\n" +
                "- Time in format HH:MM:SS" + "\n" +
                "- Amount f.e. 20,99" + "\n" + "\n" +
                "The rest is usually important for the stores, and are just identificators."+ "\n" +
                "It can also happen that it gives a link.");
        infoText.setTextSize(22);
        builder.setView(dialogView);
        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private boolean hasCameraPermission() {
        return ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera(); // permission granted -> camera can be used
            } else {
                Toast.makeText(this, "Camera permission is necessary to use the QR-Reader!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startCamera() {
        // receive an instance of ProcessCameraProvider, which is responsible for managing camera lifecycle and use cases
        ListenableFuture<ProcessCameraProvider> processCameraProviderListenableFuture = ProcessCameraProvider.getInstance(this);

        // when the processCameraProviderListenableFuture is done
        processCameraProviderListenableFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = processCameraProviderListenableFuture.get();
                previewAnalyser(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                Toast.makeText(this, "Error starting camera: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }, ContextCompat.getMainExecutor(this)); // execute the listener on the main thread to ensure UI updates are performed correctly
    }

    /**
     * The previewAnalyser() method configures and binds the camera preview and image analysis (for QR code scanning)
     * to the activity's lifecycle, setting up the QR_Analyser and selecting the back camera.
     * @param cameraProvider
     */
    private void previewAnalyser(@NonNull ProcessCameraProvider cameraProvider) {
        // camera preview is shown on screen
        Preview preview = new Preview.Builder().build();
        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        // instantiate the QR_Analyser with the context, a single-threaded executor, and the TextView
        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build();
        // register the analyser (analyses camera pictures)
        // each task is completed within a new thread -> more efficient ana enables parallelism
        // creates new QR_Analyser object with this context, the executor and the textView of this activity
        imageAnalysis.setAnalyzer(Executors.newSingleThreadExecutor(), new QR_Analyser(this, Executors.newSingleThreadExecutor(), qrTextView));

        // backcamera should be used for more convenient usage
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        cameraProvider.unbindAll(); // should be done to make sure no previous use cases are still bound
        cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis); // now the new parameters are bound
    }
}