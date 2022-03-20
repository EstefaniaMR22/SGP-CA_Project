package controller.projects;

import controller.AlertController;
import controller.Controller;
import controller.IntegrantController;
import controller.ResponsableController;
import controller.academicgroup.ModifyMemberController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import model.dao.LgacDAO;
import model.dao.MiembroDAO;
import model.dao.ProjectDAO;
import model.domain.*;
import utils.DateFormatter;

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
    @FXML private TableView<Evidence> evidencesTableView;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        evidencesTableView.setVisible(false);
        evidencesTableView.setVisible(true);
        getProjectDetails();
    }

    public void showStage() {
        loadFXMLFile(getClass().getResource("/view/ConsultProjectView.fxml"), this);
        stage.showAndWait();
    }

    public ConsultProjectController(Project projectSelected) {
        this.projectSelected = projectSelected;
    }


    public void returnViewOnAction(ActionEvent actionEvent) {
        try{
            stage.close();

        }catch(Exception returnViewException) {
            Logger.getLogger(AddProjectsInvestigationController.class.getName()).log(Level.SEVERE, null, returnViewException);
            AlertController alertView = new AlertController();
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
            lgacLabel.setText(lgacDAO.getLGAC(projectSelected.getIdLGCA()));
        } catch (SQLException lgacSqlException) {
            AlertController alertView = new AlertController();
            alertView.showActionFailedAlert(" No se pudo obtener lgac." +
                    " Causa: " + lgacSqlException);
        }
        descriptionLabel.setText(projectSelected.getDescription());
    }

    private void getProjectDetails() {
        int idProjectDetails = projectSelected.getIdProject();
        ProjectDAO projectDAO = new ProjectDAO();
        try {
            projectSelected = projectDAO.getProjectDetails(idProjectDetails);
            setProjectDataToTextField();

        }catch(SQLException getProjectDetailsExeception){
            Logger.getLogger(ResponsableController.class.getName()).log(Level.SEVERE, null, getProjectDetailsExeception);
            AlertController alertView = new AlertController();
            alertView.showActionFailedAlert(" No se pudo obtener datos del proyecto. " +
                    "Causa: " + getProjectDetailsExeception);

        }

    }

}
