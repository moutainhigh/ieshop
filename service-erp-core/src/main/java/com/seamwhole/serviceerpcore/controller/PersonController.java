package com.seamwhole.serviceerpcore.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.seamwhole.serviceerpcore.constants.BusinessConstants;
import com.seamwhole.serviceerpcore.constants.ExceptionConstants;
import com.seamwhole.serviceerpcore.model.Person;
import com.seamwhole.serviceerpcore.exception.BusinessRunTimeException;
import com.seamwhole.serviceerpcore.service.PersonService;
import com.seamwhole.serviceerpcore.utils.BaseResponseInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(value = "/person")
public class PersonController {
    private Logger logger = LoggerFactory.getLogger(PersonController.class);

    @Resource
    private PersonService personService;

    @GetMapping(value = "/getAllList")
    public BaseResponseInfo getAllList()throws Exception {
        BaseResponseInfo res = new BaseResponseInfo();
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            List<Person> personList = personService.getPerson();
            map.put("personList", personList);
            res.code = 200;
            res.data = personList;
        } catch(Exception e){
            e.printStackTrace();
            res.code = 500;
            res.data = "获取数据失败";
        }
        return res;
    }

    /**
     * 根据Id获取经手人信息
     * @param personIDs
     * @return
     */
    @GetMapping(value = "/getPersonByIds")
    public BaseResponseInfo getPersonByIds(@RequestParam("personIDs") String personIDs)throws Exception {
        BaseResponseInfo res = new BaseResponseInfo();
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String names = personService.getPersonByIds(personIDs);
            map.put("names", names);
            res.code = 200;
            res.data = map;
        } catch(Exception e){
            e.printStackTrace();
            res.code = 500;
            res.data = "获取数据失败";
        }
        return res;
    }

    /**
     * 根据类型获取经手人信息
     * @param type
     * @return
     */
    @GetMapping(value = "/getPersonByType")
    public BaseResponseInfo getPersonByType(@RequestParam("type") String type)throws Exception {
        BaseResponseInfo res = new BaseResponseInfo();
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            List<Person> personList = personService.getPersonByType(type);
            map.put("personList", personList);
            res.code = 200;
            res.data = map;
        } catch(Exception e){
            e.printStackTrace();
            res.code = 500;
            res.data = "获取数据失败";
        }
        return res;
    }

    /**
     * 根据类型获取经手人信息 1-业务员，2-仓管员，3-财务员
     * @param typeNum
     * @return
     */
    @PostMapping(value = "/getPersonByNumType")
    public JSONArray getPersonByNumType(@RequestParam("type") String typeNum)throws Exception {
        JSONArray dataArray = new JSONArray();
        try {
            String type = "";
            if (typeNum.equals("1")) {
                type = "业务员";
            } else if (typeNum.equals("2")) {
                type = "仓管员";
            } else if (typeNum.equals("3")) {
                type = "财务员";
            }
            List<Person> personList = personService.getPersonByType(type);
            if (null != personList) {
                for (Person person : personList) {
                    JSONObject item = new JSONObject();
                    item.put("id", person.getId());
                    item.put("name", person.getName());
                    dataArray.add(item);
                }
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        return dataArray;
    }
    /**
     *  批量删除经手人信息
     * @Param: ids
     * @return java.lang.Object
     */
    @PostMapping(value = "/batchDeletePersonByIds")
    public Object batchDeletePersonByIds(@RequestParam("ids") String ids,
                                         @RequestParam(value="deleteType", required =false,defaultValue= BusinessConstants.DELETE_TYPE_NORMAL)
                                                 String deleteType) throws Exception {
        JSONObject result = ExceptionConstants.standardSuccess();
        int i=0;
        if(BusinessConstants.DELETE_TYPE_NORMAL.equals(deleteType)){
            i= personService.batchDeletePersonByIdsNormal(ids);
        }else if(BusinessConstants.DELETE_TYPE_FORCE.equals(deleteType)){
            i= personService.batchDeletePersonByIds(ids);
        }else{
            logger.error("异常码[{}],异常提示[{}],参数,ids[{}],deleteType[{}]",
                    ExceptionConstants.DELETE_REFUSED_CODE,ExceptionConstants.DELETE_REFUSED_MSG,ids,deleteType);
            throw new BusinessRunTimeException(ExceptionConstants.DELETE_REFUSED_CODE,
                    ExceptionConstants.DELETE_REFUSED_MSG);
        }
        if(i<1){
            logger.error("异常码[{}],异常提示[{}],参数,ids[{}]",
                    ExceptionConstants.PERSON_DELETE_FAILED_CODE,ExceptionConstants.PERSON_DELETE_FAILED_MSG,ids);
            throw new BusinessRunTimeException(ExceptionConstants.PERSON_DELETE_FAILED_CODE,
                    ExceptionConstants.PERSON_DELETE_FAILED_MSG);
        }
        return result;
    }
}
