/*
 * @author Josué 
 * @versión v1.0
 * Last modification date: 17-06-2021
 */

package controller.old;

import java.net.URL;
import java.util.ResourceBundle;

import model.old.domain.Collaborator;
import model.old.domain.Integrant;
import model.old.dataaccess.CollaboratorDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class CollaboratorRequestController implements Initializable{

    @FXML
    private Button btnUnsubscribe;
    @FXML
    private Button btnSubscribe;
    @FXML
    private Button btnCollaboratorEdit;
    @FXML
    private Button btnExit;
    @FXML
    private TextField txtFieldRfcMember;
    @FXML
    private TextField txtFieldFullName;
    @FXML
    private TextField txtFieldEmailUv;
    @FXML
    private TextField txtFieldCurp;
    @FXML
    private TextField txtFieldNationality;
    @FXML
    private TextField txtFieldEducationalProgram;
    @FXML
    private TextField txtFieldCellNumber;
    @FXML
    private TextField txtFieldStatus;
    @FXML
    private TextField txtFieldStaffNumber;
    @FXML
    private TextField txtFieldStudyArea;
    @FXML
    private TextField txtFieldBodyAcademyName;
    @FXML
    private TextField txtFieldLevelStudy;
    @FXML
    private TextField txtFieldRegistrationDate;
    @FXML
    private Label lbParticipationType;
    @FXML
    private HBox hbCollaboratorOptions;
    
    private Collaborator collaborator;
    private Integrant token;
    private final CollaboratorDAO COLLABORATOR_DAO = new CollaboratorDAO();
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb){
        hbCollaboratorOptions.getChildren().removeAll(btnCollaboratorEdit, btnExit, btnSubscribe, btnUnsubscribe);
    }

    public void showCollaboratorByEmail(Integrant token, String collaboratorEmailUV){
        this.token = token;
        this.collaborator = (Collaborator) COLLABORATOR_DAO.getMemberByUVmail(collaboratorEmailUV);
        if(this.collaborator != null){
            this.setCollaboratorInformationInInterface();
            this.checkPrivileges();
        }else{
            WindowControllerB.getWindowController().showErrorAlert(new ActionEvent(), "Lo sentimos, parece que el sistema no se encuentra disponible por el momento");
            FXMLLoader loader = WindowControllerB.getWindowController().changeWindow("GeneralResumeRequest.fxml", btnExit);
            GeneralResumeRequestController controller = loader.getController();
            controller.showGeneralResume(token);
        }
    }

    @FXML
    private void unsubscribeCollaborator(ActionEvent event){
        if(COLLABORATOR_DAO.unsubscribeMemberByEmailUV(this.collaborator.getEmailUV())){
            WindowControllerB.getWindowController().showInfoAlert(event, "Colaborador dado de baja");
        }else{
            WindowControllerB.getWindowController().showErrorAlert(event, "Error en el sistema, favor de comunicarse con soporte técnico");
        }
        FXMLLoader loader = WindowControllerB.getWindowController().changeWindow("GeneralResumeRequest.fxml", btnExit);
        GeneralResumeRequestController controller = loader.getController();
        controller.showGeneralResume(token);
    }

    @FXML
    private void subscribeCollaborator(ActionEvent event){
        if(COLLABORATOR_DAO.subscribeMemberByEmailUV(this.collaborator.getEmailUV())){
            WindowControllerB.getWindowController().showInfoAlert(event, "Colaborador dado de alta");
        }else{
            WindowControllerB.getWindowController().showErrorAlert(event, "Error en el sistema, favor de comunicarse con soporte técnico");
        }
        FXMLLoader loader = WindowControllerB.getWindowController().changeWindow("GeneralResumeRequest.fxml", btnExit);
        GeneralResumeRequestController controller = loader.getController();
        controller.showGeneralResume(token);
    }

    @FXML
    private void editCollaborator(ActionEvent event){
        FXMLLoader loader = WindowControllerB.getWindowController().changeWindow("CollaboratorEditable.fxml", btnExit);
        CollaboratorEditableController controller = loader.getController();
        controller.showCollaboratorUpdateForm(token, this.collaborator.getEmailUV());
    }

    @FXML
    private void exit(ActionEvent event){
        FXMLLoader loader = WindowControllerB.getWindowController().changeWindow("GeneralResumeRequest.fxml", btnExit);
        GeneralResumeRequestController controller = loader.getController();
        controller.showGeneralResume(token);
    }
    
    private void setCollaboratorInformationInInterface(){
        this.txtFieldCellNumber.setText(collaborator.getCellphone());
        this.txtFieldCurp.setText(collaborator.getCurp());
        this.txtFieldEducationalProgram.setText(collaborator.getEducationalProgram());
        this.txtFieldEmailUv.setText(collaborator.getEmailUV());
        this.txtFieldFullName.setText(collaborator.getFullName());
        this.txtFieldNationality.setText(collaborator.getNationality());
        this.txtFieldRfcMember.setText(collaborator.getRfc());
        this.txtFieldRegistrationDate.setText(collaborator.getDateOfAdmission());
        this.txtFieldStaffNumber.setText(String.valueOf(collaborator.getStaffNumber()));
        this.lbParticipationType.setText(collaborator.getParticipationType());
        this.txtFieldBodyAcademyName.setText(collaborator.getNameBACollaborator());
        this.txtFieldLevelStudy.setText(collaborator.getHighestDegreeStudies());
        this.txtFieldStudyArea.setText(collaborator.getStudyArea());
        this.txtFieldStatus.setText(collaborator.getParticipationStatus());
    }    
    
    private void checkPrivileges(){
        if(this.token.getParticipationType().equalsIgnoreCase("Responsable")){
            hbCollaboratorOptions.getChildren().addAll(btnCollaboratorEdit, btnUnsubscribe, btnSubscribe, btnExit);
            if(this.collaborator.getParticipationStatus().equalsIgnoreCase("Activo")){
                hbCollaboratorOptions.getChildren().remove(btnSubscribe);
            }else{
                hbCollaboratorOptions.getChildren().remove(btnUnsubscribe);
            }
        }else{
            hbCollaboratorOptions.getChildren().addAll(btnExit);
        }
    }
}
