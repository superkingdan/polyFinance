package com.jnshu.service1.Impl;

import com.jnshu.Entry;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Calendar;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
//classes后面是启动类.class
@SpringBootTest(classes = Entry.class)
public class PaymentServiceImplTest {

    @Test
    public void getInvestment() {
        Calendar calendar=Calendar.getInstance();
        System.out.println(calendar);
    }
}