/*
* @author Johann
* @versión v1.0
* Last modification date: 17-06-2021
*/

package controller.old;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import controller.old.pattern.*;
import model.old.dataaccess.CollaboratorDAO;
import model.old.dataaccess.IntegrantDAO;
import model.old.dataaccess.ProjectDAO;
import model.old.dataaccess.ReceptionWorkDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import utils.FTPClient;
import javafx.stage.Stage;
import model.old.domain.Collaborator;
import model.old.domain.Integrant;
import model.old.domain.Member;
import model.old.domain.Project;
import model.old.domain.ReceptionWork;

public class ReceptionWorkFormController implements Initializable {

    @FXML
    private CheckBox chBoxImpactBA;
    @FXML
    private TextField txtFieldReceptionWorkName;
    @FXML
    private DatePicker dtpPublicationDate;
    @FXML
    private TextField txtFieldCountry;
    @FXML
    private ComboBox<String> cboxProject;
    @FXML
    private TableView<IntegrantTable> tvIntegrant;
    @FXML
    private TableColumn<IntegrantTable, String> colIntegrantName;
    @FXML
    private TableColumn<IntegrantTable, RadioButton> colIntegrantParticipation;
    @FXML
    private TableView<CollaboratorTable> tvCollaborator;
    @FXML
    private TableColumn<CollaboratorTable, String> colCollaboratorName;
    @FXML
    private TableColumn<CollaboratorTable, RadioButton> colCollaboratorParticipation;
    @FXML
    private TextField txtFieldStudent;
    @FXML
    private Button btnAddStudent;
    @FXML
    private Button btnDeleteStudent;
    @FXML
    private ListView<String> lvStudent;
    @FXML
    private TextField txtFieldEstimatedDurationMonth;
    @FXML
    private ComboBox<String> cboBoxStatus;
    @FXML
    private ComboBox<String> cboBoxModality;
    @FXML
    private TextArea txtAreaDescription;
    @FXML
    private TextField txtFieldRequirements;
    @FXML
    private ListView<String> lvRequirements;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnExit;
    @FXML
    private Button btnAddRequirement;
    @FXML
    private Button btnDeleteRequirements;
    @FXML
    private HBox hbReceptionOptions;
    @FXML
    private Label lbUserName;
    @FXML
    private Button btnAddDocument;
    @FXML
    private Button btnReplaceDocument;
    @FXML
    private Label lbDocumentName;
    
    private final ReceptionWorkDAO RECEPTIONWORK_DAO = new ReceptionWorkDAO();
    private final IntegrantDAO INTEGRANT_DAO = new IntegrantDAO();
    private final CollaboratorDAO COLLABORATOR_DAO = new CollaboratorDAO();
    private final ProjectDAO PROJECT_DAO = new ProjectDAO();
    private List<Button> optionButtons;
    private DialogBox TESTBOX;
    private String FILE = null;
    private Integrant token;
    private String UrlReception = "";
    private final ObservableList<String> MODALITYLIST = FXCollections.observableArrayList("Tesis", "Tesina", "Memoria", "Proyecto de Inversion", "Reporte");
    private final ObservableList<String> STATUSLIST = FXCollections.observableArrayList("Propuesto", "Asignado", "Cancelado", "Terminado");
   
    @Override
    public void initialize(URL url, ResourceBundle rb){
        colIntegrantName.setCellValueFactory(new PropertyValueFactory<IntegrantTable, String>("integrantName"));
        colIntegrantParticipation.setCellValueFactory(new PropertyValueFactory<IntegrantTable, RadioButton>("participation"));
        colCollaboratorName.setCellValueFactory(new PropertyValueFactory<CollaboratorTable, String>("collaboratorName"));
        colCollaboratorParticipation.setCellValueFactory(new PropertyValueFactory<CollaboratorTable, RadioButton>("participation"));
        cboxProject.setItems(makeitemsProject());
        cboBoxModality.setItems(MODALITYLIST);
        optionButtons = Arrays.asList(
            btnUpdate, btnSave, 
            btnExit
        );
        hbReceptionOptions.getChildren().removeAll(optionButtons);        
        cboBoxStatus.setItems(STATUSLIST);
    }    

    private void showReceptionWorkSaveForm(){
        hbReceptionOptions.getChildren().addAll(btnSave, btnExit);
        tvIntegrant.setItems(makeitemsIntegrant());
        tvCollaborator.setItems(makeitemsCollaborator());
        
    }
    
    private void showReceptionWorkUpdateForm(String receptionWorkUrl){
        hbReceptionOptions.getChildren().addAll(btnUpdate, btnExit);
        ReceptionWork receptionWork = RECEPTIONWORK_DAO.getEvidenceByUrl(receptionWorkUrl);
        this.chBoxImpactBA.setSelected(receptionWork.getImpactAB());
        this.txtFieldReceptionWorkName.setText(receptionWork.getEvidenceTitle());
        this.txtFieldCountry.setText(receptionWork.getCountry());
        this.dtpPublicationDate.setValue(LocalDate.parse(receptionWork.getPublicationDate()));
        this.cboxProject.setValue(receptionWork.getProjectName());
        tvIntegrant.setItems(makeitemsIntegrant());
        tvCollaborator.setItems(makeitemsCollaborator());
        this.lvStudent.setItems(makeItemsStudent(receptionWork.getStudents()));
        this.txtFieldEstimatedDurationMonth.setText(Integer.toString(receptionWork.getEstimatedDurationInMonths()));
        this.cboBoxStatus.setValue(receptionWork.getStatus());
        this.cboBoxModality.setValue(receptionWork.getModality());
        this.txtAreaDescription.setText(receptionWork.getDescription());
        this.lvRequirements.setItems(makeItemsRequirements(receptionWork.getRequirements()));
    }
    
    public void receiveReceptionWorkUpdateToken(ReceptionWork receptionWork, Integrant integrantToken){
        this.token = integrantToken;
        this.lbUserName.setText(token.getFullName());
        this.UrlReception = receptionWork.getUrlFile();
        showReceptionWorkUpdateForm(receptionWork.getUrlFile());
    }
    
    public void receiveReceptionWorkSaveToken(Integrant integrantToken){
        this.token = integrantToken;
        this.lbUserName.setText(token.getFullName());
        showReceptionWorkSaveForm();
    }
    
    @FXML
    private void saveReceptionWork(ActionEvent event){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        List<String> listRequirements = new ArrayList<>();
        if (UrlReception != null){
            try{
                this.isValidForm();
                ReceptionWork receptionWork = new ReceptionWork(
                    UrlReception,
                    cboxProject.getValue(),
                    chBoxImpactBA.isSelected(),
                    "Trabajo Recepcional",
                    txtFieldReceptionWorkName.getText(),
                    token.getFullName(),
                    dateFormat.format(date),
                    token.getSchooling().toString(),
                    ValidatorForm.convertJavaDateToSQlDate(dtpPublicationDate),
                    txtFieldCountry.getText(),
                    txtAreaDescription.getText(),
                    cboBoxStatus.getValue(),
                    0,
                    Integer.parseInt(txtFieldEstimatedDurationMonth.getText()),
                    cboBoxModality.getValue()
                );
                receptionWork.setRequirements(lvRequirements.getItems());
                receptionWork.setStudents(lvStudent.getItems());
                receptionWork.setIntegrants(IntegrantList());
                receptionWork.setCollaborators(CollaboratorList());
                RECEPTIONWORK_DAO.addNewEvidence(receptionWork);
                WindowControllerB.getWindowController().showInfoAlert(event, "Trabajo recepcional registrado correctamente");
                FXMLLoader loader = WindowControllerB.getWindowController().changeWindow("EvidenceList.fxml", txtFieldReceptionWorkName);
                EvidenceListController controller = loader.getController();
                controller.showGeneralResumeEvidences(token);
            }catch(InvalidFormException ie){
                WindowControllerB.getWindowController().showErrorAlert(new ActionEvent(), ie.getMessage());
            }
        }else{
            WindowControllerB.getWindowController().showErrorAlert(event, "No se a subido el archivo de la evidencia");
        } 
    }
    
    @FXML
    private void updateReceptioWork(ActionEvent event){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        List<String> listRequirements = new ArrayList<>();
         try{
            this.isValidForm();
            ReceptionWork receptionWork = new ReceptionWork(
                UrlReception,
                cboxProject.getValue(),
                chBoxImpactBA.isSelected(),
                "Trabajo Recepcional",
                txtFieldReceptionWorkName.getText(),
                token.getFullName(),
                dateFormat.format(date),
                token.getSchooling().toString(),
                ValidatorForm.convertJavaDateToSQlDate(dtpPublicationDate),
                txtFieldCountry.getText(),
                txtAreaDescription.getText(),
                cboBoxStatus.getValue(),
                0,
                Integer.parseInt(txtFieldEstimatedDurationMonth.getText()),
                cboBoxModality.getValue()
            );
            receptionWork.setRequirements(lvRequirements.getItems());
            receptionWork.setStudents(lvStudent.getItems());
            receptionWork.setIntegrants(IntegrantList());
            receptionWork.setCollaborators(CollaboratorList());
            RECEPTIONWORK_DAO.updateEvidence(receptionWork, UrlReception);
            WindowControllerB.getWindowController().showInfoAlert(event, "Trabajo recepcional actualizado correctamente");
            FXMLLoader loader = WindowControllerB.getWindowController().changeWindow("EvidenceList.fxml", txtFieldReceptionWorkName);
            EvidenceListController controller = loader.getController();
            controller.showGeneralResumeEvidences(token);
        }catch(InvalidFormException ie){
            WindowControllerB.getWindowController().showErrorAlert(new ActionEvent(), ie.getMessage());
        }
    }

    @FXML
    private void exit(ActionEvent event){
        FXMLLoader loader = WindowControllerB.getWindowController().changeWindow("EvidenceList.fxml", txtFieldReceptionWorkName);
        EvidenceListController controller = loader.getController();
        controller.showGeneralResumeEvidences(token);
    }
    
    public void isValidForm() throws InvalidFormException {
        ValidatorForm.chechkAlphabeticalField(txtFieldReceptionWorkName, 5,200);
        ValidatorForm.checkNotEmptyDateField(dtpPublicationDate);
        ValidatorForm.chechkAlphabeticalField(txtFieldCountry, 3,90);
        ValidatorForm.isComboBoxSelected(cboxProject);
        ValidatorForm.isNumberData(txtFieldEstimatedDurationMonth, 2);
        ValidatorForm.isComboBoxSelected(cboBoxStatus);
        ValidatorForm.isComboBoxSelected(cboBoxModality);
        ValidatorForm.chechkAlphabeticalArea(txtAreaDescription, 1 ,500);
    }
    
    private ObservableList<IntegrantTable> makeitemsIntegrant(){
        ObservableList<IntegrantTable> itemsIntegrant = FXCollections.observableArrayList();
        List<Member> integrantsRfcName = INTEGRANT_DAO.getMembers(token.getBodyAcademyKey());
        for(int i = 0; i < integrantsRfcName.size(); i++){
            IntegrantTable participationIntegrantTable = new IntegrantTable(integrantsRfcName.get(i).getRfc(), integrantsRfcName.get(i).getFullName());
            itemsIntegrant.add(participationIntegrantTable);
        }
        return itemsIntegrant;
    }
    
    private ObservableList<CollaboratorTable> makeitemsCollaborator(){
        ObservableList<CollaboratorTable> itemsCollaborator = FXCollections.observableArrayList();
        List<Member> collaboratorRfcName = COLLABORATOR_DAO.getMembers(token.getBodyAcademyKey());
        for(int i = 0; i < collaboratorRfcName.size(); i++){
            CollaboratorTable participationCollaboratorTable = new CollaboratorTable(collaboratorRfcName.get(i).getRfc(), collaboratorRfcName.get(i).getFullName());
            itemsCollaborator.add(participationCollaboratorTable);
        }
        return itemsCollaborator;
    }
    
    private ObservableList<String> makeItemsRequirements(List<String> requirementstList){
        ObservableList<String> itemsRequirements= FXCollections.observableArrayList();
        for(String requirements : requirementstList){
            itemsRequirements.add(requirements);
        }
        return itemsRequirements;
    }
    
    private ObservableList<String> makeItemsStudent(List<String> studentList){
        ObservableList<String> itemsStudent= FXCollections.observableArrayList();
        for(String student : studentList){
            itemsStudent.add(student);
        }
        return itemsStudent;
    }

    private ObservableList<String> makeitemsProject(){
        ObservableList<String> itemsProject = FXCollections.observableArrayList();
        List<Project> projectList = PROJECT_DAO.getProjectList();
        for(int i = 0; i < projectList.size(); i++){
            String projectName = projectList.get(i).getProjectName();
            itemsProject.add(projectName);
        }
        return itemsProject;
    }
    
    @FXML
    private void addStudent(ActionEvent event){
        List<String> itemsStudent = new ArrayList<>();
        if(txtFieldStudent.getText() == null){
            WindowControllerB.getWindowController().showErrorAlert(new ActionEvent(), "No se puede agregar un estudiante vacio");
        }else{
            itemsStudent.add(txtFieldStudent.getText());
            lvStudent.getItems().addAll(itemsStudent);
            txtFieldStudent.setText("");
        }
    }
    
    @FXML
    private void addRequirement(ActionEvent event){
        List<String> itemsRequirements = new ArrayList<>();
        if(txtFieldRequirements.getText() == null){
            WindowControllerB.getWindowController().showErrorAlert(new ActionEvent(), "No se puede agregar un estudiante vacio");
        }else{
            itemsRequirements.add(txtFieldRequirements.getText());
            lvRequirements.getItems().addAll(itemsRequirements);
            txtFieldRequirements.setText("");
        }
    }
   
    private List<Integrant> IntegrantList(){
        List<Integrant> itemsIntegrantSelected = new ArrayList<>();
        for(IntegrantTable integrantTable : tvIntegrant.getItems()){
            if(integrantTable.getParticipation().isSelected()){
               itemsIntegrantSelected.add(new Integrant(integrantTable.getIntegrantRfc(), integrantTable.getIntegrantName()));
            }
        }
        return itemsIntegrantSelected;
    }
    
    private List<Collaborator> CollaboratorList(){
        List<Collaborator> itemsCollaboratorSelected = new ArrayList<>();
        for(CollaboratorTable collaboratorTable : tvCollaborator.getItems()){
            if(collaboratorTable.getParticipation().isSelected()){
               itemsCollaboratorSelected.add(new Collaborator(collaboratorTable.getCollaboratorRfc(), collaboratorTable.getCollaboratorName()));
            }
        }
        return itemsCollaboratorSelected;
    }

    @FXML
    private void deleteStudent(ActionEvent event) {
        int indexSelection = lvStudent.getSelectionModel().getSelectedIndex();
        if(indexSelection >= 0){
            lvStudent.getItems().remove(indexSelection);
        }else{
            WindowControllerB.getWindowController().showErrorAlert(new ActionEvent(), "No a seleccionado un acuerdo");
        }
    }

    @FXML
    private void deleteRequirement(ActionEvent event) {
        int indexSelection = lvRequirements.getSelectionModel().getSelectedIndex();
        if(indexSelection >= 0){
            lvRequirements.getItems().remove(indexSelection);
        }else{
            WindowControllerB.getWindowController().showErrorAlert(new ActionEvent(), "No a seleccionado un acuerdo");
        }
    }

    @FXML
    private void addDocument(ActionEvent event){
        DialogBox dialogBox = new DialogBox((Stage)((Node)event.getSource()).getScene().getWindow());
        String documentPath = dialogBox.openDialogFileSelector();
        if(documentPath != null){
            String documentName = dialogBox.getFileNameSelected();
            try{
                checkExistFile(documentName);
                uploadFile(documentPath, documentName);
            }catch(InvalidFormException ex){
                WindowControllerB.getWindowController().showErrorAlert(event, ex.getMessage());
            }
        }
    }

    @FXML
    private void replaceDocument(ActionEvent event){
        DialogBox dialogBox = new DialogBox((Stage)((Node)event.getSource()).getScene().getWindow());
        String documentPath = dialogBox.openDialogFileSelector();
        if(documentPath != null){
            String documentName = dialogBox.getFileNameSelected();
            try{
                new FTPClient().deleteFileFromFilesSystemByName(this.UrlReception);
                checkExistFile(documentName);
                uploadFile(documentPath, documentName);
            }catch(InvalidFormException ex){
                WindowControllerB.getWindowController().showErrorAlert(event, ex.getMessage());
            }
        }
    }
    
    private void uploadFile(String path, String fileName) throws InvalidFormException {
        String link = new FTPClient().saveFileIntoFilesSystem(path, fileName);
        if(link != null){
            this.lbDocumentName.setText(link);
            UrlReception = link;
            this.btnAddDocument.setDisable(true);
            this.btnReplaceDocument.setDisable(false);
        }else{
            throw new InvalidFormException("Error, parece que el sistema no ha respondido correctamente");
        }
    }
    
    private void checkExistFile(String fileName) throws InvalidFormException {
        if(new FTPClient().checkExistFile(fileName)){
            throw new InvalidFormException("El archivo ya existe en el sistema");
        }
    }
}
