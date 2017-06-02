## file-system
主要结合mongo、ftp实现了文件存储、读取、图片类型文件处理等功能，提供使用mongo、FTP存储图片的封装，提供简单demo实现文件上传和图片查看功能
***
###项目结构介绍

　　file-system：根项目  
　　file-system-core：核心实现包，使用是应用此依赖即可，里边提供文件读取实现  
　　file-system-parent：父项目，主要对依赖包进行管理  
　　file-system-web：web项目，提供简单的文件操作工具使用demo  

***
###使用介绍

1. 使用时依赖file-system-core
2. 如果使用mongo进行文件管理，在本项目中通过 <import resource="../mongo-config.xml" />引入mongo配置，同时在项目中添加如下两个参数：
```java
mongo.host=127.0.0.1 #mongo服务IP地址
mongo.port=27107 #mongo服务端口
```
之后在项目中通过依赖注入方式注入：mongoFileManager服务
3. 如果使用FTP进行文件管理，在本项目中通过 <import resource="../ftp-config.xml" />引入ftp配置，同时在项目中添加如下两个参数：
```java
ftp.host=127.0.0.1 #ftp服务IP地址
ftp.port=27107 #ftp服务端口
ftp.username= #ftp服务认证用户
ftp.password= #ftp服务认证密匙
ftp.clientTimeout= #ftp客户端连接超时时间
```
之后在项目中通过依赖注入方式注入：ftpFileManager服务
***
