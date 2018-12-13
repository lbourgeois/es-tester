package org.talend.elasticsearch.es_tester;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.json.JSONObject;

/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] args) {
        try {
            Client client = new PreBuiltTransportClient(
                    Settings.builder().put("client.transport.sniff", true).put("cluster.name", "elasticsearch").build())
                            .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));

            org.elasticsearch.action.search.SearchRequestBuilder searchRequest = client.prepareSearch("lbourgeois") //
                    .setTypes("countries") //
                    .addSort(org.elasticsearch.search.sort.FieldSortBuilder.DOC_FIELD_NAME,
                            org.elasticsearch.search.sort.SortOrder.ASC)
                    //
                    //.setScroll(new org.elasticsearch.common.unit.TimeValue(10000)) //

                    .setSearchType(org.elasticsearch.action.search.SearchType.DEFAULT) //
                    .setQuery(org.elasticsearch.index.query.QueryBuilders.queryStringQuery("json_document:*FR*"));

            
            // https://www.elastic.co/guide/en/elasticsearch/reference/5.6/query-dsl-query-string-query.html#query-string-syntax
            
            System.out.println("------------------ Search results : ");
            SearchResponse searchResponse = searchRequest.execute().actionGet();
//            SearchResponse searchResponse = client.prepareSearch().get();            
            
//            String queryString = "{\"query\":{\"query_string\":{\"query\":\"field:value\"}},\"fields\": [\"fieldname\"]}";
//            JSONObject queryStringObject = new JSONObject(queryString);
//            SearchResponse searchResponse = client.prepareSearch("lbourgeois").setTypes("countries").setSource(queryStringObject.toString()).execute().actionGet();
            


            for (org.elasticsearch.search.SearchHit searchHit : searchResponse.getHits().getHits()) {
                java.util.Map<String, Object> source = searchHit.getSourceAsMap();

                System.out.println("Json doc : " + source.get("json_document"));
            }
            
            System.out.println("Closing");
            client.close();
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
