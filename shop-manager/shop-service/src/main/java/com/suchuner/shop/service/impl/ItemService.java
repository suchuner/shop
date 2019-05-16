package com.suchuner.shop.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.alibaba.druid.support.json.JSONUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.suchuner.shop.common.EasyUIDataResult;
import com.suchuner.shop.common.TaotaoResult;
import com.suchuner.shop.domain.TbItem;
import com.suchuner.shop.domain.TbItemDesc;
import com.suchuner.shop.domain.TbItemExample;
import com.suchuner.shop.mapper.TbItemDescMapper;
import com.suchuner.shop.mapper.TbItemMapper;
import com.suchuner.shop.service.IItemService;
import com.suchuner.shop.service.jedis.JedisClient;
import com.suchuner.shop.utils.IDUtils;
import com.suchuner.shop.utils.JsonUtils;

@Service
public class ItemService implements IItemService {
	@Autowired
	private TbItemMapper itemMapper;
	
	@Autowired
	private TbItemDescMapper descMapper;
	
	@Autowired
	private JmsTemplate jmsTemplate;
	
	@Autowired
	private JedisClient jedisClient;
	
	@Resource(name="topicDestination")
	private Destination destination;

	@Value("${ITEM_INFO_KEY}")
	private String ITEM_INFO_KEY;
	
	@Value("${ITEM_INFO_KEY_EXPIRE}")
	private Long ITEM_INFO_KEY_EXPIRE;
	public EasyUIDataResult findAll(Integer begin, Integer rows) {
		/*实现步骤:
		 * 1.使用PageHelper插件实现mybatis的逆向工程的分页(第一个list被分页)
		 * 2.查询
		 * 3.将数据封装到PageInfo中,并将查询的数据通过PageInfo的构造器注入
		 * 4.通过pageInfo来获取分页的有关信息,比如总页数,总记录数
		 * 5.返回数据
		 * 6.通过dubbo发布服务
		 * */
		PageHelper.startPage(begin, rows);
		TbItemExample example=new TbItemExample();
		List<TbItem> list = itemMapper.selectByExample(example);
		PageInfo<TbItem> info = new PageInfo<>(list);
		EasyUIDataResult result=new EasyUIDataResult();
		result.setTotal(info.getTotal());
		result.setRows(info.getList());
		return result;
	}

	public TaotaoResult saveItem(TbItem item, String desc) {
		/*补全商品表的属性
		 * 商品id,商品状态，1-正常，2-下架，3-删除
		 * 商品对象的创建时间,更新时间
		 * */
		final long itemId = IDUtils.genItemId();
		item.setId(itemId);
		item.setStatus(TbItem.ITEM_STATE_NORMAL);
		item.setCreated(new Date());
		item.setUpdated(item.getCreated());
		/*将商品描述添加到商品描述表中
		 * */
		TbItemDesc itemDesc = new TbItemDesc();
		itemDesc.setItemId(item.getId());
		itemDesc.setCreated(new Date());
		itemDesc.setUpdated(itemDesc.getCreated());
		itemDesc.setItemDesc(desc);
		/*将数据保存到数据库中*/
		itemMapper.insert(item);
		descMapper.insert(itemDesc);
		
		/*通过dubbo发布服务*/
		jmsTemplate.send(destination, new MessageCreator() {
			
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage(itemId+"");
			}
		});
		return TaotaoResult.ok();
	}

	public TbItem findItemByItemId(Long itemId) {
		try {
			String string = jedisClient.get(ITEM_INFO_KEY+":"+itemId+"BASE");
			if(string!=null&&!string.equals("")){
				jedisClient.expire(ITEM_INFO_KEY+":"+itemId+"BASE", ITEM_INFO_KEY_EXPIRE.intValue());
				System.out.println("商品详情是从缓存中取的");
				return JsonUtils.jsonToPojo(string, TbItem.class);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		TbItem tbItem = itemMapper.selectByPrimaryKey(itemId);
		System.out.println("商品详情是从数据库中取的");
		try {
			jedisClient.set(ITEM_INFO_KEY+":"+itemId+"BASE",JsonUtils.objectToJson(tbItem));
			jedisClient.expire(ITEM_INFO_KEY+":"+itemId+"BASE", ITEM_INFO_KEY_EXPIRE.intValue());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tbItem;
	}

	public TbItemDesc findItemDescByItemId(Long itemId) {
		try {
			String string = jedisClient.get(ITEM_INFO_KEY+":"+itemId+"DESC");
			if(string!=null&&!string.equals("")){
				jedisClient.expire(ITEM_INFO_KEY+":"+itemId+"DESC", ITEM_INFO_KEY_EXPIRE.intValue());
				System.out.println("商品详情是从缓存中取的");
				return JsonUtils.jsonToPojo(string, TbItemDesc.class);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		TbItemDesc tbItemDesc = descMapper.selectByPrimaryKey(itemId);
		System.out.println("商品详情是从数据库中取的");
		try {
			jedisClient.set(ITEM_INFO_KEY+":"+itemId+"DESC",JsonUtils.objectToJson(tbItemDesc));
			jedisClient.expire(ITEM_INFO_KEY+":"+itemId+"DESC", ITEM_INFO_KEY_EXPIRE.intValue());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tbItemDesc;
	}

}
