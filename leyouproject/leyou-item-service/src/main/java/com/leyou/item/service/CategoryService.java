package com.leyou.item.service;

import com.leyou.item.mapper.CategoryMapper;
import com.leyou.item.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 根据父节点查询子节点
     * @param pid
     * @return
     */


    public List<Category> queryCategoresByPid(Long pid) {

        Category category = new Category();
        category.setParentId(pid);
        return categoryMapper.select(category);
    }

    public List<String> queryNamesByIds(List<Long> ids){
        List<Category> list = this.categoryMapper.selectByIdList(ids);

        List<String> collect = list.stream().map(c -> c.getName()).collect(Collectors.toList());

        return collect;
    }
}
