package com.by_syk.schttableserver.util.net;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;

/**
 * 客户端与服务器通讯时对消息体加密和解密的工具类
 * 
 * @author shijkui
 */
public class DESBase64Util {
    /**
     * 密码长度必须是8的倍数
     */
    public final static String KEY = "RS35836780805258";
 
    /**
     * 加密方式
     */
    public final static String DES = "DES";
 
    /**
     * 默认编码
     */
    public final static String ENCODING = "UTF-8";
 
    /**
     * DES加密
     * 
     * @param src
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] encrypt(byte[] src, byte[] key) throws Exception {
        // DES算法要求有一个可信任的随机数源
        SecureRandom sr = new SecureRandom();
        // 从原始密匙数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);
        // 创建一个密匙工厂，然后用它把DESKeySpec转换成一个SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey securekey = keyFactory.generateSecret(dks);
        // Cipher对象实际完成加密操作
        Cipher cipher = Cipher.getInstance(DES);
        // 用密匙初始化Cipher对象
        cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);
        // 现在，获取数据并加密
        // 正式执行加密操作
        return cipher.doFinal(src);
    }
 
    /**
     * DES解密
     * 
     * @param src
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decrypt(byte[] src, byte[] key) throws Exception {
        // DES算法要求有一个可信任的随机数源
        SecureRandom sr = new SecureRandom();
        // 从原始密匙数据创建一个DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);
        // 创建一个密匙工厂，然后用它把DESKeySpec对象转换成
        // 一个SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey securekey = keyFactory.generateSecret(dks);
        // Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance(DES);
        // 用密匙初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, securekey, sr);
        // 现在，获取数据并解密
        // 正式执行解密操作
        return cipher.doFinal(src);
    }
 
    /**
     * BASE64编码
     * 
     * @param info
     * @return
     */
    public static String base64Encode(byte[] info) {
        return Base64.encodeBase64String(info);
    }
 
    /**
     * BASE64解码
     *
     * @param info
     * @return
     */
    public static byte[] base64Decode(String info) {
        return Base64.decodeBase64(info);
    }
 
    /**
     * 先对消息体进行DES编码再进行BASE64编码
     * 
     * @param info
     * @return
     */
    public static String encodeInfo(String info) {
        try {
            byte[] temp = encrypt(info.getBytes(ENCODING), KEY.getBytes(ENCODING));
            return base64Encode(temp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    
    /**
     * 先对消息体进行BASE64解码再进行DES解码
     * 
     * @param info
     * @return
     */
    public static String decodeInfo(String info) {
        byte[] temp = base64Decode(info);
        try {
            byte[] buf = decrypt(temp, KEY.getBytes(ENCODING));
            return StringUtils.newStringUtf8(buf);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}