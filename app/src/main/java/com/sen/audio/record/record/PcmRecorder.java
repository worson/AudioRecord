package com.sen.audio.record.record;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.os.Process;
import com.sen.audio.record.utils.AILog;

/**
 * 说明:
 *
 * @author wangshengxing  01.29 2020
 */
public class PcmRecorder {

    private static final String TAG = "PcmRecorder";

    public static final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
    public static final int AUDIO_FORMAT_IN_BYTE = 2;

    private AudioRecord mAudioRecord;
    private boolean mStopFlag = false;
    private boolean mRecording = false;
    private int mBufSize;

    private RecordThread mRecordThread;
    private RecordListener mListener;


    public PcmRecorder(int audioSource, int sampleRate, int channelCnt) {
        this(audioSource, sampleRate, channelCnt, null, null);
    }

    public PcmRecorder(int audioSource, int sampleRate, int channelCnt, Context context, RecordListener listener) {
        mListener = listener;
        int channelConfig = channelCnt == 1 ? AudioFormat.CHANNEL_CONFIGURATION_MONO : AudioFormat.CHANNEL_CONFIGURATION_STEREO;
        int minBufSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, AUDIO_FORMAT);
//        mBufSize = sampleRate * 20 / 1000 * channelCnt * AUDIO_FORMAT_IN_BYTE;
        mBufSize = 400 * channelCnt;
        int bufferSizeInBytes = 8 * minBufSize;
        AILog.i(TAG, "PcmRecorder: bufferSizeInBytes " + bufferSizeInBytes);
        mAudioRecord = new AudioRecord(audioSource, sampleRate, channelConfig, AUDIO_FORMAT, bufferSizeInBytes);
        AILog.d(TAG, "state: " + mAudioRecord.getState());
    }

    public void setRecordListener(RecordListener listener) {
        mListener = listener;
    }


    public void start() {
        AILog.d(TAG, "onStartRecord");

        mAudioRecord.startRecording();
        mRecording = true;
        mRecordThread = new RecordThread("RecordThread");
        mRecordThread.start();
        if (mListener != null) {
            mListener.onStartRecord();
        } else {
            AILog.w(TAG, "start: mListener is null");
        }
    }

    public void stop() {
        AILog.d(TAG, "stopRecord");
        mStopFlag = true;
        mRecording = false;
        try {
            mRecordThread.join();
        } catch (InterruptedException e) {
            AILog.d(TAG, "InterruptedException " + e.getMessage());
        } finally {
            if (mListener != null) {
                mListener.onStopRecord();
            } else {
                AILog.d(TAG, "stop: mListener is null");
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
            AILog.v(TAG, "thread run");
            Process.setThreadPriority(Process.THREAD_PRIORITY_URGENT_AUDIO);

            if (mAudioRecord.getState() == AudioRecord.STATE_UNINITIALIZED) {
                AILog.d(TAG, "unInit");
                return;
            }

            byte[] buffer = new byte[mBufSize];
            while (!mStopFlag) {
                int len = mAudioRecord.read(buffer, 0, buffer.length);
                if (len != mBufSize) {
                    AILog.e(TAG, "record read error:" + len);
                }
                if (mListener != null) {
                    mListener.onRecordData(buffer);
                    continue;
                }
            }
            AILog.v(TAG, "thread end");
        }
    }

}
