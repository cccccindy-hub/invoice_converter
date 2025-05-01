package com.nnroad.datacenter.mapper;


import com.nnroad.datacenter.domain.DCTableConfig;
import com.nnroad.datacenter.domain.DCTableConfigTemp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DCTableConfigTempMapper
    {
        /**
         * 查询配置表
         *
         * @param columnId 配置表ID
         * @return 配置表
         */
        public DCTableConfigTemp selectDCTableConfigTempById(Long columnId);

        /**
         * 查询配置表列表
         *
         * @param dcTableConfigTemp 配置表
         * @return 配置表集合
         */
        public List<DCTableConfigTemp> selectDCTableConfigTempList(DCTableConfigTemp dcTableConfigTemp);

        /**
         * 新增配置表
         *
         * @param dcTableConfigTemp 配置表
         * @return 结果
         */
        public int insertDCTableConfigTemp(DCTableConfigTemp dcTableConfigTemp);

        /**
         * 修改配置表
         *
         * @param dcTableConfigTemp 配置表
         * @return 结果
         */
        public int updateDCTableConfigTemp(DCTableConfigTemp dcTableConfigTemp);

        /**
         * 删除配置表
         *
         * @param columnId 配置表ID
         * @return 结果
         */
        public int deleteDCTableConfigTempById(Long columnId);

        /**
         * 批量删除配置表
         *
         * @param columnIds 需要删除的数据ID
         * @return 结果
         */
        public int deleteDCTableConfigTempByIds(String[] columnIds);

        List<DCTableConfigTemp> selectByTableId(Long tableId);


        int insertBatch(@Param("list") List<DCTableConfig> list);

        Integer selectParamIsExist(DCTableConfigTemp dcTableConfigTemp);

        Integer selectParamDbNameIsExist(DCTableConfigTemp dcTableConfigTemp);

        int selectCountByParentId(Long columnId);
        List<DCTableConfigTemp>  selectTableColumnsByTableId(Long tableId);

        int deleteDCTableConfigTempByTableID(Long tableId);

        void insertBatchWithID(List<DCTableConfig> list);
        /**
         * 查询该节点所有的子级项目
         * @param pid 节点id
         * @param tableId
         * @return List<DCTableConfigTemp>
         */
        List<DCTableConfigTemp> selectConfigsByPid(@Param("tableId")Long tableId, @Param("pid")Long pid);
    }

