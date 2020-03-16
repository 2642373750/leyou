package com.zs.test;

import com.leyou.LeyouSearchApplication;
import com.leyou.common.pojo.PageResult;
import com.leyou.item.bo.SpuBo;
import com.leyou.item.pojo.Spu;
import com.leyou.search.client.GoodsClient;
import com.leyou.search.pojo.Goods;
import com.leyou.search.repository.GoodsRepository;
import com.leyou.search.service.SearchService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;

import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest(classes = LeyouSearchApplication.class)
@RunWith(SpringRunner.class)
public class ElastTest {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private SearchService searchService;

    @Autowired
    private GoodsClient goodsClients;

    @Test
    public void test(){

        this.elasticsearchTemplate.createIndex(Goods.class);
        this.elasticsearchTemplate.putMapping(Goods.class);

        Integer page=1;
        Integer rows=100;
        do{
            PageResult<SpuBo> spuByPage = this.goodsClients.querySpuByPage(null, null, page, rows);
            List<SpuBo> items = spuByPage.getItems();
            List<Goods> collect = items.stream().map(spuBo -> {
                try {
                    return this.searchService.buildGoods(spuBo);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }).collect(Collectors.toList());

            this.goodsRepository.saveAll(collect);
            rows = items.size();
            page++;
        }while (rows==100);


    }
}
