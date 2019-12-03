package com.mage.crm.task;

import com.mage.crm.service.CustomerService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class CustomerLossTask {
    @Resource
    private CustomerService customerService;
    /*@Scheduled(cron = "0 04 10 * * ?")//秒 分 时 天 月  星期
    public void customerLoss(){
        customerService.updateCustomerLossState();
        System.out.println("我已经被触发了");
    }*/
}
