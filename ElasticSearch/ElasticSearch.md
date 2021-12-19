# ElasticSearch学习笔记



安装ES&Kibana

```yml
version: "3.1"
services:
  elasticsearch:
    image: daocloud.io/library/elasticsearch:6.5.4
    restart: always
    container_name: elasticsearch
    ports:
      - 9200:9200
  kibana:
    image: daocloud.io/library/kibana:6.5.4
    restart: always
    container_name: kibana
    ports:
      - 5601:5601
    environment:
      - elasticsearch_url=http://192.168.126.128:9200
    depends_on:
      - elasticsearch
```







```
docker run -d --name elasticsearch -p 9200:9200 -p 9300:9300 -e "discovery.type=single-node" elasticsearch:6.5.4
```





下载地址（6.5.4版本）：

```
https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-6.5.4.zip
https://artifacts.elastic.co/downloads/kibana/kibana-6.5.4-windows-x86_64.zip

https://github.com/medcl/elasticsearch-analysis-ik/releases/download/v6.5.4/elasticsearch-analysis-ik-6.5.4.zip
```





## ES语法

```json
# 创建一个索引
PUT /person
{
  "settings": {
    "number_of_shards": 5,
    "number_of_replicas": 1
  }
}

```

