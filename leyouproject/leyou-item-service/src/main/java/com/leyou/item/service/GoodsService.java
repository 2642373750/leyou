package com.leyou.item.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.pojo.PageResult;
import com.leyou.item.bo.SpuBo;
import com.leyou.item.mapper.*;
import com.leyou.item.pojo.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GoodsService {

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private SpuMapper spuMapper;

    @Autowired
    private BrandMapper brandMapper;

    @Autowired
    private SpuDetailMapper spuDetailMapper;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private AmqpTemplate amqpTemplate;

    public PageResult<SpuBo> querySpuByPage(String key, Boolean saleable, Integer page, Integer rows) {
        //添加条件
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        if(key!=null) {
            criteria.andLike("title", "%" + key + "%");
        }

        //添加上下架的商品
        if(saleable!=null){
            criteria.andEqualTo("saleable",saleable);
        }

        //添加分页
        PageHelper.startPage(page,rows);

        //执行查询
        List<Spu> spus = this.spuMapper.selectByExample(example);

        PageInfo<Spu> spuPageInfo = new PageInfo<>(spus);


        //spu转化成spubo
        List<SpuBo> spuBos = spus.stream().map(spu -> {
            SpuBo spuBo = new SpuBo();

            BeanUtils.copyProperties(spu, spuBo);
            Brand brand = this.brandMapper.selectByPrimaryKey(spu.getBrandId());
            spuBo.setBname(brand.getName());

            List<String> names = categoryService.queryNamesByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
            spuBo.setCname(StringUtils.join(names, "-"));
            return spuBo;
        }).collect(Collectors.toList());


        return new PageResult<SpuBo>(spuPageInfo.getTotal(),spuBos);
    }


    @Transactional
    public void saveGoods(SpuBo spuBo) {

        //增spu
        spuBo.setId(null);
        spuBo.setSaleable(true);
        spuBo.setValid(true);
        spuBo.setCreateTime(new Date());
        spuBo.setLastUpdateTime(spuBo.getCreateTime());
        this.spuMapper.insertSelective(spuBo);

        //增spudetail
        SpuDetail spuDetail = spuBo.getSpuDetail();
        spuDetail.setSpuId(spuBo.getId());

        this.spuDetailMapper.insertSelective(spuDetail);

        spuBo.getSkus().forEach(sku->{
            //增skus
            sku.setId(null);
            sku.setSpuId(spuBo.getId());
            sku.setCreateTime(new Date());
            sku.setLastUpdateTime(sku.getCreateTime());
            this.skuMapper.insertSelective(sku);

            //增stock
            Stock stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());
            this.stockMapper.insertSelective(stock);
        });

        sendMeg("insert",spuBo.getId());

    }

    private void sendMeg(String type,Long id){
        try {
            this.amqpTemplate.convertAndSend("item."+type,id);
        } catch (AmqpException e) {
            e.printStackTrace();
        }
    }

    public SpuDetail querySpuDetailBySpuId(Long spuId) {

        SpuDetail spuDetail = this.spuDetailMapper.selectByPrimaryKey(spuId);
        return spuDetail;
    }

    public List<Sku> querySkusById(Long spuId) {

        Sku recort = new Sku();
        recort.setSpuId(spuId);
        List<Sku> skus = this.skuMapper.select(recort);

        skus.forEach(sku->{
            Stock stock = this.stockMapper.selectByPrimaryKey(sku.getId());
            sku.setStock(stock.getStock());
        });
        return skus;
    }

    @Transactional
    public void updateGoods(SpuBo spuBo) {
        //删除stock
        Sku record = new Sku();
        record.setSpuId(spuBo.getId());
        List<Sku> skus = this.skuMapper.select(record);
        skus.forEach(sku->{
            this.stockMapper.deleteByPrimaryKey(sku.getId());
        });

        //删除sku
        this.skuMapper.delete(record);


        spuBo.getSkus().forEach(sku->{
            sku.setSpuId(spuBo.getId());
            sku.setCreateTime(new Date());
            sku.setLastUpdateTime(sku.getCreateTime());
            this.skuMapper.insertSelective(sku);
            Stock stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());
            this.stockMapper.insertSelective(stock);
        });

        // 更新spu和detail
        spuBo.setCreateTime(null);
        spuBo.setLastUpdateTime(new Date());
        spuBo.setValid(null);
        spuBo.setSaleable(null);
        this.spuMapper.updateByPrimaryKeySelective(spuBo);
        this.spuDetailMapper.updateByPrimaryKeySelective(spuBo.getSpuDetail());

        sendMeg("update",spuBo.getId());
    }

    public Spu querySpuById(Long id) {

        Spu spu = this.spuMapper.selectByPrimaryKey(id);

        return spu;
    }

    public Sku querySkuById(Long skuId) {

        Sku sku = this.skuMapper.selectByPrimaryKey(skuId);
        return sku;
    }
}
