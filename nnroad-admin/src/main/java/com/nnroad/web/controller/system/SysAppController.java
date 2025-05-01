package com.nnroad.web.controller.system;

import com.nnroad.common.annotation.Log;
import com.nnroad.common.core.controller.BaseController;
import com.nnroad.common.core.domain.AjaxResult;
import com.nnroad.common.core.page.TableDataInfo;
import com.nnroad.common.enums.BusinessType;
import com.nnroad.common.utils.poi.ExcelUtil;
import com.nnroad.system.domain.SysApp;
import com.nnroad.system.form.VerifyForm;
import com.nnroad.system.service.ISysAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * 应用管理Controller
 *
 * @author ruoyi
 * @date 2023-05-25
 */
@RestController
@RequestMapping("/system/app")
public class SysAppController extends BaseController {
    @Autowired
    private ISysAppService sysAppService;

    @PostMapping("/verify")
    public AjaxResult verifyToken(@Valid @RequestBody VerifyForm form) {

        return AjaxResult.success(sysAppService.verifyToken(form));
    }

    /**
     * 查询应用管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:app:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysApp sysApp) {
        startPage();
        List<SysApp> list = sysAppService.selectSysAppList(sysApp);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('system:app:list')")
    @GetMapping("/gen_token/{appId}")
    public AjaxResult genToken(@PathVariable("appId") String appId) {
        String token = sysAppService.genToken(appId);
        AjaxResult ajax = AjaxResult.success();
        ajax.put("token", token);
        return ajax;
    }

    /**
     * 导出应用管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:app:export')")
    @Log(title = "应用管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysApp sysApp) {
        List<SysApp> list = sysAppService.selectSysAppList(sysApp);
        ExcelUtil<SysApp> util = new ExcelUtil<SysApp>(SysApp.class);
        util.exportExcel(response, list, "应用管理数据");
    }

    /**
     * 获取应用管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:app:query')")
    @GetMapping(value = "/{appId}")
    public AjaxResult getInfo(@PathVariable("appId") String appId) {
        return success(sysAppService.selectSysAppByAppId(appId));
    }

    /**
     * 新增应用管理
     */
    @PreAuthorize("@ss.hasPermi('system:app:add')")
    @Log(title = "应用管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysApp sysApp) {
        return toAjax(sysAppService.insertSysApp(sysApp));
    }

    /**
     * 修改应用管理
     */
    @PreAuthorize("@ss.hasPermi('system:app:edit')")
    @Log(title = "应用管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysApp sysApp) {
        return toAjax(sysAppService.updateSysApp(sysApp));
    }

    /**
     * 删除应用管理
     */
    @PreAuthorize("@ss.hasPermi('system:app:remove')")
    @Log(title = "应用管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{appIds}")
    public AjaxResult remove(@PathVariable String[] appIds) {
        return toAjax(sysAppService.deleteSysAppByAppIds(appIds));
    }
}
