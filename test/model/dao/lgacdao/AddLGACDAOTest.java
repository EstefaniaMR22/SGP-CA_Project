package model.dao.lgacdao;

import model.dao.LgacDAO;
import model.domain.LGAC;
import org.junit.Assert;
import org.junit.Test;

import java.sql.SQLException;

public class AddLGACDAOTest {
    @Test
    public void addLGACTest() throws SQLException {
        String academicGroupID = "A91";
        LGAC lgac = new LGAC();
        lgac.setIdentification("only");
        lgac.setDescription("Prueba de LGAC");
        int actualId = new LgacDAO().addLgac(lgac, academicGroupID);
        int unexpectedId = 0;
        Assert.assertNotEquals(unexpectedId, actualId);
    }



}
