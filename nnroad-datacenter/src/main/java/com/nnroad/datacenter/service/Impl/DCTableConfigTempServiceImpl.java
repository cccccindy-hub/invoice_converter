package com.nnroad.datacenter.service.Impl;


import cn.hutool.core.util.StrUtil;
import com.nnroad.common.core.domain.AjaxResult;
import com.nnroad.common.core.domain.Ztree;
import com.nnroad.common.exception.BusinessException;
import com.nnroad.common.utils.DateUtils;
import com.nnroad.common.utils.StringUtils;
import com.nnroad.datacenter.common.ColumnTypeEnum;
import com.nnroad.datacenter.common.DBTypeEnum;
import com.nnroad.datacenter.domain.DCTable;
import com.nnroad.datacenter.domain.DCTableConfigTemp;
import com.nnroad.datacenter.mapper.DCTableConfigMapper;
import com.nnroad.datacenter.mapper.DCTableConfigTempMapper;
import com.nnroad.datacenter.mapper.DCTableMapper;
import com.nnroad.datacenter.service.IDCTableConfigService;
import com.nnroad.datacenter.service.IDCTableConfigTempService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class DCTableConfigTempServiceImpl implements IDCTableConfigTempService {
    @Autowired
    private DCTableConfigTempMapper dcTableConfigTempMapper;
    @Autowired
    private DCTableConfigMapper dcTableConfigMapper;
    @Autowired
    private DCTableMapper dcTableMapper;

    @Autowired
    private IDCTableConfigService dcTableConfigService;

    /**
     * 查询配置表
     *
     * @param columnId 配置表ID
     * @return 配置表
     */
    @Override
    public DCTableConfigTemp selectDCTableConfigTempById(Long columnId) {
        return dcTableConfigTempMapper.selectDCTableConfigTempById(columnId);
    }

    /**
     * 查询配置表列表
     *
     * @param dcTableConfigTemp 配置表
     * @return 配置表
     */
    @Override
    public List<DCTableConfigTemp> selectDCTableConfigTempList(DCTableConfigTemp dcTableConfigTemp) {
        return dcTableConfigTempMapper.selectDCTableConfigTempList(dcTableConfigTemp);
    }

    /**
     * 新增配置表
     *
     * @param tempConfig 配置表
     * @return 结果
     */
    @Override
    public AjaxResult insertDCTableConfigTemp(DCTableConfigTemp tempConfig) {
        if (tempConfig.getTableId() != null) {
            Integer count = dcTableConfigTempMapper.selectParamIsExist(tempConfig);
            if (count != null && count > 0) {
                return AjaxResult.error("Duplicate configuration, please change Column Name  or Column Name(EN)!");
            }
            if (tempConfig.getColumnType() != null && tempConfig.getColumnType() == 0 && StringUtils.isNotEmpty(tempConfig.getColumnEnName())) {
                String dbName = dcTableConfigService.formateDBName(tempConfig.getColumnEnName());
                tempConfig.setColumnDbname(dbName);
                generateDbNameAndCheck(tempConfig);
            }
        }
        tempConfig.setCreateTime(DateUtils.getNowDate());
        int i = dcTableConfigTempMapper.insertDCTableConfigTemp(tempConfig);
        if (i > 0) {
            DCTable dcTable = new DCTable();
            dcTable.setTableId(tempConfig.getTableId());
            dcTable.setTableSyn(0);
            dcTableMapper.updateDCTable(dcTable);
            return AjaxResult.success();
        } else {
            return AjaxResult.error();
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult updateDCTableConfigTemp(DCTableConfigTemp temp) {
        Integer count = dcTableConfigTempMapper.selectParamIsExist(temp);
        if (count != null && count > 0) {
            return AjaxResult.error("Duplicate configuration, please change Column Name  or Column Name(EN)!");
        }
        if (StringUtils.isEmpty(temp.getColumnEnName())) {
            return AjaxResult.error("Please fill in Column name(EN)");
        } else if (StringUtils.isNumeric(temp.getColumnEnName())) {
            return AjaxResult.error("Please change Column name(EN)");
        }
        checkLength(temp);
        DCTableConfigTemp before = dcTableConfigTempMapper.selectDCTableConfigTempById(temp.getColumnId());
        if (before == null) {
            return AjaxResult.error("field not exist");
        }
        //1.英文名变更 2. 从head类型变更成basic
        if (!temp.isHeader() && StrUtil.isBlank(before.getColumnDbname()) || !StrUtil.equals(before.getColumnEnName(), temp.getColumnEnName())) {
            String dbName = dcTableConfigService.formateDBName(temp.getColumnEnName());
            temp.setColumnDbname(dbName);
            generateDbNameAndCheck(temp);
        }
        temp.setUpdateTime(DateUtils.getNowDate());
        int i = dcTableConfigTempMapper.updateDCTableConfigTemp(temp);
        if (i > 0) {
            DCTable dcTable = new DCTable();
            dcTable.setTableId(temp.getTableId());
            dcTable.setTableSyn(0);
            dcTableMapper.updateDCTable(dcTable);
            return AjaxResult.success();
        } else {
            return AjaxResult.error();
        }

    }

    @Override
    public int deleteDCTableConfigTempByIds(String ids) {
        return 0;
    }

    @Override
    public int deleteDCTableConfigTempById(Long columnId, Long tableId) {
        int i = dcTableConfigTempMapper.deleteDCTableConfigTempById(columnId);
        if (i > 0) {

            DCTable dcTable = new DCTable();
            dcTable.setTableId(tableId);
            dcTable.setTableSyn(0);
            return dcTableMapper.updateDCTable(dcTable);
        } else {
            return 0;
        }
    }

    @Override
    public List<Ztree> selectDCTableConfigTempTree(Long tableId) {
        return new ArrayList<>();
    }

    @Override
    public void findTempIsExist(Long tableId) {

    }

    @Override
    public int selectCountByParentId(Long columnId) {
        return 0;
    }

    @Override
    public List<DCTableConfigTemp> getTreeList(List<DCTableConfigTemp> list) {
        return new ArrayList<>();
    }

    @Override
    public AjaxResult batchAdd(Long pid, String ids, Long tableId) {
        return null;
    }

    public void generateDbNameAndCheck(DCTableConfigTemp tempConfig) {
        Integer count = dcTableConfigTempMapper.selectParamDbNameIsExist(tempConfig);
        if (count != null && count > 0) {
            Random random = new Random();
            tempConfig.setColumnDbname(tempConfig.getColumnDbname() + "_" + (random.nextInt(9) + 1));
            generateDbNameAndCheck(tempConfig);
        }
    }

    public void checkLength(DCTableConfigTemp config) {
        if (config != null && ColumnTypeEnum.BASE.getCode().equals(config.getColumnType())) {
            if (DBTypeEnum.CHAR.getCode().equals(config.getColumnDbtype()) && config.getColumnDblength() != null) {
                if (config.getColumnDblength() > 65535) {
                    throw new BusinessException("Character type data is too long， Maximum length is 65535!");
                }
            } else if (DBTypeEnum.INT.getCode().equals(config.getColumnDbtype()) && config.getColumnDblength() != null) {
                if (config.getColumnDblength() > 65) {
                    throw new BusinessException("Number type data is too long， max length is 65, min length is 5!");
                } else if (config.getColumnDblength() <= 4) {
                    throw new BusinessException("Number type data is too long， max length is 65, min length is 5!");
                }
            }
        }
    }

}