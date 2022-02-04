/*
* @author Johann
* @versión v1.0
* Last modification date: 17-06-2021
*/
package businesslogic.testreceptionworkdao;

import businesslogic.testcollaboratordao.CollaboratorInitializer;
import businesslogic.testintegrantdao.IntegrantInitializer;
import sgp.ca.businesslogic.CollaboratorDAO;
import sgp.ca.businesslogic.IntegrantDAO;
import sgp.ca.businesslogic.ReceptionWorkDAO;
import sgp.ca.domain.ReceptionWork;


public class ReceptionWorkInitializer {
    
    private final ReceptionWorkDAO ReceptionWorkDAO = new ReceptionWorkDAO();
    private final IntegrantDAO INTEGRANT_DAO = new IntegrantDAO();
    private final IntegrantInitializer INTEGRANT_INITIALIZER = new IntegrantInitializer();
    private final CollaboratorDAO COLLABORATOR_DAO = new CollaboratorDAO();
    private final CollaboratorInitializer COLLABORATOR_INITIALIZER = new CollaboratorInitializer();
 
    public void prepareReceptionWorkInsertionForTest(){
        if(ReceptionWorkDAO.getEvidenceByUrl("PRUEBAS") != null){
            ReceptionWork receptionWork = new ReceptionWork(
                "PRUEBAS", "Crecimiento de lenguajes de programación", true, "Trabajo recepcional",
                "Investigacion docente", "Jorge Octavio Ocharan", "2020-05-5", "Licenciatura",
                "2020-12-12", "Mexico",  "Descripción_prueba",
                "Activo", 6, 11, "Tesina"
            );
            receptionWork.getRequirements().add("Requisitos de prueba");
            receptionWork.getStudents().add("Josué Sangabriel Alarcón");
            ReceptionWorkDAO.addNewEvidence(receptionWork);
        }
    }
    
    public void cleanInsertionsReceptionWorkForTest(String receptionWorkUrl){
        ReceptionWorkDAO.deleteReceptionWork(receptionWorkUrl);
    }
}
