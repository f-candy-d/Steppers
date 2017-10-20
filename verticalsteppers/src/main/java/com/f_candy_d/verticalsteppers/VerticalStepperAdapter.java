package com.f_candy_d.verticalsteppers;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by daichi on 10/19/17.
 */

public class VerticalStepperAdapter
        extends RecyclerView.Adapter<VerticalStepperAdapter.StepViewHolder>
        implements StepStateManager, StepClickListener {

    private static final int INVALID_POSITION = RecyclerView.NO_POSITION;

    private List<Step> mSteps;
    private VerticalStepperListView mParentListView;

    public VerticalStepperAdapter(@NonNull List<Step> steps, @NonNull VerticalStepperListView parentListView) {
        mParentListView = parentListView;
        mSteps = new ArrayList<>(steps);
        Collections.sort(mSteps, new Comparator<Step>() {
            @Override
            public int compare(Step step, Step t1) {
                return Integer.valueOf(step.getOrder()).compareTo(t1.getOrder());
            }
        });
    }

    /**
     * @param viewType == position
     */
    @Override
    public StepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("mylog", "onCreateViewHolder");
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_step, parent, false);
        return new StepViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(StepViewHolder holder, int position) {
        Step step = mSteps.get(position);

        Log.d("mylog", "onBindViewHolder / holder.bindingStepUid=" + holder.bindingStepUid + " != step.getUid()=" + step.getUid());

        if (holder.bindingStepUid != step.getUid()) {
            step.build(holder.stepper, this, false);
            Log.d("mylog", "1 parent => " + step.getCollapsedContentView().getParent());
            holder.stepper.removeAllContentViews();
            Log.d("mylog", "2 parent => " + step.getCollapsedContentView().getParent());
            holder.stepper.addContentView(step.getCollapsedContentView());
            Log.d("mylog", "3 parent => " + step.getCollapsedContentView().getParent());
            holder.stepper.addContentView(step.getExpandedContentView());

            holder.bindingStepUid = step.getUid();
        }

        holder.stepper.setTitle(step.getTitle());
        holder.stepper.setSubTitle(step.getSubTitle());
        if (step.useTextLabel()) {
            holder.stepper.setCircleTextLabel(step.getTextLabel());
        } else {
            holder.stepper.setCircleTextLabel(step.getNumberLabel());
        }
        applyStepStatus(step, holder.stepper);
    }

    @Override
    public int getItemCount() {
        return mSteps.size();
    }

    private void applyStepStatus(Step step, VerticalStepper stepper) {
        if (step.isStepActivated()) {
            stepper.activateStep();
        } else {
            stepper.inactivateStep();
        }

        if (step.isStepChecked()) {
            stepper.checkStep();
        } else {
            stepper.uncheckStep();
        }

        if (step.isStepExpanded()) {
            if (step.hasExpandedContentView()) {
                stepper.getContentViewContainer().setVisibility(View.VISIBLE);
                step.getExpandedContentView().setVisibility(View.VISIBLE);
                if (step.hasCollapsedContentView()) {
                    step.getCollapsedContentView().setVisibility(View.GONE);
                }

            } else {
                stepper.getContentViewContainer().setVisibility(View.GONE);
            }

        } else {
            if (step.hasCollapsedContentView()) {
                stepper.getContentViewContainer().setVisibility(View.VISIBLE);
                step.getCollapsedContentView().setVisibility(View.VISIBLE);
                if (step.hasExpandedContentView()) {
                    step.getExpandedContentView().setVisibility(View.GONE);
                }

            } else {
                stepper.getContentViewContainer().setVisibility(View.GONE);
            }
        }
    }

    private int findStepPositionByUid(int uid) {
        int position = 0;
        for (Step step : mSteps) {
            if (step.getUid() == uid) {
                return position;
            }
            ++position;
        }

        return INVALID_POSITION;
    }

    /**
     * StepStateManager interface implementation
     * ---------- */

    @Override
    public void onChangeStepExpandedState(Step step) {

    }

    @Override
    public void onChangeStepActivatedState(Step step) {
        mParentListView.beginPartialItemTransition();
        int position = findStepPositionByUid(step.getUid());
        Log.d("mylog", "onChangeStepActivatedState for position " + position);
        // TODO; Use payload for better performance
        if (position != INVALID_POSITION) {
            notifyItemChanged(position);
        }
    }

    @Override
    public void onChangeStepCheckedState(Step step) {

    }

    /**
     * STEP-CLICK-LISTENER INTERFACE IMPLEMENTATION
     * ---------- */
    @Override
    public void onStepClicked(StepViewHolder holder) {
        int adpPos = holder.getAdapterPosition();
        Log.d("mylog", "onStepClicked for position -> " + adpPos);
        if (0 <= adpPos && adpPos < mSteps.size()) {
            mSteps.get(adpPos).onStepClick();
        }
    }

    /**
     * VIEW HOLDER
     * ---------- */
    static class StepViewHolder extends RecyclerView.ViewHolder {

        VerticalStepper stepper;
        int bindingStepUid = Step.INVALID_UID;

        StepViewHolder(View view, final StepClickListener listener) {
            super(view);
            Log.d("mylog", "constructor of StepViewHolder");
            stepper = view.findViewById(R.id.stepper);
            stepper.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onStepClicked(StepViewHolder.this);
                }
            });
        }
    }
}
