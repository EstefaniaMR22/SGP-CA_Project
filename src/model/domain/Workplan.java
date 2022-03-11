package model.domain;

import java.util.Date;

public class Workplan {
    private String id;
    private int yearsDuration;
    private Date startDate;
    private Date endDate;
    private String generalObjetive;

    public Workplan() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "Workplan{" +
                "id='" + id + '\'' +
                ", yearsDuration=" + yearsDuration +
                ", startDate=" + startDate +
                ", finalDate=" + endDate +
                ", objetiveGeneral='" + generalObjetive + '\'' +
                '}';
    }
}
