package model.dao;

import model.dao.interfaces.IMiembroDAO;
import model.domain.CivilStatus;
import model.domain.Colaborator;
import model.domain.Integrant;
import model.domain.Member;
import model.domain.ParticipationType;
import model.domain.Responsable;
import utils.Database;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class MiembroDAO implements IMiembroDAO {
    private final Database database;

    public MiembroDAO() {
        this.database = new Database();
    }

    /***
     * Add Integrant to database
     * <p>
     * Add an Member as Integrant to database, include an Access Account.
     * </p>
     * @param Integrant The member of the Academic Group Program
     * @param String the account password.
     * @return id representing the user's id in database.
     */
    @Override
    public int addMember(Integrant integrant, String password) throws SQLException {
        int idMember = -1;
        try(Connection conn = database.getConnection() ) {
            conn.setAutoCommit(false);
            String statement = "CALL agregarIntegrante(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            CallableStatement callableStatement = conn.prepareCall(statement);
            callableStatement.setString(1,integrant.getName());
            callableStatement.setString(2, integrant.getPaternalLastname());
            callableStatement.setString(3, integrant.getMaternalLastname());
            callableStatement.setString(4, integrant.getNationality());
            callableStatement.setString(5, integrant.getEducationalProgram());
            callableStatement.setString(6, integrant.getPersonalNumber());
            callableStatement.setString(7, integrant.getUvEmail());
            callableStatement.setString(8, integrant.getRfc());
            callableStatement.setString(9, integrant.getTelephone());
            callableStatement.setString(10, integrant.getState());
            callableStatement.setString(11, integrant.getCurp());
            callableStatement.setString(12, integrant.getCivilStatus().getCivilStatus());
            callableStatement.setString(13, integrant.getAppointment());
            callableStatement.setString(14, integrant.getHomeTelephone());
            callableStatement.setString(15, integrant.getWorkTelephone());
            callableStatement.setString(16, integrant.getAditionalEmail());
            callableStatement.setString(17, password);
            callableStatement.registerOutParameter(18, Types.INTEGER);
            callableStatement.execute();
            idMember = callableStatement.getInt(18);
        }
        return idMember;
    }
    /***
     * Add Responsable to database
     * <p>
     * Add an Member as Responsalbe to database, include an Access Account.
     * </p>
     * @param Responsable The member of the Academic Group Program
     * @param String the account password.
     * @return id representing the user's id in database.
     */
    @Override
    public int addMember(Responsable responsable, String password) throws SQLException {
        int idMember = -1;
        try(Connection conn = database.getConnection() ) {
            conn.setAutoCommit(false);
            String statement = "CALL agregarResponsable(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            CallableStatement callableStatement = conn.prepareCall(statement);
            callableStatement.setString(1,responsable.getName());
            callableStatement.setString(2,responsable.getPaternalLastname());
            callableStatement.setString(3, responsable.getMaternalLastname());
            callableStatement.setString(4, responsable.getNationality());
            callableStatement.setString(5, responsable.getEducationalProgram());
            callableStatement.setString(6, responsable.getPersonalNumber());
            callableStatement.setString(7, responsable.getUvEmail());
            callableStatement.setString(8, responsable.getRfc());
            callableStatement.setString(9, responsable.getTelephone());
            callableStatement.setString(10, responsable.getState());
            callableStatement.setString(11, responsable.getCurp());
            callableStatement.setString(12, responsable.getCivilStatus().getCivilStatus());
            callableStatement.setString(13, responsable.getAppointment());
            callableStatement.setString(14, responsable.getHomeTelephone());
            callableStatement.setString(15, responsable.getWorkTelephone());
            callableStatement.setString(16, responsable.getAditionalEmail());
            callableStatement.setString(17, password);
            callableStatement.registerOutParameter(18, Types.INTEGER);
            callableStatement.execute();
            idMember = callableStatement.getInt(18);
        }
        return idMember;
    }
    @Override
    public int addMember(Colaborator colaborator) {
        return 0;
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
    public List<Member> getMembers() throws SQLException {
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
                member.setState(resultSet.getString("estado"));
                member.setCurp(resultSet.getString("curp"));
                member.setCivilStatus(getCivilStatus(resultSet.getString("estado_civil")));
                member.setUvEmail(resultSet.getString("email"));
                member.setParticipationType(getParticipationType(resultSet.getString("tipo_participacion")));
                memberList.add(member);
            }
        }
        return memberList;
    }
    /***
     * Get Integrant details from database.
     * <p>
     * Get all the details from Integrant.
     * </p>
     * @param id The member ID.
     * @return Integrant that contains some details.
     */
    @Override
    public Integrant getIntegrantDetails(int id) throws SQLException {
        Integrant integrant = null;
        try(Connection conn = database.getConnection()) {
            String statement = "SELECT * FROM Integrante WHERE id_miembro = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                integrant = new Integrant();
                getMemberData(integrant,id);
                integrant.setAppointment(resultSet.getString("nombramiento"));
                integrant.setHomeTelephone(resultSet.getString("telefono_casa"));
                integrant.setWorkTelephone(resultSet.getString("telefono_trabajo"));
                integrant.setAditionalEmail(resultSet.getString("correo_adicional"));
            }
        }
        return integrant;
    }
    /***
     * Get Responsable details from database.
     * <p>
     * Get all the details from Responsable.
     * </p>
     * @param id The member ID.
     * @return Responsable that contains some details.
     */
    @Override
    public Responsable getResponsableDetails(int id) throws SQLException {
        Responsable responsable = null;
        try(Connection conn = database.getConnection()) {
            String statement = "SELECT * FROM Responsable WHERE id_miembro = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                responsable = new Responsable();
                getMemberData(responsable, id);
                responsable.setAppointment(resultSet.getString("nombramiento"));
                responsable.setHomeTelephone(resultSet.getString("telefono_casa"));
                responsable.setWorkTelephone(resultSet.getString("telefono_trabajo"));
                responsable.setAditionalEmail(resultSet.getString("correo_adicional"));
            }
        }
        return responsable;
    }

    private void getMemberData(Member member, int id) throws SQLException {
        try (Connection conn = database.getConnection()) {
            String statement = "SELECT * FROM Miembro WHERE id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                member.setId(resultSet.getInt("id"));
                member.setName(resultSet.getString("nombre"));
                member.setPaternalLastname(resultSet.getString("apellido_paterno"));
                member.setMaternalLastname(resultSet.getString("apellido_materno"));
                member.setNationality(resultSet.getString("nacionalidad"));
                member.setEducationalProgram(resultSet.getString("programa_educativo"));
                member.setPersonalNumber(resultSet.getString("numero_personal"));
                member.setRfc(resultSet.getString("rfc"));
                member.setTelephone(resultSet.getString("telefono"));
                member.setState(resultSet.getString("estado"));
                member.setCurp(resultSet.getString("curp"));
                member.setCivilStatus(getCivilStatus(resultSet.getString("estado_civil")));
                member.setUvEmail(resultSet.getString("email"));
                member.setParticipationType(getParticipationType(resultSet.getString("tipo_participacion")));
            }
        }
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
}
