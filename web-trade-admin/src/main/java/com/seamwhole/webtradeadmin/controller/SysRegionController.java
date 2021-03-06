package com.seamwhole.webtradeadmin.controller;


import com.seamwhole.util.PagesInfo;
import com.seamwhole.webtradeadmin.info.SysRegion;
import com.seamwhole.webtradeadmin.info.SysRegionDO;
import com.seamwhole.webtradeadmin.info.SysRegionInfo;
import com.seamwhole.webtradeadmin.service.SysRegionService;
import com.seamwhole.webtradeadmin.util.ResponseObject;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 地址管理Controller
 */
@RestController
@RequestMapping("sys/region")
public class SysRegionController {
    @Autowired
    private SysRegionService sysRegionService;

    /**
     * 查看列表
     * @param params 请求参数
     */
    @RequestMapping("/list")
    @RequiresPermissions("sys:region:list")
    public ResponseObject list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        PagesInfo<SysRegionDO> page=sysRegionService.queryByPage(params);

        return ResponseObject.ok().put("page", page);
    }

    /**
     * 根据主键获取信息
     * @param id 主键
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("sys:region:info")
    public ResponseObject info(@PathVariable("id") Integer id) {
        SysRegion region = sysRegionService.queryObject(id);

        return ResponseObject.ok().put("region", region);
    }

    /**
     * 新增地址
     * @param region 地址
     */
    @RequestMapping("/save")
    @RequiresPermissions("sys:region:save")
    public ResponseObject save(@RequestBody SysRegion region) {
        sysRegionService.save(region);

        return ResponseObject.ok();
    }

    /**
     * 修改地址
     * @param region 地址
     */
    @RequestMapping("/update")
    @RequiresPermissions("sys:region:update")
    public ResponseObject update(@RequestBody SysRegion region) {
        sysRegionService.update(region);

        return ResponseObject.ok();
    }

    /**
     * 删除地址
     * @param ids 主键集
     */
    @RequestMapping("/delete")
    @RequiresPermissions("sys:region:delete")
    public ResponseObject delete(@RequestBody Integer[] ids) {
        sysRegionService.deleteBatch(ids);

        return ResponseObject.ok();
    }

    /**
     * 查询所有国家
     */
    @RequestMapping("/getAllCountry")
    public ResponseObject getAllCountry() {
        List<SysRegionDO> list = sysRegionService.getAllCountry();
        return ResponseObject.ok().put("list", list);
    }

    /**
     * 查询所有省份
     */
    @RequestMapping("/getAllProvice")
    public ResponseObject getAllProvice(@RequestParam(required = false) Integer areaId) {
        List<SysRegionDO> list = sysRegionService.getAllProvinceByParentId(areaId);
        return ResponseObject.ok().put("list", list);
    }

    /**
     * 查询所有城市
     */
    @RequestMapping("/getAllCity")
    public ResponseObject getAllCity(@RequestParam(required = false) Integer areaId) {
        List<SysRegionDO> list = sysRegionService.getChildrenCity(areaId);
        return ResponseObject.ok().put("list", list);
    }


    /**
     * 查询所有区县
     */
    @RequestMapping("/getChildrenDistrict")
    public ResponseObject getChildrenDistrict(@RequestParam(required = false) Integer areaId) {
        List<SysRegionDO> list = sysRegionService.getChildrenDistrict(areaId);
        return ResponseObject.ok().put("list", list);
    }

    /**
     * 查看信息(全部加载页面渲染太慢！)
     */
    @RequestMapping("/getAreaTree")
    public ResponseObject getAreaTree() {
        List<SysRegionInfo> node = sysRegionService.getAreaTree();

        return ResponseObject.ok().put("node", node);
    }

    /**
     * 根据类型获取区域
     * @param type 类型
     */
    @RequestMapping("/getAreaByType")
    public ResponseObject getAreaByType(@RequestParam(required = false) Integer type) {

        List<SysRegionDO> list = new ArrayList<>();
        if (type.equals(0)) {

        } else if (type.equals(1)) {//省份
            list = sysRegionService.getAllCountry();
        } else if (type.equals(2)) {
            list = sysRegionService.getAllProvice();
        } else if (type.equals(3)) {
            list = sysRegionService.getAllCity();
        }
        return ResponseObject.ok().put("list", list);
    }
}
