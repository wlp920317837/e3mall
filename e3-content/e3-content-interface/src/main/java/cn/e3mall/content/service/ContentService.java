package cn.e3mall.content.service;

import cn.e3mall.common.pojo.EasyUiDataGridResult;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.pojo.TbContent;

import java.util.List;

/**
 * 内容管理接口
 * @author wlp
 *
 */

public interface ContentService {

	// 分页展示内容列表categoryId=90&page=1&rows=20
	public EasyUiDataGridResult getContentList(Integer page, Integer rows, Long categoryId);

	// 新增内容,返回值e3result,输入参数表单用pojo接收
	public E3Result addContent(TbContent content);

	// 修改内容,返回值e3result,输入参数表单用pojo接收
	public E3Result editContent(TbContent content);

	// 返回值e3result,根据"ids":ids批量删除内容
	public E3Result deleteContent(String ids);

	// 根据内容分类cid查询内容
	public List<TbContent> getContentListByCid(Long categoryId);
}
