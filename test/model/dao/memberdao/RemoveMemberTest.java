package model.dao.memberdao;

import model.dao.MiembroDAO;
import org.junit.Assert;
import org.junit.Test;

import java.sql.SQLException;

public class RemoveMemberTest {
    @Test
    public void removeMemberIntegrantByIdTest() throws SQLException {
        String personalNumber = "a92";
        int idMember = new MiembroDAO().getMemberIdByPersonalNumber(personalNumber);
        boolean isRemoved = new MiembroDAO().removeMember(idMember);
        Assert.assertTrue(isRemoved);
    }

    @Test
    public void removeMemberColaboratorByIdTest() throws SQLException {
        String personalNumber = "4fsid02";
        int idMember = new MiembroDAO().getMemberIdByPersonalNumber(personalNumber);
        boolean isRemoved = new MiembroDAO().removeMember(idMember);
        Assert.assertTrue(isRemoved);
    }

    @Test
    public void removeMemberResponsableByIdTest() throws SQLException {
        String personalNumber = "afsdk";
        int idMember = new MiembroDAO().getMemberIdByPersonalNumber(personalNumber);
        boolean isRemoved = new MiembroDAO().removeMember(idMember);
        Assert.assertTrue(isRemoved);
    }

}
