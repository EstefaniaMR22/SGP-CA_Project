package controller.workplan;

import assets.utils.DateFormatter;
import controller.control.ValidatorController;
import controller.control.validator.Validator;
import controller.control.validator.ValidatorComboBoxBaseWithConstraints;
import controller.control.validator.ValidatorTextInputControl;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.domain.Action;
import model.domain.Goal;

import java.net.URL;
import java.time.LocalDate;
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


    public void showStage() {
        loadFXMLFile(getClass().getResource("/view/AddWorkplanView.fxml"), this);
        stage.showAndWait();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initValidator();
        setTableComponents();
        intializeListenerActionTable();
    }

    @FXML
    void addWorkplan(ActionEvent event) {

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
        AddActionController addActionController = new AddActionController(actionsTableView.getItems());
        Action action = addActionController.showStage();
        if(action != null ) {
            if(addActionController.isRegistered() ) {
                actionsTableView.getItems().add(action);
            }
        }
    }

    @FXML
    void deleteActionOnAction(ActionEvent event) {
        Action selected = actionsTableView.getSelectionModel().getSelectedItem();
        if(selected != null ) {
            actionsTableView.getItems().remove(selected);
        }
    }

    private void intializeListenerActionTable() {
        actionsTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Action>() {
            @Override
            public void changed(ObservableValue<? extends Action> observable, Action oldValue, Action newValue) {
                if(newValue != null) {
                    resourcesListVIew.setItems(FXCollections.observableArrayList( newValue.getRecursos()));
                } else {
                    resourcesListVIew.setItems(null);
                }
            }
        });
    }

    private void setTableComponents() {
        identificatorTableColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        descriptionTableColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        endDateTableColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        actionDescriptionTableColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        memberAssignedTableColumn.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getResponsable().getFullName() + ": ID: " + x.getValue().getResponsable().getPersonalNumber()));
        actionsTableView.setPlaceholder(new Label(""));
        goalTableView.setPlaceholder(new Label(""));
    }

    private void initValidator() {
        Function<Object, Boolean> validateRegister = a -> DateFormatter.compareActualDateToLocalDate((LocalDate) a) >= 0;
        addComponentToValidator(new ValidatorTextInputControl(idTextField, Validator.PATTERN_NUMBERS_AND_LETTER_WITH_STRANGE_SYMBOLS, Validator.LENGTH_GENERAL, this), false);
        addComponentToValidator(new ValidatorComboBoxBaseWithConstraints(startDateDatepicker, this, validateRegister), false);
        addComponentToValidator(new ValidatorComboBoxBaseWithConstraints(endDateDatepicker, this, validateRegister), false);
        addComponentToValidator(new ValidatorTextInputControl(generalObjetiveTextArea, Validator.PATTERN_NUMBERS_AND_LETTER_WITH_STRANGE_SYMBOLS, Validator.LENGTH_LONG_LONG_TEXT, this), false);
        initListenerToControls();
    }
}
