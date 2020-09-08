package model;

import database.DBConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class GrainCard extends ResourceCard {
	//Constants
    private static final char RESOURCE_TYPE_ID = 'G';

    public GrainCard(int gameID, String cardID, int userID) {
        super(gameID, cardID, userID, RESOURCE_TYPE_ID);
    }
    
    //getters
    public static GrainCard getFromDatabase(int gameId, String cardId) {
        if (!cardId.startsWith("G")) {
            throw new IllegalArgumentException("Illegal value for cardId");
        }

        if (DBConnection.checkConnection()) {
            try {
                PreparedStatement preparedStatement = DBConnection.connection.prepareStatement("SELECT idspel, sg.idgrondstofkaart, idspeler FROM spelergrondstofkaart sg JOIN grondstofkaart g ON g.idgrondstofkaart = sg.idgrondstofkaart WHERE idspel = ? AND sg.idgrondstofkaart = ?;");

                preparedStatement.setInt(1, gameId);
                preparedStatement.setString(2, cardId);

                ResultSet resultSet = preparedStatement.executeQuery();
                return getGrainCardFromResultSet(resultSet);
            } catch (SQLException ex) {
                ex.printStackTrace();
            } 
        }

        return null;
    }

    public static GrainCard[] getAllFromDatabase(int gameId) {
        if (DBConnection.checkConnection()) {
            try {
                PreparedStatement preparedStatement = DBConnection.connection.prepareStatement("SELECT idspel, sg.idgrondstofkaart, idspeler FROM spelergrondstofkaart sg JOIN grondstofkaart g ON g.idgrondstofkaart = sg.idgrondstofkaart WHERE idspeler IS NULL AND idspel = ? AND idgrondstofsoort = 'G';");

                preparedStatement.setInt(1, gameId);

                ResultSet resultSet = preparedStatement.executeQuery();
                return getAllGrainCardsFromResultSet(resultSet);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return new GrainCard[0];
    }

    public static GrainCard[] getAllFromDatabaseWithUserId(int userId) {
        if (DBConnection.checkConnection()) {
            try {
                PreparedStatement preparedStatement = DBConnection.connection.prepareStatement("SELECT idspel, sg.idgrondstofkaart, idspeler FROM spelergrondstofkaart sg JOIN grondstofkaart g ON g.idgrondstofkaart = sg.idgrondstofkaart WHERE idspeler = ? AND idgrondstofsoort = 'G';");

                preparedStatement.setInt(1, userId);

                ResultSet resultSet = preparedStatement.executeQuery();
                return getAllGrainCardsFromResultSet(resultSet);
            } catch (SQLException ex) {
                ex.printStackTrace();
            } 
        }

        return new GrainCard[0];
    }

    private static GrainCard getGrainCardFromResultSet(ResultSet resultSet) {
        try {
            if (resultSet.next()) {
                int gameId = resultSet.getInt("idspel");
                String cardId = resultSet.getString("idgrondstofkaart");
                int userId = resultSet.getInt("idspeler");

                return new GrainCard(gameId, cardId, userId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static GrainCard[] getAllGrainCardsFromResultSet(ResultSet resultSet) {
        ArrayList<GrainCard> grainCards = new ArrayList<>();

        try {
            while (resultSet.next()) {
                int gameId = resultSet.getInt("idspel");
                String cardId = resultSet.getString("idgrondstofkaart");
                int userId = resultSet.getInt("idspeler");

                grainCards.add(new GrainCard(gameId, cardId, userId));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return grainCards.toArray(new GrainCard[grainCards.size()]);
    }

}
