/*
 * @author Josué 
 * @versión v1.0
 * Last modification date: 17-06-2021
 */

package businesslogic.testgeneralresumedao;

import org.junit.Test;
import org.junit.Assert;
import sgp.ca.businesslogic.GeneralResumeDAO;
import sgp.ca.domain.GeneralResume;

public class GeneralResumeRequestTest {
    
    @Test
    public void testGetGeneralResumeByKeyNotNull(){
        GeneralResumeDAO generalResumeDAO = new GeneralResumeDAO();
        GeneralResume generalResume = generalResumeDAO.getGeneralResumeByKey("UV-CA-127");
        String bodyKeyExpected = "Ingeniería y tecnologías de Software";
        String bodyKeyRetrived = generalResume.getBodyAcademyName();
        Assert.assertEquals("Get exist body academy from database", bodyKeyExpected, bodyKeyRetrived);
    }
    
    @Test
    public void testGetGeneralResumeNotRegistered(){
        GeneralResumeDAO generalResumeDAO = new GeneralResumeDAO();
        GeneralResume generalResume = generalResumeDAO.getGeneralResumeByKey("UV-CA-3005247");
        String bodyKeyRetrived = generalResume.getBodyAcademyName();
        Assert.assertNull(bodyKeyRetrived);
    }
    
}
