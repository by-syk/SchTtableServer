package com.by_syk.schttableserver.util.net;

import com.by_syk.schttableserver.util.StringUtil;

/**
 * 网络传输数据加密解密工具类
 * 
 * @author shijkui
 */
public class NetEncryptUtil {
    /**
     * 加密
     * 
     * @param text
     * @return 加密失败返回""
     */
    public static String encrypt(String text) {
        if (StringUtil.isEmpty(text)) {
            return "";
        }
        return DESBase64Util.encodeInfo(text);
    }
    
    /**
     * 解密
     * 
     * @param text
     * @return 解密失败返回""
     */
    public static String decrypt(String text) {
        if (StringUtil.isEmpty(text)) {
            return "";
        }
        return DESBase64Util.decodeInfo(text);
    }
}
