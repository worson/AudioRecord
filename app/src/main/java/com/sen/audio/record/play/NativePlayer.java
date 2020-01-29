package com.sen.audio.record.play;

import android.content.res.AssetFileDescriptor;
import android.media.AudioAttributes.Builder;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Build.VERSION;
import androidx.annotation.RequiresApi;
import com.sen.audio.record.utils.AILog;
import com.sen.audio.record.utils.GlobalContext;
import com.sen.audio.record.utils.HandlerUtil;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 说明:
 *
 * @author wangshengxing  01.29 2020
 */
public class NativePlayer {

    private static final String TAG = "NativePlayer";
    private MediaPlayer mPlayer;
    private Queue<PlayerTask> mTaskQueue;
    private int mCurrentId;
    private PlayerTask mCurrentTask;
    private Runnable mPollTask;

    public static NativePlayer getInstance() {
        return NativePlayer.Holder.INSTANCE;
    }

    private NativePlayer() {
        this.mCurrentId = -1;
        this.mCurrentTask = null;
        this.mPollTask = new Runnable() {
            @RequiresApi(
                api = 24
            )
            public void run() {
                NativePlayer.this.poll();
            }
        };
        this.mTaskQueue = new ConcurrentLinkedQueue();
    }

    private MediaPlayer getPlayer() {
        if (null != this.mPlayer) {
            this.mPlayer.reset();
            return this.mPlayer;
        } else {
            MediaPlayer mediaPlayer = new MediaPlayer();
            return mediaPlayer;
        }
    }

    private void releasePlayer() {
        AILog.i("NativePlayer", "release player");
        this.mCurrentTask = null;
        this.mCurrentId = -1;
        if (null != this.mPlayer) {
            this.mPlayer.release();
            this.mPlayer = null;
        }

    }

    private void poll() {
        AILog.i("NativePlayer", "poll task");
        if (null != this.mCurrentTask) {
            AILog.i("NativePlayer", "current task isn't null!!!");
        } else {
            final PlayerTask playerTask = (PlayerTask) this.mTaskQueue.poll();
            if (null == playerTask) {
                this.releasePlayer();
                this.mCurrentId = -1;
                AILog.i("NativePlayer", "empty queue!!!");
            } else {
                this.mCurrentTask = playerTask;
                this.mCurrentId = playerTask.getId();
                AILog.d("NativePlayer", playerTask);
                this.mPlayer = this.getPlayer();
                this.mPlayer.reset();
                if (VERSION.SDK_INT >= 24) {
                    this.mPlayer.setAudioAttributes((new Builder()).setUsage(1).setContentType(2).build());
                } else {
                    this.mPlayer.setAudioStreamType(3);
                }

                try {
                    switch (playerTask.getType()) {
                        case 0:
                            this.mPlayer.setDataSource(playerTask.getFile().getAbsolutePath());
                            break;
                        case 1:
                            this.mPlayer.setDataSource(playerTask.getFileDescriptor());
                            break;
                        case 2:
                            this.mPlayer.setDataSource(GlobalContext.get(), playerTask.getUri());
                    }
                } catch (IOException var3) {
                    var3.printStackTrace();
                    this.mCurrentTask = null;
                    this.mCurrentId = -1;
                    if (null != playerTask.getCallback()) {
                        playerTask.getCallback().onError();
                        playerTask.getCallback().onEnd();
                    }

                    return;
                }

                this.mPlayer.setOnPreparedListener(new OnPreparedListener() {
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        AILog.d("NativePlayer", "prepared " + NativePlayer.this.mCurrentId);
                        NativePlayer.this.mPlayer.start();
                        if (null != playerTask.getCallback()) {
                            playerTask.getCallback().onStart();
                        }

                    }
                });
                this.mPlayer.setOnErrorListener(new OnErrorListener() {
                    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                        AILog.d("NativePlayer", "error:" + NativePlayer.this.mCurrentId + " " + i + " " + i1);
                        if (null != playerTask.getCallback()) {
                            playerTask.getCallback().onError();
                            playerTask.getCallback().onEnd();
                        }

                        NativePlayer.this.mCurrentTask = null;
                        NativePlayer.this.mCurrentId = -1;
                        HandlerUtil.postInMainThread(NativePlayer.this.mPollTask);
                        return false;
                    }
                });
                this.mPlayer.setOnCompletionListener(new OnCompletionListener() {
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        AILog.d("NativePlayer", "completion " + NativePlayer.this.mCurrentId);
                        if (null != playerTask.getCallback()) {
                            playerTask.getCallback().onSuccess();
                            playerTask.getCallback().onEnd();
                        }

                        NativePlayer.this.mCurrentTask = null;
                        NativePlayer.this.mCurrentId = -1;
                        HandlerUtil.postInMainThread(NativePlayer.this.mPollTask);
                    }
                });
                this.mPlayer.prepareAsync();
            }
        }
    }

    public int play(File file, IPlayerCallback callback) {
        int nextId = NativePlayer.IdUtil.getNextId();
        AILog.i("NativePlayer", "create player task:" + nextId);
        PlayerTask playerTask = new PlayerTask(0, nextId, callback);
        playerTask.setFile(file);
        this.mTaskQueue.add(playerTask);
        HandlerUtil.postInMainThread(this.mPollTask);
        return nextId;
    }

    public int play(AssetFileDescriptor fileDescriptor, IPlayerCallback callback) {
        int nextId = NativePlayer.IdUtil.getNextId();
        AILog.i("NativePlayer", "create player task:" + nextId);
        PlayerTask playerTask = new PlayerTask(1, nextId, callback);
        playerTask.setAssetFileDescriptor(fileDescriptor);
        this.mTaskQueue.add(playerTask);
        HandlerUtil.postInMainThread(this.mPollTask);
        return nextId;
    }

    public int play(Uri uri, IPlayerCallback callback) {
        int nextId = NativePlayer.IdUtil.getNextId();
        AILog.i("NativePlayer", "create player task:" + nextId);
        PlayerTask playerTask = new PlayerTask(2, nextId, callback);
        playerTask.setUri(uri);
        this.mTaskQueue.add(playerTask);
        HandlerUtil.postInMainThread(this.mPollTask);
        return nextId;
    }

    public void cancel(final int id) {
        AILog.i("NativePlayer", "cancel :" + id);
        if (id != -1) {
            HandlerUtil.postInMainThread(new Runnable() {
                @RequiresApi(
                    api = 24
                )
                public void run() {
                    if (NativePlayer.this.mCurrentId == id) {
                        if (null != NativePlayer.this.mCurrentTask && null != NativePlayer.this.mCurrentTask.getCallback()) {
                            NativePlayer.this.mCurrentTask.getCallback().onEnd();
                        }

                        if (null != NativePlayer.this.mPlayer) {
                            NativePlayer.this.mPlayer.pause();
                        }

                        NativePlayer.this.mCurrentTask = null;
                        NativePlayer.this.mCurrentId = -1;
                        NativePlayer.this.poll();
                    } else {
                        Iterator iterator = NativePlayer.this.mTaskQueue.iterator();

                        while (iterator.hasNext()) {
                            PlayerTask playerTask = (PlayerTask) iterator.next();
                            if (playerTask.getId() == id) {
                                iterator.remove();
                                if (null != playerTask.getCallback()) {
                                    playerTask.getCallback().onEnd();
                                }
                                break;
                            }
                        }

                    }
                }
            });
        }
    }

    public int play(IPlayerCallback callback) {
        return 0;
    }

    public int write(byte[] data, int offsetInBytes, int sizeInBytes) {
        return 0;
    }

    public int pause() {
        if (this.mPlayer != null) {
            this.mPlayer.pause();
        }

        return 0;
    }

    public int continuePlay() {
        if (this.mPlayer != null) {
            this.mPlayer.start();
        }

        return 0;
    }

    public void seekToPlay(int progress) {
        if (this.mPlayer != null) {
            this.mPlayer.seekTo(progress);
        }

    }

    public int getCurrentPosition() {
        int position = 0;
        if (this.mPlayer != null) {
            position = this.mPlayer.getCurrentPosition();
        }

        return position;
    }

    private static class IdUtil {

        private static int _id = 1;

        private IdUtil() {
        }

        private static int getNextId() {
            return _id++;
        }
    }

    private static class Holder {

        private static NativePlayer INSTANCE = new NativePlayer();

        private Holder() {
        }
    }

    /**
     * 说明:
     *
     * @author wangshengxing  01.29 2020
     */
    public static class PlayerTask {

        public static final int TYPE_FILE = 0;
        public static final int TYPE_ASSET = 1;
        public static final int TYPE_URI = 2;
        private final int type;
        private final int id;
        private final IPlayerCallback callback;
        private File file;
        private AssetFileDescriptor fileDescriptor;
        private Uri mUri;

        public PlayerTask(int type, int id, IPlayerCallback callback) {
            this.type = type;
            this.id = id;
            this.callback = callback;
        }

        public int getType() {
            return this.type;
        }

        public File getFile() {
            return this.file;
        }

        public AssetFileDescriptor getFileDescriptor() {
            return this.fileDescriptor;
        }

        public void setAssetFileDescriptor(AssetFileDescriptor fileDescriptor) {
            this.fileDescriptor = fileDescriptor;
        }

        public void setFile(File file) {
            this.file = file;
        }

        public int getId() {
            return this.id;
        }

        public Uri getUri() {
            return this.mUri;
        }

        public void setUri(Uri uri) {
            this.mUri = uri;
        }

        public IPlayerCallback getCallback() {
            return this.callback;
        }

        public String toString() {
            return "PlayerTask{type=" + this.type + ", id=" + this.id + ", file=" + this.file + ", fileDescriptor=" + this.fileDescriptor + ", mUri="
                + this.mUri + '}';
        }
    }
}
