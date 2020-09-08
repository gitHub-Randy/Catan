package view;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public class InviteView extends JPanel {
	// instance vars
	// buttons
	private JButton acceptButton;
	private JButton declineButton;
	// labels
	private JLabel usernameFromHost;
	// other
	@SuppressWarnings("unused")
	private String hostName;
	private int gameID;

	public InviteView(String hostName, int gameID) {
		this.gameID = gameID;
		this.hostName = hostName;
		setPreferredSize(new Dimension(215, 251));
		setLayout(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridy = 0;
		usernameFromHost = new JLabel(hostName + "'s spel. Game ID: " + gameID);
		usernameFromHost.setFont(new Font("Serif", Font.ITALIC, 24));
		acceptButton = new JButton("Accepteer");
		acceptButton.setAlignmentY(CENTER_ALIGNMENT);
		declineButton = new JButton("Weiger");
		declineButton.setAlignmentY(CENTER_ALIGNMENT);

		gbc.gridx = 0;
		gbc.weightx = 1.0;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.gridy = 1;
		add(declineButton, gbc);

		gbc.anchor = GridBagConstraints.WEST;
		gbc.gridx = 2;
		gbc.gridy = 1;
		add(acceptButton, gbc);

		gbc.weightx = 0;
		gbc.gridx = 1;
		gbc.gridy = 0;
		add(usernameFromHost, gbc);
	}

	public JButton getAcceptButton() {
		return acceptButton;
	}

	public JButton getDeclineButton() {
		return declineButton;
	}

	public int getGameID() {
		return gameID;
	}

}
