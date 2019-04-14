package cn.e3mall.manager.test;

import java.util.List;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemExample;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

public class PageHelperTest {

	@Test
	public void test() throws Exception {
		// 初始化spring容器.从容器中获得mapper对象
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring/applicationContext-dao.xml");
		TbItemMapper itemMapper = applicationContext.getBean(TbItemMapper.class);

		// 执行sql语句之前设置分页信息,使用pageHelper的startPage方法
		PageHelper.startPage(1, 10);

		// 执行查询
		TbItemExample example = new TbItemExample();
		List<TbItem> list = itemMapper.selectByExample(example);

		// 查询结果.取分页信息PageInfo,1总页数,2总记录数,3当前页码,4每页显示的条数,5每页内容
		PageInfo<TbItem> pageInfo = new PageInfo<TbItem>(list);
		System.out.println(pageInfo.getTotal());
		System.out.println(pageInfo.getPages());
		System.out.println(pageInfo.getPageSize());

		System.out.println(list.size());
	}
}
