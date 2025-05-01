package com.nnroad.web.controller.datacenter;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

import com.nnroad.common.annotation.Anonymous;
import com.nnroad.common.core.domain.entity.DCDictType;
import com.nnroad.datacenter.domain.DCDictData;
import com.nnroad.datacenter.domain.DCTable;
import com.nnroad.datacenter.service.IDCDictDataService;
import com.nnroad.datacenter.service.IDCDictTypeService;
import com.nnroad.system.service.ISysDictDataService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.nnroad.common.annotation.Log;
import com.nnroad.common.core.controller.BaseController;
import com.nnroad.common.core.domain.AjaxResult;
import com.nnroad.common.enums.BusinessType;
import com.nnroad.datacenter.domain.DCTableConfigTemp;
import com.nnroad.datacenter.service.IDCTableConfigTempService;
import com.nnroad.common.utils.poi.ExcelUtil;
import com.nnroad.common.core.page.TableDataInfo;

/**
 * columnConfigTempController
 *
 * @author Sheng
 * @date 2024-11-12
 */
@RestController
@RequestMapping("/datacenter/config")
public class DcTableConfigController extends BaseController
{
    @Autowired
    private IDCTableConfigTempService dcTableConfigTempService;

    @Autowired
    private IDCDictTypeService dictTypeService;

    @Autowired
    private ISysDictDataService sysDictDataService;

    /**
     * 查询columnConfigTemp列表
     */
    @PreAuthorize("@ss.hasPermi('columnConfigTemp:columnConfig:list')")
    @PostMapping("/list")
    public TableDataInfo list(@RequestBody DCTableConfigTemp dcTableConfigTemp)
    {
        List<DCTableConfigTemp> list = dcTableConfigTempService.selectDCTableConfigTempList(dcTableConfigTemp);
        return getDataTable(list);
    }

    /**
     * 查询columnConfigTemp列表
     */
    @PreAuthorize("@ss.hasPermi('columnConfigTemp:columnConfig:listTemp')")
    @PostMapping("/listTemp")
    public TableDataInfo listTemp(@RequestBody DCTableConfigTemp dcTableConfigTemp)
    {
        List<DCTableConfigTemp> list = dcTableConfigTempService.selectDCTableConfigTempList(dcTableConfigTemp);
        return getDataTable(list);
    }


    /**
     * 导出columnConfigTemp列表
     */
    @PreAuthorize("@ss.hasPermi('columnConfigTemp:columnConfig:export')")
    @Log(title = "columnConfigTemp", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DCTableConfigTemp dcTableConfigTemp)
    {
        List<DCTableConfigTemp> list = dcTableConfigTempService.selectDCTableConfigTempList(dcTableConfigTemp);
        ExcelUtil<DCTableConfigTemp> util = new ExcelUtil<DCTableConfigTemp>(DCTableConfigTemp.class);
        util.exportExcel(response, list, "columnConfigTemp数据");
    }

    /**
     * 获取ColumnConfig详细信息
     */
    @PreAuthorize("@ss.hasPermi('datacenter:config:query')")
    @GetMapping(value = "/{columnId}")
    public AjaxResult getInfo(@PathVariable("columnId") Long columnId)
    {
        return success(dcTableConfigTempService.selectDCTableConfigTempById(columnId));
    }

    @Anonymous
    @GetMapping("/basic")
    public AjaxResult configDicts() {
        Map<String, Object> data = new HashMap<>();
        //所有下拉字典类型
        data.put("dictTypes", dictTypeService.selectDictTypeAll());
        //日期下拉明细
        data.put("dateType", sysDictDataService.selectDictDataByType(Collections.singleton("date_format_type")).get("date_format_type"));

        return success(data);
    }

    /**
     * 新增columnConfigTemp
     */
    @PreAuthorize("@ss.hasPermi('columnConfigTemp:columnConfig:add')")
    @Log(title = "columnConfigTemp", businessType = BusinessType.INSERT)
    @PostMapping("/addTemp")
    public AjaxResult add(@RequestBody DCTableConfigTemp dcTableConfigTemp)
    {
        return dcTableConfigTempService.insertDCTableConfigTemp(dcTableConfigTemp);
    }

    /**
     * 修改tableDefinition
     */
    @PreAuthorize("@ss.hasPermi('datacenter:config:editTemp')")
    @Log(title = "columnConfig", businessType = BusinessType.UPDATE)
    @PostMapping("/editTemp")
    public AjaxResult edit(@RequestBody DCTableConfigTemp dcTableConfigTemp)
    {
        return dcTableConfigTempService.updateDCTableConfigTemp(dcTableConfigTemp);
    }


    @PreAuthorize("@ss.hasAnyPermi('datacenter:config:remove')")
    @Log(title = "remove ColumnTemp", businessType = BusinessType.DELETE)
    @GetMapping("/removeTemp/{tableId}/{columnId}")
    public AjaxResult removeTemp(@PathVariable("columnId") Long columnId,@PathVariable("tableId") Long tableId){
        if (dcTableConfigTempService.selectCountByParentId(columnId)>0)
        {
            return AjaxResult.warn("This field contains subfields.");
        }
        return toAjax(dcTableConfigTempService.deleteDCTableConfigTempById(columnId,tableId));
    }


}


