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

public class WorkPlanUpdate {
    
    public final WorkPlanInitializer INITIALIZER = new WorkPlanInitializer();
    public final WorkPlanDAO WORKPLAN_DAO = new WorkPlanDAO();
    
    @Test
    public void testCorrectWorkPlanUpdateDifferentActions(){
        INITIALIZER.prepareRequestWorkPlanTestCase();
        WorkPlan oldWorkPlan = WORKPLAN_DAO.getWorkPlan("2021-12-12", "UV-CA-127");
        WorkPlan newWorkplan = new WorkPlan(0, 2, "Mantener el grado en consolidación del cuerpo académico", "2020-12-12", "2022-12-12", "UV-CA-127");
        newWorkplan.getGoals().add(new Goal(0, "2020-12-12", "2021-01-11", false, "Para el 2022 el 80 % de los integrantes del CA tiene el grado de doctor."));
        newWorkplan.getGoals().get(0).getActions().add(new Action(0, "2020-12-12", "1012-12-11", "2012-11-11", "Integrar a un nuevo PTC con grado de Doctor al CA", "KCV y XLR", "Documentción del nuevo PTC", false));
        newWorkplan.getGoals().get(0).getActions().add(new Action(0, "2020-12-12", "1012-12-11", "2012-11-11", "Documentción del nuevo PTC", "OOH", "Ninguno", true));
        WORKPLAN_DAO.updateWorkPlan(newWorkplan, oldWorkPlan);
        WorkPlan workPlanRetrieved = WORKPLAN_DAO.getWorkPlan("2022-12-12", "UV-CA-127");
        WORKPLAN_DAO.deleteWorkPlan(newWorkplan, newWorkplan.getBodyAcademyKey());
        Assert.assertNotEquals(oldWorkPlan, workPlanRetrieved);
    }
    
    @Test
    public void testCorrectWorkPlanUpdateDifferentGoals(){
        INITIALIZER.prepareRequestWorkPlanTestCase();
        WorkPlan oldWorkPlan = WORKPLAN_DAO.getWorkPlan("2021-12-12", "UV-CA-127");
        WorkPlan workplan = new WorkPlan(0, 2, "Mantener el grado en consolidación del cuerpo académico", "2020-12-12", "2021-12-12", "UV-CA-127");
        workplan.getGoals().add(new Goal(0, "2020-12-12", "2021-01-11", false, "Para el 2022 el 80 % de los integrantes del CA tiene el grado de doctor."));
        workplan.getGoals().add(new Goal(0, "2020-12-12", "2021-05-10", false, "El 100% de los integrantes del CA cuentan con perfil deseable PRODEP."));
        workplan.getGoals().add(new Goal(0, "2020-12-12", "2021-05-10", false, "Continuar con el desarrollo de los proyectos de investigación"));
        workplan.getGoals().get(0).getActions().add(new Action(0, "2019-12-12", "1012-12-11", "2012-11-11", "Integrar a un nuevo PTC con grado de Doctor al CA", "KCV y XLR", "Documentción del nuevo PTC", false));
        workplan.getGoals().get(1).getActions().add(new Action(0, "2020-12-12", "1012-12-11", "2012-11-11", "Documentción del nuevo PTC", "OOH", "Ninguno", false));
        workplan.getGoals().get(2).getActions().add(new Action(0, "2021-12-12", "1012-12-11", "2012-11-11", "Mantener actualizado el Currículum en PRODEP.", "KVC", "Ninguno", false));
        WORKPLAN_DAO.updateWorkPlan(workplan, oldWorkPlan);
        WorkPlan workPlanRetrieved = WORKPLAN_DAO.getWorkPlan("2021-12-12", "UV-CA-127");
        WORKPLAN_DAO.deleteWorkPlan(workplan, workplan.getBodyAcademyKey());
        Assert.assertNotEquals(oldWorkPlan, workPlanRetrieved);
        
    }
    
    @Test
    public void testCorrectWorkPlanUpdateWithoutActionsAndGoals(){
        INITIALIZER.prepareRequestWorkPlanTestCase();
        WorkPlan oldWorkPlan = WORKPLAN_DAO.getWorkPlan("2021-12-12", "UV-CA-127");
        
        String generalTarget = "Mantener el grado en consolidación del cuerpo académico";
        String bodyAcademyKey = "UV-CA-127";
        WorkPlan workplan = new WorkPlan(0, 2, generalTarget, "2020-12-12", "2021-12-12", bodyAcademyKey);
        WORKPLAN_DAO.updateWorkPlan(workplan, oldWorkPlan);
        
        WorkPlan workPlanRetrieved = WORKPLAN_DAO.getWorkPlan("2021-12-12", "UV-CA-127");
        WORKPLAN_DAO.deleteWorkPlan(workplan, "UV-CA-127");
        Assert.assertNotEquals(oldWorkPlan, workPlanRetrieved);
        
    }
    
    @Test
    public void testCorrectWorkPlanUpdateNotChanges(){
        INITIALIZER.prepareRequestWorkPlanTestCase();
        WorkPlan oldWorkPlan = WORKPLAN_DAO.getWorkPlan("2021-12-12", "UV-CA-127");
        
        WORKPLAN_DAO.updateWorkPlan(oldWorkPlan, oldWorkPlan);
        WorkPlan workPlanRetrieved = WORKPLAN_DAO.getWorkPlan("2021-12-12", "UV-CA-127");
        WORKPLAN_DAO.deleteWorkPlan(oldWorkPlan, "UV-CA-127");
        Assert.assertEquals(oldWorkPlan, workPlanRetrieved);
    }
    
    @Test
    public void testIncorrectWorkPlanUpdateInvalidActionInformation(){
        INITIALIZER.prepareRequestWorkPlanTestCase();
        WorkPlan oldWorkPlan = WORKPLAN_DAO.getWorkPlan("2021-12-12", "UV-CA-127");
        WorkPlan newWorkplan = new WorkPlan(0, 2, "Mantener el grado en consolidación del cuerpo académico", "2020-12-12", "2022-12-12", "UV-CA-127");
        newWorkplan.getGoals().add(new Goal(0, "2020-12-12", "2021-01-11", false, "Para el 2022 el 80 % de los integrantes del CA tiene el grado de doctor."));
        newWorkplan.getGoals().get(0).getActions().add(new Action(0, "2020-12-12", "1012-12-11", "2012-11-11", "Integrar a un nuevo PTC con grado de Doctor al CA", "KCV y XLR", "Documentción del nuevo PTC", false));
        newWorkplan.getGoals().get(0).getActions().add(new Action(0, "202020-12-12", "1012-12-11", "2012-11-11", "Documentción del nuevo PTC", "OOH", "Ninguno", true));
        WORKPLAN_DAO.updateWorkPlan(newWorkplan, oldWorkPlan);
        WorkPlan workPlanRetrieved = WORKPLAN_DAO.getWorkPlan("2021-12-12" , "UV-CA-127");
        INITIALIZER.cleanWorkPlanTestCaseData();
        Assert.assertEquals(oldWorkPlan, workPlanRetrieved);
    }
    
}
