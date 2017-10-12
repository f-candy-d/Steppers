package com.f_candy_d.verticalsteppers;

/**
 * Created by daichi on 10/12/17.
 */

public final class VerticalStepperStatus {

    // Is a stepper active or inactive
    private boolean isActive;
    // Is a stepper marked as completed or not
    private boolean isCompleted;
    // Is a stepper's content view expanded or collapsed
    private boolean isExpanded;

    /**
     * Setters have package-private visibility,
     * Getters have public visibility
     */

    public boolean isActive() {
        return isActive;
    }

    void setActive(boolean active) {
        isActive = active;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    public VerticalStepperStatus() {}

    public VerticalStepperStatus(boolean isActive, boolean isCompleted, boolean isExpanded) {
        this.isActive = isActive;
        this.isCompleted = isCompleted;
        this.isExpanded = isExpanded;
    }

    public VerticalStepperStatus(VerticalStepperStatus source) {
        copy(source);
    }

    // This method has package-private visibility
    void copy(VerticalStepperStatus source) {
        this.isActive = source.isActive;
        this.isCompleted = source.isCompleted;
        this.isExpanded = source.isExpanded;
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
