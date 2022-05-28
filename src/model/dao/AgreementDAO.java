package model.dao;

import assets.utils.Database;
import assets.utils.DateFormatter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.domain.Agreement;
import model.domain.Meet;
import model.domain.Member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AgreementDAO {

    private final Database databaseConection;

    public AgreementDAO() {
        this.databaseConection = new Database();
    }

    public boolean addMeet(Agreement newAgreement) throws SQLException {
        boolean wasAdded = false;

        try(Connection conn = databaseConection.getConnection()) {
            conn.setAutoCommit(false);
            String statement = "INSERT INTO Acuerdo (id_reunion, descripcion, fecha, autor, id_autor) " +
                    "VALUES(?, ?, ?, ?, ?)";

            PreparedStatement preparedStatement = conn.prepareStatement(statement);

            preparedStatement.setInt(1, newAgreement.getIdMeet());
            preparedStatement.setString(2, newAgreement.getDescriptionAgreement());
            preparedStatement.setDate(3, DateFormatter.convertUtilDateToSQLDate(newAgreement.getRegisterAgreement()));
            preparedStatement.setString(4, newAgreement.getAuthorAgreement());
            preparedStatement.setInt(5, newAgreement.getAuthor());
            wasAdded = preparedStatement.executeUpdate() > 0;

            conn.commit();

        }
        databaseConection.disconnect();
        return wasAdded;
    }

    public ObservableList<Agreement> getAgreementList(int idMeet) throws SQLException {
        ObservableList<Agreement> agreementObservableList;
        agreementObservableList = FXCollections.observableArrayList();

        try(Connection conn = databaseConection.getConnection()) {

            String statement = "SELECT * FROM Acuerdo WHERE id_reunion = ?";

            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setInt(1, idMeet);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Agreement agreementData = new Agreement();
                agreementData.setIdMeet(resultSet.getInt("id_reunion"));
                agreementData.setDescriptionAgreement(resultSet.getString("descripcion"));
                agreementData.setRegisterAgreement(DateFormatter.convertSQLDateToUtilDate(resultSet.getDate("fecha")));
                agreementData.setAuthorAgreement(resultSet.getString("autor"));

                agreementObservableList.add(agreementData);

            }

        }
        databaseConection.disconnect();
        return agreementObservableList;
    }

    public boolean checkAgreement(String agreementDescription, int idMeet) throws SQLException {
        boolean exist = false;
        try(Connection conn = databaseConection.getConnection()) {

            String statement = "SELECT * FROM Acuerdo WHERE descripcion = ? AND id_reunion= ?";

            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, agreementDescription);
            preparedStatement.setInt(2, idMeet);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {
                exist = true;
            }

        }
        databaseConection.disconnect();

        return exist;
    }
}
