/**
 * @author Estefanía 
 * @versión v1.0
 * Last modification date: 17-06-2021
 */

package model.dataaccess;

import java.sql.Connection;
import java.util.List;
import model.domain.Agreement;
import model.domain.Meeting;

public interface IAgreementDAO{
    public void addAgreements(Connection connection, Meeting meeting);
    public List<Agreement> getAgreementListByMeeting(int meetingKey);
}
