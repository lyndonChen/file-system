package com.mafwo.controller;

import com.mafwo.FileManager;
import com.mafwo.mongo.entity.MongoFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * 文件上传服务测试接口，
 * @Author chend
 * @Email lxmh2012@163.com
 */
@Controller
@RequestMapping("/fileUpload")
public class FileUploadDemoController {
    private static final Logger logger = LoggerFactory.getLogger(FileUploadDemoController.class);
    @Resource
    private FileManager<MongoFile> mongoFileFileManager;
    /**
     * 文件上传页面
     * @return
     */
    @RequestMapping("index")
    public String index(){
        return "index";
    }

    /**
     * 文件上传操作
     * @return
     */
    public String upload(MultipartFile file){
        if(file!=null){
            try {
                MongoFile mongoFile = new MongoFile();
                mongoFile.setSize(file.getSize());
                mongoFile.setContentType(file.getContentType());
                mongoFile.setIn(file.getInputStream());
                mongoFile.setFileName(file.getOriginalFilename());
                mongoFileFileManager.saveFile(mongoFile);
            } catch (IOException e) {
                logger.error("[FileUploadDemoController][upload]上传文件发生异常，异常信息："+e);
            }
        }
        return "redirect:/index";
    }
}
