package model;

import controller.Catan;
import controller.Dice;
import database.DBConnection;
import view.BoardView;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Set;

public class CatanModel {
    //instance
    public User[] users; // deze array is altijd 4 groot.
    private int userIndex;
    private int ownUserID;
    private int turnPlayerID;
    private boolean firstTurn;
    //bank card arrays
    private GrainCard[] bread;
    private OreCard[] ores;
    private WoodCard[] forest;
    private ClayCard[] bricks;
    private SheepCard[] flock;
    private ArrayList<DevelopmentCard> devCards;
    //hasThrownDice moet nog iemand uit db trekken;
    private boolean hasThrownDice;
    //robbert location
    @SuppressWarnings("unused")
    private int roverTileLocation;
    //game id
    private int gameID;
    //random
    private Dice dice;
    private Catan catan;

    public CatanModel(Dice dice, Catan catan) {
        this.dice = dice;
        this.catan = catan;
        //the users
        users = new User[4];
        // 14ridders 6 vooruitgang(2 van ieder) 5 overwinningspunt
        //fill cards
        firstTurn = true;

        turnPlayerID = 0;
    }

    public void getCards(int gameID) {
        bread = GrainCard.getAllFromDatabase(gameID);
        ores = OreCard.getAllFromDatabase(gameID);
        forest = WoodCard.getAllFromDatabase(gameID);
        bricks = ClayCard.getAllFromDatabase(gameID);
        flock = SheepCard.getAllFromDatabase(gameID);
        devCards = new ArrayList<>(Arrays.asList(DevelopmentCard.getAllFromDatabase()));
    }

    public void updateFirstRound() {
        if (DBConnection.checkConnection()) {
            try {
                PreparedStatement preparedStatement = DBConnection.connection.prepareStatement("SELECT eersteronde FROM spel WHERE idspel = ?;");

                preparedStatement.setInt(1, gameID);

                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    firstTurn = resultSet.getBoolean("eersteronde");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void nextUserNormalTurn() {
        userIndex = catan.getUserIndexFromID(turnPlayerID);
        userIndex++;

        if (userIndex == 4) {
            userIndex = 0;
        }

        updateTurnPlayerID(catan.getUserIDFromIndex(userIndex));
    }

    //getters
    public int getPlayerIDTurn() {
        return this.turnPlayerID;
    }

    public User[] getUsers() {
        return users;
    }

    public User getUserWithIndex(int index) {
        try {
            return users[index];
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public User getUserWithIndexWithoutOwn(int index) {
        try {
            ArrayList<User> users = new ArrayList<>();
            for (User user : this.users) {
                if (user.getUserId() != ownUserID) {
                    users.add(user);
                }
            }
            return users.get(index);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public User getUser(int userId) {
        return Arrays.stream(users).filter(user -> user.getUserId() == userId).findFirst().orElse(null);
    }

    public int getUserIndex() {
        return userIndex;
    }

    public int getOwnUserID() {
        return ownUserID;
    }

    public boolean isThrown() {
        return this.hasThrownDice;
    }

    public boolean isFirstTurn() {
        return firstTurn;
    }

    public int getWoolAmount() {
        int counter = 0;
        for (SheepCard aFlock : flock) {
            if (aFlock != null) {
                counter++;
            }
        }
        return counter;
    }

    public int getWoodAmount() {
        int counter = 0;
        for (WoodCard aForest : forest) {
            if (aForest != null) {
                counter++;
            }
        }
        return counter;
    }

    public int getClayAmount() {
        int counter = 0;
        for (ClayCard brick : bricks) {
            if (brick != null) {
                counter++;
            }
        }
        return counter;
    }

    public int getGrainAmount() {
        int counter = 0;
        for (GrainCard aBread : bread) {
            if (aBread != null) {
                counter++;
            }
        }
        return counter;
    }

    public int getOreAmount() {
        int counter = 0;
        for (OreCard ore : ores) {
            if (ore != null) {
                counter++;
            }
        }
        return counter;
    }

    public int getRobberLocationFromDB() {
        if (DBConnection.checkConnection()) {
            try {
                Statement statement = DBConnection.connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT struikrover_idtegel FROM spel WHERE idspel = " + this.gameID + ";");
                if (resultSet.next()) {
                    int robber = resultSet.getInt(1);
                    return robber;
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return -1;
    }

    public int getUserIDFromDBWithNumber(int n) {
        if (DBConnection.checkConnection()) {
            try {
                Statement statement = DBConnection.connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT idspeler FROM speler WHERE idspel = " + this.gameID + " AND volgnr = " + n + ";");
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return -1;
    }

    public int getGameID() {
        return gameID;
    }

    public int getLastGameId() {
        if (DBConnection.checkConnection()) {
            try {
                Statement statement = DBConnection.connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT MAX(idspel) FROM spel;");
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return -1;
    }

    public ArrayList<DevelopmentCard> getDevCards() {
        return devCards;
    }

    //add resources
    public void addClay(ArrayList<ClayCard> cc) {
        for (int i = 0; i < cc.size(); i++) {
            for (int j = 0; j < bricks.length; j++) {
                if (bricks[j] == null) {
                    bricks[j] = cc.get(i);
                    bricks[i].setUserId(0);
                    bricks[i].updateInDatabase();
                    break;
                }
            }
        }
    }

    public Settlement getSecondVillage() {
        return getOwnUser().getVillages()[1];
    }

    public void addWood(ArrayList<WoodCard> cc) {
        for (int i = 0; i < cc.size(); i++) {
            for (int j = 0; j < forest.length; j++) {
                if (forest[j] == null) {
                    forest[j] = cc.get(i);
                    forest[i].setUserId(0);
                    forest[i].updateInDatabase();
                    break;
                }
            }
        }
    }

    public void addSheep(ArrayList<SheepCard> cc) {
        for (int i = 0; i < cc.size(); i++) {
            for (int j = 0; j < flock.length; j++) {
                if (flock[j] == null) {
                    flock[j] = cc.get(i);
                    flock[i].setUserId(0);
                    flock[i].updateInDatabase();
                    break;
                }
            }
        }
    }

    public void addOre(ArrayList<OreCard> cc) {
        for (int i = 0; i < cc.size(); i++) {
            for (int j = 0; j < ores.length; j++) {
                if (ores[j] == null) {
                    ores[j] = cc.get(i);
                    //ores[i].setUserId(0);
                    ores[i].updateInDatabase();
                    break;
                }
            }
        }
    }

    public void addGrain(ArrayList<GrainCard> cc) {
        for (int i = 0; i < cc.size(); i++) {
            for (int j = 0; j < bread.length; j++) {
                if (bread[j] == null) {
                    bread[j] = cc.get(i);
                    bread[i].setUserId(0);
                    bread[i].updateInDatabase();
                    break;
                }
            }
        }
    }

    //spend cards
    public ArrayList<WoodCard> spendWood(int amount) {
        ArrayList<WoodCard> cc = new ArrayList<>();

        for (int a = 0; a < amount; a++) {
            for (int i = 0; i < forest.length; i++) {
                if (forest[i] != null) {
                    if (forest[i].getUserId() == 0) {
                        System.out.println(forest[i].getCardId());
                        cc.add(forest[i]);
                        //forest[i].setUserId(userID);
                        forest[i].updateInDatabase();
                        forest[i] = null;
                        break;
                    }
                }
            }
        }
        return cc;
    }

    public ArrayList<ClayCard> spendClay(int amount) {
        ArrayList<ClayCard> cc = new ArrayList<>();
        for (int a = 0; a < amount; a++) {
            for (int i = 0; i < bricks.length; i++) {
                if (bricks[i] != null) {
                    if (bricks[i].getUserId() == 0) {
                        cc.add(bricks[i]);
                        //bricks[i].setUserId(userID);
                        bricks[i].updateInDatabase();
                        bricks[i] = null;
                        break;
                    }
                }
            }
        }
        return cc;
    }

    public ArrayList<SheepCard> spendSheep(int amount) {
        ArrayList<SheepCard> cc = new ArrayList<>();
        for (int a = 0; a < amount; a++) {
            for (int i = 0; i < flock.length; i++) {

                if (flock[i] != null) {
                    if (flock[i].getUserId() == 0) {
                        cc.add(flock[i]);
                        //flock[i].setUserId(userID);
                        flock[i].updateInDatabase();
                        flock[i] = null;
                        break;
                    }
                }
            }
        }
        return cc;
    }

    public ArrayList<OreCard> spendOre(int amount) {
        ArrayList<OreCard> cc = new ArrayList<>();
        for (int a = 0; a < amount; a++) {
            for (int i = 0; i < ores.length; i++) {
                if (ores[i] != null) {
                    if (ores[i].getUserId() == 0) {
                        cc.add(ores[i]);
                        //ores[i].setUserId(userID);
                        ores[i].updateInDatabase();
                        ores[i] = null;
                        break;
                    }
                }
            }
        }
        return cc;
    }

    public ArrayList<GrainCard> spendGrain(int amount) {
        ArrayList<GrainCard> cc = new ArrayList<>();
        for (int a = 0; a < amount; a++) {
            for (int i = 0; i < bread.length; i++) {
                if (bread[i] != null) {
                    if (bread[i].getUserId() == 0) {
                        cc.add(bread[i]);
                        //bread[i].setUserId(userID);
                        bread[i].updateInDatabase();
                        bread[i] = null;
                        break;
                    }
                }
            }
        }
        return cc;
    }

    public DevelopmentCard spendDevelopmentCard() {
        Random random = new Random();
        if (devCards.stream().anyMatch(dC -> dC.getUserId() == 0)) {
            int i = random.nextInt(devCards.size());
            if (devCards.get(i).getUserId() != 0) {
                return spendDevelopmentCard();
            }
            devCards.get(i).setUserId(ownUserID);
            devCards.get(i).updateInDatabase();
            return devCards.get(i);

        }
        return null;
    }

    //updates
    public void updateTurnPlayerID(int newPlayer) {
        if (DBConnection.checkConnection()) {
            try {
                PreparedStatement preparedStatement = DBConnection.connection.prepareStatement("UPDATE spel SET beurt_idspeler = ? WHERE idspel = ?;");

                preparedStatement.setInt(1, newPlayer);
                preparedStatement.setInt(2, gameID);

                preparedStatement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void updateRobberLocationInDB(int newLocationRobber) {
        if (DBConnection.checkConnection()) {
            try {
                PreparedStatement preparedStatement = DBConnection.connection.prepareStatement("UPDATE spel SET struikrover_idtegel = ? WHERE idspel = ?;");

                preparedStatement.setInt(1, newLocationRobber);
                preparedStatement.setInt(2, this.gameID);

                preparedStatement.execute();
            } catch (SQLException e) {
            }
        }
    }

    public void updateInDatabase() {
        User user = getUser(ownUserID);
        user.updateInDatabase();

        update();
    }

    private void update() {
        if (DBConnection.checkConnection()) {
            try {
                PreparedStatement preparedStatement = DBConnection.connection.prepareStatement("UPDATE spel SET gedobbeld = ?, laatste_worp_steen1 = ?, laatste_worp_steen2 = ?, eersteronde = ? WHERE idspel = ?;");

                preparedStatement.setBoolean(1, hasThrownDice);
                preparedStatement.setInt(2, dice.getThrow(0));
                preparedStatement.setInt(3, dice.getThrow(1));
                preparedStatement.setBoolean(4, firstTurn);
                preparedStatement.setInt(5, gameID);

                preparedStatement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void updateFromDatabase() { /*blaze it*/
        if (DBConnection.checkConnection()) {
            try {
                PreparedStatement preparedStatement = DBConnection.connection.prepareStatement("SELECT * FROM spel WHERE idspel = ?;");

                preparedStatement.setInt(1, gameID);

                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    firstTurn = resultSet.getBoolean("eersteronde");
                    int dice1 = resultSet.getInt("laatste_worp_steen1");
                    int dice2 = resultSet.getInt("laatste_worp_steen2");

                    catan.getCatangui().getCatanView().getDiceView().setDiceValue1(dice1);
                    catan.getCatangui().getCatanView().getDiceView().setDiceValue2(dice2);
                }

                updateUsers();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void updateDiceThrownFromDatabase() {
        if (DBConnection.checkConnection()) {
            try {
                PreparedStatement preparedStatement = DBConnection.connection.prepareStatement("SELECT gedobbeld FROM spel WHERE idspel = ?;");

                preparedStatement.setInt(1, gameID);

                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    hasThrownDice = resultSet.getBoolean("gedobbeld");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    //create game
    public int createGame() {
        int gameId = getLastGameId() + 1;

        if (DBConnection.checkConnection()) {
            try {
                PreparedStatement preparedStatement = DBConnection.connection.prepareStatement("INSERT INTO spel VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");

                preparedStatement.setInt(1, gameId);            // idspel
                preparedStatement.setNull(2, Types.INTEGER);    // beurt_idspeler
                preparedStatement.setNull(3, Types.INTEGER);    // grootste_rm_idspeler
                preparedStatement.setNull(4, Types.INTEGER);    // langste_hr_idspeler
                preparedStatement.setBoolean(5, false);      // gedobbeld
                preparedStatement.setNull(6, Types.INTEGER);    // laatste_worp_steen1
                preparedStatement.setNull(7, Types.INTEGER);    // laatste_worp_steen2
                preparedStatement.setBoolean(8, false);      // israndomboard
                preparedStatement.setBoolean(9, true);       // eersteronde
                preparedStatement.setNull(10, Types.INTEGER);   // struikrover_idtegel

                preparedStatement.execute();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        this.gameID = gameId;
        return gameId;
    }

    public interface Callback {
        void finished(int userId);
    }

    //insert db
    public void makeTiles() {
        String query;

        int[] xPos = {2, 3, 4, 3, 4, 5, 6, 4, 5, 6, 7, 8, 6, 7, 8, 9, 8, 9, 10};
        int[] yPos = {4, 6, 8, 3, 5, 7, 9, 2, 4, 6, 8, 10, 3, 5, 7, 9, 4, 6, 8};
        char[] resourceType = {'G', 'G', 'E', 'H', 'H', 'B', 'W', 'B', 'E', 'X', 'W', 'H', 'G', 'G', 'H', 'B', 'W', 'W', 'E'};
        int[] tileValue = {12, 18, 14, 10, 16, 8, 1, 6, 2, 0, 4, 13, 9, 5, 3, 15, 17, 7, 11};

        for (int i = 1; i <= 19; i++) {
            if (i == 10) {
                query = "INSERT INTO tegel VALUES(" + gameID + "," + i + "," + xPos[i - 1] + "," + yPos[i - 1] + ",'" + resourceType[i - 1] + "', null);";
            } else {
                query = "INSERT INTO tegel VALUES(" + gameID + "," + i + "," + xPos[i - 1] + "," + yPos[i - 1] + ",'" + resourceType[i - 1] + "'," + tileValue[i - 1] + ");";
            }
            if (DBConnection.checkConnection()) {
                try {
                    Statement statement = DBConnection.connection.createStatement();
                    statement.executeUpdate(query);


                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }

        updateRobberLocationInDB(10);
    }

    //setters
    public void setThrown(boolean thrown) {
        this.hasThrownDice = thrown;
    }

    public void setFirstTurn() {
        firstTurn = false;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
        users = User.getAllFromDatabaseWithGameId(gameID);
    }

    public void setUsername(String username, Callback callback) {
        if (DBConnection.checkConnection()) {
            try {
                PreparedStatement preparedStatement = DBConnection.connection.prepareStatement("SELECT idspeler FROM speler WHERE idspel = ? AND username = ?;");

                preparedStatement.setInt(1, gameID);
                preparedStatement.setString(2, username);

                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    int userId = resultSet.getInt("idspeler");
                    callback.finished(userId);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void setOwnUserID(int ownUserID) {
        this.ownUserID = ownUserID;
    }

    public void fillUser(User user) {
        users[user.getNumber() - 1] = user;
    }

    public void updateUsers() {
        users = User.getAllFromDatabaseWithGameId(gameID);
        BoardView boardView = catan.getCatangui().getCatanView().getBoardView();

        for (int i = 0; i < users.length; i++) {
            User user = users[i];

            boardView.addVillages(i, user.getVillages());
            boardView.addCities(i, user.getCities());
            boardView.addRoads(i, user.getRoads());
        }
    }

    public void refreshUsers() {
        for (User user : users) {
            user.setShouldRefresh(true);
            user.updateInDatabase();
        }
    }

    public User getOwnUser() {
        return getUser(ownUserID);
    }

    public void setUserIDFromDB() {
        if (DBConnection.checkConnection()) {
            try {
                PreparedStatement preparedStatement = DBConnection.connection.prepareStatement("SELECT beurt_idspeler FROM spel WHERE idspel = '" + gameID + "';");
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    this.turnPlayerID = resultSet.getInt(1);
                }
            } catch (SQLException e) {
            }
        }
    }
}
