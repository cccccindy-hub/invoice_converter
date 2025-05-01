package com.nnroad.web.controller.client;

import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.nnroad.client.domain.SysClient;
import com.nnroad.common.utils.NnroadSequence;
import com.nnroad.employee.domain.SysEmployee;
import com.nnroad.vendor.domain.SysVendor;
import org.springframework.http.ResponseEntity;
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
import com.nnroad.common.core.page.TableDataInfo;
import com.nnroad.common.enums.BusinessType;
import com.nnroad.common.utils.poi.ExcelUtil;
import com.nnroad.client.service.ISysClientService;
import com.nnroad.employee.service.ISysEmployeeService;
import com.nnroad.vendor.service.ISysVendorService;

/**
 * Controller for managing SysClient operations.
 * 
 * @author nick
 * @date 2024-10-10
 */
@RestController
@RequestMapping("/system/client")
public class ClientController extends BaseController {
    @Autowired
    private ISysClientService sysClientService;

    @Autowired
    private ISysEmployeeService sysEmployeeService;

    @Autowired
    private ISysVendorService sysVendorService;

    @Autowired
    private NnroadSequence sequence;

    /**
     * Retrieve a list of SysClient objects.
     */
//    @Anonymous
    @PreAuthorize("@ss.hasPermi('client:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysClient sysClient) {
        startPage();
        List<SysClient> list = sysClientService.selectSysClientList(sysClient);
        return getDataTable(list);
    }

    /**
     * Export the list of SysClient objects.
     */
    @PreAuthorize("@ss.hasPermi('client:export')")
    @Log(title = "SysClient", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysClient sysClient) {
        List<SysClient> list = sysClientService.selectSysClientList(sysClient);
        ExcelUtil<SysClient> util = new ExcelUtil<>(SysClient.class);
        util.exportExcel(response, list, "SysClient Data");
    }

    /**
     * Get detailed information for a specific SysClient.
     */
    @PreAuthorize("@ss.hasPermi('client:edit')")
    @Anonymous
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(sysClientService.selectSysClientById(id));
    }

    /**
     * Add a new SysClient.
     */
    @PreAuthorize("@ss.hasPermi('client:add')")
//    @Anonymous
    @Log(title = "SysClient", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysClient sysClient) {
        String prefix = "CN";
        String location = "US";
        Date startDate = sysClient.getStartDate();
        Integer startYear = startDate.getYear() + 1900;
        String companyCodePrefix = prefix + location + startYear;
        String companyCode = sequence.getCode(companyCodePrefix, "-", 3);
        SysClient c = new SysClient();
        // 查询数据库存储的code没有"-"分割
        c.setCompanyCode(companyCode.replace("-", ""));
        if (!sysClientService.checkCode(c, companyCodePrefix)) {
            companyCode = sysClientService.resetAndGetCode(companyCodePrefix);
        }
        // 数据库存储的code没有"-"分割
        sysClient.setCompanyCode(companyCode.replace("-", ""));
        return toAjax(sysClientService.insertSysClient(sysClient));
    }

    /**
     * Update an existing SysClient.
     */
//    @Anonymous
    @PreAuthorize("@ss.hasPermi('client:edit')")
    @Log(title = "SysClient", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysClient sysClient) {
        return toAjax(sysClientService.updateSysClient(sysClient));
    }

    /**
     * Delete one or more SysClient objects.
     */
    @PreAuthorize("@ss.hasPermi('client:remove')")
//    @Anonymous
    @Log(title = "SysClient", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(sysClientService.deleteSysClientByIds(ids));
    }

    /**
     * Get employees by client id
     */
    @PreAuthorize("@ss.hasPermi('client:employee')")
//    @Anonymous
    @GetMapping("/{clientId}/employees")
    public List<SysEmployee> getEmployeeByClientId(@PathVariable Long clientId) {
        return sysEmployeeService.getEmployeeByClientId(clientId);
    }

    /**
     * Off-board an employee of a client
     */
    // @PreAuthorize("@ss.hasPermi('system:client:remove')")
    @Anonymous
    @DeleteMapping("/{clientId}/employees/{employeeId}")
    public void removeEmployeeFromClient(@PathVariable Long clientId, @PathVariable Long employeeId) {
        sysEmployeeService.removeEmployeeFromClient(clientId, employeeId);
    }

    /**
     * Get vendors by client id
     */
    @PreAuthorize("@ss.hasPermi('client:vendor')")
//    @Anonymous
    @GetMapping("/{clientId}/vendors")
    public TableDataInfo getVendorByClientId(@PathVariable Long clientId) {
        startPage();
        List<SysVendor> list = sysVendorService.getVendorByClientId((long) clientId);
        return getDataTable(list);
    }

    @Anonymous
    @PreAuthorize("@ss.hasPermi('client:remove')")
    @Log(title = "SysClient", businessType = BusinessType.UPDATE)
    @PutMapping("/{id}/deactivate")
    public AjaxResult deactivate(@PathVariable Long id) {
       //Call the service method to deactivate the client
       int result = sysClientService.deactivateSysClientById(id);
       return result > 0 ? success() : error("Failed to deactivate SysClient with ID: " + id);

    }
}
