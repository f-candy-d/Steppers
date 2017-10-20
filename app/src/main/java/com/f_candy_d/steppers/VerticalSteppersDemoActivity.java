package com.f_candy_d.steppers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.f_candy_d.verticalsteppers.Step;
import com.f_candy_d.verticalsteppers.VerticalStepperListView;

import java.util.ArrayList;

public class VerticalSteppersDemoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertical_steppers_demo);

        VerticalStepperListView stepperListView = (VerticalStepperListView) findViewById(R.id.vertical_steppers);
        ArrayList<Step> steps = new ArrayList<>();
        for (int i = 0; i < 10; ++i) {
            steps.add(new DummyStep(i, i));
        }
        stepperListView.build(this, steps);
    }
}