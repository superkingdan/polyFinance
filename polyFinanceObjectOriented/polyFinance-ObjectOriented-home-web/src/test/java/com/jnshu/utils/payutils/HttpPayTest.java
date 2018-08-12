package com.jnshu.utils.payutils;

import com.jnshu.Entry;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
//classes后面是启动类.class
@SpringBootTest(classes = Entry.class)
public class HttpPayTest {

    @Test
    public void comparedParam() {
        /**
         * 0000
         0003050F0363796
         160
         000036195459
         6214855100590765
         5000000
         13047c4146daed434a53f7d4f89fe1ef
         4f3118f41cba8271b03dc47f51881901
         */
        System.out.println(HttpPay.comparedParam("3.0","10","0000","0003050F0363796","160","000036195459","6214855100590765","5000000","13047c4146daed434a53f7d4f89fe1ef"));
    }
}