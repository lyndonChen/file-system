package com.mafwo.mongo.entity;

import java.io.InputStream;
import java.util.Map;

/**
 * put文件到mongodb实体                      
 * @Filename: MongoFile.java
 * @Version: 1.0
 * @Author: chend
 * @Email: lxmh2012@163.com
 */
public class MongoFile {

    private String fileName;   //文件名
    private String contentType; //文件内容类型 "image/jpeg", "image/pjpeg", "image/png", "image/x-png" "image/x-icon""application/x-shockwave-flash"
    private long        size;       //大小
    private Integer fileUri;    //保存后返回的uri
    private InputStream in;         //文件流
    private Map<String, Object> paramMap;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public Integer getFileUri() {
        return fileUri;
    }

    private void setFileUri(Integer fileUri) {
        this.fileUri = fileUri;
    }

    public InputStream getIn() {
        return in;
    }

    public void setIn(InputStream in) {
        this.in = in;
    }

    public Map<String, Object> getParamMap() {
        return paramMap;
    }

    public void setParamMap(Map<String, Object> paramMap) {
        this.paramMap = paramMap;
    }
}
