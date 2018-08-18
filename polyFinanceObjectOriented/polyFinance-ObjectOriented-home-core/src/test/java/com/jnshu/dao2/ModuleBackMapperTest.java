package com.jnshu.dao2;

import com.jnshu.Entry;
import com.jnshu.entity.ModuleBack;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Entry.class)
public class ModuleBackMapperTest {

    @Resource
    ModuleBackMapper2 moduleBackMapper;
    @Test
    public void saveModuleBack() throws Exception {
        ModuleBack moduleBack = new ModuleBack();
        moduleBack.setCreateAt(System.currentTimeMillis());
        moduleBack.setCreateBy(8L);
        moduleBack.setUpdateAt(System.currentTimeMillis());
        moduleBack.setUpdateBy(7L);
        moduleBack.setMenuId("5.1");
        moduleBack.setModuleName("销量统计");
        moduleBack.setModuleType("web");
        moduleBack.setModuleUrl("/a/u/sales");
        moduleBack.setSuperId(4);
        moduleBackMapper.saveModuleBack(moduleBack);
        moduleBack.setMenuId("5.2");
        moduleBack.setModuleName("查看明细");
        moduleBack.setModuleUrl("/a/u/sales-detail");
        moduleBackMapper.saveModuleBack(moduleBack);
        System.out.println(moduleBack.getId());
    }

    @Test
    public void deleteModuleBack() throws Exception {
        System.out.println(moduleBackMapper.deleteModuleBack(19L));
    }

    @Test
    public void updateModuleBack() throws Exception {
        ModuleBack m= moduleBackMapper.getById(22L);
        m.setModuleName("haha");
        System.out.println(moduleBackMapper.updateModuleBack(m));
    }

    @Test
    public void getAll() throws Exception {
        System.out.println(moduleBackMapper.getAll());
    }

    @Test
    public void getById() throws Exception {
//        System.out.println(moduleBackMapper.getAll());
//        System.out.println(moduleBackMapper.getDomainModuleBack(20L));

        //新增对象
        ModuleBack moduleBack = new ModuleBack();
        moduleBack.setCreateAt(System.currentTimeMillis());
        moduleBack.setCreateBy(1L);
        moduleBack.setUpdateAt(System.currentTimeMillis());
        moduleBack.setCreateBy(1l);
        moduleBack.setMenuId("3");
        moduleBack.setModuleName("测试模块");
        moduleBack.setModuleType("web");
        moduleBack.setModuleUrl("/a/u/testModule");
        moduleBack.setSuperId(0);
//        System.out.println(moduleBackMapper.saveModuleBack(moduleBack));

        //更新对象
        moduleBack.setId(23L);
        moduleBack.setModuleName("测试模块--测试更新");
        System.out.println(moduleBackMapper.updateModuleBack(moduleBack));

        System.out.println(moduleBackMapper.deleteModuleBack(22L));
    }
}