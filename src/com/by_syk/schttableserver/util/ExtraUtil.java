package com.by_syk.schttableserver.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;

public class ExtraUtil {
    public static String getDate(long time, String format) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        
        String result = "";
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.US);
            result = dateFormat.format(calendar.getTime());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static Date parseDate(String date, String format) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.US);
            return dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }
    
    public static String readFile(File file) {
        if (file == null) {
            return null;
        }

        StringBuilder sbData = new StringBuilder();
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            String buffer;
            while ((buffer = bufferedReader.readLine()) != null) {
                sbData.append(buffer).append("\n");
            }
            if (sbData.length() > 0) {
                sbData.setLength(sbData.length() - 1);
            }

            return sbData.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }
    
    public static String readFile(InputStream inputStream) {
        if (inputStream == null) {
            return null;
        }

        StringBuilder sbData = new StringBuilder();
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String buffer;
            while ((buffer = bufferedReader.readLine()) != null) {
                sbData.append(buffer).append("\n");
            }
            if (sbData.length() > 0) {
                sbData.setLength(sbData.length() - 1);
            }

            return sbData.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }
    
    public static boolean zip(File zipFile, File[] files) {
        if (zipFile == null || files == null) {
            return false;
        }
        
        ZipOutputStream zos = null;
        try {
            zos = new ZipOutputStream(new FileOutputStream(zipFile));
            for (File file : files) {
                InputStream inputStream = new FileInputStream(file);
                zos.putNextEntry(new ZipEntry(file.getName()));
                int len;
                byte[] buffer = new byte[1024];
                while ((len = inputStream.read(buffer)) != -1) {
                    zos.write(buffer, 0, len);
                }
                zos.closeEntry();
                inputStream.close();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (zos != null) {
                try {
                    zos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        
        return false;
    }
    
    public static boolean zip(File zipFile, String[] names, String[] pages) {
        if (zipFile == null || names == null || pages == null
                || names.length != pages.length) {
            return false;
        }
        
        ZipOutputStream zos = null;
        try {
            zos = new ZipOutputStream(new FileOutputStream(zipFile));
            for (int i = 0, len = names.length; i < len; ++i) {
                zos.putNextEntry(new ZipEntry(names[i]));
                zos.write(pages[i].getBytes("UTF-8"));
                zos.closeEntry();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (zos != null) {
                try {
                    zos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        
        return false;
    }
    
    public static byte[] getFileBytes(File file) {
        if (file == null || !file.isFile()) {
            return null;
        }
        
        FileInputStream fis = null;
        ByteArrayOutputStream bos = null;
        try {
            fis = new FileInputStream(file);
            bos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1000];
            int len;
            while ((len = fis.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        
        return null;
    }
    
    public static boolean saveFile(String text, File saveFile) {
        if (text == null || saveFile == null) {
            return false;
        }

        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(saveFile);
            outputStream.write(text.getBytes("UTF-8"));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return false;
    }
    
    public static String getRemoteHost(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (StringUtil.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtil.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtil.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        
        return ip.equals("0:0:0:0:0:0:0:1") ? "127.0.0.1" : ip;
    }
    
    public static List<Integer> getIntArr(int start, int end) {
        List<Integer> list = new ArrayList<>();
        for (int i = start; i <= end; ++i) {
            list.add(i);
        }
        return list;
    }
    
//    public static String getPinYinForSorting(String text) {
//        if (text == null || text.isEmpty()) {
//            return "";
//        }
//
//        StringBuilder result = new StringBuilder();
//
//        HanyuPinyinOutputFormat hanYuPinyinOutputFormat = new HanyuPinyinOutputFormat();
//        hanYuPinyinOutputFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
//        /*
//         * WITHOUT_TONE：无音标 （zhong）
//         * WITH_TONE_NUMBER：1-4数字表示英标 （zhong4）
//         * WITH_TONE_MARK：直接用音标符（必须WITH_U_UNICODE否则异常） （zhòng）
//         */
//        hanYuPinyinOutputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
//        /*
//         * WITH_V：用v表示ü （nv）
//         * WITH_U_AND_COLON：用"u:"表示ü （nu:）
//         * WITH_U_UNICODE：直接用ü （nü）
//         */
//        hanYuPinyinOutputFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
//
//        try {
//            String[] tempStrs;
//            for (char ch : text.toCharArray()) {
//                tempStrs = PinyinHelper.toHanyuPinyinStringArray(ch, hanYuPinyinOutputFormat);
//                if (tempStrs == null) {
//                    continue;
//                }
//
//                // 仅选取多音字的第一个音
//                result.append(tempStrs[0]);
//            }
//        } catch (BadHanyuPinyinOutputFormatCombination e) {
//            e.printStackTrace();
//        }
//
//        if (result.length() > 0) {
//            result.setLength(result.length() - 1);
//        }
//
//        return result.toString();
//    }
}
