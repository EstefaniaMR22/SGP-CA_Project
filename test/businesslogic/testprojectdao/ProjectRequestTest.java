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


public class ProjectRequestTest {
    
   private final ProjectInitializer INITIALIZER = new ProjectInitializer();
   public final ProjectDAO PROJECT_DAO = new ProjectDAO();
    
    @Test
    public void testGetExistProjectByUrlFile(){
        INITIALIZER.prepareProjectInsertionForTest();
        Project projectRetrieved = PROJECT_DAO.getProjectbyName("PRUEBA PROYECTO");
        String supposedProject = "PRUEBA PROYECTO";
        INITIALIZER.cleanProjectTest("PRUEBA PROYECTO");
        Assert.assertEquals(projectRetrieved.getProjectName(), supposedProject);
    }
    
    @Test
    public void testGetNotExistReceptionWorKByUrlFile(){
        INITIALIZER.cleanProjectTest("PRUEBA PROYECTO");
        Project projectRetrieved = PROJECT_DAO.getProjectbyName("PRUEBA PROYECTO");
        Assert.assertNull(projectRetrieved.getProjectName());
    }
}
