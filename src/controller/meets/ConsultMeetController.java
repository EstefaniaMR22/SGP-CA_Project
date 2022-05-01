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
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConsultMeetController extends ValidatorController implements Initializable {

    @FXML
    private TextField bussinesTextField;
    @FXML
    private TextField hourTextField;
    @FXML
    private TextField minutesTextField;
    @FXML
    private TextField projectTextField;
    @FXML
    private TableView<Member> integrantsTableView;
    @FXML
    private TableColumn<Member, String> integrantColumn;
    @FXML
    private TableColumn<Member, String> lastName1Column;
    @FXML
    private TableColumn<Member, String> lastName2Column;
    @FXML
    private TextField meetDateTextField;
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
    private Button addTimeButton;

    private String idAcademicGroup;
    private Meet meetUpdated;
    private ObservableList<Project> projectsObservableList;
    private ObservableList<Member> memberObservableList;

    public ConsultMeetController(Meet meetUpdated, String idAcademicGroup) {
        this.meetUpdated = meetUpdated;
        this.idAcademicGroup = idAcademicGroup;
    }

    public void showStage() {
        loadFXMLFile(getClass().getResource("/view/ConsultMeetView.fxml"), this);
        stage.showAndWait();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        projectsObservableList = FXCollections.observableArrayList();
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
        bussinesTextField.setDisable(true);

        hourTextField.setText(meetUpdated.getHour().substring(0,2));
        hourTextField.setDisable(true
        );
        minutesTextField.setText(meetUpdated.getHour().substring(3,5));
        minutesTextField.setDisable(true);

        projectTextField.setText(meetUpdated.getNameProject());
        projectTextField.setDisable(true);

        meetDateTextField.setText(meetUpdated.getDateMeetString());
        meetDateTextField.setDisable(true);

        leaderTextField.setText(meetUpdated.getLeader());
        leaderTextField.setDisable(true);

        secretaryTextField.setText(meetUpdated.getSecretary());
        secretaryTextField.setDisable(true);

        timerTextField.setText(meetUpdated.getTimer());
        timerTextField.setDisable(true);

    }


    private void chargeMembers(){

        integrantsTableView.setItems(meetUpdated.getAsistents());

    }


    private void deterMinateSQLState(SQLException sqlException) {
        Logger.getLogger(AddMemberController.class.getName()).log(Level.SEVERE, null, sqlException);
        if(sqlException.getSQLState().equals(SQLStates.SQL_NO_CONNECTION.getSqlState())) {
            AlertController.getInstance().showConnectionErrorAlert();
        }
        AlertController.getInstance().showActionFailedAlert(sqlException.getLocalizedMessage());
    }

    @FXML
    void addTimeAndDealsOnAction(ActionEvent actionEvent) {

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



}
