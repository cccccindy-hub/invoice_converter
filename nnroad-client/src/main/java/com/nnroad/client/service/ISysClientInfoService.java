package com.nnroad.client.service;

import java.util.List;
import com.nnroad.client.domain.ClientInfo;

/**
 * 【请填写功能名称】Service接口
 *
 * @author ruoyi
 * @date 2024-11-08
 */
public interface ISysClientInfoService
{
    /**
     * 查询【请填写功能名称】
     *
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    public ClientInfo selectClientInfoById(Long id);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param clientInfo 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<ClientInfo> selectClientInfoList(ClientInfo clientInfo);

    /**
     * 新增【请填写功能名称】
     *
     * @param clientInfo 【请填写功能名称】
     * @return 结果
     */
    public int insertClientInfo(ClientInfo clientInfo);

    /**
     * 修改【请填写功能名称】
     *
     * @param clientInfo 【请填写功能名称】
     * @return 结果
     */
    public int updateClientInfo(ClientInfo clientInfo);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param ids 需要删除的【请填写功能名称】主键集合
     * @return 结果
     */
    public int deleteClientInfoByIds(Long[] ids);

    /**
     * 删除【请填写功能名称】信息
     *
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    public int deleteClientInfoById(Long id);

}
