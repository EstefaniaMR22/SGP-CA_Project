/*
 * @author Estefanía 
 * @versión v1.0
 * Last modification date: 17-06-2021
 */

package controller.old;

import java.net.URL;
import java.util.ResourceBundle;

import controller.old.pattern.EvidenceWindow;
import model.old.domain.Evidence;
import model.old.domain.Integrant;
import model.old.domain.Prototype;
import model.old.dataaccess.PrototypeDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import controller.old.pattern.DialogBox;

public class PrototypeController implements Initializable, EvidenceWindow {

    @FXML
    private Label lbUsername;
    @FXML
    private HBox hbOptions;
    @FXML
    private Button btnUpdateEvidence;
    @FXML
    private Button btnRemoveEvidence;
    @FXML
    private Button btnCloseWindowEvidenceRequest;
    @FXML
    private CheckBox chBoxImpactAB;
    @FXML
    private Label lbTypeEvidence;
    @FXML
    private TextField txtFieldEvidenceTittle;
    @FXML
    private TextField txtFieldPublicationCountry;
    @FXML
    private TextField txtFieldPublisherDate;
    @FXML
    private TextField txtFieldInvestigationProject;
    @FXML
    private TextField txtFieldStudyDegree;
    @FXML
    private ListView<String> lvIntegrants;
    @FXML
    private ListView<String> lvCollaborators;
    @FXML
    private ListView<String> lvStudents;
    @FXML
    private TextArea txtAreaCharacteristics;
    @FXML
    private Button btnDownloadDocument;
    @FXML
    private Label lbDocumentName;

    
    private final PrototypeDAO PROTOTYPE_DAO = new PrototypeDAO();
    private Integrant token;
    private Prototype prototype;
    
    @Override
    public void initialize(URL url, ResourceBundle rb){
        hbOptions.getChildren().removeAll(btnRemoveEvidence, btnUpdateEvidence);
    }    
    
    public void showPrototypeByUrl(String url, Integrant token){
        this.token = token;
        this.lbUsername.setText(token.getFullName());
        this.prototype = (Prototype) PROTOTYPE_DAO.getEvidenceByUrl(url);
        if(this.prototype != null){
            this.setPrototypeDataIntoInterface();
            this.grantPermissions();
        }
    }

    @FXML
    private void updateEvidence(ActionEvent event){
        FXMLLoader loader = WindowControllerB.getWindowController().changeWindow("EvidenceEdit.fxml", btnCloseWindowEvidenceRequest);
        EvidenceEditController controller = loader.getController();
        controller.receivePrototypeAndToken(this.prototype,token);
    }

    @FXML
    private void removeEvidence(ActionEvent event){
    }

    @FXML
    private void closeWindowEvidenceRequest(ActionEvent event) {
        FXMLLoader loader = WindowControllerB.getWindowController().changeWindow("EvidenceList.fxml", btnCloseWindowEvidenceRequest);
        EvidenceListController controller = loader.getController();
        controller.showGeneralResumeEvidences(token);
    }

    @FXML
    private void downloadDocument(ActionEvent event){
        DialogBox dialogBox = new DialogBox((Stage)((Node)event.getSource()).getScene().getWindow());
        if(dialogBox.openDialogDirectorySelector(this.prototype.getUrlFile())){
            WindowControllerB.getWindowController().showConfirmationAlert(event, "Archivo descargado correctamente");
        }else{
            WindowControllerB.getWindowController().showErrorAlert(event, "El prototipo no fue descargado");
        }
    }
    
    private void setPrototypeDataIntoInterface(){
        if(this.prototype.getImpactAB()){
            this.chBoxImpactAB.setSelected(true);
        }
        lvStudents.getItems().addAll(this.prototype.getStudents());
        this.prototype.getIntegrants().forEach(integrant -> this.lvIntegrants.getItems().add(integrant.getFullName()));
        this.prototype.getCollaborators().forEach(collaborator -> this.lvCollaborators.getItems().add(collaborator.getFullName()));
        this.txtAreaCharacteristics.setText(this.prototype.getFeatures());
        this.lbTypeEvidence.setText(this.prototype.getEvidenceType());
        this.txtFieldEvidenceTittle.setText(this.prototype.getEvidenceTitle());
        this.txtFieldPublisherDate.setText(this.prototype.getPublicationDate());
        this.txtFieldPublicationCountry.setText(this.prototype.getCountry());
        this.txtFieldInvestigationProject.setText(this.prototype.getProjectName());
        this.txtFieldStudyDegree.setText(this.prototype.getStudyDegree());
        this.lbDocumentName.setText(this.prototype.getUrlFile());
    }
    
    private void grantPermissions(){
        if(this.token.getRfc().equalsIgnoreCase(this.prototype.getRegistrationResponsible())){
            hbOptions.getChildren().addAll(btnUpdateEvidence);
        }
    }
    
    @Override
    public String toString(){
        return "Prototipo";
    }

    @Override
    public void createWindowAccordingEvidenceType(Evidence evidence, Node graphicElement, Integrant token) {
        if(this.toString().equalsIgnoreCase(evidence.getEvidenceType())){
            FXMLLoader loader = WindowControllerB.getWindowController().changeWindow("Prototype.fxml", graphicElement);
            PrototypeController controller = loader.getController();
            controller.showPrototypeByUrl(evidence.getUrlFile(), token);
        }
    }
    
}
