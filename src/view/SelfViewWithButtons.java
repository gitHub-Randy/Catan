package view;

import controller.Catan;
import controller.Dice;
import model.CatanModel;
import model.DevelopmentCard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class SelfViewWithButtons extends JPanel {
    private Color color;
    private JLabel userNameLabel;

    private JLabel amountOfWoodLabel;
    private JLabel amountOfGrainLabel;
    private JLabel amountOfBricksLabel;
    private JLabel amountOfWoolLabel;
    private JLabel amountOfOreLabel;
    private JLabel victoryPoints;

    private int amountOfWood;
    private int amountOfGrain;
    private int amountOfBricks;
    private int amountOfWool;
    private int amountOfOre;

    private JLabel amountOfKnightsLabel;
    private JLabel amountOfResearchLabel;
    private JLabel amountOfWinningPointLabel;
    private JLabel amountOfMonopolyLabel;
    private JLabel amountOfStreetBuildingLabel;

    private int amountOfKnights;
    private int amountOfResearch;
    private int amountOfWinningPoint;
    private int amountOfMonopoly;
    private int amountOfStreetBuilding;

    private int amountOfNotPlayedKnights;
    private int amountOfNotPlayedResearch;
    private int amountOfNotPlayedWinningPoint;
    private int amountOfNotPlayedMonopoly;
    private int amountOfNotPlayedStreetBuilding;

    private JButton road;
    private JButton city;
    private JButton village;
    private JButton tradePlayer;
    private JButton tradeBank;
    private JButton endTurn;
    private JButton diceThrow;
    private JButton devCardButton;

    private JButton robberPlaced;

    private Dice dice;
    private Catan catancontroller;
    private CatanModel catanmodel;
    private BoardView boardview;
    private JOptionPane popup = new JOptionPane();

    private ActionListener alStartRoad;
    private ActionListener alStartVillage;

    private int x1;
    private int x2;
    private int y1;
    private int y2;

    private boolean beginListenersAdded;
    private boolean listenersAdded;
    private boolean buttonUpdaterShouldActivateButtons;

    public SelfViewWithButtons(Dice dice, CatanModel catanmodel, Catan catancontroller) {

        setPreferredSize(new Dimension(206, 200));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setMinimumSize(new Dimension(306, 812));
        this.dice = dice;
        this.catancontroller = catancontroller;
        this.catanmodel = catanmodel;

        userNameLabel = new JLabel("", SwingConstants.CENTER);
        userNameLabel.setAlignmentX(CENTER_ALIGNMENT);

        JLabel grondstoffen = new JLabel("Grondstoffen");
        grondstoffen.setAlignmentX(CENTER_ALIGNMENT);
        JLabel overwinningskaarten = new JLabel("Overwinningskaarten");
        overwinningskaarten.setAlignmentX(CENTER_ALIGNMENT);

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

        victoryPoints = new JLabel();
        victoryPoints.setAlignmentX(CENTER_ALIGNMENT);

        amountOfKnightsLabel = new JLabel("K: " + amountOfKnights);
        amountOfKnightsLabel.setAlignmentX(CENTER_ALIGNMENT);

        amountOfResearchLabel = new JLabel("R: " + amountOfResearch);
        amountOfResearchLabel.setAlignmentX(CENTER_ALIGNMENT);

        amountOfWinningPointLabel = new JLabel("W: " + amountOfWinningPoint);
        amountOfWinningPointLabel.setAlignmentX(CENTER_ALIGNMENT);

        amountOfMonopolyLabel = new JLabel("M: " + amountOfMonopoly);
        amountOfMonopolyLabel.setAlignmentX(CENTER_ALIGNMENT);

        amountOfStreetBuildingLabel = new JLabel("S: " + amountOfStreetBuilding);
        amountOfStreetBuildingLabel.setAlignmentX(CENTER_ALIGNMENT);

        road = new JButton("Weg");
        road.setAlignmentX(CENTER_ALIGNMENT);

        city = new JButton("Stad");
        city.setAlignmentX(CENTER_ALIGNMENT);

        village = new JButton("Dorp");
        village.setAlignmentX(CENTER_ALIGNMENT);

        tradePlayer = new JButton("Speler handel");
        tradePlayer.setAlignmentX(CENTER_ALIGNMENT);

        tradeBank = new JButton("Bank Handel");
        tradeBank.setAlignmentX(CENTER_ALIGNMENT);

        endTurn = new JButton("Beurt be\u00EBindigen");
        endTurn.setAlignmentX(CENTER_ALIGNMENT);

        diceThrow = new JButton("Bobbelen");
        diceThrow.setAlignmentX(CENTER_ALIGNMENT);

        devCardButton = new JButton("Ontwikkelingskaart");
        devCardButton.setAlignmentX(CENTER_ALIGNMENT);

        robberPlaced = new JButton("robber geplaatst");
        robberPlaced.setAlignmentX(CENTER_ALIGNMENT);

        robberPlaced.setEnabled(false);

        add(userNameLabel);
        add(Box.createRigidArea(new Dimension(0, 65)));
        add(grondstoffen);
        add(amountOfWoodLabel);
        add(amountOfGrainLabel);
        add(amountOfBricksLabel);
        add(amountOfWoolLabel);
        add(amountOfOreLabel);
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(overwinningskaarten);
        add(amountOfKnightsLabel);
        add(amountOfResearchLabel);
        add(amountOfWinningPointLabel);
        add(amountOfMonopolyLabel);
        add(amountOfStreetBuildingLabel);
        add(victoryPoints);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(diceThrow);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(road);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(village);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(city);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(devCardButton);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(tradeBank);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(tradePlayer);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(robberPlaced);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(endTurn);
        add(Box.createRigidArea(new Dimension(306, 812)));

        disableButtons();

        beginListenersAdded = false;
        buttonUpdaterShouldActivateButtons = false;
    }

    public boolean isButtonUpdaterShouldActivateButtons() {
        return buttonUpdaterShouldActivateButtons;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(color);
        g.fillOval(this.getWidth() / 3 + 10, this.getHeight() / 20, 50, 50);
        g.setColor(Color.BLACK);
        g.drawOval(this.getWidth() / 3 + 10, this.getHeight() / 20, 50, 50);
    }

    public JButton getRoadButton() {
        return road;
    }

    public JButton getVillageButton() {
        return village;
    }

    public JButton getTradeBankButton() {
        return tradeBank;
    }

    public JButton getTradePlayerButton() {
        return tradePlayer;
    }

    public JButton getEndTurnButton() {
        return endTurn;
    }

    private void setThrowListener() {
        diceThrow.addActionListener(e -> {
            if (!catanmodel.isThrown()) {
                dice.roll();
                diceThrow.setEnabled(false);
                catanmodel.setThrown(true);
                catanmodel.updateInDatabase();
                

                if (dice.getTotalValue() == 7) {
                    JOptionPane.showMessageDialog(popup, "Je kan de struikrover verplaatsen", "Let op!", JOptionPane.INFORMATION_MESSAGE);
                    catancontroller.loseCards();
                    boardview.addMouseListener(boardview);
                }
                catancontroller.getCatangui().getCatanView().getGamesAndChatView().getChatView().getChatModel().logThrow(dice.getTotalValue());
                catanmodel.refreshUsers();

                buttonUpdaterShouldActivateButtons = true;

                activateButtons();
            }
        });
    }

    private void setEndListener() {
        endTurn.addActionListener(e -> {
            disableButtons();
            catancontroller.calculateLongestTradeRoute();
            catanmodel.setThrown(false);
            catancontroller.giveTurnToPeople();
            catanmodel.updateInDatabase();
            catancontroller.setTurnStatus(true);
            if (catancontroller.playerHasDoneFirstPhase() && !catancontroller.getStarterResourceGiven()) {
                catancontroller.getStarterResource();
                catancontroller.setResourceGiven(true);
            }
            catanmodel.refreshUsers();

        });
    }

    public void activateButtons() {
        if (catancontroller.resourceChecker(1, 0, 1, 0, 0)) {
            road.setEnabled(true);
        }
        if (catancontroller.resourceChecker(0, 0, 0, 3, 2)) {
            city.setEnabled(true);
        }
        if (catancontroller.resourceChecker(1, 1, 1, 0, 1)) {
            village.setEnabled(true);
        }
        tradeBank.setEnabled(true);
        tradePlayer.setEnabled(true);
        endTurn.setEnabled(true);
        if (catancontroller.resourceChecker(0, 1, 0, 1, 1)||catancontroller.getModel().getOwnUser().getPlayDevelopmentCards().size()!=0) {
            devCardButton.setEnabled(true);
        }

        buttonUpdaterShouldActivateButtons = false;
    }


    public void turnLogic() {
        if (!listenersAdded) {
            listenersAdded = true;
            catancontroller.updateFirstRoundInDatabase();

            removeBeginActionListeners();

            if (!beginListenersAdded) {
                beginListenersAdded = true;

                setEndListener();
            }

            setThrowListener();

            tradeBank.addActionListener(e -> {
                /*
                 * tradeshit van coen en Hatsune Miku
                 */
                tradeBank.setEnabled(false);
                tradePlayer.setEnabled(false);
            });

            tradePlayer.addActionListener(e -> {
                /*
                 * tradeshit van coen en Hatsune Miku
                 */
                tradePlayer.setEnabled(false);
                tradeBank.setEnabled(false);
            });

            village.addActionListener(arg0 -> {
                // DEZE COORDINATEN VERANDEREN NAAR JUISTE COORDINATEN UIT PIXELCOORDINAAT
                // CONVERT
                boardview.setBuildOption(2);
                if (x1 == 0 && y1 == 0) {
                    boardview.addMouseListener(boardview);
                    village.setEnabled(false);
                } else {
                    tradePlayer.setEnabled(false);
                    tradeBank.setEnabled(false);
                    boardview.setBuildOption(0);
                }

            });

            city.addActionListener(arg0 -> {
                // DEZE COORDINATEN VERANDEREN NAAR JUISTE COORDINATEN UIT PIXELCOORDINAAT
                // CONVERT
                boardview.setBuildOption(3);
                if (x1 == 0 && y1 == 0) {
                    boardview.addMouseListener(boardview);
                    city.setEnabled(false);
                } else {
                    tradePlayer.setEnabled(false);
                    tradeBank.setEnabled(false);
                    boardview.setBuildOption(0);
                }

            });

            road.addActionListener(e -> {
                boardview.setBuildOption(1);
                if (x1 == 0 && x2 == 0 && y1 == 0 && y2 == 0) {
                    boardview.addMouseListener(boardview);
                    road.setEnabled(false);
                } else {
                    tradePlayer.setEnabled(false);
                    tradeBank.setEnabled(false);
                    boardview.setBuildOption(0);
                }
            });

            devCardButton.addActionListener(e -> {
                ArrayList<String> choiceArrayList = new ArrayList<>();
                if (amountOfNotPlayedKnights > 0) {
                    choiceArrayList.add("Ridder");
                }
                if (amountOfNotPlayedResearch > 0) {
                    choiceArrayList.add("Uitvinding");
                }
                if (amountOfNotPlayedMonopoly > 0) {
                    choiceArrayList.add("Monopolie");
                }
                if (amountOfNotPlayedStreetBuilding > 0) {
                    choiceArrayList.add("Stratenbouw");
                }
                if (amountOfNotPlayedWinningPoint > 0) {
                    choiceArrayList.add("Gebouw");
                }

                if (catancontroller.resourceChecker(0, 1, 0, 1, 1)) {
                choiceArrayList.add("Koop Ontwikkelingskaart");
                }

                String[] choices = choiceArrayList.toArray(new String[0]);

                if (choices.length == 0) {
                    JOptionPane.showMessageDialog(null, "Je hebt geen ontwikkelingskaarten en ook niet genoeg geld om er een te kopen.", "Geen Ontwikkelingskaart", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                String result = (String) JOptionPane.showInputDialog(null, "Welke ontwikkelingskaart wil je spelen?",
                        "Ontwikkelingskaart Spelen", JOptionPane.QUESTION_MESSAGE, null, choices, choices[0]);

                if (result == null || result.isEmpty()) {
                    // Canceled
                    return;
                }

                // Success

                if (result.equals("Koop Ontwikkelingskaart")) {
                    catancontroller.buyDevelopmentCard();
                    return;
                }

                ArrayList<DevelopmentCard> developmentCards = catanmodel.getUser(catanmodel.getOwnUserID()).getPlayDevelopmentCards();
                DevelopmentCard developmentCard =
                        developmentCards
                                .stream()
                                .filter(dC -> dC.getName().equals(result.toLowerCase()) && !dC.isPlayed() || dC.getType().equals("gebouw") && !dC.isPlayed())
                                .findFirst()
                                .orElse(null);

                catancontroller.useDevelopmentCard(developmentCard);

            });
        }

        diceThrow.setEnabled(true);
    }

    public void disableButtons() {
        diceThrow.setEnabled(false);
        road.setEnabled(false);
        city.setEnabled(false);
        village.setEnabled(false);
        tradeBank.setEnabled(false);
        tradePlayer.setEnabled(false);
        endTurn.setEnabled(false);
        devCardButton.setEnabled(false);
    }

    private void beginTurnLogic() {
        village.setEnabled(true);
        
        if (!beginListenersAdded) {
            beginListenersAdded = true;

            village.addActionListener(alStartVillage = e -> {
                boardview.setBuildOption(5);
                if (x1 == 0 && y1 == 0) {
                    boardview.addMouseListener(boardview);
                    village.setEnabled(false);
                    buttonUpdaterShouldActivateButtons = true;
                }
            });

            road.addActionListener(alStartRoad = e -> {
                boardview.setBuildOption(4);
                if (x1 == 0 && x2 == 0 && y1 == 0 && y2 == 0) {
                    boardview.addMouseListener(boardview);
                    road.setEnabled(false);
                    buttonUpdaterShouldActivateButtons = true;
                }
            });

            setEndListener();
        }
    }

    public void removeBeginActionListeners() {
        village.removeActionListener(alStartVillage);
        road.removeActionListener(alStartRoad);
    }

    public void updateLabels() {
        if (catancontroller.getUser() != null) {
            amountOfKnights = 0;
            amountOfResearch = 0;
            amountOfWinningPoint = 0;
            amountOfMonopoly = 0;
            amountOfStreetBuilding = 0;

            amountOfNotPlayedKnights = 0;
            amountOfNotPlayedResearch = 0;
            amountOfNotPlayedWinningPoint = 0;
            amountOfNotPlayedMonopoly = 0;
            amountOfNotPlayedStreetBuilding = 0;

            for (DevelopmentCard developmentCard : catancontroller.getUser().getPlayDevelopmentCards()) {
                if (developmentCard == null) {
                    continue;
                }

                if (developmentCard.getType().equals("gebouw")) {
                    amountOfWinningPoint++;
                    if (!developmentCard.isPlayed()) {
                        amountOfNotPlayedWinningPoint++;
                    }
                    continue;
                }

                switch (developmentCard.getName()) {
                    case "ridder":
                        amountOfKnights++;
                        if (!developmentCard.isPlayed()) {
                            amountOfNotPlayedKnights++;
                        }
                        break;
                    case "uitvinding":
                        amountOfResearch++;
                        if (!developmentCard.isPlayed()) {
                            amountOfNotPlayedResearch++;
                        }
                        break;
                    case "monopolie":
                        amountOfMonopoly++;
                        if (!developmentCard.isPlayed()) {
                            amountOfNotPlayedMonopoly++;
                        }
                        break;
                    case "stratenbouw":
                        amountOfStreetBuilding++;
                        if (!developmentCard.isPlayed()) {
                            amountOfNotPlayedStreetBuilding++;
                        }
                        break;
                }
            }
            amountOfOreLabel.setText("E: " + Integer.toString(catancontroller.getOre()));
            amountOfBricksLabel.setText("S: " + Integer.toString(catancontroller.getClay()));
            amountOfWoolLabel.setText("W: " + Integer.toString(catancontroller.getSheep()));
            amountOfGrainLabel.setText("G: " + Integer.toString(catancontroller.getGrain()));
            amountOfWoodLabel.setText("H: " + Integer.toString(catancontroller.getWood()));

            amountOfKnightsLabel.setText("R: " + amountOfKnights);
            amountOfResearchLabel.setText("U: " + amountOfResearch);
            amountOfWinningPointLabel.setText("G: " + amountOfWinningPoint);
            amountOfMonopolyLabel.setText("M: " + amountOfMonopoly);
            amountOfStreetBuildingLabel.setText("SB: " + amountOfStreetBuilding);

            victoryPoints.setText("Overwinningspunten : " + catancontroller.getUser().getVictoryPoints());

            repaint();
        }
    }

    public int getX1() {
        return x1;
    }

    public void setX1(int x1) {
        this.x1 = x1;
    }

    public int getX2() {
        return x2;
    }

    public void setX2(int x2) {
        this.x2 = x2;
    }

    public int getY1() {
        return y1;
    }

    public void setY1(int y1) {
        this.y1 = y1;
    }

    public int getY2() {
        return y2;
    }

    public void setY2(int y2) {
        this.y2 = y2;
    }

    public void activateThrow() {
        diceThrow.setEnabled(true);
    }


    public JButton getRobberPlaced() {
        return robberPlaced;
    }

    public void setUsername(String username) {
        userNameLabel.setText(username);
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setBoardView(BoardView boardView) {
        this.boardview = boardView;
    }

    public void doActions() {
        // actionslisteners hier ooit ingooien.
        if (catancontroller.isNewTurn() && catancontroller.isFirstRound()) {
            beginTurnLogic();
        } else {
            turnLogic();
        }
    }

    public JButton getDiceThrowButton() {
        return diceThrow;
    }

}
