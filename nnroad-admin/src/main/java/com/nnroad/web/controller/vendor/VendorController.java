package com.nnroad.web.controller.vendor;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import com.nnroad.client.domain.SysClient;
import com.nnroad.vendor.domain.SysVendor;
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
import com.nnroad.client.service.ISysClientService;
import com.nnroad.employee.service.ISysEmployeeService;
import com.nnroad.vendor.service.ISysVendorService;
import com.nnroad.common.utils.poi.ExcelUtil;
import com.nnroad.common.core.page.TableDataInfo;

/**
 * Controller for managing vendor information.
 * This controller handles requests related to vendors, including
 * listing, adding, editing, and deleting vendor data.
 * 
 * @auth nick
 * @date 2024-10-11
 */
@RestController
@RequestMapping("/system/vendor")
public class VendorController extends BaseController
{
    @Autowired
    private ISysVendorService sysVendorService;
    
    @Autowired
    private ISysEmployeeService sysEmployeeService;
    
    @Autowired
    private ISysClientService sysClientService;

    /**
     * Fetches the list of vendors.
     * 
     * @param sysVendor the criteria for querying vendors
     * @return a paginated list of vendors
     */
    @PreAuthorize("@ss.hasPermi('vendor:list')")
//    @Anonymous
    @GetMapping("/list")
    public TableDataInfo list(SysVendor sysVendor)
    {
        startPage(); // Start pagination
        List<SysVendor> list = sysVendorService.selectSysVendorList(sysVendor);
        return getDataTable(list); // Return the data table
    }

    /**
     * Exports the list of vendors to an Excel file.
     * 
     * @param response the HTTP response to write the Excel file to
     * @param sysVendor the criteria for querying vendors
     */
    @PreAuthorize("@ss.hasPermi('vendor:export')")
    @Log(title = "Vendor Export", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysVendor sysVendor)
    {
        List<SysVendor> list = sysVendorService.selectSysVendorList(sysVendor);
        ExcelUtil<SysVendor> util = new ExcelUtil<>(SysVendor.class);
        util.exportExcel(response, list, "Vendor Data"); // Export to Excel
    }

    /**
     * Retrieves detailed information about a specific vendor.
     * 
     * @param id the ID of the vendor to retrieve
     * @return the vendor information
     */
    @PreAuthorize("@ss.hasPermi('vendor:edit')")
//    @Anonymous
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(sysVendorService.selectSysVendorById(id)); // Return vendor details
    }

    /**
     * Adds a new vendor.
     * 
     * @param sysVendor the vendor information to add
     * @return the result of the operation
     */
    @PreAuthorize("@ss.hasPermi('vendor:add')")
    @Log(title = "Add Vendor", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysVendor sysVendor)
    {
        return toAjax(sysVendorService.insertSysVendor(sysVendor)); // Add vendor
    }

    /**
     * Updates an existing vendor.
     * 
     * @param sysVendor the vendor information to update
     * @return the result of the operation
     */
//    @Anonymous
    @PreAuthorize("@ss.hasPermi('vendor:edit')")
    @Log(title = "Edit Vendor", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysVendor sysVendor)
    {
        return toAjax(sysVendorService.updateSysVendor(sysVendor)); // Update vendor
    }

    /**
     * Deletes one or more vendors.
     * 
     * @param ids the IDs of the vendors to delete
     * @return the result of the operation
     */
    @PreAuthorize("@ss.hasPermi('vendor:remove')")
    @Log(title = "Delete Vendor", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sysVendorService.deleteSysVendorByIds(ids)); // Delete vendors
    }
    
    /**
     * Get employees by vendor id
     * 
     * @param primary key of vendor
     * @return list of employee object
     */
    //@PreAuthorize("@ss.hasPermi('system:vendor:query')")
    @Anonymous
    @GetMapping(value = "/{vendorId}/employees")
    public AjaxResult getEmployeeByVendorId(@PathVariable Long vendorId)
    {
        return success(sysEmployeeService.getEmployeeByVendorId(vendorId));
    }
    
    /**
     * Get clients by vendorId
     * 
     * @param primary key of vendor
     * @return list of client object
     */
    //@PreAuthorize("@ss.hasPermi('system:vendor:query')")
    @Anonymous
    @GetMapping(value = "/{vendorId}/clients")
    public TableDataInfo getClientByVendorId(@PathVariable Long vendorId)
    {
    	startPage(); 
        List<SysClient> list = sysClientService.getClientByVendorId(vendorId);
        return getDataTable(list); 
    }
    
    /**
     * Off board an employee of a vendor
     * 
     * @param vendorId  Vendor primary key, employeeId Employee primary key
     * 
     */
    //@PreAuthorize("@ss.hasPermi('system:vendor:query')")
    @Anonymous
    @DeleteMapping(value = "/{vendorId}/employees/{employeeId}")
    public AjaxResult removeEmployeeFromVendor(@PathVariable Long vendorId, @PathVariable Long employeeId)
    {
        return success(sysEmployeeService.removeEmployeeFromVendor(vendorId, employeeId));
  
    }

    /**
     * Retrieves vendors by countries
     * 
     * @param
     * @return list of vendor object
     */
    //@PreAuthorize("@ss.hasPermi('system:vendor:query')")
    @Anonymous
    @PostMapping(value = "/countries")
    public AjaxResult getVendorByCountries(@RequestBody Map<String, List<String>> requestBody) {
        List<String> countries = requestBody.get("countries"); // Extract the ID from the request body
        return success(sysVendorService.getVendorByCountries(countries)); // Return vendor details
    }


    /**
     * Fetches the list of vendor's regions.
     *
     * @param sysVendor the criteria for querying vendors
     * @return a paginated list of vendors
     */
    //@PreAuthorize("@ss.hasPermi('system:vendor:list')")
    @Anonymous
    @GetMapping("/region/list")
    public TableDataInfo getVendorRegions() {
        List<String> regions = sysVendorService.getVendorRegionList();
        return getDataTable(regions); // Return the data table
    }

    @Anonymous
    //@PreAuthorize("@ss.hasPermi('client:remove')")
    @Log(title = "Vendor Deactivate", businessType = BusinessType.UPDATE)
    @PutMapping("/{id}/deactivate")
    public AjaxResult deactivate(@PathVariable Long id) {
        //Call the service method to deactivate the client
        int result = sysVendorService.deactivateSysVendorById(id);
        return result > 0 ? success() : error("Failed to deactivate SysClient with ID: " + id);

    }
    
    
}
