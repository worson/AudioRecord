package com.app.audio.record;

import android.Manifest.permission;
import android.media.MediaRecorder.AudioSource;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.lib.audio.player.IPlayerCallback;
import com.lib.audio.player.NativePlayer;
import com.lib.audio.record.PcmRecorder;
import com.lib.audio.record.RecordListener;
import com.lib.audio.wav.PcmUtil;
import com.lib.audio.wav.WavHeader;
import com.lib.common.androidbase.global.GlobalContext;
import com.lib.common.androidbase.task.HandlerUtil;
import com.lib.common.androidbase.utils.PermissionUtil;
import com.lib.common.io.string.Strings;
import com.worson.lib.log.L;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PcmRecorderActivity extends AppCompatActivity {

    private final static String TAG = "PcmRecorderActivityTag";

    private PcmRecorder mRecorder;

    private EditText et_file_prefix;

    private RadioGroup rg_frequecy;
    private RadioGroup rg_audio_source;

    private RadioButton rb_channel_single;
    private RadioButton rb_channel_dual;

    private RadioButton rb_file_wav;
    private RadioButton rb_file_pcm;

    private Button bt_delete;
    private Button bt_play;
    private Button bt_recorder;
    private TextView tv_tips;

    private FileOutputStream mOutputStream;
    private String mRecordFileDir = GlobalContext.get().getFilesDir()+"/record";
//    private String mRecordFileDir = Environment.getExternalStorageDirectory().getAbsolutePath();
    private File mTempRecordFile = new File(mRecordFileDir + "/record.pcm");
    private File mLastRecordFile = null;


    private Integer mPlayingId = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pcm_recorder);
        setTitle("录音机");

        PermissionUtil.requestPermissions(this, 0, new String[]{permission.RECORD_AUDIO, permission.WRITE_EXTERNAL_STORAGE, permission.READ_EXTERNAL_STORAGE});

        L.i(TAG, "onCreate: mRecordFileDir "+mRecordFileDir);
        et_file_prefix = findViewById(R.id.et_file_prefix);
        et_file_prefix.setFocusableInTouchMode(false);

        rg_audio_source = findViewById(R.id.rg_audio_source);

        rg_frequecy = findViewById(R.id.rg_frequecy);
        rb_channel_single = findViewById(R.id.rb_channel_single);
        rb_channel_dual = findViewById(R.id.rb_channel_dual);
        rb_file_wav = findViewById(R.id.rb_file_wav);
        rb_file_pcm = findViewById(R.id.rb_file_pcm);
        bt_recorder = findViewById(R.id.bt_recorder);
        bt_play = findViewById(R.id.bt_play);
        bt_delete = findViewById(R.id.bt_delete);
        tv_tips = findViewById(R.id.tv_tips);

        bt_play.setVisibility(View.INVISIBLE);

        bt_play.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPlayingId != null) {
                    stopPlay();
                } else {
                    startPlay();
                }
            }
        });

        bt_recorder.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRecorder != null) {
                    stopRecord();
                } else {
                    startRecord();
                }
            }
        });

        bt_delete.setVisibility(View.INVISIBLE);
        bt_delete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteRecord();
            }
        });

        checkEnvirement();
    }

    private void deleteRecord() {
        bt_delete.setVisibility(View.INVISIBLE);
        bt_play.setVisibility(View.INVISIBLE);
        if (mLastRecordFile.exists()) {
            mLastRecordFile.delete();
            mLastRecordFile = null;
        }
    }

    private void stopPlay() {
        if (mPlayingId != null) {
            NativePlayer.getInstance().cancel(mPlayingId);
            mPlayingId = null;
        }
        bt_play.setText("开始播放");
    }

    private void startPlay() {
        if (mLastRecordFile == null) {
            return;
        }
        mPlayingId = NativePlayer.getInstance().play(mLastRecordFile, new IPlayerCallback() {
            @Override
            public void onStart() {
                bt_play.setText("暂停播放");
            }

            @Override
            public void onSuccess() {

            }

            @Override
            public void onEnd() {
                bt_play.setText("开始播放");
                mPlayingId = null;
            }

            @Override
            public void onError() {
                mPlayingId = null;
            }
        });
        bt_play.setText("暂停播放");
    }

    private int getAudioFrequecy() {
        switch (rg_frequecy.getCheckedRadioButtonId()) {
            case R.id.rb_frequcy_16k:
                return 16000;
            case R.id.rb_frequcy_32k:
                return 32000;
            case R.id.rb_frequcy_48k:
                return 48000;
            case R.id.rb_frequcy_192k:
                return 192000;
        }
        return 16000;
    }

    private int getAudioSource() {
        switch (rg_audio_source.getCheckedRadioButtonId()) {
            case R.id.rb_source_default:
                return AudioSource.MIC;
            case R.id.rb_source_communication:
                return AudioSource.VOICE_COMMUNICATION;
            case R.id.rb_source_recognition:
                return AudioSource.VOICE_RECOGNITION;
            case R.id.rb_source_unprocessed:
                return AudioSource.UNPROCESSED;
        }
        return AudioSource.MIC;
    }

    private void checkEnvirement() {
        File workDir = new File(mRecordFileDir);
        if (!workDir.exists()) {
            workDir.mkdir();
        }
        if (!mTempRecordFile.getParentFile().exists()) {
            mTempRecordFile.getParentFile().mkdirs();
        }
    }

    private RecordListener mRecordListener = new RecordListener() {
        @Override
        public void onStartRecord() {
            L.i(TAG, "onStartRecord: ");
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
            L.i(TAG, "onStopRecord: ");
            HandlerUtil.postInMainThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        mOutputStream.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String prefix = et_file_prefix.getText().toString();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH___mm_ss");
                    Date date = new Date();
                    String fileName = String.format((mRecordFileDir + "/%s_" + sdf.format(date)),
                        Strings.notEmpty(prefix) ? prefix : (rb_file_pcm.isChecked() ? "pcm" : "wav"));
                    if (rb_file_pcm.isChecked()) {
                        fileName = fileName + ".pcm";
                        mTempRecordFile.renameTo(new File(fileName));
                    } else {
                        fileName = fileName + ".wav";
                        PcmUtil.toWav(mTempRecordFile.getAbsolutePath(), fileName,new WavHeader(getAudioFrequecy(),rb_channel_dual.isChecked() ? 2 : 1
                            , 16));
                    }
                    if (!rb_file_pcm.isChecked()) {
                        mLastRecordFile = new File(fileName);
                        bt_play.setVisibility(View.VISIBLE);
                        bt_delete.setVisibility(View.VISIBLE);
                    }
                    tv_tips.setText("录音保存在" + fileName);
                }
            });
        }
    };

    private void startRecord() {
        L.i(TAG, "startRecord: ");
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

    private void stopRecord() {
        L.i(TAG, "stopRecord: ");
        if (mRecorder != null) {
            mRecorder.stop();
            mRecorder = null;
        }
        bt_recorder.setText("开始录音");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
        String[] permission,
        int[] grantResults) {
    }
}
