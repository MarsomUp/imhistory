package com.weina.imhistory.service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sohu.idcenter.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public abstract class BaseService<T extends Serializable> {
    public abstract BaseMapper<T> getMapper();
    @Autowired
    private IdWorker idWorker;

}