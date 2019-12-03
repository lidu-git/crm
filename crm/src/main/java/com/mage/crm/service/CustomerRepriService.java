package com.mage.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mage.crm.dao.CustomerRepriDao;
import com.mage.crm.query.CustomerRepriQuery;
import com.mage.crm.util.AssertUtil;
import com.mage.crm.vo.CustomerReprieve;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CustomerRepriService {
    @Resource
    private CustomerRepriDao customerRepriDao;
    @Resource
    private  CustomerLossService customerLossService;

    public Map<String,Object> customerReprieveByLossId(CustomerRepriQuery customerRepriQuery) {
        PageHelper.startPage(customerRepriQuery.getPage(), customerRepriQuery.getRows());
        List<CustomerReprieve> customerReprieveList = customerRepriDao.customerReprieveByLossId(customerRepriQuery.getLossId());
        PageInfo<CustomerReprieve> customerReprievePageInfo = new PageInfo<>(customerReprieveList);
        Map<String, Object> map = new HashMap<>();
        map.put("total", customerReprievePageInfo.getTotal());
        map.put("rows", customerReprievePageInfo.getList());
        return map;
    }
    /**
     * 1.流失ID 暂缓措施 必须存在
     * 2.添加 额外参数
     * @param customerReprieve
     */

    public void insert(CustomerReprieve customerReprieve){
        checkParams(customerReprieve.getLossId(),customerReprieve.getMeasure());
        customerReprieve.setIsValid(1);
        customerReprieve.setCreateDate(new Date());
        customerReprieve.setUpdateDate(new Date());
        AssertUtil.isTrue(customerRepriDao.insert(customerReprieve)<1,"暂缓措施添加失败");

    }

    public void checkParams(Integer lossId,String measure){
        AssertUtil.isTrue(StringUtils.isBlank(measure),"暂缓措施不能为空");
        Map<String,Object> map = customerLossService.queryCustomerLossesById(lossId);
        AssertUtil.isTrue(lossId==null||map==null||map.isEmpty(),"流失记录不存在");
    }
    public void update(CustomerReprieve customerReprieve) {
        checkParams(customerReprieve.getLossId(),customerReprieve.getMeasure());
        customerReprieve.setUpdateDate(new Date());
        AssertUtil.isTrue(customerRepriDao.update(customerReprieve)<1,"暂缓措施修改失败");
    }
    public void delete(Integer id){
        AssertUtil.isTrue(customerRepriDao.delete(id)<1,"删除暂缓措施失败");
    }
}
