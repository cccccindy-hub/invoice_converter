package com.nnroad.datacenter.common;

public enum OperateTypeEnum {
    //1.新增，2.修改,3.删除
    INSERT("insert",1),
    UPDATE("update",2),
    DELETE("delete",3);
    private String type;
    private Integer code;
    private OperateTypeEnum(String type, Integer code) {
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
