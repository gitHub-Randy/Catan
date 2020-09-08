package controller;

import java.util.ArrayList;

import model.LocationModel;
import model.Road;
import model.Settlement;

public class Location {

	private LocationModel locModel;
	private ArrayList<Road> visited;
	public Location(int row) {
		locModel = LocationModel.getFromDatabase(row);
	}
	
	public int getUserID() {
		try {
			return locModel.getSettlement().getUserId();
		} catch (NullPointerException e) {
			return -1;
		}
	}

	public char getType() {
		return locModel.getSettlement().getStructureType();
	}

	public int getX() {
		return locModel.getX();
	}

	public int getY() {
		return locModel.getY();
	}

	public boolean hasSettlement() {
		return locModel.getSettlement() != null;
	}

	public void setSettlement(Settlement settlement) {
		locModel.setSettlement(settlement);
	}

	public int getSettlementUser() {
		return locModel.getSettlement().getUserId();
	}

	public boolean hasHarbour() {
		return locModel.isHarbor();
	}

	public char getHarbourResource() {
		return locModel.getHarborResourceTypeId();
	}

	public Settlement getSettlement() {
		return locModel.getSettlement();
	}

	public Road[] getRoads() {
		return locModel.getRoads();
	}

	public void setRoads(Road road) {
		locModel.setRoads(road);
	}

	public boolean hasRoads(int userid) {
		return locModel.hasRoads(userid);
	}

	public LocationModel getLocModel() {
		return locModel.getLocModel();
	}
	
	 public int getHandelsRouteLengte()
	  	{
	            int aantal = 0;
	            for (Road stuk : locModel.getRoads())
	            {
	                  visited = new ArrayList<Road>();
	 
	                  visited.add(stuk);
	 
	                  int aantal_van = getHandelsRouteLengte(stuk.getXPos1(), stuk.getYPos1());
	                  int aantal_naar = getHandelsRouteLengte(stuk.getXPos2(), stuk.getYPos2());
	 
	                  aantal = Math.max(aantal, 1 + aantal_van + aantal_naar);
	            }
	            return aantal;
	  	}
	 
	      public int getHandelsRouteLengte(int x, int y)
	  	{
	        	ArrayList<Road> queue = new ArrayList<Road>();
	 
	            for (Road stuk : locModel.getRoads())
	        	{
	                  if (!visited.contains(stuk) && isConnected(x, y, stuk))
	              	{
	                        visited.add(stuk);
	                        queue.add(stuk);
	              	}
	        	}
	 
	            int aantal = 0;
	            for (int i = 0; i < queue.size(); i++)
	            {
	              	Road stuk = queue.get(i);
	                 
	                  int aantal_van = getHandelsRouteLengte(stuk.getXPos1(), stuk.getYPos1());
	                  int aantal_naar = getHandelsRouteLengte(stuk.getXPos2(), stuk.getYPos2());
	 
	                  aantal = Math.max(aantal, 1 + aantal_van + aantal_naar);
	            }
	 
	            return aantal;
	  	}
	 
	      private boolean isConnected(int x, int y, Road stuk)
	  	{
	            boolean connected = false;
	 
	            connected |= x == stuk.getXPos1() && y == stuk.getYPos1();
	            connected |= x == stuk.getXPos2() && y == stuk.getYPos2();
	 
	            return connected;
	  	}


}
