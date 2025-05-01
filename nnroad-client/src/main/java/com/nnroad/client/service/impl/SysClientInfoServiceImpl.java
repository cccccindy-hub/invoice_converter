package com.nnroad.client.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nnroad.client.mapper.ClientInfoMapper;
import com.nnroad.client.domain.ClientInfo;
import com.nnroad.client.service.ISysClientInfoService;

/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author nnroad
 * @date 2024-11-08
 */
@Service
public class SysClientInfoServiceImpl implements ISysClientInfoService
{
    @Autowired
    private ClientInfoMapper clientInfoMapper;

    /**
     * 查询【请填写功能名称】
     *
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    @Override
    public ClientInfo selectClientInfoById(Long id)
    {
        return clientInfoMapper.selectClientInfoById(id);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param clientInfo 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<ClientInfo> selectClientInfoList(ClientInfo clientInfo)
    {
        return clientInfoMapper.selectClientInfoList(clientInfo);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param clientInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertClientInfo(ClientInfo clientInfo)
    {
        return clientInfoMapper.insertClientInfo(clientInfo);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param clientInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateClientInfo(ClientInfo clientInfo)
    {
        return clientInfoMapper.updateClientInfo(clientInfo);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param ids 需要删除的【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteClientInfoByIds(Long[] ids)
    {
        return clientInfoMapper.deleteClientInfoByIds(ids);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteClientInfoById(Long id)
    {
        return clientInfoMapper.deleteClientInfoById(id);
    }


}
