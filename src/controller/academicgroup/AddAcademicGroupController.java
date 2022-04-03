package controller.academicgroup;

import controller.AlertController;
import controller.ValidatorController;
import controller.listcell.MemberAcademicGroupListCell;
import controller.validator.Validator;
import controller.validator.ValidatorComboBoxBase;
import controller.validator.ValidatorComboBoxBaseWithConstraints;
import controller.validator.ValidatorTextInputControl;
import javafx.animation.PauseTransition;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WritableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.css.Styleable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import javafx.util.Duration;
import javafx.util.StringConverter;
import model.dao.AcademicGroupDAO;
import model.dao.MiembroDAO;
import model.domain.AcademicGroup;
import model.domain.ConsolidationGrade;
import model.domain.LGAC;
import model.domain.Member;
import model.domain.Participation;
import model.domain.ParticipationType;
import assets.utils.DateFormatter;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
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
    @FXML private TextArea descriptionAdscriptionTextArea;
    @FXML private Label membersSystemLabel;

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
        initializeCellFactoryListView();
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
        } else {
            systemLabel.setText("Algunos campos son inválidos, por favor verifíquelos");
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
            addParticipationToTableView(selected, ParticipationType.COLABORATOR);
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
                } else if(String.valueOf(object.getPersonalNumber()).toLowerCase().contains(lowerCaseFilter)) {
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

    private void initializeCellFactoryListView() {
        membersAvailableListView.setCellFactory( item -> new MemberAcademicGroupListCell());
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
        descriptionAdscriptionTextArea.setDisable(state);
    }

    private void setTableComponents() {
        identificatorTableColumn.setCellValueFactory(new PropertyValueFactory<>("identification"));
        descriptionTableColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        nameTableColumn.setCellValueFactory( cellData -> new SimpleStringProperty(cellData.getValue().getMember().getFullName()));
        personalNumberTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMember().getPersonalNumber()));
        ObservableList<ParticipationType> participationTypeObservableList = FXCollections.observableArrayList(ParticipationType.values());
        participationTypeObservableList.remove(ParticipationType.OTHER);
        //typeParticipationColumn.setCellFactory( o -> new ComboBoxEditingCell());

        // Three ways to put combobox inside tablecell.
        //ComboBoxCell comboBoxCell = new ComboBoxCell(participationTypeObservableList);
        //typeParticipationColumn.setCellFactory(ComboBoxCell.forTableColumn(participationTypeObservableList));
        typeParticipationColumn.setCellFactory(ComboBoxTableCell.forTableColumn(participationTypeObservableList));
        typeParticipationColumn.getStyleClass().add("comboBox");
        typeParticipationColumn.setOnEditCommit(event -> {
                event.getTableView().getItems().get(event.getTablePosition().getRow()).setParticipationType(event.getNewValue());
        });
        typeParticipationColumn.setCellValueFactory( cellData -> new SimpleObjectProperty<>(cellData.getValue().getParticipationType()));

        participationsTableView.setEditable(true);
        participationsTableView.setPlaceholder(new Label(""));
        lgacRegisteredTableView.setPlaceholder(new Label(""));
    }


   private class ComboBoxEditingCell extends TableCell<Participation, ParticipationType> {
        private ComboBox<ParticipationType> comboBox;

       public ComboBoxEditingCell() {
           comboBox = new ComboBox<>(FXCollections.observableArrayList(new ArrayList<>(Arrays.asList(ParticipationType.values()))));
           comboBox.getItems().remove(ParticipationType.OTHER);
           comboBox.getSelectionModel().select(ParticipationType.COLABORATOR);
           comboBox.getStyleClass().add("comboBox");
           comboBox.setMinWidth(160);
//           This generate a strange behavior on ComboBox
//           comboBox.setOnAction(event -> {
//               if(existResponsableInTable()) {
//                   systemLabel.setText("Ya existe otro responsable");
//                   // This method throws IndexOutIfBoundsException
//                   //comboBox.getSelectionModel().select(ParticipationType.COLABORATOR);
//               }
//               commitEdit(comboBox.getSelectionModel().getSelectedItem());
//               //updateItem(comboBox.getSelectionModel().getSelectedItem(), isEmpty());
//               ( getTableView().getItems().get( getTableRow().getIndex())).setParticipationType(comboBox.getSelectionModel().getSelectedItem());
//
//           });

           comboBox.focusedProperty().addListener((observable, oldValue, newValue) -> {
               if(newValue) {
                   getTableView().edit(getIndex(), getTableColumn());
                   if(existResponsableInTable()) {
                       systemLabel.setText("Ya existe otro responsable asignado");
                       // It doesn't work, it has a strange behaviour (bug)
                       //comboBox.getSelectionModel().select(ParticipationType.COLABORATOR);
                   }
               } else {
                   commitEdit(comboBox.getSelectionModel().getSelectedItem());
               }
           });
       }

       @Override
       public void startEdit() {
           if (!isEmpty()) {
               super.startEdit();
               setText(null);
               setGraphic(comboBox);
           }
       }

       @Override
       public void updateItem(ParticipationType item, boolean empty) {
           super.updateItem(item, empty);
           if(empty || item == null) {
               setGraphic(null);
               // Should add setText(null) to remove from tableView
               // If you don't do this you will get some strange behaviour
               // When you delete a row in tableView
               setText(null);
           } else {
               //
               // setGraphic(null);
               setText(item.toString());
//               This method generate a strange behavior...
//               if(comboBox == null || comboBox.getItems().size() == 0) {
//                   comboBox = new ComboBox<>(FXCollections.observableArrayList(Arrays.asList(ParticipationType.values())));
//                   comboBox.getItems().remove(ParticipationType.OTHER);
//                   comboBox.getSelectionModel().select(item);
//                   setGraphic(comboBox);
//               }
           }
       }

       private boolean existResponsableInTable() {
           boolean existResponsableInTable = false;
           for (Participation participation : participationsTableView.getItems()) {
               if (participation.getParticipationType() == ParticipationType.RESPONSABLE) {
                   existResponsableInTable = true;
                   break;
               }
           }
           return existResponsableInTable;
       }
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

