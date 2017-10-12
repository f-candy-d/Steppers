package com.f_candy_d.steppers;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.f_candy_d.verticalsteppers.VerticalStepperManager;
import com.f_candy_d.verticalsteppers.VerticalStepperStatus;

import java.util.ArrayList;

/**
 * Created by daichi on 10/12/17.
 */

public class Manager extends VerticalStepperManager {

    public Manager() {}

    public void addSteps() {
        ArrayList<VerticalStepperStatus> statuses = new ArrayList<>();
        for (int i = 0; i < 10; ++i) {
            if (i == 0) {
                statuses.add(new VerticalStepperStatus(true, true, true));
            } else {
                statuses.add(new VerticalStepperStatus(false, false, false));
            }
        }
        notifyStepDataInserted(0, statuses);
    }

    private int i = 0;
    private VerticalStepperStatus afterCurrentStepStatus = new VerticalStepperStatus(true, true, false);
    private VerticalStepperStatus afterNextStepStatus = new VerticalStepperStatus(true, false, true);
    public void nextStep() {
        if (i < getStepCount()) {
            notifyMoveToNextStep(i, afterCurrentStepStatus, afterNextStepStatus);
            ++i;
        }
    }

    private static final int TAG_STEP_POSITION = -123445;

    @Override
    protected View onCreateExpandedContentView(ViewGroup parent) {
        final View content = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.expanded_content, parent, false);

        content.findViewById(R.id.collapse_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notifyCollapseStepContents((Integer) content.getTag(TAG_STEP_POSITION));
            }
        });

        content.findViewById(R.id.toggle_complete_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = (Integer) content.getTag(TAG_STEP_POSITION);
                VerticalStepperStatus status = getStepStatusAt(position);
                if (status.isCompleted()) {
                    notifyIncompleteStep(position);
                } else {
                    notifyCompleteStep(position);
                }
            }
        });

        content.findViewById(R.id.toggle_active_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = (Integer) content.getTag(TAG_STEP_POSITION);
                VerticalStepperStatus status = getStepStatusAt(position);
                if (status.isActive()) {
                    notifyInactivateStep(position);
                } else {
                    notifyActivateStep(position);
                }
            }
        });

        return content;
    }

    @Override
    protected View onCreateCollapsedContentView(ViewGroup parent) {
        final View content = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.collapsed_content, parent, false);

        content.findViewById(R.id.expand_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notifyExpandStepContents((Integer) content.getTag(TAG_STEP_POSITION));
            }
        });

        content.findViewById(R.id.toggle_complete_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = (Integer) content.getTag(TAG_STEP_POSITION);
                VerticalStepperStatus status = getStepStatusAt(position);
                if (status.isCompleted()) {
                    notifyIncompleteStep(position);
                } else {
                    notifyCompleteStep(position);
                }
            }
        });

        content.findViewById(R.id.toggle_active_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = (Integer) content.getTag(TAG_STEP_POSITION);
                VerticalStepperStatus status = getStepStatusAt(position);
                if (status.isActive()) {
                    notifyInactivateStep(position);
                } else {
                    notifyActivateStep(position);
                }
            }
        });

        return content;
    }

    @Override
    protected String getStepperTitle(int position) {
        return "Title at " + String.valueOf(position);
    }

    @Override
    protected String getStepperSubTitle(int position) {
        return "Sub-Title at " + String.valueOf(position);
    }

    @Override
    protected int getStepLabelNumber(int position) {
        return position + 1;
    }

    @Override
    protected String getStepLabelText(int position) {
        return "S";
    }

    @Override
    protected void onBindExpandedContentView(@NonNull View view, final int position) {
        view.setTag(TAG_STEP_POSITION, position);
    }

    @Override
    protected void onBindCollapsedContentView(@NonNull View view, final int position) {
        view.setTag(TAG_STEP_POSITION, position);
    }
}
