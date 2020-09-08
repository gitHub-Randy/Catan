package controller;
/*Laat Wessel en Martijn niet in het weekend of andere vrije dagen zonder toezicht dingen programmeren. WE NEED SUPERVISION
 * https://bit.ly/2wEsHfC */

import database.DBConnection;
import model.*;
import view.CatanGui;
import view.GamesAndChatView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Catan {

    private static final List<String> DEVELOPMENT_CARDS_NAMES = Arrays.asList("kathedraal", "markt", "bibliotheek",
            "universiteit", "parlement");
    private CatanGui catangui;
    private CatanModel model;
    @SuppressWarnings("unused")
    private ButtonUpdater buttonupdater; // unused, niet verwijderen thread waar
    // de beurtaanvraag gebeurd
    private User user; // own user class
    private Board board;
    private Dice dice;
    private Trade trade;
    private boolean hasTurn;
    private boolean newTurn;
    private boolean firstRound;
    private boolean gameOver;
    private boolean starterResourceGiven;
    private boolean print;

    private boolean gameStarted;
    private boolean firstTurnBackwards;

    private int userNumber;
    
    public Catan(CatanGui gui) {
        this.catangui = gui;
        dice = gui.getDice();
        model = new CatanModel(dice, this);
        board = new Board();
        print = true;

        firstRound = model.isFirstTurn();
        hasTurn = false;
        newTurn = true;
        starterResourceGiven = false;
        gameOver = false;
        gameStarted = false;
        firstTurnBackwards = false;
        userNumber = -1;
    }

    public void updateFirstRound() {
        model.updateFirstRound();
        firstRound = model.isFirstTurn();
    }// logic

    public void updateLongestRoad() {
        if (DBConnection.checkConnection()) {
            try {
                PreparedStatement preparedStatement = DBConnection.connection.prepareStatement("UPDATE spel SET langste_hr_idspeler = ? WHERE idspel = ?;");

                if (userNumber != -1) {
                    preparedStatement.setInt(1, getUserIDFromIndex(userNumber));
                } else {
                    preparedStatement.setNull(1, Types.INTEGER);
                }
                preparedStatement.setInt(2, getGameID());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void giveTurnToPeople() {
        int localUserID = model.getPlayerIDTurn();
        int userIndex = getUserIndexFromID(localUserID);

        if (firstRound) {
            if (firstTurnBackwards) {
                userIndex--;
                firstRound = false;
            } else {
                userIndex++;
                firstTurnBackwards = true;
            }

            if (userIndex == 4) {
                userIndex--;
            }

            if (userIndex == -1) {
                // getCatangui().getSelfView().endFirstTurn();
                userIndex = 0;
                newTurn = true;
            }

            model.updateTurnPlayerID(getUserIDFromIndex(userIndex));
        } else {
            model.nextUserNormalTurn();
        }
    }

    public void addDevelopmentCards() {
        int gameId = model.getGameID();

        ArrayList<DevelopmentCard> developmentCards = new ArrayList<>();

        int i = 1;
        for (int g = 0; g < 5; g++) {
            String cardId = String.format("o%02dg", i);
            developmentCards
                    .add(new DevelopmentCard(gameId, cardId, DEVELOPMENT_CARDS_NAMES.get(g), "gebouw", null, 0, false));

            i++;
        }
        for (int s = 0; s < 2; s++) {
            String cardId = String.format("o%02ds", i);
            developmentCards.add(new DevelopmentCard(gameId, cardId, "stratenbouw", "vooruitgang",
                    "Bij het spelen van deze kaart mag je direct twee straten bouwen.", 0, false));

            i++;
        }
        for (int m = 0; m < 2; m++) {
            String cardId = String.format("o%02dm", i);
            developmentCards.add(new DevelopmentCard(gameId, cardId, "monopolie", "vooruitgang",
                    "Bij het spelen van deze kaart kies je een grondstof. Alle spelers geven je van deze grondstof alle kaarten die ze bezitten.",
                    0, false));

            i++;
        }
        for (int u = 0; u < 2; u++) {
            String cardId = String.format("o%02du", i);
            developmentCards.add(new DevelopmentCard(gameId, cardId, "uitvinding", "vooruitgang",
                    "Bij het spelen van deze kaart neem je direct twee grondstoffenkaarten naar keuze van de bank.", 0,
                    false));

            i++;
        }
        for (int r = 0; r < 14; r++) {
            String cardId = String.format("o%02dr", i);
            developmentCards.add(new DevelopmentCard(gameId, cardId, "ridder", "ridder",
                    "Bij het spelen van deze kaart moet je de struikrover verzetten en van één van de getroffen spelers een grondstoffenkaart trekken.",
                    0, false));

            i++;
        }

        developmentCards.forEach(DevelopmentCard::insertInDatabase);
    }

    public void addResourceCards() {
        int gameId = model.getGameID();
        ArrayList<ResourceCard> resourceCards = new ArrayList<>();

        for (int b = 0; b < 19; b++) {
            String cardId = String.format("b%02d", b + 1);
            resourceCards.add(new ResourceCard(gameId, cardId, 0, 'B'));
        }
        for (int e = 0; e < 19; e++) {
            String cardId = String.format("e%02d", e + 1);
            resourceCards.add(new ResourceCard(gameId, cardId, 0, 'E'));
        }
        for (int g = 0; g < 19; g++) {
            String cardId = String.format("g%02d", g + 1);
            resourceCards.add(new ResourceCard(gameId, cardId, 0, 'G'));
        }
        for (int h = 0; h < 19; h++) {
            String cardId = String.format("h%02d", h + 1);
            resourceCards.add(new ResourceCard(gameId, cardId, 0, 'H'));
        }
        for (int w = 0; w < 19; w++) {
            String cardId = String.format("w%02d", w + 1);
            resourceCards.add(new ResourceCard(gameId, cardId, 0, 'W'));
        }
        resourceCards.forEach(ResourceCard::insertInDatabase);
    }

    public void addStructures() {
        ArrayList<Structure> structures = new ArrayList<>();

        for (User user : model.users) {
            int userId = user.getUserId();

            for (int c = 1; c <= 4; c++) {
                String structureId = String.format("c%02d", c);
                structures.add(new City(structureId, userId, 0, 0));
            }
            for (int d = 1; d <= 5; d++) {
                String structureId = String.format("d%02d", d);
                structures.add(new Village(structureId, userId, 0, 0));
            }
            for (int r = 1; r <= 15; r++) {
                String structureId = String.format("r%02d", r);
                structures.add(new Road(structureId, userId, 0, 0, 0, 0));
            }
        }

        structures.forEach(Structure::insertInDatabase);
    }

    public void countVictoryPoints() {
        user = model.getOwnUser();
        int amountOfPoints = 0;

        for (int a = 0; a < model.getUsers().length; a++) {
            User userV = model.getUserWithIndex(a);
            amountOfPoints = 0;

            for (int i = 0; i < userV.getCities().length; i++) {
                if (userV.getCities()[i].getXPos1() != 0 && userV.getCities()[i].getYPos1() != 0) {
                    amountOfPoints += 1;
                }
            }

            for (int i = 0; i < userV.getVillages().length; i++) {
                if (userV.getVillages()[i].getXPos1() != 0 && userV.getVillages()[i].getYPos1() != 0) {
                    amountOfPoints++;

                }
            }

            for (int i = 0; i < model.getUsers().length; i++) {
            	if(userNumber != -1){
            		if (a == userNumber) {
            			if(print){
                			catangui.getCatanView().getGamesAndChatView().getChatView().getChatModel().logMostRoads();
                			print = false;
            			}

            			amountOfPoints += 2;
						break;
					}
            	}
			}

            amountOfPoints += userV.amountOfDevCardBuildings();

            if (userV.getUserId() == calculateMostKnights()) {
                amountOfPoints += 2;
            }

            userV.setVictoryPoints(amountOfPoints);
            if (userV.getVictoryPoints() == 10) {
                gameOver = true;
            }

        }
    }

    public void giveCard(int userID, char resource, int amount) {
        // deze geeft kaarten aan speler
        // kaarten die uitgedeeld worden, moeten uit catanmodel verwijderd
        // worden.
        // het is dus van bank naar speler
        // een resourceCard heeft een id dus de nieuwe toegevoede clay moet
        // hetzelfde ID
        // hebben als de verwijderde clay
        // de methode spend clay verwijderd clay van de bank en retured de
        // verwijderde
        // resource card

        switch (resource) {

            case 'B':
                model.getUser(userID).addClay(model.spendClay(amount));
                break;
            case 'G':
                model.getUser(userID).addGrain(model.spendGrain(amount));
                break;
            case 'E':
                model.getUser(userID).addOre(model.spendOre(amount));
                break;
            case 'W':
                model.getUser(userID).addSheep(model.spendSheep(amount));
                break;
            case 'H':
                model.getUser(userID).addWood(model.spendWood(amount));
                break;
        }
    }

    public void doCardDistribution() {

        // hier checken of de gegooide waarde overeenkomen met een tile met
        // dorpen er om
        // heen en daarvan kaarten uitdelen.
        // tiles zoeken met overeenkomende value
        Tile[] tiles = board.getTiles();

        int diceValue = dice.getTotalValue();
        int amount;

        if (diceValue == 7) {
            loseCards();
            return;
        }

        // loopt door de 2 tiles met corresponderende values
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].getBorderLoc().length; j++) {
                if (tiles[i].getBorderLoc()[j].getUserID() == user.getUserId()) {
                    if (!(tiles[i].getTileId() == model.getRobberLocationFromDB())) {
                        // haalt de settlement type op, en switcht het naar een
                        // hoeveelheid kaarten dat
                        // men krijgt per settlement.
                        // v is village(dorp) c is city(stad)
                        switch (tiles[i].getBorderLoc()[j].getType()) {
                            case 'c':
                                amount = 2;
                                break;
                            case 'd':
                                amount = 1;
                                break;
                            default:
                                amount = 0;
                                break;
                        }
                        // dit geeft de kaarten aan de speler wwaar de loop
                        // toevallig aangekomen is.
                        // dit gebeurt per tegel per location. IN. ELKE. BEURT.
                        // giveCard(tiles1.get(i).getBorderLoc()[j].getUserID(),//
                        // tiles1.get(i).getResourceType(), amount);

                        if (tiles[i].getValue() == diceValue) {
                            giveCard(user.getUserId(), tiles[i].getResourceType(), amount);
                            catangui.getCatanView().getGamesAndChatView().getChatView().getChatModel()
                                    .logRecourses(tiles[i].getResourceType(), amount);
                        }
                    }
                }
            }
        }

    }

    public boolean theVillageBuilder(int x, int y, boolean needResource) {
        LocationModel locationToBuild = board.getLocation(x, y).getLocModel();
        if (user == null || user.getVillage() == null) {
            return false;
        }
        Settlement villageToBuild = user.getVillage();
        // check resources
        if (needResource) {
            if (!resourceChecker(1, 1, 1, 0, 1)) {
                return false;
            }
        }
        // check location
        // eigen plek
        if ((locationToBuild.getSettlement() != null)) {
            return false;
        }

        if (!firstRound) {
            Road[] roads = locationToBuild.getRoads();
            if (roads.length == 0) {
                return false;
            }

            boolean canBuild = false;
            for (int i = 0; i < roads.length; i++) {
                if (roads[i] != null) {
                    if (roads[i].getUserId() == model.getOwnUserID()) {
                        canBuild = true;
                    }
                }
            }
            // for (Road road : roads) {
            // if (road.getUserId() == model.getOwnUserID()) {
            // canBuild = true;
            // }
            // }
            if (!canBuild) {
                return false;

            }
        }
        // plekken erom heen

        Location[] locations = board.getBorderLocations(x, y);
        for (Location location : locations) {
            if (location != null) {
                if (location.getSettlement() != null) {
                    return false;
                }
            }
        }

        // wegen

        if (needResource) {

            // if (!roadChecker(x, y)) {
            // System.out.println("iets met road");
            // return false;
            // }

            // haal resources uit speler

            model.addWood(user.spendWood(1));
            model.addClay(user.spendClay(1));
            model.addGrain(user.spendGrain(1));
            model.addSheep(user.spendSheep(1));
        }
        // village naar location
        locationToBuild.setSettlement(villageToBuild);
        villageToBuild.setPlayed(true);

        return true;
    }

    public boolean theCityBuilder(int x, int y) {
        LocationModel locationToBuild = board.getLocation(x, y).getLocModel();
        Settlement cityToBuild = user.getCity();
        // check resources
        if (user == null || cityToBuild == null) {
            return false;
        }
        if (!resourceChecker(0, 0, 0, 3, 2)) {
            return false;
        }
        // check location
        // eigen plek
        if (locationToBuild.getSettlement() == null && !user.checkForVillage(x, y)) {
            return false;
        }

        model.addOre(user.spendOre(3));
        model.addGrain(user.spendGrain(2));

        locationToBuild.setSettlement(cityToBuild);
        cityToBuild.setPlayed(true);
        // if (user.getVillage().getYPos1() == cityToBuild.getYPos1() &&
        // user.getVillage().getXPos1() == cityToBuild.getXPos1()){
        //
        // }
        Settlement[] currentVillages = user.getVillages();

        for (int i = 0; i < currentVillages.length; i++) {
            if (currentVillages[i] != null) {
                if (currentVillages[i].getXPos1() == cityToBuild.getXPos1()
                        && currentVillages[i].getYPos1() == cityToBuild.getYPos1()) {
                    currentVillages[i].setXPos1(0);
                    currentVillages[i].setYPos1(0);
                    // user.getVillages()[i].setYPos1(0);
                    // user.getVillages()[i].setYPos1(0);
                }
            }
        }

		// user.getRemovedVillage(x, y).setPlayed(false);
		Settlement villageToRemove = user.getRemovedVillage(x, y);
		villageToRemove.setXPos1(0);
		villageToRemove.setYPos1(0);
		villageToRemove.updateInDatabase();
		return true;

    }

    public boolean resourceChecker(int wood, int sheep, int clay, int ore, int grain) {
        // overload voor trading?
        if (user.getWoodAmount() < wood) {
            return false;
        }
        if (user.getSheepAmount() < sheep) {
            return false;
        }
        if (user.getClayAmount() < clay) {
            return false;
        }
        if (user.getOreAmount() < ore) {
            return false;
        }
        if (user.getGrainAmount() < grain) {
            return false;
        }
        return true;
    }

    public boolean resourceCheckerBank(int wood, int sheep, int clay, int ore, int grain) {
        // overload voor trading?
        if (getModel().getWoodAmount() < wood) {
            return false;
        }
        if (getModel().getWoolAmount() < sheep) {
            return false;
        }
        if (getModel().getClayAmount() < clay) {
            return false;
        }
        if (getModel().getOreAmount() < ore) {
            return false;
        }
        if (getModel().getGrainAmount() < grain) {
            return false;
        }
        return true;
    }

    public boolean roadChecker(int x, int y) {
        ArrayList<Road> firstRoads = new ArrayList<>();
        boolean returnValue = false;
        Location[] localLocs = board.getBorderLocations(x, y);
        Road[] localroads = user.getRoads();
        // isplayed checken
        for (int i = 0; i < localroads.length; i++) {
            for (int j = 0; j < localLocs.length; j++) {
                if ((localroads[i].getXPos1() == localLocs[j].getX())
                        && (localroads[i].getYPos1() == localLocs[j].getY()) && (localroads[i].getXPos2() == x)
                        && (localroads[i].getYPos2() == y)) {
                    firstRoads.add(localroads[i]);
                    // opslaan van wegen in een array
                }
            }
        }
        for (int a = 0; a < localroads.length; a++) {
            for (int b = 0; b < firstRoads.size(); b++) {
                if ((firstRoads.get(b).getXPos2() == localroads[a].getXPos1())
                        && (firstRoads.get(b).getYPos2() == localroads[a].getYPos1())) {
                    returnValue = true;
                }
            }
        }
        return returnValue;
    }

    public boolean roadBuilder(int x1, int y1, int x2, int y2, boolean needResource) {

        Road[] localRoads = getModel().getOwnUser().getRoadArray();
        boolean permissionToBuild = false;
        LocationModel locationToBuildFrom = board.getLocation(x1, y1).getLocModel();
        LocationModel locationToBuildTo = board.getLocation(x2, y2).getLocModel();

        if (this.hasRoad(x1, y1, x2, y2)) {
            return false;
        }

        if (needResource) {
            if (!resourceChecker(1, 0, 1, 0, 0)) {
                return false;
            }
        }

		// check location
		/*
		 * deze checked de locatie waar je de weg wilt bouwen.Settlement en
		  road. Road* wordt heen en weer gechecked.
		 */

        if (locationToBuildFrom.getSettlement() != null
                && locationToBuildFrom.getSettlement().getUserId() == model.getOwnUserID()) {
            permissionToBuild = true;
        } else {
            for (Road localRoad : localRoads) {
                if (x1 == localRoad.getXPos2() && y1 == localRoad.getYPos2()) {
                    permissionToBuild = true;
                }
            }
        }

        if (permissionToBuild) {
            // build the road
            user.getRoad().setXPos1(x1);
            user.getRoad().setYPos1(y1);
            user.getRoad().setXPos2(x2);
            user.getRoad().setYPos2(y2);

            model.addClay(user.spendClay(1));
            model.addWood(user.spendWood(1));

            locationToBuildTo.setRoads(user.getRoad());
            locationToBuildFrom.setRoads(user.getRoad());
            user.getRoad().setPlayed(true);

        }
        return permissionToBuild;
    }

    public boolean hasRoad(int x1, int y1, int x2, int y2) {

        ArrayList<Road> playedRoads = new ArrayList<>();
        playedRoads = board.getRoads();

        for (int i = 0; i < playedRoads.size(); i++) {
            if (((playedRoads.get(i).getXPos1() == x1 && playedRoads.get(i).getYPos1() == y1)
                    && (playedRoads.get(i).getXPos2() == x2 && playedRoads.get(i).getYPos2() == y2))
                    || ((playedRoads.get(i).getXPos1() == x2 && playedRoads.get(i).getYPos1() == y2)
                    && (playedRoads.get(i).getXPos2() == x1 && playedRoads.get(i).getYPos2() == y1))) {
                return true;
            }
        }
        return false;
    }

    public void doTradePlayer(TradeModel tradeModel, Trade.Callback callback) {
        trade.doTradePlayer(tradeModel, callback);

    }

    public void doTradePlayer(int giveWood, int giveBrick, int giveWool, int giveGrain, int giveOre, int wantsWood,
                              int wantsBrick, int wantsWool, int wantsGrain, int wantsOre, Trade.Callback callback) {
        // dit zorgt ervoor dat er een trade request in de database komt te
        // staan
        trade.doTradePlayer(giveWood, giveBrick, giveWool, giveGrain, giveOre, wantsWood, wantsBrick, wantsWool,
                wantsGrain, wantsOre, callback);
    }

    public void transferResourcesTradePlayer(int userIDReceiver, int giveWood, int giveClay, int giveSheep,
                                             int giveGrain, int giveOre, int wantsWood, int wantsClay, int wantsSheep, int wantsGrain, int wantsOre) {
        // Resources that the user offered are transferred to the person whom
        // accepted
        // the trade
        User receiver = model.getUser(userIDReceiver);

        receiver.addWood(user.spendWood(giveWood));
        receiver.addClay(user.spendClay(giveClay));
        receiver.addSheep(user.spendSheep(giveSheep));
        receiver.addGrain(user.spendGrain(giveGrain));
        receiver.addOre(user.spendOre(giveOre));

        // Resources that the user wanted are transferred to the user
        user.addWood(receiver.spendWood(wantsWood));
        user.addClay(receiver.spendClay(wantsClay));
        user.addSheep(receiver.spendSheep(wantsSheep));
        user.addGrain(receiver.spendGrain(wantsGrain));
        user.addOre(receiver.spendOre(wantsOre));

        catangui.getCatanView().getGamesAndChatView().getChatView().getChatModel().logTrade(receiver.getUsername(),
                giveWood, giveClay, giveSheep, giveGrain, giveOre, wantsWood, wantsClay, wantsSheep, wantsGrain,
                wantsOre);
    }

    public void doTradeBank(int giveType, int wantType, int giveResource, int wantResource) {
        trade.doTradeBank(giveType, wantType, giveResource, wantResource);
        catangui.getCatanView().getGamesAndChatView().getChatView().getChatModel().logTrade(giveType, wantType, giveResource, wantResource);
    }

    public int createGame() {
        return model.createGame();
    }

    public void buyDevelopmentCard() {
        // Geeft alle benodigde grondstoffen van speler naar bank
        model.addSheep(user.spendSheep(1));
        model.addOre(user.spendOre(1));
        model.addGrain(user.spendGrain(1));
        // geeft een developmentcard van bank naar speler
        user.addDevelopmentCard(model.spendDevelopmentCard());
        catangui.getCatanView().getSelfView().updateLabels();
        catangui.getCatanView().getGamesAndChatView().getChatView().getChatModel().logBuyDevCards();
    }

    private int counter; // hoort bij useDevelopmentCard();

    public void useDevelopmentCard(DevelopmentCard card) {

        if (card != null && !card.isPlayed()) {
            switch (card.getType()) {
                case "gebouw":
                    catangui.getCatanView().getGamesAndChatView().getChatView().getChatModel().logPlayDevCard("gebouw");
                    // gebouw word op played gezet(word gedaan aan het einde van de
                    // methode)
                    break;
                case "ridder":
                    // speler mag de rover verplaatsen

                    catangui.getCatanView().getBoardView().setPlayDevelopmentCard(true);
                    catangui.getCatanView().getSelfView().disableButtons();
                    catangui.getCatanView().getBoardView().addMouseListener(catangui.getCatanView().getBoardView());

                    catangui.getCatanView().getSelfView().getRobberPlaced().setEnabled(true);
                    catangui.getCatanView().getSelfView().getRobberPlaced().addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent arg0) {
                            catangui.getCatanView().getSelfView().getRobberPlaced().setEnabled(false);
                            ArrayList<Integer> userIdwithsettelment = new ArrayList<>();
                            for (int i = 0; i < board.getTiles().length; i++) {
                                if (board.getTiles()[i].getTileId() == model.getRobberLocationFromDB()) {
                                    for (int j = 0; j < board.getTiles()[i].getBorderLoc().length; j++) {
                                        if (board.getTiles()[i].getBorderLoc()[j].hasSettlement()) {
                                            if (!userIdwithsettelment.contains(
                                                    board.getTiles()[i].getBorderLoc()[j].getSettlement().getUserId())) {
                                                userIdwithsettelment.add(
                                                        board.getTiles()[i].getBorderLoc()[j].getSettlement().getUserId());
                                            }
                                        }
                                    }
                                }
                            }
                            if (userIdwithsettelment.size() != 0) {
                                String[] choices2 = new String[userIdwithsettelment.size()];
                                for (int i = 0; i < userIdwithsettelment.size(); i++) {
                                    choices2[i] = model.getUserWithIndex(userIdwithsettelment.get(i)).getUsername();
                                }

                                String result = (String) JOptionPane.showInputDialog(null,
                                        "Kies een speler waarvan je wilt stelen:", "Kies speler",
                                        JOptionPane.QUESTION_MESSAGE, null, choices2, choices2[0]);
                                for (int x = 0; x < userIdwithsettelment.size(); x++) {
                                    if (result == choices2[x]) {
                                        stealCard(userIdwithsettelment.get(x));
                                        break;
                                    }
                                }

                            }
                            catangui.getCatanView().getSelfView().activateButtons();
                        }

                    });
                    catangui.getCatanView().getGamesAndChatView().getChatView().getChatModel().logPlayDevCard("ridder");
                    break;
                case "vooruitgang":
                    switch (card.getName()) {
                        case "monopolie":
                            // kies een grondstof en alle andere spelers moeten die alle
                            // grondstofkaarten
                            // van die soort aan die speler geven
                            String[] choicesResources = new String[]{"Wol", "Graan", "Baksteen", "Erts", "Hout"};

                            String resourceResult = (String) JOptionPane.showInputDialog(null, "Kies de een grondstofkaart:",
                                    "Grondstofsoort kiezen", JOptionPane.QUESTION_MESSAGE, null, choicesResources,
                                    choicesResources[0]);
                            ArrayList<Integer> userIDS = new ArrayList<>();
                            for (int z = 0; z < model.getUsers().length; z++) {
                                if (model.getUsers()[z].getUserId() != user.getUserId()) {
                                    userIDS.add(model.getUsers()[z].getUserId());
                                }
                            }
                            User victim1 = model.getUser(userIDS.get(0));
                            User victim2 = model.getUser(userIDS.get(1));
                            User victim3 = model.getUser(userIDS.get(2));
                            switch (resourceResult) {
                                case "Wol":
                                    user.addSheep(victim1.spendSheep(19));
                                    user.addSheep(victim2.spendSheep(19));
                                    user.addSheep(victim3.spendSheep(19));
                                    break;
                                case "Graan":
                                    user.addGrain(victim1.spendGrain(19));
                                    user.addGrain(victim2.spendGrain(19));
                                    user.addGrain(victim3.spendGrain(19));
                                    break;
                                case "Baksteen":
                                    user.addClay(victim1.spendClay(19));
                                    user.addClay(victim2.spendClay(19));
                                    user.addClay(victim3.spendClay(19));
                                    break;
                                case "Erts":
                                    user.addOre(victim1.spendOre(19));
                                    user.addOre(victim2.spendOre(19));
                                    user.addOre(victim3.spendOre(19));
                                    break;
                                case "Hout":
                                    user.addWood(victim1.spendWood(19));
                                    user.addWood(victim2.spendWood(19));
                                    user.addWood(victim3.spendWood(19));
                                    break;
                            }
                            catangui.getCatanView().getGamesAndChatView().getChatView().getChatModel()
                                    .logPlayDevCard("monopolie");
                            break;
                        case "stratenbouw":
                            // speler mag 2 straten bouwen
                            counter = 0;
                            user.addWood(model.spendWood(2));
                            user.addClay(model.spendClay(2));

                            catangui.getSelfView().disableButtons();
                            catangui.getSelfView().getRoadButton().setEnabled(true);
                            catangui.getSelfView().getRoadButton().addActionListener(new ActionListener() {

                                @Override
                                public void actionPerformed(ActionEvent arg0) {
                                    counter++;
                                    if (counter == 2) {
                                        catangui.getSelfView().getRoadButton().setEnabled(false);
                                        catangui.getSelfView().getRoadButton().removeActionListener(this);
                                        catangui.getSelfView().activateButtons();
                                    }
                                }

                            });
                            catangui.getCatanView().getGamesAndChatView().getChatView().getChatModel()
                                    .logPlayDevCard("stratenbouw");
                            break;
                        case "uitvinding":
                            // speler mag twee grondstoffen kiezen van de bank
                            String[] choices = new String[]{"Wol", "Graan", "Baksteen", "Erts", "Hout"};

                            String result1 = (String) JOptionPane.showInputDialog(null, "Kies de eerste grondstofkaart:",
                                    "Grondstofkaart 1 kiezen", JOptionPane.QUESTION_MESSAGE, null, choices, choices[0]);
                            String result2 = (String) JOptionPane.showInputDialog(null, "Kies de tweede grondstofkaart:",
                                    "Grondstofkaart 2 kiezen", JOptionPane.QUESTION_MESSAGE, null, choices, choices[0]);

                            switch (result1) {
                                case "Wol":
                                    user.addSheep(model.spendSheep(1));
                                    break;
                                case "Graan":
                                    user.addGrain(model.spendGrain(1));
                                    break;
                                case "Baksteen":
                                    user.addClay(model.spendClay(1));
                                    break;
                                case "Erts":
                                    user.addOre(model.spendOre(1));
                                    break;
                                case "Hout":
                                    user.addWood(model.spendWood(1));
                                    break;
                            }

                            switch (result2) {
                                case "Wol":
                                    user.addSheep(model.spendSheep(1));
                                    break;
                                case "Graan":
                                    user.addGrain(model.spendGrain(1));
                                    break;
                                case "Baksteen":
                                    user.addClay(model.spendClay(1));
                                    break;
                                case "Erts":
                                    user.addOre(model.spendOre(1));
                                    break;
                                case "Hout":
                                    user.addWood(model.spendWood(1));
                                    break;
                            }
                            catangui.getCatanView().getGamesAndChatView().getChatView().getChatModel()
                                    .logPlayDevCard("uitvinding");
                            break;
                    }
                    break;
            }

            card.setPlayed(true);
            card.updateInDatabase();
            countVictoryPoints();
            catangui.getSelfView().updateLabels();
            catangui.getCatanView().getSelfView().activateButtons();
        }
    }

    public void loseCards() {
        Random r = new Random();
        int randomValue;
        int myId = model.getOwnUserID();
        User user = model.getUser(myId);

        if (user != null) {
            if (user.getPlayResourceCards().size() > 7) {

                int size = user.getPlayResourceCards().size() / 2;
                for (int i = size; i > 0; i--) {
                    randomValue = r.nextInt(user.getPlayResourceCards().size());
                    switch (user.getPlayResourceCards().get(randomValue).getResourceTypeId()) {
                        case 'B':
                            model.addClay(user.spendClay(1));
                            break;
                        case 'G':
                            model.addGrain(user.spendGrain(1));
                            break;
                        case 'E':
                            model.addOre(user.spendOre(1));
                            break;
                        case 'W':
                            model.addSheep(user.spendSheep(1));
                            break;
                        case 'H':
                            model.addWood(user.spendWood(1));
                            break;
                    }
                }
            }
        }
    }

    public void stealCard(int index) {
        User victim = model.getUserWithIndex(index);
        Random r = new Random();
        int cardToSteal = r.nextInt(victim.getPlayResourceCards().size());
        System.out.println("dit is voor de kees");
        switch (victim.getPlayResourceCards().get(cardToSteal).getResourceTypeId()) {
            case 'W':
                model.getOwnUser().addSheep(victim.spendSheep(1));
                System.out.println("steal card " + 1);
                break;
            case 'G':
                model.getOwnUser().addGrain(victim.spendGrain(1));
                System.out.println("steal card " + 2);
                break;
            case 'B':
                model.getOwnUser().addClay(victim.spendClay(1));
                System.out.println("steal card " + 3);
                break;
            case 'E':
                model.getOwnUser().addOre(victim.spendOre(1));
                System.out.println("steal card " + 4);
                break;
            case 'H':
                model.getOwnUser().addWood(victim.spendWood(1));
                System.out.println("steal card " + 5);
                break;
        }
    }

    private int userIdMostKnights = 0; // hoort bij calculatedMostKnights()
    private int mostKnights = 0; // hoort bij calculatedMostKnights()

    public int calculateMostKnights() {

        for (int i = 0; i < model.getUsers().length; i++) {
            model.getUsers()[i].countKnightsPlayed();
            if (model.getUsers()[i].getSpendKnights() > mostKnights && model.getUsers()[i].getSpendKnights() >= 3) {
                mostKnights = model.getUsers()[i].getSpendKnights();
                userIdMostKnights = model.getUsers()[i].getUserId();
                updateMostKnightsDB(userIdMostKnights);
            }
        }

        return userIdMostKnights;
    }

    public void updateMostKnightsDB(int userIdMostKnights) {
        if (DBConnection.checkConnection()) {
            try {
                PreparedStatement preparedStatement = DBConnection.connection
                        .prepareStatement("UPDATE spel SET grootste_rm_idspeler = ? WHERE idspel = ?;");

                preparedStatement.setInt(1, userIdMostKnights);
                preparedStatement.setInt(2, model.getGameID());

                preparedStatement.execute();
            } catch (SQLException e) {
            }
        }
    }

	public void calculateLongestTradeRoute() {
		int routeLength = 0;
		int longestRouteLength = 0;

		User[] users = model.getUsers();

		for (int i = 0; i < users.length; i++) {
			for (int j = 0; j < users[i].getRoads().length; j++) {
				if (users[i].getRoads()[j].getXPos1() != 0) {
					routeLength++;
				}
			}
			if (routeLength >= 5 && routeLength > longestRouteLength) {
				longestRouteLength = routeLength;
				userNumber = i;
			}
			routeLength = 0;
		}

		updateLongestRoad();
	}

    public void gameStart() {
        // user = model.getOwnUser();
        int hostId = model.getUserWithIndex(0).getUserId();
        if (!gameStarted) {
            model.updateTurnPlayerID(hostId);
            this.setGameStart(true);
        }
    }

    // getters
    public int getUserIndexFromID(int userID) {
        User[] users = model.getUsers();
        User user = Arrays.stream(users).filter(u -> u.getUserId() == userID).findFirst().orElse(null);
        return Arrays.asList(users).indexOf(user);
    }

    public int getUserIDFromIndex(int index) {
        User[] users = model.getUsers();
        User user = users[index];
        return user.getUserId();
    }

    public void getStarterResource() {
        Tile[] tiles = board.getTiles();
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].getBorderLoc().length; j++) {
                if (tiles[i].getBorderLoc()[j].getUserID() == user.getUserId()
                        && tiles[i].getBorderLoc()[j].getX() == model.getSecondVillage().getXPos1()
                        && tiles[i].getBorderLoc()[j].getY() == model.getSecondVillage().getYPos1()) {
                    giveCard(user.getUserId(), tiles[i].getResourceType(), 1);
                }
            }
        }

    }

    public int getDiceValue(int i) {
        return dice.getThrow(i);
    }

    public Dice getDice() {
        return this.dice;
    }

    public CatanModel getModel() {
        return this.model;
    }

    public boolean ourPlayerHasTurn() {
        return hasTurn;
    }

	public boolean playerHasDoneFirstPhase() {
		Village[] localVillages = user.getVillages();
		int numberOfPlayedVillages = 0;
		for (int i = 0; i < localVillages.length; i++) {
			System.out.println(localVillages[i]);
			if (localVillages[i]
					.getXPos1() != 0 /*
										 * && localVillages[i].getUserId() ==
										  model.getUserWithIndex(0).getUserId()
										 */) {
				numberOfPlayedVillages++;
				if (numberOfPlayedVillages == 2) {
					return true;
				}
			}
		}
		return false;
	}

    public boolean isNewTurn() {
        return newTurn;
    }

    public int getWood() {
        return user.getWoodAmount();
    }

    public int getGrain() {
        return user.getGrainAmount();
    }

    public int getOre() {
        return user.getOreAmount();
    }

    public int getSheep() {
        return user.getSheepAmount();
    }

    public int getClay() {
        return user.getClayAmount();
    }

    public int getUserID() {
        return model.getOwnUserID();
    }

    public int getGameID() {
        return model.getGameID();
    }

    public User getUser() {
        return this.user;
    }

	public ArrayList<Character> getResourceTypes() {
		ArrayList<Character> resourceTypes = new ArrayList<>();
		for (int i = 0; i < user.getVillages().length; i++) {
			if (user.getVillages()[i].getXPos1() != 0 && user.getVillages()[i].getYPos1() != 0) {
				if (board.getLocation(user.getVillages()[i].getXPos1(), user.getVillages()[i].getYPos1())
						.hasHarbour()) {
					if(resourceTypes.size() == 0) {
                    	resourceTypes.add(
                                board.getLocation(user.getVillages()[i].getXPos1(), user.getVillages()[i].getYPos1())
                                        .getHarbourResource());
                    }else {for (int j = 0; j < resourceTypes.size(); j++) {
						if (board.getLocation(user.getVillages()[i].getXPos1(), user.getVillages()[i].getYPos1())
								.getHarbourResource() == resourceTypes.get(j)) {
							break;
						}
						resourceTypes.add(
								board.getLocation(user.getVillages()[i].getXPos1(), user.getVillages()[i].getYPos1())
										.getHarbourResource());
					}
				}
			}
		}}

		for (int i = 0; i < user.getCities().length; i++) {
			if (user.getCities()[i].getXPos1() != 0 && user.getCities()[i].getYPos1() != 0) {
				if (board.getLocation(user.getCities()[i].getXPos1(), user.getCities()[i].getYPos1()).hasHarbour()) {
					if(resourceTypes.size() == 0) {
                       resourceTypes
                       .add(board.getLocation(user.getCities()[i].getXPos1(), user.getCities()[i].getYPos1())
                               .getHarbourResource());
                   }else {for (int j = 0; j < resourceTypes.size(); j++) {
						if (board.getLocation(user.getCities()[i].getXPos1(), user.getCities()[i].getYPos1())
								.getHarbourResource() == resourceTypes.get(j)) {
							break;
						}
						resourceTypes
								.add(board.getLocation(user.getCities()[i].getXPos1(), user.getCities()[i].getYPos1())
										.getHarbourResource());}
					}
				}
			}
		}
		return resourceTypes;
	}

    public boolean isGameOver() {
        return gameOver;
    }

    public Board getBoard() {
        return board;
    }

    public boolean getGameStart() {
        return this.gameStarted;
    }

    public CatanGui getCatangui() {
        return catangui;
    }

    public boolean isFirstRound() {
        return firstRound;
    }

    // setters
    public void setUserId() {
        model.setUsername(catangui.getUsername(), userId -> {
            trade = new Trade(this);

            user = model.getUser(userId);
            model.setOwnUserID(userId);
            trade.setUserId(model.getOwnUserID());

            GamesAndChatView gamesAndChatView = catangui.getCatanView().getGamesAndChatView();
            AmountOfGamesModel amountOfGamesModel = gamesAndChatView.getAmountOfGamesView().getModel();
            amountOfGamesModel.setUserID(userId);
            amountOfGamesModel.setGameID(getGameID());
            ChatModel chatModel = gamesAndChatView.getChatView().getChatModel();
            chatModel.setUserID(userId);
            chatModel.setGameId(getGameID());

            board.fillTiles(model.getGameID());
            // model.updateTurnPlayerID(userId);

            catangui.getSelfView().setUsername(model.getOwnUser().getUsername());
            catangui.getSelfView().setColor(model.getOwnUser().switchColors());
        });
    }

    public void setTurnStatus(boolean status) {
        newTurn = status;
    }

    public void setGameID(int gameID) {
        model.setGameID(gameID);
    }

    public void setGameStart(boolean start) {
        this.gameStarted = start;
    }


    public boolean getStarterResourceGiven() {
        return this.starterResourceGiven;
    }

    public void setResourceGiven(boolean given) {
        this.starterResourceGiven = given;
    }

    public void updateFirstRoundInDatabase() {
        if (DBConnection.checkConnection()) {
            try {
                PreparedStatement preparedStatement = DBConnection.connection
                        .prepareStatement("UPDATE spel SET eersteronde = ? WHERE idspel = ?;");

                preparedStatement.setBoolean(1, firstRound);
                preparedStatement.setInt(2, model.getGameID());

                preparedStatement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void cancelTrade(TradeModel tradeModel) {
        if (DBConnection.checkConnection()) {
            try {
                PreparedStatement preparedStatement = DBConnection.connection.prepareStatement("UPDATE ruilaanbod SET geeft_baksteen = 0, geeft_wol = 0, geeft_erts = 0, geeft_graan = 0, geeft_hout = 0, vraagt_baksteen = 0, vraagt_wol = 0, vraagt_erts = 0, vraagt_graan = 0, vraagt_hout = 0, geaccepteerd = FALSE WHERE idspeler = ?;");

                preparedStatement.setInt(1, tradeModel.getUserId());

                preparedStatement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}