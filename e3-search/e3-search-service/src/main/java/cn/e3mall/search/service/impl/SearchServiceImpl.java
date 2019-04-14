package cn.e3mall.search.service.impl;

import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.e3mall.common.pojo.SearchResult;
import cn.e3mall.search.dao.SearchDao;
import cn.e3mall.search.service.SearchService;

/**
 * 商品搜索service
 * @author wlp
 *
 */

@Service
@Transactional
public class SearchServiceImpl implements SearchService {

	@Autowired
	private SearchDao searchDao;

	@Override
	public SearchResult search(String keyword, Integer page, Integer rows) throws Exception {
		// 创建solrquery对象
		SolrQuery query = new SolrQuery();
		// 设置查询条件
		query.setQuery(keyword);
		// 设置分页条件
		if (page <= 0) {
			page = 1;
		}
		if (rows <= 0) {
			rows = 32;
		}
		query.setStart((page - 1) * rows);

		query.setRows(rows);
		// 设置默认搜索于
		query.set("df", "item_title");
		// 开启高亮,前,后缀
		query.setHighlight(true);
		query.addHighlightField("item_title");
		query.setHighlightSimplePre("<em style=\"color:blue\">");
		query.setHighlightSimplePost("</em>");
		// 调用dao执行查询
		SearchResult searchResult = searchDao.search(query);
		// 计算总页数,并设置
		Long recourdCount = searchResult.getRecourdCount();
		long totalPages = recourdCount / rows;
		if (recourdCount % rows > 0) {
			totalPages++;
		}
		searchResult.setTotalPages(totalPages);
		// 返回SearchResult
		return searchResult;
	}
}
