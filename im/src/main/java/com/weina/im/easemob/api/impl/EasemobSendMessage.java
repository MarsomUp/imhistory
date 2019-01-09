package com.weina.im.easemob.api.impl;


import com.weina.im.easemob.api.SendMessageAPI;
import com.weina.im.easemob.comm.EasemobAPI;
import com.weina.im.easemob.comm.OrgInfo;
import com.weina.im.easemob.comm.ResponseHandler;
import com.weina.im.easemob.comm.TokenUtil;
import io.swagger.client.ApiException;
import io.swagger.client.api.MessagesApi;
import io.swagger.client.model.Msg;

public class EasemobSendMessage implements SendMessageAPI {
    private ResponseHandler responseHandler = new ResponseHandler();
    private MessagesApi api = new MessagesApi();
    public Object sendMessage(final Object payload) {
        return responseHandler.handle(new EasemobAPI() {
            public Object invokeEasemobAPI() throws ApiException {
                return api.orgNameAppNameMessagesPost(OrgInfo.ORG_NAME,OrgInfo.APP_NAME,TokenUtil.getAccessToken(), (Msg) payload);
            }
        });
    }
}
