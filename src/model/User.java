package model;

import database.DBConnection;

import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class User {
	// constants
	private static final List<String> allowedColorValues = Arrays.asList("rood", "wit", "blauw", "oranje");
	private static final List<String> allowedGameStatusValues = Arrays.asList("uitdager", "uitgedaagde", "geaccepteerd",
			"geweigerd", "uitgespeeld", "afgebroken");
	// instance
	private int userId;
	private int gameId;
	private String username;
	private String color;
	private String gameStatus;
	private boolean shouldRefresh;
	private int number;
	private boolean hasArmy;
	private boolean hasTradeRoute;
	private Village[] villages;
	private City[] cities;
	private Road[] roads;
	private int victoryPoints = 0;
	private ArrayList<ResourceCard> playResourceCards;
	private ArrayList<DevelopmentCard> playDevelopmentCards;
	private int amountOfSpendKnight;

    public User(int userId, int gameId, String username, String color, String gameStatus, boolean shouldRefresh,
                int number) {
        setUserId(userId);
        setGameId(gameId);
        setUsername(username);
        setColor(color);
        setGameStatus(gameStatus);
        setShouldRefresh(shouldRefresh);
        setNumber(number);

        villages = new Village[5];
        cities = new City[4];
        roads = new Road[15];
        playResourceCards = new ArrayList<>();
        playDevelopmentCards = new ArrayList<>();

        fillStructureArrays();
        fillCardLists();
    }

    public void setVictoryPoints(int victoryPoints) {
        this.victoryPoints = victoryPoints;
    }

    // add
    public void addWood(List<WoodCard> woodCards) {
        playResourceCards.addAll(woodCards);
        for (WoodCard woodCard : woodCards) {
            woodCard.setUserId(userId);
            woodCard.updateInDatabase();
        }
    }

    public void addClay(List<ClayCard> clayCards) {
        playResourceCards.addAll(clayCards);
        for (ClayCard clayCard : clayCards) {
            clayCard.setUserId(userId);
            clayCard.updateInDatabase();
        }
    }

    public void addGrain(List<GrainCard> grainCards) {
        playResourceCards.addAll(grainCards);
        for (GrainCard grainCard : grainCards) {
            grainCard.setUserId(userId);
            grainCard.updateInDatabase();
        }
    }

    public void addSheep(List<SheepCard> sheepCards) {
        playResourceCards.addAll(sheepCards);
        for (SheepCard sheepCard : sheepCards) {
            sheepCard.setUserId(userId);
            sheepCard.updateInDatabase();
        }
    }

    public void addOre(List<OreCard> oreCards) {
        playResourceCards.addAll(oreCards);
        for (OreCard oreCard : oreCards) {
            oreCard.setUserId(userId);
            oreCard.updateInDatabase();
        }
    }

	// spend
	public ArrayList<ClayCard> spendClay(int amount) {
		ArrayList<ClayCard> cc = new ArrayList<>();
		for (int a = 0; a < amount; a++) {
			for (int i = 0; i < playResourceCards.size(); i++) {
				if (playResourceCards.get(i).getResourceTypeId() == 'B') {

						cc.add( playResourceCards.get(i).convertToClayCard());
						playResourceCards.get(i).setUserId(0);
						playResourceCards.get(i).updateInDatabase();
						playResourceCards.remove(i);
						break;

				}
			}
		}
		return cc;
	}

	public ArrayList<SheepCard> spendSheep(int amount) {
		ArrayList<SheepCard> cc = new ArrayList<>();
		for (int a = 0; a < amount; a++) {
			for (int i = 0; i < playResourceCards.size(); i++) {
				if (playResourceCards.get(i).getResourceTypeId() == 'W') {

						cc.add( playResourceCards.get(i).convertToSheepCard());
						playResourceCards.get(i).setUserId(0);
						playResourceCards.get(i).updateInDatabase();
						playResourceCards.remove(i);
						break;

				}
			}
		}
		return cc;
	}

    public ArrayList<OreCard> spendOre(int amount) {
        ArrayList<OreCard> cc = new ArrayList<>();
        for (int a = 0; a < amount; a++) {
            for (int i = 0; i < playResourceCards.size(); i++) {
                if (playResourceCards.get(i).getResourceTypeId() == 'E') {
                    cc.add(playResourceCards.get(i).convertToOreCard());
                    playResourceCards.get(i).setUserId(0);
                    playResourceCards.get(i).updateInDatabase();
                    playResourceCards.remove(i);
                    break;
                }
            }
        }
        return cc;
    }

    public ArrayList<WoodCard> spendWood(int amount) {
        ArrayList<WoodCard> cc = new ArrayList<>();
        for (int a = 0; a < amount; a++) {
            for (int i = 0; i < playResourceCards.size(); i++) {
                if (playResourceCards.get(i).getResourceTypeId() == 'H') {
                    cc.add(playResourceCards.get(i).convertToWoodCard());
                    playResourceCards.get(i).setUserId(0);
                    playResourceCards.get(i).updateInDatabase();
                    playResourceCards.remove(i);
                    break;
                }
            }
        }
        return cc;
    }

    public ArrayList<GrainCard> spendGrain(int amount) {
        ArrayList<GrainCard> cc = new ArrayList<>();
        for (int a = 0; a < amount; a++) {
            for (int i = 0; i < playResourceCards.size(); i++) {
                if (playResourceCards.get(i).getResourceTypeId() == 'G') {
                    cc.add(playResourceCards.get(i).convertToGrainCard());
                    playResourceCards.get(i).setUserId(0);
                    playResourceCards.get(i).updateInDatabase();
                    playResourceCards.remove(i);
                    break;
                }
            }
        }
        return cc;
    }

    public void addDevelopmentCard(DevelopmentCard card) {
        playDevelopmentCards.add(card);
    }

    public Road[] getRoadArray() {
        return roads;
    }

    public int getSpendKnights() {
        return amountOfSpendKnight;
    }

    // getters
    public int getVictoryPoints() {
        return victoryPoints;
    }

    public int getUserId() {
        return userId;
    }

    public int getGameId() {
        return gameId;
    }

    public String getUsername() {
        return username;
    }

    public String getColor() {
        return color;
    }

    public String getGameStatus() {
        return gameStatus;
    }

    public boolean shouldRefresh() {
        return shouldRefresh;
    }

    public int getNumber() {
        return number;
    }

    public Village[] getVillages() {
        return villages;
    }

    public City[] getCities() {
        return cities;
    }

    public Road[] getRoads() {
        return roads;
    }

    public boolean hasArmy() {
        return hasArmy;
    }

    public int amountOfDevCardBuildings() {
        int amount = 0;
        for (DevelopmentCard playDevelopmentCard : playDevelopmentCards) {
            if (playDevelopmentCard.isPlayed()) {
                if (playDevelopmentCard.getType().equals("gebouw")) {
                    amount++;
                }
            }
        }
        return amount;
    }

    public boolean hasTradeRoute() {
        return hasTradeRoute;
    }

    public int getWoodAmount() {
        return (int) playResourceCards.stream().filter(resourceCard -> resourceCard.getResourceTypeId() == 'H').count();
    }

    public int getClayAmount() {
        return (int) playResourceCards.stream().filter(resourceCard -> resourceCard.getResourceTypeId() == 'B').count();
    }

    public int getGrainAmount() {
        return (int) playResourceCards.stream().filter(resourceCard -> resourceCard.getResourceTypeId() == 'G').count();
    }

    public int getSheepAmount() {
        return (int) playResourceCards.stream().filter(resourceCard -> resourceCard.getResourceTypeId() == 'W').count();
    }

    public int getOreAmount() {
        return (int) playResourceCards.stream().filter(resourceCard -> resourceCard.getResourceTypeId() == 'E').count();
    }

    public boolean checkForVillage(int x, int y) {
        for (Village village : villages) {
            if (village.getXPos1() == x && village.getYPos1() == y) {
                return true;
            }
        }
        return false;
    }

    public Settlement getVillage() {
        for (Village village : villages) {
            if (!village.isPlayed()) {
                return village;
            }
        }

        return null;
    }

    public Settlement getRemovedVillage(int x, int y) {
        Settlement settlement = null;
    	
    	for (int i = 0; i < villages.length; i++) {
    		Village village = villages[i];
            if (village.getXPos1() == x && village.getYPos1() == y) {
                settlement = village;
                villages[i] = null;
            }
        }
    	
        return settlement;
    }

    public Settlement getCity() {
        for (City city : cities) {
            if (!city.isPlayed()) {
                return city;
            }

        }
        return null;
    }

    public Road getRoad() {
        for (Road road : roads) {
            if (!road.isPlayed()) {
                return road;
            }
        }

        return null;
    }

    public static User getFromDatabase(int traceNR, int gameID) {
        if (DBConnection.checkConnection()) {
            try {
                PreparedStatement preparedStatement = DBConnection.connection
                        .prepareStatement("SELECT * FROM speler WHERE volgnr = ? AND idspel = ?;");

                preparedStatement.setInt(1, traceNR);
                preparedStatement.setInt(2, gameID);

                ResultSet resultSet = preparedStatement.executeQuery();

                return getUserFromResultSet(resultSet);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public static User getFromDatabase(int userId) {
        if (DBConnection.checkConnection()) {
            try {
                PreparedStatement preparedStatement = DBConnection.connection
                        .prepareStatement("SELECT * FROM speler WHERE idspeler = ?;");

                preparedStatement.setInt(1, userId);

                ResultSet resultSet = preparedStatement.executeQuery();
                return getUserFromResultSet(resultSet);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public static User[] getAllFromDatabase() {
        if (DBConnection.checkConnection()) {
            try {
                Statement statement = DBConnection.connection.createStatement();

                ResultSet resultSet = statement.executeQuery("SELECT * FROM speler;");
                return getAllUsersFromResultSet(resultSet);
            } catch (SQLException e) {
                e.printStackTrace();

            }
        }

        return new User[0];
    }

    public static User[] getAllFromDatabaseWithGameId(int gameId) {
        if (DBConnection.checkConnection()) {
            try {
                PreparedStatement preparedStatement = DBConnection.connection
                        .prepareStatement("SELECT * FROM speler WHERE idspel = ? ORDER BY volgnr;");

                preparedStatement.setInt(1, gameId);

                ResultSet resultSet = preparedStatement.executeQuery();
                return getAllUsersFromResultSet(resultSet);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return new User[0];
    }

    private <T> ArrayList<T> getArrayListFromArray(T[] array) {
        return new ArrayList<>(Arrays.asList(array));
    }

    private static User getUserFromResultSet(ResultSet resultSet) {
        try {
            if (resultSet.next()) {
                int userId = resultSet.getInt("idspeler");
                int gameId = resultSet.getInt("idspel");
                String username = resultSet.getString("username");
                String color = resultSet.getString("kleur");
                String gameStatus = resultSet.getString("speelstatus");
                boolean shouldRefresh = resultSet.getBoolean("shouldrefresh");
                int number = resultSet.getInt("volgnr");

                return new User(userId, gameId, username, color, gameStatus, shouldRefresh, number);

            }
        } catch (SQLException e) {

        }

        return null;
    }

    private static User[] getAllUsersFromResultSet(ResultSet resultSet) {
        ArrayList<User> users = new ArrayList<>();

        try {
            if (resultSet == null) {
                return new User[0];
            }

            while (resultSet.next()) {
                int userId = resultSet.getInt("idspeler");
                int gameId = resultSet.getInt("idspel");
                String username = resultSet.getString("username");
                String color = resultSet.getString("kleur");
                String gameStatus = resultSet.getString("speelstatus");
                boolean shouldRefresh = resultSet.getBoolean("shouldrefresh");
                int number = resultSet.getInt("volgnr");

                users.add(new User(userId, gameId, username, color, gameStatus, shouldRefresh, number));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users.toArray(new User[0]);
    }

    public ArrayList<ResourceCard> getPlayResourceCards() {
        return playResourceCards;
    }

    public ArrayList<DevelopmentCard> getPlayDevelopmentCards() {
        return playDevelopmentCards;
    }

    // setters
    public void setHasArmy(boolean hasArmy) {
        this.hasArmy = hasArmy;
    }

    public void setHasTradeRoute(boolean hasTradeRoute) {
        this.hasTradeRoute = hasTradeRoute;
    }

    public void setColor(String color) {
        if (!allowedColorValues.contains(color)) {
            throw new IllegalArgumentException("Illegal value for color");
        }

        this.color = color;
    }

    public void setGameStatus(String gameStatus) {
        if (!allowedGameStatusValues.contains(gameStatus)) {
            throw new IllegalArgumentException("Illegal value for gameStatus");
        }

        this.gameStatus = gameStatus;
    }

    public void setShouldRefresh(boolean shouldRefresh) {
        this.shouldRefresh = shouldRefresh;
    }

    private void setUserId(int userId) {
        this.userId = userId;
    }

    private void setGameId(int gameId) {
        this.gameId = gameId;
    }

    private void setUsername(String username) {
        if (username == null || username.length() > 25) {
            throw new IllegalArgumentException("Illegal value for username");
        }

        this.username = username;
    }

    private void setNumber(int number) {
        this.number = number;
    }

    // update
    public void updateInDatabase() {
        if (DBConnection.checkConnection()) {
            try {
                PreparedStatement preparedStatement = DBConnection.connection.prepareStatement(
                        "UPDATE speler SET speelstatus = ?, shouldrefresh = ? WHERE idspel = ? AND idspeler = ?;");

                preparedStatement.setString(1, gameStatus);
                preparedStatement.setBoolean(2, shouldRefresh);
                preparedStatement.setInt(3, gameId);
                preparedStatement.setInt(4, userId);

                preparedStatement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // insert
    public void insertInDatabase() {
        if (DBConnection.checkConnection()) {
            try {
                PreparedStatement preparedStatement = DBConnection.connection.prepareStatement(
                        "INSERT INTO speler (idspeler, idspel, username, kleur, speelstatus, shouldrefresh, volgnr) VALUES (?, ?, ?, ?, ?, ?, ?)");

                preparedStatement.setInt(1, userId);
                preparedStatement.setInt(2, gameId);
                preparedStatement.setString(3, username);
                preparedStatement.setString(4, color);
                preparedStatement.setString(5, gameStatus);
                preparedStatement.setBoolean(6, shouldRefresh);
                preparedStatement.setInt(7, number);

                preparedStatement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String toString() {
        return "User{" + "userId=" + userId + ", gameId=" + gameId + ", username='" + username + '\'' + ", color='"
                + color + '\'' + ", gameStatus='" + gameStatus + '\'' + ", shouldRefresh=" + shouldRefresh + ", number="
                + number + '}';
    }

    // fills
    private void fillStructureArrays() {
        fillVillageArray();
        fillCityArray();
        fillRoadArray();
    }

    private void fillVillageArray() {
        villages = Village.getAllFromDatabaseWithUserId(userId);
    }

    private void fillCityArray() {
        cities = City.getAllFromDatabaseWithUserId(userId);
    }

    private void fillRoadArray() {
        roads = Road.getAllFromDatabaseWithUserId(userId);
    }

    private void fillCardLists() {
        fillResourceCardList();
        fillDevelopmentCardList();
    }

    private void fillResourceCardList() {
        playResourceCards = getArrayListFromArray(ResourceCard.getAllFromDatabaseWithUserId(userId));
    }

    private void fillDevelopmentCardList() {
        playDevelopmentCards = getArrayListFromArray(DevelopmentCard.getAllFromDatabaseWithUserId(userId));
    }

    public void countKnightsPlayed() {
        amountOfSpendKnight = 0;
        for (DevelopmentCard playDevelopmentCard : playDevelopmentCards) {
            if (playDevelopmentCard.getName().equals("ridder") && playDevelopmentCard.isPlayed()) {
                amountOfSpendKnight++;
            }
        }
    }

    public Color switchColors() {
        Color userColor;
        switch (color) {
            case "blauw":
                userColor = Color.BLUE;
                break;
            case "rood":
                userColor = Color.red;
                break;
            case "wit":
                userColor = Color.WHITE;
                break;
            case "oranje":
                userColor = Color.orange;
                break;
            default:
                userColor = Color.BLACK;
        }

        return userColor;
    }

    public String getNextVillageId() {
        return getNextStructureId('d');
    }

    public String getNextCityId() {
        return getNextStructureId('c');
    }

    public String getNextRoadId() {
        return getNextStructureId('r');
    }

    private String getNextStructureId(char prefix) {
        try {
            if (DBConnection.checkConnection()) {
                PreparedStatement preparedStatement = DBConnection.connection.prepareStatement(
                        "SELECT MIN(idstuk) idstuk FROM spelerstuk WHERE idspeler = ? AND idstuk LIKE ? AND x_van IS NULL AND y_van IS NULL ORDER BY idstuk;");

                preparedStatement.setInt(1, userId);
                preparedStatement.setString(2, prefix + "%");

                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    return resultSet.getString("idstuk");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void addVillage(int x, int y) {
        String structureId = getNextVillageId();

        if (structureId != null) {
            Village village = new Village(structureId, userId, x, y);
            village.setPlayed(true);
            village.updateInDatabase();
            fillStructureArrays();
        }
    }

    public void addCity(int x, int y) {
        String structureId = getNextCityId();

        if (structureId != null) {
            City city = new City(structureId, userId, x, y);
            city.setPlayed(true);
            city.updateInDatabase();
            fillStructureArrays();
        }
    }

    public void addRoad(int x1, int y1, int x2, int y2) {
        String structureId = getNextRoadId();

        if (structureId != null) {
            Road road = new Road(structureId, userId, x1, y1, x2, y2);
            road.setPlayed(true);
            road.updateInDatabase();
            fillStructureArrays();
        }
    }

}