package cn.e3mall.item.message;

import cn.e3mall.item.pojo.Item;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.service.ItemService;
import freemarker.core.ParseException;
import freemarker.template.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * 监听商品添加消息,将商品详情静态化
 * @author wlp
 *
 */

public class HtmlGenMessageListener implements MessageListener {

	@Autowired
	private ItemService itemService;

	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;

	@Value("${GENHTML}")
	private String GENHTML;

	@Override
	public void onMessage(Message message) {
		try {
			// 从消息中取商品id
			TextMessage textMessage = (TextMessage) message;
			String idStr = textMessage.getText();
			Long itemId = new Long(idStr);
			// 等待事务提交
			Thread.sleep(1000);
			// 从数据库获得商品数据,基本信息和描述
			TbItem tbItem = itemService.selectItemById(itemId);
			Item item = new Item(tbItem);
			TbItemDesc itemDesc = itemService.selectItemDescById(itemId);
			// 创建数据集,将商品数据封装
			Map map = new HashMap();
			map.put("item", item);
			map.put("itemDesc", itemDesc);
			// 创建一个模板(参考jsp),用于创建静态页面
			// 加载模板对象
			Configuration configuration = freeMarkerConfigurer.getConfiguration();
			Template template = configuration.getTemplate("item.ftl");
			// 创建输出流,指定输出路径和文件名
			Writer writer = new FileWriter(new File(GENHTML + itemId + ".html"));
			// 生成静态页面
			template.process(map, writer);
			// 关闭流
			writer.close();
		} catch (JMSException e) {
			e.printStackTrace();
		} catch (TemplateNotFoundException e) {
			e.printStackTrace();
		} catch (MalformedTemplateNameException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
