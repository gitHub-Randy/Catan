package model;

import database.DBConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class VictoryPoint extends DevelopmentCard {

    public VictoryPoint(int gameId, String cardId, String name, String type, String description, int userId, boolean played) {
        super(gameId, cardId, name, type, description, userId, played);
    }

    //getter
    public static VictoryPoint getFromDatabase(int gameId, String cardId) {
        if (!cardId.endsWith("g")) {
            throw new IllegalArgumentException("Illegal value for cardId");
        }
        if (DBConnection.checkConnection()) {
            try {
                PreparedStatement preparedStatement = DBConnection.connection.prepareStatement("SELECT sok.idspel, sok.idontwikkelingskaart, k.naam, k.type, k.uitleg, sok.idspeler, sok.gespeeld FROM spelerontwikkelingskaart sok JOIN ontwikkelingskaart o ON sok.idontwikkelingskaart = o.idontwikkelingskaart JOIN kaarttype k ON o.naam = k.naam WHERE sok.idspel = ? AND sok.idontwikkelingskaart = ?;");

                preparedStatement.setInt(1, gameId);
                preparedStatement.setString(2, cardId);

                ResultSet resultSet = preparedStatement.executeQuery();
                return getVictoryPointsFromResultSet(resultSet);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return null;
    }

    public static VictoryPoint[] getAllFromDatabase() {
        if (DBConnection.checkConnection()) {
            try {
                Statement statement = DBConnection.connection.createStatement();

                ResultSet resultSet = statement.executeQuery("SELECT sok.idspel, sok.idontwikkelingskaart, k.naam, k.type, k.uitleg, sok.idspeler, sok.gespeeld FROM spelerontwikkelingskaart sok JOIN ontwikkelingskaart o ON sok.idontwikkelingskaart = o.idontwikkelingskaart JOIN kaarttype k ON o.naam = k.naam;");
                return getAllVictoryPointsFromResultSet(resultSet);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return new VictoryPoint[0];
    }

    public static VictoryPoint[] getAllFromDatabaseWithUserId(int userId) {
        if (DBConnection.checkConnection()) {
            try {
                PreparedStatement preparedStatement = DBConnection.connection.prepareStatement("SELECT sok.idspel, sok.idontwikkelingskaart, k.naam, k.type, k.uitleg, sok.idspeler, sok.gespeeld FROM spelerontwikkelingskaart sok JOIN ontwikkelingskaart o ON sok.idontwikkelingskaart = o.idontwikkelingskaart JOIN kaarttype k ON o.naam = k.naam WHERE sok.idspeler = ? AND type = 'gebouw';");

                preparedStatement.setInt(1, userId);

                ResultSet resultSet = preparedStatement.executeQuery();
                return getAllVictoryPointsFromResultSet(resultSet);
            } catch (SQLException ex) {
                ex.printStackTrace();
        }
        }
        return new VictoryPoint[0];
    }

    private static VictoryPoint getVictoryPointsFromResultSet(ResultSet resultSet) {
        try {
            if (resultSet.next()) {
                int gameId = resultSet.getInt("idspel");
                String cardId = resultSet.getString("idontwikkelingskaart");
                String name = resultSet.getString("naam");
                String type = resultSet.getString("type");
                String description = resultSet.getString("uitleg");
                int userId = resultSet.getInt("idspeler");
                boolean played = resultSet.getBoolean("gespeeld");

                return new VictoryPoint(gameId, cardId, name, type, description, userId, played);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static VictoryPoint[] getAllVictoryPointsFromResultSet(ResultSet resultSet) {
        ArrayList<VictoryPoint> victoryPoints = new ArrayList<>();

        try {
            while (resultSet.next()) {
                int gameId = resultSet.getInt("idspel");
                String cardId = resultSet.getString("idontwikkelingskaart");
                String name = resultSet.getString("naam");
                String type = resultSet.getString("type");
                String description = resultSet.getString("uitleg");
                int userId = resultSet.getInt("idspeler");
                boolean played = resultSet.getBoolean("gespeeld");

                victoryPoints.add(new VictoryPoint(gameId, cardId, name, type, description, userId, played));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return victoryPoints.toArray(new VictoryPoint[victoryPoints.size()]);
    }

}
