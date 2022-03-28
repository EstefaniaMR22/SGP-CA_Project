package model.dao.lgacdao;

import model.dao.LgacDAO;
import model.domain.LGAC;
import org.junit.Assert;
import org.junit.Test;

import java.sql.SQLException;

public class GetLGACByIDTest {
    @Test
    public void getLGACByID() throws SQLException {
        int id = 37;
        LGAC lgac = new LgacDAO().getLGACById(id);
        String expected = "AS21";
        String actual = lgac.getIdentification();
        Assert.assertEquals(expected,actual);
    }

    @Test
    public void getLGACByNoExistingID() throws SQLException {
        int id = -1;
        LGAC lgac = new LgacDAO().getLGACById(id);
        Assert.assertNull(lgac);
    }

}
