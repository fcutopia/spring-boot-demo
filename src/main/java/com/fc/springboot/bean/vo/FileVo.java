package com.fc.springboot.bean.vo;

import lombok.Data;

/**
 * @ClassName FileVo
 * @Description
 * @Author fc
 * @Date 2022/2/24 5:28 下午
 * @Version 1.0
 **/

public class FileVo {


    /**
     * 需要解密的文件
     */
    public String filePath;


    /**
     * 保存解密结果文件地址
     */
    public String destPath;



    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getDestPath() {
        return destPath;
    }

    public void setDestPath(String destPath) {
        this.destPath = destPath;
    }
}
