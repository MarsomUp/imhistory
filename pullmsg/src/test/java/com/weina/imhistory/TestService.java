package com.weina.imhistory;

import com.weina.imhistory.entity.ImHistory;
import com.weina.imhistory.service.ImHistoryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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
    @Test
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
}
