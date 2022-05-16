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
import javafx.scene.control.*;
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

public class AddWorkplanController extends ValidatorController implements Initializable {
    @FXML private TableView<Action> actionsTableView;
    @FXML private TableColumn<Action, String> actionDescriptionTableColumn;
    @FXML private TableView<Goal> goalTableView;
    @FXML private TableColumn<Goal, String> identificatorTableColumn;
    @FXML private TableColumn<Goal, Date> endDateTableColumn;
    @FXML private TableColumn<Goal, Date> descriptionTableColumn;
    @FXML private ListView<String> resourcesListVIew;
    @FXML private DatePicker endDateDatepicker;
    @FXML private TextArea generalObjetiveTextArea;
    @FXML private TextField idTextField;
    @FXML private DatePicker startDateDatepicker;
    @FXML private Label systemLabel;
    @FXML private TableColumn<Action, String> memberAssignedTableColumn;
    @FXML private VBox actionVBox;
    @FXML private ScrollPane scrollScrollPane;
    private Workplan workplan;
    private boolean isRegistered;

    public void showStage() {
        loadFXMLFile(getClass().getResource("/view/AddWorkplanView.fxml"), this);
        stage.showAndWait();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initValidator();
        setTableComponents();
        intializeListenerActionTable();
        initializeListenerGoalTable();
    }

    @FXML
    void addWorkplan(ActionEvent event) {
        if(validateInputs()) {
            addWorkplan();
        } else {
            systemLabel.setText("¡Algunos campos son invalidos, por favor verifiquelos!");
        }
    }

    private int getDurationYears() {
        return endDateDatepicker.getValue().getYear() - startDateDatepicker.getValue().getYear();
    }

    private void addWorkplan() {
        workplan = new Workplan();
        workplan.setIdentificator(idTextField.getText());
        workplan.setGeneralObjetive(generalObjetiveTextArea.getText());
        workplan.setStartDate(DateFormatter.getDateFromDatepickerValue(startDateDatepicker.getValue()));
        workplan.setEndDate(DateFormatter.getDateFromDatepickerValue(endDateDatepicker.getValue()));
        workplan.setYearsDuration(getDurationYears());
        workplan.setGoalList(new ArrayList<>(goalTableView.getItems()));
        try{
            workplan = new WorkplanDAO().addWorkPlan(workplan, Autentication.getInstance().getIdAcademicGroup());
            isRegistered = true;
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
}
