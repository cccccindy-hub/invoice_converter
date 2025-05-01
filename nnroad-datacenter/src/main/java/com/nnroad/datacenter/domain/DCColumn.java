package com.nnroad.datacenter.domain;

import java.util.ArrayList;
import java.util.List;

public class DCColumn extends DCTableConfig {
    /** 子菜单 */
    private List<DCColumn> Childern = new ArrayList<DCColumn>();
    /** 所有子菜单数量 */
    private Integer childrenLong ;
    private Integer level;

    public List<DCColumn> getChildern() {
        return Childern;
    }

    public void setChildern(List<DCColumn> childern) {
        Childern = childern;
    }

    public Integer getChildrenLong() {
        return childrenLong;
    }

    public void setChildrenLong(Integer childrenLong) {
        this.childrenLong = childrenLong;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
}
