package model;

import database.DBConnection;

import java.sql.*;
import java.util.ArrayList;

public class TradeModel {
	//instance
    private int userId;
    private int giveWood;
    private int giveBrick;
    private int giveWool;
    private int giveGrain;
    private int giveOre;
    private int wantsWood;
    private int wantsBrick;
    private int wantsWool;
    private int wantsGrain;
    private int wantsOre;
    private boolean accepted;
    private boolean acceptedNull;

    public TradeModel(int userId, int giveWood, int giveBrick, int giveWool, int giveGrain, int giveOre, int wantsWood, int wantsBrick, int wantsWool, int wantsGrain, int wantsOre, boolean accepted, boolean acceptedNull) {
        this.userId = userId;
        this.giveWood = giveWood;
        this.giveBrick = giveBrick;
        this.giveWool = giveWool;
        this.giveGrain = giveGrain;
        this.giveOre = giveOre;
        this.wantsWood = wantsWood;
        this.wantsBrick = wantsBrick;
        this.wantsWool = wantsWool;
        this.wantsGrain = wantsGrain;
        this.wantsOre = wantsOre;
        this.accepted = accepted;
        this.acceptedNull = acceptedNull;
    }

    //getters
    public int getUserId() {
        return userId;
    }

    public int getGiveWood() {
        return giveWood;
    }

    public int getGiveBrick() {
        return giveBrick;
    }

    public int getGiveWool() {
        return giveWool;
    }

    public int getGiveGrain() {
        return giveGrain;
    }

    public int getGiveOre() {
        return giveOre;
    }

    public int getWantsWood() {
        return wantsWood;
    }

    public int getWantsBrick() {
        return wantsBrick;
    }

    public int getWantsWool() {
        return wantsWool;
    }

    public int getWantsGrain() {
        return wantsGrain;
    }

    public int getWantsOre() {
        return wantsOre;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public static TradeModel getFromDatabase(int userId) {
        if (DBConnection.checkConnection()) {
            try {
                PreparedStatement preparedStatement = DBConnection.connection.prepareStatement("SELECT * FROM ruilaanbod WHERE idspeler = ?;");

                preparedStatement.setInt(1, userId);

                ResultSet resultSet = preparedStatement.executeQuery();
                return getTradeModelFromResultSet(resultSet);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return null;
    }

    public static TradeModel[] getAllFromDatabase() {
        if (DBConnection.checkConnection()) {
            try {
                Statement statement = DBConnection.connection.createStatement();

                ResultSet resultSet = statement.executeQuery("SELECT * FROM ruilaanbod;");
                return getAllTradeModelsFromResultSet(resultSet);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return new TradeModel[0];
    }
    
    public boolean isFlipped(TradeModel tradeModel) {
        return wantsWood == tradeModel.giveWood && wantsBrick == tradeModel.giveBrick && wantsWool == tradeModel.giveWool && wantsGrain == tradeModel.giveGrain && wantsOre == tradeModel.giveOre && giveWood == tradeModel.wantsWood && giveBrick == tradeModel.wantsBrick && giveWool == tradeModel.wantsWool && giveGrain == tradeModel.wantsGrain && giveOre == tradeModel.wantsOre;
    }
  
    private static TradeModel getTradeModelFromResultSet(ResultSet resultSet) {
        try {
            if (resultSet.next()) {
                int userId = resultSet.getInt("idspeler");
                int giveBrick = resultSet.getInt("geeft_baksteen");
                int giveWool = resultSet.getInt("geeft_wol");
                int giveOre = resultSet.getInt("geeft_erts");
                int giveGrain = resultSet.getInt("geeft_graan");
                int giveWood = resultSet.getInt("geeft_hout");
                int wantBrick = resultSet.getInt("vraagt_baksteen");
                int wantWool = resultSet.getInt("vraagt_wol");
                int wantOre = resultSet.getInt("vraagt_erts");
                int wantGrain = resultSet.getInt("vraagt_graan");
                int wantWood = resultSet.getInt("vraagt_hout");
                boolean accepted = resultSet.getBoolean("geaccepteerd");
                boolean acceptedNull = resultSet.wasNull();

                return new TradeModel(userId, giveBrick, giveWool, giveOre, giveGrain, giveWood, wantBrick, wantWool, wantOre, wantGrain, wantWood, accepted, acceptedNull);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static TradeModel[] getAllTradeModelsFromResultSet(ResultSet resultSet) {
        ArrayList<TradeModel> tradeModels = new ArrayList<>();

        try {
            while (resultSet.next()) {
                int userId = resultSet.getInt("idspeler");
                int giveBrick = resultSet.getInt("geeft_baksteen");
                int giveWool = resultSet.getInt("geeft_wol");
                int giveOre = resultSet.getInt("geeft_erts");
                int giveGrain = resultSet.getInt("geeft_graan");
                int giveWood = resultSet.getInt("geeft_hout");
                int wantBrick = resultSet.getInt("vraagt_baksteen");
                int wantWool = resultSet.getInt("vraagt_wol");
                int wantOre = resultSet.getInt("vraagt_erts");
                int wantGrain = resultSet.getInt("vraagt_graan");
                int wantWood = resultSet.getInt("vraagt_hout");
                boolean accepted = resultSet.getBoolean("geaccepteerd");
                boolean acceptedNull = resultSet.wasNull();

                tradeModels.add(new TradeModel(userId, giveBrick, giveWool, giveOre, giveGrain, giveWood, wantBrick, wantWool, wantOre, wantGrain, wantWood, accepted, acceptedNull));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tradeModels.toArray(new TradeModel[tradeModels.size()]);
    }

    public boolean isAcceptedNull() {
        return acceptedNull;
    }
    
    //setter
    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    private void setGiveWood(int giveWood) {
        this.giveWood = giveWood;
    }

    private void setGiveBrick(int giveBrick) {
        this.giveBrick = giveBrick;
    }

    private void setGiveWool(int giveWool) {
        this.giveWool = giveWool;
    }

    private void setGiveGrain(int giveGrain) {
        this.giveGrain = giveGrain;
    }

    private void setGiveOre(int giveOre) {
        this.giveOre = giveOre;
    }

    private void setWantsWood(int wantsWood) {
        this.wantsWood = wantsWood;
    }

    private void setWantsBrick(int wantsBrick) {
        this.wantsBrick = wantsBrick;
    }

    private void setWantsWool(int wantsWool) {
        this.wantsWool = wantsWool;
    }

    private void setWantsGrain(int wantsGrain) {
        this.wantsGrain = wantsGrain;
    }

    private void setWantsOre(int wantsOre) {
        this.wantsOre = wantsOre;
    }

    //update
    public void updateInDatabase() {
        if (DBConnection.checkConnection()) {
            try {
                PreparedStatement preparedStatement = DBConnection.connection.prepareStatement("UPDATE ruilaanbod SET geeft_hout = ?, geeft_baksteen = ?, geeft_wol = ?, geeft_graan = ?, geeft_erts = ?, vraagt_hout = ?, vraagt_baksteen = ?, vraagt_wol = ?, vraagt_graan = ?, vraagt_erts = ? WHERE idspeler = ?;");

                preparedStatement.setInt(1, giveWood);
                preparedStatement.setInt(2, giveBrick);
                preparedStatement.setInt(3, giveWool);
                preparedStatement.setInt(4, giveGrain);
                preparedStatement.setInt(5, giveOre);
                preparedStatement.setInt(6, wantsWood);
                preparedStatement.setInt(7, wantsBrick);
                preparedStatement.setInt(8, wantsWool);
                preparedStatement.setInt(9, wantsGrain);
                preparedStatement.setInt(10, wantsOre);
                preparedStatement.setInt(11, userId);

                preparedStatement.execute();

                if (preparedStatement.getUpdateCount() <= 0) {
                    insertInDatabase();
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void updateInDatabase(int userId, int giveWood, int giveBrick, int giveWool, int giveGrain, int giveOre, int wantsWood, int wantsBrick, int wantsWool, int wantsGrain, int wantsOre) {
        setUserId(userId);
        setGiveWood(giveWood);
        setGiveBrick(giveBrick);
        setGiveWool(giveWool);
        setGiveGrain(giveGrain);
        setGiveOre(giveOre);
        setWantsWood(wantsWood);
        setWantsBrick(wantsBrick);
        setWantsWool(wantsWool);
        setWantsGrain(wantsGrain);
        setWantsOre(wantsOre);

        updateInDatabase();
    }

    //insert
    public void insertInDatabase() {
        if (DBConnection.checkConnection()) {
            try {
                PreparedStatement preparedStatement = DBConnection.connection.prepareStatement("INSERT INTO ruilaanbod (idspeler, geeft_baksteen, geeft_wol, geeft_erts, geeft_graan, geeft_hout, vraagt_baksteen, vraagt_wol, vraagt_erts, vraagt_graan, vraagt_hout, geaccepteerd) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");

                preparedStatement.setInt(1, userId);
                preparedStatement.setInt(2, giveBrick);
                preparedStatement.setInt(3, giveWool);
                preparedStatement.setInt(4, giveOre);
                preparedStatement.setInt(5, giveGrain);
                preparedStatement.setInt(6, giveWood);
                preparedStatement.setInt(7, wantsBrick);
                preparedStatement.setInt(8, wantsWool);
                preparedStatement.setInt(9, wantsOre);
                preparedStatement.setInt(10, wantsGrain);
                preparedStatement.setInt(11, wantsWood);
                preparedStatement.setNull(12, Types.BOOLEAN);

                preparedStatement.execute();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    //other
    public void flip() {
        int tmpWood = wantsWood;
        int tmpBrick = wantsBrick;
        int tmpWool = wantsWool;
        int tmpGrain = wantsGrain;
        int tmpOre = wantsOre;
        wantsWood = giveWood;
        wantsBrick = giveBrick;
        wantsWool = giveWool;
        wantsGrain = giveGrain;
        wantsOre = giveOre;
        giveWood = tmpWood;
        giveBrick = tmpBrick;
        giveWool = tmpWool;
        giveGrain = tmpGrain;
        giveOre = tmpOre;
    }

    @Override
    public String toString() {
        return "TradeModel{" +
                "userId = " + userId +
                ", giveWood=" + giveWood +
                ", giveBrick=" + giveBrick +
                ", giveWool=" + giveWool +
                ", giveGrain=" + giveGrain +
                ", giveOre=" + giveOre +
                ", wantsWood=" + wantsWood +
                ", wantsBrick=" + wantsBrick +
                ", wantsWool=" + wantsWool +
                ", wantsGrain=" + wantsGrain +
                ", wantsOre=" + wantsOre +
                ", accepted=" + accepted +
                '}';
    }








}
