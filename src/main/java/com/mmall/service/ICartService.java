package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.vo.CartVo;

/**
 * @author rain
 *
 */
public interface ICartService {
	ServerResponse<CartVo> add(Integer userId,Integer productId,Integer count);
	//更新购物车  增减产品数量等
	ServerResponse<CartVo> update(Integer userId,Integer productId,Integer count);
	//删除购物车产品
	ServerResponse<CartVo> deleteProdcut(Integer userId,String productIds);
	//查询
	public ServerResponse<CartVo> list(Integer userId);
	//全选或反选
	ServerResponse<CartVo> selectOrUnSelect(Integer userId,Integer productId,Integer checked);

	//查询当前用户的购物车中产品数量， 如果一个产品有10个 那么数量就是10
	ServerResponse<Integer> getCartProductCount(Integer userId);
}
