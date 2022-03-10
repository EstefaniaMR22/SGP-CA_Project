package model.domain;

public class Responsable extends Member {
    private String appointment;
    private String workTelephone;
    private String homeTelephone;
    private String aditionalEmail;

    public Responsable() {
    }

    public String getAppointment() {
        return appointment;
    }

    public void setAppointment(String appointment) {
        this.appointment = appointment;
    }

    public String getWorkTelephone() {
        return workTelephone;
    }

    public void setWorkTelephone(String workTelephone) {
        this.workTelephone = workTelephone;
    }

    public String getHomeTelephone() {
        return homeTelephone;
    }

    public void setHomeTelephone(String homeTelephone) {
        this.homeTelephone = homeTelephone;
    }

    public String getAditionalEmail() {
        return aditionalEmail;
    }

    public void setAditionalEmail(String aditionalEmail) {
        this.aditionalEmail = aditionalEmail;
    }

    @Override
    public String toString() {
        return "Responsable{" +
                "appointment='" + appointment + '\'' +
                ", workTelephone='" + workTelephone + '\'' +
                ", homeTelephone='" + homeTelephone + '\'' +
                ", aditionalEmail='" + aditionalEmail + '\'' +
                '}';

    }
}
