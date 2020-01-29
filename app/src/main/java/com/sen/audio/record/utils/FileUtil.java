package com.sen.audio.record.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.TextUtils;
import android.util.Log;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * 说明:
 *
 * @author wangshengxing  01.29 2020
 */
public class FileUtil {
    private final static String TAG = "FileUtil";
    private static ExecutorService mSingleThreadExecutor = Executors.newSingleThreadExecutor();
    private static final char[] HEX_CHAR = {'0', '1', '2', '3', '4', '5',
        '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private static String hexString = "0123456789abcdef";

    /**
     * 字符串保存到手机内存设备中
     *
     * @param str
     */
    public static void saveFile(String str, String parent, String fileName) {
        // 创建String对象保存文件名路径
        try {
            // 创建指定路径的文件
//            File file = new File(Environment.getExternalStorageDirectory(), fileName);
            File file = new File(parent, fileName);
            // 如果文件不存在
            if (file.exists()) {
                // 创建新的空文件
                file.delete();
            }
            file.createNewFile();
            // 获取文件的输出流对象
            FileOutputStream outStream = new FileOutputStream(file);
            // 获取字符串对象的byte数组并写入文件流
            outStream.write(str.getBytes());
            // 最后关闭文件输出流
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 删除已存储的文件
     *
     * @param parent   文件夹路径
     * @param fileName 文件名
     */
    public static void deletefile(String parent, String fileName) {
        try {
            // 找到文件所在的路径并删除该文件
            File file = new File(parent, fileName);
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //包括文件和文件夹，全删除
    public static void deletefile(File file) {
        try {
            if (file.isFile()) {
                file.delete();
                return;
            }

            if (file.isDirectory()) {
                File[] childFiles = file.listFiles();
                if (childFiles == null || childFiles.length == 0) {
                    file.delete();
                    return;
                }

                for (int i = 0; i < childFiles.length; i++) {
                    deletefile(childFiles[i]);
                }
                file.delete();
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    /**
     * 删除指定目录下文件及目录
     *
     * @param deleteThisPath
     * @param filePath
     * @return
     */
    public static void deleteFolderFile(String filePath, boolean deleteThisPath) {
        if (!TextUtils.isEmpty(filePath)) {
            try {
                File file = new File(filePath);
                if (file.isDirectory()) {// 处理目录
                    File files[] = file.listFiles();
                    for (int i = 0; i < files.length; i++) {
                        deleteFolderFile(files[i].getAbsolutePath(), true);
                    }
                }
                if (deleteThisPath) {
                    if (!file.isDirectory()) {// 如果是文件，删除
                        file.delete();
                    } else {// 目录
                        if (file.listFiles().length == 0) {// 目录下没有文件或者目录，删除
                            file.delete();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 清空文件夹
     */
    public static void clearTemp(File file) {
        try {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    File f = files[i];
                    f.delete();
                }
            } else if (file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 读取文件里面的内容
     */
    public static String getFile(String filePath, String fileName) {
        try {
            // 创建文件
            File file = new File(filePath, fileName);
            // 创建FileInputStream对象
            FileInputStream fis = new FileInputStream(file);
            // 创建字节数组 每次缓冲1M
            byte[] b = new byte[1024];
            int len = 0;// 一次读取1024字节大小，没有数据后返回-1.
            // 创建ByteArrayOutputStream对象
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            // 一次读取1024个字节，然后往字符输出流中写读取的字节数
            while ((len = fis.read(b)) != -1) {
                baos.write(b, 0, len);
            }
            // 将读取的字节总数生成字节数组
            byte[] data = baos.toByteArray();
            // 关闭字节输出流
            baos.close();
            // 关闭文件输入流
            fis.close();
            // 返回字符串对象
            return new String(data);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 读取文件里面的内容
     */
    public static String readFile(File file) {
        try {

            // 创建FileInputStream对象
            FileInputStream fis = new FileInputStream(file);
            // 创建字节数组 每次缓冲1M
            byte[] b = new byte[1024];
            int len = 0;// 一次读取1024字节大小，没有数据后返回-1.
            // 创建ByteArrayOutputStream对象
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            // 一次读取1024个字节，然后往字符输出流中写读取的字节数
            while ((len = fis.read(b)) != -1) {
                baos.write(b, 0, len);
            }
            // 将读取的字节总数生成字节数组
            byte[] data = baos.toByteArray();
            // 关闭字节输出流
            baos.close();
            // 关闭文件输入流
            fis.close();
            // 返回字符串对象
            return new String(data);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }


    /**
     * byte转string
     *
     * @param src
     * @return
     */
    public static String bytesToHex(byte[] b) {
//        if (src == null || src.length <= 0) {
//            return null;
//        }
//
//        StringBuilder stringBuilder = new StringBuilder("");
//        for (int i = 0; i < src.length; i++) {
//            // 之所以用byte和0xff相与，是因为int是32位，与0xff相与后就舍弃前面的24位，只保留后8位
//            String str = Integer.toHexString(src[i] & 0xff);
//            if (str.length() < 2) { // 不足两位要补0
//                stringBuilder.append(0);
//            }
//            stringBuilder.append(str);
//        }
//        return stringBuilder.toString();

        String stmp = "";
        StringBuilder sb = new StringBuilder("");
        for (int n = 0; n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0xFF);
            sb.append((stmp.length() == 1) ? "0" + stmp : stmp);
            sb.append(" ");
        }
        return sb.toString().toUpperCase().trim();
    }


    /**
     * hex to string
     *
     * @param s
     * @return
     */
    public static String hexStringToString(String s) {
        if (s == null || s.equals("")) {
            return null;
        }
        s = s.replace(" ", "");
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new String(baKeyword);

    }


    public static String bytesToHexFun3(byte[] bytes) {
        char[] buf = new char[bytes.length * 2];
        int index = 0;
        for (byte b : bytes) { // 利用位运算进行转换，可以看作方法一的变种
            buf[index++] = HEX_CHAR[b >>> 4 & 0xf];
            buf[index++] = HEX_CHAR[b & 0xf];
        }

        return hexStringToString(new String(buf));
    }


    public static StringBuilder byte2hex(byte[] data) {
        StringBuilder stringBuilder = new StringBuilder(data.length);
        for (byte byteChar : data) {
            stringBuilder.append(String.format("%02X ", byteChar).trim());
        }
        return stringBuilder;
    }


    public static String hexStr2Str(String hexStr) {
        String str = "0123456789ABCDEF";
        char[] hexs = hexStr.toCharArray();
        byte[] bytes = new byte[hexStr.length() / 2];
        int n;

        for (int i = 0; i < bytes.length; i++) {
            n = str.indexOf(hexs[2 * i]) * 16;
            n += str.indexOf(hexs[2 * i + 1]);
            bytes[i] = (byte) (n & 0xff);
        }
        return new String(bytes);
    }


    /**
     * 将二进制转换成十六进制串
     */
    public static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }


    /**
     * unicode的String转换成String的字符串
     *
     * @param String hex 16进制值字符串 （一个unicode为2byte）
     * @return String 全角字符串
     */
    public static String unicodeToString(String hex) {
        int t = hex.length() / 6;
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < t; i++) {
            String s = hex.substring(i * 6, (i + 1) * 6);
            // 高位需要补上00再转
            String s1 = s.substring(2, 4) + "00";
            // 低位直接转
            String s2 = s.substring(4);
            // 将16进制的string转为int
            int n = Integer.valueOf(s1, 16) + Integer.valueOf(s2, 16);
            // 将int转换为字符
            char[] chars = Character.toChars(n);
            str.append(new String(chars));
        }
        return str.toString();
    }


    public static String byte2HexStr(byte[] array) {
        StringBuilder builder = new StringBuilder();

        for (byte b : array) {
            String hex = Integer.toHexString(b & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            builder.append(hex);
        }

        return builder.toString().toUpperCase();

    }


    /**
     * byte to file
     */
    public static void byteToFile(byte[] bytesArray, String fileDest) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(fileDest);
            fos.write(bytesArray);
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 复制单个文件
     *
     * @param oldPath String 原文件路径 如：c:/fqf.txt
     * @param newPath String 复制后路径 如：f:/fqf.txt
     * @return boolean
     */
    public static boolean copyFile(String oldPath, String newPath) {
        InputStream inStream = null;
        FileOutputStream fs = null;
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { //文件存在时
                inStream = new FileInputStream(oldPath); //读入原文件

                String path = newPath.substring(0, newPath.lastIndexOf("/"));
                File file = new File(path);

                if (!file.exists()) {
                    boolean bl = file.mkdirs();
                    AILog.i(TAG, "bl0:" + bl);
                }
                File newFile = new File(newPath);
                if (!newFile.exists()) {
                    boolean bl = newFile.createNewFile();
                    AILog.i(TAG, "bl:" + bl);
                }

                fs = new FileOutputStream(newFile);
                byte[] buffer = new byte[16 * 1024];   //利好大文件
                int length;
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    //==System.out.println("copyfile:"+bytesum);

                    fs.write(buffer, 0, byteread);
                }
                IOUtils.closeQuietly(inStream);
                return true;
            }
        } catch (Exception e) {
            AILog.d(TAG, "error 复制单个文件操作出错 " + oldPath + "->" + newPath);
            e.printStackTrace();
            return false;
        } finally {
            IOUtils.closeQuietly(inStream);
            IOUtils.closeQuietly(fs);

        }

        return false;
    }

    /**
     * 复制单个文件
     *
     * @param oldPath String 原文件路径 如：c:/fqf.txt
     * @param newPath String 复制后路径 如：f:/fqf.txt
     * @return boolean
     */
    public static boolean copyFileLow(String oldPath, String newPath) {
        FileOutputStream fs = null;
        InputStream inStream = null;
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { //文件存在时
                inStream = new FileInputStream(oldPath); //读入原文件

                String path = newPath.substring(0, newPath.lastIndexOf("/"));
                File file = new File(path);

                if (!file.exists()) {
                    boolean bl = file.mkdirs();
                    AILog.i(TAG, "bl0:" + bl);
                }
                File newFile = new File(newPath);
                if (!newFile.exists()) {
                    boolean bl = newFile.createNewFile();
                    AILog.i(TAG, "bl:" + bl);
                }

                fs = new FileOutputStream(newFile);
                byte[] buffer = new byte[2 * 1024];   //利好大文件
                int length;
                AILog.w(TAG, "in copyFileLow " + oldPath);
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    //==System.out.println("copyfile:"+bytesum);
                    Sleep(1);
                    fs.write(buffer, 0, byteread);
                }


                AILog.w(TAG, "in copyFileLow " + newPath);
                return true;
            }
        } catch (Exception e) {
            AILog.d(TAG, "error 复制单个文件操作出错 " + oldPath + "->" + newPath);

            e.printStackTrace();
            return false;
        } finally {
            IOUtils.closeQuietly(inStream);
            IOUtils.closeQuietly(fs);
        }

        return false;
    }

    private static void Sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 复制整个文件夹内容
     *
     * @param oldPath String 原文件路径 如：c:/fqf
     * @param newPath String 复制后路径 如：f:/fqf/ff
     * @return boolean //这里的uid和gid用1023 也就是  media_rw
     */
    public static boolean copyFolder(String oldPath, String newPath) {
        AILog.i(TAG, "oldPath:" + oldPath + ";newPath:" + newPath);
        FileInputStream input = null;
        FileOutputStream output = null;
        try {
            (new File(newPath)).mkdirs(); //如果文件夹不存在 则建立新文件夹
            //==FileUtilsWrap.setPermissions(newPath, FileUtilsWrap.S_IRWXU | FileUtilsWrap.S_IRWXG | FileUtilsWrap.S_IRWXO, -1, -1);
            File a = new File(oldPath);
            String[] file = a.list();
            File temp = null;
            for (int i = 0; i < file.length; i++) {
                AILog.i(TAG, "file:" + file[i]);
                if (oldPath.endsWith(File.separator)) {
                    temp = new File(oldPath + file[i]);
                    AILog.i(TAG, "temp1:" + temp);
                } else {
                    temp = new File(oldPath + File.separator + file[i]);
                    AILog.i(TAG, "temp2:" + temp);
                }

                if (temp.isFile()) {
                    String newFile = newPath + "/" + (temp.getName()).toString();
                    AILog.i(TAG, "new 3:" + newFile);
                    try {
                        input = new FileInputStream(temp);

                        output = new FileOutputStream(newFile);
                        byte[] b = new byte[1024 * 16];
                        int len;
                        while ((len = input.read(b)) != -1) {
                            output.write(b, 0, len);
                        }

                        output.flush();


                        //==FileUtilsWrap.setPermissions(newFile, FileUtilsWrap.S_IRUSR | FileUtilsWrap.S_IWUSR | FileUtilsWrap.S_IRGRP | FileUtilsWrap.S_IWGRP | FileUtilsWrap.S_IROTH | FileUtilsWrap.S_IWOTH, -1, -1);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } finally {
                        IOUtils.closeQuietly(output);
                        IOUtils.closeQuietly(input);
                    }
                } else if (temp.isDirectory()) {//如果是子文件夹
                    copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
                } else {
                    AILog.w(TAG, "file type :" + temp.getPath());
                }
            }
        } catch (Exception e) {
            System.out.println("copy 复制整个文件夹内容操作出错");

            e.printStackTrace();

            return false;
        } finally {
            IOUtils.closeQuietly(output);
            IOUtils.closeQuietly(input);
        }
        return true;
    }

    /**
     * 读取aseet目录
     *
     * @param context
     * @param filePath
     * @return
     */
    public static String readAssets(Context context, String filePath) {
        InputStream is = null;
        String result = null;
        try {
            is = context.getAssets().open(filePath);
            int length = is.available();
            byte[] buffer = new byte[length];
            is.read(buffer);
            result = new String(buffer, "utf8");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * 复制Assets的文件到本地文件夹
     *
     * @param context
     * @param filePath
     */
    public static void copyFilesFromAssets(Context context, String filePath) {

        File jhPath = new File(filePath + "/Langogo" + "/out.apk");

        //查看该文件是否存在
        if (jhPath.exists()) {
            AILog.d("test", "该文件已存在");
        } else {
            //不存在先创建文件夹
            File path = new File(filePath);
            AILog.d("test", "pathStr=" + path);
            if (path.mkdir()) {
                AILog.d("test", "创建成功");
            } else {
                AILog.d("test", "创建失败");
            }
            try {
                //得到资源
                AssetManager am = context.getAssets();
                //得到该文件的输入流
                InputStream is = am.open("out.apk");
                AILog.d("test", is + "");
                //用输出流写到特定路径下
                FileOutputStream fos = new FileOutputStream(jhPath);
                AILog.d("test", "fos=" + fos);
                AILog.d("test", "jhPath=" + jhPath);
                //创建byte数组  用于1KB写一次
                byte[] buffer = new byte[1024];
                int count = 0;
                while ((count = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, count);
                }

                //最后关闭流
                fos.flush();
                fos.close();
                is.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取文件的大小，无此文件时返回0
     *
     * @return
     */
    public static long getFileSize(String filename) {
        File file = new File(filename);
        return getFileSize(file);
    }

    /**
     * 获取文件夹大小
     *
     * @param file File实例
     * @return long
     */
    public static long getFileSize(File file) {
        long size = 0;
        try {
            if (file.exists()) {
                File[] fileList = file.listFiles();
                if (fileList != null) {
                    for (int i = 0; i < fileList.length; i++) {
                        if (fileList[i].isDirectory()) {
                            size = size + getFileSize(fileList[i]);

                        } else {
                            size = size + fileList[i].length();

                        }
                    }
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        //return size/1048576;
        return size;
    }


    /**
     * 修改文件内容（覆盖或者添加）
     *
     * @param path    文件地址
     * @param content 覆盖内容
     * @param append  指定了写入的方式，是覆盖写还是追加写(true=追加)(false=覆盖)
     */
    public static void modifyFile(String path, String content, boolean append) {
        try {
            FileWriter fileWriter = new FileWriter(path, append);
            BufferedWriter writer = new BufferedWriter(fileWriter);
            writer.append(content);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将文本写入到文件中
     *
     * @param filePath
     * @param content
     * @param isAppend 是否追加
     */
    public static void saveStringToFile(final String filePath, final String content, final boolean isAppend) {
        mSingleThreadExecutor.submit(new Runnable() {

            @Override
            public synchronized void run() {
                if (TextUtils.isEmpty(filePath) || filePath.matches("null\\/.*")) {
                    Log.w(TAG, "The file to be saved is null!");
                    return;
                }
                FileWriter fw = null;
                try {
                    File file = new File(filePath);
                    File parent = file.getParentFile();
                    if (parent != null && !parent.exists()) {
                        parent.mkdirs();
                    }

                    fw = new FileWriter(file, isAppend);
                    fw.write(content);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (fw != null) {
                            fw.close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    /**
     * 将目标文件夹的大小调整到指定大小以内，该方法没有递归查找，只处理目标文件夹内的子文件，子文件夹不处理
     *
     * @param file       目标文件夹
     * @param targetSize 目标大小，单位 byte
     * @return true 调整成功 false 调整失败
     */
    public static boolean trimToSize(File file, long targetSize) {
        if (null == file || !file.isDirectory() || targetSize < 0) {
            return false;
        }

        File[] files = file.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isFile();
            }
        });


        if (null == files) {
            return false;
        }

        long totalSize = 0;
        for (File f : files) {
            totalSize += f.length();
        }

        Arrays.sort(files, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                return (int) (o1.lastModified() - o2.lastModified());
            }
        });

        for (int i = 0; i < files.length && totalSize > targetSize; i++) {
            File tempFile = files[i];
            long tempLength = tempFile.length();
            boolean delete = tempFile.delete();
            if (delete) {
                totalSize -= tempLength;
            }
        }

        return totalSize <= targetSize;
    }


    /**
     * 判断文件是否存在
     * @param strFile
     * @return
     */
    public static boolean fileIsExists(String strFile) {
        try {
            File f = new File(strFile);
            if (!f.exists()) {
                return false;
            }

        } catch (Exception e) {
            return false;
        }

        return true;
    }


    /**
     *
     * @param filePath
     */
    public static void setFile777(String filePath){
        File dirFile = new File(filePath);
        dirFile.setReadable(true, false);
        dirFile.setExecutable(true, false);
        dirFile.setWritable(true, false);
    }

    /**
     * @param buffer   data
     * @param filePath destination file path
     * @param isAppend
     */
    public static void writeFile(byte[] buffer, String filePath, boolean isAppend) {

        File file = new File(filePath);
        FileOutputStream fos = null;
        try {
            if (!file.exists())
                file.createNewFile();

            fos = new FileOutputStream(file, isAppend);
            fos.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.flush();
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    public static byte[] loadDatatoByte(String filePath) throws IOException {
        File file = new File(filePath);
        long fileSize = file.length();
        if (fileSize > Integer.MAX_VALUE) {
            System.out.println("file too big...");
            return null;
        }
        FileInputStream fi = new FileInputStream(file);
        byte[] buffer = new byte[(int) fileSize];
        int offset = 0;
        int numRead = 0;
        while (offset < buffer.length && (numRead = fi.read(buffer, offset, buffer.length - offset)) >= 0) {
            offset += numRead;
        }
        if (offset != buffer.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }
        fi.close();
        return buffer;
    }

}
