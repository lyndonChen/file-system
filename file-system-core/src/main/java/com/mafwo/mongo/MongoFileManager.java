package com.mafwo.mongo;

import com.mafwo.FileManager;
import com.mafwo.common.Constants;
import com.mafwo.exception.BusinessException;
import com.mafwo.mongo.entity.MongoFile;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.FileCopyUtils;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * mongoDB操作工具类<br/>
 * 提供的方法：1.保存文件到Mongodb中;2.读取文件信息；3.读取图片信息;4.读取缩略图信息
 * @Filename: MongoFileManager.java
 * @Version: 1.0
 * @Author: chend
 * @Email: lxmh2012@163.com
 */
public class MongoFileManager implements FileManager<MongoFile> {
    private static org.apache.log4j.Logger log = org.apache.log4j.LogManager
                                                   .getLogger(MongoFileManager.class);

    @Resource
    private GridFSUtils                    fsUtils;
    @Resource
    private GridFSUtils                    thumbUtils;
    /**
     * 保存原图
     * @param mongoFile fileName、contentType、size必填
     * @return fs.files._id
     */
    public String saveFile(MongoFile mongoFile) {
        log.info("[file-system-core][MongoFileManager] save start");
        //校验 文件大小，文件类型是否支持
        valdatate(mongoFile);
        try {
            GridFSInputFile gridFSInputFile = fsUtils.save(mongoFile);
            log.info("[file-system-core][MongoFileManager] save end & return:" + gridFSInputFile.getId().toString());
            return gridFSInputFile.getId().toString();
        } catch (Exception e) {
            log.info("[file-system-core][MongoFileManager] save exception end");
            log.error("[file-system-core][MongoFileManager] save exception:", e);
            throw new BusinessException(e.getMessage());
        }

    }

    @Override
    public MongoFile getFile(String uri) {
        log.info("[file-system-core][MongoFileManager] getFile start &uri:" + uri);
        if (StringUtils.isEmpty(uri)) {
            log.info("[file-system-core][MongoFileManager] getFile uri is Empty end");
            return null;
        }
        uri = uri.indexOf(".") != -1 ? uri.substring(0, uri.lastIndexOf("."))
                : uri.substring(0);
        //从mongo中读取文件信息
        GridFSDBFile girdFSDBFile = fsUtils.getFileById(uri);
        if(null == girdFSDBFile){
            log.info("[file-system-core][MongoFileManager] getFile not find file end");
            return null;
        }
        // 封装文件交换数据
        MongoFile mongoFile = new MongoFile();
        mongoFile.setFileName(girdFSDBFile.getFilename());
        mongoFile.setContentType(girdFSDBFile.getContentType());
        mongoFile.setIn(girdFSDBFile.getInputStream());
        mongoFile.setSize(girdFSDBFile.getChunkSize());
        log.info("[file-system-core][MongoFileManager] getFile end");
        return mongoFile;


    }

    @Override
    public MongoFile getImage(String uri) {
        log.info("[file-system-core][MongoFileManager] getImage start &uri:" + uri);
        if (StringUtils.isEmpty(uri)) {
            log.info("[file-system-core][MongoFileManager] getImage uri is Empty end");
            return null;
        }
        uri = uri.indexOf(".") != -1 ? uri.substring(0, uri.lastIndexOf("."))
                : uri.substring(0);
        //从mongo中读取文件信息
        GridFSDBFile girdFSDBFile = fsUtils.getFileById(uri);
        if(null == girdFSDBFile){
            return null;
        }
        //判断是否是图片
        if(!StringUtils.startsWith("image/",girdFSDBFile.getContentType())){
            log.info("[file-system-core][MongoFileManager] getImage file contentType is not image end");
            throw new BusinessException("非图片类型文件！");
        }
        // 封装文件交换数据
        MongoFile mongoFile = new MongoFile();
        mongoFile.setFileName(girdFSDBFile.getFilename());
        mongoFile.setContentType(girdFSDBFile.getContentType());
        mongoFile.setIn(girdFSDBFile.getInputStream());
        mongoFile.setSize(girdFSDBFile.getChunkSize());
        log.info("[file-system-core][MongoFileManager] getFile end");
        return mongoFile;
    }

    @Override
    public MongoFile getThumbImage(String uri) {
        log.info("[file-system-core][MongoFileManager] getProductImage start &uri:" + uri);
        if (StringUtils.isEmpty(uri)) {
            log.info("[file-system-core][MongoFileManager] getProductImage uri is Empty end");
            return null;
        }
        Integer width = null;
        Integer height = null;
        //判断是否是缩略图模式,否直接查询原图返回
        if(uri.indexOf("_") ==-1){
            return getImage(uri);
        }
        //TODO 此处后期可添加逻辑，判断是否是系统支持的图片大小，可防止随意传参，浪费系统资源
        /**
         * 查询缩略图，如果存在直接返回，如果不存在查询原图,同时生成缩略图存储，文件名称为缩略图uri
         */
        MongoFile thumbFile = queryThumbByFileName(uri);
        if (null != thumbFile)
            return thumbFile;

        //2.不存在，去fs.files中查找原图
        log.info("[file-system-core][MongoFileManager] getProductImage uri.indexOf('_') != -1 in");
        String urls[] = uri.split("_");
        if (null == urls || urls.length == 0) {
            log.info("urls == null end");
            throw new BusinessException("参数无效");
        }
        urls[2] = urls[2].indexOf(".") != -1 ? urls[2].substring(0, urls[2].lastIndexOf("."))
                : urls[2].substring(0);

        GridFSDBFile girdFSDBFile = fsUtils.getFileById(urls[0]);
        //2.1.找不到原图，返回空
        if (null == girdFSDBFile) {
            log.info("[file-system-core][MongoFileManager] getThumbImage girdFSDBFile is null end");
            return null;
        }
        //找到原图，切割原图，存储到thumbnail中
        try {
            //TODO 暂时屏蔽推按切割方法，图片缩放可以考虑java方式实现，或者调用Python远程服务实现
           /* byte[] imgByte = pythonImageUtil.resize(Integer.parseInt(url[1]),
                    Integer.parseInt(url[2]),
                    FileCopyUtils.copyToByteArray(girdFSDBFile.getInputStream()));*/
            byte[] imgByte = FileCopyUtils.copyToByteArray(girdFSDBFile.getInputStream());
            if (null != imgByte) {
                MongoFile file = new MongoFile();
                file.setIn(new ByteArrayInputStream(imgByte));
                file.setFileName(uri);
                file.setContentType(girdFSDBFile.getContentType());
                file.setSize(imgByte.length);
                thumbUtils.save(file);
                //返回切割后的数据
                log.info("[file-system-core][MongoFileManager] getThumbImage end");
                return file;
            }
        } catch (NumberFormatException e) {
            log.error("[file-system-core][MongoFileManager] getThumbImage NumberFormatException:", e);
            log.info("[file-system-core][MongoFileManager] getThumbImage NumberFormatException end");
            return null;
        } catch (IOException e) {
            log.error("[file-system-core][MongoFileManager] getThumbImage exception:", e);
            log.info("[file-system-core][MongoFileManager] getThumbImage IOException end");
            return null;
        }
        log.info("[file-system-core][MongoFileManager] getThumbImage return null end");
        return null;
    }

    private MongoFile queryThumbByFileName(String fileName) {
        if (!StringUtils.isEmpty(fileName)) {
            GridFSDBFile girdFSDBFile = thumbUtils.getFileByFileName(fileName);
            if (null != girdFSDBFile) {
                //2.存在，返回
                try {
                    MongoFile mongoFile = new MongoFile();
                    mongoFile.setSize(girdFSDBFile.getChunkSize());
                    mongoFile.setIn(girdFSDBFile.getInputStream());
                    mongoFile.setContentType(girdFSDBFile.getContentType());
                    return mongoFile;
                } catch (Exception e) {
                    log.error("获取mongo数据发生异常:" + e.getMessage());
                    log.error(e);
                    log.info("[file-system-core][MongoFileManager] queryThumbByFileName IOException end");
                    return null;
                }
            }
        }
        log.info("[file-system-core][MongoFileManager] queryThumbByFileName return null end");
        return null;
    }

    /**
     * 检验文件内容，文件名称等信息
     * @param file
     */
    private void valdatate(MongoFile file) {
        if (null == file.getFileName())
            throw new BusinessException("fileName is null");
        //验证文件内容是否为空
        if (null == file.getIn())
            throw new BusinessException("in is null");

        //验证文件大小
        if (file.getSize() > Constants.ALLOWED_SIZE)
            throw new BusinessException("文件大小超出允许值");
    }

}
