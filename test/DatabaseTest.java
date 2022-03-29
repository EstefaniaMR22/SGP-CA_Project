import org.junit.Assert;
import org.junit.Test;
import assets.utils.Database;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseTest {
    private final Database database;

    public DatabaseTest() {
        database = new Database();
        database.setAnotherDatabaseProperties("database.properties");
    }

    @Test
    public void getConnection() throws SQLException {
        Connection connection = database.getConnection();
        Assert.assertNotNull(connection);
    }

    @Test
    public void disconnect() {
        Connection connection = database.disconnect();
        Assert.assertNull(connection);
    }


}
