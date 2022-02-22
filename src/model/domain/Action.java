/*
* @author Josué 
* @version v1.0
* Last modification date: 16-06-2021
*/

package model.domain;

import java.util.Calendar;
import java.util.GregorianCalendar;


public class Action{
    
    private int actionKey;
    private String startDate;
    private String endDate;
    private String estimatedEndDate;
    private String descriptionAction;
    private String responsibleAction;
    private String resource;
    private Boolean statusAction = false;

    public Action(int actionKey, String startDate, String endDate, 
    String estimatedEndDate, String descriptionAction, 
    String responsibleAction, String resource, boolean statusAction){
        this.actionKey = actionKey;
        this.startDate = startDate;
        this.endDate = endDate;
        this.estimatedEndDate = estimatedEndDate;
        this.descriptionAction = descriptionAction;
        this.responsibleAction = responsibleAction;
        this.resource = resource;
        this.statusAction = statusAction;
    }
    
    public Action(String descriptionAction, String estimatedEndDate, String responsibleAction, String startDate, String resource, Boolean statusAction){
        this.startDate = startDate;
        this.estimatedEndDate = estimatedEndDate;
        this.descriptionAction = descriptionAction;
        this.responsibleAction = responsibleAction;
        this.resource = resource;
        this.statusAction = statusAction;
    }
    
    public Action(String descriptionAction, String estimatedEndDate, String endDate, String responsibleAction, String startDate, String resource){
        this.resource = resource;
        this.startDate = startDate;
        this.endDate = endDate;
        this.estimatedEndDate = estimatedEndDate;
        this.descriptionAction = descriptionAction;
        this.responsibleAction = responsibleAction;
    }
    
    public Action(){
        
    }

    public int getActionKey(){
        return actionKey;
    }

    public void setActionKey(int actionKey){
        this.actionKey = actionKey;
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

    public String getEstimatedEndDate(){
        return estimatedEndDate;
    }

    public void setEstimatedEndDate(String estimatedEndDate){
        this.estimatedEndDate = estimatedEndDate;
    }

    public boolean isStatusAction(){
        return statusAction;
    }

    public void setStatusAction(boolean statusAction){
        this.statusAction = statusAction;
    }

    public String getDescriptionAction(){
        return descriptionAction;
    }

    public void setDescriptionAction(String descriptionAction){
        this.descriptionAction = descriptionAction;
    }

    public String getResponsibleAction(){
        return responsibleAction;
    }

    public void setResponsibleAction(String responsibleAction){
        this.responsibleAction = responsibleAction;
    }

    public String getResource(){
        return resource;
    }

    public void setResource(String resource){
        this.resource = resource;
    }
    
    public void updateEndDate(){
        Calendar currentDate = new GregorianCalendar();
        int year = currentDate.get(Calendar.YEAR);
        int month = currentDate.get(Calendar.MONTH);
        int day = currentDate.get(Calendar.DAY_OF_MONTH);
        if(this.statusAction){
            this.endDate = year + "-" + (month+1) + "-" + day;
        }else{
            this.endDate = this.estimatedEndDate;
        }
    }
    
    @Override
    public boolean equals(Object obj){
        boolean isEqual = false;
        if(this.descriptionAction.equalsIgnoreCase(((Action)obj).getDescriptionAction())){
            isEqual = true;
        }
        return isEqual;
    }
    
}
