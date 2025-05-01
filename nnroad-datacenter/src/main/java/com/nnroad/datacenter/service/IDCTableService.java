package com.nnroad.datacenter.service;

import com.nnroad.common.core.domain.AjaxResult;
import com.nnroad.common.core.page.TableDataInfo;
import com.nnroad.datacenter.domain.DCTable;
import com.nnroad.datacenter.domain.DCTableConfig;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * tableDefinitionService接口
 * 
 * @author Sheng
 * @date 2024-11-11
 */
public interface IDCTableService
{

    /**
     * 查询tableDefinition
     * 
     * @param tableId tableDefinition主键
     * @return tableDefinition
     */
    public DCTable selectDCTableByTableId(Long tableId);

    /**
     * 查询tableDefinition列表
     * 
     * @param dcTable tableDefinition
     * @return tableDefinition集合
     */
    public List<DCTable> selectDcTableList(DCTable dcTable);

    /**
     * 新增tableDefinition
     * 
     * @param dcTable tableDefinition
     * @return 结果
     */
    public int insertDCTable(DCTable dcTable);

    /**
     * 修改tableDefinition
     * 
     * @param dcTable tableDefinition
     * @return 结果
     */
    public int updateDCTable(DCTable dcTable);

    /**
     * 批量删除tableDefinition
     * 
     * @param tableIds 需要删除的tableDefinition主键集合
     * @return 结果
     */
    public int deleteDcTableByTableIds(Long[] tableIds);


    int deleteDcTableByTableId(Long tableId);

    List<DCTable> selectDCTableByTableType(Long tableType);

    TableDataInfo getGenTableColumnList(Map<String, String> map,boolean isExport);

    String formateDBName(String tableEnName);

    /**
     * 生成或者修改表
     *
     * @param dcTable 配置表
     * @return 结果
     */
    AjaxResult generateOrChangeTable(DCTable dcTable);

    public Long selectInsertIndex(Long id, DCTable table);


    /**
     * 判断数据是否可以插入和修改
     *
     * @param tableId 表id
     * @param   map   参数
     * @param isUpdate 是否是修改
     * @param id 数据id
     * @return 结果
     */
    boolean findDataIsDuplicated(Long tableId, Map<String, String> map, boolean isUpdate, Long id);

    void formatParam(Map<String, String> map, DCTable dcTable,Boolean isChange);

    Map<String, String> insertByMap(Map<String,String> param,String tableName);

    /**
     * 根据id查询生成表字段
     *
     * @param id id列表
     * @param tableId
     * @return 结果
     */
    Map<String, Object> selectUseTableDataById(Long id, DCTable tableId);


    /**
     * 配置生成表删除
     *
     * @param ids id列表
     * @param tableId
     * @return 结果
     */
    int delUseTableData(String ids, Long tableId);

    /**
     * 根据配置表导入数据
     *
     * @param dcTable 配置表
     * @param   file   上传文件
     * @return 结果
     */
    AjaxResult ipmortByDCTable(DCTable dcTable, MultipartFile file, String batchId, Boolean isRequire) throws Exception;

    AjaxResult ipmortByDCTableBySpecial(DCTable dcTable, MultipartFile file,String batchId,Boolean isRequire) throws Exception;


    void formateImportColumns(List<DCTableConfig> tableColumns, Map<String, Integer> mtcMap, Map<String, String> columnMap);


    void updateByMap(Map<String, String> map, String tableDbName);


    /**
     * 查询配置表列表
     *
     * @param dcTable 配置表
     * @return 配置表集合
     */
    public List<DCTable> selectDCTableList(DCTable dcTable);





}
