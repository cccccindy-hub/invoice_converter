package com.nnroad.datacenter.mapper;


import com.nnroad.datacenter.domain.DCReport;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 配置表Mapper接口
 *
 */
public interface DCReportMapper
{
    /**
     * 查询配置表
     * 
     * @param reportId 配置表ID
     * @return 配置表
     */

    public DCReport selectDCReportById(Long reportId);

    /**
     * 查询配置表列表
     * 
     * @param dcReport 配置表
     * @return 配置表集合
     */
    public List<DCReport> selectDCReportResult(DCReport dcReport);

    /**
     * 查询财务报表列表
     *
     * @param dcReport 配置表
     * @return 配置表集合
     */
    public List<DCReport> selectDCReportWithFinance(DCReport dcReport);
    /**
     * 新增配置表
     * 
     * @param dcReport 配置表
     * @return 结果
     */
    public int insertDCReport(DCReport dcReport);

    /**
     * 修改配置表
     * 
     * @param dcReport 配置表
     * @return 结果
     */
    public int updateDCReport(DCReport dcReport);

    /**
     * 删除配置表
     * 
     * @param reportId 配置表ID
     * @return 结果
     */
    public int deleteDCReportById(Long reportId);

    /**
     * 批量删除配置表
     * 
     * @param reportIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteDCReportByIds(String[] reportIds);

    int createView(@Param("sql") String sql);

    int stopThisDCReport(DCReport dcReport);

    List<DCReport> selectUsedREPORTbyDBname(@Param("tableDbName") String tableDbName);

    DCReport selectReportByName(String reportDbname);

    String getTableAllDbName(@Param("tableName") String tableName);

    /**
     * 查询Reporte_db_name是否存在
     * @param reportDbName 表名
     * @return 结果
     */
    Integer checkReportDbNameIsExist(String reportDbName);


    /**
     * 查询Reporte name是否存在
     * @param report
     * @return 结果
     */
    Integer nameIsExist(DCReport report);

    /**
     * 查询配置表
     *
     * @param viewDbName DBname
     * @return 配置表
     */

    public DCReport selectFinanceByDbName(String viewDbName);


}
