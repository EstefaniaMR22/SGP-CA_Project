package model.domain;

public enum ActivityStateMember {
    ACTIVE("ALTA"),
    INACTIVE("BAJA");

    private final String activityState;

    ActivityStateMember(String activityState) {
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
