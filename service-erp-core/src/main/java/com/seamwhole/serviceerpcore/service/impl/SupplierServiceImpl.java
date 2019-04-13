package com.seamwhole.serviceerpcore.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.seamwhole.serviceerpcore.constants.BusinessConstants;
import com.seamwhole.serviceerpcore.constants.ExceptionConstants;
import com.seamwhole.serviceerpcore.exception.BusinessRunTimeException;
import com.seamwhole.serviceerpcore.mapper.SupplierMapper;
import com.seamwhole.serviceerpcore.mapper.ext.AccountHeadExtMapper;
import com.seamwhole.serviceerpcore.mapper.ext.DepotHeadExtMapper;
import com.seamwhole.serviceerpcore.mapper.ext.SupplierExtMapper;
import com.seamwhole.serviceerpcore.model.*;
import com.seamwhole.serviceerpcore.service.SupplierService;
import com.seamwhole.serviceerpcore.utils.BaseResponseInfo;
import com.seamwhole.serviceerpcore.utils.StringUtil;
import org.apache.commons.lang3.StringUtils;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SupplierServiceImpl implements SupplierService {
    private Logger logger = LoggerFactory.getLogger(SupplierServiceImpl.class);

    @Resource
    private SupplierMapper supplierMapper;

    @Resource
    private SupplierExtMapper supplierExtMapper;
    @Resource
    private LogServiceImpl logService;
    @Resource
    private UserServiceImpl userService;
    @Resource
    private AccountHeadExtMapper accountHeadExtMapper;
    @Resource
    private DepotHeadExtMapper depotHeadExtMapper;

    public Supplier getSupplier(long id) {
        return supplierMapper.selectByPrimaryKey(id);
    }

    public List<Supplier> getSupplier() {
        SupplierExample example = new SupplierExample();
        return supplierMapper.selectByExample(example);
    }

    public List<Supplier> select(String supplier, String type, String phonenum, String telephone, String description, int offset, int rows) {
        return supplierExtMapper.selectByConditionSupplier(supplier, type, phonenum, telephone, description, offset, rows);
    }

    public Long countSupplier(String supplier, String type, String phonenum, String telephone, String description) {
        return supplierExtMapper.countsBySupplier(supplier, type, phonenum, telephone, description);
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int insertSupplier(String beanJson, HttpServletRequest request) {
        Supplier supplier = JSONObject.parseObject(beanJson, Supplier.class);
        return supplierMapper.insertSelective(supplier);
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int updateSupplier(String beanJson, Long id) {
        Supplier supplier = JSONObject.parseObject(beanJson, Supplier.class);
        supplier.setId(id);
        return supplierMapper.updateByPrimaryKeySelective(supplier);
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int deleteSupplier(Long id) {
        return supplierMapper.deleteByPrimaryKey(id);
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int batchDeleteSupplier(String ids) {
        List<Long> idList = StringUtil.strToLongList(ids);
        SupplierExample example = new SupplierExample();
        example.createCriteria().andIdIn(idList);
        return supplierMapper.deleteByExample(example);
    }

    public int checkIsNameExist(Long id, String name) {
        SupplierExample example = new SupplierExample();
        example.createCriteria().andIdNotEqualTo(id).andSupplierEqualTo(name).andDeleteFlagNotEqualTo(BusinessConstants.DELETE_FLAG_DELETED);
        List<Supplier> list = supplierMapper.selectByExample(example);
        return list.size();
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int updateAdvanceIn(Long supplierId, BigDecimal advanceIn){
        logService.insertLog(BusinessConstants.LOG_INTERFACE_NAME_SUPPLIER,
                new StringBuffer(BusinessConstants.LOG_OPERATION_TYPE_EDIT).append(supplierId).toString(),
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest());
        Supplier supplier = supplierMapper.selectByPrimaryKey(supplierId);
        if(supplier!=null){
            supplier.setAdvancein(supplier.getAdvancein().add(advanceIn));  //增加预收款的金额，可能增加的是负值
            return supplierMapper.updateByPrimaryKeySelective(supplier);
        }else{
            return 0;
        }

    }

    public List<Supplier> findBySelectCus() {
        SupplierExample example = new SupplierExample();
        example.createCriteria().andTypeLike("客户").andEnabledEqualTo(true);
        example.setOrderByClause("id desc");
        return supplierMapper.selectByExample(example);
    }

    public List<Supplier> findBySelectSup() {
        SupplierExample example = new SupplierExample();
        example.createCriteria().andTypeLike("供应商").andEnabledEqualTo(true);
        example.setOrderByClause("id desc");
        return supplierMapper.selectByExample(example);
    }

    public List<Supplier> findBySelectRetail() {
        SupplierExample example = new SupplierExample();
        example.createCriteria().andTypeLike("会员").andEnabledEqualTo(true);
        example.setOrderByClause("id desc");
        return supplierMapper.selectByExample(example);
    }

    public List<Supplier> findById(Long supplierId) {
        SupplierExample example = new SupplierExample();
        example.createCriteria().andIdEqualTo(supplierId);
        example.setOrderByClause("id desc");
        return supplierMapper.selectByExample(example);
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int batchSetEnable(Boolean enabled, String supplierIDs) {
        logService.insertLog(BusinessConstants.LOG_INTERFACE_NAME_SUPPLIER,
                new StringBuffer(BusinessConstants.LOG_OPERATION_TYPE_EDIT).append(supplierIDs).toString(),
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest());
        List<Long> ids = StringUtil.strToLongList(supplierIDs);
        Supplier supplier = new Supplier();
        supplier.setEnabled(enabled);
        SupplierExample example = new SupplierExample();
        example.createCriteria().andIdIn(ids);
        return supplierMapper.updateByExampleSelective(supplier, example);
    }

    public List<Supplier> findUserCustomer(){
        SupplierExample example = new SupplierExample();
        example.createCriteria().andTypeEqualTo("客户");
        example.setOrderByClause("id desc");
        List<Supplier> list = supplierMapper.selectByExample(example);
        return list;
    }

    public List<Supplier> findByAll(String supplier, String type, String phonenum, String telephone, String description) {
        return supplierExtMapper.findByAll(supplier, type, phonenum, telephone, description);
    }
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public BaseResponseInfo importExcel(List<Supplier> mList) throws Exception {
        logService.insertLog(BusinessConstants.LOG_INTERFACE_NAME_SUPPLIER,
                new StringBuffer(BusinessConstants.LOG_OPERATION_TYPE_IMPORT).append(mList.size()).append(BusinessConstants.LOG_DATA_UNIT).toString(),
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest());
        BaseResponseInfo info = new BaseResponseInfo();
        Map<String, Object> data = new HashMap<String, Object>();
        try {
            for(Supplier s: mList) {
                supplierMapper.insertSelective(s);
            }
            info.code = 200;
            data.put("message", "成功");
        } catch (Exception e) {
            e.printStackTrace();
            info.code = 500;
            data.put("message", e.getMessage());
        }
        info.data = data;
        return info;
    }
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int batchDeleteSupplierByIds(String ids) {
        logService.insertLog(BusinessConstants.LOG_INTERFACE_NAME_SUPPLIER,
                new StringBuffer(BusinessConstants.LOG_OPERATION_TYPE_DELETE).append(ids).toString(),
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest());
        User userInfo=userService.getCurrentUser();
        String [] idArray=ids.split(",");
        return supplierExtMapper.batchDeleteSupplierByIds(new Date(),userInfo==null?null:userInfo.getId(),idArray);
    }
    /**
     * create by: qiankunpingtai
     * website：https://qiankunpingtai.cn
     * description:
     *正常删除，要考虑数据完整性，进行完整性校验
     * create time: 2019/4/10 14:48
     * @Param: ids
     * @return int
     */
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int batchDeleteSupplierByIdsNormal(String ids) throws Exception {
        /**
         * 校验
         * 1、财务主表	jsh_accounthead
         * 2、单据主表	jsh_depothead
         * 是否有相关数据
         * */
        int deleteTotal=0;
        if(StringUtils.isEmpty(ids)){
            return deleteTotal;
        }
        String [] idArray=ids.split(",");
        /**
         * 校验财务主表	jsh_accounthead
         * */
        List<AccountHead> accountHeadList=accountHeadExtMapper.getAccountHeadListByOrganIds(idArray);
        if(accountHeadList!=null&&accountHeadList.size()>0){
            logger.error("异常码[{}],异常提示[{}],参数,OrganIds[{}]",
                    ExceptionConstants.DELETE_FORCE_CONFIRM_CODE,ExceptionConstants.DELETE_FORCE_CONFIRM_MSG,ids);
            throw new BusinessRunTimeException(ExceptionConstants.DELETE_FORCE_CONFIRM_CODE,
                    ExceptionConstants.DELETE_FORCE_CONFIRM_MSG);
        }
        /**
         * 校验单据主表	jsh_depothead
         * */
        List<DepotHead> depotHeadList=depotHeadExtMapper.getDepotHeadListByOrganIds(idArray);
        if(depotHeadList!=null&&depotHeadList.size()>0){
            logger.error("异常码[{}],异常提示[{}],参数,OrganIds[{}]",
                    ExceptionConstants.DELETE_FORCE_CONFIRM_CODE,ExceptionConstants.DELETE_FORCE_CONFIRM_MSG,ids);
            throw new BusinessRunTimeException(ExceptionConstants.DELETE_FORCE_CONFIRM_CODE,
                    ExceptionConstants.DELETE_FORCE_CONFIRM_MSG);
        }
        /**
         * 校验通过执行删除操作
         * */
        deleteTotal= batchDeleteSupplierByIds(ids);
        return deleteTotal;

    }
}