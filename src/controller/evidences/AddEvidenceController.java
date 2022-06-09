package controller.evidences;

import assets.utils.DateFormatter;
import assets.utils.SQLStates;
import controller.academicgroup.AddMemberController;
import controller.control.AlertController;
import controller.control.ValidatorController;
import controller.control.validator.Validator;
import controller.control.validator.ValidatorComboBoxBase;
import controller.control.validator.ValidatorComboBoxBaseWithConstraints;
import controller.control.validator.ValidatorTextInputControl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.dao.EvidenceDAO;
import model.dao.ProjectDAO;
import model.domain.Evidence;
import model.domain.Member;
import model.domain.Project;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AddEvidenceController extends ValidatorController implements Initializable {

    @FXML ComboBox<Project> projectsCombobox;
    @FXML ComboBox<String> typeEvidenceCombobox;
    @FXML ComboBox<Evidence> titleBooksCombobox;
    @FXML TextField titleTextField;
    @FXML TextField referTextField;
    @FXML TextField countryTextField;
    @FXML TextField editionNumberTextField;
    @FXML TextField isbnTextField;
    @FXML TextField editorialTextField;
    @FXML TextField pagesTextField;
    @FXML TextField articleISNNTextField;
    @FXML TextField articleIndiceTextField;
    @FXML TextField articleNameMagazineTextField;
    @FXML TextField characteristicsPrototipeTextField;
    @FXML DatePicker publicationDateDataPicker;
    @FXML VBox bookVbox;
    @FXML HBox chapterVbox;
    @FXML VBox prototipeVbox;
    @FXML VBox articleVbox;

    private String idAcademicGroup;
    private  int idResponse;
    private  boolean haveBooks;
    private ObservableList<String> typesEvidences;
    private ObservableList<Project> projectsObservableList;
    private ObservableList<Evidence> booksObservableList;


    public AddEvidenceController(String idAcademicGroup, int idResponse, boolean haveBooks) {
        this.idAcademicGroup = idAcademicGroup;
        this.idResponse = idResponse;
        this.haveBooks = haveBooks;

    }

    public void showStage() {
        loadFXMLFile(getClass().getResource("/view/AddEvidencesView.fxml"), this);
        stage.showAndWait();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        initValidatorToTextInput();
        typesEvidences = FXCollections.observableArrayList();
        projectsObservableList = FXCollections.observableArrayList();

        disableTextFields();
        chargeTypesCombobox();
        chargeProjectsComboBox();
        initListenerCombobox();

    }

    private void disableTextFields(){
        bookVbox.setDisable(true);
        bookVbox.setVisible(false);

        chapterVbox.setDisable(true);
        chapterVbox.setVisible(false);

        prototipeVbox.setDisable(true);
        prototipeVbox.setVisible(false);

        articleVbox.setDisable(true);
        articleVbox.setVisible(false);
    }

    private void chargeProjectsComboBox(){
        ProjectDAO projectDAO= new ProjectDAO();

        try {

            projectsObservableList = projectDAO.getProjectList(idAcademicGroup);

            projectsCombobox.setItems(projectsObservableList);

        } catch(SQLException chargeLGACException) {
            deterMinateSQLState(chargeLGACException);
        }

    }

    private void chargeEvidences(){
        EvidenceDAO evidenceDAO= new EvidenceDAO();

        try {

            booksObservableList = evidenceDAO.verifyExistBooks(idAcademicGroup);

            titleBooksCombobox.setItems(booksObservableList);

        } catch(SQLException chargeLGACException) {
            deterMinateSQLState(chargeLGACException);
        }

    }

    private void chargeTypesCombobox(){

        typesEvidences.setAll("Libro", "Capitulo de libro", "Prototipo", "Articulo");
        typeEvidenceCombobox.setItems(typesEvidences);
    }

    @FXML
    void addEvidenceOnAction(ActionEvent actionEvent) {
        try {
            if(validateInputs()) {
                if(!validateEvidence()) {
                    addEvidence();

                } else {
                    AlertController alertView = AlertController.getInstance();
                    alertView.showActionFailedAlert("¡Al parecer ya existe una evidencia igual\n"+
                            " ,de favor ingrese una distinta!");
                }

            }else {
                AlertController alertView = AlertController.getInstance();
                alertView.showActionFailedAlert("Algunos datos ingresados son inválidos, por favor verifíquelos");
            }
        } catch (SQLException e) {
            AlertController alertView = AlertController.getInstance();
            alertView.showActionFailedAlert("Error: " + e);

        }


    }

    private boolean validateEvidence() throws SQLException {
        boolean validate = false;
        int typeEvidencePosition = typeEvidenceCombobox.getSelectionModel().getSelectedIndex();
        String typeEvidence =  typesEvidences.get(typeEvidencePosition).toString();
        validate = new EvidenceDAO().checkEvidence(titleTextField.getText(), typeEvidence);
        return validate;
    }

    private void addEvidence(){
        Evidence evidenceNew = new Evidence();
        EvidenceDAO evidenceDAO = new EvidenceDAO();

        evidenceNew.setPublicationDate(DateFormatter.getDateFromDatepickerValue(publicationDateDataPicker.getValue()));
        evidenceNew.setEvidenceTitle(titleTextField.getText());
        evidenceNew.setCountry(countryTextField.getText());
        evidenceNew.setIdResponsable(idResponse);
        evidenceNew.setPagesBook(pagesTextField.getText());
        evidenceNew.setDescriptionEvidence(characteristicsPrototipeTextField.getText());
        evidenceNew.setEditorialBook(editorialTextField.getText());
        evidenceNew.setEditionNumberBook(editionNumberTextField.getText());
        evidenceNew.setIsbnBook(isbnTextField.getText());
        evidenceNew.setReference(referTextField.getText());
        int positionProject = projectsCombobox.getSelectionModel().getSelectedIndex();

        int typeEvidencePosition = typeEvidenceCombobox.getSelectionModel().getSelectedIndex();
        String typeEvidence =  typesEvidences.get(typeEvidencePosition).toString();

        evidenceNew.setIdProject(projectsObservableList.get(positionProject).getIdProject());
        evidenceNew.setEvidenceType(typeEvidence);
        evidenceNew.setIdAcademicBody(idAcademicGroup);
        evidenceNew.setNameMagazine(articleNameMagazineTextField.getText());
        evidenceNew.setIndiceMagazine(articleIndiceTextField.getText());
        evidenceNew.setIsnnMagazine(articleISNNTextField.getText());

        try {
            boolean correctAddEvidence = false;

            correctAddEvidence = evidenceDAO.addEvidence(evidenceNew);
            if (correctAddEvidence == true) {
                AlertController.getInstance().showSuccessfullRegisterAlert();
                stage.close();
            }else {
                AlertController alertView = AlertController.getInstance();
                alertView.showActionFailedAlert("Error al guardar evidencia");
            }

        } catch (SQLException addAgreementException) {

            deterMinateSQLState(addAgreementException);

        }
    }

    @FXML
    void returnViewOnAction(ActionEvent actionEvent) {
        try{
            stage.close();
        }catch(Exception returnViewOnActionExeception){
            AlertController alertView = AlertController.getInstance();
            alertView.showActionFailedAlert(" No se pudo volver a la ventana anterior." +
                    "Causa: " + returnViewOnActionExeception);

        }
    }

    public void initListenerComboboxTitleBook() {
        titleBooksCombobox.valueProperty().addListener( (observable, oldValue, newValue) -> {
                countryTextField.setText(newValue.getCountry());
        });
    }

    public void clearTextFields(){
        titleTextField.setText("");
        referTextField.setText("");
        countryTextField.setText("");
        editionNumberTextField.setText("");
        isbnTextField.setText("");
        editorialTextField.setText("");
        pagesTextField.setText("");
        articleISNNTextField.setText("");
        articleIndiceTextField.setText("");
        articleNameMagazineTextField.setText("");
        characteristicsPrototipeTextField.setText("");
    }

    public void initListenerCombobox() {
        typeEvidenceCombobox.valueProperty().addListener( (observable, oldValue, newValue) -> {
            if(newValue != null ) {
                int typeEvidencePosition = typeEvidenceCombobox.getSelectionModel().getSelectedIndex();
                String typeEvidence =  typesEvidences.get(typeEvidencePosition).toString();
                disableTextFields();
                clearTextFields();
                countryTextField.setDisable(false);
                countryTextField.setText("");

                switch (typeEvidence){
                    case "Libro":

                        bookVbox.setDisable(false);
                        bookVbox.setVisible(true);
                        initListenerBook();
                        break;
                    case "Capitulo de libro":

                        if(haveBooks){
                            booksObservableList = FXCollections.observableArrayList();
                            chargeEvidences();
                            initListenerComboboxTitleBook();
                            chapterVbox.setDisable(false);
                            chapterVbox.setVisible(true);
                            initListenerChapterBook();
                        }else {
                            AlertController alertView = AlertController.getInstance();
                            alertView.showActionFailedAlert("Si no hay registrados libros,no puedes agregar un capitulo de libro");
                            break;
                        }

                        break;
                    case "Articulo":

                        articleVbox.setDisable(false);
                        articleVbox.setVisible(true);
                        initListenerArticle();

                        break;
                    case "Prototipo":

                        prototipeVbox.setDisable(false);
                        prototipeVbox.setVisible(true);
                        initListenerPrototipe();
                        break;
                }

            }
        });
    }

    public void initListenerBook() {
        addComponentToValidator(new ValidatorTextInputControl(editionNumberTextField, Validator.PATTERN_LETTERS, Validator.LENGTH_GENERAL, this), false);

        addComponentToValidator(new ValidatorTextInputControl(isbnTextField, Validator.PATTERN_ISBN, 30, this), false);

        addComponentToValidator(new ValidatorTextInputControl(editorialTextField, Validator.PATTERN_LETTERS, Validator.LENGTH_GENERAL, this), false);
        initListenerToControls();
    }

    public void initListenerChapterBook() {
        countryTextField.setDisable(true);
        addComponentToValidator(new ValidatorComboBoxBase(titleBooksCombobox, this), false);

        addComponentToValidator(new ValidatorTextInputControl(pagesTextField, Validator.PATTERN_NUMBERS_AND_LETTERS, Validator.LENGTH_GENERAL, this), false);
        initListenerToControls();
    }

    public void initListenerPrototipe() {

        addComponentToValidator(new ValidatorTextInputControl(characteristicsPrototipeTextField, Validator.PATTERN_LETTERS, Validator.LENGTH_GENERAL, this), false);
        initListenerToControls();
    }

    public void initListenerArticle() {

        addComponentToValidator(new ValidatorTextInputControl(articleIndiceTextField, Validator.PATTERN_LETTERS, Validator.LENGTH_GENERAL, this), false);

        addComponentToValidator(new ValidatorTextInputControl(articleISNNTextField, Validator.PATTERN_LETTERS, Validator.LENGTH_GENERAL, this), false);

        addComponentToValidator(new ValidatorTextInputControl(articleNameMagazineTextField, Validator.PATTERN_LETTERS, Validator.LENGTH_GENERAL, this), false);
        initListenerToControls();

    }

    private void deterMinateSQLState(SQLException sqlException) {
        Logger.getLogger(AddMemberController.class.getName()).log(Level.SEVERE, null, sqlException);
        if(sqlException.getSQLState().equals(SQLStates.SQL_NO_CONNECTION.getSqlState())) {
            AlertController.getInstance().showConnectionErrorAlert();
        }
        AlertController.getInstance().showActionFailedAlert(sqlException.getLocalizedMessage());
    }


    private void initValidatorToTextInput() {
        Function<Object, Boolean> validateDate = a -> {
            return DateFormatter.compareActualDateToSelectedDate((LocalDate) a) == -1;
        };
        addComponentToValidator(new ValidatorComboBoxBase(typeEvidenceCombobox, this), false);

        addComponentToValidator(new ValidatorTextInputControl(countryTextField, Validator.PATTERN_LETTERS, Validator.LENGTH_GENERAL, this), false);

        addComponentToValidator(new ValidatorTextInputControl(referTextField, Validator.PATTERN_NUMBERS_AND_LETTER_WITH_STRANGE_SYMBOLS, Validator.LENGTH_LONG_LONG_TEXT, this), false);

        addComponentToValidator(new ValidatorTextInputControl(titleTextField, Validator.PATTERN_LETTERS, Validator.LENGTH_GENERAL, this), false);

        addComponentToValidator(new ValidatorComboBoxBaseWithConstraints(publicationDateDataPicker, this, validateDate), false);

        initListenerToControls();
    }

}
