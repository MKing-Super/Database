package pers.mk.es.esjavawork.test;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;

/**
 * @Description: elasticsearch测试
 * @Author: kun.ma
 * @Date: 2022/2/8 16:52
 */
public class ESClient {

    public static void main(String[] args) {

        HttpHost httpHost = new HttpHost("127.0.0.1", 9200);

        RestClientBuilder clientBuilder = RestClient.builder(httpHost);

        RestHighLevelClient client = new RestHighLevelClient(clientBuilder);

        System.out.println(client);
    }

}
