package com.f_candy_d.verticalsteppers;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by daichi on 10/19/17.
 */

abstract public class Step {

    public static final int INVALID_UID = -1;

    // Uid must be a non-negative integer
    private final int mUid;
    private boolean mIsAlreadyBuilded = false;
    private View mExpandedContentView;
    private View mCollapsedContentView;
    private StepStateManager mStepStateManager;

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

    /* Intentional package-private */
    void build(VerticalStepper parentStepper, StepStateManager stepStateManager, boolean enforceBuild) {
        if (mIsAlreadyBuilded && !enforceBuild) return;
        mStepStateManager = stepStateManager;
        mExpandedContentView = onCreateExpandedContentView(parentStepper.getContentViewContainer());
        mCollapsedContentView = onCreateCollapsedContentView(parentStepper.getContentViewContainer());
//        Log.d("mylog", "build / view's parent -> " + mCollapsedContentView.getParent());
        mIsAlreadyBuilded = true;
    }

    /* Intentional package-private */
    View getExpandedContentView() {
        return mExpandedContentView;
    }

    /* Intentional package-private */
    View getCollapsedContentView() {
//        Log.d("mylog", "getCollapsedContentView / view's parent -> " + mCollapsedContentView.getParent());
        return mCollapsedContentView;
    }

    /* Intentional package-private */
    boolean hasExpandedContentView() {
        return mExpandedContentView != null;
    }

    /* Intentional package-private */
    boolean hasCollapsedContentView() {
        return mCollapsedContentView != null;
    }

    public boolean isStepExpanded() {
        return mIsStepExpanded;
    }

    public boolean isStepActivated() {
        return mIsStepActivated;
    }

    public boolean isStepChecked() {
        return mIsStepChecked;
    }

    public void setStepExpanded(boolean stepExpanded) {
        mIsStepExpanded = stepExpanded;
        mStepStateManager.onChangeStepExpandedState(this);
    }

    public void setStepActivated(boolean stepActivated) {
        mIsStepActivated = stepActivated;
        mStepStateManager.onChangeStepActivatedState(this);
    }

    public void setStepChecked(boolean stepChecked) {
        mIsStepChecked = stepChecked;
        mStepStateManager.onChangeStepCheckedState(this);
    }

    public int getUid() {
        return mUid;
    }

    /**
     * Do not attach a created view to 'parent' in these methods.
     */
    abstract protected View onCreateExpandedContentView(ViewGroup parent);
    abstract protected View onCreateCollapsedContentView(ViewGroup parent);

    abstract public int getOrder();

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
     * Override this method if you want to handle a step's click event.
     */
    protected void onStepClick() {}
}
