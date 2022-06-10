package controller.evidences;

import assets.utils.Autentication;
import assets.utils.SQLStates;
import controller.IntegrantController;
import controller.academicgroup.AddMemberController;
import controller.control.AlertController;
import controller.control.Controller;
import controller.meets.AddMeetController;
import controller.meets.ConsultMeetController;
import controller.meets.ModifyMeetController;
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
import model.dao.EvidenceDAO;
import model.dao.MeetDAO;
import model.dao.ProjectDAO;
import model.domain.Evidence;
import model.domain.Meet;
import model.domain.ParticipationType;
import model.domain.Project;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EvidencesController extends Controller implements Initializable {

    @FXML private Button addEvidenceButton;
    @FXML private Button deleteEvidenceButton;
    @FXML private Button consultEvidenceButton;
    @FXML private Button btnExit;
    @FXML private TableView<Evidence> evidencesTableView;
    @FXML private TableColumn<Evidence, String> titleColumn;
    @FXML private TableColumn<Evidence, String> authorColumn;
    @FXML private TableColumn<Evidence, String> typeColumn;
    @FXML private TableColumn<Evidence, String> dateColumn;
    private String idAcademicGroup;
    private int idMember;

    private ObservableList<Evidence> evidenceObservableList;

    public void showStage() {
        loadFXMLFile(getClass().getResource("/view/EvidencesView.fxml"), this);
        stage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setTableComponents();
    }

    public EvidencesController(String idAcademicGroup, int idMember){

        this.idAcademicGroup = idAcademicGroup;
        this.idMember = idMember;
    }

    private void setTableComponents() {
        evidenceObservableList = FXCollections.observableArrayList();
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("evidenceTitle"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("responsableEvidence"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("evidenceType"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("publicationDate"));
        chargeEvidences();
    }

    private void chargeEvidences()
    {
        EvidenceDAO evidenceDAO = new EvidenceDAO();
        try {

            evidenceObservableList = evidenceDAO.getEvidenceList(idAcademicGroup);
            evidencesTableView.setItems(evidenceObservableList);

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
    void addEvidenceOnAction(ActionEvent actionEvent) throws SQLException {

            try {
                if(!verifyBooks().isEmpty()) {

                    AddEvidenceController consultEvidence = new AddEvidenceController(idAcademicGroup, idMember, true);
                    consultEvidence.showStage();

                }else {
                    AddEvidenceController consultEvidence = new AddEvidenceController(idAcademicGroup, idMember, false);
                    consultEvidence.showStage();
                }

            } catch (Exception addMeetException){
                    AlertController alertView = AlertController.getInstance();
                    alertView.showActionFailedAlert(" No se pudo abrir la ventana " +
                            "AddEvidence. Causa: " + addMeetException);

                }

        chargeEvidences();

    }

    private ObservableList<Evidence> verifyBooks() {
        EvidenceDAO evidenceDAO= new EvidenceDAO();
        ObservableList<Evidence> evidenceObservableList = null;
        try {
            evidenceObservableList = evidenceDAO.verifyExistBooks(idAcademicGroup);
        }catch (SQLException getAllLgacsException) {
            Logger.getLogger(AddReceptionalWorkController.class.getName()).log(Level.SEVERE, null, getAllLgacsException);
            AlertController alertView = AlertController.getInstance();
            alertView.showActionFailedAlert(" No se pudo cargar las evidencias tipo libro. Causa: " + getAllLgacsException);

        }
        return evidenceObservableList;
    }

    @FXML
    void deleteEvidenceOnAction(ActionEvent actionEvent) {

        Evidence selectedEvidence = evidencesTableView.getSelectionModel().getSelectedItem();
        EvidenceDAO evidenceDAO = new EvidenceDAO();
        if(selectedEvidence != null) {
            try{
                boolean correctDeleteEvidence = false;
                correctDeleteEvidence = evidenceDAO.removeEvidence(selectedEvidence.getIdEvidence());
                    if (correctDeleteEvidence == true) {
                        AlertController.getInstance().showSuccessfullRegisterAlert();
                        stage.close();
                    }else {
                        AlertController alertView = AlertController.getInstance();
                        alertView.showActionFailedAlert("Error al borrar evidencia");
                    }

            }catch (Exception consultProjectInvestigationException) {
                Logger.getLogger(EvidencesController.class.getName()).log(Level.SEVERE, null, consultProjectInvestigationException);

                AlertController alertView = AlertController.getInstance();
                alertView.showActionFailedAlert(" No se pudo Eliminar la evidencia." +
                        " Causa: " + consultProjectInvestigationException);

            }
        }else {
            AlertController alertView = AlertController.getInstance();
            alertView.showActionFailedAlert(" Antes de presionar eliminar debes seleccionar una " +
                    "evidencia de la tabla");
        }

        chargeEvidences();

    }

    @FXML
    void consultEvidenceOnAction(ActionEvent actionEvent) {

            Evidence selectedEvidence = evidencesTableView.getSelectionModel().getSelectedItem();
            if(selectedEvidence != null) {
                try{

                ConsultEvidenceController consultEvidence = new ConsultEvidenceController(selectedEvidence.getIdEvidence());
                consultEvidence.showStage();


                }catch (Exception consultProjectInvestigationException) {
                    Logger.getLogger(EvidencesController.class.getName()).log(Level.SEVERE, null, consultProjectInvestigationException);

                    AlertController alertView = AlertController.getInstance();
                    alertView.showActionFailedAlert(" No se pudo abrir la ventana " +
                            "ConsultEvidence. Causa: " + consultProjectInvestigationException);

                }
            }else {
                AlertController alertView = AlertController.getInstance();
                alertView.showActionFailedAlert(" Antes de presionar consultar debes seleccionar una " +
                        "evidencia de la tabla");
            }

        chargeEvidences();

    }

    @FXML
    void returnViewOnAction(ActionEvent actionEvent) {
        try{
            stage.close();
        }catch(Exception returnViewOnActionExeception){
            AlertController alertView = AlertController.getInstance();
            alertView.showActionFailedAlert(" Error en el metodo returnViewOnActionExeception:  " + returnViewOnActionExeception);

        }
    }

}
