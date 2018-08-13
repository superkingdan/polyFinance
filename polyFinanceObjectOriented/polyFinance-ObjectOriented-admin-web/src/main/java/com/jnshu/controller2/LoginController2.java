package com.jnshu.controller2;

import com.jnshu.entity.UserBack;
import com.jnshu.exception.MyException;
import com.jnshu.service.UserBackService2;
import com.jnshu.utils.AliyunOSSUtil;
import com.jnshu.utils.CAM;
import com.jnshu.utils.CookieUtil;
import com.jnshu.utils.ossutils.ConvertToFile;
import com.jnshu.utils.ossutils.OSSUploadUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class LoginController2 {

    @Autowired
    UserBackService2 userBackService2;

    private static final Logger log= LoggerFactory.getLogger(LoginController2.class);

    @RequestMapping(value = "/a/login",method = RequestMethod.POST)
    public Object loginBackEnd(@RequestParam(required = false) String loginName, @RequestParam(required = false) String hashKey, HttpServletRequest request, HttpServletResponse response){
        UserBack userBack = new UserBack();
        userBack.setLoginName(loginName);
        userBack.setHashKey(hashKey);

        if (0 ==userBack.getLoginName().length() || userBack.getHashKey().length() == 0){
            CAM cam = new CAM();
            cam.setErrorMessage("用户名或密码不能为空");
            return cam;
        }

        Object result = null;
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


    /**
     * 上传图片
     * @param file 用户上传图片
     * @return 返回结果
     * @throws Exception
     */
    @PostMapping(value = "/a/u/image-upload",produces = "application/json;charset=UTF-8")
    public Map uploadUserSign(@RequestParam(value = "file",required = false) MultipartFile file, HttpServletRequest request)throws Exception{
        Map<String,Object> map=new HashMap<>();
        //获取用户id
        long id;
        String uidS= CookieUtil.getCookieValue(request,"uid");
        if (uidS!=null) {
            id = Long.parseLong(uidS);
        }
        //如果cookie中没有uid直接报错
        else {
            log.error("上传图片，但是cookie中没有uid");
            throw new MyException(10001,"授权已过期，请重新登录");
        }
        log.info("用户"+id+"在上传图片");
        //判断是否有图片上传
        if(file==null||file.equals("")||file.getSize()<=0){
            log.error("用户"+id+"想上传图片，但是无上传图片，很尴尬");
            throw new MyException(10030,"文件不能为空");
        }
        //判断是否为图片类型
        File f= ConvertToFile.multipartFileToFile(file);//转化文件格式
        String fName=f.getName();
        System.out.println(fName);
//        //判断是否为图片格式
//        if(!IsImage.isImage(fName)){
//            //如果不是就删除临时文件，并报错
//            File del = new File(f.toURI());
//            boolean delete=del.delete();
//            System.out.println(delete);
//            map.put("code",10031);
//            map.put("message","there is error file");
//            log.info("图片格式不正确，很尴尬");
//            return map;
//        }
        String fileUrl="";
        try {
            fileUrl = OSSUploadUtil.uploadFile(f, "jpg");//上传文件,设置后缀为jpg
        }catch (Exception e){
            log.error("用户"+id+"上传图片发生问题");
            log.error(e.getMessage());
            //删除临时文件
            File del = new File(f.toURI());
            boolean delete=del.delete();
            System.out.println(delete);
            throw new MyException(10032,"图片上传失败");
        }
        map.put("code",0);
        map.put("message","success");
        map.put("piclink",fileUrl);
        log.info("上传成功，图片url为"+fileUrl);
        //删除临时文件
        File del = new File(f.toURI());
        boolean delete=del.delete();
        System.out.println(delete);
        return map;
    }
}
