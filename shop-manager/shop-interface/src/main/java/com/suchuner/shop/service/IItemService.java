package com.suchuner.shop.service;

import com.suchuner.shop.common.EasyUIDataResult;
import com.suchuner.shop.common.TaotaoResult;
import com.suchuner.shop.domain.TbItem;
import com.suchuner.shop.domain.TbItemDesc;

public interface IItemService {
		/**
		 * @param begin 开始的页码
		 * @param rows 每页显示的记录数
		 * @return 封装好的easyUI的数据对象,包括total,rows
		 */
		public EasyUIDataResult findAll(Integer begin,Integer rows);

		/**
		 * @param item 需要保存的商品对象(对象中包含商品信息)
		 * @param desc 商品的描述,需要将该信息添加到商品的描述表中
		 * @return 保存的结果
		 */
		public TaotaoResult saveItem(TbItem item, String desc);
		
		/**根据商品的id查询商品对象
		 * @param itemId
		 * @return
		 */
		public TbItem findItemByItemId(Long itemId);
		
		/**根据商品的id查询商品的描述对象
		 * @param itemId
		 * @return
		 */
		public TbItemDesc findItemDescByItemId(Long itemId);
}
