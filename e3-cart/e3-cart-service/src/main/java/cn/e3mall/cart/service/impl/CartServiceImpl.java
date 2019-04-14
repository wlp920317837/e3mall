package cn.e3mall.cart.service.impl;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 购物车处理服务
 * @author wlp
 *
 */

@Service
@Transactional
public class CartServiceImpl implements CartService {

	@Value("${REDIS_CART_PRE}")
	private String REDIS_CART_PRE;

	@Autowired
	private JedisClient jedisClient;

	@Autowired
	private TbItemMapper itemMapper;

	// 服务端添加购物车
	@Override
	public E3Result addCart(Long userId, Long itemId, Integer num) {
		// 向redis中添加购物车
		// 类型是hash,key为userId,field为itemId,值为商品信息
		// 判断商品是否存在
		Boolean hexists = jedisClient.hexists(REDIS_CART_PRE + userId, itemId + "");
		if (hexists) {
			// 存在则数量相加
			String json = jedisClient.hget(REDIS_CART_PRE + userId, itemId + "");
			TbItem item = JsonUtils.jsonToPojo(json, TbItem.class);
			item.setNum(item.getNum() + num);
			jedisClient.hset(REDIS_CART_PRE + userId, itemId + "", JsonUtils.objectToJson(item));
			// 返回成功
			return E3Result.ok();
		} else {
			// 不存在则根据itemId查询商品,并添加到购物车列表
			TbItem item = itemMapper.selectByPrimaryKey(itemId);
			item.setNum(num);
			String image = item.getImage();
			if (StringUtils.isNotBlank(image)) {
				item.setImage(image.split(",")[0]);
			}
			jedisClient.hset(REDIS_CART_PRE + userId, itemId + "", JsonUtils.objectToJson(item));
			// 返回成功
			return E3Result.ok();
		}
	}

	// 合并购物车
	@Override
	public E3Result mergeCart(Long userId, List<TbItem> list) {
		// 遍历商品
		// 把列表添加到购物车
		// 看购物车中是否有此商品
		// 如果存在则数量相加,如果不存在则从数据库读取添加
		for (TbItem tbItem : list) {
			addCart(userId, tbItem.getId(), tbItem.getNum());
		}
		// 返回成功
		return E3Result.ok();
	}

	// 获得购物车列表
	@Override
	public List<TbItem> getCartlist(Long userId) {
		// 根据用户id获得购物车列表,hvals获得当前hash下的所有value
		List<String> jsonlist = jedisClient.hvals(REDIS_CART_PRE + userId);
		List<TbItem> itemList = new ArrayList<TbItem>();
		for (String json : jsonlist) {
			TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
			itemList.add(tbItem);
		}
		// 返回
		return itemList;
	}

	// 更新数量
	@Override
	public E3Result updateNum(Long userId, Long itemId, Integer num) {
		String json = jedisClient.hget(REDIS_CART_PRE + userId, itemId + "");
		TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
		tbItem.setNum(num);
		jedisClient.hset(REDIS_CART_PRE + userId, itemId + "", JsonUtils.objectToJson(tbItem));
		return E3Result.ok();
	}

	// 删除商品
	@Override
	public E3Result deleteCart(Long userId, Long itemId) {
		jedisClient.hdel(REDIS_CART_PRE + userId, itemId + "");
		return E3Result.ok();
	}

	// 删除商品,删除一个用户的购物车信息
	@Override
	public E3Result clearCart(Long userId) {
		jedisClient.del(REDIS_CART_PRE + userId);
		return E3Result.ok();
	}
}
