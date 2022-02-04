/**
 * @author Estefanía 
 * @versión v1.0
 * Last modification date: 17-06-2021
 */

package sgp.ca.businesslogic;

import java.sql.Connection;
import java.util.List;
import sgp.ca.domain.Agreement;
import sgp.ca.domain.Meeting;

public interface IAgreementDAO{
    public void addAgreements(Connection connection, Meeting meeting);
    public List<Agreement> getAgreementListByMeeting(int meetingKey);
}
