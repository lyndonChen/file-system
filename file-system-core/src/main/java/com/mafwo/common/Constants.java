package com.mafwo.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Constants {

    //mongo允许的文件类型
    public static final List<String> allowedFileTypes                  = new ArrayList<String>(
                                                                                    10);
    //mongo允许的内容类型
    public static final Map<String, String[]> ALLOWED_CONTENTTYPE               = new HashMap<String, String[]>();

    public static final String[]              IMAGE_CONTENTTYPE                 = new String[] {
            "image/jpeg", "image/pjpeg", "image/png", "image/x-png", "image/gif"            };

    public static final String[]              ICON_CONTENTTYPE                  = new String[] { "image/x-icon" };

    public static final String[]              FLASH_CONTENTTYPE                 = new String[] { "application/x-shockwave-flash" };

    //mongo允许上传的文件大小（字节）
    public static final Integer ALLOWED_SIZE                      = 2048000;

    static {
        //'image', 'flash', 'icon'
        allowedFileTypes.add("image");
        allowedFileTypes.add("flash");
        allowedFileTypes.add("icon");

        ALLOWED_CONTENTTYPE.put("image", IMAGE_CONTENTTYPE);
        ALLOWED_CONTENTTYPE.put("icon", ICON_CONTENTTYPE);
        ALLOWED_CONTENTTYPE.put("flash", FLASH_CONTENTTYPE);
    }

    //mongo上传文件类型：产品图片
    public static final String FILE_TYPE_PDT                     = "pdt";
    //mongo上传文件类型：广告图片
    public static final String FILE_TYPE_AD                      = "ad";
    //mongo上传文件类型：其他图片
    public static final String FILE_TYPE_OTHER                   = "other";
    //product方式获取图片
    public static final String FILE_TYPE_PRODUCT                 = "product";
    //产品图片宽高限制
    public static final String FILE_TYPE_PDT_WIDTH_HEIGHT        = "mongo.pdt.width&height";
    //广告图片宽高限制
    public static final String FILE_TYPE_AD_WIDTH_HEIGHT         = "mongo.ad.width&height";
    //其他图片宽高限制
    public static final String FILE_TYPE_OTHER_WIDTH_HEIGHT      = "mongo.other.width&height";
    //product方式获取图片宽高限制
    public static final String FILE_TYPE_PRODUCT_WIDTH_HEIGHT    = "mongo.product.width&height";

    //默认城市id
    public final static int                   DEFAULT_CITY_ID                   = 173;
}
