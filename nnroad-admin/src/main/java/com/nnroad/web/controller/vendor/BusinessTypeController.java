package com.nnroad.web.controller.vendor;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.nnroad.vendor.domain.VendorBusinessType;
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
import com.nnroad.vendor.service.IBusinessTypeService;
import com.nnroad.common.utils.poi.ExcelUtil;
import com.nnroad.common.core.page.TableDataInfo;

/**
 * 【Please fill in the function name】 Controller
 *
 * @author nnroad
vendorte 2024-11-25
 */
@RestController
@RequestMapping("/businessType")
public class BusinessTypeController extends BaseController
{
    @Autowired
    private IBusinessTypeService businessTypeService;

    /**
     * Query for a list of 【Please fill in the function name】
     */
    @PreAuthorize("@ss.hasPermi('system:type:list')")
    @GetMapping("/list")
    public TableDataInfo list(VendorBusinessType vendorBusinessType)
    {
        startPage();
        List<VendorBusinessType> list = businessTypeService.selectBusinessTypeList(vendorBusinessType);
        return getDataTable(list);
    }

    /**
     * Export the list of 【Please fill in the function name】
     */
    @PreAuthorize("@ss.hasPermi('system:type:export')")
    @Log(title = "【Please fill in the function name】", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, VendorBusinessType vendorBusinessType)
    {
        List<VendorBusinessType> list = businessTypeService.selectBusinessTypeList(vendorBusinessType);
        ExcelUtil<VendorBusinessType> util = new ExcelUtil<VendorBusinessType>(VendorBusinessType.class);
        util.exportExcel(response, list, "【Please fill in the function name】 Data");
    }

    /**
     * Get the detailed information of 【Please fill in the function name】
     */
    @PreAuthorize("@ss.hasPermi('system:type:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(businessTypeService.selectBusinessTypeById(id));
    }

    /**
     * Add a new 【Please fill in the function name】
     */
    @PreAuthorize("@ss.hasPermi('system:type:add')")
    @Log(title = "【Please fill in the function name】", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody VendorBusinessType vendorBusinessType)
    {
        return toAjax(businessTypeService.insertBusinessType(vendorBusinessType));
    }

    /**
     * Update the 【Please fill in the function name】
     */
    @PreAuthorize("@ss.hasPermi('system:type:edit')")
    @Log(title = "【Please fill in the function name】", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody VendorBusinessType vendorBusinessType)
    {
        return toAjax(businessTypeService.updateBusinessType(vendorBusinessType));
    }

    /**
     * Delete 【Please fill in the function name】
     */
    @PreAuthorize("@ss.hasPermi('system:type:remove')")
    @Log(title = "【Please fill in the function name】", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(businessTypeService.deleteBusinessTypeByIds(ids));
    }
}

