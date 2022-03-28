package model.dao.memberdao;

import model.dao.MiembroDAO;
import org.junit.Assert;
import org.junit.Test;

import java.sql.SQLException;

public class GetEducationProgramTest {
    @Test
    public void getEducationalProgramTest() throws SQLException {
        int expectedSize = 107;
        int actual = new MiembroDAO().getAllEducationProgram().size();
        Assert.assertEquals(expectedSize, actual);
    }
}
