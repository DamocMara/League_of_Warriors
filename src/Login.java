import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JLabel messageLabel;
    private Game game;

    public Login(Game game) {
        this.game = game;
        setTitle("Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        // Email label si field
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(10, 50, 100, 30);
        add(emailLabel);

        emailField = new JTextField();
        emailField.setBounds(100, 50, 280, 30);
        add(emailField);

        // Password label si field
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(10, 100, 100, 30);
        add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(100, 100, 280, 30);
        add(passwordField);

        messageLabel = new JLabel("", SwingConstants.CENTER);
        messageLabel.setForeground(Color.RED);
        messageLabel.setBounds(50, 150, 300, 30);
        add(messageLabel);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(135, 200, 100, 30);
        add(loginButton);


        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });

        setLocationRelativeTo(null); // Center the window
        setVisible(true);
    }

    private void handleLogin() {
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());

        Account authenticatedAccount = null;
        for(Account account : game.accounts) {
            Account.Information info = account.getInformation();
            if(info.getCredentials().getEmail().equals(email) &&
            info.getCredentials().getPassword().equals(password)) {
                authenticatedAccount = account;
                break;
            }
        }
        if(authenticatedAccount == null) {
            messageLabel.setText("Incorrect credentials.Try again.");
        } else {
            messageLabel.setForeground(Color.GREEN);
            messageLabel.setText(("Login succesfull!"));
            new CharacterSelection(game, authenticatedAccount); // Navigăm la selecția personajului
            dispose();
        }
    }

    public static void main(String[] args) {
        Game game = Game.getInstance(); // Singleton instance
        new Login(game);
    }

}