package com.example.expensermanager;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Debug;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.content.FileProvider;

import com.example.expensermanager.databinding.ActivityMainBinding;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class UpdateDataActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    private static final int REQUEST_TAKE_PHOTO = 1;

    private String currentPhotoPath; // Variablen für den Pfad des Bildes
    private ImageView photoImageView;
    private DatabaseHelper dbHelper;
    String description;
    String amount;
    String id;
    String date;
    DatePicker datePicker;
    EditText dateFieldText;
    ImageButton deleteButton;
    ImageButton backButton;

    private Animation scaleAnimation;
    private Animation bounceAnimation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_update_data);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        scaleAnimation = AnimationUtils.loadAnimation(this, R.transition.scale);
        bounceAnimation = AnimationUtils.loadAnimation(this, R.transition.bounce);

        dbHelper = new DatabaseHelper(UpdateDataActivity.this);
        EditText descriptionField = findViewById(R.id.description);
        EditText amountField = findViewById(R.id.amount);
        Button updateButton = findViewById(R.id.updateButton);
        EditText dateField = findViewById(R.id.date);

        Spinner spinner = findViewById(R.id.spinner2);

        ArrayList<String> spinnerList = dbHelper.getAllCategories();
        spinner.setAdapter(new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, spinnerList)); //show categories in spinner

        photoImageView = findViewById(R.id.expense_image_view);


        Intent intent = getIntent();
        if (intent != null) {
            description = intent.getStringExtra("description");
            amount = intent.getStringExtra("amount");
            id = intent.getStringExtra("id");
            date = intent.getStringExtra("date");
            currentPhotoPath = intent.getStringExtra("image_path");

            descriptionField.setText(description);
            amountField.setText(amount);
            dateField.setText(date);

            if (currentPhotoPath != null) {
                displayImageFromPath(currentPhotoPath);
            }
        }
        doAnimation(updateButton);
        updateButton.setOnClickListener(v -> {
            String newDescription = descriptionField.getText().toString();
            String newAmount = amountField.getText().toString();
            String newDate = dateField.getText().toString();
            String newPath = currentPhotoPath;
            Log.d("Path", "" + id);
            dbHelper.updateData(id, newDescription, newAmount, newDate, newPath, "expense_manager");

            //give data back to ExpenseViewActivity
            Intent goBackIntent = new Intent();
            goBackIntent.putExtra("id", id);
            goBackIntent.putExtra("description", newDescription);
            goBackIntent.putExtra("amount", newAmount);
            goBackIntent.putExtra("date", newDate);
            //goBackIntent.putExtra("image_path", currentPhotoPath);
            setResult(RESULT_OK, goBackIntent);
            finish();
        });


        dateFieldText = findViewById(R.id.date);
        dateFieldText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        UpdateDataActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                dateFieldText.setText(dayOfMonth + "-" + (month+1) + "-" + year);
                            }
                        },year,month, day);

                datePickerDialog.show();

            }
        });

        deleteButton =  findViewById(R.id.deleteButton);
        doAnimation(deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.deleteData(dbHelper, id, "expense_manager" );
                Intent intent = new Intent();
                intent.putExtra("deleteID", id);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        backButton = findViewById(R.id.backButton);
        doAnimation(backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button takePictureButton = findViewById(R.id.receiptButton);
        doAnimation(takePictureButton);
        takePictureButton.setOnClickListener(v -> dispatchTakePictureIntent());
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Log.d("Debug", "Error while saving picture");
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.expensermanager.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Calendar.getInstance().getTime());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(null);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            File file = new File(currentPhotoPath);
            if (file.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);
                photoImageView.setImageBitmap(bitmap);
            }
        }
    }

    private void displayImageFromPath(String imagePath) {
        File imgFile = new File(imagePath);
        if (imgFile.exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            photoImageView.setImageBitmap(myBitmap);
        }
    }
    private void doAnimation(View button) {
        button.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    v.startAnimation(scaleAnimation);
                    break;
                case MotionEvent.ACTION_UP:
                    v.startAnimation(bounceAnimation);
                    break;
            }
            return false;
        });
    }
}