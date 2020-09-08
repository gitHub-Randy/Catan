package view;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public class RejoinView extends JPanel {
	// instance vars
	// buttons
	private JButton rejoinButton;
	private JButton cancelGameButton;
	// labels
	private JLabel usernameFromHost;
	// other
	@SuppressWarnings("unused")
	private String hostName;
	private int gameID;

	public RejoinView(String hostName, int gameID) {
		this.gameID = gameID;
		this.hostName = hostName;
		setPreferredSize(new Dimension(215, 251));
		setLayout(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridy = 0;
		usernameFromHost = new JLabel(hostName + "'s spel. Game ID: " + gameID);
		usernameFromHost.setFont(new Font("Serif", Font.ITALIC, 24));
		rejoinButton = new JButton("Hervatten");
		rejoinButton.setAlignmentY(CENTER_ALIGNMENT);

		cancelGameButton = new JButton("Spel afbreken");
		cancelGameButton.setAlignmentY(CENTER_ALIGNMENT);

		gbc.anchor = GridBagConstraints.CENTER;
		gbc.gridx = 0;
		gbc.gridy = 1;
		add(cancelGameButton, gbc);

		gbc.anchor = GridBagConstraints.CENTER;
		gbc.gridx = 2;
		gbc.gridy = 1;
		add(rejoinButton, gbc);

		gbc.weightx = 0;
		gbc.gridx = 1;
		gbc.gridy = 0;
		add(usernameFromHost, gbc);
	}

	public JButton getRejoinButton() {
		return rejoinButton;
	}

	public JButton getCancelGameButton() {
		return cancelGameButton;
	}

	public int getGameID() {
		return gameID;
	}

}
