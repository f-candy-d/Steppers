package com.f_candy_d.verticalsteppers;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by daichi on 10/12/17.
 */

abstract public class VerticalStepperManager {

    private VerticalStepperManagerCallback mStepperStatusObserver;
    private boolean mUseTextInStepperCircle = false;

    public VerticalStepperManager() {}

    void setCallback(VerticalStepperManagerCallback stepperStatusObserver) {
        mStepperStatusObserver = stepperStatusObserver;
    }

    boolean isUseTextInStepperCircle() {
        return mUseTextInStepperCircle;
    }

    public void useTextAsStepLabel(boolean useTextInStepperCircle) {
        mUseTextInStepperCircle = useTextInStepperCircle;
    }

    abstract protected View onCreateExpandedContentView(ViewGroup parent);
    abstract protected View onCreateCollapsedContentView(ViewGroup parent);
    abstract protected String getStepperTitle(int position);
    abstract protected String getStepperSubTitle(int position);
    abstract protected int getStepLabelNumber(int position);
    abstract protected String getStepLabelText(int position);
    abstract protected void onBindExpandedContentView(@NonNull View view, int position);
    abstract protected void onBindCollapsedContentView(@NonNull View view, int position);

    /**
     * DELEGATE METHODS
     * ----------------------------------------------------------------------------- */

    public void notifyStepDataInserted(int position, VerticalStepperStatus status) {
        if (mStepperStatusObserver != null) {
            mStepperStatusObserver.onInsertStep(position, status);
        }
    }

    public void notifyStepDataInserted(int startPosition, List<VerticalStepperStatus> statuses) {
        mStepperStatusObserver.onInsertSteps(startPosition, statuses);
    }

    public void notifyStepDataRemoved(int position) {
        mStepperStatusObserver.onRemoveStep(position);
    }

    public void notifyStepDataRangeRemoved(int startPosition, int count) {
        mStepperStatusObserver.onRemoveStepsInRange(startPosition, count);
    }

    public void notifyStepDataMoved(int fromPosition, int toPosition) {
        mStepperStatusObserver.onMoveStep(fromPosition, toPosition);
    }

    public void notifyCompleteStep(int position) {
        mStepperStatusObserver.onCompleteStep(position);
    }

    public void notifyIncompleteStep(int position) {
        mStepperStatusObserver.onIncompleteStep(position);
    }

    public void notifyExpandStepContents(int position) {
        mStepperStatusObserver.onExpandStepContents(position);
    }

    public void notifyCollapseStepContents(int position) {
        mStepperStatusObserver.onCollapseStepContents(position);
    }

    public void notifyActivateStep(int position) {
        mStepperStatusObserver.onActivateStep(position);
    }

    public void notifyInactivateStep(int position) {
        mStepperStatusObserver.onInactivateStep(position);
    }

    public void notifyMoveToNextStep(int position, VerticalStepperStatus afterStatusOfCurrentStep, VerticalStepperStatus afterStatusOfNextStep) {
        mStepperStatusObserver.onMoveToNextStep(position, afterStatusOfCurrentStep, afterStatusOfNextStep);
    }

    public void notifyChangeStepStatusesInRange(int position, List<VerticalStepperStatus> statuses) {
        mStepperStatusObserver.onChangeStepStatusesInRange(position, statuses);
    }

    public VerticalStepperStatus getStepStatusAt(int position) {
        return mStepperStatusObserver.getStepStatusOf(position);
    }

    public int getStepCount() {
        return mStepperStatusObserver.getStepCount();
    }
}
