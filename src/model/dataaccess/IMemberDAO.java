/*
 * @author Josué 
 * @versión v1.0
 * Last modification date: 17-06-2021
 */

package model.dataaccess;

import java.util.List;
import model.domain.Member;

public interface IMemberDAO{
    
    public Member getMemberByUVmail(String emailUV);
    public boolean addMember(Member newMember);
    public boolean updateMember(Member member, String oldEmailUv);
    public boolean unsubscribeMemberByEmailUV(String emailUV);
    public boolean subscribeMemberByEmailUV(String emailUV);
    public List<Member> getMembers(String bodyAcademyKey);
    
}
