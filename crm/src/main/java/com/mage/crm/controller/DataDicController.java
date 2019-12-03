package com.mage.crm.controller;

import com.mage.crm.base.BaseController;
import com.mage.crm.service.DataDicService;
import com.mage.crm.vo.DataDic;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@RequestMapping("data_dic")
@Controller
public class DataDicController  extends BaseController{
    @Resource
    private DataDicService dataDicService;

    @RequestMapping("queryDataDicValueByDataDicName")
    @ResponseBody
    public List queryDataDicValueByDataDicName(String dataDicName){
        List<DataDic> list = dataDicService.queryDataDicValueByDataDicName(dataDicName);
        return list;
    }
}
