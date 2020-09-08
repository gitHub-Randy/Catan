package view;

import controller.Dice;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public class DiceView extends JPanel {

    private Dice dice;

    // add dice model to parameter
    public DiceView(Dice dice) {
        setPreferredSize(new Dimension(1200, 150));
        this.dice = dice;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.GRAY);
        //Dice controller;
        int ARC = 20;
        g.fillRoundRect(this.getWidth() / 4, this.getHeight() / 4, 100, 100, ARC, ARC);
        g.fillRoundRect(this.getWidth() / 2 + 200, this.getHeight() / 4, 100, 100, ARC, ARC);

        g.setColor(Color.YELLOW);

        int d1 = dice.getThrow(0);
        int d2 = dice.getThrow(1);

        int DOT = 20;
        switch (d1) {
            case 1:
                g.fillOval(this.getWidth() / 4 + 40, (this.getHeight() / 4 + 35), DOT, DOT);
                break;
            case 2:
                g.fillOval(this.getWidth() / 4 + 10, (this.getHeight() / 4 + 10), DOT, DOT);
                g.fillOval(this.getWidth() / 4 + 70, (this.getHeight() / 4 + 65), DOT, DOT);
                break;
            case 3:
                g.fillOval(this.getWidth() / 4 + 10, (this.getHeight() / 4 + 10), DOT, DOT);
                g.fillOval(this.getWidth() / 4 + 40, (this.getHeight() / 4 + 35), DOT, DOT);
                g.fillOval(this.getWidth() / 4 + 70, (this.getHeight() / 4 + 65), DOT, DOT);
                break;
            case 4:
                g.fillOval(this.getWidth() / 4 + 10, (this.getHeight() / 4 + 10), DOT, DOT);
                g.fillOval(this.getWidth() / 4 + 70, (this.getHeight() / 4 + 65), DOT, DOT);
                g.fillOval(this.getWidth() / 4 + 10, (this.getHeight() / 4 + 65), DOT, DOT);
                g.fillOval(this.getWidth() / 4 + 70, (this.getHeight() / 4 + 10), DOT, DOT);
                break;
            case 5:
                g.fillOval(this.getWidth() / 4 + 10, (this.getHeight() / 4 + 10), DOT, DOT);
                g.fillOval(this.getWidth() / 4 + 70, (this.getHeight() / 4 + 65), DOT, DOT);
                g.fillOval(this.getWidth() / 4 + 10, (this.getHeight() / 4 + 65), DOT, DOT);
                g.fillOval(this.getWidth() / 4 + 70, (this.getHeight() / 4 + 10), DOT, DOT);
                g.fillOval(this.getWidth() / 4 + 40, (this.getHeight() / 4 + 35), DOT, DOT);
                break;
            case 6:
                g.fillOval(this.getWidth() / 4 + 10, (this.getHeight() / 4 + 10), DOT, DOT);
                g.fillOval(this.getWidth() / 4 + 70, (this.getHeight() / 4 + 65), DOT, DOT);
                g.fillOval(this.getWidth() / 4 + 10, (this.getHeight() / 4 + 65), DOT, DOT);
                g.fillOval(this.getWidth() / 4 + 70, (this.getHeight() / 4 + 10), DOT, DOT);
                g.fillOval(this.getWidth() / 4 + 40, (this.getHeight() / 4 + 10), DOT, DOT);
                g.fillOval(this.getWidth() / 4 + 40, (this.getHeight() / 4 + 65), DOT, DOT);
                break;
        }

        switch (d2) {
            case 1:
                g.fillOval(this.getWidth() / 2 + 240, this.getHeight() / 4 + 35, DOT, DOT);
                break;
            case 2:
                g.fillOval(this.getWidth() / 2 + 210, this.getHeight() / 4 + 10, DOT, DOT);
                g.fillOval(this.getWidth() / 2 + 270, this.getHeight() / 4 + 65, DOT, DOT);
                break;
            case 3:
                g.fillOval(this.getWidth() / 2 + 210, this.getHeight() / 4 + 10, DOT, DOT);
                g.fillOval(this.getWidth() / 2 + 240, this.getHeight() / 4 + 35, DOT, DOT);
                g.fillOval(this.getWidth() / 2 + 270, this.getHeight() / 4 + 65, DOT, DOT);
                break;
            case 4:
                g.fillOval(this.getWidth() / 2 + 210, this.getHeight() / 4 + 10, DOT, DOT);
                g.fillOval(this.getWidth() / 2 + 270, this.getHeight() / 4 + 10, DOT, DOT);
                g.fillOval(this.getWidth() / 2 + 210, this.getHeight() / 4 + 65, DOT, DOT);
                g.fillOval(this.getWidth() / 2 + 270, this.getHeight() / 4 + 65, DOT, DOT);
                break;
            case 5:
                g.fillOval(this.getWidth() / 2 + 210, this.getHeight() / 4 + 10, DOT, DOT);
                g.fillOval(this.getWidth() / 2 + 270, this.getHeight() / 4 + 10, DOT, DOT);
                g.fillOval(this.getWidth() / 2 + 210, this.getHeight() / 4 + 65, DOT, DOT);
                g.fillOval(this.getWidth() / 2 + 270, this.getHeight() / 4 + 65, DOT, DOT);
                g.fillOval(this.getWidth() / 2 + 240, this.getHeight() / 4 + 35, DOT, DOT);
                break;
            case 6:
                g.fillOval(this.getWidth() / 2 + 210, this.getHeight() / 4 + 10, DOT, DOT);
                g.fillOval(this.getWidth() / 2 + 270, this.getHeight() / 4 + 10, DOT, DOT);
                g.fillOval(this.getWidth() / 2 + 210, this.getHeight() / 4 + 65, DOT, DOT);
                g.fillOval(this.getWidth() / 2 + 270, this.getHeight() / 4 + 65, DOT, DOT);
                g.fillOval(this.getWidth() / 2 + 240, this.getHeight() / 4 + 10, DOT, DOT);
                g.fillOval(this.getWidth() / 2 + 240, this.getHeight() / 4 + 65, DOT, DOT);
                break;
        }

        repaint();
    }

    public void setDiceValue1(int diceValue1) {
        dice.setValue(0, diceValue1);
        repaint();
    }

    public void setDiceValue2(int diceValue2) {
        dice.setValue(1, diceValue2);
        repaint();
    }

}
