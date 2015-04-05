package picluster;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashMap;

public class DetailEvent extends EventObject {

    private String text;

    public DetailEvent(Object source, String text) {
        super(source);

        this.text = text;
    }

    public ArrayList<HashMap<String, String>> getText() {

        ElasticSearch elasticSearch = new ElasticSearch("pi-cluster");

        return elasticSearch.search("kill");

    }
}