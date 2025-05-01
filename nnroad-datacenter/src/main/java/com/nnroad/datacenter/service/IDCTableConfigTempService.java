package com.nnroad.datacenter.service;


import com.nnroad.common.core.domain.AjaxResult;
import com.nnroad.common.core.domain.Ztree;
import com.nnroad.datacenter.domain.DCTableConfigTemp;

import java.util.List;

public interface IDCTableConfigTempService {
    /**
     * 查询配置表
     *
     * @param columnId 配置表ID
     * @return 配置表
     */
    public DCTableConfigTemp selectDCTableConfigTempById(Long columnId);

    /**
     * 查询配置表列表
     *
     * @param dcTableConfigTemp 配置表
     * @return 配置表集合
     */
    public List<DCTableConfigTemp> selectDCTableConfigTempList(DCTableConfigTemp dcTableConfigTemp);

    /**
     * 新增配置表
     *
     * @param dcTableConfigTemp 配置表
     * @return 结果
     */
    public AjaxResult insertDCTableConfigTemp(DCTableConfigTemp dcTableConfigTemp);

    /**
     * 修改配置表
     *
     * @param dcTableConfigTemp 配置表
     * @return 结果
     */
    public AjaxResult updateDCTableConfigTemp(DCTableConfigTemp dcTableConfigTemp);

    /**
     * 批量删除配置表
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteDCTableConfigTempByIds(String ids);

    /**
     * 删除配置表信息
     *
     * @param columnId 配置表ID
     * @return 结果
     */
    public int deleteDCTableConfigTempById(Long columnId,Long tableId);

    /**
     * 查询配置表树列表
     *
     * @return 所有配置表信息
     * @param tableId
     */
    public List<Ztree> selectDCTableConfigTempTree(Long tableId);
    /**
     * 查询临时表是否有数据  没有则复制主表数据
     *
     * @return 所有配置表信息
     */
    void findTempIsExist(Long tableId);



    int selectCountByParentId(Long columnId);

    List<DCTableConfigTemp> getTreeList(List<DCTableConfigTemp> list);
    /**
     * 批量插入配置
     * @param pid 父级菜单id
     * @param ids 默认配置id
     * @param tableId
     * @return 结果
     */
    AjaxResult batchAdd(Long pid, String ids, Long tableId);
}
