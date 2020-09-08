package model;

import database.DBConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class City extends Settlement {

    public City(String structureID, int userID, int xPos1, int yPos1) {
        super(structureID, userID, xPos1, yPos1);
    }

    
    @Override
    public String toString() {
        return "City{" +
                "structureId='" + getStructureId() + "'" +
                ", userId=" + getUserId() +
                ", xPos1=" + getXPos1() +
                ", yPos1=" + getYPos1() +
                '}';
    }
    
    //getters
    public static City getFromDatabase(int gameId, String cityId) {
        if (!cityId.startsWith("c")) {
            throw new IllegalArgumentException("Illegal value for cityId");
        }

        if (DBConnection.checkConnection()) {
            try {
                PreparedStatement preparedStatement = DBConnection.connection.prepareStatement("SELECT idstuk, ss.idspeler, x_van, y_van FROM spelerstuk ss JOIN speler s ON ss.idspeler = s.idspeler WHERE idspel = ? AND idstuk = ?;");

                preparedStatement.setInt(1, gameId);
                preparedStatement.setString(2, cityId);

                ResultSet resultSet = preparedStatement.executeQuery();

                return getCityFromResultSet(resultSet);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return null;
    }

    public static City[] getAllFromDatabase() {
        if (DBConnection.checkConnection()) {
            try {
                Statement statement = DBConnection.connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM spelerstuk WHERE idstuk LIKE 'c%' ORDER BY idstuk, idspeler;");

                return getAllCitiesFromResultSet(resultSet);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return new City[0];
    }

    public static City[] getAllFromDatabaseWithUserId(int userId) {
        if (DBConnection.checkConnection()) {
            try {
                PreparedStatement preparedStatement = DBConnection.connection.prepareStatement("SELECT * FROM spelerstuk WHERE idstuk LIKE 'c%' AND idspeler = ? ORDER BY idstuk, idspeler;");

                preparedStatement.setInt(1, userId);

                ResultSet resultSet = preparedStatement.executeQuery();
                return getAllCitiesFromResultSet(resultSet);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return new City[0];
    }
    private static City getCityFromResultSet(ResultSet resultSet) {
        try {
            if (resultSet.next()) {
                String structureId = resultSet.getString("idstuk");
                int userId = resultSet.getInt("idspeler");
                int xPos1 = resultSet.getInt("x_van");
                int yPos1 = resultSet.getInt("y_van");

                return new City(structureId, userId, xPos1, yPos1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static City[] getAllCitiesFromResultSet(ResultSet resultSet) {
        ArrayList<City> cities = new ArrayList<>();

        try {
            while (resultSet.next()) {
                String structureId = resultSet.getString("idstuk");
                int userId = resultSet.getInt("idspeler");
                int xPos1 = resultSet.getInt("x_van");
                int yPos1 = resultSet.getInt("y_van");

                cities.add(new City(structureId, userId, xPos1, yPos1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cities.toArray(new City[cities.size()]);
    }

}
