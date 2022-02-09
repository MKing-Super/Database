package pers.mk.es.esjavawork.practice;

import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
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
public class DemoIndex {
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
        //1、准备索引的settings
        Settings.Builder settings = Settings.builder()
                .put("number_of_shards", 3)
                .put("number_of_replicas", 1);
        //2、准备索引的结构mappings
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
        //3、将索引的settings和mappings封装到一个request对象中
        CreateIndexRequest request = new CreateIndexRequest(index)
                .settings(settings)
                .mapping(type, mappings);
        //4、通过client对象去连接es并执行创建索引
        CreateIndexResponse resp = client.indices().create(request, RequestOptions.DEFAULT);
        //5、输出
        System.out.println(resp.toString());

    }

    /**
     * 判断刚刚创建的索引是否存在
     * @Author  kun.ma
     * @Date    2022/2/8 17:46
     * @Param   []
     * @Return  void
     */
    @Test
    public void existsIndex() throws IOException {
        //1、准备request对象
        GetIndexRequest request = new GetIndexRequest();
        request.indices(index);
        //2、通过client操作
        boolean exists = client.indices().exists(request, RequestOptions.DEFAULT);
        //3、输出
        System.out.println(exists);
    }

    /**
     * 删除索引
     * @Author  kun.ma
     * @Date    2022/2/9 9:02
     * @Param   []
     * @Return  void
     */
    @Test
    public void deleteIndex() throws IOException {
        //1、准备request对象
        DeleteIndexRequest request = new DeleteIndexRequest();
        request.indices(index);
        //2、通过client对象执行
        AcknowledgedResponse delete = client.indices().delete(request, RequestOptions.DEFAULT);
        //3、返回
        System.out.println(delete.isAcknowledged());
    }


}
