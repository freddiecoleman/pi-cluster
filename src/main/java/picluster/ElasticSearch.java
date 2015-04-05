package picluster;

import org.elasticsearch.action.admin.indices.alias.get.GetAliasesRequest;
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesResponse;
import org.elasticsearch.action.admin.indices.get.GetIndexResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.hppc.ObjectLookupContainer;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.SimpleQueryParser;

import java.util.ArrayList;
import java.util.Date;
import java.util.EventObject;
import java.util.List;

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

    public List<String> allIndices(){

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

        return allIndices;

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
}
