package com.visionify.app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import com.visionify.app.R;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import java.io.IOException;
import java.util.Locale;

public class DetectText extends AppCompatActivity {
    ImageView imageview;
    Button detecttext;
    Button clear;
    TextToSpeech t1;
    public static final int PIC_IMAGE = 121;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detect_text);
        imageview = findViewById(R.id.imageView3);
        detecttext = findViewById(R.id.button4);
        clear = findViewById(R.id.button5);

        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                }
            }
        });



        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageview.setImageBitmap(null);
                imageview.destroyDrawingCache();
            }
        });

        detecttext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Image"),PIC_IMAGE);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PIC_IMAGE){
            imageview.setImageURI(data.getData());
            FirebaseVisionImage image;
            try {
                image = FirebaseVisionImage.fromFilePath(getApplicationContext(), data.getData());
                FirebaseVisionTextRecognizer textRecognizer = FirebaseVision.getInstance()
                        .getOnDeviceTextRecognizer();
                textRecognizer.processImage(image)
                        .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                            @Override
                            public void onSuccess(FirebaseVisionText result) {
                                // Task completed successfully
                                // ...
                                //resultTv.setText(result.getText());
                                t1.speak(result.getText(), TextToSpeech.QUEUE_FLUSH, null);
                            }
                        })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Task failed with an exception
                                        // ...
                                    }
                                });
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}