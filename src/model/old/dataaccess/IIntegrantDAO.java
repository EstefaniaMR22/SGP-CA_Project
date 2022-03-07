/*
 * @author Josué 
 * @versión v1.0
 * Last modification date: 17-06-2021
 */

package model.old.dataaccess;

import model.old.domain.Integrant;
import model.old.domain.Member;

public interface IIntegrantDAO{
    
    public Integrant getIntegrantTocken(Integrant usuario);
    public Integrant getIntegrantToken(String email, String password);
    public Member searchMemberByEmailUv(String emailUv);
    
}
