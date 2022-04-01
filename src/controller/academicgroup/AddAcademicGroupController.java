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
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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
import assets.utils.Autentication;
import assets.utils.DateFormatter;

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
    private FilteredList<Member> filteredData;
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
        loadFXMLFile(getClass().getResource("/view/AddAcademicGroupView.fxml"), this);
        stage.setTitle("Registrar miembro");
        stage.showAndWait();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initValidator();
        setTableComponents();
        getConsolidationGradeFromDatabase();
        getAllMembersFromDatabase();
        initializeFilterSearchInput();
        // For later version...
        //addSelfMemberToProgram();
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
            searchMemberTextField.setText("");
            addParticipationToTableView(selected, ParticipationType.INTEGRANT);
        }
    }

    @FXML
    void removeMemberFromCAOnAction(ActionEvent event) {
        Participation selected = participationsTableView.getSelectionModel().getSelectedItem();
        if(selected != null ) {
            removeParticipationFromTableView(selected);
        }
    }

    @FXML
    void cancelOnAction(ActionEvent event) {
        if(AlertController.getInstance().showCancelationConfirmationAlert()) {
            stage.close();
        }
    }

    private void pause(int seconds) {
        PauseTransition pause = new PauseTransition(Duration.seconds(seconds));
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

    private void addSelfMemberToProgram() {
        Member selfResponsable = Autentication.getInstance().getParticipation().getMember();
        addParticipationToTableView(selfResponsable, ParticipationType.RESPONSABLE);
    }

    private void addParticipationToTableView(Member member, ParticipationType participationType) {
        Participation participation = new Participation();
        participation.setMember(member);
        participation.setParticipationType(participationType);
        if(participationsTableView.getItems() == null) {
            ObservableList<Participation> participationObservableList = FXCollections.observableArrayList(new ArrayList<>());
            participationsTableView.setItems(participationObservableList);
        }
        participationsTableView.getItems().add(participation);
        filteredData.getSource().remove(member);
    }

    private void removeParticipationFromTableView(Participation participation) {
        searchMemberTextField.setText("");
        ObservableList<Member> observableList = FXCollections.observableArrayList(new ArrayList<>());
        observableList.setAll(membersAvailableListView.getItems());
        observableList.add(participation.getMember());
        membersAvailableListView.setItems(observableList);
        initializeFilterSearchInput();
        participationsTableView.getItems().remove(participation);
    }

    private void initializeFilterSearchInput() {
        filteredData = new FilteredList<>(membersAvailableListView.getItems(), p -> true);
        searchMemberTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate( object -> {
                if(newValue == null | newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if(String.valueOf(object.getFullName()).toLowerCase().contains(lowerCaseFilter)){
                    return true;
                } else if(String.valueOf(object.getFullName()).toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
            SortedList<Member> sortedList = new SortedList<>(filteredData);
            membersAvailableListView.setItems(sortedList);
        });
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
            pause(3);
        } catch(SQLException sqlException) {
            Logger.getLogger(AddAcademicGroupController.class.getName()).log(Level.SEVERE, null, sqlException);
        }
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

    private void setTableComponents() {
        identificatorTableColumn.setCellValueFactory(new PropertyValueFactory<>("identification"));
        descriptionTableColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        nameTableColumn.setCellValueFactory( cellData -> new SimpleStringProperty(cellData.getValue().getMember().getFullName()));
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
        addComponentToValidator(new ValidatorTextInputControl(idTextField, Validator.PATTERN_NUMBERS_AND_LETTER_WITH_STRANGE_SYMBOLS, Validator.LENGTH_GENERAL, this), false);
        addComponentToValidator(new ValidatorTextInputControl(adscriptionAreaTextField, Validator.PATTERN_LETTERS,Validator.LENGTH_GENERAL, this), false);
        addComponentToValidator(new ValidatorTextInputControl(nameTextField, Validator.PATTERN_LETTERS, Validator.LENGTH_GENERAL, this), false);
        addComponentToValidator(new ValidatorTextInputControl(adscriptionUnitTextField, Validator.PATTERN_LETTERS, Validator.LENGTH_GENERAL, this), false);
        addComponentToValidator(new ValidatorComboBoxBase(consolidationGradeComboBox, this), false);
        addComponentToValidator(new ValidatorComboBoxBaseWithConstraints(registerDateDatePicker, this, validateRegister), false);
        addComponentToValidator(new ValidatorComboBoxBaseWithConstraints(lastEvaluationDatePicker, this, validateRegister), false);
        addComponentToValidator(new ValidatorTextInputControl(generalObjetiveTextArea, Validator.PATTERN_NUMBERS_AND_LETTER_WITH_STRANGE_SYMBOLS, Validator.LENGTH_LONG_SMALL_TEXT, this), false);
        addComponentToValidator(new ValidatorTextInputControl(misionTextArea, Validator.PATTERN_NUMBERS_AND_LETTER_WITH_STRANGE_SYMBOLS, Validator.LENGTH_LONG_LONG_TEXT, this), false);
        addComponentToValidator(new ValidatorTextInputControl(visionTextArea, Validator.PATTERN_NUMBERS_AND_LETTER_WITH_STRANGE_SYMBOLS, Validator.LENGTH_LONG_MEDIUM_TEXT, this), false);
        addComponentToValidator(new ValidatorTextInputControl(adscriptionAreaTextField, Validator.PATTERN_NUMBERS_AND_LETTERS, Validator.LENGTH_LONG_LONG_TEXT, this), false);
        initListenerToControls();
    }
}
