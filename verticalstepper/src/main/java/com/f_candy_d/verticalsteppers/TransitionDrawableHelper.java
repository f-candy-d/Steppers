package com.f_candy_d.verticalsteppers;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;

/**
 * Created by daichi on 10/14/17.
 */

class TransitionDrawableHelper {

    private static final int FIRST_LAYER_INDEX = 0;
    private static final int SECOND_LAYER_INDEX = 1;

    private TransitionDrawable mTarget;
    private int mFirstLayerId = FIRST_LAYER_INDEX;
    private int mSecondLayerId = SECOND_LAYER_INDEX;
    private int mCurrentLayerIndex;

    public TransitionDrawableHelper(TransitionDrawable transitionDrawable) {
        mTarget = transitionDrawable;
        resetDrawableTransition();
    }

    public void setTarget(TransitionDrawable transitionDrawable) {
        mTarget = transitionDrawable;
        resetDrawableTransition();
    }

    public void setFirstLayerId(int firstLayerId) {
        if (firstLayerId == mSecondLayerId) {
            throw new IllegalArgumentException(
                    "Layer id must be unique, but firstLayerId=" + firstLayerId + " , secondLayerId=" + mSecondLayerId);
        }
        mFirstLayerId = firstLayerId;
        mTarget.setId(FIRST_LAYER_INDEX, firstLayerId);
    }

    public void setSecondLayerId(int secondLayerId) {
        if (secondLayerId == mFirstLayerId) {
            throw new IllegalArgumentException(
                    "Layer id must be unique, but firstLayerId=" + mFirstLayerId + " , secondLayerId=" + secondLayerId);
        }

        mSecondLayerId = secondLayerId;
        mTarget.setId(SECOND_LAYER_INDEX, secondLayerId);
    }

    public int getFirstLayerId() {
        return mFirstLayerId;
    }

    public int getSecondLayerId() {
        return mSecondLayerId;
    }

    public Drawable getDrawableById(int id) {
        if (id == mFirstLayerId) {
            return mTarget.getDrawable(FIRST_LAYER_INDEX);
        } else if (id == mSecondLayerId) {
            return mTarget.getDrawable(SECOND_LAYER_INDEX);
        } else {
            throw new IllegalArgumentException("Invalid id -> " + id);
        }
    }

    public void translateDrawable(int id, int duration) {
        if (id == mFirstLayerId) {
            if (mCurrentLayerIndex == SECOND_LAYER_INDEX) {
                mTarget.reverseTransition(duration);
                mCurrentLayerIndex = FIRST_LAYER_INDEX;
            }

        } else if (id == mSecondLayerId) {
            if (mCurrentLayerIndex == FIRST_LAYER_INDEX) {
                mTarget.startTransition(duration);
                mCurrentLayerIndex = SECOND_LAYER_INDEX;
            }
        } else {
            throw new IllegalArgumentException("Invalid id -> " + id);
        }
    }

    public void setCurrentDrawable(int id) {
        translateDrawable(id, 0);
    }


    public void resetDrawableTransition() {
        mTarget.resetTransition();
        mCurrentLayerIndex = FIRST_LAYER_INDEX;
    }
}
