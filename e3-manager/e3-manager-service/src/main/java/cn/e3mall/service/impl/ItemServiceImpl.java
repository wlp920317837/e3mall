package cn.e3mall.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.pojo.EasyUiDataGridResult;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.IDUtils;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.mapper.TbItemDescMapper;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.pojo.TbItemDescExample;
import cn.e3mall.pojo.TbItemDescExample.Criteria;
import cn.e3mall.pojo.TbItemExample;
import cn.e3mall.service.ItemService;

@Service
@Transactional
public class ItemServiceImpl implements ItemService {

	// jedis缓存客户端
	@Autowired
	private JedisClient jedisClient;

	// 缓存key
	@Value("${REDIS_ITEM_PRE}")
	private String REDIS_ITEM_PRE;

	// 缓存保持时间
	@Value("${ITEM_EXPRIRE}")
	private Integer ITEM_EXPRIRE;

	// JmsTemplate,消息发送对象
	@Autowired
	private JmsTemplate jmsTemplate;

	// Destination,消息类型,广播型
	@Resource
	private Destination topicDestination;

	// 商品dao
	@Autowired
	private TbItemMapper itemMapper;

	// 商品描述dao
	@Autowired
	private TbItemDescMapper itemDescMapper;

	// 根据商品id查询商品
	@Override
	public TbItem selectItemById(long itemId) {
		// 查询缓存
		try {
			String json = jedisClient.get(REDIS_ITEM_PRE + itemId + ":BASE");
			if (StringUtils.isNotBlank(json)) {
				TbItem item = JsonUtils.jsonToPojo(json, TbItem.class);
				return item;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 根据主键查询
		TbItem item = itemMapper.selectByPrimaryKey(itemId);

		// 添加缓存
		try {
			if (item != null) {
				String json = JsonUtils.objectToJson(item);
				jedisClient.set(REDIS_ITEM_PRE + itemId + ":BASE", json);
				jedisClient.expire(REDIS_ITEM_PRE + itemId + ":BASE", ITEM_EXPRIRE);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return item;
		}

		return item;
	}

	// 根据商品id获得商品描述
	@Override
	public TbItemDesc selectItemDescById(long itemId) {
		// 查询缓存
		try {
			String json = jedisClient.get(REDIS_ITEM_PRE + itemId + ":DESC");
			if (StringUtils.isNotBlank(json)) {
				TbItemDesc itemDesc = JsonUtils.jsonToPojo(json, TbItemDesc.class);
				return itemDesc;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		TbItemDesc itemDesc = itemDescMapper.selectByPrimaryKey(itemId);

		// 添加缓存
		try {
			if (itemDesc != null) {
				String json = JsonUtils.objectToJson(itemDesc);
				jedisClient.set(REDIS_ITEM_PRE + itemId + ":DESC", json);
				jedisClient.expire(REDIS_ITEM_PRE + itemId + ":DESC", ITEM_EXPRIRE);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return itemDesc;
		}

		return itemDesc;
	}

	// easyui商品分页查询
	@Override
	public EasyUiDataGridResult getItemList(Integer page, Integer rows) {
		// 设置分页条件
		PageHelper.startPage(page, rows);

		// 设置查询条件
		TbItemExample example = new TbItemExample();
		List<TbItem> list = itemMapper.selectByExample(example);

		// 获得分页查询结果
		PageInfo<TbItem> pageInfo = new PageInfo<TbItem>(list);
		long total = pageInfo.getTotal();

		// 封装分页对象
		EasyUiDataGridResult result = new EasyUiDataGridResult();
		result.setTotal(total);
		result.setRows(list);

		return result;
	}

	// 添加一个商品
	@Override
	public E3Result addItem(TbItem tbItem, String desc) {
		// 生成商品id
		final Long id = IDUtils.genItemId();
		// 补全tbitem属性
		tbItem.setId(id);
		// '商品状态，1-正常，2-下架，3-删除'
		tbItem.setStatus((byte) 1);
		tbItem.setCreated(new Date());
		tbItem.setUpdated(new Date());
		// 向商品表插入数据
		itemMapper.insert(tbItem);

		// 商品描述表
		TbItemDesc itemDesc = new TbItemDesc();
		// 补全商品描述表属性
		itemDesc.setItemId(id);
		itemDesc.setItemDesc(desc);
		itemDesc.setCreated(new Date());
		itemDesc.setUpdated(new Date());
		// 向商品描述表插入数据
		itemDescMapper.insert(itemDesc);

		// 发送一个商品添加消息
		jmsTemplate.send(topicDestination, new MessageCreator() {

			@Override
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage(id + "");
			}
		});

		// 返回成功
		return E3Result.ok();
	}

}
