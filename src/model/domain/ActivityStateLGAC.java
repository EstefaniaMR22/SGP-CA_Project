package model.domain;

public enum ActivityStateLGAC {
    ACTIVE("ALTA"),
    INACTIVE("BAJA");

    private final String activityState;

    ActivityStateLGAC(String activityState) {
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
