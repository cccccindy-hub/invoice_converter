package com.nnroad.payroll.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.nnroad.common.core.domain.entity.SysUser;
import com.nnroad.common.core.page.TableDataInfo;
import com.nnroad.common.utils.DateUtils;
import com.nnroad.common.utils.SecurityUtils;
import com.nnroad.datacenter.domain.DCTable;
import com.nnroad.datacenter.mapper.DCTableMapper;
import com.nnroad.payroll.domain.PayrollAttribute;
import com.nnroad.payroll.mapper.PayrollAttributeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nnroad.payroll.mapper.PayrollOtherTableMapper;
import com.nnroad.payroll.domain.PayrollOtherTable;
import com.nnroad.payroll.service.IPayrollOtherTableService;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * payrollService业务层处理
 * 
 * @author Sheng
 * @date 2025-02-06
 */
@Service
public class PayrollOtherTableServiceImpl implements IPayrollOtherTableService 
{
    @Autowired
    private PayrollOtherTableMapper payrollOtherTableMapper;
    @Autowired
    private PayrollAttributeServiceImpl payrollAttributeServiceImpl;

    @Autowired
    private PayrollAttributeMapper payrollAttributeMapper;

    @Autowired
    private DCTableMapper dcTableMapper;

    private static final Pattern FIELD_PATTERN = Pattern.compile("\\b([a-zA-Z_][a-zA-Z0-9_]*)\\b");
    private static final List<String> SQL_RESERVED_WORDS = Arrays.asList(
            "SELECT", "FROM", "TO", "WHERE", "ORDER", "GROUP", "BY", "AS",
            "JOIN", "TABLE", "COLUMN", "INDEX", "ALTER", "CREATE", "DROP"
    );


    /**
     * 查询payroll
     * 
     * @param id payroll主键
     * @return payroll
     */
    @Override
    public PayrollOtherTable selectPayrollOtherTableById(Long id)
    {
        return payrollOtherTableMapper.selectPayrollOtherTableById(id);
    }

    /**
     * 查询payroll列表
     * 
     * @param payrollOtherTable payroll
     * @return payroll
     */
    @Override
    public List<PayrollOtherTable> selectPayrollOtherTableList(PayrollOtherTable payrollOtherTable)
    {
        return payrollOtherTableMapper.selectPayrollOtherTableList(payrollOtherTable);
    }

    /**
     * 新增payroll
     * 
     * @param payrollOtherTable payroll
     * @return 结果
     */
    @Override
    public int insertPayrollOtherTable(PayrollOtherTable payrollOtherTable)
    {
        SysUser sysUser=SecurityUtils.getLoginUser().getUser();
        payrollOtherTable.setCreateBy(sysUser.getUserName());
        payrollOtherTable.setCreateTime(DateUtils.getNowDate());
        return payrollOtherTableMapper.insertPayrollOtherTable(payrollOtherTable);
    }

    /**
     * 修改payroll
     * 
     * @param payrollOtherTable payroll
     * @return 结果
     */
    @Override
    public int updatePayrollOtherTable(PayrollOtherTable payrollOtherTable)
    {
        payrollOtherTable.setUpdateBy(SecurityUtils.getUsername());
        return payrollOtherTableMapper.updatePayrollOtherTable(payrollOtherTable);
    }

    /**
     * 批量删除payroll
     * 
     * @param ids 需要删除的payroll主键
     * @return 结果
     */
    @Override
    public int deletePayrollOtherTableByIds(Long[] ids)
    {

        return payrollOtherTableMapper.deletePayrollOtherTableByIds(ids);
    }

    /**
     * 删除payroll信息
     * 
     * @param id payroll主键
     * @return 结果
     */
    @Override
    public int deletePayrollOtherTableById(Long id)
    {
        return payrollOtherTableMapper.deletePayrollOtherTableById(id);
    }

    @Override
    public TableDataInfo calculateFormula(PayrollOtherTable payrollOtherTable) {

        TableDataInfo rspData = new TableDataInfo();

        PayrollOtherTable otherTable=payrollOtherTableMapper.selectPayrollOtherTableById(payrollOtherTable.getId());

        PayrollAttribute payrollAttribute=new PayrollAttribute();
        payrollAttribute.setOtherTableId(otherTable.getId());
        payrollAttribute.setHeader(0);
        List<PayrollAttribute> formulas = payrollAttributeMapper.selectPayrollAttributeList(payrollAttribute);
        StringBuilder sql = new StringBuilder("SELECT id");
        for (PayrollAttribute formula : formulas) {
            // 判断formula.getFieldFormula()  是否为空，如果为空就跳过当前循环
            if (formula.getFieldFormula() == null || formula.getFieldFormula().isEmpty()) {
                continue; // 跳过当前循环，进入下一个 formula
            }
            String fieldName = escapeField(formula.getFieldName());
            String formulaExpr = escapeFormulaFields(formula.getFieldFormula());
            sql.append(", ").append(formulaExpr).append(" AS ").append(fieldName);
        }

        DCTable dctable=dcTableMapper.selectDcTableByTableId(otherTable.getTableId());
        sql.append(" FROM ").append(dctable.getTableDbName());

//        分页语句

        int pageNum= (int) payrollOtherTable.getParams().get("pageNum");
        int pageSize= (int) payrollOtherTable.getParams().get("pageSize");
//        计算分页的 offset
        int offset = (pageNum - 1) * pageSize;

        // 添加分页条件：LIMIT 和 OFFSET
        sql.append(" LIMIT ").append(pageSize).append(" OFFSET ").append(offset);

        List<Map<String, Object>> result=payrollOtherTableMapper.selectBySql(sql);
        String countSql="select count(1) from " + dctable.getTableDbName();
        Long count = payrollOtherTableMapper.getDataCount(countSql);

        rspData.setCode(0);
        rspData.setRows(result);
        rspData.setTotal(count);
        return rspData;
    }

    // 辅助方法
    private String escapeField(String field) {
        if (isReservedWord(field) || !field.matches("^[a-zA-Z_][a-zA-Z0-9_]*$")) {
            return "`" + field + "`";
        }
        return field;
    }

    private boolean isReservedWord(String word) {
        return SQL_RESERVED_WORDS.contains(word.toUpperCase());
    }

    private String escapeFormulaFields(String formula) {
        Matcher matcher = FIELD_PATTERN.matcher(formula);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String field = matcher.group(1);
            matcher.appendReplacement(sb, escapeField(field));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }


}
