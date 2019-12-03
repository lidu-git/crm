package com.mage.crm.controller;

import com.mage.crm.base.BaseController;
import com.mage.crm.model.MessageModel;
import com.mage.crm.query.CustomerDevPlanQuery;
import com.mage.crm.service.CustomerDevPlanService;
import com.mage.crm.service.SaleChanceService;
import com.mage.crm.vo.CustomerDevPlan;
import com.mage.crm.vo.SaleChance;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

@Controller
@RequestMapping("cus_dev_plan")
public class CustomerDevPlanController extends BaseController {
    @Resource
    private CustomerDevPlanService customerDevPlanService;
    @Resource
    private SaleChanceService saleChanceService;
    @RequestMapping("index")
    public String index(Integer id, Model model){
        SaleChance saleChance=saleChanceService.querySaleChancesById(id);
        model.addAttribute("saleChance",saleChance);
        return "cus_dev_plan_detail";
    }

    @ResponseBody
    @RequestMapping("queryCusDevPlans")
    public Map<String,Object> queryCusDevPlans(CustomerDevPlanQuery customerDevPlanQuery){
        return  customerDevPlanService.queryCusDevPlans(customerDevPlanQuery);
    }
    @ResponseBody
    @RequestMapping("insert")
    public void save(CustomerDevPlan customerDevPlan){
        customerDevPlanService.insert(customerDevPlan);
    }
    @ResponseBody
    @RequestMapping("update")
    public MessageModel update(CustomerDevPlan customerDevPlan){
        customerDevPlanService.update(customerDevPlan);
        return createMessageModel("客户开发计划更新成功");
    }

    @ResponseBody
    @RequestMapping("delete")
    public MessageModel delete(Integer id){
        customerDevPlanService.delete(id);
        return createMessageModel("客户开发计划删除成功");
    }
}
