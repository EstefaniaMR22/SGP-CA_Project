package model.domain;

import java.util.Date;

public class Workplan {
    private int id;
    private int yearsDuration;
    private Date startDate;
    private Date finalDate;
    private String objetiveGeneral;

    public Workplan() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public Date getFinalDate() {
        return finalDate;
    }

    public void setFinalDate(Date finalDate) {
        this.finalDate = finalDate;
    }

    public String getObjetiveGeneral() {
        return objetiveGeneral;
    }

    public void setObjetiveGeneral(String objetiveGeneral) {
        this.objetiveGeneral = objetiveGeneral;
    }
}
