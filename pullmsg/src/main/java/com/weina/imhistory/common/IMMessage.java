package com.weina.imhistory.common;

import com.weina.imhistory.common.immsg.Bodies;

import java.util.List;

/**
 * @Description:
 * @Author: mayc
 * 从环信获取的数据格式如下：
 * {
 * 	"chat_type": "chat",
 * 	"payload": {
 * 		"ext": {
 * 			"weichat": {
 * 				"originType": "webim"
 * 			}
 * 		},
 * 		"bodies": [{
 * 			"msg": "测试消息\n",
 * 			"type": "txt"
 * 		}],
 * 		"from": "18000000001",
 * 		"to": "18000000002"
 * 	},
 * 	"from": "18000000001",
 * 	"to": "18000000002",
 * 	"msg_id": "537873274147702424",
 * 	"timestamp": 1542798180640,
 * 	"direction": "outgoing"
 * }
 * @Date: 2018/11/23 09:31
 */
public class IMMessage {
    private String chatType;
    private String from;
    private String to;
    private String msgId;
    private Long createTime;
    private String direction;
    private Payload payload;

    public class Payload {
        private Ext ext;
        private List<Bodies> bodies;
        private String from;
        private String to;

        public Ext getExt() {
            return ext;
        }

        public void setExt(Ext ext) {
            this.ext = ext;
        }

        public List<Bodies> getBodies() {
            return bodies;
        }

        public void setBodies(List<Bodies> bodies) {
            this.bodies = bodies;
        }

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }
    }

    public class Ext {
        private Weichat weichat;

        public Weichat getWeichat() {
            return weichat;
        }

        public void setWeichat(Weichat weichat) {
            this.weichat = weichat;
        }
    }

    public class Weichat {
        private String originType;

        public String getOriginType() {
            return originType;
        }

        public void setOriginType(String originType) {
            this.originType = originType;
        }
    }

    public String getChatType() {
        return chatType;
    }

    public void setChatType(String chatType) {
        this.chatType = chatType;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public Payload getPayload() {
        return payload;
    }

    public void setPayload(Payload payload) {
        this.payload = payload;
    }
}
