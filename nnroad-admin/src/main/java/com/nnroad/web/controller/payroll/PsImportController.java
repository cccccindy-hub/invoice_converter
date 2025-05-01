package com.nnroad.web.controller.datacenter;

import com.nnroad.common.annotation.Log;
import com.nnroad.common.config.NNRoadConfig;
import com.nnroad.common.core.controller.BaseController;
import com.nnroad.common.core.domain.AjaxResult;
import com.nnroad.common.core.domain.entity.SysUser;
import com.nnroad.common.core.page.TableDataInfo;
import com.nnroad.common.enums.BusinessType;
import com.nnroad.common.utils.DateUtils;
import com.nnroad.common.utils.SecurityUtils;
import com.nnroad.common.utils.file.FileUtils;
import com.nnroad.common.utils.poi.ExcelUtil;
import com.nnroad.datacenter.common.ImportOptTypeEnum;
import com.nnroad.datacenter.common.ImportStatusEnum;
import com.nnroad.datacenter.common.TableTypeEnum;
import com.nnroad.datacenter.domain.DCTable;
import com.nnroad.datacenter.domain.DCTableImportLog;
import com.nnroad.datacenter.domain.DCTableTemp;
import com.nnroad.datacenter.mapper.DCTableImportLogMapper;
import com.nnroad.datacenter.service.*;
import com.nnroad.extraAttribute.service.ISysExtraAttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * payroll Import
 *
 * @author Sheng
 * @date 2025-02-06
 */
@RestController
@RequestMapping("/payroll/import")
public class PsImportController extends BaseController {
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




    @PreAuthorize("@ss.hasPermi('datacenter:tablerResult:importData')")
    @PostMapping("/importData/{tableId}")
    @ResponseBody
    @Transactional
    public AjaxResult importData(MultipartFile file, boolean updateSupport, @PathVariable("tableId") Long tableId, String leaderId, Boolean ignoreSheetName) throws Exception
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String batchId = UUID.randomUUID().toString().replace("-", "");
        String fileType = "";
        DCTable dcTable = dcTableService.selectDCTableByTableId(tableId);
        if(ignoreSheetName != null && ignoreSheetName) {
            dcTable.setTableName("");
        }
        dcTableService.ipmortByDCTable(dcTable, file,batchId,true);

        SysUser sysUser= SecurityUtils.getLoginUser().getUser();

        DCTableImportLog importLog = new DCTableImportLog();
        importLog.setCreateBy(sysUser.getUserName());
        importLog.setCreateTime(DateUtils.getNowDate());
        importLog.setRoleName(sysUser.getRoles().get(0).getRoleName());
        importLog.setOpType(ImportOptTypeEnum.SINGLE.getCode());
        importLog.setBatchId(batchId);
        // 默认设置失败
        importLog.setStatus(ImportStatusEnum.failed.getCode());
        //1.生成文件，
        String fileRealName = file.getOriginalFilename();//获得原始文件名;

        // 获取文件名去掉扩展名
        if (FileUtils.isExcel(fileRealName)) {
            String fileName=fileRealName;
            int dotIndex = fileName.lastIndexOf(".");
            if (dotIndex > 0  && dotIndex < fileName.length() - 1) {
                fileRealName = fileName.substring(0, dotIndex); // 获取去掉扩展名的文件名
                fileType = fileName.substring(dotIndex+1); // 获取扩展名
            }
        }


        // 另存为的文件名
        String saveFileName = fileRealName + batchId + "." + fileType;
        // 原来的文件名字
        fileRealName = fileRealName + "." + fileType;
        String filePath  = NNRoadConfig.getProfile()+ "/upload/" ;
        File path = new File(filePath); //判断文件路径下的文件夹是否存在，不存在则创建
        if (!path.exists()) {
            path.mkdirs();
        }
        File savedFile = new File(filePath + saveFileName);
        boolean isCreateSuccess = savedFile.createNewFile(); // 是否创建文件成功
        if(isCreateSuccess){      //将文件写入
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


