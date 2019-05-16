package com.suchuner.shop.order.service;

import com.suchuner.shop.common.TaotaoResult;
import com.suchuner.shop.order.pojo.OrderInfo;

public interface IOrderService {
/**添加用户订单
 * @param orderInfo 订单信息包装类
 * @return
 */
public TaotaoResult createOrder(OrderInfo orderInfo);
}
