package model.dao;

import assets.utils.Database;
import assets.utils.DateFormatter;
import assets.utils.SQLStates;
import controller.academicgroup.AddMemberController;
import controller.control.AlertController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.dao.interfaces.IMeetDAO;
import model.domain.CivilStatus;
import model.domain.Meet;
import model.domain.Member;
import model.domain.StudyGrade;

import java.sql.*;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MeetDAO implements IMeetDAO {

    private final Database databaseConection;

    public MeetDAO() {
        this.databaseConection = new Database();
    }

    /***
     * Get meets to database
     * <p>
     * Get all the meets from acdemicGroupId
     * </p>
     * @param academicGroupID academicGroupId for filtered meets
     * @return ObservableList that contain all the registered meets
     */
    @Override
    public ObservableList<Meet> getMeetList(String academicGroupID) throws SQLException {
        ObservableList<Meet> meetObservableList;
        meetObservableList = FXCollections.observableArrayList();

        try(Connection conn = databaseConection.getConnection()) {

            String statement = "SELECT * FROM Reunion INNER JOIN ProyectoInvestigacion ON Reunion.id_proyecto_investigacion" +
                    " = ProyectoInvestigacion.id INNER JOIN LGACProyectoInvestigacion ON  ProyectoInvestigacion.id = " +
                    "LGACProyectoInvestigacion.id_proyecto_investigacion INNER JOIN LGAC ON LGACProyectoInvestigacion.id_lgac = LGAC.id " +
                    "where LGAC.id_programa_cuerpo_academico = ?";

            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, academicGroupID);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Meet meetDataTable = new Meet();
                meetDataTable.setIdMeet(resultSet.getInt("Reunion.id"));
                meetDataTable.setAsunto(resultSet.getString("Reunion.nota_reunion"));
                meetDataTable.setHour(resultSet.getString("Reunion.hora"));
                meetDataTable.setIdProject(resultSet.getInt("Reunion.id_proyecto_investigacion"));
                meetDataTable.setNameProject(resultSet.getString("Reunion.nombre_proyecto"));

                meetDataTable.setDateMeet(DateFormatter.convertSQLDateToUtilDate(resultSet.getDate("Reunion.fecha")));
                meetDataTable.setDateMeetString(DateFormatter.getParseDate(meetDataTable.getDateMeet()));

                MemberDAO memberDAO = new MemberDAO();

                meetDataTable.setIdLeader(resultSet.getInt("Reunion.lider"));
                Member leader = memberDAO.getMember(meetDataTable.getIdLeader());
                meetDataTable.setLeader(leader.getFullName());

                meetObservableList.add(meetDataTable);

            }

        }
        databaseConection.disconnect();
        return meetObservableList;
    }

    /***
     * Get asistents from the meet to database
     * <p>
     * Get all asistents
     * </p>
     * @param idMeet for filtered asistents from particular meet
     * @return ObsevableList that contain all the registered asistents
     */
    @Override
    public ObservableList<Member> getAsistentsMeetList(int idMeet) throws SQLException {
        ObservableList<Member> memberObservableList= null;
        try(Connection conn = databaseConection.getConnection()) {
            String statement = "SELECT Miembro.id, Miembro.nombre, Miembro.apellido_paterno, Miembro.apellido_materno from " +
                    "Miembro INNER JOIN RolAsistencia ON RolAsistencia.id_asistente = Miembro.id " +
                    "INNER JOIN Reunion ON Reunion.id = RolAsistencia.id_reunion WHERE RolAsistencia.id_reunion = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setInt(1, idMeet);
            ResultSet resultSet = preparedStatement.executeQuery();
            memberObservableList = FXCollections.observableArrayList();
            while(resultSet.next()) {
                Member member = new Member();
                member.setId(resultSet.getInt("Miembro.id"));
                member.setName(resultSet.getString("Miembro.nombre"));
                member.setPaternalLastname(resultSet.getString("Miembro.apellido_paterno"));
                member.setMaternalLastname(resultSet.getString("Miembro.apellido_materno"));

                memberObservableList.add(member);
            }
        }
        return memberObservableList;


    }

    private StudyGrade getStudyGradeType(String studyGradeType) {
        for(StudyGrade studyGrade : StudyGrade.values() ) {
            if(studyGradeType.equalsIgnoreCase(studyGrade.getStudyGrade())) {
                return studyGrade;
            }
        }
        return null;
    }

    private CivilStatus getCivilStatus(String status) {
        for(CivilStatus civilStatus : CivilStatus.values()) {
            if(status.equalsIgnoreCase(civilStatus.getCivilStatus())) {
                return civilStatus;
            }
        }
        return null;
    }

    /***
     * Add meet
     * <p>
     * Add a new meet in database from a investigation project
     * </p>
     * @param newMeet the object meet
     * @return true if the meet registered
     */

    @Override
    public boolean addMeet(Meet newMeet) throws SQLException {
        boolean wasAdded = false;

        try(Connection conn = databaseConection.getConnection()) {
            conn.setAutoCommit(false);
            String statement = "INSERT INTO Reunion(hora, nota_reunion, nombre_proyecto, registro, tiempo_total," +
                    " id_proyecto_investigacion, lider, secretario, fecha) " +
                    "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement preparedStatement = conn.prepareStatement(statement);

            preparedStatement.setString(1, newMeet.getHour());
            preparedStatement.setString(2, newMeet.getAsunto());
            preparedStatement.setString(3, newMeet.getNameProject());
            preparedStatement.setDate(4, DateFormatter.convertUtilDateToSQLDate(newMeet.getRegister()));
            preparedStatement.setFloat(5, 0);
            preparedStatement.setInt(6, newMeet.getIdProject());
            preparedStatement.setInt(7, newMeet.getIdLeader());
            preparedStatement.setInt(8, newMeet.getIdSecretary());
            preparedStatement.setDate(9, DateFormatter.convertUtilDateToSQLDate(newMeet.getDateMeet()));
            
            wasAdded = preparedStatement.executeUpdate() > 0;

            conn.commit();
            
            if(wasAdded==true){
                try {
                addAsistentsMeet(getIdMeet(newMeet),newMeet.getAsistents());
                } catch (SQLException addProjectInvestigationException) {

                    deterMinateSQLState(addProjectInvestigationException);
                }
            }

        }
        databaseConection.disconnect();
        return wasAdded;
    }

    private void deterMinateSQLState(SQLException sqlException) {
        Logger.getLogger(AddMemberController.class.getName()).log(Level.SEVERE, null, sqlException);
        if(sqlException.getSQLState().equals(SQLStates.SQL_NO_CONNECTION.getSqlState())) {
            AlertController.getInstance().showConnectionErrorAlert();
        }
        AlertController.getInstance().showActionFailedAlert(sqlException.getLocalizedMessage());
    }

    /***
     * Add asistents from a meet
     * <p>
     * Add asistents in a meet
     * </p>
     * @param idMeet the idMeet for relation
     * @param asistents the list objects members from a meet
     * @return true if the assistents meet registered
     */

    @Override
    public boolean addAsistentsMeet(int idMeet, ObservableList<Member> asistents) throws SQLException {
        boolean wasAdded = false;

        try (Connection conn = databaseConection.getConnection()) {
            conn.setAutoCommit(false);

            for (Member asistente : asistents) {
                String statement = "INSERT INTO RolAsistencia(id_asistente, id_reunion)" +
                        "VALUES(?, ?)";

                PreparedStatement preparedStatement = conn.prepareStatement(statement);

                preparedStatement.setInt(1, asistente.getId());
                preparedStatement.setInt(2, idMeet);
                wasAdded = preparedStatement.executeUpdate() > 0;

                conn.commit();
            }


        }
        databaseConection.disconnect();

        return wasAdded;
    }

    /***
     * Get idMeet
     * <p>
     * Get id from a meet
     * </p>
     * @param meetWithoutId the object meet to find
     * @return idMeet
     */

    @Override
    public int getIdMeet(Meet meetWithoutId) throws SQLException {
        int idMeet = 0;

        try(Connection conn = databaseConection.getConnection()) {

            String statement = "SELECT id FROM Reunion " +
                    "WHERE registro = ? AND fecha = ? AND hora = ?";

            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setDate(1, DateFormatter.convertUtilDateToSQLDate(meetWithoutId.getRegister()));
            preparedStatement.setDate(2, DateFormatter.convertUtilDateToSQLDate(meetWithoutId.getDateMeet()));
            preparedStatement.setString(3, meetWithoutId.getHour());
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                idMeet = resultSet.getInt("id");
                System.out.println(idMeet);

            }

        }
        databaseConection.disconnect();
        return idMeet;
    }

    /***
     * Get meet details
     * <p>
     * Get all details from a reunion
     * </p>
     * @param idMeet the idMeet to consult
     * @return Meet
     */

    @Override
    public Meet getMeetDetails(int idMeet) throws SQLException {
        Meet meetDetails = new Meet();

        try(Connection conn = databaseConection.getConnection()) {

            String statement = "SELECT Reunion.id, Reunion.nota_reunion, Reunion.hora, Reunion.id_proyecto_investigacion, Reunion.nombre_proyecto," +
                    " Reunion.registro, Reunion.fecha, Reunion.tiempo_total, Reunion.lider, Reunion.secretario " +
                    "FROM Reunion INNER JOIN ProyectoInvestigacion ON Reunion.id_proyecto_investigacion = ProyectoInvestigacion.id " +
                    "INNER JOIN LGACProyectoInvestigacion ON  ProyectoInvestigacion.id = LGACProyectoInvestigacion.id_proyecto_investigacion" +
                    " INNER JOIN LGAC ON LGACProyectoInvestigacion.id_lgac = LGAC.id WHERE Reunion.id = ?";

            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setInt(1, idMeet);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                meetDetails.setIdMeet(resultSet.getInt("Reunion.id"));
                meetDetails.setAsunto(resultSet.getString("Reunion.nota_reunion"));
                meetDetails.setHour(resultSet.getString("Reunion.hora"));
                meetDetails.setIdProject(resultSet.getInt("Reunion.id_proyecto_investigacion"));
                meetDetails.setNameProject(resultSet.getString("Reunion.nombre_proyecto"));

                meetDetails.setRegister(DateFormatter.convertSQLDateToUtilDate(resultSet.getDate("Reunion.registro")));
                meetDetails.setRegisterString(DateFormatter.getParseDate(meetDetails.getRegister()));

                meetDetails.setDateMeet(DateFormatter.convertSQLDateToUtilDate(resultSet.getDate("Reunion.fecha")));
                meetDetails.setDateMeetString(DateFormatter.getParseDate(meetDetails.getDateMeet()));

                meetDetails.setTotalTime(resultSet.getString("Reunion.tiempo_total"));

                MemberDAO memberDAO = new MemberDAO();

                meetDetails.setIdLeader(resultSet.getInt("Reunion.lider"));
                Member leader = memberDAO.getMember(meetDetails.getIdLeader());
                meetDetails.setLeader(leader.getFullName());

                meetDetails.setIdSecretary(resultSet.getInt("Reunion.secretario"));
                Member secretary = memberDAO.getMember(meetDetails.getIdSecretary());
                meetDetails.setSecretary(secretary.getFullName());

                meetDetails.setAsistents(getAsistentsMeetList(meetDetails.getIdMeet()));

            }

        }
        databaseConection.disconnect();
        return meetDetails;
    }

    /***
     * Get meet details
     * <p>
     * Get details from a reunion and verificate if exist the same hour and date from meet
     * </p>
     * @param meetDate the date meet to consult
     * @param hourMeet the hour meet to consult
     * @return boolean if exist a copy return false, but if not exist return true
     */

    @Override
    public boolean checkMeet(Date meetDate, String hourMeet) throws SQLException {
        boolean existMeet = false;

        try(Connection conn = databaseConection.getConnection()) {

            String statement = "SELECT * FROM Reunion " +
                    "WHERE Reunion.fecha = ? AND Reunion.hora = ?";

            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setDate(1, DateFormatter.convertUtilDateToSQLDate(meetDate));
            preparedStatement.setString(2, hourMeet);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {
                existMeet = true;
            }

        }
        databaseConection.disconnect();
        return existMeet;
    }

    /***
     * Get meet details
     * <p>
     * Get details from a reunion and verificate if exist the same hour and date from meet
     * </p>
     * @param idMeet the date meet to consult
     * @param idMember the hour meet to consult
     * @return boolean if exist a copy return false, but if not exist return true
     */

    public boolean checkSecretary(int idMeet, int idMember) throws SQLException {
        boolean isSecretary = false;

        try(Connection conn = databaseConection.getConnection()) {

            String statement = "SELECT * FROM Reunion " +
                    "WHERE Reunion.id = ? AND Reunion.secretario = ?";

            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setInt(1, idMeet);
            preparedStatement.setInt(2, idMember);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {
                isSecretary = true;
            }

        }
        databaseConection.disconnect();
        return isSecretary;
    }

    /***
     * Update meet
     * <p>
     * Update details from a meet registered
     * </p>
     * @param updatedMeet the details meet to update
     * @return boolean if action is rigth return true
     */

    @Override
    public boolean updateMeet(Meet updatedMeet) throws SQLException {
        boolean wasUpdated = false;

        try(Connection conn = databaseConection.getConnection()) {
            int rowsAffected = 0;
            conn.setAutoCommit(false);
            String statement = "UPDATE Reunion SET nota_reunion = ?, fecha = ?, hora = ?  WHERE id = ?";

            PreparedStatement preparedStatement = conn.prepareStatement(statement);

            preparedStatement.setString(1, updatedMeet.getAsunto());
            preparedStatement.setDate(2, DateFormatter.convertUtilDateToSQLDate(updatedMeet.getDateMeet()));
            preparedStatement.setString(3, updatedMeet.getHour());
            preparedStatement.setInt(4, updatedMeet.getIdMeet());
            rowsAffected = preparedStatement.executeUpdate();
            wasUpdated = rowsAffected > 0;

            conn.commit();

        }
        databaseConection.disconnect();
        return wasUpdated;
    }


    public boolean addTimeMeet(int idMeet, String timeMeet) throws SQLException {
        boolean wasUpdated = false;

        try(Connection conn = databaseConection.getConnection()) {
            int rowsAffected = 0;
            conn.setAutoCommit(false);
            String statement = "UPDATE Reunion SET tiempo_total = ? WHERE id = ?";

            PreparedStatement preparedStatement = conn.prepareStatement(statement);

            preparedStatement.setString(1, timeMeet);
            preparedStatement.setInt(2, idMeet);
            rowsAffected = preparedStatement.executeUpdate();
            wasUpdated = rowsAffected > 0;

            conn.commit();

        }
        databaseConection.disconnect();
        return wasUpdated;
    }

    public boolean timeMeetIsNull(int idMeet) throws SQLException {
        boolean isNull = true;

        try(Connection conn = databaseConection.getConnection()) {
            String statement = "SELECT * FROM Reunion " +
                    "WHERE Reunion.id = ?";

            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setInt(1, idMeet);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {
                String timeMeet = resultSet.getString("tiempo_total");
                System.out.println(timeMeet);
                if(timeMeet != "0.0"){
                    isNull = false;
                }else {
                    isNull = true;
                }

            }

        }
        databaseConection.disconnect();
        return isNull;
    }
}
