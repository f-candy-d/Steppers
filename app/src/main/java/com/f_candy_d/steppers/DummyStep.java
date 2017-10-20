package com.f_candy_d.steppers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.f_candy_d.verticalsteppers.Step;

/**
 * Created by daichi on 10/20/17.
 */

public class DummyStep extends Step {

    public DummyStep(int uid) {
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
    protected void onStepClick() {
        toggleAllStepStatus();
        notifyStepStatusChanged(true, true, true);
    }

    @Override
    protected ContentViewHolder onCreateCollapsedContentViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vertical_stepper_collapsed_content, parent, false);
        return new CollapsedContentViewHolder(view);
    }

    @Override
    protected ContentViewHolder onCreateExpandedContentViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vertical_stepper_expanded_content, parent, false);
        return new ExpandedContentViewHolder(view);
    }

    /**
     * COLLAPSED CONTENT VIEW HOLDER
     * ---------- */

    static class CollapsedContentViewHolder extends Step.ContentViewHolder {

        CollapsedContentViewHolder(View view) {
            super(view);
        }
    }

    /**
     * EXPANDED CONTENT VIEW HOLDER
     * ---------- */

    static class ExpandedContentViewHolder extends Step.ContentViewHolder {

        ExpandedContentViewHolder(View view) {
            super(view);
        }
    }
}
