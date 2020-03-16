package com.leyou.goods.client;

import com.leyou.item.api.GoodsApo;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("item-service")
public interface GoodsClient extends GoodsApo {

}
