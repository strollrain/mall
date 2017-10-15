package com.mmall.service.impl;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.pojo.Category;
import com.mmall.service.ICategoryService;
/**
 * @author rain;
 *
 */
@Service("ICategoryService")
public class CategoryServiceImpl implements ICategoryService {

	private Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class	);
	@Autowired
	private CategoryMapper categoryMapper;
	public ServerResponse addCategory(String  categoryName,Integer parentId) {
		if(parentId==null || StringUtils.isBlank(categoryName)) {
			ServerResponse.createByErrorMessage("添加品类参数错误");
		}
		Category category=new Category();
		category.setName(categoryName);
		category.setParentId(parentId);
		category.setStatus(true);
		
		int rowCount = categoryMapper.insert(category);
		if(rowCount>0) {
			return ServerResponse.createBySuccess("添加品类成功");
		}
		return ServerResponse.createByErrorMessage("添加品类失败");
		
		
	}
	
	
	// 更新categoryName

	 public ServerResponse updateCategoryName(Integer categoryId,String categoryName) {
		 //参数校验
			if(categoryId==null || StringUtils.isBlank(categoryName)) {
				ServerResponse.createByErrorMessage("添加品类参数错误");
			}
			Category category=new Category();
			//设置id 为了选择性更新 	
			category.setId(categoryId);
			category.setName(categoryName);
			
			int rowCount=categoryMapper.updateByPrimaryKeySelective(category);
			if(rowCount>0) {
				return ServerResponse.createBySuccess("更新品类成功");
			}
			return ServerResponse.createByErrorMessage("更新品类失败");
	 }
	
	//获取品类子节点(平级)
	 public ServerResponse<List<Category>> getChildParallelCategory(Integer categoryId){
		 List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);
		 //判断集合  为空时并不需要返回错误  打印日志
		 if(CollectionUtils.isEmpty(categoryList)) {
			 logger.info("未找到当前分类子分类");
		 }
		 return ServerResponse.createBySuccess(categoryList);
		 
	 }
	 
	 //获取当前分类id及递归子节点categoryId
	public ServerResponse<List<Integer>> selectCategoryAndChildrenById(Integer categoryId) {   
		Set<Category> categorySet=Sets.newHashSet();
		//调用私有递归函数
		findChildCategory(categorySet,categoryId);
		
		//创建集合接收遍历结果
		List<Integer> categoryIdList=Lists.newArrayList();
		if(categoryId != null) {
			for(Category categoryItem : categorySet) {
				categoryIdList.add(categoryItem.getId());
			}
		}
		return ServerResponse.createBySuccess(categoryIdList);
		
	}
	//递归算法算出子节点
	private Set<Category> findChildCategory(Set<Category> categorySet,Integer categoryId){
		Category category=categoryMapper.selectByPrimaryKey(categoryId);
		if(category != null) {
			categorySet.add(category);
		}
		//查找子节点，递归算法一定要有退出条件         //mybatis避免的返回 null的错误  否则遍历时需要空判断
		 List<Category> categoryList=categoryMapper.selectCategoryChildrenByParentId(categoryId);
		 
		 for(Category categoryItem : categoryList) {
			 findChildCategory(categorySet,categoryItem.getId());
		 }
		 return categorySet;
	}
	
	
	
	
	
	
	
}
