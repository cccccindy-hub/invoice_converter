package com.nnroad.web.controller.employee;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.nnroad.client.domain.SysClient;
import com.nnroad.client.service.ISysClientService;
import com.nnroad.common.utils.MessageUtils;
import com.nnroad.common.utils.NnroadSequence;
import com.nnroad.common.utils.StringUtils;
import com.nnroad.employee.domain.SysEmployee;
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

import com.nnroad.common.annotation.Anonymous;
import com.nnroad.common.annotation.Log;
import com.nnroad.common.core.controller.BaseController;
import com.nnroad.common.core.domain.AjaxResult;
import com.nnroad.common.enums.BusinessType;
import com.nnroad.employee.service.ISysEmployeeService;
import com.nnroad.common.utils.poi.ExcelUtil;
import com.nnroad.common.core.page.TableDataInfo;

/**
 * Employee Controller
 * 
 * This controller handles requests related to employee management, including
 * listing, exporting, adding, editing, and deleting employee records.
 * 
 * @author nnroad
 * @date 2024-10-11
 */
@RestController
@RequestMapping("/system/employee")
public class EmployeeController extends BaseController
{
    @Autowired
    private ISysEmployeeService sysEmployeeService;

    @Autowired
    private ISysClientService sysClientService;

    @Autowired
    private NnroadSequence sequence;

    /**
     * Retrieve a list of employees.
     * 
     * @param sysEmployee Filters to apply to the employee list
     * @return A paginated list of employees
     */
//    @Anonymous
    @PreAuthorize("@ss.hasPermi('employee:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysEmployee sysEmployee)
    {
        System.out.println(sysEmployee.getVendorId());
        startPage();
        List<SysEmployee> list = sysEmployeeService.selectSysEmployeeList(sysEmployee);
        return getDataTable(list);
    }

    /**
     * Export the list of employees.
     * 
     * @param response The HTTP response to write the Excel file to
     * @param sysEmployee Filters to apply to the employee list
     */
    @PreAuthorize("@ss.hasPermi('employee:export')")
    @Log(title = "Employee", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysEmployee sysEmployee)
    {
        List<SysEmployee> list = sysEmployeeService.selectSysEmployeeList(sysEmployee);
        ExcelUtil<SysEmployee> util = new ExcelUtil<SysEmployee>(SysEmployee.class);
        util.exportExcel(response, list, "Employee Data");
    }

    /**
     * Get detailed information about a specific employee.
     * 
     * @param id The ID of the employee to retrieve
     * @return The employee's details
     */
    @PreAuthorize("@ss.hasPermi('employee:edit')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(sysEmployeeService.selectSysEmployeeById(id));
    }

    /**
     * Add a new employee.
     * 
     * @param sysEmployee The employee information to add
     * @return The result of the add operation
     */
    @PreAuthorize("@ss.hasPermi('employee:add')")
    @Log(title = "Employee", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysEmployee sysEmployee)
    {
        SysClient c = sysClientService.selectSysClientById(sysEmployee.getClientId());
        sysEmployee.setCompanyCode(c.getCompanyCode());
        // 没有录入员工编号的通过clientCode生成最新的员工编号
        if (StringUtils.isEmpty(sysEmployee.getEmployeeCode())) {
            String eeCode =  sequence.getCode(sysEmployee.getCompanyCode(),"-" , 4);
            sysEmployee.setEmployeeCode(eeCode);
            if(!sysEmployeeService.checkCode(sysEmployee, sysEmployee.getCompanyCode())){
                eeCode = sysEmployeeService.resetAndGetCode("-", sysEmployee.getCompanyCode());
            }
            sysEmployee.setEmployeeCode(eeCode);
        }
//        else {
//            if (sysEmployeeService.selectEmployeeByCode(sysEmployee.getEmployeeCode()) != null) {
//                return AjaxResult.error(MessageUtils.message("html.client.employee.temp.messageErrer"));
//            }
//        }
        return toAjax(sysEmployeeService.insertSysEmployee(sysEmployee));
    }

    /**
     * Update an existing employee.
     * 
     * @param sysEmployee The updated employee information
     * @return The result of the update operation
     */
    @PreAuthorize("@ss.hasPermi('employee:edit')")
    @Log(title = "Employee", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysEmployee sysEmployee)
    {
        return toAjax(sysEmployeeService.updateSysEmployee(sysEmployee));
    }

    /**
     * Delete one or more employees.
     * 
     * @param ids The IDs of the employees to delete
     * @return The result of the delete operation
     */
    @PreAuthorize("@ss.hasPermi('employee:remove')")
    @Log(title = "Employee", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sysEmployeeService.deleteSysEmployeeByIds(ids));
    }

    @Anonymous
    @PreAuthorize("@ss.hasPermi('client:remove')")
    @Log(title = "Sys Employee Deactivate", businessType = BusinessType.UPDATE)
    @PutMapping("/{id}/deactivate")
    public AjaxResult deactivate(@PathVariable Long id) {
        //Call the service method to deactivate the client
        int result = sysEmployeeService.deactivateEmployeeById(id);
        return result > 0 ? success() : error("Failed to deactivate SysClient with ID: " + id);

    }
    
}
