package controller.receptionalWorks;

import assets.utils.SQLStates;
import controller.control.Controller;
import controller.academicgroup.AddMemberController;
import controller.control.AlertController;
import controller.projects.AddProjectsInvestigationController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.dao.ReceptionalWorkDAO;
import model.domain.Evidence;
import model.domain.ReceptionalWork;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConsultReceptionalWorkController extends Controller implements Initializable {
    @FXML private Label receptionalWorkNameLabel;
    @FXML private Label modalityLabel;
    @FXML private Label endDateLabel;
    @FXML private Label directorLabel;
    @FXML private Label codirectorLabel;
    @FXML private Label participantsLabel;
    @FXML private Label statusLabel;
    @FXML private Label descriptionLabel;
    @FXML private Label requerimentsLabel;

    ReceptionalWork receptionalWorkSelected;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getReceptionalWorkDetails();
    }

    public void showStage() {
        loadFXMLFile(getClass().getResource("/view/ConsultReceptionalWorkView.fxml"), this);
        stage.showAndWait();
    }

    public ConsultReceptionalWorkController(ReceptionalWork receptionalWorkSelected){
        this.receptionalWorkSelected = receptionalWorkSelected;
    }

    @FXML
    public void returnViewOnAction(ActionEvent actionEvent) {
        try{
            stage.close();

        }catch(Exception returnViewException) {
            Logger.getLogger(AddProjectsInvestigationController.class.getName()).log(Level.SEVERE, null, returnViewException);
            AlertController alertView = AlertController.getInstance();
            alertView.showActionFailedAlert(" No se pudo volver a la ventana anterior." +
                    " Causa: " + returnViewException);

        }
    }

    private void setReceptionalWorkDataToTextField() {
        receptionalWorkNameLabel.setText(receptionalWorkSelected.getNameReceptionalWork());

        modalityLabel.setText(String.valueOf(receptionalWorkSelected.getModality()));
        endDateLabel.setText((receptionalWorkSelected.getEndDateString()));
        directorLabel.setText(receptionalWorkSelected.getDirector());
        codirectorLabel.setText(receptionalWorkSelected.getCodirector());
        participantsLabel.setText(String.valueOf(receptionalWorkSelected.getParticipants()));
        statusLabel.setText(receptionalWorkSelected.getStatus());
        descriptionLabel.setText(receptionalWorkSelected.getDescription());
        requerimentsLabel.setText(receptionalWorkSelected.getRequeriments());

    }

    private void deterMinateSQLState(SQLException sqlException) {
        Logger.getLogger(AddMemberController.class.getName()).log(Level.SEVERE, null, sqlException);
        if(sqlException.getSQLState().equals(SQLStates.SQL_NO_CONNECTION.getSqlState())) {
            AlertController.getInstance().showConnectionErrorAlert();
        }
        AlertController.getInstance().showActionFailedAlert(sqlException.getLocalizedMessage());
    }

    private void getReceptionalWorkDetails() {

        ReceptionalWorkDAO receptionalWorkDAO = new ReceptionalWorkDAO();
        try {
            receptionalWorkSelected = receptionalWorkDAO.getReceptionalWorkDetails(receptionalWorkSelected.getIdReceptionalWork());
            setReceptionalWorkDataToTextField();
            chargeEvidences();

        }catch(SQLException getProjectDetailsExeception){

            deterMinateSQLState(getProjectDetailsExeception);

        }

    }

    private void chargeEvidences() {


    }

}
