package com.nnroad.datacenter.service;


import com.nnroad.datacenter.domain.DCTableDataPullConfig;
import java.util.List;
import java.util.Map;

/**
 * 快速引入接口
 */

public interface IDCTableDataPullService {

    /**
     * 通过数据表id 查询快速导入配置
     * @param tableId
     * @return
     * */
    DCTableDataPullConfig selectConfigByTableId(Long tableId);
}
