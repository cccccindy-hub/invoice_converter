package com.nnroad.client.mapper;

import java.util.List;
import com.nnroad.client.domain.ClientInfo;
import org.apache.ibatis.annotations.Param;

/**
 * 【请填写功能名称】Mapper接口
 *
 * @author ruoyi
 * @date 2024-11-08
 */
public interface ClientInfoMapper
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
     * @param ClientInfo 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<ClientInfo> selectClientInfoList(ClientInfo ClientInfo);

    /**
     * 新增【请填写功能名称】
     *
     * @param ClientInfo 【请填写功能名称】
     * @return 结果
     */
    public int insertClientInfo(ClientInfo ClientInfo);

    /**
     * 修改【请填写功能名称】
     *
     * @param ClientInfo 【请填写功能名称】
     * @return 结果
     */
    public int updateClientInfo(ClientInfo ClientInfo);

    /**
     * 删除【请填写功能名称】
     *
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    public int deleteClientInfoById(Long id);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteClientInfoByIds(Long[] ids);


}
