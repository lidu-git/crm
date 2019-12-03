package com.mage.crm.dao;

import com.mage.crm.vo.UserRole;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserRoleDao {
    int insertBacth(List<UserRole> roleList);

    int deleteUserRolesByUserId(int i);

    int queryUserRoleCountsByUserId(Integer id);
    @Delete("delete from t_user_role where role_id=#{roleId}")
    public Integer deleteUserRolesByRoleId(@Param("roleId") Integer roleId);
    @Select("select count(1) from t_user_role where role_id=#{roleId}")
    public Integer queryUserRoleCountsByRoleId(Integer roleId);
}
