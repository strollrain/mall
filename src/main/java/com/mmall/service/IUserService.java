package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;

/**
 * @author rain
 */
public interface IUserService {

	ServerResponse<User> login(String username,String password);
	
	ServerResponse<String> register(User user);
	ServerResponse<String> checkValid(String str,String type);
	ServerResponse<String> selectQuestion(String username);
    //密保问题核对
	ServerResponse<String> checkAnswer(String username, String question, String answer);
    //密码重置
	ServerResponse<String> forgetResetPassword(String username,String passwordNew,String forgetToken);
	//登录状态
	ServerResponse<String> resetPassword(String passwordOld,String passwordNew,User user);
   //更新用户信息
	ServerResponse<User> updateInformation(User user);
	//获取用户详细信息
	ServerResponse<User> getInformation(Integer userId);
	//校验管理员身份
	ServerResponse checkAdminRole(User user);
	
}
