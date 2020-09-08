package view;

import model.LoginModel;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public class LoginView extends JPanel {
    //instance vars
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private JLabel messageLabel;
    private LoginModel model;

    private String username;

    public LoginView() {
        //model
        model = new LoginModel();
        //panel
        this.setPreferredSize(new Dimension(812, 900));
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        //font
        Font font = new Font("Arial", Font.PLAIN, 20);
        // user field
        usernameField = new JTextField();
        usernameField.setFont(font);
        usernameField.setHorizontalAlignment(JTextField.CENTER);
        usernameField.setText("1test");
        usernameField.setBackground(Color.lightGray);
        usernameField.setMaximumSize(new Dimension(200, 50));
        usernameField.addActionListener(e -> loginButton.doClick());

        //pass field
        passwordField = new JPasswordField();
        passwordField.setHorizontalAlignment(JPasswordField.CENTER);
        passwordField.setText("hallo");
        passwordField.setBackground(Color.lightGray);
        passwordField.setMaximumSize(new Dimension(200, 50));
        passwordField.addActionListener(e -> loginButton.doClick());

        // login button
        loginButton = new JButton("Login");
        loginButton.setMaximumSize(new Dimension(200, 50));
        loginButton.setAlignmentX(CENTER_ALIGNMENT);
        setLoginButtonAction();

        //regis button
        registerButton = new JButton("Registreer");
        registerButton.setMaximumSize(new Dimension(200, 50));
        registerButton.setAlignmentX(CENTER_ALIGNMENT);
        setRegisterButtonAction();

        //label
        messageLabel = new JLabel();
        //locating
        //white space
        add(Box.createRigidArea(new Dimension(812, 400)));
        add(usernameField);
        add(passwordField);
        add(loginButton);
        add(registerButton);
        add(messageLabel);
    }

    private void setLoginButtonAction() {
        loginButton.addActionListener(arg0 -> {
            if (!model.checkUser(usernameField.getText())) {
                messageLabel.setText("Deze gebruikersnaam bestaat niet.");
            } else if (!checkLogin()) {
                messageLabel.setText("Het wachtwoord is niet correct.");
            }
        });
    }

    @SuppressWarnings("deprecation")
    private void setRegisterButtonAction() {
        registerButton.addActionListener(arg0 -> {
            String user = usernameField.getText();
            String pass = passwordField.getText();
            if (!model.checkLegalName(user)) {
                messageLabel.setText("Dit is geen geldige gebruikersnaam.");
            } else if (!model.checkLegalName(pass)) {
                messageLabel.setText("Dit is geen geldig wachtwoord.");
            } else {
                model.createNewUser(user, passwordField.getText());
                messageLabel.setText("U bent geregistreerd.");
            }
        });
    }

    @SuppressWarnings("deprecation")
    public boolean checkLogin() {
        if (model.checkPassword(usernameField.getText(), passwordField.getText())) {
            username = usernameField.getText();
            return true;
        }

        return false;
    }

    public JButton getButton() {
        return loginButton;
    }

    public String getUsername() {
        return username;
    }

    public String getUserName() {
        return model.getUsername();
    }

}








