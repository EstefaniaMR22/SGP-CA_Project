/*
 * @author Josué 
 * @versión v1.0
 * Last modification date: 17-06-2021
 */

package model.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class WorkPlan implements Cloneable{
    
    private int workplanKey;
    private int durationInYears;
    private String generalTarget;
    private String startDatePlan;
    private String endDatePlan;
    private List<Goal> goals;
    private String bodyAcademyKey;

    public WorkPlan(int workplanKey, int durationInYears, String generalTarget, 
    String startDatePlan, String endDatePlan,String bodyAcademyKey){
        this.workplanKey = workplanKey;
        this.durationInYears = durationInYears;
        this.generalTarget = generalTarget;
        this.startDatePlan = startDatePlan;
        this.endDatePlan = endDatePlan;
        this.goals = new ArrayList<>();
        this.bodyAcademyKey = bodyAcademyKey;
    }

    public WorkPlan(){
        this.goals = new ArrayList<>();
    }
    
    public Goal getGoalByKey(int goalKey){
        Goal goalReturn = null;
        for(Goal goal : this.goals){
            if(goal.getGoalIdentifier() == goalKey){
                goalReturn = goal;
            }
        }
        return goalReturn;
    }

    public List<Goal> getGoals(){
        return goals;
    }

    public void setGoals(List<Goal> goals){
        this.goals = goals;
    }

    public String getBodyAcademyKey(){
        return bodyAcademyKey;
    }

    public void setBodyAcademyKey(String bodyAcademyKey){
        this.bodyAcademyKey = bodyAcademyKey;
    }
    
    public void addGoal(Goal newGoal){
        goals.add(newGoal);
    }
    
    public void removeGoal(Goal goal){
        goals.remove(goal);
    }

    public int getWorkplanKey(){
        return workplanKey;
    }

    public void setWorkplanKey(int workplanKey){
        this.workplanKey = workplanKey;
    }

    public int getDurationInYears(){
        return durationInYears;
    }

    public void setDurationInYears(int durationInYears){
        this.durationInYears = durationInYears;
    }

    public String getGeneralTarget(){
        return generalTarget;
    }

    public void setGeneralTarget(String generalTarget){
        this.generalTarget = generalTarget;
    }

    public String getStartDatePlan(){
        return startDatePlan;
    }

    public void setStartDatePlan(String startDatePlan){
        this.startDatePlan = startDatePlan;
    }

    public String getEndDatePlan(){
        return endDatePlan;
    }

    public void setEndDatePlan(String endDatePlan){
        this.endDatePlan = endDatePlan;
    }

    @Override
    public int hashCode(){
        int hash = Integer.parseInt(this.endDatePlan.substring(0, 4));
        hash += this.bodyAcademyKey.hashCode();
        return hash;
    }

    @Override
    public boolean equals(Object obj){
        boolean sameWorkPlans = true;
        WorkPlan workPlan = (WorkPlan)obj;
        if((this.hashCode() != workPlan.hashCode()) || (this.goals.size() != workPlan.getGoals().size())){
            sameWorkPlans = false;
        }else{
            for(int i = 0; i < this.goals.size(); i++){
                if(this.goals.get(i).getActions().size() != workPlan.getGoals().get(i).getActions().size()){
                    sameWorkPlans = false;
                }
            }
        }
        return sameWorkPlans;
    }
    
    public Goal searchGoalByDescription(String goalDescription){
        Goal goalSearched = null;
        for(Goal goal : this.goals){
            if(goal.getDescription().equalsIgnoreCase(goalDescription)){
                goalSearched = goal;
            }
        }
        return goalSearched;
    }
    
    public void deleteGoal(String goalDescription){
        for(Goal goal : this.goals){
            if(goal.getDescription().equalsIgnoreCase(goalDescription)){
                this.goals.remove(goal);
                break;
            }
        }
    }
    
    public void calculateDurationInYears(){
        LocalDate limimitDate = LocalDate.parse(this.endDatePlan);
        LocalDate initDate = LocalDate.parse(this.startDatePlan);
        this.durationInYears = limimitDate.getYear() - initDate.getYear();
    }
    
    public void updateGoal(Goal newGoal, String oldGoalDescription){
        this.deleteGoal(oldGoalDescription);
        this.goals.add(newGoal);
    }
    
}
