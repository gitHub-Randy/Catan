package view;

import java.awt.Dimension;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import controller.Dice;
import model.AmountOfGamesModel;

@SuppressWarnings("serial")
public class AmountOfGamesView extends JPanel implements Observer {

	private JScrollPane scrollPane;
	private JTextArea textArea;
	private AmountOfGamesModel model;

	public AmountOfGamesView(Dice dice) {
		
		this.setPreferredSize(new Dimension(600,100));
		
		textArea = new JTextArea();
		model = new AmountOfGamesModel(dice);
		
		textArea.setEditable(false);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		
		scrollPane = new JScrollPane(textArea);
		
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setPreferredSize(new Dimension(350, 80));
		
		this.add(scrollPane);
		model.addObserver(this);
		
	}
	
	public AmountOfGamesModel getModel(){
		return model;
		
		
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		textArea.setText(model.getAllGames());

	}

}
