package controller.workplan;

import controller.academicgroup.AcademicGroupDetailsController;
import controller.control.ValidatorController;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.dao.WorkplanDAO;
import model.domain.Action;
import model.domain.ActionState;
import model.domain.Goal;
import model.domain.GoalState;
import model.domain.Workplan;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WorkplanDetailsController extends ValidatorController implements Initializable {
    private Workplan workplanSelected;
    @FXML private TableView<Action> actionsTableView;
    @FXML private TableColumn<Action, String> actionDescriptionTableColumn;
    @FXML private TableColumn<Goal, String> stateTableColumn;
    @FXML private TableColumn<Action, String> descriptionTableColumn;
    @FXML private TableColumn<Action, String> memberAssignedTableColumn;
    @FXML private Label endDateLabel;
    @FXML private TextArea generalObjetiveTextArea;
    @FXML private TableView<Goal> goalTableView;
    @FXML private TableColumn<Goal, Date> endDateTableColumn;
    @FXML private TableColumn<Goal, String> identificatorTableColumn;
    @FXML private TableColumn<Action, String> stateActionTableColumn;
    @FXML private TextField idTextField;
    @FXML private ListView<String> resourcesListVIew;
    @FXML private Label startDateLabel;
    @FXML private Label systemLabel;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setTableComponents();
        getWorkplanDetails();
        initializeListener();
        initializeResoucrListener();
    }

    public void showStage() {
        loadFXMLFile(getClass().getResource("/view/WorkplanDetailsView.fxml"), this);
        stage.showAndWait();
    }

    public WorkplanDetailsController(Workplan workplanSelected) {
        this.workplanSelected = workplanSelected;
    }

    public Workplan getWorkplanSelected() {
        return workplanSelected;
    }

    @FXML
    void cancelOnAction(ActionEvent event) {
        stage.close();
    }

    @FXML
    void modifyWorkplan(ActionEvent event) {

    }

    private void getWorkplanDetails() {
        try{
            workplanSelected = new WorkplanDAO().getWorkplanDetails(workplanSelected.getId());
            setDetailtsToComponents();
        } catch (SQLException sqlException) {
            Logger.getLogger(WorkplanDetailsController.class.getName()).log(Level.SEVERE, null, sqlException);
        }
    }

    private void setTableComponents() {
        identificatorTableColumn.setCellValueFactory(new PropertyValueFactory<>("identificator"));
        descriptionTableColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        endDateTableColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        stateTableColumn.setCellValueFactory( x -> new SimpleStringProperty( x.getValue().getState().getActivityState() ));
        actionDescriptionTableColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        memberAssignedTableColumn.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getResponsable().getFullName() + ": ID: " + x.getValue().getResponsable().getPersonalNumber()));
        actionsTableView.setPlaceholder(new Label(""));
        goalTableView.setPlaceholder(new Label(""));
        stateActionTableColumn.setCellValueFactory( x -> new SimpleStringProperty( x.getValue().getState().getActionState()));

    }

    private void setDetailtsToComponents() {
        idTextField.setText(workplanSelected.getIdentificator());
        startDateLabel.setText(workplanSelected.getStartDate().toString());
        endDateLabel.setText(workplanSelected.getEndDate().toString());
        generalObjetiveTextArea.setText(workplanSelected.getGeneralObjetive());
        goalTableView.setItems(FXCollections.observableArrayList (workplanSelected.getGoalList() == null ? new ArrayList<>() : workplanSelected.getGoalList()) );
    }

    private void initializeListener() {
        goalTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Goal>() {
            @Override
            public void changed(ObservableValue<? extends Goal> observable, Goal oldValue, Goal newValue) {
                if(newValue != null ) {
                    actionsTableView.setItems(FXCollections.observableArrayList(newValue.getActions()));
                } else {
                    actionsTableView.setItems(null);
                }
            }
        });
    }

    private void initializeResoucrListener() {
        actionsTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Action>() {
            @Override
            public void changed(ObservableValue<? extends Action> observable, Action oldValue, Action newValue) {
                if(newValue != null ) {
                    resourcesListVIew.setItems(FXCollections.observableArrayList(newValue.getRecursos()));
                } else {
                    resourcesListVIew.setItems(null);
                }
            }
        });
    }

}
