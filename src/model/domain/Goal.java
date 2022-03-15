package model.domain;

import java.util.Date;

public class Goal {
    private String id;
    private String description;
    private String state;
    private Date endDate;

    public Goal() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Goal{" +
                "id='" + id + '\'' +
                ", description='" + description + '\'' +
                ", state='" + state + '\'' +
                ", endDate=" + endDate +
                '}';
    }
}
