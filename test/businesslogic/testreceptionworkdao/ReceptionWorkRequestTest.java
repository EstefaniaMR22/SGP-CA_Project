/*
* @author Johann
* @versión v1.0
* Last modification date: 17-06-2021
*/
package businesslogic.testreceptionworkdao;

import org.junit.Assert;
import org.junit.Test;
import sgp.ca.businesslogic.ReceptionWorkDAO;
import sgp.ca.domain.ReceptionWork;


public class ReceptionWorkRequestTest {
    
    public final ReceptionWorkInitializer INITIALIZER = new ReceptionWorkInitializer();
    public final ReceptionWorkDAO RECEPTIONWORK_DAO = new ReceptionWorkDAO();
    
    @Test
    public void testGetExistReceptionWorkByUrlFile(){
        INITIALIZER.prepareReceptionWorkInsertionForTest();
        ReceptionWork receptionWorkRetrieved = RECEPTIONWORK_DAO.getEvidenceByUrl("PRUEBAS");
        String supposedReceptionWork = "PRUEBAS";
        INITIALIZER.cleanInsertionsReceptionWorkForTest("PRUEBAS");
        Assert.assertEquals(receptionWorkRetrieved.getUrlFile(), supposedReceptionWork);
    }
    
    @Test
    public void testGetNotExistReceptionWorKByUrlFile(){
        ReceptionWork receptionWorkRetrieved = RECEPTIONWORK_DAO.getEvidenceByUrl("trabajo.pdf");
        Assert.assertNull(receptionWorkRetrieved.getUrlFile());
    }
}
