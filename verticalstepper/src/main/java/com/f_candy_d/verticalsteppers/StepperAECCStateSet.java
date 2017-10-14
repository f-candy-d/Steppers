package com.f_candy_d.verticalsteppers;

/**
 * Created by daichi on 10/14/17.
 *
 * 'StepperAECCStateSet' means 'Stepper And Expanded Content and Collapsed content State Set'.
 */

public class StepperAECCStateSet<T, T2> extends StepperACCStateSet<T> {

    private T2 mExpandedContentState;

    public T2 getExpandedContentState() {
        return mExpandedContentState;
    }

    public void setExpandedContentState(T2 expandedContentState) {
        mExpandedContentState = expandedContentState;
    }
}
