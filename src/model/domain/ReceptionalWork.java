package model.domain;

import java.util.Date;

public class ReceptionalWork {
    private int idReceptionalWork;
    private String nameReceptionalWork;
    private String description;
    private String nameProject;
    private int estimatedDurationInMonths;
    private int durationInMonths;
    private Date register;
    private String registerString;
    private Date endDate;
    private String endDateString;
    private String modality;
    private String status;
    private String requeriments;
    private int idDirector;
    private String director;
    private int idCodirector;
    private String codirector;
    private int participants;

    public int getParticipants() {
        return participants;
    }

    public void setParticipants(int participants) {
        this.participants = participants;
    }

    public int getIdDirector() {
        return idDirector;
    }

    public void setIdDirector(int idDirector) {
        this.idDirector = idDirector;
    }

    public int getIdCodirector() {
        return idCodirector;
    }

    public void setIdCodirector(int idCodirector) {
        this.idCodirector = idCodirector;
    }

    public int getStimatedDurationInMonths() {
        return estimatedDurationInMonths;
    }

    public void setStimatedDurationInMonths(int stimatedDurationInMonths) {
        this.estimatedDurationInMonths = stimatedDurationInMonths;
    }

    public String getRegisterString() {
        return registerString;
    }

    public void setRegisterString(String registerString) {
        this.registerString = registerString;
    }

    public String getEndDateString() {
        return endDateString;
    }

    public void setEndDateString(String endDateString) {
        this.endDateString = endDateString;
    }

    public int getIdReceptionalWork() {
        return idReceptionalWork;
    }

    public void setIdReceptionalWork(int idReceptionalWork) {
        this.idReceptionalWork = idReceptionalWork;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getCodirector() {
        return codirector;
    }

    public void setCodirector(String codirector) {
        this.codirector = codirector;
    }

    public String getNameProject() {
        return nameProject;
    }

    public void setNameProject(String nameProject) {
        this.nameProject = nameProject;
    }

    public String getNameReceptionalWork() {
        return nameReceptionalWork;
    }

    public void setNameReceptionalWork(String nameReceptionalWork) {
        this.nameReceptionalWork = nameReceptionalWork;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getRegister() {
        return register;
    }

    public void setRegister(Date register) {
        this.register = register;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getModality() {
        return modality;
    }

    public void setModality(String modality) {
        this.modality = modality;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRequeriments() {
        return requeriments;
    }

    public void setRequeriments(String requeriments) {
        this.requeriments = requeriments;
    }

    public int getDurationInMonths() {
        return durationInMonths;
    }

    public void setDurationInMonths(int durationInMonths) {
        this.durationInMonths = durationInMonths;
    }
}
