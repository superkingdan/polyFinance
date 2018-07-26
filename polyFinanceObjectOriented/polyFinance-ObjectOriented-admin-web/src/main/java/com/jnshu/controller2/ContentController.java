package com.jnshu.controller2;

import com.jnshu.model.content.Content;
import com.jnshu.model.content.ContentInfo;
import com.jnshu.service.ContentService;
import com.jnshu.utils.CAM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ContentController {
    private CAM cam= new CAM();

    @Autowired
    ContentService contentService;

    //内容列表
    @RequestMapping(value = "/a/u/contents",method = RequestMethod.GET)
    public Object getContents(@RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize, @RequestParam(required = false) String title, @RequestParam(required = false) String updateBy, @RequestParam(required = false) Long updateAt1, @RequestParam(required = false) Long updateAt2, @RequestParam(required = false) Integer status, @RequestParam(required = false) Integer type){
        List<Object> list = new ArrayList<>();
        List<Content> contents = contentService.getAll(pageNum, pageSize);
        list.add(cam);
        Map<String,Integer> t = new HashMap<>();
        t.put("total",100);
        list.add(t);
        list.add(contents);
        list.add("内容列表  收到的参数：pageNum="+pageNum+",pageSize="+pageSize+", title="+title+",updateBy="+updateBy+", updateAt="+updateAt1+",updateAt2="+updateAt2+", status="+status+",type="+status);
        return list;
    }

    //获得内容详情
    @RequestMapping(value = "/a/u/contents/{id}",method = RequestMethod.GET)
    public List<Object> getInfo(@PathVariable long id){
        List<Object> list = new ArrayList<>();
        ContentInfo contentInfo = new ContentInfo();
        contentInfo.setId(id);
        contentInfo.setTitle("关于我们");
        contentInfo.setType((int) (id%3));
        contentInfo.setBannerCover("wwww.");
        contentInfo.setDetails("wwww.");
        contentInfo.setStatus((int) (id%2));

        list.add(cam);
        list.add(contentInfo);
        return list;
    }

    //保存内容编辑
    @RequestMapping(value = "/a/u/contents/{id}",method = RequestMethod.PUT)
    public List<Object> updateInfo(@PathVariable long id, @RequestParam(required = false) String title, @RequestParam(required = false) Integer type, @RequestParam(required = false) String bannerCover, @RequestParam(required = false) String detail, @RequestParam(required = false) Integer status){
        List<Object> list = new ArrayList<>();

        list.add(cam);
        list.add("保存内容编辑  收到的参数：id="+id+", title="+title+",type="+type+", bannerCover"+bannerCover+",details"+detail+", status="+status+",type="+status);
        return list;
    }

    //新增内容
    @RequestMapping(value = "/a/u/contents/new",method = RequestMethod.POST)
    public List<Object> saveInfo(@RequestParam long id, @RequestParam(required = false) String title, @RequestParam(required = false) Integer type, @RequestParam(required = false) String bannerCover, @RequestParam(required = false) String detail, @RequestParam(required = false) Integer status){
        List<Object> list = new ArrayList<>();

        list.add(cam);
        list.add("新增内容  收到的参数：id="+String.valueOf(id)+", title="+title+",type="+type+", bannerCover"+bannerCover+",details"+detail+", status="+status);
        return list;
    }

    //指定内容上下线操作
    @RequestMapping(value = "/a/u/contents/{id}/status",method = RequestMethod.PUT)
    public List<Object> updateContentStatus(@PathVariable long id, @RequestParam Integer status){
        List<Object> list = new ArrayList<>();

        list.add(cam);
        list.add("指定内容上下线操作  收到的参数：id="+id+", status="+status);
        return list;
    }

    //删除指定内容
    @RequestMapping(value = "/a/u/contents/{id}",method = RequestMethod.DELETE)
    public List<Object> deleteContent(@PathVariable Long id){
        List<Object> list = new ArrayList<>();

        list.add(cam);
        list.add("删除指定内容  收到的参数：id="+id);
        return list;
    }
}
