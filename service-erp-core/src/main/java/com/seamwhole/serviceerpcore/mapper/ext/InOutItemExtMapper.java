package com.seamwhole.serviceerpcore.mapper.ext;


import com.seamwhole.serviceerpcore.model.InOutItem;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface InOutItemExtMapper {

    List<InOutItem> selectByConditionInOutItem(
            @Param("name") String name,
            @Param("type") String type,
            @Param("remark") String remark,
            @Param("offset") Integer offset,
            @Param("rows") Integer rows);

    Long countsByInOutItem(
            @Param("name") String name,
            @Param("type") String type,
            @Param("remark") String remark);

    int batchDeleteInOutItemByIds(@Param("updateTime") Date updateTime, @Param("updater") Long updater, @Param("ids") String ids[]);
}