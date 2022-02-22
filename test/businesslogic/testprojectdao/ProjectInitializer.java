/*
* @author Johann
* @versión v1.0
* Last modification date: 17-06-2021
*/
package businesslogic.testprojectdao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import sgp.ca.businesslogic.ProjectDAO;
import utils.ConnectionDatabase;
import sgp.ca.domain.Project;

public class ProjectInitializer{
    
    public final ProjectDAO PROJECT_DAO = new ProjectDAO();
    private final ConnectionDatabase CONNECTION = new ConnectionDatabase();
    private Project project;
    
    public void prepareProjectInsertionForTest(){
        if(PROJECT_DAO.getProjectbyName("PRUEBA PROYECTO") != null){
             project = new Project(
                "PRUEBA PROYECTO","UV-CA-127", 6, "Activo", 
                "2021-04-07", null, "2021-10-09", "Enfocado en predecir lo que sucedera en los años siguientes"
            );
        PROJECT_DAO.addProject(project);
        }
    }
    
    public void cleanProjectTest(String projectName){
        Connection connection = CONNECTION.getConnectionDatabase();
        try{
            PreparedStatement sentenceQuerySchooling = connection.prepareStatement(
                "DELETE FROM Project WHERE projectName = ?;"
            );
            sentenceQuerySchooling.setString(1, projectName);
            sentenceQuerySchooling.executeUpdate();
        }catch(SQLException sqlException){
            Logger.getLogger(Project.class.getName()).log(Level.SEVERE, null, sqlException);
        }finally{
            CONNECTION.closeConnection();
        }    
    }
}
