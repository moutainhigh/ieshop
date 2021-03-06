package com.seamwhole.serviceerpcore.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.seamwhole.serviceerpcore.constants.BusinessConstants;
import com.seamwhole.serviceerpcore.constants.ExceptionConstants;
import com.seamwhole.serviceerpcore.model.AccountItem;
import com.seamwhole.serviceerpcore.model.AccountItemExample;
import com.seamwhole.serviceerpcore.model.User;
import com.seamwhole.serviceerpcore.mapper.AccountItemMapper;
import com.seamwhole.serviceerpcore.mapper.ext.AccountItemExtMapper;
import com.seamwhole.serviceerpcore.mapper.vo.AccountItemVo4List;
import com.seamwhole.serviceerpcore.exception.BusinessRunTimeException;
import com.seamwhole.serviceerpcore.service.AccountItemService;
import com.seamwhole.serviceerpcore.service.LogService;
import com.seamwhole.serviceerpcore.service.UserService;
import com.seamwhole.serviceerpcore.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class AccountItemServiceImpl implements AccountItemService {
    private Logger logger = LoggerFactory.getLogger(AccountItemServiceImpl.class);

    @Resource
    private AccountItemMapper accountItemMapper;

    @Resource
    private AccountItemExtMapper accountItemMapperEx;
    @Resource
    private LogService logService;
    @Resource
    private UserService userService;

    public AccountItem getAccountItem(long id)throws Exception {
        AccountItem result=null;
        try{
            result=accountItemMapper.selectByPrimaryKey(id);
        }catch(Exception e){
            logger.error("异常码[{}],异常提示[{}],异常[{}]",
                    ExceptionConstants.DATA_READ_FAIL_CODE,ExceptionConstants.DATA_READ_FAIL_MSG,e);
            throw new BusinessRunTimeException(ExceptionConstants.DATA_READ_FAIL_CODE,
                    ExceptionConstants.DATA_READ_FAIL_MSG);
        }
        return result;
    }

    public List<AccountItem> getAccountItem()throws Exception {
        AccountItemExample example = new AccountItemExample();
        example.createCriteria().andDeleteFlagNotEqualTo(BusinessConstants.DELETE_FLAG_DELETED);
        List<AccountItem> list=null;
        try{
            list=accountItemMapper.selectByExample(example);
        }catch(Exception e){
            logger.error("异常码[{}],异常提示[{}],异常[{}]",
                    ExceptionConstants.DATA_READ_FAIL_CODE,ExceptionConstants.DATA_READ_FAIL_MSG,e);
            throw new BusinessRunTimeException(ExceptionConstants.DATA_READ_FAIL_CODE,
                    ExceptionConstants.DATA_READ_FAIL_MSG);
        }
        return list;
    }

    public List<AccountItem> select(String name, Integer type, String remark, int offset, int rows)throws Exception {
        List<AccountItem> list=null;
        try{
            list = accountItemMapperEx.selectByConditionAccountItem(name, type, remark, offset, rows);
        }catch(Exception e){
            logger.error("异常码[{}],异常提示[{}],异常[{}]",
                    ExceptionConstants.DATA_READ_FAIL_CODE,ExceptionConstants.DATA_READ_FAIL_MSG,e);
            throw new BusinessRunTimeException(ExceptionConstants.DATA_READ_FAIL_CODE,
                    ExceptionConstants.DATA_READ_FAIL_MSG);
        }
        return list;
    }

    public Long countAccountItem(String name, Integer type, String remark)throws Exception {
        Long result=null;
        try{
            result = accountItemMapperEx.countsByAccountItem(name, type, remark);
        }catch(Exception e){
            logger.error("异常码[{}],异常提示[{}],异常[{}]",
                    ExceptionConstants.DATA_READ_FAIL_CODE,ExceptionConstants.DATA_READ_FAIL_MSG,e);
            throw new BusinessRunTimeException(ExceptionConstants.DATA_READ_FAIL_CODE,
                    ExceptionConstants.DATA_READ_FAIL_MSG);
        }
        return result;
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int insertAccountItem(String beanJson, HttpServletRequest request) throws Exception{
        AccountItem accountItem = JSONObject.parseObject(beanJson, AccountItem.class);
        int result=0;
        try{
            result = accountItemMapper.insertSelective(accountItem);
        }catch(Exception e){
            logger.error("异常码[{}],异常提示[{}],异常[{}]",
                    ExceptionConstants.DATA_WRITE_FAIL_CODE,ExceptionConstants.DATA_WRITE_FAIL_MSG,e);
            throw new BusinessRunTimeException(ExceptionConstants.DATA_WRITE_FAIL_CODE,
                    ExceptionConstants.DATA_WRITE_FAIL_MSG);
        }
        return result;
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int updateAccountItem(String beanJson, Long id)throws Exception {
        AccountItem accountItem = JSONObject.parseObject(beanJson, AccountItem.class);
        accountItem.setId(id);
        int result=0;
        try{
            result = accountItemMapper.updateByPrimaryKeySelective(accountItem);
        }catch(Exception e){
            logger.error("异常码[{}],异常提示[{}],异常[{}]",
                    ExceptionConstants.DATA_WRITE_FAIL_CODE,ExceptionConstants.DATA_WRITE_FAIL_MSG,e);
            throw new BusinessRunTimeException(ExceptionConstants.DATA_WRITE_FAIL_CODE,
                    ExceptionConstants.DATA_WRITE_FAIL_MSG);
        }
        return result;
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int deleteAccountItem(Long id)throws Exception {
        int result=0;
        try{
            result = accountItemMapper.deleteByPrimaryKey(id);
        }catch(Exception e){
            logger.error("异常码[{}],异常提示[{}],异常[{}]",
                    ExceptionConstants.DATA_WRITE_FAIL_CODE,ExceptionConstants.DATA_WRITE_FAIL_MSG,e);
            throw new BusinessRunTimeException(ExceptionConstants.DATA_WRITE_FAIL_CODE,
                    ExceptionConstants.DATA_WRITE_FAIL_MSG);
        }
        return result;
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int batchDeleteAccountItem(String ids)throws Exception {
        List<Long> idList = StringUtil.strToLongList(ids);
        AccountItemExample example = new AccountItemExample();
        example.createCriteria().andIdIn(idList);
        int result=0;
        try{
            result = accountItemMapper.deleteByExample(example);
        }catch(Exception e){
            logger.error("异常码[{}],异常提示[{}],异常[{}]",
                    ExceptionConstants.DATA_WRITE_FAIL_CODE,ExceptionConstants.DATA_WRITE_FAIL_MSG,e);
            throw new BusinessRunTimeException(ExceptionConstants.DATA_WRITE_FAIL_CODE,
                    ExceptionConstants.DATA_WRITE_FAIL_MSG);
        }
        return result;
    }

    public int checkIsNameExist(Long id, String name)throws Exception {
        AccountItemExample example = new AccountItemExample();
        example.createCriteria().andIdNotEqualTo(id).andDeleteFlagNotEqualTo(BusinessConstants.DELETE_FLAG_DELETED);
        List<AccountItem> list = null;
        try{
            list = accountItemMapper.selectByExample(example);
        }catch(Exception e){
            logger.error("异常码[{}],异常提示[{}],异常[{}]",
                    ExceptionConstants.DATA_READ_FAIL_CODE,ExceptionConstants.DATA_READ_FAIL_MSG,e);
            throw new BusinessRunTimeException(ExceptionConstants.DATA_READ_FAIL_CODE,
                    ExceptionConstants.DATA_READ_FAIL_MSG);
        }
        return list==null?0:list.size();
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int insertAccountItemWithObj(AccountItem accountItem)throws Exception {
        int result=0;
        try{
            result = accountItemMapper.insertSelective(accountItem);
        }catch(Exception e){
            logger.error("异常码[{}],异常提示[{}],异常[{}]",
                    ExceptionConstants.DATA_WRITE_FAIL_CODE,ExceptionConstants.DATA_WRITE_FAIL_MSG,e);
            throw new BusinessRunTimeException(ExceptionConstants.DATA_WRITE_FAIL_CODE,
                    ExceptionConstants.DATA_WRITE_FAIL_MSG);
        }
        return result;
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int updateAccountItemWithObj(AccountItem accountItem)throws Exception {
        int result=0;
        try{
            result = accountItemMapper.updateByPrimaryKeySelective(accountItem);
        }catch(Exception e){
            logger.error("异常码[{}],异常提示[{}],异常[{}]",
                    ExceptionConstants.DATA_WRITE_FAIL_CODE,ExceptionConstants.DATA_WRITE_FAIL_MSG,e);
            throw new BusinessRunTimeException(ExceptionConstants.DATA_WRITE_FAIL_CODE,
                    ExceptionConstants.DATA_WRITE_FAIL_MSG);
        }
        return result;
    }

    public List<AccountItemVo4List> getDetailList(Long headerId) {
        List<AccountItemVo4List> list=null;
        try{
            list = accountItemMapperEx.getDetailList(headerId);
        }catch(Exception e){
            logger.error("异常码[{}],异常提示[{}],异常[{}]",
                    ExceptionConstants.DATA_READ_FAIL_CODE,ExceptionConstants.DATA_READ_FAIL_MSG,e);
            throw new BusinessRunTimeException(ExceptionConstants.DATA_READ_FAIL_CODE,
                    ExceptionConstants.DATA_READ_FAIL_MSG);
        }
        return list;
    }
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public String saveDetials(String inserted, String deleted, String updated, Long headerId, String listType) throws Exception {
        logService.insertLog(BusinessConstants.LOG_INTERFACE_NAME_ACCOUNT_ITEM,
                new StringBuffer(BusinessConstants.LOG_OPERATION_TYPE_EDIT).append(",headerId:").append(headerId).toString(),
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest());
        //转为json
            JSONArray insertedJson = JSONArray.parseArray(inserted);
            JSONArray deletedJson = JSONArray.parseArray(deleted);
            JSONArray updatedJson = JSONArray.parseArray(updated);
            if (null != insertedJson) {
                for (int i = 0; i < insertedJson.size(); i++) {
                    AccountItem accountItem = new AccountItem();
                    JSONObject tempInsertedJson = JSONObject.parseObject(insertedJson.getString(i));
                    accountItem.setHeaderid(headerId);
                    if (tempInsertedJson.get("AccountId") != null && !tempInsertedJson.get("AccountId").equals("")) {
                        accountItem.setAccountid(tempInsertedJson.getLong("AccountId"));
                    }
                    if (tempInsertedJson.get("InOutItemId") != null && !tempInsertedJson.get("InOutItemId").equals("")) {
                        accountItem.setInoutitemid(tempInsertedJson.getLong("InOutItemId"));
                    }
                    if (tempInsertedJson.get("EachAmount") != null && !tempInsertedJson.get("EachAmount").equals("")) {
                        BigDecimal eachAmount = tempInsertedJson.getBigDecimal("EachAmount");
                        if (listType.equals("付款")) {
                            eachAmount = BigDecimal.ZERO.subtract(eachAmount);
                        }
                        accountItem.setEachamount(eachAmount);
                    } else {
                        accountItem.setEachamount(BigDecimal.ZERO);
                    }
                    accountItem.setRemark(tempInsertedJson.getString("Remark"));
                    this.insertAccountItemWithObj(accountItem);
                }
            }
            if (null != deletedJson) {
                StringBuffer bf=new StringBuffer();
                for (int i = 0; i < deletedJson.size(); i++) {
                    JSONObject tempDeletedJson = JSONObject.parseObject(deletedJson.getString(i));
                    bf.append(tempDeletedJson.getLong("Id"));
                    if(i<(deletedJson.size()-1)){
                        bf.append(",");
                    }

                }
                this.batchDeleteAccountItemByIds(bf.toString());
            }
            if (null != updatedJson) {
                for (int i = 0; i < updatedJson.size(); i++) {
                    JSONObject tempUpdatedJson = JSONObject.parseObject(updatedJson.getString(i));
                    AccountItem accountItem = this.getAccountItem(tempUpdatedJson.getLong("Id"));
                    accountItem.setId(tempUpdatedJson.getLong("Id"));
                    accountItem.setHeaderid(headerId);
                    if (tempUpdatedJson.get("AccountId") != null && !tempUpdatedJson.get("AccountId").equals("")) {
                        accountItem.setAccountid(tempUpdatedJson.getLong("AccountId"));
                    }
                    if (tempUpdatedJson.get("InOutItemId") != null && !tempUpdatedJson.get("InOutItemId").equals("")) {
                        accountItem.setInoutitemid(tempUpdatedJson.getLong("InOutItemId"));
                    }
                    if (tempUpdatedJson.get("EachAmount") != null && !tempUpdatedJson.get("EachAmount").equals("")) {
                        BigDecimal eachAmount = tempUpdatedJson.getBigDecimal("EachAmount");
                        if (listType.equals("付款")) {
                            eachAmount = BigDecimal.ZERO.subtract(eachAmount);
                        }
                        accountItem.setEachamount(eachAmount);
                    } else {
                        accountItem.setEachamount(BigDecimal.ZERO);
                    }
                    accountItem.setRemark(tempUpdatedJson.getString("Remark"));
                    this.updateAccountItemWithObj(accountItem);
                }
            }

        return null;
    }
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int batchDeleteAccountItemByIds(String ids) throws Exception{
        logService.insertLog(BusinessConstants.LOG_INTERFACE_NAME_ACCOUNT_ITEM,
                new StringBuffer(BusinessConstants.LOG_OPERATION_TYPE_DELETE).append(ids).toString(),
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest());
        User userInfo=userService.getCurrentUser();
        String [] idArray=ids.split(",");
        int result=0;
        try{
            result = accountItemMapperEx.batchDeleteAccountItemByIds(new Date(),userInfo==null?null:userInfo.getId(),idArray);
        }catch(Exception e){
            logger.error("异常码[{}],异常提示[{}],异常[{}]",
                    ExceptionConstants.DATA_WRITE_FAIL_CODE,ExceptionConstants.DATA_WRITE_FAIL_MSG,e);
            throw new BusinessRunTimeException(ExceptionConstants.DATA_WRITE_FAIL_CODE,
                    ExceptionConstants.DATA_WRITE_FAIL_MSG);
        }
        return result;
    }
}
