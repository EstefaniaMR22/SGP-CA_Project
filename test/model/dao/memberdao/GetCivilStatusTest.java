package model.dao.memberdao;

import model.dao.MiembroDAO;
import model.domain.CivilStatus;
import org.junit.Assert;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;

public class GetCivilStatusTest {
    @Test
    public void getCivilStatus() throws SQLException {
        List<CivilStatus> civilStatusList = new MiembroDAO().getCivilStatus();
        int expectedSize = 6;
        int actual = civilStatusList.size();
        Assert.assertEquals(expectedSize,actual);
    }
}
