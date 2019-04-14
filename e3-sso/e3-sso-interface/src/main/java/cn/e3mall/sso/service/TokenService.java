package cn.e3mall.sso.service;

import cn.e3mall.common.utils.E3Result;

public interface TokenService {

	// 根据toekn返回一个用户信息用e3result包装
	public E3Result getUserByToken(String token);
}
