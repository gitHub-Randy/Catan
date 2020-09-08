package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import database.DBConnection;


public class LoginModel {
	//instance vars
	private String username;
	//checks
	public boolean checkUser(String user) {
        if (DBConnection.checkConnection()) {
            try {
                PreparedStatement preparedStatement = DBConnection.connection.prepareStatement("SELECT username FROM account");
                ResultSet resultSet = preparedStatement.executeQuery();
	            while(resultSet.next()) {
	            	if(resultSet.getString(1).equals(user)) {
	            		return true;
	            	}
	            }
            } catch (SQLException ex) {
                ex.printStackTrace();
            } 
        }		
		return false;
	}

	public boolean checkPassword(String user, String pass) {
        if (DBConnection.checkConnection()) {
            try {
                PreparedStatement preparedStatement = DBConnection.connection.prepareStatement("SELECT wachtwoord FROM account WHERE username = \"" + user + "\"");
                ResultSet resultSet = preparedStatement.executeQuery();
	            while(resultSet.next()) {
	            	if(resultSet.getString(1).equals(pass)) {
	            		this.username = user;
	            		return true;
	            	}
	            }
            } catch (SQLException ex) {
            }
        }		
		return false;
	}
	
	public boolean checkLegalName(String user) {
		if(checkUser(user)) {
			return false;
		}else if(user.length() < 3) {
			return false;
		}else if(!checkRightSimbols(user)) {
			return false;
		}		
		return true;
	}
	
	@SuppressWarnings("deprecation")
	public boolean checkRightSimbols(String user) {
		char c;
		for(int i=0; i< user.length(); i++) {
			c = user.charAt(i);
			if(!(Character.isDigit(c) || Character.isAlphabetic(c) || Character.isSpace(c))) {
				return false;
			}
		}
		return true;
	}
	//insert
	public void createNewUser(String username, String password) {
		if (DBConnection.checkConnection()) {
            try {
            	String query = "INSERT INTO account VALUES(\"" + username + "\",\"" + password + "\")";
            	Statement s = DBConnection.connection.createStatement();
            	s.executeUpdate(query);
            } catch (SQLException ex) {
            }
        }		

	
	}
	//getter
	public int getUserId(String username) {
		if (DBConnection.checkConnection()) {
			try {
				PreparedStatement preparedStatement = DBConnection.connection.prepareStatement("SELECT idspeler FROM speler WHERE username = ?;");

				preparedStatement.setString(1, username);

				ResultSet resultSet = preparedStatement.executeQuery();
				if (resultSet.next()) {
					return resultSet.getInt("idspeler");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return 0;
	}

	public String getUsername() {
		return username;
	}
}
