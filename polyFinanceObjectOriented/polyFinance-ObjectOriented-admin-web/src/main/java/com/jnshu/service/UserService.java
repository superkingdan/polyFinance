package com.jnshu.service;


import com.jnshu.model.user.User;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    public List<User> getAllUser(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        System.out.println(pageNum+ "，" + pageSize);
        List<User> userList = new ArrayList<>();
        for (int i=1; i< pageSize;i++){
            User user = new User(Long.valueOf(i),"UK1810"+000+String.valueOf(i),System.currentTimeMillis()+100000000L,"136989",String.valueOf(i),i%2,"张三","3453245234354"+i,String.valueOf(879079834),String.valueOf(4353453),i%2);
            userList.add(user);
        }
        return userList;
    }
}
