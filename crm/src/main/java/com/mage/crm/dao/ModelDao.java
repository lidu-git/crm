package com.mage.crm.dao;

import com.mage.crm.dto.ModuleDto;
import com.mage.crm.query.ModuleQuery;
import com.mage.crm.vo.Module;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.ui.Model;
import java.util.List;

public interface ModelDao {
    @Select("SELECT m.id,m.module_name as 'name',m.parent_id  as 'pId'from t_module m where is_valid=1")
    List<ModuleDto> queryAllsModuleDtos();
    @Select("select id,module_name as moduleName,opt_value as optValue"
            + " from t_module where id=#{mid} and is_valid=1")
    Module queryModuleById(Integer moduleId);
    public List<Module> queryModulesByParams(ModuleQuery moduleQuery);
    @Insert("insert into t_module(module_name,module_style,url,opt_value,parent_id,grade,"
            + "orders,is_valid,create_date,update_date) values("
            + "#{moduleName},#{moduleStyle},#{url},#{optValue},#{parentId},#{grade},"
            + "#{orders},#{isValid},#{createDate},#{updateDate})")
    public Integer insert(Module module);

    @Update("update t_module set module_name=#{moduleName},module_style=#{moduleStyle},"
            + "url=#{url},opt_value=#{optValue},parent_id=#{parentId},"
            + "grade=#{grade},orders=#{orders},update_date=#{updateDate}"
            + "where id=#{id} and is_valid=1")
    public Integer update(Module module);

    @Select("select  id,module_name as moduleName,url,opt_value as optValue"
            + "  from t_module where opt_value=#{optValue} and is_valid=1")
    public Module queryModuleByOptValue(@Param("optValue") String optValue);

    @Select("select id,module_name as moduleName,url,opt_value as optValue"
            + " from t_module where grade=#{grade} and module_name=#{mname} and is_valid=1" )
    public Module queryModuleByGradeAndModuleName(@Param("grade") Integer grade, @Param("mname") String mname);


    @Select("select id,module_name as moduleName,url,opt_value as optValue"
            + " from t_module where id=#{pid} and is_valid=1" )
    public Module queryModuleByPid(@Param("pid") Integer pid);

    public Integer delete(List<Integer> ids);


    @Select("select id,module_name as moduleName "
            + " from t_module where parent_id=#{pid} and is_valid=1")
    public List<Module> querySubModulesByPid(Integer pid);


    @Select("select id,module_name as moduleName from t_module where is_valid=1 and grade=#{grade}")
    public List<Module> queryModulesByGrade(@Param("grade") Integer grade);

}
