package com.f_candy_d.verticalsteppers;

import android.support.annotation.NonNull;

/**
 * Created by daichi on 10/21/17.
 */

public class DefaultStepperBehavior extends StepperBehavior {

    private static final StepViewStatus FOCUSED_AND_COMPLETED;
    private static final StepViewStatus NOT_FOCUSED_AND_COMPLETED;
    private static final StepViewStatus FOCUSED_AND_NOT_COMPLETED;
    private static final StepViewStatus NOT_FOCUSED_AND_NOT_COMPLETED;

    static {
        FOCUSED_AND_COMPLETED = new StepViewStatus();
        FOCUSED_AND_COMPLETED.setExpanded(true);
        FOCUSED_AND_COMPLETED.setActivated(true);
        FOCUSED_AND_COMPLETED.setChecked(true);

        NOT_FOCUSED_AND_COMPLETED = new StepViewStatus();
        NOT_FOCUSED_AND_COMPLETED.setExpanded(false);
        NOT_FOCUSED_AND_COMPLETED.setActivated(true);
        NOT_FOCUSED_AND_COMPLETED.setChecked(true);

        FOCUSED_AND_NOT_COMPLETED = new StepViewStatus();
        FOCUSED_AND_NOT_COMPLETED.setExpanded(true);
        FOCUSED_AND_NOT_COMPLETED.setActivated(true);
        FOCUSED_AND_NOT_COMPLETED.setChecked(false);

        NOT_FOCUSED_AND_NOT_COMPLETED = new StepViewStatus();
        NOT_FOCUSED_AND_NOT_COMPLETED.setExpanded(false);
        NOT_FOCUSED_AND_NOT_COMPLETED.setActivated(false);
        NOT_FOCUSED_AND_NOT_COMPLETED.setChecked(false);
    }


    @NonNull
    @Override
    public StepViewStatus getFocusedAndCompletedStepViewStatus() {
        return FOCUSED_AND_COMPLETED;
    }

    @NonNull
    @Override
    public StepViewStatus getNotFocusedAndCompletedStepViewStatus() {
        return NOT_FOCUSED_AND_COMPLETED;
    }

    @NonNull
    @Override
    public StepViewStatus getFocusedAndNotCompletedStepViewStatus() {
        return FOCUSED_AND_NOT_COMPLETED;
    }

    @NonNull
    @Override
    public StepViewStatus getNotFocusedAndNotCompletedStepViewStatus() {
        return NOT_FOCUSED_AND_NOT_COMPLETED;
    }

    @Override
    public int getNextStepPosition(int fromPosition) {
        if (fromPosition + 1 < getStepCount()) {
            return fromPosition + 1;
        } else {
            return fromPosition;
        }
    }

    @Override
    public int getPreviousStepPosition(int fromPosition) {
        if (0 <= fromPosition - 1) {
            return fromPosition - 1;
        } else {
            return fromPosition;
        }
    }

    @Override
    public int getInitialPosition() {
        return 0;
    }
}
