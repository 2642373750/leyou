package com.leyou.goods.service;

import com.leyou.goods.client.BrandClient;
import com.leyou.goods.client.CategoryClient;
import com.leyou.goods.client.GoodsClient;
import com.leyou.goods.client.SpecificationClient;
import com.leyou.item.api.CategoryApi;
import com.leyou.item.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GoodsService {

    @Autowired
    private BrandClient brandClient;

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private SpecificationClient specificationClient;

    public Map<String,Object> queryGoodsBySpuId(Long id) {

        Map<String,Object> map = new HashMap<>();

        Spu spu = this.goodsClient.querySpuById(id);

        List<Long> cid = Arrays.asList(spu.getCid1(),spu.getCid2(),spu.getCid3());
        List<String> categoryNames = this.categoryClient.queryNamesByIds(cid);
        List<Map<String,Object>> categorys = new ArrayList<>();
        for(int i=0; i<cid.size();i++){
            Map<String,Object> ca = new HashMap<>();
            ca.put("id",cid.get(i));
            ca.put("name",categoryNames.get(i));
            categorys.add(ca);
        }

        SpuDetail spuDetail = this.goodsClient.querySpuDetailBySpuId(spu.getId());

        Brand brand = this.brandClient.queryBrandById(spu.getBrandId());

        List<Sku> skus = this.goodsClient.querySkusById(spu.getId());

        List<SpecGroup> grouds = this.specificationClient.queryGroupsWithParam(spu.getCid3());

        List<SpecParam> params = this.specificationClient.queryParamsById(null, spu.getCid3(), false, null);
        Map<Long,String> parmaMap = new HashMap<>();
        params.forEach(specParam -> {
            parmaMap.put(specParam.getId(),specParam.getName());
        });

        map.put("spu",spu);  //spu
        map.put("categorys",categorys); //
        map.put("spuDetail",spuDetail);
        map.put("brand",brand);
        map.put("skus",skus);
        map.put("grouds",grouds);
        map.put("paramMap",parmaMap);

        return map;
    }
}
