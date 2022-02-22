/*
 * @author Josué 
 * @versión v1.0
 * Last modification date: 17-06-2021
 */

package businesslogic.testgeneralresumedao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import sgp.ca.businesslogic.GeneralResumeDAO;
import sgp.ca.domain.GeneralResume;
import utils.ConnectionDatabase;


public class GeneralResumeInitializer {
    
    private final ConnectionDatabase CONNECTION = new ConnectionDatabase();
    private final GeneralResumeDAO GENERAL_RESUME_DAO = new GeneralResumeDAO();
    
    public void prepareGeneralResumeTestCase(){
        if(GENERAL_RESUME_DAO.getGeneralResumeByKey("UV-CA-777").getBodyAcademyName() == null){
            GeneralResumeDAO generalResumeDAO = new GeneralResumeDAO();
            GeneralResume generalResume = new GeneralResume(
                "UV-CA-777", "Física", "Ingenierías", "Facultad de Física", "Consolidado", 
                "El Cuerpo Académico se encuentra consolidado y es líder en investigación cuántica.", 
                "Generar conocimiento práctico y teórico", "Desarrollar métodos, técnicas y herramientas para la investigación aritmética.", 
                "2012-12-25", "2019-01-11"
            );
            generalResumeDAO.addGeneralResume(generalResume);
        }
    }
    
    public void cleanGeneralResumeInsertionTestCase(String bodyAcademyKey){
        Connection connection = CONNECTION.getConnectionDatabase();
        try{
            PreparedStatement sentenceQuery = connection.prepareStatement(
                "DELETE FROM GeneralResume WHERE bodyAcademyKey = ?;"
            );
            sentenceQuery.setString(1, bodyAcademyKey);
            sentenceQuery.executeUpdate();
        }catch(SQLException sqlException){
            Logger.getLogger(GeneralResume.class.getName()).log(Level.SEVERE, null, sqlException);
        }finally{
            CONNECTION.closeConnection();
        }   
    }
    
}
