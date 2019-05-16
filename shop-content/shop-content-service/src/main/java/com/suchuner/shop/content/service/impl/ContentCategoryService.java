package com.suchuner.shop.content.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suchuner.shop.common.EasyUITreeResult;
import com.suchuner.shop.common.TaotaoResult;
import com.suchuner.shop.content.service.IContentCategoryService;
import com.suchuner.shop.domain.TbContentCategory;
import com.suchuner.shop.domain.TbContentCategoryExample;
import com.suchuner.shop.mapper.TbContentCategoryMapper;

@Service
public class ContentCategoryService implements IContentCategoryService {

	@Autowired
	private TbContentCategoryMapper categoryMapper;

	public List<EasyUITreeResult> getContentCategoryTree(Long parentId) {
		TbContentCategoryExample example = new TbContentCategoryExample();
		example.createCriteria().andParentIdEqualTo(parentId);
		List<TbContentCategory> list = categoryMapper.selectByExample(example );
		List<EasyUITreeResult> results= new ArrayList<>();
		for (TbContentCategory tbContentCategory : list) {
			EasyUITreeResult result = new EasyUITreeResult();
			result.setId(tbContentCategory.getId());
			result.setState(tbContentCategory.getIsParent()?"closed":"open");
			result.setText(tbContentCategory.getName());
			results.add(result);
		}
		return results;
	}

	public TaotaoResult addContentCategory(Long parentId, String name) {
		/*需求:添加内容结点的步骤:
		 * 1.创建被添加节点的对象,将数据补充完成,添加父节点id,设置isparent为false,等
		 * 2.更新父节点,判断父节点是否是叶子节点,是:设置isparent为true,否:不变
		 * 3.添加返回节点的对象,封装了ContentCategory对象,包含了id属性,所以需要主键返回,修改逆向工程
		 * */
		TbContentCategory contentCategory = new TbContentCategory();
		contentCategory.setIsParent(false);
		contentCategory.setParentId(parentId);
		contentCategory.setName(name);
		contentCategory.setCreated(new  Date());
		contentCategory.setUpdated(contentCategory.getCreated());
		//状态。可选值:1(正常),2(删除)
		contentCategory.setSortOrder(1);
		contentCategory.setStatus(TbContentCategory.CONTENTCATEGORY_STATE_NORMAL);
		categoryMapper.insert(contentCategory);
		//=====================
		TbContentCategory category = categoryMapper.selectByPrimaryKey(parentId);
		if(!category.getIsParent()){
			category.setIsParent(true);
			categoryMapper.updateByPrimaryKey(category);
		}
		return TaotaoResult.ok(contentCategory);
	}

	public TaotaoResult updateContentCategory(Long id, String name) {
		TbContentCategory contentCategory = categoryMapper.selectByPrimaryKey(id);
		contentCategory.setName(name);
		categoryMapper.updateByPrimaryKey(contentCategory);
		return TaotaoResult.ok();
		}

	public TaotaoResult deleteContentCategory(Long id) {
		/*需求:删除结点步骤
		 * 1.判断当前节点是否是父节点,是:不删除,返回,否:删除该结点
		 * 2.建立在该结点是叶子结点的基础上,判断父节点的叶子节点是否还有,有,不修改,否:修改父节点为叶子结点
		 * */
		TbContentCategory contentCategory = categoryMapper.selectByPrimaryKey(id);
		if(contentCategory.getIsParent()){
			return TaotaoResult.build(500, "当前节点为父节点,不被允许删除");
		}
		categoryMapper.deleteByPrimaryKey(id);
		TbContentCategoryExample example = new TbContentCategoryExample();
		example.createCriteria().andParentIdEqualTo(contentCategory.getParentId());
		List<TbContentCategory> list = categoryMapper.selectByExample(example );
		if(list.isEmpty()||list.size()==0){
			TbContentCategory category = categoryMapper.selectByPrimaryKey(contentCategory.getParentId());
			category.setIsParent(false);
			categoryMapper.updateByPrimaryKey(category);
		}
		return TaotaoResult.ok();
	}

}
