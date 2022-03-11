package model.domain;

import java.util.Date;
import java.util.List;

public class AcademicGroupProgram {
    private String id;
    private String name;
    private String vision;
    private String generalObjetive;
    private String mission;
    private ConsolidationGrade consolidationGrade;
    private Date registerDate;
    private Date lastEvaluationDate;
    private String adscriptionUnit;
    private String descriptionAdscription;
    private String adscriptionArea;
    private List<Colaborator> colaborators;
    private List<LGAC> lgacList;

    public AcademicGroupProgram() {
    }

    public String getAdscriptionArea() {
        return adscriptionArea;
    }

    public void setAdscriptionArea(String adscriptionArea) {
        this.adscriptionArea = adscriptionArea;
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

    public String getAdscriptionUnit() {
        return adscriptionUnit;
    }

    public void setAdscriptionUnit(String adscriptionUnit) {
        this.adscriptionUnit = adscriptionUnit;
    }

    public String getDescriptionAdscription() {
        return descriptionAdscription;
    }

    public void setDescriptionAdscription(String descriptionAdscription) {
        this.descriptionAdscription = descriptionAdscription;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Colaborator> getColaborators() {
        return colaborators;
    }

    public void setColaborators(List<Colaborator> colaborators) {
        this.colaborators = colaborators;
    }

    public List<LGAC> getLgacList() {
        return lgacList;
    }

    public void setLgacList(List<LGAC> lgacList) {
        this.lgacList = lgacList;
    }

    @Override
    public String toString() {
        return "AcademicGroupProgram{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", vision='" + vision + '\'' +
                ", generalObjetive='" + generalObjetive + '\'' +
                ", mission='" + mission + '\'' +
                ", consolidationGrade=" + consolidationGrade +
                ", registerDate=" + registerDate +
                ", lastEvaluationDate=" + lastEvaluationDate +
                ", adscriptionUnit='" + adscriptionUnit + '\'' +
                ", descriptionAscription='" + descriptionAdscription + '\'' +
                ", adscriptionArea='" + adscriptionArea + '\'' +
                ", colaborators=" + colaborators +
                ", lgacList=" + lgacList +
                '}';
    }
}