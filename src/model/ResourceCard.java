package model;

import database.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ResourceCard {
	//constants
    private final List<Character> allowedResourceTypeIDs = Arrays.asList('W', 'G', 'B', 'E', 'H', 'X');
    //instance
    private int gameId;
    private String cardId;
    private int userId;
    private char resourceTypeId;

    public ResourceCard(int gameId, String cardId, int userId, char resourceTypeId) {
        setGameId(gameId);
        setCardId(cardId);
        setUserId(userId);
        setResourceTypeId(resourceTypeId);
    }

    //getter
    
    private static ResourceCard getResourceCardFromResultSet(ResultSet resultSet) {
        try {
            if (resultSet.next()) {
                int gameID = resultSet.getInt("idspel");
                String cardID = resultSet.getString("idgrondstofkaart");
                int userID = resultSet.getInt("idspeler");
                String resourceTypeIDString = resultSet.getString("idgrondstofsoort");

                char resourceTypeID = 'X';
                if (resourceTypeIDString.length() == 1) {
                    resourceTypeID = resourceTypeIDString.charAt(0);
                }

                return new ResourceCard(gameID, cardID, userID, resourceTypeID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static ResourceCard[] getAllResourceCardsFromResultSet(ResultSet resultSet) {
        ArrayList<ResourceCard> resourceCards = new ArrayList<>();

        try {
            while (resultSet.next()) {
                int gameID = resultSet.getInt("idspel");
                String cardID = resultSet.getString("idgrondstofkaart");
                int userID = resultSet.getInt("idspeler");
                String resourceTypeIDString = resultSet.getString("idgrondstofsoort");

                char resourceTypeID = 'X';
                if (resourceTypeIDString.length() == 1) {
                    resourceTypeID = resourceTypeIDString.charAt(0);
                }

                resourceCards.add(new ResourceCard(gameID, cardID, userID, resourceTypeID));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resourceCards.toArray(new ResourceCard[resourceCards.size()]);
    }
    
    public char getResourceTypeId() {
        return resourceTypeId;
    }

    public int getGameId() {
        return gameId;
    }

    public String getCardId() {
        return cardId;
    }

    public int getUserId() {
        return userId;
    }
    
    public static ResourceCard getFromDatabase(int gameId, String cardId) {
        if (DBConnection.checkConnection()) {
            try {
                PreparedStatement preparedStatement = DBConnection.connection.prepareStatement("SELECT idspel, sg.idgrondstofkaart, idspeler FROM spelergrondstofkaart sg JOIN grondstofkaart g ON g.idgrondstofkaart = sg.idgrondstofkaart WHERE idspel = ? AND sg.idgrondstofkaart = ?;");

                preparedStatement.setInt(1, gameId);
                preparedStatement.setString(2, cardId);

                ResultSet resultSet = preparedStatement.executeQuery();
                return getResourceCardFromResultSet(resultSet);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return null;
    }

    public static ResourceCard[] getAllFromDatabase() {
        if (DBConnection.checkConnection()) {
            try {
                Statement statement = DBConnection.connection.createStatement();

                ResultSet resultSet = statement.executeQuery("SELECT idspel, sg.idgrondstofkaart, idspeler FROM spelergrondstofkaart sg JOIN grondstofkaart g ON g.idgrondstofkaart = sg.idgrondstofkaart;");
                return getAllResourceCardsFromResultSet(resultSet);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return new ResourceCard[0];
    }

    public static ResourceCard[] getAllFromDatabaseWithUserId(int userId) {
        if (DBConnection.checkConnection()) {
            try {
                PreparedStatement preparedStatement = DBConnection.connection.prepareStatement("SELECT idspel, sg.idgrondstofkaart, idspeler, idgrondstofsoort FROM spelergrondstofkaart sg JOIN grondstofkaart g ON g.idgrondstofkaart = sg.idgrondstofkaart WHERE idspeler = ?;");

                preparedStatement.setInt(1, userId);

                ResultSet resultSet = preparedStatement.executeQuery();
                return getAllResourceCardsFromResultSet(resultSet);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return new ResourceCard[0];
    }
    
    //setters
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    private void setGameId(int gameId) {
        this.gameId = gameId;
    }

    private void setCardId(String cardId) {
        this.cardId = cardId;
    }

    private void setResourceTypeId(char resourceTypeId) {
        if (!allowedResourceTypeIDs.contains(resourceTypeId)) {
            throw new IllegalArgumentException("Illegal value for resourceTypeId");
        }

        this.resourceTypeId = resourceTypeId;
    }
    
    //update
    public void updateInDatabase() {
        if (DBConnection.checkConnection()) {
            try {
                PreparedStatement preparedStatement = DBConnection.connection.prepareStatement("UPDATE spelergrondstofkaart SET idspeler = ? WHERE idspel = ? AND idgrondstofkaart = ?;");

                if (userId != 0) {
                    preparedStatement.setInt(1, userId);
                } else {
                    preparedStatement.setNull(1, Types.INTEGER);
                }
                preparedStatement.setInt(2, gameId);
                preparedStatement.setString(3, cardId);

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
                PreparedStatement preparedStatement = DBConnection.connection.prepareStatement("INSERT INTO spelergrondstofkaart (idspel, idgrondstofkaart, idspeler) VALUES (?, ?, ?);");

                preparedStatement.setInt(1, gameId);
                preparedStatement.setString(2, cardId);
                if (userId == 0) {
                    preparedStatement.setNull(3, Types.INTEGER);
                } else {
                    preparedStatement.setInt(3, userId);
                }

                preparedStatement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public ClayCard convertToClayCard() {
        return new ClayCard(gameId, cardId, userId);
    }

    public GrainCard convertToGrainCard() {
        return new GrainCard(gameId, cardId, userId);
    }

    public OreCard convertToOreCard() {
        return new OreCard(gameId, cardId, userId);
    }

    public SheepCard convertToSheepCard() {
        return new SheepCard(gameId, cardId, userId);
    }

    public WoodCard convertToWoodCard() {
        return new WoodCard(gameId, cardId, userId);
    }

    @Override
    public String toString() {
        return "ResourceCard{" +
                "gameId=" + gameId +
                ", cardId='" + cardId + '\'' +
                ", userId=" + userId +
                ", resourceTypeId=" + resourceTypeId +
                '}';
    }





}
