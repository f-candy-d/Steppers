package com.f_candy_d.verticalsteppers;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
        implements StepperStatusObserver {

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
            mStepperManager.setStepperStatusObserver(this);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return (mStepperStatuses.get(position).isExpanded)
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
                stepperView.setContentView(mStepperManager
                        .onCreateExpandedContentView(stepperView.getContentViewContainer()));
            }
        } else if (viewType == TYPE_COLLAPSED_VIEW) {
            if (mStepperManager != null) {
                VerticalStepperView stepperView = itemView.findViewById(R.id.stepper_view);
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
        stepperView.setIsActiveUi(status.isActive, false);
    }

    @Override
    public int getItemCount() {
        return mStepperStatuses.size();
    }

    /**
     * STEPPER_STATUS_OBSERVER
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
        if (!status.isCompleted) {
            status.isCompleted = true;
            notifyItemChanged(position);
        }
    }

    @Override
    public void onIncompleteStep(int position) {
        VerticalStepperStatus status = mStepperStatuses.get(position);
        if (status.isCompleted) {
            status.isCompleted = false;
            notifyItemChanged(position);
        }
    }

    @Override
    public void onExpandStepContents(int position) {
        VerticalStepperStatus status = mStepperStatuses.get(position);
        if (!status.isExpanded) {
            status.isExpanded = true;
            notifyItemChanged(position);
        }
    }

    @Override
    public void onCollapseStepContents(int position) {
        VerticalStepperStatus status = mStepperStatuses.get(position);
        if (status.isExpanded) {
            status.isExpanded = false;
            notifyItemChanged(position);
        }
    }

    @Override
    public void onActivateStep(int position) {
        VerticalStepperStatus status = mStepperStatuses.get(position);
        if (!status.isActive) {
            status.isActive = true;
            notifyItemChanged(position);
        }
    }

    @Override
    public void onInactivateStep(int position) {
        VerticalStepperStatus status = mStepperStatuses.get(position);
        if (status.isActive) {
            status.isActive = false;
            notifyItemChanged(position);
        }
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
