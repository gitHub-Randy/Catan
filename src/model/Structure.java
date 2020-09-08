package model;

import database.DBConnection;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

public abstract class Structure {
    //instance
    private String structureId;
    private int userId;
    private int xPos1;
    private int yPos1;
    private String Color;
    private boolean played;

    public Structure(String structureId, int userId, int xPos1, int yPos1) {
        setStructureID(structureId);
        setUserId(userId);
        setXPos1(xPos1);
        setYPos1(yPos1);
    }

    //getters
    public String getStructureId() {
        return structureId;
    }

    public int getUserId() {
        return userId;
    }

    public int getXPos1() {
        return xPos1;
    }

    public int getYPos1() {
        return yPos1;
    }

    public String getColor() {
        return Color;
    }

    public boolean isPlayed() {
        return played;
    }

    public char getStructureType() {
        // hier wordt structureID getrimmed van bijv. c01 naar 'c'.
        char structureType = structureId.charAt(0);
        // String structureNumber = structureID.substring(1);
        return structureType;
    }

    //setters
    public void setXPos1(int xPos1) {
        if (xPos1 < 0 || xPos1 > 11) {

            throw new IllegalArgumentException("Illegal value for xPos1");
        }

        this.xPos1 = xPos1;
    }

    public void setYPos1(int yPos1) {
        if (yPos1 < 0 || yPos1 > 11) {
            throw new IllegalArgumentException("Illegal value for yPos1");
        }

        this.yPos1 = yPos1;
    }

    public void setPlayed(boolean played) {
        this.played = played;
    }

    private void setStructureID(String structureId) {
        if (structureId == null || structureId.length() != 3) {
            throw new IllegalArgumentException("Illegal value for structureId");
        }

        this.structureId = structureId;
    }

    private void setUserId(int userId) {
        this.userId = userId;
    }

    //update
    public void updateInDatabase() {
        if (DBConnection.checkConnection()) {
            try {
                PreparedStatement preparedStatement = DBConnection.connection.prepareStatement("UPDATE spelerstuk SET x_van = ?, y_van = ? WHERE idstuk = ? AND idspeler = ?;");

                preparedStatement.setInt(1, xPos1);
                preparedStatement.setInt(2, yPos1);
                preparedStatement.setString(3, structureId);
                preparedStatement.setInt(4, userId);

                preparedStatement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    //delete
    public void insertInDatabase() {
        if (DBConnection.checkConnection()) {
            try {
                PreparedStatement preparedStatement = DBConnection.connection.prepareStatement("INSERT INTO spelerstuk (idstuk, idspeler, x_van, y_van) VALUES (?, ?, ?, ?);");

                preparedStatement.setString(1, structureId);
                preparedStatement.setInt(2, userId);
                if (xPos1 == 0) {
                    preparedStatement.setNull(3, Types.INTEGER);
                } else {
                    preparedStatement.setInt(3, xPos1);
                }
                if (yPos1 == 0) {
                    preparedStatement.setNull(4, Types.INTEGER);
                } else {
                    preparedStatement.setInt(4, yPos1);
                }

                preparedStatement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


}
