package com.nnroad.datacenter.service.Impl;

import com.nnroad.datacenter.domain.DCReport;
import com.nnroad.datacenter.mapper.DCReportMapper;
import com.nnroad.datacenter.mapper.DCTableMapper;
import com.nnroad.datacenter.service.IDCReportService;
import com.nnroad.system.service.ISysRoleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 配置表Service业务层处理
 * 
 * @author Hrone
 */
@Service
public class DCReportServiceImpl implements IDCReportService
{
    @Autowired
    private DCReportMapper dcReportMapper;
    @Autowired
    private ISysRoleService roleService;
    @Autowired
    private DCTableMapper dcTableMapper;


    @Override
    public Integer checkDCTableNameOrReportNameIsExist(String tableName) {
        return dcTableMapper.selectDCTableNameOrReportNameIsExist(tableName);
    }
}
