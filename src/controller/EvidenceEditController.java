/*
 * @author Estefanía 
 * @versión v1.0
 * Last modification date: 17-06-2021
 */

package controller;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import controller.pattern.DialogBox;
import controller.pattern.GenericWindowDriver;
import controller.pattern.InvalidFormException;
import controller.pattern.ValidatorForm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.dataaccess.ArticleDAO;
import model.dataaccess.BookDAO;
import model.dataaccess.CollaboratorDAO;
import model.dataaccess.EvidenceDAO;
import model.dataaccess.IntegrantDAO;
import model.dataaccess.ProjectDAO;
import model.dataaccess.PrototypeDAO;
import utils.FtpClient;
import model.domain.Article;
import model.domain.Book;
import model.domain.Collaborator;
import model.domain.Evidence;
import model.domain.Integrant;
import model.domain.Member;
import model.domain.Prototype;

public class EvidenceEditController implements Initializable{

    @FXML
    private Label lbWindowTitle;
    @FXML
    private Label lbUsername;
    @FXML
    private Button btnAddEvidence;
    @FXML
    private Button btnUpdateEvidence;
    @FXML
    private Button btnCloseWindow;
    @FXML
    private CheckBox chBoxImpactAB;
    @FXML
    private Label lbTypeEvidence;
    @FXML
    private TextField txtFieldEvidenceTittle;
    @FXML
    private DatePicker dtpPublicationDate;
    @FXML
    private TextField txtFieldPublicationCountry;
    @FXML
    private ComboBox<String> cboBoxInvestigationProject;
    @FXML
    private ComboBox<String> cboBoxStudyDegree;
    @FXML
    private Button btnAddRowIntegrantTable;
    @FXML
    private Button btnRemoveRowIntegrantTable;
    @FXML
    private ComboBox<Member> cboBoxIntegrantsName;
    @FXML
    private TableView<Integrant> tvIntegrant;

    @FXML
    private TableColumn<Integrant, String> colRFCIntegrant;

    @FXML
    private TableColumn<Integrant, String> colIntegrantName;
    @FXML
    private Button btnAddRowCollaboratorTable;
    @FXML
    private Button btnRemoveRowCollaboratorTable;
    @FXML
    private ComboBox<Member> cboBoxCollaboratorsName;
    @FXML
    private TableView<Collaborator> tvCollaborador;

    @FXML
    private TableColumn<Collaborator, String> colRFCCollaborator;

    @FXML
    private TableColumn<Collaborator, String> colCollaboratorName;
    @FXML
    private Tab tabBook;
    @FXML
    private TextField txtFieldPublisher;
    @FXML
    private TextField txtFieldISBN;
    @FXML
    private TextField txtFieldEditionsNumber;
    @FXML
    private Tab tabArticle;
    @FXML
    private TextField txtFieldMagazineName;
    @FXML
    private TextField txtFieldMagazineEditorial;
    @FXML
    private TextField txtFieldISNN;
    @FXML
    private Tab tabPrototype;
    @FXML
    private TextArea txtAreaFeatures;
    @FXML
    private Button btnAddRowStudentsTable;
    @FXML
    private Button btnRemoveRowStudentsTable;
    @FXML
    private ListView<String> lvStudent;
    @FXML
    private TextField txtFieldStudentName;
    @FXML
    private Button btnAddDocument;
    @FXML
    private Button btnReplaceDocument;
    @FXML
    private ImageView imgViewPDFEvidence;
    @FXML
    private Label lbDocumentName;
    
    private final IntegrantDAO INTEGRANT_DAO = new IntegrantDAO();
    private final CollaboratorDAO COLLABORATOR_DAO = new CollaboratorDAO();
    private final ProjectDAO PROJECT_DAO = new ProjectDAO();
    private EvidenceDAO EVIDENCE_DAO;
    private Evidence evidence;
    private Integrant token;
    private String urlFileEvidence;
    Calendar date = new GregorianCalendar();
    
    @Override
    public void initialize(URL url, ResourceBundle rb){
        setWindowInformationCamps();
    }    
    
    public void receiveBookAndToken(Book book, Integrant token){
        this.token = token;
        this.lbUsername.setText(token.getFullName());
        this.urlFileEvidence = book.getUrlFile();
        determineBookEvidence(book);
        determineTypeOperationBook(book);
        
    }
    public void receiveArticleAndToken(Article article, Integrant token){
        this.token = token;
        this.lbUsername.setText(token.getFullName());
        this.urlFileEvidence = article.getUrlFile();
        determineArticleEvidence(article);
        determineTypeOperationArticle(article);
        
    }
    
    public void receivePrototypeAndToken(Prototype prototype, Integrant token){
        this.token = token;
        this.lbUsername.setText(token.getFullName());
        this.urlFileEvidence = prototype.getUrlFile();
        determinePrototypeEvidence(prototype);
        determineTypeOperationPrototype(prototype);
        
    }
    
    private void setEvidenceInformationModification(){
        if(evidence.getImpactAB()){
            this.chBoxImpactAB.setSelected(true);
        }
        this.txtFieldEvidenceTittle.setText(evidence.getEvidenceTitle());
        LocalDate publicationDate = LocalDate.parse(evidence.getPublicationDate());
        this.dtpPublicationDate.setValue(publicationDate);
        this.txtFieldPublicationCountry.setText(evidence.getCountry());
        this.cboBoxInvestigationProject.setValue(evidence.getProjectName());
        this.cboBoxStudyDegree.setValue(evidence.getStudyDegree());
    }
    
    private void determineBookEvidence(Book book){
        this.evidence = new Book();
        this.EVIDENCE_DAO = new BookDAO();
        this.lbTypeEvidence.setText("Libro");
        this.tabBook.setDisable(false);
    }
    
    private void determinePrototypeEvidence(Prototype prototype){
        this.evidence = new Prototype();
        this.EVIDENCE_DAO = new PrototypeDAO();
        this.lbTypeEvidence.setText("Prototipo");
        this.tabPrototype.setDisable(false);
    }
    
    private void determineArticleEvidence(Article article){
        this.evidence = new Article();
        this.EVIDENCE_DAO = new ArticleDAO();
        this.lbTypeEvidence.setText("Artículo");
        this.tabArticle.setDisable(false);
    }
    
    
    private void determineTypeOperationBook(Book book){
        if(book.getUrlFile() == null){
            this.btnAddEvidence.setVisible(true);
            prepareIntegrantTable();
            prepareCollaboratorTable();
            prepareStudentTable();
        }else{
            this.evidence = book;
            this.btnUpdateEvidence.setVisible(true);
            modifyWindowForModification();
            setEvidenceInformationModification();
            setBookInformationModification(book);
            prepareIntegrantTable();
            prepareCollaboratorTable();
            prepareStudentTable();
        }
    }
    
    private void determineTypeOperationArticle(Article article){
        if(article.getUrlFile() == null){
            this.btnAddEvidence.setVisible(true);
            prepareIntegrantTable();
            prepareCollaboratorTable();
            prepareStudentTable();
        }else{
            this.evidence = article;
            this.btnUpdateEvidence.setVisible(true);
            modifyWindowForModification();
            setEvidenceInformationModification();
            setArticleInformationModification(article);
            prepareIntegrantTable();
            prepareCollaboratorTable();
            prepareStudentTable();
        }
    }
    
    private void determineTypeOperationPrototype(Prototype prototype){
        if(prototype.getUrlFile() == null){
            this.btnAddEvidence.setVisible(true);
            prepareIntegrantTable();
            prepareCollaboratorTable();
            prepareStudentTable();
        }else{
            this.evidence = prototype;
            this.btnUpdateEvidence.setVisible(true);
            modifyWindowForModification();
            setEvidenceInformationModification();
            setPrototypeInformationModification(prototype);
            prepareIntegrantTable();
            prepareCollaboratorTable();
            prepareStudentTable();
        }
    }
    
    private void setBookInformationModification(Book book){
        this.txtFieldPublisher.setText(book.getPublisher());
        this.txtFieldEditionsNumber.setText(book.getEditionsNumber() + "");
        this.txtFieldISBN.setText(book.getIsbn());
    }
    
    private void setPrototypeInformationModification(Prototype prototype){
        this.txtAreaFeatures.setText(prototype.getFeatures());
    }
    
    private void setArticleInformationModification(Article article){
        this.txtFieldMagazineName.setText(article.getMagazineName());
        this.txtFieldMagazineEditorial.setText(article.getMagazineEditorial());
        this.txtFieldISNN.setText(article.getIsnn());
    }
    
    private void setWindowInformationCamps(){
        this.cboBoxCollaboratorsName.setItems(makeitemsCollaboratorsListForComboBox());
        this.cboBoxIntegrantsName.setItems(makeitemsIntegrantsListForComboBox());
        this.cboBoxInvestigationProject.setItems(makeitemsProjectListForComboBox());
        this.cboBoxStudyDegree.setItems(makeitemsStudiesDegreeListForComboBox());
    }
    
    private void modifyWindowForModification(){
        this.lbWindowTitle.setText("MODIFICAR PRODUCCIÓN");
        this.lbDocumentName.setText(evidence.getUrlFile());
        this.lbDocumentName.setVisible(true);
        this.imgViewPDFEvidence.setVisible(true);
        this.btnReplaceDocument.setVisible(true);
        this.btnReplaceDocument.setDisable(false);
        this.btnAddDocument.setVisible(false);
        this.btnUpdateEvidence.setVisible(true);
    }
    
    @FXML
    private void addDocument(ActionEvent event) {
        DialogBox dialogBox = new DialogBox((Stage)((Node)event.getSource()).getScene().getWindow());
        String documentPath = dialogBox.openDialogFileSelector();
        if(documentPath != null){
            String documentName = dialogBox.getFileNameSelected();
            evidence.setUrlFile(dialogBox.getFileNameSelected());
            try{
                checkExistFile(documentName);
                uploadFile(documentPath, documentName);
            }catch(InvalidFormException ex){
                GenericWindowDriver.getGenericWindowDriver().showErrorAlert(event, ex.getMessage());
            }
        }
    }
    
    private void uploadFile(String path, String fileName) throws InvalidFormException{
        String link = new FtpClient().saveFileIntoFilesSystem(path, fileName);
        if(link != null){
            this.lbDocumentName.setText(link);
            this.evidence.setUrlFile(link);
            this.btnAddDocument.setDisable(true);
            this.btnReplaceDocument.setDisable(false);
        }else{
            throw new InvalidFormException("Error, parece que el sistema no ha respondido correctamente");
        }
    }
    
    private void checkExistFile(String fileName) throws InvalidFormException{
        if(new FtpClient().checkExistFile(fileName)){
            throw new InvalidFormException("El archivo ya existe en el sistema");
        }
    }
    
    public void validateEvidenceInformation() throws InvalidFormException{
        ValidatorForm.chechkAlphabeticalField(this.txtFieldEvidenceTittle, 5, 100);
        ValidatorForm.checkNotEmptyDateField(dtpPublicationDate);
        validateDate();
        ValidatorForm.chechkAlphabeticalField(this.txtFieldPublicationCountry, 3, 90);
        ValidatorForm.isComboBoxSelected(this.cboBoxInvestigationProject);
        ValidatorForm.isComboBoxSelected(this.cboBoxStudyDegree);
        if(this.evidence.getUrlFile() == null){
            throw new InvalidFormException("No has subido ningún archivo");
        }
    }
    
    private void validateDate() throws InvalidFormException{
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat ("yyyy-MM-dd");
            Date meetingDate = dateFormat.parse(this.dtpPublicationDate.getValue().toString());
            Date actualDate = dateFormat.parse(date.get(Calendar.YEAR) + "-" + (date.get(Calendar.MONTH) + 1) + "-" + date.get(Calendar.DAY_OF_MONTH));
            if(meetingDate.after(actualDate)){
                this.dtpPublicationDate.setStyle("-fx-border-color: red;");
                throw new InvalidFormException("La fecha de publicación no puede ser mayor a la fecha actual");
            }
        } catch (ParseException ex) {
            Logger.getLogger(MeetingEditController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void validateBookInformation() throws InvalidFormException{
        ValidatorForm.chechkAlphabeticalField(this.txtFieldPublisher, 2, 30);
        ValidatorForm.isIntegerNumberData(this.txtFieldEditionsNumber, 3);
        ValidatorForm.chechkAlphabeticalField(this.txtFieldISBN, 13, 13);
        ValidatorForm.isNumberData(this.txtFieldISBN, 13);
    }
    
    public void validateArticleInformation() throws InvalidFormException{
        ValidatorForm.chechkAlphabeticalField(this.txtFieldMagazineName, 1, 100);
        ValidatorForm.chechkAlphabeticalField(this.txtFieldMagazineEditorial, 1, 100);
        ValidatorForm.chechkAlphabeticalField(this.txtFieldISNN, 13, 13);
        ValidatorForm.isNumberData(this.txtFieldISNN, 13);
    }
    
    private void getOutEvidenceInformation(){
        this.evidence.setEvidenceTitle(this.txtFieldEvidenceTittle.getText());
        this.evidence.setCountry(this.txtFieldPublicationCountry.getText());
        this.evidence.setPublicationDate(this.dtpPublicationDate.getValue().toString());
        
        if(this.chBoxImpactAB.isSelected()){
            this.evidence.setImpactAB(true);
        }else{
            this.evidence.setImpactAB(false);
        }
        
        this.evidence.setRegistrationResponsible(token.getRfc());
        this.evidence.setStudyDegree(this.cboBoxStudyDegree.getSelectionModel().getSelectedItem());
        this.evidence.setProjectName(this.cboBoxInvestigationProject.getSelectionModel().getSelectedItem());
        this.evidence.setEvidenceType(this.lbTypeEvidence.getText());
    }
    
    private void getOutBookInformation(){
        ((Book)this.evidence).setPublisher(this.txtFieldPublisher.getText());
        int editionNumber = Integer.parseInt(this.txtFieldEditionsNumber.getText());
        ((Book)this.evidence).setEditionsNumber(editionNumber);
        ((Book)this.evidence).setIsbn(this.txtFieldISBN.getText());
    }
    
    private void getOurArticleInformation(){
        ((Article)this.evidence).setMagazineName(this.txtFieldMagazineName.getText());
        ((Article)this.evidence).setMagazineEditorial(this.txtFieldMagazineEditorial.getText());
        ((Article)this.evidence).setIsnn(this.txtFieldISNN.getText());
    }
    
    

    @FXML
    private void addEvidence(ActionEvent event){
        try{
            this.validateEvidenceInformation();
            this.getOutEvidenceInformation();
            this.evidence.setRegistrationDate(
                date.get(Calendar.YEAR) + "-" + (date.get(Calendar.MONTH) + 1) + "-" + date.get(Calendar.DAY_OF_MONTH)
            );
            if(evidence instanceof Book){
                this.validateBookInformation();
                this.getOutBookInformation();
            }
            if(evidence instanceof Article){
                this.validateArticleInformation();
                this.getOurArticleInformation();
            }
            if(evidence instanceof Prototype){
                ValidatorForm.chechkAlphabeticalArea(this.txtAreaFeatures, 1, 1000);
                ((Prototype)this.evidence).setFeatures(this.txtAreaFeatures.getText());
            }
            if(EVIDENCE_DAO.addNewEvidence(this.evidence)){
                GenericWindowDriver.getGenericWindowDriver().showInfoAlert(event, "Evidencia registrada con éxito");
            }else{
                GenericWindowDriver.getGenericWindowDriver().showErrorAlert(event, "Error en el sistema, favor de ponerse en contacto con sopoerte técnico");
            }
            FXMLLoader loader = GenericWindowDriver.getGenericWindowDriver().changeWindow("EvidenceList.fxml", btnCloseWindow);
            EvidenceListController controller = loader.getController();
            controller.showGeneralResumeEvidences(token);
        }catch(InvalidFormException ex){
            GenericWindowDriver.getGenericWindowDriver().showErrorAlert(event, ex.getMessage());
        }
    }

    @FXML
    private void addRowCollaboratorTable(ActionEvent event){
        try{
            ValidatorForm.isComboBoxSelected(cboBoxCollaboratorsName);
            Member collaborator = this.cboBoxCollaboratorsName.getValue();
            if(validateNotRepeatCollaborator(collaborator)){
                GenericWindowDriver.getGenericWindowDriver().showWarningAlert(event, "Este colaborador ya ha sido ingresado");
                this.cboBoxCollaboratorsName.setValue(null);
            }else{
                this.evidence.getCollaborators().add((Collaborator) collaborator);
                this.tvCollaborador.setItems(makeitemsCollaboratorsListForTable());
                this.cboBoxCollaboratorsName.setValue(null);
            }
        }catch(InvalidFormException ex){
            GenericWindowDriver.getGenericWindowDriver().showErrorAlert(event, ex.getMessage());
        }
    }
    
    private boolean validateNotRepeatCollaborator(Member collaborator){
        boolean repeated = false;
        for(int i = 0; i < this.evidence.getCollaborators().size(); i++){
            if(collaborator.getRfc().equals(this.evidence.getCollaborators().get(i).getRfc())){
                repeated = true;
            }
        }
        return repeated;
    }
    
    private boolean validateNotRepeatIntegrant(Member integrant){
        boolean repeated = false;
        for(int i = 0; i < this.evidence.getIntegrants().size(); i++){
            if(integrant.getRfc().equals(this.evidence.getIntegrants().get(i).getRfc())){
                repeated = true;
            }
        }
        return repeated;
    }

    @FXML
    private void addRowIntegrantTable(ActionEvent event){
        try{
            ValidatorForm.isComboBoxSelected(cboBoxIntegrantsName);
            Member integrant = this.cboBoxIntegrantsName.getValue();
            if(validateNotRepeatIntegrant(integrant)){
                GenericWindowDriver.getGenericWindowDriver().showWarningAlert(event, "Este integrante ya ha sido ingresado");
                this.cboBoxIntegrantsName.setValue(null);
            }else{
                this.evidence.getIntegrants().add((Integrant) integrant);
                this.tvIntegrant.setItems(makeitemsIntegrantsListForTable());
                this.cboBoxIntegrantsName.setValue(null);
            }
        }catch(InvalidFormException ex){
            GenericWindowDriver.getGenericWindowDriver().showErrorAlert(event, ex.getMessage());
        }
    }

    @FXML
    private void addRowStudentsTable(ActionEvent event){
        try{
            ValidatorForm.chechkAlphabeticalField(this.txtFieldStudentName, 3, 50);
            String studentName = this.txtFieldStudentName.getText();
            this.evidence.getStudents().add(studentName);
            this.lvStudent.setItems(makeitemsStudentsListForTable());
            this.txtFieldStudentName.clear();
        }catch(InvalidFormException ex){
            GenericWindowDriver.getGenericWindowDriver().showErrorAlert(event, ex.getMessage());
        }
    }

    @FXML
    private void closeWindow(ActionEvent event){
        Optional<ButtonType> action = GenericWindowDriver.getGenericWindowDriver().showConfirmacionAlert(event,
            "¿Seguro que desea salir? No se guardará la información");
        if(action.get() == ButtonType.OK){
            FXMLLoader loader = GenericWindowDriver.getGenericWindowDriver().changeWindow("EvidenceList.fxml", btnCloseWindow);
            EvidenceListController controller = loader.getController();
            controller.showGeneralResumeEvidences(token);
        }
    }

    @FXML
    private void removeRowCollaboratorTable(ActionEvent event){
        Collaborator collaboratorRemove = this.tvCollaborador.getSelectionModel().getSelectedItem();
        this.evidence.getCollaborators().remove(collaboratorRemove);
        this.tvCollaborador.setItems(makeitemsCollaboratorsListForTable());
    }

    @FXML
    private void removeRowIntegrantTable(ActionEvent event){
        Integrant integrantRemove = this.tvIntegrant.getSelectionModel().getSelectedItem();
        this.evidence.getIntegrants().remove(integrantRemove);
        this.tvIntegrant.setItems(makeitemsIntegrantsListForTable());
    }

    @FXML
    private void removeRowStudentsTable(ActionEvent event){
        String studentRemove = this.lvStudent.getSelectionModel().getSelectedItem();
        this.evidence.getStudents().remove(studentRemove);
        this.lvStudent.setItems(makeitemsStudentsListForTable());
    }

    @FXML
    private void replaceDocument(ActionEvent event){
        DialogBox dialogBox = new DialogBox((Stage)((Node)event.getSource()).getScene().getWindow());
        String documentPath = dialogBox.openDialogFileSelector();
        if(documentPath != null){
            String documentName = dialogBox.getFileNameSelected();
            try{
                new FtpClient().deleteFileFromFilesSystemByName(this.evidence.getUrlFile());
                checkExistFile(documentName);
                uploadFile(documentPath, documentName);
            }catch(InvalidFormException ex){
                GenericWindowDriver.getGenericWindowDriver().showErrorAlert(event, ex.getMessage());
            }
        }
    }

    @FXML
    private void updateEvidence(ActionEvent event){
        try{
            this.validateEvidenceInformation();
            this.getOutEvidenceInformation();
            if(evidence instanceof Book){
                this.validateBookInformation();
                this.getOutBookInformation();
            }
            if(evidence instanceof Article){
                this.validateArticleInformation();
                this.getOurArticleInformation();
            }
            if(evidence instanceof Prototype){
                ValidatorForm.chechkAlphabeticalArea(this.txtAreaFeatures, 1, 1000);
                ((Prototype)this.evidence).setFeatures(this.txtAreaFeatures.getText());
            }
            if(EVIDENCE_DAO.updateEvidence(this.evidence, this.urlFileEvidence)){
                GenericWindowDriver.getGenericWindowDriver().showInfoAlert(event, "Evidencia modificada con éxito");
            }else{
                GenericWindowDriver.getGenericWindowDriver().showErrorAlert(event, "Error en el sistema, favor de ponerse en contacto con sopoerte técnico");
            }
            
            FXMLLoader loader = GenericWindowDriver.getGenericWindowDriver().changeWindow("EvidenceList.fxml", btnCloseWindow);
            EvidenceListController controller = loader.getController();
            controller.showGeneralResumeEvidences(token);
        }catch(InvalidFormException ex){
            GenericWindowDriver.getGenericWindowDriver().showErrorAlert(event, ex.getMessage());
        }
        
    }
    
    private void prepareIntegrantTable(){
        this.colRFCIntegrant.setCellValueFactory(new PropertyValueFactory<Integrant, String>("rfc"));
        this.colIntegrantName.setCellValueFactory(new PropertyValueFactory<Integrant, String>("fullName"));
        this.tvIntegrant.setItems(makeitemsIntegrantsListForTable());
    }
    
    private void prepareCollaboratorTable(){
        this.colRFCCollaborator.setCellValueFactory(new PropertyValueFactory<Collaborator, String>("rfc"));
        this.colCollaboratorName.setCellValueFactory(new PropertyValueFactory<Collaborator, String>("fullName"));
        this.tvCollaborador.setItems(makeitemsCollaboratorsListForTable());
    }
    
    private void prepareStudentTable(){
        this.lvStudent.setItems(makeitemsStudentsListForTable());
    }
    
    private ObservableList<Integrant> makeitemsIntegrantsListForTable(){
        ObservableList<Integrant> itemsIntegrants = FXCollections.observableArrayList();
        List<Integrant> integrantList = evidence.getIntegrants();
        itemsIntegrants.addAll(integrantList);
        return itemsIntegrants;
    }
    
    private ObservableList<Collaborator> makeitemsCollaboratorsListForTable(){
        ObservableList<Collaborator> itemsCollaborator = FXCollections.observableArrayList();
        List<Collaborator> collaboratorList = evidence.getCollaborators();
        itemsCollaborator.addAll(collaboratorList);
        return itemsCollaborator;
    }
    
    private ObservableList<String> makeitemsStudentsListForTable(){
        ObservableList<String> itemsStudentNames = FXCollections.observableArrayList();
        List<String> studentList = evidence.getStudents();
        itemsStudentNames.addAll(studentList);
        return itemsStudentNames;
    }
    
    private ObservableList<Member> makeitemsIntegrantsListForComboBox(){
        ObservableList<Member> itemsIntegrantNames = FXCollections.observableArrayList();
        List<Member> integrantsList = INTEGRANT_DAO.getMembers("UV-CA-127");
        itemsIntegrantNames.addAll(integrantsList);
        return itemsIntegrantNames;
    }
    
    private ObservableList<Member> makeitemsCollaboratorsListForComboBox(){
        ObservableList<Member> itemsCollaboratorNames = FXCollections.observableArrayList();
        List<Member> collaboratorsList = COLLABORATOR_DAO.getMembers("UV-CA-127");
        itemsCollaboratorNames.addAll(collaboratorsList);
        return itemsCollaboratorNames;
    }
    
    private ObservableList<String> makeitemsProjectListForComboBox(){
        ObservableList<String> itemsProject = FXCollections.observableArrayList();
        List<String> projectsList = PROJECT_DAO.getProjectNameListForEvidence();
        itemsProject.addAll(projectsList);
        return itemsProject;
    }
    
    private ObservableList<String> makeitemsStudiesDegreeListForComboBox(){
        ObservableList<String> itemsStudies = FXCollections.observableArrayList();
        List<String> studiesList = Arrays.asList("Licenciatura", "Maestría", "Doctorado", "Especialidad");
        itemsStudies.addAll(studiesList);
        return itemsStudies;
    }
}
