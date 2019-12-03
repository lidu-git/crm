package com.mage.crm.dao;

import com.mage.crm.vo.DataDic;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface DataDicDao {
    @Select("SELECT\n" +
            "\tdata_dic_value 'dataDicValue'\n" +
            "FROM\n" +
            "\tt_datadic\n" +
            "WHERE\n" +
            "\tis_valid = 1\n" +
            "AND data_dic_name = #{dataDicName}")
    List<DataDic> queryDataDicValueByDataDicName(String dataDicName);
}
