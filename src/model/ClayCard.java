package model;

import database.DBConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ClayCard extends ResourceCard {
	//constants 
    private static final char RESOURCE_TYPE_ID = 'B';

    public ClayCard(int gameID, String cardID, int userID) {
        super(gameID, cardID, userID, RESOURCE_TYPE_ID);
    }

    //getter
    public static ClayCard getFromDatabase(int gameId, String cardId) {
        if (!cardId.startsWith("B")) {
            throw new IllegalArgumentException("Illegal value for cardId");
        }

        if (DBConnection.checkConnection()) {
            try {
                PreparedStatement preparedStatement = DBConnection.connection.prepareStatement("SELECT idspel, sg.idgrondstofkaart, idspeler FROM spelergrondstofkaart sg JOIN grondstofkaart g ON g.idgrondstofkaart = sg.idgrondstofkaart WHERE idspel = ? AND sg.idgrondstofkaart = ?;");

                preparedStatement.setInt(1, gameId);
                preparedStatement.setString(2, cardId);

                ResultSet resultSet = preparedStatement.executeQuery();
                return getClayCardFromResultSet(resultSet);
            } catch (SQLException ex) {
                ex.printStackTrace();
            } 
        }

        return null;
    }

    public static ClayCard[] getAllFromDatabase(int gameId) {
        if (DBConnection.checkConnection()) {
            try {
                PreparedStatement preparedStatement = DBConnection.connection.prepareStatement("SELECT idspel, sg.idgrondstofkaart, idspeler FROM spelergrondstofkaart sg JOIN grondstofkaart g ON g.idgrondstofkaart = sg.idgrondstofkaart WHERE idspeler IS NULL AND idspel = ? AND idgrondstofsoort = 'B';");

                preparedStatement.setInt(1, gameId);

                ResultSet resultSet = preparedStatement.executeQuery();
                return getAllClayCardsFromResultSet(resultSet);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return new ClayCard[0];
    }

    public static ClayCard[] getAllFromDatabaseWithUserId(int userId) {
        if (DBConnection.checkConnection()) {
            try {
                PreparedStatement preparedStatement = DBConnection.connection.prepareStatement("SELECT idspel, sg.idgrondstofkaart, idspeler FROM spelergrondstofkaart sg JOIN grondstofkaart g ON g.idgrondstofkaart = sg.idgrondstofkaart WHERE idspeler = ? AND idgrondstofsoort = 'B';");

                preparedStatement.setInt(1, userId);

                ResultSet resultSet = preparedStatement.executeQuery();
                return getAllClayCardsFromResultSet(resultSet);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return new ClayCard[0];
    }

    private static ClayCard getClayCardFromResultSet(ResultSet resultSet) {
        try {
            if (resultSet.next()) {
                int gameId = resultSet.getInt("idspel");
                String cardId = resultSet.getString("idgrondstofkaart");
                int userId = resultSet.getInt("idspeler");

                return new ClayCard(gameId, cardId, userId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static ClayCard[] getAllClayCardsFromResultSet(ResultSet resultSet) {
        ArrayList<ClayCard> clayCards = new ArrayList<>();

        try {
            while (resultSet.next()) {
                int gameId = resultSet.getInt("idspel");
                String cardId = resultSet.getString("idgrondstofkaart");
                int userId = resultSet.getInt("idspeler");

                clayCards.add(new ClayCard(gameId, cardId, userId));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return clayCards.toArray(new ClayCard[clayCards.size()]);
    }

}
