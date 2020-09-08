package view;

import model.TradeModel;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public class TradeView extends JPanel {
	// alle nodige variables
	private int giveWoodAmountCounter = 0;
	private int giveClayAmountCounter = 0;
	private int giveWoolAmountCounter = 0;
	private int giveGrainAmountCounter = 0;
	private int giveOreAmountCounter = 0;

	private int wantWoodAmountCounter = 0;
	private int wantClayAmountCounter = 0;
	private int wantWoolAmountCounter = 0;
	private int wantGrainAmountCounter = 0;
	private int wantOreAmountCounter = 0;

	private JList<String> numberWoodList;
	private JList<String> numberWoodListW;
	private JList<String> numberClayList;
	private JList<String> numberClayListW;
	private JList<String> numberGrainList;
	private JList<String> numberGrainListW;
	private JList<String> numberWoolList;
	private JList<String> numberWoolListW;
	private JList<String> numberOreList;
	private JList<String> numberOreListW;

	// numbers
	private String[] numbers;

	private JScrollPane giveWoodAmount;
	private JScrollPane giveClayAmount;
	private JScrollPane giveWoolAmount;
	private JScrollPane giveGrainAmount;
	private JScrollPane giveOreAmount;

	private JScrollPane wantWoodAmount;
	private JScrollPane wantClayAmount;
	private JScrollPane wantWoolAmount;
	private JScrollPane wantGrainAmount;
	private JScrollPane wantOreAmount;

	private JButton confirmTrade;
	private JButton cancelTrade;

	public TradeView(String name) {
		// list of numbers
		numbers = new String[20];
		for (int i = 0; i < 20; i++) {
			numbers[i] = Integer.toString(i);
		}
		createTrade();

		setBackground(Color.LIGHT_GRAY);
		setPreferredSize(new Dimension(300, 500));
		setLayout(new GridLayout(6, 4));

		cancelTrade = new JButton("<html>Annuleer<br>Handel</html>");

		// knop dat alle ervoor zorgt dat de request wordt doorgestuurd
		confirmTrade = new JButton("<html>Bevestig<br>Handel</html>");

		JLabel giveWoodAmountText = new JLabel("<html>Aanbod aantal<br>hout:</html>");
		giveWoodAmount.setMaximumSize(giveWoodAmount.getPreferredSize());
		giveWoodAmount.setMinimumSize(giveWoodAmount.getPreferredSize());
		add(giveWoodAmountText);
		add(giveWoodAmount);

		JLabel wantWoodAmountText = new JLabel("<html>Vraag aantal<br>hout:</html>");
		wantWoodAmount.setMaximumSize(giveWoodAmount.getPreferredSize());
		wantWoodAmount.setMinimumSize(giveWoodAmount.getPreferredSize());
		add(wantWoodAmountText);
		add(wantWoodAmount);

		JLabel giveClayAmountText = new JLabel("<html>Aanbod aantal<br>klei:</html>");
		giveClayAmount.setMaximumSize(giveWoodAmount.getPreferredSize());
		giveClayAmount.setMinimumSize(giveWoodAmount.getPreferredSize());
		add(giveClayAmountText);
		add(giveClayAmount);

		JLabel wantClayAmountText = new JLabel("<html>Vraag aantal<br>klei:</html>");
		wantClayAmount.setMaximumSize(giveWoodAmount.getPreferredSize());
		wantClayAmount.setMinimumSize(giveWoodAmount.getPreferredSize());
		add(wantClayAmountText);
		add(wantClayAmount);

		JLabel giveGrainAmountText = new JLabel("<html>Aanbod aantal<br>graan:</html>");
		giveGrainAmount.setMaximumSize(giveWoodAmount.getPreferredSize());
		giveGrainAmount.setMinimumSize(giveWoodAmount.getPreferredSize());
		add(giveGrainAmountText);
		add(giveGrainAmount);

		JLabel wantGrainAmountText = new JLabel("<html>Vraag aantal<br>graan:</html>");
		wantGrainAmount.setMaximumSize(giveWoodAmount.getPreferredSize());
		wantGrainAmount.setMinimumSize(giveWoodAmount.getPreferredSize());
		add(wantGrainAmountText);
		add(wantGrainAmount);

		JLabel giveWoolAmountText = new JLabel("<html>Aanbod aantal<br>wol:</html>");
		giveWoolAmount.setMaximumSize(giveWoodAmount.getPreferredSize());
		giveWoolAmount.setMinimumSize(giveWoodAmount.getPreferredSize());
		add(giveWoolAmountText);
		add(giveWoolAmount);

		JLabel wantWoolAmountText = new JLabel("<html>Vraag aantal<br>wol:</html>");
		wantWoolAmount.setMaximumSize(giveWoodAmount.getPreferredSize());
		wantWoolAmount.setMinimumSize(giveWoodAmount.getPreferredSize());
		add(wantWoolAmountText);
		add(wantWoolAmount);

		JLabel giveOreAmountText = new JLabel("<html>Aanbod aantal<br>erts:</html>");
		giveOreAmount.setMaximumSize(giveWoodAmount.getPreferredSize());
		giveOreAmount.setMinimumSize(giveWoodAmount.getPreferredSize());
		add(giveOreAmountText);
		add(giveOreAmount);

		JLabel wantOreAmountText = new JLabel("<html>Vraag aantal<br>erts:</html>");
		wantOreAmount.setMaximumSize(giveWoodAmount.getPreferredSize());
		wantOreAmount.setMinimumSize(giveWoodAmount.getPreferredSize());
		add(wantOreAmountText);
		add(wantOreAmount);

		JLabel nameView = new JLabel(name);
		nameView.setFont(new Font("SansSerif", Font.BOLD, 20));
		add(nameView);

		JLabel filler = new JLabel("");
		add(filler);

		cancelTrade.setMaximumSize(cancelTrade.getPreferredSize());
		cancelTrade.setMinimumSize(cancelTrade.getPreferredSize());
		add(cancelTrade);

		confirmTrade.setMaximumSize(confirmTrade.getPreferredSize());
		confirmTrade.setMinimumSize(confirmTrade.getPreferredSize());
		add(confirmTrade);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	}

	private void createTrade() {
		numberWoodList = new JList<>(numbers);
		numberWoodList.addListSelectionListener(e -> giveWoodAmountCounter = numberWoodList.getSelectedIndex());
		numberWoodList.setVisibleRowCount(1);

		giveWoodAmount = new JScrollPane(numberWoodList);
		giveWoodAmount.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		numberWoodList.setSelectedIndex(0);

		// want wood amount
		numberWoodListW = new JList<>(numbers);
		numberWoodListW.addListSelectionListener(e -> wantWoodAmountCounter = numberWoodListW.getSelectedIndex());
		numberWoodListW.setVisibleRowCount(1);

		wantWoodAmount = new JScrollPane(numberWoodListW);
		wantWoodAmount.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		numberWoodListW.setSelectedIndex(0);

		// give clay amount
		numberClayList = new JList<>(numbers);
		numberClayList.addListSelectionListener(e -> giveClayAmountCounter = numberClayList.getSelectedIndex());
		numberClayList.setVisibleRowCount(1);

		giveClayAmount = new JScrollPane(numberClayList);
		giveClayAmount.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		numberClayList.setSelectedIndex(0);

		// want clay amount
		numberClayListW = new JList<>(numbers);
		numberClayListW.addListSelectionListener(e -> wantClayAmountCounter = numberClayListW.getSelectedIndex());
		numberClayListW.setVisibleRowCount(1);

		wantClayAmount = new JScrollPane(numberClayListW);
		wantClayAmount.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		numberClayListW.setSelectedIndex(0);

		// give grain amount
		numberGrainList = new JList<>(numbers);
		numberGrainList.addListSelectionListener(e -> giveGrainAmountCounter = numberGrainList.getSelectedIndex());
		numberGrainList.setVisibleRowCount(1);

		giveGrainAmount = new JScrollPane(numberGrainList);
		giveGrainAmount.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		numberGrainList.setSelectedIndex(0);

		// want grain amount
		numberGrainListW = new JList<>(numbers);
		numberGrainListW.addListSelectionListener(e -> wantGrainAmountCounter = numberGrainListW.getSelectedIndex());
		numberGrainListW.setVisibleRowCount(1);

		wantGrainAmount = new JScrollPane(numberGrainListW);
		wantGrainAmount.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		numberGrainListW.setSelectedIndex(0);

		// give wool amount
		numberWoolList = new JList<>(numbers);
		numberWoolList.addListSelectionListener(e -> giveWoolAmountCounter = numberWoolList.getSelectedIndex());
		numberWoolList.setVisibleRowCount(1);

		giveWoolAmount = new JScrollPane(numberWoolList);
		giveWoolAmount.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		numberWoolList.setSelectedIndex(0);

		// want wool amount
		numberWoolListW = new JList<>(numbers);
		numberWoolListW.addListSelectionListener(e -> wantWoolAmountCounter = numberWoolListW.getSelectedIndex());
		numberWoolListW.setVisibleRowCount(1);

		wantWoolAmount = new JScrollPane(numberWoolListW);
		wantWoolAmount.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		numberWoolListW.setSelectedIndex(0);

		// give ore amount
		numberOreList = new JList<>(numbers);
		numberOreList.addListSelectionListener(e -> giveOreAmountCounter = numberOreList.getSelectedIndex());
		numberOreList.setVisibleRowCount(1);

		giveOreAmount = new JScrollPane(numberOreList);
		giveOreAmount.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		numberOreList.setSelectedIndex(0);

		// want ore amount
		numberOreListW = new JList<>(numbers);
		numberOreListW.addListSelectionListener(e -> wantOreAmountCounter = numberOreListW.getSelectedIndex());
		numberOreListW.setVisibleRowCount(1);

		wantOreAmount = new JScrollPane(numberOreListW);
		wantOreAmount.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		numberOreListW.setSelectedIndex(0);

	}

	public void clearTrade() {
		// confirmed
		boolean confirmed = false;

		numberWoodList.setSelectedIndex(0);
		numberWoodListW.setSelectedIndex(0);
		numberClayList.setSelectedIndex(0);
		numberClayListW.setSelectedIndex(0);
		numberGrainList.setSelectedIndex(0);
		numberGrainListW.setSelectedIndex(0);
		numberWoolList.setSelectedIndex(0);
		numberWoolListW.setSelectedIndex(0);
		numberOreList.setSelectedIndex(0);
		numberOreListW.setSelectedIndex(0);
	}

	public boolean returnOffer(TradeModel returnOffer) {
		return returnOffer(returnOffer.getGiveWood(), returnOffer.getGiveBrick(), returnOffer.getGiveWool(),
				returnOffer.getGiveGrain(), returnOffer.getGiveOre(), returnOffer.getWantsWood(),
				returnOffer.getWantsBrick(), returnOffer.getWantsWool(), returnOffer.getWantsGrain(),
				returnOffer.getWantsOre());
	}

	private boolean returnOffer(int giveWood, int giveBrick, int giveWool, int giveGrain, int giveOre, int wantsWood,
			int wantsBrick, int wantsWool, int wantsGrain, int wantsOre) {
		JOptionPane popup = new JOptionPane();
		int buttons = JOptionPane.YES_NO_OPTION;

		int result = JOptionPane.showConfirmDialog(popup,
				"Tegen speler wil " + giveWood + " hout, " + giveBrick + " klei, " + giveWool + " wol, " + giveGrain
						+ " graan en" + giveOre + " erts ruilen tegen " + wantsWood + " hout, " + wantsBrick + " klei, "
						+ wantsWool + " wol, " + wantsGrain + " graan en " + wantsOre + " erts. Accepteren?",
				"Er is een tegenbod", buttons, JOptionPane.QUESTION_MESSAGE);


		return result == 0;
	}

	public int showOffer(String username, TradeModel offer) {
		return showOffer(username, offer.getGiveWood(), offer.getGiveBrick(), offer.getGiveWool(), offer.getGiveGrain(),
				offer.getGiveOre(), offer.getWantsWood(), offer.getWantsBrick(), offer.getWantsWool(),
				offer.getWantsGrain(), offer.getWantsOre());
	}

	private int showOffer(String username, int giveWood, int giveBrick, int giveWool, int giveGrain, int giveOre,
			int wantsWood, int wantsBrick, int wantsWool, int wantsGrain, int wantsOre) {
		JOptionPane popup = new JOptionPane();
		int buttons = JOptionPane.YES_NO_CANCEL_OPTION;

		int tradeOption = JOptionPane.showConfirmDialog(popup,
				username + " wil\n" + giveWood + " hout, " + giveBrick + " klei, " + giveWool + " wol, " + giveGrain
						+ " graan en " + giveOre + " erts\nruilen tegen\n" + wantsWood + " hout, " + wantsBrick
						+ " klei, " + wantsWool + " wol, " + wantsGrain + " graan en " + wantsOre
						+ " erts.\nAccepteren?",
				"Ruilaanbod", buttons, JOptionPane.QUESTION_MESSAGE);

		return tradeOption;
	}

	public JButton getCancelTradeButton() {
		return cancelTrade;
	}

	public JButton getConfirmTradeButton() {
		return confirmTrade;
	}

	// getters voor de resources
	public int getGiveWoodAmount() {
		return giveWoodAmountCounter;
	}

	public int getGiveClayAmount() {
		return giveClayAmountCounter;
	}

	public int getGiveGrainAmount() {
		return giveGrainAmountCounter;
	}

	public int getGiveWoolAmount() {
		return giveWoolAmountCounter;
	}

	public int getGiveOreAmount() {
		return giveOreAmountCounter;
	}

	public int getWantWoodAmount() {
		return wantWoodAmountCounter;
	}

	public int getWantClayAmount() {
		return wantClayAmountCounter;
	}

	public int getWantGrainAmount() {
		return wantGrainAmountCounter;
	}

	public int getWantWoolAmount() {
		return wantWoolAmountCounter;
	}

	public int getWantOreAmount() {
		return wantOreAmountCounter;
	}

	public int getTotalGive() {
		return giveWoodAmountCounter + giveClayAmountCounter + giveWoolAmountCounter + giveGrainAmountCounter
				+ giveOreAmountCounter;
	}

	public int getTotalWant() {
		return wantWoodAmountCounter + wantClayAmountCounter + wantWoolAmountCounter + wantGrainAmountCounter
				+ wantOreAmountCounter;
	}

	public int[] getGives() {
		int[] gives = new int[5];
		gives[0] = giveWoodAmountCounter;
		gives[1] = giveClayAmountCounter;
		gives[2] = giveWoolAmountCounter;
		gives[3] = giveGrainAmountCounter;
		gives[4] = giveOreAmountCounter;
		return gives;
	}

	public int[] getWants() {
		int[] wants = new int[5];
		wants[0] = wantWoodAmountCounter;
		wants[1] = wantClayAmountCounter;
		wants[2] = wantWoolAmountCounter;
		wants[3] = wantGrainAmountCounter;
		wants[4] = wantOreAmountCounter;
		return wants;
	}

	public boolean tradeBank() {
		int counter = 0;
		int counter2 = 0;

		for (int i = 0; i < getGives().length; i++) {
			if (getGives()[i] != 0) {
				counter++;
			}
		}

		for (int i = 0; i < getWants().length; i++) {
			if (getWants()[i] != 0) {
				counter2++;
			}
		}

		return counter == 1 && counter2 == 1;
	}

	public int getFilledGiveBank() {
		int amount = 0;
		for (int i = 0; i < getGives().length; i++) {
			if (getGives()[i] != 0) {
				amount = getGives()[i];
			}
		}
		return amount;
	}

	public int getFilledWantBank() {
		int amount = 0;
		for (int i = 0; i < getWants().length; i++) {
			if (getWants()[i] != 0) {
				amount = getWants()[i];
			}
		}
		return amount;
	}

	public int getTypeGiveFilledBank() {
		int type = 0;
		for (int i = 0; i < getGives().length; i++) {
			if (getGives()[i] != 0) {
				type = i;
			}
		}
		return type;
	}

	public int getTypeWantFilledBank() {
		int type = 0;
		for (int i = 0; i < getWants().length; i++) {
			if (getWants()[i] != 0) {
				type = i;
			}
		}
		return type;
	}

}
