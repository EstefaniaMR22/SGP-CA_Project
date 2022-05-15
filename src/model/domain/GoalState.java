package model.domain;

public enum GoalState {
    ACTIVE("ALTA"),
    INACTIVE("BAJA");

    private final String activityState;

    GoalState(String activityState) {
        this.activityState = activityState;
    }

    public String getActivityState() {
        return activityState;
    }

    @Override
    public String toString() {
        return "ActivityStateMember{" +
                "activityState='" + activityState + '\'' +
                '}';
    }
}
