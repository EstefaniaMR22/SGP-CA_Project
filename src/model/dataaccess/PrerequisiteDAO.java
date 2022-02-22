/**
 * @author Estefanía 
 * @versión v1.0
 * Last modification date: 17-06-2021
 */

package model.dataaccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.domain.MeetingAgenda;
import model.domain.Prerequisite;
import utils.ConnectionDatabase;

public class PrerequisiteDAO implements IPrerequisiteDAO{
    private final ConnectionDatabase CONNECTION = new ConnectionDatabase();

    @Override
    public void addPrerequisite(Connection connection, MeetingAgenda meetingAgenda){
        meetingAgenda.getPrerequisites().forEach( prerequisite -> {
            try{
                PreparedStatement sentenceQuery = connection.prepareStatement(
                    "INSERT INTO Prerequisite (meetingAgendaKey, responsiblePrerequisite, "
                    + "descriptionPrerequisite) VALUES(?,?,?);"
                );
                sentenceQuery.setInt(1, meetingAgenda.getMeetingAgendaKey());
                sentenceQuery.setString(2, prerequisite.getResponsiblePrerequisite());
                sentenceQuery.setString(3, prerequisite.getDescrptionPrerequisite());
                sentenceQuery.executeUpdate();
            }catch(SQLException sqlException){
                try{
                    connection.rollback();
                    CONNECTION.closeConnection();
                    Logger.getLogger(Prerequisite.class.getName()).log(Level.SEVERE, null, sqlException);
                }catch(SQLException ex){
                    Logger.getLogger(PrerequisiteDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    @Override
    public List<Prerequisite> getPrerequisiteByAgendaMeeting(int meetingAgendaKey){
        List<Prerequisite> prerequisiteList = new ArrayList();
        try{
            PreparedStatement sentenceQuery = CONNECTION.getConnectionDatabase().prepareStatement(
                "SELECT * FROM Prerequisite WHERE meetingAgendaKey = ?;"
            );
            sentenceQuery.setInt(1, meetingAgendaKey);
            ResultSet queryResult = sentenceQuery.executeQuery();
            while(queryResult.next()){
                Prerequisite newPrerequisite = new Prerequisite(
                     queryResult.getInt("prerequisiteKey"),
                     queryResult.getString("responsiblePrerequisite"),
                     queryResult.getString("descriptionPrerequisite")
                );
                prerequisiteList.add(newPrerequisite);
            }
        }catch(SQLException sqlException){
            Logger.getLogger(PrerequisiteDAO.class.getName()).log(Level.SEVERE, null, sqlException);
        }finally{
            return prerequisiteList;
        }
    }
    
}
