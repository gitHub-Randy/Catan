package model;

import database.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DevelopmentCard {
	//constants
    private final List<String> allowedNames = Arrays.asList("kathedraal", "markt", "bibliotheek", "universiteit", "parlement", "stratenbouw", "monopolie", "uitvinding", "ridder");
    private final List<String> allowedTypes = Arrays.asList("gebouw", "vooruitgang", "ridder");
    //instance
    private int gameId;
    private String cardId;
    private int userId;
    private String name;
    private String type;
    private String description;
    private boolean played;

    public DevelopmentCard(int gameId, String cardId, String name, String type, String description, int userId, boolean played) {
        setGameId(gameId);
        setCardId(cardId);
        setName(name);
        setType(type);
        setDescription(description);
        setUserId(userId);
        setPlayed(played);
    }

    //getters
    public int getGameId() {
        return gameId;
    }

    public String getCardId() {
        return cardId;
    }

    public int getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public boolean isPlayed() {
        return played;
    }
    
    public static DevelopmentCard getFromDatabase(int gameId, String cardId) {
        if (DBConnection.checkConnection()) {
            try {
                PreparedStatement preparedStatement = DBConnection.connection.prepareStatement("SELECT sok.idspel, sok.idontwikkelingskaart, k.naam, k.type, k.uitleg, sok.idspeler, sok.gespeeld FROM spelerontwikkelingskaart sok JOIN ontwikkelingskaart o ON sok.idontwikkelingskaart = o.idontwikkelingskaart JOIN kaarttype k ON o.naam = k.naam WHERE sok.idspel = ? AND sok.idontwikkelingskaart = ?;");

                preparedStatement.setInt(1, gameId);
                preparedStatement.setString(2, cardId);

                ResultSet resultSet = preparedStatement.executeQuery();
                return getDevelopmentCardFromResultSet(resultSet);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return null;
    }

    public static DevelopmentCard[] getAllFromDatabase() {
        if (DBConnection.checkConnection()) {
            try {
                Statement statement = DBConnection.connection.createStatement();

                ResultSet resultSet = statement.executeQuery("SELECT sok.idspel, sok.idontwikkelingskaart, k.naam, k.type, k.uitleg, sok.idspeler, sok.gespeeld FROM spelerontwikkelingskaart sok JOIN ontwikkelingskaart o ON sok.idontwikkelingskaart = o.idontwikkelingskaart JOIN kaarttype k ON o.naam = k.naam;");
                return getAllDevelopmentCardsFromResultSet(resultSet);
            } catch (SQLException ex) {
                ex.printStackTrace();
            } 
        }

        return new DevelopmentCard[0];
    }

    public static DevelopmentCard[] getAllFromDatabaseWithUserId(int userId) {
        if (DBConnection.checkConnection()) {
            try {
                PreparedStatement preparedStatement = DBConnection.connection.prepareStatement("SELECT sok.idspel, sok.idontwikkelingskaart, k.naam, k.type, k.uitleg, sok.idspeler, sok.gespeeld FROM spelerontwikkelingskaart sok JOIN ontwikkelingskaart o ON sok.idontwikkelingskaart = o.idontwikkelingskaart JOIN kaarttype k ON o.naam = k.naam WHERE sok.idspeler = ?;");

                preparedStatement.setInt(1, userId);

                ResultSet resultSet = preparedStatement.executeQuery();
                return getAllDevelopmentCardsFromResultSet(resultSet);
            } catch (SQLException ex) {
                ex.printStackTrace();
            } 
        }

        return new DevelopmentCard[0];
    }
    
    private static DevelopmentCard getDevelopmentCardFromResultSet(ResultSet resultSet) {
        try {
            if (resultSet.next()) {
                int gameId = resultSet.getInt("idspel");
                String cardId = resultSet.getString("idontwikkelingskaart");
                String name = resultSet.getString("naam");
                String type = resultSet.getString("type");
                String description = resultSet.getString("uitleg");
                int userId = resultSet.getInt("idspeler");
                boolean played = resultSet.getBoolean("gespeeld");

                return new DevelopmentCard(gameId, cardId, name, type, description, userId, played);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static DevelopmentCard[] getAllDevelopmentCardsFromResultSet(ResultSet resultSet) {
        ArrayList<DevelopmentCard> developmentCards = new ArrayList<>();

        try {
            while (resultSet.next()) {
                int gameId = resultSet.getInt("idspel");
                String cardId = resultSet.getString("idontwikkelingskaart");
                String name = resultSet.getString("naam");
                String type = resultSet.getString("type");
                String description = resultSet.getString("uitleg");
                int userId = resultSet.getInt("idspeler");
                boolean played = resultSet.getBoolean("gespeeld");

                developmentCards.add(new DevelopmentCard(gameId, cardId, name, type, description, userId, played));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return developmentCards.toArray(new DevelopmentCard[0]);
    }
    
    //setter
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setPlayed(boolean played) {
        this.played = played;
    }
    
    private void setGameId(int gameId) {
        this.gameId = gameId;
    }

    private void setCardId(String cardId) {
        if (cardId == null || cardId.length() != 4) {
            throw new IllegalArgumentException("Illegal value for cardId");
        }

        this.cardId = cardId;
    }

    private void setName(String name) {
        if (!allowedNames.contains(name)) {
            throw new IllegalArgumentException("Illegal value for name");
        }

        this.name = name;
    }

    private void setType(String type) {
        if (!allowedTypes.contains(type)) {
            throw new IllegalArgumentException("Illegal value for type");
        }

        this.type = type;
    }

    private void setDescription(String description) {
        if (description != null && description.length() > 145) {
            throw new IllegalArgumentException("Illegal value for description");
        }

        this.description = description;
    }
    //update
    public void updateInDatabase() {
        if (DBConnection.checkConnection()) {
            try {
                PreparedStatement preparedStatement = DBConnection.connection.prepareStatement("UPDATE spelerontwikkelingskaart SET idspeler = ?, gespeeld = ? WHERE idspel = ? AND idontwikkelingskaart = ?;");

                if (userId != 0) {
                    preparedStatement.setInt(1, userId);
                } else {
                    preparedStatement.setNull(1, Types.INTEGER);
                }
                preparedStatement.setBoolean(2, played);
                preparedStatement.setInt(3, gameId);
                preparedStatement.setString(4, cardId);

                preparedStatement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    //insert
    public void insertInDatabase() {
        if (DBConnection.checkConnection()) {
            try {
                PreparedStatement preparedStatement = DBConnection.connection.prepareStatement("INSERT INTO spelerontwikkelingskaart (idspel, idontwikkelingskaart, gespeeld) VALUES (?, ?, ?);");

                preparedStatement.setInt(1, gameId);
                preparedStatement.setString(2, cardId);
                preparedStatement.setBoolean(3, false);

                preparedStatement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String toString() {
        return "DevelopmentCard{" +
                "cardId='" + cardId + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                ", played=" + played +
                '}';
    }



 

}