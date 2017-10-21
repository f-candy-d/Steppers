package com.f_candy_d.verticalsteppers;

/**
 * Created by daichi on 10/21/17.
 */

public class StepViewStatus {

    private boolean mPrevIsExpanded;
    private boolean mPrevIsActivated;
    private boolean mPrevIsChecked;
    private boolean mIsExpanded;
    private boolean mIsActivated;
    private boolean mIsChecked;

    public StepViewStatus() {
        mIsExpanded = mPrevIsExpanded = false;
        mIsActivated = mPrevIsActivated = false;
        mIsChecked = mPrevIsChecked = false;
    }

    public boolean isDirty() {
        return isExpandedStateChanged() ||
                isCheckedStateChanged() ||
                isActivatedStateChanged();
    }

    public void refresh() {
        mPrevIsExpanded = mIsExpanded;
        mPrevIsActivated = mIsActivated;
        mPrevIsChecked = mIsChecked;
    }

    public void set(StepViewStatus source) {
        setExpanded(source.isExpanded());
        setActivated(source.isActivated());
        setChecked(source.isChecked());
    }

    public boolean isExpanded() {
        return mIsExpanded;
    }

    public void setExpanded(boolean expanded) {
        mPrevIsExpanded = mIsExpanded;
        mIsExpanded = expanded;
    }

    public boolean isActivated() {
        return mIsActivated;
    }

    public void setActivated(boolean activated) {
        mPrevIsActivated = mIsActivated;
        mIsActivated = activated;
    }

    public boolean isChecked() {
        return mIsChecked;
    }

    public void setChecked(boolean checked) {
        mPrevIsChecked = mIsChecked;
        mIsChecked = checked;
    }

    public boolean isExpandedStateChanged() {
        return mPrevIsExpanded != mIsExpanded;
    }

    public boolean isActivatedStateChanged() {
        return mPrevIsActivated != mIsActivated;
    }

    public boolean isCheckedStateChanged() {
        return mPrevIsChecked != mIsChecked;
    }
}
