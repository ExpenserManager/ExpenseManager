package com.example.expensermanager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
//import com.google.mlkit.vision.barcode.Barcode;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;
import java.util.List;
import java.util.concurrent.Executor;

// https://developer.android.com/media/camera/camerax/mlkitanalyzer
// https://developer.android.com/media/camera/camerax/analyze
// https://developers.google.com/android/reference/com/google/mlkit/vision/barcode/BarcodeScanner


/**
 * The QR_Analyser class is an implementation of ImageAnalysis.Analyzer used for
 * processing images from the camera to detect and read QR codes.
 * It utilizes Google ML Kit's BarcodeScanning API to perform QR code detection.
 */
public class QR_Analyser implements ImageAnalysis.Analyzer {
    //
    private final BarcodeScanner barcodeScanner; // used to read barcode and QR-Code (here just QR)
    private final Executor executor; // executes in background
    private TextView qrTextView;


    public QR_Analyser(Context context, Executor executor, TextView qrTextView) {
        // create scanner object which should only read QR-Codes
        BarcodeScannerOptions options = new BarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                .build();
        this.barcodeScanner = BarcodeScanning.getClient(options); // initializes the scanner with the previously defined options
        this.executor = executor;
        this.qrTextView = qrTextView; // QR-Code text will be shown here
    }

    /**
     * analyses the inPutImage to detect the QR-Code
     */
    @Override
    public void analyze(@NonNull ImageProxy image) {
        @SuppressLint("UnsafeOptInUsageError")
        InputImage inputImage = InputImage.fromMediaImage(image.getImage(), image.getImageInfo().getRotationDegrees());

        barcodeScanner.process(inputImage)
                .addOnSuccessListener(executor, barcodes -> { // onSuccess callback is executed on the specified executor thread.
                    for (Barcode barcode : barcodes) {  // for each detected QR-Code its text will be displayed
                        String qrContent = barcode.getDisplayValue();
                        Log.d("QRCodeAnalyzer", "QR Code detected: " + qrContent);
                        // set the received text of the read QR-Code in the textView
                        ((AppCompatActivity) qrTextView.getContext()).runOnUiThread(() -> qrTextView.setText(qrContent));
                    }
                })
                .addOnFailureListener(executor, e -> Log.e("QRCodeAnalyzer", "QR Code detection failed", e))
                .addOnCompleteListener(task -> image.close()); // closes the ImageProxy object (image.close()) to free resources
    }
}

