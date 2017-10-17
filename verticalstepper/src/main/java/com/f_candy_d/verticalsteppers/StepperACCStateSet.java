package com.f_candy_d.verticalsteppers;

/**
 * Created by daichi on 10/14/17.
 *
 * 'StepperACCStateSet' means 'Stepper And Collapsed Content view State Set'.
 */

public class StepperACCStateSet<C> extends StepperStateSet {

    private C mCollapsedContentState;

    public C getCollapsedContentState() {
        return mCollapsedContentState;
    }

    public void setCollapsedContentState(C collapsedContentState) {
        mCollapsedContentState = collapsedContentState;
    }
}
