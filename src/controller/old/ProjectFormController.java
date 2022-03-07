/*
* @author Johann
* @versión v1.0
* Last modification date: 17-06-2021
*/
package controller.old;

import java.net.URL;
import java.time.LocalDate;
import java.time.Period;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import model.old.domain.Integrant;
import model.old.domain.Project;
import model.old.dataaccess.GeneralResumeDAO;
import model.old.dataaccess.ProjectDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import controller.old.pattern.InvalidFormException;
import controller.old.pattern.ValidatorForm;

public class ProjectFormController implements Initializable {

    @FXML
    private Button btnSave;
    @FXML
    private Button btnExit;
    @FXML
    private Button btnUpdate;
    @FXML
    private TextField txtFieldProjectName;
    @FXML
    private DatePicker dtpStartDate;
    @FXML
    private DatePicker dtpEstimatedEndDate;
    @FXML
    private DatePicker dtpEndDate;
    @FXML
    private ComboBox<String> cboBoxStatus;
    @FXML
    private TextArea txtAreaDescription;
    @FXML
    private HBox hbProjectOptions;
    
    private final ProjectDAO PROJECT_DAO = new ProjectDAO();
    private final GeneralResumeDAO GENERALRESUME_DAO = new GeneralResumeDAO();
    private List<Button> optionButtons;
    private Project oldProject  = new Project();
    private Integrant token;
    private final ObservableList<String> STATUSLIST = FXCollections.observableArrayList("Propuesto", "Asignado", "Cancelado", "Terminado");
    
    @Override
    public void initialize(URL url, ResourceBundle rb){
         optionButtons = Arrays.asList(
            btnUpdate, btnSave, 
            btnExit
        );
        hbProjectOptions.getChildren().removeAll(optionButtons);
        cboBoxStatus.setItems(STATUSLIST);
    }    

    public void showProjectSaveForm(){
        hbProjectOptions.getChildren().addAll(btnSave, btnExit);
    }
    
    public void showProjectUpdateForm(Project project){
        oldProject = project;
        this.txtFieldProjectName.setText(project.getProjectName());
        this.dtpStartDate.setValue(LocalDate.parse(project.getStartDate()));
        this.dtpEstimatedEndDate.setValue(LocalDate.parse(project.getEstimatedEndDate()));
        this.cboBoxStatus.setValue(project.getStatus());
        this.txtAreaDescription.setText(project.getDescription());
        this.dtpEndDate.setValue(LocalDate.parse(project.getEndDate()));
        hbProjectOptions.getChildren().addAll(btnUpdate,  btnExit);
    }
     
    @FXML
    private void saveProject(ActionEvent event){
        try{
            this.isValidForm();
            this.checkExistProjectName();
            Project project = new Project(
                txtFieldProjectName.getText(), 
                token.getBodyAcademyKey(),
                calculateMonths(),
                cboBoxStatus.getValue(),
                ValidatorForm.convertJavaDateToSQlDate(dtpStartDate),
                optionEndDate(),
                ValidatorForm.convertJavaDateToSQlDate(dtpEstimatedEndDate),
                txtAreaDescription.getText()
            );
            PROJECT_DAO.addProject(project);
            WindowControllerB.getWindowController().showInfoAlert(event, "Proyecto registrado correctamente");
            FXMLLoader loader = WindowControllerB.getWindowController().changeWindow("ProjectList.fxml", txtFieldProjectName);
            ProjectListController controller = loader.getController();
            controller.receiveToken(token);
        }catch(InvalidFormException ex){
            WindowControllerB.getWindowController().showErrorAlert(new ActionEvent(), ex.getMessage());
        }
    }
    
    public void isValidForm() throws InvalidFormException{
        ValidatorForm.chechkAlphabeticalField(txtFieldProjectName, 5 ,480);
        ValidatorForm.checkNotEmptyDateField(dtpStartDate);
        ValidatorForm.checkNotEmptyDateField(dtpEstimatedEndDate);
        ValidatorForm.isComboBoxSelected(cboBoxStatus);
        if(cboBoxStatus.getValue() == "Terminado"){
            ValidatorForm.checkNotEmptyDateField(dtpEndDate);
        }
        ValidatorForm.chechkAlphabeticalArea(txtAreaDescription, 1 ,480);
        if(dtpEstimatedEndDate.getValue().isBefore(dtpStartDate.getValue())){
            dtpStartDate.setStyle("-fx-border-color: red;");
            throw new InvalidFormException("La fecha de inicio no puede ser despues de la fecha estimada de fin");
        }
    }
    
    public void receiveProjectUpdateToken(Project project, Integrant integrantToken){
        this.token = integrantToken;
        showProjectUpdateForm(project);
    }
    
     public void receiveProjectSaveToken(Integrant integrantToken){
        this.token = integrantToken;
        showProjectSaveForm();
    }

    @FXML
    private void exit(ActionEvent event){
        FXMLLoader loader = WindowControllerB.getWindowController().changeWindow("ProjectList.fxml", txtFieldProjectName);
        ProjectListController controller = loader.getController();
        controller.receiveToken(token);
    }
    
    @FXML
    private void updateProyect(ActionEvent event){
        try{
            this.isValidForm();
            Project project = new Project(
                txtFieldProjectName.getText(), 
                token.getBodyAcademyKey(),
                calculateMonths(),
                cboBoxStatus.getValue(),
                ValidatorForm.convertJavaDateToSQlDate(dtpStartDate),
                optionEndDate(),
                ValidatorForm.convertJavaDateToSQlDate(dtpEstimatedEndDate),
                txtAreaDescription.getText()
            );
            PROJECT_DAO.updateProject(project, oldProject.getProjectName());
            WindowControllerB.getWindowController().showInfoAlert(event, "Proyecto actualizado correctamente");
            FXMLLoader loader = WindowControllerB.getWindowController().changeWindow("ProjectList.fxml", txtFieldProjectName);
            ProjectListController controller = loader.getController();
            controller.receiveToken(token);
        }catch(InvalidFormException ie){
            WindowControllerB.getWindowController().showErrorAlert(new ActionEvent(), ie.getMessage());
        }
    }
    
    public String optionEndDate(){
        String endDate = null;
        if((dtpEndDate.getValue())!=null){
            endDate = ValidatorForm.convertJavaDateToSQlDate(dtpEndDate);
        }else{
            endDate = ValidatorForm.convertJavaDateToSQlDate(dtpEstimatedEndDate);
        }
        return endDate;
    }
    
    private void checkExistProjectName() throws InvalidFormException{
        if(PROJECT_DAO.projectRegistered(this.txtFieldProjectName.getText())){
            this.txtFieldProjectName.setStyle("-fx-border-color: red;");
            throw new InvalidFormException("Ya existe un Proyecto con el mismo nombre");
        }
    }

    private int calculateMonths(){
         int totalMonths = 0;
        if(dtpEstimatedEndDate.getValue() != null && dtpStartDate.getValue() != null){
            Period diferrence = Period.between(dtpStartDate.getValue(),dtpEstimatedEndDate.getValue());
            totalMonths = diferrence.getMonths();
        }
        return totalMonths;
    }
}
