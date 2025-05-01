package com.nnroad.web.controller.payroll;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.nnroad.payroll.service.IPsResultService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.nnroad.common.annotation.Log;
import com.nnroad.common.core.controller.BaseController;
import com.nnroad.common.core.domain.AjaxResult;
import com.nnroad.common.enums.BusinessType;
import com.nnroad.payroll.domain.PsResult;
import com.nnroad.common.utils.poi.ExcelUtil;
import com.nnroad.common.core.page.TableDataInfo;

/**
 * 计算结果Controller
 * 
 * @author sheng
 * @date 2024-11-05
 */
@RestController
@RequestMapping("/payroll/result")
public class PsResultController extends BaseController
{
    @Autowired
    private IPsResultService psResultService;

    /**
     * 查询计算结果列表
     */
    @PreAuthorize("@ss.hasPermi('payroll:result:list')")
    @GetMapping("/list")
    public TableDataInfo list(PsResult psResult)
    {
        startPage();
        List<PsResult> list = psResultService.selectPsResultList(psResult);
        return getDataTable(list);
    }

    /**
     * 导出计算结果列表
     */
    @PreAuthorize("@ss.hasPermi('payroll:result:export')")
    @Log(title = "计算结果", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PsResult psResult)
    {
        List<PsResult> list = psResultService.selectPsResultList(psResult);
        ExcelUtil<PsResult> util = new ExcelUtil<PsResult>(PsResult.class);
        util.exportExcel(response, list, "计算结果数据");
    }

    /**
     * 获取计算结果详细信息
     */
    @PreAuthorize("@ss.hasPermi('payroll:result:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(psResultService.selectPsResultById(id));
    }

    /**
     * 新增计算结果
     */
    @PreAuthorize("@ss.hasPermi('payroll:result:add')")
    @Log(title = "计算结果", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PsResult psResult)
    {

        return toAjax(psResultService.insertPsResult(psResult));
    }

    /**
     * 修改计算结果
     */
    @PreAuthorize("@ss.hasPermi('payroll:result:edit')")
    @Log(title = "计算结果", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PsResult psResult)
    {
        return toAjax(psResultService.updatePsResult(psResult));
    }

    /**
     * 删除计算结果
     */
    @PreAuthorize("@ss.hasPermi('payroll:result:remove')")
    @Log(title = "计算结果", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(psResultService.deletePsResultByIds(ids));
    }
}
