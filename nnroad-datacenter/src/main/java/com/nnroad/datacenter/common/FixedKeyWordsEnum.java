package com.nnroad.datacenter.common;

//id小于0 大于-100
public enum FixedKeyWordsEnum {
    status(-1l,"status","Status","`status` int DEFAULT '0' COMMENT '状态特殊标记字段'"),
    sort_key(-2l,"sort_key","sort key","`sort_key` decimal(12,2) DEFAULT '0.00' COMMENT '排序用字段'"),
    create_by(-3l,"create_by","create by","`create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '创建者'"),
    create_time(-4l,"create_time","create time","`create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间'"),
    update_by(-5l,"update_by","update by","`update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '更新者'"),
    update_time(-6l,"update_time","update time","`update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间'"),
    extra_data(-7L, "extra_data", "extra data", "`extra_data` JSON DEFAULT NULL COMMENT '扩展数据字段'");


    private String dbname;
    private Long id;
    private String title;
    private String sql;

    FixedKeyWordsEnum(Long id, String dbname, String title, String sql) {
        this.dbname = dbname;
        this.id = id;
        this.title = title;
        this.sql = sql;
    }

    public String getDbname() {
        return dbname;
    }

    public void setDbname(String dbname) {
        this.dbname = dbname;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public static FixedKeyWordsEnum getConfig(Long id) {

        for (FixedKeyWordsEnum item : FixedKeyWordsEnum.values()) {
             if (item.getId().equals(id)){
                 return item;
             }
        }
        return null;
    }
}
