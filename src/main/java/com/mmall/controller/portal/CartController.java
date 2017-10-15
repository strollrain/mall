package com.mmall.controller.portal;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.ICartService;
import com.mmall.vo.CartVo;

/**
 * @author rain
 *
 */
@Controller
@RequestMapping("/cart/")
public class CartController {
	
	@Autowired
	private ICartService iCartService;
	
	
	
	//查询
	@RequestMapping("list.do")
	@ResponseBody
	public ServerResponse<CartVo> list(HttpSession session) {
		//登录判断
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if(user == null) {
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
		}
		return iCartService.list(user.getId());	
	}
	
	
	
	//添加产品购物车
	@RequestMapping("add.do")
	@ResponseBody
	public ServerResponse<CartVo> add(HttpSession session,Integer count,Integer productId) {
		//登录判断
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if(user == null) {
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
		}
		return iCartService.add(user.getId(), productId, count);	
	}
	
	//更新购物车  增减产品数量等
	
	@RequestMapping("update.do")
	@ResponseBody
	public ServerResponse<CartVo> update(HttpSession session,Integer count,Integer productId) {
		//登录判断
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if(user == null) {
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
		}
		//return iCartService.add(user.getId(), productId, count);
		return iCartService.update(user.getId(), productId, count);
		
	}
	
	//在购物车中删除产品
	@RequestMapping("delete_product.do")
	@ResponseBody                                                  //可以实现多个商品删除
	public ServerResponse<CartVo> deleteProduct(HttpSession session,String productIds) {
		//登录判断
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if(user == null) {
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
		}
		//return iCartService.add(user.getId(), productId, count);
		return iCartService.deleteProdcut(user.getId(), productIds);
		
	}
	//全选
	@RequestMapping("select_all.do")
	@ResponseBody
	public ServerResponse<CartVo> selectAll(HttpSession session) {
		//登录判断
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if(user == null) {
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
		}
		return iCartService.selectOrUnSelect(user.getId(),null, Const.Cart.CHECKED);	
	}
	//全返选
	@RequestMapping("un_select_all.do")
	@ResponseBody
	public ServerResponse<CartVo> UnSelectAll(HttpSession session) {
		//登录判断
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if(user == null) {
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
		}
		return iCartService.selectOrUnSelect(user.getId(),null, Const.Cart.UN_CHECKED);	
	}
	
	//单独选
	@RequestMapping("select.do")
	@ResponseBody
	public ServerResponse<CartVo> Select(HttpSession session,Integer productId) {
		//登录判断
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if(user == null) {
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
		}
		return iCartService.selectOrUnSelect(user.getId(),productId, Const.Cart.CHECKED);	
	}
	
	//单选反选
	@RequestMapping("un_select.do")
	@ResponseBody
	public ServerResponse<CartVo> UnSelect(HttpSession session,Integer productId) {
		//登录判断
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if(user == null) {
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
		}
		return iCartService.selectOrUnSelect(user.getId(),productId, Const.Cart.UN_CHECKED);	
	}

	
	
	//查询当前用户的购物车中产品数量， 如果一个产品有10个 那么数量就是10
	@RequestMapping("get_cart_product_count.do")
	@ResponseBody
	public ServerResponse<Integer> getCartProductCount(HttpSession session) {
		//登录判断
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if(user == null) {
			return ServerResponse.createBySuccess(0);
		}
		return iCartService.getCartProductCount(user.getId());	
	}
	
	

}
