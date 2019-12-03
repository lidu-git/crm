package com.mage.crm.util;

import org.apache.commons.codec.binary.Base64;
import sun.misc.BASE64Encoder;

public class Base64Util {
    /**
     * 加密
     * @param str
     * @return
     */
    public static String enCode(String str){
        String s = Base64.encodeBase64String(str.getBytes());
        return s;
    }

    /**
     * 解密
     * @param str
     * @return
     */
    public static String deCode(String str){
        byte[] bytes = Base64.decodeBase64(str);
        return  new String (bytes);
    }
}
