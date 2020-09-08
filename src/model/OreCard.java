package model;

import database.DBConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class OreCard extends ResourceCard {
	//constants
    private static final char RESOURCE_TYPE_ID = 'E';

    public OreCard(int gameID, String cardID, int userID) {
        super(gameID, cardID, userID, RESOURCE_TYPE_ID);
    }

    //getters
    public static OreCard getFromDatabase(int gameId, String cardId) {
        if (!cardId.startsWith("E")) {
            throw new IllegalArgumentException("Illegal value for cardId");
        }

        if (DBConnection.checkConnection()) {
            try {
                PreparedStatement preparedStatement = DBConnection.connection.prepareStatement("SELECT idspel, sg.idgrondstofkaart, idspeler FROM spelergrondstofkaart sg JOIN grondstofkaart g ON g.idgrondstofkaart = sg.idgrondstofkaart WHERE idspel = ? AND sg.idgrondstofkaart = ?;");

                preparedStatement.setInt(1, gameId);
                preparedStatement.setString(2, cardId);

                ResultSet resultSet = preparedStatement.executeQuery();
                return getOreCardFromResultSet(resultSet);
            } catch (SQLException ex) {
                ex.printStackTrace();
            } 
        }

        return null;
    }

    public static OreCard[] getAllFromDatabase(int gameId) {
        if (DBConnection.checkConnection()) {
            try {
                PreparedStatement preparedStatement = DBConnection.connection.prepareStatement("SELECT idspel, sg.idgrondstofkaart, idspeler FROM spelergrondstofkaart sg JOIN grondstofkaart g ON g.idgrondstofkaart = sg.idgrondstofkaart WHERE idspeler IS NULL AND idspel = ? AND idgrondstofsoort = 'E';");

                preparedStatement.setInt(1, gameId);

                ResultSet resultSet = preparedStatement.executeQuery();
                return getAllOreCardsFromResultSet(resultSet);
            } catch (SQLException ex) {
                ex.printStackTrace();
            } 
        }

        return new OreCard[0];
    }

    public static OreCard[] getAllFromDatabaseWithUserId(int userId) {
        if (DBConnection.checkConnection()) {
            try {
                PreparedStatement preparedStatement = DBConnection.connection.prepareStatement("SELECT idspel, sg.idgrondstofkaart, idspeler FROM spelergrondstofkaart sg JOIN grondstofkaart g ON g.idgrondstofkaart = sg.idgrondstofkaart WHERE idspeler = ? AND idgrondstofsoort = 'E';");

                preparedStatement.setInt(1, userId);

                ResultSet resultSet = preparedStatement.executeQuery();
                return getAllOreCardsFromResultSet(resultSet);
            } catch (SQLException ex) {
                ex.printStackTrace();
            } 
        }

        return new OreCard[0];
    }

    private static OreCard getOreCardFromResultSet(ResultSet resultSet) {
        try {
            if (resultSet.next()) {
                int gameId = resultSet.getInt("idspel");
                String cardId = resultSet.getString("idgrondstofkaart");
                int userId = resultSet.getInt("idspeler");

                return new OreCard(gameId, cardId, userId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static OreCard[] getAllOreCardsFromResultSet(ResultSet resultSet) {
        ArrayList<OreCard> oreCards = new ArrayList<>();

        try {
            while (resultSet.next()) {
                int gameId = resultSet.getInt("idspel");
                String cardId = resultSet.getString("idgrondstofkaart");
                int userId = resultSet.getInt("idspeler");

                oreCards.add(new OreCard(gameId, cardId, userId));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return oreCards.toArray(new OreCard[0]);
    }

}
