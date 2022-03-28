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
        List<LGAC> list = new LgacDAO().getAllLgacsByIdAcademicGroup("A91");
        int expected = 1;
        int actual = list.size();
        Assert.assertEquals(expected,actual);
    }
}
