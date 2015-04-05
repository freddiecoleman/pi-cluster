package picluster;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashMap;

public class SearchEvent extends EventObject {

    ElasticSearch elasticSearch;
    String query;

    public SearchEvent(Object source, String query) {
        super(source);
        this.query = query;
        this.elasticSearch = new ElasticSearch("pi-cluster");
    }

    public ArrayList<HashMap<String, String>> search() {
        return elasticSearch.search(query);

    }
}