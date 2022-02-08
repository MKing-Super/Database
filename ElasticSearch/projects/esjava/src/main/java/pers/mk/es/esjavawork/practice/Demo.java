package pers.mk.es.esjavawork.practice;

import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.json.JsonXContent;
import org.junit.Test;
import pers.mk.es.esjavawork.utils.ESUtils;

import java.io.IOException;

/**
 * @Description: es练习
 * @Author: kun.ma
 * @Date: 2022/2/8 16:59
 */
public class Demo {
    RestHighLevelClient client = ESUtils.getClient();
    String index = "person";
    String type = "man";

    /**
     * 创建索引
     * @Author  kun.ma
     * @Date    2022/2/8 17:40
     * @Param   []
     * @Return  void
     */
    @Test
    public void createIndex() throws IOException {

        Settings.Builder settings = Settings.builder()
                .put("number_of_shards", 3)
                .put("number_of_replicas", 1);

        XContentBuilder mappings = JsonXContent.contentBuilder()
                .startObject()
                .startObject("properties")
                .startObject("name")
                .field("type", "text")
                .endObject()
                .startObject("age")
                .field("type", "integer")
                .endObject()
                .startObject("birthday")
                .field("type", "date")
                .field("format", "yyyy-MM-dd")
                .endObject()
                .endObject()
                .endObject();

        CreateIndexRequest request = new CreateIndexRequest(index)
                .settings(settings)
                .mapping(type, mappings);

        CreateIndexResponse resp = client.indices().create(request, RequestOptions.DEFAULT);

        System.out.println(resp.toString());

    }


}
