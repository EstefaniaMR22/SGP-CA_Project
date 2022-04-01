package controller.projects;

import controller.AlertController;
import controller.Controller;
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

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProjectsInvestigationController extends Controller {

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

    private ObservableList<Project> Projects;

    public void showStage() {
        loadFXMLFile(getClass().getResource("/view/ProjectsInvestigationView.fxml"), this);
        searchTextField.setText("Buscar");

        setTableComponents();
        stage.show();
    }

    private void setTableComponents() {
        Projects = FXCollections.observableArrayList();
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

        if(Projects.size()>0)
        {
            FilteredList<Project> projectSearch = new FilteredList<Project>(Projects, p -> true);
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

            Projects = projectDAO.getProjectList();
            projectsTableView.setItems(Projects);

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
        LgacDAO lgac= new LgacDAO();
        try {
            //List<LGAC> listlgac = lgac.getAlllgacs();+
            List<LGAC> listlgac = null;
            /*
            Don't use a try catch inside a try catch
            Fix this method
             */
            if(!listlgac.isEmpty()) {
                try {
                    AddProjectsInvestigationController addProjectInvestigationController = new AddProjectsInvestigationController();
                    addProjectInvestigationController.showStage();

                } catch (Exception addProjectInvestigationException) {
                    AlertController alertView = AlertController.getInstance();
                    alertView.showActionFailedAlert(" No se pudo abrir la ventana " +
                            "AddProyectInvestigation. Causa: " + addProjectInvestigationException);

                }
                throw new SQLException("FIX ME NOW");
            }else {
                AlertController alertView = AlertController.getInstance();
                alertView.showActionFailedAlert(" Sin 'lgac' registrados, no puede agregar un proyecto de investigacion");
            }

        }catch (SQLException getAllLgacsException) {
            Logger.getLogger(AddProjectsInvestigationController.class.getName()).log(Level.SEVERE, null, getAllLgacsException);
            AlertController alertView = AlertController.getInstance();
            alertView.showActionFailedAlert(" No se pudo cargar las LGAC. Causa: " + getAllLgacsException);

        }

    }


    @FXML
    void updateProjectInvestigation(ActionEvent actionEvent) {

        Project selectedProjectUpdate = projectsTableView.getSelectionModel().getSelectedItem();
        if(selectedProjectUpdate != null) {
            if(selectedProjectUpdate.getEndDate()==null) {

                try {
                    ModifyProjectInvestigationController updateProjectInvestigationController = new ModifyProjectInvestigationController(selectedProjectUpdate);
                    updateProjectInvestigationController.showStage();

                }catch (Exception addProjectInvestigationException) {
                    Logger.getLogger(ProjectsInvestigationController.class.getName()).log(Level.SEVERE, null, addProjectInvestigationException);

                    AlertController alertView = AlertController.getInstance();
                    alertView.showActionFailedAlert(" No se pudo abrir la ventana " +
                            "ModifyProyectInvestigation. Causa: " + addProjectInvestigationException);


                }
            }else {
                AlertController alertView = AlertController.getInstance();
                alertView.showActionFailedAlert(" No puedes modificar un proyecto que ya esta 'Completado'");
            }
        }else {
            AlertController alertView = AlertController.getInstance();
            alertView.showActionFailedAlert(" Antes de presionar modificar debes seleccionar un " +
                    "proyecto de investigación de la tabla");
        }
    }

    @FXML
    void consultProjectsInvestigation(ActionEvent actionEvent) {

            Project selectedProject = projectsTableView.getSelectionModel().getSelectedItem();
            if(selectedProject != null) {
                ConsultProjectController consultProjectsInvestigationController = new ConsultProjectController(selectedProject);
                consultProjectsInvestigationController.showStage();
            }else {
                AlertController alertView = AlertController.getInstance();
                alertView.showActionFailedAlert(" Antes de presionar consultar debes seleccionar un " +
                        "proyecto de investigación de la tabla");
            }

    }

    @FXML
    void returnViewOnAction(ActionEvent actionEvent) {
        try{
            stage.close();
            IntegrantController viewReturn = new IntegrantController();
            viewReturn.showStage();

        }catch(Exception returnViewOnActionExeception){
            AlertController alertView = AlertController.getInstance();
            alertView.showActionFailedAlert(" Error en el metodo returnViewOnActionExeception:  " + returnViewOnActionExeception);

        }
    }

}
