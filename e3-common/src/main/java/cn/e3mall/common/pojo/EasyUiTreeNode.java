package cn.e3mall.common.pojo;

import java.io.Serializable;

/**
 * EasyUiTree生成叶子节点工具
 * 
 * id:当前节点id
 * text:当前节点名称
 * state:open无子节点,close有子节点点击后继续查询
 * @author wlp
 *
 */
public class EasyUiTreeNode implements Serializable {

	private Long id;
	private String text;
	private String state;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
}
