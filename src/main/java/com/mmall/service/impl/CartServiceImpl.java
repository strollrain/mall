package com.mmall.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CartMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Cart;
import com.mmall.pojo.Product;
import com.mmall.service.ICartService;
import com.mmall.util.BigDecimalUtil;
import com.mmall.util.PropertiesUtil;
import com.mmall.vo.CartProductVo;
import com.mmall.vo.CartVo;

/**
 * @author rain
 *
 */
@Service("iCartService")
public class CartServiceImpl implements ICartService {

	@Autowired
	private CartMapper cartMapper;
	@Autowired
	private ProductMapper productMapper; 
	public ServerResponse<CartVo> add(Integer userId,Integer productId,Integer count) {
		//参数判断
		if(productId == null || count == null) {
			return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
		}
		Cart cart = cartMapper.selectCartByUserIdProductId(userId, productId);
		if(cart == null) {
			//产品不在购物车，需要新增产品记录
			Cart cartItem =new Cart();
			cartItem.setQuantity(count);
			cartItem.setChecked(Const.Cart.CHECKED);
			cartItem.setUserId(userId);
			cartItem.setProductId(productId);
			cartMapper.insert(cartItem);
		}else {
			//产品在购物车 数量 增加
			count=cart.getQuantity()+count;
			cart.setQuantity(count);
			cartMapper.updateByPrimaryKeySelective(cart);
		}
		return this.list(userId);
	}
	
	//更新购物车  增减产品数量等
	public ServerResponse<CartVo> update(Integer userId,Integer productId,Integer count){
		//参数判断
		if(productId == null || count == null) {
			return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
		}
		Cart cart = cartMapper.selectCartByUserIdProductId(userId, productId);
		if(cart != null) {
			cart.setQuantity(count);
		}
		//更新购物车
		cartMapper.updateByPrimaryKeySelective(cart);
		return this.list(userId);
	}
	
	
	//删除购物车产品
	public ServerResponse<CartVo> deleteProdcut(Integer userId,String productIds){
		                                     //利用工具分割 id组成的字符串  
		List<String> productList = Splitter.on(",").splitToList(productIds);
		//判断
		if(CollectionUtils.isEmpty(productList)) {
			return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());	 
		}
		//执行删除操作  在mapper中增加方法
		cartMapper.deleteByUserIdProductIds(userId, productList);
		return this.list(userId);
	}
	
	//查询 
	public ServerResponse<CartVo> list(Integer userId){
		CartVo cartVo = this.getCartVoLimit(userId);
		return ServerResponse.createBySuccess(cartVo);
	}
	
	//全选
		public ServerResponse<CartVo> selectOrUnSelect(Integer userId,Integer productId,Integer checked){
			cartMapper.checkedOrUncheckedProduct(userId,productId,checked);
			return this.list(userId);
			
		}
		//查询当前用户的购物车中产品数量， 如果一个产品有10个 那么数量就是10

		public ServerResponse<Integer> getCartProductCount(Integer userId){
			if(userId == null) {
				return ServerResponse.createBySuccess(0);
			}
			return ServerResponse.createBySuccess(cartMapper.selectCartProductCount(userId));
			
			
		}
		
	
	
	private CartVo getCartVoLimit(Integer userId) {
		CartVo cartVo = new CartVo();
		List<Cart> cartList=cartMapper.selectCartByUserId(userId);
	    List<CartProductVo> cartProductVoList=Lists.newArrayList();
	    //初始化购物车总价
	    BigDecimal cartTotalPrice = new BigDecimal("0");
	    // 购物列表空判断
	    if(CollectionUtils.isNotEmpty(cartList)) {
	    	for (Cart cartItem : cartList) {
	    		CartProductVo cartProductVo = new CartProductVo();
	    		cartProductVo.setId(cartItem.getId());
	    		cartProductVo.setUserId(cartItem.getUserId());
	    		cartProductVo.setProductId(cartItem.getProductId());
	    		Product product = productMapper.selectByPrimaryKey(cartItem.getProductId());
	    		if(product !=null) {
	    			cartProductVo.setProductMainImage(product.getMainImage());
	    			cartProductVo.setProductName(product.getName());
	    			cartProductVo.setProductSubtitle(product.getSubtitle());
	    			cartProductVo.setProductStatus(product.getStatus());
	    			cartProductVo.setProductPrice(product.getPrice());
	    			cartProductVo.setProductStock(product.getStock());
	    			//判断库存
	    			int buyLimitCount = 0;
	    			if(product.getStock() >=cartItem.getQuantity()) {
	    				//库存充足更新
	    				buyLimitCount=	cartItem.getQuantity();
	    				cartProductVo.setLimitQuatity(Const.Cart.LIMIT_NUM_SUCCESS);
	    			 
	    				
	    			}else {
	    				//超出库存
	    				buyLimitCount=product.getStock();
	    				cartProductVo.setLimitQuatity(Const.Cart.LIMIT_NUM_FAIL);
	    				//更新购物车中 有效库存
	    				Cart cartQuatity=new Cart();
	    				cartQuatity.setId(cartItem.getId());
	    				cartQuatity.setQuantity(buyLimitCount);
	    				cartMapper.updateByPrimaryKeySelective(cartQuatity);
	    			}
	    			cartProductVo.setQuantity(buyLimitCount);
	    			//计算 该产品总价
	    			cartProductVo.setProductTotalPrice(BigDecimalUtil.mul(product.getPrice().doubleValue(),cartProductVo.getQuantity()));
	    			cartProductVo.setProductChecked(cartItem.getChecked());
	    		}
	    		//判断该产品是否勾选
	    		if(cartItem.getChecked() == Const.Cart.CHECKED) {
	    			//增加至总价
	    			cartTotalPrice =BigDecimalUtil.add(cartTotalPrice.doubleValue(), cartProductVo.getProductTotalPrice().doubleValue());
	    		}
	    		
	    		cartProductVoList.add(cartProductVo);
			}
	    	
	    }
	    cartVo.setCartTotalPrice(cartTotalPrice);
	    cartVo.setCartProductVoList(cartProductVoList);
	    cartVo.setAllChecked(this.getAllCheckedStatus(userId));//通过私有方法  查看勾选状态
	    cartVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));
	    return cartVo;
	}
	
	private boolean getAllCheckedStatus(Integer userId) {
		if(userId == null ) {
			return false;
		}
		return cartMapper.selectCartProductCheckedStatusByUserId(userId) == 0;  //sql查询 未勾选数量
	}
	
}
