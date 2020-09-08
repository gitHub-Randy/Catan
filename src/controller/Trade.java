package controller;

import java.util.ArrayList;

import model.TradeModel;
import model.User;
import view.TradeView;


public class Trade {
    private int userId;

    private TradeModel model;
    private Catan catanController;
    private User user;

    private TradeView tradeViewPlayer;

    public Trade(Catan catan) {
    	this.catanController = catan;
        model = new TradeModel(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, false, true);
        this.user = catanController.getModel().getOwnUser();
        
        this.tradeViewPlayer = catanController.getCatangui().getCatanView().getTradeViewPlayer();
    }

    public void doTradePlayer(TradeModel tradeModel, Callback callback) {
        user = catanController.getModel().getOwnUser();

        doTradePlayer(
                tradeModel.getGiveWood(),
                tradeModel.getGiveBrick(),
                tradeModel.getGiveWool(),
                tradeModel.getGiveGrain(),
                tradeModel.getGiveOre(),
                tradeModel.getWantsWood(),
                tradeModel.getWantsBrick(),
                tradeModel.getWantsWool(),
                tradeModel.getWantsGrain(),
                tradeModel.getWantsOre(),
                callback
        );
    }

    public void doTradePlayer(int giveWood, int giveBrick, int giveWool, int giveGrain, int giveOre,
                              int wantsWood, int wantsBrick, int wantsWool, int wantsGrain, int wantsOre, Callback callback) {
        user = catanController.getModel().getOwnUser();

        // als dit is ingevuld wordt het geupdate zowel in de database en elders
        // waardoor deze methode wordt aangeroepen
        model.updateInDatabase(userId, giveWood, giveBrick, giveWool, giveGrain, giveOre, wantsWood, wantsBrick,
                wantsWool, wantsGrain, wantsOre);

        Thread thread = new Thread(() -> {
            int result;

            whileLoop:
            while (true) {
                model = TradeModel.getFromDatabase(userId);

                User[] users = User.getAllFromDatabaseWithGameId(catanController.getModel().getGameID());

                int amountRefused = 0;
                for (User user : users) {
                    if (user.getUserId() == userId) {
                        continue;
                    }

                    TradeModel tradeModel = TradeModel.getFromDatabase(user.getUserId());
                    if (model == null) {
                        break;
                    } else if (tradeModel == null) {
                        continue;
                    } else if (model.isFlipped(tradeModel) && tradeModel.isAccepted()) {
                        catanController.transferResourcesTradePlayer(tradeModel.getUserId(), giveWood, giveBrick, giveWool, giveGrain, giveOre, wantsWood, wantsBrick, wantsWool, wantsGrain, wantsOre);

                        result = 1;
                        break whileLoop;
                    } else if (tradeModel.isAccepted()) {
                        boolean acceptedReturnOffer = tradeViewPlayer.returnOffer(tradeModel);

                        if (acceptedReturnOffer) {
                            catanController.transferResourcesTradePlayer(tradeModel.getUserId(), tradeModel.getGiveWood(), tradeModel.getGiveBrick(), tradeModel.getGiveWool(), tradeModel.getGiveGrain(), tradeModel.getGiveOre(), tradeModel.getWantsWood(), tradeModel.getWantsBrick(), tradeModel.getWantsWool(), tradeModel.getWantsGrain(), tradeModel.getWantsOre());
                            result = 2;
                            break whileLoop;
                        } else {
                            amountRefused++;
                        }
                    } else if (!tradeModel.isAccepted()) {
                        amountRefused++;
                    }
                }

                if (amountRefused == users.length - 1) {
                    result = -1;
                    break;
                }

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if (callback != null) {
                callback.finished(result);
            }
        });
        thread.start();
    }

    public void doTradeBank(int giveType, int wantType, int giveResource, int wantResource) {
        user = catanController.getModel().getOwnUser();

        int xto1 = 4;
        ArrayList<Character> resourceTypes = catanController.getResourceTypes();
        for (int i = 0; i < resourceTypes.size(); i++) {
            if ('.' == resourceTypes.get(i)) {
                xto1 = 3;
                break;
            }
        }

        for (int i = 0; i < resourceTypes.size(); i++) {
            if (intToChar(giveType) == resourceTypes.get(i)) {
                xto1 = 2;
                break;
            }
        }
        int amountOfTradeTimes = tradeBankxto1Times(giveResource, xto1, wantResource);

        for (int i = 0; i < amountOfTradeTimes; i++) {
            giveResources(intToChar(giveType), xto1);
            wantResources(intToChar(wantType));
        }

    }

    // 'W', 'G', 'B', 'E', 'H'
    public char intToChar(int typeNumber) {
        char typeChar = ' ';
        switch (typeNumber) {
            case 0:
                typeChar = 'H';
                break;
            case 1:
                typeChar = 'B';
                break;
            case 2:
                typeChar = 'W';
                break;
            case 3:
                typeChar = 'G';
                break;
            case 4:
                typeChar = 'E';
                break;
        }
        return typeChar;
    }

    public int tradeBankxto1Times(int giveAmount, int xto1, int wantAmount) {
        user = catanController.getModel().getOwnUser();

        int times = 0;
        for (int i = 0; i < wantAmount; i++) {
            if (giveAmount >= (i + 1) * xto1 && giveAmount < (i + 2) * xto1) {
                times = i + 1;
            }
        }
        return times;
    }


    public void giveResources(char giveType, int xto1) {
        switch (giveType) {
            case 'H':
                catanController.getModel().addWood(user.spendWood(xto1));
                break;
            case 'B':
                catanController.getModel().addClay(user.spendClay(xto1));
                break;
            case 'W':
                catanController.getModel().addSheep(user.spendSheep(xto1));
                break;
            case 'G':
                catanController.getModel().addGrain(user.spendGrain(xto1));
                break;
            case 'E':
                catanController.getModel().addOre(user.spendOre(xto1));
                break;
        }
    }

    public void wantResources(char wantType) {
        switch (wantType) {
            case 'H':
                user.addWood(catanController.getModel().spendWood(1));
                break;
            case 'B':
                user.addClay(catanController.getModel().spendClay(1));
                break;
            case 'W':
                user.addSheep(catanController.getModel().spendSheep(1));
                break;
            case 'G':
                user.addGrain(catanController.getModel().spendGrain(1));
                break;
            case 'E':
                user.addOre(catanController.getModel().spendOre(1));
                break;
        }
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public interface Callback {
        void finished(int result);
    }

}
