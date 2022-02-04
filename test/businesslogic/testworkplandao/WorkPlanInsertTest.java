/*
 * @author Josué 
 * @versión v1.0
 * Last modification date: 17-06-2021
 */

package businesslogic.testworkplandao;

import org.junit.Test;
import org.junit.Assert;
import sgp.ca.businesslogic.WorkPlanDAO;
import sgp.ca.domain.Action;
import sgp.ca.domain.Goal;
import sgp.ca.domain.WorkPlan;


public class WorkPlanInsertTest {
    
    public final WorkPlanInitializer INITIALIZER = new WorkPlanInitializer();
    public final WorkPlanDAO WORKPLAN_DAO = new WorkPlanDAO();    
    private WorkPlan workplan;
    
    @Test
    public void testCorrectWorkPlanInsert(){
        workplan = new WorkPlan(0, 2, "Mantener el grado en consolidación del cuerpo académico", "2020-12-12", "2021-12-12", "UV-CA-127");
        workplan.getGoals().add(new Goal(0, "2012-12-12", "2021-01-11", false, "Para el 2022 el 80 % de los integrantes del CA tiene el grado de doctor."));
        workplan.getGoals().add(new Goal(0, "2012-12-12", "2021-05-10", false, "El 100% de los integrantes del CA cuentan con perfil deseable PRODEP."));
        workplan.getGoals().get(0).getActions().add(new Action(0, "2012-12-12", "1012-12-11", "2012-11-11", "Integrar a un nuevo PTC con grado de Doctor al CA", "KCV y XLR", "Documentción del nuevo PTC", false));
        workplan.getGoals().get(1).getActions().add(new Action(0, "2012-12-12", "1012-12-11", "2012-11-11", "Documentción del nuevo PTC", "OOH", "Ninguno", false));
        workplan.getGoals().get(1).getActions().add(new Action(0, "2012-12-12", "1012-12-11", "2012-11-11", "Mantener actualizado el Currículum en PRODEP.", "KVC", "Ninguno", false));
        WORKPLAN_DAO.addWorkPlan(workplan);
        WorkPlan workPlanRetrieved = WORKPLAN_DAO.getWorkPlan("2021-12-12", "UV-CA-127");
        WORKPLAN_DAO.deleteWorkPlan(workplan, workplan.getBodyAcademyKey());
        Assert.assertEquals(workplan, workPlanRetrieved);
    }
    
    @Test
    public void testIncorrectWorkPlanInsertInvalidData(){
        workplan = new WorkPlan(0, 2, "Mantener el grado en consolidación del cuerpo académico", "2020-12-12", "2021-12-12", "UV-CA-127");
        workplan.getGoals().add(new Goal(0, "2012-12-12", "2021-01-11", false, "Para el 2022 el 80 % de los integrantes del CA tiene el grado de doctor."));
        workplan.getGoals().add(new Goal(0, "2012-12-12", "2021-05-10", false, "El 100% de los integrantes del CA cuentan con perfil deseable PRODEP."));
        workplan.getGoals().get(0).getActions().add(new Action(0, "2012-12-12", "1012-12-11", "2012-11-11", "Integrar a un nuevo PTC con grado de Doctor al CA", "KCV y XLR", "Documentción del nuevo PTC", false));
        workplan.getGoals().get(1).getActions().add(new Action(0, "2012-12-12", "1012-12-11", "2012-11-11", "Documentción del nuevo PTC", "OOH", "Ninguno", false));
        workplan.getGoals().get(1).getActions().add(new Action(0, "20122-12-12", "1012-12-11", "2012-11-11", "Mantener actualizado el Currículum en PRODEP.", "KVC", "Ninguno", false));
        WORKPLAN_DAO.addWorkPlan(workplan);
        WorkPlan workPlanRetrieved = WORKPLAN_DAO.getWorkPlan("2021-12-12", "UV-CA-127");
        Assert.assertNull(workPlanRetrieved.getGeneralTarget());
    }
    
}
