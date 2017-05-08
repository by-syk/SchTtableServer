package com.by_syk.schttableserver.core.ncu;

/**
 * conwork.js -> EncodeUtil.java
 */
public class EncodeUtil {
    private static final String KEY_STR = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
    
    public static String encodeInp(String input) {
        if (input == null) {
            return "";
        }
        
        String output = "";
        char[] chArr = input.toCharArray();
        for (int i = 0, len = chArr.length; i < len; i += 3) {
            int enc1 = chArr[i] >>> 2;
            int enc2 = (chArr[i] & 3) << 4;
            int enc3 = 64;
            if (i + 1 < len) {
                enc2 |= (chArr[i + 1] >>> 4);
                enc3 = (chArr[i + 1] & 15) << 2;
            }
            int enc4 = 64;
            if (i + 2 < len) {
                enc3 |= (chArr[i + 2] >>> 6);
                enc4 = chArr[i + 2] & 63;
            }
            output += KEY_STR.substring(enc1, enc1 + 1) + KEY_STR.substring(enc2, enc2 + 1)
                + KEY_STR.substring(enc3, enc3 + 1) + KEY_STR.substring(enc4, enc4 + 1);
        }
        
        return output;
    }
}
