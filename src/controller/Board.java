package controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import model.Road;

public class Board {
	// grid.
	// instvars.
	private Tile[] tiles = new Tile[19];
	private Location[] locations = new Location[73];
	// locations worden met een loop aangemaakt\73 locatie punten
	private Set<String> locs;
	private int[] xArray;
	private int[] yArray;

	public Board() {
		locs = new HashSet<String>();
		xArray = new int[73];
		yArray = new int[73];

        fillLocations();
	}

	private void fillBorderLocations(Tile tile) {
		// Zoekt de borderlocations en zet ze in de juiste volgorde in de locations
		// array van Tile.
		Location[] locationBorders = new Location[6];
		int x = tile.getXLoc();
		int y = tile.getYLoc();
		locationBorders[0] = getLocation(x, y + 1);
		locationBorders[1] = getLocation(x + 1, y + 1);
		locationBorders[2] = getLocation(x + 1, y);
		locationBorders[3] = getLocation(x, y - 1);
		locationBorders[4] = getLocation(x - 1, y - 1);
		locationBorders[5] = getLocation(x - 1, y);
		// zet de locations in de array van borderLocations.
		tile.setBorderLocations(locationBorders);
	}

	public ArrayList<Tile> getTiles(int value) {
		// geef array van tegels terug om kaarten te geven
		// value is het getal van dobbelsteen, die overeenkomt met getallenfiche.

		ArrayList<Tile> tileslist = new ArrayList<>();
		for (int i = 0; i < tiles.length; i++) {
			if (tiles[i].getValue() == value) {
				tileslist.add(tiles[i]);
			}
		}
		return tileslist;
	}

	public Location getLocation(int x, int y) {
		for (Location location : locations) {
			if (location.getX() == x && location.getY() == y) {
				return location;
			}
		}
		return null;
	}

	

	public Set<String> getLocations() {
		for (int i = 0; i < locations.length; i++) {
			if (locations[i] != null) {
				this.fillXLoc(locations[i].getX(), i);
				this.fillYLoc(locations[i].getY(), i);
				String cords = "" + locations[i].getX() + "," + locations[i].getY();
				locs.add(cords);
			}
		}
		return locs;
	}

	public void fillXLoc(int x, int loopcounter) {
		x = xArray[loopcounter];
	}

	public void fillYLoc(int y, int loopcounter) {
		y = yArray[loopcounter];
	}

	public Location[] getBorderLocations(int x, int y) {
		Location[] locations = new Location[6];

		// pakt alle punten om zich heen //
		if (getLocation(x, y + 1) != null) {
			locations[0] = getLocation(x, y + 1);
		}
		if (getLocation(x + 1, y + 1) != null) {
			locations[1] = getLocation(x + 1, y + 1);
		}
		if (getLocation(x + 1, y) != null) {
			locations[2] = getLocation(x + 1, y);
		}
		if (getLocation(x, y - 1) != null) {
			locations[3] = getLocation(x, y - 1);
		}
		if (getLocation(x, y - 1) != null) {
			locations[4] = getLocation(x - 1, y - 1);
		}
		if (getLocation(x - 1, y) != null) {
			locations[5] = getLocation(x - 1, y);
		}

		return locations;
	}

	// wessels FILL BOARD STUFF
	public void fillLocations() {
		for (int i = 0; i < locations.length; i++) {
			locations[i] = new Location(i);

		}
	}

	public void fillTiles(int gameID) {
		for (int i = 0; i < tiles.length; i++) {
			tiles[i] = new Tile(i, gameID);
			setTileLocation(i);
			fillBorderLocations(tiles[i]);
		}
		
	}

	public void setTileLocation(int id) {
		tiles[id].setOwnLocation(getLocation(tiles[id].getXLoc(), tiles[id].getYLoc()));
	}


	public Tile getTiles2(int index) {
		return tiles[index];
	}

	public Tile[] getTiles() {
		return tiles;
	}
	public ArrayList<Road> getRoads() {
		ArrayList<Road> playedRoads = new ArrayList<>();
		Road[] localRoads;
		for (int i = 0; i < locations.length; i++) {
			localRoads = locations[i].getRoads();
			for (int y = 0; y < localRoads.length; y++) {
				if (localRoads[y] != null && localRoads[y].isPlayed()) {
					playedRoads.add(localRoads[y]);
				}
			}
		}
		return playedRoads;
	}


}
