package com.jnshu.controller2;

import com.jnshu.entity.UserBack;
import com.jnshu.service.UserBackService2;
import com.jnshu.utils.AliyunOSSUtil;
import com.jnshu.utils.CAM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class LoginController2 {

    @Autowired
    UserBackService2 userBackService2;

    @RequestMapping(value = "/a/login",method = RequestMethod.POST)
    public Object loginBackEnd(@RequestParam String loginName, @RequestParam String hashKey, HttpServletRequest request, HttpServletResponse response){
        UserBack userBack = new UserBack();
        userBack.setLoginName(loginName);
        userBack.setHashKey(hashKey);

        if (0 ==userBack.getLoginName().length() || userBack.getHashKey().length() == 0){
            CAM cam = new CAM();
            cam.setErrorMessage("用户名或密码不能为空");
            return cam;
        }

        Object result = new ArrayList<>();
        try {
            result = userBackService2.verifyUserBack(userBack,request,response);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(result);
        return result;
    }

    @RequestMapping("/intercepted")
    public Map<String,Object> beIntercepted(HttpServletRequest request,@RequestParam(value = "next",required = false) String next){
        request.getSession().setAttribute("next", next);

        Map<String,Object> cAm = new HashMap<>();
        cAm.put("code", -1);
        cAm.put("errorMessage", "当前访问需要权限，请登录。");
        return cAm;
    }

    //上传图片接口。
    @RequestMapping(value = "/a/u/image-upload",method = RequestMethod.POST)
    public String uploadImage(@RequestParam MultipartFile pic, HttpServletRequest request) throws Exception {
        String result = null;

        if (null == pic){
            result = "图片没有传递成功";
        }

        //获得图片后缀
        String[] fileName = pic.getOriginalFilename().split("\\.");
        String photoSuffix = fileName[fileName.length-1];
        //判断图片格式
        if(!photoSuffix.equals("jpg") && !photoSuffix.equals("png") && !photoSuffix.equals("img")){
            result = "图片格式不符合";
        }
        //创建文件key
        Long fileId = System.currentTimeMillis();
        String key =  fileId+"/" + fileId +"." + photoSuffix;

        AliyunOSSUtil aliyunOSSUtil = new AliyunOSSUtil();
        String piclink =null;
        //上传图片。
        try {
            aliyunOSSUtil.uploadFile(key, pic.getBytes());

            //图片链接。
             piclink = "https://imageuploadbychenxin.oss-cn-hangzhou.aliyuncs.com/" + fileId + "/"+fileId +"."+ photoSuffix;
        } catch (IOException e) {

        }

        System.out.println(piclink);
       return piclink;
    }
}
