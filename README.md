# AudioRecord
本工程实现pcm多配置(采样率、文件格式可选)的录音和播放工具，可用户音频的录制。

![img/picture_2020_01_29_14___52_41.png]

# 录音参数

在安卓应用中，使用AudioRecord可实现更灵活的录音配置，AudioRecord录制的是原始的pcm数据。
AudioRecord的构造方法如下：
```
AudioRecord(int audioSource, int sampleRateInHz, int channelConfig, int audioFormat, int bufferSizeInBytes)
```

audioSource: 音频录制的音原，具体如下
```
MediaRecorder.AudioSource.CAMCORDER 
设定录音来源于同方向的相机麦克风相同，若相机无内置相机或无法识别，则使用预设的麦克风
MediaRecorder.AudioSource.DEFAULT  默认音频源
MediaRecorder.AudioSource.MIC
设定录音来源为主麦克风。
MediaRecorder.AudioSource.VOICE_CALL
设定录音来源为语音拨出的语音与对方说话的声音
MediaRecorder.AudioSource.VOICE_COMMUNICATION
摄像头旁边的麦克风
MediaRecorder.AudioSource.VOICE_DOWNLINK
下行声音
MediaRecorder.AudioSource.VOICE_RECOGNITION
语音识别
MediaRecorder.AudioSource.VOICE_UPLINK
上行声音

```
sampleRateInHz:音频采样率，即可每秒中采集多少个音频数据

channelConfig: 录音通道，单通道为 AudioFormat.CHANNEL_CONFIGURATION_MONO，双通道为AudioFormat.CHANNEL_CONFIGURATION_STEREO

audioFormat: 音频每个采样点的位数，即音频的精度，通道选用AudioFormat.ENCODING_PCM_16BIT即pcm 16位即可

bufferSizeInBytes: 音频数据写入缓冲区的总数，通过 AudioRecord.getMinBufferSize 获取最小的缓冲区。

# 关于音频数据量的计算
对于采样率为16k位深有16bit的录制参数，每秒钟的byte数为:16000*2=32000，即每分钟为:32000*60 byte/min= 1.875 MB/min的数据量


# 录音流程

## 创建录音机
```
public PcmRecorder(int audioSource, int sampleRate, int channelCnt, Context context, RecordListener listener) {
        mListener = listener;
        int channelConfig = channelCnt == 1 ? AudioFormat.CHANNEL_CONFIGURATION_MONO : AudioFormat.CHANNEL_CONFIGURATION_STEREO;
        int minBufSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, AUDIO_FORMAT);
        mBufSize = minBufSize;
        int bufferSizeInBytes = minBufSize;
        AILog.i(TAG, "PcmRecorder: bufferSizeInBytes " + bufferSizeInBytes);
        mAudioRecord = new AudioRecord(audioSource, sampleRate, channelConfig, AUDIO_FORMAT, bufferSizeInBytes);
        AILog.d(TAG, "state: " + mAudioRecord.getState());
    }
```


## 开始录音
开始录音，并创建数据读取线程
```
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

```


## 读取音频数据
不断读取 Buffer 中声音数据，并回调上层应用处理
```
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
```

## 停止录音
停止录音并释放相关资源
```
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
```

## 上层应用调用

```
private void startRecord() {
    AILog.i(TAG, "startRecord: ");
    checkEnvirement();
    if (rb_channel_dual.isChecked()) {
        mRecorder = new PcmRecorder(getAudioSource(), getAudioFrequecy(), 2);
    } else {
        mRecorder = new PcmRecorder(getAudioSource(), getAudioFrequecy(), 1);
    }
    try {
        mOutputStream = new FileOutputStream(mTempRecordFile);
        mRecorder.setRecordListener(mRecordListener);
        mRecorder.start();
        tv_tips.setText("正在录音...");
        bt_recorder.setText("结束录音");
        bt_play.setVisibility(View.INVISIBLE);
        bt_delete.setVisibility(View.INVISIBLE);
    } catch (FileNotFoundException e) {
        e.printStackTrace();
        tv_tips.setText("录音启动失败...");
    }

}

private RecordListener mRecordListener = new RecordListener() {
    @Override
    public void onStartRecord() {
        AILog.i(TAG, "onStartRecord: ");
    }

    @Override
    public void onRecordData(byte[] bytes) {
        try {
            mOutputStream.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStopRecord() {
        AILog.i(TAG, "onStopRecord: ");
        //保存文件
    }
};
```