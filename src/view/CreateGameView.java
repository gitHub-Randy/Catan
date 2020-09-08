package view;

import javax.swing.*;
import java.awt.*;

public class CreateGameView extends JPanel {

    private ImageIcon imgIcon;
    private JButton createGame;
    private JButton acceptInvite;
    private JButton logout;

    public CreateGameView() {
        setPreferredSize(new Dimension(812, 900));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        createGame = new JButton("Cre\u00EBer het spel(Host)  ");
        createGame.setOpaque(false);
        createGame.setFocusPainted(false);
        createGame.setBorder(new RoundBorder(20));
        createGame.setBackground(new Color(160, 82, 45));
        createGame.setForeground(new Color(160, 82, 45));

        acceptInvite = new JButton("Uitnodiging Accepteren");
        acceptInvite.setOpaque(false);
        acceptInvite.setFocusPainted(false);
        acceptInvite.setBorder(new RoundBorder(20));
        acceptInvite.setBackground(new Color(160, 82, 45));
        acceptInvite.setForeground(new Color(160, 82, 45));

        logout = new JButton("              Uitloggen              ");
        logout.setOpaque(false);
        logout.setFocusPainted(false);
        logout.setBorder(new RoundBorder(20));
        logout.setBackground(new Color(160, 82, 45));
        logout.setForeground(new Color(160, 82, 45));

        imgIcon = new ImageIcon(getClass().getClassLoader().getResource("resources/CatanBackGround.jpg"));
        Image image = imgIcon.getImage();
        Image newimg = image.getScaledInstance(1200, 900, java.awt.Image.SCALE_SMOOTH);
        imgIcon = new ImageIcon(newimg);

        add(Box.createRigidArea(new Dimension(240, 300)));
        add(createGame);
        add(Box.createRigidArea(new Dimension(0, 70)));
        add(acceptInvite);
        add(Box.createRigidArea(new Dimension(0, 70)));
        add(logout);
    }

    public void paintComponent(Graphics g) {
        imgIcon.paintIcon(this, g, 0, 0);
    }

    public JButton getCreateGame() {
        return createGame;
    }

    public JButton getAcceptInvite() {
        return acceptInvite;
    }

    public JButton getLogout() {
        return logout;
    }

}
