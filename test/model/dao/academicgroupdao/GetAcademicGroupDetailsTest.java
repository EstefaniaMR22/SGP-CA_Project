package model.dao.academicgroupdao;

import model.dao.AcademicGroupDAO;
import model.domain.AcademicGroup;
import org.junit.Assert;
import org.junit.Test;

import java.sql.SQLException;

public class GetAcademicGroupDetailsTest {
    @Test
    public void getAcademicGroupDetailsTest() throws SQLException {
        String id = "UV-CA-387";
        AcademicGroup expected = new AcademicGroupDAO().getAcademicGroupProgramDetails(id);
        Assert.assertNotNull(expected);
    }

    @Test
    public void getAcademicGroupDetailsByNoExistingIdTest()  throws SQLException{
        String id = "NOEXINSTING";
        AcademicGroup expected = new AcademicGroupDAO().getAcademicGroupProgramDetails(id);
        Assert.assertNull(expected);
    }

}
