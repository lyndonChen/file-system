package com.mafwo.mongo;

import com.mafwo.exception.BusinessException;
import com.mafwo.mongo.entity.MongoFile;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;

import java.util.Iterator;
import java.util.Map;

 /**
 * put文件到mongodb实体
 * @Filename: MongoFile.java
 * @Version: 1.0
 * @Author: chend
 * @Email: lxmh2012@163.com
 *
 */
public class GridFSUtils {
    private static org.apache.log4j.Logger log = org.apache.log4j.LogManager
                                                   .getLogger(GridFSUtils.class);

    private final GridFS                   fs;

    public GridFSUtils(String dbname, String gridName, Mongo _mongo) {
        fs = new GridFS(_mongo.getDB(dbname), gridName);
    }

    /**
     * put 文件到mongo中
     * @return
     */
    @SuppressWarnings("rawtypes")
    protected GridFSInputFile save(MongoFile mongoFile) {
        if (null == fs) {
            log.error("gridFs is null");
            throw new ClassCastException("inin fs error");
        }
        if (null == mongoFile) {
            log.error("mongoFile is null");
            throw new BusinessException("mongoFile is null");
        }
        Map<String, Object> paramMap = mongoFile.getParamMap();
        GridFSInputFile gridFSInputFile = fs.createFile(mongoFile.getIn());
        gridFSInputFile.setFilename(mongoFile.getFileName());
        gridFSInputFile.setContentType(mongoFile.getContentType());
        if (null != paramMap && paramMap.size() > 0) {
            Iterator keyIter = paramMap.keySet().iterator();
            while (keyIter.hasNext()) {
                String key = (String) keyIter.next();
                gridFSInputFile.put(key, paramMap.get(key));
            }
        }
        gridFSInputFile.save();
        return gridFSInputFile;
    }

    /**
     * 根据id获取文件信息
     * @param id
     * @return
     * @throws Exception
     */
    protected GridFSDBFile getFileById(String id) {
        if (null == fs) {
            log.error("gridFs is null");
            return null;
        }
        DBObject query = new BasicDBObject();
        query.put("_id", new ObjectId(id));
        return fs.findOne(query);
    }
     /**
      * 目前仅有product方式获取图片用该方法
      * @param fileName
      * @return
      */
     protected GridFSDBFile getFileByFileName(String fileName) {
         if (null == fs) {
             log.error("gridFs is null");
             return null;
         }
         if (null == fileName || fileName.length() == 0) {
             log.error("fileName is null");
             return null;
         }
         return fs.findOne(fileName);
     }
    /**
     * 缩略图接口重复访问会重新插入数据，可能存在冗余数据，通过此方法判断是否存在数据
     * @param uri
     * @return
     */
    protected GridFSDBFile getFileByUri(String uri) {
        if (StringUtils.isEmpty(uri)) {
            log.error("uri is null");
            return null;
        }
        DBObject query = new BasicDBObject();
        query.put("uri", uri);
        return fs.findOne(query);
    }

}
