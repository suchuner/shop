package com.suchuner.shop.cart.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.suchuner.shop.common.TaotaoResult;
import com.suchuner.shop.domain.TbItem;
import com.suchuner.shop.service.IItemService;
import com.suchuner.shop.utils.CookieUtils;
import com.suchuner.shop.utils.JsonUtils;

@Controller
public class CartController {
	@Autowired
	private IItemService itemService;

	@Value("${CART_ITEMS_KEY}")
	private String CART_ITEMS_KEY;

	@Value("${CART_ITEMS_EXPIRE}")
	private Long CART_ITEMS_EXPIRE;

	@RequestMapping("/cart/add/{itemId}")
	public String addItemToCart(@PathVariable Long itemId, @RequestParam(defaultValue="1")Integer num, HttpServletRequest request,
			HttpServletResponse response) {
		/*
		 * 需求:未登录下的添加进购物车中,即将商品信息放入cookie中 实现步骤:
		 * 1.根据传入的商品id在cookie中查询,如果存在,则数量增加,不存在,从数据库中查询,然后存入cookie中
		 */
		List<TbItem> cartList = getCartList(request);
		boolean flag = false;
		for (TbItem tbItem : cartList) {
			if (tbItem.getId() == itemId.longValue()) {
				tbItem.setNum(tbItem.getNum() + num);
				flag = true;
				break;
			}
		}
		if (flag) {
			CookieUtils.setCookie(request, response, CART_ITEMS_KEY, JsonUtils.objectToJson(cartList),
					CART_ITEMS_EXPIRE.intValue(), true);
		} else {
			TbItem item = itemService.findItemByItemId(itemId);
			String images = item.getImage();
			if(StringUtils.isNotBlank(images)){
				String[] split = images.split(",");
				item.setImage(split[0]);
			}
			item.setNum(num);
			cartList.add(item);
			CookieUtils.setCookie(request, response, CART_ITEMS_KEY, JsonUtils.objectToJson(cartList),
					CART_ITEMS_EXPIRE.intValue(), true);
		}
		return "cartSuccess";
	}

	@RequestMapping("/cart/cart")
	public String showItemCart(HttpServletRequest request) {
		/*未登录下的购物车的展示*/
		List<TbItem> cartList = getCartList(request);
		request.setAttribute("cartList", cartList);
		return "cart";
	}
	@RequestMapping("/cart/delete/{itemId}")
	public String deleteItemInCart(@PathVariable Long itemId,HttpServletRequest request, HttpServletResponse response) {
		/*未登录下的购物车的展示*/
		List<TbItem> cartList = getCartList(request);
		for (TbItem tbItem : cartList) {
			if(tbItem.getId()==itemId.longValue()){
				cartList.remove(tbItem);
				break;
			}
		}
		CookieUtils.setCookie(request, response, CART_ITEMS_KEY, JsonUtils.objectToJson(cartList), CART_ITEMS_EXPIRE.intValue(),true);
		return "redirect:/cart/cart.html";
	}
	@RequestMapping("/cart/update/num/{itemId}/{num}")
	@ResponseBody
	public TaotaoResult updateItemInCart(@PathVariable Long itemId,@PathVariable Integer num,HttpServletRequest request, HttpServletResponse response) {
		/*未登录下的购物车的展示*/
		List<TbItem> cartList = getCartList(request);
		for (TbItem tbItem : cartList) {
			if(tbItem.getId()==itemId.longValue()){
				tbItem.setNum(num);
				break;
			}
		}
		CookieUtils.setCookie(request, response, CART_ITEMS_KEY, JsonUtils.objectToJson(cartList), CART_ITEMS_EXPIRE.intValue(),true);
		return TaotaoResult.ok();
	}

	private List<TbItem> getCartList(HttpServletRequest request) {
		String cookieValue = CookieUtils.getCookieValue(request, CART_ITEMS_KEY, true);
		List<TbItem> list = new ArrayList<>();
		if (StringUtils.isNotBlank(cookieValue)) {
			list = JsonUtils.jsonToList(cookieValue, TbItem.class);
		}
		return list;
	}
}
