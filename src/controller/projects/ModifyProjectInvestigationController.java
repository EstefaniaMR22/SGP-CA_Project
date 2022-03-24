package controller.projects;

import controller.AlertController;
import controller.Controller;
import controller.ResponsableController;
import controller.ValidatorController;
import controller.academicgroup.AddMemberController;
import controller.validator.Validator;
import controller.validator.ValidatorComboBoxBase;
import controller.validator.ValidatorComboBoxBaseWithConstraints;
import controller.validator.ValidatorTextInputControl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.dao.LgacDAO;
import model.dao.ProjectDAO;
import model.domain.Project;
import utils.DateFormatter;
import utils.SQLStates;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ModifyProjectInvestigationController extends ValidatorController implements Initializable {

    @FXML private TextField projectNameTextField;
    @FXML private TextArea descriptionTextArea;
    @FXML private DatePicker startDateDataPicker;
    @FXML private DatePicker estimatedEndDateDataPicker;
    @FXML private Label statusProjectLabel;
    @FXML private Label lgacLabel;

    @FXML private Button updateButton;
    @FXML private Button exitButton;
    @FXML private Button endProjectStatusButton;
    @FXML private Label systemLabel;

    private Project updatedProject;

    public ModifyProjectInvestigationController(Project updatedProject) {
        this.updatedProject = updatedProject;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getProjectDetails();
        initValidatorToTextInput();
    }

    public void showStage() {
        loadFXMLFile(getClass().getResource("/view/ModifyProjectView.fxml"), this);
        stage.showAndWait();

    }

    private void getProjectDetails() {
        int idProjectDetails = updatedProject.getIdProject();
        ProjectDAO projectDAO = new ProjectDAO();
        try {
            updatedProject = projectDAO.getProjectDetails(idProjectDetails);
            chargeProjectInvestigationUpdate();

        }catch(SQLException getProjectDetailsExeception){

            deterMinateSQLState(getProjectDetailsExeception);

        }

    }

    private void deterMinateSQLState(SQLException sqlException) {
        Logger.getLogger(AddMemberController.class.getName()).log(Level.SEVERE, null, sqlException);
        if(sqlException.getSQLState().equals(SQLStates.SQL_NO_CONNECTION.getSqlState())) {
            AlertController.showConnectionErrorAlert();
        }
        AlertController.showActionFailedAlert(sqlException.getLocalizedMessage());
    }

    private void chargeProjectInvestigationUpdate() {

        projectNameTextField.setText(updatedProject.getProjectName());
        statusProjectLabel.setText(updatedProject.getStatus());
        descriptionTextArea.setText(updatedProject.getDescription());

        LgacDAO lgacDAO = new LgacDAO();
            try {

                lgacLabel.setText(lgacDAO.getLGACById(updatedProject.getIdLGCA()).toString());
            } catch (SQLException lgacSqlException) {
                deterMinateSQLState(lgacSqlException);
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
        try {
            if(validateInputs()) {
                try {


                    if (!validateProjectName()) {
                        updateProjectInvestigation();

                    } else {
                        systemLabel.setText("¡Al parecer ya existe un proyecto de investigación con \n" +
                                " el mismo nombre,de favor ingrese un nombre distinto!");
                    }
                }catch (SQLException validateProjectName){
                    deterMinateSQLState(validateProjectName);
                }

            }else {
                AlertController alertView = new AlertController();
                alertView.showActionFailedAlert("Algunos datos ingresados son inválidos, por favor verifíquelos");
            }
        } catch (Exception exception) {

            systemLabel.setText(exception.getLocalizedMessage());
        }

    }

    private void updateProjectInvestigation(){
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

            deterMinateSQLState(addProjectInvestigationException);

        }
    }

    private boolean validateProjectName() throws SQLException {
        return new ProjectDAO().checkProject(projectNameTextField.getText());
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

    private void initValidatorToTextInput() {

        addComponentToValidator(new ValidatorTextInputControl(projectNameTextField, Validator.PATTERN_LETTERS, Validator.LENGTH_GENERAL, this), false);

        addComponentToValidator(new ValidatorTextInputControl(descriptionTextArea, Validator.PATTERN_LETTERS, Validator.LENGTH_GENERAL, this), false);

        initListenerToControls();
    }

}
