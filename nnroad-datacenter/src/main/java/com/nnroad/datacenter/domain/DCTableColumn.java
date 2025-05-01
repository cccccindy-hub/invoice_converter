package com.nnroad.datacenter.domain;

import com.nnroad.common.core.domain.entity.SysDictData;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class DCTableColumn implements Serializable {
    private static final long serialVersionUID = 1L;
    private String align;
    private String valign;
    private String field;
    private String title;
    private boolean el;
    private Integer rowspan;
    private Integer colspan;
    private Integer dataType;
    private Long columnId;
    private Long parentId;
    private List<SysDictData> dictList;

}
