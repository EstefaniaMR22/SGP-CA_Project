/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businesslogic.testworkplandao;

import sgp.ca.businesslogic.WorkPlanDAO;
import sgp.ca.domain.Action;
import sgp.ca.domain.Goal;
import sgp.ca.domain.WorkPlan;


public class WorkPlanInitializer {
    
    private final WorkPlanDAO WORKPLAN_DAO = new WorkPlanDAO();
    private WorkPlan workplan;
    
    public void createObjectForTest(){
        workplan = new WorkPlan(0, 2, "Mantener el grado en consolidación del cuerpo académico", "2020-12-12", "2021-12-12", "UV-CA-127");
        workplan.getGoals().add(new Goal(0, "2012-12-12", "2021-01-11", false, "Para el 2022 el 80 % de los integrantes del CA tiene el grado de doctor."));
        workplan.getGoals().add(new Goal(0, "2012-12-12", "2021-05-10", false, "El 100% de los integrantes del CA cuentan con perfil deseable PRODEP."));
        workplan.getGoals().get(0).getActions().add(new Action(0, "2012-12-12", "1012-12-11", "2012-11-11", "Integrar a un nuevo PTC con grado de Doctor al CA", "KCV y XLR", "Documentción del nuevo PTC", false));
        workplan.getGoals().get(1).getActions().add(new Action(0, "2012-12-12", "1012-12-11", "2012-11-11", "Documentción del nuevo PTC", "OOH", "Ninguno", false));
        workplan.getGoals().get(1).getActions().add(new Action(0, "2012-12-12", "1012-12-11", "2012-11-11", "Mantener actualizado el Currículum en PRODEP.", "KVC", "Ninguno", false));
    }
    
    public void prepareRequestWorkPlanTestCase(){
        if(WORKPLAN_DAO.getWorkPlan("2021-12-12", "UV-CA-127").getGeneralTarget() == null){
            this.createObjectForTest();
            WORKPLAN_DAO.addWorkPlan(workplan);
        }
    }
    
    public void cleanWorkPlanTestCaseData(){
        this.createObjectForTest();
        workplan = WORKPLAN_DAO.getWorkPlan("2021-12-12", "UV-CA-127");
        WORKPLAN_DAO.deleteWorkPlan(workplan, "UV-CA-127");
    }
    
}
