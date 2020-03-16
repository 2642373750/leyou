package com.leyou.search.client;

import com.leyou.item.api.GoodsApo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

@FeignClient("item-service")
public interface GoodsClient extends GoodsApo {

}
