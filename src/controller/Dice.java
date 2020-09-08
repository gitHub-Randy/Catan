package controller;

import java.util.Random;

public class Dice {
	private Random random;
	private int[] dices;
	private int totalValue;

	public Dice() {
		random = new Random();
		dices = new int[2];
	}

	private int getValue() {
		return random.nextInt(6) + 1;
	}

	public int getThrow(int i) {
		return dices[i];
	}

	public int getTotalValue() {
		return totalValue;
	}

	public void roll() {
		dices[0] = getValue();
		dices[1] = getValue();
		totalValue = dices[0] + dices[1];
	}

	public void setValue(int i, int n) {
		dices[i] = n;
		this.setTotalValue();
	}
	
	public void setTotalValue() {
		totalValue = dices[0] + dices[1];
	}
}
