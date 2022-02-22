/*
 * @author Josué 
 * @versión v1.0
 * Last modification date: 17-06-2021
 */

package model.dataaccess;

import java.sql.Connection;
import java.util.List;
import model.domain.Action;
import model.domain.Goal;
import model.domain.WorkPlan;

public interface IGoalDAO{
    
    public void addGoals(Connection connection, WorkPlan workPlan);
    public List<Goal> getGoalListByWorkPlan(Connection connection, int getWorkplanKey);
    public void addActions(Connection connection, Goal goal);
    public List<Action> getActionsByGoal(Connection connection, int goalIdentifier);
    
}
