package com.weina.imhistory.common.immsg;

/**
 * @Description: 图片消息
 * @Author: mayc
 * @Date: 2019/01/08 09:56
 */
public class ImgBody extends Bodies {

    private String fileLength;
    private String fileName;
    private String secret; // 这个字段不空，才能下载该图片
    private String size;
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

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
