package com.seamwhole.serviceerpcore.mapper.ext;

import com.seamwhole.serviceerpcore.model.Unit;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface UnitExtMapper {

    List<Unit> selectByConditionUnit(
            @Param("name") String name,
            @Param("offset") Integer offset,
            @Param("rows") Integer rows);

    Long countsByUnit(
            @Param("name") String name);

    int batchDeleteUnitByIds(@Param("updateTime") Date updateTime, @Param("updater") Long updater, @Param("ids") String ids[]);
}