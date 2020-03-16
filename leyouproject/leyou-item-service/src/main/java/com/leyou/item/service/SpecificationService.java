package com.leyou.item.service;

import com.leyou.item.mapper.SpecGroupMapper;
import com.leyou.item.mapper.SpecParamMapper;
import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class SpecificationService {

    @Autowired
    private SpecGroupMapper specGroupMapper;

    @Autowired
    private SpecParamMapper specParamMapper;

    public List<SpecGroup> queryGroupsById(Long cid) {

        SpecGroup record = new SpecGroup();
        record.setCid(cid);
        List<SpecGroup> specGroups = this.specGroupMapper.select(record);
        return specGroups;
    }

    /**
     * 根据条件查询
     * @param
     * @param gid
     * @param generic
     * @param searching
     * @return
     */
    public List<SpecParam> queryParamsById(Long gid,Long cid, Boolean generic, Boolean searching) {

        SpecParam record = new SpecParam();
        record.setGroupId(gid);
        record.setCid(cid);
        record.setGeneric(generic);
        record.setSearching(searching);
        List<SpecParam> specParams = this.specParamMapper.select(record);
        return specParams;
    }

    public List<SpecGroup> queryGroupsWithParam(Long id) {
        List<SpecGroup> list = this.queryGroupsById(id);

        list.forEach(specGroup -> {
          specGroup.setParams(this.queryParamsById(specGroup.getId(),null,null,null));
        });

        return list;
    }
}
