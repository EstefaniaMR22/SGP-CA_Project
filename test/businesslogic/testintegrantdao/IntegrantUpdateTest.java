/*
 * @author Josué 
 * @versión v1.0
 * Last modification date: 17-06-2021
 */

package businesslogic.testintegrantdao;

import org.junit.Assert;
import org.junit.Test;
import sgp.ca.businesslogic.IntegrantDAO;
import sgp.ca.domain.Integrant;
import sgp.ca.domain.Schooling;


public class IntegrantUpdateTest{
    
    private final IntegrantInitializer INITIALIZER = new IntegrantInitializer();
    private final IntegrantDAO INTEGRANT_DAO = new IntegrantDAO();
    
    @Test
    public void testCorrecIntegrantUpdateWithAddSchooling(){
        INITIALIZER.prepareUpdateTestCase();
        Integrant oldIntegrant =  (Integrant) INTEGRANT_DAO.getMemberByUVmail("integrantTest@uv.mx");
        Integrant newIntegrant = new Integrant(
            "SAA8906245M8", "Angel Juan Sánchez García", "angelsanchez@uv.mx", "angel", "Activo", "SAGA890624HVZNRN09", 
            "Integrante", "Mexicano", "2012-08-12", "Licenciatura en Ingeniería de Software", 41306, "2281394721",
            "UV-CA-127", "PTC", "angelsg89@hotmail.com", "2288146210", "141912288421700"
        );     
        
        newIntegrant.addSchooling(new Schooling(
            "Licenciatura", "Lic. en Ingeniería en Tecnologías Estratégicas de la Información", "2003-06-08",
            "Universidad Anáhuac de Xalapa", "Veracruz", "08759567", "Computación", "Ingeniería"
        ));
        
        newIntegrant.addSchooling(new Schooling(
            "Maestría", "Lic. en Ingeniería en Tecnologías Estratégicas de la Información", "2003-06-08",
            "Universidad Anáhuac de Xalapa", "Veracruz", "08759566", "Inteligencia artificial", "Ingeniería"
        ));
        
        newIntegrant.addSchooling(new Schooling(
            "Doctorado", "Lic. en Ingeniería en Tecnologías Estratégicas de la Información", "2003-06-08", 
            "Universidad veracruzana", "Veracruz", "02450244", "Ingeniería de software", "Ingeniería"
        ));
        
        INTEGRANT_DAO.updateMember(newIntegrant, oldIntegrant.getEmailUV());
        Integrant newIntegrantRetrieved = (Integrant) INTEGRANT_DAO.getMemberByUVmail("angelsanchez@uv.mx");
        INITIALIZER.cleanIntegrantTest(newIntegrantRetrieved.getRfc());
        Assert.assertNotEquals(oldIntegrant, newIntegrantRetrieved);
    }
    
    @Test
    public void testIncorrectIntegrantDataUpdateNotAdded(){
        INITIALIZER.prepareUpdateTestCase();
        Integrant oldIntegrant =  (Integrant) INTEGRANT_DAO.getMemberByUVmail("integrantTest@uv.mx");
        
        Integrant newIntegrant = new Integrant(
            "AAS285R5EF", "Angel Juan Sánchez García", "integrantTest@uv.mx", "angel", "Activo", "SAGA890624HVZNRN09", 
            "Integrante", "Mexicano", "2012-08-12", "Licenciatura en Ingeniería de Software", 41306, "2281394721",
            "UV-CA-127", "PTC", "angelsg89@hotmail.com", "2288146210", "141912288421700"
        );     
        
        newIntegrant.addSchooling(new Schooling(
            "Licenciatura", "Lic. en Ingeniería en Tecnologías Estratégicas de la Información",
            "200003-06-08", "Universidad Anáhuac de Xalapa",  "Veracruz",  "08759567", 
            "Computación", "Ingeniería"
        ));
        
        INTEGRANT_DAO.updateMember(newIntegrant, oldIntegrant.getEmailUV());
        Integrant newIntegrantRetrieved = (Integrant) INTEGRANT_DAO.getMemberByUVmail("integrantTest@uv.mx");
        INITIALIZER.cleanIntegrantTest("SAGA8906245M7");
        Assert.assertEquals(oldIntegrant.getRfc(), newIntegrantRetrieved.getRfc());
    }
    
    @Test
    public void testCorrecIntegrantUpdateWithoutSchooling(){
        INITIALIZER.prepareUpdateTestCase();
        Integrant oldIntegrant =  (Integrant) INTEGRANT_DAO.getMemberByUVmail("integrantTest@uv.mx");
        
        Integrant newIntegrant = new Integrant(
            "JAJCUYEDF2", "Angel Juan Sánchez García", "angelsanchez@uv.mx", "angel", "Activo", "SAGA890624HVZNRN09", 
            "Integrante", "Mexicano", "2012-08-12", "Licenciatura en Ingeniería de Software", 41306, "2281394721",
            "UV-CA-127", "PTC", "angelsg89@hotmail.com", "2288146210", "141912288421700"
        );     
        
        INTEGRANT_DAO.updateMember(newIntegrant, oldIntegrant.getEmailUV());
        Integrant newIntegrantRetrieved = (Integrant) INTEGRANT_DAO.getMemberByUVmail("angelsanchez@uv.mx");
        INITIALIZER.cleanIntegrantTest(newIntegrantRetrieved.getRfc());
        Assert.assertNotEquals(oldIntegrant, newIntegrantRetrieved);
    }
    
    @Test
    public void correctUnsubscribeIntegrant(){
        INITIALIZER.prepareRequestTestCase();
        Integrant oldIntegrant = (Integrant) INTEGRANT_DAO.getMemberByUVmail("integrantTest@uv.mx");
        INTEGRANT_DAO.unsubscribeMemberByEmailUV(oldIntegrant.getEmailUV());
        Integrant integrantRetrieved = (Integrant) INTEGRANT_DAO.getMemberByUVmail("integrantTest@uv.mx");
        INITIALIZER.cleanIntegrantTest(integrantRetrieved.getRfc());
        Assert.assertNotEquals(oldIntegrant.getParticipationStatus(), integrantRetrieved.getParticipationStatus());
    }
    
    @Test
    public void correctSubscribeIntegrant(){
        INITIALIZER.prepareRequestTestCase();
        Integrant oldIntegrant = (Integrant) INTEGRANT_DAO.getMemberByUVmail("integrantTest@uv.mx");
        INTEGRANT_DAO.unsubscribeMemberByEmailUV(oldIntegrant.getEmailUV());
        oldIntegrant = (Integrant) INTEGRANT_DAO.getMemberByUVmail("integrantTest@uv.mx");
        INTEGRANT_DAO.subscribeMemberByEmailUV(oldIntegrant.getEmailUV());
        Integrant integrantRetrieved = (Integrant) INTEGRANT_DAO.getMemberByUVmail("integrantTest@uv.mx");
        INITIALIZER.cleanIntegrantTest(integrantRetrieved.getRfc());
        String stateExpected = "Activo";
        Assert.assertEquals(stateExpected, integrantRetrieved.getParticipationStatus());
    }
    
}
