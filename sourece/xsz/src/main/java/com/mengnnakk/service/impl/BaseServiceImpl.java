package com.mengnnakk.service.impl;

import com.mengnnakk.mapper.BaseMapper;
import com.mengnnakk.service.BaseService;

public abstract class BaseServiceImpl<T> implements BaseService<T>{
    private final BaseMapper<T> baseMapper;

    public BaseServiceImpl(BaseMapper<T> baseMapper){
        this.baseMapper = baseMapper;
    }

    @Override
    public int deleteById(Integer id){
        return baseMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(T record){
        return baseMapper.insert(record);
    }

    @Override
    public int insertByFilter(T record){
        return baseMapper.insertSelective(record);
    }

    @Override
    public T selectById(Integer id){
        return baseMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByIdFiter(T record){
        return baseMapper.updateByPrimaryKeySelective(record);
    }
    @Override
    public int updateById(T record) {
        return baseMapper.updateByPrimaryKey(record);
    }

}