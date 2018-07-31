package com.jnshu.service1.impl;

import com.jnshu.Entry;
import com.jnshu.utils.TransString;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
//classes后面是启动类.class
@SpringBootTest(classes = Entry.class)
        public class ClaimsMatchingServiceImplTest {

    @Test
    public void getClaimsInfoById() {
        System.out.println(TransString.transClaimsCode("UKZQ18054687"));
    }

    @Test
    public void getClaimsMatchingListByRpo() {
    }

    @Test
    public void getClaimsMatchingListById() {
    }

    @Test
    public void saveClaimsMatching() {
    }
}