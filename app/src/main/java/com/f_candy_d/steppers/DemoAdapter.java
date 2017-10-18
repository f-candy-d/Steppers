package com.f_candy_d.steppers;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.f_candy_d.verticalsteppers.StepperStateSet;
import com.f_candy_d.verticalsteppers.VerticalStepperView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daichi on 10/18/17.
 */

public class DemoAdapter extends RecyclerView.Adapter<DemoAdapter.ViewHolder> {

    private List<StepperStateSet> mStateSetList;

    public DemoAdapter() {
        mStateSetList = new ArrayList<>();
        StepperStateSet base = new StepperStateSet();
        base.contentViewIsCollapsed().stepIsInactive().stepIsIncompleted();
        for (int i = 0; i < 20; ++i) {
            mStateSetList.add(new StepperStateSet(base));
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_stepper, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        // footer content view
        view = holder.stepperView.getFooterContentView();
        view.findViewById(R.id.toggle_status_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VerticalStepperView stepperView = holder.stepperView;
                @VerticalStepperView.ChangingStatusAnimationFlag int flags = 0;
                flags |= (stepperView.isContentViewExpanded()) ? VerticalStepperView.ANIMATE_COLLAPSE_CONTENTS : VerticalStepperView.ANIMATE_EXPAND_CONTENTS;
                flags |= (stepperView.isStepActive()) ? VerticalStepperView.ANIMATE_INACTIVATE_STEP : VerticalStepperView.ANIMATE_ACTIVATE_STEP;
                flags |= (stepperView.isStepCompleted()) ? VerticalStepperView.ANIMATE_INCOMPLETE_STEP : VerticalStepperView.ANIMATE_COMPLETE_STEP;
                stepperView.animateChangingStatus(flags);
            }
        });

        return holder;
    }

    @Override
    public int getItemCount() {
        return mStateSetList.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.stepperView.applyStatusSet(mStateSetList.get(position), false);
        holder.stepperView.setTitle("Stepper title for position " + position);
        holder.stepperView.setSubTitle("Step summary for position " + position + " if needed...");
        holder.stepperView.setStepLabel(position + 1);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        VerticalStepperView stepperView;

        ViewHolder(View view) {
            super(view);
            stepperView = view.findViewById(R.id.stepper);
        }
    }
}
