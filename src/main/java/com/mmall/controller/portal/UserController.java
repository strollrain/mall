package com.mmall.controller.portal;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;


/**
 * @author rain
 *
 */
@Controller
@RequestMapping("/user/")
public class UserController {
	@Autowired
	private IUserService iUserService;
	//登录接口
	@RequestMapping(value="login.do",method=RequestMethod.POST)
	@ResponseBody
	public ServerResponse<User> login(String username,String password,HttpSession session) {
		//service>>mybaitis>>dao
		ServerResponse<User> response=iUserService.login(username, password);
		if(response.isSuccess()) {
			//利用声明的常量类
			session.setAttribute(Const.CURRENT_USER, response.getData());
		}
		return response;
	}
	
	//退出登录
	@RequestMapping(value="logout.do",method=RequestMethod.POST)
	@ResponseBody
	public ServerResponse<String> logout(HttpSession session){
		//删除 session即可
		session.removeAttribute(Const.CURRENT_USER);
		return ServerResponse.createBySuccess();	
		
	}
	
	//注册用户
	@RequestMapping(value="register.do",method=RequestMethod.POST)
	@ResponseBody
	public ServerResponse<String> register(User user){
		
		return iUserService.register(user);	
	}
	//注册用户时候 实时输入检查
	@RequestMapping(value="check_valid.do",method=RequestMethod.POST)
	@ResponseBody
	public ServerResponse<String> check_valid(String str,String type){
		
		return iUserService.checkValid(str, type);
	}
	
	//获取用户信息
	@RequestMapping(value="get_user_info.do",method=RequestMethod.POST)
	@ResponseBody
	public ServerResponse<User> get_user_info(HttpSession session){
		User user=(User) session.getAttribute(Const.CURRENT_USER);
	    //判断
		if(user != null) {
			return ServerResponse.createBySuccess(user);
		}
		return ServerResponse.createByErrorMessage("用户未登录，无法获取信息");
	}
	//忘记密码
	@RequestMapping(value="forget_get_question.do",method=RequestMethod.POST)
	@ResponseBody
	 public ServerResponse<String> forget_get_question(String username){
		 return iUserService.selectQuestion(username);
	 }
	
	//校验问题答案
	@RequestMapping(value="forget_check_answer.do",method=RequestMethod.POST)
	@ResponseBody
	public ServerResponse<String> forget_check_answer(String username,String question,String answer){
		return iUserService.checkAnswer(username, question, answer);
	}
	
	//密码重置
	@RequestMapping(value="forget_reset_password.do",method=RequestMethod.POST)
	@ResponseBody
	public ServerResponse<String> forget_reset_password(String username,String passwordNew,String forgetToken){
		
		return iUserService.forgetResetPassword(username, passwordNew, forgetToken);
	}
	
	//登录状态重置密码
	@RequestMapping(value="reset_password.do",method=RequestMethod.POST)
	@ResponseBody
	public ServerResponse<String> reset_password(HttpSession session,String passwordOld,
			String passwordNew){
		User user=(User) session.getAttribute(Const.CURRENT_USER);
		if(user==null) {
			return ServerResponse.createByErrorMessage("用户未登录");
		}
		return iUserService.resetPassword(passwordOld, passwordNew, user);
	}
	
	//更新个人信息
	@RequestMapping(value="update_Information.do",method=RequestMethod.POST)
	@ResponseBody
	public ServerResponse<User> update_Information(HttpSession session,User user){
		User currentUser=(User) session.getAttribute(Const.CURRENT_USER);
		if(currentUser==null) {
			return ServerResponse.createByErrorMessage("用户未登录");
		}
		user.setId(currentUser.getId());
		user.setUsername(currentUser.getUsername());
		ServerResponse<User> response=iUserService.updateInformation(user);
		if(response.isSuccess()) {
			//成功后  更新session
			session.setAttribute(Const.CURRENT_USER, response.getData());
		}
		return response;
	}
	//获取用户详细信息
	@RequestMapping(value="get_information.do",method=RequestMethod.POST)
	@ResponseBody
	public ServerResponse<User> get_information(HttpSession session){
		//强制登录
		User currentUser=(User) session.getAttribute(Const.CURRENT_USER);
		if(currentUser==null) {
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "未登录，需要登录");
		}
		return iUserService.getInformation(currentUser.getId());
	}
		
	

}
	
