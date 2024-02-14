import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ContributorFrame extends JFrame {
    private Contributor<String> user;

    public ContributorFrame(Contributor<String> user) {
        this.user = user;

        setTitle("Contributor User Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(10, 1));

        add(createButton("View Productions", e -> productionDisplayInfo()));
        add(createButton("View Actors", e -> actorsDisplayInfo()));
        add(createButton("View Notifications", e -> viewNotifications()));
        add(createButton("Search a Production/Actor", e -> searchProdActor()));
        add(createButton("Go to Favorites", e -> favorites()));
        add(createButton("Add a Request", e -> addRequest()));
        add(createButton("Add/Delete a Production/Actor from System", e -> addOrDeleteProductionActor()));
        add(createButton("View Requests", e -> viewRequests()));
        add(createButton("Update Information about a Production/Actor", e -> updateInformation()));
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
        StringBuilder notificationText = new StringBuilder();
        if(user.notifications != null)
            user.notifications.forEach(notification -> notificationText.append(notification).append("\n"));
        JOptionPane.showMessageDialog(this, notificationText.toString(), "Notifications", JOptionPane.INFORMATION_MESSAGE);
    }

    private void searchProdActor() {
        JOptionPane.showMessageDialog(this, "Search for Production/Actor");
    }

    private void favorites() {
        StringBuilder favoritesText = new StringBuilder();
        if(user.favorites != null)
            user.favorites.forEach(favorite -> favoritesText.append(favorite).append("\n"));
        JOptionPane.showMessageDialog(this, favoritesText.toString(), "Favorites", JOptionPane.INFORMATION_MESSAGE);

    }

    private void addRequest() {
        JOptionPane.showMessageDialog(this, "Add a Request");
    }

    private void addOrDeleteProductionActor() {
        JOptionPane.showMessageDialog(this, "Add/Delete a Production/Actor from System");
    }

    private void viewRequests() {
        StringBuilder requestsText = new StringBuilder();
        if(user.requests != null) {
            for (Request request : user.requests)
                requestsText.append(request).append("\n-----------\n");
            }
        JOptionPane.showMessageDialog(this, requestsText.toString(), "Requests", JOptionPane.INFORMATION_MESSAGE);

    }

    private void updateInformation() {
        JOptionPane.showMessageDialog(this, "Update Information about a Production/Actor");
    }

    private void logout() {
        dispose();
    }

}
