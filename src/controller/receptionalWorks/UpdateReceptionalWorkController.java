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
import model.dao.MemberDAO;
import model.dao.ProjectDAO;
import model.dao.ReceptionalWorkDAO;
import model.domain.Member;
import model.domain.Modality;
import model.domain.Project;
import model.domain.ReceptionalWork;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UpdateReceptionalWorkController extends ValidatorController implements Initializable {

    @FXML private TextField receptionalWorkNameTextField;
    @FXML private TextField participantsTextField;
    @FXML private TextArea descriptionTextArea;
    @FXML private TextArea requerimentsTextArea;
    @FXML private DatePicker estimatedEndDateDataPicker;
    @FXML private DatePicker startDateDataPicker;
    @FXML private ComboBox<Member> directorCombobox;
    @FXML private ComboBox<Member> codirectorCombobox;
    @FXML private ComboBox<String> statusCombobox;
    @FXML private ComboBox<Modality> modalityCombobox;
    @FXML private ComboBox<Project> projectsCombobox;
    @FXML private Button updateReceptionalWork;
    @FXML private Button exitButton;
    @FXML private Label systemLabel;
    private String idAcademicGroup;

    private ObservableList<Member> memberObservableList;
    private ObservableList<String> statusObservableList;
    private ObservableList<Modality> modalitiesObservableList;
    private ObservableList<Project> projectsObservableList;
    ReceptionalWork modifyReceptionalWork;

    public void showStage() {
        loadFXMLFile(getClass().getResource("/view/ModifyReceptionalWorkView.fxml"), this);
        stage.showAndWait();
    }

    public UpdateReceptionalWorkController(ReceptionalWork modifyReceptionalWork, String idAcademicGroup){
        this.idAcademicGroup = idAcademicGroup;
        this.modifyReceptionalWork = modifyReceptionalWork;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initValidatorToTextInput();
        memberObservableList = FXCollections.observableArrayList();
        statusObservableList = FXCollections.observableArrayList();
        modalitiesObservableList = FXCollections.observableArrayList();
        projectsObservableList = FXCollections.observableArrayList();
        chargeComboBoxMember();
        chargeProjectsComboBox();
        projectsCombobox.setDisable(true);
        chargeModalitiesComboBox();

        getReceptionaWorkDetails();

    }

    private void getReceptionaWorkDetails() {

        ReceptionalWorkDAO receptionalWorkDAO = new ReceptionalWorkDAO();
        try {
            modifyReceptionalWork = receptionalWorkDAO.getReceptionalWorkDetails(modifyReceptionalWork.getIdReceptionalWork());
            chargeReceptionalWorkUpdate();
            chargeStatusComboBox();
        }catch(SQLException getProjectDetailsExeception){

            deterMinateSQLState(getProjectDetailsExeception);

        }

    }

    private void chargeReceptionalWorkUpdate(){

        receptionalWorkNameTextField.setText(modifyReceptionalWork.getNameReceptionalWork());
        participantsTextField.setText(modifyReceptionalWork.getParticipants() + "");
        descriptionTextArea.setText(modifyReceptionalWork.getDescription());
        requerimentsTextArea.setText(modifyReceptionalWork.getRequeriments());
        estimatedEndDateDataPicker.setValue(DateFormatter.getLocalDateFromUtilDate(modifyReceptionalWork.getEndDate()));
        startDateDataPicker.setValue(DateFormatter.getLocalDateFromUtilDate(modifyReceptionalWork.getRegister()));

        ProjectDAO projectDAO = new ProjectDAO();
        try {

            int positionProject = getIndexProject(projectDAO.getProjectDetails(projectDAO.getIdProject(modifyReceptionalWork.getNameProject())).getIdProject());
            projectsCombobox.getSelectionModel().select(positionProject);
            projectsCombobox.setDisable(true);

        } catch (SQLException memberSqlException) {
            deterMinateSQLState(memberSqlException);
        }

        int positionModality = getIndexModality(modifyReceptionalWork.getModality());
        modalityCombobox.getSelectionModel().select(positionModality);

        int positionDirector = getIndexMember(modifyReceptionalWork.getIdDirector());
        directorCombobox.getSelectionModel().select(positionDirector);
        directorCombobox.setDisable(true);

        int positionCodirector = getIndexMember(modifyReceptionalWork.getIdCodirector());
        codirectorCombobox.getSelectionModel().select(positionCodirector);
        codirectorCombobox.setDisable(true);

    }

    @FXML
    void modifyReceptionalWorkOnAction(ActionEvent actionEvent) {

        try {
            if(validateInputs()) {
                if(!validateReceptionalWork()) {
                    int participants = Integer.parseInt(participantsTextField.getText());
                    if(participants<=3) {
                        modifyReceptionalWork();
                    }else {
                        systemLabel.setText("¡Maximo son 3 participantes!");
                    }
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
        return new ReceptionalWorkDAO().checkReceptionalWorkUpdated(receptionalWorkNameTextField.getText(), modifyReceptionalWork.getIdReceptionalWork());
    }

    private void modifyReceptionalWork(){
        ReceptionalWorkDAO receptionalWorkDAO = new ReceptionalWorkDAO();

        modifyReceptionalWork.setNameReceptionalWork(receptionalWorkNameTextField.getText());
        modifyReceptionalWork.setParticipants(Integer.parseInt(participantsTextField.getText()));
        modifyReceptionalWork.setDescription(descriptionTextArea.getText());
        modifyReceptionalWork.setEndDate(DateFormatter.getDateFromDatepickerValue(estimatedEndDateDataPicker.getValue()));

        modifyReceptionalWork.setRegister(DateFormatter.getDateFromDatepickerValue(startDateDataPicker.getValue()));

        int positionStatus = statusCombobox.getSelectionModel().getSelectedIndex();
        modifyReceptionalWork.setStatus(statusObservableList.get(positionStatus));

        int positionModalities = modalityCombobox.getSelectionModel().getSelectedIndex();
        modifyReceptionalWork.setModality(modalitiesObservableList.get(positionModalities));

        modifyReceptionalWork.setStimatedDurationInMonths(calculateMonths(modifyReceptionalWork.getRegister(), modifyReceptionalWork.getEndDate()));

        modifyReceptionalWork.setRequeriments(requerimentsTextArea.getText());
        try {
            boolean correctAddProject = false;

            correctAddProject = receptionalWorkDAO.updateReceptionalWork(modifyReceptionalWork);

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

    private void chargeComboBoxMember(){
        MemberDAO member = new MemberDAO();
        try {

            memberObservableList = member.getAllMembersByIdAcademicBody(idAcademicGroup);

            directorCombobox.setItems(memberObservableList);
            codirectorCombobox.setItems(memberObservableList);

        } catch(SQLException chargeLGACSQLException) {
            deterMinateSQLState(chargeLGACSQLException);
        }

    }

    private int getIndexMember(int idMember)
    {
        int value = 0;
        if(memberObservableList.size()>0)
        {
            for(int i = 0; i < memberObservableList.size(); i++)
            {
                Member get = memberObservableList.get(i);
                if(get.getId()== idMember)
                {
                    return i;
                }
            }
        }
        return value;
    }

    private void chargeStatusComboBox(){

        statusObservableList.setAll("En proceso", "Completado");
        statusCombobox.setItems(statusObservableList);


        if(modifyReceptionalWork.getStatus().equals("En proceso")) {
            statusCombobox.getSelectionModel().select(0);

        }else if(modifyReceptionalWork.getStatus().equals("Completado")) {

            statusCombobox.getSelectionModel().select(1);

        }
    }

    private void chargeModalitiesComboBox(){
        modalitiesObservableList.setAll(Modality.values());
        modalityCombobox.setItems(modalitiesObservableList);

    }

    private int getIndexModality(Modality modality)
    {
        int value = 0;
        if(modalitiesObservableList.size()>0)
        {
            for(int i = 0; i < modalitiesObservableList.size(); i++)
            {
                Modality get = modalitiesObservableList.get(i);
                System.out.println(get.toString());
                if(get.toString()== modality.toString())
                {
                    return i;
                }
            }
        }
        return value;
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

    private int getIndexProject(int idProject)
    {
        int value = 0;
        if(projectsObservableList.size()>0)
        {
            for(int i = 0; i < projectsObservableList.size(); i++)
            {
                Project get = projectsObservableList.get(i);
                if(get.getIdProject()== idProject)
                {
                    return i;
                }
            }
        }
        return value;
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

    private static int compareTwoDates(LocalDate selectedLocalDate, LocalDate selectedLocalDateEnd) {

        int resultCompareDate = 0;
        if (selectedLocalDate.isBefore(selectedLocalDateEnd)){
            resultCompareDate = 1;
        }else if (selectedLocalDate.isAfter(selectedLocalDateEnd)){
            resultCompareDate = -1;
        }

        return resultCompareDate;
    }

    private void initValidatorToTextInput() {
        Function<Object, Boolean> validateDate = a -> {
            if(DateFormatter.compareActualDateToSelectedDate((LocalDate) a) == 1 &&
                    compareTwoDates(startDateDataPicker.getValue(),estimatedEndDateDataPicker.getValue()) ==1){
                return true;
            }else {
                return false;
            }
        };

        addComponentToValidator(new ValidatorTextInputControl(receptionalWorkNameTextField, Validator.PATTERN_LETTERS, Validator.LENGTH_GENERAL, this), false);

        addComponentToValidator(new ValidatorTextInputControl(participantsTextField, Validator.PATTERN_NUMBERS, Validator.LENGTH_SMALL_TEXT, this), false);

        addComponentToValidator(new ValidatorTextInputControl(descriptionTextArea, Validator.PATTERN_LETTERS, Validator.LENGTH_GENERAL, this), false);

        addComponentToValidator(new ValidatorTextInputControl(requerimentsTextArea, Validator.PATTERN_LETTERS, Validator.LENGTH_GENERAL, this), false);

        addComponentToValidator(new ValidatorComboBoxBase(modalityCombobox, this), false);

        addComponentToValidator(new ValidatorComboBoxBaseWithConstraints(startDateDataPicker, this, validateDate), false);

        addComponentToValidator(new ValidatorComboBoxBaseWithConstraints(estimatedEndDateDataPicker, this, validateDate), false);

        initListenerToControls();
    }

}

