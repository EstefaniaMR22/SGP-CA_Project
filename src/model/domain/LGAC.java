package model.domain;

public class LGAC {
    private int id;
    private String identification;
    private String description;
    private ActivityStateLGAC activityState;

    public LGAC() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ActivityStateLGAC getActivityState() {
        return activityState;
    }

    public void setActivityState(ActivityStateLGAC activityState) {
        this.activityState = activityState;
    }

    @Override
    public String toString() {
        return "LGAC{" +
                "id=" + id +
                ", identification='" + identification + '\'' +
                ", description='" + description + '\'' +
                ", activityState=" + activityState +
                '}';
    }
}
