package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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

    public void returnView(ActionEvent actionEvent) {

    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }
}
