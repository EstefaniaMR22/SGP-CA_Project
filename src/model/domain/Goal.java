/*
 * @author Josué 
 * @versión v1.0
 * Last modification date: 17-06-2021
 */

package model.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class Goal{
    
    private int goalIdentifier;
    private String startDate;
    private String endDate;
    private boolean statusGoal;
    private String description;
    private List<Action> actions;

    public Goal(int goalIdentifier, String startDate, String endDate, 
    boolean statusGoal, String description){
        this.goalIdentifier = goalIdentifier;
        this.startDate = startDate;
        this.endDate = endDate;
        this.statusGoal = statusGoal;
        this.description = description;
        this.actions = new ArrayList<>();
    }
    
    public Goal(){
        this.actions = new ArrayList<>();
    }
    
    public Action getActionByIdentifier(int actionKey){
        Action actionReturn = null;
        for(Action action : this.actions){
            if(action.getActionKey() == actionKey){
                actionReturn = action;
            }
        }
        return actionReturn;
    }

    public List<Action> getActions(){
        return actions;
    }

    public void setActions(List<Action> actions){
        this.actions = actions;
    }
    
    public void addAction(Action newAction){
        actions.add(newAction);
    }
    
    public void removeAction(Action action){
        actions.remove(action);
    }

    public int getGoalIdentifier(){
        return goalIdentifier;
    }

    public void setGoalIdentifier(int goalIdentifier){
        this.goalIdentifier = goalIdentifier;
    }

    public String getStartDate(){
        return startDate;
    }

    public void setStartDate(String startDate){
        this.startDate = startDate;
    }

    public String getEndDate(){
        return endDate;
    }

    public void setEndDate(String endDate){
        this.endDate = endDate;
    }

    public boolean isStatusGoal(){
        return statusGoal;
    }

    public void setStatusGoal(boolean statusGoal){
        this.statusGoal = statusGoal;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }
    
    public Action searchActionByDescripcion(String actionDescription){
        Action actionReturn = null;
        for(Action action : this.actions){
            if(action.getDescriptionAction().equalsIgnoreCase(actionDescription)){
                actionReturn = action;
            }
        }
        return actionReturn;
    }
    
    public void updateEndDate(){
        this.endDate = "1000-01-01";
        for(Action action : this.actions){
            LocalDate dateTimeOne = LocalDate.parse(action.getEndDate());
            LocalDate dateTimeTwo = LocalDate.parse(this.endDate);
            if(dateTimeOne.isAfter(dateTimeTwo)){
                this.endDate = action.getEndDate();
            }
        }
    }
    
    public void updateStatus(){
        boolean goalStatus = true;
        for(Action action : this.actions){
            if(!action.isStatusAction()){
                goalStatus = false;
                break;
            }
        }
        this.statusGoal = goalStatus;
    }
    
    @Override
    public boolean equals(Object obj){
        boolean isEqual = false;
        Goal goal = (Goal)obj;
        if((this.description.equalsIgnoreCase(goal.getDescription())) && (this.actions.size() == goal.getActions().size())){
            isEqual = true;
        }
        return isEqual;
    }
}
