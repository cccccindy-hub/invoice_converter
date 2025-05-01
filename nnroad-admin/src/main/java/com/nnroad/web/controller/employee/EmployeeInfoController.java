package com.nnroad.web.controller.employee;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.nnroad.common.annotation.Anonymous;
import com.nnroad.employee.service.ISysEmployeeService;
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
import com.nnroad.employee.domain.EmployeeInfo;
import com.nnroad.employee.service.EmployeeInfoService;
import com.nnroad.common.utils.poi.ExcelUtil;
import com.nnroad.common.core.page.TableDataInfo;

/**
 * 【请填写功能名称】Controller
 *
 * @author nnroad
 * @date 2024-11-13
 */
@RestController
@RequestMapping("/employeeInfo")
public class EmployeeInfoController extends BaseController
{
    @Autowired
    private EmployeeInfoService employeeInfoService;

    /**
     * 查询【请填写功能名称】列表
     */
    @PreAuthorize("@ss.hasPermi('system:Info:list')")
    @GetMapping("/list")
    public TableDataInfo list(EmployeeInfo employeeInfo)
    {
        startPage();
        List<EmployeeInfo> list = employeeInfoService.selectEmployeeInfoList(employeeInfo);
        return getDataTable(list);
    }

    /**
     * 导出【请填写功能名称】列表
     */
    @PreAuthorize("@ss.hasPermi('system:Info:export')")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, EmployeeInfo employeeInfo)
    {
        List<EmployeeInfo> list = employeeInfoService.selectEmployeeInfoList(employeeInfo);
        ExcelUtil<EmployeeInfo> util = new ExcelUtil<EmployeeInfo>(EmployeeInfo.class);
        util.exportExcel(response, list, "【请填写功能名称】数据");
    }

    /**
     * 获取【请填写功能名称】详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:Info:query')")
    @GetMapping(value = "{tableName}/{id}")
    public AjaxResult getInfo(@PathVariable("tableName") String tableName, @PathVariable("id") Long id)
    {
        return success(employeeInfoService.selectEmployeeInfoById(id, tableName));
    }

    /**
     * 新增【请填写功能名称】
     */
    @PreAuthorize("@ss.hasPermi('system:Info:add')")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody EmployeeInfo employeeInfo)
    {
        return toAjax(employeeInfoService.insertEmployeeInfo(employeeInfo));
    }

    /**
     * 修改【请填写功能名称】
     */
    @PreAuthorize("@ss.hasPermi('system:Info:edit')")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody EmployeeInfo employeeInfo)
    {
        return toAjax(employeeInfoService.updateEmployeeInfo(employeeInfo));
    }

    /**
     * 删除【请填写功能名称】
     */
    @PreAuthorize("@ss.hasPermi('system:Info:remove')")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids, @PathVariable String tableName)
    {
        return toAjax(employeeInfoService.deleteEmployeeInfoByIds(ids, tableName));
    }

}
