package controller.projects;

import controller.control.Controller;
import controller.control.AlertController;
import controller.IntegrantController;
import controller.academicgroup.AddMemberController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.dao.LgacDAO;
import model.dao.ProjectDAO;
import model.domain.LGAC;
import model.domain.Project;
import assets.utils.SQLStates;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProjectsInvestigationController extends Controller implements Initializable {

    @FXML
    private Button newProjectButton;
    @FXML
    private Button updateProjectButton;
    @FXML
    private Button consultProjectButton;
    @FXML
    private Button btnExit;
    @FXML
    private TextField searchTextField;
    @FXML
    private TableView<Project> projectsTableView;
    @FXML
    private TableColumn<Project, String> nameProjectColumn;
    @FXML
    private TableColumn<Project, String> durationProjectColumn;
    @FXML
    private TableColumn<Project, String> statusProjectColumn;
    @FXML
    private TableColumn<Project, String> startDateProjectColumn;
    @FXML
    private TableColumn<Project, String> endDateProjectColumn;
    private String idAcademicGroup;

    private ObservableList<Project> projectsInvestigation;

    public void showStage() {
        loadFXMLFile(getClass().getResource("/view/ProjectsInvestigationView.fxml"), this);
        searchTextField.setText("Buscar");
        System.out.println("java version: "+System.getProperty("java.version"));
        System.out.println("javafx.version: " + System.getProperty("javafx.version"));
        setTableComponents();
        stage.show();
    }

    public ProjectsInvestigationController(String idAcademicGroup){
        this.idAcademicGroup = idAcademicGroup;
    }

    private void setTableComponents() {
        projectsInvestigation = FXCollections.observableArrayList();
        nameProjectColumn.setCellValueFactory(new PropertyValueFactory<>("projectName"));
        durationProjectColumn.setCellValueFactory(new PropertyValueFactory<>("durationProjectInMonths"));
        statusProjectColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        startDateProjectColumn.setCellValueFactory(new PropertyValueFactory<>("startDateString"));
        endDateProjectColumn.setCellValueFactory(new PropertyValueFactory<>("endDateString"));
        chargeProjects();
        searchProject();
    }

    private void searchProject()
    {

        if(projectsInvestigation.size()>0)
        {
            FilteredList<Project> projectSearch = new FilteredList<Project>(projectsInvestigation, p -> true);
            searchTextField.textProperty().addListener(new ChangeListener<String>() {
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
            sortedData.comparatorProperty().bind(projectsTableView.comparatorProperty());
            projectsTableView.setItems(sortedData);
        }
    }

    private void chargeProjects()
    {
        ProjectDAO projectDAO = new ProjectDAO();
        try {

            projectsInvestigation = projectDAO.getProjectList(idAcademicGroup);
            projectsTableView.setItems(projectsInvestigation);

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
    void addProjectInvestigationOnAction(ActionEvent actionEvent) throws SQLException {
        if(!verifyLGAC().isEmpty()) {
            try {
                AddProjectsInvestigationController addProjectInvestigationController = new AddProjectsInvestigationController(idAcademicGroup);
                addProjectInvestigationController.showStage();

            } catch (Exception addProjectInvestigationException) {
                AlertController alertView = AlertController.getInstance();
                alertView.showActionFailedAlert(" No se pudo abrir la ventana " +
                        "AddProyectInvestigation. Causa: " + addProjectInvestigationException);

            }
        }else {
            AlertController alertView = AlertController.getInstance();
            alertView.showActionFailedAlert(" Sin 'lgac' registrados, no puede agregar un proyecto de investigacion");
        }

        chargeProjects();

    }

    private List<LGAC> verifyLGAC() {
        LgacDAO lgac= new LgacDAO();
        List<LGAC> listLGAC = null;
        try {
            listLGAC = lgac.getAllLgacsByIdAcademicGroup(idAcademicGroup);
        }catch (SQLException getAllLgacsException) {
            Logger.getLogger(AddProjectsInvestigationController.class.getName()).log(Level.SEVERE, null, getAllLgacsException);
            AlertController alertView = AlertController.getInstance();
            alertView.showActionFailedAlert(" No se pudo cargar las LGAC. Causa: " + getAllLgacsException);

        }
        return listLGAC;
    }

    @FXML
    void updateProjectInvestigation(ActionEvent actionEvent) {

        Project selectedProjectUpdate = projectsTableView.getSelectionModel().getSelectedItem();
        if(selectedProjectUpdate != null) {

                try {
                    ModifyProjectInvestigationController updateProjectInvestigationController = new ModifyProjectInvestigationController(selectedProjectUpdate, idAcademicGroup);
                    updateProjectInvestigationController.showStage();

                }catch (Exception modifyProjectInvestigationException) {
                    Logger.getLogger(ProjectsInvestigationController.class.getName()).log(Level.SEVERE, null, modifyProjectInvestigationException);

                    AlertController alertView = AlertController.getInstance();
                    alertView.showActionFailedAlert(" No se pudo abrir la ventana " +
                            "ModifyProyectInvestigation. Causa: " + modifyProjectInvestigationException);

                }

        }else {
            AlertController alertView = AlertController.getInstance();
            alertView.showActionFailedAlert(" Antes de presionar modificar debes seleccionar un " +
                    "proyecto de investigación de la tabla");
        }

        chargeProjects();

    }

    @FXML
    void consultProjectsInvestigation(ActionEvent actionEvent) {

            Project selectedProject = projectsTableView.getSelectionModel().getSelectedItem();
            if(selectedProject != null) {
                try{
                ConsultProjectController consultProjectsInvestigationController = new ConsultProjectController(selectedProject, idAcademicGroup);
                consultProjectsInvestigationController.showStage();

                }catch (Exception consultProjectInvestigationException) {
                    Logger.getLogger(ProjectsInvestigationController.class.getName()).log(Level.SEVERE, null, consultProjectInvestigationException);

                    AlertController alertView = AlertController.getInstance();
                    alertView.showActionFailedAlert(" No se pudo abrir la ventana " +
                            "ConsultProyectInvestigation. Causa: " + consultProjectInvestigationException);

                }
            }else {
                AlertController alertView = AlertController.getInstance();
                alertView.showActionFailedAlert(" Antes de presionar consultar debes seleccionar un " +
                        "proyecto de investigación de la tabla");
            }

        chargeProjects();

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
