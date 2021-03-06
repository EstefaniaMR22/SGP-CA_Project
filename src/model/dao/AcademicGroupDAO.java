package model.dao;

import controller.academicgroup.AddAcademicGroupController;
import model.dao.interfaces.IAcademicGroupDAO;
import assets.utils.Database;
import assets.utils.DateFormatter;
import model.domain.AcademicGroup;
import model.domain.ActivityStateLGAC;
import model.domain.ConsolidationGrade;
import model.domain.LGAC;
import model.domain.Participation;
import model.domain.ParticipationType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class AcademicGroupDAO implements IAcademicGroupDAO {
    private final Database database;

    public AcademicGroupDAO() {
        this.database = new Database();
    }

    /***
     * Add an Academic Group Program to database.
     * <p>
     * Add an AGP to database. This is so important for the activities.
     * </p>
     * @param academicGroupProgram the AGP to register.
     * @return boolean true if it was added to database.
     */
    @Override
    public String addAcademicGroupProgram(AcademicGroup academicGroupProgram) throws SQLException {
        String idAcademicGroup = null;
        try (Connection conn = database.getConnection()) {
            conn.setAutoCommit(false);
            String statement = "INSERT INTO ProgramaCuerpoAcademico(id, nombre, vision, objetivo_general, mision, grado_consolidacion, fecha_registro, fecha_ultima_evaluacion, unidad_adscripcion, descripcion_adscripcion, area_adscripcion) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, academicGroupProgram.getId());
            preparedStatement.setString(2, academicGroupProgram.getName());
            preparedStatement.setString(3, academicGroupProgram.getVision());
            preparedStatement.setString(4, academicGroupProgram.getGeneralObjetive());
            preparedStatement.setString(5, academicGroupProgram.getMission());
            preparedStatement.setString(6, academicGroupProgram.getConsolidationGrade().getConsolidationGrade());
            preparedStatement.setDate(7, DateFormatter.convertUtilDateToSQLDate(academicGroupProgram.getRegisterDate()));
            preparedStatement.setDate(8, DateFormatter.convertUtilDateToSQLDate(academicGroupProgram.getLastEvaluationDate()));
            preparedStatement.setString(9, academicGroupProgram.getAdscriptionUnit());
            preparedStatement.setString(10, academicGroupProgram.getDescriptionAdscription());
            preparedStatement.setString(11, academicGroupProgram.getAdscriptionArea());
            if (preparedStatement.executeUpdate() > 0) {
                String lastInsertStatement = "SELECT id FROM ProgramaCuerpoAcademico WHERE nombre = ? AND vision = ? AND grado_consolidacion = ?";
                preparedStatement = conn.prepareStatement(lastInsertStatement);
                preparedStatement.setString(1, academicGroupProgram.getName());
                preparedStatement.setString(2, academicGroupProgram.getVision());
                preparedStatement.setString(3, academicGroupProgram.getConsolidationGrade().getConsolidationGrade());
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    idAcademicGroup = resultSet.getString(1);
                    // ADD LGACS
                    if (academicGroupProgram.getLgacList() != null) {
                        String statementLgac = "INSERT INTO LGAC(identificador, descripcion, id_programa_cuerpo_academico, estado_actividad) VALUES(?,?,?, 1);";
                        for (LGAC lgac : academicGroupProgram.getLgacList()) {
                            preparedStatement = conn.prepareStatement(statementLgac);
                            preparedStatement.setString(1, lgac.getIdentification());
                            preparedStatement.setString(2, lgac.getDescription());
                            preparedStatement.setString(3, idAcademicGroup);
                            preparedStatement.executeUpdate();
                        }
                    }
                    // ADD MEMBER PARTICIPATION
                    if (academicGroupProgram.getParticipationList() != null) {
                        String statementMember = "INSERT INTO ParticipacionCuerpoAcademicoMiembro(tipo_participacion, id_miembro, id_cuerpo_academico) VALUES(?,?,?)";
                        for (Participation participation : academicGroupProgram.getParticipationList()) {
                            preparedStatement = conn.prepareStatement(statementMember);
                            preparedStatement.setString(1, participation.getParticipationType().getParticipationType());
                            preparedStatement.setInt(2, participation.getMember().getId());
                            preparedStatement.setString(3, idAcademicGroup);
                            preparedStatement.executeUpdate();
                        }
                    }
                }
            }
            conn.commit();
        }
        return idAcademicGroup;
    }

    /***
     * Get all consolidation grades from database
     * <p>
     * Get all the consolidation grades: Consolidated, in consolidation and in formation
     * </p>
     * @return List that contain all consolidation grades
     */
    @Override
    public List<ConsolidationGrade> getConsolidationGrades() throws SQLException {
        List<ConsolidationGrade> list;
        try (Connection conn = database.getConnection()) {
            String statement = "SELECT * FROM GradoConsolidacion";
            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            ResultSet resultSet = preparedStatement.executeQuery();
            list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(getConsolidationGrade(resultSet.getString("grado_consolidacion")));
            }
        }
        return list;
    }

    /***
     * Get all details from Academic Group.
     * <p>
     * Get details from Academic Group..
     * The details are Members and LGAS assigned to AG.
     * </p>
     * @param id The id of Academic Group.
     * @return List that contain all Academic Group.
     */
    @Override
    public AcademicGroup getAcademicGroupProgramDetails(String id) throws SQLException {
        AcademicGroup academicGroupProgram = null;
        try (Connection conn = database.getConnection()) {
            String statement = "SELECT * FROM ProgramaCuerpoAcademico WHERE id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                academicGroupProgram = new AcademicGroup();
                academicGroupProgram.setId(resultSet.getString("id"));
                academicGroupProgram.setName(resultSet.getString("nombre"));
                academicGroupProgram.setVision(resultSet.getString("vision"));
                academicGroupProgram.setGeneralObjetive(resultSet.getString("objetivo_general"));
                academicGroupProgram.setMission(resultSet.getString("mision"));
                academicGroupProgram.setConsolidationGrade(getConsolidationGrade(resultSet.getString("grado_consolidacion")));
                academicGroupProgram.setRegisterDate(DateFormatter.convertSQLDateToUtilDate(resultSet.getDate("fecha_registro")));
                academicGroupProgram.setLastEvaluationDate(DateFormatter.convertSQLDateToUtilDate(resultSet.getDate("fecha_ultima_evaluacion")));
                academicGroupProgram.setAdscriptionUnit(resultSet.getString("unidad_adscripcion"));
                academicGroupProgram.setDescriptionAdscription(resultSet.getString("descripcion_adscripcion"));
                academicGroupProgram.setAdscriptionArea(resultSet.getString("area_adscripcion"));
                String lgacStatement = "SELECT LG.identificador, LG.descripcion, LG.id, LG.estado_actividad FROM LGAC AS LG WHERE id_programa_cuerpo_academico = ?";
                preparedStatement = conn.prepareStatement(lgacStatement);
                preparedStatement.setString(1, id);
                ResultSet resultSet1 = preparedStatement.executeQuery();
                List<LGAC> lgacList = new ArrayList<>();
                while (resultSet1.next()) {
                    LGAC lgac = new LGAC();
                    lgac.setDescription(resultSet1.getString("descripcion"));
                    lgac.setIdentification(resultSet1.getString("identificador"));
                    lgac.setId(resultSet1.getInt("id"));
                    lgac.setActivityState(getActivityStateLagc(resultSet1.getString("estado_actividad")));
                    lgacList.add(lgac);
                }
                if (!lgacList.isEmpty()) {
                    academicGroupProgram.setLgacList(lgacList);
                }
                String memberStatement = "SELECT MIE.id, PCAM.tipo_participacion FROM Miembro AS MIE INNER JOIN ParticipacionCuerpoAcademicoMiembro AS PCAM ON MIE.id = PCAM.id_miembro INNER JOIN ProgramaCuerpoAcademico AS PCA ON PCAM.id_cuerpo_academico = PCA.id AND PCAM.id_cuerpo_academico = ?";
                preparedStatement = conn.prepareStatement(memberStatement);
                preparedStatement.setString(1, id);
                ResultSet resultSet2 = preparedStatement.executeQuery();
                List<Participation> participationList = new ArrayList<>();
                while (resultSet2.next()) {
                    Participation participation = new Participation();
                    participation.setParticipationType(getParticipationType(resultSet2.getString("PCAM.tipo_participacion")));
                    participation.setMember(new MemberDAO().getMember(resultSet2.getInt("MIE.id")));
                    participationList.add(participation);
                }
                if (!participationList.isEmpty()) {
                    academicGroupProgram.setParticipationList(participationList);
                }
            }
        }
        return academicGroupProgram;
    }

    /***
     * Get all Academic Group Programs
     * <p>
     * Get all the Academic Group Programs with basic information.
     * </p>
     * @return List that contain all Academic Group Programs
     */
    @Override
    public List<AcademicGroup> getAllAcademicGroup() throws SQLException {
        List<AcademicGroup> list;
        try (Connection conn = database.getConnection()) {
            String statement = "SELECT * FROM ProgramaCuerpoAcademico";
            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            ResultSet resultSet = preparedStatement.executeQuery();
            list = new ArrayList<>();
            while (resultSet.next()) {
                AcademicGroup academicGroupProgram = new AcademicGroup();
                academicGroupProgram.setId(resultSet.getString("id"));
                academicGroupProgram.setName(resultSet.getString("nombre"));
                academicGroupProgram.setVision(resultSet.getString("vision"));
                academicGroupProgram.setGeneralObjetive(resultSet.getString("objetivo_general"));
                academicGroupProgram.setMission(resultSet.getString("mision"));
                academicGroupProgram.setConsolidationGrade(getConsolidationGrade(resultSet.getString("grado_consolidacion")));
                academicGroupProgram.setRegisterDate(DateFormatter.convertSQLDateToUtilDate(resultSet.getDate("fecha_registro")));
                academicGroupProgram.setLastEvaluationDate(DateFormatter.convertUtilDateToSQLDate(resultSet.getDate("fecha_ultima_evaluacion")));
                academicGroupProgram.setAdscriptionUnit(resultSet.getString("unidad_adscripcion"));
                academicGroupProgram.setDescriptionAdscription(resultSet.getString("descripcion_adscripcion"));
                list.add(academicGroupProgram);
            }
        }
        return list;
    }

    /***
     * Update a registered Academic Group.
     * <p>
     *     Including his LGACS and Members.
     * </p>
     * @param academicGroupProgram the new data to be registered in database.
     * @return true if it was registered otherwise false.
     */
    @Override
    public boolean updateAcademicGroup(AcademicGroup academicGroupProgram) throws SQLException {
        boolean isUpdated = false;
        int rowsAffected = 0;
        try (Connection conn = database.getConnection()) {
            conn.setAutoCommit(false);
            String statement = "UPDATE ProgramaCuerpoAcademico SET nombre = ?, vision = ?, objetivo_general = ?, mision = ?, grado_consolidacion = ?, fecha_registro = ?, fecha_ultima_evaluacion = ?, unidad_adscripcion = ?, descripcion_adscripcion = ?, area_adscripcion = ? WHERE id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, academicGroupProgram.getName());
            preparedStatement.setString(2, academicGroupProgram.getVision());
            preparedStatement.setString(3, academicGroupProgram.getGeneralObjetive());
            preparedStatement.setString(4, academicGroupProgram.getMission());
            preparedStatement.setString(5, academicGroupProgram.getConsolidationGrade().getConsolidationGrade());
            preparedStatement.setDate(6, new java.sql.Date(academicGroupProgram.getRegisterDate().getTime()));
            preparedStatement.setDate(7, new java.sql.Date(academicGroupProgram.getLastEvaluationDate().getTime()));
            preparedStatement.setString(8, academicGroupProgram.getAdscriptionUnit());
            preparedStatement.setString(9, academicGroupProgram.getDescriptionAdscription());
            preparedStatement.setString(10, academicGroupProgram.getAdscriptionArea());
            preparedStatement.setString(11, academicGroupProgram.getId());
            rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                List<LGAC> oldLGACList = getAcademicGroupProgramDetails(academicGroupProgram.getId()).getLgacList();
                oldLGACList.forEach(x -> {
                    LGAC isPresent = academicGroupProgram.getLgacList().stream().filter(e -> e.getIdentification().equals(x.getIdentification())).findAny().orElse(null);
                    if (isPresent != null) {
                        if (isPresent.getActivityState() != x.getActivityState()) {
                            try {
                                new LgacDAO().updateActivityState(x.getIdentification(), academicGroupProgram.getId(), isPresent.getActivityState());
                            } catch (SQLException e) {
                                Logger.getLogger(AcademicGroupDAO.class.getName()).log(Level.SEVERE, null, e);
                            }
                        }
                    } else {
                        try {
                            new LgacDAO().removeLgac(x.getId());
                        } catch (SQLException e) {
                            Logger.getLogger(AcademicGroupDAO.class.getName()).log(Level.SEVERE, null, e);
                        }
                    }
                });
//

                academicGroupProgram.getLgacList().forEach(x -> {
                    if(oldLGACList.stream().noneMatch(e -> x.getIdentification().equals(e.getIdentification()))) {
                        try {
                            String statementlgac = "INSERT INTO LGAC(identificador, descripcion, id_programa_cuerpo_academico, estado_actividad) VALUES(?,?,?,1)";
                            PreparedStatement preparedStatementlgac = conn.prepareStatement(statementlgac);
                            preparedStatementlgac.setString(1, x.getIdentification());
                            preparedStatementlgac.setString(2, x.getDescription());
                            preparedStatementlgac.setString(3, academicGroupProgram.getId());
                            preparedStatementlgac.executeUpdate();
                        } catch (SQLException e) {
                            Logger.getLogger(AcademicGroupDAO.class.getName()).log(Level.SEVERE, null, e);
                        }
                    }
                });

                // DELETE Participations
                String statementDeleteParticipation = "DELETE FROM ParticipacionCuerpoAcademicoMiembro WHERE id_cuerpo_academico = ?";
                preparedStatement = conn.prepareStatement(statementDeleteParticipation);
                preparedStatement.setString(1, academicGroupProgram.getId());
                rowsAffected += preparedStatement.executeUpdate();
                // ADD MEMBER PARTICIPATION
                if (academicGroupProgram.getParticipationList() != null) {
                    String statementMember = "INSERT INTO ParticipacionCuerpoAcademicoMiembro(tipo_participacion, id_miembro, id_cuerpo_academico) VALUES(?,?,?)";
                    for (Participation participation : academicGroupProgram.getParticipationList()) {
                        preparedStatement = conn.prepareStatement(statementMember);
                        preparedStatement.setString(1, participation.getParticipationType().getParticipationType());
                        preparedStatement.setInt(2, participation.getMember().getId());
                        preparedStatement.setString(3, academicGroupProgram.getId());
                        rowsAffected += preparedStatement.executeUpdate();
                    }
                }
            }
            conn.commit();
            isUpdated = rowsAffected > 0;
        }
        return isUpdated;
    }

    /***
     * Remove Academic Group.
     * @param academicGroupId the id of academic group
     * @return true if it was removed otherwise false.
     */
    @Override
    public boolean removeAcademicGroup(String academicGroupId) throws SQLException {
        boolean isRemoved = false;
        try (Connection conn = database.getConnection()) {
            conn.setAutoCommit(false);
            // DELETE LGAC
            String statement = "DELETE FROM LGAC WHERE id_programa_cuerpo_academico = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, academicGroupId);
            preparedStatement.executeUpdate();
            // DELETE Participations
            String statementDeleteParticipation = "DELETE FROM ParticipacionCuerpoAcademicoMiembro WHERE id_cuerpo_academico = ?";
            preparedStatement = conn.prepareStatement(statementDeleteParticipation);
            preparedStatement.setString(1, academicGroupId);
            preparedStatement.executeUpdate();
            // Delete Academic Group
            String statementAG = "DELETE FROM ProgramaCuerpoAcademico WHERE id = ?";
            preparedStatement = conn.prepareStatement(statementAG);
            preparedStatement.setString(1, academicGroupId);
            isRemoved = preparedStatement.executeUpdate() > 0;
            conn.commit();
        }
        return isRemoved;
    }

    private ConsolidationGrade getConsolidationGrade(String value) {
        for (ConsolidationGrade consolidationGrade : ConsolidationGrade.values()) {
            if (value.equalsIgnoreCase(consolidationGrade.getConsolidationGrade())) {
                return consolidationGrade;
            }
        }
        return null;
    }

    private ParticipationType getParticipationType(String type) {
        for (ParticipationType participationType : ParticipationType.values()) {
            if (type.equalsIgnoreCase(participationType.getParticipationType())) {
                return participationType;
            }
        }
        return null;
    }

    private ActivityStateLGAC getActivityStateLagc(String type) {
        for (ActivityStateLGAC activityStateLGAC : ActivityStateLGAC.values()) {
            if (type.equalsIgnoreCase(activityStateLGAC.getActivityState())) {
                return activityStateLGAC;
            }
        }
        return null;
    }
}

