package com.seamwhole.serviceerpcore.service.serialNumber;

import com.jsh.erp.service.ICommonQuery;
import com.jsh.erp.utils.Constants;
import com.jsh.erp.utils.QueryUtils;
import com.jsh.erp.utils.StringUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Description
 *
 * @Author: cjl
 * @Date: 2019/1/21 16:33
 */
@Service(value = "serialNumber_component")
@SerialNumberResource
public class SerialNumberComponent implements ICommonQuery {
    @Resource
    private SerialNumberService serialNumberService;

    @Override
    public Object selectOne(String condition) {
        return null;
    }

    @Override
    public List<?> select(Map<String, String> map) {
        return getSerialNumberList(map);
    }

    private List<?> getSerialNumberList(Map<String, String> map) {
        String search = map.get(Constants.SEARCH);
        String serialNumber = StringUtil.getInfo(search, "serialNumber");
        String materialName = StringUtil.getInfo(search, "materialName");
        return serialNumberService.select(serialNumber,materialName,QueryUtils.offset(map), QueryUtils.rows(map));
    }

    @Override
    public Long counts(Map<String, String> map) {
        String search = map.get(Constants.SEARCH);
        String serialNumber = StringUtil.getInfo(search, "serialNumber");
        String materialName = StringUtil.getInfo(search, "materialName");
        return serialNumberService.countSerialNumber(serialNumber, materialName);
    }

    @Override
    public int insert(String beanJson, HttpServletRequest request) {
        return serialNumberService.insertSerialNumber(beanJson, request);
    }

    @Override
    public int update(String beanJson, Long id) {
        return serialNumberService.updateSerialNumber(beanJson, id);
    }

    @Override
    public int delete(Long id) {
        return serialNumberService.deleteSerialNumber(id);
    }

    @Override
    public int batchDelete(String ids) {
        return serialNumberService.batchDeleteSerialNumber(ids);
    }

    @Override
    public int checkIsNameExist(Long id, String serialNumber) {
        return serialNumberService.checkIsNameExist(id, serialNumber);
    }
}
