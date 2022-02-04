/*
 * @author Josué 
 * @versión v1.0
 * Last modification date: 17-06-2021
 */

package businesslogic.testgeneralresumedao;

import org.junit.Test;
import sgp.ca.businesslogic.GeneralResumeDAO;
import sgp.ca.domain.GeneralResume;
import org.junit.Assert;

public class GeneralResumeUpdateTest{
    
    private final GeneralResumeDAO GENERAL_RESUME_DAO = new GeneralResumeDAO();
    private final GeneralResumeInitializer INITIALIZER = new GeneralResumeInitializer();
    
    @Test
    public void testCorrectUpdateGeneralResume(){
        INITIALIZER.prepareGeneralResumeTestCase();
        GeneralResume oldGeneralResume = GENERAL_RESUME_DAO.getGeneralResumeByKey("UV-CA-777");
        GeneralResume generalResume = new GeneralResume(
            "UV-CA-888", "Estadística", "Ingenierías", "Facultad de Física", "Consolidado", 
            "El Cuerpo Académico se encuentra consolidado y es líder en investigación cuántica.", 
            "Generar conocimiento práctico y teórico", "Desarrollar métodos, técnicas y herramientas para la investigación aritmética.", 
            "2012-12-25", "2019-01-11"
        );
        GENERAL_RESUME_DAO.updateGeneralResume(generalResume, "UV-CA-777");
        GeneralResume generalResumeRetrieved = GENERAL_RESUME_DAO.getGeneralResumeByKey("UV-CA-888");
        INITIALIZER.cleanGeneralResumeInsertionTestCase("UV-CA-888");
        Assert.assertNotEquals(oldGeneralResume, generalResumeRetrieved);
    }
    
    @Test
    public void testIncorrectUpdateGeneralResumeWithInvalidField(){
        INITIALIZER.prepareGeneralResumeTestCase();
        GeneralResume generalResume = new GeneralResume(
            "UV-CA-888", "Estadística", "Ingenierías", "Facultad de Física", "Consolidado", 
            "El Cuerpo Académico se encuentra consolidado y es líder en investigación cuántica.", 
            "Generar conocimiento práctico y teórico", "Desarrollar métodos, técnicas y herramientas para la investigación aritmética.", 
            "2012-12-25", "2019-0111-11"
        );
        GENERAL_RESUME_DAO.updateGeneralResume(generalResume, "UV-CA-777");
        GeneralResume generalResumeRetrieved = GENERAL_RESUME_DAO.getGeneralResumeByKey("UV-CA-888");
        INITIALIZER.cleanGeneralResumeInsertionTestCase("UV-CA-777");
        Assert.assertNull(generalResumeRetrieved.getBodyAcademyName());
    }
    
}
