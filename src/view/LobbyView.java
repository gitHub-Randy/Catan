package view;

import controller.Catan;
import model.LobbyModel;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public class LobbyView extends JPanel {
    //instance vars
    private LobbyModel model;
    private UserLobbyView lobbyViewHost;
    private UserLobbyView lobbyView2;
    private UserLobbyView lobbyView3;
    private UserLobbyView lobbyView4;
    private int gameID;
    private String hostName;

    //edit this
    private JButton playButton = new JButton("speel");
    private JButton refreshButton = new JButton("ververs");

    public LobbyView(String host, LobbyModel model, int gameID) {
        //model
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        //controller
        this.gameID = gameID;
        this.hostName = host;
        this.model = model;
        //views
        setupUserLobbyViews();
        add(lobbyViewHost);
        add(lobbyView2);
        add(lobbyView3);
        add(lobbyView4);
    }

    public LobbyView(Catan controller, String host, LobbyModel model, int gameID, String player2, String player3, String player4) {
        //model
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        //controller
        this.gameID = gameID;
        this.hostName = host;
        this.model = model;

        setupUserLobbyViews(player2, player3, player4);
        add(lobbyViewHost);
        add(lobbyView2);
        add(lobbyView3);
        add(lobbyView4);
    }

    private void setupUserLobbyViews(String player2, String player3, String player4) {
        lobbyViewHost = new UserLobbyView("rood", model, 1, gameID, hostName);
        lobbyViewHost.add(playButton);
        lobbyViewHost.add(refreshButton);
        lobbyViewHost.removeInviteList();
        lobbyViewHost.removeCancelButton();
        lobbyViewHost.setStatusLabel("Uitdager");

        lobbyView2 = new UserLobbyView("wit", model, 2, gameID, player2);
        lobbyView2.removeInviteList();
        lobbyView2.removeCancelButton();

        lobbyView3 = new UserLobbyView("blauw", model, 3, gameID, player3);
        lobbyView3.removeInviteList();
        lobbyView3.removeCancelButton();

        lobbyView4 = new UserLobbyView("oranje", model, 4, gameID, player4);
        lobbyView4.removeInviteList();
        lobbyView4.removeCancelButton();

        lobbyViewHost.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        lobbyView2.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        lobbyView3.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        lobbyView4.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
    }

    private void setupUserLobbyViews() {
        lobbyViewHost = new UserLobbyView("rood", model, 1, gameID, hostName);
        lobbyViewHost.removeRefreshButton();
        lobbyViewHost.add(playButton);
        lobbyViewHost.add(refreshButton);
        lobbyViewHost.removeInviteList();
        lobbyViewHost.removeCancelButton();
        lobbyViewHost.setStatusLabel("Uitdager");

        lobbyView2 = new UserLobbyView("wit", model, 2, gameID, hostName);
        lobbyView3 = new UserLobbyView("blauw", model, 3, gameID, hostName);
        lobbyView4 = new UserLobbyView("oranje", model, 4, gameID, hostName);

        lobbyViewHost.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        lobbyView2.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        lobbyView3.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        lobbyView4.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
    }

    public JButton getPlayButton() {
        return playButton;
    }

    public JButton getRefreshButton() {
        return refreshButton;
    }

    public void createUser(String username, int gameID, String color, String status, int number) {
        model.createPlayer(username, gameID, color, status, number);
    }

    public void setHost() {
        lobbyViewHost.setNameLabel(model.getHostName(gameID));
    }

    public int getHostID() {
        return model.getHostUserID(gameID);
    }

    public int getGameID() {
        return gameID;
    }

    public UserLobbyView getLobbyViewHost() {
        return lobbyViewHost;
    }

    public UserLobbyView getLobbyView2() {
        return lobbyView2;
    }

    public UserLobbyView getLobbyView3() {
        return lobbyView3;
    }

    public UserLobbyView getLobbyView4() {
        return lobbyView4;
    }

}
