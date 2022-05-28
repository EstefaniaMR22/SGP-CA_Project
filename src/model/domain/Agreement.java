package model.domain;

import java.util.Date;

public class Agreement {

    private int idAgreement;
    private String descriptionAgreement;
    private Date registerAgreement;
    private int author;
    private String authorAgreement;
    private int idMeet;

    public int getIdMeet() {
        return idMeet;
    }

    public void setIdMeet(int idMeet) {
        this.idMeet = idMeet;
    }

    public int getIdAgreement() {
        return idAgreement;
    }

    public void setIdAgreement(int idAgreement) {
        this.idAgreement = idAgreement;
    }

    public String getDescriptionAgreement() {
        return descriptionAgreement;
    }

    public void setDescriptionAgreement(String descriprionAgreement) {
        this.descriptionAgreement = descriprionAgreement;
    }

    public Date getRegisterAgreement() {
        return registerAgreement;
    }

    public void setRegisterAgreement(Date registerAgreement) {
        this.registerAgreement = registerAgreement;
    }

    public int getAuthor() {
        return author;
    }

    public void setAuthor(int author) {
        this.author = author;
    }

    public String getAuthorAgreement() {
        return authorAgreement;
    }

    public void setAuthorAgreement(String authorAgreement) {
        this.authorAgreement = authorAgreement;
    }
}
