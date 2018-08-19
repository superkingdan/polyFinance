package com.jnshu;

import org.springframework.boot.test.context.SpringBootTest;

/**
 * Created by Administrator on 2018/8/19.
 */

@SpringBootTest
public class Test {

    @org.junit.Test
    public void t() {
        String creditorLine = "0.4";

        if (Float.isNaN(Float.parseFloat(creditorLine))){
            System.out.println("错误");
        }
        System.out.println("正确");
    }
}
