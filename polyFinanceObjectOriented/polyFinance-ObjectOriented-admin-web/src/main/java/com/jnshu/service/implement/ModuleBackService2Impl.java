package com.jnshu.service.implement;

import com.jnshu.Domain2.DomainModuleBack;
import com.jnshu.dao2.ModuleBackMapper2;
import com.jnshu.entity.ModuleBack;
import com.jnshu.service.ModuleBackService2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2018/8/18.
 */
@Service
@Component
public class ModuleBackService2Impl implements ModuleBackService2 {

    @Autowired
    ModuleBackMapper2 moduleBackMapper2;

    /**
     * 模块列表
     */
    @Override
    public List<DomainModuleBack> getAll() throws Exception {
        return moduleBackMapper2.getAll();
    }

    /**
     * 模块详情-获取
     */
    @Override
    public DomainModuleBack getDomainModuleBack(Long id) throws Exception {
        return moduleBackMapper2.getDomainModuleBack(id);
    }

    /**
     * 模块更新
     * @return
     * @throws Exception
     */
    @Override
    public Boolean updateModuleBack(ModuleBack moduleBack) throws Exception {
        return moduleBackMapper2.updateModuleBack(moduleBack);
    }

    /**
     * 模块新增
     * @return
     * @throws Exception
     */
    @Override
    public Integer saveModuleBack(ModuleBack moduleBack) throws Exception {
        moduleBackMapper2.saveModuleBack(moduleBack);
        return (int) moduleBack.getId();
    }

    /**
     * 模块删除
     * @return
     * @throws Exception
     */
    @Override
    public Boolean deleteModuleBack(Long id) throws Exception {
        return moduleBackMapper2.deleteModuleBack(id);
    }
}
