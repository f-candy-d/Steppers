package com.f_candy_d.verticalsteppers;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daichi on 10/12/17.
 */

public final class VerticalStepperAdapter
        extends RecyclerView.Adapter<VerticalStepperAdapter.StepperViewHolder>
        implements VerticalStepperManagerCallback {

    private static final int TYPE_EXPANDED_VIEW = 0;
    private static final int TYPE_COLLAPSED_VIEW = 1;

    private VerticalStepperManager mStepperManager;
    @NonNull private List<VerticalStepperStatus> mStepperStatuses;

    public VerticalStepperAdapter() {
        this(null);
    }

    public VerticalStepperAdapter(VerticalStepperManager stepperManager) {
        setStepperManager(stepperManager);
        mStepperStatuses = new ArrayList<>();
    }

    public void setStepperManager(VerticalStepperManager stepperManager) {
        mStepperManager = stepperManager;
        if (mStepperManager != null) {
            mStepperManager.setCallback(this);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return (mStepperStatuses.get(position).isExpanded())
                ? TYPE_EXPANDED_VIEW
                : TYPE_COLLAPSED_VIEW;
    }

    @Override
    public StepperViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_vertical_stepper, parent, false);

        if (viewType == TYPE_EXPANDED_VIEW) {
            if (mStepperManager != null) {
                VerticalStepperView stepperView = itemView.findViewById(R.id.stepper_view);
                stepperView.applyStatus(new VerticalStepperStatus(false, false, false), true);
                stepperView.setContentView(mStepperManager
                        .onCreateExpandedContentView(stepperView.getContentViewContainer()));
            }
        } else if (viewType == TYPE_COLLAPSED_VIEW) {
            if (mStepperManager != null) {
                VerticalStepperView stepperView = itemView.findViewById(R.id.stepper_view);
                stepperView.applyStatus(new VerticalStepperStatus(false, false, false), true);
                stepperView.setContentView(mStepperManager
                        .onCreateCollapsedContentView(stepperView.getContentViewContainer()));
            }
        }

        return new StepperViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(StepperViewHolder holder, int position) {
        final int viewType = getItemViewType(position);
        VerticalStepperView stepperView = holder.stepperView;

        // # Title & Sub-Title

        if (mStepperManager != null) {
            stepperView.setTitle(mStepperManager.getStepperTitle(position));
            stepperView.setSubTitle(mStepperManager.getStepperSubTitle(position));
        }

        // # Step Label

        if (mStepperManager != null) {
            if (mStepperManager.isUseTextInStepperCircle()) {
                stepperView.setStepLabel(mStepperManager.getStepLabelText(position));
            } else {
                stepperView.setStepLabel(mStepperManager.getStepLabelNumber(position));
            }
        }

        // # ContentView

        if (viewType == TYPE_EXPANDED_VIEW) {
            if (stepperView.getContentView() != null && mStepperManager != null) {
                mStepperManager.onBindExpandedContentView(stepperView.getContentView(), position);
            }
        } else if (viewType == TYPE_COLLAPSED_VIEW) {
            if (stepperView.getContentView() != null && mStepperManager != null) {
                mStepperManager.onBindCollapsedContentView(stepperView.getContentView(), position);
            }
        }

        // # Status

        VerticalStepperStatus status = mStepperStatuses.get(position);
        stepperView.setIsActiveUi(status.isActive(), false);
        stepperView.setIsCompletedUi(status.isCompleted(), false);
    }

    @Override
    public int getItemCount() {
        return mStepperStatuses.size();
    }

    /**
     * VERTICAL_STEPPER_MANAGER_CALLBACK
     * ----------------------------------------------------------------------------- */

    @Override
    public void onInsertStep(int position, VerticalStepperStatus status) {
        mStepperStatuses.add(position, status);
        notifyItemInserted(position);
    }

    @Override
    public void onInsertSteps(int startPosition, List<VerticalStepperStatus> statuses) {
        mStepperStatuses.addAll(startPosition, statuses);
        notifyItemRangeInserted(startPosition, statuses.size());
    }

    @Override
    public void onRemoveStep(int position) {
        mStepperStatuses.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onRemoveStepsInRange(int startPosition, int count) {
        for (int i = startPosition + count - 1; startPosition <= i; --i) {
            mStepperStatuses.remove(i);
        }
        notifyItemRangeRemoved(startPosition, count);
    }

    @Override
    public void onMoveStep(int fromPosition, int toPosition) {
        VerticalStepperStatus status = mStepperStatuses.remove(fromPosition);
        mStepperStatuses.add(toPosition, status);
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onCompleteStep(int position) {
        VerticalStepperStatus status = mStepperStatuses.get(position);
        if (!status.isCompleted()) {
            status.setCompleted(true);
            notifyItemChanged(position);
        }
    }

    @Override
    public void onIncompleteStep(int position) {
        VerticalStepperStatus status = mStepperStatuses.get(position);
        if (status.isCompleted()) {
            status.setCompleted(false);
            notifyItemChanged(position);
        }
    }

    @Override
    public void onExpandStepContents(int position) {
        VerticalStepperStatus status = mStepperStatuses.get(position);
        if (!status.isExpanded()) {
            status.setExpanded(true);
            notifyItemChanged(position);
        }
    }

    @Override
    public void onCollapseStepContents(int position) {
        VerticalStepperStatus status = mStepperStatuses.get(position);
        if (status.isExpanded()) {
            status.setExpanded(false);
            notifyItemChanged(position);
        }
    }

    @Override
    public void onActivateStep(int position) {
        VerticalStepperStatus status = mStepperStatuses.get(position);
        if (!status.isActive()) {
            status.setActive(true);
            notifyItemChanged(position);
        }
    }

    @Override
    public void onInactivateStep(int position) {
        VerticalStepperStatus status = mStepperStatuses.get(position);
        if (status.isActive()) {
            status.setActive(false);
            notifyItemChanged(position);
        }
    }

    @Override
    public void onMoveToNextStep(int position, VerticalStepperStatus afterStatusOfCurrentStep, VerticalStepperStatus afterStatusOfNextStep) {
        VerticalStepperStatus status = mStepperStatuses.get(position);
        status.copy(afterStatusOfCurrentStep);
        if (mStepperStatuses.size() <= position + 1) {
            notifyItemChanged(position);
        } else {
            status = mStepperStatuses.get(position + 1);
            status.copy(afterStatusOfNextStep);
            // Update 2 step-views in the same time
            notifyItemRangeChanged(position, 2);
        }
    }

    @Override
    public void onChangeStepStatusesInRange(int startPosition, List<VerticalStepperStatus> statuses) {
        for (int i = startPosition; i < startPosition + statuses.size(); ++i) {
            mStepperStatuses.get(i).copy(statuses.get(i - startPosition));
        }
        notifyItemRangeChanged(startPosition, statuses.size());
    }

    @Override
    public VerticalStepperStatus getStepStatusOf(int position) {
        return mStepperStatuses.get(position);
    }

    @Override
    public int getStepCount() {
        return mStepperStatuses.size();
    }

    /**
     * VIEW HOLDER
     * ----------------------------------------------------------------------------- */

    static class StepperViewHolder extends RecyclerView.ViewHolder {

        VerticalStepperView stepperView;

        StepperViewHolder(View view) {
            super(view);
            stepperView = view.findViewById(R.id.stepper_view);
        }
    }
}
