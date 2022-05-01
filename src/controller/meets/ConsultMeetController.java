package controller.meets;


import assets.utils.SQLStates;
import controller.Controller;
import controller.academicgroup.AddMemberController;

import controller.control.AlertController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.dao.MeetDAO;
import model.domain.Meet;
import model.domain.Member;
import model.domain.Project;

import java.awt.*;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConsultMeetController extends Controller implements Initializable {

    @FXML
    private Label dealLabel;
    @FXML
    private Label projectLabel;
    @FXML
    private Label endaDateLabel;

    @FXML
    private TableView<Member> integrantsTableView;
    @FXML
    private TableColumn<Member, String> integrantColumn;
    @FXML
    private TableColumn<Member, String> lastName1Column;
    @FXML
    private TableColumn<Member, String> lastName2Column;
    @FXML
    private Label houLabel;
    @FXML
    private Label leaderLabel;
    @FXML
    private Label secretaryLabel;
    @FXML
    private Label timerLabel;

    @FXML
    private Button exitButton;
    @FXML
    private Button registerTimeButton;

    private String idAcademicGroup;
    private Meet meetSelected;
    private ObservableList<Project> projectsObservableList;
    private ObservableList<Member> memberObservableList;
    private Member leaderMeet;
    private Member secretaryMeet;
    private Member timerMeet;

    public ConsultMeetController(Meet meetSelected, String idAcademicGroup) {
        this.meetSelected = meetSelected;
        this.idAcademicGroup = idAcademicGroup;
    }

    public void showStage() {
        loadFXMLFile(getClass().getResource("/view/ConsultMeetView.fxml"), this);
        stage.showAndWait();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getMeetDetails();
        setTableComponents();

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
            meetSelected = meetDAO.getMeetDetails(meetSelected.getIdMeet());
            chargeMeetToTextField();
        }catch(SQLException getProjectDetailsExeception){

            deterMinateSQLState(getProjectDetailsExeception);

        }

    }

    private void chargeMeetToTextField() {

        dealLabel.setText(meetSelected.getAsunto());
        houLabel.setText(meetSelected.getHour());

        projectLabel.setText(meetSelected.getNameProject());
        endaDateLabel.setText(meetSelected.getDateMeetString());

        leaderLabel.setText(meetSelected.getLeader());

        secretaryLabel.setText(meetSelected.getSecretary());

        timerLabel.setText(meetSelected.getTimer());

    }


    private void chargeMembers(){

        integrantsTableView.setItems(meetSelected.getAsistents());

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
