package com.jnshu.service1.Impl;

import com.jnshu.Entry;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;
@RunWith(SpringJUnit4ClassRunner.class)
//classes后面是启动类.class
@SpringBootTest(classes = Entry.class)
public class PaymentServiceImpl1Test {
@Autowired
PaymentServiceImpl1 paymentServiceImpl1;
    @Test
    public void addTransaction() {
        System.out.println(paymentServiceImpl1.addTransaction(29,"UKZXC1810000031"));
    }

    @Test
    public void updateTransaction(){
        System.out.println(paymentServiceImpl1.updateTransactionLog(29));
    }

    @Test
    public void updateContract(){
        System.out.println(paymentServiceImpl1.updateContract(55));
    }
}