package picluster;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.event.EventListenerList;

public class DetailsPanel extends JPanel {

    private static final long serialVersionUID = 6915622549267792262L;

    private EventListenerList listenerList = new EventListenerList();

    public DetailsPanel() {

        ElasticSearch search = new ElasticSearch("pi-cluster");

        Dimension size = getPreferredSize();
        size.width = 250;
        setPreferredSize(size);

        setBorder(BorderFactory.createTitledBorder("Filter"));

        JLabel indexLabel = new JLabel("Search: ");
        final JTextField searchText = new JTextField(10);

        JButton searchBtn = new JButton("Search");

        searchBtn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                fireSearchEvent(new SearchEvent(this, searchText.getText()));
            }

        });

        setLayout(new GridBagLayout());

        GridBagConstraints gc = new GridBagConstraints();

        // // First column /////////////////////////

        gc.anchor = GridBagConstraints.LINE_END;
        gc.weightx = 0.5;
        gc.weighty = 0.5;

        gc.gridx = 0;
        gc.gridy = 0;
        add(indexLabel, gc);

        // // Second column
        gc.anchor = GridBagConstraints.LINE_START;

        gc.gridx = 1;
        gc.gridy = 0;
        add(searchText, gc);

        // Final row
        gc.weighty = 10;

        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        gc.gridx = 1;
        gc.gridy = 3;
        add(searchBtn, gc);
    }

    public void fireSearchEvent(SearchEvent event) {
        Object[] listeners = listenerList.getListenerList();

        for(int i=0; i < listeners.length; i += 2) {
            if(listeners[i] == SearchListener.class) {
                ((SearchListener)listeners[i+1]).detailEventOccurred(event);
            }
        }
    }

    public void addSearchListener(SearchListener listener) {
        listenerList.add(SearchListener.class, listener);
    }

    public void removeSearchListener(SearchListener listener) {
        listenerList.remove(SearchListener.class, listener);
    }
}