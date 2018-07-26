package com.jnshu.service;

import com.jnshu.model.userApplication.UserApplication;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserApplicationService {

    public List<UserApplication> getAllUser(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        System.out.println(pageNum+ "，" + pageSize);
        List<UserApplication> userList = new ArrayList<>();
        for (int i=1; i< pageSize;i++){
            UserApplication user = new UserApplication(Long.valueOf(i),"3453245234354"+i,System.currentTimeMillis()+100000000L,"1369894564","张三",String.valueOf(879079834),i%2,"拒绝");
            userList.add(user);
        }
        return userList;
    }
}
