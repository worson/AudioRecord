package com.sen.audio.record.utils;

import android.os.Handler;
import android.os.Looper;

/**
 * 说明:
 *
 * @author wangshengxing  01.29 2020
 */
public class HandlerUtil {

    private static Handler mMainHandler = null;

    public HandlerUtil() {
    }

    public static Handler getMainHandler() {
        if (mMainHandler == null) {
            mMainHandler = new Handler(Looper.getMainLooper());
        }

        return mMainHandler;
    }

    public static void postInMainThread(Runnable runnable) {
        if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
            runnable.run();
        } else {
            getMainHandler().post(runnable);
        }

    }

    public static void postInMainThread(Runnable runnable, long delay) {
        getMainHandler().postDelayed(runnable, delay);
    }

    public static void removeCallbacks(Runnable runnable) {
        getMainHandler().removeCallbacks(runnable);
    }
}
