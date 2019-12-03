package com.mage.crm.dao;

import com.mage.crm.vo.CustomerDevPlan;

import java.util.List;

public interface CustomerDevPlanDao {
    List<CustomerDevPlan> queryCusDevPlansBySaleChanceId(Integer saleChanceId);

    int insert(CustomerDevPlan customerDevPlan);

    int update(CustomerDevPlan customerDevPlan);

    int delete(Integer id);
}
