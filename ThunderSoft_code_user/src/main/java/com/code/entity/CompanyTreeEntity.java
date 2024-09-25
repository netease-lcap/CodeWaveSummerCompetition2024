package com.code.entity;

import com.netease.lowcode.core.annotation.NaslStructure;

/**
 * @author: cfn
 * @date: 2024/2/19 15:23
 * @description: 单位实体
 */
@NaslStructure
public class CompanyTreeEntity {

	/**
	 * id
	 */
	public String id;

	/**
	 * 单位名称
	 */
	public String name;

	/**
	 * 排序
	 */
	public String sortNumber;

	/**
	 * 单位类型
	 */
	public String type;

	/**
	 * 父id
	 */
	public String parentId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSortNumber() {
		return sortNumber;
	}

	public void setSortNumber(String sortNumber) {
		this.sortNumber = sortNumber;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
}