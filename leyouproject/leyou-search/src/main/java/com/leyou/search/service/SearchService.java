package com.leyou.search.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.*;
import com.leyou.search.client.BrandClient;
import com.leyou.search.client.CategoryClient;
import com.leyou.search.client.GoodsClient;
import com.leyou.search.client.SpecificationClient;
import com.leyou.search.pojo.Goods;
import com.leyou.search.pojo.SearchRequest;
import com.leyou.search.pojo.SearchResult;
import com.leyou.search.repository.GoodsRepository;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.mockito.internal.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SearchService {

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private BrandClient brandClient;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private SpecificationClient specificationClient;

    @Autowired
    private GoodsRepository goodsRepository;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public Goods buildGoods(Spu spu) throws IOException {
        Goods goods = new Goods();

        //根据分类id查询名称
        List<String> names = this.categoryClient.queryNamesByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
        //根据品牌id查询品牌
        Brand brand = this.brandClient.queryBrandById(spu.getBrandId());
        //根据spuID插叙所有的sku
        List<Sku> skus = this.goodsClient.querySkusById(spu.getId());
        //价格集合
        List<Long> prices = new ArrayList<>();
        //收集sku必要的字段信息
        List<Map<String,Object>> skuMaplist = new ArrayList<>();
        skus.forEach(sku->{
            prices.add(sku.getPrice());

            Map<String,Object> map = new HashMap<>();
            map.put("id",sku.getId());
            map.put("title",sku.getTitle());
            map.put("price",sku.getPrice());
            map.put("image",sku.getImages());

            skuMaplist.add(map);
        });
        //根据spu中的查询规格参数
        List<SpecParam> Params = this.specificationClient.queryParamsById(null, spu.getCid3(), null, true);
        //根据spuiD查询spuDetail
        SpuDetail spuDetail = this.goodsClient.querySpuDetailBySpuId(spu.getId());
        //把json反序列化成Map
        Map<String,Object> genericSpecMap = MAPPER.readValue(spuDetail.getGenericSpec(), new TypeReference<Map<String, Object>>() {
        });
        Map<String,List<Object>> specMap = MAPPER.readValue(spuDetail.getSpecialSpec(), new TypeReference<Map<String, List<Object>>>() {
        });

        Map<String,Object> spec = new HashMap<>();

        Params.forEach(param->{
            //判断规格参数的类型
            if(param.getGeneric()){
                String value = genericSpecMap.get(param.getId().toString()).toString();
                if(param.getNumeric()){
                    value = chooseSegment(value,param);
                }
                spec.put(param.getName(),value);
            }else{
                List<Object> value = specMap.get(param.getId().toString());
                spec.put(param.getName(),value);
            }
        });

        goods.setId(spu.getId());
        goods.setCid1(spu.getCid1());
        goods.setCid2(spu.getCid2());
        goods.setCid3(spu.getCid3());
        goods.setBrandId(spu.getBrandId());
        goods.setCreateTime(spu.getCreateTime());
        goods.setSubTitle(spu.getSubTitle());
        //查询的字段拼接
        goods.setAll(spu.getTitle()+" "+ StringUtil.join(names," ")+" "+brand.getName());
        goods.setPrice(prices);
        goods.setSkus(MAPPER.writeValueAsString(skuMaplist));
        goods.setSpecs(spec);
        return goods;
    }

    private String chooseSegment(String value, SpecParam p) {
        double val = NumberUtils.toDouble(value);
        String result = "其它";
        // 保存数值段
        for (String segment : p.getSegments().split(",")) {
            String[] segs = segment.split("-");
            // 获取数值范围
            double begin = NumberUtils.toDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if(segs.length == 2){
                end = NumberUtils.toDouble(segs[1]);
            }
            // 判断是否在范围内
            if(val >= begin && val < end){
                if(segs.length == 1){
                    result = segs[0] + p.getUnit() + "以上";
                }else if(begin == 0){
                    result = segs[1] + p.getUnit() + "以下";
                }else{
                    result = segment + p.getUnit();
                }
                break;
            }
        }
        return result;
    }

    public SearchResult search(SearchRequest searchRequest) {

        if(StringUtils.isBlank(searchRequest.getKey())){
            return null;
        }
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        //添加条件
       // QueryBuilder all = QueryBuilders.matchQuery("all", searchRequest.getKey()).operator(Operator.AND);
        BoolQueryBuilder all = buildBool(searchRequest);
        queryBuilder.withQuery(all);
        //添加分页
        queryBuilder.withPageable(PageRequest.of(searchRequest.getPage()-1,searchRequest.getSize()));
        //添加结果集过滤
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{"id","skus","subTitle"},null));

        String ca="categorys";
        String br="brands";
        //添加分类和品牌的聚合
        queryBuilder.addAggregation(AggregationBuilders.terms(ca).field("cid3"));
        queryBuilder.addAggregation(AggregationBuilders.terms(br).field("brandId"));

        //执行查询
        AggregatedPage<Goods> search = (AggregatedPage<Goods>) this.goodsRepository.search(queryBuilder.build());

        //获得结果集
        List<Map<String,Object>> cas=getCategoryAggResult(search.getAggregation(ca));

        List<Brand> brs = getBrandAggResult(search.getAggregation(br));
        List<Map<String,Object>> specs=null;
        if(!CollectionUtils.isEmpty(cas)&&cas.size()==1){
            //对规格参数进行聚合
            specs = getParamAggResult((Long)cas.get(0).get("id"),all);
        }

        return new SearchResult(search.getTotalElements(),search.getTotalPages(),search.getContent(),cas,brs,specs);
    }

    private BoolQueryBuilder buildBool(SearchRequest searchRequest) {

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        boolQueryBuilder.must(QueryBuilders.matchQuery("all",searchRequest.getKey()).operator(Operator.AND));

        Map<String, Object> filter = searchRequest.getFilter();
        for (Map.Entry<String, Object> entry : filter.entrySet()) {
            String key = entry.getKey();
            if(StringUtils.equals("品牌",key)) {
                key = "brandId";
            }else if(StringUtils.equals("分类",key)){
                key = "cid3";
            }else{
                key ="specs."+ key + ".keyword";
            }
            boolQueryBuilder.filter(QueryBuilders.termQuery(key,entry.getValue()));
        }

        return boolQueryBuilder;
    }

    private List<Map<String,Object>> getParamAggResult(Long id, QueryBuilder all) {

        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();

        queryBuilder.withQuery(all);

        List<SpecParam> specParams = this.specificationClient.queryParamsById(null, id, null, true);

        specParams.forEach(specParam -> {
            queryBuilder.addAggregation(AggregationBuilders.terms(specParam.getName()).field("specs."+ specParam.getName() +".keyword"));
        });

        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{},null));

        AggregatedPage<Goods> goods = (AggregatedPage<Goods>) this.goodsRepository.search(queryBuilder.build());

        List<Map<String,Object>> list = new ArrayList<>();
        Map<String, Aggregation> stringAggregationMap = goods.getAggregations().asMap();

        for (Map.Entry<String ,Aggregation> entry:stringAggregationMap.entrySet()){

            Map<String,Object> map = new HashMap<>();

            map.put("k",entry.getKey());

            List<String> options = new ArrayList<>();
            StringTerms value = (StringTerms) entry.getValue();
            value.getBuckets().forEach(val->{
                options.add(val.getKeyAsString());
            });

            map.put("options",options);
            list.add(map);
        }

        return list;
    }

    private List<Brand> getBrandAggResult(Aggregation aggregation) {

        LongTerms terms= (LongTerms) aggregation;

        List<Brand> brands = new ArrayList<>();

        terms.getBuckets().forEach(bucket->{
            Brand brand = this.brandClient.queryBrandById(bucket.getKeyAsNumber().longValue());
            brands.add(brand);
        });

        return brands;
    }

    private List<Map<String,Object>> getCategoryAggResult(Aggregation aggregation) {
        LongTerms terms= (LongTerms) aggregation;

        return terms.getBuckets().stream().map(bucket -> {
            Map<String,Object> map = new HashMap<>();
            Long id =bucket.getKeyAsNumber().longValue();
            List<String> list = this.categoryClient.queryNamesByIds(Arrays.asList(id));
           map.put("id",id);
           map.put("name",list.get(0));
           return map;
        }).collect(Collectors.toList());
    }

    public void save(Long id) throws IOException {
        Spu spu = this.goodsClient.querySpuById(id);
        Goods goods = this.buildGoods(spu);
        this.goodsRepository.save(goods);
    }
}
