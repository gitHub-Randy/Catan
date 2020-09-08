package controller;

import model.CatanModel;
import model.TradeModel;
import model.User;
import view.CatanGui;
import view.CatanView;
import view.SelfViewWithButtons;
import view.ThreeUserView;

import javax.swing.*;
import java.util.HashMap;

public class ButtonUpdater implements Runnable {
    private SelfViewWithButtons svwb;
    private CatanModel catanModel;
    private Catan catancontroller;
    private CatanGui gui;
    private CatanView catanView;
    private ThreeUserView tuv;
    private boolean resourceGiven;
    private boolean shouldActivateButtons;

    private HashMap<Integer, Boolean> cancelled;

    public ButtonUpdater(Catan catancontroller) {
        this.catancontroller = catancontroller;
        this.catanView = catancontroller.getCatangui().getCatanView();
        this.svwb = catanView.getSelfView();
        this.catanModel = catancontroller.getModel();
        this.gui = catancontroller.getCatangui();
        this.tuv = catanView.getThreeUserView();
        resourceGiven = false;
        shouldActivateButtons = false;

        cancelled = new HashMap<>();

        Thread t1 = new Thread(this);
        t1.start();
    }

    public void checkForTurn() {
    	
        svwb.updateLabels();
        tuv.updateLabels();
        gui.goBackToStartScreen(catanView);
        catancontroller.countVictoryPoints();

        catanModel.setUserIDFromDB();
        catanModel.updateDiceThrownFromDatabase();

        if (catanModel.isThrown() && !resourceGiven) {
        	try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
            catancontroller.doCardDistribution();
            //catancontroller.getStarterResource();
            resourceGiven = true;

        }
        if (!catanModel.isThrown()) {
            resourceGiven = false;
            shouldActivateButtons = true;
        }
        if ((catanModel.getOwnUserID() == catanModel.getPlayerIDTurn()) && catancontroller.isNewTurn()) {
        	catanModel.getCards(catanModel.getGameID());
        	catancontroller.getCatangui().getCatanView().getGamesAndChatView().getChatView().getChatModel().logTurn();
            catanModel.getCards(catanModel.getGameID());
            svwb.doActions();
            shouldActivateButtons = svwb.isButtonUpdaterShouldActivateButtons();
            catancontroller.setTurnStatus(false);

            if ((catanModel.isThrown() || shouldActivateButtons) && !catanModel.isFirstTurn()) {
                shouldActivateButtons = false;
                svwb.activateButtons();

                if (catanModel.isThrown()) {
                    svwb.getDiceThrowButton().setEnabled(false);
                }
            }
        }
    }

    public void checkForTradeRequest() {
        User[] users = catanModel.getUsers();

        if (users == null) {
            return;
        }
        
        
        for (User user : users) {
            TradeModel tradeModel = TradeModel.getFromDatabase(user.getUserId());
            
            if(user.getUserId() == catancontroller.getModel().getOwnUserID()) {
            	return;
            }
            
            if (tradeModel == null || !tradeModel.isAcceptedNull() || cancelled.getOrDefault(user.getUserId(), false)) {
                continue;
            }
            // Voor testen
            // tradeModel = new TradeModel(0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, false, true);

            int result = gui.getCatanView().getTradeViewPlayer().showOffer(user.getUsername(), tradeModel);
            if (result == JOptionPane.YES_OPTION) {
                catancontroller.doTradePlayer(tradeModel, null);
            } else {
            	catancontroller.cancelTrade(tradeModel);
            	cancelled.put(user.getUserId(), true);
            }
        }
    }

    public void checkForRefresh() {
        catanModel.updateUsers();
        User ownUser = catanModel.getOwnUser();
        if (ownUser.shouldRefresh()) {
            catanModel.updateFromDatabase();

            ownUser.setShouldRefresh(false);
            ownUser.updateInDatabase();
        }
    }

    @Override
    public void run() {
        while (true) {
            checkForTurn();
            checkForTradeRequest();
            checkForRefresh();

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
