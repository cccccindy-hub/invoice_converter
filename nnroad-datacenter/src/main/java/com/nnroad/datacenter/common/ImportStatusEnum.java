package com.nnroad.datacenter.common;

public enum ImportStatusEnum {
    //1.成功，2.失败,3.rollback
    success("success","1"),
    failed("failed","2"),
    rollback("rollback","3");
    private String type;
    private String code;


    private ImportStatusEnum(String type, String code) {
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
