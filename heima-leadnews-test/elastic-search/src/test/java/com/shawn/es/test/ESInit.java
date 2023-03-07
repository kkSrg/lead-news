package com.shawn.es.test;

import com.alibaba.fastjson.JSON;
import com.heima.model.article.entities.Article;
import com.shawn.es.ESApplication;
import com.shawn.es.entities.ArticleDoc;
import com.shawn.es.mapper.ArticleMapper;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * @author shawn
 * @date 2023年 01月 26日 21:55
 */
@SpringBootTest(classes = ESApplication.class)
@RunWith(SpringRunner.class)
public class ESInit {

    @Resource
    private ArticleMapper articleMapper;

    @Resource
    private RestHighLevelClient restHighLevelClient;

    @Test
    public void init() throws IOException {
        List<ArticleDoc> articleDocs = articleMapper.initElasticSearch();

        BulkRequest bulkRequest = new BulkRequest("app_info_article");

        for (ArticleDoc articleDoc : articleDocs) {
            String data = JSON.toJSONString(articleDoc);
            IndexRequest request = new IndexRequest().id(articleDoc.getId().toString());
            request.source(data, XContentType.JSON);
            bulkRequest.add(request);
        }

        BulkResponse response = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
    }


}
