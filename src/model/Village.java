package model;

import database.DBConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Village extends Settlement {

    public Village(String structureID, int userID, int xPos1, int yPos1) {
        super(structureID, userID, xPos1, yPos1);
    }

    @Override
    public String toString() {
        return "Village{" +
                "structureId='" + getStructureId() + "'" +
                ", userId=" + getUserId() +
                ", xPos1=" + getXPos1() +
                ", yPos1=" + getYPos1() +
                '}';
    }

    //getter
    public static Village getFromDatabase(int gameId, String villageId) {
        if (!villageId.startsWith("d")) {
            throw new IllegalArgumentException("Illegal value for villageId");
        }
        if (DBConnection.checkConnection()) {
            try {
                PreparedStatement preparedStatement = DBConnection.connection.prepareStatement("SELECT idstuk, ss.idspeler, x_van, y_van FROM spelerstuk ss JOIN speler s ON ss.idspeler = s.idspeler WHERE idspel = ? AND idstuk = ?;");

                preparedStatement.setInt(1, gameId);
                preparedStatement.setString(2, villageId);

                ResultSet resultSet = preparedStatement.executeQuery();
                return getVillageFromResultSet(resultSet);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return null;
    }

    public static Village[] getAllFromDatabase() {
        if (DBConnection.checkConnection()) {
            try {
                Statement statement = DBConnection.connection.createStatement();

                ResultSet resultSet = statement.executeQuery("SELECT * FROM spelerstuk WHERE idstuk LIKE 'd%';");
                return getAllVillagesFromResultSet(resultSet);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return new Village[0];
    }

	public static Village[] getAllFromDatabaseWithUserId(int userId) {
		if (DBConnection.checkConnection()) {
			try {
				PreparedStatement preparedStatement = DBConnection.connection
						.prepareStatement("SELECT * FROM spelerstuk WHERE idstuk LIKE 'd%' AND idspeler = ?;");

				preparedStatement.setInt(1, userId);

				ResultSet resultSet = preparedStatement.executeQuery();
				return getAllVillagesFromResultSet(resultSet);
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}

		return new Village[0];
	}

    private static Village getVillageFromResultSet(ResultSet resultSet) {
        try {
            if (resultSet.next()) {
                String structureId = resultSet.getString("idstuk");
                int userId = resultSet.getInt("idspeler");
                int xPos1 = resultSet.getInt("x_van");
                int yPos1 = resultSet.getInt("y_van");

                return new Village(structureId, userId, xPos1, yPos1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static Village[] getAllVillagesFromResultSet(ResultSet resultSet) {
        ArrayList<Village> villages = new ArrayList<>();

        try {
            while (resultSet.next()) {
                String structureId = resultSet.getString("idstuk");
                int userId = resultSet.getInt("idspeler");
                int xPos1 = resultSet.getInt("x_van");
                int yPos1 = resultSet.getInt("y_van");

                villages.add(new Village(structureId, userId, xPos1, yPos1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return villages.toArray(new Village[villages.size()]);
    }

}
