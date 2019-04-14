package cn.e3mall.sso.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.TokenService;

@Service
@Transactional
public class TokenServiceImpl implements TokenService {

	// 过期时间
	@Value("${SESSION_EXPIRE}")
	private Integer SESSION_EXPIRE;

	// redise客户端
	@Autowired
	private JedisClient jedisClient;

	// 根据toekn返回一个用户信息用e3result包装
	@Override
	public E3Result getUserByToken(String token) {
		// 根据token从redis中取用户信息
		String json = jedisClient.get("SESSION:" + token);
		if (StringUtils.isBlank(json)) {
			// 取不到用户信息,登录过期,返回登录过期
			return E3Result.build(202, "用户登录过期");
		} else {
			// 取到用户信息,用e3result包装
			TbUser tbUser = JsonUtils.jsonToPojo(json, TbUser.class);
			// 更新session过期时间
			jedisClient.expire("SESSION:" + token, SESSION_EXPIRE);
			return E3Result.ok(tbUser);
		}
	}
}
