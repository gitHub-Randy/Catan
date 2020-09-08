package controller;

import view.CatanGui;

public class Game {

	private Catan c;
	private CatanGui gui;

	public Game() {
		gui = new CatanGui(this);
	}
	
	public void createCatan() {
		c = new Catan(gui);
		gui.setCatanController(c);
	}
	
	public Catan getCatan() {
		return c;
	}
}
