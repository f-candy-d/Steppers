package com.f_candy_d.verticalsteppers;

import android.support.annotation.NonNull;

/**
 * Created by daichi on 10/21/17.
 */

abstract public class StepperBehavior implements Constants {

    private StepManager mParentManager;

    /* Intentional package-private */
    void setParentManager(StepManager parentManager) {
        mParentManager = parentManager;
    }

    public int getStepCount() {
        if (mParentManager == null) {
            throw new NullPointerException("Attach the StepperBehavior to a StepManager");
        }
        return mParentManager.getStepCount();
    }

    @NonNull abstract public StepViewStatus getFocusedAndCompletedStepViewStatus();
    @NonNull abstract public StepViewStatus getNotFocusedAndCompletedStepViewStatus();
    @NonNull abstract public StepViewStatus getFocusedAndNotCompletedStepViewStatus();
    @NonNull abstract public StepViewStatus getNotFocusedAndNotCompletedStepViewStatus();
    abstract public int getNextStepPosition(int fromPosition);
    abstract public int getPreviousStepPosition(int fromPosition);

    public int getInitialPosition() {
        return INVALID_POSITION;
    }
}
