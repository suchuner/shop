package com.suchuner.shop.domain;

import java.io.Serializable;
import java.util.Date;

public class TbContentCategory implements Serializable{
	/**正常*/
	public static final Integer CONTENTCATEGORY_STATE_NORMAL = 1;
	/**删除*/
	public static final Integer CONTENTCATEGORY_STATE_DEL = 2;
    private Long id;

    private Long parentId;

    private String name;
	//状态。可选值:1(正常),2(删除)
    private Integer status;
//排列序号，表示同级类目的展现次序，如数值相等则按名称次序排列。取值范围:大于零的整数
    private Integer sortOrder;

    private Boolean isParent;

    private Date created;

    private Date updated;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Boolean getIsParent() {
        return isParent;
    }

    public void setIsParent(Boolean isParent) {
        this.isParent = isParent;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }
}