package controller.meets;

import assets.utils.Autentication;
import assets.utils.SQLStates;
import controller.control.AlertController;
import controller.control.Controller;
import controller.IntegrantController;
import controller.academicgroup.AddMemberController;
import controller.receptionalWorks.AddReceptionalWorkController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.dao.MeetDAO;
import model.dao.ProjectDAO;
import model.domain.Meet;
import model.domain.ParticipationType;
import model.domain.Project;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MeetsController extends Controller implements Initializable {

    @FXML private Button newMeetButton;
    @FXML private Button modifyMeetButton;
    @FXML private Button consultMeetButton;
    @FXML private Button btnExit;
    @FXML private TableView<Meet> meetsTableView;
    @FXML private TableColumn<Meet, String> businessColumn;
    @FXML private TableColumn<Meet, String> nameProjectColumn;
    @FXML private TableColumn<Meet, String> hourMeetColumn;
    @FXML private TableColumn<Meet, String> dateMeetColumn;
    @FXML private TableColumn<Meet, String> leaderColumn;
    private String idAcademicGroup;
    private int idMember;

    private ObservableList<Meet> meetsObservableList;

    public void showStage() {
        loadFXMLFile(getClass().getResource("/view/MeetsView.fxml"), this);
        stage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setTableComponents();
        ParticipationType participationType = Autentication.getInstance().getParticipation().getParticipationType();
        if (participationType == ParticipationType.INTEGRANT) {
            newMeetButton.setDisable(true);
            newMeetButton.setVisible(false);
            modifyMeetButton.setDisable(true);
            modifyMeetButton.setVisible(false);

        }

        if (participationType == ParticipationType.COLABORATOR) {
            newMeetButton.setDisable(true);
            newMeetButton.setVisible(false);
            modifyMeetButton.setDisable(true);
            modifyMeetButton.setVisible(false);
        }
    }

    public MeetsController(String idAcademicGroup, int idMember){

        this.idAcademicGroup = idAcademicGroup;
        this.idMember = idMember;
    }

    private void setTableComponents() {
        meetsObservableList = FXCollections.observableArrayList();
        businessColumn.setCellValueFactory(new PropertyValueFactory<>("asunto"));
        nameProjectColumn.setCellValueFactory(new PropertyValueFactory<>("nameProject"));
        hourMeetColumn.setCellValueFactory(new PropertyValueFactory<>("hour"));
        dateMeetColumn.setCellValueFactory(new PropertyValueFactory<>("dateMeet"));
        leaderColumn.setCellValueFactory(new PropertyValueFactory<>("leader"));
        chargeMeets();
    }

    private void chargeMeets()
    {
        MeetDAO meetDAO = new MeetDAO();
        try {

            meetsObservableList = meetDAO.getMeetList(idAcademicGroup);
            meetsTableView.setItems(meetsObservableList);

        }catch(SQLException chargeProjectsExeception){
            deterMinateSQLState(chargeProjectsExeception);
        }

    }

    private void deterMinateSQLState(SQLException sqlException) {
        Logger.getLogger(AddMemberController.class.getName()).log(Level.SEVERE, null, sqlException);
        if(sqlException.getSQLState().equals(SQLStates.SQL_NO_CONNECTION.getSqlState())) {
            AlertController.getInstance().showConnectionErrorAlert();
        }
        AlertController.getInstance().showActionFailedAlert(sqlException.getLocalizedMessage());
    }

    @FXML
    void addMeetOnAction(ActionEvent actionEvent) throws SQLException {
        if(!verifyProjects().isEmpty()) {
            try {
                AddMeetController addMeetController = new AddMeetController(idAcademicGroup);
                addMeetController.showStage();

            } catch (Exception addMeetException) {
                AlertController alertView = AlertController.getInstance();
                alertView.showActionFailedAlert(" No se pudo abrir la ventana " +
                        "AddMeetController. Causa: " + addMeetException);

            }
        }else {
            AlertController alertView = AlertController.getInstance();
            alertView.showActionFailedAlert(" Sin 'proyectos' registrados, no puede agendar una reunión");

        }

        chargeMeets();

    }

    private List<Project> verifyProjects() {
        ProjectDAO projectDAO= new ProjectDAO();
        List<Project> projectList = null;
        try {
            projectList = projectDAO.getProjectList(idAcademicGroup);
        }catch (SQLException getAllLgacsException) {
            Logger.getLogger(AddReceptionalWorkController.class.getName()).log(Level.SEVERE, null, getAllLgacsException);
            AlertController alertView = AlertController.getInstance();
            alertView.showActionFailedAlert(" No se pudo cargar los proyectos de investigación. Causa: " + getAllLgacsException);

        }
        return projectList;
    }

    @FXML
    void modifyMeetOnAction(ActionEvent actionEvent) {

        Meet selectedMeetUpdate = meetsTableView.getSelectionModel().getSelectedItem();
        if(selectedMeetUpdate != null) {

                try {
                    ModifyMeetController updateMeetController = new ModifyMeetController(selectedMeetUpdate, idAcademicGroup);
                    updateMeetController.showStage();

                }catch (Exception modifyProjectInvestigationException) {
                    Logger.getLogger(MeetsController.class.getName()).log(Level.SEVERE, null, modifyProjectInvestigationException);

                    AlertController alertView = AlertController.getInstance();
                    alertView.showActionFailedAlert(" No se pudo abrir la ventana " +
                            "ModifyMeets. Causa: " + modifyProjectInvestigationException);

                }

        }else {
            AlertController alertView = AlertController.getInstance();
            alertView.showActionFailedAlert(" Antes de presionar modificar debes seleccionar una " +
                    "reunión de la tabla");
        }

        chargeMeets();

    }

    @FXML
    void consultMeetOnAction(ActionEvent actionEvent) {

            Meet selectedMeet = meetsTableView.getSelectionModel().getSelectedItem();
            if(selectedMeet != null) {
                try{

                ConsultMeetController consultMeetController = new ConsultMeetController(selectedMeet, idAcademicGroup, idMember);
                consultMeetController.showStage();

                }catch (Exception consultProjectInvestigationException) {
                    Logger.getLogger(MeetsController.class.getName()).log(Level.SEVERE, null, consultProjectInvestigationException);

                    AlertController alertView = AlertController.getInstance();
                    alertView.showActionFailedAlert(" No se pudo abrir la ventana " +
                            "ConsultMeet. Causa: " + consultProjectInvestigationException);

                }
            }else {
                AlertController alertView = AlertController.getInstance();
                alertView.showActionFailedAlert(" Antes de presionar consultar debes seleccionar una " +
                        "reunión de la tabla");
            }

        chargeMeets();

    }

    @FXML
    void returnViewOnAction(ActionEvent actionEvent) {
        try{
            stage.close();
            IntegrantController viewReturn = new IntegrantController(idAcademicGroup);
            viewReturn.showStage();

        }catch(Exception returnViewOnActionExeception){
            AlertController alertView = AlertController.getInstance();
            alertView.showActionFailedAlert(" Error en el metodo returnViewOnActionExeception:  " + returnViewOnActionExeception);

        }
    }

}
