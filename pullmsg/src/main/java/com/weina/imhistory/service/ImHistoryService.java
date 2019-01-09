package com.weina.imhistory.service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.weina.imhistory.entity.ImHistory;
import com.weina.imhistory.entity.ImHistoryBody;
import com.weina.imhistory.mapper.ImHistoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ImHistoryService extends BaseService<ImHistory> {
    @Autowired
    private ImHistoryMapper mapper;
    @Autowired
    private ImHistoryBodyService imHistoryBodyService;
    @Override
    public BaseMapper<ImHistory> getMapper() {
        return this.mapper;
    }

    public int addImHistroy(ImHistory imHistory, List<ImHistoryBody> bodies) {
        if (imHistory != null) {
            mapper.insert(imHistory);
        }
        if (!bodies.isEmpty()) {
            imHistoryBodyService.addBatch(bodies);
        }
        return 0;
    }
}
