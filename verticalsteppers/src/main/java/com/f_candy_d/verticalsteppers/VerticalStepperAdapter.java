package com.f_candy_d.verticalsteppers;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daichi on 10/19/17.
 */

public class VerticalStepperAdapter
        extends RecyclerView.Adapter<VerticalStepperAdapter.StepViewHolder>
        implements StepClickListener {

    private StepManager mParentManager;

    public VerticalStepperAdapter(@NonNull StepManager parentManager) {
        mParentManager = parentManager;
    }

    /**
     * This Adapter does not recycle item views
     */
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    /**
     * @param viewType == position
     */
    @Override
    public StepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_step, parent, false);
        StepViewHolder holder = new StepViewHolder(view, this);
        Step step = mParentManager.getStepAt(viewType);
        holder.expandedContentVh = step.onCreateExpandedContentViewHolder(
                holder.stepper.getContentViewContainer());
        holder.collapsedContentVh = step.onCreateCollapsedContentViewHolder(
                holder.stepper.getContentViewContainer());

        // Attach content views to a stepper
        if (holder.expandedContentVh != null) {
            holder.stepper.addContentView(holder.expandedContentVh.contentRoot);
        }
        if (holder.collapsedContentVh != null) {
            holder.stepper.addContentView(holder.collapsedContentVh.contentRoot);
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(StepViewHolder holder, int position) {
        Step step = mParentManager.getStepAt(position);
        holder.stepper.setTitle(step.getTitle());
        holder.stepper.setSubTitle(step.getSubTitle());
        if (step.useTextLabel()) {
            holder.stepper.setCircleTextLabel(step.getTextLabel());
        } else {
            holder.stepper.setCircleTextLabel(step.getNumberLabel());
        }
    }

    // Use these constants as a payload to ignore unnecessary updates for an item view
    private static final int PAYLOAD_EXPAND_COLLAPSE = 1;
    private static final int PAYLOAD_ACTIVATE_INACTIVATE = 2;
    private static final int PAYLOAD_CHECK_UNCHECK = 3;

    @Override
    public void onBindViewHolder(StepViewHolder holder, int position, List<Object> payloads) {
        // On full bind
        if (payloads.size() == 0) {
            onBindViewHolder(holder, position);
        }

        Step step = mParentManager.getStepAt(position);

        // Is step active
        if (payloads.size() == 0 ||
                payloads.contains(PAYLOAD_ACTIVATE_INACTIVATE)) {

            if (step.isStepActivated()) {
                holder.stepper.activateStep();
            } else {
                holder.stepper.inactivateStep();
            }
        }

        // Is step checked
        if (payloads.size() == 0 ||
                payloads.contains(PAYLOAD_CHECK_UNCHECK)) {

            if (step.isStepChecked()) {
                holder.stepper.checkStep();
            } else {
                holder.stepper.uncheckStep();
            }
        }

        // Is step's content view expanded
        if (payloads.size() == 0
                || payloads.contains(PAYLOAD_EXPAND_COLLAPSE)) {

            if (step.isStepExpanded()) {
                if (holder.hasExpandedContentView()) {
                    holder.stepper.getContentViewContainer().setVisibility(View.VISIBLE);
                    holder.getExpandedContentView().setVisibility(View.VISIBLE);
                    if (holder.hasCollapsedContentView()) {
                        holder.getCollapsedContentView().setVisibility(View.GONE);
                    }

                } else {
                    holder.stepper.getContentViewContainer().setVisibility(View.GONE);
                }

            } else {
                if (holder.hasCollapsedContentView()) {
                    holder.stepper.getContentViewContainer().setVisibility(View.VISIBLE);
                    holder.getCollapsedContentView().setVisibility(View.VISIBLE);
                    if (holder.hasExpandedContentView()) {
                        holder.getExpandedContentView().setVisibility(View.GONE);
                    }

                } else {
                    holder.stepper.getContentViewContainer().setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return mParentManager.getStepCount();
    }

    /**
     * UPDATE STEPPER STATUS
     * ---------- */

    /* Intentional package-private */
    void onUpdateStepExpandedState(int position) {
        applyStepStateUpdates(position, PAYLOAD_EXPAND_COLLAPSE);
    }

    /* Intentional package-private */
    void onUpdateStepActivatedState(int position) {
        applyStepStateUpdates(position, PAYLOAD_ACTIVATE_INACTIVATE);
    }

    /* Intentional package-private */
    void onUpdateStepCheckedState(int position) {
        applyStepStateUpdates(position, PAYLOAD_CHECK_UNCHECK);
    }

    /* Intentional package-private */
    void onUpdateStepStatus(
            int position,
            boolean isExpandStateChanged,
            boolean isActiveStateChanged,
            boolean isCheckedStateChanged) {

        ArrayList<Integer> payloads = new ArrayList<>();
        if (isExpandStateChanged) {
            payloads.add(PAYLOAD_EXPAND_COLLAPSE);
        }
        if (isActiveStateChanged) {
            payloads.add(PAYLOAD_ACTIVATE_INACTIVATE);
        }
        if (isCheckedStateChanged) {
            payloads.add(PAYLOAD_CHECK_UNCHECK);
        }

        applyStepStateUpdates(position, payloads.toArray());
    }

    private void applyStepStateUpdates(int position, Object... payloads) {
        if (payloads.length != 0) {
            for (Object payload : payloads) {
                notifyItemChanged(position, payload);
            }
        } else {
            notifyItemChanged(position);
        }
    }

    /**
     * INTERFACE IMPL -> STEP-CLICK-LISTENER
     * ---------- */
    @Override
    public void onStepClicked(StepViewHolder holder) {
        int adpPos = holder.getAdapterPosition();
        if (0 <= adpPos && adpPos < mParentManager.getStepCount()) {
            mParentManager.getStepAt(adpPos).onStepClick();
        }
    }

    /**
     * VIEW HOLDER
     * ---------- */

    /* Intentional package-private */
    static class StepViewHolder extends RecyclerView.ViewHolder {

        VerticalStepper stepper;
        Step.ContentViewHolder expandedContentVh;
        Step.ContentViewHolder collapsedContentVh;

        StepViewHolder(View view, final StepClickListener listener) {

            super(view);
            this.stepper = view.findViewById(R.id.stepper);
            this.stepper.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onStepClicked(StepViewHolder.this);
                }
            });
        }

        boolean hasExpandedContentView() {
            return this.expandedContentVh != null;
        }

        boolean hasCollapsedContentView() {
            return this.collapsedContentVh != null;
        }

        View getExpandedContentView() {
            return (hasExpandedContentView())
                    ? this.expandedContentVh.contentRoot
                    : null;
        }

        View getCollapsedContentView() {
            return (hasCollapsedContentView())
                    ? this.collapsedContentVh.contentRoot
                    : null;
        }
    }
}
