package com.mafwo.ftp;

import com.mafwo.FileManager;
import com.mafwo.ftp.entity.FTPFile;
import com.mafwo.ftp.pool.FTPClientPool;
import com.mafwo.mongo.MongoFileManager;
import org.apache.commons.net.ftp.FTPClient;

import javax.annotation.Resource;

/**
 * ftp操作工具类<br/>
 * 提供的方法：1.保存文件到ftp中;2.读取文件信息；3.读取图片信息;4.读取缩略图信息
 * @Filename: FTPFileManager.java
 * @Version: 1.0
 * @Author: chend
 * @Email: lxmh2012@163.com
 */
public class FTPFileManager implements FileManager<FTPFile> {
    private static org.apache.log4j.Logger log = org.apache.log4j.LogManager
            .getLogger(FTPFileManager.class);
    @Resource
    private FTPClientPool ftpClientPool;
    @Override
    public String saveFile(FTPFile ftpFile) {
        try {
            FTPClient ftpClient = ftpClientPool.borrowObject();
            ftpClient.changeWorkingDirectory("/");
            ftpClient.storeFile("",null);
        } catch (Exception e) {
            log.error("[file-system-core][FTPFileManager] saveFile exception:", e);
        }
        return null;
    }

    @Override
    public FTPFile getFile(String uri) {
        return null;
    }

    @Override
    public FTPFile getImage(String uri) {
        return null;
    }

    @Override
    public FTPFile getThumbImage(String uri) {
        return null;
    }
}
