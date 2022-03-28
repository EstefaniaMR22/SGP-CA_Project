package model.dao.academicgroupdao;

import model.dao.AcademicGroupDAO;
import model.domain.ConsolidationGrade;
import org.junit.Assert;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;

public class GetConsolidationGradeTest {
    @Test
    public void getConsolidationGrade() throws SQLException {
        List<ConsolidationGrade> consolidationGradeList = new AcademicGroupDAO().getConsolidationGrades();
        int size = 3;
        int actual = consolidationGradeList.size();
        Assert.assertEquals(size, actual);
    }
}
