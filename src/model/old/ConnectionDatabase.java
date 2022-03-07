/**
* @author Josué Alarcón
* @version v1.0
* Last modification date: 20-06-2021
*/

package model.old;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ConnectionDatabase {
    
    private Connection connectionDatabase = null;
    
    public Connection getConnectionDatabase(){
        establishConnection();
        return connectionDatabase;
    }
    
    public Connection getConnectionDatabaseNotAutoCommit(){
        establishConnection();
        try {
            connectionDatabase.setAutoCommit(false);
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            return connectionDatabase;
        }
    }
    
    private void establishConnection(){
        Scanner input = null; 
        try{
            input = new Scanner(new File("dataAccess.txt"));
            connectionDatabase = DriverManager.getConnection(
                input.nextLine(), 
                input.nextLine(), 
                input.nextLine()
            );
        }catch(SQLException sqlException){
            Logger.getLogger(ConnectionDatabase.class.getName()).log(Level.SEVERE, null, sqlException);
        }catch(FileNotFoundException ex){
             Logger.getLogger(ConnectionDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            input.close();
        }
    }
    
    public void closeConnection(){
        if(connectionDatabase != null){
            try{
                if(!connectionDatabase.isClosed()){
                    connectionDatabase.close();
                }
            }catch(SQLException sqlException){
                Logger.getLogger(ConnectionDatabase.class.getName()).log(Level.SEVERE, null, sqlException);
            }
        }
    }    

}
