package com.nnroad.datacenter.common;


import com.nnroad.common.utils.MessageUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//业务表类型
public enum TableTypeEnum {

    /**普通业务表	 1*/
//    NORMAL("normal business table", 1L),
//    /**账单业务表	2*/
//    INVOICE("billing business table",2L),
    /**薪资业务表	3*/
    PAYSTUB("payroll business table",3L),
    /** 供应商账单表 4 */
    VENDOR("vendor bill business table",4L);
    /** 财务表 4 */
//    FINANCE_ACCOUNT("finance account",9L),
//    FINANCE_REPORT("finance report",10L),
//    /**系统内置表	3*/
//    SYSTEM("system Table",99L);



    private String type;
    private Long code;

    private TableTypeEnum(String type, Long code) {
        this.type = type;
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }
    public static String getTypeName(Long code) {
        for (TableTypeEnum item : TableTypeEnum.values()) {
            if (item.code.equals(code)){
                return item.getType();
            }
        }
         return "";
    }

    public static List<Map<String, Object>> toList() {
        List<Map<String, Object>> list = new ArrayList<>();//Lists.newArrayList()其实和new ArrayList()几乎一模
        for (TableTypeEnum item : TableTypeEnum.values()) {
            Map<String, Object> map =new HashMap<>();
            map.put("dictLabel", item.getType());
            map.put("dictValue", item.getCode());
            list.add(map);
        }
        return list;
    }


    public static List<Map<String, Object>> toListWithOut(List<Long> without) {
        List<Map<String, Object>> list = new ArrayList<>();//Lists.newArrayList()其实和new ArrayList()几乎一模
        for (TableTypeEnum item : TableTypeEnum.values()) {
            if(!without.contains(item.getCode())){
                Map<String, Object> map =new HashMap<>();
                map.put("dictLabel", item.getType());
                map.put("dictValue", item.getCode());
                list.add(map);
            }
        }
        return list;
    }
}
