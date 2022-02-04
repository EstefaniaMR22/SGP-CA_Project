/*
* @author Johann
* @versión v1.0
* Last modification date: 17-06-2021
*/
package businesslogic.testprojectdao;

import org.junit.Assert;
import org.junit.Test;
import sgp.ca.businesslogic.ProjectDAO;
import sgp.ca.domain.Project;

public class ProjectInsertTest {
    
    public final ProjectDAO PROJECT_DAO = new ProjectDAO();
    private final ProjectInitializer INITIALIZER = new ProjectInitializer();
    private Project project;
    
    @Test
    public void testCorrectProjectInsert(){
        project = new Project(
                "PRUEBA PROYECTO","UV-CA-127", 6, "Activo", 
                "2021-04-07", null, "2021-10-09", "Enfocado en predecir lo que sucedera en los años siguientes"
            );
        PROJECT_DAO.addProject(project);
        Project projectRetrieved = PROJECT_DAO.getProjectbyName("PRUEBA PROYECTO");
        INITIALIZER.cleanProjectTest("PRUEBA PROYECTO");
        Assert.assertEquals(project.getProjectName(), projectRetrieved.getProjectName());
    }
    
     @Test
    public void testIncorrectProjectInsertionDuplicated(){
        String oldProjectName = "PRUEBA PROYECTO";
        project = new Project(
            "PRUEBA PROYECTO","UV-CA-127", 6, "Activo", 
                "2021-04-07", null, "2021-10-09", "Enfocado en predecir lo que sucedera en los años siguientes"
        );
        INITIALIZER.cleanProjectTest("PRUEBA PROYECTO");
        PROJECT_DAO.addProject(project);
        Project projectRetrieved = PROJECT_DAO.getProjectbyName("PRUEBA PROYECTO");
        INITIALIZER.cleanProjectTest("PRUEBA PROYECTO");
        Assert.assertEquals(oldProjectName, projectRetrieved.getProjectName());
    }
    
}
