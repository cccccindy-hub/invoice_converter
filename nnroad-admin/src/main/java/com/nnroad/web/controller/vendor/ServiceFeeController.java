package com.nnroad.web.controller.vendor;

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
import com.nnroad.vendor.domain.SysServiceFee;
import com.nnroad.vendor.service.ISysServiceFeeService;
import com.nnroad.common.utils.poi.ExcelUtil;
import com.nnroad.common.core.page.TableDataInfo;

/**
 * 【请填写功能名称】Controller
 * 
 * @author nnroad
 * @date 2024-10-24
 */
@RestController
@RequestMapping("/system/fee")
public class ServiceFeeController extends BaseController
{
    @Autowired
    private ISysServiceFeeService sysServiceFeeService;

    /**
     * 查询【请填写功能名称】列表
     */
    @PreAuthorize("@ss.hasPermi('system:fee:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysServiceFee sysServiceFee)
    {
        startPage();
        List<SysServiceFee> list = sysServiceFeeService.selectSysServiceFeeList(sysServiceFee);
        return getDataTable(list);
    }

    /**
     * 导出【请填写功能名称】列表
     */
    @PreAuthorize("@ss.hasPermi('system:fee:export')")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysServiceFee sysServiceFee)
    {
        List<SysServiceFee> list = sysServiceFeeService.selectSysServiceFeeList(sysServiceFee);
        ExcelUtil<SysServiceFee> util = new ExcelUtil<SysServiceFee>(SysServiceFee.class);
        util.exportExcel(response, list, "【请填写功能名称】数据");
    }

    /**
     * 获取【请填写功能名称】详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:fee:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(sysServiceFeeService.selectSysServiceFeeById(id));
    }

    /**
     * 新增【请填写功能名称】
     */
    @PreAuthorize("@ss.hasPermi('system:fee:add')")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysServiceFee sysServiceFee)
    {
        return toAjax(sysServiceFeeService.insertSysServiceFee(sysServiceFee));
    }

    /**
     * 修改【请填写功能名称】
     */
    @PreAuthorize("@ss.hasPermi('system:fee:edit')")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysServiceFee sysServiceFee)
    {
        return toAjax(sysServiceFeeService.updateSysServiceFee(sysServiceFee));
    }

    /**
     * 删除【请填写功能名称】
     */
    @PreAuthorize("@ss.hasPermi('system:fee:remove')")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sysServiceFeeService.deleteSysServiceFeeByIds(ids));
    }
}
