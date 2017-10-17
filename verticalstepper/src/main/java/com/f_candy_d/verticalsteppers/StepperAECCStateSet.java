package com.f_candy_d.verticalsteppers;

/**
 * Created by daichi on 10/14/17.
 *
 * 'StepperAECCStateSet' means 'Stepper And Expanded Content and Collapsed content State Set'.
 */

public class StepperAECCStateSet<C, E> extends StepperACCStateSet<C> {

    private E mExpandedContentState;

    public E getExpandedContentState() {
        return mExpandedContentState;
    }

    public void setExpandedContentState(E expandedContentState) {
        mExpandedContentState = expandedContentState;
    }
}
