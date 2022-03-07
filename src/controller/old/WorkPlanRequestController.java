/*
 * @author Josué 
 * @versión v1.0
 * Last modification date: 17-06-2021
 */

package controller.old;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import model.old.domain.Action;
import model.old.domain.Goal;
import model.old.domain.Integrant;
import model.old.domain.WorkPlan;
import model.old.dataaccess.WorkPlanDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

public class WorkPlanRequestController implements Initializable{

    @FXML
    private Button btnExit;
    @FXML
    private Button btnUpdateWorkplan;
    @FXML
    private Button btnAddNewWorkplan;
    @FXML
    private Label lbUserName;
    @FXML
    private Label lbDuration;
    @FXML
    private Label lbGoalStartDate;
    @FXML
    private Label lbGoalEndDate;
    @FXML
    private Label lbGoalStatus;
    @FXML
    private HBox hbSelectionPane;
    @FXML
    private ComboBox<String> cboBoxWorkPlanPeriot;
    @FXML
    private TextArea txtAreaGeneralTarget;
    @FXML
    private ComboBox<String> cboBoxGoals;
    @FXML
    private TextArea txtAreaGoalDescription;
    @FXML
    private TableView<Action> tvActions;
    @FXML
    private TableColumn<Action, String> colActionDescription;
    @FXML
    private TableColumn<Action, String> colActionEstimatedEndDate;
    @FXML
    private TableColumn<Action, String> colActionEndDate;
    @FXML
    private TableColumn<Action, String> colActionResponsible;
    @FXML
    private TableColumn<Action, String> colActionStartDate;
    @FXML
    private TableColumn<Action, String> colResources;
    @FXML
    private TextArea txtAreaActionDescription;
    @FXML
    private TextArea txtAreaActionResources;
    
    private Integrant token;
    private final WorkPlanDAO WORKPLAN_DAO = new WorkPlanDAO();
    private WorkPlan workplan;
    
    @Override
    public void initialize(URL url, ResourceBundle rb){
        this.workplan = new WorkPlan();
        this.preprareActionsTable();
    }

    public void receiveToken(Integrant token){
        this.token = token;
        this.lbUserName.setText(this.token.getFullName());
        this.setDataInWorkPlanInterface();
    }

    @FXML
    private void exit(ActionEvent event){
        FXMLLoader loader = WindowControllerB.getWindowController().changeWindow("Start.fxml", btnExit);
        StartController controller = loader.getController();
        controller.receiveIntegrantToken(token);
    }

    @FXML
    private void updateWorkplan(ActionEvent event){
        if(this.cboBoxWorkPlanPeriot.getValue() != null){
            String date = this.cboBoxWorkPlanPeriot.getSelectionModel().getSelectedItem();
            date = date.substring(date.length()-11, date.length()-1);
            FXMLLoader loader = WindowControllerB.getWindowController().changeWindow("WorkPlanEditable.fxml", btnExit);
            WorkPlanEditableController controller = loader.getController();
            controller.showWorkPlanUpdateForm(token, date); 
        }else{
            WindowControllerB.getWindowController().showErrorAlert(new ActionEvent(), "Debes seleccionar un plan de trabajo primero");
        }
    }

    @FXML
    private void addNewWorkPlan(ActionEvent event){
        FXMLLoader loader = WindowControllerB.getWindowController().changeWindow("WorkPlanEditable.fxml", btnExit);
        WorkPlanEditableController controller = loader.getController();
        controller.showWorkPlanInsertionForm(token);
    }
    
    @FXML
    private void selectPeriot(ActionEvent event){
        String date = this.cboBoxWorkPlanPeriot.getSelectionModel().getSelectedItem();
        date = date.substring(date.length()-11, date.length()-1);
        this.workplan = WORKPLAN_DAO.getWorkPlan(date, this.token.getBodyAcademyKey());
        if(this.workplan == null){
            WindowControllerB.getWindowController().showErrorAlert(new ActionEvent(), "Lo sentimos, prece que el sistema no se encuentra disponible en este momento.");
            FXMLLoader loader = WindowControllerB.getWindowController().changeWindow("Start.fxml", btnExit);
            StartController controller = loader.getController();
            controller.receiveIntegrantToken(token);
        }
        this.setWorkPlanSelectedDataIntoInterface();
    }

    
    private void setDataInWorkPlanInterface(){
        List<WorkPlan> workplanPeriots = WORKPLAN_DAO.getWorkPlanPeriots(token.getBodyAcademyKey());
        if(workplanPeriots != null){
            workplanPeriots.forEach(periot -> {
                cboBoxWorkPlanPeriot.getItems().add("Periodo: [" + periot.getStartDatePlan() + " - " + periot.getEndDatePlan() + "]");
            });
        }else{
            WindowControllerB.getWindowController().showErrorAlert(new ActionEvent(), "Lo sentimos, prece que el sistema no se encuentra disponible en este momento.");
            FXMLLoader loader = WindowControllerB.getWindowController().changeWindow("Start.fxml", btnExit);
            StartController controller = loader.getController();
            controller.receiveIntegrantToken(token);
        }
    }

    @FXML
    private void selectGoal(ActionEvent event){
        String goalDescriptionSelected = this.cboBoxGoals.getSelectionModel().getSelectedItem(); 
        if(goalDescriptionSelected != null){
            Goal goal = this.workplan.searchGoalByDescription(goalDescriptionSelected);
            this.lbGoalStatus.setText(String.valueOf(goal.isStatusGoal()));
            this.lbGoalStartDate.setText(goal.getStartDate());
            this.lbGoalEndDate.setText(goal.getEndDate());
            this.txtAreaGoalDescription.setText(goal.getDescription());
            this.tvActions.getItems().clear();
            goal.getActions().forEach(action -> {
                this.tvActions.getItems().add(action);
            });
        }
    }

    @FXML
    private void selectAction(MouseEvent event){
        if(tvActions.getSelectionModel().getSelectedItem() != null){
            Action action = this.tvActions.getSelectionModel().getSelectedItem();
            this.txtAreaActionDescription.setText(action.getDescriptionAction());
            this.txtAreaActionResources.setText(action.getResource());
        }
    }
    
    private void setWorkPlanSelectedDataIntoInterface(){
        this.lbDuration.setText(String.valueOf(this.workplan.getDurationInYears()) + " Años");
        this.txtAreaGeneralTarget.setText(this.workplan.getGeneralTarget());
        this.cboBoxGoals.getItems().clear();
        this.workplan.getGoals().forEach(goal -> {
            cboBoxGoals.getItems().add(goal.getDescription());
        });
    }
    
    private void preprareActionsTable(){
        colActionDescription.setCellValueFactory(new PropertyValueFactory<>("descriptionAction"));
        colActionEndDate.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        colActionEstimatedEndDate.setCellValueFactory(new PropertyValueFactory<>("estimatedEndDate"));
        colActionStartDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        colResources.setCellValueFactory(new PropertyValueFactory<>("resource"));
        colActionResponsible.setCellValueFactory(new PropertyValueFactory<>("responsibleAction"));
    }
    
}
