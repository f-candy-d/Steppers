package com.f_candy_d.verticalsteppers;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by daichi on 10/12/17.
 */

abstract public class VerticalStepperManager {

    private StepperStatusObserver mStepperStatusObserver;

    public VerticalStepperManager() {}

    void setStepperStatusObserver(StepperStatusObserver stepperStatusObserver) {
        mStepperStatusObserver = stepperStatusObserver;
    }

    abstract protected View onCreateExpandedContentView(ViewGroup parent);
    abstract protected View onCreateCollapsedContentView(ViewGroup parent);
    abstract protected String getStepperTitle(int position);
    abstract protected String getStepperSubTitle(int position);
    abstract protected void onBindExpandedContentView(@NonNull View view, int position);
    abstract protected void onBindCollapsedContentView(@NonNull View view, int position);

    public void notifyStepDataInserted(int position, VerticalStepperStatus status) {
        if (mStepperStatusObserver != null) {
            mStepperStatusObserver.onInsertStep(position, status);
        }
    }

    public void notifyStepDataInserted(int startPosition, List<VerticalStepperStatus> statuses) {
        if (mStepperStatusObserver != null) {
            mStepperStatusObserver.onInsertSteps(startPosition, statuses);
        }
    }

    public void notifyStepDataRemoved(int position) {
        if (mStepperStatusObserver != null) {
            mStepperStatusObserver.onRemoveStep(position);
        }
    }

    public void notifyStepDataRangeRemoved(int startPosition, int count) {
        if (mStepperStatusObserver != null) {
            mStepperStatusObserver.onRemoveStepsInRange(startPosition, count);
        }
    }

    public void notifyStepDataMoved(int fromPosition, int toPosition) {
        if (mStepperStatusObserver != null) {
            mStepperStatusObserver.onMoveStep(fromPosition, toPosition);
        }
    }

    public void notifyCompleteStep(int position) {
        if (mStepperStatusObserver != null) {
            mStepperStatusObserver.onCompleteStep(position);
        }
    }

    public void notifyIncompleteStep(int position) {
        if (mStepperStatusObserver != null) {
            mStepperStatusObserver.onIncompleteStep(position);
        }
    }

    public void notifyExpandStepContents(int position) {
        if (mStepperStatusObserver != null) {
            mStepperStatusObserver.onExpandStepContents(position);
        }
    }

    public void notifyCollapseStepContents(int position) {
        if (mStepperStatusObserver != null) {
            mStepperStatusObserver.onCollapseStepContents(position);
        }
    }

    public void notifyActivateStep(int position) {
        if (mStepperStatusObserver != null) {
            mStepperStatusObserver.onActivateStep(position);
        }
    }

    public void notifyInactivateStep(int position) {
        if (mStepperStatusObserver != null) {
            mStepperStatusObserver.onInactivateStep(position);
        }
    }
}
