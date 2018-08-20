package com.jnshu.dao2;

import com.jnshu.Domain2.DomainUserFrontDetail;
import com.jnshu.Entry;
import com.jnshu.dto2.UserFrontListRPO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Entry.class)
public class UserFrontMapperTest {

    @Resource
    UserFrontMapper2 mapper;
    @Test
    public void getUserFrontList() throws Exception {
        UserFrontListRPO rpo = new UserFrontListRPO();
//        rpo.setRealName("王大锤");
//        rpo.setPhoneNumber("13101436675");
        rpo.setCreateAt1(1532886769206L);
        rpo.setCreateAt2(1532886789006l);
//        rpo.setReferrerId("123");
        rpo.setStatus(1);
        System.out.println(mapper.getUserFrontList(rpo));
    }

    @Test
    public void getUserFrontDetail() throws Exception{
        System.out.println(mapper.getUserFrontDetailById(2L));
        System.out.println(mapper.getUserFrontBankCardsById(2l));
    }

    @Test
    public void getUserFrontBankCardSById() throws Exception{
        DomainUserFrontDetail U = mapper.getUserFrontDetailById(100L);
        System.out.println(U);
    }
}