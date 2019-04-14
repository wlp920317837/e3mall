package cn.e3mall.itemweb.test;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class TestFreeMarker {

	@Test
	public void testFreeMarker() throws Exception {
		// 1.创建模板文件.ftl
		// 2.创建一个configuration对象
		Configuration configuration = new Configuration(Configuration.getVersion());
		// 3.设置模板文件保存的路径
		configuration.setDirectoryForTemplateLoading(new File("E:/workspacetest/e3-item-web/src/main/webapp/WEB-INF/ftl"));
		// 4.模板文件编码格式,一般utf-8
		configuration.setDefaultEncoding("utf-8");
		// 5.加载一个模板文件.创建一个模板对象
		// Template template = configuration.getTemplate("hello.ftl");
		Template template = configuration.getTemplate("student.ftl");
		// 6.创建一个数据集,可以是pojo也可以是map,推荐使用map
		Map map = new HashMap();
		// 添加一个字符串
		map.put("hello", "hello freemarker");
		// 添加一个pojo
		Student student = new Student(1, "小明", 18, "杭州");
		map.put("student", student);
		// 添加一个list
		List<Student> list = new ArrayList<Student>();
		Student student1 = new Student(2, "小黑", 11, "北京");
		Student student2 = new Student(3, "小白", 12, "河北");
		list.add(student);
		list.add(student1);
		list.add(student2);
		map.put("stuList", list);
		// 添加日期类型
		map.put("date", new Date());
		// 添加null值
		map.put("val", null);
		// map.put("val", 123);
		// 7.创建一个write对象,指定输出文件的路径和文件名
		Writer writer = new FileWriter(new File("E:/temp/student.html"));
		// 8.输出,生成静态页面
		template.process(map, writer);
		// 9.关流
		writer.close();
	}
}
