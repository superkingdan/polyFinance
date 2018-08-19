package com.jnshu.dao;

import com.jnshu.Entry;
import com.jnshu.entity.User;
import com.jnshu.exception.MyException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Calendar;

@RunWith(SpringJUnit4ClassRunner.class)
//classes后面是启动类.class
@SpringBootTest(classes = Entry.class)
public class UserMapperTest {
@Autowired
UserMapper1 userMapper1;
    @Test
    public void getUserRealInfo() {
        System.out.println(userMapper1.getUserRealInfo(1));
    }

    @Test
    public void getPropertyById() {
        System.out.println(userMapper1.getPropertyById(1));
    }

    @Test
    public void updatePropertyById() {
        User user=new User();
        user.setProperty("7000");
        user.setId(1);
        System.out.println(userMapper1.updatePropertyById(user));
    }

    @Test
    public void getDefaultCardById() {
        System.out.println(userMapper1.getDefaultCardById(1));
    }

    @Test
    public void getUserInfoByContractCode(){
        System.out.println(userMapper1.getUserInfoByContractCode("UKZXC18000001"));
    }

    @Test
    public void getUserIdByName(){
        Long[] longs= userMapper1.getUserIdByName("王");
        System.out.println(longs[0]);
        System.out.println(longs[1]);
    }

    @Test
    public  void  getUserNameById(){
        System.out.println(userMapper1.getUserNameById(1));
    }

    @Test
    public void test(){
        Calendar time=Calendar.getInstance();
        int thisYear=time.get(Calendar.YEAR);
        System.out.println(thisYear%100);
    }

    @Test
    public void getUserRealStatusById(){
        System.out.println(userMapper1.getUserRealStatusById(1));
    }

    @Test
    public void getIsNewById(){
        Integer isNew=userMapper1.getIsNewById(1);
        System.out.println(isNew);
        if(isNew!=null){
            System.out.println("新手礼包以购买过");
        }
    }

    @Test
    public void updateIsNew(){
        System.out.println(userMapper1.updateIsNewById(2));
    }
}