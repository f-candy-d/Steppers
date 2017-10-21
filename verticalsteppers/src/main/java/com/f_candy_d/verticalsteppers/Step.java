package com.f_candy_d.verticalsteppers;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by daichi on 10/19/17.
 */

abstract public class Step {

    private final int mUid;
    private StepManager mParentManager;
    private boolean mIsFocused;
    private boolean mIsCompleted;

    public Step(int uid) {
        this(uid, false, false);
    }

    public Step(int uid, boolean isFocused, boolean isCompleted) {
        mUid = uid;
        mIsFocused = isFocused;
        mIsCompleted = isCompleted;
    }

    /**
     * GETTER & SETTER
     * ---------- */

    /* Intentional package-private */
    void setParentManager(StepManager parentManager) {
        mParentManager = parentManager;
    }

    public int getUid() {
        return mUid;
    }

    /**
     * STEP DATA
     * ---------- */

    abstract public String getTitle();
    abstract public String getSubTitle();
    abstract public int getNumberLabel();

    /**
     * Override this method and return TRUE if you want to
     * use text as a stepper's circular label instead of number.
     */
    protected boolean useTextLabel() { return false; }

    /**
     * Override this method if you override {@link Step#useTextLabel()}
     * method and return TRUE in it.
     */
    public String getTextLabel() { return null; }

    /**
     * STEP STATE
     * ---------- */

    public boolean isFocused() {
        return mIsFocused;
    }

    public void setFocused(boolean focused) {
        mIsFocused = focused;
    }

    public boolean isCompleted() {
        return mIsCompleted;
    }

    public void setCompleted(boolean completed) {
        mIsCompleted = completed;
    }

    public void toggleFocusedState() {
        setFocused(!isFocused());
    }

    public void toggleCompletedState() {
        setCompleted(!isCompleted());
    }

    public void toggleAllStatus() {
        toggleFocusedState();
        toggleCompletedState();
    }

    public void notifyStepStatusChanged() {
        if (mParentManager != null) {
            mParentManager.notifyStepStatusChanged(this);
        }
    }

    /**
     * EXPANDED (COLLAPSED) CONTENT VIEW
     * ---------- */

    abstract public static class ContentViewHolder {

        public View contentRoot;

        public ContentViewHolder(View view) {
            if (view == null) {
                throw new NullPointerException(
                        "The first parameter can not be a null");
            }
            contentRoot = view;
        }
    }

    /**
     * Do not attach a inflated view to 'parent' in this method.
     */
    protected ContentViewHolder onCreateExpandedContentViewHolder(ViewGroup parent) {
        return null;
    }

    /**
     * Do not attach a inflated view to 'parent' in this method.
     */
    protected ContentViewHolder onCreateCollapsedContentViewHolder(ViewGroup parent) {
        return null;
    }

    /**
     * TOUCH EVENT
     * ---------- */

    /**
     * Override this method if you want to handle a stepper's click event.
     */
    protected void onStepClick() {}

    /**
     * Override this method if you want to handle a stepper's long click event.
     *
     * @return {@link Step#onStepClick()} will be not called if return TRUE.
     */
    protected boolean onStepLongClick() { return false; }

    /**
     * ACTIVITY
     * ---------- */

    /**
     * Move to the next step from this step.
     * Which is the next step depends on {@link StepperBehavior#getNextStepPosition(int)} method.
     */
    public void moveToNextStep() {
        if (mParentManager != null) {
            mParentManager.moveToNextStep(this);
        }
    }

    /**
     * Move to the previous step from this step.
     * Which is the previous step depends on {@link StepperBehavior#getPreviousStepPosition(int)} method.
     */
    public void moveToPreviousStep() {
        if (mParentManager != null) {
            mParentManager.moveToPreviousStep(this);
        }
    }
}
