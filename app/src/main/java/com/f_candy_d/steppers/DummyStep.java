package com.f_candy_d.steppers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.f_candy_d.verticalsteppers.Step;

/**
 * Created by daichi on 10/20/17.
 */

public class DummyStep extends Step {

    private final int mOrder;

    public DummyStep(int uid, int order) {
        super(uid);
        mOrder = order;
    }

    @Override
    public int getOrder() {
        return mOrder;
    }

    @Override
    public String getTitle() {
        return "Step title at " + mOrder;
    }

    @Override
    public String getSubTitle() {
        return "Step sub-title at " + mOrder;
    }

    @Override
    public int getNumberLabel() {
        return mOrder + 1;
    }

    @Override
    protected void onStepClick() {
        toggleAllStepStatus();
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
