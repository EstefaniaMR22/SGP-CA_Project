package model.dao.memberdao;

import model.dao.MiembroDAO;
import model.domain.Member;
import org.junit.Assert;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;

public class GetMembersTest {
    @Test
    public void getMembersTest() throws SQLException {
        List<Member> memberList = new MiembroDAO().getAllMembers();
        int expectedSize = 1003;
        int actual = memberList.size();
        Assert.assertEquals(expectedSize, actual);
    }

    @Test
    public void getMemberById() throws SQLException {
        int idMember = 1;
        Member member = new MiembroDAO().getMember(idMember);
        Assert.assertNotNull(member);
    }

    @Test
    public void getMemberIdByPersonalNumber() throws SQLException {
        String personalNumber = "ADMIN";
        int idMember = new MiembroDAO().getMemberIdByPersonalNumber(personalNumber);
        int unexpected = -1;
        Assert.assertNotEquals(unexpected, idMember);
    }

    @Test
    public void getMemberIdBYNoExistingPersonalNumber() throws SQLException {
        String personalNumber = "unknown";
        int idMember = new MiembroDAO().getMemberIdByPersonalNumber(personalNumber);
        int expected = -1;
        Assert.assertEquals(expected, idMember);
    }

    @Test
    public void checkMemberTest() throws SQLException {
        String personalNumber = "ADMIN";
        boolean existMember = new MiembroDAO().checkMember(personalNumber);
        Assert.assertTrue(existMember);
    }

    @Test
    public void checkMemberByNoExistingPersonalNumber() throws SQLException {
        String personalNumber = "noexist";
        boolean existMember = new MiembroDAO().checkMember(personalNumber);
        Assert.assertFalse(existMember);
    }
}
