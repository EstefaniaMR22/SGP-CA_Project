package model.domain;

import java.util.Date;

public class Member {
    private int id;
    private String name;
    private String maternalLastname;
    private String paternalLastname;
    private String nationality;
    private String educationalProgram;
    private String personalNumber;
    private Date admissionDate;
    private String rfc;
    private String telephone;
    private String curp;
    private CivilStatus civilStatus;
    private String uvEmail;
    private String birthState;
    private Date birthDate;
    private ParticipationType participationType;

    public Member() {
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

    public String getMaternalLastname() {
        return maternalLastname;
    }

    public void setMaternalLastname(String maternalLastname) {
        this.maternalLastname = maternalLastname;
    }

    public String getPaternalLastname() {
        return paternalLastname;
    }

    public void setPaternalLastname(String paternalLastname) {
        this.paternalLastname = paternalLastname;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getEducationalProgram() {
        return educationalProgram;
    }

    public void setEducationalProgram(String educationalProgram) {
        this.educationalProgram = educationalProgram;
    }

    public String getPersonalNumber() {
        return personalNumber;
    }

    public void setPersonalNumber(String personalNumber) {
        this.personalNumber = personalNumber;
    }

    public String getRfc() {
        return rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getCurp() {
        return curp;
    }

    public void setCurp(String curp) {
        this.curp = curp;
    }

    public CivilStatus getCivilStatus() {
        return civilStatus;
    }

    public void setCivilStatus(CivilStatus civilStatus) {
        this.civilStatus = civilStatus;
    }

    public String getUvEmail() {
        return uvEmail;
    }

    public void setUvEmail(String uvEmail) {
        this.uvEmail = uvEmail;
    }

    public String getBirthState() {
        return birthState;
    }

    public void setBirthState(String birthState) {
        this.birthState = birthState;
    }

    public ParticipationType getParticipationType() {
        return participationType;
    }

    public void setParticipationType(ParticipationType participationType) {
        this.participationType = participationType;
    }

    public Date getAdmissionDate() {
        return admissionDate;
    }

    public void setAdmissionDate(Date admissionDate) {
        this.admissionDate = admissionDate;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", maternalLastname='" + maternalLastname + '\'' +
                ", paternalLastname='" + paternalLastname + '\'' +
                ", nationality='" + nationality + '\'' +
                ", educationalProgram='" + educationalProgram + '\'' +
                ", personalNumber='" + personalNumber + '\'' +
                ", admissionDate=" + admissionDate +
                ", rfc='" + rfc + '\'' +
                ", telephone='" + telephone + '\'' +
                ", curp='" + curp + '\'' +
                ", civilStatus=" + civilStatus +
                ", uvEmail='" + uvEmail + '\'' +
                ", birthState='" + birthState + '\'' +
                ", birthDate=" + birthDate +
                ", participationType=" + participationType +
                '}';
    }
}
