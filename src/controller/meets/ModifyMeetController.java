package controller.meets;

import assets.utils.DateFormatter;
import assets.utils.SQLStates;
import controller.academicgroup.AddMemberController;
import controller.control.AlertController;
import controller.control.ValidatorController;
import controller.control.validator.Validator;
import controller.control.validator.ValidatorComboBoxBase;
import controller.control.validator.ValidatorComboBoxBaseWithConstraints;
import controller.control.validator.ValidatorTextInputControl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.dao.MeetDAO;
import model.dao.ProjectDAO;
import model.domain.Meet;
import model.domain.Member;
import model.domain.Project;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ModifyMeetController extends ValidatorController implements Initializable {

    @FXML
    private TextField bussinesTextField;
    @FXML
    private TextField hourTextField;
    @FXML
    private TextField minutesTextField;
    @FXML
    private ComboBox<Project> projectsCombobox;
    @FXML
    private TableView<Member> integrantsTableView;
    @FXML
    private TableColumn<Member, String> integrantColumn;
    @FXML
    private TableColumn<Member, String> lastName1Column;
    @FXML
    private TableColumn<Member, String> lastName2Column;
    @FXML
    private DatePicker meetDateDataPicker;
    @FXML
    private TextField leaderTextField;
    @FXML
    private TextField secretaryTextField;
    @FXML
    private TextField timerTextField;
    @FXML
    private Label systemLabel;
    @FXML
    private Button exitButton;
    @FXML
    private Button modifyMeetButton;

    private String idAcademicGroup;
    private Meet meetUpdated;
    private ObservableList<Project> projectsObservableList;
    private ObservableList<Member> memberObservableList;
    private Member leaderMeet;
    private Member secretaryMeet;
    private Member timerMeet;

    public ModifyMeetController(Meet meetUpdated, String idAcademicGroup) {
        this.meetUpdated = meetUpdated;
        this.idAcademicGroup = idAcademicGroup;
    }

    public void showStage() {
        loadFXMLFile(getClass().getResource("/view/ModifyMeetView.fxml"), this);
        stage.showAndWait();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initValidatorToTextInput();
        projectsObservableList = FXCollections.observableArrayList();
        chargeProjectsComboBox();
        projectsCombobox.setDisable(true);
        getMeetDetails();
        setTableComponents();
        leaderTextField.setDisable(true);
        secretaryTextField.setDisable(true);
        timerTextField.setDisable(true);

    }

    private void setTableComponents() {
        memberObservableList = FXCollections.observableArrayList();
        integrantColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        lastName1Column.setCellValueFactory(new PropertyValueFactory<>("maternalLastname"));
        lastName2Column.setCellValueFactory(new PropertyValueFactory<>("paternalLastname"));

        chargeMembers();
    }

    private void getMeetDetails() {

        MeetDAO meetDAO = new MeetDAO();
        try {
            meetUpdated = meetDAO.getMeetDetails(meetUpdated.getIdMeet());
            chargeMeetUpdate();
        }catch(SQLException getProjectDetailsExeception){

            deterMinateSQLState(getProjectDetailsExeception);

        }

    }

    private void chargeMeetUpdate() {

        bussinesTextField.setText(meetUpdated.getAsunto());

        hourTextField.setText(meetUpdated.getHour().substring(0,2));
        minutesTextField.setText(meetUpdated.getHour().substring(3,5));

        int positionProject = getIndexProjects(meetUpdated.getIdProject());
        projectsCombobox.getSelectionModel().select(positionProject);

        meetDateDataPicker.setValue(DateFormatter.getLocalDateFromUtilDate(meetUpdated.getDateMeet()));
        leaderTextField.setText(meetUpdated.getLeader());

        secretaryTextField.setText(meetUpdated.getSecretary());

        timerTextField.setText(meetUpdated.getTimer());

    }


    private void chargeMembers(){

        integrantsTableView.setItems(meetUpdated.getAsistents());

    }

    private void chargeProjectsComboBox(){
        ProjectDAO projectDAO= new ProjectDAO();

        try {

            projectsObservableList = projectDAO.getProjectList(idAcademicGroup);

            projectsCombobox.setItems(projectsObservableList);

        } catch(SQLException chargeLGACException) {
            deterMinateSQLState(chargeLGACException);
        }

    }

    private int getIndexProjects(int idProject)
    {
        int value = 0;
        if(projectsObservableList.size()>0)
        {
            for(int i = 0; i < projectsObservableList.size(); i++)
            {
                Project get = projectsObservableList.get(i);
                if(get.getIdProject()== idProject)
                {
                    return i;
                }
            }
        }
        return value;
    }

    private void deterMinateSQLState(SQLException sqlException) {
        Logger.getLogger(AddMemberController.class.getName()).log(Level.SEVERE, null, sqlException);
        if(sqlException.getSQLState().equals(SQLStates.SQL_NO_CONNECTION.getSqlState())) {
            AlertController.getInstance().showConnectionErrorAlert();
        }
        AlertController.getInstance().showActionFailedAlert(sqlException.getLocalizedMessage());
    }

    @FXML
    void modifyMeetOnAction(ActionEvent actionEvent) {
        try {
            if(validateInputs()) {
                if(!validateMeet()) {
                    modifyMeet();

                } else {
                    systemLabel.setText("¡Al parecer ya existe una reunión en la misma fecha y hora, \n"+
                            " ingrese una fecha y hora distinta");
                }

            }else {
                AlertController alertView = AlertController.getInstance();
                alertView.showActionFailedAlert("Algunos datos ingresados son inválidos, por favor verifíquelos");
            }
        } catch (Exception e) {
            systemLabel.setText(e.getLocalizedMessage());
        }
    }

    private boolean validateMeet() throws SQLException {
        return new MeetDAO().checkMeet(DateFormatter.getDateFromDatepickerValue(meetDateDataPicker.getValue()), hourTextField.getText() + ":" + minutesTextField.getText());
    }

    private void modifyMeet(){
        MeetDAO meetDAO = new MeetDAO();

        meetUpdated.setAsunto(bussinesTextField.getText());

        meetUpdated.setDateMeet(DateFormatter.getDateFromDatepickerValue(meetDateDataPicker.getValue()));
        meetUpdated.setHour(hourTextField.getText() + ":" + minutesTextField.getText());

        try {
            boolean correctAddMeet = false;

            correctAddMeet = meetDAO.updateMeet(meetUpdated);
            if (correctAddMeet == true) {
                AlertController.getInstance().showSuccessfullRegisterAlert();
                stage.close();
            }

        } catch (SQLException addProjectInvestigationException) {

            deterMinateSQLState(addProjectInvestigationException);

        }
    }


    @FXML
    void returnViewOnAction(ActionEvent actionEvent) {
        try{
            stage.close();
        }catch(Exception returnViewOnActionExeception){
            AlertController alertView = AlertController.getInstance();
            alertView.showActionFailedAlert(" No se pudo volver a la ventana anterior." +
                    "Causa: " + returnViewOnActionExeception);

        }
    }

    private void initValidatorToTextInput() {
        Function<Object, Boolean> validateDate = a -> {
            return DateFormatter.compareActualDateToSelectedDate((LocalDate) a) == 1;
        };

        addComponentToValidator(new ValidatorTextInputControl(bussinesTextField, Validator.PATTERN_LETTERS, Validator.LENGTH_GENERAL, this), false);

        addComponentToValidator(new ValidatorTextInputControl(hourTextField, Validator.PATTERN_NUMBERS, Validator.LENGTH_SMALL_TEXT, this), false);

        addComponentToValidator(new ValidatorTextInputControl(minutesTextField, Validator.PATTERN_NUMBERS, Validator.LENGTH_SMALL_TEXT, this), false);

        addComponentToValidator(new ValidatorComboBoxBaseWithConstraints(meetDateDataPicker, this, validateDate), false);

        initListenerToControls();
    }

}
