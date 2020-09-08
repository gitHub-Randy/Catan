package view;

import model.User;

import javax.swing.*;

import controller.Catan;

import java.awt.*;

public class ThreeUserView extends JPanel {

    private UserView userView1;
    private UserView userView2;
    private UserView userView3;
    
    private Catan catanController;

    // kleur wat om het panel heen zit moet laten zien welke speler aan de beurt is.
    // zwart is neit aan de beurt
    // rood is aan de beurt
    public ThreeUserView(Color panelColor1, Color panelColor2, Color panelColor3, Catan catanController) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        userView1 = new UserView();
        userView1.setBorder(BorderFactory.createLineBorder(panelColor1, 2));

        userView2 = new UserView();
        userView2.setBorder(BorderFactory.createLineBorder(panelColor2, 2));

        userView3 = new UserView();
        userView3.setBorder(BorderFactory.createLineBorder(panelColor3, 2));

        add(userView1);
        add(userView2);
        add(userView3);
        
        this.catanController = catanController;
    }

    public void setUsers(User user1, User user2, User user3) {
        userView1.setUser(user1);
        userView2.setUser(user2);
        userView3.setUser(user3);
    }
    
    public void updateLabels() {
    	catanController.getModel().getUserWithIndexWithoutOwn(0);
    	userView1.setOnlyUser(catanController.getModel().getUserWithIndexWithoutOwn(0));
        userView2.setOnlyUser(catanController.getModel().getUserWithIndexWithoutOwn(1));
        userView3.setOnlyUser(catanController.getModel().getUserWithIndexWithoutOwn(2));
    	userView1.updateLabels();
    	userView2.updateLabels();
    	userView3.updateLabels();
    }

}
