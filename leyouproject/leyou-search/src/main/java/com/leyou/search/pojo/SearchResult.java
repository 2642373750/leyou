package com.leyou.search.pojo;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.Brand;

import java.util.List;
import java.util.Map;

public class SearchResult extends PageResult<Goods> {

    private List<Map<String,Object>> categorys;

    private List<Brand> brands;

    private List<Map<String,Object>> spec;

    public SearchResult() {
    }


    public SearchResult(Long total, Integer totalPage, List<Goods> items, List<Map<String, Object>> categorys, List<Brand> brands, List<Map<String, Object>> spec) {
        super(total, totalPage, items);
        this.categorys = categorys;
        this.brands = brands;
        this.spec = spec;
    }

    public List<Map<String, Object>> getCategorys() {
        return categorys;
    }

    public void setCategorys(List<Map<String, Object>> categorys) {
        this.categorys = categorys;
    }

    public List<Brand> getBrands() {
        return brands;
    }

    public void setBtands(List<Brand> brands) {
        this.brands = brands;
    }

    public List<Map<String, Object>> getSpec() {
        return spec;
    }

    public void setSpec(List<Map<String, Object>> spec) {
        this.spec = spec;
    }
}
