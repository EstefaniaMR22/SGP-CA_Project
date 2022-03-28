package model.dao.memberdao;

import model.dao.MiembroDAO;
import org.junit.Assert;
import org.junit.Test;

import java.sql.SQLException;

public class RemoveMemberTest {
    @Test
    public void removeMemberByIdTest() throws SQLException {
        String personalNumber = "9432";
        int idMember = new MiembroDAO().getMemberIdByPersonalNumber(personalNumber);
        boolean isRemoved = new MiembroDAO().removeMember(idMember);
        Assert.assertTrue(isRemoved);
    }

    @Test
    public void removeAnotherMemberByIdTest() throws SQLException {
        String personalNumber = "9433";
        int idMember = new MiembroDAO().getMemberIdByPersonalNumber(personalNumber);
        boolean isRemoved = new MiembroDAO().removeMember(idMember);
        Assert.assertTrue(isRemoved);
    }
}
