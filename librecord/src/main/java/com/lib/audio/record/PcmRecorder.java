package com.lib.audio.record;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.os.Process;
import android.util.Log;

/**
 * 说明:
 *
 * @author wangshengxing  01.29 2020
 */
public class PcmRecorder {

    private static final String TAG = "PcmRecorder";

    public static final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;

    private AudioRecord mAudioRecord;
    private boolean mStopFlag = false;
    private boolean mRecording = false;
    private int mBufSize;

    private RecordThread mRecordThread;
    private RecordListener mListener;


    public PcmRecorder(int audioSource, int sampleRate, int channelCnt) {
        int channelConfig = channelCnt == 1 ? AudioFormat.CHANNEL_CONFIGURATION_MONO : AudioFormat.CHANNEL_CONFIGURATION_STEREO;
        int minBufSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, AUDIO_FORMAT);
        mBufSize = minBufSize;
        int bufferSizeInBytes = minBufSize;
        Log.i(TAG, "PcmRecorder: bufferSizeInBytes " + bufferSizeInBytes);
        mAudioRecord = new AudioRecord(audioSource, sampleRate, channelConfig, AUDIO_FORMAT, bufferSizeInBytes);
        Log.d(TAG, "state: " + mAudioRecord.getState());
    }

    public void setRecordListener(RecordListener listener) {
        mListener = listener;
    }

    public void start() {
        Log.d(TAG, "onStartRecord");

        mAudioRecord.startRecording();
        mRecording = true;
        mRecordThread = new RecordThread("RecordThread");
        mRecordThread.start();
        if (mListener != null) {
            mListener.onStartRecord();
        } else {
            Log.w(TAG, "start: mListener is null");
        }
    }

    public void stop() {
        Log.d(TAG, "stopRecord");
        mStopFlag = true;
        mRecording = false;
        try {
            mRecordThread.join();
        } catch (InterruptedException e) {
            Log.d(TAG, "InterruptedException " + e.getMessage());
        } finally {
            if (mListener != null) {
                mListener.onStopRecord();
            } else {
                Log.d(TAG, "stop: mListener is null");
            }
            mAudioRecord.stop();
            mAudioRecord.release();
        }
        mListener = null;
    }

    public boolean isRecording() {
        return mRecording;
    }


    class RecordThread extends Thread {

        public RecordThread(String name) {
            super(name);
        }

        @Override
        public void run() {
            Log.v(TAG, "recording thread run");
            Process.setThreadPriority(Process.THREAD_PRIORITY_URGENT_AUDIO);

            if (mAudioRecord.getState() == AudioRecord.STATE_UNINITIALIZED) {
                Log.d(TAG, "unInit");
                return;
            }

            byte[] buffer = new byte[mBufSize];
            while (!mStopFlag) {
                int len = mAudioRecord.read(buffer, 0, buffer.length);
                if (len != mBufSize) {
                    Log.e(TAG, "record read error:" + len);
                }
                if (mListener != null) {
                    mListener.onRecordData(buffer);
                    continue;
                }
            }
            Log.v(TAG, "recording  thread end");
        }
    }

}
