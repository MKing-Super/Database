package pers.mk.es.esjavawork.utils;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;

/**
 * @Description: es连接工具类
 * @Author: kun.ma
 * @Date: 2022/2/8 17:02
 */
public class ESUtils {

    public static RestHighLevelClient getClient(){
        //创建HttpHost对象
        HttpHost httpHost = new HttpHost("127.0.0.1", 9200);
        //创建RestClientBuilder
        RestClientBuilder clientBuilder = RestClient.builder(httpHost);
        //创建RestHighLevelClient
        RestHighLevelClient client = new RestHighLevelClient(clientBuilder);
        //返回
        return client;
    }
}
