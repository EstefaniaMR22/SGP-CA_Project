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

            if(meetDAO.timeMeetIsNull(meetUpdated.getIdMeet())){
                hourTextField.setDisable(true);
            }

            chargeMeetUpdate();
        }catch(SQLException getProjectDetailsExeception){

            deterMinateSQLState(getProjectDetailsExeception);

        }

    }

    private void chargeMeetUpdate() {

        bussinesTextField.setText(meetUpdated.getAsunto());

        MeetDAO meetDAO = new MeetDAO();


        hourTextField.setText(meetUpdated.getHour());

        try {
            if (!meetDAO.timeMeetIsNull(meetUpdated.getIdMeet())) {

                hourTextField.setText("" + meetUpdated.getHour());
                hourTextField.setDisable(true);
                meetDateDataPicker.setDisable(true);
            } else {
                initListenerHour();
            }
        }catch(SQLException chargeHourException) {
            deterMinateSQLState(chargeHourException);
        }

        int positionProject = getIndexProjects(meetUpdated.getIdProject());
        projectsCombobox.getSelectionModel().select(positionProject);

        meetDateDataPicker.setValue(DateFormatter.getLocalDateFromUtilDate(meetUpdated.getDateMeet()));
        leaderTextField.setText(meetUpdated.getLeader());

        secretaryTextField.setText(meetUpdated.getSecretary());
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
                    systemLabel.setText("??Al parecer ya existe una reuni??n en la misma fecha y hora, \n"+
                            " ingrese una fecha y hora distinta");
                }

            }else {
                AlertController alertView = AlertController.getInstance();
                alertView.showActionFailedAlert("Algunos datos ingresados son inv??lidos, por favor verif??quelos");
            }
        } catch (Exception e) {
            systemLabel.setText(e.getLocalizedMessage());
        }
    }

    private boolean validateMeet() throws SQLException {
        return new MeetDAO().checkMeet(DateFormatter.getDateFromDatepickerValue(meetDateDataPicker.getValue()),
                hourTextField.getText());
    }

    private void modifyMeet(){
        MeetDAO meetDAO = new MeetDAO();

        meetUpdated.setAsunto(bussinesTextField.getText());

        meetUpdated.setDateMeet(DateFormatter.getDateFromDatepickerValue(meetDateDataPicker.getValue()));
        meetUpdated.setHour(hourTextField.getText());

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

    public void initListenerHour() {
        hourTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            addComponentToValidator(new ValidatorTextInputControl(hourTextField, Validator.PATTERN_HOURS, Validator.LENGTH_HOUR, this), false);
            initListenerToControls();
        });

    }

    private void initValidatorToTextInput() {
        Function<Object, Boolean> validateDate = a -> {
            return DateFormatter.compareActualDateToSelectedDate((LocalDate) a) == 1;
        };

        addComponentToValidator(new ValidatorTextInputControl(bussinesTextField, Validator.PATTERN_LETTERS, Validator.LENGTH_GENERAL, this), false);

        initListenerToControls();
    }

}
