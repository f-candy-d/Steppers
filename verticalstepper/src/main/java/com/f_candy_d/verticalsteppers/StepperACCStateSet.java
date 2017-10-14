package com.f_candy_d.verticalsteppers;

/**
 * Created by daichi on 10/14/17.
 *
 * 'StepperACCStateSet' means 'Stepper And Collapsed Content view State Set'.
 */

public class StepperACCStateSet<T> extends StepperStateSet {

    private T mCollapsedContentState;

    public T getCollapsedContentState() {
        return mCollapsedContentState;
    }

    public void setCollapsedContentState(T collapsedContentState) {
        mCollapsedContentState = collapsedContentState;
    }
}
