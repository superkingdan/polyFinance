package com.jnshu.service1.Impl;

import com.jnshu.Entry;
import com.jnshu.service1.PaymentService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Calendar;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
//classes后面是启动类.class
@SpringBootTest(classes = Entry.class)
public class PaymentServiceImplTest {
     @Autowired
    PaymentService paymentService;


    @Test
    public void getInvestment() {

    }

    @Test
    public void updateTransactionLog(){
        System.out.println(paymentService.updateTransactionLog(18));
    }

    @Test
    public void updateContract(){
        System.out.println(paymentService.updateContract(44));

    }

    @Test
    public void addTransaction(){
        System.out.println(paymentService.addTransaction(18,"UKZXC1810000020"));
    }
}