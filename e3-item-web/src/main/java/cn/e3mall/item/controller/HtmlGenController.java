package cn.e3mall.item.controller;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * 生成静态页面测试
 * @author wlp
 *
 */

@Controller
public class HtmlGenController {

	// 注入freemarker对象
	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;

	@RequestMapping("/genhtml")
	public @ResponseBody String genHtml() throws Exception {
		Configuration configuration = freeMarkerConfigurer.getConfiguration();
		// 加载模板
		Template template = configuration.getTemplate("hello.ftl");
		// 创建数据集
		Map map = new HashMap();
		map.put("hello", "测试spring整合");
		// 输出流writer
		Writer out = new FileWriter(new File("E:/temp/test.html"));
		// 指定文件输出路径及文件名
		template.process(map, out);
		// 关闭流
		out.close();
		return "OK";
	}
}
