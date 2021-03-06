package com.seamwhole.serviceerpcore.component;

import com.seamwhole.serviceerpcore.service.ICommonQuery;
import com.seamwhole.serviceerpcore.service.UnitService;
import com.seamwhole.serviceerpcore.utils.Constants;
import com.seamwhole.serviceerpcore.utils.QueryUtils;
import com.seamwhole.serviceerpcore.utils.StringUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Service(value = "unit_component")
@UnitResource
public class UnitComponent implements ICommonQuery {

    @Resource
    private UnitService unitService;

    @Override
    public Object selectOne(String condition)throws Exception {
        return null;
    }

    @Override
    public List<?> select(Map<String, String> map)throws Exception {
        return getUnitList(map);
    }

    private List<?> getUnitList(Map<String, String> map)throws Exception {
        String search = map.get(Constants.SEARCH);
        String name = StringUtil.getInfo(search, "name");
        String order = QueryUtils.order(map);
        return unitService.select(name, QueryUtils.offset(map), QueryUtils.rows(map));
    }

    @Override
    public Long counts(Map<String, String> map)throws Exception {
        String search = map.get(Constants.SEARCH);
        String name = StringUtil.getInfo(search, "name");
        return unitService.countUnit(name);
    }

    @Override
    public int insert(String beanJson, HttpServletRequest request)throws Exception {
        return unitService.insertUnit(beanJson, request);
    }

    @Override
    public int update(String beanJson, Long id)throws Exception {
        return unitService.updateUnit(beanJson, id);
    }

    @Override
    public int delete(Long id)throws Exception {
        return unitService.deleteUnit(id);
    }

    @Override
    public int batchDelete(String ids)throws Exception {
        return unitService.batchDeleteUnit(ids);
    }

    @Override
    public int checkIsNameExist(Long id, String name)throws Exception {
        return unitService.checkIsNameExist(id, name);
    }

}
