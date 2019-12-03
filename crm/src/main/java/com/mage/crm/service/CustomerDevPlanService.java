package com.mage.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mage.crm.dao.CustomerDevPlanDao;
import com.mage.crm.dao.SaleChanceDao;
import com.mage.crm.query.CustomerDevPlanQuery;
import com.mage.crm.util.AssertUtil;
import com.mage.crm.vo.CustomerDevPlan;
import com.mage.crm.vo.SaleChance;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CustomerDevPlanService {
    @Resource
    private CustomerDevPlanDao customerDevPlanDao;
    @Resource
    private SaleChanceDao saleChanceDao;
    public Map<String,Object> queryCusDevPlans(CustomerDevPlanQuery customerDevPlanQuery) {
        PageHelper.startPage(customerDevPlanQuery.getPage(),customerDevPlanQuery.getRows());
        List<CustomerDevPlan> customerDevPlanList=customerDevPlanDao.queryCusDevPlansBySaleChanceId(customerDevPlanQuery.getSaleChanceId());
        PageInfo<CustomerDevPlan> customerDevPlanPageInfo = new PageInfo<>(customerDevPlanList);
        Map<String, Object> map = new HashMap<>();
        map.put("total",customerDevPlanPageInfo.getTotal());
        map.put("rows",customerDevPlanPageInfo.getList());
        return map;
    }

    public void insert(CustomerDevPlan customerDevPlan) {
        SaleChance saleChance = saleChanceDao.querySaleChancesById(customerDevPlan.getSaleChanceId());
        AssertUtil.isTrue(saleChance==null,"营销机会不存在了");
        AssertUtil.isTrue(saleChance.getDevResult()==3,"营销机会已经完成了");
        AssertUtil.isTrue(saleChance.getDevResult()==4,"营销机会已经失败了");
        customerDevPlan.setIsValid(1);
        customerDevPlan.setCreateDate(new Date());
        customerDevPlan.setUpdateDate(new Date());
        AssertUtil.isTrue(customerDevPlanDao.insert(customerDevPlan)<1,"添加客户开发计划失败了");
        if (saleChance.getDevResult()==0){//我已经开发过了
            saleChanceDao.updateDevResult(customerDevPlan.getSaleChanceId(),1);
        }
    }

    public void update(CustomerDevPlan customerDevPlan) {
        SaleChance saleChance = saleChanceDao.querySaleChancesById(customerDevPlan.getSaleChanceId());
        AssertUtil.isTrue(saleChance==null,"营销机会不存在了");
        AssertUtil.isTrue(saleChance.getDevResult()==3,"营销机会已经完成了");
        AssertUtil.isTrue(saleChance.getDevResult()==4,"营销机会已经失败了");
        customerDevPlan.setUpdateDate(new Date());
        AssertUtil.isTrue(customerDevPlanDao.update(customerDevPlan)<1,"客户开发计划更新失败");
    }

    public void delete(Integer id) {
        AssertUtil.isTrue(customerDevPlanDao.delete(id)<1,"客户开发计划删除失败");
    }
}
