package com.seamwhole.servicetradecore.mapper.model;

import com.seamwhole.servicetradecore.model.Product;

import java.io.Serializable;

public class ProductDO extends Product implements Serializable {

    private String goodsName;

    private String listPicUrl;

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getListPicUrl() {
        return listPicUrl;
    }

    public void setListPicUrl(String listPicUrl) {
        this.listPicUrl = listPicUrl;
    }
}