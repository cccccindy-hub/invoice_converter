package com.nnroad.datacenter.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.nnroad.common.constant.GenConstants;
import com.nnroad.common.exception.BusinessException;
import com.nnroad.datacenter.domain.DCDictData;
import com.nnroad.datacenter.domain.DCTableColumn;
import com.nnroad.datacenter.domain.DCTableConfig;
import com.nnroad.datacenter.domain.DCTableConfigTemp;
import com.nnroad.datacenter.mapper.DCTableConfigMapper;
import com.nnroad.datacenter.mapper.DCTableConfigTempMapper;
import com.nnroad.datacenter.service.IDCTableConfigService;
import com.nnroad.dict.mapper.DCDictDataMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DCTableConfigServiceImpl implements IDCTableConfigService {

    @Autowired
    private DCTableConfigMapper dcTableConfigMapper;

    @Autowired
    private DCTableConfigTempMapper dcTableConfigTempMapper;

    @Autowired
    private DCDictDataMapper dictDataMapper;


    @Override
    public List<List<DCTableColumn>> getListColumns(Long tableId, int tempFlag) {
        List<DCTableConfig> allConfigs = configList(tableId, tempFlag);
        if (CollUtil.isEmpty(allConfigs)) {
            return new ArrayList<>();
        }
        List<DCTableConfig> root = new ArrayList<>();
        for (DCTableConfig config : allConfigs) {
            if (config.getColumnParentDbname() == null) {
                config.setColumnLevel(1);
                config.setMaxLevel(1);
                getTree(allConfigs, config, config, 1);
                root.add(config);
            }
        }
        return formateColumn(root);
    }

    @Override
    public List<DCTableConfig> columnList(Long tableId, int tempFlag) {

        return configList(tableId, tempFlag);
    }

    private List<DCTableConfig> configList(Long tableId, int tempFlag) {
        List<DCTableConfig> allConfigs;
        if (tempFlag == 0) {
            DCTableConfig queryConfig = new DCTableConfig();
            queryConfig.setTableId(tableId);
            allConfigs = dcTableConfigMapper.selectDCTableConfigList(queryConfig);
        } else {
            DCTableConfigTemp dcTableConfigTemp = new DCTableConfigTemp();
            dcTableConfigTemp.setTableId(tableId);
            List<DCTableConfigTemp> dcTableConfigTemps = dcTableConfigTempMapper.selectDCTableConfigTempList(dcTableConfigTemp);
            if (dcTableConfigTemps == null) {
                dcTableConfigTemps = new ArrayList<>();
            }
            allConfigs = BeanUtil.copyToList(dcTableConfigTemps, DCTableConfig.class);
        }
        return allConfigs;
    }

    /**
     * 查询表字段配置列表
     *
     * @param dcTableConfig 表字段配置
     * @return 表字段配置
     */
    @Override
    public List<DCTableConfig> selectDCTableConfigList(DCTableConfig dcTableConfig) {
        return dcTableConfigMapper.selectDCTableConfigList(dcTableConfig);
    }

    @Override
    public Map<String, List<DCDictData>> getDictList(List<String> dictList) {
        if (dictList.size() > 0) {
            List<DCDictData> dictDatalist = dictDataMapper.selectDictDataByDictList(dictList);
            // 临时对应：客户信息字典值从客户信息表中取得
            Map<String, List<DCDictData>> collect = dictDatalist.stream().collect(Collectors.groupingBy(DCDictData::getDictType));
            return collect;
        }
        return null;
    }

    @Override
    public String formateDBName(String name) {
        //特殊字符 正则
        String regEx = "[\n`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）—\\-—+|{}【】‘；：”“’。，、？]";
        String REGEX_CHINESE = "[\u4e00-\u9fa5]";
        //小写 去除特殊字符 去除中文
        String s = name.toLowerCase(Locale.ROOT).replaceAll(regEx, "").replaceAll(REGEX_CHINESE, "").trim();

        //空格替换成_
        String s1 = s.replaceAll("\\s+", "_");
        String[] columnName = GenConstants.COLUMNNAME_NOT_QUERY;
        for (String change : columnName) {
            if (change.equals(s1)) {
                throw new BusinessException(MessageFormat.format("Import settings error! The column does not exist in the business table", change));
            }
        }
        return s1;
    }

    private void getTree(List<DCTableConfig> allConfigs, DCTableConfig father, DCTableConfig rootFahter, int level) {
        List<DCTableConfig> root = new ArrayList<>();

        for (DCTableConfig config : allConfigs) {
            if (father.getColumnId().equals(config.getColumnParentDbname())) {
                config.setColumnLevel(level + 1);
                root.add(config);
            }
        }
        father.setChildList(root);

        if (root.size() > 0) {
            rootFahter.setMaxLevel(level + 1);
            for (DCTableConfig config : root) {
                getTree(allConfigs, config, rootFahter, level + 1);
            }
        }

    }

    /**
     * @param root 包含了层级关系的列表
     * @return 所有配置表信息
     */
    private List<List<DCTableColumn>> formateColumn(List<DCTableConfig> root) {
        for (DCTableConfig config : root) {
            getTreeCount(config, config);
        }
        Integer maxlevel = root.stream().max(Comparator.comparing(DCTableConfig::getMaxLevel)).get().getMaxLevel();
        List<DCTableColumn> lineColumn = new ArrayList<>();
        List<List<DCTableColumn>> result = new ArrayList<>();
        lineFormate(root, maxlevel, result, true);
        return result;
    }

    /**
     * 获得每个节点个数量
     *
     * @param config 下一阶段查询的子对象  若无则使用查询对象
     * @param root   查询对象
     * @return 所有配置表信息
     */
    public void getTreeCount(DCTableConfig config, DCTableConfig root) {
        //当时基本项目时 ，查询节点的所包含的数目+1
        if (config.getColumnType() == 0) {
            root.setColumnCount((root.getColumnCount() == null ? 0 : root.getColumnCount()) + 1);
            //表头项目----- 子列表如果有基本项目 加上子列表所有的基本项目的数量
        } else {
            List<DCTableConfig> childList = config.getChildList();
            if (childList != null && childList.size() > 0) {
                Map<Integer, List<DCTableConfig>> collect = childList.stream().collect(Collectors.groupingBy(x -> x.getColumnType()));
                root.setColumnCount((root.getColumnCount() == null ? 0 : root.getColumnCount()) + (collect.containsKey(0) ? collect.get(0).size() : 0));
                if (collect.containsKey(1)) {
                    List<DCTableConfig> list = collect.get(1);
                    if (list != null && list.size() > 0) {
                        for (DCTableConfig dcTableConfig : list) {
                            getTreeCount(dcTableConfig, root);
                        }
                    }
                }
            }
        }
    }

    /**
     * 格式化 bootstrap-table 表头 每行所需要的数据格式
     *
     * @param lineList 该行需要格式化的列表
     * @param maxLevel 最大层级
     * @param isFirst  是否需要格式化  ， ture不需要  false 需要
     * @return 所有配置表信息
     */
    private void lineFormate(List<DCTableConfig> lineList, Integer maxLevel, List<List<DCTableColumn>> resultList, boolean isFirst) {
        List<DCTableColumn> lineColumn = new ArrayList<>();
        List<DCTableConfig> childList = new ArrayList<>();
        if (!isFirst) {
            for (DCTableConfig config : lineList) {
                getTreeCount(config, config);
            }
        }
        for (DCTableConfig config : lineList) {
            if (config.getColumnType() == 0) {
                DCTableColumn dcTableColumn = new DCTableColumn();
                dcTableColumn.setAlign("center");
                dcTableColumn.setValign("middle");
                dcTableColumn.setField(config.getColumnDbname());
                dcTableColumn.setTitle(config.getColumnName());
                dcTableColumn.setRowspan(maxLevel);
                dcTableColumn.setColumnId(config.getColumnId());
                dcTableColumn.setParentId(config.getColumnParentDbname());
                dcTableColumn.setDataType(config.getColumnDbtype());
                dcTableColumn.setDictList(config.getDictList());
                lineColumn.add(dcTableColumn);
            } else {
                DCTableColumn dcTableColumn = new DCTableColumn();
                dcTableColumn.setAlign("center");
                dcTableColumn.setValign("middle");
                dcTableColumn.setField(config.getColumnDbname());
                dcTableColumn.setTitle(config.getColumnName());
                dcTableColumn.setColspan(config.getColumnCount());
                lineColumn.add(dcTableColumn);
                dcTableColumn.setDataType(config.getColumnDbtype());
                dcTableColumn.setDictList(config.getDictList());
                dcTableColumn.setColumnId(config.getColumnId());
                dcTableColumn.setParentId(config.getColumnParentDbname());
                childList.addAll(config.getChildList());
            }
        }
        resultList.add(lineColumn);
        if (maxLevel - 1 > 0) {
            lineFormate(childList, maxLevel - 1, resultList, false);
        }
    }

    @Override
    public List<DCTableConfig> getListImportColumns(Long tableId) {
        List<DCTableConfig> allConfigs = new ArrayList<>();
            /*DCTableConfigTemp dcTableConfigTemp = new DCTableConfigTemp();
            dcTableConfigTemp.setTableId(tableId);
            List<DCTableConfigTemp> dcTableConfigTemps = dcTableConfigTempMapper.selectDCTableConfigTempList(dcTableConfigTemp);
            for (DCTableConfigTemp tableConfigTemp : dcTableConfigTemps) {
                DCTableConfig dcTableConfig = new DCTableConfig();
                BeanUtils.copyProperties(tableConfigTemp,dcTableConfig);
                allConfigs.add(dcTableConfig);
            }*/
        DCTableConfig queryConfig = new DCTableConfig();
        queryConfig.setTableId(tableId);
        allConfigs = dcTableConfigMapper.selectDCTableConfigList(queryConfig);
        if (allConfigs.size() < 1) {
            return null;
        }
        List<DCTableConfig> root = new ArrayList<>();
        for (DCTableConfig config : allConfigs) {
            if (config.getColumnParentDbname() == null) {
                config.setColumnLevel(1);
                config.setMaxLevel(1);
                getTree(allConfigs, config, config, 1);
                root.add(config);
            }
        }
        return root;
    }


}
