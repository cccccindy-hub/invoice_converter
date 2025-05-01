package com.nnroad.datacenter.mapper;


import com.nnroad.datacenter.domain.DCTableDataOperateLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 数据回滚Mapper接口
 * 
 * @author qhw
 * @date 2022-03-04
 */
public interface DCTableDataOperateLogMapper
{


    /**
     * 新增数据回滚
     * 
     * @param dcTableResultLog 数据回滚
     * @return 结果
     */
    int insertDcTableDataOperateLog(DCTableDataOperateLog dcTableResultLog);

    /**
     * 通过batchID查询这次操作所涉及的表
     * @param batchId
     * @return 结果
     */
    List<String> selectTablesByBatchId(String batchId);
    /**
     * 通过batchID查询这次操作所涉及的数据条数
     * @param batchId
     * @return 结果
     */
    Integer  selectTablesDatasCountByBatchId(String batchId);
    /**
     * 查询可回滚的数据id
     * @param batchId
     * @return 结果
     */
    List<String> selectCanRollBackDataId(@Param("tableId") String tableId,@Param("batchId") String batchId);
    /**
     * 数据回滚日志表 通过id列表查询数据
     * @param ids id列表
     * @return List<DcTableDataOperateLog>
     */
    List<DCTableDataOperateLog> selectByIds(@Param("array") List<String> ids);
    /**
     * 数据回滚日志表deleteFlag修改为2
     * @param batchId
     */
    void deleteByIds(@Param("batchId") String batchId);

}
