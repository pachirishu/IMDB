import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegularFrame extends JFrame {
    private Regular<String> user;

    public RegularFrame(Regular<String> user) {
        this.user = user;

        setTitle("Regular User Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(9, 1));

        add(createButton("View Productions Details", e -> productionDisplayInfo()));
        add(createButton("View Actors Details", e -> actorsDisplayInfo()));
        add(createButton("View Notifications", e -> viewNotifications()));
        add(createButton("Search for Actor/Movie/Series", e -> searchProdActor()));
        add(createButton("Go to Favorites", e -> favorites()));
        add(createButton("Add/Delete a Request", e -> addOrDeleteRequest()));
        add(createButton("Write/Delete a Review", e -> writeOrDeleteReview()));
        add(createButton("View Experience", e -> viewExperience()));
        add(createButton("Logout", e -> logout()));

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JButton createButton(String text, ActionListener actionListener) {
        JButton button = new JButton(text);
        button.addActionListener(actionListener);
        return button;
    }

    private void productionDisplayInfo() {
        new ProductionFrame();
    }

    private void actorsDisplayInfo() {
        new ActorViewFrame();
    }

    private void viewNotifications() {
        // Implement view notifications logic
        StringBuilder notificationText = new StringBuilder();
        if(user.notifications != null)
            user.notifications.forEach(notification -> notificationText.append(notification).append("\n"));
        JOptionPane.showMessageDialog(this, notificationText.toString(), "Notifications", JOptionPane.INFORMATION_MESSAGE);
    }

    private void searchProdActor() {
        JOptionPane.showMessageDialog(this, "Search for Actor/Movie/Series");
    }

    private void favorites() {
        StringBuilder favoritesText = new StringBuilder();
        if(user.favorites != null)
            user.favorites.forEach(favorite -> favoritesText.append(favorite).append("\n"));
        JOptionPane.showMessageDialog(this, favoritesText.toString(), "Favorites", JOptionPane.INFORMATION_MESSAGE);

    }

    private void addOrDeleteRequest() {
        JOptionPane.showMessageDialog(this, "Add/Delete a Request");
    }

    private void writeOrDeleteReview() {
        JOptionPane.showMessageDialog(this, "Write/Delete a Review");
    }

    private void viewExperience() {
        JOptionPane.showMessageDialog(this, "Experience: " + user.experience);
    }

    private void logout() {
        dispose();
    }

}
