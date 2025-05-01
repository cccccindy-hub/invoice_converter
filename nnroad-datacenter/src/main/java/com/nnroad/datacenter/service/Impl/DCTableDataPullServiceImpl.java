package com.nnroad.datacenter.service.Impl;

import com.nnroad.datacenter.mapper.DCTableDataPullConfigMapper;
import com.nnroad.datacenter.service.IDCTableDataPullService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nnroad.datacenter.domain.DCTableDataPullConfig;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


/**
    * 快速引入接口

    */
@Service
public class DCTableDataPullServiceImpl implements IDCTableDataPullService {

    @Autowired
    private DCTableDataPullConfigMapper dcTableDataPullConfigMapper;
    @Override
    public DCTableDataPullConfig selectConfigByTableId(Long tableId) {
        return dcTableDataPullConfigMapper.selectConfigByTableId(tableId);
    }

}
