package com.leyou.item.controller;

import com.leyou.item.bo.SpuBo;
import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import com.leyou.item.service.SpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("spec")
public class SpecificationController {

    @Autowired
    private SpecificationService specificationService;

    /**
     * 根据cid查询参数组
     * @param cid
     * @return
     */
    @GetMapping("groups/{cid}")
    public ResponseEntity<List<SpecGroup>> queryGroupsById(@PathVariable("cid")Long cid){

        List<SpecGroup> list = this.specificationService.queryGroupsById(cid);
        if(CollectionUtils.isEmpty(list)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(list);
    }

    @GetMapping("params")
    public ResponseEntity<List<SpecParam>> queryParamsById(
            @RequestParam(value="gid",required = false)Long gid,
            @RequestParam(value="cid",required = false) Long cid,
            @RequestParam(value="generic",required = false) Boolean generic,
            @RequestParam(value="searching",required = false) Boolean searching
    ){

        List<SpecParam> list = this.specificationService.queryParamsById(gid,cid,generic,searching);
        if(CollectionUtils.isEmpty(list)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(list);
    }

    @GetMapping("group/parma/{spuId}")
    public ResponseEntity<List<SpecGroup>> queryGroupsWithParam(@PathVariable("spuId")Long id){

        List<SpecGroup> list = this.specificationService.queryGroupsWithParam(id);
        if(CollectionUtils.isEmpty(list)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(list);
    }


}
