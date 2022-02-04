/*
 * @author Josué 
 * @versión v1.0
 * Last modification date: 17-06-2021
 */

package sgp.ca.businesslogic;

import sgp.ca.domain.Integrant;
import sgp.ca.domain.Member;

public interface IIntegrantDAO{
    
    public Integrant getIntegrantTocken(Integrant usuario);
    public Integrant getIntegrantToken(String email, String password);
    public Member searchMemberByEmailUv(String emailUv);
    
}
