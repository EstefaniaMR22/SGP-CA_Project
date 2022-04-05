package model.dao.memberdao;

import model.dao.MemberDAO;
import model.domain.CivilStatus;
import org.junit.Assert;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;

public class GetCivilStatusTest {
    @Test
    public void getCivilStatus() throws SQLException {
        List<CivilStatus> civilStatusList = new MemberDAO().getCivilStatus();
        int expectedSize = 6;
        int actual = civilStatusList.size();
        Assert.assertEquals(expectedSize,actual);
    }
}
