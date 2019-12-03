package com.mage.crm.controller;

import com.mage.crm.base.BaseController;
import com.mage.crm.dto.ModuleDto;
import com.mage.crm.model.MessageModel;
import com.mage.crm.query.ModuleQuery;
import com.mage.crm.service.ModelService;
import com.mage.crm.vo.Module;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RequestMapping("module")
@Controller
public class ModelController extends BaseController {
    @Resource
    private ModelService modelService;

    @RequestMapping("queryAllsModuleDtos")
    @ResponseBody
    public List<ModuleDto> queryAllsModuleDtos(Integer rid) {

        return modelService.queryAllsModuleDtos(rid);
    }
    @RequestMapping("index")
    public String index(){
        return "module";
    }


    @RequestMapping("queryModulesByParams")
    @ResponseBody
    public Map<String, Object> queryModulesByParams(ModuleQuery moduleQuery){
        return modelService.queryModulesByParams(moduleQuery);
    }


    @RequestMapping("insert")
    @ResponseBody
    public MessageModel insert(Module module){
        modelService.insert(module);
        return createMessageModel("模块记录添加成功!");
    }

    @RequestMapping("update")
    @ResponseBody
    public MessageModel update(Module module){
        modelService.update(module);
        return createMessageModel("模块记录更新成功!");
    }

    @RequestMapping("delete")
    @ResponseBody
    public MessageModel delete(Integer[] ids){
        modelService.delete(ids[0]);
        return createMessageModel("模块记录删除成功!");
    }


    @RequestMapping("queryModulesByGrade")
    @ResponseBody
    public List<Module> queryModulesByGrade(Integer grade){
        return modelService.queryModulesByGrade(grade);

    }
}
