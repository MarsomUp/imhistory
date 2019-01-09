package com.weina.imhistory.common.immsg;

/**
 * @Description: 语音类型消息
 * @Author: mayc
 * @Date: 2019/01/08 09:59
 */
public class AudioBody extends Bodies {

    private String fileLength; // 单位 字节
    private String fileName;
    private String secret; // 这个字段不为空，才能下载这个语音
    private String length; // 语音时间 单位 SECEND
    private String url;

    public String getFileLength() {
        return fileLength;
    }

    public void setFileLength(String fileLength) {
        this.fileLength = fileLength;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
