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
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class DetectObject extends AppCompatActivity {
    ImageView imageview;
    Button detectobject;
    Button clear;
    TextToSpeech t1;
    public static final int PIC_IMAGE = 121;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detect_object);
        imageview = findViewById(R.id.imageView4);
        detectobject = findViewById(R.id.button7);
        clear = findViewById(R.id.button8);

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

        detectobject.setOnClickListener(new View.OnClickListener() {
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

                FirebaseVisionImageLabeler labeler = FirebaseVision.getInstance()
                        .getOnDeviceImageLabeler();

                labeler.processImage(image)
                        .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionImageLabel>>() {
                            @Override
                            public void onSuccess(List<FirebaseVisionImageLabel> labels) {
                                // Task completed successfully
                                // ...
                                int temp = 1;
                                for (FirebaseVisionImageLabel label: labels) {

                                    String text = label.getText();
                                    //resultTv.append(text+"   "+label.getConfidence()+"\n");
                                    String entityId = label.getEntityId();
                                    float confidence = label.getConfidence();
                                    if(temp==1){
                                        t1.speak(text, TextToSpeech.QUEUE_FLUSH, null);
                                    }
                                    temp = temp+1;
                                }

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
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