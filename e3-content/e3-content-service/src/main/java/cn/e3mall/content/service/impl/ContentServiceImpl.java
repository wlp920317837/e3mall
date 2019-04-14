package cn.e3mall.content.service.impl;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.pojo.EasyUiDataGridResult;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.content.service.ContentService;
import cn.e3mall.mapper.TbContentMapper;
import cn.e3mall.pojo.TbContent;
import cn.e3mall.pojo.TbContentExample;
import cn.e3mall.pojo.TbContentExample.Criteria;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 内容管理服务
 * @author wlp
 *
 */

@Service
@Transactional
public class ContentServiceImpl implements ContentService {

	// CONTENT_LIST
	@Value("${CONTENT_LIST}")
	private String CONTENT_LIST;

	// redis缓存客户端
	@Autowired
	private JedisClient jedisClient;

	// 内容dao
	@Autowired
	private TbContentMapper contentMapper;

	// 分页展示内容列表categoryId=90&page=1&rows=20
	@Override
	public EasyUiDataGridResult getContentList(Integer page, Integer rows, Long categoryId) {

		// 开始分页
		PageHelper.startPage(page, rows);

		// 设置查询条件
		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(categoryId);
		List<TbContent> contentList = contentMapper.selectByExample(example);

		// 获得分页查询结果
		PageInfo<TbContent> pageInfo = new PageInfo<TbContent>(contentList);
		long total = pageInfo.getTotal();

		// 封装分页对象
		EasyUiDataGridResult result = new EasyUiDataGridResult();
		result.setTotal(total);
		result.setRows(contentList);

		return result;
	}

	// 新增内容,返回值e3result,输入参数表单用pojo接收
	@Override
	public E3Result addContent(TbContent content) {
		// 页面接收过来的content,不完整
		content.setCreated(new Date());
		content.setUpdated(new Date());

		// 保存到数据库
		contentMapper.insertSelective(content);

		// 缓存同步,即只要将原先的缓存清空
		jedisClient.hdel(CONTENT_LIST, content.getCategoryId().toString());

		return E3Result.ok();
	}

	// 修改内容,返回值e3result,输入参数表单用pojo接收
	@Override
	public E3Result editContent(TbContent content) {
		// 页面传入不完整,补全
		content.setUpdated(new Date());

		// 将修改保存到数据库
		contentMapper.updateByPrimaryKeyWithBLOBs(content);

		// 缓存同步,即只要将原先的缓存清空
		jedisClient.hdel(CONTENT_LIST, content.getCategoryId().toString());

		return E3Result.ok();
	}

	// 返回值e3result,根据"ids":ids批量删除内容
	@Override
	public E3Result deleteContent(String ids) {
		Long categoryId = null;
		// 页面传入ids字符串.使用正则分割
		String[] idArr = ids.split(",");
		for (String idStr : idArr) {
			// 根据id删除内容
			Long id = new Long(idStr);
			TbContent tbContent = contentMapper.selectByPrimaryKey(id);
			categoryId = tbContent.getCategoryId();
			contentMapper.deleteByPrimaryKey(id);
		}

		// 缓存同步,即只要将原先的缓存清空
		jedisClient.hdel(CONTENT_LIST, categoryId.toString());

		return E3Result.ok();
	}

	// 根据内容分类cid查询内容
	@Override
	public List<TbContent> getContentListByCid(Long categoryId) {
		// 先查询缓存
		try {
			// hget从缓存中取得json
			String json = jedisClient.hget(CONTENT_LIST, categoryId + "");
			// 如果在缓存中有则直接返回
			if (StringUtils.isNotBlank(json)) {
				List<TbContent> list = JsonUtils.jsonToList(json, TbContent.class);
				return list;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 如果没有则查询数据库
		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		// 设置查询条件
		criteria.andCategoryIdEqualTo(categoryId);
		List<TbContent> list = contentMapper.selectByExampleWithBLOBs(example);

		// 将数据添加到缓存
		try {
			jedisClient.hset(CONTENT_LIST, categoryId + "", JsonUtils.objectToJson(list));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}
}
