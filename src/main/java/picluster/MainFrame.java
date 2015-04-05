package picluster;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;


public class MainFrame extends JFrame {

    private DetailsPanel detailsPanel;

    public MainFrame(String title) {
        super(title);

        // Set layout manager
        setLayout(new BorderLayout());

        // Create Swing component
        final DefaultTableModel model = new DefaultTableModel();
        JTable resultsTable = new JTable(model);
        model.addColumn("Play");
        model.addColumn("Speech #");
        model.addColumn("Speaker");
        model.addColumn("Line");
        model.addColumn("Text");



        JScrollPane scrollPane = new JScrollPane(resultsTable);

        detailsPanel = new DetailsPanel();

        detailsPanel.addDetailListener(new DetailListener() {
            public void detailEventOccurred(DetailEvent event) {
                ArrayList<HashMap<String, String>> results = event.getText();

                for(HashMap<String, String> row : results){

                    model.addRow(new Object[]{row.get("play_name"), row.get("speech_number"), row.get("speaker"), row.get("line_number"), row.get("text_entry")});

                }


            }
        });

        // Add Swing components to content pane
        Container c = getContentPane();

        c.add(scrollPane, BorderLayout.CENTER);
        c.add(detailsPanel, BorderLayout.WEST);
    }
}