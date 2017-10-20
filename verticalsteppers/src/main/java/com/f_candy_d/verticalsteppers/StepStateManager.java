package com.f_candy_d.verticalsteppers;

/**
 * Created by daichi on 10/20/17.
 */

/* Intentional package-private */
interface StepStateManager {
    void onChangeStepExpandedState(Step step);
    void onChangeStepActivatedState(Step step);
    void onChangeStepCheckedState(Step step);
    void onChangeStepStatus(
            Step step,
            boolean isExpandStateChanged,
            boolean isActiveStateChanged,
            boolean isCheckedStateChanged);
}
