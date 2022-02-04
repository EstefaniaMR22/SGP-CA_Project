/*
 * @author Josué 
 * @versión v1.0
 * Last modification date: 17-06-2021
 */

package businesslogic.testintegrantdao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import sgp.ca.businesslogic.IntegrantDAO;
import sgp.ca.dataaccess.ConnectionDatabase;
import sgp.ca.domain.Integrant;
import sgp.ca.domain.Schooling;


public class IntegrantInitializer {
    
    private final IntegrantDAO INTEGRANT_DAO = new IntegrantDAO();
    private final ConnectionDatabase CONNECTION = new ConnectionDatabase();
    
    public void prepareRequestTestCase(){
        if(INTEGRANT_DAO.getMemberByUVmail("integrantTest@uv.mx").getRfc() == null){
            Integrant integrant = new Integrant(
                "INTEGRANTETEST", "Angel Juan Sánchez García", "integrantTest@uv.mx",
                "password", "Activo", "SAGA890624HVZNRN09", "Integrante","Mexicano", "2012-08-12", 
                "Licenciatura en Ingeniería de Software", 41306, "2281394721",
                "UV-CA-127", "PTC", "angelsg89@hotmail.com", "2288146210", 
                "141912288421700"
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
        }
    }
    
    public void prepareUpdateTestCase(){
        this.prepareRequestTestCase();
        Integrant integrantRetrieved = (Integrant) INTEGRANT_DAO.getMemberByUVmail("integrantTest@uv.mx");
        integrantRetrieved.setRfc("SAGA8906245M7");
        INTEGRANT_DAO.updateMember(integrantRetrieved, INTEGRANT_DAO.getMemberByUVmail("integrantTest@uv.mx").getEmailUV());
    }
    
    public void cleanIntegrantTest(String rfcIntegrant){
        Connection connection = CONNECTION.getConnectionDatabase();
        try{
            PreparedStatement sentenceQuerySchooling = connection.prepareStatement(
                "DELETE FROM Schooling WHERE rfc = ?;"
            );
            sentenceQuerySchooling.setString(1, rfcIntegrant);
            sentenceQuerySchooling.executeUpdate();
            PreparedStatement sentenceQuery = connection.prepareStatement(
                "DELETE FROM Integrant WHERE rfc = ?;"
            );
            sentenceQuery.setString(1, rfcIntegrant);
            sentenceQuery.executeUpdate();
        }catch(SQLException sqlException){
            Logger.getLogger(Integrant.class.getName()).log(Level.SEVERE, null, sqlException);
        }finally{
            CONNECTION.closeConnection();
        }    
    }
    
}
