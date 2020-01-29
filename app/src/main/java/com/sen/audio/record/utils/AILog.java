package com.sen.audio.record.utils;


import android.text.TextUtils;
import android.util.Log;
import com.sen.audio.record.BuildConfig;
import java.util.Arrays;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 日志输出工具
 * @author wangshengxing  07.17 2018
 */
public class AILog {
    protected static final String DEFAULT_MESSAGE = "execute";
    protected static final String LINE_SEPARATOR = System.getProperty("line.separator");
    protected static final String NULL_TIPS = "Log with null object";
    protected static final int JSON_INDENT = 4;

    /**
     * 使用log输出
     */
    public static final int OUT_DEFAULT = 0;
    /**
     * 标准java输出
     */
    public static final int OUT_SYSTEM = 1;

    public static final int V = 1;
    public static final int D = 2;
    public static final int I = 3;
    public static final int W = 4;
    public static final int E = 5;
    public static final int A = 6;
    protected static final int JSON = 7;


    //quite dangerous to rely on BuildConfig.DEBUG and ADT to do the right thing,
    //because there are bugs in the build system
    //that cause exported signed release builds to be built with BuildConfig.DEBUG set to true!
    private static final String TAG = "AILog";
    public static final int DEFAULT_LOG_LEVEL = V;
    private static int currentLogLevel = DEFAULT_LOG_LEVEL;
    public static final String ADAPTER_MARK_TAG = "adapter_mark";
    private static final String LOG_PREFIX = "AI";

    /**
     * prints normal frequency log with verbose level
     *
     * @param msg normal frequency log content
     * @see #vHighFreq(Object)
     * @since 1.0
     */
    public static void v(Object msg) {
        if (msg != null) {
            printLog(V, "", msg.toString());
        }
    }


    /**
     * prints normal frequency log with verbose level
     *
     * @param tag log tag which will be appended  prefix ''AIN-"
     * @param obj object.toString will be the content
     * @see #vHighFreq(Object)
     * @since 1.0
     */
    public static void v(String tag, Object obj) {
        if (obj != null) {
            printLog(V, tag, obj.toString());
        }
    }

    /**
     * prints normal frequency log with verbose level
     *
     * @param tag log tag which will be appended  prefix ''AIN-"
     * @param msg log content
     * @see #vHighFreq(String, String)
     * @since 1.0
     */
    public static void v(String tag, String msg) {
        if(msg != null){
            printLog(V, tag, msg);
        }
    }

    /**
     * prints high frequency log with verbose level
     *
     * @param msg high frequency log content
     * @see #v(Object)
     * @since 3.2 sp1
     */
    public static void vHighFreq(Object msg) {
        if (msg != null) {
            printLogHighFreq(V, "", msg.toString());
        }
    }

    /**
     * prints high frequency log with verbose level
     *
     * @param tag log tag which will be appended  prefix ''AIHF-"
     * @param obj object.toString will be the content
     * @see #v(String, Object)
     * @since 3.2 sp1
     */
    public static void vHighFreq(String tag, Object obj) {
        if (obj != null) {
            printLogHighFreq(V, tag, obj.toString());
        }
    }

    /**
     * prints high frequency log with verbose level
     *
     * @param tag log tag which will be appended  prefix ''AIHF-"
     * @param msg log content
     * @see #v(String, String)
     * @since 3.2 sp1
     */
    public static void vHighFreq(String tag, String msg) {
        if(msg != null) {
            printLogHighFreq(V, tag, msg);
        }
    }

    /**
     * prints normal frequency log with debug level
     *
     * @param msg normal frequency log content
     * @see #dHighFreq(Object)
     * @since 1.0
     */
    public static void d(Object msg) {
        if (msg != null) {
            printLog(D, "", msg.toString());
        }
    }

    /**
     * prints normal frequency log with debug level
     *
     * @param tag log tag which will be appended  prefix ''AIN-"
     * @param obj object.toString will be the content
     * @see #dHighFreq(String, Object)
     * @since 1.0
     */
    public static void d(String tag, Object obj) {
        if (obj != null) {
            printLog(D, tag, obj.toString());
        }
    }

    /**
     * prints normal frequency log with debug level
     *
     * @param tag log tag which will be appended  prefix ''AIN-"
     * @param msg log content
     * @see #dHighFreq(String, String)
     * @since 1.0
     */
    public static void d(String tag, String msg) {
        if (msg != null) {
            printLog(D, tag, msg);
        }
    }

    /**
     * prints normal frequency log with debug level
     *
     * @param tag log tag which will be appended  prefix ''AIN-"
     * @param msg log content
     * @param strings
     * @see #dHighFreq(String, String, Throwable)
     * @since 1.0
     */
    public static void d(String tag, String msg, Object... strings) {
        if (msg != null) {
            printLog(D, tag, msg + '\n' + Arrays.toString(strings));
        }
    }

    /**
     * prints normal frequency log with debug level
     *
     * @param tag log tag which will be appended  prefix ''AIN-"
     * @param msg log content
     * @param tr exception info
     * @see #dHighFreq(String, String, Throwable)
     * @since 1.0
     */
    public static void d(String tag, String msg, Throwable tr) {
        if (msg != null) {
            printLog(D, tag, msg + '\n' + Log.getStackTraceString(tr));
        }
    }

    /**
     * prints high frequency log with debug level
     *
     * @param msg normal frequency log content
     * @see #d(Object)
     * @since 3.2 sp1
     */
    public static void dHighFreq(Object msg) {
        if (msg != null) {
            printLogHighFreq(D, "", msg.toString());
        }
    }

    /**
     * prints high frequency log with debug level
     *
     * @param tag log tag which will be appended  prefix ''AIHF-"
     * @param obj object.toString will be the content
     * @see #d(String, Object)
     * @since 3.2 sp1
     */
    public static void dHighFreq(String tag, Object obj) {
        if (obj != null) {
            printLogHighFreq(D, tag, obj.toString());
        }
    }

    /**
     * prints high frequency log with debug level
     *
     * @param tag log tag which will be appended  prefix ''AIHF-"
     * @param msg log content
     * @see #d(String, String)
     * @since 3.2 sp1
     */
    public static void dHighFreq(String tag, String msg) {
        if (msg != null) {
            printLogHighFreq(D, tag, msg);
        }
    }

    /**
     * prints normal frequency log with debug level
     *
     * @param tag log tag which will be appended  prefix ''AIHF-"
     * @param msg log content
     * @param tr exception info
     * @see #d(String, String, Throwable)
     * @since 3.2 sp1
     */
    public static void dHighFreq(String tag, String msg, Throwable tr) {
        if (msg != null) {
            printLogHighFreq(D, tag, msg + '\n' + Log.getStackTraceString(tr));
        }
    }

    /**
     * prints log with info level
     *
     * @param msg log content
     * @since 1.0
     */
    public static void i(Object msg) {
        if (msg != null) {
            printLog(I, "", msg.toString());
        }
    }

    /**
     * prints log with info level
     *
     * @param tag log tag which will be appended  prefix ''AIN-"
     * @param obj object.toString will be the content
     * @since 1.0
     */
    public static void i(String tag, Object obj) {
        if (obj != null) {
            printLog(I, tag, obj.toString());
        }
    }

    /**
     * prints log with info level
     *
     * @param tag log tag which will be appended  prefix ''AIN-"
     * @param msg log content
     * @since 1.0
     */
    public static void i(String tag, String msg) {
        if (msg != null) {
            printLog(I, tag, msg);
        }
    }


    public static void t(String tag, String msg) {
        if (null != msg) {
            printLog(D, tag, msg);
        }
    }

    /**
     * prints log with info level
     *
     * @param tag log tag which will be appended  prefix ''AIN-"
     * @param msg log content
     * @param tr exception info
     * @since 1.0
     */
    public static void i(String tag, String msg, Throwable tr) {
        if (msg != null) {
            printLog(I, tag, msg + '\n' + Log.getStackTraceString(tr));
        }
    }


    /**
     * prints topic info log with info level
     *
     * @param tag log tag which will be appended  prefix ''AIN-"
     * @param s topic name
     * @param bytes topic params with byte array
     * @since 1.0
     */
    public static void i(String tag, String s, byte[]... bytes) {
        for (int i = 0; i < bytes.length; i++) {
            s = s + "\n" + "args[" + i + "]: " + new String(bytes[i]);
        }
        printLog(I, tag, s);
    }

    /**
     * prints topic info log with info level
     *
     * @param tag log tag which will be appended  prefix ''AIN-"
     * @param s topic name
     * @param bytes topic params
     * @since 1.0
     */
    public static void i(String tag, String s, String[] bytes) {
        for (int i = 0; i < bytes.length; i++) {
            s = s + "\n" + "args[" + i + "]: " + bytes[i];
        }
        printLog(I, tag, s);
    }

    /**
     * prints log with warn level
     *
     * @param msg log content
     * @since 1.0
     */
    public static void w(Object msg) {
        if (msg != null) {
            printLog(W, "", msg.toString());
        }
    }

    /**
     * prints log with warn level
     *
     * @param tag log tag which will be appended  prefix ''AIN-"
     * @param obj object.toString will be the content
     * @since 1.0
     */
    public static void w(String tag, Object obj) {
        if (obj != null) {
            printLog(W, tag, obj.toString());
        }
    }

    /**
     * prints log with warn level
     *
     * @param tag log tag which will be appended  prefix ''AIN-"
     * @param msg log content
     * @since 1.0
     */
    public static void w(String tag, String msg) {
        if (msg != null) {
            printLog(W, tag, msg);
        }
    }

    /**
     * prints log with warn level
     *
     * @param tag log tag which will be appended  prefix ''AIN-"
     * @param msg log content
     * @param tr exception info
     * @since 1.0
     */
    public static void w(String tag, String msg, Throwable tr) {
        if (msg != null) {
            printLog(W, tag, msg + '\n' + Log.getStackTraceString(tr));
        }
    }

    /**
     * prints log with error level
     *
     * @param msg log content
     * @since 1.0
     */
    public static void e(Object msg) {
        if (msg != null) {
            printLog(E, "", msg.toString());
        }
    }

    /**
     * prints log with error level
     *
     * @param tag log tag which will be appended  prefix ''AIN-"
     * @param obj object.toString will be the content
     * @since 1.0
     */
    public static void e(String tag, Object obj) {
        if (obj != null) {
            printLog(E, tag, obj.toString());
        }
    }

    /**
     * prints log with error level
     *
     * @param tag log tag which will be appended  prefix ''AIN-"
     * @param msg log content
     * @since 1.0
     */
    public static void e(String tag, String msg) {
        if (msg != null) {
            printLog(E, tag, msg);
        }
    }

    /**
     * prints log with error level
     *
     * @param tag log tag which will be appended  prefix ''AIN-"
     * @param msg log content
     * @param tr exception info
     * @since 1.0
     */
    public static void e(String tag, String msg, Throwable tr) {
        if (msg != null) {
            printLog(E, tag, msg + '\n' + Log.getStackTraceString(tr));
        }
    }

    /**
     * prints log with assert level
     *
     * @param msg log content
     * @since 1.0
     */
    public static void a(Object msg) {
        if (msg != null) {
            printLog(A, "", msg.toString());
        }
    }

    /**
     * prints log with assert level
     *
     * @param tag log tag which will be appended  prefix ''AIN-"
     * @param obj object.toString will be the content
     * @since 1.0
     */
    public static void a(String tag, Object obj) {
        if (obj != null) {
            printLog(A, tag, obj.toString());
        }
    }

    /**
     * prints log with assert level
     *
     * @param tag log tag which will be appended  prefix ''AIN-"
     * @param msg log content
     * @since 1.0
     */
    public static void a(String tag, String msg) {
        if (msg != null) {
            printLog(A, tag, msg);
        }
    }

    /**
     * prints log with wtf(what a terrible failure) level
     *
     * @param tag log tag which will be appended  prefix ''AIN-"
     * @param msg log content
     * @since 1.0
     */
    public static void wtf(String tag, String msg) {
        if (msg != null) {
            Log.wtf(tag, msg);
        }
    }

    /**
     * prints log with wtf(what a terrible failure) level
     *
     * @param tag log tag which will be appended  prefix ''AIN-"
     * @param tr exception info
     * @since 1.0
     */
    public static void wtf(String tag, Throwable tr) {
        if (tr != null) {
            Log.wtf(tag, tr);
        }
    }

    /**
     * prints log with wtf(what a terrible failure) level
     *
     * @param tag log tag which will be appended  prefix ''AIN-"
     * @param msg log content
     * @param tr exception info
     * @since 1.0
     */
    public static void wtf(String tag, String msg, Throwable tr) {
        if (tr != null) {
            Log.wtf(tag, msg, tr);
        }
    }

    /**
     * prints log in json format
     *
     * @param jsonFormat log in json format
     * @since 1.0
     */
    public static void json(String jsonFormat) {
        printLog(JSON, "", jsonFormat);
    }

    /**
     * prints log in json format
     *
     * @param tag og tag which will be appended  prefix ''AIN-"
     * @param jsonFormat log in json format
     * @since 1.0
     */
    public static void json(String tag, String jsonFormat) {
        printLog(JSON, tag, jsonFormat);
    }

    /**
     * @deprecated 2.0, no replacement
     */
    @Deprecated
    public static void snipet(String tag, String message) {
        int maxLogSize = 1000;
        for (int i = 0; i <= message.length() / maxLogSize; i++) {
            int start = i * maxLogSize;
            int end = (i + 1) * maxLogSize;
            end = end > message.length() ? message.length() : end;
            printLog(I, tag, message.substring(start, end));
        }
    }

    /**
     * prints high frequency log
     *
     * @param type log level
     * @param tagStr log tag which will be appended  prefix ''AIHF-"
     * @param info log content
     * @since 3.2 sp1
     */
    private synchronized static void printLogHighFreq(int type, String tagStr, String info) {
        printLog(type, tagStr, info, true);
    }

    /**
     * prints normal frequency log
     *
     * @param type log level
     * @param tagStr log tag which will be appended  prefix ''AIN-"
     * @param info log content
     * @since 3.2 sp1
     */
    private synchronized static void printLog(int type, String tagStr, String info) {
        printLog(type, tagStr, info, false);
    }

    /**
     * prints log
     *
     * @param type log level
     * @param tag log tag which will be appended  prefix ''AIN-" or "AIHF-"
     * @param info log content
     * @param isHighFreq content is high frequency or not
     * @since 3.2 sp1
     */
    private synchronized static void printLog(int type, String tag, String info, boolean isHighFreq) {
        if ((type == JSON && currentLogLevel > I) || currentLogLevel > type) {
            return;
        }

        // String[] contents = wrapperContent(tagStr, info);
        String msg = info == null ? "(null)" : info; // contents[1];
        String headString = ""; // contents[2];
        String fullInfo = (currentLogLevel == V ? (float) System.nanoTime() / 1000000000 : "") + "\t"
            + headString + msg;

        switch (type) {
            case V:
            case D:
            case I:
            case W:
            case E:
            case A:
                printDefault(type, LOG_PREFIX + (isHighFreq ? "HF-" : "N-") +  getHeadInfo() + tag, fullInfo);
                break;
            case JSON:
                if (currentLogLevel == V) {
                    printJson(JSON,LOG_PREFIX + "HF-" + tag, (float) System.nanoTime() / 1000000000 + "\t" + msg, headString);
                } else {
                    printJson(JSON,LOG_PREFIX + "HF-" + tag, msg, headString);
                }
                break;
            default:
                break;
        }
    }

    public static void printJson(int level, String tag, String msg, String headString) {
        String message;
        try {
            if(msg.startsWith("{")) {
                JSONObject lines = new JSONObject(msg);
                message = lines.toString(4);
            } else if(msg.startsWith("[")) {
                JSONArray var10 = new JSONArray(msg);
                message = var10.toString(4);
            } else {
                message = msg;
            }
        } catch (JSONException var9) {
            message = msg;
        }

        printLine(level,tag, true);
        message = headString + LINE_SEPARATOR + message;
        String[] var11 = message.split(LINE_SEPARATOR);
        String[] var5 = var11;
        int var6 = var11.length;

        for(int var7 = 0; var7 < var6; ++var7) {
            String line = var5[var7];
            printDefault(level,tag, "║ " + line);
        }

        printLine(level,tag, false);
    }

    /*
    deprecated, no performance report and proguard - shun.zhang
    private static String[] wrapperContent(String tagStr, String info) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        byte index = 5;
        String className = stackTrace[index].getFileName();
        String methodName = stackTrace[index].getMethodName();

        int lineNumber = stackTrace[index].getLineNumber();
        String methodNameShort = methodName.substring(0, 1).toUpperCase() + methodName.substring(1);

        String headString = "[ (" + className + ":" + lineNumber + ")#" + methodNameShort + " ] ";

        tagStr = TextUtils.isEmpty(tagStr) ? className : tagStr;
        info = TextUtils.isEmpty(info) ? "Log with null object" : info;
        return new String[]{tagStr, info, headString};
    }
    */

    /**
     * gets current log level
     *
     * @return int value, {V = 1 , D =2 , I = 3 , W = 4 , E = 5, A = 6}
     */
    public static int getLogLevel() {
        return currentLogLevel;
    }

    /**
     * sets log level
     *
     * @param logLevel {V = 1 , D =2 , I = 3 , W = 4 , E = 5, A = 6}
     */
    public static void setLogLevel(int logLevel) {
        if (currentLogLevel == logLevel) {
            return;
        }

        if (logLevel > E) {
            printLog(E, TAG, "Set log level failed . wrong log level");
        }

        Log.i(TAG, "setLogLevel " + currentLogLevel + "->" + logLevel);
        currentLogLevel = logLevel;
    }


    private static final int MIN_STACK_OFFSET = 2;
    private static final String SUFFIX_JAVA = ".java";

    /**
     * 获取打日志位置在方法调用栈的位置
     */
    private static int getStackOffset(StackTraceElement[] stackTrace) {
        if (null != stackTrace) {
            for (int i = MIN_STACK_OFFSET; i < stackTrace.length; i++) {
                if (!stackTrace[i].getClassName().equals(AILog.class.getName())) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * 获取打日志位置所在的类，行号，方法名，只在debug的时候输出
     */
    private static String getHeadInfo() {
        if(!BuildConfig.DEBUG){
            return "";
        }

        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

        int index = getStackOffset(stackTrace);
        if (index == -1) {
            return "[]";
        }
        StackTraceElement element = stackTrace[index];
        String className = element.getClassName();
        if (className.contains(".")) {
            String[] names = className.split("\\.");
            className = names[names.length - 1] + SUFFIX_JAVA;
        }

        if (className.contains("$")) {
            className = className.split("\\$")[0] + SUFFIX_JAVA;
        }

        String methodName = element.getMethodName();
        int lineNumber = element.getLineNumber();

        return "[(" + className + ":" + lineNumber + ")#" + methodName + "]";
    }

    protected static void printLine(int type, String tag, boolean isTop) {

        if (isTop) {
            printDefault(type, tag, "╔═══════════════════════════════════════════════════════════════════════════════════════");
        } else {
            printDefault(type, tag, "╚═══════════════════════════════════════════════════════════════════════════════════════");
        }
    }

    protected static boolean isEmpty(String line) {
        return TextUtils.isEmpty(line) || line.equals("\n") || line.equals("\t") || TextUtils.isEmpty(line.trim());
    }

    public static void printDefault(int level, String tag, String msg) {
        switch(level) {
            case V:
                Log.v(tag, msg);
                break;
            case D:
                Log.d(tag, msg);
                break;
            case I:
                Log.i(tag, msg);
                break;
            case W:
                Log.w(tag, msg);
                break;
            case E:
                Log.e(tag, msg);
                break;
            case A:
                Log.wtf(tag, msg);
        }
    }

}