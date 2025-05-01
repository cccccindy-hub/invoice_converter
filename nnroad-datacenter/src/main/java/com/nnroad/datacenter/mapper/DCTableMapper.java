package com.nnroad.datacenter.mapper;

import com.nnroad.datacenter.domain.DCTable;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * tableDefintionMapper接口
 * 
 * @author Sheng
 * @date 2024-11-11
 */
public interface DCTableMapper
{
    /**
     * 查询tableDefintion
     * 
     * @param tableId tableDefintion主键
     * @return tableDefintion
     */
    public DCTable selectDcTableByTableId(Long tableId);

    /**
     * 查询tableDefintion列表
     * 
     * @param dcTable tableDefintion
     * @return tableDefintion集合
     */
    public List<DCTable> selectDcTableList(DCTable dcTable);

    /**
     * 新增tableDefintion
     * 
     * @param dcTable tableDefintion
     * @return 结果
     */
    public int insertDCTable(DCTable dcTable);

    /**
     * 修改tableDefintion
     * 
     * @param dcTable tableDefintion
     * @return 结果
     */
    public int updateDCTable(DCTable dcTable);

    /**
     * 删除tableDefintion
     * 
     * @param tableId tableDefintion主键
     * @return 结果
     */
    public int deleteDcTableByTableId(Long tableId);

    /**
     * 批量删除tableDefintion
     * 
     * @param tableIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteDcTableByTableIds(Long[] tableIds);

    List<DCTable> selectDcTableByTableType(@Param("tableType") Long tableType);

    List<Map<String, Object>> getGenTableColumnList(@Param("sql")String sql);

    Long getGenTableColumnCount(@Param("sql")String sql);


    Integer selectDCTableNameOrReportNameIsExist(String tableName);

    Integer findTableDbNameIsExist(String name);

    int findTableIsExists(DCTable dcTable);

    int renameTable(@Param("sql")String sql);

    int copyData(@Param("sql")String sql);

    int createTable(@Param("createSQL")String createSQL);

    int createIndex(@Param("sql")String sql);

    Map<String, Object> selectById(@Param("id") Long id,@Param("tableName") String tableName);


    Map<String, Object> selectUseTableDataBySql(String sqlStr);

    int updateBySql(@Param("sql")String sql);

    /**
     * 业务表插入数据
     * @param param map实体类
     * @param tableName 表名
     * @return 结果
     */
    void insertByMap(@Param("param")  Map<String, String>  param, @Param("tableName") String tableName);

    /**
     * 业务表 删除数据
     * @param id  id
     * @param tableName  表名
     * @return 结果
     */
    int deleteById(@Param("id") Long id,@Param("tableName") String tableName);

    /**
     * 业务表 批量删除数据
     * @param ids 主键id列表
     * @return 结果
     */
    int deleteByIdS(@Param("ids")String[] ids,@Param("tableName") String tableName);


    void updateByMap(@Param("param")  Map<String, String>  param, @Param("tableName") String tableName);

    /**
     * 查询配置表列表
     *
     * @param dcTable 配置表
     * @return 配置表集合
     */
    public List<DCTable> selectDCTableList(DCTable dcTable);

    DCTable selectDCTableByName(@Param("tableDbName")String tableDbName);

}
