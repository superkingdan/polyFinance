package com.jnshu.service;

import com.jnshu.model.content.Content;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ContentService {

    public List<Content> getAll(int pageNum, int pageSize){
        List<Content> contents = new ArrayList<>();

        for (int i=1;i<pageSize;i++){
            Content content = new Content();
            content.setId(i);
            content.setTitle("推荐");
            content.setType(i%2);
            content.setStatus(i%2);
            content.setLoginName("host");
            content.setUpdateAt(System.currentTimeMillis() + 1000*i + 10000*1);
            contents.add(content);
        }
        return contents;
    }
}
