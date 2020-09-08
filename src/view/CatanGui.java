package view;

import controller.ButtonUpdater;
import controller.Catan;
import controller.Dice;
import controller.Game;
import model.CatanModel;
import model.LobbyModel;
import model.User;

import javax.swing.*;
import java.util.ArrayList;

public class CatanGui extends JFrame {
    //instance vars
    //views
    private LobbyModel lobbyModel;
    private LoginView loginView;
    private CreateGameView createGameView;
    private LobbyView lobbyView;
    private CatanView gameView;
    private InviteLobbyView inviteLobbyView;

    private Catan catanController;

    private Dice dice;
    private CatanModel catanmodel;
    private Game game;

    // add dice model as parameters
    public CatanGui(Game g) {
        this.game = g;
        dice = new Dice();
        loginView = new LoginView();
        createGameView = new CreateGameView();
        lobbyModel = new LobbyModel();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(loginView);
        setTitle("Catan");
        setResizable(false);
        pack();
        setLocationRelativeTo(null);
        setLobbyView();
        setInviteView();
        addLogoutListener();
        setCreateGameView();
        setVisible(true);
    }

    public Dice getDice() {
        return dice;
    }

    private void setGameView() {
        JButton play = lobbyView.getPlayButton();
        play.addActionListener(e -> {
            if (checkStates()) {
                catanmodel.fillUser(User.getFromDatabase(1, catanController.getGameID()));
                catanmodel.fillUser(User.getFromDatabase(2, catanController.getGameID()));
                catanmodel.fillUser(User.getFromDatabase(3, catanController.getGameID()));
                catanmodel.fillUser(User.getFromDatabase(4, catanController.getGameID()));

                gameView = new CatanView(catanController, dice, catanmodel, this);
                catanController.setUserId();
                gameView.getThreeUserView().setUsers(catanmodel.getUserWithIndexWithoutOwn(0), catanmodel.getUserWithIndexWithoutOwn(1), catanmodel.getUserWithIndexWithoutOwn(2));
                lobbyModel.getGameIDFromPlayerID(1);

                new ButtonUpdater(catanController);

                if (catanmodel.getUserWithIndex(0).getUserId() == catanmodel.getOwnUserID()) {
                    catanController.addStructures();
                }

                setContentPane(gameView);
                validate();
                repaint();
                catanController.gameStart();
            }
        });

        JButton refresh = lobbyView.getRefreshButton();
        refresh.addActionListener(e -> {
            lobbyView.getLobbyView2().updateUsers();
            lobbyView.getLobbyView3().updateUsers();
            lobbyView.getLobbyView4().updateUsers();
        });
    }

    private void setLobbyView() {
        JButton creategame = createGameView.getCreateGame();
        creategame.addActionListener(e -> {
            game.createCatan();
            catanController = game.getCatan();
            catanmodel = catanController.getModel();

            int gameId = catanController.createGame();
            catanController.getModel().makeTiles();
            lobbyView = new LobbyView(loginView.getUserName(), lobbyModel, gameId);
            lobbyView.createUser(loginView.getUserName(), gameId, "rood", "uitdager", 1);
            catanmodel.setOwnUserID(catanmodel.getUserIDFromDBWithNumber(1));
            lobbyView.setHost();
            if(catanController.getGameStart()) {
            catanmodel.updateTurnPlayerID(lobbyView.getHostID());
            }
            catanController.addDevelopmentCards();
            catanController.addResourceCards();
            catanmodel.getCards(gameId);

            setGameView();
            setContentPane(lobbyView);
            validate();
            repaint();
        });
    }

    private void setInviteView() {
        JButton creategame = createGameView.getAcceptInvite();
        creategame.addActionListener(e -> {

            inviteLobbyView = new InviteLobbyView(catanController, loginView.getUserName(), lobbyModel);
            addBackActionListener();
            addInviteAcceptButtons();
            addInviteRejoinButtons();
            setContentPane(inviteLobbyView);

            validate();
            repaint();
        });
    }

    private void addLogoutListener() {
        JButton logout = createGameView.getLogout();
        logout.addActionListener(e -> {
            setContentPane(loginView);

            validate();
            repaint();
        });
    }

    private void setCreateGameView() {
        JButton next = loginView.getButton();
        next.addActionListener(e -> {
            if (loginView.checkLogin()) {
                setContentPane(createGameView);

                validate();
                repaint();
            }
        });
    }

    public SelfViewWithButtons getSelfView() {
        return gameView.getSelfView();
    }

    public String getUsername() {
        return loginView.getUsername();
    }

    public CatanView getCatanView() {
        return gameView;
    }

    private void addInviteAcceptButtons() {

        // a list with all the buttons
        ArrayList<JButton> list = inviteLobbyView.getAcceptButtons();
        //loop door deze list
        for (int i = 0; i < list.size(); i++) {

            //per button
            InviteView view = inviteLobbyView.getInviteView(i);

            list.get(i).addActionListener(e -> {
                game.createCatan();
                catanController = game.getCatan();
                catanmodel = catanController.getModel();
                //action
                int gameID = view.getGameID();
                catanmodel.setGameID(gameID);

                User host = User.getFromDatabase(1, gameID);
                catanmodel.fillUser(host);

                User player2 = User.getFromDatabase(2, gameID);
                if (player2 != null) {
                    catanmodel.fillUser(player2);
                }

                User player3 = User.getFromDatabase(3, gameID);
                if (player3 != null) {
                    catanmodel.fillUser(player3);
                }

                User player4 = User.getFromDatabase(4, gameID);
                if (player4 != null) {
                    catanmodel.fillUser(player4);
                }

                String hostName = lobbyModel.getHostName(gameID);
                String player2Name = lobbyModel.getNameFromGame(gameID, 2);
                String player3Name = lobbyModel.getNameFromGame(gameID, 3);
                String player4Name = lobbyModel.getNameFromGame(gameID, 4);

                lobbyView = new LobbyView(catanController, hostName, lobbyModel, gameID, player2Name, player3Name, player4Name);

                catanmodel.getCards(gameID);
                setGameView();
                setContentPane(lobbyView);

                validate();
                repaint();
            });
        }
    }

    private void addInviteRejoinButtons() {

        // a list with all the buttons
        ArrayList<JButton> list = inviteLobbyView.getRejoinButtons();
        //loop door deze list
        for (int i = 0; i < list.size(); i++) {
            //per button
            RejoinView view = inviteLobbyView.getRejoinView(i);

            list.get(i).addActionListener(e -> {
                game.createCatan();
                catanController = game.getCatan();
                catanmodel = catanController.getModel();
                //action
                int gameID = view.getGameID();
                catanmodel.setGameID(gameID);
                catanController.updateFirstRound();

                User host = User.getFromDatabase(1, gameID);
                catanmodel.fillUser(host);

                User player2 = User.getFromDatabase(2, gameID);
                if (player2 != null) {
                    catanmodel.fillUser(player2);
                }

                User player3 = User.getFromDatabase(3, gameID);
                if (player3 != null) {
                    catanmodel.fillUser(player3);
                }

                User player4 = User.getFromDatabase(4, gameID);
                if (player4 != null) {
                    catanmodel.fillUser(player4);
                }

                String hostName = lobbyModel.getHostName(gameID);
                String player2Name = lobbyModel.getNameFromGame(gameID, 2);
                String player3Name = lobbyModel.getNameFromGame(gameID, 3);
                String player4Name = lobbyModel.getNameFromGame(gameID, 4);

                lobbyView = new LobbyView(catanController, hostName, lobbyModel, gameID, player2Name, player3Name, player4Name);
                catanController.setGameStart(true);
                setGameView();
                setContentPane(lobbyView);
                validate();
                repaint();
            });
        }
    }

    private void addBackActionListener() {
        inviteLobbyView.getBackButton().addActionListener(e -> {
            setContentPane(createGameView);
            repaint();
        });
    }

    private boolean checkStates() {
        int player2 = lobbyView.getLobbyView2().getPlayerID();
        int player3 = lobbyView.getLobbyView3().getPlayerID();
        int player4 = lobbyView.getLobbyView4().getPlayerID();

        return lobbyModel.getPlayerStatus(player2).equals("geaccepteerd")
                && lobbyModel.getPlayerStatus(player3).equals("geaccepteerd")
                && lobbyModel.getPlayerStatus(player4).equals("geaccepteerd");
    }

    public void goBackToStartScreen(CatanView catanView) {
        if (catanView.gameEnd()) {
            this.setContentPane(createGameView);
            this.repaint();
        }
    }

    public void setCatanController(Catan controller) {
        this.catanController = controller;
        catanmodel = catanController.getModel();
    }

}
