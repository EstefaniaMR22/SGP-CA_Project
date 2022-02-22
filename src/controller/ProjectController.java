/*
* @author Johann
* @versión v1.0
* Last modification date: 17-06-2021
*/

package controller;

import java.net.URL;
import java.util.ResourceBundle;

import model.domain.Evidence;
import model.domain.Integrant;
import model.domain.Project;
import model.dataaccess.ProjectDAO;
import model.dataaccess.ReceptionWorkDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import controller.pattern.GenericWindowDriver;

public class ProjectController implements Initializable {

    @FXML
    private Button btnModify;
    @FXML
    private Button btnExit;
    @FXML
    private TextField txtFieldProjectName;
    @FXML
    private TextField txtFieldDuration;
    @FXML
    private TextField txtFieldStartDate;
    @FXML
    private TextField txtFieldEstimatedEndDate;
    @FXML
    private TextField txtFieldEndDate;
    @FXML
    private TextField txtFieldStatus;
    @FXML
    private TextArea txtAreaDescription;
    @FXML
    private TableView<Evidence> tvEvidence;
    @FXML
    private TableColumn<Evidence, String> colEvidenceType;
    @FXML
    private TableColumn<Evidence, String> colEvidenceName;
    @FXML
    private TableColumn<Evidence, String> colPublicationDate;
    @FXML
    private TableColumn<Evidence, Boolean> colImpactBA;
    @FXML
    private Label lbUserName;
    
    private final ProjectDAO PROJECT_DAO = new ProjectDAO();
    private final ReceptionWorkDAO RECEPTIONWORK_DAO = new ReceptionWorkDAO();
    private Project PROJECT = new Project();
    private Integrant token;
    
    @Override
    public void initialize(URL url, ResourceBundle rb){
        this.prepareTableFormat();
    }    
    
    public void showProject(Project projectSelected, Integrant token){
        this.token = token;
        setProjectInformation(projectSelected.getProjectName());
        this.lbUserName.setText(this.token.getFullName());
        tvEvidence.getItems().addAll(RECEPTIONWORK_DAO.getEvidencesByProjectName(projectSelected.getProjectName()));
    }
    
    private void setProjectInformation(String projectName){
        PROJECT = PROJECT_DAO.getProjectbyName(projectName);
        this.txtFieldProjectName.setText(PROJECT.getProjectName());
        this.txtFieldDuration.setText(Integer.toString(PROJECT.getDurationProjectInMonths()));
        this.txtFieldStartDate.setText(PROJECT.getStartDate());
        this.txtFieldEstimatedEndDate.setText(PROJECT.getEstimatedEndDate());
        this.txtFieldEndDate.setText(PROJECT.getEndDate());
        this.txtFieldStatus.setText(PROJECT.getStatus());
        this.txtAreaDescription.setText(PROJECT.getDescription());
    }

    @FXML
    private void modiftyProject(ActionEvent event){
        FXMLLoader loader = GenericWindowDriver.getGenericWindowDriver().changeWindow("ProjectForm.fxml", btnExit);
        ProjectFormController controller = loader.getController();
        controller.receiveProjectUpdateToken(PROJECT, token);
    }

     @FXML
    private void exit(ActionEvent event){
        FXMLLoader loader = GenericWindowDriver.getGenericWindowDriver().changeWindow("ProjectList.fxml", btnExit);
        ProjectListController controller = loader.getController();
        controller.receiveToken(this.token);
    }
    
    private void prepareTableFormat(){
        this.colEvidenceType.setCellValueFactory(new PropertyValueFactory ("evidenceType"));
        this.colEvidenceName.setCellValueFactory(new PropertyValueFactory ("evidenceTitle"));
        this.colPublicationDate.setCellValueFactory(new PropertyValueFactory ("registrationDate"));
        this.colImpactBA.setCellValueFactory(new PropertyValueFactory ("impactAB"));
    }
    
}
