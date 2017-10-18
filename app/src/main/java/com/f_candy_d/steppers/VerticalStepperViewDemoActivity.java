package com.f_candy_d.steppers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.f_candy_d.verticalsteppers.VerticalStepperView;

public class VerticalStepperViewDemoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertical_stepper_view_demo);

        final VerticalStepperView stepperView = (VerticalStepperView) findViewById(R.id.vertical_stepper_view);

        findViewById(R.id.activate_inactivate_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (stepperView.isStepActive()) {
                    stepperView.animateChangingStatus(VerticalStepperView.ANIMATE_INACTIVATE_STEP);
                } else {
                    stepperView.animateChangingStatus(VerticalStepperView.ANIMATE_ACTIVATE_STEP);
                }
            }
        });

        findViewById(R.id.expand_collapse_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (stepperView.isContentViewExpanded()) {
                    stepperView.animateChangingStatus(VerticalStepperView.ANIMATE_COLLAPSE_CONTENTS);
                } else {
                    stepperView.animateChangingStatus(VerticalStepperView.ANIMATE_EXPAND_CONTENTS);
                }
            }
        });

        findViewById(R.id.complete_incomplete_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (stepperView.isStepCompleted()) {
                    stepperView.animateChangingStatus(VerticalStepperView.ANIMATE_INCOMPLETE_STEP);
                } else {
                    stepperView.animateChangingStatus(VerticalStepperView.ANIMATE_COMPLETE_STEP);
                }
            }
        });

        findViewById(R.id.toggle_all_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                @VerticalStepperView.ChangingStatusAnimationFlag int flags = 0;
                flags |= (stepperView.isContentViewExpanded()) ? VerticalStepperView.ANIMATE_COLLAPSE_CONTENTS : VerticalStepperView.ANIMATE_EXPAND_CONTENTS;
                flags |= (stepperView.isStepActive()) ? VerticalStepperView.ANIMATE_INACTIVATE_STEP : VerticalStepperView.ANIMATE_ACTIVATE_STEP;
                flags |= (stepperView.isStepCompleted()) ? VerticalStepperView.ANIMATE_INCOMPLETE_STEP : VerticalStepperView.ANIMATE_COMPLETE_STEP;
                stepperView.animateChangingStatus(flags);
            }
        });
    }
}
