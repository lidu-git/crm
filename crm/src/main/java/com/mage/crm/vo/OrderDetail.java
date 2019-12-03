package com.mage.crm.vo;

import com.mage.crm.base.BaseVo;

public class OrderDetail extends BaseVo{
    private Integer id;

    private Integer orderId;

    private String goodsName;

    private Integer goodsNum;

    private String unit;

    private Float price;

    private Float sum;

    private Integer isValid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public Integer getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(Integer goodsNum) {
        this.goodsNum = goodsNum;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Float getSum() {
        return sum;
    }

    public void setSum(Float sum) {
        this.sum = sum;
    }

    @Override
    public Integer getIsValid() {
        return isValid;
    }

    @Override
    public void setIsValid(Integer isValid) {
        this.isValid = isValid;
    }
}
