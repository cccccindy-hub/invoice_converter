package com.nnroad.datacenter.mapper;


import com.nnroad.datacenter.domain.DCTableDataPullConfig;

import java.util.List;

public interface DCTableDataPullConfigMapper {

    DCTableDataPullConfig selectConfigByTableId(Long tableId);
}
