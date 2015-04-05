package picluster;

import org.elasticsearch.action.admin.indices.alias.get.GetAliasesRequest;
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesResponse;
import org.elasticsearch.action.admin.indices.get.GetIndexResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.hppc.ObjectLookupContainer;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.SimpleQueryParser;
import org.elasticsearch.search.SearchHit;

import java.util.*;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

/**
 * Created by freddiecoleman on 05/04/15.
 */
public class ElasticSearch {

    private Settings settings;
    private Client client;

    public ElasticSearch(String clusterName) { // cluster name = "pi-cluster"

        this.settings = ImmutableSettings.settingsBuilder().put("cluster.name", clusterName).build();
        this.client = new TransportClient(settings).addTransportAddress(new InetSocketTransportAddress("192.168.0.6", 9300));

    }

    public String[] allIndices(){

        List<String> allIndices = new ArrayList<String>();

        try {

            ObjectLookupContainer<String> response =
                    client.admin()
                            .cluster().prepareState()
                            .execute().actionGet().getState()
                            .getMetaData().indices().keys();

            Object[] indices = response.toArray();

            for(Object index : indices){

                String indexString = index.toString();

                if(!indexString.startsWith(".")){
                    allIndices.add(indexString);
                }

            }

        } catch (Exception e) {

            System.out.println("error: " + e.getMessage());

        }

        return allIndices.toArray(new String[allIndices.size()]);

    }

    public boolean addRecord(String indexName, String indexType, String indexId, String user, String message) {

        // once this is working refactor away user and message to be more relevant to what i want to achieve

        try {

            IndexResponse response = client.prepareIndex(indexName, indexType, indexId)
                    .setSource(jsonBuilder()
                                    .startObject()
                                    .field("user", user)
                                    .field("postDate", new Date())
                                    .field("message", message)
                                    .endObject()
                    )
                    .execute()
                    .actionGet();

            return true;

        } catch (Exception e) {

            System.out.println("error: " + e.getMessage());

        }

        return false;

    }

    public ArrayList<HashMap<String, String>> search(String query){

        ArrayList<HashMap<String, String>> results = new ArrayList<HashMap<String, String>>();

        SearchResponse scrollResp = client.prepareSearch()
                .setSearchType(SearchType.SCAN)
                .setScroll(new TimeValue(60000))
                .setQuery(QueryBuilders.multiMatchQuery(query, "_all"))
                .setSize(100).execute().actionGet(); //100 hits per shard will be returned for each scroll

        //Scroll until no hits are returned
        while (true) {

            for (SearchHit hit : scrollResp.getHits().getHits()) {

                HashMap<String, String> result = new HashMap<String, String>();

                result.put("play_name", hit.getSource().get("play_name").toString());
                result.put("speech_number", hit.getSource().get("speech_number").toString());
                result.put("speaker", hit.getSource().get("speaker").toString());
                result.put("line_number", hit.getSource().get("line_number").toString());
                result.put("text_entry", hit.getSource().get("text_entry").toString());

                results.add(result);
            }

            scrollResp = client.prepareSearchScroll(scrollResp.getScrollId()).setScroll(new TimeValue(600000)).execute().actionGet();
            //Break condition: No hits are returned
            if (scrollResp.getHits().getHits().length == 0) {
                break;
            }
        }

        return results;

    }


}
