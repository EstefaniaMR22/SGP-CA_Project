/**
* @author Johann 
* Last modification date format: 26-03-2021
*/

package sgp.ca.domain;

public class Agreement {
    private int agreementNumber;
    private String descriptionAgreement;
    private String responsibleAgreement;
    private String deliveryDate;

    public Agreement(int agreementNumber, String descriptionAgreement, 
    String responsibleAgreement, String deliveryDate) {
        this.agreementNumber = agreementNumber;
        this.descriptionAgreement = descriptionAgreement;
        this.responsibleAgreement = responsibleAgreement;
        this.deliveryDate = deliveryDate;
    }
    
    public Agreement(String descriptionAgreement, 
    String responsibleAgreement, String deliveryDate) {
        this.descriptionAgreement = descriptionAgreement;
        this.responsibleAgreement = responsibleAgreement;
        this.deliveryDate = deliveryDate;
    }

    public Agreement() {
        
    }

    public int getAgreementNumber() {
        return agreementNumber;
    }

    public String getDescriptionAgreement() {
        return descriptionAgreement;
    }

    public String getResponsibleAgreement() {
        return responsibleAgreement;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setAgreementNumber(int agreementNumber) {
        this.agreementNumber = agreementNumber;
    }

    public void setDescriptionAgreement(String descriptionAgreement) {
        this.descriptionAgreement = descriptionAgreement;
    }

    public void setResponsibleAgreement(String responsibleAgreement) {
        this.responsibleAgreement = responsibleAgreement;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }
}
