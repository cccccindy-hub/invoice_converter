package com.nnroad.web.controller.payroll;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.nnroad.common.annotation.Log;
import com.nnroad.common.config.NNRoadConfig;
import com.nnroad.common.core.controller.BaseController;
import com.nnroad.common.core.domain.AjaxResult;
import com.nnroad.common.core.page.TableDataInfo;
import com.nnroad.common.enums.BusinessType;
import com.nnroad.common.utils.DateUtils;
import com.nnroad.common.utils.file.FileUtils;
import com.nnroad.common.utils.poi.ExcelUtil;
import com.nnroad.datacenter.common.ImportOptTypeEnum;
import com.nnroad.datacenter.common.ImportStatusEnum;
import com.nnroad.datacenter.domain.DCTable;
import com.nnroad.datacenter.domain.DCTableImportLog;
import com.nnroad.datacenter.mapper.DCTableImportLogMapper;
import com.nnroad.datacenter.service.IDCTableConfigService;
import com.nnroad.datacenter.service.IDCTableDataOperateLogService;
import com.nnroad.datacenter.service.IDCTableDataPullService;
import com.nnroad.datacenter.service.IDCTableService;
import com.nnroad.extraAttribute.service.ISysExtraAttributeService;
import com.nnroad.payroll.domain.PayrollOtherTable;
import com.nnroad.payroll.domain.PsResult;
import com.nnroad.payroll.service.IPayrollOtherTableService;
import com.nnroad.payroll.service.IPsResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 计算结果Controller
 * 
 * @author sheng
 * @date 2024-11-05
 */
@RestController
@RequestMapping("/payroll/config")
public class PsConfigController extends BaseController
{
    @Autowired
    private IPayrollOtherTableService payrollOtherTableService;


    /**
     * 获取tableDefinition详细信息
     */
    @PreAuthorize("@ss.hasPermi('payroll:config:query')")
    @GetMapping(value = "/getInfo/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(payrollOtherTableService.selectPayrollOtherTableById(id));
    }

    /**
     * 查询tableDefinition列表
     */
    @PreAuthorize("@ss.hasPermi('payroll:config:list')")
    @PostMapping("/list")
    public TableDataInfo list(@RequestBody PayrollOtherTable payrollOtherTable) {
        startPage();
        List<PayrollOtherTable> list = payrollOtherTableService.selectPayrollOtherTableList(payrollOtherTable);
        return getDataTable(list);
    }

    /**
     * 新增tableDefinition
     */
    @PreAuthorize("@ss.hasPermi('payroll:config:add')")
    @Log(title = "PayrollConfig", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PayrollOtherTable payrollOtherTable) {
        if (payrollOtherTableService.insertPayrollOtherTable(payrollOtherTable) != 0) {
            return success();
        } else {
            return error("业务表名称已存在!请重新输入便于区分的业务表名称");
        }
    }


    /**
     * 修改tableDefinition
     */
    @PreAuthorize("@ss.hasPermi('payroll:config:edit')")
    @Log(title = "tableDefinition", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody PayrollOtherTable payrollOtherTable) {
        return toAjax(payrollOtherTableService.updatePayrollOtherTable(payrollOtherTable));
    }


    /**
     * 修改tableDefinition
     */
    @PreAuthorize("@ss.hasPermi('payroll:config:view')")
    @PostMapping("/view")
    @ResponseBody
    public TableDataInfo View(@RequestBody PayrollOtherTable payrollOtherTable) {
        return payrollOtherTableService.calculateFormula(payrollOtherTable);
    }


    /**
     * 删除Formula Confiig
     */
    @PreAuthorize("@ss.hasPermi('payroll:config:remove')")
    @Log(title = "Formula Confiig", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(payrollOtherTableService.deletePayrollOtherTableByIds(ids));
    }


}
