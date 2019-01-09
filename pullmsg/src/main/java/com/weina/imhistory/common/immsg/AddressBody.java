package com.weina.imhistory.common.immsg;

/**
 * @Description:
 * @Author: mayc
 * @Date: 2019/01/08 09:58
 */
public class AddressBody extends Bodies {

    private String addr;
    private String lat; // 纬度
    private String lng; // 经度


    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }
}
