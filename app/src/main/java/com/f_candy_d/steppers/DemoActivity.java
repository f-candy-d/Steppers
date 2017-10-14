package com.f_candy_d.steppers;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.transition.TransitionManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.f_candy_d.verticalsteppers.VerticalStepperListView;
import com.f_candy_d.verticalsteppers.VerticalStepperView;

public class DemoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final VerticalStepperView stepperView = (VerticalStepperView) findViewById(R.id.stepper);

        findViewById(R.id.toggle_active_inactive).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (stepperView.isStepActive()) {
//                    stepperView.inactivateStep(false);
                    stepperView.animateChangingStatus(VerticalStepperView.ANIMATE_INACTIVATE_STEP);
                } else {
//                    stepperView.activateStep(false);
                    stepperView.animateChangingStatus(VerticalStepperView.ANIMATE_ACTIVATE_STEP);
                }
            }
        });

        findViewById(R.id.toggle_expanded_collapsed).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (stepperView.isContentViewExpanded()) {
//                    stepperView.collapseContentView(false);
                    stepperView.animateChangingStatus(VerticalStepperView.ANIMATE_COLLAPSE_CONTENTS);
                } else {
//                    stepperView.expandContentView(false);
                    stepperView.animateChangingStatus(VerticalStepperView.ANIMATE_EXPAND_CONTENTS);
                }
            }
        });

        findViewById(R.id.toggle_completed_incompleted).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (stepperView.isStepCompleted()) {
//                    stepperView.incompleteStep(false);
                    stepperView.animateChangingStatus(VerticalStepperView.ANIMATE_INCOMPLETE_STEP);
                } else {
//                    stepperView.completeStep(false);
                    stepperView.animateChangingStatus(VerticalStepperView.ANIMATE_COMPLETE_STEP);
                }
            }
        });

        final RelativeLayout rlRoot = (RelativeLayout) findViewById(R.id.rl_root);

        findViewById(R.id.toggle_all).setOnClickListener(new View.OnClickListener() {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_demo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
