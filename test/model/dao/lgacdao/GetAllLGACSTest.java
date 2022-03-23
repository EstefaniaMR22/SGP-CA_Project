package model.dao.lgacdao;

import model.dao.LgacDAO;
import model.domain.LGAC;
import org.junit.Assert;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;

public class GetAllLGACSTest {
    @Test
    public void getAllLGACTest() throws SQLException {
        List<LGAC> list = new LgacDAO().getAlllgacs();
        int expected = 4;
        int actual = list.size();
        Assert.assertEquals(expected,actual);
    }
}
