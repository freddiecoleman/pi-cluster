package picluster;

import org.jfree.chart.ChartPanel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.event.EventListenerList;

public class ChartsPanel extends JPanel {

    private static final long serialVersionUID = 6915622549267792262L;

    private EventListenerList listenerList = new EventListenerList();

    public ChartsPanel() {

        ElasticSearch search = new ElasticSearch("pi-cluster");

        Dimension size = getPreferredSize();
        size.width = 250;
        size.height = 100;
        setPreferredSize(size);

        setBorder(BorderFactory.createTitledBorder("Charts"));

        JLabel defaultLabel = new JLabel("Search for something to see some charts!");
        defaultLabel.setForeground(Color.lightGray);

        setLayout(new GridBagLayout());

        GridBagConstraints gc = new GridBagConstraints();

        JFreeChartBarChartExample chart = new JFreeChartBarChartExample("Browser Usage Statistics", "Which Browser are you using?");
        chart.pack();
        chart.setVisible(true);


        // // First column /////////////////////////

        gc.anchor = GridBagConstraints.CENTER;
        gc.weightx = 0.5;
        gc.weighty = 0.5;

        gc.gridx = 0;
        gc.gridy = 0;
        add(defaultLabel, gc);


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