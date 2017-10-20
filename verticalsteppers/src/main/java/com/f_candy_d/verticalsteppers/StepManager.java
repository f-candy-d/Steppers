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
     * ADD STEP
     * ---------- */

    public void addStep(@NonNull Step step) {
        addStep(mSteps.size(), step);
    }

    public void addStep(int position, @NonNull Step step) {
        if (mIsBuildAlreadyFinished) {
            throw new IllegalStateException(
                    "Steps must be added to StepManager before call #build() method");
        }
        mSteps.add(position, step);
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
    public void onChangeStepExpandedState(Step step) {
        int position = getStepPositionForUid(step.getUid());
        if (position != INVALID_POSITION) {
            mStepperListView.beginPartialItemTransition();
            mStepperAdapter.onUpdateStepExpandedState(position);
        }
    }

    @Override
    public void onChangeStepActivatedState(Step step) {
        int position = getStepPositionForUid(step.getUid());
        if (position != INVALID_POSITION) {
            mStepperListView.beginPartialItemTransition();
            mStepperAdapter.onUpdateStepActivatedState(position);
        }
    }

    @Override
    public void onChangeStepCheckedState(Step step) {
        int position = getStepPositionForUid(step.getUid());
        if (position != INVALID_POSITION) {
            mStepperListView.beginPartialItemTransition();
            mStepperAdapter.onUpdateStepCheckedState(position);
        }
    }

    @Override
    public void onChangeStepStatus(Step step,
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
