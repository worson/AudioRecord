package com.sen.audio.record.play;

/**
 * 说明:
 *
 * @author wangshengxing  01.29 2020
 */
public interface IPlayerCallback {

    void onStart();

    void onSuccess();

    void onEnd();

    void onError();
}
