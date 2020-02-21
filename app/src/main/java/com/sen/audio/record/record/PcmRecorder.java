package com.sen.audio.record.record;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.os.Process;
import com.lib.common.dlog.DLog;

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
        mBufSize = minBufSize;
        int bufferSizeInBytes = minBufSize;
        DLog.i(TAG, "PcmRecorder: bufferSizeInBytes " + bufferSizeInBytes);
        mAudioRecord = new AudioRecord(audioSource, sampleRate, channelConfig, AUDIO_FORMAT, bufferSizeInBytes);
        DLog.d(TAG, "state: " + mAudioRecord.getState());
    }

    public void setRecordListener(RecordListener listener) {
        mListener = listener;
    }


    public void start() {
        DLog.d(TAG, "onStartRecord");

        mAudioRecord.startRecording();
        mRecording = true;
        mRecordThread = new RecordThread("RecordThread");
        mRecordThread.start();
        if (mListener != null) {
            mListener.onStartRecord();
        } else {
            DLog.w(TAG, "start: mListener is null");
        }
    }

    public void stop() {
        DLog.d(TAG, "stopRecord");
        mStopFlag = true;
        mRecording = false;
        try {
            mRecordThread.join();
        } catch (InterruptedException e) {
            DLog.d(TAG, "InterruptedException " + e.getMessage());
        } finally {
            if (mListener != null) {
                mListener.onStopRecord();
            } else {
                DLog.d(TAG, "stop: mListener is null");
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
            DLog.v(TAG, "thread run");
            Process.setThreadPriority(Process.THREAD_PRIORITY_URGENT_AUDIO);

            if (mAudioRecord.getState() == AudioRecord.STATE_UNINITIALIZED) {
                DLog.d(TAG, "unInit");
                return;
            }

            byte[] buffer = new byte[mBufSize];
            while (!mStopFlag) {
                int len = mAudioRecord.read(buffer, 0, buffer.length);
                if (len != mBufSize) {
                    DLog.e(TAG, "record read error:" + len);
                }
                if (mListener != null) {
                    mListener.onRecordData(buffer);
                    continue;
                }
            }
            DLog.v(TAG, "thread end");
        }
    }

}
