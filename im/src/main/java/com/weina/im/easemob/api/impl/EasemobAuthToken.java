package com.weina.im.easemob.api.impl;


import com.weina.im.easemob.api.AuthTokenAPI;
import com.weina.im.easemob.comm.TokenUtil;

public class EasemobAuthToken implements AuthTokenAPI {

	public Object getAuthToken(){
		return TokenUtil.getAccessToken();
	}
}
