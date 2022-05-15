package model.domain;

import java.util.List;

public class Action {
    private String description;
    private ActionState state;
    private List<String> recursos;
    private Member responsable;
    private int id;

    public Action() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ActionState getState() {
        return state;
    }

    public void setState(ActionState state) {
        this.state = state;
    }

    public List<String> getRecursos() {
        return recursos;
    }

    public void setRecursos(List<String> recursos) {
        this.recursos = recursos;
    }

    public Member getResponsable() {
        return responsable;
    }

    public void setResponsable(Member responsable) {
        this.responsable = responsable;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Action{" +
                "description='" + description + '\'' +
                ", state=" + state +
                ", recursos=" + recursos +
                ", responsable=" + responsable +
                ", id=" + id +
                '}';
    }
}
