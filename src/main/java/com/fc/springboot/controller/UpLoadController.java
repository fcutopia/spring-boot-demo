package com.fc.springboot.controller;

import com.alibaba.fastjson.JSON;
import com.fc.springboot.bean.vo.FileVo;
import com.fc.springboot.bean.vo.IntersectionRequestVo;
import com.fc.springboot.bean.vo.TaskRequestParam;
import com.fc.springboot.service.FileService;
import com.fc.springboot.utils.*;
import com.google.common.base.Charsets;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

@RestController
@RequestMapping("upload")
public class UpLoadController {

    /**
     * 文件在服务器端保存的主目录
     * /app/yunzhenxin/current/upload
     */
    private String basePath = "/usr/local/codes/";

    /**
     * 访问文件时的基础url
     */
    private String baseUrl;

    @Autowired
    private FtpUtil ftpFileUtil;

    @Autowired
    private FileDESUtil fileDESUtil;

    @Autowired
    private FileService fileService;


    @Autowired
    private DesUtils desUtils;

    @PostMapping("/file")
    public String fileUpload(@RequestParam("file") MultipartFile uploadFile) {
        try {
            //获取文件名字
            String fileName = uploadFile.getOriginalFilename();
            //生成文件在服务器端存储的子目录
            //把文件上传到服务器
            //转io流
            InputStream inputStream = uploadFile.getInputStream();
            boolean result = ftpFileUtil.uploadFile(fileName, basePath, inputStream);
            if (result) {
                System.out.println("文件地址：" + baseUrl + "/" + fileName);
                return baseUrl + "/" + fileName;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "文件上传失败";
    }

    /**
     * 上传求交集文件到本地指定目录
     *
     * @param file
     * @return
     */
    @RequestMapping(value = "/local", method = {RequestMethod.POST, RequestMethod.GET})
    public String fileUploadController(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return "上传失败，请选择文件";
        }
        String fileName = file.getOriginalFilename();
        String fileFullPath = "/Users/fc/Documents/work/encode/" + fileName;
        try {
            File dest = new File(fileFullPath);
            file.transferTo(dest);
            return "上传成功";
        } catch (Exception e) {
            System.out.println("上传文件 失败");
        }
        return "上传失败！";
    }

    /**
     * 申请授权（guest端请求）
     *
     * @param requestParam
     * @return
     */
    @RequestMapping(value = "/apply", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String apply(@RequestBody TaskRequestParam requestParam) {
        System.out.println(JSON.toJSONString(requestParam));
        return "success";
    }

    /**
     * 获取交集Id763位特征
     * phone_md5 手机号码求交集，在特征表里直接查找数据
     *
     * @return
     */
    @RequestMapping("/federation/host/queryFeatures")
    public Object queryFeatures(@RequestBody IntersectionRequestVo requestVo) {
        System.out.println(JSON.toJSONString(requestVo));
        return "success";
    }


    /**
     * 文件加密后上传
     *
     * @param file
     * @return
     */
    @RequestMapping(value = "/encrypt", method = {RequestMethod.POST, RequestMethod.GET})
    public String encrypt(@RequestParam("file") MultipartFile file) {
        String fileName = file.getOriginalFilename();
        String fileFullPath = "/Users/fc/Documents/work/encode/" + fileName;
        try {
            desUtils.encrypt(file.getInputStream(), fileFullPath);
            return "上传加密文件成功";
        } catch (Exception e) {
            System.out.println("上传加密文件失败");
        }
        return "上传加密文件失败！";
    }

    /**
     * 解密文件
     *
     * @param fileVo
     * @return
     */
    @RequestMapping(value = "/decrypt", method = {RequestMethod.POST, RequestMethod.GET})
    public String decrypt(@RequestBody FileVo fileVo) {
        try {
            String filePath = fileVo.getFilePath();
            File file = new File(filePath);
            String fileName = file.getName();
            String fullPath = file.getPath();

            System.out.println(fileName);
            System.out.println(fullPath);
            // System.out.println(fullPath.split(".")[0] + "_decrypt.csv");

            //String path = file.getPath() + File.pathSeparator + file.getName().split(".")[0] + "_decrypt" + "csv";
            String destPath = fileVo.getDestPath();
            desUtils.decryptV2(filePath, destPath);
            return "解密成功";
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("解密失败");
        }
        return "解密失败！";
    }

    /**
     * 文件加密后上传
     *
     * @return
     */
    @RequestMapping(value = "/encryptList", method = {RequestMethod.POST, RequestMethod.GET})
    public String encrypt() {

        String fileFullPath = "/Users/fc/Documents/work/encode/enc.csv";
        try {
            desUtils.encrypt(fileFullPath, new ArrayList<>());
            return "上传加密文件成功";
        } catch (Exception e) {
            System.out.println("上传加密文件失败");
        }
        return "上传加密文件失败！";
    }

    @RequestMapping(value = "/sendFile", method = {RequestMethod.POST, RequestMethod.GET})
    public String sendFile() {
        fileService.handEvent("/Users/fc/Documents/work/code/springboot/src/main/resources/bloomFilter/bloomFilter_test.txt");
        return "success";
    }

    @RequestMapping(value = "/receiveFile", method = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT})
    public String receiveFile(HttpServletRequest request) throws IOException {
        BloomFilter<CharSequence> bf = BloomFilter.readFrom(request.getInputStream(), Funnels.stringFunnel(Charsets.UTF_8));
        if (bf.mightContain("7cbd708bd6c68f47e2247d817ce37d")) {
            System.out.println("已匹配");
        } else {
            System.out.println("未匹配到");
        }
        return "success";
    }


}
