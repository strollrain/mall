package com.mmall.service;

import java.util.List;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.Category;

public interface ICategoryService {
	//添加品类
	ServerResponse addCategory(String  categoryName,Integer parentId);
	ServerResponse updateCategoryName(Integer categoryId,String categoryName);
	ServerResponse<List<Category>> getChildParallelCategory(Integer categoryId);
	ServerResponse<List<Integer>> selectCategoryAndChildrenById(Integer categoryId);
}
