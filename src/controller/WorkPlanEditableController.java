/*
 * @author Josué 
 * @versión v1.0
 * Last modification date: 17-06-2021
 */

package controller;

import com.jfoenix.controls.JFXDatePicker;
import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

import controller.pattern.GenericWindowDriver;
import controller.pattern.InvalidFormException;
import controller.pattern.ValidatorForm;
import model.domain.Action;
import model.domain.Goal;
import model.domain.Integrant;
import model.domain.WorkPlan;
import model.dataaccess.WorkPlanDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

public class WorkPlanEditableController implements Initializable{

    @FXML
    private HBox hboxWorkPlanOptions;
    @FXML
    private Button btnSaveWorkplan;
    @FXML
    private Button btnUpdateWorkplan;
    @FXML
    private Button btnCancelChanges;
    @FXML
    private HBox hboxActionOptions;
    @FXML
    private Button btnAddAction;
    @FXML
    private Button btnDeleteAction;
    @FXML
    private Button btnAddGoal;
    @FXML
    private Button btnSaveGoalChanges;
    @FXML
    private Button btnDeleteGoal;
    @FXML
    private TextArea txtAreaWorkPlanGeneralTarget;
    @FXML
    private TextArea txtAreaGoalDescripctionDetail;
    @FXML
    private TextField txtFieldActionResponsible;
    @FXML
    private TextArea txtAreaActionDescriptionDetail;
    @FXML
    private TextArea txtAreaActionResources;  
    @FXML
    private Label lbUserName;
    @FXML
    private JFXDatePicker dtpWorkplanStartDate;
    @FXML
    private JFXDatePicker dtpWorkplanEndDate;
    @FXML
    private ListView<String> lvGoals;
    @FXML
    private JFXDatePicker dtpGoalStartDate;
    @FXML
    private TextField txtFieldGoalEndDate;
    @FXML
    private JFXDatePicker dtpStimatedEndDate;
    @FXML
    private JFXDatePicker dtpStartDate;
    @FXML
    private CheckBox chBoxActionStatus;
    @FXML
    private TableView<Action> tvWorkplanActions;
    @FXML
    private TableColumn<Action, String> colActionDescription;
    @FXML
    private TableColumn<Action, String> colActionEndDateEstimated;
    @FXML
    private TableColumn<Action, String> colActionResponsible;
    @FXML
    private TableColumn<Action, String> colActionStartDate;
    @FXML
    private TableColumn<Action, String> colResourceAction;
    @FXML
    private TableColumn<Action, Boolean> colActionStatus;
    
    private Integrant token;
    private WorkPlan workplan;
    private WorkPlan oldWorkPlanClone;
    private final WorkPlanDAO WORKPLAN_DAO = new WorkPlanDAO();
    
    @Override
    public void initialize(URL url, ResourceBundle rb){
        this.hboxWorkPlanOptions.getChildren().removeAll(btnSaveWorkplan, btnUpdateWorkplan, btnCancelChanges);
        this.preprareActionsTable();
        this.workplan = new WorkPlan();
    }  
    
    public void showWorkPlanInsertionForm(Integrant token){
        this.token = token;
        this.hboxWorkPlanOptions.getChildren().addAll(btnSaveWorkplan, btnCancelChanges);
        this.lbUserName.setText(this.token.getFullName());
    }
    
    public void showWorkPlanUpdateForm(Integrant token, String workPlanEndDate){
        this.token = token;
        this.lbUserName.setText(this.token.getFullName());
        this.hboxWorkPlanOptions.getChildren().addAll(btnUpdateWorkplan, btnCancelChanges);
        this.workplan = WORKPLAN_DAO.getWorkPlan(workPlanEndDate, this.token.getBodyAcademyKey());
        this.oldWorkPlanClone = WORKPLAN_DAO.getWorkPlan(workPlanEndDate, this.token.getBodyAcademyKey());
        this.setWorkPlanDataIntoInterface();
    }
    
    @FXML
    private void saveWorkPlan(ActionEvent event){
        try{
            this.getOutWorkPlanDataFromInterface();
            if(WORKPLAN_DAO.addWorkPlan(this.workplan)){
                GenericWindowDriver.getGenericWindowDriver().showInfoAlert(event, "El plan de trabajo ha sido registrado exitosamente");
            }else{
                GenericWindowDriver.getGenericWindowDriver().showErrorAlert(event, "El sistema ha presentado un error, favor de ponerse en contacto con soporte técnico");
            }
            FXMLLoader loader = GenericWindowDriver.getGenericWindowDriver().changeWindow("WorkPlanRequest.fxml", btnCancelChanges);
            WorkPlanRequestController controller = loader.getController();
            controller.receiveToken(token);
        }catch(InvalidFormException ex){
            GenericWindowDriver.getGenericWindowDriver().showErrorAlert(event, ex.getMessage());
        }
    }

    @FXML
    private void updateWorkPlan(ActionEvent event){
        try{
            this.getOutWorkPlanDataFromInterface();
            if(WORKPLAN_DAO.updateWorkPlan(this.workplan, this.oldWorkPlanClone)){
                GenericWindowDriver.getGenericWindowDriver().showInfoAlert(event, "El plan de trabajo ha sido actualizado exitosamente");
            }else{
                GenericWindowDriver.getGenericWindowDriver().showErrorAlert(event, "El sistema ha presentado un error, favor de ponerse en contacto con soporte técnico");
            }
            FXMLLoader loader = GenericWindowDriver.getGenericWindowDriver().changeWindow("WorkPlanRequest.fxml", btnCancelChanges);
            WorkPlanRequestController controller = loader.getController();
            controller.receiveToken(token);
        }catch(InvalidFormException ex){
            GenericWindowDriver.getGenericWindowDriver().showErrorAlert(event, ex.getMessage());
        }
    }

    @FXML
    private void cancelChanges(ActionEvent event){
        Optional<ButtonType> action = GenericWindowDriver.getGenericWindowDriver().showConfirmacionAlert(event, "¿Seguro que deseas cancelar los cambios?");
        if(action.get() == ButtonType.OK){
            FXMLLoader loader = GenericWindowDriver.getGenericWindowDriver().changeWindow("WorkPlanRequest.fxml", btnCancelChanges);
            WorkPlanRequestController controller = loader.getController();
            controller.receiveToken(token);
        }
    }

    @FXML
    private void addAction(ActionEvent event){
        Action newAction = this.getActionDataFromInterface();
        if(newAction != null){
            Action oldAction = this.searchActionByDescription(txtAreaActionDescriptionDetail.getText());
            if(oldAction != null){
                tvWorkplanActions.getItems().remove(oldAction);
            }
            tvWorkplanActions.getItems().add(newAction);
            tvWorkplanActions.refresh();
        }
    }
    
    private Action getActionDataFromInterface(){
        Action action = null;
        try{
            this.checkActionForm();
            action = new Action(
                this.txtAreaActionDescriptionDetail.getText(),
                this.dtpStimatedEndDate.getValue().toString(),
                this.txtFieldActionResponsible.getText(),
                this.dtpStartDate.getValue().toString(),
                this.txtAreaActionResources.getText(),
                this.chBoxActionStatus.isSelected()
            );
            action.updateEndDate();
        }catch(InvalidFormException ex){
            GenericWindowDriver.getGenericWindowDriver().showErrorAlert(new ActionEvent(), ex.getMessage());
        }finally{
            return action;
        } 
    }
    

    @FXML
    private void deleteAction(ActionEvent event){
        tvWorkplanActions.getItems().remove(
            tvWorkplanActions.getSelectionModel().getSelectedItem()
        );
    }

    @FXML
    private void selectAction(MouseEvent event){
        if(tvWorkplanActions.getSelectionModel().getSelectedItem() != null){
            Action action = tvWorkplanActions.getSelectionModel().getSelectedItem();
            this.txtFieldActionResponsible.setText(action.getResponsibleAction());
            this.txtAreaActionResources.setText(action.getResource());
            this.txtAreaActionDescriptionDetail.setText(action.getDescriptionAction());
            this.dtpStartDate.setValue(LocalDate.parse(action.getStartDate()));
            this.dtpStimatedEndDate.setValue(LocalDate.parse(action.getEstimatedEndDate()));
            this.chBoxActionStatus.setSelected(action.isStatusAction());
            this.btnDeleteAction.setDisable(false);
        }
    }

    @FXML
    private void addGoal(ActionEvent event){
        this.lvGoals.getItems().add("[Sin guardar]");
    }

    @FXML
    private void saveGoalChanges(ActionEvent event){
        Goal goal = saveGoalDataFromInterface();
        if(goal != null){
            if(workplan.searchGoalByDescription(this.lvGoals.getSelectionModel().getSelectedItem()) == null){
                this.workplan.addGoal(goal);
            }else{
                this.workplan.updateGoal(goal, this.lvGoals.getSelectionModel().getSelectedItem());
            }
            this.clearGoalForm();
            this.refreshGoalsList();
        }
    }

    @FXML
    private void deleteGoal(ActionEvent event){
        if(this.lvGoals.getSelectionModel().getSelectedItem() != null){
            this.workplan.deleteGoal(this.lvGoals.getSelectionModel().getSelectedItem());
            this.refreshGoalsList();
        }
    }
    
    @FXML
    private void selectGoal(MouseEvent event){
        this.clearGoalForm();
        if(this.lvGoals.getSelectionModel().getSelectedItem() != null){
            this.setGoalDataInInterface(this.workplan.searchGoalByDescription(this.lvGoals.getSelectionModel().getSelectedItem()));
            this.btnAddAction.setDisable(false);
            this.btnSaveGoalChanges.setDisable(false);
            this.btnDeleteGoal.setDisable(false);
        }
    }
    
    private void setWorkPlanDataIntoInterface(){
        this.dtpWorkplanStartDate.setValue(LocalDate.parse(this.workplan.getStartDatePlan()));
        this.dtpWorkplanEndDate.setValue(LocalDate.parse(this.workplan.getEndDatePlan()));
        this.txtAreaWorkPlanGeneralTarget.setText(this.workplan.getGeneralTarget());
        this.refreshGoalsList();
    }
    
    private void getOutWorkPlanDataFromInterface() throws InvalidFormException{
        this.checkWorkPlanForm();
        this.workplan.setEndDatePlan(ValidatorForm.convertJavaDateToSQlDate(dtpWorkplanEndDate));
        this.workplan.setStartDatePlan(ValidatorForm.convertJavaDateToSQlDate(dtpWorkplanStartDate));
        this.workplan.setGeneralTarget(this.txtAreaWorkPlanGeneralTarget.getText());
        this.workplan.setBodyAcademyKey(this.token.getBodyAcademyKey());
    }
    
    private void setGoalDataInInterface(Goal goal){
        if(goal != null){
            this.txtAreaGoalDescripctionDetail.setText(goal.getDescription());
            this.dtpGoalStartDate.setValue(LocalDate.parse(goal.getStartDate()));
            this.txtFieldGoalEndDate.setText(goal.getEndDate());
            this.tvWorkplanActions.getItems().addAll(goal.getActions());
        }
    }
                
    private void preprareActionsTable(){
        colActionDescription.setCellValueFactory(new PropertyValueFactory<>("descriptionAction"));
        colActionEndDateEstimated.setCellValueFactory(new PropertyValueFactory<>("estimatedEndDate"));
        colActionResponsible.setCellValueFactory(new PropertyValueFactory<>("responsibleAction"));
        colActionStartDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        colResourceAction.setCellValueFactory(new PropertyValueFactory<>("resource"));
        colActionStatus.setCellValueFactory(new PropertyValueFactory<>("statusAction"));
    }
    
    private void clearGoalForm(){
        this.tvWorkplanActions.getItems().removeAll(tvWorkplanActions.getItems());
        this.txtAreaGoalDescripctionDetail.setText("");
        this.txtFieldGoalEndDate.setText(null);
        this.dtpGoalStartDate.setValue(null);
        this.txtFieldActionResponsible.setText("");
        this.dtpStimatedEndDate.setValue(null);
        this.dtpStartDate.setValue(null);
        this.txtAreaActionDescriptionDetail.setText("");
        this.txtAreaActionResources.setText("");
        this.btnAddAction.setDisable(true);
        this.btnDeleteAction.setDisable(true);
    }
    
    private Goal saveGoalDataFromInterface(){
        Goal goal = null;
        try{
            this.checkGoalForm();
            goal = new Goal();
            goal.setDescription(txtAreaGoalDescripctionDetail.getText());
            goal.setStartDate(ValidatorForm.convertJavaDateToSQlDate(dtpGoalStartDate));
            for(Action action : tvWorkplanActions.getItems()){
                action.updateEndDate();
                goal.addAction(action);
            }
            goal.updateEndDate();
            goal.updateStatus();
        }catch(InvalidFormException ex){
            GenericWindowDriver.getGenericWindowDriver().showErrorAlert(new ActionEvent(), ex.getMessage());
        }finally{
            return goal;
        }
    }
    
    private void refreshGoalsList(){
        this.lvGoals.getItems().clear();
        this.workplan.getGoals().forEach(goalElement -> {
            this.lvGoals.getItems().add(goalElement.getDescription());
        });
    }
    
    private Action searchActionByDescription(String description){
        Action actionReturn = null;
        for(Action action : tvWorkplanActions.getItems()){
            if(action.getDescriptionAction().equalsIgnoreCase(description)){
                actionReturn = action;
                break;
            }
        }
        return actionReturn;
    }
    
    private void checkActionForm() throws InvalidFormException{
        ValidatorForm.checkNotEmptyDateField(dtpWorkplanStartDate);
        ValidatorForm.checkNotEmptyDateField(this.dtpStartDate);
        ValidatorForm.checkNotEmptyDateField(this.dtpStimatedEndDate);
        ValidatorForm.chechkAlphabeticalField(txtFieldActionResponsible, 3, 50);
        ValidatorForm.chechkAlphabeticalArea(txtAreaActionResources, 0, 450);
        ValidatorForm.chechkAlphabeticalArea(txtAreaActionDescriptionDetail, 5, 450);
        if(dtpStimatedEndDate.getValue().isBefore(dtpStartDate.getValue())){
            dtpStartDate.setStyle("-fx-border-color: red;");
            throw new InvalidFormException("La fecha de inicio no puede ser despues del supuesto final");
        }
        this.checkIntervalDate(dtpStartDate, dtpWorkplanStartDate, dtpStimatedEndDate);
    }
    
    private void checkGoalForm() throws InvalidFormException{
        ValidatorForm.checkNotEmptyDateField(dtpWorkplanStartDate);
        ValidatorForm.chechkAlphabeticalArea(txtAreaGoalDescripctionDetail, 5, 450);
        ValidatorForm.checkNotEmptyDateField(dtpGoalStartDate);
        if(tvWorkplanActions.getItems().isEmpty()){
            throw new InvalidFormException("No puede haber una meta sin acciones");
        }
        if(dtpGoalStartDate.getValue().isBefore(dtpWorkplanStartDate.getValue())){
            dtpGoalStartDate.setStyle("-fx-border-color: red;");
            throw new InvalidFormException("La fecha de inicio no puede ser antes del inicio del plan de trabajo");
        }
    }
    
    private void checkWorkPlanForm() throws InvalidFormException{
        ValidatorForm.checkNotEmptyDateField(dtpWorkplanStartDate);
        ValidatorForm.checkNotEmptyDateField(dtpWorkplanEndDate);
        ValidatorForm.chechkAlphabeticalArea(txtAreaWorkPlanGeneralTarget, 5, 450);
        this.workplan.setStartDatePlan(ValidatorForm.convertJavaDateToSQlDate(this.dtpWorkplanStartDate));
        this.workplan.setEndDatePlan(ValidatorForm.convertJavaDateToSQlDate(this.dtpWorkplanEndDate));
        this.workplan.calculateDurationInYears();
        if(this.workplan.getDurationInYears() < 1){
            throw new InvalidFormException("El plan de trabajo debe durar almenos 1 año");
        }
        if(this.workplan.getGoals().isEmpty()){
            throw new InvalidFormException("Un plan de trabajo debe tener almenos 1 meta");
        }
    }
    
    private void checkIntervalDate(JFXDatePicker evaluationDate, JFXDatePicker initDate, JFXDatePicker endDate) throws InvalidFormException{
        if(evaluationDate.getValue().isBefore(initDate.getValue()) || evaluationDate.getValue().isAfter(endDate.getValue())){
            evaluationDate.setStyle("-fx-border-color: red;");
            throw new InvalidFormException("fecha fuera del límite");
        }
    }
    
}
