package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import database.DBConnection;

public class LocationModel {
	//Constants
	private final List<Character> allowedHarborResourceTypeIDs = Arrays.asList('W', 'G', 'B', 'E', 'H', 'X', '.');
	//instance
    private int x;
    private int y;
	private boolean harbor;
    private char harborResourceTypeId; // '.' is geen resource, want een char kan niet null zijn & 'x' is al bezet (woestijn)
    private Settlement settlement;
    private Road[] roads;

    public LocationModel(int x, int y, boolean harbor, char harborResourceTypeId) {
        setX(x);
        setY(y);
        setHarbor(harbor);
        setHarborResourceTypeId(harborResourceTypeId);
        roads = new Road[3];
    }

   
    //getters
    public Road[] getRoads() {
		return roads;
	}
    
	public int getX() {
        return x;
    }
	
    public boolean isHarbor() {
        return harbor;
    }

    public int getY() {
        return y;
    }
    
	public boolean locationHasStructure() {
		if(settlement == null && roads[0] == null && roads[1] == null && roads[2] == null) {
			return false;
		}else {
			return true;
		}
	}
	
    public char getHarborResourceTypeId() {
        return harborResourceTypeId;
    }

    public Settlement getSettlement() {
        return settlement;
    }
	
	//andere has roads functie die checked of een locatie een road of roads bevat
	public boolean hasRoads(int userid) {
		boolean locBool = false;
		for(int i = 0; i < roads.length; i++) {
				if(roads[i] != null) {
				if(roads[i].getUserId() == userid) {
					locBool = true;
					return locBool;
				}
			}
		}
		return locBool;
	}
 
	public static LocationModel getFromDatabase(int row) {
        if (DBConnection.checkConnection()) {
            try {
                PreparedStatement preparedStatement = DBConnection.connection.prepareStatement("SELECT * FROM locatie ORDER BY x, y LIMIT 1 OFFSET ?;");

                preparedStatement.setInt(1, row);

                ResultSet resultSet = preparedStatement.executeQuery();
                return getLocationModelFromResultSet(resultSet);
            } catch (SQLException ex) {
                ex.printStackTrace();
            } 
        }

        return null;
    }

    public static LocationModel getFromDatabase(int x, int y) {
        if (DBConnection.checkConnection()) {
            try {
                PreparedStatement preparedStatement = DBConnection.connection.prepareStatement("SELECT * FROM locatie WHERE x = ? AND y = ?;");

                preparedStatement.setInt(1, x);
                preparedStatement.setInt(2, y);

                ResultSet resultSet = preparedStatement.executeQuery();
                return getLocationModelFromResultSet(resultSet);
            } catch (SQLException ex) {
                ex.printStackTrace();
            } 
        }

        return null;
    }

    public static LocationModel[] getAllFromDatabase() {
        if (DBConnection.checkConnection()) {
            try {
                Statement statement = DBConnection.connection.createStatement();

                ResultSet resultSet = statement.executeQuery("SELECT * FROM locatie;");
                return getAllLocationModelsFromResultSet(resultSet);
            } catch (SQLException ex) {
                ex.printStackTrace();
            } 
        }

        return new LocationModel[0];
    }
    
    public LocationModel getLocModel() {
    	return this;
    }
	//setter
	public void setRoads(Road road) {
		for(int i = 0; i < roads.length; i++) {
			if(roads[i] == null) {
				roads[i] = road;
				return;
			}
		}
	}

    public void setSettlement(Settlement settlement) {
        this.settlement = settlement;
    }

    private void setX(int x) {
        if (x < 1 || x > 11) {
            throw new IllegalArgumentException("Illegal value for x");
        }

        this.x = x;
    }

    private void setY(int y) {
        if (y < 1 || y > 11) {
            throw new IllegalArgumentException("Illegal value for y");
        }

        this.y = y;
    }

    private void setHarbor(boolean harbor) {
        this.harbor = harbor;
    }

    private void setHarborResourceTypeId(char harborResourceTypeId) {
        if (!allowedHarborResourceTypeIDs.contains(harborResourceTypeId)) {
            throw new IllegalArgumentException("Illegal value for harborResourceTypeId");
        }

        this.harborResourceTypeId = harborResourceTypeId;
    }
  
    private static LocationModel getLocationModelFromResultSet(ResultSet resultSet) {
        try {
            if (resultSet.next()) {
                int x = resultSet.getInt("x");
                int y = resultSet.getInt("y");
                boolean hasHarbor = resultSet.getBoolean("haven");
                String resourceTypeIDString = resultSet.getString("idgrondstofsoort");

                char resourceTypeID = '.';
                if (resourceTypeIDString != null && resourceTypeIDString.length() == 1) {
                    resourceTypeID = resourceTypeIDString.charAt(0);
                }

                return new LocationModel(x, y, hasHarbor, resourceTypeID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static LocationModel[] getAllLocationModelsFromResultSet(ResultSet resultSet) {
        ArrayList<LocationModel> locationModels = new ArrayList<>();

        try {
            while (resultSet.next()) {
                int x = resultSet.getInt("x");
                int y = resultSet.getInt("y");
                boolean hasHarbor = resultSet.getBoolean("haven");
                String resourceTypeIDString = resultSet.getString("idgrondstofsoort");

                char resourceTypeID = '.';
                if (resourceTypeIDString != null && resourceTypeIDString.length() == 1) {
                    resourceTypeID = resourceTypeIDString.charAt(0);
                }

                locationModels.add(new LocationModel(x, y, hasHarbor, resourceTypeID));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return locationModels.toArray(new LocationModel[locationModels.size()]);
    }
    //update
    public void updateInDatabase() {
        if (DBConnection.checkConnection()) {
            try {
                PreparedStatement preparedStatement = DBConnection.connection.prepareStatement("UPDATE locatie SET haven = ?, idgrondstofsoort = ? WHERE x = ? AND y = ?;");

                preparedStatement.setBoolean(1, harbor);
                preparedStatement.setString(2, String.valueOf(harborResourceTypeId));
                preparedStatement.setInt(3, x);
                preparedStatement.setInt(4, y);

                preparedStatement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    //inserts
    public void insertInDatabase() {
        if (DBConnection.checkConnection()) {
            try {
                PreparedStatement preparedStatement = DBConnection.connection.prepareStatement("INSERT INTO locatie (x, y, haven, idgrondstofsoort) VALUES (?, ?, ?, ?);");

                preparedStatement.setInt(1, x);
                preparedStatement.setInt(2, y);
                preparedStatement.setBoolean(3, harbor);
                preparedStatement.setString(4, String.valueOf(harborResourceTypeId));

                preparedStatement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            } 
        }
    }
    //prints
    @Override
    public String toString() {
        return "LocationModel{" +
                "x=" + x +
                ", y=" + y +
                ", harbor=" + harbor +
                ", harborResourceTypeId=" + harborResourceTypeId +
                ", settlement=" + settlement +
                '}';
    }
    
    public void printRoads() {
    }

}
