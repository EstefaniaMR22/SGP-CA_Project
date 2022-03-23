package model.dao.lgacdao;

import model.dao.LgacDAO;
import model.domain.LGAC;
import org.junit.Assert;
import org.junit.Test;

import java.sql.SQLException;

public class AddLGACDAOTest {
    @Test
    public void addLGACTest() throws SQLException {
        LGAC lgac = new LGAC();
        lgac.setIdentification("only");
        lgac.setDescription("Prueba de LGAC");
        int actualId = new LgacDAO().addLgac(lgac);
        int unexpectedId = 0;
        Assert.assertNotEquals(unexpectedId, actualId);
    }

}
