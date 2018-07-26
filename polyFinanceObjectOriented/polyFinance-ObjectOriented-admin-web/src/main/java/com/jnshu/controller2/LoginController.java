package com.jnshu.controller2;

import com.jnshu.model.module.Module;
import com.jnshu.model.module.ModuleListInit;
import com.jnshu.model.module.ModuleProduce;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LoginController {

    @RequestMapping("/a/login")
    public Object t(){

        ModuleListInit listInit = new ModuleListInit();
        //相当于查询结果。
        List<Module> list =listInit.init();
        ModuleProduce moduleProduce = new ModuleProduce();
        return moduleProduce.getSuper(list);
    }
}
