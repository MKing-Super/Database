package pers.mk.apollo.work.apollowork.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ctrip.framework.apollo.core.enums.Env;
import com.ctrip.framework.apollo.openapi.client.ApolloOpenApiClient;
import com.ctrip.framework.apollo.openapi.dto.NamespaceReleaseDTO;
import com.ctrip.framework.apollo.openapi.dto.OpenItemDTO;
import com.ctrip.framework.apollo.openapi.dto.OpenReleaseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Description: TODO
 * @Author: kun.ma
 * @Date: 2022/8/11 15:19
 */
@Slf4j
@Controller
public class TestController {

    private static final String portalUrl = "http://127.0.0.1:8070"; // portal url
    private static final String APOLLO_TOKEN = "3537deae79083ef5126a9c1170264e958a7e59fa";
    private static final String MK_APP_ID = "common-config";
    private static final String MK_NAMESPACE = "all-config";
    private static ApolloOpenApiClient apolloOpenApiClient;

    public static ApolloOpenApiClient getClient() {
        if (apolloOpenApiClient != null) {
            return apolloOpenApiClient;
        }
        apolloOpenApiClient = ApolloOpenApiClient.newBuilder()
                .withPortalUrl(portalUrl)
                .withToken(APOLLO_TOKEN)
                .build();
        return apolloOpenApiClient;
    }

    @GetMapping("/get-config")
    @ResponseBody
    public JSONObject getConfig(String parameter){
        ApolloOpenApiClient client = this.getClient();
        OpenItemDTO mmm = client.getItem(MK_APP_ID, Env.DEV.name(), "default", MK_NAMESPACE, parameter);
        return JSON.parseObject(JSON.toJSONString(mmm));
    }

    @GetMapping("/update-config")
    @ResponseBody
    public Boolean updateConfig(String key,String value,String comment){
        try {
            ApolloOpenApiClient client = this.getClient();
            OpenItemDTO openItemDTO = new OpenItemDTO();
            openItemDTO.setKey(key);
            openItemDTO.setValue(value);
            openItemDTO.setComment(comment);
            openItemDTO.setDataChangeCreatedBy("apollo");
            client.createOrUpdateItem(MK_APP_ID, Env.DEV.name(),"default",MK_NAMESPACE,openItemDTO);
        }catch (Exception e){
            log.error(e.getMessage());
            return false;
        }
        return true;
    }

    @GetMapping("/release-config")
    @ResponseBody
    public JSONObject releaseConfig(){
        try {
            ApolloOpenApiClient client = this.getClient();
            NamespaceReleaseDTO namespaceReleaseDTO = new NamespaceReleaseDTO();
            namespaceReleaseDTO.setReleaseTitle("test版本");
            namespaceReleaseDTO.setReleasedBy("apollo");
            namespaceReleaseDTO.setEmergencyPublish(true);
            OpenReleaseDTO result = client.publishNamespace(MK_APP_ID, Env.DEV.name(), "default", MK_NAMESPACE, namespaceReleaseDTO);
            return JSON.parseObject(JSON.toJSONString(result));
        }catch (Exception e){
            log.error(e.getMessage());
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("error","500");
            return jsonObject;
        }
    }




}
