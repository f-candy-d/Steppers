package com.f_candy_d.steppers;

import android.support.transition.TransitionManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.f_candy_d.verticalsteppers.VerticalStepper;

public class VerticalStepperViewDemoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertical_stepper_view_demo);

        final RelativeLayout rlRoot = (RelativeLayout) findViewById(R.id.rl_root);
        final VerticalStepper stepper = (VerticalStepper) findViewById(R.id.vertical_stepper);

        findViewById(R.id.activate_inactivate_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TransitionManager.beginDelayedTransition(rlRoot);
                stepper.toggleStepActiveMode();
            }
        });

        findViewById(R.id.check_uncheck_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TransitionManager.beginDelayedTransition(rlRoot);
                stepper.toggleStepCheckMode();
            }
        });

        findViewById(R.id.toggle_all_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TransitionManager.beginDelayedTransition(rlRoot);
                stepper.toggleStepModes();
            }
        });
    }
}
