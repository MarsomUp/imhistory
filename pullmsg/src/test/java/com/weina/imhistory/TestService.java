package com.weina.imhistory;

import com.weina.imhistory.entity.ImHistory;
import com.weina.imhistory.entity.ImHistoryBody;
import com.weina.imhistory.service.ImHistoryBodyService;
import com.weina.imhistory.service.ImHistoryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @Description:
 * @Author: mayc
 * @Date: 2019/01/09 15:09
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestService {

    @Autowired
    private ImHistoryService imHistoryService;
    @Autowired
    private ImHistoryBodyService imHistoryBodyService;
    //@Test
    public void test() {
        ImHistory im = new ImHistory();
        im.setId(1L);
        im.setChatType("chat");
        im.setMsgFrom("1234123");
        im.setMsgId("1234123123");
        int r = imHistoryService.getMapper().insert(im);
        System.out.println(r);
        assert (1 == r);
    }

    @Test
    public void updateBatch() {
        List<ImHistoryBody> list = imHistoryBodyService.testGet();
        for (ImHistoryBody b : list) {
            b.setUrl("https://www.baidu.com");
        }
        imHistoryBodyService.updatePatch(list);
    }
}
