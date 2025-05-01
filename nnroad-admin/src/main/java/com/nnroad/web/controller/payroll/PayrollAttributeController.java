package com.nnroad.web.controller.payroll;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
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
import com.nnroad.payroll.domain.PayrollAttribute;
import com.nnroad.payroll.service.IPayrollAttributeService;
import com.nnroad.common.utils.poi.ExcelUtil;
import com.nnroad.common.core.page.TableDataInfo;

/**
 * Formula ConfiigController
 * 
 * @author Sheng
 * @date 2025-02-08
 */
@RestController
@RequestMapping("/payroll/attribute")
public class PayrollAttributeController extends BaseController
{
    @Autowired
    private IPayrollAttributeService payrollAttributeService;

    /**
     * 查询Formula Confiig列表
     */
    @PreAuthorize("@ss.hasPermi('payroll:attribute:list')")
    @PostMapping("/list")
    public AjaxResult list(@RequestBody PayrollAttribute payrollAttribute)
    {
        List<PayrollAttribute> list = payrollAttributeService.selectPayrollAttributeList(payrollAttribute);
        return success(payrollAttributeService.buildTree(list));
    }

    /**
     * 导出Formula Confiig列表
     */
    @PreAuthorize("@ss.hasPermi('payroll:attribute:export')")
    @Log(title = "Formula Confiig", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PayrollAttribute payrollAttribute)
    {
        List<PayrollAttribute> list = payrollAttributeService.selectPayrollAttributeList(payrollAttribute);
        ExcelUtil<PayrollAttribute> util = new ExcelUtil<PayrollAttribute>(PayrollAttribute.class);
        util.exportExcel(response, list, "Formula Confiig数据");
    }

    /**
     * 获取Formula Confiig详细信息
     */
    @PreAuthorize("@ss.hasPermi('payroll:attribute:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(payrollAttributeService.selectPayrollAttributeById(id));
    }

    /**
     * 新增Formula Confiig
     */
    @PreAuthorize("@ss.hasPermi('payroll:attribute:add')")
    @Log(title = "Formula Confiig", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@RequestBody PayrollAttribute payrollAttribute)
    {
        return toAjax(payrollAttributeService.insertPayrollAttribute(payrollAttribute));
    }

    /**
     * 修改Formula Confiig
     */
    @PreAuthorize("@ss.hasPermi('payroll:attribute:edit')")
    @Log(title = "Formula Confiig", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody PayrollAttribute payrollAttribute)
    {
        return toAjax(payrollAttributeService.updatePayrollAttribute(payrollAttribute));
    }

    /**
     * 删除Formula Confiig
     */
    @PreAuthorize("@ss.hasPermi('payroll:attribute:remove')")
    @Log(title = "Formula Confiig", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(payrollAttributeService.deletePayrollAttributeByIds(ids));
    }
}
