package model.dao.memberdao;

import model.dao.MemberDAO;
import org.junit.Assert;
import org.junit.Test;

import java.sql.SQLException;

public class GetEducationProgramTest {
    @Test
    public void getEducationalProgramTest() throws SQLException {
        int expectedSize = 107;
        int actual = new MemberDAO().getAllEducationProgram().size();
        Assert.assertEquals(expectedSize, actual);
    }
}
