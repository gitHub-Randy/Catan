package controller;

import model.TileModel;

public class Tile {
	@SuppressWarnings("unused")
	private Location location;// set met de x en de y uit model
	private TileModel model;
	private Location[] borderLocations = new Location[6];

	public Tile(int id, int gameID) {
		model = TileModel.getFromDatabase(id, gameID);
	}

	public void setBorderLocations(Location[] locations) {
		this.borderLocations = locations;
	}

	public Location[] getBorderLoc() {
		return borderLocations;
	}

	public char getResourceType() {
		return model.getResourceTypeId();
	}

	public void setOwnLocation(Location l) {
		this.location = l;
	}

	public int getXLoc() {
		return model.getXPos();
	}

	public int getYLoc() {
		return model.getYPos();
	}

	public int getValue() {
		return model.getValue();
	}

	public boolean getRobber() {
		return model.hasRobber();
	}

	public int getTileId() {
		return model.getTileId();
	}
}
