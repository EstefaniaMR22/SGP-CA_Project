
import org.junit.Assert;
import org.junit.Test;
import utils.DatabaseProperties;

import java.util.HashMap;
import java.util.Map;

class DatabasePropertiesTest {
    private final DatabaseProperties databaseProperties;

    public DatabasePropertiesTest() {
        databaseProperties = new DatabaseProperties("databaseTest.properties");
    }

    @Test
    public void writeProperties() {
        boolean isPropertieWritten = databaseProperties.writeProperties("anonimo", "12345", "jdbc:mysql://localhost:3306/Pruebas?useTimezone=true&serverTimezone=UTC");
        Assert.assertTrue(isPropertieWritten);
    }

    @Test
    public void readProperties() {
        Map<String, String> expected = new HashMap<String, String>();
        expected.put("db.user","anonimo");
        expected.put("db.password","12345");
        expected.put("db.url","jdbc:mysql://localhost:3306/Pruebas?useTimezone=true&serverTimezone=UTC");
        Map<String, String> result = databaseProperties.readProperties();
        Assert.assertEquals(expected, result);
    }

}