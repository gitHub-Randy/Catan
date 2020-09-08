package model;

import database.DBConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class KnightCard extends DevelopmentCard {

    public KnightCard(int gameId, String cardId, String name, String type, String description, int userId, boolean played) {
        super(gameId, cardId, name, type, description, userId, played);
    }
    
    //getters
    public static KnightCard getFromDatabase(int gameId, String cardId) {
        if (!cardId.endsWith("r")) {
            throw new IllegalArgumentException("Illegal value for cardId");
        }

        if (DBConnection.checkConnection()) {
            try {
                PreparedStatement preparedStatement = DBConnection.connection.prepareStatement("SELECT sok.idspel, sok.idontwikkelingskaart, k.naam, k.type, k.uitleg, sok.idspeler, sok.gespeeld FROM spelerontwikkelingskaart sok JOIN ontwikkelingskaart o ON sok.idontwikkelingskaart = o.idontwikkelingskaart JOIN kaarttype k ON o.naam = k.naam WHERE sok.idspel = ? AND sok.idontwikkelingskaart = ?;");

                preparedStatement.setInt(1, gameId);
                preparedStatement.setString(2, cardId);

                ResultSet resultSet = preparedStatement.executeQuery();
                return getKnightCardFromResultSet(resultSet);
            } catch (SQLException ex) {
                ex.printStackTrace();
            } 
        }

        return null;
    }

    public static KnightCard[] getAllFromDatabase() {
        if (DBConnection.checkConnection()) {
            try {
                Statement statement = DBConnection.connection.createStatement();

                ResultSet resultSet = statement.executeQuery("SELECT sok.idspel, sok.idontwikkelingskaart, k.naam, k.type, k.uitleg, sok.idspeler, sok.gespeeld FROM spelerontwikkelingskaart sok JOIN ontwikkelingskaart o ON sok.idontwikkelingskaart = o.idontwikkelingskaart JOIN kaarttype k ON o.naam = k.naam;");
                return getAllKnightCardsFromResultSet(resultSet);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return new KnightCard[0];
    }

    public static KnightCard[] getAllFromDatabaseWithUserId(int userId) {
        if (DBConnection.checkConnection()) {
            try {
                PreparedStatement preparedStatement = DBConnection.connection.prepareStatement("SELECT sok.idspel, sok.idontwikkelingskaart, k.naam, k.type, k.uitleg, sok.idspeler, sok.gespeeld FROM spelerontwikkelingskaart sok JOIN ontwikkelingskaart o ON sok.idontwikkelingskaart = o.idontwikkelingskaart JOIN kaarttype k ON o.naam = k.naam WHERE sok.idspeler = ? AND type = 'ridder';");

                preparedStatement.setInt(1, userId);

                ResultSet resultSet = preparedStatement.executeQuery();
                return getAllKnightCardsFromResultSet(resultSet);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return new KnightCard[0];
    }

    private static KnightCard getKnightCardFromResultSet(ResultSet resultSet) {
        try {
            if (resultSet.next()) {
                int gameId = resultSet.getInt("idspel");
                String cardId = resultSet.getString("idontwikkelingskaart");
                String name = resultSet.getString("naam");
                String type = resultSet.getString("type");
                String description = resultSet.getString("uitleg");
                int userId = resultSet.getInt("idspeler");
                boolean played = resultSet.getBoolean("gespeeld");

                return new KnightCard(gameId, cardId, name, type, description, userId, played);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static KnightCard[] getAllKnightCardsFromResultSet(ResultSet resultSet) {
        ArrayList<KnightCard> knightCards = new ArrayList<>();

        try {
            while (resultSet.next()) {
                int gameId = resultSet.getInt("idspel");
                String cardId = resultSet.getString("idontwikkelingskaart");
                String name = resultSet.getString("naam");
                String type = resultSet.getString("type");
                String description = resultSet.getString("uitleg");
                int userId = resultSet.getInt("idspeler");
                boolean played = resultSet.getBoolean("gespeeld");

                knightCards.add(new KnightCard(gameId, cardId, name, type, description, userId, played));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return knightCards.toArray(new KnightCard[knightCards.size()]);
    }

}
