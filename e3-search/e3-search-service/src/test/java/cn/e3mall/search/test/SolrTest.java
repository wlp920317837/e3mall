package cn.e3mall.search.test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

public class SolrTest {

	@Test
	public void addDocument() throws SolrServerException, IOException {
		// 创建slorserver连接,构造参数服务地址
		SolrServer server = new HttpSolrServer("http://192.168.217.129:8080/solr/collection1");
		// 新建文档对象SolrInputDocument
		SolrInputDocument document = new SolrInputDocument();
		// 新建业务域
		document.addField("id", "doc01");
		document.addField("item_title", "测试商品01");
		document.addField("item_price", 1000);
		// 提交文档
		server.add(document);
		// 提交
		server.commit();
	}

	@Test
	public void delDocument() throws SolrServerException, IOException {
		// 创建slorserver连接,构造参数服务地址
		SolrServer server = new HttpSolrServer("http://192.168.217.129:8080/solr/collection1");
		// 删除文档
		// server.deleteById("doc01");
		server.deleteByQuery("*:*"); // 根据查询条件删除
		server.commit();
	}

	@Test
	public void testQuery() throws Exception {
		// 创建slorserver连接,构造参数服务地址
		SolrServer server = new HttpSolrServer("http://192.168.217.129:8080/solr/collection1");
		// 创建查询对象solrquery
		SolrQuery query = new SolrQuery();
		// 设置查询条件
		query.setQuery("手机");
		// 设置附件条件
		// 1.商品分类名
		// query.addFilterQuery("fq1", "item_category_name:手机");
		// 2.价格区间
		// query.addFilterQuery("fq2", "item_price:{* TO * }");
		// 设置价格排序
		// query.setSort("item_price", ORDER.asc);
		// 设置分页
		query.setStart(0);
		query.setRows(5);
		// 设置默认查询域
		query.set("df", "item_title");
		// 开启高亮
		query.setHighlight(true);
		query.setHighlightSimplePost("</em>");
		query.setHighlightSimplePre("<em>");

		// 执行查询
		QueryResponse queryResponse = server.query(query);
		// 获得查询结果集和总记录数和高亮文本
		SolrDocumentList solrDocumentList = queryResponse.getResults();
		System.out.println(solrDocumentList.getNumFound());
		Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
		// 遍历结果集
		for (SolrDocument solrDocument : solrDocumentList) {
			System.out.println(solrDocument.get("id"));
			List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");
			if (list != null && list.size() > 0) {
				String highlightTitle = list.get(0);
				System.out.println(highlightTitle);
			}
			System.out.println(solrDocument.get("item_title"));
			System.out.println(solrDocument.get("item_sell_point"));
			System.out.println(solrDocument.get("item_price"));
			System.out.println(solrDocument.get("item_image"));
			System.out.println(solrDocument.get("item_category_name"));
		}
	}
}
