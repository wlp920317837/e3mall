package cn.e3mall.search.message;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import cn.e3mall.common.pojo.SearchItem;
import cn.e3mall.search.mapper.ItemMapper;

/**
 * 监听商品添加消息,一旦添加则同步信息到索引库
 * @author wlp
 *
 */
public class ItemAddMessageListener implements MessageListener {

	// solr服务
	@Autowired
	private SolrServer solrServer;

	// 商品索引mapper
	@Autowired
	private ItemMapper itemMapper;

	@Override
	public void onMessage(Message message) {
		// 从消息中获得商品id
		TextMessage textMessage = (TextMessage) message;
		try {
			String itemId = textMessage.getText();
			// 等待事务提交
			Thread.sleep(1000);
			// 根据id获得商品
			SearchItem searchItem = itemMapper.getItemByID(new Long(itemId));
			// 创建一个文档对象
			SolrInputDocument document = new SolrInputDocument();
			// 向文档对象中添加域
			document.addField("id", searchItem.getId());
			document.addField("item_title", searchItem.getTitle());
			document.addField("item_sell_point", searchItem.getSell_point());
			document.addField("item_price", searchItem.getPrice());
			document.addField("item_image", searchItem.getImage());
			document.addField("item_category_name", searchItem.getCategory_name());
			// 把文档写入索引库
			solrServer.add(document);
			// 提交
			solrServer.commit();
			System.out.println("索引库添加成功");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
