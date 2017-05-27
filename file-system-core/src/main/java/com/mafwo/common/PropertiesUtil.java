package com.mafwo.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {

    private final static Log LOG        = LogFactory.getLog(PropertiesUtil.class);

    private final static Properties PROPERTIES = new Properties();

    static {
        loadProperties();
    }

    /**
     * @Description: 加载属性文件
     */
    private static void loadProperties() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader == null) {
            classLoader = Thread.class.getClassLoader();
        }
        if (classLoader == null) {
            classLoader = ClassLoader.getSystemClassLoader();
        }
        InputStream is = classLoader.getResourceAsStream("/conf/dubbo.properties");
        if (null == is)
            is = classLoader.getResourceAsStream("/config/conf.properties");
        try {
            PROPERTIES.load(is);
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    /**
     * @Description: 根据属性名称获取属性值
     * @param name
     * @return
     */
    public static String getProperty(String name) {
        return PROPERTIES.getProperty(name);
    }
}
