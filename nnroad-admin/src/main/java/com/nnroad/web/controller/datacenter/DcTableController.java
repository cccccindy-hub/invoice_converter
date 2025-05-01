package com.nnroad.web.controller.datacenter;

import com.nnroad.common.annotation.Log;
import com.nnroad.common.core.controller.BaseController;
import com.nnroad.common.core.domain.AjaxResult;
import com.nnroad.common.core.page.TableDataInfo;
import com.nnroad.common.enums.BusinessType;
import com.nnroad.common.utils.poi.ExcelUtil;
import com.nnroad.datacenter.common.TableTypeEnum;
import com.nnroad.datacenter.domain.DCTable;
import com.nnroad.datacenter.domain.DCTableTemp;
import com.nnroad.datacenter.service.IDCReportService;
import com.nnroad.datacenter.service.IDCTableService;
import com.nnroad.datacenter.service.IDCTableTempService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * tableDefinitionController
 *
 * @author Sheng
 * @date 2024-11-11
 */
@RestController
@RequestMapping("/datacenter/tableDefinition")
public class DcTableController extends BaseController {
    @Autowired
    private IDCTableService dcTableService;

    @Autowired
    private IDCTableTempService dcTableTempService;

    @Autowired
    private IDCReportService dcReportService;


    /**
     * 查询tableDefinition列表
     */
    @PreAuthorize("@ss.hasPermi('datacenter:tableDefinition:list')")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(@RequestBody DCTable dcTable) {
        startPage();
        List<DCTable> list = dcTableService.selectDcTableList(dcTable);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('datacenter:tableDefinition:Billinglist')")
    @PostMapping("/listAuth")
    public TableDataInfo BillingList(@RequestBody DCTable dcTable) {
//        dcTable.setTableType(TableTypeEnum.VENDOR.getCode());
//        List<DCTable> list = dcTableService.selectDCTableByTableType(TableTypeEnum.PAYSTUB.getCode());
        List<DCTable> list = dcTableService.selectDcTableList(dcTable);
        return getDataTable(list);
    }

    /**
     * 导出tableDefinition列表
     */
    @PreAuthorize("@ss.hasPermi('datacenter:tableDefinition:export')")
    @Log(title = "tableDefinition", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DCTable dcTable) {
        List<DCTable> list = dcTableService.selectDcTableList(dcTable);
        ExcelUtil<DCTable> util = new ExcelUtil<DCTable>(DCTable.class);
        util.exportExcel(response, list, "tableDefinition数据");
    }

    /**
     * 获取tableDefinition详细信息
     */
    @PreAuthorize("@ss.hasPermi('datacenter:tableDefinition:query')")
    @GetMapping(value = "/getInfo/{tableId}")
    public AjaxResult getInfo(@PathVariable("tableId") Long tableId) {
        return success(dcTableService.selectDCTableByTableId(tableId));
    }

    /**
     * 获取tableDefinition详细信息
     */
    @PreAuthorize("@ss.hasPermi('datacenter:tableDefinition:queryTemp')")
    @GetMapping(value = "/getInfoTemp/{tableId}")
    public AjaxResult getTempInfo(@PathVariable("tableId") Long tableId) {
        return success(dcTableTempService.selectDCTableTempByTableId(tableId));
    }


    /**
     * 新增tableDefinition
     */
    @PreAuthorize("@ss.hasPermi('datacenter:tableDefinition:add')")
    @Log(title = "tableDefinition", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DCTable dcTable) {
        if (dcTableService.insertDCTable(dcTable) != 0) {
            return success();
        } else {
            return error("业务表名称已存在!请重新输入便于区分的业务表名称");
        }
    }

    /**
     * 修改tableDefinition
     */
    @PreAuthorize("@ss.hasPermi('datacenter:tableDefinition:edit')")
    @Log(title = "tableDefinition", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody DCTable dcTable) {
        return toAjax(dcTableService.updateDCTable(dcTable));
    }


    /**
     * 修改tableDefinition
     */
    @PreAuthorize("@ss.hasPermi('datacenter:tableDefinition:editTemp')")
    @Log(title = "tableDefinition", businessType = BusinessType.UPDATE)
    @PostMapping("/editTemp")
    public AjaxResult editTemp(@RequestBody DCTableTemp dcTableTemp) {

        DCTable table = dcTableService.selectDCTableByTableId(dcTableTemp.getTableId());
        if (table == null) {
            return AjaxResult.error();
        }
        if (!table.getTableName().equals(dcTableTemp.getTableName())) {
            Integer count = dcReportService.checkDCTableNameOrReportNameIsExist(dcTableTemp.getTableName());
            if (count != null && count > 0) {
                return error("业务表名称已存在!请重新输入便于区分的业务表名称");
            }
        }
        if (dcTableTemp.getTableEnName() != null && !table.getTableEnName().equals(dcTableTemp.getTableEnName())) {
            String tableDbName = dcTableService.formateDBName(dcTableTemp.getTableEnName());
            dcTableTemp.setTableDbName(tableDbName);
        }
        return toAjax(dcTableTempService.updateDCTableTemp(dcTableTemp));
    }


    /**
     * 删除tableDefinition
     */
    @PreAuthorize("@ss.hasPermi('datacenter:tableDefinition:remove')")
    @Log(title = "tableDefinition", businessType = BusinessType.DELETE)
    @DeleteMapping("/{tableIds}")
    public AjaxResult remove(@PathVariable Long[] tableIds) {
        return toAjax(dcTableService.deleteDcTableByTableIds(tableIds));
    }


    @PreAuthorize("@ss.hasPermi('datacenter:tableDefinition:tableType')")
    @GetMapping("/tableTypeOptions")
    public AjaxResult getTableTypeOptions() {
        // 获取 tableType 字典数据
        List<Map<String, Object>> dictArray = TableTypeEnum.toList();
        return success(dictArray);
    }

    /**
     * 同步表格
     */
    @PreAuthorize("@ss.hasPermi('datacenter:tableDefinition:synTable')")
    @PostMapping("/synTable")
    public AjaxResult synTable(@RequestBody DCTable dcTable) {
        return dcTableService.generateOrChangeTable(dcTable);
    }


    /**
     * 生成表格
     */
    @PreAuthorize("@ss.hasPermi('datacenter:tableDefinition:generate')")
    @PostMapping("/generateOrChangeTable")
    public AjaxResult generateOrChangeTable(@RequestBody DCTable dcTable) {
        return dcTableService.generateOrChangeTable(dcTable);
    }

    /**
     * 生成表格
     */
    @PreAuthorize("@ss.hasPermi('datacenter:tableDefinition:stopTable')")
    @PostMapping("/stopTable")
    public AjaxResult stopTable(@RequestBody DCTable dcTable) {
        return toAjax(dcTableService.updateDCTable(dcTable));
    }


    /**
     * 回滚表格
     */
    @PreAuthorize("@ss.hasPermi('datacenter:tableDefinition:rollback')")
    @PostMapping("/rollBackTable")
    public AjaxResult rollBackTable(@RequestBody DCTable dcTable) {
        return toAjax(dcTableTempService.rollBackTable(dcTable.getTableId()));
    }


}


