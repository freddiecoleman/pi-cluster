package picluster;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashMap;

public class SearchEvent extends EventObject {

    ElasticSearch elasticSearch;
    String query;

    public SearchEvent(Object source, String query, ElasticSearch elasticSearch) {
        super(source);
        this.query = query;
        this.elasticSearch = elasticSearch;
    }

    public ArrayList<HashMap<String, String>> search() {
        return elasticSearch.search(query);

    }

    public long getResultCount()
    {
        return elasticSearch.getResultCount();
    }
}