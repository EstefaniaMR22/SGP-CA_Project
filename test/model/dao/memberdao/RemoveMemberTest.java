package model.dao.memberdao;

import model.dao.MemberDAO;
import org.junit.Assert;
import org.junit.Test;

import java.sql.SQLException;

public class RemoveMemberTest {
    @Test
    public void removeMemberByIdTest() throws SQLException {
        String personalNumber = "9432";
        int idMember = new MemberDAO().getMemberIdByPersonalNumber(personalNumber);
        boolean isRemoved = new MemberDAO().removeMember(idMember);
        Assert.assertTrue(isRemoved);
    }

    @Test
    public void removeAnotherMemberByIdTest() throws SQLException {
        String personalNumber = "9433";
        int idMember = new MemberDAO().getMemberIdByPersonalNumber(personalNumber);
        boolean isRemoved = new MemberDAO().removeMember(idMember);
        Assert.assertTrue(isRemoved);
    }

    @Test
    public void removeMemberNoExisting() throws SQLException {
        int idMember = -1;
        boolean isRemoved = new MemberDAO().removeMember(idMember);
        Assert.assertFalse(isRemoved);
    }

}
