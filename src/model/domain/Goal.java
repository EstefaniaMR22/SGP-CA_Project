package model.domain;

import java.util.Date;
import java.util.List;

public class Goal {
    private String identificator;
    private int id;
    private String description;
    private GoalState state;
    private Date endDate;
    private List<Action> actions;

    public Goal() {
    }

    public String getIdentificator() {
        return identificator;
    }

    public void setIdentificator(String identificator) {
        this.identificator = identificator;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public GoalState getState() {
        return state;
    }

    public void setState(GoalState state) {
        this.state = state;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public List<Action> getActions() {
        return actions;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }

    @Override
    public String toString() {
        return "Goal{" +
                "identificator='" + identificator + '\'' +
                ", id=" + id +
                ", description='" + description + '\'' +
                ", state=" + state +
                ", endDate=" + endDate +
                ", actions=" + actions +
                '}';
    }
}
