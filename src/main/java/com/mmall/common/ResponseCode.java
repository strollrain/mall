package com.mmall.common;

/**
 * @author rain
 *
 */
public enum ResponseCode {
    //注意状态声明格式
	SUCCESS(0,"SUCCESS"),
	ERROR(1,"ERROR"),
	NEED_LOGIN(10,"NEED_LOGIN"),
	//不合法登录
	ILLEGAL_ARGUMENT(2,"ILLEGAL_ARGUMENT");
	
	//声明属性
	private final String desc;
	private final int code;
	private ResponseCode(int code,String desc) {
		this.code=code;
		this.desc=desc;
	}
	public String getDesc() {
		return desc;
	}
	public int getCode() {
		return code;
	}
	
}
