/*
 * @author Josué 
 * @versión v1.0
 * Last modification date: 17-06-2021
 */

package model.old.domain;

import java.util.ArrayList;
import java.util.List;


public class Integrant extends Member {
    
    private String appointmentMember = "PTC";
    private String aditionalMail = "";
    private String homePhone = "";
    private String workPhone = "";
    private List<Schooling> schooling;
    String password = "root";

    public Integrant(String rfc,String fullName, String emailUV, String participationStatus, String password,
    String curp, String participationType, String nationality, String dateOfAdmission, String educationalProgram, 
    int staffNumber, String cellphone, String bodyAcademyKey, String appointmentMember, String aditionalMail, String homePhone, String workPhone){
        super(
            rfc, fullName, emailUV, participationStatus, curp, participationType, nationality, dateOfAdmission, 
            educationalProgram, staffNumber, cellphone, bodyAcademyKey
        );
        this.appointmentMember = appointmentMember;
        this.aditionalMail = aditionalMail;
        this.homePhone = homePhone;
        this.workPhone = workPhone;
        this.password = password;
        this.schooling = new ArrayList<>();
    }    
    
    public Integrant(String participationType, String fullName, String emailUV, String cellphone){
        super(participationType, fullName, emailUV, cellphone);
    }

    public Integrant(){
        this.schooling = new ArrayList<>();
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public List<Schooling> getSchooling(){
        return schooling;
    }
    
    public void addSchooling(Schooling newSchooling){
        this.schooling.add(newSchooling);
    }
    
    public void removeSchooling(Schooling schooling){
        this.schooling.remove(schooling);
    }

    public void setSchooling(List<Schooling> schooling){
        this.schooling = schooling;
    }  
    

    public String getAppointmentMember(){
        return appointmentMember;
    }

    public void setAppointmentMember(String appointmentMember){
        this.appointmentMember = appointmentMember;
    }

    public String getAditionalMail(){
        return aditionalMail;
    }

    public void setAditionalMail(String aditionalMail){
        this.aditionalMail = aditionalMail;
    }

    public String getHomePhone(){
        return homePhone;
    }

    public void setHomePhone(String homePhone){
        this.homePhone = homePhone;
    }

    public String getWorkPhone(){
        return workPhone;
    }

    public void setWorkPhone(String workPhone){
        this.workPhone = workPhone;
    }
    
    public Integrant(String rfc,String fullName){
        super(rfc, fullName);
    }

    @Override
    public String toString() {
        return super.toString();
    }
    
    @Override
    public int hashCode(){
        return super.getEmailUV().hashCode();
    }
    
}
