package view;

import model.User;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public class UserView extends JPanel {

	private Color color;
	private JLabel userNameLabel;

	private User user;

	private int amountOfWood;
	private int amountOfGrain;
	private int amountOfBricks;
	private int amountOfWool;
	private int amountOfOre;

	private int amountOfKnights;
	private int amountOfResearch;
	private int amountOfWinningPoint;
	private int amountOfMonopoly;
	private int amountOfStreetBuilding;

	private int amountOfResources;
	private int amountOfDevCards;

	private JLabel resources;
	private JLabel devcard;
	private JLabel playedDevCards;
	private JLabel amountOfWoodLabel;
	private JLabel amountOfGrainLabel;
	private JLabel amountOfBricksLabel;
	private JLabel amountOfWoolLabel;
	private JLabel amountOfOreLabel;
	private JLabel amountOfKnightsLabel;
	private JLabel amountOfResearchLabel;
	private JLabel amountOfWinningPointLabel;
	private JLabel amountOfMonopolyLabel;
	private JLabel amountOfStreetBuildingLabel;

	private int tradeRouteLength = 0;

	public UserView() {
		setPreferredSize(new Dimension(206, 200));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setMinimumSize(new Dimension(306, 812));

		userNameLabel = new JLabel("", SwingConstants.CENTER);

		userNameLabel.setAlignmentX(CENTER_ALIGNMENT);

		resources = new JLabel("Aantal Grondstoffen: " + amountOfResources);
		resources.setAlignmentX(CENTER_ALIGNMENT);

		devcard = new JLabel("Aantal Ontwikkelingskaarten: " + amountOfDevCards);
		devcard.setAlignmentX(CENTER_ALIGNMENT);

		playedDevCards = new JLabel("Gespeelde Ontwikkelingskaarten");
		playedDevCards.setAlignmentX(CENTER_ALIGNMENT);

		amountOfWoodLabel = new JLabel("H: " + amountOfWood);
		amountOfWoodLabel.setAlignmentX(CENTER_ALIGNMENT);

		amountOfGrainLabel = new JLabel("G: " + amountOfGrain);
		amountOfGrainLabel.setAlignmentX(CENTER_ALIGNMENT);

		amountOfBricksLabel = new JLabel("S: " + amountOfBricks);
		amountOfBricksLabel.setAlignmentX(CENTER_ALIGNMENT);

		amountOfWoolLabel = new JLabel("W:" + amountOfWool);
		amountOfWoolLabel.setAlignmentX(CENTER_ALIGNMENT);

		amountOfOreLabel = new JLabel("E: " + amountOfOre);
		amountOfOreLabel.setAlignmentX(CENTER_ALIGNMENT);

		amountOfKnightsLabel = new JLabel("R: " + amountOfKnights);
		amountOfKnightsLabel.setAlignmentX(CENTER_ALIGNMENT);

		amountOfResearchLabel = new JLabel("U: " + amountOfResearch);
		amountOfResearchLabel.setAlignmentX(CENTER_ALIGNMENT);

		amountOfWinningPointLabel = new JLabel("G: " + amountOfWinningPoint);
		amountOfWinningPointLabel.setAlignmentX(CENTER_ALIGNMENT);

		amountOfMonopolyLabel = new JLabel("M: " + amountOfMonopoly);
		amountOfMonopolyLabel.setAlignmentX(CENTER_ALIGNMENT);

		amountOfStreetBuildingLabel = new JLabel("SB: " + amountOfStreetBuilding);
		amountOfStreetBuildingLabel.setAlignmentX(CENTER_ALIGNMENT);

		add(userNameLabel);
		add(Box.createRigidArea(new Dimension(0, 55)));
		add(resources);
		add(devcard);
		add(playedDevCards);
		add(amountOfKnightsLabel);
		add(amountOfResearchLabel);
		add(amountOfWinningPointLabel);
		add(amountOfMonopolyLabel);
		add(amountOfStreetBuildingLabel);
		add(Box.createRigidArea(new Dimension(306, 750)));
	}

	public void setUser(User user) {
		this.color = user.switchColors();

		userNameLabel.setText(user.getUsername());

		this.user = user;
	}
	
	public void setOnlyUser(User user) {
		this.user = user;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(color);
		g.fillOval(this.getWidth() / 3 + 10, this.getHeight() / 10, 35, 35);
		g.setColor(Color.BLACK);
		g.drawOval(this.getWidth() / 3 + 10, this.getHeight() / 10, 35, 35);
	}

	public void setResources(int amountOfWood, int amountOfGrain, int amountOfBricks, int amountOfWool,
			int amountOfOre) {
		this.amountOfWood = amountOfWood;
		this.amountOfGrain = amountOfGrain;
		this.amountOfBricks = amountOfBricks;
		this.amountOfWool = amountOfWool;
		this.amountOfOre = amountOfOre;
	}

	public void setDevelopmentCards(int amountOfKnights, int amountOfResearch, int amountOfWinningPoint,
			int amountOfMonopoly, int amountOfStreetBuilding) {
		this.amountOfKnights = amountOfKnights;
		this.amountOfResearch = amountOfResearch;
		this.amountOfWinningPoint = amountOfWinningPoint;
		this.amountOfMonopoly = amountOfMonopoly;
		this.amountOfStreetBuilding = amountOfStreetBuilding;
	}

	public void updateLabels() {
		amountOfResources = user.getPlayResourceCards().size();
		amountOfDevCards = user.getPlayDevelopmentCards().size();
		this.amountOfKnights = 0;
		this.amountOfResearch = 0;
		this.amountOfWinningPoint = 0;
		this.amountOfMonopoly = 0;
		this.amountOfStreetBuilding = 0;
		for (int i = 0; i < user.getPlayDevelopmentCards().size(); i++) {
			if (user.getPlayDevelopmentCards().get(i).isPlayed()) {
				switch (user.getPlayDevelopmentCards().get(i).getType()) {
				case "gebouw":
					this.amountOfWinningPoint++;
					break;
				case "ridder":
					this.amountOfKnights++;
					break;
				case "vooruitgang":
					switch (user.getPlayDevelopmentCards().get(i).getName()) {
					case "monopolie":
						this.amountOfMonopoly++;
						break;
					case "uitvinding":
						this.amountOfResearch++;
						break;
					case "stratenbouw":
						this.amountOfStreetBuilding++;
						break;

					}
					break;
				}
			}
		}
		
		//tradeRouteLength moet nog worden geupdate
		
		resources.setText("Aantal Grondstoffen: " + amountOfResources);
		devcard.setText("Aantal Ontwikkelingskaarten: " + amountOfDevCards);
		amountOfKnightsLabel.setText("R: " + amountOfKnights);
		amountOfResearchLabel.setText("U: " + amountOfResearch);
		amountOfWinningPointLabel.setText("G: " + amountOfWinningPoint);
		amountOfMonopolyLabel.setText("M: " + amountOfMonopoly);
		amountOfStreetBuildingLabel.setText("SB: " + amountOfStreetBuilding);
		repaint();
	}

}
