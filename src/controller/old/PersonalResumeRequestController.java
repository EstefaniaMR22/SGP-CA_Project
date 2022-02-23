/*
 * @author Josué 
 * @versión v1.0
 * Last modification date: 17-06-2021
 */

package controller.old;

import java.net.URL;
import java.util.ResourceBundle;

import model.domain.Integrant;
import model.domain.Schooling;
import model.dataaccess.IntegrantDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class PersonalResumeRequestController implements Initializable{

    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnProduction;
    @FXML
    private Button btnExit;
    @FXML
    private TextField txtFieldRfc;
    @FXML
    private TextField txtFieldFullName;
    @FXML
    private TextField txtFieldEmailUv;
    @FXML
    private TextField txtFieldStatus;
    @FXML
    private TextField txtFieldCurp;
    @FXML
    private TextField txtFieldNationality;
    @FXML
    private TextField txtFieldEducationalProgram;
    @FXML
    private TextField txtFieldStaffNumber;
    @FXML
    private TextField txtFieldCellPhone;
    @FXML
    private TextField txtFieldWorkPhone;
    @FXML
    private TextField txtFieldHomePhone;
    @FXML
    private TextField txtFieldBodyAcademyKey;
    @FXML
    private TextField txtFieldAppoinment;
    @FXML
    private TextField txtFieldParticipationType;
    @FXML
    private TextField txtFieldAditionalMail;
    @FXML
    private PasswordField passFieldIntegrantPassword;
    @FXML
    private Label lbUserName;
    @FXML
    private TextField txtFieldRegistrationDate;
    @FXML
    private TableView<Schooling> tvSchooling;
    @FXML
    private TableColumn<Schooling, String> colSchoolingDegree;
    @FXML
    private TableColumn<Schooling, String> colSchoolingName;
    @FXML
    private TableColumn<Schooling, String> colRegistrationStudyDate;
    @FXML
    private TableColumn<Schooling, String> colInstitution;
    @FXML
    private TableColumn<Schooling, String> colState;
    @FXML
    private TableColumn<Schooling, String> colCeduleNumber;
    @FXML
    private TableColumn<Schooling, String> colSchoolingArea;
    @FXML
    private TableColumn<Schooling, String> colDiscipline;
    
    private Integrant integrantToken;
    private final IntegrantDAO INTEGRANT_DAO = new IntegrantDAO();
    
    @Override
    public void initialize(URL url, ResourceBundle rb){
        this.preprareSchoolingTable();
    }    
    
    public void receiveIntegrantToken(Integrant integrant){
        this.integrantToken = integrant;
        this.lbUserName.setText(this.integrantToken.getFullName());
        this.setIntegrantDataIntoInterface();
    }

    @FXML
    private void updatePersonalResume(ActionEvent event){
        FXMLLoader loader = WindowControllerB.getWindowController().changeWindow("PersonalResumeEditable.fxml", btnExit);
        PersonalResumeEditableController controller = loader.getController();
        controller.receiveIntegrantToken(integrantToken);
    }
    
    @FXML
    private void requestProduction(ActionEvent event){
        FXMLLoader loader = WindowControllerB.getWindowController().changeWindow("EvidenceList.fxml", btnExit);
        EvidenceListController controller = loader.getController();
        controller.showGeneralResumeEvidences(integrantToken);
    }

    @FXML
    private void exit(ActionEvent event){
        FXMLLoader loader = WindowControllerB.getWindowController().changeWindow("Start.fxml", btnExit);
        StartController controller = loader.getController();
        controller.receiveIntegrantToken(integrantToken);
    }
    
    private void setIntegrantDataIntoInterface(){
        Integrant integrant = (Integrant) INTEGRANT_DAO.getMemberByUVmail(integrantToken.getEmailUV());
        if(integrant != null){
            this.txtFieldAditionalMail.setText(integrant.getAditionalMail());
            this.txtFieldAppoinment.setText(integrant.getAppointmentMember());
            this.txtFieldBodyAcademyKey.setText(integrant.getBodyAcademyKey());
            this.txtFieldCellPhone.setText(integrant.getCellphone());
            this.txtFieldCurp.setText(integrant.getCurp());
            this.txtFieldEducationalProgram.setText(integrant.getEducationalProgram());
            this.txtFieldEmailUv.setText(integrant.getEmailUV());
            this.txtFieldFullName.setText(integrant.getFullName());
            this.txtFieldHomePhone.setText(integrant.getHomePhone());
            this.txtFieldNationality.setText(integrant.getNationality());
            this.txtFieldParticipationType.setText(integrant.getParticipationType());
            this.passFieldIntegrantPassword.setText(integrant.getPassword());
            this.txtFieldRfc.setText(integrant.getRfc());
            this.txtFieldStaffNumber.setText( String.valueOf(integrant.getStaffNumber()));
            this.txtFieldStatus.setText(integrant.getParticipationStatus());
            this.txtFieldWorkPhone.setText(integrant.getWorkPhone());
            this.txtFieldRegistrationDate.setText(integrant.getDateOfAdmission());
            ObservableList<Schooling> list = FXCollections.observableArrayList();
            list.addAll(integrant.getSchooling());
            tvSchooling.setItems(list);
        }else{
            WindowControllerB.getWindowController().showErrorAlert(new ActionEvent(), "Error en el sistema, favor de contactar con soportr técnico");
            FXMLLoader loader = WindowControllerB.getWindowController().changeWindow("Start.fxml", btnExit);
            StartController controller = loader.getController();
            controller.receiveIntegrantToken(this.integrantToken);
        }
    }
    
    private void preprareSchoolingTable(){
        colSchoolingDegree.setCellValueFactory(new PropertyValueFactory<>("levelOfStudy"));
        colSchoolingName.setCellValueFactory(new PropertyValueFactory<>("studyName"));
        colRegistrationStudyDate.setCellValueFactory(new PropertyValueFactory<>("dateOfObteiningStudies"));
        colInstitution.setCellValueFactory(new PropertyValueFactory<>("studiesInstitution"));
        colState.setCellValueFactory(new PropertyValueFactory<>("studiesSatate"));
        colCeduleNumber.setCellValueFactory(new PropertyValueFactory<>("idProfessionalStudies"));
        colSchoolingArea.setCellValueFactory(new PropertyValueFactory<>("studyArea"));
        colDiscipline.setCellValueFactory(new PropertyValueFactory<>("studiesDiscipline"));
    }    
}
