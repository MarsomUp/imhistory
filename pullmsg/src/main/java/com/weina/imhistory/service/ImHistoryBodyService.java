package com.weina.imhistory.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.weina.imhistory.entity.ImHistoryBody;
import com.weina.imhistory.mapper.ImHistoryBodyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class ImHistoryBodyService extends BaseService<ImHistoryBody> {
    @Autowired
    private ImHistoryBodyMapper mapper;
    @Override
    public BaseMapper<ImHistoryBody> getMapper() {
        return this.mapper;
    }

    /**
     * 批量加入消息
     * @param bodies
     */
    public void addBatch(List<ImHistoryBody> bodies) {
        mapper.addPatch(bodies);
    }

    /**
     * 获取消息类型为图片、语音、视频、文件的消息
     * @param historyId
     * @return
     */
    public List<ImHistoryBody> listByHistoryId(Long historyId) {
        QueryWrapper<ImHistoryBody> q = new QueryWrapper<>();
        q.eq("history_id", historyId);
        q.ne("msg_type", "txt");
        q.ne("msg_type", "loc");
        List<ImHistoryBody> list = this.mapper.selectList(q);
        return list;
    }

    /**
     * 批量更新数据
     * @param list
     */
    public void updatePatch(List<ImHistoryBody> list) {
        mapper.updatePatch(list);
    }

    public List<ImHistoryBody> testGet() {
        QueryWrapper<ImHistoryBody> q = new QueryWrapper<>();
        q.in("id", Arrays.asList(new String[]{"1082928227014045698", "1082928227144069121"}));
        return this.mapper.selectList(q);
    }

}
