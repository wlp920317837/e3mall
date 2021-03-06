package cn.e3mall.sso.service.impl;

import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.common.utils.MD5Utils;
import cn.e3mall.mapper.TbUserMapper;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.pojo.TbUserExample;
import cn.e3mall.pojo.TbUserExample.Criteria;
import cn.e3mall.sso.service.LoginService;

@Service
@Transactional
public class LoginServiceImpl implements LoginService {

	// userdao
	@Autowired
	private TbUserMapper tbUserMapper;

	// jedis客户端
	@Autowired
	private JedisClient jedisClient;

	@Value("${SESSION_EXPIRE}")
	private Integer SESSION_EXPIRE;

	// 登录,参数用户名密码,返回值E3Result包含数据token
	@Override
	public E3Result login(String username, String password) {
		// * 1.判断用户名密码是否正确
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		criteria.andUsernameEqualTo(username);
		List<TbUser> list = tbUserMapper.selectByExample(example);
		// * 2.如果不正确返回登录失败
		if (list != null && list.size() == 0) {
			return E3Result.build(400, "用户名或密码错误");
		}
		TbUser tbUser = list.get(0);
		if (StringUtils.isBlank(password) && !tbUser.getPassword().equals(MD5Utils.md5(password))) {
			return E3Result.build(400, "用户名或密码错误");
		}
		// * 3.如果正确生成token
		String token = UUID.randomUUID().toString();
		// * 4.把用户消息存到redis,key为token,value为用户信息
		tbUser.setPassword(null);
		String json = JsonUtils.objectToJson(tbUser);
		jedisClient.set("SESSION:" + token, json);
		// * 5.设置session过期时间
		jedisClient.expire("SESSION:" + token, SESSION_EXPIRE);
		// * 6.把token返回
		return E3Result.ok(token);
	}
	
}
