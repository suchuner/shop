package com.suchuner.shop.common;

import java.io.Serializable;

/**
 * 该类定义了需要从数据库中导入哪些数据到索引库中
 * @author suchuner
 *
 */
public class SearchItemResult implements Serializable{
	private static final long serialVersionUID = 1L;
	private String id;
	private String title;
	private String  sellPoint;
	private long price;
	private String image;
	private String   categoryName;
	private String  itemDesc;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSellPoint() {
		return sellPoint;
	}
	public void setSellPoint(String sellPoint) {
		this.sellPoint = sellPoint;
	}
	public long getPrice() {
		return price;
	}
	public void setPrice(long price) {
		this.price = price;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getItemDesc() {
		return itemDesc;
	}
	public void setItemDesc(String itemDesc) {
		this.itemDesc = itemDesc;
	}
	
}