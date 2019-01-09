package com.weina.imhistory.common.immsg;

/**
 * @Description: 视频类型消息
 * @Author: mayc
 * @Date: 2019/01/08 10:02
 */
public class VideoBody extends Bodies {

    private String fileLength; // 单位  字节
    private String fileName;
    private String secret;  //这个字段不为空，才能下载该视频
    private String length; // 视频播放长度 (单位：秒）
    private String size; // 视频缩略图的长宽尺寸
    private String thumb; // 上传视频缩略图远程地址，在上传视频缩略图后会返回UUID
    private String thumbSecret; // 在上传视频缩略图后会返回字符串，只有含有secret才能够下载此视频缩略图
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

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getThumbSecret() {
        return thumbSecret;
    }

    public void setThumbSecret(String thumbSecret) {
        this.thumbSecret = thumbSecret;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
