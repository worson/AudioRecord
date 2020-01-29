package com.sen.audio.record.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Build.VERSION;
import androidx.core.content.ContextCompat;

/**
 * 说明:
 *
 * @author wangshengxing  01.29 2020
 */
public class PermissionUtil {

    public static boolean hasPermission(Context context, String... permissons) {
        String[] var2 = permissons;
        int var3 = permissons.length;

        for (int var4 = 0; var4 < var3; ++var4) {
            String permisson = var2[var4];
            if (ContextCompat.checkSelfPermission(context, permisson) != 0) {
                return false;
            }
        }

        return true;
    }

    public static boolean requestPermissions(Activity activity, int requestCode, String... permissions) {
        if (VERSION.SDK_INT >= 23 && !hasPermission(activity, permissions)) {
            activity.requestPermissions(permissions, requestCode);
            return false;
        } else {
            return true;
        }
    }

    public static boolean isDynamicPermission() {
        return VERSION.SDK_INT >= 23;
    }
}
