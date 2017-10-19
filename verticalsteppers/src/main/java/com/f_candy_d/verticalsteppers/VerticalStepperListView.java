package com.f_candy_d.verticalsteppers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.transition.AutoTransition;
import android.support.transition.Transition;
import android.support.transition.TransitionManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daichi on 10/19/17.
 */

public class VerticalStepperListView extends RecyclerView {

    private PreventableItemAnimator mPreventableItemAnimator;

    private final View.OnTouchListener TOUCH_EATER = new OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            return true;
        }
    };

    public VerticalStepperListView(Context context) {
        super(context);
        init();
    }

    public VerticalStepperListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public VerticalStepperListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mPreventableItemAnimator = new PreventableItemAnimator();
        setItemAnimator(mPreventableItemAnimator);
    }

    /**
     * Call this method just before we change view's status that TransitionManager can handle.
     * See more -> https://gist.github.com/yatatsu/6a60e1a0c384bb91ddec68951341f561
     */
    public void beginPartialItemTransition(@NonNull Transition transition) {
        transition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(@NonNull Transition transition) {
                setOnTouchListener(TOUCH_EATER);
            }

            @Override
            public void onTransitionEnd(@NonNull Transition transition) {
                mPreventableItemAnimator.setPreventMoveAnimation(false);
                setOnTouchListener(null);
            }

            @Override
            public void onTransitionCancel(@NonNull Transition transition) {}

            @Override
            public void onTransitionPause(@NonNull Transition transition) {}

            @Override
            public void onTransitionResume(@NonNull Transition transition) {}
        });

        TransitionManager.beginDelayedTransition(this, transition);
        mPreventableItemAnimator.setPreventMoveAnimation(true);
    }

    public void beginPartialItemTransition() {
        beginPartialItemTransition(new AutoTransition());
    }

    /**
     * Call this method to setup vertical steppers ui.
     */
    public void build(@NonNull Context context, List<Step> steps) {
        if (steps == null) {
            steps = new ArrayList<>(0);
        }

        setLayoutManager(new LinearLayoutManager(context));
        VerticalStepperAdapter adapter = new VerticalStepperAdapter(steps, this);
        setAdapter(adapter);
    }

    private static class PreventableItemAnimator extends DefaultItemAnimator {

        private boolean mPreventMoveAnimation = false;

        void setPreventMoveAnimation(boolean preventMoveAnimation) {
            mPreventMoveAnimation = preventMoveAnimation;
        }

        @Override
        public boolean animateMove(ViewHolder holder, int fromX, int fromY, int toX, int toY) {
            if (mPreventMoveAnimation) {
                dispatchMoveFinished(holder);
                return false;
            }
            return super.animateMove(holder, fromX, fromY, toX, toY);
        }
    }
}
