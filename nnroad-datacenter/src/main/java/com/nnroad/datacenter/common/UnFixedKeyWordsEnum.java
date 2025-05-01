package com.nnroad.datacenter.common;
//id小于-100
public enum UnFixedKeyWordsEnum {
    read_ids(-100l,"read_ids","read ids","`read_ids` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '查看权限组'"),
    operation_ids(-101l,"operation_ids","operation ids","`operation_ids` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '操作权限组'");
    private Long id;
    private String dbname;
    private String title;
    private String sql;

    UnFixedKeyWordsEnum(Long id, String dbname, String title, String sql) {
        this.id = id;
        this.dbname = dbname;
        this.title = title;
        this.sql = sql;
    }
    public static UnFixedKeyWordsEnum getConfig(Long id) {

        for (UnFixedKeyWordsEnum item : UnFixedKeyWordsEnum.values()) {
            if (item.getId().equals(id)){
                return item;
            }
        }
        return null;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

}
