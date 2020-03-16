package com.leyou.cart.service;


import com.leyou.cart.client.GoodsClient;
import com.leyou.cart.listener.LoginInterceptor;
import com.leyou.cart.pojo.Cart;
import com.leyou.common.pojo.UserInfo;
import com.leyou.common.utils.JsonUtils;
import com.leyou.item.pojo.Sku;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static String REDIS_CART = "user:cart:";

    @Autowired
    private GoodsClient goodsClient;

    public void addCart(Cart cart) {

        UserInfo userInfo = LoginInterceptor.getUserInfo();

        BoundHashOperations<String, Object, Object> boundHashOps = this.redisTemplate.boundHashOps(REDIS_CART + userInfo.getId());
        Integer num=cart.getNum();
        String key = cart.getSkuId().toString();
        
        if(boundHashOps.hasKey(key)){
            String c = boundHashOps.get(key).toString();
            cart = JsonUtils.parse(c, Cart.class);
            cart.setNum(cart.getNum()+num);
        }else{
            Sku sku = this.goodsClient.querySkuById(cart.getSkuId());
            cart.setTitle(sku.getTitle());
            cart.setUserId(userInfo.getId());
            cart.setOwnSpec(sku.getOwnSpec());
            cart.setImage(StringUtils.isBlank(sku.getImages()) ? "":StringUtils.split(sku.getImages(),",")[0]);
           cart.setPrice(sku.getPrice());
        }
        boundHashOps.put(key,JsonUtils.serialize(cart));

    }

    public List<Cart> queryCarts() {

        UserInfo userInfo = LoginInterceptor.getUserInfo();

        if(!this.redisTemplate.hasKey(REDIS_CART + userInfo.getId())){
                return null;
        }

        BoundHashOperations<String, Object, Object> boundHashOps = this.redisTemplate.boundHashOps(REDIS_CART + userInfo.getId());

        List<Object> cartsJson = boundHashOps.values();

        if(CollectionUtils.isEmpty(cartsJson)){
            return null;
        }

        return cartsJson.stream().map(cartJson->JsonUtils.parse(cartJson.toString(),Cart.class)).collect(Collectors.toList());
    }

    public void updateNum(Cart cart) {
        UserInfo userInfo = LoginInterceptor.getUserInfo();

        if(!this.redisTemplate.hasKey(REDIS_CART + userInfo.getId())){
            return ;
        }

        BoundHashOperations<String, Object, Object> boundHashOps = this.redisTemplate.boundHashOps(REDIS_CART + userInfo.getId());

        String cartJson = boundHashOps.get(cart.getSkuId().toString()).toString();
        Integer num=cart.getNum();
        cart = JsonUtils.parse(cartJson, Cart.class);

        cart.setNum(num);

        boundHashOps.put(cart.getSkuId().toString(),JsonUtils.serialize(cart));
    }
}
