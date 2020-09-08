package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Observable;

import controller.Dice;
import database.DBConnection;

public class AmountOfGamesModel extends Observable implements Runnable {
	// instance var
	private Thread amountOfGamesThread;
	private String game;
	private String turn;
	private int thrown1;
	private int thrown2;
	private int thrownTotal;
	private String throwPrint;
	private String robbertile;
	private String fullgame;
	private String allGames = "";
	private int userID;
	private int gameID;
	private Dice dice;

	public AmountOfGamesModel(Dice dice) {
		this.dice = dice;
		thrown1 = 0;
		thrown2 = 0;
		amountOfGamesThread = new Thread(this);
		amountOfGamesThread.start();
	}

	// setters
	public void makeAllGameslist(String game) {
		allGames = allGames + game;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public void setGameID(int gameID) {
		this.gameID = gameID;
	}

	public void setAllGames(String allGames) {
		this.allGames = allGames;
	}

	// getters
	public String getAllGames() {
		return allGames;
	}

	// thread
	@Override
	public void run() {
		while (true) {
			if (DBConnection.checkConnection()) {

				Statement stmt = null;
				String query = "select s1.idspel, (select username from spel as s1 join speler as s2 on s1.beurt_idspeler = s2.idspeler where s1.idspel = "
						+ gameID
						+ ") as beurt, laatste_worp_steen1, laatste_worp_steen2, struikrover_idtegel from spel as s1 join speler as s2 on s1.idspel = s2.idspel where s2.idspeler = "
						+ userID;

				try {
					stmt = DBConnection.connection.createStatement();
					ResultSet rs = stmt.executeQuery(query);
					while (rs.next()) {
						game = rs.getString("s1.idspel");
						turn = rs.getString("beurt");
						thrown1 = rs.getInt("laatste_worp_steen1");
						thrown2 = rs.getInt("laatste_worp_steen2");
//						if(this.getTotalValue() != 0) {
//						dice.setTotalVaue(this.getTotalValue());
//						}
						thrownTotal = thrown1 + thrown2;
						if (thrown1 == 0 || thrown2 == 0) {
							throwPrint = "Nog niet gegooid";
						}else{
							throwPrint = Integer.toString(thrownTotal);
						}
						robbertile = rs.getString("s1.struikrover_idtegel");
						fullgame = "Spelnummer: " + game + "\nSpeler aan de beurt: " + turn + "\nLaatst gegooid: "
								+ throwPrint + "\nLocatie Struikrover:" + robbertile
								+ "\n_______________________________________________\n";
						makeAllGameslist(fullgame);
						setChanged();
					}

					notifyObservers();
					setAllGames("");

					stmt.close();

				} catch (SQLException e) {
				}
			}

			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public int getTotalValue() {
		return thrown1 + thrown2;
	}
	
}