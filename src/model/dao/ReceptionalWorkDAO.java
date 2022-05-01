package model.dao;

import assets.utils.Database;
import assets.utils.DateFormatter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.dao.interfaces.IReceptionalWorkDAO;
import model.domain.Member;
import model.domain.Modality;
import model.domain.ReceptionalWork;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReceptionalWorkDAO implements IReceptionalWorkDAO {

    private final Database databaseConection;

    public ReceptionalWorkDAO() {
        this.databaseConection = new Database();
    }

    /***
     * Get receptionals works to database
     * <p>
     * Get all the receptionals works
     * </p>
     * @return List that contain all the registered receptionals works.
     */

    @Override
    public ObservableList<ReceptionalWork> getReceptionalWorksList(String idAcademicBody) throws SQLException {
        ObservableList<ReceptionalWork> receptionalWorksList;
        receptionalWorksList = FXCollections.observableArrayList();

        try(Connection conn = databaseConection.getConnection()) {
            String statement = "SELECT * FROM TrabajoRecepcional INNER JOIN ProyectoInvestigacion ON TrabajoRecepcional.id_proyecto_investigacion" +
                    " = ProyectoInvestigacion.id INNER JOIN LGACProyectoInvestigacion ON  ProyectoInvestigacion.id = " +
                    "LGACProyectoInvestigacion.id_proyecto_investigacion INNER JOIN LGAC ON LGACProyectoInvestigacion.id_lgac = LGAC.id " +
                    "where LGAC.id_programa_cuerpo_academico = ?";

            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, idAcademicBody);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                ReceptionalWork receptionalWorkDataTable = new ReceptionalWork();
                receptionalWorkDataTable.setIdReceptionalWork(resultSet.getInt("TrabajoRecepcional.id"));
                receptionalWorkDataTable.setDescription(resultSet.getString("TrabajoRecepcional.descripcion"));
                receptionalWorkDataTable.setStimatedDurationInMonths(resultSet.getInt("TrabajoRecepcional.duracion_estimada_meses"));
                receptionalWorkDataTable.setStatus(resultSet.getString("TrabajoRecepcional.estado"));
                receptionalWorkDataTable.setParticipants(resultSet.getInt("TrabajoRecepcional.participantes"));
                receptionalWorkDataTable.setRequeriments(resultSet.getString("TrabajoRecepcional.requisitos"));

                receptionalWorkDataTable.setEndDate(DateFormatter.convertSQLDateToUtilDate(resultSet.getDate("TrabajoRecepcional.fecha_entrega")));
                receptionalWorkDataTable.setEndDateString(DateFormatter.getParseDate(receptionalWorkDataTable.getEndDate()));


                receptionalWorkDataTable.setModality(Modality.valueOf(resultSet.getString("TrabajoRecepcional.modalidad")));
                receptionalWorkDataTable.setNameReceptionalWork(resultSet.getString("TrabajoRecepcional.nombre"));
                receptionalWorkDataTable.setNameProject(resultSet.getString("ProyectoInvestigacion.nombre"));

                receptionalWorkDataTable.setRegister(resultSet.getDate("TrabajoRecepcional.registro"));
                receptionalWorkDataTable.setRegisterString(DateFormatter.getParseDate(receptionalWorkDataTable.getRegister()));

                if(receptionalWorkDataTable.getStatus().equals("En proceso")){
                    receptionalWorkDataTable.setEndDateString("Pendiente");
                }

                MemberDAO memberDAO = new MemberDAO();

                receptionalWorkDataTable.setIdDirector(resultSet.getInt("director"));
                Member director = memberDAO.getMember(receptionalWorkDataTable.getIdDirector());
                receptionalWorkDataTable.setDirector(director.getFullName());

                receptionalWorkDataTable.setIdCodirector(resultSet.getInt("codirector"));
                Member codirector = memberDAO.getMember(receptionalWorkDataTable.getIdCodirector());
                receptionalWorkDataTable.setCodirector(codirector.getFullName());

                receptionalWorksList.add(receptionalWorkDataTable);

            }

        }
        databaseConection.disconnect();
        return receptionalWorksList;
    }

    /***
     * Add receptionals works to database
     * <p>
     * Add receptionals works
     * </p>
     * @return boolean that result of register
     */

    @Override
    public boolean addReceptionalWork(ReceptionalWork newReceptionalWork) throws SQLException {
        boolean wasAdded = false;

        try(Connection conn = databaseConection.getConnection() ) {
            conn.setAutoCommit(false);
            String statement = "INSERT INTO TrabajoRecepcional(descripcion, duracion_estimada_meses," +
                    " estado, fecha_entrega, modalidad, nombre, registro, id_proyecto_investigacion, director, codirector, participantes, requisitos) " +
                    "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(statement);

            preparedStatement.setString(1, newReceptionalWork.getDescription());
            preparedStatement.setInt(2, newReceptionalWork.getStimatedDurationInMonths());
            preparedStatement.setString(3, newReceptionalWork.getStatus());

            preparedStatement.setDate(4, DateFormatter.convertUtilDateToSQLDate(newReceptionalWork.getEndDate()));
            preparedStatement.setString(5, newReceptionalWork.getModality().getModality());
            preparedStatement.setString(6, newReceptionalWork.getNameReceptionalWork());
            preparedStatement.setDate(7, DateFormatter.convertUtilDateToSQLDate(newReceptionalWork.getRegister()));
            ProjectDAO projectDAO = new ProjectDAO();
            preparedStatement.setInt(8, projectDAO.getIdProject(newReceptionalWork.getNameProject()));
            preparedStatement.setInt(9, newReceptionalWork.getIdDirector());
            preparedStatement.setInt(10, newReceptionalWork.getIdCodirector());
            preparedStatement.setInt(11, newReceptionalWork.getParticipants());
            preparedStatement.setString(12, newReceptionalWork.getRequeriments());

            wasAdded = preparedStatement.executeUpdate() > 0;

            conn.commit();

        }
        databaseConection.disconnect();

        return wasAdded;
    }

    /***
     * Get id receptional work to database
     * <p>
     * Get id from a receptional work
     * </p>
     * @return boolean that result of register
     */

    @Override
    public int getIdReceptionalWork(String nameReceptionalWork) throws SQLException {
        int idReceptionalWork = 0;

        try(Connection conn = databaseConection.getConnection()) {
            String statement = "SELECT id FROM TrabajoRecepcional WHERE nombre = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, nameReceptionalWork);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                idReceptionalWork = resultSet.getInt("id");
            }
        }
        databaseConection.disconnect();
        return idReceptionalWork;
    }

    /***
     * Get receptional work to database
     * <p>
     * Get details receptional work
     * </p>
     * @return ReceptionalWork from database
     */

    @Override
    public ReceptionalWork getReceptionalWorkDetails(int idReceptionalWork) throws SQLException {
        ReceptionalWork receptionalWorkDetails = new ReceptionalWork();
        try(Connection conn = databaseConection.getConnection()) {
            String statement = "SELECT * FROM TrabajoRecepcional WHERE id = ?";

            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setInt(1, idReceptionalWork);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){

                receptionalWorkDetails.setIdReceptionalWork(resultSet.getInt("id"));
                receptionalWorkDetails.setDescription(resultSet.getString("descripcion"));
                receptionalWorkDetails.setStimatedDurationInMonths(resultSet.getInt("duracion_estimada_meses"));
                receptionalWorkDetails.setStatus(resultSet.getString("estado"));
                receptionalWorkDetails.setParticipants(resultSet.getInt("participantes"));

                receptionalWorkDetails.setEndDate(DateFormatter.convertSQLDateToUtilDate(resultSet.getDate("fecha_entrega")));
                receptionalWorkDetails.setEndDateString(DateFormatter.getParseDate(receptionalWorkDetails.getEndDate()));
                receptionalWorkDetails.setRequeriments(resultSet.getString("requisitos"));

                receptionalWorkDetails.setModality(Modality.valueOf(resultSet.getString("modalidad")));
                receptionalWorkDetails.setNameReceptionalWork(resultSet.getString("nombre"));

                receptionalWorkDetails.setRegister(DateFormatter.convertSQLDateToUtilDate(resultSet.getDate("registro")));
                receptionalWorkDetails.setRegisterString(DateFormatter.getParseDate(receptionalWorkDetails.getRegister()));

                if(receptionalWorkDetails.getEndDate().equals(null)){

                    receptionalWorkDetails.setEndDate(null);
                    receptionalWorkDetails.setEndDateString("Pendiente");
                }

                MemberDAO memberDAO = new MemberDAO();

                receptionalWorkDetails.setIdDirector(resultSet.getInt("director"));
                Member director = memberDAO.getMember(receptionalWorkDetails.getIdDirector());
                receptionalWorkDetails.setDirector(director.getFullName());

                receptionalWorkDetails.setIdCodirector(resultSet.getInt("codirector"));
                Member codirector = memberDAO.getMember(receptionalWorkDetails.getIdCodirector());
                receptionalWorkDetails.setCodirector(codirector.getFullName());

            }

        }
        databaseConection.disconnect();

        return receptionalWorkDetails;
    }

    /***
     * Check if exist receptional work
     * <p>
     * Check if exist receptional work in database
     * </p>
     * @return boolean true if receptional work exist in database
     */

    @Override
    public boolean checkReceptionalWork(String nameReceptionalWork) throws SQLException {
        boolean receptionalWorkAlreadyExist = false;
        try(Connection conn = databaseConection.getConnection()) {
            String statement = "SELECT * FROM TrabajoRecepcional WHERE nombre = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, nameReceptionalWork);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {

                receptionalWorkAlreadyExist = true;
            }
            databaseConection.disconnect();

        }
        return receptionalWorkAlreadyExist;
    }

    /***
     * Check if exist receptional work
     * <p>
     * Check if exist receptional work in database for update
     * </p>
     * @return boolean true if receptional work exist in database
     */

    @Override
    public boolean checkReceptionalWorkUpdated(String nameReceptionalWork, int idReceptionalWork) throws SQLException {
        boolean receptionalWorkAlreadyExist = false;
        try(Connection conn = databaseConection.getConnection()) {
            String statement = "SELECT * FROM TrabajoRecepcional WHERE nombre = ? AND id != ?";
            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, nameReceptionalWork);
            preparedStatement.setInt(2, idReceptionalWork);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                receptionalWorkAlreadyExist = true;
            }
            databaseConection.disconnect();
        }
        return receptionalWorkAlreadyExist;
    }

    @Override
    public boolean updateReceptionalWork(ReceptionalWork updateReceptionalWork) throws SQLException {
        boolean wasUpdated = false;

        try(Connection conn = databaseConection.getConnection() ) {
            System.out.println("Hay un problema llegando aquí");
            int rowsAffected = 0;
            conn.setAutoCommit(false);
            String statement = "UPDATE TrabajoRecepcional SET descripcion = ?, duracion_estimada_meses = ?, " +
                    "estado = ?, fecha_entrega = ?, modalidad = ?, nombre = ?, participantes = ?," +
                    " requisitos = ? WHERE id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(statement);

            preparedStatement.setString(1, updateReceptionalWork.getDescription());
            preparedStatement.setInt(2, updateReceptionalWork.getStimatedDurationInMonths());
            preparedStatement.setString(3, updateReceptionalWork.getStatus());

            preparedStatement.setDate(4, DateFormatter.convertUtilDateToSQLDate(updateReceptionalWork.getEndDate()));
            preparedStatement.setString(5, updateReceptionalWork.getModality().getModality());
            preparedStatement.setString(6, updateReceptionalWork.getNameReceptionalWork());
            preparedStatement.setInt(7, updateReceptionalWork.getParticipants());
            preparedStatement.setString(8, updateReceptionalWork.getRequeriments());
            preparedStatement.setInt(9, updateReceptionalWork.getIdReceptionalWork());
            rowsAffected = preparedStatement.executeUpdate();
            wasUpdated = rowsAffected > 0;

            conn.commit();

        }
        databaseConection.disconnect();

        return wasUpdated;

    }
}
