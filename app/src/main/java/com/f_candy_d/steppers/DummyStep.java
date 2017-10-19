package com.f_candy_d.steppers;

import android.util.Log;
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
    protected View onCreateExpandedContentView(ViewGroup parent) {
        return null;
    }

    @Override
    protected View onCreateCollapsedContentView(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vertical_stepper_content, parent, false);
        Log.d("mylog", "onCreateCollapsedContentView / view's parent -> " + view.getParent());
        return view;
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
        setStepActivated(!isStepActivated());
    }
}
