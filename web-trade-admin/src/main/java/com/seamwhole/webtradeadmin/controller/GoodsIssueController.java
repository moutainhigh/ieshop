package com.seamwhole.webtradeadmin.controller;

import com.seamwhole.webtradeadmin.util.ResponseObject;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("goodsissue")
public class GoodsIssueController {
    @Autowired
    private GoodsIssueService goodsIssueService;

    /**
     * 查看列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("goodsissue:list")
    public ResponseObject list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);

        List<GoodsIssueEntity> goodsIssueList = goodsIssueService.queryList(query);
        int total = goodsIssueService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(goodsIssueList, total, query.getLimit(), query.getPage());

        return ResponseObject.ok().put("page", pageUtil);
    }

    /**
     * 查看信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("goodsissue:info")
    public ResponseObject info(@PathVariable("id") Integer id) {
        GoodsIssueEntity goodsIssue = goodsIssueService.queryObject(id);

        return ResponseObject.ok().put("goodsIssue", goodsIssue);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("goodsissue:save")
    public ResponseObject save(@RequestBody GoodsIssueEntity goodsIssue) {
        goodsIssueService.save(goodsIssue);

        return ResponseObject.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("goodsissue:update")
    public ResponseObject update(@RequestBody GoodsIssueEntity goodsIssue) {
        goodsIssueService.update(goodsIssue);

        return ResponseObject.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("goodsissue:delete")
    public ResponseObject delete(@RequestBody Integer[] ids) {
        goodsIssueService.deleteBatch(ids);

        return ResponseObject.ok();
    }

    /**
     * 查看所有列表
     */
    @RequestMapping("/queryAll")
    public ResponseObject queryAll(@RequestParam Map<String, Object> params) {

        List<GoodsIssueEntity> list = goodsIssueService.queryList(params);

        return ResponseObject.ok().put("list", list);
    }
}
