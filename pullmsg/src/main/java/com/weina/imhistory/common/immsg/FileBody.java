package com.weina.imhistory.common.immsg;

/**
 * @Description:
 * @Author: mayc
 * @Date: 2019/01/08 10:57
 */
public class FileBody extends Bodies {

    private String fileLength;
    private String fileName;
    private String secret;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
