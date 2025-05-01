package com.nnroad.datacenter.common;

public enum ColumnTypeEnum {
    //0.基本项目  1.表头项目
    BASE("base",0),
    TITTLE("tittle",1);

    private String type;
    private Integer code;
    private ColumnTypeEnum(String type, Integer code) {
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
