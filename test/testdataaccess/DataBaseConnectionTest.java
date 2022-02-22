/**
* @author Josué Alarcón
* @version v1.0
* Last modification date: 20/06/2021
*/

package testdataaccess;

import java.sql.SQLException;
import java.sql.Connection;

import utils.ConnectionDatabase;
import org.junit.Test;
import org.junit.Assert;


public class DataBaseConnectionTest {
    
    @Test
    public void testDataBaseConnection() throws SQLException{
        Connection currentConection = new ConnectionDatabase().getConnectionDatabase();
        Assert.assertNotNull(currentConection);
    }
}
