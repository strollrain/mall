package com.mmall.vo;

import java.math.BigDecimal;

/**
 * @author rain
 *
 */
public class CartProductVo {

	//结合产品购物车给的对象
	private Integer id;
	private Integer userId;
	private Integer productId;
	private Integer quantity;//购物车本产品数量
	private String productName;
	private String productSubtitle;
	private String productMainImage;
	private BigDecimal productPrice;
	private Integer productStatus;
	private BigDecimal productTotalPrice;
	private Integer productStock;//库存
	
	private Integer productChecked;//此商品是否选择
	private String limitQuatity;//限制数量返回结果
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductSubtitle() {
		return productSubtitle;
	}
	public void setProductSubtitle(String productSubtitle) {
		this.productSubtitle = productSubtitle;
	}
	public String getProductMainImage() {
		return productMainImage;
	}
	public void setProductMainImage(String productMainImage) {
		this.productMainImage = productMainImage;
	}
	public BigDecimal getProductPrice() {
		return productPrice;
	}
	public void setProductPrice(BigDecimal productPrice) {
		this.productPrice = productPrice;
	}
	public Integer getProductStatus() {
		return productStatus;
	}
	public void setProductStatus(Integer productStatus) {
		this.productStatus = productStatus;
	}
	public BigDecimal getProductTotalPrice() {
		return productTotalPrice;
	}
	public void setProductTotalPrice(BigDecimal productTotalPrice) {
		this.productTotalPrice = productTotalPrice;
	}
	public Integer getProductStock() {
		return productStock;
	}
	public void setProductStock(Integer productStock) {
		this.productStock = productStock;
	}
	public Integer getProductChecked() {
		return productChecked;
	}
	public void setProductChecked(Integer productChecked) {
		this.productChecked = productChecked;
	}
	public String getLimitQuatity() {
		return limitQuatity;
	}
	public void setLimitQuatity(String limitQuatity) {
		this.limitQuatity = limitQuatity;
	}
	
}