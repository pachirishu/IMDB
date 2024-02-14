import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ProductionFrame extends JFrame {

    private JTextArea productionInfoArea;

    public ProductionFrame() {
        setTitle("Production List");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        add(createProductionList(), BorderLayout.WEST);
        productionInfoArea = new JTextArea();
        productionInfoArea.setEditable(false);
        JScrollPane infoScrollPane = new JScrollPane(productionInfoArea);
        infoScrollPane.setBorder(BorderFactory.createTitledBorder("Production Details"));
        add(infoScrollPane, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JScrollPane createProductionList() {
        DefaultListModel<String> productionListModel = new DefaultListModel<>();
        JList<String> productionList = new JList<>(productionListModel);

        for (Production production : IMDB.getInstance().productions) {
            productionListModel.addElement(production.name.toString());
        }

        JScrollPane scrollPane = new JScrollPane(productionList);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Productions"));

        productionList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedIndex = productionList.getSelectedIndex();
                if (selectedIndex != -1) {
                    Production selectedProduction = IMDB.getInstance().productions.get(selectedIndex);
                    displayProductionInfo(selectedProduction);
                }
            }
        });

        return scrollPane;
    }

    private void displayProductionInfo(Production production) {
        StringBuilder info = new StringBuilder();
        info.append("Name: ").append(production.name).append("\n");
        info.append("Type: ").append(production.type).append("\n");
        info.append("Genres: ").append(production.genres).append("\n");

        productionInfoArea.setText(info.toString());
    }

}
