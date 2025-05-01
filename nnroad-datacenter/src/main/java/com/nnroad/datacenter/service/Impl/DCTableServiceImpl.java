package com.nnroad.datacenter.service.Impl;

import com.nnroad.common.core.domain.AjaxResult;
import com.nnroad.common.core.page.TableDataInfo;
import com.nnroad.common.core.text.Convert;
import com.nnroad.common.exception.BusinessException;
import com.nnroad.common.utils.*;
import com.nnroad.common.utils.file.FileUtils;
import com.nnroad.common.utils.poi.NnroadExcelUtil;
import com.nnroad.datacenter.common.DBTypeEnum;
import com.nnroad.datacenter.common.FixedKeyWordsEnum;
import com.nnroad.datacenter.domain.*;
import com.nnroad.datacenter.mapper.*;
import com.nnroad.datacenter.service.IDCTableConfigService;
import com.nnroad.datacenter.service.IDCTableDataOperateLogService;
import com.nnroad.datacenter.service.IDCTableService;
import com.nnroad.system.constants.SysConstants;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * tableDefinitionService业务层处理
 *
 * @author Sheng
 * @date 2024-11-11
 */
@Service
public class DCTableServiceImpl implements IDCTableService {
    @Autowired
    private DCTableMapper dcTableMapper;
    @Autowired
    private DCTableConfigMapper dcTableConfigMapper;

    @Autowired
    private DCTableConfigDefaultMapper dcTableConfigDefaultMapper;

    @Autowired
    private DCTableConfigTempMapper dcTableConfigTempMapper;

    @Autowired
    private IDCTableConfigService dcTableConfigService;

    @Autowired
    private DCTableTempMapper dcTableTempMapper;

    @Autowired
    private DCReportMapper dcReportMapper;

    @Autowired
    private NnroadSequence sequence;

    @Autowired
    private IDCTableDataOperateLogService dcTableResultLogService;


    @Autowired
    private DCTableSkipKeywordsConfigMapper skipKeywordsConfigMapper;

    @Autowired
    private IDCTableService dcTableService;

    @Override
    public DCTable selectDCTableByTableId(Long tableId) {
        return dcTableMapper.selectDcTableByTableId(tableId);
    }


    /**
     * 查询tableDefinition列表
     *
     * @param dcTable tableDefinition
     * @return tableDefinition
     */
    @Override
    public List<DCTable> selectDcTableList(DCTable dcTable) {
        return dcTableMapper.selectDcTableList(dcTable);
    }

    /**
     * 新增tableDefinition
     *
     * @param dcTable tableDefinition
     * @return 结果
     */
    @Override
    @Transactional
    public int insertDCTable(DCTable dcTable) {
        Integer count = dcTableMapper.selectDCTableNameOrReportNameIsExist(dcTable.getTableName());
        if (count != null && count > 0) {
            return 0;
        }
        String tableEnName = dcTable.getTableEnName();
        String tableBdName = formateDBName(tableEnName);
        dcTable.setTableDbName(tableBdName);
        dcTable.setCreateTime(DateUtils.getNowDate());
        dcTable.setTableSyn(0);
        dcTable.setTableIsgen(0);
        dcTable.setTableStatus(0);
        dcTableMapper.insertDCTable(dcTable);
//        if (dcTable.getTableId() != null && dcTable.getTableType() != 1) {
//            List<DCTableConfig> defaults = dcTableConfigDefaultMapper.selectTitleByTableType(dcTable.getTableType(), dcTable.getTableId());
//            if (defaults.size() > 0) {
//                for (int i = 0; i < defaults.size(); i++) {
//                    defaults.get(i).setColumnSort((long) (i + 1));
//                }
//                dcTableConfigTempMapper.insertBatch(defaults);
//            }
//        }
        return 1;
    }

    /**
     * 修改tableDefinition
     *
     * @param dcTable tableDefinition
     * @return 结果
     */
    @Override
    public int updateDCTable(DCTable dcTable) {
        dcTable.setUpdateTime(DateUtils.getNowDate());
        return dcTableMapper.updateDCTable(dcTable);
    }

    /**
     * 批量删除tableDefinition
     *
     * @param tableIds 需要删除的tableDefinition主键
     * @return 结果
     */
    @Override
    public int deleteDcTableByTableIds(Long[] tableIds) {
        return dcTableMapper.deleteDcTableByTableIds(tableIds);
    }

    /**
     * 删除tableDefinition信息
     *
     * @param tableId tableDefinition主键
     * @return 结果
     */
    @Override
    public int deleteDcTableByTableId(Long tableId) {
        return dcTableMapper.deleteDcTableByTableId(tableId);
    }

    @Override
    public List<DCTable> selectDCTableByTableType(Long tableType) {
        return dcTableMapper.selectDcTableByTableType(tableType);
    }

    @Override
    public TableDataInfo getGenTableColumnList(Map<String, String> map, boolean isExport) {
        TableDataInfo rspData = new TableDataInfo();
        Long tableId = Long.parseLong(map.get("tableId"));
        DCTable dcTable = dcTableMapper.selectDcTableByTableId(tableId);
        List<DCTableConfig> list = dcTableConfigMapper.selectTableColumnsByTableId(tableId);
        String where = queryWhere(list, dcTable, map);
        String sql = qurySql(list, dcTable, map, isExport) + where;

        //order BY
        if (map.containsKey("orderByColumn") && map.containsKey("isAsc")) {
            String orderByColumn = map.get("orderByColumn");
            String isAsc = map.get("isAsc");
            if (StringUtils.isNotEmpty(orderByColumn)) {
                sql = sql + " order by temp." + orderByColumn + "  " + isAsc;
            }
        } else {
            String isAsc = map.get("isAsc");
            if (StringUtils.isNotEmpty(isAsc)) {
                sql = sql + " order by temp.sort_key  " + isAsc;
            }

        }
        //LIMIT
        String pageSql = sql;
        if (map.containsKey("pageSize") && map.containsKey("pageNum")) {
            Integer pageSize = Integer.parseInt(map.get("pageSize"));
            Integer pageNum = Integer.parseInt(map.get("pageNum"));
            Integer start = pageSize * (pageNum - 1);
            pageSql = sql + " limit " + start + " , " + pageSize;
        }
        List<Map<String, Object>> result = dcTableMapper.getGenTableColumnList(pageSql);
        // String[] froms = sql.split("from",2);
        String countSql = "select count(1) from " + dcTable.getTableDbName() + " as temp  " + where;
        Long count = dcTableMapper.getGenTableColumnCount(countSql);
        rspData.setCode(0);
        rspData.setRows(result);
        rspData.setTotal(count);
        return rspData;
    }

    /**
     * 生成业务表的查询语句的where条件
     *
     * @param columns 字段配置信息
     * @param dcTable 表配置信息
     * @param queryParams 查询条件信息
     * @return 结果
     */
    public String queryWhere(List<DCTableConfig> columns, DCTable dcTable, Map<String, String> queryParams) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(" where 1=1");
        if (queryParams.size() > 0) {
            Map<DCTableConfig, String> queryColumn = new HashMap<>();
            queryParams.forEach((k, v) -> {
                columns.forEach(param -> {
                    if (param.getColumnDbname().equals(k) && v != null && v != "") {
                        queryColumn.put(param, v);
                    }
                });
            });
            if (queryColumn.size() > 0) {
                queryColumn.forEach((k, v) -> {
                    //字符串查询
                    if (k.getColumnDbtype() == 0) {
                        if (k.getColumnChooseType() != null && k.getColumnChooseType() == 1) {
                            StringBuffer inSql = new StringBuffer();
                            String[] inValue = v.split(",");
                            for (String value : inValue) {
                                inSql.append("'").append(value).append("',");
                            }
                            stringBuffer.append(" and temp.").append(k.getColumnDbname()).append(" in (").append(inSql.toString().substring(0, inSql.length() - 1)).append(")");
                        } else {
                            stringBuffer.append(" and temp.").append(k.getColumnDbname()).append(" like '%").append(v).append("%'");
                        }
                        //数字查询
                    } else if (k.getColumnDbtype() == 1) {
                        stringBuffer.append(" and temp.").append(k.getColumnDbname()).append(" = ").append(v);
                        //日期
                    } else if (k.getColumnDbtype() == 2) {
                        if (v.contains("~")) {
                            String[] split = v.split("~");
                            if (split.length == 2) {
                                stringBuffer.append(" AND temp.").append(k.getColumnDbname()).append(" between '").append(split[0])
                                        .append("' and '").append(split[1]).append("' ");
                            }
                        }
                    }
                });
            }
        }
        return stringBuffer.toString();
    }

    public String JavaDateFormate(String formate) {
        String result = formate;
        if (formate.contains("mm")) {
            result = formate.replaceAll("m", "M");
        }
        if (formate.contains("DD")) {
            result = result.replaceAll("D", "d");
        }
        if (formate.contains("YYYY")) {
            result = result.replaceAll("Y", "y");
        }
        return result;
    }

    //生成sql语句
    public String qurySql(List<DCTableConfig> columns, DCTable dcTable, Map<String, String> queryParams, boolean isExport) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("select ");

        // 获取查询所需要的字段
        columns.forEach(column -> {
            String columnName = "`" + column.getColumnDbname() + "`"; // 字段名加反引号
            if (column.getColumnDbtype() == 2 && !isExport) {
                // 非导出逻辑：格式化日期字段
                String dateFormat = StringUtils.isNotEmpty(column.getColumnFormate())
                        ? dbDateFormate(column.getColumnFormate())
                        : "%Y-%m-%d";
                stringBuffer.append("DATE_FORMAT(temp.").append(columnName)
                        .append(", '").append(dateFormat).append("') as ").append(columnName).append(" ,");
            } else if (column.getColumnDbtype() == 2 && isExport) {
                // 导出逻辑：格式化日期字段
                stringBuffer.append("DATE_FORMAT(temp.").append(columnName)
                        .append(", '%Y-%m-%d') as ").append(columnName).append(" ,");
            } else {
                // 普通字段
                stringBuffer.append("temp.").append(columnName).append(" as ").append(columnName).append(" ,");
            }
        });

        // 数据权限字段处理
//        if (dcTable.getTableDataHasAuth() != null && dcTable.getTableDataHasAuth() == 1) {
//            stringBuffer.append("temp.`read_ids`, temp.`operation_ids`, "); // 数据权限字段也加反引号
//        }

        // 添加主键字段
        stringBuffer.append("`id`");

        // 添加表名
        stringBuffer.append(" from `").append(dcTable.getTableDbName()).append("` as temp");

        return stringBuffer.toString();
    }


    /**
     * 日期格式转化为数据库日期格式
     *
     * @param formate 日期格式
     * @return 数据库日期格式
     */
    public String dbDateFormate(String formate) {
        String result = formate;
        if (formate.contains("mm")) {
            result = result.replaceAll("mm", "%i");
        }
        if (formate.contains("MM")) {
            result = result.replaceAll("MM", "%m");
        }
        if (formate.contains("dd")) {
            result = result.replaceAll("dd", "%d");
        } else if (formate.contains("DD")) {
            result = result.replaceAll("DD", "%d");
        }
        if (formate.contains("YYYY")) {
            result = result.replaceAll("YYYY", "%Y");
        } else if (formate.contains("yyyy")) {
            result = result.replaceAll("yyyy", "%Y");
        }
        if (formate.contains("HH")) {
            result = result.replaceAll("HH", "%H");
        }
        if (formate.contains("ss")) {
            result = result.replaceAll("ss", "%s");
        }
        return result;
    }

    @Override
    public String formateDBName(String tableEnName) {
        //特殊字符 正则
        String regEx = "[\n`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";

        //小写 去除特殊字符 空格替换成_
        String tableDbName = "dc_gt_" + tableEnName.toLowerCase(Locale.ROOT).replaceAll(regEx, "").replaceAll("\\s+", "_");
        //查询表名是否存在
        tableDbName = findTableDbNameIsExist(tableDbName);
        return tableDbName;

    }

    public String findTableDbNameIsExist(String tableDbName) {
        String name = tableDbName;
        Integer count = dcTableMapper.findTableDbNameIsExist(name);
        if (count != null && count > 0) {
            Random random = new Random();
            name = name + "_" + (random.nextInt(9) + 1);
            name = findTableDbNameIsExist(name);
        }
        return name;
    }


    /**
     * 生成或者修改表
     *
     * @param dcTable 配置表
     * @return 结果
     */
    @Override
    @Transactional
    public AjaxResult generateOrChangeTable(DCTable dcTable) {
        boolean synFlag = false;
        boolean useFlag = false;
        if (dcTable.getTableSyn() != null && dcTable.getTableSyn() == 1) {
            synFlag = true;
        }
        if (dcTable.getTableStatus() != null && dcTable.getTableStatus() == 1) {
            useFlag = true;
        }
        //原表配置
        DCTable dcTable1 = dcTableMapper.selectDcTableByTableId(dcTable.getTableId());

        int isExists = dcTableMapper.findTableIsExists(dcTable1);
        //如果该表未生成已经存在则报错
        if (dcTable1.getTableIsgen() == 0 && isExists > 0) {
            return AjaxResult.error("this form already exists,please change name");
        }
        if (isExists > 0) {
            //报表启用且生成
            if (useFlag && dcTable1.getTableIsgen() == 1) {
                return useTable(dcTable1);
            }
            //改变表  临时表-》配置表  -》生成表
            return changeTable(dcTable1, synFlag, useFlag);
        } else {
            //生成表
            return createTable(dcTable1, synFlag, useFlag);
        }
    }

    //使用表  停用已生成-》使用
    private AjaxResult useTable(DCTable dcTable) {
        DCTable dcTable1 = new DCTable();
        dcTable1.setTableId(dcTable.getTableId());
        dcTable1.setTableLiveTime(new Date());
        dcTable1.setTableStatus(1);
        int i = dcTableMapper.updateDCTable(dcTable1);
        if (i > 0) {
            return AjaxResult.success();
        } else {
            return AjaxResult.error();
        }
    }


    //修改备份表
    private AjaxResult changeTable(DCTable dcTable, boolean synFlag, boolean useFlag) {
        DCTableTemp dcTableTemp = dcTableTempMapper.selectDCTableTempByTableId(dcTable.getTableId());
        List<DCTableConfig> oldColumnList = dcTableConfigMapper.selectTableColumnsByTableId(dcTable.getTableId());
        boolean changeNameFlag = false;
        //已生成 判断是否改名
        if (dcTable.getTableIsgen() == 1 && dcTable.getTableSyn() == 0 && dcTableTemp != null && dcTableTemp.getTableDbName() != null
                && !dcTable.getTableDbName().equals(dcTableTemp.getTableDbName())) {
            changeNameFlag = true;
        }
        //临时表改名并且该配置表在报表中使用则无法同步
        if (changeNameFlag) {
            List<DCReport> reportResult = dcReportMapper.selectUsedREPORTbyDBname(dcTable.getTableDbName());
            if (reportResult != null && reportResult.size() > 0) {
                return AjaxResult.error("Modification failed! This business table is in use");
            }
        }
        //零时表替换至主表
        if (dcTable.getTableSyn() == 0 && synFlag) {

            /*List<DCTableConfigTemp> temps = DCTableConfigTempMapper.selectByTableId(dcTable.getTableId());
            if (temps!=null && temps.size()>0){
                DCTableConfigService.deleteDCTableConfigBytableId(dcTable.getTableId());
                DCTableConfigService.copyTree(temps);

            }*/
            if (dcTableTemp != null) {
                DCTable dcTable1 = new DCTable();
                BeanUtils.copyProperties(dcTableTemp, dcTable1, new String[]{"tableLiveTime", "tableStatus", "tableSyn", "delFlag", "id"});
                dcTableMapper.updateDCTable(dcTable1);
            }
            //dcTableTempMapper.selectDCTableTempByTableId(dcTable.getTableId());
            //DCTableConfigTempMapper.deleteDCTableConfigTempByTableID(dcTable.getTableId());
        }

        //备份老表
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("_yyyyMMddHHmmss");

        String oldTableName = dcTable.getTableDbName() + simpleDateFormat.format(dcTable.getTableLiveTime() == null ? new Date() : dcTable.getTableLiveTime());
        //原表改名为xxxx_2021222222
        DCTable oldTable = new DCTable();
        oldTable.setTableDbName(oldTableName);
        //判断该表是否已经备份  如果没备份这备份
        int tableIsExists = dcTableMapper.findTableIsExists(oldTable);
        if (tableIsExists == 0) {
            String sql = "ALTER  TABLE " + dcTable.getTableDbName() + " RENAME TO " + oldTableName;

            dcTableMapper.renameTable(sql);
        }
        //创建新表
        if (!changeNameFlag) {
            createTable(dcTable, synFlag, useFlag);
        } else {
            DCTable dcTable2 = dcTableMapper.selectDcTableByTableId(dcTable.getTableId());
            createTable(dcTable2, synFlag, useFlag);
        }
        String equalColumns = "status, sort_key,create_by,create_time,update_by,update_time,";
        String oldColumnstr = "status, sort_key,create_by,create_time,update_by,update_time,";
        //从备份表中复制数据
//        if (dcTable != null) {
//            if (dcTableTemp != null && dcTableTemp.getTableDataHasAuth() == 1 && dcTable.getTableDataHasAuth() == 1) {
//                oldColumnstr += "read_ids,operation_ids,";
//                equalColumns += "read_ids,operation_ids,";
//            } else if (dcTableTemp == null && dcTable.getTableDataHasAuth() == 1) {
//                oldColumnstr += "read_ids,operation_ids,";
//                equalColumns += "read_ids,operation_ids,";
//            }
//        }
        List<DCTableConfigTemp> list = dcTableConfigTempMapper.selectTableColumnsByTableId(dcTable.getTableId());
        List<String> columns = new ArrayList<>();
        List<String> oldcolumns = new ArrayList<>();
        list.forEach(v -> {
            for (DCTableConfig old : oldColumnList) {
                if (old.getColumnDbtype() == v.getColumnDbtype() && (old.getColumnId().equals(v.getColumnId()) || old.getColumnDbname().equals(v.getColumnDbname()))) {
                    columns.add(v.getColumnDbname());
                    oldcolumns.add(old.getColumnDbname());
                }
            }
        });

        if (columns.size() > 0 && oldcolumns.size() > 0) {
            for (int i = 0; i < columns.size(); i++) {
                if (i < columns.size() - 1) {
                    equalColumns += "`" + columns.get(i) + "`" + ",";
                } else {
                    equalColumns += "`" + columns.get(i) + "`";
                }
            }
            for (int i = 0; i < oldcolumns.size(); i++) {
                if (i < oldcolumns.size() - 1) {
                    oldColumnstr += "`" + oldcolumns.get(i) + "`" + ",";
                } else {
                    oldColumnstr += "`" + oldcolumns.get(i) + "`";
                }
            }
            String copySql = "";
            if (!changeNameFlag) {
                copySql = "insert into " + dcTable.getTableDbName() + "(" + equalColumns + ")" + " select " + oldColumnstr + " from " + oldTableName;
            } else {
                copySql = "insert into " + dcTableTemp.getTableDbName() + "(" + equalColumns + ")" + " select " + oldColumnstr + " from " + oldTableName;
            }

            dcTableMapper.copyData(copySql);


        }
        return AjaxResult.success();
    }


    // 创建新表
    private AjaxResult createTable(DCTable dcTable, boolean synFlag, boolean useFlag) {
        //List<DCTableConfig> list = dcTableConfigMapper.selectTableColumnsByTableId(dcTable.getTableId());
        List<DCTableConfigTemp> list = dcTableConfigTempMapper.selectByTableId(dcTable.getTableId());
        if (list == null || list.size() == 0) {
            return AjaxResult.error("No column is defined in this business table!");
        }
        String sql = "CREATE TABLE " + dcTable.getTableDbName() + "( id bigint(20) NOT NULL AUTO_INCREMENT,";
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(sql);
        if (list != null && list.size() > 0) {
            list.forEach(column -> {
                stringBuffer.append(genetateColumnSql(column));
            });
            stringBuffer.append(FixedKeyWordsEnum.extra_data.getSql()).append(",")
                    .append(FixedKeyWordsEnum.status.getSql()).append(",")
                    .append(FixedKeyWordsEnum.sort_key.getSql()).append(",")
                    .append(FixedKeyWordsEnum.create_by.getSql()).append(",")
                    .append(FixedKeyWordsEnum.create_time.getSql()).append(",")
                    .append(FixedKeyWordsEnum.update_by.getSql()).append(",")
                    .append(FixedKeyWordsEnum.update_time.getSql()).append(",");

            stringBuffer.append("PRIMARY KEY (id))");
            String createSQL = stringBuffer.toString();
            //生成数据库表

            dcTableMapper.createTable(createSQL);

            //创建索引
            DCTableConfigTemp dcTableConfig = new DCTableConfigTemp();
            dcTableConfig.setTableId(dcTable.getTableId());
            dcTableConfig.setColumnIsindex(1);
            List<DCTableConfigTemp> indexList = dcTableConfigTempMapper.selectDCTableConfigTempList(dcTableConfig);
            if (indexList != null && indexList.size() > 0) {
                createIndex(indexList, dcTable);

            }
            //修改配置表状态
            DCTable dcTable1 = dcTableMapper.selectDcTableByTableId(dcTable.getTableId());

            dcTable1.setTableLiveTime(new Date());
            dcTable1.setTableStatus(1);
            if (dcTable1.getTableSyn() == 0 || dcTable1.getTableSyn() == null || synFlag) {
                dcTable1.setTableSyn(1);
            }
            if (dcTable1.getTableIsgen() == 0 && useFlag) {
                dcTable1.setTableIsgen(1);
            }
            int i = dcTableMapper.updateDCTable(dcTable1);
            dcTableConfigMapper.deleteDCTableConfigBytableId(dcTable.getTableId());
            dcTableConfigMapper.insertBatchWithId(list);
            if (i > 0) {
                return AjaxResult.success();
            } else {
                return AjaxResult.error("错误");
            }
        } else {
            return AjaxResult.error("无配置信息");
        }
    }


    //配置新表，字段配置
    private String genetateColumnSql(DCTableConfigTemp column) {
        StringBuffer stringBuffer = new StringBuffer();

        String defaultValue = "''";

        if (column.getColumnDbname() != null && column.getColumnDbtype() != null) {
            if (DBTypeEnum.CHAR.getCode().equals(column.getColumnDbtype()) && StringUtils.isNotEmpty(column.getColumnDefault())) {
                defaultValue = "'" + column.getColumnDefault() + "'";
            } else if (DBTypeEnum.INT.getCode().equals(column.getColumnDbtype()) && StringUtils.isNotEmpty(column.getColumnDefault())) {
                defaultValue = column.getColumnDefault();
            } else if (DBTypeEnum.INT.getCode().equals(column.getColumnDbtype()) && StringUtils.isEmpty(column.getColumnDefault())) {
                defaultValue = "0.00";
            }
        }
        String regEx = "[\n`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）—\\-—+|{}【】‘；：”“’。，、？]";
        String s = column.getColumnName().replaceAll(regEx, "");
        String comment = " COMMENT '" + s + "' ";
        //字符-有长度-必填
        if (column.getColumnDbname() != null && column.getColumnDbtype() != null && DBTypeEnum.CHAR.getCode().equals(column.getColumnDbtype()) && column.getColumnDblength() != null && column.getColumnIsrequired() == 1) {
            stringBuffer.append(" ")
                    .append("`").append(column.getColumnDbname()).append("`")
                    .append(" varchar")
                    .append("(").append(column.getColumnDblength()).append(") CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT ").append(defaultValue).append(comment).append(" ,");
        }//字符-有长度-非必填
        else if (column.getColumnDbname() != null && column.getColumnDbtype() != null && DBTypeEnum.CHAR.getCode().equals(column.getColumnDbtype()) && column.getColumnDblength() != null && column.getColumnIsrequired() != 1) {
            stringBuffer.append(" ")
                    .append("`").append(column.getColumnDbname()).append("`")
                    .append(" varchar")
                    .append("(").append(column.getColumnDblength()).append(") CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT ").append(defaultValue).append(comment).append(" ,");
        }//字符-无长度-必填
        else if (column.getColumnDbname() != null && column.getColumnDbtype() != null && DBTypeEnum.CHAR.getCode().equals(column.getColumnDbtype()) && column.getColumnDblength() == null && column.getColumnIsrequired() == 1) {
            stringBuffer.append(" ")
                    .append("`").append(column.getColumnDbname()).append("`")
                    .append(" varchar(255)")
                    .append(" CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT ").append(defaultValue).append(comment).append(" ,");
        }//字符-无长度-非必填
        else if (column.getColumnDbname() != null && column.getColumnDbtype() != null && DBTypeEnum.CHAR.getCode().equals(column.getColumnDbtype()) && column.getColumnDblength() == null && column.getColumnIsrequired() != 1) {
            stringBuffer.append(" ")
                    .append("`").append(column.getColumnDbname()).append("`")
                    .append(" varchar(255)")
                    .append(" CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT ").append(defaultValue).append(comment).append(" ,");
        }
        //----------------数字--------------------------------
        //数字-有长度-必填
        else if (column.getColumnDbname() != null && column.getColumnDbtype() != null && DBTypeEnum.INT.getCode().equals(column.getColumnDbtype()) && column.getColumnDblength() != null && column.getColumnIsrequired() == 1) {
            stringBuffer.append(" ")
                    .append("`").append(column.getColumnDbname()).append("`")
                    .append(" decimal")
                    .append(" (").append(column.getColumnDblength()).append(", 4) NOT NULL DEFAULT ").append(defaultValue).append(comment).append(" ,");
        }//数字-有长度-非填
        else if (column.getColumnDbname() != null && column.getColumnDbtype() != null && DBTypeEnum.INT.getCode().equals(column.getColumnDbtype()) && column.getColumnDblength() != null && column.getColumnIsrequired() != 1) {
            stringBuffer.append(" ")
                    .append("`").append(column.getColumnDbname()).append("`")
                    .append(" decimal")
                    .append(" (").append(column.getColumnDblength()).append(", 4) NULL DEFAULT ").append(defaultValue).append(comment).append(" ,");
        }//数字-无长度-非填
        else if (column.getColumnDbname() != null && column.getColumnDbtype() != null && DBTypeEnum.INT.getCode().equals(column.getColumnDbtype()) && column.getColumnDblength() == null && column.getColumnIsrequired() == 1) {
            stringBuffer.append(" ")
                    .append("`").append(column.getColumnDbname()).append("`")
                    .append(" decimal")
                    .append(" (12, 4) NOT NULL DEFAULT ").append(defaultValue).append(comment).append(" ,");
        }//数字-有长度-非填
        else if (column.getColumnDbname() != null && column.getColumnDbtype() != null && DBTypeEnum.INT.getCode().equals(column.getColumnDbtype()) && column.getColumnDblength() == null && column.getColumnIsrequired() != 1) {
            stringBuffer.append(" ")
                    .append("`").append(column.getColumnDbname()).append("`")
                    .append(" decimal")
                    .append(" (12, 4) NULL DEFAULT ").append(defaultValue).append(comment).append(" ,");
        }

        //-----------日期------------------------------
        else if (column.getColumnDbname() != null && column.getColumnDbtype() != null && DBTypeEnum.DATE.getCode().equals(column.getColumnDbtype()) && column.getColumnIsrequired() == 1) {
            stringBuffer.append(" ")
                    .append("`").append(column.getColumnDbname()).append("`")
                    .append(" datetime(0)")
                    .append(" NOT NULL ").append(comment).append(" ,");
        } else if (column.getColumnDbname() != null && column.getColumnDbtype() != null && DBTypeEnum.DATE.getCode().equals(column.getColumnDbtype()) && column.getColumnIsrequired() != 1) {
            stringBuffer.append(" ")
                    .append("`").append(column.getColumnDbname()).append("`")
                    .append(" datetime(0)")
                    .append(" NULL DEFAULT NULL ").append(comment).append(" ,");
        }
        return stringBuffer.toString();
    }

    private void createIndex(List<DCTableConfigTemp> indexList, DCTable dcTable) {
        for (DCTableConfigTemp indexConfig : indexList) {
            StringBuffer sqlsbf = new StringBuffer();
            sqlsbf.append("CREATE INDEX index_").append(indexConfig.getColumnDbname()).append(" ON ").append(dcTable.getTableDbName())
                    .append("(").append(indexConfig.getColumnDbname()).append(")");
            dcTableMapper.createIndex(sqlsbf.toString());
        }
    }

    @Override
    public Long selectInsertIndex(Long id, DCTable table) {
        Long ret = 0L;
        Map<String, Object> date = dcTableMapper.selectById(id, table.getTableDbName());

        if (date == null) {
            // 末尾插入则获取最新序列
            return sequence.generate(table.getTableDbName() + table.getTableId(), SysConstants.SORT_KEY_INCREMENT);
        }

        Long currentKey = date.get("sort_key") != null ? new BigDecimal(date.get("sort_key").toString()).longValue() : null;
        Long nextKey = getNextSortKey(table, currentKey);

        // 中间插入
        if (nextKey != null) {
            ret = getHalfIndex(currentKey, nextKey, table);
        } else {
            // 末尾插入则获取最新序列
            ret = sequence.generate(table.getTableDbName() + table.getTableId(), SysConstants.SORT_KEY_INCREMENT);
        }

        return ret;
    }

    /**
     * 判断数据是否可以插入和修改
     *
     * @param tableId 表id
     * @param   map   参数
     * @param isUpdate 是否是修改
     * @param id 数据id
     * @return 结果
     */
    @Override
    public boolean findDataIsDuplicated(Long tableId, Map<String, String> map, boolean isUpdate, Long id) {

        DCTable table = dcTableMapper.selectDcTableByTableId(tableId);
        DCTableConfig tableConfig = new DCTableConfig();
        tableConfig.setTableId(tableId);
        List<DCTableConfig> configList = dcTableConfigMapper.selectDCTableConfigList(tableConfig);
        List<DCTableConfig> isonlyList = new ArrayList<>();
        List<DCTableConfig> requiredList = new ArrayList<>();
        List<String> lengthErrorList = new ArrayList<>();
        String errorMsg = "";
        configList.forEach(x -> {
            if (x != null && StringUtils.isNotEmpty(x.getColumnDbname())) {
                if (x.getColumnIsrequired() != null && x.getColumnIsrequired() == 1) {
                    requiredList.add(x);
                }
                if (x.getColumnIsonly() != null && x.getColumnIsonly() == 1) {
                    isonlyList.add(x);
                }
                //参数长度校验
                if (map.containsKey(x.getColumnDbname())) {
                    //字符长度校验
                    if (x.getColumnDbtype() == null || DBTypeEnum.CHAR.getCode().equals(x.getColumnDbtype())) {
                        if (StringUtils.isNotEmpty(map.get(x.getColumnDbname()))) {
                            String value = map.get(x.getColumnDbname());
                            if (x.getColumnDblength() == null && value.length() > 255) {
                                lengthErrorList.add(x.getColumnName());
                            } else if (x.getColumnDblength() != null && value.length() > x.getColumnDblength()) {
                                lengthErrorList.add(x.getColumnName());
                            }
                        }
                    }
                    //数字长度校验
                    else if (x.getColumnDbtype() != null && DBTypeEnum.INT.getCode().equals(x.getColumnDbtype())) {
                        if (StringUtils.isNotEmpty(map.get(x.getColumnDbname())) && StringUtils.isNumeric(map.get(x.getColumnDbname()))) {
                            double value = Double.parseDouble(map.get(x.getColumnDbname()));

                            if (x.getColumnDblength() == null) {
                                // 使用固定范围
                                double max = 9999999999.99;
                                double min = -9999999999.99;
                                if (value > max || value < min) {
                                    lengthErrorList.add(x.getColumnName());
                                }
                            } else {
                                // 动态计算范围
                                int num = x.getColumnDblength() - 2;
                                StringBuilder strBuilder = new StringBuilder();
                                for (int i = 0; i < num; i++) {
                                    strBuilder.append("9");
                                }
                                strBuilder.append(".99");

                                double max = Double.parseDouble(strBuilder.toString());
                                double min = -max;

                                if (value > max || value < min) {
                                    lengthErrorList.add(x.getColumnName());
                                }
                            }
                        }


                    }
                }
            }
        });
        if (!lengthErrorList.isEmpty()) {
            errorMsg += "item [ " + String.join(",", lengthErrorList) + " ] data too long!";
        }

        List<String> noRequiredColum = new ArrayList<>();
        if (!requiredList.isEmpty()) {
            for (DCTableConfig config : requiredList) {
                if (map.containsKey(config.getColumnDbname())) {
                    if (StringUtils.isEmpty(map.get(config.getColumnDbname()))) {
                        noRequiredColum.add(config.getColumnName());
                    }
                } else {
                    noRequiredColum.add(config.getColumnName());
                }
            }
        }
        if (!noRequiredColum.isEmpty()) {
            errorMsg += "required items [ " + String.join(",", noRequiredColum) + " ] not filled!";
        }
        if (StringUtils.isNotEmpty(errorMsg)) {
            throw new BusinessException(errorMsg);
        }
        if (isonlyList.size() <= 0) {
            return true;
        } else {
            StringBuffer sbf = new StringBuffer();
            sbf.append("SELECT ");
            Map<String, String> onlyValueMap = new HashMap<>();
            List<String> needOnlyList = new LinkedList<>();
            for (int i = 0; i < isonlyList.size(); i++) {
                if (map.containsKey(isonlyList.get(i).getColumnDbname())) {
                    onlyValueMap.put(isonlyList.get(i).getColumnDbname(), map.get(isonlyList.get(i).getColumnDbname()));
                } else {
                    needOnlyList.add(isonlyList.get(i).getColumnName());
                }
                if (i < isonlyList.size() - 1) {
                    sbf.append(isonlyList.get(i).getColumnDbname()).append(",");
                } else {
                    sbf.append(isonlyList.get(i).getColumnDbname());
                }
            }
            sbf.append(" FROM ").append(table.getTableDbName());
            if (onlyValueMap.size() != isonlyList.size()) {
//                String needOnly = needOnlyList.stream().collect(Collectors.joining(","));
//                throw new BusinessException("These Columns "+needOnly+" are missing.");
                return false;
            } else {
                sbf.append(" WHERE 1=1 ");
                onlyValueMap.forEach((k, v) -> {

                    sbf.append(" AND ").append(k).append(" = '").append(v).append("'");
                });
                if (isUpdate) {
                    if (id != 0) {
                        sbf.append(" AND ").append("id").append(" !='").append(id).append("'");
                    } else {
                        return false;
                    }
                }
                List<Map<String, Object>> genTableColumnList = dcTableMapper.getGenTableColumnList(sbf.toString());
                return genTableColumnList.isEmpty();

            }
        }
    }

    @Override
    public void formatParam(Map<String, String> map, DCTable dcTable, Boolean isChange) {
//        SysUser sysUser = ShiroUtils.getSysUser();
        Date nowDate = DateUtils.getNowDate();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = sdf.format(nowDate);
        if (!isChange) {
            map.put("create_time", format);
            map.put("create_by", SecurityUtils.getUsername());
        }
        map.put("update_time", format);
        map.put("update_by", SecurityUtils.getUsername());
        // 业务表有权限时
//        if (dcTable.getTableDataHasAuth() != null && dcTable.getTableDataHasAuth() == 1) {
//            String groups = SecurityUtils.getUsername().toString();
////            map.put("read_ids", groups);
//            map.put("operation_ids", groups);
//        }
    }


    private Long getNextSortKey(DCTable table, Long currentKey) {
        String nextSql = "select * from " + table.getTableDbName() + " where sort_key > " + currentKey + " order by sort_key limit 1 ";
        Map<String, Object> next = dcTableMapper.selectUseTableDataBySql(nextSql);
        if (next != null) {
            return next.get("sort_key") != null ? new BigDecimal(next.get("sort_key").toString()).longValue() : null;
        } else {
            return null;
        }
    }

    private long getHalfIndex(Long minIndex, Long maxIndex, DCTable table) {
        Long halfIndex = -1L;
        halfIndex = (minIndex + maxIndex) / 2;
        // 如果最大和最小中间可以插入
        if (halfIndex > minIndex) {
            // 直接返回
        } else {
            // 中间已经无法插入，需要将最大下标后移（如果最大下标无法后移，要连带一部分一起后移）
            Long startKey = maxIndex;
            Long endKey = maxIndex;
            while (true) {
                Long nextKey = getNextSortKey(table, endKey);

                if (nextKey == null) {
                    break;
                }

                // 有1已经可以插入，但是为了减更新次数，提高效率这里设定间隔5
                if (nextKey - endKey < 5) {
                    endKey = nextKey;
                } else {
                    endKey = nextKey;
                    break;
                }
                startKey = endKey;
            }
            // 换算出需要更新增长的数字
            Long increment = (endKey - startKey) / 2;

            String updateSql = "update " + table.getTableDbName() + " set sort_key = sort_key + " + increment + " where sort_key >= " + maxIndex + " and sort_key <= " + endKey;
            int updateCnt = dcTableMapper.updateBySql(updateSql);
            if (updateCnt > 0) {
                halfIndex = (minIndex + maxIndex + increment) / 2;
            }
        }

        return halfIndex;
    }


    @Override
    @Transactional
    public Map<String, String> insertByMap(Map<String, String> param, String tableName) {
        dcTableMapper.insertByMap(param, tableName);
        return param;
    }

    @Override
    public Map<String, Object> selectUseTableDataById(Long id, DCTable table) {
        return dcTableMapper.selectById(id, table.getTableDbName());
    }

    @Override
    public int delUseTableData(String ids, Long tableId) {
        String[] strings = Convert.toStrArray(ids);
        DCTable dcTable = dcTableMapper.selectDcTableByTableId(tableId);

        if (strings.length == 1) {
            dcTableResultLogService.deleteById(Long.valueOf(strings[0]), tableId, null);
            return dcTableMapper.deleteById(Long.valueOf(strings[0]), dcTable.getTableDbName());

        } else if (strings.length > 1) {
            String batchId = UUID.randomUUID().toString().replace("-", "");
            for (String id : strings) {
                dcTableResultLogService.deleteById(Long.valueOf(id), tableId, batchId);
            }
            return dcTableMapper.deleteByIdS(strings, dcTable.getTableDbName());
        }
        return 0;
    }


    /**
     * 根据配置表导入数据
     *
     * @param dcTable 配置表
     * @param   file   上传文件
     * @return 结果
     */
    @Override
    @Transactional
    public AjaxResult ipmortByDCTable(DCTable dcTable, MultipartFile file, String batchId, Boolean isRequire) throws Exception {
        StringBuffer errorSbf = new StringBuffer();
        String fileRealName = file.getOriginalFilename();//获得原始文件名;
        String fileType = "";
        int startLine = Math.toIntExact(dcTable.getImportStart());
        if (fileRealName.contains("/")) {
            String[] fileArry = fileRealName.split("/");
            fileRealName = fileArry[fileArry.length - 1];
        }
//        // 获取文件名去掉扩展名
//        if (FileUtils.isExcel(fileRealName)) {
//            String [] fileArray = fileRealName.split("\\.");
//            fileRealName = fileArray[0];
//            fileType = fileArray[1];
//        }

        if (FileUtils.isExcel(fileRealName)) {
            String fileName = fileRealName;
            int dotIndex = fileName.lastIndexOf(".");
            if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
                fileRealName = fileName.substring(0, dotIndex); // 获取去掉扩展名的文件名
                fileType = fileName.substring(dotIndex + 1); // 获取扩展名
            }
        }

        // 原来的文件名字
        fileRealName = fileRealName + "." + fileType;

        Map<String, Integer> mtcMap = new LinkedHashMap<String, Integer>();
        Map<String, String> columnMap = new LinkedHashMap<String, String>();
        List<DCTableConfig> tableColumns = dcTableConfigService.getListImportColumns(dcTable.getTableId());

        Integer maxlevel = tableColumns.stream().max(Comparator.comparing(DCTableConfig::getMaxLevel)).get().getMaxLevel();
        //导出带id
        DCTableConfig idConfig = new DCTableConfig();
        idConfig.setTableId(dcTable.getTableId());
        idConfig.setColumnType(0);
        idConfig.setColumnName("id");
        idConfig.setColumnDbname("id");
        idConfig.setColumnSort(new Long(0));
        idConfig.setColumnDbtype(1);
        tableColumns.add(idConfig);
        dcTableService.formateImportColumns(tableColumns, mtcMap, columnMap);
        NnroadExcelUtil<Map> nnroadExcelUtil = new NnroadExcelUtil<Map>(Map.class);
        //读取excel中数据并且捕获错误信息
        List<Map> excelList = new ArrayList<>();
        try {
            List<String> skipKeyWords = skipKeywordsConfigMapper.selectSkipKeyWordsByTableId(dcTable.getTableId(), dcTable.getTableType());
//            excelList= nnroadExcelUtil.importExcelCustomize(dcTable.getTableName(), file.getInputStream(), mtcMap,maxlevel,0,isRequire, fileRealName, skipKeyWords);
            excelList = nnroadExcelUtil.importExcelCustomize(dcTable.getImportSheetName(), file.getInputStream(), mtcMap, maxlevel, startLine, isRequire, fileRealName, skipKeyWords);
        } catch (Exception e) {
            errorSbf.append(e.getMessage());
        }

        //遍历数据 mao为对象 一条一条插入。
        for (Map map : excelList) {
            Map<String, String> line = new HashMap<>();
            for (Object key : map.keySet()) {
                String value = map.get(key).toString();
                String realKey = columnMap.get(key);
                if (StringUtils.isNotEmpty(value)) {
                    if (DBTypeEnum.DATE.getCode().equals(mtcMap.get(key))) {
                        Date time = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.UK).parse(value);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        value = sdf.format(time);
                    }
                } else {
                    if (DBTypeEnum.INT.getCode().equals(mtcMap.get(key))) {
                        if (!org.apache.commons.lang3.StringUtils.isNotEmpty(value)) {
                            value = "0";
                        }
                    }
                }
                line.put(realKey, value);
            }

            //添加id
            if (line.get("id") == null) {
                String id = queryId(dcTable.getTableId(), line);
                if (id != null) {
                    line.put("id", id);
                }
            }

            Integer index = excelList.indexOf(map) + maxlevel + 1;
            if (!line.containsKey("id") || StringUtils.isEmpty(line.get("id")) || "0".equals(line.get("id"))) {
                Long sortKey = sequence.generate(dcTable.getTableDbName() + dcTable.getTableId(), SysConstants.SORT_KEY_INCREMENT);
                line.put("sort_key", sortKey.toString());
                formatParam(line, dcTable, false);
                //判断是否可插入
                try {
                    boolean canDo = findDataIsDuplicated(dcTable.getTableId(), line, true, new Long(0));
                    if (canDo) {
                        try {
                            dcTableService.insertByMap(line, dcTable.getTableDbName());
                            dcTableResultLogService.insertByOne(line, dcTable.getTableId(), batchId);
                        } catch (Exception e) {
                            //插入失败

                            errorSbf.append(fileRealName + " [" + dcTable.getTableName() + "]: The " + index + "th data is wrong, please modify the excel!\\n");
                        }
                    } else {
                        errorSbf.append(fileRealName + " [" + dcTable.getTableName() + "]: This row cannot be inserted because it contains duplicate data at row " + index + ".\\n");

                    }
                    //捕获存在的 必填项异常
                } catch (BusinessException e) {
                    errorSbf.append(fileRealName + " [" + dcTable.getTableName() + "]: The " + index + "th data is wrong, please modify the excel! Error: " + e.getMessage() + "\\n");

                }
            } else {
                formatParam(line, dcTable, true);
                try {
                    boolean canDo = findDataIsDuplicated(dcTable.getTableId(), line, true, Long.valueOf(line.get("id").toString()));
                    if (canDo) {
                        try {
                            dcTableResultLogService.updateByOne(line, dcTable, batchId);
                            dcTableService.updateByMap(line, dcTable.getTableDbName());
                        } catch (Exception e) {
                            System.out.println(e);
                            errorSbf.append(fileRealName + " [" + dcTable.getTableName() + "]: The " + index + "th data is wrong, please modify the excel!\\n");

                        }
                    } else {
                        errorSbf.append(fileRealName + " [" + dcTable.getTableName() + "]: " + "The " + index + "th data is duplicated and cannot be inserted.\\n");
                    }
                } catch (BusinessException e) {
                    errorSbf.append(fileRealName + " [" + dcTable.getTableName() + "]: " + "The " + index + "th data is wrong, please modify the excel!" + "\\n");
                }
            }
        }
        String errorMsg = errorSbf.toString();
        //如果有报错信息 则回滚数据。
        if (StringUtils.isNotEmpty(errorMsg)) {
            throw new BusinessException(errorMsg);
        }
        return AjaxResult.success();
    }

    /**
     * 根据配置表导入数据 特殊模式
     *
     * @param dcTable 配置表
     * @param   file   上传文件
     * @param batchId   批量id
     * @return 结果
     */
    @Override
    @Transactional
    public AjaxResult ipmortByDCTableBySpecial(DCTable dcTable, MultipartFile file, String batchId, Boolean isRequire) throws Exception {

        //获得原始文件名;
        String fileRealName = file.getOriginalFilename();
        String fileType = "";
        if (fileRealName.contains("/")) {
            String[] fileArry = fileRealName.split("/");
            fileRealName = fileArry[fileArry.length - 1];
        }
        // 获取文件名去掉扩展名
        if (FileUtils.isExcel(fileRealName)) {
            String[] fileArray = fileRealName.split("\\.");
            fileRealName = fileArray[0];
            fileType = fileArray[1];
        }

        // 原来的文件名字
        fileRealName = fileRealName + "." + fileType;
        //错误信息
        StringBuffer errorMsg = new StringBuffer();

        NnroadExcelUtil<Map> nnroadExcelUtil = new NnroadExcelUtil<Map>(Map.class);
        Map<String, Integer> mtcMap = new LinkedHashMap<String, Integer>();
        Map<String, String> columnMap = new LinkedHashMap<String, String>();
        List<DCTableConfig> tableColumns = dcTableConfigService.getListImportColumns(dcTable.getTableId());
        //获取特殊位置的值
        String specialColumnValue = "";
        String specialColumn = "";
        ArrayList<String> columnDBNameList = new ArrayList<>();
        for (DCTableConfig tableColumn : tableColumns) {
            if (tableColumn.getColumnDbname() != null) {
                columnDBNameList.add(tableColumn.getColumnDbname());
            }
        }
        if (dcTable.getImportType() != null && dcTable.getImportType() == 1) {
            //判断是否有 导入特殊字段
            if (StringUtils.isNotEmpty(dcTable.getImportSpecialColumn()) && columnDBNameList.contains(dcTable.getImportSpecialColumn())) {
                Long specialType = dcTable.getImportSpecialColumnType();
                //读取文件名后缀 xxxxxxxxxxxxxx-值.xls
                if (specialType != null && specialType == 1) {
                    String filename = file.getOriginalFilename();
                    String realFileName = filename.substring(0, filename.lastIndexOf("."));
                    String[] split = realFileName.split("-");
                    String datestr1 = split[split.length - 1];
                    boolean isNumber = StringUtils.isNumeric(datestr1);
                    DCTableConfig tableConfig = new DCTableConfig();
                    specialColumn = dcTable.getImportSpecialColumn();
                    tableConfig.setTableId(dcTable.getTableId());
                    tableConfig.setColumnDbname(specialColumn);
                    DCTableConfig specialColumnConfig = dcTableConfigMapper.selectParamIsExist(tableConfig);
                    if (specialColumnConfig != null && specialColumnConfig.getColumnDbtype() != null) {
                        if (DBTypeEnum.DATE.getCode().equals(specialColumnConfig.getColumnDbtype())) {
                            if (isNumber) {
                                String datestr;
                                try {
                                    datestr = formateStringToDateStr(datestr1, filename);
                                    specialColumnValue = datestr;
                                } catch (Exception e) {
                                    throw new BusinessException(e.getMessage());
                                }
                            } else {
                                throw new BusinessException(fileRealName + " [" + dcTable.getTableName() + "]: " + MessageUtils.message("datacenter.message.fileNameError") + "\\n");
                            }
                        } else {
                            if (specialColumnConfig.getColumnDblength() != null && datestr1.length() < specialColumnConfig.getColumnDblength()) {
                                specialColumnValue = datestr1;
                            } else if (specialColumnConfig.getColumnDblength() == null) {
                                specialColumnValue = datestr1;
                            } else {
                                throw new BusinessException(fileRealName + " [" + dcTable.getTableName() + "]: " + MessageUtils.message("datacenter.message.fileNameError") + "\\n");
                            }
                        }
                    }

                }
                //读取特定位置值
                else if (specialType != null && specialType == 2) {
                    String importSpecialColumnWhere = dcTable.getImportSpecialColumnWhere();
                    String[] split = importSpecialColumnWhere.split(",");
                    if (split.length == 2) {
                        specialColumn = dcTable.getImportSpecialColumn();
                        DCTableConfig tableConfig = new DCTableConfig();
                        tableConfig.setTableId(dcTable.getTableId());
                        tableConfig.setColumnDbname(specialColumn);
                        DCTableConfig specialColumnConfig = dcTableConfigMapper.selectParamIsExist(tableConfig);
                        if (specialColumnConfig != null) {
                            Object cellValue = nnroadExcelUtil.getValueByRowNumColNum(dcTable.getTableName(), file.getInputStream(), Integer.parseInt(split[0]) - 1, Integer.parseInt(split[1]) - 1);
                            if (DBTypeEnum.DATE.getCode().equals(specialColumnConfig.getColumnDbtype())) {

                                Date time = new SimpleDateFormat("yyyy-MM-dd").parse(cellValue.toString());
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                specialColumnValue = sdf.format(time);

                            } else {
                                specialColumnValue = cellValue.toString();
                            }
                        }

                    }
                }
            }
        }

        Integer maxlevel = tableColumns.stream().max(Comparator.comparing(DCTableConfig::getMaxLevel)).get().getMaxLevel();

        //对已经存在的数据覆盖

        dcTableService.formateImportColumns(tableColumns, mtcMap, columnMap);

        List<Map> excelList = new ArrayList<>();
        try {
            List<String> skipKeyWords = skipKeywordsConfigMapper.selectSkipKeyWordsByTableId(dcTable.getTableId(), dcTable.getTableType());
            excelList = nnroadExcelUtil.importExcelCustomize(dcTable.getTableName(), file.getInputStream(), mtcMap, maxlevel, dcTable.getImportStart().intValue(), isRequire, fileRealName, skipKeyWords);
        } catch (Exception e) {
            errorMsg.append(e.getMessage());
        }
        //格式化excel中读取的值
        for (Map map : excelList) {
            Map<String, String> line = new HashMap<>();
            for (Object key : map.keySet()) {
                String value = map.get(key).toString();
                String realKey = columnMap.get(key);
                if (StringUtils.isNotEmpty(value)) {
                    if (DBTypeEnum.DATE.getCode().equals(mtcMap.get(key))) {
                        //Date time = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.UK).parse(value);
                        Date time = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.US).parse(value);
                        ;
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        value = sdf.format(time);
                    }
                } else {
                    if (DBTypeEnum.INT.getCode().equals(mtcMap.get(key))) {
                        if (!StringUtils.isNotEmpty(value)) {
                            value = "0";
                        }
                    }
                }
                line.put(realKey, value);

            }
            Set<String> valueList = new HashSet<>();
            line.forEach((k, v) -> {
                valueList.add(v);
            });
            if (valueList.contains("")) {
                valueList.remove("");
            }
            if (valueList.contains("0")) {
                valueList.remove("0");
            }
            Boolean canADD = true;
            if (valueList.isEmpty()) {
                canADD = false;
            }
            //添加特殊参数
            if (dcTable.getImportType() != null && dcTable.getImportType() == 1 && specialColumn != "" && specialColumnValue != "") {
                line.put(specialColumn, specialColumnValue);
            }

            //添加id
            if (line.get("id") == null) {
                String id = queryId(dcTable.getTableId(), line);
                if (id != null) {
                    line.put("id", id);
                }
            }

            //是否能插入
            if (canADD) {
                Integer index = excelList.indexOf(map) + maxlevel + dcTable.getImportStart().intValue();
                if (!line.containsKey("id") || StringUtils.isEmpty(line.get("id")) || "0".equals(line.get("id"))) {
                    Long sortKey = sequence.generate(dcTable.getTableDbName() + dcTable.getTableId(), SysConstants.SORT_KEY_INCREMENT);
                    line.put("sort_key", sortKey.toString());
                    formatParam(line, dcTable, false);
                    //判断是否可插入
                    try {
                        boolean canDo = findDataIsDuplicated(dcTable.getTableId(), line, false, new Long(0));
                        if (canDo) {
                            try {
                                dcTableService.insertByMap(line, dcTable.getTableDbName());
                                dcTableResultLogService.insertByOne(line, dcTable.getTableId(), batchId);
                            } catch (Exception e) {
                                System.out.println(e);
                                errorMsg.append(fileRealName + " [" + dcTable.getTableName() + "]: " + MessageUtils.message("datacenter.message.dataErrorTip", index) + "\\n");
                                //throw new BusinessException(file.getOriginalFilename()+" ["+dcTable.getTableName()+"] "+ MessageUtils.message("datacenter.message.dataErrorTip",index));
                            }
                        } else {
                            errorMsg.append(fileRealName + " [" + dcTable.getTableName() + "]: " + MessageUtils.message("datacenter.message.dataErrorDuplicatedTip", index) + "\\n");
                            //throw new BusinessException(file.getOriginalFilename()+" ["+dcTable.getTableName()+"] "+ MessageUtils.message("datacenter.message.dataErrorDuplicatedTip",index));
                        }
                    } catch (BusinessException e) {
                        errorMsg.append(fileRealName + " [" + dcTable.getTableName() + "]: " + MessageUtils.message("datacenter.message.dataErrorTip", index) + "\\n");
                        //throw new BusinessException(file.getOriginalFilename()+" ["+dcTable.getTableName()+"] "+ MessageUtils.message("datacenter.message.dataErrorTip",index));
                    }
                } else {
                    formatParam(line, dcTable, true);
                    try {
                        boolean canDo = findDataIsDuplicated(dcTable.getTableId(), line, true, Long.valueOf(line.get("id").toString()));
                        if (canDo) {
                            try {
                                dcTableResultLogService.updateByOne(line, dcTable, batchId);
                                dcTableService.updateByMap(line, dcTable.getTableDbName());
                            } catch (Exception e) {
                                System.out.println(e);
                                errorMsg.append(fileRealName + " [" + dcTable.getTableName() + "]: " + MessageUtils.message("datacenter.message.dataErrorTip", index) + "\\n");
                            }
                        } else {
                            errorMsg.append(fileRealName + " [" + dcTable.getTableName() + "]: " + MessageUtils.message("datacenter.message.dataErrorDuplicatedTip", index) + "\\n");
                        }
                    } catch (BusinessException e) {
                        errorMsg.append(fileRealName + " [" + dcTable.getTableName() + "]: " + MessageUtils.message("datacenter.message.dataErrorTip", index) + "\\n");
                    }


                }
            }
        }
        String errorMsgS = errorMsg.toString();
        if (StringUtils.isNotEmpty(errorMsgS)) {
            throw new BusinessException(errorMsgS);
        }
        return AjaxResult.success();
    }

    @Override
    public void formateImportColumns(List<DCTableConfig> root, Map<String, Integer> mtcMap, Map<String, String> columnMap) {
        for (DCTableConfig config : root) {
            if (config.getColumnType() == 0) {
                if (mtcMap.containsKey(config.getColumnName())) {
                    //String  tempName=config.getColumnName() +"-"+ config.getColumnDbname();
                    String tempName = config.getColumnName();
                    //System.out.println("同级重复表头"+ config.getColumnName());
                    mtcMap.put(tempName, config.getColumnDbtype());
                    columnMap.put(tempName, config.getColumnDbname());
                } else {
                    mtcMap.put(config.getColumnName(), config.getColumnDbtype());
                    columnMap.put(config.getColumnName(), config.getColumnDbname());
                }

            } else {
                if (config.getChildList() != null) {
                    List<DCTableConfig> child = config.getChildList();
                    for (DCTableConfig dcTableConfig : child) {
                        dcTableConfig.setColumnName(config.getColumnName() + dcTableConfig.getColumnName());
                        //root.addAll(child);
                        if (dcTableConfig.getColumnType() == 0) {
                            if (mtcMap.containsKey(dcTableConfig.getColumnName())) {
                                String tempName = config.getColumnName();
                                System.out.println("同级重复表头" + dcTableConfig.getColumnName());
                                mtcMap.put(tempName, dcTableConfig.getColumnDbtype());
                                columnMap.put(tempName, dcTableConfig.getColumnDbname());
                            } else {
                                mtcMap.put(dcTableConfig.getColumnName(), dcTableConfig.getColumnDbtype());
                                columnMap.put(dcTableConfig.getColumnName(), dcTableConfig.getColumnDbname());
                            }
                        }
                    }

                    formateImportColumns(child, mtcMap, columnMap);
                }
            }
        }
    }

    /**
     * 查询id
     * @param tableId 表id
     * @param map   一条记录值
     */
    private String queryId(Long tableId, Map<String, String> map) {
        DCTable table = dcTableMapper.selectDcTableByTableId(tableId);
        DCTableConfig tableConfig = new DCTableConfig();
        tableConfig.setTableId(tableId);
        List<DCTableConfig> configList = dcTableConfigMapper.selectDCTableConfigList(tableConfig);
        List<DCTableConfig> isonlyList = new ArrayList<>();

        configList.forEach(x -> {
            if (x != null && StringUtils.isNotEmpty(x.getColumnDbname())) {
                if (x.getColumnIsonly() != null && x.getColumnIsonly() == 1) {
                    isonlyList.add(x);
                }
            }
        });
        if (isonlyList.size() <= 0) {
            return null;
        }


        StringBuffer sbf = new StringBuffer();
        sbf.append("SELECT ");
        Map<String, String> onlyValueMap = new HashMap<>();
        List<String> needOnlyList = new LinkedList<>();
        sbf.append("id,");
        for (int i = 0; i < isonlyList.size(); i++) {
            if (map.containsKey(isonlyList.get(i).getColumnDbname())) {
                onlyValueMap.put(isonlyList.get(i).getColumnDbname(), map.get(isonlyList.get(i).getColumnDbname()));
            } else {
                needOnlyList.add(isonlyList.get(i).getColumnName());
            }
            if (i < isonlyList.size() - 1) {
                sbf.append(isonlyList.get(i).getColumnDbname()).append(",");
            } else {
                sbf.append(isonlyList.get(i).getColumnDbname());
            }
        }
        sbf.append(" FROM ").append(table.getTableDbName());
        if (onlyValueMap.size() != isonlyList.size()) {
            String needOnly = needOnlyList.stream().collect(Collectors.joining(","));
            throw new BusinessException("These Columns " + needOnly + " are missing.");
        }

        sbf.append(" WHERE 1=1 ");
        onlyValueMap.forEach((k, v) -> {

            sbf.append(" AND ").append(k).append(" = '").append(v).append("'");
        });

        List<Map<String, Object>> genTableColumnList = dcTableMapper.getGenTableColumnList(sbf.toString());
        if (genTableColumnList.size() == 0) {
            return null;
        } else if (genTableColumnList.size() > 1) {
            throw new BusinessException("There are more than 1 record id db of only field.");
        }
        Map<String, Object> result = genTableColumnList.get(0);
        String id = String.valueOf(result.get("id"));
        return id;
    }


    @Override
    public void updateByMap(Map<String, String> map, String tableDbName) {
        //query value before update;
        String objectName = null;
        String id = null;
//        ServiceObjectType type = null;
        Map<String, Object> before = null;
//        if (ServiceObjectType.getTableNames().contains(tableDbName)) {
//            type = ServiceObjectType.getByTableName(tableDbName);
//            objectName = map.get(type.getObjectName());
//            id = map.get(type.getObjectIdName());
//            DCTable dcTable = selectDCTableByTableDbName(tableDbName);
//            before = selectUseTableDataById(NumberUtil.parseLong(id), dcTable);
//        }
        dcTableMapper.updateByMap(map, tableDbName);
//        if("dc_gt_crm_client_empoyee_info".equals(tableDbName)){
//            if (map.get("employment_contract")!=null&&!map.get("employment_contract").isEmpty()){
//                String temp=map.get("employment_contract");
//                if ("Client".equals(temp)){
//                    dcTableMapper.updateDc(map.get("ee_code"),"None",map.get("client_name"),"None");
//                }else if ("HROne SH".equals(temp)||"HROne BJ".equals(temp)||"FDI".equals(temp)){
//                    dcTableMapper.updateDc(map.get("ee_code"),temp,temp,"None");
//                }else {
//                    dcTableMapper.updateDc(map.get("ee_code"),temp,temp,temp);
//                }
//            }
//        }
        //add notice log
//        if (ServiceObjectType.getTableNames().contains(tableDbName)){
//            iServiceNoticeLogService.editOp(type, Long.valueOf(id),objectName, JSONObject.toJSONString(before),JSONObject.toJSONString(map));
//        }
    }


    /**
     * 查询配置表列表
     *
     * @param dcTable 配置表
     * @return 配置表
     */
    @Override
    public List<DCTable> selectDCTableList(DCTable dcTable) {
        Map<String, Object> params = dcTable.getParams();
//        Long userId = ShiroUtils.getSysUser().getUserId();
//        params.put("userId", userId);
        dcTable.setParams(params);
        return dcTableMapper.selectDCTableList(dcTable);
    }

    public String formateStringToDateStr(String value, String fileName) {
        String date = "";
        if (value != null && value.length() > 0) {
            if (value.length() == 4) {
                date = value + "0101";
            } else if (value.length() == 6) {
                date = value + "01";
            } else if (value.length() == 8) {
                date = value;
            }
        }
        Date time = null;
        try {
            time = new SimpleDateFormat("yyyyMMdd", Locale.UK).parse(date);
        } catch (Exception e) {
            throw new BusinessException(fileName + ": File name date format error \\n");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(time);
    }

}
