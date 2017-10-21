package com.f_candy_d.steppers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.f_candy_d.verticalsteppers.Step;
import com.f_candy_d.verticalsteppers.StepViewStatus;

/**
 * Created by daichi on 10/20/17.
 */

public class DemoStep extends Step {

    public DemoStep(int uid) {
        super(uid);
    }

    @Override
    public String getTitle() {
        return "Step title at " + getUid();
    }

    @Override
    public String getSubTitle() {
        return "Step sub-title at " + getUid();
    }

    @Override
    public int getNumberLabel() {
        return getUid() + 1;
    }

    @Override
    protected ContentViewHolder onCreateExpandedContentViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vertical_stepper_content, parent, false);
        return new ExpandedContentViewHolder(view, this);
    }

    /**
     * EXPANDED CONTENT VIEW HOLDER
     * ---------- */

    private static class ExpandedContentViewHolder extends Step.ContentViewHolder {

        ExpandedContentViewHolder(View view, final Step step) {
            super(view);

            view.findViewById(R.id.continue_btn)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (!step.isLastStep()) {
                                step.getStepStatus().setExpanded(false);
                                step.getStepStatus().setActivated(true);
                                step.getStepStatus().setChecked(true);
                                StepViewStatus next = step.getNextStepStatus();
                                if (next != null) {
                                    next.setExpanded(true);
                                    next.setActivated(true);
                                    next.setChecked(false);
                                }
                                step.moveToNextStep(next);
                            }
                        }
                    });

            view.findViewById(R.id.cancel_btn)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (!step.isFirstStep()) {
                                step.getStepStatus().setExpanded(false);
                                step.getStepStatus().setActivated(false);
                                step.getStepStatus().setChecked(false);
                                StepViewStatus prev = step.getPreviousStepStatus();
                                if (prev != null) {
                                    prev.setExpanded(true);
                                    prev.setActivated(true);
                                    prev.setChecked(false);
                                }
                                step.moveToPreviousStep(prev);
                            }
                        }
                    });
        }
    }
}
