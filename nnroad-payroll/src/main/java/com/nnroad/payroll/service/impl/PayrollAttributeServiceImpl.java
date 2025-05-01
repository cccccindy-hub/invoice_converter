package com.nnroad.payroll.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import com.nnroad.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nnroad.payroll.mapper.PayrollAttributeMapper;
import com.nnroad.payroll.domain.PayrollAttribute;
import com.nnroad.payroll.service.IPayrollAttributeService;

/**
 * Formula ConfiigService业务层处理
 * 
 * @author Sheng
 * @date 2025-02-08
 */
@Service
public class PayrollAttributeServiceImpl implements IPayrollAttributeService 
{
    @Autowired
    private PayrollAttributeMapper payrollAttributeMapper;

    /**
     * 查询Formula Confiig
     * 
     * @param id Formula Confiig主键
     * @return Formula Confiig
     */
    @Override
    public PayrollAttribute selectPayrollAttributeById(Long id)
    {
        return payrollAttributeMapper.selectPayrollAttributeById(id);
    }

    /**
     * 查询Formula Confiig列表
     * 
     * @param payrollAttribute Formula Confiig
     * @return Formula Confiig
     */
    @Override
    public List<PayrollAttribute> selectPayrollAttributeList(PayrollAttribute payrollAttribute)
    {
        return payrollAttributeMapper.selectPayrollAttributeList(payrollAttribute);
    }

    /**
     * 新增Formula Confiig
     * 
     * @param payrollAttribute Formula Confiig
     * @return 结果
     */
    @Override
    public int insertPayrollAttribute(PayrollAttribute payrollAttribute)
    {
        payrollAttribute.setCreateTime(DateUtils.getNowDate());
        return payrollAttributeMapper.insertPayrollAttribute(payrollAttribute);
    }

    /**
     * 修改Formula Confiig
     * 
     * @param payrollAttribute Formula Confiig
     * @return 结果
     */
    @Override
    public int updatePayrollAttribute(PayrollAttribute payrollAttribute)
    {
        payrollAttribute.setUpdateTime(DateUtils.getNowDate());
        return payrollAttributeMapper.updatePayrollAttribute(payrollAttribute);
    }

    /**
     * 批量删除Formula Confiig
     * 
     * @param ids 需要删除的Formula Confiig主键
     * @return 结果
     */
    @Override
    public int deletePayrollAttributeByIds(Long[] ids)
    {
        return payrollAttributeMapper.deletePayrollAttributeByIds(ids);
    }

    /**
     * 删除Formula Confiig信息
     * 
     * @param id Formula Confiig主键
     * @return 结果
     */
    @Override
    public int deletePayrollAttributeById(Long id)
    {
        return payrollAttributeMapper.deletePayrollAttributeById(id);
    }

    @Override
    public List<PayrollAttribute> buildTree(List<PayrollAttribute> fieldList) {
        // 创建一个 map 用于存储 id 和对应的 PayrollAttribute
        Map<Long, PayrollAttribute> attributeMap = new HashMap<>();
        List<PayrollAttribute> tree = new ArrayList<>();

        // 填充 map
        for (PayrollAttribute attribute : fieldList) {
            attributeMap.put(attribute.getId(), attribute);
            // 确保每个 PayrollAttribute 都有一个 children 字段
            if (attribute.getChildList() == null) {
                attribute.setChildList(new ArrayList<>());
            }
        }

        // 遍历 fieldList，将子节点添加到父节点的 childList 中
        for (PayrollAttribute attribute : fieldList) {
            if (attribute.getFieldParentId() == null) {
                // 如果没有父级，说明是顶级节点，添加到 tree 中
                tree.add(attribute);
            } else {
                // 否则，将当前节点添加到其父节点的 childList 中
                PayrollAttribute parent = attributeMap.get(attribute.getFieldParentId());
                if (parent != null) {
                    parent.getChildList().add(attribute);
                }
            }
        }
        return tree;
    }
}
