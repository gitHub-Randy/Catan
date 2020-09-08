package view;

import controller.Catan;
import model.LobbyModel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class InviteLobbyView extends JPanel {

    //inctance vars
    private ArrayList<InviteView> inviteViews;
    private ArrayList<RejoinView> rejoinViews;
    private JButton goBack;
    private LobbyModel model;
    @SuppressWarnings("unused")
	private Catan catanController;
    private String[] hostNames;
    private String[] hostNamesRejoins;
    private String ownUser;

    public InviteLobbyView(Catan catanController, String ownUser, LobbyModel model) {
        //general
        setPreferredSize(new Dimension(843, 957));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        //back button
        goBack = new JButton("Terug");
        this.add(goBack);
        //catan
        this.model = model;
        this.catanController = catanController;
        this.ownUser = ownUser;
        this.hostNames = model.getHostNamesInvites(ownUser);
        this.hostNamesRejoins = model.getHostNamesRejoins(ownUser);
        inviteViews = new ArrayList<>();
        rejoinViews = new ArrayList<>();
        createInviteViews();
        createRejoinViews();
        addViews();
    }
    
    private void createInviteViews() {
        InviteView v;
        ArrayList<Integer> s = model.getPlayerIDFromUsernameWithInviteStatus(ownUser);
        for (int i = 0; i < hostNames.length; i++) {
            v = new InviteView(hostNames[i], model.getGameIDFromPlayerID(s.get(i)));
            v.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
            addAcceptAction(v.getAcceptButton(), s.get(i));
            addDeclineAction(v.getDeclineButton(), s.get(i), i);
            inviteViews.add(v);
        }
    }
    private void createRejoinViews() {
        RejoinView v;
        ArrayList<Integer> s = model.getPayerIDFromUsernameWithRejoinStatus(ownUser);
        for (int i = 0; i < hostNamesRejoins.length; i++) {
            v = new RejoinView(hostNamesRejoins[i], model.getGameIDFromPlayerID(s.get(i)));
            v.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
            addCancelGameAction(v.getCancelGameButton(), s.get(i), i);
            rejoinViews.add(v);
        }
    }

    private void addViews() {
        for (InviteView inviteView : inviteViews) {
            add(inviteView);
        }
        
        for(RejoinView rejoinView : rejoinViews) {
        	add(rejoinView);
        }
    }

    private void addAcceptAction(JButton b, int playerID) {
        b.addActionListener(e -> {
            model.updatePlayerStatus(playerID, "geaccepteerd");
            b.setEnabled(false);
        });
    }

    private void addDeclineAction(JButton b, int playerID, int index) {
        b.addActionListener(e -> {
            model.updatePlayerStatus(playerID, "geweigerd");
            InviteView v = inviteViews.get(index);
            removeFromScreen(v);
        });
    }
    
    private void addCancelGameAction(JButton b, int playerID, int index) {
        b.addActionListener(e -> {
            model.updatePlayerStatus(playerID, "afgebroken");
            RejoinView v = rejoinViews.get(index);
            removeFromScreen(v);
        });
    }

    private void removeFromScreen(Component c) {
        remove(c);
        repaint();
    }

    public ArrayList<JButton> getAcceptButtons() {
        ArrayList<JButton> list = new ArrayList<>();
        for (InviteView inviteView : inviteViews) {
            list.add(inviteView.getAcceptButton());
        }
        return list;
    }
    
    public ArrayList<JButton> getRejoinButtons() {
        ArrayList<JButton> list = new ArrayList<>();
        for (RejoinView rejoinView : rejoinViews) {
            list.add(rejoinView.getRejoinButton());
        }
        return list;
    }

    public JButton getBackButton() {
        return goBack;
    }

    public InviteView getInviteView(int i) {
        return inviteViews.get(i);
    }
    
    public RejoinView getRejoinView(int i) {
        return rejoinViews.get(i);
    }

}
