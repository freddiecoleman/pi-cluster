package picluster;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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

        setLayout(new BorderLayout());

    }

    public void updateChart(ArrayList<HashMap<String, String>> searchResults){
        add(createPlayBarChart("Occurences by play", searchResults), BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private ChartPanel createPlayBarChart(String chartTitle, ArrayList<HashMap<String, String>> searchResults){

        // based on the dataset we create the chart
        JFreeChart pieChart = ChartFactory.createBarChart(chartTitle, "Play", "Count", createDataset(searchResults), PlotOrientation.VERTICAL, true, true, false);

        // Adding chart into a chart panel
        ChartPanel chartPanel = new ChartPanel(pieChart);

        // settind default size
        chartPanel.setSize(this.getSize());

        return chartPanel;
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

    private CategoryDataset createDataset(ArrayList<HashMap<String, String>> searchResults) {

        HashMap<String, Integer> data = new HashMap<String, Integer>();

        for (HashMap<String, String> row : searchResults) {

            if(data.get(row.get("play_name")) == null){ // first time word encountered in this play

                data.put(row.get("play_name"), 1);
                continue;

            }

            data.put(row.get("play_name"), data.get(row.get("play_name")) + 1); // increment word count

        }

        // create the dataset...
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for(Map.Entry<String, Integer> entry : data.entrySet()){
            dataset.addValue(entry.getValue(), "Count", entry.getKey());
        }

        return dataset;

    }
}