package controller.workplan;

import assets.utils.Autentication;
import assets.utils.DateFormatter;
import controller.control.ValidatorController;
import controller.control.validator.Validator;
import controller.control.validator.ValidatorComboBoxBaseWithConstraints;
import controller.control.validator.ValidatorTextInputControl;
import javafx.animation.PauseTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import model.dao.WorkplanDAO;
import model.domain.Action;
import model.domain.Goal;
import model.domain.Workplan;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.function.Function;


public class ModifyActionController extends ValidatorController implements Initializable {
    @FXML private TableView<Action> actionsTableView;
    @FXML private TableColumn<Action, String> actionDescriptionTableColumn;
    @FXML private TableView<Goal> goalTableView;
    @FXML private TableColumn<Goal, String> identificatorTableColumn;
    @FXML private TableColumn<Goal, Date> endDateTableColumn;
    @FXML private TableColumn<Goal, Date> descriptionTableColumn;
    @FXML private ListView<String> resourcesListVIew;
    @FXML private DatePicker endDateDatepicker;
    @FXML private TextArea generalObjetiveTextArea;
    @FXML private TableColumn<Action, String> stateActionTableColumn;
    @FXML private TableColumn<Goal, String> stateTableColumn;
    @FXML private TextField idTextField;
    @FXML private DatePicker startDateDatepicker;
    @FXML private Label systemLabel;
    @FXML private TableColumn<Action, String> memberAssignedTableColumn;
    @FXML private VBox actionVBox;
    @FXML private ScrollPane scrollScrollPane;
    private Workplan workplan;
    private boolean isUpdated;

    public Workplan showStage() {
        loadFXMLFile(getClass().getResource("/view/ModifyWorkplanView.fxml"), this);
        stage.showAndWait();
        return workplan;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initValidator();
        setTableComponents();
        intializeListenerActionTable();
        initializeListenerGoalTable();
        setDataToComponents();
    }

    public boolean isUpdated() {
        return isUpdated;
    }

    public ModifyActionController(Workplan workplan) {
        this.workplan = workplan;
    }

    @FXML
    void modifyWorkplan(ActionEvent event) {
        if(validateInputs()) {
            updateWorkplan();
        } else {
            systemLabel.setText("¡Algunos campos son inválidos, por favor verifíquelos!");
        }
    }

    private int getDurationYears() {
        return endDateDatepicker.getValue().getYear() - startDateDatepicker.getValue().getYear();
    }

    private void updateWorkplan() {
        Workplan newWorkplan = new Workplan();
        newWorkplan.setId(workplan.getId());
        newWorkplan.setIdentificator(idTextField.getText());
        newWorkplan.setGeneralObjetive(generalObjetiveTextArea.getText());
        newWorkplan.setStartDate(DateFormatter.getDateFromDatepickerValue(startDateDatepicker.getValue()));
        newWorkplan.setEndDate(DateFormatter.getDateFromDatepickerValue(endDateDatepicker.getValue()));
        newWorkplan.setYearsDuration(getDurationYears());
        newWorkplan.setGoalList(new ArrayList<>(goalTableView.getItems()));
        try{
            //workplan = new WorkplanDAO().addWorkPlan(workplan, Autentication.getInstance().getIdAcademicGroup());
            workplan = new WorkplanDAO().updateWorkplan(newWorkplan, Autentication.getInstance().getIdAcademicGroup());
            isUpdated = true;
            scrollScrollPane.setDisable(true);
            systemLabel.setText("¡Se ha registrado de manera exitosa!");
            pause(3);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void cancelOnAction(ActionEvent event) {
        stage.close();
    }

    @FXML
    void addGoalOnAction(ActionEvent event) {
        AddGoalController addGoalController = new AddGoalController(goalTableView.getItems());
        Goal newGoal = addGoalController.showStage();
        if(newGoal != null) {
            if(addGoalController.isRegistered()) {
                goalTableView.getItems().add(newGoal);
            }
        }
    }

    @FXML
    void deleteGoalOnAction(ActionEvent event) {
        Goal selected = goalTableView.getSelectionModel().getSelectedItem();
        if(selected != null) {
            goalTableView.getItems().remove(selected);
        }
    }

    @FXML
    void addActionOnAction(ActionEvent event) {
        Goal selected = goalTableView.getSelectionModel().getSelectedItem();
        if(selected != null ) {
            AddActionController addActionController = new AddActionController(actionsTableView.getItems());
            Action action = addActionController.showStage();
            if(action != null ) {
                if(addActionController.isRegistered() ) {
                    actionsTableView.getItems().add(action);
                    selected.setActions(new ArrayList<>(actionsTableView.getItems()));
                }
            }
        }
    }

    @FXML
    void deleteActionOnAction(ActionEvent event) {
        Goal selectedGoal = goalTableView.getSelectionModel().getSelectedItem();
        if( selectedGoal != null ) {
            Action selected = actionsTableView.getSelectionModel().getSelectedItem();
            if(selected != null ) {
                actionsTableView.getItems().remove(selected);
                selectedGoal.setActions(new ArrayList<>(actionsTableView.getItems()));
            }
        }
    }

    private void pause(int seconds) {
        PauseTransition pause = new PauseTransition(Duration.seconds(seconds));
        pause.setOnFinished(event -> stage.close());
        pause.play();
    }

    private void initializeListenerGoalTable() {
        goalTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null) {
                actionVBox.setDisable(false);
                actionsTableView.setItems(FXCollections.observableArrayList( (newValue.getActions() == null ? new ArrayList<>() : newValue.getActions() )));
            } else {
                actionsTableView.setItems(null);
                actionVBox.setDisable(true);
            }
        });
    }

    private void intializeListenerActionTable() {
        actionsTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null) {
                resourcesListVIew.setItems(FXCollections.observableArrayList( newValue.getRecursos()));
            } else {
                resourcesListVIew.setItems(null);
            }
        });
    }

    private void setTableComponents() {
        identificatorTableColumn.setCellValueFactory(new PropertyValueFactory<>("identificator"));
        descriptionTableColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        endDateTableColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        actionDescriptionTableColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        memberAssignedTableColumn.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getResponsable().getFullName() + ": ID: " + x.getValue().getResponsable().getPersonalNumber()));
        actionsTableView.setPlaceholder(new Label(""));
        stateActionTableColumn.setCellValueFactory( x -> new SimpleStringProperty( x.getValue().getState().getActionState()));
        stateTableColumn.setCellValueFactory( x -> new SimpleStringProperty( x.getValue().getState().getActivityState() ));
        goalTableView.setPlaceholder(new Label(""));
    }

    private void initValidator() {
        Function<Object, Boolean> validateRegister = a -> true;
        Function<Object, Boolean> validateWithOther = a -> (((LocalDate) a).compareTo(startDateDatepicker.getValue()) > 0);
        addComponentToValidator(new ValidatorTextInputControl(idTextField, Validator.PATTERN_NUMBERS_AND_LETTER_WITH_STRANGE_SYMBOLS, Validator.LENGTH_GENERAL, this), false);
        addComponentToValidator(new ValidatorComboBoxBaseWithConstraints(startDateDatepicker, this, validateRegister), false);
        addComponentToValidator(new ValidatorComboBoxBaseWithConstraints(endDateDatepicker, this, validateWithOther), false);
        addComponentToValidator(new ValidatorTextInputControl(generalObjetiveTextArea, Validator.PATTERN_NUMBERS_AND_LETTER_WITH_STRANGE_SYMBOLS, Validator.LENGTH_LONG_LONG_TEXT, this), false);
        initListenerToControls();
    }

    private void setDataToComponents() {
        idTextField.setText(workplan.getIdentificator());
        startDateDatepicker.setValue(DateFormatter.getLocalDateFromUtilDate(workplan.getStartDate()));
        endDateDatepicker.setValue(DateFormatter.getLocalDateFromUtilDate(workplan.getEndDate()));
        generalObjetiveTextArea.setText(workplan.getGeneralObjetive());
        goalTableView.setItems(FXCollections.observableArrayList(workplan.getGoalList()));
    }

}
