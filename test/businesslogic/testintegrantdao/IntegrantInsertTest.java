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

public class IntegrantInsertTest{
    
    private final IntegrantDAO INTEGRANT_DAO = new IntegrantDAO();
    private final IntegrantInitializer INITIALIZER = new IntegrantInitializer();
    
    @Test
    public void correctIntegrantInsertion(){
        Integrant integrant = new Integrant(
            "OGA8903456J", "Jorge Octavio Ocharán Hernpandez", "ocharan@uv.mx", "ocharan", "Activo", "OOOH890624HVZNRN09", 
            "Integrante", "Mexicano", "2012-08-12", "Licenciatura en Ingeniería de Software", 41306, "2281394721", "UV-CA-127",
            "PTC", "angelsg89@hotmail.com", "2288146210", "141912288421700"
        );     
        
        integrant.addSchooling(new Schooling(
                "Licenciatura", "Lic. en Ingeniería en Tecnologías Estratégicas de la Información",
                "2003-06-08", "Universidad Anáhuac de Xalapa",  "Veracruz",  "08759567", 
                "Computación", "Ingeniería"
        ));

        integrant.addSchooling(new Schooling(
            "Maestría", "Lic. en Ingeniería en Tecnologías Estratégicas de la Información",
            "2003-06-08", "Universidad Anáhuac de Xalapa", "Veracruz", "08759566", 
            "Computación", "Ingeniería"
        ));
        
        INTEGRANT_DAO.addMember(integrant);
        Integrant integrantRetrieved = (Integrant) INTEGRANT_DAO.getMemberByUVmail("ocharan@uv.mx");
        INITIALIZER.cleanIntegrantTest(integrantRetrieved.getRfc());
        Assert.assertEquals(integrant.getEmailUV(), integrantRetrieved.getEmailUV());
    }
    
    @Test
    public void incorrectDuplicatedSchoolingIntegrantInsertion(){
        INITIALIZER.prepareRequestTestCase();
        Integrant integrant = new Integrant(
            "COLABORADORTEST", "Karen Verdín Cortés", "karen@uv.mx", "karen", "Activo", "KDV890624HVZNRN09", "Integrante", "Mexicana", 
            "2012-08-12", "Licenciatura en Ingeniería de Software", 41306, "2281394721", "UV-CA-127",
            "PTC", "karen@hotmail.com", "2288146210", "141912288421700"
        );     
        
        integrant.addSchooling(new Schooling(
            "Maestría", "Lic. en Ingeniería en Tecnologías Estratégicas de la Información", "2003-06-08",
            "Universidad Anáhuac de Xalapa", "Veracruz", "08759566", "Computación", "Ingeniería"
        ));
        
        INTEGRANT_DAO.addMember(integrant);
        Integrant integrantRetireved = (Integrant) INTEGRANT_DAO.getMemberByUVmail("karen@uv.mx");
        INITIALIZER.cleanIntegrantTest("INTEGRANTETEST");
        Assert.assertNull(integrantRetireved.getBodyAcademyKey());
    }
    
}
