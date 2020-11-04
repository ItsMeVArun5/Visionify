package com.visionify.app;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import com.visionify.app.R;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;

public class DetectColor extends AppCompatActivity {
    ImageView imageview;
    TextView ResultTv;
    Button detectcolor;
    Button clear;
    Bitmap bitmap;
    int rgbdistance;
    int colorindex;
    int temprgbdistance;
    String resultcolor;
    TextToSpeech t1;

    public static final int PIC_IMAGE = 121;

    String[] colors = new String[]{"black","white","light-gray","gray","dark-gray","red"
            ,"pink","purple","light-blue","blue","yellow-green"
            ,"green","yellow","orange","brown","pale-pink"};

    int[][] colorsrgb = new int[][]{{0,0,0},{255,255,255},{224,224,224},{128,128,128}
            ,{64,64,64},{255,0,0},{255,96,208},{160,32,255}
            ,{80,208,255},{0,32,255},{96,255,128},{0,192,0}
            ,{255,224,32},{255,160,16},{160,128,96},{255,208,160}};

    public String getColorNameFromrgb(int r,int g,int b){
        colorindex = 0;
        rgbdistance = Math.abs(colorsrgb[0][0]-r)+Math.abs(colorsrgb[0][1]-g)+Math.abs(colorsrgb[0][2]-b);
        for(int i=1;i<colorsrgb.length;i++){
            temprgbdistance = Math.abs(colorsrgb[i][0]-r)+Math.abs(colorsrgb[i][1]-g)+Math.abs(colorsrgb[i][2]-b);
            if (temprgbdistance<rgbdistance){
                rgbdistance = temprgbdistance;
                colorindex = i;
            }
        }
        return colors[colorindex];
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detect_color);
        imageview = findViewById(R.id.imageView5);
        detectcolor = findViewById(R.id.button10);
        clear = findViewById(R.id.button11);
        ResultTv = findViewById(R.id.textView);

        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                }
            }
        });

        detectcolor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Image"),PIC_IMAGE);
            }
        });



        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageview.setImageBitmap(null);
                imageview.destroyDrawingCache();
            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode,int resultCode,@Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PIC_IMAGE){
            imageview.setImageURI(data.getData());
            imageview.setDrawingCacheEnabled(true);
            imageview.buildDrawingCache(true);

            imageview.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE){
                        bitmap = imageview.getDrawingCache();
                        int pixel = bitmap.getPixel((int)event.getX(),(int)event.getY());

                        // GETTING RGB VALUES
                        int r = Color.red(pixel);
                        int g = Color.green(pixel);
                        int b = Color.blue(pixel);

                        resultcolor = getColorNameFromrgb(r,g,b);

                        ResultTv.setText(resultcolor);
                        t1.speak(resultcolor, TextToSpeech.QUEUE_FLUSH, null);

                    }
                    return true;


                }
            });
        }
    }

}