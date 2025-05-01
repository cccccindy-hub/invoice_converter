package com.nnroad.datacenter.common;

public enum ImportOptTypeEnum {
    //1.新增，2.修改,3.删除
    SINGLE("SINGLE","1"),
    Multi("Multi","2"),
    INV("INV","3");
    private String type;
    private String code;
    private ImportOptTypeEnum(String type, String code) {
        this.type = type;
        this.code = code;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
