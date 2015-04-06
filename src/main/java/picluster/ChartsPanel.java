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






        String chartTitle = "test";

        // based on the dataset we create the chart
        JFreeChart pieChart = ChartFactory.createBarChart(chartTitle, "Category", "Score", createDataset(), PlotOrientation.VERTICAL, true, true, false);

        // Adding chart into a chart panel
        ChartPanel chartPanel = new ChartPanel(pieChart);

        // settind default size
        chartPanel.setSize(this.getSize());




        // // First column /////////////////////////


        add(chartPanel, BorderLayout.CENTER);


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

    private CategoryDataset createDataset() {

        // row keys...
        final String firefox = "Firefox";
        final String chrome = "Chrome";
        final String iexplorer = "InternetExplorer";

        // column keys...
        final String speed = "Speed";
        final String popular = "Popular";
        final String response = "Response";
        final String osindependent = "OS Independent";
        final String features = "Features";

        // create the dataset...
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        dataset.addValue(1.0, firefox, speed);
        dataset.addValue(4.0, firefox, popular);
        dataset.addValue(3.0, firefox, response);
        dataset.addValue(5.0, firefox, osindependent);
        dataset.addValue(5.0, firefox, features);

        dataset.addValue(5.0, chrome, speed);
        dataset.addValue(7.0, chrome, popular);
        dataset.addValue(6.0, chrome, response);
        dataset.addValue(8.0, chrome, osindependent);
        dataset.addValue(4.0, chrome, features);

        dataset.addValue(4.0, iexplorer, speed);
        dataset.addValue(3.0, iexplorer, popular);
        dataset.addValue(2.0, iexplorer, response);
        dataset.addValue(3.0, iexplorer, osindependent);
        dataset.addValue(6.0, iexplorer, features);

        return dataset;

    }
}