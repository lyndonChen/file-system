package com.mafwo;

/**
 * 文件㢆接口，提供文件存储操作，图片读取操作，图片类型数据读取，图片类型数据缩略图片读取
 * 暂时提供两种类型文件存贮方式供选择：
 * 1、使用mongodb的gridsFile存储文件信息
 * 2、通过ftp方式存储图片
 * @Author chend
 * @Email lxmh2012@163.com
 */
public interface FileManager<T> {

    /**
     * 保存文件，返回文件唯一标识
     * @param t 文件存储对象
     * @return
     */
    public String saveFile(T t);

    /**
     * 通过文件唯一标识获取文件信息
     * uri格式：
     *      1、mongo方式存储图片uri格式为：591d40d0ec3dc178746d2262（|.文件类型）
     *      2、如果是ftp方式则传输为文件存储路径 /2017/05/18/SSSSSSS.文件类型
     * @param uri
     * @return 文件存储对象，选择类型提供
     */
    public T getFile(String uri);

    /**
     *  通过唯一标识返回图片信息，未找到返回空，返回非图片类型数据抛出异常
     *  uri格式：
     *      1、mongo方式uri格式为：591d40d0ec3dc178746d2262（|.文件类型）
     *      2、ftp方式uri格式为：/2017/05/18/SSSSSSS.文件类型
     * @return
     */
    public T getImage(String uri);

    /**
     *  通过唯一标识和缩放参数返回图片信息，未找到返回空，返回非图片类型数据抛出异常
     *  暂时进支持指定宽高缩放方式
     *  uri格式：
     *      1、mongo方式uri格式为：591d40d0ec3dc178746d2262_100_100(.文件类型)
     *      2、ftp方式uri格式为：/2017/05/18/SSSSSSS_100_100.文件类型
     * @return
     */
    public T getThumbImage(String uri);

}
