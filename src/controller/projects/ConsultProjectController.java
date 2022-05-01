package controller.projects;

import controller.control.AlertController;
import controller.control.Controller;
import controller.academicgroup.AddMemberController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import model.dao.LgacDAO;
import model.dao.ProjectDAO;
import model.domain.*;
import assets.utils.DateFormatter;
import assets.utils.SQLStates;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConsultProjectController extends Controller implements Initializable {
    private Project projectSelected;
    @FXML private Label projectNameLabel;
    @FXML private Label statusProjectLabel;
    @FXML private Label estimatedEndDateLabel;
    @FXML private Label startDateLabel;
    @FXML private Label endDateLabel;
    @FXML private Label lgacLabel;
    @FXML private Label descriptionLabel;
    @FXML private Label durationInMonthsProject;
    @FXML private Button modifyButton;
    @FXML private Button exitButton;
    private String idAcademicGroup;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getProjectDetails();
    }

    public void showStage() {
        loadFXMLFile(getClass().getResource("/view/ConsultProjectView.fxml"), this);
        stage.showAndWait();
    }

    public ConsultProjectController(Project projectSelected, String idAcademicGroup){
        this.idAcademicGroup = idAcademicGroup;
        this.projectSelected = projectSelected;
    }


    @FXML
    public void modifyProjectOnAction(ActionEvent actionEvent) {
        try {
            ModifyProjectInvestigationController updateProjectInvestigationController = new ModifyProjectInvestigationController(projectSelected, idAcademicGroup);
            updateProjectInvestigationController.showStage();

        } catch (Exception addProjectInvestigationException) {
            Logger.getLogger(ProjectsInvestigationController.class.getName()).log(Level.SEVERE, null, addProjectInvestigationException);

            AlertController alertView = AlertController.getInstance();
            alertView.showActionFailedAlert(" No se pudo abrir la ventana " +
                    "ModifyProyectInvestigation. Causa: " + addProjectInvestigationException);

        }

    }

    @FXML
    public void returnViewOnAction(ActionEvent actionEvent) {
        try{
            stage.close();

        }catch(Exception returnViewException) {
            Logger.getLogger(AddProjectsInvestigationController.class.getName()).log(Level.SEVERE, null, returnViewException);
            AlertController alertView = AlertController.getInstance();
            alertView.showActionFailedAlert(" No se pudo volver a la ventana anterior." +
                    " Causa: " + returnViewException);

        }
    }

    private void setProjectDataToTextField() {
        projectNameLabel.setText(projectSelected.getProjectName());
        statusProjectLabel.setText(projectSelected.getStatus());
        startDateLabel.setText(DateFormatter.getParseDate(projectSelected.getStartDate()));
        estimatedEndDateLabel.setText(DateFormatter.getParseDate(projectSelected.getEstimatedEndDate()));
        endDateLabel.setText(projectSelected.getEndDateString());
        durationInMonthsProject.setText(projectSelected.getDurationProjectInMonths() + " meses");
        LgacDAO lgacDAO = new LgacDAO();
     try {
         lgacLabel.setText(lgacDAO.getLGACById(projectSelected.getIdLGCA()).toString());

     } catch (SQLException lgacSqlException) {

         deterMinateSQLState(lgacSqlException);

      }
        descriptionLabel.setText(projectSelected.getDescription());
    }

    private void deterMinateSQLState(SQLException sqlException) {
        Logger.getLogger(AddMemberController.class.getName()).log(Level.SEVERE, null, sqlException);
        if(sqlException.getSQLState().equals(SQLStates.SQL_NO_CONNECTION.getSqlState())) {
            AlertController.getInstance().showConnectionErrorAlert();
        }
        AlertController.getInstance().showActionFailedAlert(sqlException.getLocalizedMessage());
    }

    private void getProjectDetails() {
        int idProjectDetails = projectSelected.getIdProject();
        ProjectDAO projectDAO = new ProjectDAO();
        try {
            projectSelected = projectDAO.getProjectDetails(idProjectDetails);
            setProjectDataToTextField();

        }catch(SQLException getProjectDetailsExeception){

            deterMinateSQLState(getProjectDetailsExeception);

        }

    }

}
