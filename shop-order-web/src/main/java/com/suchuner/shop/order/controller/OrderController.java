package com.suchuner.shop.order.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.suchuner.shop.common.TaotaoResult;
import com.suchuner.shop.domain.TbItem;
import com.suchuner.shop.domain.TbUser;
import com.suchuner.shop.order.pojo.OrderInfo;
import com.suchuner.shop.order.service.IOrderService;
import com.suchuner.shop.utils.CookieUtils;
import com.suchuner.shop.utils.JsonUtils;

@Controller
public class OrderController {
	@Autowired
	private IOrderService orderService;
	@Value("${CART_ITEMS_KEY}")
	private String CART_ITEMS_KEY;

	@RequestMapping("/order/order-cart")
	public String cartItemsToOrder(HttpServletRequest request) {
		String cookieValue = CookieUtils.getCookieValue(request, CART_ITEMS_KEY, true);
		if (cookieValue != null) {
			List<TbItem> cartList = JsonUtils.jsonToList(cookieValue, TbItem.class);
			request.setAttribute("cartList", cartList);
			return "order-cart";
		}
		request.setAttribute("message", "亲,你的购物车没有商品存在呢");
		return "/error/exception";
	}

	@RequestMapping(value = "/order/create", method = RequestMethod.POST)
	public String createOrder(HttpServletRequest request, OrderInfo orderInfo) {
		// 1、接收表单提交的数据OrderInfo。
		// 2、补全用户信息。
		TbUser user = (TbUser) request.getAttribute("loginUser");
		orderInfo.setUserId(user.getId());
		orderInfo.setBuyerNick(user.getUsername());
		// 3、调用Service创建订单。
		TaotaoResult result = orderService.createOrder(orderInfo);
		// 取订单号
		String orderId = result.getData().toString();
		// a)需要Service返回订单号
		request.setAttribute("orderId", orderId);
		request.setAttribute("payment", orderInfo.getPayment());
		// b)当前日期加三天。
		DateTime dateTime = new DateTime();
		dateTime = dateTime.plusDays(3);
		request.setAttribute("date", dateTime.toString("yyyy-MM-dd"));
		// 4、返回逻辑视图展示成功页面
		return "success";
	}
}
