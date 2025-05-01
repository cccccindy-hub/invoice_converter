package com.nnroad.web.controller.datacenter;


import com.github.pagehelper.util.StringUtil;
import com.nnroad.common.annotation.Log;
import com.nnroad.common.config.NNRoadConfig;
import com.nnroad.common.core.controller.BaseController;
import com.nnroad.common.core.domain.AjaxResult;
import com.nnroad.common.core.page.TableDataInfo;
import com.nnroad.common.enums.BusinessType;
import com.nnroad.common.exception.Asserts;
import com.nnroad.common.utils.DateUtils;
import com.nnroad.common.utils.MessageUtils;
import com.nnroad.common.utils.file.FileUtils;
import com.nnroad.datacenter.common.ImportOptTypeEnum;
import com.nnroad.datacenter.common.ImportStatusEnum;
import com.nnroad.datacenter.domain.DCDictData;
import com.nnroad.datacenter.domain.DCTable;
import com.nnroad.datacenter.domain.DCTableConfig;
import com.nnroad.datacenter.domain.DCTableImportLog;
import com.nnroad.datacenter.mapper.DCTableImportLogMapper;
import com.nnroad.datacenter.service.*;
import com.nnroad.extraAttribute.service.ISysExtraAttributeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/datacenter/tableResult")
@Slf4j
public class DCTableResultController extends BaseController {

    @Autowired
    private IDCTableConfigService dcTableConfigService;

    @Autowired
    private IDCTableDataOperateLogService dcTableResultLogService;

    @Autowired
    private IDCTableService dcTableService;

    @Autowired
    private IDCTableDataPullService dcTableDataPullService;

    @Autowired
    private ISysExtraAttributeService extraAttributeService;

    @Autowired
    private DCTableImportLogMapper dcTableImportLogMapper;

    @Autowired
    private IDCDictDataService dictDataService;

    @PreAuthorize("@ss.hasPermi('datacenter:tableResult:show')")
    @GetMapping("/showTable/{tableId}")
    public AjaxResult showTable(@PathVariable("tableId") Long tableId) {
        try {
            Map<String, Object> result = new HashMap<>();
            //所有配置
            List<DCTableConfig> configs = dcTableConfigService.columnList(tableId, 0);
            result.put("configs", configs);
            //所有字典
            List<String> dictTypes = configs.stream().filter(DCTableConfig::isDict)
                    .map(DCTableConfig::getColumnDictid)
                    .collect(Collectors.toList());
            Map<String, List<DCDictData>> dictMap = dictDataService.selectByTypes(dictTypes);
            result.put("dictMap", dictMap);

//            // 获取表配置信息
//            DCTable dcTable = dcTableService.selectDCTableByTableId(tableId);
//            result.put("config", dcTable);
//
//            // 获取数据拉取配置
//            DCTableDataPullConfig dataPullConfig = dcTableDataPullService.selectConfigByTableId(tableId);
//            result.put("dataPullConfig", dataPullConfig);
//            result.put("dataPullEnable", dataPullConfig != null ? "1" : "0");
//
//            // 设置表名称、数据权限及用户ID
//            result.put("tableDbName", dcTable.getTableDbName());
//
//            SysExtraAttribute sysExtraAttribute = new SysExtraAttribute();
//            sysExtraAttribute.setTableName(dcTable.getTableDbName());
//            List<SysExtraAttribute> extraColumn = extraAttributeService.selectSysExtraAttributeList(sysExtraAttribute);
//            result.put("extraColumn", extraColumn);

            // 返回封装的 AjaxResult
            return success(result);
        } catch (Exception e) {
            log.error("select dc table config error:", e);
            // 捕获异常并返回错误信息
            return AjaxResult.error("Failed to fetch table config");
        }
    }


    /**
     * 查询业务表数据
     */
    @PreAuthorize("@ss.hasPermi('datacenter:tableResult:data')")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(@RequestBody Map<String, String> params) {
        return dcTableService.getGenTableColumnList(params, false);
    }

    @GetMapping("/detail/{tableId}/{resultId}")
    @ResponseBody
    @PreAuthorize("@ss.hasPermi('datacenter:tableResult:data')")
    public AjaxResult detail(@PathVariable Long tableId,
                             @PathVariable Long resultId) {
        DCTable table = dcTableService.selectDCTableByTableId(tableId);
        Asserts.notNull(table, "Table Not exist！");

        return success(dcTableService.selectUseTableDataById(resultId, table));
    }

    /**
     * 新增tableDefinition
     */
    @PreAuthorize("@ss.hasPermi('datacenter:tablerResult:add')")
    @Log(title = "tableResult", businessType = BusinessType.INSERT)
    @PostMapping("add/{tableId}")
    @ResponseBody
    public AjaxResult addTableData(@RequestBody Map<String, String> map, @PathVariable("tableId") Long tableId) {
        DCTable dcTable = dcTableService.selectDCTableByTableId(tableId);
        Long insertIndex = dcTableService.selectInsertIndex(null, dcTable);
        boolean canInsert = dcTableService.findDataIsDuplicated(tableId, map, true, 0L);
        if (canInsert) {
            map.put("sort_key", insertIndex.toString());
            dcTableService.formatParam(map, dcTable, false);
            dcTableService.insertByMap(map, dcTable.getTableDbName());
            dcTableResultLogService.insertByOne(map, tableId, null);
            return AjaxResult.success();
        } else {
            return AjaxResult.error("Failed to add! This data's datapulling is already exists!");
        }
    }


    @PreAuthorize("@ss.hasPermi('datacenter:tablerResult:remove')")
    @Log(title = "tablerResult", businessType = BusinessType.DELETE)
    @DeleteMapping("/{tableId}/{id}")
    public AjaxResult remove(@PathVariable Long tableId,
                             @PathVariable String id) {
        return toAjax(dcTableService.delUseTableData(id, tableId));
    }

    /**
     * 修改保存配置表
     */
    @PreAuthorize("@ss.hasPermi('datacenter:tableResult:edit')")
    @Log(title = "Table Result Edit", businessType = BusinessType.UPDATE)
    @PostMapping("/edit/{tableId}/{id}")
    @ResponseBody
    @Transactional
    public AjaxResult editSave(@PathVariable("tableId") Long tableId, @PathVariable("id") Long id, @RequestBody Map<String, String> map) throws Exception {
        DCTable paramTable = dcTableService.selectDCTableByTableId(tableId);
        Map<String, Object> oldColumn = dcTableService.selectUseTableDataById(id, paramTable);
        try {
            boolean canUpdate = dcTableService.findDataIsDuplicated(tableId, map, true, id);
            if (canUpdate) {
                if (oldColumn.containsKey("create_time")) {
                    Object create_time = oldColumn.get("create_time");
                    if (create_time instanceof Date) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String format = sdf.format(create_time);
                        map.put("create_time", format);
                    }
                }
                if (oldColumn.containsKey("create_by")) {
                    map.put("create_by", oldColumn.get("create_by").toString());
                }
                map.put("id", id.toString());
                // 获取表结构
                DCTable dcTable = dcTableService.selectDCTableByTableId(tableId);
                dcTableService.formatParam(map, dcTable, false);
                try {
                    dcTableResultLogService.updateByOne(map, paramTable, null);
                    dcTableService.updateByMap(map, dcTable.getTableDbName());
                    return AjaxResult.success();
                } catch (Exception e) {
                    log.error("datacenter edit error: ", e);
                    return AjaxResult.error(e.getMessage());
                }

            } else {
                return AjaxResult.error(MessageUtils.message("datacenter.message.DataIsDuplicated"));
            }
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }


    @PreAuthorize("@ss.hasPermi('datacenter:tablerResult:importData')")
    @PostMapping("/importData/{tableId}")
    @ResponseBody
    @Transactional
    public AjaxResult importData(MultipartFile file, boolean updateSupport, @PathVariable("tableId") Long tableId, String leaderId, Boolean ignoreSheetName) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String batchId = UUID.randomUUID().toString().replace("-", "");
        String fileType = "";
        DCTable dcTable = dcTableService.selectDCTableByTableId(tableId);
        if (ignoreSheetName != null && ignoreSheetName) {
            dcTable.setTableName("");
        }
        dcTableService.ipmortByDCTable(dcTable, file, batchId, true);


        DCTableImportLog importLog = new DCTableImportLog();
//        importLog.setCreateBy(sysUser.getUserName());
        importLog.setCreateTime(DateUtils.getNowDate());
//        importLog.setRoleName(sysUser.getRoles().get(0).getRoleName());
        importLog.setOpType(ImportOptTypeEnum.SINGLE.getCode());
        importLog.setBatchId(batchId);
        // 默认设置失败
        importLog.setStatus(ImportStatusEnum.failed.getCode());
        //1.生成文件，
        String fileRealName = file.getOriginalFilename();//获得原始文件名;

        // 获取文件名去掉扩展名
        if (FileUtils.isExcel(fileRealName)) {
            String fileName = fileRealName;
            int dotIndex = fileName.lastIndexOf(".");
            if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
                fileRealName = fileName.substring(0, dotIndex); // 获取去掉扩展名的文件名
                fileType = fileName.substring(dotIndex + 1); // 获取扩展名
            }
        }


        // 另存为的文件名
        String saveFileName = fileRealName + batchId + "." + fileType;
        // 原来的文件名字
        fileRealName = fileRealName + "." + fileType;
        String filePath = NNRoadConfig.getProfile() + "/upload/";
        File path = new File(filePath); //判断文件路径下的文件夹是否存在，不存在则创建
        if (!path.exists()) {
            path.mkdirs();
        }
        File savedFile = new File(filePath + saveFileName);
        boolean isCreateSuccess = savedFile.createNewFile(); // 是否创建文件成功
        if (isCreateSuccess) {      //将文件写入
            savedFile.delete();
            file.transferTo(savedFile);
            importLog.setInputFileName(fileRealName);
            // 设置导入状态：成功
            importLog.setStatus(ImportStatusEnum.success.getCode());
            importLog.setFile_path(filePath + saveFileName);
        }
        dcTableImportLogMapper.insertDCTableImportLog(importLog);
        return AjaxResult.success();
    }

}
