package com.seamwhole.serviceerpcore.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.seamwhole.serviceerpcore.constants.BusinessConstants;
import com.seamwhole.serviceerpcore.constants.ExceptionConstants;
import com.seamwhole.serviceerpcore.model.Account;
import com.seamwhole.serviceerpcore.mapper.vo.AccountVo4InOutList;
import com.seamwhole.serviceerpcore.exception.BusinessRunTimeException;
import com.seamwhole.serviceerpcore.service.AccountService;
import com.seamwhole.serviceerpcore.utils.BaseResponseInfo;
import com.seamwhole.serviceerpcore.utils.ErpInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.seamwhole.serviceerpcore.utils.ResponseJsonUtil.returnJson;


@RestController
@RequestMapping(value = "/account")
public class AccountController {
    private Logger logger = LoggerFactory.getLogger(AccountController.class);

    @Resource
    private AccountService accountService;

    /**
     * 查找结算账户信息-下拉框
     * @return
     */
    @GetMapping(value = "/findBySelect")
    public String findBySelect() throws Exception {
        String res = null;
        try {
            List<Account> dataList = accountService.findBySelect();
            //存放数据json数组
            JSONArray dataArray = new JSONArray();
            if (null != dataList) {
                for (Account account : dataList) {
                    JSONObject item = new JSONObject();
                    item.put("Id", account.getId());
                    //结算账户名称
                    item.put("AccountName", account.getName());
                    dataArray.add(item);
                }
            }
            res = dataArray.toJSONString();
        } catch(Exception e){
            e.printStackTrace();
            res = "获取数据失败";
        }
        return res;
    }

    /**
     * 获取所有结算账户
     * @return
     */
    @GetMapping(value = "/getAccount")
    public BaseResponseInfo getAccount() throws Exception {
        BaseResponseInfo res = new BaseResponseInfo();
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            List<Account> accountList = accountService.getAccount();
            map.put("accountList", accountList);
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
     * 账户流水信息
     * @param currentPage
     * @param pageSize
     * @param accountId
     * @param initialAmount
     * @return
     */
    @GetMapping(value = "/findAccountInOutList/{currentPage}/{pageSize}/{accountId}/{initialAmount}")
    public BaseResponseInfo findAccountInOutList(@PathVariable("currentPage") Integer currentPage,
                                                 @PathVariable("pageSize") Integer pageSize,
                                                 @PathVariable("accountId") Long accountId,
                                                 @PathVariable("initialAmount") BigDecimal initialAmount) throws Exception{
        BaseResponseInfo res = new BaseResponseInfo();
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            List<AccountVo4InOutList> dataList = accountService.findAccountInOutList(accountId, (currentPage-1)*pageSize, pageSize);
            int total = accountService.findAccountInOutListCount(accountId);
            map.put("total", total);
            //存放数据json数组
            JSONArray dataArray = new JSONArray();
            if (null != dataList) {
                for (AccountVo4InOutList aEx : dataList) {
                    String timeStr = aEx.getOperTime().toString();
                    BigDecimal balance = accountService.getAccountSum(accountId, timeStr, "date").add(accountService.getAccountSumByHead(accountId, timeStr, "date"))
                            .add(accountService.getAccountSumByDetail(accountId, timeStr, "date")).add(accountService.getManyAccountSum(accountId, timeStr, "date")).add(initialAmount);
                    aEx.setBalance(balance);
                    dataArray.add(aEx);
                }
            }
            map.put("rows", dataArray);
            res.code = 200;
            res.data = map;
        } catch(Exception e){
            e.printStackTrace();
            res.code = 500;
            res.data = "获取数据失败";
        }
        return res;
    }


    @PostMapping(value = "/updateAmountIsDefault")
    public String updateAmountIsDefault(@RequestParam("isDefault") Boolean isDefault,
                                 @RequestParam("accountId") Long accountId) throws Exception{
        Map<String, Object> objectMap = new HashMap<String, Object>();
        int res = accountService.updateAmountIsDefault(isDefault, accountId);
        if(res > 0) {
            return returnJson(objectMap, ErpInfo.OK.name, ErpInfo.OK.code);
        } else {
            return returnJson(objectMap, ErpInfo.ERROR.name, ErpInfo.ERROR.code);
        }
    }

    
    /**
     *  批量删除账户信息
     */
    @PostMapping(value = "/batchDeleteAccountByIds")
    public Object batchDeleteAccountByIds(@RequestParam("ids") String ids, @RequestParam(value="deleteType",
            required =false,defaultValue=BusinessConstants.DELETE_TYPE_NORMAL)String deleteType) throws Exception {

        JSONObject result = ExceptionConstants.standardSuccess();

        /**
         *  出于兼容性考虑，没有传递删除类型时，默认为正常删除
         */
        int i=0;
        if(BusinessConstants.DELETE_TYPE_NORMAL.equals(deleteType)){
             i= accountService.batchDeleteAccountByIdsNormal(ids);
        }else if(BusinessConstants.DELETE_TYPE_FORCE.equals(deleteType)){
             i= accountService.batchDeleteAccountByIds(ids);
        }else{
            logger.error("异常码[{}],异常提示[{}],参数,ids[{}],deleteType[{}]",
                    ExceptionConstants.DELETE_REFUSED_CODE,ExceptionConstants.DELETE_REFUSED_MSG,ids,deleteType);
            throw new BusinessRunTimeException(ExceptionConstants.DELETE_REFUSED_CODE,
                    ExceptionConstants.DELETE_REFUSED_MSG);
        }
        if(i<1){
            logger.error("异常码[{}],异常提示[{}],参数,ids[{}]",
                    ExceptionConstants.ACCOUNT_DELETE_FAILED_CODE,ExceptionConstants.ACCOUNT_DELETE_FAILED_MSG,ids);
            throw new BusinessRunTimeException(ExceptionConstants.ACCOUNT_DELETE_FAILED_CODE,
                    ExceptionConstants.ACCOUNT_DELETE_FAILED_MSG);
        }
        return result;
    }

}
