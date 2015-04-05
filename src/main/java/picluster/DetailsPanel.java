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

        JLabel indexLabel = new JLabel("Index: ");

        final JComboBox indexComboBox = new JComboBox(search.allIndices());

        JButton addBtn = new JButton("Add");

        addBtn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String text = "test";

                fireDetailEvent(new DetailEvent(this, text));
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
        add(indexComboBox, gc);

        // Final row
        gc.weighty = 10;

        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        gc.gridx = 1;
        gc.gridy = 3;
        add(addBtn, gc);
    }

    public void fireDetailEvent(DetailEvent event) {
        Object[] listeners = listenerList.getListenerList();

        for(int i=0; i < listeners.length; i += 2) {
            if(listeners[i] == DetailListener.class) {
                ((DetailListener)listeners[i+1]).detailEventOccurred(event);
            }
        }
    }

    public void addDetailListener(DetailListener listener) {
        listenerList.add(DetailListener.class, listener);
    }

    public void removeDetailListener(DetailListener listener) {
        listenerList.remove(DetailListener.class, listener);
    }
}