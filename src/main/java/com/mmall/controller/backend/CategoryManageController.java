package com.mmall.controller.backend;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.ICategoryService;
import com.mmall.service.IUserService;

/**
 * @author rain
 *
 */
@Controller
@RequestMapping("/manage/category")
public class CategoryManageController {
      @Autowired
      private IUserService iUserService;
      @Autowired
      private ICategoryService iCategoryService;
	//增加分类
      @RequestMapping("add_category.do")
      @ResponseBody 
	public ServerResponse addCategory(HttpSession session,String categoryName,@RequestParam(value="parentId",defaultValue="0")int parentId ){
		//判断登录
		User user =(User)session.getAttribute(Const.CURRENT_USER);
		if(user == null) {
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
		}
		//身份校验 写入service中
		if(iUserService.checkAdminRole(user).isSuccess()) {
			//是管理员
			//增加分类助理逻辑 
			return iCategoryService.addCategory(categoryName, parentId);
		}else {
			return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
		}
	}
      
      //更新品类名称
      @RequestMapping("set_category_name.do")
      @ResponseBody 
     public ServerResponse setCategoryName(HttpSession session,Integer categoryId,String categoryName) {
    	//判断登录
 		User user =(User)session.getAttribute(Const.CURRENT_USER);
 		if(user == null) {
 			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
 		}
 		//身份校验 写入service中
 		if(iUserService.checkAdminRole(user).isSuccess()) {
 			//是管理员
 			//更新 categoryName
 			return iCategoryService.updateCategoryName(categoryId, categoryName);
 		}else {
 			return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
 		}	 
    	 
     }
	
      
      //获取品类子节点(平级)
      @RequestMapping("get_category.do")
      @ResponseBody 
      public ServerResponse getChildParallelCategory(HttpSession session,@RequestParam(value="categoryId",defaultValue="0")Integer categoryId) {
    	//判断登录
   		User user =(User)session.getAttribute(Const.CURRENT_USER);
   		if(user == null) {
   			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
   		}
   		//身份校验 写入service中
   		if(iUserService.checkAdminRole(user).isSuccess()) {
   			//是管理员
   			//获取品类子节点信息 不递归
   			return iCategoryService.getChildParallelCategory(categoryId);
   		}else {
   			return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
   		}
    	  
      }
    
      //获取当前categoryId并且递归查询子节点categoryId
      @RequestMapping("get_deep_category.do")
      @ResponseBody  
      public ServerResponse  getCategoryAndDeepChildrenCategory(HttpSession session,@RequestParam(value="categoryId",defaultValue="0")Integer categoryId) {
    	//判断登录
     		User user =(User)session.getAttribute(Const.CURRENT_USER);
     		if(user == null) {
     			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
     		}
     		//身份校验 写入service中
     		if(iUserService.checkAdminRole(user).isSuccess()) {
     			//是管理员
     			//获取当前categoryId并且递归查询子节点categoryId
     			return iCategoryService.selectCategoryAndChildrenById(categoryId);
     		}else {
     			return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
     		}  
      }
	
	
	
	
	
}



















