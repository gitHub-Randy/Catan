package model;

import database.DBConnection;

import java.sql.*;
import java.util.ArrayList;

public class Road extends Structure {
	//instance
    private int xPos2;
    private int yPos2;

    public Road(String structureId, int userId, int xPos1, int yPos1, int xPos2, int yPos2) {
        super(structureId, userId, xPos1, yPos1);
        setXPos2(xPos2);
        setYPos2(yPos2);
    }

    
    //getters
    public int getXPos2() {
        return xPos2;
    }

    public int getYPos2() {
        return yPos2;
    }
    
    public static Road getFromDatabase(int gameId, String roadId) {
        if (!roadId.startsWith("r")) {
            throw new IllegalArgumentException("Illegal value for roadId");
        }
        if (DBConnection.checkConnection()) {
            try {
                PreparedStatement preparedStatement = DBConnection.connection.prepareStatement("SELECT idstuk, ss.idspeler, x_van, y_van, x_naar, y_naar FROM spelerstuk ss JOIN speler s ON ss.idspeler = s.idspeler WHERE idspel = ? AND idstuk = ?;");

                preparedStatement.setInt(1, gameId);
                preparedStatement.setString(2, roadId);

                ResultSet resultSet = preparedStatement.executeQuery();
                return getRoadFromResultSet(resultSet);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return null;
    }

    public static Road[] getAllFromDatabase() {
        if (DBConnection.checkConnection()) {
            try {
                Statement statement = DBConnection.connection.createStatement();

                ResultSet resultSet = statement.executeQuery("SELECT * FROM spelerstuk WHERE idstuk LIKE 'r%';");
                return getAllRoadsFromResultSet(resultSet);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return new Road[0];
    }

    public static Road[] getAllFromDatabaseWithUserId(int userId) {
        if (DBConnection.checkConnection()) {
            try {
                PreparedStatement preparedStatement = DBConnection.connection.prepareStatement("SELECT * FROM spelerstuk WHERE idstuk LIKE 'r%' AND idspeler = ?;");

                preparedStatement.setInt(1, userId);

                ResultSet resultSet = preparedStatement.executeQuery();
                return getAllRoadsFromResultSet(resultSet);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return new Road[0];
    }

    private static Road getRoadFromResultSet(ResultSet resultSet) {
        try {
            if (resultSet.next()) {
                String structureId = resultSet.getString("idstuk");
                int userId = resultSet.getInt("idspeler");
                int xPos1 = resultSet.getInt("x_van");
                int yPos1 = resultSet.getInt("y_van");
                int xPos2 = resultSet.getInt("x_naar");
                int yPos2 = resultSet.getInt("y_naar");

                return new Road(structureId, userId, xPos1, yPos1, xPos2, yPos2);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static Road[] getAllRoadsFromResultSet(ResultSet resultSet) {
        ArrayList<Road> roads = new ArrayList<>();

        try {
            while (resultSet.next()) {
                String structureId = resultSet.getString("idstuk");
                int userId = resultSet.getInt("idspeler");
                int xPos1 = resultSet.getInt("x_van");
                int yPos1 = resultSet.getInt("y_van");
                int xPos2 = resultSet.getInt("x_naar");
                int yPos2 = resultSet.getInt("y_naar");

                roads.add(new Road(structureId, userId, xPos1, yPos1, xPos2, yPos2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return roads.toArray(new Road[roads.size()]);
    }
    //setters

    public void setXPos2(int xPos2) {
        if (xPos2 < 0 || xPos2 > 11) {
            throw new IllegalArgumentException("Illegal value for xPos2");
        }

        this.xPos2 = xPos2;
    }

    public void setYPos2(int yPos2) {
        if (yPos2 < 0 || yPos2 > 11) {
            throw new IllegalArgumentException("Illegal value for yPos2");
        }

        this.yPos2 = yPos2;
    }

    //updates
    @Override
    public void updateInDatabase() {
        if (DBConnection.checkConnection()) {
            try {
                PreparedStatement preparedStatement = DBConnection.connection.prepareStatement("UPDATE spelerstuk SET x_van = ?, y_van = ?, x_naar = ?, y_naar = ? WHERE idstuk = ? AND idspeler = ?;");

                preparedStatement.setInt(1, getXPos1());
                preparedStatement.setInt(2, getYPos1());
                preparedStatement.setInt(3, getXPos2());
                preparedStatement.setInt(4, getYPos2());
                preparedStatement.setString(5, getStructureId());
                preparedStatement.setInt(6, getUserId());

                preparedStatement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    //inserts
    @Override
    public void insertInDatabase() {

        if (DBConnection.checkConnection()) {
            try {
                PreparedStatement preparedStatement = DBConnection.connection.prepareStatement("INSERT INTO spelerstuk (idstuk, idspeler, x_van, y_van, x_naar, y_naar) VALUES (?, ?, ?, ?, ?, ?);");

                preparedStatement.setString(1, getStructureId());
                preparedStatement.setInt(2, getUserId());
                if (getXPos1() == 0) {
                    preparedStatement.setNull(3, Types.INTEGER);
                } else {
                    preparedStatement.setInt(3, getXPos1());
                }
                if (getYPos1() == 0) {
                    preparedStatement.setNull(4, Types.INTEGER);
                } else {
                    preparedStatement.setInt(4, getYPos1());
                }
                if (getXPos2() == 0) {
                    preparedStatement.setNull(5, Types.INTEGER);
                } else {
                    preparedStatement.setInt(5, getXPos2());
                }
                if (getYPos2() == 0) {
                    preparedStatement.setNull(6, Types.INTEGER);
                } else {
                    preparedStatement.setInt(6, getYPos2());
                }

                preparedStatement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String toString() {
        return "Road{" +
                "structureId='" + getStructureId() + "'" +
                ", userId=" + getUserId() +
                ", xPos1=" + getXPos1() +
                ", yPos1=" + getYPos1() +
                ", xPos2=" + xPos2 +
                ", yPos2=" + yPos2 +
                '}';
    }



}

