package com.seamwhole.serviceerpcore.controller;

import com.alibaba.fastjson.JSONObject;
import com.seamwhole.serviceerpcore.constants.ExceptionConstants;
import com.seamwhole.serviceerpcore.exception.BusinessRunTimeException;
import com.seamwhole.serviceerpcore.service.MaterialPropertyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
@RequestMapping(value = "/materialProperty")
public class MaterialPropertyController {
    private Logger logger = LoggerFactory.getLogger(MaterialPropertyController.class);
    @Resource
    private MaterialPropertyService materialPropertyService;


    /**
     *  批量删除商品扩展信息
     */
    @PostMapping(value = "/batchDeleteMaterialPropertyByIds")
    public Object batchDeleteMaterialPropertyByIds(@RequestParam("ids") String ids) throws Exception {
        JSONObject result = ExceptionConstants.standardSuccess();
        int i= materialPropertyService.batchDeleteMaterialPropertyByIds(ids);
        if(i<1){
            logger.error("异常码[{}],异常提示[{}],参数,ids[{}]",
                    ExceptionConstants.MATERIAL_PROPERTY_DELETE_FAILED_CODE,ExceptionConstants.MATERIAL_PROPERTY_DELETE_FAILED_MSG,ids);
            throw new BusinessRunTimeException(ExceptionConstants.MATERIAL_PROPERTY_DELETE_FAILED_CODE,
                    ExceptionConstants.MATERIAL_PROPERTY_DELETE_FAILED_MSG);
        }
        return result;
    }
}
