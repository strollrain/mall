package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.vo.ProductDetailVo;

/**
 * @author rain
 *
 */
public interface IProductService {

	//新增或更新产品
	ServerResponse saveOrUpdateProduct(Product product);
	//设置产品状态
	ServerResponse<String> setSaleStatus(Integer productId,Integer status);
	//获取产品详情
	ServerResponse<ProductDetailVo> manageProductDetail(Integer productId);
	//获取分页信息
	ServerResponse<PageInfo>  getProductList(int pageNum,int pageSize);
	//后台产品搜索
	ServerResponse<PageInfo> searchProduct(String productName,Integer productId,int pageNum,int pageSize);
	//前台获取产品详情
	ServerResponse<ProductDetailVo> getProductDetail(Integer productId);
	
	//前台根据关键字 id搜索
	ServerResponse<PageInfo> getProductByKeywordCategory(String keyword,Integer categoryId,int pageNum,int pageSize,String orderBy);
}
