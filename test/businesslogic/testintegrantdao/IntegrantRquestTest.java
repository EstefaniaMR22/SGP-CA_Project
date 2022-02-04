/**
* @author Josué Alarcón  
* Last modification date format: 
*/

package businesslogic.testintegrantdao;

import org.junit.Test;
import org.junit.Assert;
import sgp.ca.businesslogic.IntegrantDAO;
import sgp.ca.domain.Integrant;

public class IntegrantRquestTest{
    
    private final IntegrantInitializer INITIALIZER = new IntegrantInitializer();
    private final IntegrantDAO INTEGRANT_DAO = new IntegrantDAO();
    
    @Test
    public void testGetExistIntegrantByUVmail(){
        INITIALIZER.prepareRequestTestCase();
        Integrant integrant = (Integrant) INTEGRANT_DAO.getMemberByUVmail("integrantTest@uv.mx");
        String rfcExpected = "INTEGRANTETEST";
        String rfcRetrived = integrant.getRfc();
        INITIALIZER.cleanIntegrantTest(rfcRetrived);
        Assert.assertEquals("Get exist integrant from database", rfcExpected, rfcRetrived);
    }
    
    @Test
    public void testGetIntegrantNotRegisteredByUVmail(){
        Integrant integrant = (Integrant) INTEGRANT_DAO.getMemberByUVmail("joijeoijd");
        String rfcRetrived = integrant.getRfc();
        Assert.assertNull(rfcRetrived);
    }
    
    @Test 
    public void testGetExistIntegrantStudies(){
        INITIALIZER.prepareRequestTestCase();
        Integrant integrant = (Integrant) INTEGRANT_DAO.getMemberByUVmail("integrantTest@uv.mx");
        int StudiesNumberExpected = 2;
        INITIALIZER.cleanIntegrantTest(integrant.getRfc());
        Assert.assertEquals("Get integrant studies, at least 1", StudiesNumberExpected, integrant.getSchooling().size());
    }
    
}
