package com.nnroad.datacenter.common;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum TableColumnRequiredEnum {
    //列表填写colum_bdName
    /**普通业务表*/
    NORMAL("",1),
    /**账单业务表*/
    INVOICE("client_code,client_name,ee_code,ee_name,pay_period_from,pay_period_to",2),
    /**薪酬业务表*/
    PAYSTUB("client_code,client_name,ee_code,ee_name,pay_period_from,pay_period_to",3);



//    FINANCE("date,service_name,check_type,client_name,client_code,for_period,bank,bank_account,wanted_currency,in_bank_currency,in_bank_amount,exchange_rate, wanted_currency_amount",9);


    private String columns;
    private Integer tableTypeCode;

    TableColumnRequiredEnum(String columns, Integer tableTypeCode) {
        this.columns = columns;
        this.tableTypeCode = tableTypeCode;
    }

    public static List<Map<String, String>> toList() {
        List<Map<String, String>> list = new ArrayList<>();//Lists.newArrayList()其实和new ArrayList()几乎一模
        for (TableColumnRequiredEnum item : TableColumnRequiredEnum.values()) {
            Map<String, String> map =new HashMap<>();
            map.put("tableTypeCode", item.getTableTypeCode().toString());
            map.put("columns", item.getColumns());
            list.add(map);
        }
        return list;
    }

    public String getColumns() {
        return columns;
    }

    public void setColumns(String columns) {
        this.columns = columns;
    }

    public Integer getTableTypeCode() {
        return tableTypeCode;
    }

    public void setTableTypeCode(Integer tableTypeCode) {
        this.tableTypeCode = tableTypeCode;
    }
}
