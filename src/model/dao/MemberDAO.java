package model.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.dao.interfaces.IMemberDAO;
import assets.utils.Database;
import assets.utils.DateFormatter;
import model.domain.ActivityStateMember;
import model.domain.CivilStatus;
import model.domain.Member;
import model.domain.ParticipationType;
import model.domain.StudyGrade;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class MemberDAO implements IMemberDAO {
    private final Database database;

    public MemberDAO() {
        this.database = new Database();
    }

    /***
     * Add Member to database including Account to system access.
     * <p>
     * Add an Member as Responsalbe to database, include an Access Account.
     * </p>
     * @param member The member of the Academic Group Program
     * @param password the account password.
     * @return id representing the user's id in database.
     */
    @Override
    public int addMember(Member member, String password) throws SQLException {
        int idMember = -1;
        try(Connection conn = database.getConnection() ) {
            conn.setAutoCommit(false);
            String statement = "CALL agregarResponsableIntegrante(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            CallableStatement callableStatement = conn.prepareCall(statement);
            callableStatement.setString(1,member.getName());
            callableStatement.setString(2,member.getPaternalLastname());
            callableStatement.setString(3, member.getMaternalLastname());
            callableStatement.setString(4, member.getNationality());
            callableStatement.setString(5, member.getEducationalProgram());
            callableStatement.setString(6, member.getPersonalNumber());
            callableStatement.setString(7, member.getUvEmail());
            callableStatement.setString(8, member.getRfc());
            callableStatement.setString(9, member.getTelephone());
            callableStatement.setString(10, member.getBirthState());
            callableStatement.setString(11, member.getCurp());
            callableStatement.setString(12, member.getCivilStatus().getCivilStatus());
            callableStatement.setString(13, member.getAppointment());
            callableStatement.setString(14, member.getHomeTelephone());
            callableStatement.setString(15, member.getWorkTelephone());
            callableStatement.setString(16, member.getAditionalEmail());
            callableStatement.setString(17, password);
            callableStatement.setDate(18, DateFormatter.convertUtilDateToSQLDate(member.getAdmissionDate()));
            callableStatement.setDate(19, DateFormatter.convertUtilDateToSQLDate(member.getBirthDate()));
            callableStatement.setString(20, member.getStudyArea());
            callableStatement.setString(21, member.getMaxStudyGrade().getStudyGrade());
            callableStatement.registerOutParameter(22, Types.INTEGER);
            callableStatement.execute();
            idMember = callableStatement.getInt(22);
        }
        return idMember;
    }
    /***
     * Add Colaborator to database
     * <p>
     * Add an Member as Colaborator to database.
     * </p>
     * @param colaborator The member of the Academic Group Program
     * @return int representing the user's id in database.
     */
    @Override
    public int addMember(Member colaborator) throws SQLException {
        int idMember = -1;
        try(Connection conn = database.getConnection() ) {
            conn.setAutoCommit(false);
            String statement = "CALL agregarColaborador(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            CallableStatement callableStatement = conn.prepareCall(statement);
            callableStatement.setString(1,colaborator.getName());
            callableStatement.setString(2,colaborator.getPaternalLastname());
            callableStatement.setString(3, colaborator.getMaternalLastname());
            callableStatement.setString(4, colaborator.getNationality());
            callableStatement.setString(5, colaborator.getEducationalProgram());
            callableStatement.setString(6, colaborator.getPersonalNumber());
            callableStatement.setString(7, colaborator.getUvEmail());
            callableStatement.setString(8, colaborator.getRfc());
            callableStatement.setString(9, colaborator.getTelephone());
            callableStatement.setString(10, colaborator.getBirthState());
            callableStatement.setString(11, colaborator.getCurp());
            callableStatement.setString(12, colaborator.getCivilStatus().getCivilStatus());
            callableStatement.setString(13, colaborator.getAppointment());
            callableStatement.setString(14, colaborator.getHomeTelephone());
            callableStatement.setString(15, colaborator.getWorkTelephone());
            callableStatement.setString(16, colaborator.getAditionalEmail());
            callableStatement.setDate(17, DateFormatter.convertUtilDateToSQLDate(colaborator.getAdmissionDate()));
            callableStatement.setDate(18, DateFormatter.convertUtilDateToSQLDate(colaborator.getBirthDate()));
            callableStatement.setString(19, colaborator.getStudyArea());
            callableStatement.setString(20, colaborator.getMaxStudyGrade().getStudyGrade());
            callableStatement.registerOutParameter(21, Types.INTEGER);
            callableStatement.execute();
            idMember = callableStatement.getInt(21);
        }
        return idMember;
    }
    /***
     * Get all the civil status
     * <p>
     * Get all the civil status like Single, Concubinate, Married, and so on...
     * </p>
     * @return List that contain all the available civil status.
     */
    @Override
    public List<CivilStatus> getCivilStatus() throws SQLException {
        List<CivilStatus> civilStatuses = new ArrayList<>();
        try(Connection conn = database.getConnection() ) {
            String statement = "SELECT * FROM EstadoCivil";
            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                civilStatuses.add(getCivilStatus(resultSet.getString(2)));
            }
        }
        return civilStatuses;
    }
    /***
     * Get all members registered in database
     * <p>
     * Get all the members including colaborators, responsables and integrants
     * </p>
     * @return List that contain all the registered members
     */
    @Override
    public List<Member> getAllMembers() throws SQLException {
        List<Member> memberList = null;
        try(Connection conn = database.getConnection()) {
            String statement = "SELECT * FROM Miembro";
            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            ResultSet resultSet = preparedStatement.executeQuery();
            memberList = new ArrayList<>();
            while(resultSet.next()) {
                Member member = new Member();
                member.setId(resultSet.getInt("id"));
                member.setName(resultSet.getString("nombre"));
                member.setPaternalLastname(resultSet.getString("apellido_paterno"));
                member.setMaternalLastname(resultSet.getString("apellido_materno"));
                member.setNationality(resultSet.getString("nacionalidad"));
                member.setEducationalProgram(resultSet.getString("programa_educativo"));
                member.setPersonalNumber(resultSet.getString("numero_personal"));
                member.setRfc(resultSet.getString("rfc"));
                member.setTelephone(resultSet.getString("telefono"));
                member.setBirthState(resultSet.getString("estado"));
                member.setCurp(resultSet.getString("curp"));
                member.setCivilStatus(getCivilStatus(resultSet.getString("estado_civil")));
                member.setUvEmail(resultSet.getString("email"));
                member.setBirthDate(DateFormatter.convertSQLDateToUtilDate(resultSet.getDate("fecha_nacimiento")));
                member.setAdmissionDate(DateFormatter.convertSQLDateToUtilDate(resultSet.getDate("fecha_ingreso")));
                member.setAppointment(resultSet.getString("nombramiento"));
                member.setHomeTelephone(resultSet.getString("telefono_casa"));
                member.setWorkTelephone(resultSet.getString("telefono_trabajo"));
                member.setAditionalEmail(resultSet.getString("correo_adicional"));
                member.setStudyArea(resultSet.getString("area_estudio"));
                member.setMaxStudyGrade(getStudyGradeType(resultSet.getString("grado_estudios")));
                member.setActivityStateMember(getActivityStateMember(resultSet.getString("estado_actividad")));
                memberList.add(member);
            }
        }
        return memberList;
    }

    @Override
    public ObservableList<Member> getAllMembersByIdAcademicBody(String idAcademicBody) throws SQLException {
        ObservableList<Member> memberObservableList= null;
        try(Connection conn = database.getConnection()) {
            String statement = "SELECT * from Miembro INNER JOIN " +
                    "ParticipacionCuerpoAcademicoMiembro ON ParticipacionCuerpoAcademicoMiembro.id_miembro = Miembro.id " +
                    "INNER JOIN ProgramaCuerpoAcademico ON ParticipacionCuerpoAcademicoMiembro.id_cuerpo_academico = " +
                    "ProgramaCuerpoAcademico.id WHERE ProgramaCuerpoAcademico.id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, idAcademicBody);
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
                member.setActivityStateMember(getActivityStateMember(resultSet.getString("estado_actividad")));
                memberObservableList.add(member);
            }
        }
        return memberObservableList;
    }

    /***
     * Update member
     * <p>
     * Update the member data by newer information.
     * </p>
     * @param member that contains all the data
     * @return true if integrant was updated otherwise it returns false
     */
    @Override
    public boolean updateMember(Member member) throws SQLException {
        boolean isUpdated = false;
        try(Connection conn = database.getConnection() ) {
            int rowsAffected = 0;
            conn.setAutoCommit(false);
            String updateMiembroStatement = "UPDATE Miembro SET nombre = ?, apellido_paterno = ?, apellido_materno = ?, nacionalidad = ?, programa_educativo = ?, numero_personal = ?, rfc = ?, telefono = ?, estado = ?, curp = ?, estado_civil = ?, email = ?, fecha_nacimiento = ?, fecha_ingreso = ?, area_estudio = ?, grado_estudios = ?, nombramiento = ?, telefono_casa = ?, telefono_trabajo = ?, correo_adicional = ?, estado_actividad = ? WHERE id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(updateMiembroStatement);
            preparedStatement.setString(1, member.getName());
            preparedStatement.setString(2, member.getPaternalLastname());
            preparedStatement.setString(3, member.getMaternalLastname());
            preparedStatement.setString(4, member.getNationality());
            preparedStatement.setString(5, member.getEducationalProgram());
            preparedStatement.setString(6, member.getPersonalNumber());
            preparedStatement.setString(7, member.getRfc());
            preparedStatement.setString(8, member.getTelephone());
            preparedStatement.setString(9, member.getBirthState());
            preparedStatement.setString(10, member.getCurp());
            preparedStatement.setString(11, member.getCivilStatus().getCivilStatus());
            preparedStatement.setString(12, member.getUvEmail());
            preparedStatement.setDate(13, DateFormatter.convertUtilDateToSQLDate(member.getBirthDate()));
            preparedStatement.setDate(14, DateFormatter.convertUtilDateToSQLDate(member.getAdmissionDate()));
            preparedStatement.setString(15, member.getStudyArea());
            preparedStatement.setString(16, member.getMaxStudyGrade().getStudyGrade());
            preparedStatement.setString(17, member.getAppointment());
            preparedStatement.setString(18, member.getHomeTelephone());
            preparedStatement.setString(19, member.getWorkTelephone());
            preparedStatement.setString(20, member.getAditionalEmail());
            preparedStatement.setString(21, member.getActivityStateMember().getActivityState());
            preparedStatement.setInt(22, member.getId());
            rowsAffected = preparedStatement.executeUpdate();
            String updateCuentaStatement = "UPDATE Cuenta SET email = ? WHERE id_miembro = ?";
            preparedStatement = conn.prepareStatement(updateCuentaStatement);
            preparedStatement.setString(1,member.getUvEmail());
            preparedStatement.setInt(2, member.getId());
            rowsAffected += preparedStatement.executeUpdate();
            isUpdated = rowsAffected > 0;
            conn.commit();
        }
        return isUpdated;
    }
    /***
     * Remove Member
     * <p>
     * Remove all the member data in the database.
     * </p>
     * @param idMember member to be removed.
     * @return true if it was removed otherwise false.
     */
    @Override
    public boolean removeMember(int idMember) throws SQLException {
        boolean wasRemoved = false;
        try (Connection conn = database.getConnection()) {
            String statement = "DELETE FROM Miembro WHERE id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setInt(1, idMember);
            int rowsAffected = preparedStatement.executeUpdate();
            wasRemoved = rowsAffected > 0;
        } catch(SQLException sqlException) {
            if(sqlException.getSQLState().equals( String.valueOf(23000))) {
                wasRemoved = updateActivityStateMember(idMember);
            } else {
                throw sqlException;
            }
        }
        return wasRemoved;
    }

    /***
     * Get Member
     * <p>
     * Get member data by id
     * </p>
     * @param id the member's id registered in database.
     * @return Member that contains all the information about member.
     */
    @Override
    public Member getMember(int id) throws SQLException {
        Member member = null;
        try(Connection conn = database.getConnection()) {
            String statement = "SELECT * FROM Miembro WHERE id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                member = new Member();
                member.setId(resultSet.getInt("id"));
                member.setName(resultSet.getString("nombre"));
                member.setPaternalLastname(resultSet.getString("apellido_paterno"));
                member.setMaternalLastname(resultSet.getString("apellido_materno"));
                member.setNationality(resultSet.getString("nacionalidad"));
                member.setEducationalProgram(resultSet.getString("programa_educativo"));
                member.setPersonalNumber(resultSet.getString("numero_personal"));
                member.setRfc(resultSet.getString("rfc"));
                member.setTelephone(resultSet.getString("telefono"));
                member.setBirthState(resultSet.getString("estado"));
                member.setCurp(resultSet.getString("curp"));
                member.setCivilStatus(getCivilStatus(resultSet.getString("estado_civil")));
                member.setUvEmail(resultSet.getString("email"));
                member.setBirthDate(DateFormatter.convertSQLDateToUtilDate(resultSet.getDate("fecha_nacimiento")));
                member.setAdmissionDate(DateFormatter.convertSQLDateToUtilDate(resultSet.getDate("fecha_ingreso")));
                member.setAppointment(resultSet.getString("nombramiento"));
                member.setHomeTelephone(resultSet.getString("telefono_casa"));
                member.setWorkTelephone(resultSet.getString("telefono_trabajo"));
                member.setAditionalEmail(resultSet.getString("correo_adicional"));
                member.setStudyArea(resultSet.getString("area_estudio"));
                member.setMaxStudyGrade(getStudyGradeType(resultSet.getString("grado_estudios")));
                member.setActivityStateMember(getActivityStateMember(resultSet.getString("estado_actividad")));
            }
        }
        return member;
    }
    /***
     * Get member by personal number
     * @param personalNumber the member's id.
     * @return int that represents the member's id.
     */
    public int getMemberIdByPersonalNumber(String personalNumber) throws SQLException {
        int idMember = -1;
        try(Connection conn = database.getConnection()) {
           String statement = "SELECT id FROM Miembro WHERE numero_personal = ?";
           PreparedStatement preparedStatement = conn.prepareStatement(statement);
           preparedStatement.setString(1, personalNumber);
           ResultSet resultSet = preparedStatement.executeQuery();
           if(resultSet.next()) {
               idMember = resultSet.getInt(1);
           }
        }
        return idMember;
    }
    /***
     * Get all the study grades
     * <p>
     * Get all the study grades like PRIMARY, SECONDARY, HIGH SCHOOL...
     * </p>
     * @return List that contain all the available Study grades;
     */
    @Override
    public List<StudyGrade> getStudyGrades() throws SQLException {
        List<StudyGrade> studyGradeList = null;
        try(Connection conn = database.getConnection()) {
            String statement = "SELECT * FROM GradoEstudios";
            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            ResultSet resultSet = preparedStatement.executeQuery();
            studyGradeList = new ArrayList<>();
            while(resultSet.next()) {
                studyGradeList.add(getStudyGradeType(resultSet.getString("grado_estudios")));
            }
        }
        return studyGradeList;
    }
    /***
     * Check if Member already exists in database.
     * <p>
     * From a Personal Number check if member exist in database
     * </p>
     * @param personalNumber the string code.
     * @return true if member already existe in database otherwise false.
     */
    @Override
    public boolean checkMember(String personalNumber) throws SQLException {
        boolean memberAlreadyExist = false;
        try(Connection conn = database.getConnection()) {
            String statement = "SELECT * FROM Miembro WHERE numero_personal = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, personalNumber);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                memberAlreadyExist = true;
            }
        }
        return memberAlreadyExist;
    }
    /***
     * Check if Member email already exists in database.
     * <p>
     * From a UV Email check if member exist in database
     * </p>
     * @param emailUV the string code.
     * @param id member id.
     * @return true if member already existe in database otherwise false.
     */
    @Override
    public boolean checkMemberByEmail(String emailUV, int id) throws SQLException {
        boolean memberAlreadyExist = false;
        try(Connection conn = database.getConnection()) {
            String statement = "SELECT * FROM Miembro WHERE email = ? and id != ?";
            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, emailUV);
            preparedStatement.setInt(2, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                memberAlreadyExist = true;
            }
        }
        return memberAlreadyExist;
    }
    /***
     * Get all educational Programs.
     * <p>
     *  Get a list with all registered educational program in database.
     * </p>
     * @return List that contains strings of educational program.
     */
    @Override
    public List<String> getAllEducationProgram() throws SQLException {
        List<String> educationalProgramList = null;
        try(Connection conn = database.getConnection()) {
            String statement = "SELECT * FROM ProgramaEducativo ORDER BY programa_educativo ASC";
            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            ResultSet resultSet = preparedStatement.executeQuery();
            educationalProgramList = new ArrayList<>();
            while(resultSet.next()) {
                educationalProgramList.add(resultSet.getString(1));
            }
        }
        return educationalProgramList;
    }

    /***
     * Change the member activity status
     * @param idMember the member's id
     * @param activityStateMember the actual activity state of member.
     * @return true if it was updated otherwise false.
     */
    @Override
    public boolean changeActivityState(int idMember, ActivityStateMember activityStateMember) throws SQLException {
        boolean isUpdated = false;
        try (Connection conn = database.getConnection()) {
            String statementUpdate = "UPDATE Miembro SET estado_actividad = " + (activityStateMember == ActivityStateMember.ACTIVE ? "2" : "1")  + " WHERE id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(statementUpdate);
            preparedStatement.setInt(1, idMember);
            int rowsAffected = preparedStatement.executeUpdate();
            isUpdated = rowsAffected > 0;
        }
        return isUpdated;
    }

    private StudyGrade getStudyGradeType(String studyGradeType) {
        for(StudyGrade studyGrade : StudyGrade.values() ) {
            if(studyGradeType.equalsIgnoreCase(studyGrade.getStudyGrade())) {
                return studyGrade;
            }
        }
        return null;
    }

    private ActivityStateMember getActivityStateMember(String type) {
        for(ActivityStateMember activityStateMember : ActivityStateMember.values() ) {
            if(type.equalsIgnoreCase(activityStateMember.getActivityState())) {
                return activityStateMember;
            }
        }
        return null;
    }

    private ParticipationType getParticipationType(String type) {
        for(ParticipationType participationType : ParticipationType.values()) {
            if(type.equalsIgnoreCase(participationType.getParticipationType())){
                return participationType;
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

    private boolean updateActivityStateMember(int idMember ) throws SQLException {
        boolean isUpdated = false;
        try (Connection conn = database.getConnection()) {
            String statementUpdate = "UPDATE Miembro SET estado_actividad = 2 WHERE id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(statementUpdate);
            preparedStatement.setInt(1, idMember);
            int rowsAffected = preparedStatement.executeUpdate();
            isUpdated = rowsAffected > 0;
        }
        return isUpdated;
    }

}
