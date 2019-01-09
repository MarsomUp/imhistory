package com.weina.imhistory.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.weina.imhistory.entity.ImHistoryBody;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *  @Description: <b>历史消息内容数据层</b>
 *  @by mayc
 *  @date 2019-1-8 11:42:09
 */
@Component
public interface ImHistoryBodyMapper extends BaseMapper<ImHistoryBody> {

    /**
     * 批量添加数据
     * @param list
     */
    void addPatch(List<ImHistoryBody> list);

    /**
     * 批量更新数据
     * @param list
     */
    void updatePatch(List<ImHistoryBody> list);
}