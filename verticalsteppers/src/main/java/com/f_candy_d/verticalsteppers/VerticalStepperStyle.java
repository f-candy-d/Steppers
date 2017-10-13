package com.f_candy_d.verticalsteppers;

import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Px;
import android.support.annotation.StyleRes;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by daichi on 10/13/17.
 */

public final class VerticalStepperStyle {

    @ColorRes private int mStepConnectorLineColorRes;
    @ColorRes private int mActiveColorRes;
    @ColorRes private int mInactiveColorRes;
    @DrawableRes private int mCompletedIconRes;
    @StyleRes private int mActiveTitleAppearanceRes;
    @StyleRes private int mActiveSubTitleAppearanceRes;
    @StyleRes private int mInactiveTitleAppearanceRes;
    @StyleRes private int mInactiveSubTitleAppearanceRes;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({STEPPER_CIRCLE_TEXT_TYPE_CHAR, STEPPER_CIRCLE_TEXT_TYPE_NUMBER})
    public @interface StepperCircleTextType {}
    public static final int STEPPER_CIRCLE_TEXT_TYPE_CHAR = 0;
    public static final int STEPPER_CIRCLE_TEXT_TYPE_NUMBER = 1;
    @StepperCircleTextType private int mStepperCircleTextType;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({STEPPER_CIRCLE_SIZE_SMALL, STEPPER_CIRCLE_SIZE_REGULAR})
    public @interface StepperCircleSize {}
    public static final int STEPPER_CIRCLE_SIZE_SMALL = 0;
    public static final int STEPPER_CIRCLE_SIZE_REGULAR = 1;
    @StepperCircleSize private int mStepperCircleSize;


    public static VerticalStepperStyle createAsDefaultStyle() {
        VerticalStepperStyle style = new VerticalStepperStyle();
        style.mStepConnectorLineColorRes = R.color.vertical_stepper_connector_line;
        style.mActiveColorRes = R.color.vertical_stepper_active_primary;
        style.mInactiveColorRes = R.color.vertical_stepper_inactive_primary;
        style.mCompletedIconRes = R.drawable.ic_check;
        style.mActiveTitleAppearanceRes = R.style.VerticalStepperActiveTitleAppearance;
        style.mActiveSubTitleAppearanceRes = R.style.VerticalStepperActiveSubTitleAppearance;
        style.mInactiveTitleAppearanceRes = R.style.VerticalStepperInactiveTitleAppearance;
        style.mInactiveSubTitleAppearanceRes = R.style.VerticalStepperInactiveSubTitleAppearance;
        style.mStepperCircleTextType = STEPPER_CIRCLE_TEXT_TYPE_NUMBER;
        style.mStepperCircleSize = STEPPER_CIRCLE_SIZE_REGULAR;

        return style;
    }


    public void copy(@NonNull VerticalStepperStyle source) {
        setStepConnectorLineColorRes(source.getStepConnectorLineColorRes());
        setActiveColorRes(source.getActiveColorRes());
        setInactiveColorRes(source.getInactiveColorRes());
        setCompletedIconRes(source.getCompletedIconRes());
        setActiveTitleAppearanceRes(source.getActiveTitleAppearanceRes());
        setActiveSubTitleAppearanceRes(source.getActiveSubTitleAppearanceRes());
        setInactiveTitleAppearanceRes(source.getInactiveTitleAppearanceRes());
        setInactiveSubTitleAppearanceRes(source.getInactiveSubTitleAppearanceRes());
        setStepperCircleTextType(source.getStepperCircleTextType());
        setStepperCircleSize(source.getStepperCircleSize());
    }

    public void setStepConnectorLineColorRes(int stepConnectorLineColorRes) {
        mStepConnectorLineColorRes = stepConnectorLineColorRes;
    }

    public void setActiveColorRes(@ColorRes int activeColorRes) {
        this.mActiveColorRes = activeColorRes;
    }

    public void setInactiveColorRes(@ColorRes int inactiveColorRes) {
        this.mInactiveColorRes = inactiveColorRes;
    }

    public void setCompletedIconRes(@DrawableRes int completedIconRes) {
        this.mCompletedIconRes = completedIconRes;
    }

    public void setActiveTitleAppearanceRes(int activeTitleAppearanceRes) {
        mActiveTitleAppearanceRes = activeTitleAppearanceRes;
    }

    public void setActiveSubTitleAppearanceRes(int activeSubTitleAppearanceRes) {
        mActiveSubTitleAppearanceRes = activeSubTitleAppearanceRes;
    }

    public void setInactiveTitleAppearanceRes(int inactiveTitleAppearanceRes) {
        mInactiveTitleAppearanceRes = inactiveTitleAppearanceRes;
    }

    public void setInactiveSubTitleAppearanceRes(int inactiveSubTitleAppearanceRes) {
        mInactiveSubTitleAppearanceRes = inactiveSubTitleAppearanceRes;
    }

    public void setStepperCircleSize(@StepperCircleSize int stepperCircleSize) {
        mStepperCircleSize = stepperCircleSize;
    }

    public void setStepperCircleTextType(@StepperCircleTextType int stepperCircleTextType) {
        this.mStepperCircleTextType = stepperCircleTextType;
    }

    public int getStepConnectorLineColorRes() {
        return mStepConnectorLineColorRes;
    }

    public int getActiveColorRes() {
        return mActiveColorRes;
    }

    public int getInactiveColorRes() {
        return mInactiveColorRes;
    }

    public int getCompletedIconRes() {
        return mCompletedIconRes;
    }

    public int getActiveTitleAppearanceRes() {
        return mActiveTitleAppearanceRes;
    }

    public int getActiveSubTitleAppearanceRes() {
        return mActiveSubTitleAppearanceRes;
    }

    public int getInactiveTitleAppearanceRes() {
        return mInactiveTitleAppearanceRes;
    }

    public int getInactiveSubTitleAppearanceRes() {
        return mInactiveSubTitleAppearanceRes;
    }

    @StepperCircleTextType
    public int getStepperCircleTextType() {
        return mStepperCircleTextType;
    }

    @StepperCircleSize
    public int getStepperCircleSize() {
        return mStepperCircleSize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VerticalStepperStyle that = (VerticalStepperStyle) o;

        if (mStepConnectorLineColorRes != that.mStepConnectorLineColorRes) return false;
        if (mActiveColorRes != that.mActiveColorRes) return false;
        if (mInactiveColorRes != that.mInactiveColorRes) return false;
        if (mCompletedIconRes != that.mCompletedIconRes) return false;
        if (mActiveTitleAppearanceRes != that.mActiveTitleAppearanceRes) return false;
        if (mActiveSubTitleAppearanceRes != that.mActiveSubTitleAppearanceRes) return false;
        if (mInactiveTitleAppearanceRes != that.mInactiveTitleAppearanceRes) return false;
        if (mInactiveSubTitleAppearanceRes != that.mInactiveSubTitleAppearanceRes) return false;
        if (mStepperCircleTextType != that.mStepperCircleTextType) return false;
        return mStepperCircleSize == that.mStepperCircleSize;

    }

    @Override
    public int hashCode() {
        int result = mStepConnectorLineColorRes;
        result = 31 * result + mActiveColorRes;
        result = 31 * result + mInactiveColorRes;
        result = 31 * result + mCompletedIconRes;
        result = 31 * result + mActiveTitleAppearanceRes;
        result = 31 * result + mActiveSubTitleAppearanceRes;
        result = 31 * result + mInactiveTitleAppearanceRes;
        result = 31 * result + mInactiveSubTitleAppearanceRes;
        result = 31 * result + mStepperCircleTextType;
        result = 31 * result + mStepperCircleSize;
        return result;
    }
}
