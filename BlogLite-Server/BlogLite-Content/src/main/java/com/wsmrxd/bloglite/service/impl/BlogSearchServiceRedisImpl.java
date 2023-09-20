package com.wsmrxd.bloglite.service.impl;

import com.wsmrxd.bloglite.entity.Blog;
import com.wsmrxd.bloglite.mapping.BlogMapper;
import com.wsmrxd.bloglite.service.BlogSearchService;
import io.redisearch.Document;
import io.redisearch.Query;
import io.redisearch.Schema;
import io.redisearch.SearchResult;
import io.redisearch.client.AddOptions;
import io.redisearch.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;

@Service
@ConditionalOnProperty(name = "myConfig.blog.searchEngine", havingValue = "RediSearch")
public class BlogSearchServiceRedisImpl implements BlogSearchService {

    @Autowired
    private Jedis jedis;        // 单连接复用

    @Autowired
    private BlogMapper blogMapper;

    private static final Schema BLOG_SCHEMA = new Schema()
            .addTextField("title", 5.0)
            .addTextField("abstract", 2.0)
            .addTextField("content", 1.0);

    private static final String INDEX_NAME = "BlogSearch";

    private static final String DOCUMENT_ID_PREFIX = "BlogIndexed::";

    public void createBlogIndex(){
        Client client = new Client(INDEX_NAME, jedis);
        client.createIndex(BLOG_SCHEMA, Client.IndexOptions.defaultOptions());
    }

    public void dropBlogIndex(){
        Client client = new Client(INDEX_NAME, jedis);
        client.dropIndex(true);
    }

    public void initBlogRediSearch(){
        Client client = new Client(INDEX_NAME, jedis);
        client.dropIndex(true);
        client.createIndex(BLOG_SCHEMA, Client.IndexOptions.defaultOptions());

        List<Integer> blogIDs = blogMapper.selectAllBlogID();
        for(Integer id : blogIDs)
            addDocument(id);
    }

    public boolean addDocument(Blog blog){
        AddOptions addOptions = new AddOptions()
                .setLanguage("chinese")
                .setReplacementPolicy(AddOptions.ReplacementPolicy.FULL);

        Document document = new Document(DOCUMENT_ID_PREFIX + blog.getId());
        document.set("title", blog.getTitle());
        document.set("abstract", blog.getContentAbstract());
        document.set("content", blog.getContent());

        Client client = new Client(INDEX_NAME, jedis);
        return client.addDocument(document, addOptions);
    }

    public boolean addDocument(int blogID){
        Blog blog = blogMapper.selectBlogByID(blogID);
        if(blog == null)
            return false;
        else
            return addDocument(blog);
    }

    public boolean deleteDocument(int blogID){
        Client client = new Client(INDEX_NAME, jedis);
        String docID = DOCUMENT_ID_PREFIX + blogID;
        return client.deleteDocument(docID);
    }

    public List<Integer> searchBlogByKeyword(String keyword){
        Client client = new Client(INDEX_NAME, jedis);

        Query query = new Query(keyword)
                .setLanguage("chinese")
                .limit(0, 20);

        SearchResult result = client.search(query);
        var resultList = result.docs;

        List<Integer> ret = new ArrayList<>(resultList.size());
        for(var doc : resultList){
            String[] split = doc.getId().split(DOCUMENT_ID_PREFIX);
            if(split.length == 2)
                ret.add(Integer.parseInt(split[1]));
        }
        return ret;
    }
}
