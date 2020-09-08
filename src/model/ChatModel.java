package model;

import database.DBConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Observable;

public class ChatModel extends Observable implements Runnable {
	// instance
	private int userID;
	private String fullMessage;
	private String message;
	private String username;
	private String lastTimeStamp;
	private String currentTimeStamp;
	private Thread chatThread;
	private boolean newMessage;
	private ArrayList<String> fullMessages;
	private int gameId;
	private int totalValue;

	public ChatModel() {
		lastTimeStamp = getBeginTime();
		fullMessages = new ArrayList<>();

		// thread
		chatThread = new Thread(this);
		chatThread.start();
	}

	// add message
	public void addNewMessages(String message) {
		setCurrentTimeStamp();
		if (DBConnection.checkConnection()) {
			// nog own player id toevoegen.
			try {
				PreparedStatement preparedStatement = DBConnection.connection
						.prepareStatement("INSERT INTO chatregel (tijdstip, idspeler, bericht) VALUES (?, ?, ?);");

				preparedStatement.setString(1, getCurrentTimeStamp());
				preparedStatement.setInt(2, userID);
				preparedStatement.setString(3, message);

				preparedStatement.execute();

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	// thread
	@Override
	public void run() {
		while (true) {
			if (DBConnection.checkConnection()) {
				try {
					// System.out.println("b " + lastTimeStamp);
					PreparedStatement preparedStatement = DBConnection.connection.prepareStatement(
							"SELECT c.bericht, s.username FROM speler s JOIN chatregel c ON s.idspeler = c.idspeler WHERE c.tijdstip > ? AND s.idspel = ? ORDER BY c.tijdstip;");

					preparedStatement.setString(1, lastTimeStamp);
					preparedStatement.setInt(2, gameId);

					ResultSet resultSet = preparedStatement.executeQuery();
					while (resultSet.next()) {
						message = resultSet.getString("bericht");
						username = resultSet.getString("s.username");
						fullMessage = String.format("%s: %s\n", username, message);
						fullMessages.add(fullMessage);
						// System.out.println("m " + fullMessage);
						setLastTimeStamp();
						setChanged();
					}

					notifyObservers();

					if (fullMessages != null) {
						fullMessages.clear();
					}

					// System.out.println("a " + lastTimeStamp);
				} catch (SQLException e) {
					e.printStackTrace();
				}

				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void logThrow(int totalValue) {
		setCurrentTimeStamp();

		String logMessage = "heeft " + totalValue + " gegooid! [LOG]";
		if (DBConnection.checkConnection()) {
			try {
				PreparedStatement preparedStatement = DBConnection.connection
						.prepareStatement("INSERT INTO chatregel (tijdstip, idspeler, bericht) VALUES (?, ?, ?);");

				preparedStatement.setString(1, getCurrentTimeStamp());
				preparedStatement.setInt(2, userID);
				preparedStatement.setString(3, logMessage);

				preparedStatement.execute();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void logTurn() {
		setCurrentTimeStamp();

		String logMessage = "is aan de beurt [LOG]";
		if (DBConnection.checkConnection()) {
			try {
				PreparedStatement preparedStatement = DBConnection.connection
						.prepareStatement("INSERT INTO chatregel (tijdstip, idspeler, bericht) VALUES (?, ?, ?);");

				preparedStatement.setString(1, getCurrentTimeStamp());
				preparedStatement.setInt(2, userID);
				preparedStatement.setString(3, logMessage);

				preparedStatement.execute();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void logVillage() {
		setCurrentTimeStamp();

		String logMessage = "heeft een dorp gebouwd [LOG]";
		if (DBConnection.checkConnection()) {
			try {
				PreparedStatement preparedStatement = DBConnection.connection
						.prepareStatement("INSERT INTO chatregel (tijdstip, idspeler, bericht) VALUES (?, ?, ?);");

				preparedStatement.setString(1, getCurrentTimeStamp());
				preparedStatement.setInt(2, userID);
				preparedStatement.setString(3, logMessage);

				preparedStatement.execute();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void logCity() {
		setCurrentTimeStamp();

		String logMessage = "heeft een stad gebouwd [LOG]";
		if (DBConnection.checkConnection()) {
			try {
				PreparedStatement preparedStatement = DBConnection.connection
						.prepareStatement("INSERT INTO chatregel (tijdstip, idspeler, bericht) VALUES (?, ?, ?);");

				preparedStatement.setString(1, getCurrentTimeStamp());
				preparedStatement.setInt(2, userID);
				preparedStatement.setString(3, logMessage);

				preparedStatement.execute();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void logRoad() {
		setCurrentTimeStamp();

		String logMessage = "heeft een weg gebouwd [LOG]";
		if (DBConnection.checkConnection()) {
			try {
				PreparedStatement preparedStatement = DBConnection.connection
						.prepareStatement("INSERT INTO chatregel (tijdstip, idspeler, bericht) VALUES (?, ?, ?);");

				preparedStatement.setString(1, getCurrentTimeStamp());
				preparedStatement.setInt(2, userID);
				preparedStatement.setString(3, logMessage);

				preparedStatement.execute();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void logRobber(int robberTile) {
		setCurrentTimeStamp();

		String logMessage = "heeft de struikrover verplaatst naar locatie: " + robberTile + " [LOG]";
		if (DBConnection.checkConnection()) {
			try {
				PreparedStatement preparedStatement = DBConnection.connection
						.prepareStatement("INSERT INTO chatregel (tijdstip, idspeler, bericht) VALUES (?, ?, ?);");

				preparedStatement.setString(1, getCurrentTimeStamp());
				preparedStatement.setInt(2, userID);
				preparedStatement.setString(3, logMessage);

				preparedStatement.execute();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void logBuyDevCards() {
		setCurrentTimeStamp();

		String logMessage = "heeft een ontwikkelingskaart gekocht [LOG]";
		if (DBConnection.checkConnection()) {
			try {
				PreparedStatement preparedStatement = DBConnection.connection
						.prepareStatement("INSERT INTO chatregel (tijdstip, idspeler, bericht) VALUES (?, ?, ?);");

				preparedStatement.setString(1, getCurrentTimeStamp());
				preparedStatement.setInt(2, userID);
				preparedStatement.setString(3, logMessage);

				preparedStatement.execute();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void logRecourses(char resource, int amount) {
		setCurrentTimeStamp();

		String logMessage = "heeft ";
		switch (resource) {
		case 'W':
			logMessage = logMessage + Integer.toString(amount) + " wol ";
			break;
		case 'G':
			logMessage = logMessage + Integer.toString(amount) + " graan ";
			break;
		case 'B':
			logMessage = logMessage + Integer.toString(amount) + " steen ";
			break;
		case 'E':
			logMessage = logMessage + Integer.toString(amount) + " erts ";
			break;
		case 'H':
			logMessage = logMessage + Integer.toString(amount) + " hout ";
			break;
		}

		logMessage = logMessage + "ontvangen [log]";

		if (DBConnection.checkConnection()) {
			try {
				PreparedStatement preparedStatement = DBConnection.connection
						.prepareStatement("INSERT INTO chatregel (tijdstip, idspeler, bericht) VALUES (?, ?, ?);");

				preparedStatement.setString(1, getCurrentTimeStamp());
				preparedStatement.setInt(2, userID);
				preparedStatement.setString(3, logMessage);

				preparedStatement.execute();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void logPlayDevCard(String type) {
		setCurrentTimeStamp();

		String logMessage = "heeft een " + type + " gespeeld [LOG]";
		if (DBConnection.checkConnection()) {
			try {
				PreparedStatement preparedStatement = DBConnection.connection
						.prepareStatement("INSERT INTO chatregel (tijdstip, idspeler, bericht) VALUES (?, ?, ?);");

				preparedStatement.setString(1, getCurrentTimeStamp());
				preparedStatement.setInt(2, userID);
				preparedStatement.setString(3, logMessage);

				preparedStatement.execute();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void logMostRoads() {
		setCurrentTimeStamp();

		String logMessage = "heeft de langste handelsroute gekregen [LOG]";
		if (DBConnection.checkConnection()) {
			try {
				PreparedStatement preparedStatement = DBConnection.connection
						.prepareStatement("INSERT INTO chatregel (tijdstip, idspeler, bericht) VALUES (?, ?, ?);");

				preparedStatement.setString(1, getCurrentTimeStamp());
				preparedStatement.setInt(2, userID);
				preparedStatement.setString(3, logMessage);

				preparedStatement.execute();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void logTrade(int giveType, int wantType, int giveAmount, int getAmount) {
		setCurrentTimeStamp();

		String logMessage = "heeft de trade: " + Integer.toString(giveAmount) + intToString(giveType) + "tegen "
				+ Integer.toString(getAmount) + intToString(wantType) + "gemaakt [LOG]";
		
		if (DBConnection.checkConnection()) {
			try {
				PreparedStatement preparedStatement = DBConnection.connection
						.prepareStatement("INSERT INTO chatregel (tijdstip, idspeler, bericht) VALUES (?, ?, ?);");

				preparedStatement.setString(1, getCurrentTimeStamp());
				preparedStatement.setInt(2, userID);
				preparedStatement.setString(3, logMessage);

				preparedStatement.execute();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public String intToString(int typeNumber) {
		String resource = " ";
		switch (typeNumber) {
		case 0:
			resource = " hout ";
			break;
		case 1:
			resource = " steen ";
			break;
		case 2:
			resource = " wol ";
			break;
		case 3:
			resource = " graan ";
			break;
		case 4:
			resource = " erts ";
			break;
		}
		return resource;
	}

	public void logTrade(String player, int giveWood, int giveClay, int giveSheep, int giveGrain, int giveOre,
			int wantsWood, int wantsClay, int wantsSheep, int wantsGrain, int wantsOre) {
		setCurrentTimeStamp();

		String logMessage = "heeft de trade: ";

		if (giveWood != 0) {
			logMessage = logMessage + Integer.toString(giveWood) + "hout ";
		}
		if (giveClay != 0) {
			logMessage = logMessage + Integer.toString(giveClay) + "steen ";
		}
		if (giveSheep != 0) {
			logMessage = logMessage + Integer.toString(giveSheep) + "wol ";
		}
		if (giveGrain != 0) {
			logMessage = logMessage + Integer.toString(giveGrain) + "graan ";
		}
		if (giveOre != 0) {
			logMessage = logMessage + Integer.toString(giveOre) + "erts ";
		}
		logMessage = logMessage + "tegen ";
		if (wantsWood != 0) {
			logMessage = logMessage + Integer.toString(wantsWood) + "hout ";
		}
		if (wantsClay != 0) {
			logMessage = logMessage + Integer.toString(wantsClay) + "steen ";
		}
		if (wantsSheep != 0) {
			logMessage = logMessage + Integer.toString(wantsSheep) + "wol ";
		}
		if (wantsGrain != 0) {
			logMessage = logMessage + Integer.toString(wantsGrain) + "graan ";
		}
		if (wantsOre != 0) {
			logMessage = logMessage + Integer.toString(wantsOre) + "erts ";
		}

		logMessage = logMessage + "gemaakt met " + player + " [LOG]";
		if (DBConnection.checkConnection()) {
			try {
				PreparedStatement preparedStatement = DBConnection.connection
						.prepareStatement("INSERT INTO chatregel (tijdstip, idspeler, bericht) VALUES (?, ?, ?);");

				preparedStatement.setString(1, getCurrentTimeStamp());
				preparedStatement.setInt(2, userID);
				preparedStatement.setString(3, logMessage);

				preparedStatement.execute();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	// getters
	public String getLastTimeStamp() {
		return lastTimeStamp;
	}

	public ArrayList<String> getFullMessages() {
		return fullMessages;
	}

	public String getCurrentTimeStamp() {
		return currentTimeStamp;
	}

	public String getCurrentTime() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.SECOND, calendar.get(Calendar.SECOND) - 1);
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime());
	}

	public String getBeginTime() {
        Calendar calendar = Calendar.getInstance();
        return new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()) + " 00:00:00";
    }

	public String getFullMessage() {
		return fullMessage;
	}

	// setters
	public void setLastTimeStamp() {
		lastTimeStamp = getCurrentTime();
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public void setCurrentTimeStamp() {
		currentTimeStamp = getCurrentTime();
	}

	public void setNewMessage(boolean b) {
		newMessage = b;
	}
}
