package com.nnroad.datacenter.service.Impl;

import com.alibaba.fastjson2.JSONObject;
import com.nnroad.common.exception.BusinessException;
import com.nnroad.common.utils.SecurityUtils;
import com.nnroad.common.utils.StringUtils;
import com.nnroad.common.utils.DateUtils;
import com.nnroad.common.utils.ShiroUtils;
import com.nnroad.datacenter.common.OperateTypeEnum;
import com.nnroad.datacenter.domain.DCTable;
import com.nnroad.datacenter.domain.DCTableDataOperateLog;
import com.nnroad.datacenter.mapper.DCTableDataOperateLogMapper;
import com.nnroad.datacenter.service.IDCTableDataOperateLogService;
import com.nnroad.datacenter.service.IDCTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 数据回滚Service业务层处理
 * 
 * @author qhw
 * @date 2022-03-04
 */
@Service
public class DCTableDataOperateLogServiceImpl implements IDCTableDataOperateLogService
{
    @Autowired
    private DCTableDataOperateLogMapper dcTableResultLogMapper;
    @Autowired
    private IDCTableService dcTableService;
    /**
     * 业务表日志变数据插入  类型-新增单条
     * @param map 数据
     * @param tableId 表字段配置ID
     * @return 结果
     */
    @Override
    public void insertByOne(Map<String, String> map, Long tableId,String batchId) {
        String uuid ="";
        if (StringUtils.isNotEmpty(batchId)){
            uuid = batchId;
        }else {
            uuid = UUID.randomUUID().toString().replace("-", "");
        }

        DCTableDataOperateLog operateLog = new DCTableDataOperateLog();
        operateLog.setTableId(tableId);
        if (map.containsKey("id") &&  map.get("id")!=null){
            Object obj = map.get("id");
            operateLog.setDataId(Long.valueOf(String.valueOf(obj)));
        }else {
            throw new BusinessException("");
        }
        //operateLog.setAfter(JSONObject.valueAsStr(map));
        operateLog.setAfter(JSONObject.toJSONString(map));
        operateLog.setOperateType(OperateTypeEnum.INSERT.getCode());
//        operateLog.setCreateBy(ShiroUtils.getSysUser().getUserName());
        operateLog.setCreateTime(DateUtils.getNowDate());
        operateLog.setDelFlag("0");
        operateLog.setBatchId(uuid);
        dcTableResultLogMapper.insertDcTableDataOperateLog(operateLog);
    }

    @Override
    public void updateByOne(Map<String, String> map, DCTable table, String batchId) {
        String uuid ="";
        if (StringUtils.isNotEmpty(batchId)){
            uuid = batchId;
        }else {
            uuid = UUID.randomUUID().toString().replace("-", "");
        }
        Long id = Long.valueOf(map.get("id"));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Map<String, Object> before = dcTableService.selectUseTableDataById(id, table);
        if (before!=null && before.size()>0){
            Map<String, Object> before1 =new HashMap<>();
            before.forEach((k,v)->{
                if(v instanceof Timestamp){
                    Timestamp time =  (Timestamp)v;
                    long time1 = time.getTime();
                    Date date = new Date(time1);
                    String format = sdf.format(date);
                    before1.put(k,format);

                } else if (v!=null && StringUtils.isNotEmpty(String.valueOf(v))){
                    before1.put(k,v);
                }
            });

            if (before.containsKey("sort_key") ){
                map.put("sort_key",before.get("sort_key").toString());
            }
            DCTableDataOperateLog operateLog = new DCTableDataOperateLog();
            operateLog.setTableId(table.getTableId());
            operateLog.setDataId(id);

            operateLog.setBefore(JSONObject.toJSONString(before1));
            //operateLog.setAfter(JSONObject.valueAsStr(map));
            operateLog.setAfter(JSONObject.toJSONString(map));
            operateLog.setOperateType(OperateTypeEnum.UPDATE.getCode());
            operateLog.setCreateBy(SecurityUtils.getUsername());
            operateLog.setCreateTime(DateUtils.getNowDate());
            operateLog.setDelFlag("0");
            operateLog.setBatchId(uuid);
            dcTableResultLogMapper.insertDcTableDataOperateLog(operateLog);
        }

    }

    @Override
    public void deleteById(Long id, Long tableId,String batchId) {
        String uuid ="";
        if (StringUtils.isNotEmpty(batchId)){
            uuid = batchId;
        }else {
            uuid = UUID.randomUUID().toString().replace("-", "");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DCTable table = dcTableService.selectDCTableByTableId(tableId);
        Map<String, Object> before = dcTableService.selectUseTableDataById(id, table);
        if (before!=null && before.size()>0){
            Map<String, Object> before1 =new HashMap<>();
            before.forEach((k,v)->{
                if(v instanceof Timestamp){
                    Timestamp time =  (Timestamp)v;
                    long time1 = time.getTime();
                    Date date = new Date(time1);
                    String format = sdf.format(date);
                    before1.put(k,format);

                } else if (v!=null && StringUtils.isNotEmpty(String.valueOf(v))){
                    before1.put(k,v);
                }
            });
            DCTableDataOperateLog operateLog = new DCTableDataOperateLog();
            operateLog.setTableId(tableId);
            operateLog.setDataId(id);
            operateLog.setBefore(JSONObject.toJSONString(before1));
            operateLog.setOperateType(OperateTypeEnum.DELETE.getCode());
//            operateLog.setCreateBy(ShiroUtils.getSysUser().getUserName());
            operateLog.setCreateTime(DateUtils.getNowDate());
            operateLog.setDelFlag("0");
            operateLog.setBatchId(uuid);
            dcTableResultLogMapper.insertDcTableDataOperateLog(operateLog);
        }

    }
}
