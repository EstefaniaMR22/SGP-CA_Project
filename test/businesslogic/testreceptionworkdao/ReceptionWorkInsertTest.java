/*
* @author Johann
* @versión v1.0
* Last modification date: 17-06-2021
*/
package businesslogic.testreceptionworkdao;

import org.junit.Assert;
import org.junit.Test;
import sgp.ca.businesslogic.ReceptionWorkDAO;
import sgp.ca.domain.Collaborator;
import sgp.ca.domain.Integrant;
import sgp.ca.domain.Lgac;
import sgp.ca.domain.ReceptionWork;

public class ReceptionWorkInsertTest {
    
    public final ReceptionWorkInitializer INITIALIZER = new ReceptionWorkInitializer();
    public final ReceptionWorkDAO RECEPTIONWORK_DAO = new ReceptionWorkDAO();
    private ReceptionWork receptionWork;
    
    @Test
    public void testCorrectReceptionWorkInsert(){
        receptionWork = new ReceptionWork(
                "PRUEBAS", "TITULO PRUEBA", true, "Trabajo recepcional",
                "Investigacion docente", "Jorge Octavio Ocharan", "2020-05-5", "Licenciatura",
                "2020-12-12", "Mexico",  "Descripción_prueba",
                "Activo", 6, 11, "Tesina"
            );
        receptionWork.getRequirements().add("Requisitos de prueba");
        receptionWork.getStudents().add("Estefania Martinez");
        receptionWork.getStudents().add("Johann Alexis");
        receptionWork.getStudents().add("Josue SanGabriel");
        RECEPTIONWORK_DAO.addReceptionWork(receptionWork);
        ReceptionWork receptionWorkRetrieved = RECEPTIONWORK_DAO.getEvidenceByUrl("PRUEBAS"); 
        INITIALIZER.cleanInsertionsReceptionWorkForTest("PRUEBAS");
        Assert.assertEquals(receptionWork.getUrlFile(), receptionWorkRetrieved.getUrlFile());
    }
         
    @Test
    public void testIncorrectReceptionWorkInsertionDuplicated(){
        INITIALIZER.prepareReceptionWorkInsertionForTest();
        String oldReceptionWorkUrlFile = "PRUEBAS";
        receptionWork = new ReceptionWork(
            "PRUEBAS", "TITULO PRUEBA", true, "Trabajo recepcional",
                "Investigacion docente", "Jorge Octavio Ocharan", "2020-05-5", "Licenciatura",
                "2020-12-12", "Mexico",  "Descripción_prueba",
                "Activo", 6, 11, "Tesina"
        );
        INITIALIZER.cleanInsertionsReceptionWorkForTest("PRUEBAS");
        RECEPTIONWORK_DAO.addNewEvidence(receptionWork);
        ReceptionWork receptionWorkRetrieved = RECEPTIONWORK_DAO.getEvidenceByUrl("PRUEBAS");
        INITIALIZER.cleanInsertionsReceptionWorkForTest("PRUEBAS");
        Assert.assertEquals(oldReceptionWorkUrlFile, receptionWorkRetrieved.getUrlFile());
    }
}
