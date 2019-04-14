package cn.e3mall.sso.service;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.pojo.TbUser;

public interface RegisterService {

	// 用户名.手机号可用性校验
	public E3Result checkData(String param, Integer type);

	// 注册
	public E3Result register(TbUser tbUser);
}
