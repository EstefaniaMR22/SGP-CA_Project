package controller;

import controller.exceptions.AlertException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.domain.Project;

import java.net.URL;
import java.util.ResourceBundle;

public class ProjectsInvestigationController extends Controller {

    @FXML
    private Button btNewProject;

    @FXML
    private Button btUpdateProject;
    @FXML
    private Button btnExit;
    @FXML
    private TextField txtFieldSearch;
    @FXML
    private TableView<Project> tvProjects;
    @FXML
    private TableColumn<Project, String> colNameProject;
    @FXML
    private TableColumn<Project, String> colDuration;
    @FXML
    private TableColumn<Project, String> colStatus;
    @FXML
    private TableColumn<Project, String> colStartDate;
    @FXML
    private TableColumn<Project, String> colEndDate;

    public void showStage() {
        loadFXMLFile(getClass().getResource("/view/ProjectsInvestigationView.fxml"), this);
        stage.show();
    }

    private void setTableComponents() {
        colNameProject.setCellValueFactory(new PropertyValueFactory<>("projectName"));
        colDuration.setCellValueFactory(new PropertyValueFactory<>("durationProjectInMonths"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colStartDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        colEndDate.setCellValueFactory(new PropertyValueFactory<>("endDate"));

    }

    public void addProjectInvestigation(ActionEvent actionEvent) {

    }

    public void updateProjectInvestigation(ActionEvent actionEvent) {

    }

    public void consultProjectsInvestigation(ActionEvent actionEvent) {

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
        txtFieldSearch.setText("Buscar");
        setTableComponents();
    }
}
