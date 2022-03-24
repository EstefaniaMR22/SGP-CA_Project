package model.dao.memberdao;

import model.dao.MiembroDAO;
import model.domain.StudyGrade;
import org.junit.Assert;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;

public class GetStudyGradesTest {
    @Test
    public void getStudyGradesTest() throws SQLException {
        List<StudyGrade> studyGradeList = new MiembroDAO().getStudyGrades();
        int expectedSize = 5;
        int actual = studyGradeList.size();
        Assert.assertEquals(expectedSize, actual);
    }
}
