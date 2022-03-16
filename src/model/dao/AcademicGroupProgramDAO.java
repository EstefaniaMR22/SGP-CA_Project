package model.dao;

import model.dao.interfaces.IAcademicGroupProgramDAO;
import model.domain.AcademicGroupProgram;
import model.domain.ConsolidationGrade;
import model.domain.LGAC;
import utils.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AcademicGroupProgramDAO implements IAcademicGroupProgramDAO {
    private final Database database;

    public AcademicGroupProgramDAO() {
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
    public boolean addAcademicGroupProgram(AcademicGroupProgram academicGroupProgram) throws SQLException {
        boolean wasAdded = false;
        try (Connection conn = database.getConnection()) {
            conn.setAutoCommit(false);
            String statement = "INSERT INTO ProgramaCuerpoAcademico(id, nombre, vision, objetivo_general, mision, grado_consolidacion, fecha_registro, fecha_ultima_evaluacion, unidad_adscripcion, descripcion_adscripcion) VALUES(?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, academicGroupProgram.getId());
            preparedStatement.setString(2, academicGroupProgram.getName());
            preparedStatement.setString(3, academicGroupProgram.getVision());
            preparedStatement.setString(4, academicGroupProgram.getGeneralObjetive());
            preparedStatement.setString(5, academicGroupProgram.getMission());
            preparedStatement.setString(6, academicGroupProgram.getConsolidationGrade().getConsolidationGrade());
            preparedStatement.setDate(7, new java.sql.Date(academicGroupProgram.getRegisterDate().getTime()));
            preparedStatement.setDate(8, new java.sql.Date(academicGroupProgram.getLastEvaluationDate().getTime()));
            preparedStatement.setString(9, academicGroupProgram.getAdscriptionUnit());
            preparedStatement.setString(10, academicGroupProgram.getDescriptionAdscription());
            wasAdded = preparedStatement.executeUpdate() > 0;
            String statementLgac = "INSERT INTO LGACProgramaCuerpoAcademico(id_programa_cuerpo_academico, id_lgac) VALUES(?,?);";
            for(LGAC lgac : academicGroupProgram.getLgacList() ) {
                preparedStatement = conn.prepareStatement(statementLgac);
                preparedStatement.setString(1, academicGroupProgram.getId());
                preparedStatement.setInt(2, 1);
                preparedStatement.executeUpdate();
            }
            conn.commit();
        }
        return wasAdded;
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


    @Override
    public AcademicGroupProgram getAcademicGroupProgramDetails(String id) throws SQLException {
        AcademicGroupProgram academicGroupProgram = null;
        try (Connection conn = database.getConnection()) {
            String statement = "SELECT * FROM ProgramaCuerpoAcademico WHERE id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                academicGroupProgram = new AcademicGroupProgram();
                academicGroupProgram.setId(resultSet.getString("id"));
                academicGroupProgram.setName(resultSet.getString("nombre"));
                academicGroupProgram.setVision(resultSet.getString("vision"));
                academicGroupProgram.setGeneralObjetive(resultSet.getString("objetivo_general"));
                academicGroupProgram.setMission(resultSet.getString("mision"));
                academicGroupProgram.setConsolidationGrade(getConsolidationGrade(resultSet.getString("grado_consolidacion")));
                academicGroupProgram.setRegisterDate(new java.util.Date(resultSet.getDate("fecha_registro").getTime()));
                academicGroupProgram.setLastEvaluationDate(new java.util.Date(resultSet.getDate("fecha_ultima_evaluacion").getTime()));
                academicGroupProgram.setAdscriptionUnit(resultSet.getString("unidad_adscripcion"));
                academicGroupProgram.setDescriptionAdscription(resultSet.getString("descripcion_adscripcion"));
                String lgacStatement = "SELECT LG.identificador, LG.descripcion, LG.id FROM LGAC AS LG INNER JOIN LGACProgramaCuerpoAcademico ON id_lgac = id AND id_programa_cuerpo_academico = ?";
                preparedStatement = conn.prepareStatement(lgacStatement);
                preparedStatement.setString(1, id);
                ResultSet resultSet1 = preparedStatement.executeQuery();
                academicGroupProgram.setLgacList(new ArrayList<>());
                while( resultSet1.next() ) {
                    LGAC lgac = new LGAC();
                    lgac.setDescription(resultSet1.getString("descripcion"));
                    lgac.setIdentification(resultSet1.getString("identificador"));
                    lgac.setId(resultSet1.getInt("id"));
                    academicGroupProgram.getLgacList().add(lgac);
                }
            }
        }
        return academicGroupProgram;
    }

    @Override
    public List<AcademicGroupProgram> getAllAcademicGroupPrograms() throws SQLException {
        List<AcademicGroupProgram> list;
        try (Connection conn = database.getConnection()) {
            String statement = "SELECT * FROM ProgramaCuerpoAcademico";
            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            ResultSet resultSet = preparedStatement.executeQuery();
            list = new ArrayList<>();
            while (resultSet.next()) {
                AcademicGroupProgram academicGroupProgram = new AcademicGroupProgram();
                academicGroupProgram.setId(resultSet.getString("id"));
                academicGroupProgram.setName(resultSet.getString("nombre"));
                academicGroupProgram.setVision(resultSet.getString("vision"));
                academicGroupProgram.setGeneralObjetive(resultSet.getString("objetivo_general"));
                academicGroupProgram.setMission(resultSet.getString("mision"));
                academicGroupProgram.setConsolidationGrade(getConsolidationGrade(resultSet.getString("grado_consolidacion")));
                academicGroupProgram.setRegisterDate(new java.util.Date(resultSet.getDate("fecha_registro").getTime()));
                academicGroupProgram.setLastEvaluationDate(new java.util.Date(resultSet.getDate("fecha_ultima_evaluacion").getTime()));
                academicGroupProgram.setAdscriptionUnit(resultSet.getString("unidad_adscripcion"));
                academicGroupProgram.setDescriptionAdscription(resultSet.getString("descripcion_adscripcion"));
                list.add(academicGroupProgram);
            }
        }
        return list;
    }

    private ConsolidationGrade getConsolidationGrade(String value) {
        for(ConsolidationGrade consolidationGrade : ConsolidationGrade.values()) {
            if(value.equalsIgnoreCase(consolidationGrade.getConsolidationGrade())){
                return consolidationGrade;
            }
        }
        return null;
    }

}

