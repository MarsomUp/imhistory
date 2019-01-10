package com.weina.imhistory.common;

import com.alibaba.fastjson.JSON;
import com.sohu.idcenter.IdWorker;
import com.weina.imhistory.common.immsg.*;
import com.weina.imhistory.entity.ImHistory;
import com.weina.imhistory.entity.ImHistoryBody;
import com.weina.imhistory.service.ImHistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 历史消息获取任务类
 * @Author: mayc
 * @Date: 2019/01/08 14:45
 */
@Component
public class IMMsgHistorySchedule {
    private static final Logger LOGGER = LoggerFactory.getLogger(IMMsgHistorySchedule.class);

    public static final long ONE_HOUR = 60 * 60 * 1000;
    public static final long one_minute = 60 * 1000;
    public static final long half_hour = 30 * 60 * 1000;

    @Autowired
    private ImHistoryService imHistoryService;
    @Autowired
    private IdWorker idWorker;

    @Scheduled(fixedDelay = ONE_HOUR)
    public void msgJob() {
        long startTime = System.nanoTime();
        LOGGER.info("任务调度开始");
        // 获取当前时间的往前3个小时
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHH");
        LocalDateTime time = LocalDateTime.now();
        LOGGER.info("当前时间："+time.format(formatter));
        LocalDateTime time2 = time.plusHours(-3);
        LOGGER.info("拉取"+time2.format(formatter)+"的消息");
        List<IMMessage> messages = IMMessageUtil.exportIMHistoryMessage(formatter.format(time2));
        if (messages.isEmpty()) {
            LOGGER.info("本次拉取的数据为空");
        } else {
            CommonTreadPools commonTreadPools = CommonTreadPools.getInstance();
            List<ImHistory> histories = new ArrayList<>(10);
            for (IMMessage message : messages) {
                ImHistory imHistory = new ImHistory();
                imHistory.setId(idWorker.getId());
                imHistory.setMsgId(message.getMsgId());
                imHistory.setSendTime(String.valueOf(message.getCreateTime()));
                imHistory.setMsgFrom(message.getFrom());
                imHistory.setMsgTo(message.getTo());
                imHistory.setChatType(message.getChatType());
                //imHistory.setMsgType(message.get);
                IMMessage.Payload payload = message.getPayload();
                List<ImHistoryBody> list = new ArrayList<>(10);
                if (payload != null) {
                    List<Bodies> bodies = payload.getBodies();
                    if (!bodies.isEmpty()) {
                        for (Bodies b : bodies) {
                            ImHistoryBody ib = new ImHistoryBody();
                            if (b instanceof TxtBody) {
                                ib.setMsg(((TxtBody)b).getMsg());
                            } else if (b instanceof ImgBody) {
                                ImgBody imgBody = (ImgBody)b;
                                ib.setFileLength(imgBody.getFileLength());
                                ib.setFileName(imgBody.getFileName());
                                ib.setSecret(imgBody.getSecret());
                                ib.setSize(imgBody.getSize());
                                ib.setUrl(imgBody.getUrl());
                            } else if (b instanceof AddressBody) {
                                AddressBody addressBody = (AddressBody) b;
                                ib.setAddr(addressBody.getAddr());
                                ib.setLat(addressBody.getLat());
                                ib.setLng(addressBody.getLng());
                            } else if (b instanceof AudioBody) {
                                AudioBody audioBody = (AudioBody) b;
                                ib.setFileLength(audioBody.getFileLength());
                                ib.setFileName(audioBody.getFileName());
                                ib.setSecret(audioBody.getSecret());
                                ib.setLength(audioBody.getLength());
                                ib.setUrl(audioBody.getUrl());
                            } else if (b instanceof VideoBody) {
                                VideoBody videoBody = (VideoBody) b;
                                ib.setFileLength(videoBody.getFileLength());
                                ib.setFileName(videoBody.getFileName());
                                ib.setSecret(videoBody.getSecret());
                                ib.setLength(videoBody.getLength());
                                ib.setSize(videoBody.getSize());
                                ib.setThumb(videoBody.getThumb());
                                ib.setThumbSecret(videoBody.getThumbSecret());
                                ib.setUrl(videoBody.getUrl());
                            } else {
                                FileBody fileBody = (FileBody) b;
                                ib.setFileName(fileBody.getFileName());
                                ib.setFileLength(fileBody.getFileLength());
                                ib.setSecret(fileBody.getSecret());
                                ib.setUrl(fileBody.getUrl());
                            }
                            ib.setMsgType(b.getType());
                            ib.setHistoryId(imHistory.getId());
                            list.add(ib);
                        }
                    }
                    IMMessage.Ext ext = payload.getExt();
                    String extStr = JSON.toJSONString(ext);
                    imHistory.setExt(extStr);
                }
                histories.add(imHistory);
                imHistoryService.addImHistroy(imHistory, list);

            }
            // 开始执行替换工作
            IMMsgContentReplaceThread thread = new IMMsgContentReplaceThread(histories);
            commonTreadPools.addThred(thread);
        }
        long endTime = System.nanoTime();
        LOGGER.info("本次拉取数据共消耗："+(endTime-startTime)/(1000 * 1000)+"ms");
    }
}
