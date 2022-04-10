package controller.projects;

import controller.control.AlertController;
import controller.control.ValidatorController;
import controller.academicgroup.AddMemberController;
import controller.control.validator.Validator;
import controller.control.validator.ValidatorComboBoxBase;
import controller.control.validator.ValidatorComboBoxBaseWithConstraints;
import controller.control.validator.ValidatorTextInputControl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.dao.ProjectDAO;
import model.domain.LGAC;
import model.domain.Project;
import assets.utils.DateFormatter;
import assets.utils.SQLStates;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AddProjectsInvestigationController extends ValidatorController implements Initializable {

    @FXML private TextField projectNameTextField;
    @FXML private TextArea descriptionTextArea;
    @FXML private DatePicker startDateDataPicker;
    @FXML private DatePicker estimatedEndDateDataPicker;
    @FXML private Label statusProjectLabel;
    @FXML private ComboBox<LGAC> lgacComboBox;
    @FXML private Button newProjectButton;
    @FXML private Button exitButton;
    @FXML private Label systemLabel;

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
        initValidatorToTextInput();
    }

    @FXML
    void addProjectInvestigationOnAction(ActionEvent actionEvent) {

        try {
            if(validateInputs()) {
                if(!validateProjectName()) {
                        addProjectInvestigation();

                } else {
                    systemLabel.setText("¡Al parecer ya existe un proyecto de investigación con \n"+
                           " el mismo nombre,de favor ingrese un nombre distinto!");
                }

            }else {
                AlertController alertView = AlertController.getInstance();
                alertView.showActionFailedAlert("Algunos datos ingresados son inválidos, por favor verifíquelos");
            }
        } catch (Exception e) {
            systemLabel.setText(e.getLocalizedMessage());
        }

    }

    private boolean validateProjectName() throws SQLException {
        return new ProjectDAO().checkProject(projectNameTextField.getText());
    }

    private void addProjectInvestigation(){
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
                AlertController.getInstance().showSuccessfullRegisterAlert();
                stage.close();
            }

        } catch (SQLException addProjectInvestigationException) {

            deterMinateSQLState(addProjectInvestigationException);

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
            /*
            FIX THIS METHOD
             */

            //listProjectLGAC = new LgacDAO().getAlllgacs();
            ObservableList<LGAC> observableLististProjectLGAC = FXCollections.observableArrayList(listProjectLGAC);
            lgacComboBox.setItems(observableLististProjectLGAC);
            throw new SQLException("FIX THIS METHOD");

        } catch(SQLException chargeLGACException) {
            deterMinateSQLState(chargeLGACException);
        }

    }

    private void deterMinateSQLState(SQLException sqlException) {
        Logger.getLogger(AddMemberController.class.getName()).log(Level.SEVERE, null, sqlException);
        if(sqlException.getSQLState().equals(SQLStates.SQL_NO_CONNECTION.getSqlState())) {
            AlertController.getInstance().showConnectionErrorAlert();
        }
        AlertController.getInstance().showActionFailedAlert(sqlException.getLocalizedMessage());
    }

    public void returnViewOnAction(ActionEvent actionEvent) {
        try{
            stage.close();
        }catch(Exception returnViewOnActionExeception){
            AlertController alertView = AlertController.getInstance();
            alertView.showActionFailedAlert(" No se pudo volver a la ventana anterior." +
                    "Causa: " + returnViewOnActionExeception);

        }
    }

    private void initValidatorToTextInput() {
        Function<Object, Boolean> validateEstimatedEndDate = a -> {
            return DateFormatter.compareActualDateToSelectedDate((LocalDate) a) == 1;
        };

        addComponentToValidator(new ValidatorTextInputControl(projectNameTextField, Validator.PATTERN_LETTERS, Validator.LENGTH_GENERAL, this), false);

        addComponentToValidator(new ValidatorTextInputControl(descriptionTextArea, Validator.PATTERN_LETTERS, Validator.LENGTH_GENERAL, this), false);

        addComponentToValidator(new ValidatorComboBoxBase(lgacComboBox, this), false);

        addComponentToValidator(new ValidatorComboBoxBaseWithConstraints(estimatedEndDateDataPicker, this, validateEstimatedEndDate), false);

        initListenerToControls();
    }

}

