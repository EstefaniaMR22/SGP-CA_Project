package model.dao;

import model.dao.interfaces.ILgacDAO;
import model.domain.LGAC;
import utils.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LgacDAO implements ILgacDAO {
    private final Database database;

    public LgacDAO() {
        this.database = new Database();
    }

    @Override
    public int addLgac(LGAC lgac) throws SQLException {
        int idLgac = -1;
        try (Connection conn = database.getConnection()) {
            conn.setAutoCommit(false);
            String statement = "INSERT INTO LGAC(identificador, descripcion) VALUES(?,?)";
            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, lgac.getIdentification());
            preparedStatement.setString(2, lgac.getDescription());
            int rowsAffected = preparedStatement.executeUpdate();
            if(rowsAffected > 0) {
                statement = "SELECT LAST_INSERT_ID()";
                preparedStatement = conn.prepareStatement(statement);
                ResultSet resultSet = preparedStatement.executeQuery();
                if(resultSet.next()) {
                    idLgac = resultSet.getInt(1);
                }
            }
            conn.commit();
        }
        return idLgac;
    }

    @Override
    public boolean removeLgac(int idLgac) throws SQLException {
        boolean isRemoved = false;
        try (Connection conn = database.getConnection()) {
            String statement = "DELETE FROM LGAC WHERE id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setInt(1, idLgac);
            int rowsAffected = preparedStatement.executeUpdate();
            isRemoved = rowsAffected > 0;
        }
        return isRemoved;
    }

    @Override
    public List<LGAC> getAlllgacs() throws SQLException {
        List<LGAC> list;
        try (Connection conn = database.getConnection()) {
            String statement = "SELECT * FROM LGAC";
            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            ResultSet resultSet = preparedStatement.executeQuery();
            list = new ArrayList<>();
            while (resultSet.next()) {
                LGAC lgac = new LGAC();
                lgac.setId(resultSet.getInt("id"));
                lgac.setIdentification(resultSet.getString("identificador"));
                lgac.setDescription(resultSet.getString("descripcion"));
                list.add(lgac);
            }
        }
        return list;
    }


}
