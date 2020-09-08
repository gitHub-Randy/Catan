package model;

import database.DBConnection;

import java.sql.*;
import java.util.ArrayList;

public class WoodCard extends ResourceCard {

    private static final char RESOURCE_TYPE_ID = 'H';

    public WoodCard(int gameID, String cardID, int userID) {
        super(gameID, cardID, userID, RESOURCE_TYPE_ID);
    }

    //getters
    public static WoodCard getFromDatabase(int gameId, String cardId) {
        if (!cardId.startsWith("H")) {
            throw new IllegalArgumentException("Illegal value for cardId");
        }

        if (DBConnection.checkConnection()) {
            try {
                PreparedStatement preparedStatement = DBConnection.connection.prepareStatement("SELECT idspel, sg.idgrondstofkaart, idspeler FROM spelergrondstofkaart sg JOIN grondstofkaart g ON g.idgrondstofkaart = sg.idgrondstofkaart WHERE idspel = ? AND sg.idgrondstofkaart = ?;");

                preparedStatement.setInt(1, gameId);
                preparedStatement.setString(2, cardId);

                ResultSet resultSet = preparedStatement.executeQuery();
                return getWoodCardFromResultSet(resultSet);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return null;
    }

    public static WoodCard[] getAllFromDatabase(int gameId) {
        if (DBConnection.checkConnection()) {
            try {
                PreparedStatement preparedStatement = DBConnection.connection.prepareStatement("SELECT idspel, sg.idgrondstofkaart, idspeler FROM spelergrondstofkaart sg JOIN grondstofkaart g ON g.idgrondstofkaart = sg.idgrondstofkaart WHERE idspeler IS NULL AND idspel = ? AND idgrondstofsoort = 'H';");

                preparedStatement.setInt(1, gameId);

                ResultSet resultSet = preparedStatement.executeQuery();
                return getAllWoodCardsFromResultSet(resultSet);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return new WoodCard[0];
    }

    public static WoodCard[] getAllFromDatabaseWithUserId(int userId) {
        if (DBConnection.checkConnection()) {
            try {
                PreparedStatement preparedStatement = DBConnection.connection.prepareStatement("SELECT idspel, sg.idgrondstofkaart, idspeler FROM spelergrondstofkaart sg JOIN grondstofkaart g ON g.idgrondstofkaart = sg.idgrondstofkaart WHERE idspeler = ? AND idgrondstofsoort = 'H';");

                preparedStatement.setInt(1, userId);

                ResultSet resultSet = preparedStatement.executeQuery();
                return getAllWoodCardsFromResultSet(resultSet);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return new WoodCard[0];
    }

    private static WoodCard getWoodCardFromResultSet(ResultSet resultSet) {
        try {
            if (resultSet.next()) {
                int gameId = resultSet.getInt("idspel");
                String cardId = resultSet.getString("idgrondstofkaart");
                int userId = resultSet.getInt("idspeler");

                return new WoodCard(gameId, cardId, userId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static WoodCard[] getAllWoodCardsFromResultSet(ResultSet resultSet) {
        ArrayList<WoodCard> woodCards = new ArrayList<>();

        try {
            while (resultSet.next()) {
                int gameId = resultSet.getInt("idspel");
                String cardId = resultSet.getString("idgrondstofkaart");
                int userId = resultSet.getInt("idspeler");

                woodCards.add(new WoodCard(gameId, cardId, userId));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return woodCards.toArray(new WoodCard[woodCards.size()]);
    }

}
