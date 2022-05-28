package model.dao;

import assets.utils.Database;
import assets.utils.DateFormatter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.domain.Evidence;
import model.domain.Meet;
import model.domain.Member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EvidenceDAO {

    private final Database databaseConection;

    public EvidenceDAO() {
        this.databaseConection = new Database();
    }

    public boolean addEvidence(Evidence newEvidence) throws SQLException {
        boolean wasAdded = false;

        try(Connection conn = databaseConection.getConnection()) {
            conn.setAutoCommit(false);
            String statement = "INSERT INTO Evidencia (fecha_publicacion, titulo, pais, id_responsable, paginas, descripcion," +
                    "editorial, numero_edicion, isbn, referencia, id_proyecto_investigacion, tipo, " +
                    "id_cuerpo_academico, nombre_revista, indice_revista, isnn_revista) " +
                    "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement preparedStatement = conn.prepareStatement(statement);

            preparedStatement.setDate(1, DateFormatter.convertUtilDateToSQLDate(newEvidence.getPublicationDate()));
            preparedStatement.setString(2, newEvidence.getEvidenceTitle());
            preparedStatement.setString(3, newEvidence.getCountry());
            preparedStatement.setInt(4, newEvidence.getIdResponsable());
            preparedStatement.setString(5, newEvidence.getPagesBook());
            preparedStatement.setString(6, newEvidence.getDescriptionEvidence());
            preparedStatement.setString(7, newEvidence.getEditorialBook());
            preparedStatement.setString(8, newEvidence.getEditionNumberBook());
            preparedStatement.setString(9, newEvidence.getIsbnBook());
            preparedStatement.setString(10, newEvidence.getReference());
            preparedStatement.setInt(11, newEvidence.getIdProject());
            preparedStatement.setString(12, newEvidence.getEvidenceType());
            preparedStatement.setString(13, newEvidence.getIdAcademicBody());
            preparedStatement.setString(14, newEvidence.getNameMagazine());
            preparedStatement.setString(15, newEvidence.getIndiceMagazine());
            preparedStatement.setString(16, newEvidence.getIsnnMagazine());


            wasAdded = preparedStatement.executeUpdate() > 0;

            conn.commit();

        }
        databaseConection.disconnect();
        return wasAdded;
    }

    public ObservableList<Evidence> getEvidenceList(String idAcademicBody) throws SQLException {
        ObservableList<Evidence> evidenceObservableList;
        evidenceObservableList = FXCollections.observableArrayList();

        try(Connection conn = databaseConection.getConnection()) {

            String statement = "SELECT * FROM Evidencia WHERE id_cuerpo_academico = ?";

            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, idAcademicBody);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Evidence evidenceData = new Evidence();

                evidenceData.setIdEvidence(resultSet.getInt("id"));
                evidenceData.setPublicationDate(DateFormatter.convertSQLDateToUtilDate(resultSet.getDate("fecha_publicacion")));
                evidenceData.setEvidenceTitle(resultSet.getString("titulo"));
                evidenceData.setCountry(resultSet.getString("pais"));
                evidenceData.setIdResponsable(resultSet.getInt("id_responsable"));
                evidenceData.setPagesBook(resultSet.getString("paginas"));
                evidenceData.setDescriptionEvidence(resultSet.getString("descripcion"));
                evidenceData.setEditorialBook(resultSet.getString("editorial"));
                evidenceData.setEditionNumberBook(resultSet.getString("numero_edicion"));
                evidenceData.setIsbnBook(resultSet.getString("isbn"));
                evidenceData.setReference(resultSet.getString("referencia"));
                evidenceData.setIdProject(resultSet.getInt("id_proyecto_investigacion"));
                evidenceData.setEvidenceType(resultSet.getString("tipo"));
                evidenceData.setIdAcademicBody(resultSet.getString("id_cuerpo_academico"));
                evidenceData.setNameMagazine(resultSet.getString("nombre_revista"));
                evidenceData.setIndiceMagazine(resultSet.getString("indice_revista"));
                evidenceData.setIsnnMagazine(resultSet.getString("isnn_revista"));

                MemberDAO memberDAO = new MemberDAO();
                evidenceData.setResponsableEvidence(memberDAO.getMember(evidenceData.getIdResponsable()).getFullName());

                evidenceObservableList.add(evidenceData);

            }

        }
        databaseConection.disconnect();
        return evidenceObservableList;
    }

    public ObservableList<Evidence> verifyExistBooks(String idAcademicBody) throws SQLException {
        ObservableList<Evidence> evidenceObservableList;
        evidenceObservableList = FXCollections.observableArrayList();

        try(Connection conn = databaseConection.getConnection()) {

            String statement = "SELECT * FROM Evidencia WHERE id_cuerpo_academico = ? AND tipo = ?;";

            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, idAcademicBody);
            preparedStatement.setString(2, "Libro");
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Evidence evidenceData = new Evidence();

                evidenceData.setIdEvidence(resultSet.getInt("id"));
                evidenceData.setPublicationDate(DateFormatter.convertSQLDateToUtilDate(resultSet.getDate("fecha_publicacion")));
                evidenceData.setEvidenceTitle(resultSet.getString("titulo"));
                evidenceData.setCountry(resultSet.getString("pais"));
                evidenceData.setIdResponsable(resultSet.getInt("id_responsable"));
                evidenceData.setPagesBook(resultSet.getString("paginas"));
                evidenceData.setDescriptionEvidence(resultSet.getString("descripcion"));
                evidenceData.setEditorialBook(resultSet.getString("editorial"));
                evidenceData.setEditionNumberBook(resultSet.getString("numero_edicion"));
                evidenceData.setIsbnBook(resultSet.getString("isbn"));
                evidenceData.setReference(resultSet.getString("referencia"));
                evidenceData.setIdProject(resultSet.getInt("id_proyecto_investigacion"));
                evidenceData.setEvidenceType(resultSet.getString("tipo"));
                evidenceData.setIdAcademicBody(resultSet.getString("id_cuerpo_academico"));
                evidenceData.setNameMagazine(resultSet.getString("nombre_revista"));
                evidenceData.setIndiceMagazine(resultSet.getString("indice_revista"));
                evidenceData.setIsnnMagazine(resultSet.getString("isnn_revista"));

                MemberDAO memberDAO = new MemberDAO();
                evidenceData.setResponsableEvidence(memberDAO.getMember(evidenceData.getIdResponsable()).getFullName());

                evidenceObservableList.add(evidenceData);

            }

        }
        databaseConection.disconnect();
        return evidenceObservableList;
    }

    public Evidence getEvidence(int idEvidence) throws SQLException {
        Evidence evidenceData = new Evidence();

        try(Connection conn = databaseConection.getConnection()) {

            String statement = "SELECT * FROM Evidencia WHERE id = ?";

            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setInt(1, idEvidence);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                evidenceData.setPublicationDate(DateFormatter.convertSQLDateToUtilDate(resultSet.getDate("fecha_publicacion")));
                evidenceData.setEvidenceTitle(resultSet.getString("titulo"));
                evidenceData.setCountry(resultSet.getString("pais"));
                evidenceData.setIdResponsable(resultSet.getInt("id_responsable"));
                evidenceData.setPagesBook(resultSet.getString("paginas"));
                evidenceData.setDescriptionEvidence(resultSet.getString("descripcion"));
                evidenceData.setEditorialBook(resultSet.getString("editorial"));
                evidenceData.setEditionNumberBook(resultSet.getString("numero_edicion"));
                evidenceData.setIsbnBook(resultSet.getString("isbn"));
                evidenceData.setReference(resultSet.getString("referencia"));
                evidenceData.setIdProject(resultSet.getInt("id_proyecto_investigacion"));
                evidenceData.setEvidenceType(resultSet.getString("tipo"));
                evidenceData.setIdAcademicBody(resultSet.getString("id_cuerpo_academico"));
                evidenceData.setNameMagazine(resultSet.getString("nombre_revista"));
                evidenceData.setIndiceMagazine(resultSet.getString("indice_revista"));
                evidenceData.setIsnnMagazine(resultSet.getString("isnn_revista"));

                MemberDAO memberDAO = new MemberDAO();
                evidenceData.setResponsableEvidence(memberDAO.getMember(evidenceData.getIdResponsable()).getFullName());


            }

        }
        databaseConection.disconnect();
        return evidenceData;
    }

    public boolean removeEvidence(int idEvidence) throws SQLException {
        boolean isRemoved = false;
        try (Connection conn = databaseConection.getConnection()) {
            String statement = "DELETE FROM Evidencia WHERE id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setInt(1, idEvidence);
            int rowsAffected = preparedStatement.executeUpdate();
            isRemoved = rowsAffected > 0;
        }
        return isRemoved;
    }


    public boolean checkEvidence(String titulo, String tipo) throws SQLException {
        boolean exist = false;
        try(Connection conn = databaseConection.getConnection()) {

            String statement = "SELECT * FROM Evidencia WHERE titulo = ? AND tipo = ?";

            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, titulo);
            preparedStatement.setString(2, tipo);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {
                exist = true;
            }

        }
        databaseConection.disconnect();

        return exist;
    }
}
