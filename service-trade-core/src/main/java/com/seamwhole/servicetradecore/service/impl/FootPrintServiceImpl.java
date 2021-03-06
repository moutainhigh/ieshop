package com.seamwhole.servicetradecore.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.seamwhole.servicetradecore.mapper.FootPrintMapper;
import com.seamwhole.servicetradecore.mapper.ext.FootPrintExtMapper;
import com.seamwhole.servicetradecore.mapper.model.FootPrintDO;
import com.seamwhole.servicetradecore.mapper.model.ShopFootPrintDO;
import com.seamwhole.servicetradecore.model.FootPrint;
import com.seamwhole.servicetradecore.model.FootPrintExample;
import com.seamwhole.servicetradecore.service.FootPrintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


@Service
public class FootPrintServiceImpl implements FootPrintService {

    @Autowired
    private FootPrintMapper footPrintMapper;

    @Autowired
    private FootPrintExtMapper footPrintExtMapper;


    public FootPrint queryObject(Integer id) {
        return footPrintMapper.selectByPrimaryKey(id);
    }


    public List<FootPrintDO> queryListFootprint(Integer userid) {
    	return footPrintExtMapper.queryListFootprint(userid);
    }

    public List<FootPrintDO> shareList(Map<String, Object> map) {
        return footPrintExtMapper.shareList(map);
    }

    public int queryTotal(Map<String, Object> map) {
        return footPrintExtMapper.queryTotal(map);
    }

    public void save(FootPrint footprint) {
        footPrintMapper.insertSelective(footprint);
    }

    public void deleteByParam(Map<String, Object> map) {
        FootPrintExample example=new FootPrintExample();
        FootPrintExample.Criteria criteria=example.createCriteria();
        if(map.get("userId")!=null)
            criteria.andUserIdEqualTo((int)map.get("userId"));
        if(map.get("goodsId")!=null)
            criteria.andGoodsIdEqualTo((int)map.get("goodsId"));
        footPrintMapper.deleteByExample(example);
    }

    @Override
    public PageInfo<FootPrintDO> queryByPage(Integer userId, Integer pageNum, Integer pageSize) {
        Page<FootPrintDO> page= PageHelper.startPage(pageNum,pageSize);
        footPrintExtMapper.queryListFootprint(userId);
        return page.toPageInfo();
    }

    @Override
    public PageInfo<ShopFootPrintDO> queryShopByPage(Map<String, Object> params, Integer pageNum, Integer pageSize) {
        Page<ShopFootPrintDO> page=PageHelper.startPage(pageNum,pageSize);
        footPrintExtMapper.queryShopFootPrintList(params);
        return page.toPageInfo();
    }

    @Override
    public void updateById(FootPrint footprint) {
        footPrintMapper.updateByPrimaryKeySelective(footprint);
    }

    @Override
    public void deleteBatch(Integer[] ids) {
        FootPrintExample example=new FootPrintExample();
        example.createCriteria().andIdIn(Arrays.asList(ids));
        footPrintMapper.deleteByExample(example);
    }
}
