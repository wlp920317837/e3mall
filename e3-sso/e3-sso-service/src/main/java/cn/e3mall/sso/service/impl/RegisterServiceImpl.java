package cn.e3mall.sso.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.MD5Utils;
import cn.e3mall.mapper.TbUserMapper;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.pojo.TbUserExample;
import cn.e3mall.pojo.TbUserExample.Criteria;
import cn.e3mall.sso.service.RegisterService;

/**
 * 注册服务service
 * @author wlp
 *
 */

@Service
@Transactional
public class RegisterServiceImpl implements RegisterService {

	// 用户dao
	@Autowired
	private TbUserMapper tbUserMapper;

	// 用户名.手机号可用性校验
	@Override
	public E3Result checkData(String param, Integer type) {
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		// 根据传入type判断需要判断的是用户名,手机号,还是邮箱
		if (1 == type) {
			// 查询用户名
			// 根据传入的param查询数据库
			criteria.andUsernameEqualTo(param);
		} else if (2 == type) {
			// 查询手机号
			// 根据传入的param查询数据库
			criteria.andPhoneEqualTo(param);
		} else {
			return E3Result.build(404, "输入类型不存在", false);
		}
		List<TbUser> list = tbUserMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			// 查询到结果,说明已经存在,返回一个flase失败
			return E3Result.build(500, "已存在", false);
		} else {
			// 没有查询到结果,说明不存在.返回一个true成功
			return E3Result.build(200, "不存在", true);
		}
	}

	// 注册
	@Override
	public E3Result register(TbUser tbUser) {
		// 最好将用户信息在校验一次
		if (StringUtils.isBlank(tbUser.getUsername()) && StringUtils.isBlank(tbUser.getPassword()) && StringUtils.isBlank(tbUser.getPhone())) {
			return E3Result.build(400, "用户数据不完整!");
		}

		E3Result result = checkData(tbUser.getUsername(), 1);
		if (!(boolean) result.getData()) {
			return E3Result.build(400, "用户名已经被占用!");
		}

		E3Result result2 = checkData(tbUser.getPassword(), 2);
		if (!(boolean) result2.getData()) {
			return E3Result.build(400, "手机号已经被占用!");
		}

		// 补全tbUser,需要将密码进行md5加密
		tbUser.setCreated(new Date());
		tbUser.setUpdated(new Date());
		if (StringUtils.isNotBlank(tbUser.getPassword())) {
			tbUser.setPassword(MD5Utils.md5(tbUser.getPassword()));
			// String md5pass
			// =DigestUtils.md5DigestAsHex(tbUser.getPassword().getBytes());
			// tbUser.setPassword(md5pass);
		}

		// 插入到数据库
		tbUserMapper.insertSelective(tbUser);

		return E3Result.ok();
	}
}
