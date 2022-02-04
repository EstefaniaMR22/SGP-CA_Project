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

public class ReceptionWorkUpdateTest {
    
    public final ReceptionWorkInitializer INITIALIZER = new ReceptionWorkInitializer();
    public final ReceptionWorkDAO RECEPTIONWORK_DAO = new ReceptionWorkDAO();
    private ReceptionWork receptionWork;
    
    @Test
    public void testCorrectReceptionWorkUpdateNotChanges(){
        ReceptionWork oldReceptionWork =RECEPTIONWORK_DAO.getEvidenceByUrl("PRUEBAS");
        RECEPTIONWORK_DAO.updateEvidence(oldReceptionWork, oldReceptionWork.getUrlFile());
        ReceptionWork receptionWorkRetrieved = RECEPTIONWORK_DAO.getEvidenceByUrl("PRUEBAS");
        RECEPTIONWORK_DAO.deleteEvidenceByUrl("PRUEBAS.pdf");
        Assert.assertEquals(oldReceptionWork.getUrlFile(), receptionWorkRetrieved.getUrlFile());
    }
    
    @Test
    public void testCorrectUpdateReceptionWork(){
        ReceptionWork oldReceptionWork = RECEPTIONWORK_DAO.getEvidenceByUrl("PRUEBAS");
        receptionWork = new ReceptionWork(
            "PRUEBAS", "Crecimiento de lenguajes de programación", true, "Trabajo recepcional",
                "Investigacion docente", "Jorge Octavio Ocharan", "2020-05-5", "Licenciatura",
                "2020-12-12", "Mexico",  "Descripción_Cambio",
                "Activo", 6, 11, "Tesina"
        );
        RECEPTIONWORK_DAO.updateEvidence(receptionWork, "PRUEBAS");
        ReceptionWork receptionWorkRetrieved = RECEPTIONWORK_DAO.getEvidenceByUrl("PRUEBAS");
        RECEPTIONWORK_DAO.deleteEvidenceByUrl("PRUEBAS");
        Assert.assertNotEquals(oldReceptionWork, receptionWorkRetrieved);
    }
}