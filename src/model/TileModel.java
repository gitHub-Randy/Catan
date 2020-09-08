package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import database.DBConnection;

public class TileModel {
	//constants
    private final List<Character> allowedResourceTypeIDs = Arrays.asList('W', 'G', 'B', 'E', 'H', 'X');
    //instance
    private int gameId;
    private int tileId;
    private int xPos;
    private int yPos;
    private char resourceTypeId;
    private int value;
    private boolean hasRobber;

    public TileModel(int gameId, int tileId, int xPos, int yPos, char resourceTypeId, int value) {
        setGameId(gameId);
        setTileId(tileId);
        setXPos(xPos);
        setYPos(yPos);
        setResourceTypeId(resourceTypeId);
        setValue(value);
        setHasRobber(false);
    }

    //getters
    public int getGameId() {
        return gameId;
    }

    public int getTileId() {
        return tileId;
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }

    public char getResourceTypeId() {
        return resourceTypeId;
    }

    public int getValue() {
        return value;
    }

    public boolean hasRobber() {
        return hasRobber;
    }
    
    public static TileModel getFromDatabase(int row, int gameID) {
        if (DBConnection.checkConnection()) {
            try {
                PreparedStatement preparedStatement = DBConnection.connection.prepareStatement("SELECT * FROM tegels WHERE idspel = ? ORDER BY idspel, idtegel LIMIT 1 OFFSET ?;");
                // 11 is a random idspel in the database we still need to replace this with our own
                preparedStatement.setInt(1, gameID);
                preparedStatement.setInt(2, row);

                ResultSet resultSet = preparedStatement.executeQuery();
                return getTileModelFromResultSet(resultSet);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return null;
    }

    public static TileModel[] getAllFromDatabase() {
        if (DBConnection.checkConnection()) {
            try {
                Statement statement = DBConnection.connection.createStatement();

                ResultSet resultSet = statement.executeQuery("SELECT * FROM tegels ORDER BY idspel, idtegel;");
                return getAllTileModelsFromResultSet(resultSet);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return new TileModel[0];
    }

    private static TileModel getTileModelFromResultSet(ResultSet resultSet) {
        try {
            if (resultSet.next()) {
                int gameId = resultSet.getInt("idspel");
                int tileId = resultSet.getInt("idtegel");
                int x = resultSet.getInt("x");
                int y = resultSet.getInt("y");
                String resourceTypeIdString = resultSet.getString("grondstof");
                int value = resultSet.getInt("waarde");

                char resourceTypeId = 'X';
                if (resourceTypeIdString.length() == 1) {
                    resourceTypeId = resourceTypeIdString.charAt(0);
                }

                return new TileModel(gameId, tileId, x, y, resourceTypeId, value);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static TileModel[] getAllTileModelsFromResultSet(ResultSet resultSet) {
        ArrayList<TileModel> tileModels = new ArrayList<>();

        try {
            while (resultSet.next()) {
                int gameId = resultSet.getInt("idspel");
                int tileId = resultSet.getInt("idtegel");
                int x = resultSet.getInt("x");
                int y = resultSet.getInt("y");
                String resourceTypeIdString = resultSet.getString("grondstof");
                int value = resultSet.getInt("waarde");

                char resourceTypeId = 'X';
                if (resourceTypeIdString.length() == 1) {
                    resourceTypeId = resourceTypeIdString.charAt(0);
                }

                tileModels.add(new TileModel(gameId, tileId, x, y, resourceTypeId, value));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tileModels.toArray(new TileModel[tileModels.size()]);
    }

    //setter
    private void setHasRobber(boolean hasRobber) {
        this.hasRobber = hasRobber;
    }

    private void setGameId(int gameId) {
        this.gameId = gameId;
    }

    private void setTileId(int tileId) {
        if (tileId < 1 || tileId > 19) {
            throw new IllegalArgumentException("Illegal value for tileId");
        }

        this.tileId = tileId;
    }

    private void setXPos(int xPos) {
        if (xPos < 1 || xPos > 11) {
            throw new IllegalArgumentException("Illegal value for xPos");
        }

        this.xPos = xPos;
    }

    private void setYPos(int yPos) {
        if (yPos < 1 || yPos > 11) {
            throw new IllegalArgumentException("Illegal value for yPos");
        }

        this.yPos = yPos;
    }

    private void setResourceTypeId(char resourceTypeId) {
        if (!allowedResourceTypeIDs.contains(resourceTypeId)) {
            throw new IllegalArgumentException("Illegal value for resourceTypeId");
        }

        this.resourceTypeId = resourceTypeId;
    }

    private void setValue(int value) {
        if (resourceTypeId != 'X' && (value < 2 || value > 12)) {
            throw new IllegalArgumentException("Illegal value for value");
        }

        this.value = value;
    }


    


}
