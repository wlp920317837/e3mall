package cn.e3mall.common.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * EasyUi分页的工具类
 * total 总条数
 * rows 每页内容
 * @author wlp
 *
 */
public class EasyUiDataGridResult implements Serializable {

	private Long total;
	private List rows;

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public List getRows() {
		return rows;
	}

	public void setRows(List rows) {
		this.rows = rows;
	}

}
