package com.nnroad.web.controller.datacenter;

import com.nnroad.common.core.controller.BaseController;
import com.nnroad.datacenter.domain.DCTableImportLog;
import com.nnroad.datacenter.service.IDCTableImportLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.nnroad.common.core.page.TableDataInfo;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 业务表数据导入Controller
 * 
 * @author Hrone
 * @date 2024-12-09
 */
@Controller
@RequestMapping("/datacenter/import")
public class DCTableImportLogController extends BaseController
{
    @Autowired
    private IDCTableImportLogService dcTableImportLogService;

    /**
     * 查询
     */
    @PreAuthorize("@ss.hasPermi('datacenter:import:list')")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(@RequestBody DCTableImportLog dcTableImportLog)
    {
        startPage();
        List<DCTableImportLog> list = dcTableImportLogService.selectDCTableImportLogList(dcTableImportLog);
        return getDataTable(list);
    }

}
