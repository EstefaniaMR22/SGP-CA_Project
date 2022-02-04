/*
* @author Johann
* @versión v1.0
* Last modification date: 17-06-2021
*/

package sgp.ca.demodao;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.stage.Stage;
import sgp.ca.businesslogic.ReceptionWorkDAO;
import sgp.ca.domain.Collaborator;
import sgp.ca.domain.Evidence;
import sgp.ca.domain.Integrant;
import sgp.ca.domain.ReceptionWork;

public class ReceptionWorkController implements Initializable, EvidenceWindow {

    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnExit;
    @FXML
    private CheckBox chBoxImpactBA;
    @FXML
    private TextField txtFieldReceptionWorkName;
    @FXML
    private TextField txtFieldCountry;
    @FXML
    private TextField txtFieldProject;
    @FXML
    private TextField txtFieldPublicationDate;
    @FXML
    private ListView<String> lvIntegrant;
    @FXML
    private ListView<String> lvCollaborator;
    @FXML
    private TextField txtFieldEstimatedDurationMonth;
    @FXML
    private TextField txtFieldStatus;
    @FXML
    private TextField txtFieldModality;
    @FXML
    private TextArea txtAreaDescription;
    @FXML
    private Button btnFile;
    @FXML
    private ListView<String> lvStudent;
    @FXML
    private ListView<String> lvRequirements;
    @FXML
    private Label lbUserName;
    
    private final ReceptionWorkDAO RECEPTIONWORK_DAO = new ReceptionWorkDAO();
    private Integrant token;
    private ReceptionWork receptionWork;
    private String urlFile = "";
        
    @Override
    public void initialize(URL url, ResourceBundle rb){
    }    

    public void receiveReceptionWork(String url, Integrant token){  
        this.token = token;
        this.urlFile = url;
        this.lbUserName.setText(token.getFullName());
        setReceptionWorkInformation(url);
    }
    
    private void setReceptionWorkInformation(String receptionWorkUrl){
        receptionWork = RECEPTIONWORK_DAO.getEvidenceByUrl(receptionWorkUrl);
        this.chBoxImpactBA.setDisable(true);
        this.chBoxImpactBA.setSelected(receptionWork.getImpactAB());
        this.txtFieldReceptionWorkName.setText(receptionWork.getEvidenceTitle());
        this.txtFieldCountry.setText(receptionWork.getCountry());
        this.txtFieldPublicationDate.setText(receptionWork.getPublicationDate());
        this.txtFieldProject.setText(receptionWork.getProjectName());
        this.lvIntegrant.setItems(makeItemsIntegrantName(receptionWork.getIntegrants()));
        this.lvCollaborator.setItems(makeItemsCollaboratorName(receptionWork.getCollaborators()));
        this.lvStudent.setItems(makeItemsStudent(receptionWork.getStudents()));
        this.txtFieldEstimatedDurationMonth.setText(Integer.toString(receptionWork.getEstimatedDurationInMonths()));
        this.txtFieldStatus.setText(receptionWork.getStatus());
        this.txtFieldModality.setText(receptionWork.getModality());
        this.txtAreaDescription.setText(receptionWork.getDescription());
        this.lvRequirements.setItems(makeItemsRequirements(receptionWork.getRequirements()));
    }
    
    @FXML
    private void exit(ActionEvent event){
        FXMLLoader loader = GenericWindowDriver.getGenericWindowDriver().changeWindow("EvidenceList.fxml", txtFieldReceptionWorkName);          
        EvidenceListController controller = loader.getController();
        controller.showGeneralResumeEvidences(token);
    }
    
    private ObservableList<String> makeItemsIntegrantName(List<Integrant> integrantList){
        ObservableList<String> itemsIntegrant= FXCollections.observableArrayList();
        for(Integrant integrant : integrantList){
            itemsIntegrant.add(integrant.getFullName());
        }
        return itemsIntegrant;
    }
    
    private ObservableList<String> makeItemsCollaboratorName(List<Collaborator> collaboratorList){
        ObservableList<String> itemsCollaborator= FXCollections.observableArrayList();
        for(Collaborator collaborator : collaboratorList){
            itemsCollaborator.add(collaborator.getFullName());
        }
        return itemsCollaborator;
    }
    
    private ObservableList<String> makeItemsStudent(List<String> studentList){
        ObservableList<String> itemsStudent= FXCollections.observableArrayList();
        for(String student : studentList){
            itemsStudent.add(student);
        }
        return itemsStudent;
    }
    
    private ObservableList<String> makeItemsRequirements(List<String> requirementstList){
        ObservableList<String> itemsRequirements= FXCollections.observableArrayList();
        for(String requirements : requirementstList){
            itemsRequirements.add(requirements);
        }
        return itemsRequirements;
    }

    @FXML
    private void updateReceptionWork(ActionEvent event){
        FXMLLoader loader = GenericWindowDriver.getGenericWindowDriver().changeWindow("ReceptionWorkForm.fxml", txtFieldReceptionWorkName);
        ReceptionWorkFormController controller = loader.getController();
        controller.receiveReceptionWorkUpdateToken(receptionWork, token);
    }
    
    @Override
    public String toString(){
        return "Trabajo Recepcional";
    }

    @Override
    public void createWindowAccordingEvidenceType(Evidence evidence, Node graphicElement, Integrant token){
        if(this.toString().equalsIgnoreCase(evidence.getEvidenceType())){
            FXMLLoader loader = GenericWindowDriver.getGenericWindowDriver().changeWindow("ReceptionWork.fxml", graphicElement);
            ReceptionWorkController controller = loader.getController();
            controller.receiveReceptionWork(evidence.getUrlFile(), token);
        }
    }

    @FXML
    private void downloadFile(ActionEvent event){
        DialogBox dialogBox = new DialogBox((Stage)((Node)event.getSource()).getScene().getWindow());
        if(dialogBox.openDialogDirectorySelector(this.urlFile)){
            GenericWindowDriver.getGenericWindowDriver().showConfirmationAlert(event, "Archivo descargado correctamente");
        }else{
            GenericWindowDriver.getGenericWindowDriver().showErrorAlert(event, "El documento no fue descargado");
        }
    }
}
