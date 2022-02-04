/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businesslogic.testprototypedao;

import org.junit.Assert;
import org.junit.Test;
import sgp.ca.businesslogic.CollaboratorDAO;
import sgp.ca.businesslogic.EvidenceDAO;
import sgp.ca.businesslogic.IntegrantDAO;
import sgp.ca.businesslogic.PrototypeDAO;
import sgp.ca.domain.Prototype;

public class PrototypeUpdateTest {

    private EvidenceDAO PROTOTYPE_DAO = new PrototypeDAO();
    private IntegrantDAO INTEGRANT_DAO = new IntegrantDAO();
    private CollaboratorDAO COLLABORATOR_DAO = new CollaboratorDAO();
    
    @Test
    public void testCorrectUdatePrototype(){
        Prototype oldPrototype = (Prototype) PROTOTYPE_DAO.getEvidenceByUrl("prototipoPrueba.pdf");
        Prototype prototypo = new Prototype(
            "newPrototypeUpdated.pdf", "ProyectoPrueba", 
            "prorotipoInsertado", "Mexico", "2020-04-12", true, "2010-01-10",
            "Jorge Octavio Ocharan", "Licenciatura", "UV-CA-127", "Nueva Caracteristica"
        );
        PROTOTYPE_DAO.updateEvidence(prototypo, "prototipoPrueba.pdf");
        Prototype retrievedPrototype = (Prototype) PROTOTYPE_DAO.getEvidenceByUrl("newPrototypeUpdated.pdf");
        PROTOTYPE_DAO.deleteEvidenceByUrl("newPrototypeUpdated.pdf");
        Assert.assertNotEquals(retrievedPrototype.getFeatures(), oldPrototype.getFeatures());
    }
    
    @Test
    public void testIncorrectUdatePrototype(){
        Prototype oldPrototype = (Prototype) PROTOTYPE_DAO.getEvidenceByUrl("prototipoPrueba.pdf");
        Prototype prototypo = new Prototype(
            "newPrototypeUpdated.pdf", "ProyectoPrueba", 
            "prorotipoInsertado", "Mexico", "2020-04-12", true, "201010-01-10",
            "Jorge Octavio Ocharan", "Licenciatura", "UV-CA-127", "Nueva Caracteristica"
        );
        PROTOTYPE_DAO.updateEvidence(prototypo, "prototipoPrueba.pdf");
        Prototype retrievedPrototype = (Prototype) PROTOTYPE_DAO.getEvidenceByUrl("newPrototypeUpdated.pdf");
        Assert.assertNull(retrievedPrototype.getFeatures());
    }
    
}
