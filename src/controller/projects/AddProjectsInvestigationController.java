package controller.projects;

import controller.AlertController;
import controller.Controller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.dao.LgacDAO;
import model.dao.ProjectDAO;
import model.domain.LGAC;
import model.domain.Project;
import utils.DateFormatter;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AddProjectsInvestigationController extends Controller implements Initializable {

    @FXML private TextField projectNameTextField;
    @FXML private TextArea descriptionTextArea;
    @FXML private DatePicker startDateDataPicker;
    @FXML private DatePicker estimatedEndDateDataPicker;
    @FXML private Label statusProjectLabel;
    @FXML private ComboBox<LGAC> lgacComboBox;
    @FXML private Button newProjectButton;
    @FXML private Button exitButton;

    private List<LGAC> listProjectLGAC;

    public void showStage() {
        loadFXMLFile(getClass().getResource("/view/AddProjectView.fxml"), this);
        stage.showAndWait();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        chargeComboBoxLGAC();
        startDateDataPicker.setValue(LocalDate.from(LocalDateTime.now()));
        startDateDataPicker.setDisable(true);
        statusProjectLabel.setText("En proceso");
    }

    @FXML
    void addProjectInvestigationOnAction(ActionEvent actionEvent) {

        ProjectDAO projectDAO = new ProjectDAO();

        Project addProjectInvestigation = new Project();
         addProjectInvestigation.setProjectName(projectNameTextField.getText());
         addProjectInvestigation.setDescription(descriptionTextArea.getText());
         addProjectInvestigation.setStatus(statusProjectLabel.getText());

         int positionLGAC = lgacComboBox.getSelectionModel().getSelectedIndex();
         addProjectInvestigation.setIdLGCA(listProjectLGAC.get(positionLGAC).getId());
         addProjectInvestigation.setStartDate(DateFormatter.getDateFromDatepickerValue(startDateDataPicker.getValue()));
         addProjectInvestigation.setEstimatedEndDate(DateFormatter.getDateFromDatepickerValue(estimatedEndDateDataPicker.getValue()));
         addProjectInvestigation.setDurationProjectInMonths(calculateMonths());

         try {
             boolean correctAddProject = false;

             correctAddProject = projectDAO.addProject(addProjectInvestigation);
             if (correctAddProject == true) {
                 AlertController.showSuccessfullRegisterAlert();
                 stage.close();
             }

         } catch (Exception addProjectInvestigationException) {
             Logger.getLogger(ModifyProjectInvestigationController.class.getName()).log(Level.SEVERE, null, addProjectInvestigationException);
             AlertController alertView = new AlertController();
             alertView.showActionFailedAlert(" No se pudo guardar el proyecto de investigación." +
                     " Causa: " + addProjectInvestigationException);

         }

    }

    private int calculateMonths(){
        int totalMonths = 0;
        if(startDateDataPicker!= null && estimatedEndDateDataPicker!= null){
            Period diferrence = Period.between(startDateDataPicker.getValue(),estimatedEndDateDataPicker.getValue());
            totalMonths = diferrence.getMonths();
        }
        return totalMonths;
    }

    private void chargeComboBoxLGAC(){
        listProjectLGAC = null;
        try {
            listProjectLGAC = new LgacDAO().getAlllgacs();
            ObservableList<LGAC> observableLististProjectLGAC = FXCollections.observableArrayList(listProjectLGAC);
            lgacComboBox.setItems(observableLististProjectLGAC);

        } catch(SQLException chargeLGACException) {
            Logger.getLogger(ModifyProjectInvestigationController.class.getName()).log(Level.SEVERE, null, chargeLGACException);
            AlertController alertView = new AlertController();
            alertView.showActionFailedAlert(" No se pudo cargar las LGAC al combobox. Causa: " + chargeLGACException);

        }

    }

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

