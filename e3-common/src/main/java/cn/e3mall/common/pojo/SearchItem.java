package cn.e3mall.common.pojo;

import java.io.Serializable;

/**
 * 商品索引实体
 * @author wlp
 *
 */
public class SearchItem implements Serializable {

	private static final long serialVersionUID = 1L;
	private String id;
	private String title;
	private Long price;
	private String sell_point;
	private String image;
	private String images;
	private String category_name;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Long getPrice() {
		return price;
	}

	public void setPrice(Long price) {
		this.price = price;
	}

	public String getSell_point() {
		return sell_point;
	}

	public void setSell_point(String sell_point) {
		this.sell_point = sell_point;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getCategory_name() {
		return category_name;
	}

	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}

	public String[] getImages() {
		if (image != null && !"".equals(image)) {
			String[] images = image.split(",");
			return images;
		}
		return null;
	}

}
