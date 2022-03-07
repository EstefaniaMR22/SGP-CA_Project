/*
 * @author Josué 
 * @versión v1.0
 * Last modification date: 17-06-2021
 */

package model.old.dataaccess;

import java.util.List;
import model.old.domain.WorkPlan;

public interface IWorkPlanDAO{
    
    public List<WorkPlan> getWorkPlanPeriots(String bodyAcademyKey);
    public WorkPlan getWorkPlan(String endDatePlan, String bodyAcademyKey);
    public boolean addWorkPlan(WorkPlan newWorkPlan);
    public boolean updateWorkPlan(WorkPlan worPlan, WorkPlan oldWorkPlan);
    public boolean deleteWorkPlan(WorkPlan workPlan, String bodyAcademyKey);
    
}
