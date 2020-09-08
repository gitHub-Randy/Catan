package view;

import model.LobbyModel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class UserLobbyView extends JPanel {

    //instance vars
    private LobbyModel model;
    private int paneID;
    private int gameID;

    //own player
    private String color;
    private int playerID;
    private String hostName;

    //button
    private JButton invitePlayer;
    private JButton cancelInvite;
    private JButton refreshButton;

    //labels
    private JLabel inviteStatus;
    private JLabel usernameLabel;

    // player list
    private JList<String> userList;
    private JScrollPane scrollPane;

    //general use
    private Component filler;
    private Color playerColor;

    public UserLobbyView(String color, LobbyModel model, int paneID, int gameID, String hostName) {
        //model
        this.model = model;
        this.gameID = gameID;
        //panel
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(200, 900));
        setMinimumSize(new Dimension(200, 900));
        setBackground(Color.WHITE);
        this.paneID = paneID;
        Font font = new Font("Serif", Font.PLAIN, 20);
        filler = Box.createRigidArea(new Dimension(200, 900));

        //own player
        playerColor = StringToColor(color);
        this.color = color;
        this.hostName = hostName;
        playerID = model.getPlayerIDFromGame(gameID, paneID);
        refreshButton = new JButton("Ververs");
        refreshButton.setAlignmentX(CENTER_ALIGNMENT);

        //invite button
        invitePlayer = new JButton("Uitnodigen");
        invitePlayer.setAlignmentX(CENTER_ALIGNMENT);
        setInvitePlayerAction();

        // cancel button
        cancelInvite = new JButton("Annuleren");
        cancelInvite.setAlignmentX(CENTER_ALIGNMENT);
        setCancelInviteAction();

        //user label
        usernameLabel = new JLabel("Nog geen Speler", SwingConstants.CENTER);
        usernameLabel.setAlignmentX(CENTER_ALIGNMENT);
        usernameLabel.setFont(font);

        //invite label
        inviteStatus = new JLabel("Geen", SwingConstants.CENTER);
        inviteStatus.setAlignmentX(CENTER_ALIGNMENT);
        inviteStatus.setFont(font);
        setRefreshAction();

        //add
        add(refreshButton);
        add(Box.createRigidArea(new Dimension(0, 20)));

        add(Box.createRigidArea(new Dimension(100, 110)));
        add(usernameLabel);
        add(Box.createRigidArea(new Dimension(0, 110)));
        add(inviteStatus);
        add(Box.createRigidArea(new Dimension(0, 400)));
        add(invitePlayer);
        makeScrollPane();

        refreshButton.doClick();
    }

    private void setRefreshAction() {
        refreshButton.addActionListener(e -> {
            setStatusLabel(model.getPlayerStatus(playerID));
            usernameLabel.setText(model.getNameFromGame(gameID, color));
            repaint();
        });
    }

    private void setCancelInviteAction() {
        cancelInvite.addActionListener(e -> {
            model.updatePlayerStatus(playerID, "afgebroken");
            setStatusLabel("Geen");
            setNameLabel("Nog geen Speler");
            addInviteList();
        });
    }

    private void setInvitePlayerAction() {
        invitePlayer.addActionListener(e -> {
            if (!userList.isSelectionEmpty()) {
                playerID = model.createPlayer(userList.getSelectedValue(), gameID, color, "uitgedaagde", paneID);
                setStatusLabel("uitgedaagde");
                setNameLabel(userList.getSelectedValue());
                removeInviteList();
            }
        });
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(playerColor);
        g.fillOval(this.getWidth() / 8, this.getHeight() / 10, 150, 150);
        g.setColor(Color.black);
        g.drawOval(this.getWidth() / 8, this.getHeight() / 10, 150, 150);
    }

    private void addInviteList() {
        remove(filler);
        add(scrollPane);
        add(invitePlayer);
        removeCancelButton();
    }

    void removeInviteList() {
        remove(scrollPane);
        remove(invitePlayer);
        add(cancelInvite);
        add(filler);
    }

    public void removeCancelButton() {
        remove(cancelInvite);
    }

    private String[] getPlayerArray() {
        ArrayList<String> s = model.getAllUsers();
        for (int i = 0; i < s.size(); i++) {
            if (s.get(i).equals(hostName)) {
                s.remove(i);
            }
        }

        String[] output = new String[s.size()];
        s.toArray(output);
        return output;
    }

    public void updateUsers() {
        userList.setListData(getPlayerArray());
        userList.updateUI();
    }

    private void makeScrollPane() {
        userList = new JList<>(getPlayerArray());
        scrollPane = new JScrollPane(userList);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        this.add(scrollPane);
    }

    public void setNameLabel(String name) {
        usernameLabel.setText(name);
    }

    public void setStatusLabel(String text) {
        inviteStatus.setText(text);
    }

    private Color StringToColor(String s) {
        switch (s) {
            case "rood":
                return Color.red;
            case "wit":
                return Color.white;
            case "blauw":
                return Color.blue;
            case "oranje":
                return Color.orange;
            default:
                return Color.black;
        }
    }

    public int getPlayerID() {
        return playerID;
    }
    
    public void removeRefreshButton() {
    	remove(refreshButton);
    }

}
