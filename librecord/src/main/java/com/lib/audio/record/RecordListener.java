package com.lib.audio.record;

/**
 * 说明:
 *
 * @author wangshengxing  01.29 2020
 */
public interface RecordListener {

    void onStartRecord();

    void onRecordData(byte[] var1);

    void onStopRecord();
}
