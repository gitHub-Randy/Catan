package model;

import database.DBConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class SheepCard extends ResourceCard {
	//constants
    private static final char RESOURCE_TYPE_ID = 'W';
    
    public SheepCard(int gameID, String cardID, int userID) {
        super(gameID, cardID, userID, RESOURCE_TYPE_ID);
    }

    //getters
    public static SheepCard getFromDatabase(int gameId, String cardId) {
        if (!cardId.startsWith("W")) {
            throw new IllegalArgumentException("Illegal value for cardId");
        }

        if (DBConnection.checkConnection()) {
            try {
                PreparedStatement preparedStatement = DBConnection.connection.prepareStatement("SELECT idspel, sg.idgrondstofkaart, idspeler FROM spelergrondstofkaart sg JOIN grondstofkaart g ON g.idgrondstofkaart = sg.idgrondstofkaart WHERE idspel = ? AND sg.idgrondstofkaart = ?;");

                preparedStatement.setInt(1, gameId);
                preparedStatement.setString(2, cardId);

                ResultSet resultSet = preparedStatement.executeQuery();
                return getSheepCardFromResultSet(resultSet);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return null;
    }

    public static SheepCard[] getAllFromDatabase(int gameId) {
        if (DBConnection.checkConnection()) {
            try {
                PreparedStatement preparedStatement = DBConnection.connection.prepareStatement("SELECT idspel, sg.idgrondstofkaart, idspeler FROM spelergrondstofkaart sg JOIN grondstofkaart g ON g.idgrondstofkaart = sg.idgrondstofkaart WHERE idspeler IS NULL AND idspel = ? AND idgrondstofsoort = 'W';");

                preparedStatement.setInt(1, gameId);

                ResultSet resultSet = preparedStatement.executeQuery();
                return getAllSheepCardsFromResultSet(resultSet);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return new SheepCard[0];
    }

    public static SheepCard[] getAllFromDatabaseWithUserId(int userId) {
        if (DBConnection.checkConnection()) {
            try {
                PreparedStatement preparedStatement = DBConnection.connection.prepareStatement("SELECT idspel, sg.idgrondstofkaart, idspeler FROM spelergrondstofkaart sg JOIN grondstofkaart g ON g.idgrondstofkaart = sg.idgrondstofkaart WHERE idspeler = ? AND idgrondstofsoort = 'W';");

                preparedStatement.setInt(1, userId);

                ResultSet resultSet = preparedStatement.executeQuery();
                return getAllSheepCardsFromResultSet(resultSet);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return new SheepCard[0];
    }

    private static SheepCard getSheepCardFromResultSet(ResultSet resultSet) {
        try {
            if (resultSet.next()) {
                int gameId = resultSet.getInt("idspel");
                String cardId = resultSet.getString("idgrondstofkaart");
                int userId = resultSet.getInt("idspeler");

                return new SheepCard(gameId, cardId, userId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static SheepCard[] getAllSheepCardsFromResultSet(ResultSet resultSet) {
        ArrayList<SheepCard> sheepCards = new ArrayList<>();

        try {
            while (resultSet.next()) {
                int gameId = resultSet.getInt("idspel");
                String cardId = resultSet.getString("idgrondstofkaart");
                int userId = resultSet.getInt("idspeler");

                sheepCards.add(new SheepCard(gameId, cardId, userId));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return sheepCards.toArray(new SheepCard[sheepCards.size()]);
    }

}
