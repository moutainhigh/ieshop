package com.seamwhole.servicetradecore.service;

import com.github.pagehelper.PageInfo;
import com.seamwhole.servicetradecore.model.ScheduleJob;

import java.util.List;
import java.util.Map;

/**
 * 定时任务
 */
public interface ScheduleJobService {

    /**
     * 根据ID，查询定时任务
     */
    ScheduleJob queryObject(Long jobId);

    /**
     * 查询定时任务列表
     */
    List<ScheduleJob> queryList(Map<String, Object> map);

    /**
     * 查询总数
     */
    int queryTotal(Map<String, Object> map);

    /**
     * 保存定时任务
     */
    void save(ScheduleJob scheduleJob);

    /**
     * 更新定时任务
     */
    void update(ScheduleJob scheduleJob);

    /**
     * 批量删除定时任务
     */
    void deleteBatch(Long[] jobIds);

    /**
     * 批量更新定时任务状态
     */
    int updateBatch(Long[] jobIds, int status);

    /**
     * 立即执行
     */
    void run(Long[] jobIds);

    /**
     * 暂停运行
     */
    void pause(Long[] jobIds);

    /**
     * 恢复运行
     */
    void resume(Long[] jobIds);

    PageInfo<ScheduleJob> queryByPage(Map<String, Object> params, Integer page, Integer limit);
}
