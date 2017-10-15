package com.mmall.dao;

import org.apache.ibatis.annotations.Param;

import com.mmall.pojo.User;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
    
    int checkEmail(String email);
    
    int checkUsername(String username);
    //传递多个参数  需要 param注解 
    User selectLogin(@Param("username")String username,@Param("password")String password);

    String selectQuestionByUsername(String username);
    
    int checkAnswer(@Param("username")String username,@Param("question")String question,@Param("answer")String answer);

    //更新密码
    int updatePasswordByUsername(@Param("username")String username,@Param("passwordNew")String passwordNew);

    //检查密码
    
    int checkPassword(@Param(value="password")String password,@Param("userId")Integer userId);
   
    int checkEmailByUserId(@Param(value="email")String email,@Param(value="userId")Integer userId);
    
    
    
}