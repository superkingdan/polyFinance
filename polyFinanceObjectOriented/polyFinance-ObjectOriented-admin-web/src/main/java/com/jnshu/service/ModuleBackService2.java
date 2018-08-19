package com.jnshu.service;

import com.jnshu.Domain2.DomainModuleBack;
import com.jnshu.entity.ModuleBack;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Administrator on 2018/8/18.
 */
@Component(value = "moduleBackService2")
public interface ModuleBackService2 {

    /**
     * 模块列表
     */
    List<DomainModuleBack> getAll() throws Exception;

    /**
     * 模块详情-获取
     */
    DomainModuleBack getDomainModuleBack(Long id) throws Exception;

    /**
     * 模块更新
     * @return
     * @throws Exception
     */
    Boolean updateModuleBack(ModuleBack moduleBack) throws Exception;

    /**
     * 模块新增
     * @return
     * @throws Exception
     */
    Integer saveModuleBack(ModuleBack moduleBack) throws Exception;

    /**
     * 模块删除
     * @return
     * @throws Exception
     */
    Boolean deleteModuleBack(Long id) throws Exception;
}
