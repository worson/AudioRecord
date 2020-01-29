package com.sen.audio.record.utils;

import android.text.TextUtils;
import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * 说明:
 *
 * @author wangshengxing  01.29 2020
 */
public class StringUtils {

    private static final String TAG = "StringUtils";

    private StringUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 字符串拼接,线程安全
     */
    public static String buffer(String... array) {
        StringBuffer s = new StringBuffer();
        for (String str : array) {
            s.append(str);
        }
        return s.toString();
    }

    /**
     * 字符串拼接,线程不安全,效率高
     */
    public static String builder(String... array) {
        StringBuilder s = new StringBuilder();
        for (String str : array) {
            s.append(str);
        }
        return s.toString();
    }


    /**
     * 判断一个字符串是否全部为空白字符，空白字符指由空格' '、制表符'\t'、回车符'\r'、换行符'\n'组成的字符串
     *
     * @param input 输入字符集
     * @return boolean
     */
    public static boolean isBlank(CharSequence input) {
        if (input == null || "".equals(input)) {
            return true;
        }
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            // 只要有一个字符不是制表 空格 换行 回车中的一个,就不是空字符
            if (c != '\t' && c != ' ' && c != '\n' && c != '\r') {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断字符串是否为null或长度为0
     *
     * @param s 待校验字符串
     * @return {@code true}: 空<br> {@code false}: 不为空
     */
    public static boolean isEmpty(CharSequence s) {
        return s == null || s.length() == 0;
    }

    /**
     * 字符是否为空，有"null"的判断
     *
     * @param s 待校验字符串
     * @return {@code true}: 不为空<br> {@code false}: 空
     */
    public static boolean notEmpty(String s) {
        return !isEmpty(s);
    }

    /**
     * 判断字符串是否为null或全为空格
     *
     * @param s 待校验字符串
     * @return {@code true}: null或全空格<br> {@code false}: 不为null且不全空格
     */
    public static boolean isSpace(String s) {
        return (s == null || s.trim().length() == 0);
    }

    /**
     * 判断两字符串是否相等
     *
     * @param a 待校验字符串a
     * @param b 待校验字符串b
     * @return {@code true}: 相等<br>{@code false}: 不相等
     */
    public static boolean equals(CharSequence a, CharSequence b) {
        if (a == b) {
            return true;
        }
        int length;
        if (a != null && b != null && (length = a.length()) == b.length()) {
            if (a instanceof String && b instanceof String) {
                return a.equals(b);
            } else {
                for (int i = 0; i < length; i++) {
                    if (a.charAt(i) != b.charAt(i)) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 判断两字符串忽略大小写是否相等
     *
     * @param a 待校验字符串a
     * @param b 待校验字符串b
     * @return {@code true}: 相等<br>{@code false}: 不相等
     */
    public static boolean equalsIgnoreCase(String a, String b) {
        return (a == b) || (b != null) && (a.length() == b.length()) && a.regionMatches(true, 0, b, 0, b.length());
    }

    /**
     * null转为长度为0的字符串
     *
     * @param s 待转字符串
     * @return s为null转为长度为0字符串，否则不改变
     */
    public static String null2Length0(String s) {
        return s == null ? "" : s;
    }

    /**
     * 返回字符串长度
     *
     * @param s 字符串
     * @return null返回0，其他返回自身长度
     */
    public static int length(CharSequence s) {
        return s == null ? 0 : s.length();
    }

    /**
     * 首字母大写
     *
     * @param s 待转字符串
     * @return 首字母大写字符串
     */
    public static String upperFirstLetter(String s) {
        if (isEmpty(s) || !Character.isLowerCase(s.charAt(0))) {
            return s;
        }
        return String.valueOf((char) (s.charAt(0) - 32)) + s.substring(1);
    }

    /**
     * 首字母小写
     *
     * @param s 待转字符串
     * @return 首字母小写字符串
     */
    public static String lowerFirstLetter(String s) {
        if (isEmpty(s) || !Character.isUpperCase(s.charAt(0))) {
            return s;
        }
        return String.valueOf((char) (s.charAt(0) + 32)) + s.substring(1);
    }

    /**
     * 反转字符串
     *
     * @param s 待反转字符串
     * @return 反转字符串
     */
    public static String reverse(String s) {
        int len = length(s);
        if (len <= 1) {
            return s;
        }
        int mid = len >> 1;
        char[] chars = s.toCharArray();
        char c;
        for (int i = 0; i < mid; ++i) {
            c = chars[i];
            chars[i] = chars[len - i - 1];
            chars[len - i - 1] = c;
        }
        return new String(chars);
    }

    /**
     * 转化为半角字符
     *
     * @param s 待转字符串
     * @return 半角字符串
     */
    public static String toDBC(String s) {
        if (isEmpty(s)) {
            return s;
        }
        char[] chars = s.toCharArray();
        for (int i = 0, len = chars.length; i < len; i++) {
            if (chars[i] == 12288) {
                chars[i] = ' ';
            } else if (65281 <= chars[i] && chars[i] <= 65374) {
                chars[i] = (char) (chars[i] - 65248);
            } else {
                chars[i] = chars[i];
            }
        }
        return new String(chars);
    }

    /**
     * 转化为全角字符
     *
     * @param s 待转字符串
     * @return 全角字符串
     */
    public static String toSBC(String s) {
        if (isEmpty(s)) {
            return s;
        }
        char[] chars = s.toCharArray();
        for (int i = 0, len = chars.length; i < len; i++) {
            if (chars[i] == ' ') {
                chars[i] = (char) 12288;
            } else if (33 <= chars[i] && chars[i] <= 126) {
                chars[i] = (char) (chars[i] + 65248);
            } else {
                chars[i] = chars[i];
            }
        }
        return new String(chars);
    }

    /**
     * 字节数组转字符串，经过“utf-8”编码
     *
     * @param bytes 源数据
     * @return 经过编码的String数据
     */
    public static String getEncodedString(byte[] bytes) {
        String result = "";
        try {
            result = new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            AILog.e(TAG, e.toString());
        }
        return result.trim();
    }

    /**
     * 判断参数是否是十进制数
     *
     * @param match 需要判断的数
     * @return true:是十进制数;false:不是十进制数
     */
    public static boolean isDecimalNumber(String match) {
        String reg = "([+|-])?(([0-9]+.[0-9]*)|([0-9]*.[0-9]+)|([0-9]+))((e|E)([+|-])?[0-9]+)?";

        if (match == null || match.trim().equals("")) {
            return false;
        }

        Pattern pattern = Pattern.compile(reg);
        return pattern.matcher(match).matches();
    }

    /**
     * 找到str在数组中的下标
     */
    public static int indexOf(String str, String[] values) {
        for (int i = 0; i < values.length; i++) {
            if (str.equals(values[i])) {
                return i;
            }
        }
        return -1;
    }


    /**
     * yyyy.MM.dd HH.mm.ss 截取--> yyyy.MM.dd HH:mm
     */
    public static String conversationNameSub(String value) {

        String time = value.trim().substring(0, 16);
        String[] array = time.split(" ");
        String min = array[1].replace(".", ":");

        return array[0] + " " + min;
    }


    /**
     * 获取特定长度的随机数 字母加数字
     */
    public static String getCharAndNumr(int length) {
        String val = "";
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            // 输出字母还是数字
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            // 字符串
            if ("char".equalsIgnoreCase(charOrNum)) {
                // 取得大写字母还是小写字母
                int choice = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char) (choice + random.nextInt(26));
            } else if ("num".equalsIgnoreCase(charOrNum)) { // 数字
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }


    /**
     * 获取特定长度的随机数字
     */
    public static String getRandomNum(int length) {
        String val = String.valueOf(Math.random());

        if (TextUtils.isEmpty(val)) {
            return "12345678";
        }

        String[] rand = val.split("\\.");

        if (length > 15) {
            return rand[1];
        }

        val = rand[1].substring(0, length).trim();

        return val;
    }

    /**
     * 超过指定长度加省略号
     */
    public static String ellipsis(String string, int max) {
        if (string == null) {
            return null;
        }
        if (string.length() > max) {
            return string.substring(0, max - 3) + "...";
        }
        return string;
    }

}
