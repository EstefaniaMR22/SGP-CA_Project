package model.domain;

import javafx.collections.ObservableList;

import java.util.Date;

public class Meet {
    private int idMeet;
    private String hour;
    private String asunto;
    private int idProject;
    private String nameProject;
    private Date register;
    private String registerString;
    private Date dateMeet;
    private String dateMeetString;
    private String totalTime;
    private int idLeader;
    private String leader;
    private int idSecretary;
    private String secretary;
    private int idTimer;
    private String timer;
    private ObservableList<Member> asistents;

    public Date getRegister() {
        return register;
    }

    public void setRegister(Date register) {
        this.register = register;
    }

    public String getRegisterString() {
        return registerString;
    }

    public void setRegisterString(String registerString) {
        this.registerString = registerString;
    }

    public Date getDateMeet() {
        return dateMeet;
    }

    public void setDateMeet(Date dateMeet) {
        this.dateMeet = dateMeet;
    }

    public String getDateMeetString() {
        return dateMeetString;
    }

    public void setDateMeetString(String dateMeetString) {
        this.dateMeetString = dateMeetString;
    }

    public int getIdLeader() {
        return idLeader;
    }

    public void setIdLeader(int idLeader) {
        this.idLeader = idLeader;
    }

    public String getLeader() {
        return leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }

    public int getIdSecretary() {
        return idSecretary;
    }

    public void setIdSecretary(int idSecretary) {
        this.idSecretary = idSecretary;
    }

    public String getSecretary() {
        return secretary;
    }

    public void setSecretary(String secretary) {
        this.secretary = secretary;
    }

    public int getIdTimer() {
        return idTimer;
    }

    public void setIdTimer(int idTimer) {
        this.idTimer = idTimer;
    }

    public String getTimer() {
        return timer;
    }

    public void setTimer(String timer) {
        this.timer = timer;
    }

    public ObservableList<Member> getAsistents() {
        return asistents;
    }

    public void setAsistents(ObservableList<Member> asistents) {
        this.asistents = asistents;
    }

    public int getIdMeet() {
        return idMeet;
    }

    public void setIdMeet(int idMeet) {
        this.idMeet = idMeet;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getNameProject() {
        return nameProject;
    }

    public void setNameProject(String nameProject) {
        this.nameProject = nameProject;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

    public int getIdProject() {
        return idProject;
    }

    public void setIdProject(int idProject) {
        this.idProject = idProject;
    }
}
