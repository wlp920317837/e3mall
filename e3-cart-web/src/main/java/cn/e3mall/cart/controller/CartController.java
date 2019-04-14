package cn.e3mall.cart.controller;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.utils.CookieUtils;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.service.ItemService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 购物车处理表现层
 * @author wlp
 *
 */

@Controller
public class CartController {

	@Value("${COOKIE_MAXAGE}")
	private Integer COOKIE_MAXAGE;

	// 商品服务
	@Autowired
	private ItemService itemService;

	@Autowired
	private CartService cartService;

	// 从cookie中获得购物车列表的方法
	private List<TbItem> getCartListFromCookie(HttpServletRequest request) {
		String json = CookieUtils.getCookieValue(request, "cart", true);
		if (StringUtils.isBlank(json)) {
			return new ArrayList<TbItem>();
		} else {
			List<TbItem> list = JsonUtils.jsonToList(json, TbItem.class);
			return list;
		}
	}

	// 添加购物车
	@RequestMapping("/cart/add/{itemId}")
	public String addCart(@PathVariable Long itemId, @RequestParam(defaultValue = "1") Integer num, HttpServletRequest request, HttpServletResponse response) {
		// 判断用户是否登录
		TbUser user = (TbUser) request.getAttribute("user");
		// 若果登录,则将购物车放入redis
		if (user != null) {
			// 保存到服务端
			cartService.addCart(user.getId(), itemId, num);
			return "cartSuccess";
		}
		// 从cookie中获得购物车列表
		List<TbItem> list = getCartListFromCookie(request);
		// 看商品在购物车中是否存在
		boolean flag = false;
		for (TbItem tbItem : list) {
			if (tbItem.getId() == itemId.longValue()) {
				// 如果存在,数量相加
				tbItem.setNum(tbItem.getNum() + num);
				flag = true;
				// 跳出循环
				break;
			}
		}
		// 如果不存在,则根据商品id查询数据库.用TbItem返回
		if (!flag) {
			// 没找到则根据商品id查询数据库
			TbItem tbItem = itemService.selectItemById(itemId);
			tbItem.setNum(num);
			if (StringUtils.isNotBlank(tbItem.getImage())) {
				tbItem.setImage(tbItem.getImage().split(",")[0]);
			}
			// 将商品写入购物车列表
			list.add(tbItem);
		}
		// 写回cookie
		CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(list), COOKIE_MAXAGE, true);
		// 返回添加成功页面
		return "cartSuccess";
	}

	// 展示购物车
	@RequestMapping("cart/cart")
	public String showCart(HttpServletRequest request, HttpServletResponse response) {
		// 若果是 未登录状态
		// 从cookie中获得购物车列表
		List<TbItem> cartlist = getCartListFromCookie(request);

		// 判断用户是否为登录状态
		TbUser user = (TbUser) request.getAttribute("user");
		// 如果为登录状态,将服务端的购物车和本地购物车合并
		if (user != null) {
			cartService.mergeCart(user.getId(), cartlist);
			// 删除cookie中的购物车
			CookieUtils.deleteCookie(request, response, "cart");
			// 从服务端获得购物车列表
			cartlist = cartService.getCartlist(user.getId());
		}

		// 将购物车列表添加到页面
		request.setAttribute("cartList", cartlist);
		// 返回页面
		return "cart";
	}

	// 修改购物车中商品数量(在springmvc中请求路径中为*.html时不能返回一个json)
	@RequestMapping(value = "/cart/update/num/{itemId}/{num}", method = RequestMethod.POST)
	public @ResponseBody E3Result updateCartNum(@PathVariable Long itemId, @PathVariable Integer num, HttpServletRequest request, HttpServletResponse response) {
		// 判断用户是否登录
		TbUser user = (TbUser) request.getAttribute("user");
		if (user != null) {
			cartService.updateNum(user.getId(), itemId, num);
			return E3Result.ok();
		}
		// 从cookie中获得购物车列表
		List<TbItem> list = getCartListFromCookie(request);
		// 从购物车列表中找到对应商品
		for (TbItem tbItem : list) {
			if (tbItem.getId() == itemId.longValue()) {
				// 更新商品的数量
				tbItem.setNum(num);
				// 跳出循环
				break;
			}
		}
		// 将购物车列表写回cookie
		CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(list), COOKIE_MAXAGE, true);
		// 返回成功
		return E3Result.ok();
	}

	// 删除购物车商品
	@RequestMapping("/cart/delete/{itemId}")
	public String deleteCartItem(@PathVariable Long itemId, HttpServletRequest request, HttpServletResponse response) {
		// 判断用户是否登录
		TbUser user = (TbUser) request.getAttribute("user");
		if (user != null) {
			cartService.deleteCart(user.getId(), itemId);
			// 跳转视图,重定向redirect
			return "redirect:/cart/cart.html";
		}
		// 从cookie中获得购物车列表
		List<TbItem> list = getCartListFromCookie(request);
		// 根据id找到商品
		for (TbItem tbItem : list) {
			if (tbItem.getId() == itemId.longValue()) {
				// 找到商品删除
				list.remove(tbItem);
				// 跳出循环
				break;
			}
		}
		// 将购物车列表写回cookie
		CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(list), COOKIE_MAXAGE, true);
		// 跳转视图,重定向redirect
		return "redirect:/cart/cart.html";
	}
}
