package model;

import database.DBConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class LobbyModel {
    //getters
    public ArrayList<String> getAllUsers() {
        ArrayList<String> list = new ArrayList<>();
        if (DBConnection.checkConnection()) {
            try {
                Statement statement = DBConnection.connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT username FROM account;");
                while (resultSet.next()) {
                    list.add(resultSet.getString(1));
                }
            } catch (SQLException ex) {
            }
        }
        return list;
    }

    public int getLastUserID() {
        if (DBConnection.checkConnection()) {
            try {
                Statement statement = DBConnection.connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT MAX(idspeler) FROM speler;");
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            } catch (SQLException ex) {
            }
        }
        return -1;
    }

    public String getHostName(int gameID) {
        if (DBConnection.checkConnection()) {
            try {
                Statement statement = DBConnection.connection.createStatement();

                ResultSet resultSet = statement.executeQuery("SELECT username FROM speler WHERE speelstatus = 'uitdager' AND idspel = " + gameID + ";");
                if (resultSet.next()) {
                    return resultSet.getString(1);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return null;
    }

    public int getHostUserID(int gameID) {
        if (DBConnection.checkConnection()) {
            try {
                PreparedStatement preparedStatement = DBConnection.connection.prepareStatement("SELECT idspeler FROM speler WHERE speelstatus = 'uitdager' AND idspel = ?;");

                preparedStatement.setInt(1, gameID);

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

    public String getPlayerStatus(int playerID) {
        String query = "SELECT speelstatus FROM speler WHERE idspeler = " + playerID + ";";
        if (DBConnection.checkConnection()) {
            try {
                Statement statement = DBConnection.connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                if (resultSet.next()) {
                    return resultSet.getString(1);
                }
            } catch (SQLException ex) {
            }
        }
        return "";
    }

    public ArrayList<Integer> getPlayerIDFromUsernameWithInviteStatus(String username) {
        String query = "SELECT idspeler FROM speler WHERE username = '" + username + "' AND speelstatus = 'uitgedaagde'";
        ArrayList<Integer> list = new ArrayList<>();
        if (DBConnection.checkConnection()) {
            try {
                Statement stmt = DBConnection.connection.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    list.add(rs.getInt(1));
                }
            } catch (SQLException ex) {
            }
        }
        return list;
    }

    public ArrayList<Integer> getPayerIDFromUsernameWithRejoinStatus(String username) {
        String query = "SELECT idspeler FROM speler WHERE username = '" + username + "' AND speelstatus = 'geaccepteerd' OR username = '" + username + "' AND speelstatus = 'uitdager'";
        ArrayList<Integer> list = new ArrayList<>();
        if (DBConnection.checkConnection()) {
            try {
                Statement stmt = DBConnection.connection.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    list.add(rs.getInt(1));
                }
            } catch (SQLException ex) {
            }
        }
        return list;
    }

    public int getGameIDFromPlayerID(int id) {
        String query = "SELECT idspel FROM speler WHERE idspeler = '" + id + "';";
        if (DBConnection.checkConnection()) {
            try {
                Statement stmt = DBConnection.connection.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                if (rs.next()) {
                    return rs.getInt(1);
                }
            } catch (SQLException ex) {
            }
        }
        return -1;
    }

    public String[] getHostNamesInvites(String username) {
        //geeft alle host namen van de spellen waar een speler is geinvite
        String query;
        ArrayList<String> hostNames = new ArrayList<>();
        ArrayList<Integer> playerID = getPlayerIDFromUsernameWithInviteStatus(username);
        if (DBConnection.checkConnection()) {
            try {
                for (int i = 0; i < playerID.size(); i++) {
                    int spelid = getGameIDFromPlayerID(playerID.get(i));
                    query = "SELECT username FROM speler WHERE speelstatus = 'uitdager' AND idspel = '" + spelid + "';";
                    Statement stmt = DBConnection.connection.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs.next()) {
                        hostNames.add(rs.getString(1));
                    }
                }
            } catch (SQLException ex) {
            }
        }
        String[] s = new String[hostNames.size()];
        hostNames.toArray(s);
        return s;
    }

    public String[] getHostNamesRejoins(String username) {
        //geeft alle host namen van de spellen waar een speler is geinvite
        String query;
        ArrayList<String> hostNames = new ArrayList<>();
        ArrayList<Integer> playerID = getPayerIDFromUsernameWithRejoinStatus(username);
        if (DBConnection.checkConnection()) {
            try {
                for (int i = 0; i < playerID.size(); i++) {
                    int spelid = getGameIDFromPlayerID(playerID.get(i));
                    query = "SELECT username FROM speler WHERE speelstatus = 'uitdager' AND idspel = '" + spelid + "' OR speelstatus = 'geaccepteerd' AND idspel = '" + spelid + "';";
                    Statement stmt = DBConnection.connection.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs.next()) {
                        hostNames.add(rs.getString(1));
                    }
                }
            } catch (SQLException ex) {
            }
        }
        String[] s = new String[hostNames.size()];
        hostNames.toArray(s);
        return s;
    }

    public String getNameFromGame(int gameID, int number) {
        String query = "SELECT username FROM speler WHERE idspel = " + gameID + " AND volgnr =  " + number + ";";
        if (DBConnection.checkConnection()) {
            try {
                Statement statement = DBConnection.connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                if (resultSet.next()) {
                    return resultSet.getString(1);
                }
            } catch (SQLException ex) {
            }
        }
        return "";
    }

    public String getNameFromGame(int gameID, String kleur) {
        String query = "SELECT username FROM speler WHERE idspel = " + gameID + " AND kleur =  '" + kleur + "';";
        if (DBConnection.checkConnection()) {
            try {
                Statement statement = DBConnection.connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                if (resultSet.next()) {
                    return resultSet.getString(1);
                }
            } catch (SQLException ex) {
            }
        }
        return "";
    }

    public int getPlayerIDFromGame(int gameID, int number) {
        String query = "SELECT idspeler FROM speler WHERE idspel = " + gameID + " AND volgnr =  " + number + ";";
        if (DBConnection.checkConnection()) {
            try {
                Statement statement = DBConnection.connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            } catch (SQLException ex) {
            }
        }
        return -1;
    }

    //inserts
    public int createPlayer(String username, int gameID, String color, String status, int number) {
        int playerID = getLastUserID() + 1;
        String query = "INSERT INTO speler VALUES (" + playerID + "," + gameID + ",'" + username + "','" + color + "','" + status + "',false," + number + ");";
        if (DBConnection.checkConnection()) {
            try {
                Statement statement = DBConnection.connection.createStatement();
                statement.executeUpdate(query);
            } catch (SQLException ex) {
            }
        }
        return playerID;
    }

    //update
    public void updatePlayerStatus(int playerID, String status) {
        String query = "UPDATE speler SET speelstatus = '" + status + "' WHERE idspeler = " + playerID + ";";
        if (DBConnection.checkConnection()) {
            try {
                Statement statement = DBConnection.connection.createStatement();
                statement.executeUpdate(query);
            } catch (SQLException ex) {
            }
        }
    }


}
