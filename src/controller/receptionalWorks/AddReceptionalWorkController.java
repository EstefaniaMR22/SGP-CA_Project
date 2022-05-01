package controller.receptionalWorks;

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
import model.dao.LgacDAO;
import model.dao.MemberDAO;
import model.dao.ProjectDAO;
import model.dao.ReceptionalWorkDAO;
import model.domain.Member;
import model.domain.Modality;
import model.domain.Project;
import model.domain.ReceptionalWork;

import java.awt.event.ActionListener;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AddReceptionalWorkController extends ValidatorController implements Initializable {

    @FXML private TextField receptionalWorkNameTextField;
    @FXML private TextField participantsTextField;
    @FXML private TextArea descriptionTextArea;
    @FXML private TextArea requerimentsTextArea;
    @FXML private DatePicker estimatedEndDateDataPicker;
    @FXML private ComboBox<Member> directorCombobox;
    @FXML private ComboBox<Member> codirectorCombobox;
    @FXML private ComboBox<String> statusCombobox;
    @FXML private ComboBox<Modality> modalityCombobox;
    @FXML private ComboBox<Project> projectsCombobox;
    @FXML private Button newReceptionalWork;
    @FXML private Button exitButton;
    @FXML private Label systemLabel;
    private String idAcademicGroup;

    private ObservableList<Member> directorObservableList;
    private ObservableList<Member> codirectorObservableList;
    private ObservableList<String> statusObservableList;
    private ObservableList<Modality> modalitiesObservableList;
    private ObservableList<Project> projectsObservableList;

    public void showStage() {
        loadFXMLFile(getClass().getResource("/view/AddReceptionalWorkView.fxml"), this);
        stage.showAndWait();
    }

    public AddReceptionalWorkController(String idAcademicGroup){
        this.idAcademicGroup = idAcademicGroup;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        directorObservableList = FXCollections.observableArrayList();
        codirectorObservableList = FXCollections.observableArrayList();
        statusObservableList = FXCollections.observableArrayList();
        modalitiesObservableList = FXCollections.observableArrayList();
        projectsObservableList = FXCollections.observableArrayList();
        chargeComboBoxDirector();
        initListenerCombobox();
        chargeProjectsComboBox();
        chargeStatusComboBox();
        chargeModalitiesComboBox();

        initValidatorToTextInput();
    }

    @FXML
    void addReceptionalWorkOnAction(ActionEvent actionEvent) {

        try {
            if(validateInputs()) {
                if(!validateReceptionalWork()) {
                        addReceptionalWork();

                } else {
                    systemLabel.setText("¡Al parecer ya existe un trabajo recepcional con \n"+
                           " el mismo nombre,de favor ingrese un nombre distinto!");
                }

            }else {
                AlertController alertView = AlertController.getInstance();
                alertView.showActionFailedAlert("Algunos datos ingresados son inválidos, por favor verifíquelos");
            }
        } catch (Exception e) {
            systemLabel.setText(e.getLocalizedMessage());
        }

    }

    private boolean validateReceptionalWork() throws SQLException {
        return new ProjectDAO().checkProject(receptionalWorkNameTextField.getText());
    }

    private void addReceptionalWork(){
        ReceptionalWorkDAO receptionalWorkDAO = new ReceptionalWorkDAO();

        ReceptionalWork addReceptionalWork = new ReceptionalWork();
        addReceptionalWork.setNameReceptionalWork(receptionalWorkNameTextField.getText());
        addReceptionalWork.setParticipants(Integer.parseInt(participantsTextField.getText()));

        addReceptionalWork.setEndDate(DateFormatter.getDateFromDatepickerValue(estimatedEndDateDataPicker.getValue()));
        estimatedEndDateDataPicker.hide();
        estimatedEndDateDataPicker.setValue(LocalDate.from(LocalDateTime.now()));
        addReceptionalWork.setRegister(DateFormatter.getDateFromDatepickerValue(estimatedEndDateDataPicker.getValue()));

        int positionDirector = directorCombobox.getSelectionModel().getSelectedIndex();
        addReceptionalWork.setIdDirector(directorObservableList.get(positionDirector).getId());

        int positionCodirector = codirectorCombobox.getSelectionModel().getSelectedIndex();
        addReceptionalWork.setIdCodirector(codirectorObservableList.get(positionCodirector).getId());

        int positionStatus = statusCombobox.getSelectionModel().getSelectedIndex();
        addReceptionalWork.setStatus(statusObservableList.get(positionStatus));

        int positionModalities = modalityCombobox.getSelectionModel().getSelectedIndex();
        addReceptionalWork.setModality(modalitiesObservableList.get(positionModalities));

        int positionProject = projectsCombobox.getSelectionModel().getSelectedIndex();
        addReceptionalWork.setNameProject((projectsObservableList.get(positionProject).getProjectName()));

        addReceptionalWork.setStimatedDurationInMonths(calculateMonths(addReceptionalWork.getRegister(), addReceptionalWork.getEndDate()));
        addReceptionalWork.setRequeriments(requerimentsTextArea.getText());

        try {
            boolean correctAddProject = false;

            correctAddProject = receptionalWorkDAO.addReceptionalWork(addReceptionalWork);
            if (correctAddProject == true) {
                AlertController.getInstance().showSuccessfullRegisterAlert();
                stage.close();
            }

        } catch (SQLException addProjectInvestigationException) {

            deterMinateSQLState(addProjectInvestigationException);

        }
    }

    private int calculateMonths(Date register, Date endDate){
        int totalMonths = 0;
        if(register!= null && endDate!= null){
            Period diferrence = Period.between(DateFormatter.getLocalDateFromUtilDate(register),DateFormatter.getLocalDateFromUtilDate(endDate));
            totalMonths = diferrence.getMonths();
        }
        return totalMonths;
    }

    private void chargeComboBoxDirector() {
        MemberDAO memberDAO = new MemberDAO();
        try {

            directorObservableList = memberDAO.getAllMembersByIdAcademicBody(idAcademicGroup);

            directorCombobox.setItems(directorObservableList);

        } catch (SQLException chargeDirectorException) {
            deterMinateSQLState(chargeDirectorException);
        }
    }

    private void chargeComboBoxCodirector(int selectedDirector){
        MemberDAO memberDAO = new MemberDAO();
        try {
            codirectorObservableList = memberDAO.getAllMembersByIdAcademicBody(idAcademicGroup);
            codirectorObservableList.remove(selectedDirector);
            codirectorCombobox.setItems(codirectorObservableList);
        } catch(SQLException chargeLGACSQLException) {
            deterMinateSQLState(chargeLGACSQLException);
        }
    }

    private void chargeStatusComboBox(){

        statusObservableList.setAll("En proceso", "Completado");
        statusCombobox.setItems(statusObservableList);
        statusCombobox.getSelectionModel().select(0);
        statusCombobox.setDisable(true);
    }

    private void chargeModalitiesComboBox(){
        modalitiesObservableList.setAll(Modality.values());
        modalityCombobox.setItems(modalitiesObservableList);

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

    private void deterMinateSQLState(SQLException sqlException) {
        Logger.getLogger(AddMemberController.class.getName()).log(Level.SEVERE, null, sqlException);
        if(sqlException.getSQLState().equals(SQLStates.SQL_NO_CONNECTION.getSqlState())) {
            AlertController.getInstance().showConnectionErrorAlert();
        }
        AlertController.getInstance().showActionFailedAlert(sqlException.getLocalizedMessage());
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

    public void initListenerCombobox() {
        directorCombobox.valueProperty().addListener( (observable, oldValue, newValue) -> {
            if(newValue == null ) {

            } else {
                int positionDirector = directorCombobox.getSelectionModel().getSelectedIndex();

                chargeComboBoxCodirector(positionDirector);


            }
        });
    }

    private void initValidatorToTextInput() {
        Function<Object, Boolean> validateDate = a -> {
            return DateFormatter.compareActualDateToSelectedDate((LocalDate) a) == 1;
        };



        addComponentToValidator(new ValidatorTextInputControl(receptionalWorkNameTextField, Validator.PATTERN_LETTERS, Validator.LENGTH_GENERAL, this), false);

        addComponentToValidator(new ValidatorTextInputControl(participantsTextField, Validator.PATTERN_NUMBERS, Validator.LENGTH_SMALL_TEXT, this), false);

        addComponentToValidator(new ValidatorTextInputControl(descriptionTextArea, Validator.PATTERN_LETTERS, Validator.LENGTH_GENERAL, this), false);

        addComponentToValidator(new ValidatorTextInputControl(requerimentsTextArea, Validator.PATTERN_LETTERS, Validator.LENGTH_GENERAL, this), false);

        addComponentToValidator(new ValidatorComboBoxBase(modalityCombobox, this), false);

        addComponentToValidator(new ValidatorComboBoxBase(directorCombobox, this), false);

        addComponentToValidator(new ValidatorComboBoxBase(codirectorCombobox    , this), false);

        addComponentToValidator(new ValidatorComboBoxBaseWithConstraints(estimatedEndDateDataPicker, this, validateDate), false);

        initListenerToControls();
    }

}

