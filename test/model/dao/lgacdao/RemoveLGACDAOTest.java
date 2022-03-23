package model.dao.lgacdao;

import model.dao.LgacDAO;
import model.domain.LGAC;
import org.junit.Assert;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;

public class RemoveLGACDAOTest {
    @Test
    public void removeLgacTest() throws SQLException {
        List<LGAC> list = new LgacDAO().getAlllgacs();
        int idLastLGAC = list.get(list.size()-1).getId();
        boolean isRemoved = new LgacDAO().removeLgac(idLastLGAC);
        Assert.assertTrue(isRemoved);
    }

    @Test
    public void removeLgacByNoExistingIdTest() throws SQLException {
        int id = -1;
        boolean isRemoved = new LgacDAO().removeLgac(id);
        Assert.assertFalse(isRemoved);
    }

}
