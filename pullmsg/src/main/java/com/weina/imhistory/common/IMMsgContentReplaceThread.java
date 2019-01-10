package com.weina.imhistory.common;

import com.weina.imhistory.common.immsg.IMConstants;
import com.weina.imhistory.entity.ImHistory;
import com.weina.imhistory.entity.ImHistoryBody;
import com.weina.imhistory.service.ImHistoryBodyService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

/**
 * @Description: 这个线程用来替换从环信拉取的消息中的文件内容
 * @Author: mayc
 * @Date: 2019/01/08 17:58
 */
public class IMMsgContentReplaceThread implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(IMMsgContentReplaceThread.class);
    private OssTools ossTools = null;
    private List<ImHistory> imHistorys;

    public IMMsgContentReplaceThread() {}


    public IMMsgContentReplaceThread(List<ImHistory> imHistorys) {
        this.imHistorys = imHistorys;
    }

    @Override
    public void run() {
        if (imHistorys.isEmpty()) {
            LOGGER.info("本次没有可替换的数据");
            return;
        }
        Thread.currentThread().setName("IMMsgContentReplaceThread");
        // 在拉取任务中，消息可能还没有完全添加到数据库，所以在这里等待一会再执行
        try {
            Thread.sleep(30 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LOGGER.debug("开始替换");
        if (ossTools == null) {
            ossTools = new OssTools();
        }
        List<ImHistoryBody> rList = new ArrayList<>(10);
        ImHistoryBodyService imHistoryBodyService = (ImHistoryBodyService) SpringContextUtil.getBean("imHistoryBodyService");
        if (imHistoryBodyService != null) {
            for (ImHistory imHistory : imHistorys) {
                List<ImHistoryBody> list = imHistoryBodyService.listByHistoryId(imHistory.getId());
                if (!list.isEmpty()) {
                    for (ImHistoryBody body : list) {
                        ImHistoryBody ibody = downloadFileAndPushToAliOS(body);
                        rList.add(ibody);
                    }
                }
            }
        }
        if (!rList.isEmpty()) {
            // 更新数据
            imHistoryBodyService.updatePatch(rList);
        }
        if (ossTools != null) {
            ossTools.destory();
        }
        LOGGER.debug("替换结束");
    }

    private ImHistoryBody downloadFileAndPushToAliOS(ImHistoryBody body) {
        if (body == null) {
            LOGGER.info("body为空");
            return body;
        }
        String type = body.getMsgType();
        switch (type) {
            case IMConstants.IMG:
            case IMConstants.FILE:
            case IMConstants.AUDIO:
                String secret = body.getSecret();
                if (secret != null && secret != "") {
                    String url = body.getUrl();
                    InputStream in = null;
                    try {
                        in = downLoad(url);
                        String result = ossTools.uploadFile2OSS(in, body.getFileName());
                        if (result != "") {
                            body.setUrl(result);
                            return body;
                        }
                    } catch (IOException e) {
                        LOGGER.error("从环信服务器下载文件异常，对应的消息为："+body.toString(), e);
                    } finally {
                        if (in != null) {
                            try {
                                in.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                break;
            case IMConstants.VIDEO:
                String secret1 = body.getSecret();
                String thunbSecret = body.getThumbSecret();
                InputStream in = null;
                // 先上传视频文件
                if (secret1 != null && secret1 != "") {
                    // 这是视频文件的url地址
                    String url = body.getUrl();
                    try {
                        in = downLoad(url);
                        String result = ossTools.uploadFile2OSS(in, body.getFileName());
                        if (result != "") {
                            body.setUrl(result);
                        }
                    } catch (IOException e) {
                        LOGGER.error("下载视频文件异常，对应的消息为："+body.toString());
                    } finally {
                        if (in != null) {
                            try {
                                in.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                // 再上传视频文件的缩略图
                if (thunbSecret != null && thunbSecret != "") {
                    // 这是视频缩略图的url地址
                    String thumbUrl = body.getThumb();
                    try {
                        in = downLoad(thumbUrl);
                        String fileName = body.getFileName();
                        String fileNamePer = fileName.substring(0, fileName.indexOf("."));
                        String result = ossTools.uploadFile2OSS(in, fileNamePer+".jpg");
                        if (StringUtils.isNotBlank(result)) {
                            body.setThumb(result);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
        return body;
    }

    private InputStream downLoad(String fileUrl) throws IOException {
        // 先判断文件URL是否是以https开头，如果是，就得忽略ssl证书
        if (fileUrl.startsWith("https")) {
            try {
                SSLUtil.ignoreSsl();
            } catch (Exception e) {
                LOGGER.error("下载文件异常", e);
            }
        }
        URL url = new URL(fileUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Charset", "UTF-8");
        conn.setConnectTimeout(5*1000);
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        conn.connect();
        LOGGER.info("获取文件的大小为"+conn.getContentLength());
        DataInputStream dataInputStream = new DataInputStream(conn.getInputStream());
        return dataInputStream;
    }
}
