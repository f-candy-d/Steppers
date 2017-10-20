package com.f_candy_d.verticalsteppers;

/**
 * Created by daichi on 10/20/17.
 */

/* Intentional package-private */
interface StepStateObserver {
    void onStepExpandedStateChanged(Step step);
    void onStepActivatedStateChanged(Step step);
    void onStepCheckedStateChanged(Step step);
    void onStepStatusChanged(
            Step step,
            boolean isExpandStateChanged,
            boolean isActiveStateChanged,
            boolean isCheckedStateChanged);
}
