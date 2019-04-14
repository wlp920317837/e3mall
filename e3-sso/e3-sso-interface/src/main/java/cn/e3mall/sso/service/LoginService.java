package cn.e3mall.sso.service;

import cn.e3mall.common.utils.E3Result;

public interface LoginService {

	/*
	 * 登录,参数用户名密码,返回值E3Result包含数据token
	 * 业务逻辑
	 * 1.判断用户名密码是否正确
	 * 2.如果不正确返回登录失败
	 * 3.如果正确生成token
	 * 4.把用户消息存到redis,key为token,value为用户信息
	 * 5.设置session过期时间
	 * 6.把token返回
	 * 
	 */
	public E3Result login(String username, String password);
}
