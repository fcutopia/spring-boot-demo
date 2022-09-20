package com.fc.springboot.bean.vo;

import java.util.List;

public class IntersectionRequestVo {

    /**
     * idType
     */
    public String idType;

    /**
     * idvalues
     */
    public List<String> idValues;

    /**
     * 文件名称
     */
    public String fileName;

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public List<String> getIdValues() {
        return idValues;
    }

    public void setIdValues(List<String> idValues) {
        this.idValues = idValues;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
