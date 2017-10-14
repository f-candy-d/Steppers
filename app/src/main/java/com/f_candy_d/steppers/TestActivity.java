package com.f_candy_d.steppers;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.media.Image;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class TestActivity extends AppCompatActivity {

    boolean isTranslated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        final ImageView img = (ImageView) findViewById(R.id.img_view);

        TransitionDrawable drawable = (TransitionDrawable) img.getDrawable();
        drawable.setId(0, 0);
        drawable.setId(1, 1);

        Drawable d = drawable.findDrawableByLayerId(0);
        DrawableCompat.setTint(d, Color.CYAN);
        DrawableCompat.setTintMode(d, PorterDuff.Mode.SRC_IN);

        findViewById(R.id.toggle_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TransitionDrawable transitionDrawable = (TransitionDrawable) img.getDrawable();
                if (isTranslated) {
                    transitionDrawable.reverseTransition(200);
                } else {
                    transitionDrawable.startTransition(200);
                }
                isTranslated = !isTranslated;
            }
        });
    }
}
