/*
* @author Johann
* @versión v1.0
* Last modification date: 17-06-2021
*/

package sgp.ca.demodao;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import sgp.ca.businesslogic.ProjectDAO;
import sgp.ca.domain.Integrant;
import sgp.ca.domain.Project;

public class ProjectListController implements Initializable{

    @FXML
    private Button btnAddIntegrant;
    @FXML
    private Button btnExit;
    @FXML
    private Button btnSearch;
    @FXML
    private TextField txtFieldSearch;
    @FXML
    private DatePicker dtpSearchDate;
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
    @FXML
    private Label lbUserName;
    
    private Integrant token;
    private final ProjectDAO PROJECT_DAO = new ProjectDAO();
    private List<Project> listProject = new ArrayList<>();
   
    @Override
    public void initialize(URL url, ResourceBundle rb){
    }    
    
    public void receiveToken(Integrant integrantToken){
        this.token = integrantToken;
        this.lbUserName.setText(integrantToken.getFullName());
        listProject = PROJECT_DAO.getProjectList();
        if(listProject != null){
            this.prepareTableFormat();
            tvProjects.setItems(FXCollections.observableArrayList(listProject));
        }else{
            GenericWindowDriver.getGenericWindowDriver().showErrorAlert(new ActionEvent(), "Lo sentimos, el sistema no está disponible, favor de contactar a soporte técnico");
            FXMLLoader loader = GenericWindowDriver.getGenericWindowDriver().changeWindow("Start.fxml", btnExit);
            StartController controller = loader.getController();
            controller.receiveIntegrantToken(token);
        }
    }

    @FXML
    private void addProject(ActionEvent event){
        FXMLLoader loader = GenericWindowDriver.getGenericWindowDriver().changeWindow("ProjectForm.fxml", btnExit);
        ProjectFormController controller = loader.getController();
        controller.receiveProjectSaveToken(token);
    }

    @FXML
    private void exit(ActionEvent event){
        FXMLLoader loader = GenericWindowDriver.getGenericWindowDriver().changeWindow("Start.fxml", btnExit);
        StartController controller = loader.getController();
        controller.receiveIntegrantToken(token);
    }
    
    @FXML
    private void searchProject(ActionEvent event){
        if((txtFieldSearch.getText()).equals(null)){
            if((dtpSearchDate.getValue())!= null){
                tvProjects.getItems().clear();
                setProjectListByDate();
            }    
        }else{
            if((dtpSearchDate.getValue())!= null){
                tvProjects.getItems().clear();
                setProjectListInformationByDateAndIssueIntoTable();
            }else{
                tvProjects.getItems().clear();
                setProjectListByIssue();
            }  
        }
        this.txtFieldSearch.clear();
        this.dtpSearchDate.setValue(null);
    }

    @FXML
    private void selectProject(MouseEvent event){
        Project projectSelected = this.tvProjects.getSelectionModel().getSelectedItem();
        if (projectSelected != null){
            FXMLLoader loader = GenericWindowDriver.getGenericWindowDriver().changeWindow("Project.fxml", btnExit);
            ProjectController controller = loader.getController();
            controller.showProject(projectSelected, this.token);
        }
    }
    
    private void prepareTableFormat(){
        this.colNameProject.setCellValueFactory(new PropertyValueFactory ("projectName"));
        this.colDuration.setCellValueFactory(new PropertyValueFactory ("durationProjectInMonths"));
        this.colStatus.setCellValueFactory(new PropertyValueFactory ("status"));
        this.colStartDate.setCellValueFactory(new PropertyValueFactory ("startDate"));
        this.colEndDate.setCellValueFactory(new PropertyValueFactory ("endDate"));
    }
    
     private void setProjectListByIssue(){
        String projectIssue = (txtFieldSearch.getText());
        List<Project> listProjectAux = new ArrayList<>();
        for(Project project : listProject){
            if(project.getProjectName().contains(projectIssue)){
                listProjectAux.add(project);
            }
        }
        tvProjects.setItems(makeitemsProjects(listProjectAux));
    }
    
    private void setProjectListByDate(){
        String projectDate = dtpSearchDate.getValue().toString();
        List<Project> listProjectAux = new ArrayList<>();
        for(Project project : listProject){
            if(project.getStartDate().equals(projectDate)){
                listProjectAux.add(project);
            }
        }
         tvProjects.setItems(makeitemsProjects(listProjectAux)); 
    }
    
    private void setProjectListInformationByDateAndIssueIntoTable(){
        String projectDate = dtpSearchDate.getValue().toString();
        String projectIssue = (txtFieldSearch.getText());
        List<Project> listProjectAux = new ArrayList<>();        
        for(Project project : listProject){
            if((project.getStartDate().equals(projectDate)) && 
            (project.getProjectName().contains(projectIssue))){
                listProjectAux.add(project);
            }
        }
       tvProjects.setItems(makeitemsProjects(listProjectAux)); 
    }
      
    private ObservableList<Project> makeitemsProjects(List<Project> listProject){
        ObservableList<Project> itemsProject = FXCollections.observableArrayList();
        itemsProject.addAll(listProject);
        return itemsProject;
    }
}
