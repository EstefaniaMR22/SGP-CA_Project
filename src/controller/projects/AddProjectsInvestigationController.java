package controller.projects;

import controller.Controller;
import controller.IntegrantController;
import controller.exceptions.AlertException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.domain.Project;

import java.net.URL;
import java.util.ResourceBundle;

public class AddProjectsInvestigationController extends Controller {

    @FXML
    private TextField txtFieldProjectName;
    @FXML
    private TextArea txtAreaDescription;
    @FXML
    private DatePicker dtpEstimatedEndDate;
    @FXML
    private DatePicker dtpEndDate;
    @FXML
    private ComboBox cboBoxStatus;



    public void showStage() {
        loadFXMLFile(getClass().getResource("/view/AddProjectInvestigationView.fxml"), this);
        stage.show();
    }


    public void addProjectInvestigation(ActionEvent actionEvent) {

    }

    public void returnView(ActionEvent actionEvent) {
        try{
            stage.close();
            IntegrantController viewReturn = new IntegrantController();
            viewReturn.showStage();

        }catch(Exception integrantOnActionExeception){
            Alert alertView;
            alertView = AlertException.builderAlert("Error FXML", "No se encuentra "
                    + "el FXML por: " + integrantOnActionExeception, Alert.AlertType.ERROR);
            alertView.showAndWait();
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }
}
