package controller.projects;

import controller.AlertController;
import controller.Controller;
import controller.IntegrantController;
import controller.exceptions.AlertException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.dao.ProjectDAO;
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

    private ObservableList<Project> Projects;


    public void showStage() {
        loadFXMLFile(getClass().getResource("/view/ProjectsInvestigationView.fxml"), this);
        txtFieldSearch.setText("Buscar");

        setTableComponents();
        stage.show();
    }

    private void setTableComponents() {
        Projects = FXCollections.observableArrayList();
        colNameProject.setCellValueFactory(new PropertyValueFactory<>("projectName"));
        colDuration.setCellValueFactory(new PropertyValueFactory<>("durationProjectInMonths"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colStartDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        colEndDate.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        chargeProjects();
        searchProject();
    }

    private void searchProject()
    {

        if(Projects.size()>0)
        {
            FilteredList<Project> projectSearch = new FilteredList<Project>(Projects, p -> true);
            txtFieldSearch.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
                {
                    projectSearch.setPredicate(search ->{
                        if(newValue == null || newValue.isEmpty())
                        {
                            return true;
                        }

                        String lowerCaseFilter = newValue.toLowerCase();

                        if(search.getProjectName().toLowerCase().contains(lowerCaseFilter))
                        {
                            return true;
                        }
                        else if(search.getStartDate().toLowerCase().contains(lowerCaseFilter))
                        {
                            return true;
                        }
                        else if(search.getEndDate().toLowerCase().contains(lowerCaseFilter))
                        {
                            return true;
                        }
                        else if(search.getStatus().toLowerCase().contains(lowerCaseFilter))
                        {
                            return true;
                        }
                        else
                        {
                            return false;
                        }
                    });
                }
            });

            SortedList<Project> sortedData = new SortedList<>(projectSearch);
            sortedData.comparatorProperty().bind(tvProjects.comparatorProperty());
            tvProjects.setItems(sortedData);
        }
    }


    private void chargeProjects()
    {
        ProjectDAO projectDAO = new ProjectDAO();
        Projects = projectDAO.getProjectList();
        tvProjects.setItems(Projects);

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

    }
}
