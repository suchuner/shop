package com.suchuner.shop.order.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.suchuner.shop.common.TaotaoResult;
import com.suchuner.shop.domain.TbOrderItem;
import com.suchuner.shop.domain.TbOrderShipping;
import com.suchuner.shop.mapper.TbOrderItemMapper;
import com.suchuner.shop.mapper.TbOrderMapper;
import com.suchuner.shop.mapper.TbOrderShippingMapper;
import com.suchuner.shop.order.pojo.OrderInfo;
import com.suchuner.shop.order.service.IOrderService;
import com.suchuner.shop.order.service.jedis.JedisClient;

@Service
public class OrderService implements IOrderService {
	@Autowired
	private TbOrderMapper orderMapper;
	@Autowired
	private TbOrderItemMapper orderItemMapper;
	@Autowired
	private TbOrderShippingMapper orderShippingMapper;
	@Autowired
	private JedisClient jedisClient;	
	@Value("${ORDER_GEN_KEY}")
	private String ORDER_GEN_KEY;
	@Value("${ORDER_ID_BEGIN}")
	private String ORDER_ID_BEGIN;
	@Value("${ORDER_ITEM_ID_GEN_KEY}")
	private String ORDER_ITEM_ID_GEN_KEY;

	public TaotaoResult createOrder(OrderInfo orderInfo) {
			// 1、接收表单的数据
			// 2、生成订单id
			if (!jedisClient.exists(ORDER_GEN_KEY)) {
				//设置初始值
				jedisClient.set(ORDER_GEN_KEY, ORDER_ID_BEGIN);
			}
			String orderId = jedisClient.incr(ORDER_GEN_KEY).toString();
			orderInfo.setOrderId(orderId);
			orderInfo.setPostFee("0");
			//1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭
			orderInfo.setStatus(1);
			Date date = new Date();
			orderInfo.setCreateTime(date);
			orderInfo.setUpdateTime(date);
			// 3、向订单表插入数据。
			orderMapper.insert(orderInfo);
			// 4、向订单明细表插入数据
			List<TbOrderItem> orderItems = orderInfo.getOrderItems();
			for (TbOrderItem tbOrderItem : orderItems) {
				//生成明细id
				Long orderItemId = jedisClient.incr(ORDER_ITEM_ID_GEN_KEY);
				tbOrderItem.setId(orderItemId.toString());
				tbOrderItem.setOrderId(orderId);
				//插入数据
				orderItemMapper.insert(tbOrderItem);
			}
			// 5、向订单物流表插入数据。
			TbOrderShipping orderShipping = orderInfo.getOrderShipping();
			orderShipping.setOrderId(orderId);
			orderShipping.setCreated(date);
			orderShipping.setUpdated(date);
			orderShippingMapper.insert(orderShipping);
			// 6、返回TaotaoResult。
			return TaotaoResult.ok(orderId);
		}
}
