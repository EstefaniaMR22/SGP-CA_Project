/*
 * @author Josué 
 * @versión v1.0
 * Last modification date: 17-06-2021
 */

package businesslogic.testcollaboratordao;

import org.junit.Assert;
import org.junit.Test;
import sgp.ca.businesslogic.CollaboratorDAO;
import sgp.ca.domain.Collaborator;

public class CollaboratorUpdateTest{
    
    private final CollaboratorDAO COLLABORATOR_DAO = new CollaboratorDAO();
    private final CollaboratorInitializer INITIALIZER = new CollaboratorInitializer();
    
    @Test
    public void correctUpdatedCollaborator(){
        INITIALIZER.prepareRequestTestCase();
        Collaborator oldCollaborator = (Collaborator) COLLABORATOR_DAO.getMemberByUVmail("prueba@uv.mx");
        Collaborator newCollaborador = new Collaborator(
            "GRTCEVSFTJRB", "María de los Ángeles Arenas Valdes", "arenas@uv.mx", "Activo",
            "SAGA890624HVZNRN09", "Colaborador", "Mexicano", "2012-08-12", "Licenciatura en Ingeniería de Software", 
            41306, "2281394721", "UV-CA-127", "Ingeniería y Tecnologías de software", 
            "Informática", "Maestría"
        );
        COLLABORATOR_DAO.updateMember(newCollaborador, oldCollaborator.getRfc());
        Collaborator collaboratorRetrieved = (Collaborator) COLLABORATOR_DAO.getMemberByUVmail("arenas@uv.mx");
        INITIALIZER.cleanCollaboratorTest(collaboratorRetrieved.getRfc());
        Assert.assertNotEquals(oldCollaborator, collaboratorRetrieved);
    }
    
    @Test
    public void incorrectUpdatedCollaboratorNotRegistered(){
        Collaborator oldCollaborator = (Collaborator) COLLABORATOR_DAO.getMemberByUVmail("arenas@uv.mx");
        Collaborator newCollaborador = new Collaborator(
            "EFFYEVZPF", "Gerardo Contreras Vega", "contreras@uv.mx", "Activo",
            "SAGA890624HVZNRN09", "Colaborador", "Mexicano", "2012-08-12", "Licenciatura en Ingeniería de Software", 
            41306, "2281394721", "UVV-CA-127", "Ingeniería y Tecnologías de software", 
            "Informática", "Maestría"
        );
        COLLABORATOR_DAO.updateMember(newCollaborador, oldCollaborator.getRfc());
        Assert.assertNull(COLLABORATOR_DAO.getMemberByUVmail("contreras@uv.mx").getRfc());
    }
    
    @Test
    public void correctUnsubscribeCollaborator(){
        INITIALIZER.prepareRequestTestCase();
        Collaborator oldCollaborator = (Collaborator) COLLABORATOR_DAO.getMemberByUVmail("prueba@uv.mx");
        COLLABORATOR_DAO.unsubscribeMemberByEmailUV(oldCollaborator.getEmailUV());
        Collaborator collaboratorRetrieved = (Collaborator) COLLABORATOR_DAO.getMemberByUVmail("prueba@uv.mx");
        INITIALIZER.cleanCollaboratorTest(collaboratorRetrieved.getRfc());
        Assert.assertNotEquals(oldCollaborator.getParticipationStatus(), collaboratorRetrieved.getParticipationStatus());
    }
    
    @Test
    public void correctSubscribeCollaborator(){
        INITIALIZER.prepareRequestTestCase();
        Collaborator oldCollaborator = (Collaborator) COLLABORATOR_DAO.getMemberByUVmail("prueba@uv.mx");
        COLLABORATOR_DAO.unsubscribeMemberByEmailUV(oldCollaborator.getEmailUV());
        oldCollaborator = (Collaborator) COLLABORATOR_DAO.getMemberByUVmail("prueba@uv.mx");
        COLLABORATOR_DAO.subscribeMemberByEmailUV(oldCollaborator.getEmailUV());
        Collaborator collaboratorRetrieved = (Collaborator) COLLABORATOR_DAO.getMemberByUVmail("prueba@uv.mx");
        INITIALIZER.cleanCollaboratorTest(collaboratorRetrieved.getRfc());
        String stateExpected = "Activo";
        Assert.assertEquals(stateExpected, collaboratorRetrieved.getParticipationStatus());
    }
    
}
