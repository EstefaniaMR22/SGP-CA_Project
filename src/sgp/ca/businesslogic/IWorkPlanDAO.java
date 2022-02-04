/*
 * @author Josué 
 * @versión v1.0
 * Last modification date: 17-06-2021
 */

package sgp.ca.businesslogic;

import java.util.List;
import sgp.ca.domain.WorkPlan;

public interface IWorkPlanDAO{
    
    public List<WorkPlan> getWorkPlanPeriots(String bodyAcademyKey);
    public WorkPlan getWorkPlan(String endDatePlan, String bodyAcademyKey);
    public boolean addWorkPlan(WorkPlan newWorkPlan);
    public boolean updateWorkPlan(WorkPlan worPlan, WorkPlan oldWorkPlan);
    public boolean deleteWorkPlan(WorkPlan workPlan, String bodyAcademyKey);
    
}
