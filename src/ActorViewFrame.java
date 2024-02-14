import javax.swing.*;
import java.awt.*;

public class ActorViewFrame extends JFrame {

    public ActorViewFrame() {
        setTitle("Production List");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        add(createProductionList(), BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JScrollPane createProductionList() {
        DefaultListModel<String> actorListModel = new DefaultListModel<>();
        JList<String> actorList = new JList<>(actorListModel);

        for (Actor actor : IMDB.getInstance().actors) {
            actorListModel.addElement(actor.name.toString());
        }

        JScrollPane scrollPane = new JScrollPane(actorList);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Actors"));

        return scrollPane;
    }

}
