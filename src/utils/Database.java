package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Database {
    private static String user;
    private static String pass;
    private static String url;
    private Connection connection;

    /***
     * This method set the properties to database
     * @see DatabaseProperties
     */
    public Database() {
        DatabaseProperties databaseProperties = new DatabaseProperties("databaseTest.properties");
        Map<String, String> propertiesMap = databaseProperties.readProperties();
        user = propertiesMap.get("db.user");
        pass = propertiesMap.get("db.password");
        url = propertiesMap.get("db.url");
    }

    /***
     * This method set another database file (properties file).
     * <p>
     * It should be used when need to change the database.
     * </p>
     * @param path Path's file
     */
    public void setAnotherDatabaseProperties(String path) {
        DatabaseProperties databaseProperties = new DatabaseProperties(path);
        Map<String, String> propertiesMap = databaseProperties.readProperties();
        user = propertiesMap.get("db.user");
        pass = propertiesMap.get("db.password");
        url = propertiesMap.get("db.url");
    }

    /***
     * This method try to connect to database.
     * <p>
     * It is used when the system need to connect to database.
     * </p>
     */
    public Connection getConnection() throws SQLException {
        connectToDatabase();
        return connection;
    }

    /***
     * This method disconnect from database.
     * <p>
     * It is used by the system when it doesn't need a connection to database anymore
     * </p>
     * @return Connection object to describe Connection
     */
    public Connection disconnect() {
        if(connection != null){
            try {
                connection.close();
            } catch(SQLException e) {
                Logger.getLogger( Database.class.getName() ).log(Level.SEVERE, null, e);
            }
            connection = null;
        }
        return null;
    }

    private void connectToDatabase() throws SQLException {
        connection = DriverManager.getConnection(url,user,pass);
    }

}
