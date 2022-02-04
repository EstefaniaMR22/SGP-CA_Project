/**
 * @author Estefanía 
 * @versión v1.0
 * Last modification date: 17-06-2021
 */

package sgp.ca.businesslogic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import sgp.ca.domain.Agreement;
import sgp.ca.domain.Meeting;
import sgp.ca.dataaccess.ConnectionDatabase;

public class AgreementDAO implements IAgreementDAO{
    private final ConnectionDatabase CONNECTION = new ConnectionDatabase();
    
    @Override
    public void addAgreements(Connection connection, Meeting meeting) {
        meeting.getAgreements().forEach( agreement -> {
            try{
                PreparedStatement sentenceQuery = connection.prepareStatement(
                    "INSERT INTO Agreement (meetingKey, descriptionAgreement, "
                    + "responsibleAgreement, deliveryDate) "
                    + "VALUES(?,?,?,?);"
                );
                sentenceQuery.setInt(1, meeting.getMeetingKey());
                sentenceQuery.setString(2, agreement.getDescriptionAgreement());
                sentenceQuery.setString(3, agreement.getResponsibleAgreement());
                sentenceQuery.setString(4, agreement.getDeliveryDate());
                sentenceQuery.executeUpdate();
            }catch(SQLException sqlException){
                try{
                    connection.rollback();
                    CONNECTION.closeConnection();
                    Logger.getLogger(Meeting.class.getName()).log(Level.SEVERE, null, sqlException);
                }catch(SQLException ex){
                    Logger.getLogger(MeetingDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    @Override
    public List<Agreement> getAgreementListByMeeting(int meetingKey){
        List<Agreement> agreementsList = new ArrayList<>();
        try{
            PreparedStatement sentenceQuery = CONNECTION.getConnectionDatabase().prepareStatement(
                "SELECT * FROM Agreement WHERE meetingKey = ?;"
            );
            sentenceQuery.setInt(1, meetingKey);
            ResultSet queryResult = sentenceQuery.executeQuery();
            while(queryResult.next()){
                Agreement agreement = new Agreement(
                    queryResult.getInt("agreementNumber"),
                    queryResult.getString("descriptionAgreement"),
                    queryResult.getString("responsibleAgreement"),
                    queryResult.getDate("deliveryDate").toString()
                );
                agreementsList.add(agreement);
            }
        }catch(SQLException sqlException){
            Logger.getLogger(AgreementDAO.class.getName()).log(Level.SEVERE, null, sqlException);
        }finally{
            return agreementsList;
        }
    }
}
