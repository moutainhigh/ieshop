package com.seamwhole.serviceerpcore.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.seamwhole.serviceerpcore.constants.BusinessConstants;
import com.seamwhole.serviceerpcore.constants.ExceptionConstants;
import com.seamwhole.serviceerpcore.model.MaterialCategory;
import com.seamwhole.serviceerpcore.mapper.vo.TreeNode;
import com.seamwhole.serviceerpcore.exception.BusinessRunTimeException;
import com.seamwhole.serviceerpcore.service.MaterialCategoryService;
import com.seamwhole.serviceerpcore.utils.BaseResponseInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author ji—sheng—hua   华夏ERP
 */
@RestController
@RequestMapping(value = "/materialCategory")
public class MaterialCategoryController {
    private Logger logger = LoggerFactory.getLogger(MaterialCategoryController.class);

    @Resource
    private MaterialCategoryService materialCategoryService;

    @GetMapping(value = "/getAllList")
    public BaseResponseInfo getAllList(@RequestParam("parentId") Long parentId) throws Exception{
        BaseResponseInfo res = new BaseResponseInfo();
        try {
            List<MaterialCategory> materialCategoryList = materialCategoryService.getAllList(parentId);
            res.code = 200;
            res.data = materialCategoryList;
        } catch(Exception e){
            e.printStackTrace();
            res.code = 500;
            res.data = "获取数据失败";
        }
        return res;
    }

    /**
     * 根据id来查询商品名称
     * @param id
     * @param request
     * @return
     */
    @GetMapping(value = "/findById")
    public BaseResponseInfo findById(@RequestParam("id") Long id)throws Exception {
        BaseResponseInfo res = new BaseResponseInfo();
        try {
            List<MaterialCategory> dataList = materialCategoryService.findById(id);
            JSONObject outer = new JSONObject();
            if (null != dataList) {
                for (MaterialCategory mc : dataList) {
                    outer.put("id", mc.getId());
                    outer.put("name", mc.getName());
                    outer.put("parentId", mc.getParentid());
                    List<MaterialCategory> dataParentList = materialCategoryService.findById(mc.getParentid());
                    if(dataParentList!=null&&dataParentList.size()>0){
                        outer.put("parentName", dataParentList.get(0).getName());
                    }
                    outer.put("sort", mc.getSort());
                    outer.put("serialNo", mc.getSerialNo());
                    outer.put("remark", mc.getRemark());
                }
            }
            res.code = 200;
            res.data = outer;
        } catch(Exception e){
            e.printStackTrace();
            res.code = 500;
            res.data = "获取数据失败";
        }
        return res;
    }
    /**
     * 获取商品类别树数据
     * @Param:
     * @return com.alibaba.fastjson.JSONArray
     */
    @GetMapping(value = "/getMaterialCategoryTree")
    public JSONArray getMaterialCategoryTree(@RequestParam("id") Long id) throws Exception{
       JSONArray arr=new JSONArray();
       List<TreeNode> materialCategoryTree = materialCategoryService.getMaterialCategoryTree(id);
       if(materialCategoryTree!=null&&materialCategoryTree.size()>0){
           for(TreeNode node:materialCategoryTree){
               String str=JSON.toJSONString(node);
               JSONObject obj=JSON.parseObject(str);
               arr.add(obj) ;
           }
       }
        return arr;
    }
    /**
     *  新增商品类别数据
     * @Param: beanJson
     * @return java.lang.Object
     */
    @PostMapping(value = "/addMaterialCategory")
    public Object addMaterialCategory(@RequestParam("info") String beanJson) throws Exception {
        JSONObject result = ExceptionConstants.standardSuccess();
        MaterialCategory mc= JSON.parseObject(beanJson, MaterialCategory.class);
        int i= materialCategoryService.addMaterialCategory(mc);
        if(i<1){
            throw new BusinessRunTimeException(ExceptionConstants.MATERIAL_CATEGORY_ADD_FAILED_CODE,
                    ExceptionConstants.MATERIAL_CATEGORY_ADD_FAILED_MSG);
        }
        return result;
    }

    /**
     *  修改商品类别数据
     * @Param: beanJson
     * @return java.lang.Object
     */
    @PostMapping(value = "/editMaterialCategory")
    public Object editMaterialCategory(@RequestParam("info") String beanJson) throws Exception {
        JSONObject result = ExceptionConstants.standardSuccess();
        MaterialCategory mc= JSON.parseObject(beanJson, MaterialCategory.class);
        int i= materialCategoryService.editMaterialCategory(mc);
        if(i<1){
            throw new BusinessRunTimeException(ExceptionConstants.MATERIAL_CATEGORY_EDIT_FAILED_CODE,
                    ExceptionConstants.MATERIAL_CATEGORY_EDIT_FAILED_MSG);
        }
        return result;
    }

    /**
     *  批量删除商品类别信息
     * @Param: ids
     * @return java.lang.Object
     */
    @PostMapping(value = "/batchDeleteMaterialCategory")
    public Object batchDeleteMaterialCategory(@RequestParam("ids") String ids,
                                              @RequestParam(value="deleteType", required =false,defaultValue= BusinessConstants.DELETE_TYPE_NORMAL)
                                                      String deleteType) throws Exception {
        JSONObject result = ExceptionConstants.standardSuccess();
        int i=0;
        if(BusinessConstants.DELETE_TYPE_NORMAL.equals(deleteType)){
            i= materialCategoryService.batchDeleteMaterialCategoryByIdsNormal(ids);
        }else if(BusinessConstants.DELETE_TYPE_FORCE.equals(deleteType)){
            i= materialCategoryService.batchDeleteMaterialCategoryByIds(ids);
        }else{
            logger.error("异常码[{}],异常提示[{}],参数,ids[{}],deleteType[{}]",
                    ExceptionConstants.DELETE_REFUSED_CODE,ExceptionConstants.DELETE_REFUSED_MSG,ids,deleteType);
            throw new BusinessRunTimeException(ExceptionConstants.DELETE_REFUSED_CODE,
                    ExceptionConstants.DELETE_REFUSED_MSG);
        }
        if(i<1){
            throw new BusinessRunTimeException(ExceptionConstants.MATERIAL_CATEGORY_DELETE_FAILED_CODE,
                    ExceptionConstants.MATERIAL_CATEGORY_DELETE_FAILED_MSG);
        }
        return result;
    }
}
