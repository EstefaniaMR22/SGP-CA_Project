package model.dao.academicgroupdao;

import model.dao.AcademicGroupDAO;
import org.junit.Assert;
import org.junit.Test;

import java.sql.SQLException;

public class RemoveAcademicGroupTest {
    @Test
    public void removeAcademicGroupTest() throws SQLException {
        String academicId = "UV-CA-387";
        boolean isRemoved = new AcademicGroupDAO().removeAcademicGroup(academicId);
        Assert.assertTrue(isRemoved);
    }

    @Test
    public void removeAcademicGroupWithMembersTest() throws SQLException {
        String academicId = "UV-CA-388";
        boolean isRemoved = new AcademicGroupDAO().removeAcademicGroup(academicId);
        Assert.assertTrue(isRemoved);
    }

    @Test
    public void removeAcademicGroupWithLgacsTest() throws SQLException {
        String academicId = "UV-CA-389";
        boolean isRemoved = new AcademicGroupDAO().removeAcademicGroup(academicId);
        Assert.assertTrue(isRemoved);
    }

    @Test
    public void removeAcademicGroupWithLgacsAndMembersTest() throws SQLException {
        String academicId = "UV-CA-390";
        boolean isRemoved = new AcademicGroupDAO().removeAcademicGroup(academicId);
        Assert.assertTrue(isRemoved);
    }

}
