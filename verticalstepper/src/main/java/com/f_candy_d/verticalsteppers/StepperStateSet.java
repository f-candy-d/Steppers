package com.f_candy_d.verticalsteppers;

/**
 * Created by daichi on 10/14/17.
 */

public class StepperStateSet {

    private boolean mIsContentViewExpanded;
    private boolean mIsStepActive;
    private boolean mIsStepCompleted;

    public boolean isContentViewExpanded() {
        return mIsContentViewExpanded;
    }

    public boolean isStepActive() {
        return mIsStepActive;
    }

    public boolean isStepCompleted() {
        return mIsStepCompleted;
    }

    public StepperStateSet contentViewIsExpanded() {
        mIsContentViewExpanded = true;
        return this;
    }

    public StepperStateSet contentViewIsCollapsed() {
        mIsContentViewExpanded = false;
        return this;
    }

    public StepperStateSet stepIsActive() {
        mIsStepActive = true;
        return this;
    }

    public StepperStateSet stepIsInactive() {
        mIsStepActive = false;
        return this;
    }

    public StepperStateSet stepIsCompleted() {
        mIsStepCompleted = true;
        return this;
    }

    public StepperStateSet stepIsIncompleted() {
        mIsStepCompleted = false;
        return this;
    }

    @VerticalStepperView.ChangingStatusAnimationFlag
    public int toAnimationFlags() {
        @VerticalStepperView.ChangingStatusAnimationFlag int flags = 0;
        flags |= (mIsContentViewExpanded) ? VerticalStepperView.ANIMATE_EXPAND_CONTENTS : VerticalStepperView.ANIMATE_COLLAPSE_CONTENTS;
        flags |= (mIsStepActive) ? VerticalStepperView.ANIMATE_ACTIVATE_STEP : VerticalStepperView.ANIMATE_INACTIVATE_STEP;
        flags |= (mIsStepCompleted) ? VerticalStepperView.ANIMATE_COMPLETE_STEP : VerticalStepperView.ANIMATE_INCOMPLETE_STEP;
        return flags;
    }
}