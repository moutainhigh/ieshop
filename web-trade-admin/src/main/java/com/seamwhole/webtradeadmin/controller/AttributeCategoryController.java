package com.seamwhole.webtradeadmin.controller;

import com.seamwhole.webtradeadmin.util.ResponseObject;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/attributecategory")
public class AttributeCategoryController {
    @Autowired
    private AttributeCategoryService attributeCategoryService;

    /**
     * 查看列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("attributecategory:list")
    public ResponseObject list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);

        List<AttributeCategoryEntity> attributeCategoryList = attributeCategoryService.queryList(query);
        int total = attributeCategoryService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(attributeCategoryList, total, query.getLimit(), query.getPage());

        return ResponseObject.ok().put("page", pageUtil);
    }


    /**
     * 查看信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("attributecategory:info")
    public ResponseObject info(@PathVariable("id") Integer id) {
        AttributeCategoryEntity attributeCategory = attributeCategoryService.queryObject(id);

        return ResponseObject.ok().put("attributeCategory", attributeCategory);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("attributecategory:save")
    public ResponseObject save(@RequestBody AttributeCategoryEntity attributeCategory) {
        attributeCategoryService.save(attributeCategory);

        return ResponseObject.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("attributecategory:update")
    public ResponseObject update(@RequestBody AttributeCategoryEntity attributeCategory) {
        attributeCategoryService.update(attributeCategory);

        return ResponseObject.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("attributecategory:delete")
    public ResponseObject delete(@RequestBody Integer[] ids) {
        attributeCategoryService.deleteBatch(ids);

        return ResponseObject.ok();
    }

    /**
     * 查看所有列表
     */
    @RequestMapping("/queryAll")
    public ResponseObject queryAll(@RequestParam Map<String, Object> params) {

        List<AttributeCategoryEntity> list = attributeCategoryService.queryList(params);

        return ResponseObject.ok().put("list", list);
    }
}
