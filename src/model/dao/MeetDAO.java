package model.dao;

import assets.utils.Database;
import assets.utils.DateFormatter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.dao.interfaces.IMeetDAO;
import model.domain.CivilStatus;
import model.domain.Meet;
import model.domain.Member;
import model.domain.StudyGrade;

import java.sql.*;
import java.util.Date;

public class MeetDAO implements IMeetDAO {

    private final Database databaseConection;

    public MeetDAO() {
        this.databaseConection = new Database();
    }

    /***
     * Get meets to database
     * <p>
     * Get all the investigation projects
     * </p>
     * @return List that contain all the registered investigation projects.
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
                meetDataTable.setIdProject(resultSet.getInt("Reunion"));
                meetDataTable.setNameProject(resultSet.getString("Reunion.nombre_proyecto"));

                meetDataTable.setRegister(DateFormatter.convertSQLDateToUtilDate(resultSet.getDate("Reunion.registro")));
                meetDataTable.setRegisterString(DateFormatter.getParseDate(meetDataTable.getRegister()));

                meetDataTable.setDateMeet(DateFormatter.convertSQLDateToUtilDate(resultSet.getDate("Reunion.fecha")));
                meetDataTable.setDateMeetString(DateFormatter.getParseDate(meetDataTable.getDateMeet()));

                meetDataTable.setTotalTime(resultSet.getString("Reunion.tiempo_total"));

                MemberDAO memberDAO = new MemberDAO();

                meetDataTable.setIdLeader(resultSet.getInt("Reunion.lider"));
                Member leader = memberDAO.getMember(meetDataTable.getIdLeader());
                meetDataTable.setLeader(leader.getFullName());

                meetDataTable.setIdSecretary(resultSet.getInt("Reunion.secretario"));
                Member secretary = memberDAO.getMember(meetDataTable.getIdSecretary());
                meetDataTable.setSecretary(secretary.getFullName());

                meetDataTable.setIdTimer(resultSet.getInt("Reunion.tomador_de_tiempo"));
                Member timer = memberDAO.getMember(meetDataTable.getIdTimer());
                meetDataTable.setTimer(timer.getFullName());
                meetDataTable.setAsistents(getAsistentsMeetList(meetDataTable.getIdMeet()));

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
     * @return List that contain all the registered asistents
     */
    @Override
    public ObservableList<Member> getAsistentsMeetList(int idMeet) throws SQLException {
        ObservableList<Member> memberObservableList= null;
        try(Connection conn = databaseConection.getConnection()) {
            String statement = "SELECT * from Miembro INNER JOIN RolAsistencia ON RolAsistencia.id_asistente = Miembro.id " +
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
                member.setNationality(resultSet.getString("Miembro.nacionalidad"));
                member.setEducationalProgram(resultSet.getString("Miembro.programa_educativo"));
                member.setPersonalNumber(resultSet.getString("Miembro.numero_personal"));
                member.setRfc(resultSet.getString("Miembro.rfc"));
                member.setTelephone(resultSet.getString("Miembro.telefono"));
                member.setBirthState(resultSet.getString("Miembro.estado"));
                member.setCurp(resultSet.getString("Miembro.curp"));
                member.setCivilStatus(getCivilStatus(resultSet.getString("Miembro.estado_civil")));
                member.setUvEmail(resultSet.getString("Miembro.email"));
                member.setBirthDate(DateFormatter.convertSQLDateToUtilDate(resultSet.getDate("Miembro.fecha_nacimiento")));
                member.setAdmissionDate(DateFormatter.convertSQLDateToUtilDate(resultSet.getDate("Miembro.fecha_ingreso")));
                member.setAppointment(resultSet.getString("Miembro.nombramiento"));
                member.setHomeTelephone(resultSet.getString("Miembro.telefono_casa"));
                member.setWorkTelephone(resultSet.getString("Miembro.telefono_trabajo"));
                member.setAditionalEmail(resultSet.getString("Miembro.correo_adicional"));
                member.setStudyArea(resultSet.getString("Miembro.area_estudio"));
                member.setMaxStudyGrade(getStudyGradeType(resultSet.getString("Miembro.grado_estudios")));
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


    @Override
    public boolean addMeet(Meet newMeet) throws SQLException {
        boolean wasAdded = false;

        try(Connection conn = databaseConection.getConnection()) {
            conn.setAutoCommit(false);
            String statement = "INSERT INTO Reunion(hora, nota_reunion, nombre_proyecto, registro, tiempo_total, tiempo_total_estimado," +
                    " id_proyecto_investigacion, lider, secretario, tomador_de_tiempo, fecha) " +
                    "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement preparedStatement = conn.prepareStatement(statement);

            preparedStatement.setTime(1, Time.valueOf(newMeet.getHour()));
            preparedStatement.setString(2, newMeet.getAsunto());
            preparedStatement.setString(3, newMeet.getNameProject());
            preparedStatement.setDate(4, DateFormatter.convertUtilDateToSQLDate(newMeet.getRegister()));
            preparedStatement.setFloat(5, Float.parseFloat(newMeet.getTotalTime()));
            preparedStatement.setInt(6, newMeet.getIdProject());
            preparedStatement.setInt(7, newMeet.getIdLeader());
            preparedStatement.setInt(8, newMeet.getIdSecretary());
            preparedStatement.setInt(9, newMeet.getIdTimer());
            preparedStatement.setDate(10, DateFormatter.convertUtilDateToSQLDate(newMeet.getDateMeet()));
            
            wasAdded = preparedStatement.executeUpdate() > 0;

            conn.commit();
            
            if(wasAdded==true){
                addAsistentsMeet(getIdMeet(newMeet),newMeet.getAsistents());
            }

        }
        databaseConection.disconnect();
        return wasAdded;
    }


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
                preparedStatement.setInt(1, asistente.getId());
                wasAdded = preparedStatement.executeUpdate() > 0;

                conn.commit();
            }


        }
        databaseConection.disconnect();

        return wasAdded;
    }

    @Override
    public int getIdMeet(Meet meetWithoutId) throws SQLException {
        int idMeet = 0;

        try(Connection conn = databaseConection.getConnection()) {

            String statement = "SELECT id FROM Reunion " +
                    "WHERE Reunion.registro = ? AND Reunion.fecha = ? AND Reunion.hora = ?";

            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setDate(1, DateFormatter.convertUtilDateToSQLDate(meetWithoutId.getRegister()));
            preparedStatement.setDate(2, DateFormatter.convertUtilDateToSQLDate(meetWithoutId.getDateMeet()));
            preparedStatement.setTime(3, Time.valueOf(meetWithoutId.getHour()));
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                idMeet = resultSet.getInt("id");

            }

        }
        databaseConection.disconnect();
        return idMeet;
    }


    @Override
    public Meet getMeetDetails(int idMeet) throws SQLException {
        Meet meetDetails = new Meet();

        try(Connection conn = databaseConection.getConnection()) {

            String statement = "SELECT * FROM Reunion INNER JOIN ProyectoInvestigacion ON Reunion.id_proyecto_investigacion" +
                    " = ProyectoInvestigacion.id INNER JOIN LGACProyectoInvestigacion ON  ProyectoInvestigacion.id = " +
                    "LGACProyectoInvestigacion.id_proyecto_investigacion INNER JOIN LGAC ON LGACProyectoInvestigacion.id_lgac = LGAC.id " +
                    "WHERE Reunion.id = ?";

            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setInt(1, idMeet);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                meetDetails.setIdMeet(resultSet.getInt("Reunion.id"));
                meetDetails.setAsunto(resultSet.getString("Reunion.nota_reunion"));
                meetDetails.setHour(resultSet.getString("Reunion.hora"));
                meetDetails.setIdProject(resultSet.getInt("Reunion"));
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

                meetDetails.setIdTimer(resultSet.getInt("Reunion.tomador_de_tiempo"));
                Member timer = memberDAO.getMember(meetDetails.getIdTimer());
                meetDetails.setTimer(timer.getFullName());
                meetDetails.setAsistents(getAsistentsMeetList(meetDetails.getIdMeet()));

            }

        }
        databaseConection.disconnect();
        return meetDetails;
    }

    @Override
    public boolean checkMeet(Date meetDate, String hourMeet) throws SQLException {
        boolean existMeet = false;

        try(Connection conn = databaseConection.getConnection()) {

            String statement = "SELECT * FROM Reunion " +
                    "WHERE Reunion.fecha = ? AND Reunion.hora = ?";

            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setDate(1, DateFormatter.convertUtilDateToSQLDate(meetDate));
            preparedStatement.setTime(2, Time.valueOf(hourMeet));
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {
                existMeet = true;
            }

        }
        databaseConection.disconnect();
        return existMeet;
    }

    @Override
    public boolean updateMeet(Meet updatedMeet) throws SQLException {
        boolean wasAdded = false;

        try(Connection conn = databaseConection.getConnection()) {
            conn.setAutoCommit(false);
            String statement = "UPDATE Reunion SET nota_reunion = ?, fecha = ?, hora = ? WHERE id = ?";

            PreparedStatement preparedStatement = conn.prepareStatement(statement);

            preparedStatement.setString(1, updatedMeet.getAsunto());
            preparedStatement.setTime(2, Time.valueOf(updatedMeet.getHour()));
            preparedStatement.setDate(3, DateFormatter.convertUtilDateToSQLDate(updatedMeet.getDateMeet()));
            preparedStatement.setInt(4, updatedMeet.getIdMeet());

            wasAdded = preparedStatement.executeUpdate() > 0;

            conn.commit();

        }
        databaseConection.disconnect();
        return wasAdded;
    }
}
