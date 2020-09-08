package model;

import database.DBConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class AccountModel {
	//instance vars
    private String username;
    private String password;

    public AccountModel(String username, String password) {
        setUsername(username);
        setPassword(password);
    }

    //db insert
    public void insertInDatabase() {
        if (DBConnection.checkConnection()) {
            try {
                PreparedStatement preparedStatement = DBConnection.connection.prepareStatement("INSERT INTO account (username, wachtwoord) VALUES (?, ?);");

                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);

                preparedStatement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            } 
        }
    }
    
    //get from db
    public static AccountModel getFromDatabase(String username) {
        if (DBConnection.checkConnection()) {
            try {
                PreparedStatement preparedStatement = DBConnection.connection.prepareStatement("SELECT * FROM account WHERE username = ?;");

                preparedStatement.setString(1, username);

                ResultSet resultSet = preparedStatement.executeQuery();

                return getAccountModelFromResultSet(resultSet);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        
        return null;
    }

    public static AccountModel[] getAllFromDatabase() {
        if (DBConnection.checkConnection()) {
            try {
                Statement statement = DBConnection.connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM account ORDER BY username;");

                return getAllAccountModelsFromResultSet(resultSet);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return new AccountModel[0];
    }

    private static AccountModel getAccountModelFromResultSet(ResultSet resultSet) {
        try {
            if (resultSet.next()) {
                String username = resultSet.getString("username");
                String password = resultSet.getString("wachtwoord");

                return new AccountModel(username, password);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static AccountModel[] getAllAccountModelsFromResultSet(ResultSet resultSet) {
        ArrayList<AccountModel> accountModels = new ArrayList<>();

        try {
            while (resultSet.next()) {
                String username = resultSet.getString("username");
                String password = resultSet.getString("wachtwoord");

                accountModels.add(new AccountModel(username, password));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return accountModels.toArray(new AccountModel[accountModels.size()]);
    }
    //setters
    private void setUsername(String username) {
        if (username == null || username.length() > 25) {
            throw new IllegalArgumentException("Illegal value for username");
        }
        this.username = username;
    }

    private void setPassword(String password) {
        if (password == null || password.length() > 25) {
            throw new IllegalArgumentException("Illegal value for password");
        }
        this.password = password;
    }
    //print
    @Override
    public String toString() {
        return "AccountModel{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
