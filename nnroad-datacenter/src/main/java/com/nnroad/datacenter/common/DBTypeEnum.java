package com.nnroad.datacenter.common;

import com.alibaba.fastjson2.JSON;

public enum DBTypeEnum {
    //1.数字  0.字符 2.日期
    CHAR("CHAR",0),
    INT("INT",1),
    DATE("DATE",2),
    JSON("JSON",3);
    private String type;
    private Integer code;
    private DBTypeEnum(String type, Integer code) {
         this.type = type;
         this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
