package com.jnshu.dao2;

import com.jnshu.Domain2.DomainUserBack;
import com.jnshu.dto2.UserBackListRPO;
import com.jnshu.entity.UserBack;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component(value = "userBackMapper2")
public interface UserBackMapper2 {

    @Insert("insert into user_back(create_at, update_at,create_by, update_by,login_name, salt,hash_key,phone_number) values (#{createAt},#{updateAt},#{createAt},#{updateBy},#{loginName},#{salt},#{hashKey},#{phoneNumber})")
    @Options(useGeneratedKeys = true,keyProperty = "id")
    Long saveUserBack(UserBack userBack) throws Exception;

    //删除
    @Delete("delete from user_back where id=#{id}")
    Boolean deleteById(Long id) throws Exception;

    //更新
    @Update("update user_back set update_at=#{updateAt}, update_by=#{updateBy},hash_key=#{hashKey}, phone_number=#{phoneNumber} where id=#{id}")
    Boolean updateUserBack(UserBack userBack) throws Exception;

    //查询总数
    @Select("select count(*) from user_back")
    Integer getTotal() throws Exception;
    //查找by id
    @Select("select id,login_name, salt, hash_key,phone_number from user_back where id=#{id}")
    UserBack getUserBackById(Long id) throws Exception;

    //通过登录名获取账户信息
    @Select("select id,login_name,hash_key,salt from user_back where login_name=#{loginName}")
    UserBack getUserBackByLoginName(String loginName) throws Exception;

    //查找后台用户列表--查询条件登录名或角色，没有排序。
    @SelectProvider(type = UserBackDaoProvider.class,method = "getUserBacks")
    List<DomainUserBack> getUserBacksByNameAndRole(UserBackListRPO rpo) throws Exception ;

    class UserBackDaoProvider{
        public String getUserBacks(UserBackListRPO rpo){
            return new SQL(){{
                SELECT("a.id,a.login_name,c.role,a.create_at,(select login_name from user_back where id=a.create_by) as createBy,a.update_at,(select login_name from user_back where id=a.update_by) as updateBy");
//                FROM("user_back a,role_back c");
//                WHERE("c.id=(select role_id from role_user_back where user_id=a.id)");
                FROM("user_back a");
                INNER_JOIN("role_back c on c.id=(select role_id from role_user_back where user_id=a.id)");
                if (null != rpo.getLoginName()){
                    WHERE("a.login_name like '%"+rpo.getLoginName()+"%'");
                }
                if (null != rpo.getRole()){
//                    WHERE(" a.id in (select user_id from role_user_back where role_id=(select id from role_back where role=#{role}))");
                    WHERE(" a.id in (select user_id from role_user_back x inner join role_back y on x.role_id=y.id where role like '%"+rpo.getRole()+"%')");
                }

//                SELECT("a.id,a.login_name,c.role,a.create_at,b.login_name as createBy,a.update_at,d.login_name as updateBy");
//                FROM("user_back a");
//                INNER_JOIN("user_back b on a.create_by=b.id or a.update_by=b.id");
//                INNER_JOIN("role_back c on c.id=e.role_id");
//                INNER_JOIN("role_user_back e on e.user_id=a.id");
////                INNER_JOIN("user_back d on a.update_by=d.id");
////                INNER_JOIN("role_user_back e on e.role_id=c.id and e.user_id=a.id");
////                WHERE("c.id=(select role_id from role_user_back where user_id=a.id)");
//                if (null != rpo.getLoginName()){
//                    WHERE("a.login_name = #{loginName}");
//                }
//                if (null != rpo.getRole()){
//                    WHERE(" a.id in (select user_id from role_user_back where role_id=(select id from role_back where role=#{role}))");
//                }
            }}.toString();
        }
    }
}
