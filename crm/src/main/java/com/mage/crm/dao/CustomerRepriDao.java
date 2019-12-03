package com.mage.crm.dao;

import com.mage.crm.vo.CustomerReprieve;

import java.util.List;

public interface CustomerRepriDao {
    List<CustomerReprieve> customerReprieveByLossId(String lossId);

    int insert(CustomerReprieve customerReprieve);

    int update(CustomerReprieve customerReprieve);

    int delete(Integer id);
}
