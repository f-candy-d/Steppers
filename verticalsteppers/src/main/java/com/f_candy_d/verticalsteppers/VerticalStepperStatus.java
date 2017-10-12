package com.f_candy_d.verticalsteppers;

/**
 * Created by daichi on 10/12/17.
 */

public class VerticalStepperStatus {

    // Is a stepper active or inactive
    boolean isActive;
    // Is a stepper marked as completed or not
    boolean isCompleted;
    // Is a stepper's content view expanded or collapsed
    boolean isExpanded;

    public VerticalStepperStatus() {}

    public VerticalStepperStatus(boolean isActive, boolean isCompleted, boolean isExpanded) {
        this.isActive = isActive;
        this.isCompleted = isCompleted;
        this.isExpanded = isExpanded;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VerticalStepperStatus status = (VerticalStepperStatus) o;

        if (isActive != status.isActive) return false;
        if (isCompleted != status.isCompleted) return false;
        return isExpanded == status.isExpanded;

    }

    @Override
    public int hashCode() {
        int result = (isActive ? 1 : 0);
        result = 31 * result + (isCompleted ? 1 : 0);
        result = 31 * result + (isExpanded ? 1 : 0);
        return result;
    }
}
