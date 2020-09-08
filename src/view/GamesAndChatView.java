package view;

import javax.swing.*;

import controller.Dice;

import java.awt.*;

@SuppressWarnings("serial")
public class GamesAndChatView extends JPanel {
    private AmountOfGamesView amountOfGamesView;
    private ChatView chatView;

    public GamesAndChatView(Dice dice) {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        amountOfGamesView = new AmountOfGamesView(dice);
        amountOfGamesView.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        chatView = new ChatView();
        chatView.setBorder(BorderFactory.createLineBorder(Color.black, 2));

        add(amountOfGamesView);
        add(chatView);
    }

    
    public ChatView getChatView() {
    	return chatView;
    }
    
    public AmountOfGamesView getAmountOfGamesView() {
    	return amountOfGamesView;
    }
}
