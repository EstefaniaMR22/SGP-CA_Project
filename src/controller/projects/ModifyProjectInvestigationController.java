package controller.projects;

import controller.academicgroup.AddMemberController;
import controller.control.AlertController;
import controller.control.ValidatorController;
import controller.control.validator.Validator;
import controller.control.validator.ValidatorComboBoxBase;
import controller.control.validator.ValidatorTextInputControl;
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
import assets.utils.DateFormatter;
import assets.utils.SQLStates;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ModifyProjectInvestigationController extends ValidatorController implements Initializable {

    @FXML private TextField projectNameTextField;
    @FXML private TextArea descriptionTextArea;
    @FXML private DatePicker startDateDataPicker;
    @FXML private DatePicker estimatedEndDateDataPicker;
    @FXML private ComboBox<String> statusProjectCombobox;
    @FXML private ComboBox<LGAC> lgacComboBox;

    @FXML private Button updateButton;
    @FXML private Button exitButton;
    @FXML private Button changeProjectStatusButton;
    @FXML private Label systemLabel;

    private String idAcademicGroup;
    private Project updatedProject;
    private List<LGAC> listAcademicGroupLGAC;
    private ObservableList<LGAC> observableListAcademicGroupLGAC;
    private ObservableList<String> observableListStatus;

    public ModifyProjectInvestigationController(Project updatedProject, String idAcademicGroup){
        this.idAcademicGroup = idAcademicGroup;
        this.updatedProject = updatedProject;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initValidatorToTextInput();
        chargeComboBoxLGAC();
        observableListAcademicGroupLGAC = FXCollections.observableArrayList();
        observableListStatus = FXCollections.observableArrayList();
        getProjectDetails();

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
            chargeStatusCombobox();
        }catch(SQLException getProjectDetailsExeception){

            deterMinateSQLState(getProjectDetailsExeception);

        }

    }

    private void deterMinateSQLState(SQLException sqlException) {
        Logger.getLogger(AddMemberController.class.getName()).log(Level.SEVERE, null, sqlException);
        if(sqlException.getSQLState().equals(SQLStates.SQL_NO_CONNECTION.getSqlState())) {
            AlertController.getInstance().showConnectionErrorAlert();
        }
        AlertController.getInstance().showActionFailedAlert(sqlException.getLocalizedMessage());
    }

    private void chargeProjectInvestigationUpdate() {

        projectNameTextField.setText(updatedProject.getProjectName());
        descriptionTextArea.setText(updatedProject.getDescription());

        LgacDAO lgacDAO = new LgacDAO();
            try {

                int positionLGAC = getIndexLGAC(lgacDAO.getLGACById(updatedProject.getIdLGCA()).getId());
                lgacComboBox.getSelectionModel().select(positionLGAC);

            } catch (SQLException lgacSqlException) {
                deterMinateSQLState(lgacSqlException);
       }

        startDateDataPicker.setValue(DateFormatter.getLocalDateFromUtilDate(updatedProject.getStartDate()));
        estimatedEndDateDataPicker.setValue(DateFormatter.getLocalDateFromUtilDate(updatedProject.getEstimatedEndDate()));
        estimatedEndDateDataPicker.setDisable(true);

        startDateDataPicker.setDisable(true);
    }

    private void chargeComboBoxLGAC(){
        LgacDAO lgac= new LgacDAO();
        listAcademicGroupLGAC = null;
        try {

            listAcademicGroupLGAC = lgac.getAllLgacsByIdAcademicGroup(idAcademicGroup);

            observableListAcademicGroupLGAC = (ObservableList<LGAC>) listAcademicGroupLGAC;

            lgacComboBox.setItems(observableListAcademicGroupLGAC);

        } catch(SQLException chargeLGACException) {
            deterMinateSQLState(chargeLGACException);
        }

    }

    private int getIndexLGAC(int idLGAC)
    {
        int value = 0;
        if(listAcademicGroupLGAC.size()>0)
        {
            for(int i = 0; i < listAcademicGroupLGAC.size(); i++)
            {
                LGAC get = listAcademicGroupLGAC.get(i);
                if(get.getId()== idLGAC)
                {
                    return i;
                }
            }
        }
        return value;
    }

    private void chargeStatusCombobox(){
        observableListStatus.setAll("En proceso", "Completado");
        statusProjectCombobox.setItems(observableListStatus);


        if(updatedProject.getStatus().equals("En proceso")) {
            statusProjectCombobox.getSelectionModel().select(0);

        }else if(updatedProject.getStatus().equals("Completado")) {

            statusProjectCombobox.getSelectionModel().select(1);

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
                AlertController alertView = AlertController.getInstance();
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
        int positionStatus = statusProjectCombobox.getSelectionModel().getSelectedIndex();
        updateProjectInvestigation.setStatus(observableListStatus.get(positionStatus));
        if (statusProjectCombobox.getSelectionModel().getSelectedIndex()==1){
            updatedProject.setEndDate(DateFormatter.getDateFromDatepickerValue(LocalDate.from(LocalDateTime.now())));
        }else{
            updatedProject.setEndDate(null);
        }

        int positionLGAC = lgacComboBox.getSelectionModel().getSelectedIndex();
        updateProjectInvestigation.setIdLGCA(listAcademicGroupLGAC.get(positionLGAC).getId());

        updateProjectInvestigation.setEndDate(updatedProject.getEndDate());

        updateProjectInvestigation.setIdProject(updatedProject.getIdProject());

        try{

            correctUpdatedProject = projectDAO.updateProject(updateProjectInvestigation);

            if (correctUpdatedProject) {
                AlertController.getInstance().showSuccessfullRegisterAlert();
                stage.close();
            }

        }catch (SQLException addProjectInvestigationException) {

            deterMinateSQLState(addProjectInvestigationException);

        }
    }

    private boolean validateProjectName() throws SQLException {
        System.out.println("ID: " + updatedProject.getIdProject());
        return new ProjectDAO().checkProjectUpdated(projectNameTextField.getText(),updatedProject.getIdProject());
    }

    @FXML
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

        addComponentToValidator(new ValidatorTextInputControl(projectNameTextField, Validator.PATTERN_LETTERS, Validator.LENGTH_GENERAL, this), false);

        addComponentToValidator(new ValidatorTextInputControl(descriptionTextArea, Validator.PATTERN_LETTERS, Validator.LENGTH_GENERAL, this), false);

        addComponentToValidator(new ValidatorComboBoxBase(lgacComboBox, this), false);

        addComponentToValidator(new ValidatorComboBoxBase(statusProjectCombobox, this), false);

        initListenerToControls();
    }

}
