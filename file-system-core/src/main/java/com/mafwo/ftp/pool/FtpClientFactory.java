package com.mafwo.ftp.pool;

import java.io.IOException;

import com.mafwo.exception.BusinessException;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.pool.PoolableObjectFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
* FTPClient工厂类，通过FTPClient工厂提供FTPClient实例的创建和销毁
* @author heaven
*/
public class FtpClientFactory implements PoolableObjectFactory {
    private static Logger logger = LoggerFactory.getLogger("file");
     private  FTPClientConfigure config;
     //给工厂传入一个参数对象，方便配置FTPClient的相关参数
     public FtpClientFactory(FTPClientConfigure config){
          this.config=config;
     }

     /* (non-Javadoc)
     * @see org.apache.commons.pool.PoolableObjectFactory#makeObject()
     */
     public FTPClient makeObject() throws Exception {
          FTPClient ftpClient = new FTPClient();
          ftpClient.setConnectTimeout(config.getClientTimeout());
          try {
               ftpClient.connect(config.getHost(), config.getPort());
               int reply = ftpClient.getReplyCode();
               if (!FTPReply.isPositiveCompletion(reply)) {
                    ftpClient.disconnect();
                    logger.warn("FTPServer refused connection");
                    return null;
               }
               boolean result = ftpClient.login(config.getUsername(), config.getPassword());
               if (!result) {
                    throw new BusinessException("ftpClient登陆失败! userName:" + config.getUsername() + " ; password:" + config.getPassword());
               }
               ftpClient.setFileType(config.getTransferFileType());
               ftpClient.setBufferSize(1024);
               ftpClient.setControlEncoding(config.getEncoding());
               if (config.getPassiveMode().equals("true")) {
                    ftpClient.enterLocalPassiveMode();
               }
          } catch (IOException e) {
               e.printStackTrace();
          } catch (BusinessException e) {
               e.printStackTrace();
          }
          return ftpClient;
     }

        /* (non-Javadoc)
        * @see org.apache.commons.pool.PoolableObjectFactory#destroyObject(java.lang.Object)
        */
     public void destroyObject(Object client) throws Exception {
         FTPClient ftpClient = (FTPClient) client;
          try {
               if (ftpClient != null && ftpClient.isConnected()) {
                    ftpClient.logout();
               }
          } catch (IOException io) {
               io.printStackTrace();
          } finally {
               // 注意,一定要在finally代码中断开连接，否则会导致占用ftp连接情况
               try {
                    ftpClient.disconnect();
               } catch (IOException io) {
                    io.printStackTrace();
               }
          }
     }

     /* (non-Javadoc)
     * @see org.apache.commons.pool.PoolableObjectFactory#validateObject(java.lang.Object)
     */
     public boolean validateObject(Object client) {
         FTPClient ftpClient = (FTPClient) client;
          try {
               return ftpClient.sendNoOp();
          } catch (IOException e) {
               throw new RuntimeException("Failed to validate client: " + e, e);
          }
     }

     public void activateObject(Object client) throws Exception {
     }

     public void passivateObject(Object client) throws Exception {

     }
}