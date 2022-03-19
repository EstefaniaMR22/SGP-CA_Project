package controller.projects;

import controller.AlertController;
import controller.Controller;
import controller.ResponsableController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.dao.LgacDAO;
import model.dao.ProjectDAO;
import model.domain.Project;
import utils.DateFormatter;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ModifyProjectInvestigationController extends Controller implements Initializable {

    @FXML private TextField projectNameTextField;
    @FXML private TextArea descriptionTextArea;
    @FXML private DatePicker startDateDataPicker;
    @FXML private DatePicker estimatedEndDateDataPicker;
    @FXML private Label statusProjectLabel;
    @FXML private Label lgacLabel;

    @FXML private Button updateButton;
    @FXML private Button exitButton;
    @FXML private Button endProjectStatusButton;

    private Project updatedProject;

    public ModifyProjectInvestigationController(Project updatedProject) {
        this.updatedProject = updatedProject;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void showStage() {
        loadFXMLFile(getClass().getResource("/view/ModifyProjectView.fxml"), this);
        getProjectDetails();
        stage.showAndWait();

    }

    private void getProjectDetails() {
        int idProjectDetails = updatedProject.getIdProject();
        ProjectDAO projectDAO = new ProjectDAO();
        try {
            updatedProject = projectDAO.getProjectDetails(idProjectDetails);
            chargeProjectInvestigationUpdate();

        }catch(SQLException getProjectDetailsExeception){
            Logger.getLogger(ResponsableController.class.getName()).log(Level.SEVERE, null, getProjectDetailsExeception);
            AlertController alertView = new AlertController();
            alertView.showActionFailedAlert(" No se pudo obtener datos del proyecto. " +
                    "Causa: " + getProjectDetailsExeception);

        }

    }

    private void chargeProjectInvestigationUpdate() {

        projectNameTextField.setText(updatedProject.getProjectName());
        statusProjectLabel.setText(updatedProject.getStatus());
        descriptionTextArea.setText(updatedProject.getDescription());

        LgacDAO lgacDAO = new LgacDAO();
        try {
            lgacLabel.setText(lgacDAO.getLGAC(updatedProject.getIdLGCA()));
        } catch (SQLException lgacSqlException) {
            AlertController alertView = new AlertController();
            alertView.showActionFailedAlert(" No se pudo obtener lgac." +
                    " Causa: " + lgacSqlException);
        }

        startDateDataPicker.setValue(DateFormatter.getLocalDateFromUtilDate(updatedProject.getStartDate()));
        estimatedEndDateDataPicker.setValue(DateFormatter.getLocalDateFromUtilDate(updatedProject.getEstimatedEndDate()));
        estimatedEndDateDataPicker.setDisable(true);

        startDateDataPicker.setDisable(true);
    }

    @FXML
    public void endProjectStatusOnAction(ActionEvent actionEvent) {
        AlertController alertView = new AlertController();

        if(alertView.showConfirmationAlert()){
            statusProjectLabel.setText("Completado");
            updatedProject.setEndDate(DateFormatter.getDateFromDatepickerValue(LocalDate.from(LocalDateTime.now())));
        }else {
            statusProjectLabel.setText("En proceso");
            updatedProject.setEndDate(null);
        }

    }

    @FXML
    public void updateProjectInvestigationOnAction(ActionEvent actionEvent){
        boolean correctUpdatedProject = false;
        ProjectDAO projectDAO = new ProjectDAO();
        Project updateProjectInvestigation = new Project();

        updateProjectInvestigation.setProjectName(projectNameTextField.getText());
        updateProjectInvestigation.setDescription(descriptionTextArea.getText());
        updateProjectInvestigation.setStatus(statusProjectLabel.getText());

        updateProjectInvestigation.setEndDate(updatedProject.getEndDate());

        updateProjectInvestigation.setIdProject(updatedProject.getIdProject());

        try{

            correctUpdatedProject = projectDAO.updateProject(updateProjectInvestigation);

            if (correctUpdatedProject) {
                AlertController.showSuccessfullRegisterAlert();
                stage.close();
            }

        }catch (SQLException addProjectInvestigationException) {
            AlertController alertView = new AlertController();
            alertView.showActionFailedAlert(" No se pudo 'actualizar' el proyecto de investigación." +
                    " Causa: " + addProjectInvestigationException);

        }



    }

    @FXML
    public void returnViewOnAction(ActionEvent actionEvent) {
        try{
            stage.close();
        }catch(Exception returnViewOnActionExeception){
            AlertController alertView = new AlertController();
            alertView.showActionFailedAlert(" No se pudo volver a la ventana anterior." +
                    "Causa: " + returnViewOnActionExeception);

        }
    }

}
