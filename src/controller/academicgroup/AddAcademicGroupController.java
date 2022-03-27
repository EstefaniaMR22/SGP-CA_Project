package controller.academicgroup;

import controller.AlertController;
import controller.ValidatorController;
import controller.validator.Validator;
import controller.validator.ValidatorComboBoxBase;
import controller.validator.ValidatorComboBoxBaseWithConstraints;
import controller.validator.ValidatorTextInputControl;
import javafx.animation.PauseTransition;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;
import model.dao.AcademicGroupDAO;
import model.dao.MiembroDAO;
import model.domain.AcademicGroup;
import model.domain.ConsolidationGrade;
import model.domain.LGAC;
import model.domain.Member;
import model.domain.Participation;
import model.domain.ParticipationType;
import utils.DateFormatter;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AddAcademicGroupController extends ValidatorController implements Initializable {
    private List<LGAC> listAddedToProgram;
    @FXML private TableView<LGAC> lgacRegisteredTableView;
    @FXML private TableColumn<LGAC, Integer> identificatorTableColumn;
    @FXML private TableColumn<LGAC, String> descriptionTableColumn;
    @FXML private TextField activeMembersTextField;
    @FXML private TextField adscriptionAreaTextField;
    @FXML private TextField adscriptionUnitTextField;
    @FXML private ComboBox<ConsolidationGrade> consolidationGradeComboBox;
    @FXML private TextArea generalObjetiveTextArea;
    @FXML private TextField idTextField;
    @FXML private DatePicker lastEvaluationDatePicker;
    @FXML private ListView<Member> membersAvailableListView;
    @FXML private TextArea misionTextArea;
    @FXML private TextField nameTextField;
    @FXML private TableView<Participation> participationsTableView;
    @FXML private TableColumn<Participation, String> nameTableColumn;
    @FXML private TableColumn<Participation, String> personalNumberTableColumn;
    @FXML private TableColumn<Participation, ParticipationType> typeParticipationColumn;
    @FXML private DatePicker registerDateDatePicker;
    @FXML private Label totalLGACInProgramLabel;
    @FXML private Label totalMembersInProgramLabel;
    @FXML private TextArea visionTextArea;
    @FXML private Button registerButton;
    @FXML private Button addLgacToCAButton;
    @FXML private Button removeLgacCAButton;
    @FXML private Button addMemberToCAButton;
    @FXML private TextField searchMemberTextField;
    @FXML private Label systemLabel;
    @FXML private Button removeMemberCAButton;
    @FXML private Button searchMemberButton;
    @FXML private TextArea descriptionAdscriptionTextArea;
    private AcademicGroup academicGroupProgramRegistered;

    public void showStage() {
        loadFXMLFile(getClass().getResource("/view/AddAcademicGroupProgramView.fxml"), this);
        stage.showAndWait();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initValidator();
        setTableComponents();
        getConsolidationGradeFromDatabase();
        getAllMembersFromDatabase();
    }

    public AcademicGroup getAcademicGroupProgramRegistered() {
        return academicGroupProgramRegistered;
    }

    @FXML
    void addAcademicGroupProgramOnAction(ActionEvent event) {
        if(validateInputs()) {
            addAcademicGroup();
        }
    }

    @FXML
    void addLGACToProgramOnAction(ActionEvent event) {
        AddLgacController addLgacController = new AddLgacController(lgacRegisteredTableView.getItems());
        LGAC registered = addLgacController.showStage();
        if(lgacRegisteredTableView.getItems() == null ) {
            ObservableList<LGAC> lgacObservableList = FXCollections.observableArrayList(new ArrayList<>());
            lgacRegisteredTableView.setItems(lgacObservableList);
        }
        if(registered != null ) {
            lgacRegisteredTableView.getItems().add(registered);
        }
    }

    @FXML
    void removeLGACFromProgramOnAction(ActionEvent event) {
        LGAC lgacSelected = lgacRegisteredTableView.getSelectionModel().getSelectedItem();
        if(lgacSelected != null ) {
            lgacRegisteredTableView.getItems().remove(lgacSelected);
        }
    }

    @FXML
    void addMemberToProgramOnAction(ActionEvent event) {
        Member selected = membersAvailableListView.getSelectionModel().getSelectedItem();
        if(selected != null ) {
            Participation participation = new Participation();
            participation.setMember(selected);
            participation.setParticipationType(ParticipationType.INTEGRANT);
            if(participationsTableView.getItems() == null) {
                ObservableList<Participation> participationObservableList = FXCollections.observableArrayList(new ArrayList<>());
                participationsTableView.setItems(participationObservableList);
            }
            membersAvailableListView.getItems().remove(selected);
            participationsTableView.getItems().add(participation);
        }
    }

    @FXML
    void removeMemberFromCAOnAction(ActionEvent event) {
        Participation participation = participationsTableView.getSelectionModel().getSelectedItem();
        if(participation != null ) {
            membersAvailableListView.getItems().add(participation.getMember());
            participationsTableView.getItems().remove(participation);
        }
    }

    @FXML
    void cancelOnAction(ActionEvent event) {
        if(AlertController.showCancelationConfirmationAlert()) {
            stage.close();
        }
    }


    private void pause() {
        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(event -> stage.close());
        pause.play();
    }

    private void getConsolidationGradeFromDatabase() {
        List<ConsolidationGrade> grades = null;
        try{
            grades = new AcademicGroupDAO().getConsolidationGrades();
            ObservableList<ConsolidationGrade> gradeObservableList = FXCollections.observableArrayList(grades);
            consolidationGradeComboBox.setItems(gradeObservableList);
        } catch (SQLException sqlException) {
            Logger.getLogger(AddAcademicGroupController.class.getName()).log(Level.SEVERE, null, sqlException);
        }
    }

    private void getAllMembersFromDatabase() {
        List<Member> members = null;
        try {
            members = new MiembroDAO().getAllMembers();
            members.removeIf(member -> member.getId() == 1);
            ObservableList<Member> memberObservableList = FXCollections.observableArrayList(members);
            membersAvailableListView.setItems(memberObservableList);
        } catch (SQLException sqlException) {
            Logger.getLogger(AddAcademicGroupController.class.getName()).log(Level.SEVERE, null, sqlException);
        }
    }

    private void setTableComponents() {
        identificatorTableColumn.setCellValueFactory(new PropertyValueFactory<>("identification"));
        descriptionTableColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        nameTableColumn.setCellValueFactory( cellData -> new SimpleStringProperty(cellData.getValue().getMember().getName() + " " + cellData.getValue().getMember().getPaternalLastname() + " " + cellData.getValue().getMember().getMaternalLastname()));
        personalNumberTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMember().getPersonalNumber()));
        ObservableList<ParticipationType> participationTypeObservableList = FXCollections.observableArrayList(ParticipationType.values());
        participationTypeObservableList.remove(ParticipationType.OTHER);
        typeParticipationColumn.setCellFactory(ComboBoxTableCell.forTableColumn(participationTypeObservableList));
        typeParticipationColumn.setOnEditCommit(event -> event.getTableView().getItems().get(event.getTablePosition().getRow()).setParticipationType(event.getNewValue()));
        typeParticipationColumn.setCellValueFactory( cellData -> new SimpleObjectProperty<>(cellData.getValue().getParticipationType()));
        participationsTableView.setEditable(true);
    }

    private void initValidator() {
        Function<Object, Boolean> validateRegister = a -> DateFormatter.compareActualDateToLocalDate((LocalDate) a) >= 0;
        addComponentToValidator(new ValidatorTextInputControl(idTextField, Validator.PATTERN_NUMBERS_AND_LETTERS, Validator.LENGTH_GENERAL, this), false);
        addComponentToValidator(new ValidatorTextInputControl(adscriptionAreaTextField, Validator.PATTERN_LETTERS,Validator.LENGTH_GENERAL, this), false);
        addComponentToValidator(new ValidatorTextInputControl(nameTextField, Validator.PATTERN_LETTERS, Validator.LENGTH_GENERAL, this), false);
        addComponentToValidator(new ValidatorTextInputControl(adscriptionUnitTextField, Validator.PATTERN_LETTERS, Validator.LENGTH_GENERAL, this), false);
        addComponentToValidator(new ValidatorComboBoxBase(consolidationGradeComboBox, this), false);
        addComponentToValidator(new ValidatorComboBoxBaseWithConstraints(registerDateDatePicker, this, validateRegister), false);
        addComponentToValidator(new ValidatorComboBoxBaseWithConstraints(lastEvaluationDatePicker, this, validateRegister), false);
        addComponentToValidator(new ValidatorTextInputControl(generalObjetiveTextArea, Validator.PATTERN_NUMBERS_AND_LETTERS, Validator.LENGTH_LONG_SMALL_TEXT, this), false);
        addComponentToValidator(new ValidatorTextInputControl(misionTextArea, Validator.PATTERN_NUMBERS_AND_LETTERS, Validator.LENGTH_LONG_LONG_TEXT, this), false);
        addComponentToValidator(new ValidatorTextInputControl(visionTextArea, Validator.PATTERN_NUMBERS_AND_LETTERS, Validator.LENGTH_LONG_MEDIUM_TEXT, this), false);
        addComponentToValidator(new ValidatorTextInputControl(adscriptionAreaTextField, Validator.PATTERN_NUMBERS_AND_LETTERS, Validator.LENGTH_LONG_LONG_TEXT, this), false);
        initListenerToControls();
    }

    private void disableInput(boolean state) {
        idTextField.setDisable(state);
        adscriptionAreaTextField.setDisable(state);
        nameTextField.setDisable(state);
        adscriptionUnitTextField.setDisable(state);
        consolidationGradeComboBox.setDisable(state);
        registerDateDatePicker.setDisable(state);
        lastEvaluationDatePicker.setDisable(state);
        generalObjetiveTextArea.setDisable(state);
        misionTextArea.setDisable(state);
        visionTextArea.setDisable(state);
        lgacRegisteredTableView.setDisable(state);
        participationsTableView.setDisable(state);
        membersAvailableListView.setDisable(state);
        registerButton.setDisable(state);
        addLgacToCAButton.setDisable(state);
        removeLgacCAButton.setDisable(state);
        addMemberToCAButton.setDisable(state);
        searchMemberTextField.setDisable(state);
        removeMemberCAButton.setDisable(state);
        searchMemberButton.setDisable(state);
        descriptionAdscriptionTextArea.setDisable(state);
    }

    private void addAcademicGroup() {
        AcademicGroup academicGroupProgram = new AcademicGroup();
        academicGroupProgram.setId(idTextField.getText());
        academicGroupProgram.setAdscriptionArea(adscriptionAreaTextField.getText());
        academicGroupProgram.setName(nameTextField.getText());
        academicGroupProgram.setAdscriptionUnit(adscriptionUnitTextField.getText());
        academicGroupProgram.setConsolidationGrade(consolidationGradeComboBox.getSelectionModel().getSelectedItem());
        academicGroupProgram.setRegisterDate(DateFormatter.getDateFromDatepickerValue(registerDateDatePicker.getValue()));
        academicGroupProgram.setLastEvaluationDate(DateFormatter.getDateFromDatepickerValue(lastEvaluationDatePicker.getValue()));
        academicGroupProgram.setGeneralObjetive(generalObjetiveTextArea.getText());
        academicGroupProgram.setMission(misionTextArea.getText());
        academicGroupProgram.setVision(visionTextArea.getText());
        academicGroupProgram.setLgacList(lgacRegisteredTableView.getItems());
        academicGroupProgram.setParticipationList(participationsTableView.getItems());
        academicGroupProgram.setDescriptionAdscription(descriptionAdscriptionTextArea.getText());
        try{
            academicGroupProgram.setId(new AcademicGroupDAO().addAcademicGroupProgram(academicGroupProgram));
            academicGroupProgramRegistered = academicGroupProgram;
            systemLabel.setText("¡Se ha registrado manera exitosa!");
            disableInput(true);
            pause();
        } catch(SQLException sqlException) {
            Logger.getLogger(AddAcademicGroupController.class.getName()).log(Level.SEVERE, null, sqlException);
        }
    }
}
