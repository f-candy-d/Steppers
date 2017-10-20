package com.f_candy_d.verticalsteppers;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by daichi on 10/19/17.
 */

abstract public class Step {

    // Uid must be a non-negative integer
    private final int mUid;
    private StepStateObserver mStepStateObserver;

    // Step status
    private boolean mIsStepExpanded;
    private boolean mIsStepActivated;
    private boolean mIsStepChecked;

    public Step(int uid) {
        this(uid, false, false, false);
    }

    public Step(int uid, boolean isStepExpanded, boolean isStepActivated, boolean isStepChecked) {
        if (uid < 0) {
            throw new IllegalArgumentException(
                    "'uid' must be a non-negative integer, but passed uid is " + uid);
        }

        mUid = uid;
        mIsStepExpanded = isStepExpanded;
        mIsStepActivated = isStepActivated;
        mIsStepChecked = isStepChecked;
    }

    /**
     * GETTER & SETTER
     * ---------- */

    /* Intentional package-private */
    void setStepStateObserver(StepStateObserver stepStateObserver) {
        mStepStateObserver = stepStateObserver;
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

    public boolean isStepExpanded() {
        return mIsStepExpanded;
    }

    public boolean isStepActivated() {
        return mIsStepActivated;
    }

    public boolean isStepChecked() {
        return mIsStepChecked;
    }

    public void setStepExpanded(boolean isStepExpanded) {
        mIsStepExpanded = isStepExpanded;
    }

    public void setStepActivated(boolean isStepActivated) {
        mIsStepActivated = isStepActivated;
    }

    public void setStepChecked(boolean isStepChecked) {
        mIsStepChecked = isStepChecked;
    }

    public void setStepStatus(
            boolean isStepExpanded,
            boolean isStepActivated,
            boolean isStepChecked) {

        setStepExpanded(isStepExpanded);
        setStepActivated(isStepActivated);
        setStepChecked(isStepChecked);
    }

    public void toggleAllStepStatus() {
        setStepStatus(!isStepExpanded(), !isStepActivated(), !isStepChecked());
    }

    public void toggleIsStepExpanded() {
        setStepExpanded(!isStepExpanded());
    }

    public void toggleIsStepActivated() {
        setStepActivated(!isStepActivated());
    }

    public void toggleIsStepChecked() {
        setStepChecked(!isStepChecked());
    }

    /**
     * NOTIFY CHANGING STATE TO OBSERVER
     *
     * Call the following methods to update a certain stepper view.
     * ---------- */

    public void notifyStepExpandedStateChanged() {
        if (mStepStateObserver != null) {
            mStepStateObserver.onStepExpandedStateChanged(this);
        }
    }

    public void notifyStepActivatedStateChanged() {
        if (mStepStateObserver != null) {
            mStepStateObserver.onStepActivatedStateChanged(this);
        }
    }

    public void notifyStepCheckedStateChanged() {
        if (mStepStateObserver != null) {
            mStepStateObserver.onStepCheckedStateChanged(this);
        }
    }

    public void notifyStepStatusChanged(
            boolean isExpandStateChanged,
            boolean isActiveStateChanged,
            boolean isCheckedStateChanged) {

        if (mStepStateObserver != null) {
            mStepStateObserver.onStepStatusChanged(this,
                    isExpandStateChanged,
                    isActiveStateChanged,
                    isCheckedStateChanged);
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
}
