/*
 * @author Josué 
 * @versión v1.0
 * Last modification date: 17-06-2021
 */

package businesslogic.testgeneralresumedao;

import org.junit.Assert;
import org.junit.Test;
import sgp.ca.businesslogic.GeneralResumeDAO;
import sgp.ca.domain.GeneralResume;

public class GeneralResumeInsertTest{
    
    GeneralResumeInitializer INITIALIZER = new GeneralResumeInitializer();
    
    @Test
    public void testCorrectGeneralResumeInsertion(){
        GeneralResumeDAO generalResumeDAO = new GeneralResumeDAO();
        GeneralResume generalResume = new GeneralResume(
            "UV-CA-777", "Física", "Ingenierías", "Facultad de Física", "Consolidado", 
            "El Cuerpo Académico se encuentra consolidado y es líder en investigación cuántica.", 
            "Generar conocimiento práctico y teórico", 
            "Desarrollar métodos, técnicas y herramientas para la investigación aritmética.", 
            "2012-12-25", "2019-01-11"
        );
        generalResumeDAO.addGeneralResume(generalResume);
        GeneralResume generalResumeRetrieved = generalResumeDAO.getGeneralResumeByKey(generalResume.getBodyAcademyKey());
        INITIALIZER.cleanGeneralResumeInsertionTestCase("UV-CA-777");
        Assert.assertEquals(generalResume, generalResumeRetrieved);
    }
    
    @Test
    public void testIncorrectGeneralResumeInsertionDuplicated(){
        GeneralResumeDAO generalResumeDAO = new GeneralResumeDAO();
        GeneralResume oldGeneralResume = generalResumeDAO.getGeneralResumeByKey("UV-CA-127");
        GeneralResume generalResume = new GeneralResume(
            "UV-CA-127", "Física", "Ingenierías", "Facultad de Física", "Consolidado", 
            "El Cuerpo Académico se encuentra consolidado y es líder en investigación cuántica.", 
            "Generar conocimiento práctico y teórico", 
            "Desarrollar métodos, técnicas y herramientas para la investigación aritmética.", 
            "2012-12-25", "2019-01-11"
        );
        generalResumeDAO.addGeneralResume(generalResume);
        GeneralResume generalResumeRetrieved = generalResumeDAO.getGeneralResumeByKey("UV-CA-127");
        Assert.assertEquals(oldGeneralResume, generalResumeRetrieved);
    }

}
    