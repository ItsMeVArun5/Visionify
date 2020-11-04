package com.visionify.app;

import android.content.Intent;
import com.visionify.app.R;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    private ImageView logo;
    private static int splashTimeout=3000;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        logo=(ImageView)findViewById(R.id.visionify_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashScreen.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        }, splashTimeout);

        Animation myanim = AnimationUtils.loadAnimation(this,R.anim.mysplashanimation);
        logo.setAnimation(myanim);


    }
}
