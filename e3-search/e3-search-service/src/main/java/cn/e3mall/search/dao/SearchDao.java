package cn.e3mall.search.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.e3mall.common.pojo.SearchItem;
import cn.e3mall.common.pojo.SearchResult;

/**
 * 商品搜索dao
 * @author wlp
 *
 */

@Repository
public class SearchDao {

	@Autowired
	private SolrServer solrServer;
	
	/**
	 * 根据查询条件查询索引库
	 * @param query
	 * @return
	 * @throws Exception 
	 */
	public SearchResult search(SolrQuery query) throws Exception {
		// 根据query查询索引库
		// 创建slorserver连接,构造参数服务地址
		QueryResponse queryResponse = solrServer.query(query);
		// 取查询结果
		SolrDocumentList solrDocumentList = queryResponse.getResults();
		Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
		// 取总记录数
		long recourdCount = solrDocumentList.getNumFound();
		// 取商品列表,包括高亮
		List<SearchItem> itemList = new ArrayList<SearchItem>();

		for (SolrDocument solrDocument : solrDocumentList) {
			String id = (String) solrDocument.get("id");
			List<String> list = highlighting.get(id).get("item_title");
			String item_title = null;
			if (list != null && list.size() > 0) {
				item_title = list.get(0);
			} else {
				item_title = (String) solrDocument.get("item_title");
			}
			String item_sell_point = (String) solrDocument.get("item_sell_point");
			Long item_price = (Long) solrDocument.get("item_price");
			String item_image = (String) solrDocument.get("item_image");
			String item_category_name = (String) solrDocument.get("item_category_name");
			SearchItem item = new SearchItem();
			item.setId(id);
			item.setTitle(item_title);
			item.setSell_point(item_sell_point);
			item.setPrice(item_price);
			item.setImage(item_image);
			item.setCategory_name(item_category_name);

			itemList.add(item);
		}
		// 返回SearchResult
		SearchResult searchResult = new SearchResult();
		searchResult.setRecourdCount(recourdCount);
		searchResult.setItemList(itemList);
		return searchResult;
	}
}
