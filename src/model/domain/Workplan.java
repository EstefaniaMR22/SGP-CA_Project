package model.domain;

import java.util.Date;
import java.util.List;

public class Workplan {
    private int id;
    private String identificator;
    private int yearsDuration;
    private Date startDate;
    private Date endDate;
    private String generalObjetive;
    private List<Goal> goalList;

    public Workplan() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdentificator() {
        return identificator;
    }

    public void setIdentificator(String identificator) {
        this.identificator = identificator;
    }

    public int getYearsDuration() {
        return yearsDuration;
    }

    public void setYearsDuration(int yearsDuration) {
        this.yearsDuration = yearsDuration;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getGeneralObjetive() {
        return generalObjetive;
    }

    public void setGeneralObjetive(String generalObjetive) {
        this.generalObjetive = generalObjetive;
    }

    public List<Goal> getGoalList() {
        return goalList;
    }

    public void setGoalList(List<Goal> goalList) {
        this.goalList = goalList;
    }

    @Override
    public String toString() {
        return "Workplan{" +
                "id=" + id +
                ", identificator='" + identificator + '\'' +
                ", yearsDuration=" + yearsDuration +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", generalObjetive='" + generalObjetive + '\'' +
                ", goalList=" + goalList +
                '}';
    }
}
