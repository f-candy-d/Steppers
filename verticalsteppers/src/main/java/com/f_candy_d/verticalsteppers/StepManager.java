package com.f_candy_d.verticalsteppers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daichi on 10/20/17.
 */

public class StepManager implements StepStateObserver {

    public static final int INVALID_POSITION = RecyclerView.NO_POSITION;

    private VerticalStepperListView mStepperListView;
    private VerticalStepperAdapter mStepperAdapter;
    private List<Step> mSteps;

    public StepManager() {
        mSteps = new ArrayList<>();
    }

    private boolean mIsBuildAlreadyFinished = false;

    /**
     * Call this method to setup vertical-steppers.
     * Steps must be added to StepManager before call this method.
     */
    public void build(Context context, VerticalStepperListView stepperListView) {
        if (mIsBuildAlreadyFinished) return;

        for (Step step : mSteps) {
            step.setStepStateObserver(this);
        }

        mStepperListView = stepperListView;
        mStepperAdapter = new VerticalStepperAdapter(this);
        mStepperListView.setLayoutManager(new LinearLayoutManager(context));
        mStepperListView.setAdapter(mStepperAdapter);

        mIsBuildAlreadyFinished = true;
    }

    /**
     * GETTER
     * ---------- */

    public VerticalStepperListView getStepperListView() {
        return mStepperListView;
    }

    public VerticalStepperAdapter getStepperAdapter() {
        return mStepperAdapter;
    }

    public boolean isBuildAlreadyFinished() {
        return mIsBuildAlreadyFinished;
    }

    /**
     * ADD STEP
     * ---------- */

    public void addStep(@NonNull Step step) {
        addStep(mSteps.size(), step);
    }

    public void addStep(int position, @NonNull Step step) {
        if (isBuildAlreadyFinished()) {
            throw new IllegalStateException(
                    "Steps must be added to StepManager before call #build() method");
        }
        mSteps.add(position, step);
    }

    /**
     * MANAGE STEP'S STATE
     * ---------- */

    public void setStepExpandedAt(int position, boolean isStepExpanded) {
        Step step = mSteps.get(position);
        if (step.isStepExpanded() != isStepExpanded) {
            step.setStepExpanded(isStepExpanded);
            if (isBuildAlreadyFinished()) {
                onStepExpandedStateChanged(step);
            }
        }
    }

    public void setStepActivatedAt(int position, boolean isStepActivated) {
        Step step = mSteps.get(position);
        if (step.isStepActivated() != isStepActivated) {
            step.setStepActivated(isStepActivated);
            if (isBuildAlreadyFinished()) {
                onStepActivatedStateChanged(step);
            }
        }
    }

    public void setStepCheckedAt(int position, boolean isStepChecked) {
        Step step = mSteps.get(position);
        if (step.isStepChecked() != isStepChecked) {
            step.setStepChecked(isStepChecked);
            if (isBuildAlreadyFinished()) {
                onStepCheckedStateChanged(step);
            }
        }
    }

    public void setStepStatusAt(
            int position,
            boolean isStepExpanded,
            boolean isStepActivated,
            boolean isStepChecked) {

        Step step = mSteps.get(position);
        boolean isExpandedStateChanged = step.isStepExpanded() != isStepExpanded;
        boolean isActivatedStateChanged = step.isStepActivated() != isStepActivated;
        boolean isCheckedStateChanged = step.isStepChecked() != isStepChecked;
        step.setStepStatus(isStepExpanded, isStepActivated, isStepChecked);

        if (isBuildAlreadyFinished()) {
            onStepStatusChanged(step,
                    isExpandedStateChanged,
                    isActivatedStateChanged,
                    isCheckedStateChanged);
        }
    }

    /**
     * UTILS
     * ---------- */

    public Step getStepAt(int position) {
        return mSteps.get(position);
    }

    public Step getStepForUid(int uid) {
        int position = getStepPositionForUid(uid);
        if (position != INVALID_POSITION) {
            return getStepAt(position);
        } else {
            return null;
        }
    }

    public int getStepPositionForUid(int uid) {
        int position = 0;
        for (Step step : mSteps) {
            if (step.getUid() == uid) {
                return position;
            }
            ++position;
        }

        return INVALID_POSITION;
    }

    public int getStepCount() {
        return mSteps.size();
    }

    /**
     * INTERFACE IMPLEMENTATION -> STEP_STATE_OBSERVER
     * ---------- */

    @Override
    public void onStepExpandedStateChanged(Step step) {
        int position = getStepPositionForUid(step.getUid());
        if (position != INVALID_POSITION) {
            mStepperListView.beginPartialItemTransition();
            mStepperAdapter.onUpdateStepExpandedState(position);
        }
    }

    @Override
    public void onStepActivatedStateChanged(Step step) {
        int position = getStepPositionForUid(step.getUid());
        if (position != INVALID_POSITION) {
            mStepperListView.beginPartialItemTransition();
            mStepperAdapter.onUpdateStepActivatedState(position);
        }
    }

    @Override
    public void onStepCheckedStateChanged(Step step) {
        int position = getStepPositionForUid(step.getUid());
        if (position != INVALID_POSITION) {
            mStepperListView.beginPartialItemTransition();
            mStepperAdapter.onUpdateStepCheckedState(position);
        }
    }

    @Override
    public void onStepStatusChanged(Step step,
                                    boolean isExpandStateChanged,
                                    boolean isActiveStateChanged,
                                    boolean isCheckedStateChanged) {

        int position = getStepPositionForUid(step.getUid());
        if (position != INVALID_POSITION) {
            mStepperListView.beginPartialItemTransition();
            mStepperAdapter.onUpdateStepStatus(position,
                    isExpandStateChanged,
                    isActiveStateChanged,
                    isCheckedStateChanged);
        }
    }
}
