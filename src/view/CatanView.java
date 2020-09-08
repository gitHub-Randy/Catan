package view;

import controller.ButtonUpdater;
import controller.Catan;
import controller.Dice;
import model.CatanModel;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class CatanView extends JPanel {
    private DiceView diceView;
    private CatanModel catanModel;
    private ThreeUserView threeUserView;
    private BoardView boardView;
    private TradeView tradeViewPlayer;
    private TradeView tradeViewBank;
    private GamesAndChatView gamesAndChatView;
    private SelfViewWithButtons selfView;

    private Catan catanController;

    public CatanView(Catan catancontroller, Dice dice, CatanModel model, CatanGui gui) {
        //general
        setPreferredSize(new Dimension(1200, 800));
        setLayout(new BorderLayout());
        //set parameters
        this.catanModel = model;
        this.catanController = catancontroller;

        //new objects
        tradeViewPlayer = new TradeView("<html>Speler<br>Handel</html>");
        tradeViewBank = new TradeView("<html>Bank<br>Handel</html>");
        gamesAndChatView = new GamesAndChatView(dice);
        diceView = new DiceView(dice);
        threeUserView = new ThreeUserView(Color.BLACK, Color.BLACK, Color.BLACK, catanController);

        //add
        add(threeUserView, BorderLayout.LINE_END);

        add(gamesAndChatView, BorderLayout.SOUTH);
        add(diceView, BorderLayout.NORTH);

        selfView = new SelfViewWithButtons(dice, catanModel, catancontroller);
        selfView.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        boardView = new BoardView(catancontroller, this);
        add(boardView, BorderLayout.CENTER);

        selfView.setBoardView(boardView);

        //set borders
        diceView.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        boardView.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        //onder aanmaken boardview
        //add shit
        add(selfView, BorderLayout.LINE_START);

        tradeButtons();
        gameEnd();

        //other
        setRobbertInBoard(getRobbert());
    }

    public SelfViewWithButtons getSelfView() {
        return this.selfView;
    }

    private int getRobbert() {
        return catanModel.getRobberLocationFromDB();
    }

    private void setRobbertInBoard(int tile) {
        boardView.setRobbert(tile);
    }

    private JButton getTradePlayerButton() {
        return selfView.getTradePlayerButton();
    }

    private JButton getTradeBankButton() {
        return selfView.getTradeBankButton();
    }

    private void tradeButtons() {
        getTradePlayerButton().addActionListener(e -> openTradeView(tradeViewPlayer));

        tradeViewPlayer.getCancelTradeButton().addActionListener(e -> cancelTrade(tradeViewPlayer));

        tradeViewPlayer.getConfirmTradeButton().addActionListener(e -> {
            JOptionPane popup = new JOptionPane();
            if (!checkResources()) {
                tradeViewPlayer.clearTrade();
                repaint();
                JOptionPane.showMessageDialog(
                        this,
                        "<html>Jij hebt te weinig grondstoffen!<br>Probeer opnieuw!</html>", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (tradeViewPlayer.getTotalGive() == 0 || tradeViewPlayer.getTotalWant() == 0) {
                tradeViewPlayer.clearTrade();
                repaint();
                JOptionPane.showMessageDialog(popup,
                        "<html>Je moet minstens 1 grondstof aanbieden en 1 grondstof vragen<br>Probeer opnieuw!</html>",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            tradeViewPlayer.getConfirmTradeButton().setEnabled(false);

            catanController.doTradePlayer(tradeViewPlayer.getGiveWoodAmount(), tradeViewPlayer.getGiveClayAmount(),
                    tradeViewPlayer.getGiveWoolAmount(), tradeViewPlayer.getGiveGrainAmount(),
                    tradeViewPlayer.getGiveOreAmount(), tradeViewPlayer.getWantWoodAmount(),
                    tradeViewPlayer.getWantClayAmount(), tradeViewPlayer.getWantWoolAmount(),
                    tradeViewPlayer.getWantGrainAmount(), tradeViewPlayer.getWantOreAmount(),
                    (int result) -> {
                        switch (result) {
                            case -1:
                                JOptionPane.showMessageDialog(tradeViewPlayer, "Iedereen heeft het ruilaanbod afgewezen.");
                                break;
                            case 1:
                                JOptionPane.showMessageDialog(tradeViewPlayer, "Het ruilaanbod is geaccepteerd.");
                                break;
                        }

                        selfView.activateButtons();
                        remove(tradeViewPlayer);
                        add(boardView, BorderLayout.CENTER);
                        boardView.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
                        selfView.activateButtons();
                        repaint();
                    });
        });

        getTradeBankButton().addActionListener(e -> openTradeView(tradeViewBank));

        tradeViewBank.getCancelTradeButton().addActionListener(arg0 -> cancelTrade(tradeViewBank));

        tradeViewBank.getConfirmTradeButton().addActionListener(arg0 -> {
            JOptionPane popup = new JOptionPane();
            if (!catanController.resourceChecker(tradeViewBank.getGiveWoodAmount(),
                    tradeViewBank.getGiveWoolAmount(), tradeViewBank.getGiveClayAmount(),
                    tradeViewBank.getGiveOreAmount(), tradeViewBank.getGiveGrainAmount())) {
                tradeViewBank.clearTrade();
                repaint();
                JOptionPane.showMessageDialog(popup,
                        "<html>Jij hebt te weinig grondstoffen!<br>Probeer opnieuw!</html>", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (tradeViewBank.getTotalGive() == 0 || tradeViewBank.getTotalWant() == 0) {
                tradeViewBank.clearTrade();
                repaint();
                JOptionPane.showMessageDialog(popup,
                        "<html>Je moet minstens 2 grondstoffen aanbieden en 1 grondstof vragen<br>Probeer opnieuw!</html>",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!catanController.resourceCheckerBank(tradeViewBank.getWantWoodAmount(),
                    tradeViewBank.getWantWoolAmount(), tradeViewBank.getWantClayAmount(),
                    tradeViewBank.getWantOreAmount(), tradeViewBank.getWantGrainAmount())) {
                tradeViewBank.clearTrade();
                repaint();
                JOptionPane.showMessageDialog(popup,
                        "<html>De bank heeft te weinig grondstoffen!<br>Probeer opnieuw!</html>", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!tradeViewBank.tradeBank()) {
                tradeViewBank.clearTrade();
                repaint();
                JOptionPane.showMessageDialog(popup,
                        "<html>Je mag maar 1 soort grondstof aanbieden en vragen!<br>Probeer opnieuw!</html>",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            catanController.doTradeBank(tradeViewBank.getTypeGiveFilledBank(),
                    tradeViewBank.getTypeWantFilledBank(), tradeViewBank.getFilledGiveBank(),
                    tradeViewBank.getFilledWantBank());

            remove(tradeViewBank);
            add(boardView, BorderLayout.CENTER);
            boardView.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
            repaint();
            selfView.getTradePlayerButton().setEnabled(true);
            selfView.getTradeBankButton().setEnabled(true);
            selfView.activateButtons();
        });
    }

    private boolean checkResources() {
        return catanController.resourceChecker(
                tradeViewPlayer.getGiveWoodAmount(),
                tradeViewPlayer.getGiveWoolAmount(),
                tradeViewPlayer.getGiveClayAmount(),
                tradeViewPlayer.getGiveOreAmount(),
                tradeViewPlayer.getGiveGrainAmount());
    }

    private void cancelTrade(TradeView tradeView) {
        remove(tradeView);
        add(boardView, BorderLayout.CENTER);
        boardView.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        selfView.activateButtons();
        repaint();
    }

    private void openTradeView(TradeView tradeView) {
        remove(boardView);
        tradeView.clearTrade();
        add(tradeView, BorderLayout.CENTER);
        tradeView.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        selfView.disableButtons();
        repaint();
    }

    public TradeView getTradeViewBank() {
        return tradeViewBank;
    }

    public TradeView getTradeViewPlayer() {
        return tradeViewPlayer;
    }

    public GamesAndChatView getGamesAndChatView() {
        return gamesAndChatView;
    }

    public boolean gameEnd() {
        boolean gameEnder = false;
        if (catanController.isGameOver()) {
            if (JOptionPane.showConfirmDialog(null, "Speler " + catanModel.getUser(catanModel.getPlayerIDTurn()).getUsername() + " heeft gewonnen. Spel is gestopt. ",
                    "Spel voorbij", JOptionPane.OK_OPTION) == JOptionPane.OK_OPTION) {
                System.exit(0);
            }
        }
        return gameEnder;
    }

    public BoardView getBoardView() {
        return boardView;
    }

    public ThreeUserView getThreeUserView() {
        return threeUserView;
    }

    public DiceView getDiceView() {
        return diceView;
    }



}
