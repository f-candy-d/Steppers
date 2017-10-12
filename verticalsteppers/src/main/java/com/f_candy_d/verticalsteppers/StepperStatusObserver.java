package com.f_candy_d.verticalsteppers;

import java.util.List;

/**
 * Created by daichi on 10/12/17.
 */

interface StepperStatusObserver {
    void onInsertStep(int position, VerticalStepperStatus status);
    void onInsertSteps(int startPosition, List<VerticalStepperStatus> statuses);
    void onRemoveStep(int position);
    void onRemoveStepsInRange(int startPosition, int count);
    void onMoveStep(int fromPosition, int toPosition);
    void onCompleteStep(int position);
    void onIncompleteStep(int position);
    void onExpandStepContents(int position);
    void onCollapseStepContents(int position);
    void onActivateStep(int position);
    void onInactivateStep(int position);
}
