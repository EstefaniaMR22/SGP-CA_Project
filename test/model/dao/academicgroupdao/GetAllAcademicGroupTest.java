package model.dao.academicgroupdao;

import model.dao.AcademicGroupDAO;
import model.domain.AcademicGroup;
import org.junit.Assert;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;

public class GetAllAcademicGroupTest {
    @Test
    public void getAllAcademicGroup() throws SQLException {
        List<AcademicGroup> academicGroups = new AcademicGroupDAO().getAllAcademicGroup();
        int sizeExpected = 5;
        int actual = academicGroups.size();
        Assert.assertEquals(sizeExpected, actual);
    }
}
