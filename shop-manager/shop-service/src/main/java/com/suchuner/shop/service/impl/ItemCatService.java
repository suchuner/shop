package com.suchuner.shop.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suchuner.shop.common.EasyUITreeResult;
import com.suchuner.shop.domain.TbItemCat;
import com.suchuner.shop.domain.TbItemCatExample;
import com.suchuner.shop.domain.TbItemCatExample.Criteria;
import com.suchuner.shop.mapper.TbItemCatMapper;
import com.suchuner.shop.service.IItemCatService;

@Service
public class ItemCatService implements IItemCatService {
	@Autowired
	private TbItemCatMapper itemCatMapper;
	

	public List<EasyUITreeResult> getTree(Long parentId) {
		/*需求:根据商品类目的父节点id查询所有的子结点,并且将查询的集合封装到EasyUITreeResult对象的集合中
		 * 步骤:由于是单表查询,可以用mybatis的逆向工程生成的查询方法进行查询
		 * 1.建立单表的条件查询,查询条件为parentId
		 * 2.将查询到的子结点集合封装到List<EasyUITreeResult>中
		 * 3.返回封装好的集合
		 * 4.发布dubbo服务
		 * */
		TbItemCatExample example = new TbItemCatExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		List<EasyUITreeResult> treeResults = new ArrayList<>();
		List<TbItemCat> list = itemCatMapper.selectByExample(example);
		for (TbItemCat cat : list) {
			EasyUITreeResult treeResult = new EasyUITreeResult();
			treeResult.setId(cat.getId());
			treeResult.setText(cat.getName());
			//state在数据库中为Boolean型,得到用三目运算符取值
			treeResult.setState(cat.getIsParent()?"closed":"open");
			treeResults.add(treeResult);
		}
		return treeResults;
	}

}
