package com.mmall.service.impl;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.common.TokenCache;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.MD5Util;

/**
 * @author rain
 *
 */
@Service("IUserService")
public class UserServiceImpl implements IUserService {
	@Autowired
	private UserMapper userMapper;

	@Override
	public ServerResponse<User> login(String username, String password) {
		int resultCount = userMapper.checkUsername(username);
		if (resultCount == 0) {
			return ServerResponse.createByErrorMessage("用户名不存在");
		}
		// todo密码登录 MD5
		String md5Passwaord = MD5Util.MD5EncodeUtf8(password);

		User user = userMapper.selectLogin(username, md5Passwaord);
		if (user == null) {
			// 逻辑到此 可以判断用户存在
			return ServerResponse.createByErrorMessage("密码错误");
		}
		user.setPassword(StringUtils.EMPTY);
		return ServerResponse.createBySuccess("登录成功", user);
	}

	// 注册 用户
	public ServerResponse<String> register(User user) {
		// 校验username emal
		ServerResponse validResponse = this.checkValid(user.getUsername(), Const.USERNAME);
		if (!validResponse.isSuccess()) {
			return validResponse;
		}
		validResponse = this.checkValid(user.getEmail(), Const.EMAIL);
		if (!validResponse.isSuccess()) {
			return validResponse;
		}
		// 设置用户角色 在Const类中统一定义
		user.setRole(Const.Role.ROLE_CUSTOMER);
		// MD5加密
		user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
		int resultCount = userMapper.insert(user);
		// 判断结果
		if (resultCount == 0) {
			return ServerResponse.createByErrorMessage("注册失败");
		}
		return ServerResponse.createBySuccessMessage("注册成功");
	}

	public ServerResponse<String> checkValid(String str, String type) {
		// 空格为false
		if (StringUtils.isNotBlank(type)) {
			// 校验
			if (Const.USERNAME.equals(type)) {
				int resultCount = userMapper.checkUsername(str);
				if (resultCount > 0) {
					return ServerResponse.createByErrorMessage("用户已存在");
				}
			}
			if (Const.EMAIL.equals(type)) {
				int resultCount = userMapper.checkEmail(str);
				if (resultCount > 0) {
					return ServerResponse.createByErrorMessage("email已存在");
				}
			}

		} else {
			return ServerResponse.createByErrorMessage("参数错误");
		}
		return ServerResponse.createBySuccess("校验成功");
	}

	// 忘记密码
	public ServerResponse<String> selectQuestion(String username) {
		ServerResponse validResponse = this.checkValid(username, Const.USERNAME);
		if (validResponse.isSuccess()) {
			// 用户不存在
			return ServerResponse.createByErrorMessage("用户不存在");
		}
		String question = userMapper.selectQuestionByUsername(username);
		if (StringUtils.isNotBlank(question)) {
			return ServerResponse.createBySuccess(question);
		}
		return ServerResponse.createByErrorMessage("问题为空");
	}

	// 问题核对
	public ServerResponse<String> checkAnswer(String username, String question, String answer) {
		int resultCount = userMapper.checkAnswer(username, question, answer);
		if (resultCount > 0) {
			// 问题存在，并且正确
			String forgetToken = UUID.randomUUID().toString();
			TokenCache.setKey(TokenCache.TOKEN_PREFIX+ username, forgetToken);
			return ServerResponse.createBySuccess(forgetToken);
		}
		return ServerResponse.createByErrorMessage("问题答案错误");
	}
	//重置密码
	public ServerResponse<String> forgetResetPassword(String username,String passwordNew,String forgetToken){
		 //判断forgetToken
		if(StringUtils.isBlank(forgetToken)) {
			return ServerResponse.createByErrorMessage("参数错误,token需要传递");
		}
		ServerResponse validResponse = this.checkValid(username, Const.USERNAME);
		if (validResponse.isSuccess()) {
			// 用户不存在
			return ServerResponse.createByErrorMessage("用户不存在");
		}
		String token =TokenCache.getKey(TokenCache.TOKEN_PREFIX+username);
		if(StringUtils.isBlank(token)) {
			return ServerResponse.createByErrorMessage("token无效或过期");
		}
		if(StringUtils.equals(forgetToken, token)) {
			String md5Password=MD5Util.MD5EncodeUtf8(passwordNew);
			int rowCount = userMapper.updatePasswordByUsername(username, md5Password);
	        //判断生效行数
			if(rowCount>0) {
				
				return ServerResponse.createBySuccess("修改密码成功");
			}
		}else {
			//forgetToken, token不一致
			return ServerResponse.createByErrorMessage("token错误，请重新获取token");
		}
		
		return ServerResponse.createByErrorMessage("重置密码失败");
	}

	//登录情况下 重置密码
	public ServerResponse<String> resetPassword(String passwordOld,String passwordNew,User user){
		//防止横向越权 校验旧密码 指定用户   注意本查询 为count（1） 
		int resultCount=userMapper.checkPassword(MD5Util.MD5EncodeUtf8(passwordOld), user.getId());
		if(resultCount==0) {
			return ServerResponse.createByErrorMessage("旧密码错误");
		}
		user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
		int updateCount=userMapper.updateByPrimaryKeySelective(user);
		if(updateCount>0) {
			return ServerResponse.createBySuccess("密码更新成功");
			
		}
		return ServerResponse.createByErrorMessage("重置密码失败");
		
	}
	
	//用户更新方法
	public ServerResponse<User> updateInformation(User user){
		//username 不更新
		//email校验  需要非目前用户邮箱 且不存在
		int resultCount=userMapper.checkEmailByUserId(user.getEmail(), user.getId());
		if(resultCount>0) {
			return ServerResponse.createByErrorMessage("email已存在，请更换");
		}
		User updateUser = new User();
		updateUser.setId(user.getId());
		updateUser.setEmail(user.getEmail());
		updateUser.setPhone(user.getPhone());
		updateUser.setQuestion(user.getQuestion());
		updateUser.setAnswer(user.getAnswer());
		
		int updateCount=userMapper.updateByPrimaryKeySelective(updateUser);
		if(updateCount>0) {
			return ServerResponse.createBySuccess("密码个人信息成功",updateUser);
			
		}
		return ServerResponse.createByErrorMessage("更新个人信息失败");
		
		
	}
	//获取用户详细信息
	public ServerResponse<User> getInformation(Integer userId){
		User user =userMapper.selectByPrimaryKey(userId);
		if(user==null) {
			return ServerResponse.createByErrorMessage("找不到当前用户");
		}
		user.setPassword(StringUtils.EMPTY);
		return ServerResponse.createBySuccess(user);
	}
	
	//backend   后端用户
	
	
	//校验管理员身份
	public ServerResponse checkAdminRole(User user) {
		if(user !=null && user.getRole().intValue()==Const.Role.ROLE_ADMIN) {
			return ServerResponse.createBySuccess();		
		}else {
			return ServerResponse.createByError();
		}
		
	}
	
	
	
	
	
	

}
