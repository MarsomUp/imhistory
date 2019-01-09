package com.weina.imhistory.common;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.weina.im.easemob.api.ChatMessageAPI;
import com.weina.im.easemob.api.impl.EasemobChatMessage;
import com.weina.imhistory.common.immsg.*;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.zip.GZIPInputStream;

/**
 * @Description: 环信消息历史记录
 * @Author: mayc
 * @Date: 2018/11/22 19:50
 */
public class IMMessageUtil {

    /**
     * 从环信拉取的每一条消息的格式如下：
     * {
     *   "action" : "get",
     *   "application" : "ce344310-e18d-11e8-a9e4-1904b567cc55",
     *   "uri" : "http://a1.easemob.com/1117181106211103/hx-987456123/chatmessages/2018112119",
     *   "data" : [ {
     *     "url" : "http://ebs-chatmessage-a1.easemob.com/history/3D/1117181106211103/hx-987456123/2018112119.gz?Expires=1542888717&OSSAccessKeyId=LTAIlKPZStPokdA8&Signature=YSnVeZMGD6ai0wZFh4Wu4nTv1sE%3D"
     *   } ],
     *   "timestamp" : 1542886917391,
     *   "duration" : 0,
     *   "organization" : "1117181106211103",
     *   "applicationName" : "hx-987456123"
     * }
     * @param {timestamp} 格式化的时间,需要：年月日小时 yyyyMMddHH
     */
    public static List<IMMessage> exportIMHistoryMessage(String timestamp) {
        ChatMessageAPI c = new EasemobChatMessage();
        String message = (String) c.exportChatMessages(timestamp);
        // 保存环信数据
        List<IMMessage> msgList = new ArrayList<>();
        if (StringUtils.isBlank(message)) {
            return msgList;
        }
        JSONObject jsonObject = JSONObject.parseObject(message);
        // 这是获取的聊天记录文件的下载地址信息
        JSONArray data = jsonObject.getJSONArray("data");
        if (data.size() == 0) {
            return msgList;
        }
        for (int i = 0;i < data.size(); i++) {
            JSONObject object = data.getJSONObject(i);
            String url = object.getString("url");
            try {
                String fileContent = downloadChatFile(url);
                System.out.println("原始的数据"+fileContent);
                // 由于返回的fileContent不是标准的json字符串，所以需要通过正则切割字符串，重新组装为标准的json
                if (StringUtils.isNotBlank(fileContent)) {
                    String reg = "\\}\\s*\\{\\\"chat_type";
                    String[] msgs = fileContent.split(reg);
                    if (msgs.length == 1) {
                        IMMessage imMessage = parseMessage(msgs[0]);
                        msgList.add(imMessage);
                    } else if (msgs.length > 0 && msgs.length != 1) {
                        for (int j = 0; j < msgs.length; j++) {
                            String msg = msgs[j];
                            String jsonMsg = "";
                            if (j % 2 == 0) {
                                if (j == 0) {
                                    jsonMsg = msg + "}";
                                } else {
                                    jsonMsg = "{\"chat_type" + msg + "}";
                                }
                            } else {
                                if (j == msgs.length -1) { // 最后一条
                                    jsonMsg = "{\"chat_type" + msg;
                                } else {
                                    jsonMsg = "{\"chat_type" + msg + "}";
                                }

                            }
                            IMMessage imMessage = parseMessage(jsonMsg);
                            msgList.add(imMessage);
                        }
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return msgList;
    }

    public static String downloadChatFile(String fileUrl) throws IOException {
        URL url = new URL(fileUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Charset", "UTF-8");
        conn.setConnectTimeout(5*1000);
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        conn.connect();
        System.out.println("获取的文件大小"+conn.getContentLength());

        InputStream in = new GZIPInputStream(conn.getInputStream());
        InputStreamReader isr = new InputStreamReader(in);
        BufferedReader buff = new BufferedReader(isr);
        StringBuilder sb = new StringBuilder();
        String s = "";
        while ((s = buff.readLine()) != null) {
            sb.append(s);
        }
        conn.disconnect();
        return sb.toString();
    }

    public static IMMessage parseMessage(String jsonMsg) {
        JSONObject IMMsgObject = JSONObject.parseObject(jsonMsg);
        IMMessage imMessage = null;
        if (IMMsgObject != null) {
            imMessage = new IMMessage();
            String chatType = IMMsgObject.getString("chat_type");
            String from = IMMsgObject.getString("from");
            String to = IMMsgObject.getString("to");
            String msgId = IMMsgObject.getString("msg_id");
            Long createTime = IMMsgObject.getLong("timestamp");
            String dircetion = IMMsgObject.getString("direction");
            imMessage.setChatType(chatType);
            imMessage.setFrom(from);
            imMessage.setTo(to);
            imMessage.setMsgId(msgId);
            imMessage.setCreateTime(createTime);
            imMessage.setDirection(dircetion);
            JSONObject payloadObject = IMMsgObject.getJSONObject("payload");
            IMMessage.Payload payload = null;
            if (payloadObject != null) {
                payload = new IMMessage().new Payload();

                // 获取 ext 对象
                JSONObject extObject = payloadObject.getJSONObject("ext");
                if (extObject != null) {
                    IMMessage.Ext ext = new IMMessage().new Ext();

                    // 获取weichat对象
                    JSONObject weichatObject = extObject.getJSONObject("weichat");
                    if (weichatObject != null) {
                        IMMessage.Weichat weichat = new IMMessage().new Weichat();
                        String originType = weichatObject.getString("originType");
                        weichat.setOriginType(originType);
                        ext.setWeichat(weichat);
                        payload.setExt(ext);
                    }
                }

                // 获取bodies对象
                JSONArray bodiesArray = payloadObject.getJSONArray("bodies");
                List<Bodies> bodiesList = new ArrayList<>();
                if (bodiesArray != null && bodiesArray.size() > 0) {
                    Bodies bodies = null;
                    for (int k = 0; k < bodiesArray.size(); k++) {
                        JSONObject bodiesObject = bodiesArray.getJSONObject(k);
                        String type = bodiesObject.getString("type");
                        String fileLength,fileName,secret,size,url,length;
                        switch (type) {
                            case IMConstants.TXT:
                                bodies = new TxtBody();
                                ((TxtBody) bodies).setType(type);
                                String bmsg = bodiesObject.getString("msg");
                                ((TxtBody) bodies).setMsg(bmsg);
                                break;
                            case IMConstants.IMG:
                                bodies = new ImgBody();
                                fileLength = bodiesObject.getString("file_length");
                                fileName = bodiesObject.getString("filename");
                                secret = bodiesObject.getString("secret");
                                JSONObject sizeObject = bodiesObject.getJSONObject("size");
                                size = "";
                                if (sizeObject != null) {
                                    size += sizeObject.getString("width");
                                    size += ",";
                                    size += sizeObject.getString("height");
                                }
                                url = bodiesObject.getString("url");
                                ((ImgBody) bodies).setType(type);
                                ((ImgBody) bodies).setFileLength(fileLength);
                                ((ImgBody) bodies).setFileName(fileName);
                                ((ImgBody) bodies).setSecret(secret);
                                ((ImgBody) bodies).setSize(size);
                                ((ImgBody) bodies).setUrl(url);
                                break;
                            case IMConstants.LOCATION:
                                bodies = new AddressBody();
                                String addr = bodiesObject.getString("addr");
                                String lat = bodiesObject.getString("lat");
                                String lng = bodiesObject.getString("lng");
                                ((AddressBody) bodies).setType(type);
                                ((AddressBody) bodies).setAddr(addr);
                                ((AddressBody) bodies).setLat(lat);
                                ((AddressBody) bodies).setLng(lng);
                                break;
                            case IMConstants.AUDIO:
                                bodies = new AudioBody();
                                fileLength = bodiesObject.getString("file_length");
                                fileName = bodiesObject.getString("filename");
                                secret = bodiesObject.getString("secret");
                                length = bodiesObject.getString("length");
                                url = bodiesObject.getString("url");
                                ((AudioBody) bodies).setType(type);
                                ((AudioBody) bodies).setFileLength(fileLength);
                                ((AudioBody) bodies).setFileName(fileName);
                                ((AudioBody) bodies).setSecret(secret);
                                ((AudioBody) bodies).setLength(length);
                                ((AudioBody) bodies).setUrl(url);
                                break;
                            case IMConstants.VIDEO:
                                bodies = new VideoBody();
                                fileLength = bodiesObject.getString("file_length");
                                fileName = bodiesObject.getString("filename");
                                secret = bodiesObject.getString("secret");
                                length = bodiesObject.getString("length");
                                JSONObject size2Object = bodiesObject.getJSONObject("size");
                                size = "";
                                if (size2Object != null) {
                                    size += size2Object.getString("width");
                                    size += ",";
                                    size += size2Object.getString("height");
                                }
                                String thumb = bodiesObject.getString("thumb");
                                String thumbSecret = bodiesObject.getString("thumb_secret");
                                url = bodiesObject.getString("url");
                                ((VideoBody) bodies).setFileLength(fileLength);
                                ((VideoBody) bodies).setFileName(fileName);
                                ((VideoBody) bodies).setSecret(secret);
                                ((VideoBody) bodies).setLength(length);
                                ((VideoBody) bodies).setSize(size);
                                ((VideoBody) bodies).setThumb(thumb);
                                ((VideoBody) bodies).setThumbSecret(thumbSecret);
                                ((VideoBody) bodies).setUrl(url);
                                ((VideoBody) bodies).setType(type);
                                break;
                            case IMConstants.FILE:
                                bodies = new FileBody();
                                fileLength = bodiesObject.getString("file_length");
                                fileName = bodiesObject.getString("filename");
                                secret = bodiesObject.getString("secret");
                                url = bodiesObject.getString("url");
                                ((FileBody) bodies).setFileLength(fileLength);
                                ((FileBody) bodies).setFileName(fileName);
                                ((FileBody) bodies).setSecret(secret);
                                ((FileBody) bodies).setUrl(url);
                                ((FileBody) bodies).setType(type);
                                break;
                        }
                        bodiesList.add(bodies);
                    }
                }
                payload.setBodies(bodiesList);

                // 获取from 和 to
                String pfrom = payloadObject.getString("from");
                String pto = payloadObject.getString("to");
                payload.setFrom(pfrom);
                payload.setTo(pto);
            }
            imMessage.setPayload(payload);
        }
        return imMessage;
    }

    public static void main(String[] args) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, -18);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHH");
        List<IMMessage> messages = exportIMHistoryMessage(format.format(calendar.getTime()));
        System.out.println("最终获取的数据数量"+messages.size());
    }
}
