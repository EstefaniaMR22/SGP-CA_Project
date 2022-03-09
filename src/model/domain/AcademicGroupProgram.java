package model.domain;

import java.util.Date;

public class AcademicGroupProgram {
    private int id;
    private String name;
    private String vision;
    private String generalObjetive;
    private String mission;
    private ConsolidationGrade consolidationGrade;
    private Date registerDate;
    private Date lastEvaluationDate;
    private String secondmentUnit;
    private String descriptionAscription;

    public AcademicGroupProgram() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVision() {
        return vision;
    }

    public void setVision(String vision) {
        this.vision = vision;
    }

    public String getGeneralObjetive() {
        return generalObjetive;
    }

    public void setGeneralObjetive(String generalObjetive) {
        this.generalObjetive = generalObjetive;
    }

    public String getMission() {
        return mission;
    }

    public void setMission(String mission) {
        this.mission = mission;
    }

    public ConsolidationGrade getConsolidationGrade() {
        return consolidationGrade;
    }

    public void setConsolidationGrade(ConsolidationGrade consolidationGrade) {
        this.consolidationGrade = consolidationGrade;
    }

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    public Date getLastEvaluationDate() {
        return lastEvaluationDate;
    }

    public void setLastEvaluationDate(Date lastEvaluationDate) {
        this.lastEvaluationDate = lastEvaluationDate;
    }

    public String getSecondmentUnit() {
        return secondmentUnit;
    }

    public void setSecondmentUnit(String secondmentUnit) {
        this.secondmentUnit = secondmentUnit;
    }

    public String getDescriptionAscription() {
        return descriptionAscription;
    }

    public void setDescriptionAscription(String descriptionAscription) {
        this.descriptionAscription = descriptionAscription;
    }


}