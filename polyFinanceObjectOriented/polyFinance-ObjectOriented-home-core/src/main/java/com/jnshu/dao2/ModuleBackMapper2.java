package com.jnshu.dao2;

import com.jnshu.Domain2.DomainModuleBack;
import com.jnshu.Domain2.DomainModuleBackForLogin;
import com.jnshu.entity.ModuleBack;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component(value = "moduleBackMapper2")
public interface ModuleBackMapper2 {

    /**
     * 模块新增
     * @return
     * @throws Exception
     */
    @Insert("insert into module_back( create_at,create_by, update_at,update_by,module_name,super_id,module_type,module_url,menu_id) values (#{createAt},#{createBy}, #{updateAt},#{updateBy},#{moduleName},#{superId},#{moduleType},#{moduleUrl},#{menuId})")
    @Options(useGeneratedKeys = true)
    Integer saveModuleBack(ModuleBack moduleBack) throws Exception;

    /**
     * 模块删除
     * @return
     * @throws Exception
     */
    @Delete("delete from module_back where id = #{id}")
    Boolean deleteModuleBack(Long id) throws Exception;

    /**
     * 模块更新
     * @return
     * @throws Exception
     */
    @Update("update module_back set create_at=#{createAt}, create_by=#{createBy}, update_at=#{updateAt},update_by=#{updateBy},module_name=#{moduleName},super_id=#{superId},module_type=#{moduleType},module_url=#{moduleUrl},menu_id=#{menuId} where id=#{id}")
    Boolean updateModuleBack(ModuleBack moduleBack) throws Exception;

    /**
     * 模块列表--左连接
     * @return
     * @throws Exception
     */
//    @Select("select a.id,a.create_at,b.login_name as createBy,a.update_at,b.login_name as updateBy,a.module_name,a.super_id,a.module_type, a.module_url, a.menu_id  from module_back a, user_back b where a.create_by=b.id or a.update_by=b.id")
    @Select("select distinct a.id,a.create_at,b.login_name as create_by,a.update_at,b.login_name as update_by,a.module_name,a.super_id,a.module_type, a.module_url, a.menu_id  from module_back a left join  user_back b on a.create_by=b.id or a.update_by=b.id")
    List<DomainModuleBack> getAll() throws Exception;

    /**
     * 模块详情
     * @return DomainModuleBack
     * @throws Exception
     */
    @Select("select id, module_name, super_id, module_type, module_url, menu_id from module_back where id = #{id}")
    DomainModuleBack getDomainModuleBack(Long id) throws Exception;

    //获得全部模块列表。
    @Select("select id, module_name, super_id from module_back")
    List<DomainModuleBackForLogin> getAllModules() throws Exception;

    //获得全部模块id列表。
    @Select("select id, module_name, super_id from module_back")
    List<Long> getAllModuleIds() throws Exception;

    //查找-通过id查详情。
    @Select("select * from module_back where id=#{id}")
//    @Select("select id,module_name,super_id,module_type,module_url,menu_id from module_back where id=#{id}")
    ModuleBack getById(Long id) throws Exception;


}
