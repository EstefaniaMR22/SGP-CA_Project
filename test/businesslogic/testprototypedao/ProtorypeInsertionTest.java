/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businesslogic.testprototypedao;


import sgp.ca.businesslogic.EvidenceDAO;
import sgp.ca.businesslogic.PrototypeDAO;

import org.junit.Assert;
import org.junit.Test;
import sgp.ca.businesslogic.CollaboratorDAO;
import sgp.ca.businesslogic.IntegrantDAO;
import sgp.ca.domain.Collaborator;
import sgp.ca.domain.Integrant;
import sgp.ca.domain.Prototype;

public class ProtorypeInsertionTest {
    
    private final EvidenceDAO PROTOTYPE_DAO = new PrototypeDAO();
    private final IntegrantDAO INTEGRANT_DAO = new IntegrantDAO();
    private final CollaboratorDAO COLLABORATOR_DAO = new CollaboratorDAO();
    
    @Test
    public void testCorrectInsertionPrototype(){
        Prototype prototypo = new Prototype(
            "testPrototypoInsertion.pdf", "ProyectoPrueba", 
            "prorotipoInsertado", "Mexico", "2020-04-12", true, "2010-01-10",
            "Jorge Octavio Ocharan", "Licenciatura", "UV-CA-127", "Ninguna Característica"
        );
        prototypo.getIntegrants().add((Integrant) INTEGRANT_DAO.getMemberByUVmail("integrantTest@uv.mx"));
        prototypo.getCollaborators().add((Collaborator) COLLABORATOR_DAO.getMemberByUVmail("prueba@uv.mx"));
        prototypo.getStudents().add("Bere Martínez");
        PROTOTYPE_DAO.addNewEvidence(prototypo);
        Prototype retrievedPrototype = (Prototype) PROTOTYPE_DAO.getEvidenceByUrl("testPrototypoInsertion.pdf");
        PROTOTYPE_DAO.deleteEvidenceByUrl("testPrototypoInsertion.pdf");
        Assert.assertEquals("prorotipoInsertado", retrievedPrototype.getEvidenceTitle());
    }
    
    @Test
    public void testIncorrectInsertionPrototype(){
        Prototype prototypo = new Prototype(
            "testPrototypoInsertion.pdf", "ProyectoPrueba", 
            "prorotipoInsertado", "Mexico", "2020-04-12", true, "201012-01-10",
            "Jorge Octavio Ocharan", "Licenciatura", "UV-CA-127", "Ninguna Característica"
        );
        prototypo.getIntegrants().add((Integrant) INTEGRANT_DAO.getMemberByUVmail("integrantTest@uv.mx"));
        prototypo.getCollaborators().add((Collaborator) COLLABORATOR_DAO.getMemberByUVmail("prueba@uv.mx"));
        prototypo.getStudents().add("Bere Martínez");
        PROTOTYPE_DAO.addNewEvidence(prototypo);
        Prototype retrievedPrototype = (Prototype) PROTOTYPE_DAO.getEvidenceByUrl("testPrototypoInsertion.pdf");
        Assert.assertNull(retrievedPrototype.getEvidenceTitle());
    }
    
}
