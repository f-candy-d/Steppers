package com.f_candy_d.verticalsteppers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daichi on 10/20/17.
 */

public class StepManager implements Constants {

    private VerticalStepperListView mStepperListView;
    private VerticalStepperAdapter mStepperAdapter;
    private List<Pair<Step, StepViewStatus>> mStepInfoList;
    // Non-null
    private StepperBehavior mStepperBehavior;

    public StepManager() {
        mStepInfoList = new ArrayList<>();
        setStepperBehavior(new DefaultStepperBehavior());
    }

    private boolean mIsBuildAlreadyFinished = false;

    /**
     * Call this method to setup vertical-steppers.
     * Steps must be added to StepManager before call this method.
     */
    public void build(Context context, VerticalStepperListView stepperListView) {
        if (mIsBuildAlreadyFinished) return;

        for (Pair<Step, StepViewStatus> stepInfo : mStepInfoList) {
            stepInfo.first.setParentManager(this);
        }

        mStepperListView = stepperListView;
        mStepperAdapter = new VerticalStepperAdapter(this);
        mStepperListView.setLayoutManager(new LinearLayoutManager(context));
        mStepperListView.setAdapter(mStepperAdapter);

        int initialPos = mStepperBehavior.getInitialPosition();
        if (initialPos != INVALID_POSITION) {
            setStepFocusedAt(initialPos, true);
            onStepStatusChanged(initialPos);
        }

        mIsBuildAlreadyFinished = true;
    }

    /**
     * GETTER & SETTER
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

    public void setStepperBehavior(@NonNull StepperBehavior stepperBehavior) {
        mStepperBehavior = stepperBehavior;
        mStepperBehavior.setParentManager(this);
    }

    /**
     * ADD STEP
     * ---------- */

    public void addStep(@NonNull Step step) {
        addStep(mStepInfoList.size(), step);
    }

    public void addStep(int position, @NonNull Step step) {
        if (isBuildAlreadyFinished()) {
            throw new IllegalStateException(
                    "Steps must be added to StepManager before call #build() method");
        }
        mStepInfoList.add(position, new Pair<>(step, new StepViewStatus()));
    }

    /**
     * MANAGE STEP'S STATE
     * ---------- */

    public void setStepFocusedAt(int position, boolean isStepFocused) {
        getStepAt(position).setFocused(isStepFocused);
    }

    public void setStepCompletedAt(int position, boolean isStepCompleted) {
        getStepAt(position).setCompleted(isStepCompleted);
    }

    /**
     * Call this method when status of a step change to apply updates to step view.
     */
    public void notifyStepStatusChanged(Step step) {
        onStepStatusChanged(step);
    }

    public void notifyManyStepsStatusChanged(int... positions) {
        onManyStepsStatusChanged(positions);
    }

    private void onStepStatusChanged(Step step) {
        int position = getStepPositionForUid(step.getUid());
        if (position != INVALID_POSITION) {
            onManyStepsStatusChanged(position);
        }
    }

    private void onStepStatusChanged(int position) {
        onManyStepsStatusChanged(position);
    }

    private void onManyStepsStatusChanged(int... positions) {
        mStepperListView.beginPartialItemTransition();
        for (int position : positions) {
            invalidateStepViewStatus(position);
            mStepperAdapter.onUpdateStepViewStatus(position);
        }
    }

    public void moveToNextStep(Step fromStep) {
        int fromPos = getStepPositionForUid(fromStep.getUid());
        int nextPos = mStepperBehavior.getNextStepPosition(fromPos);
        if (nextPos < 0 || getStepCount() <= nextPos) {
            throw new IndexOutOfBoundsException("Invalid position");
        }

        if (fromPos == nextPos) return;

        setStepFocusedAt(fromPos, false);
        setStepFocusedAt(nextPos, true);
        onManyStepsStatusChanged(fromPos, nextPos);
    }

    public void moveToPreviousStep(Step fromStep) {
        int fromPos = getStepPositionForUid(fromStep.getUid());
        int prevPos = mStepperBehavior.getPreviousStepPosition(fromPos);
        if (prevPos < 0 || getStepCount() <= prevPos) {
            throw new IndexOutOfBoundsException("Invalid position");
        }

        if (fromPos == prevPos) return;

        setStepFocusedAt(prevPos, true);
        setStepFocusedAt(fromPos, false);
        onManyStepsStatusChanged(fromPos, prevPos);
    }

    /**
     * UTILS
     * ---------- */

    public Step getStepAt(int position) {
        return mStepInfoList.get(position).first;
    }

    @Nullable
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
        for (Pair<Step, StepViewStatus> stepInfo : mStepInfoList) {
            if (stepInfo.first.getUid() == uid) {
                return position;
            }
            ++position;
        }

        return INVALID_POSITION;
    }

    public int getStepCount() {
        return mStepInfoList.size();
    }

    /* Intentional package-private */
    StepViewStatus getStepViewStatusAt(int position) {
        return mStepInfoList.get(position).second;
    }

    @Nullable
    private StepViewStatus getStepViewStatusForUid(int uid) {
        for (Pair<Step, StepViewStatus> stepInfo : mStepInfoList) {
            if (stepInfo.first.getUid() == uid) {
                return stepInfo.second;
            }
        }

        return null;
    }

    private void invalidateStepViewStatus(int position) {
        Step step = getStepAt(position);
        StepViewStatus newStatus;

        if (step.isFocused()) {
            newStatus = (step.isCompleted())
                    ? mStepperBehavior.getFocusedAndCompletedStepViewStatus()
                    : mStepperBehavior.getFocusedAndNotCompletedStepViewStatus();
        } else {
            newStatus = (step.isCompleted())
                    ? mStepperBehavior.getNotFocusedAndCompletedStepViewStatus()
                    : mStepperBehavior.getNotFocusedAndNotCompletedStepViewStatus();
        }

        StepViewStatus currentStatus = getStepViewStatusAt(position);
        if (currentStatus != null) {
            currentStatus.set(newStatus);
        }
    }
}
