import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame implements ActionListener {
    private JButton loginButton;
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginFrame(String title) {
        super(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout(FlowLayout.CENTER));

        usernameField = new JTextField(15);
        add(new JLabel("Username:"));
        add(usernameField);

        passwordField = new JPasswordField(15);
        add(new JLabel("Password:"));
        add(passwordField);

        loginButton = new JButton("Login");
        loginButton.addActionListener(this);
        add(loginButton);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            User<String> userAuth = null;
            for (User<String> user : IMDB.getInstance().users) {
                if (user.info.getEmail().equals(username) && user.info.getPassword().equals(password)) {
                    System.out.println("Login successful!");
                    userAuth = user;
                    break;
                }
            }
            System.out.println("Username: " + username);
            System.out.println("Password: " + password);
            // close the window if the action is correct
            dispose();
            // open the window for the user type
            if (userAuth.accountType == AccountType.Admin) {
                AdminFrame adminFrame = new AdminFrame((Admin<String>) userAuth);
            } else if (userAuth.accountType == AccountType.Regular) {
                RegularFrame regularFrame = new RegularFrame((Regular<String>) userAuth);
            } else if (userAuth.accountType == AccountType.Contributor) {
                ContributorFrame contributorFrame = new ContributorFrame((Contributor<String>) userAuth);
            }
        }
    }

}
