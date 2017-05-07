// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.mediaclient.service.player;

import com.netflix.mediaclient.event.nrdp.media.UpdatePts;
import com.netflix.mediaclient.event.UIEvent;
import com.netflix.mediaclient.util.AndroidUtils;
import com.netflix.mediaclient.media.Subtitle;
import java.nio.ByteBuffer;
import com.netflix.mediaclient.media.AudioSource;
import com.netflix.mediaclient.media.AudioSubtitleDefaultOrderInfo;
import java.util.concurrent.Executors;
import com.netflix.mediaclient.media.MediaPlayerHelperFactory;
import com.netflix.mediaclient.service.configuration.PlayerTypeFactory;
import com.netflix.mediaclient.javabridge.ui.EventListener;
import com.netflix.mediaclient.service.configuration.BitrateRangeFactory;
import android.media.AudioManager;
import com.netflix.mediaclient.media.PlayoutMetadata;
import com.netflix.mediaclient.service.player.subtitles.SubtitleScreen;
import com.netflix.mediaclient.service.player.subtitles.TextStyle;
import com.netflix.mediaclient.android.app.BackgroundTask;
import com.netflix.mediaclient.event.nrdp.media.NccpActionId;
import java.util.Iterator;
import android.net.NetworkInfo;
import com.netflix.mediaclient.util.ConnectivityUtils;
import android.os.Handler;
import com.netflix.mediaclient.service.NetflixService;
import com.netflix.mediaclient.event.nrdp.media.Warning;
import com.netflix.mediaclient.event.nrdp.media.Error;
import com.netflix.mediaclient.event.nrdp.media.BufferRange;
import com.netflix.mediaclient.event.nrdp.media.Statechanged;
import com.netflix.mediaclient.event.nrdp.media.AudioTrackChanged;
import com.netflix.mediaclient.event.nrdp.media.SubtitleData;
import com.netflix.mediaclient.event.nrdp.media.ShowSubtitle;
import com.netflix.mediaclient.event.nrdp.media.RemoveSubtitle;
import com.netflix.mediaclient.event.nrdp.media.Buffering;
import com.netflix.mediaclient.event.nrdp.media.GenericMediaEvent;
import android.content.Intent;
import android.content.Context;
import com.netflix.mediaclient.service.user.SimpleUserAgentWebCallback;
import com.netflix.mediaclient.javabridge.invoke.media.Open;
import com.netflix.mediaclient.Log;
import java.util.TimerTask;
import java.util.ArrayList;
import com.netflix.mediaclient.service.user.UserAgentWebCallback;
import android.content.BroadcastReceiver;
import android.os.PowerManager$WakeLock;
import java.util.Timer;
import android.view.SurfaceHolder;
import android.view.Surface;
import com.netflix.mediaclient.service.player.subtitles.SubtitleParser;
import com.netflix.mediaclient.service.configuration.SubtitleConfiguration;
import com.netflix.mediaclient.media.PlayerType;
import java.util.List;
import java.util.concurrent.ExecutorService;
import com.netflix.mediaclient.ui.common.PlayContext;
import com.netflix.mediaclient.javabridge.ui.Nrdp;
import com.netflix.mediaclient.javabridge.ui.IMedia;
import com.netflix.mediaclient.media.JPlayer.JPlayer;
import com.netflix.mediaclient.media.MediaPlayerHelper;
import com.netflix.mediaclient.media.BifManager;
import com.netflix.mediaclient.event.nrdp.media.NccpError;
import com.netflix.mediaclient.service.configuration.ConfigurationAgent;
import com.netflix.mediaclient.servicemgr.IPlayer;
import com.netflix.mediaclient.service.ServiceAgent;

public class PlayerAgent extends ServiceAgent implements IPlayer, ConfigAgentListener
{
    private static final int BANDWITH_CHECK_INTERVAL = 30000;
    public static final int CREATED = 1;
    private static final int EOS_DELTA = 10000;
    public static final int IN_PLAYBACK = 3;
    private static final int MAX_CELLULAR_DOWNLOAD_LIMIT = 90000;
    private static final int MAX_WIFI_DOWNLOAD_LIMIT = 300000;
    private static final int NETWORK_CHECK_INTERVAL = 1000;
    private static final int NETWORK_CHECK_TIMEOUT = 30000;
    public static final int PLAYBACK_ENDED = 4;
    public static final int PLAYBACK_INITIATED = 2;
    private static final int STATE_CLOSED = 4;
    private static final int STATE_CREATED = -1;
    private static final int STATE_OPENING = 0;
    private static final int STATE_PAUSED = 2;
    private static final int STATE_PLAYING = 1;
    private static final int STATE_PRECLOSE = 8;
    private static final int STATE_PREOPEN = 5;
    private static final int STATE_PREPLAY = 6;
    private static final int STATE_PRESTOP = 7;
    private static final int STATE_STOPPED = 3;
    private static final String TAG;
    private static final int TimeToWaitBeforeShutdown = 30000;
    private static final int TimeToWaitBeforeUnmute = 10000;
    private boolean ignoreErrorsWhileActionId12IsProcessed;
    private boolean inPlaybackSession;
    private NccpError mActionId12Error;
    private BifManager mBifManager;
    private int mBitrateCap;
    private long mBookmark;
    private boolean mBufferingCompleted;
    private CloseTimeoutTask mCloseTimeoutTask;
    private boolean mForcedRebuffer;
    private MediaPlayerHelper mHelper;
    private boolean mInPlayback;
    private volatile JPlayer.JplayerListener mJPlayerListener;
    private long mLastBandwidthCheck;
    private IMedia mMedia;
    private GenericMediaEventListener mMediaEventListener;
    private long mMovieId;
    private final int mNetworkProfile;
    private Nrdp mNrdp;
    private PlayContext mPlayContext;
    private PlayParamsReceiver mPlayParamsRecvr;
    private ExecutorService mPlayerExecutor;
    private final List<PlayerListener> mPlayerListeners;
    private PlayerType mPlayerType;
    private boolean mScreenOnWhilePlaying;
    private StartPlayTimeoutTask mStartPlayTimeoutTask;
    private volatile int mState;
    private boolean mStayAwake;
    private SubtitleConfiguration mSubtitleConfiguration;
    private SubtitleParser mSubtitles;
    private Surface mSurface;
    private SurfaceHolder mSurfaceHolder;
    private Timer mTimer;
    private final PowerManager$WakeLock mWakeLock;
    private boolean muted;
    private final Runnable onCloseRunnable;
    private final Runnable onOpenRunnable;
    private final Runnable onPlayRunnable;
    private final Runnable onSeekRunnable;
    private NccpError pendingError;
    private final BroadcastReceiver playerChangesReceiver;
    private boolean preparedCompleted;
    private int prevEndPosition;
    private int seekedToPosition;
    private boolean seeking;
    private boolean splashScreenRemoved;
    private boolean toCancelOpen;
    private boolean toOpenAfterClose;
    private boolean toPlayAfterStop;
    private boolean validPtsRecieved;
    private final UserAgentWebCallback webClientCallback;
    
    static {
        TAG = PlayerAgent.class.getSimpleName();
    }
    
    public PlayerAgent() {
        this.mPlayerListeners = new ArrayList<PlayerListener>();
        this.mWakeLock = null;
        this.mBitrateCap = -1;
        this.seeking = false;
        this.validPtsRecieved = false;
        this.preparedCompleted = false;
        this.mNetworkProfile = 2;
        this.splashScreenRemoved = false;
        this.mBufferingCompleted = false;
        this.ignoreErrorsWhileActionId12IsProcessed = false;
        this.mState = -1;
        this.toPlayAfterStop = false;
        this.toOpenAfterClose = false;
        this.toCancelOpen = false;
        this.onOpenRunnable = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    Label_0544: {
                    Label_0519:
                        while (true) {
                            synchronized (PlayerAgent.this) {
                                PlayerAgent.this.mMedia.reset();
                                PlayerAgent.this.prevEndPosition = -1;
                                PlayerAgent.this.validPtsRecieved = false;
                                PlayerAgent.this.mInPlayback = false;
                                PlayerAgent.this.inPlaybackSession = false;
                                PlayerAgent.this.preparedCompleted = false;
                                PlayerAgent.this.seekedToPosition = (int)(Object)Long.valueOf(PlayerAgent.this.mBookmark);
                                PlayerAgent.this.mBufferingCompleted = false;
                                PlayerAgent.this.pendingError = null;
                                if (PlayerAgent.this.mTimer != null) {
                                    PlayerAgent.this.mStartPlayTimeoutTask = new StartPlayTimeoutTask();
                                    PlayerAgent.this.mTimer.schedule(PlayerAgent.this.mStartPlayTimeoutTask, 30000L);
                                }
                                if (Log.isLoggable(PlayerAgent.TAG, 3)) {
                                    Log.d(PlayerAgent.TAG, "Player state is " + PlayerAgent.this.mState);
                                }
                                if (PlayerAgent.this.mState != 4 && PlayerAgent.this.mState != -1) {
                                    break Label_0544;
                                }
                                Log.d(PlayerAgent.TAG, "Player state was CLOSED or CREATED, cancel timeout task!");
                                PlayerAgent.this.mState = 5;
                                if (PlayerAgent.this.mStartPlayTimeoutTask != null) {
                                    final boolean cancel = PlayerAgent.this.mStartPlayTimeoutTask.cancel();
                                    if (Log.isLoggable(PlayerAgent.TAG, 3)) {
                                        Log.d(PlayerAgent.TAG, "Task was canceled " + cancel);
                                    }
                                }
                                else {
                                    Log.w(PlayerAgent.TAG, "Timer task was null!!!");
                                }
                                if (PlayerAgent.this.mTimer != null) {
                                    final int purge = PlayerAgent.this.mTimer.purge();
                                    if (Log.isLoggable(PlayerAgent.TAG, 3)) {
                                        Log.d(PlayerAgent.TAG, "Canceled tasks: " + purge);
                                    }
                                    PlayerAgent.this.reloadPlayer();
                                    PlayerAgent.this.mMedia.setStreamingQoe(PlayerAgent.this.getConfigurationAgent().getStreamingQoe());
                                    PlayerAgent.this.mMedia.open(PlayerAgent.this.mMovieId, PlayerAgent.this.mPlayContext, PlayerAgent.this.getCurrentNetType());
                                    PlayerAgent.this.toOpenAfterClose = false;
                                    final String value = PlayerAgent.this.getConfigurationAgent().getDeviceCategory().getValue();
                                    if (Open.NetType.wifi.equals(PlayerAgent.this.getCurrentNetType())) {
                                        Log.i(PlayerAgent.TAG, "Setting WifiApInfo");
                                        PlayerAgent.this.mMedia.setWifiApsInfo(PlayerAgent.this.getContext(), value, true);
                                        return;
                                    }
                                    break Label_0519;
                                }
                            }
                            Log.w(PlayerAgent.TAG, "Timer was null!!!");
                            continue;
                        }
                        final String s;
                        PlayerAgent.this.mMedia.setWifiApsInfo(PlayerAgent.this.getContext(), s, false);
                        return;
                    }
                    PlayerAgent.this.toOpenAfterClose = true;
                    Log.d(PlayerAgent.TAG, "invokeMethod(open) has to wait...");
                }
            }
        };
        this.onPlayRunnable = new Runnable() {
            @Override
            public void run() {
                synchronized (PlayerAgent.this) {
                    if (PlayerAgent.this.mState != 3) {
                        PlayerAgent.this.toPlayAfterStop = true;
                    }
                    else {
                        PlayerAgent.this.playWithBookmarkCheck();
                    }
                }
            }
        };
        this.onSeekRunnable = new Runnable() {
            @Override
            public void run() {
                synchronized (PlayerAgent.this) {
                    PlayerAgent.this.prevEndPosition = PlayerAgent.this.getCurrentPositionMs();
                    PlayerAgent.this.validPtsRecieved = false;
                    PlayerAgent.this.seeking = true;
                    PlayerAgent.this.mInPlayback = false;
                    final int duration = PlayerAgent.this.getDuration();
                    final int access$700 = PlayerAgent.this.seekedToPosition;
                    int n;
                    if (PlayerAgent.this.seekedToPosition + 10000 >= duration && duration > 0) {
                        Log.d(PlayerAgent.TAG, "seek to close to EOS, defaulting to 10 seconss before EOS.");
                        n = duration - 10000;
                    }
                    else {
                        n = access$700;
                        if (Log.isLoggable(PlayerAgent.TAG, 3)) {
                            Log.d(PlayerAgent.TAG, "seek to position " + PlayerAgent.this.seekedToPosition + ", duration " + duration);
                            n = access$700;
                        }
                    }
                    PlayerAgent.this.mMedia.seekTo(n, PlayerAgent.this.mForcedRebuffer);
                    PlayerAgent.this.seekedToPosition = n;
                    PlayerAgent.this.mBufferingCompleted = false;
                }
            }
        };
        this.onCloseRunnable = new Runnable() {
            @Override
            public void run() {
                synchronized (PlayerAgent.this) {
                    if (PlayerAgent.this.mStartPlayTimeoutTask != null) {
                        PlayerAgent.this.mStartPlayTimeoutTask.cancel();
                    }
                    if (PlayerAgent.this.mTimer != null) {
                        PlayerAgent.this.mTimer.purge();
                    }
                    PlayerAgent.this.toOpenAfterClose = false;
                    if (PlayerAgent.this.mState == 5 || PlayerAgent.this.mState == 0 || PlayerAgent.this.mState == 3) {
                        PlayerAgent.this.toCancelOpen = true;
                    }
                    if (PlayerAgent.this.mState == 4 || PlayerAgent.this.mState == 8) {
                        Log.d(PlayerAgent.TAG, "close() pending or already closed");
                        return;
                    }
                    PlayerAgent.this.close2();
                    // monitorexit(this.this$0)
                    if (PlayerAgent.this.mTimer != null) {
                        PlayerAgent.this.mCloseTimeoutTask = new CloseTimeoutTask();
                        PlayerAgent.this.mTimer.schedule(PlayerAgent.this.mCloseTimeoutTask, 10000L);
                    }
                }
            }
        };
        this.webClientCallback = new SimpleUserAgentWebCallback() {
            @Override
            public void onDummyWebCallDone(final int n) {
                PlayerAgent.this.ignoreErrorsWhileActionId12IsProcessed = false;
                if (n == 0) {
                    Log.d(PlayerAgent.TAG, "Dummy webcall completed with statusCode=" + n);
                    PlayerAgent.this.notifyPlayerListenersRestart();
                    return;
                }
                Log.e(PlayerAgent.TAG, "Dummy webcall completed  failed (skipping user info update) with statusCode=" + n);
                PlayerAgent.this.notifyPlayerListeners(PlayerAgent.this.mActionId12Error);
            }
        };
        this.muted = false;
        this.playerChangesReceiver = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                if (Log.isLoggable(PlayerAgent.TAG, 2)) {
                    Log.v(PlayerAgent.TAG, "Received intent " + intent);
                }
                final String action = intent.getAction();
                if ("com.netflix.mediaclient.intent.action.PLAYER_SUBTITLE_CONFIG_CHANGED".equals(action)) {
                    Log.d(PlayerAgent.TAG, "subtitle configuration is changed");
                    PlayerAgent.this.updateSubtitleSettingsFromQaLocalOverride(intent.getIntExtra("lookupType", -1));
                }
                else if (Log.isLoggable(PlayerAgent.TAG, 3)) {
                    Log.d(PlayerAgent.TAG, "We do not support action " + action);
                }
            }
        };
    }
    
    private void close2() {
        synchronized (this) {
            this.mState = 8;
            this.mMedia.close();
            this.mNrdp.getLog().flush();
        }
    }
    
    private SubtitleConfiguration findSubtitleConfiguration() {
        SubtitleConfiguration subtitleConfiguration = null;
        final ConfigurationAgentInterface configurationAgent = this.getConfigurationAgent();
        if (configurationAgent != null) {
            subtitleConfiguration = configurationAgent.getSubtitleConfiguration();
        }
        SubtitleConfiguration default1;
        if ((default1 = subtitleConfiguration) == null) {
            default1 = SubtitleConfiguration.DEFAULT;
        }
        return default1;
    }
    
    private Open.NetType getCurrentNetType() {
        final NetworkInfo activeNetworkInfo = ConnectivityUtils.getActiveNetworkInfo(this.getContext());
        if (activeNetworkInfo == null) {
            return null;
        }
        switch (activeNetworkInfo.getType()) {
            default: {
                return Open.NetType.mobile;
            }
            case 9: {
                return Open.NetType.wired;
            }
            case 1: {
                return Open.NetType.wifi;
            }
        }
    }
    
    private void handleAudioTrackChanged(final AudioTrackChanged audioTrackChanged) {
        Log.d(PlayerAgent.TAG, "MEDIA_AUDIO_CHANGE 53");
        final Iterator<PlayerListener> iterator = this.mPlayerListeners.iterator();
        while (iterator.hasNext()) {
            this.getMainHandler().post((Runnable)new Runnable() {
                final /* synthetic */ PlayerListener val$listener = iterator.next();
                
                @Override
                public void run() {
                    this.val$listener.onAudioChange(audioTrackChanged.getTrackIndex());
                }
            });
        }
    }
    
    private void handleBufferRange(final BufferRange bufferRange) {
        if (Log.isLoggable(PlayerAgent.TAG, 3)) {
            Log.d(PlayerAgent.TAG, "MEDIA_BANDWIDTH_UPDATE :" + bufferRange.getBandwidth());
        }
        final Iterator<PlayerListener> iterator = this.mPlayerListeners.iterator();
        while (iterator.hasNext()) {
            this.getMainHandler().post((Runnable)new Runnable() {
                final /* synthetic */ PlayerListener val$listener = iterator.next();
                
                @Override
                public void run() {
                    this.val$listener.onBandwidthChange(bufferRange.getBandwidth());
                }
            });
        }
    }
    
    private void handleBufferingComplete() {
        Log.d(PlayerAgent.TAG, "BUFFERING COMPLETE 100");
        this.mBufferingCompleted = true;
        if (this.mPlayerType != PlayerType.device9) {
            this.handlePlayback();
        }
    }
    
    private void handleBufferring(final Buffering buffering) {
        if (Log.isLoggable(PlayerAgent.TAG, 3)) {
            Log.d(PlayerAgent.TAG, "MEDIA_BANDWIDTH_UPDATE :" + buffering.getPercentage());
        }
        final Iterator<PlayerListener> iterator = this.mPlayerListeners.iterator();
        while (iterator.hasNext()) {
            this.getMainHandler().post((Runnable)new Runnable() {
                final /* synthetic */ PlayerListener val$listener = iterator.next();
                
                @Override
                public void run() {
                    this.val$listener.onBufferingUpdate(buffering.getPercentage());
                }
            });
        }
    }
    
    private void handleEndOfPlayback() {
        Log.d(PlayerAgent.TAG, "MEDIA_PLAYBACK_COMPLETE 2");
        final Iterator<PlayerListener> iterator = this.mPlayerListeners.iterator();
        while (iterator.hasNext()) {
            this.getMainHandler().post((Runnable)new Runnable() {
                final /* synthetic */ PlayerListener val$listener = iterator.next();
                
                @Override
                public void run() {
                    this.val$listener.onCompletion();
                }
            });
        }
    }
    
    private void handleError(final NccpError mActionId12Error) {
        Log.d(PlayerAgent.TAG, "Nccp error receieved");
        if (!this.ignoreErrorsWhileActionId12IsProcessed) {
            if (!(mActionId12Error instanceof NccpActionId)) {
                Log.w(PlayerAgent.TAG, "We will ignore received NccpNetworkingError/NetworkError, since if we need to handle it will be followed with action ID.");
            }
            else {
                final NccpActionId nccpActionId = (NccpActionId)mActionId12Error;
                if (nccpActionId.getActionId() == 11) {
                    Log.w(PlayerAgent.TAG, "ActionID 11 NFErr_MC_Abort Playback.");
                    this.notifyPlayerListeners(mActionId12Error);
                    return;
                }
                if (this.inPlaybackSession) {
                    Log.d(PlayerAgent.TAG, "We are in playback. Ignore all errors, except 11.");
                    if (Log.isLoggable(PlayerAgent.TAG, 3)) {
                        Log.d(PlayerAgent.TAG, "Error in Playback, being ignored " + nccpActionId);
                    }
                }
                else {
                    if (nccpActionId.getActionId() == 12) {
                        Log.w(PlayerAgent.TAG, "ActionID 12 NFErr_MC_StaleCredentials");
                        this.ignoreErrorsWhileActionId12IsProcessed = true;
                        this.mActionId12Error = mActionId12Error;
                        this.getUserAgent().doDummyWebCall(this.webClientCallback);
                        return;
                    }
                    Log.d(PlayerAgent.TAG, "Handle all errors except if they are for background events, such as logblob, ping, playdata or heartbeat...");
                    final String transaction = nccpActionId.getTransaction();
                    if ("heartbeat".equalsIgnoreCase(transaction) || "logblob".equalsIgnoreCase(transaction) || "playdata".equalsIgnoreCase(transaction) || "ping".equalsIgnoreCase(transaction)) {
                        if (Log.isLoggable(PlayerAgent.TAG, 3)) {
                            Log.d(PlayerAgent.TAG, "Ignore action id on " + transaction + ". We will deal with only licence and authorize here when not in playback");
                        }
                    }
                    else {
                        if ("background".equals(mActionId12Error.getType())) {
                            Log.d(PlayerAgent.TAG, "We received background nccp error. Ignoring!");
                            return;
                        }
                        if (Log.isLoggable(PlayerAgent.TAG, 3)) {
                            Log.d(PlayerAgent.TAG, "Handling error: " + mActionId12Error);
                        }
                        this.notifyPlayerListeners(mActionId12Error);
                    }
                }
            }
        }
    }
    
    private void handleGenericMediaEvent(final GenericMediaEvent genericMediaEvent) {
        final String type = genericMediaEvent.getType();
        if (IMedia.MediaEventEnum.media_openComplete.getName().equalsIgnoreCase(type)) {
            this.handlePrepare();
        }
        else {
            if (IMedia.MediaEventEnum.media_endOfStream.getName().equalsIgnoreCase(type)) {
                this.handleEndOfPlayback();
                return;
            }
            if (IMedia.MediaEventEnum.media_bufferingComplete.getName().equalsIgnoreCase(type)) {
                this.handleBufferingComplete();
                return;
            }
            if (IMedia.MediaEventEnum.media_underflow.getName().equalsIgnoreCase(type)) {
                this.handleUnderflow();
                return;
            }
            if (Log.isLoggable(PlayerAgent.TAG, 6)) {
                Log.e(PlayerAgent.TAG, "Tags not handled yet " + type);
            }
        }
    }
    
    private void handleMediaError(final Error error) {
        Log.d(PlayerAgent.TAG, "Media error receieved");
        if (!this.ignoreErrorsWhileActionId12IsProcessed) {
            this.notifyPlayerListeners(error);
        }
    }
    
    private void handleMediaWarning(final Warning warning) {
        Log.d(PlayerAgent.TAG, "Media warning receieved");
        if (warning.containsInStack("NFErr_MC_SubtitleFailure")) {
            Log.e(PlayerAgent.TAG, "=====> Subtitle failed!");
            final Iterator<PlayerListener> iterator = this.mPlayerListeners.iterator();
            while (iterator.hasNext()) {
                this.getMainHandler().post((Runnable)new Runnable() {
                    final /* synthetic */ PlayerListener val$listener = iterator.next();
                    
                    @Override
                    public void run() {
                        this.val$listener.onSubtitleFailed();
                    }
                });
            }
        }
    }
    
    private void handlePlayback() {
        Label_0147: {
            synchronized (this) {
                Log.d(PlayerAgent.TAG, "handlePlayback starts...");
                if (this.seeking) {
                    Log.d(PlayerAgent.TAG, "MEDIA_SEEK_COMPLETE 4");
                    this.seeking = false;
                    final Iterator<PlayerListener> iterator = this.mPlayerListeners.iterator();
                    while (iterator.hasNext()) {
                        this.getMainHandler().post((Runnable)new Runnable() {
                            final /* synthetic */ PlayerListener val$listener = iterator.next();
                            
                            @Override
                            public void run() {
                                this.val$listener.onSeekComplete();
                            }
                        });
                    }
                    break Label_0147;
                }
            }
            Log.d(PlayerAgent.TAG, "MEDIA_PLAYBACK_STARTED 6");
            final Iterator<PlayerListener> iterator2 = this.mPlayerListeners.iterator();
            while (iterator2.hasNext()) {
                this.getMainHandler().post((Runnable)new Runnable() {
                    final /* synthetic */ PlayerListener val$listener = iterator2.next();
                    
                    @Override
                    public void run() {
                        this.val$listener.onPlaying();
                    }
                });
            }
        }
        Log.d(PlayerAgent.TAG, "handlePlayback end");
    }
    // monitorexit(this)
    
    private void handlePrepare() {
        while (true) {
            Label_0108: {
                synchronized (this) {
                    if (this.preparedCompleted) {
                        Log.w(PlayerAgent.TAG, "openComplete already executed");
                    }
                    else {
                        Log.d(PlayerAgent.TAG, "handle openComplete starts...");
                        this.preparedCompleted = true;
                        if (!this.toCancelOpen) {
                            Log.d(PlayerAgent.TAG, "handle openComplete notifying client");
                            final Iterator<PlayerListener> iterator = this.mPlayerListeners.iterator();
                            while (iterator.hasNext()) {
                                this.getMainHandler().post((Runnable)new Runnable() {
                                    final /* synthetic */ PlayerListener val$listener = iterator.next();
                                    
                                    @Override
                                    public void run() {
                                        this.val$listener.onVideoSizeChanged(PlayerAgent.this.mMedia.getVideoWidth(), PlayerAgent.this.mMedia.getVideoHeight());
                                        this.val$listener.onPrepared();
                                    }
                                });
                            }
                            break Label_0108;
                        }
                    }
                    return;
                }
            }
            if (Log.isLoggable(PlayerAgent.TAG, 3)) {
                Log.d(PlayerAgent.TAG, "MEDIA_SET_VIDEO_SIZE 5, w " + this.mMedia.getVideoWidth() + ", h " + this.mMedia.getVideoHeight());
            }
            Log.d(PlayerAgent.TAG, "handle openComplete end");
        }
    }
    
    private void handleRemoveSubtitle(final RemoveSubtitle removeSubtitle) {
        Log.d(PlayerAgent.TAG, "MEDIA_SUBTITLE_REMOVE 52");
        final Iterator<PlayerListener> iterator = this.mPlayerListeners.iterator();
        while (iterator.hasNext()) {
            this.getMainHandler().post((Runnable)new Runnable() {
                final /* synthetic */ PlayerListener val$listener = iterator.next();
                
                @Override
                public void run() {
                    this.val$listener.onSubtitleRemove();
                }
            });
        }
    }
    
    private void handleShowSubtitle(final ShowSubtitle showSubtitle) {
        Log.d(PlayerAgent.TAG, "MEDIA_SUBTITLE_SHOW 51");
        String text;
        if ((text = showSubtitle.getText()) == null) {
            text = "";
        }
        final Iterator<PlayerListener> iterator = this.mPlayerListeners.iterator();
        while (iterator.hasNext()) {
            this.getMainHandler().post((Runnable)new Runnable() {
                final /* synthetic */ PlayerListener val$listener = iterator.next();
                
                @Override
                public void run() {
                    this.val$listener.onSubtitleShow(text);
                }
            });
        }
    }
    
    private void handleStatechanged(final Statechanged statechanged) {
        while (true) {
            Label_0159: {
                Label_0129: {
                    Label_0103: {
                        synchronized (this) {
                            switch (statechanged.getState()) {
                                case 0: {
                                    Log.d(PlayerAgent.TAG, "State OPENING");
                                    if (this.mState != 0) {
                                        this.transitToOpeningState();
                                        this.mState = 0;
                                    }
                                    return;
                                }
                                case 1: {
                                    break;
                                }
                                case 2: {
                                    break Label_0103;
                                }
                                case 3: {
                                    break Label_0129;
                                }
                                case 4: {
                                    break Label_0159;
                                }
                                default: {
                                    return;
                                }
                            }
                        }
                        Log.d(PlayerAgent.TAG, "State PLAYING");
                        if (this.mState != 1) {
                            this.mState = 1;
                            return;
                        }
                        return;
                    }
                    Log.d(PlayerAgent.TAG, "State PAUSED");
                    if (this.mState != 2) {
                        this.mState = 2;
                        return;
                    }
                    return;
                }
                Log.d(PlayerAgent.TAG, "State STOPPED");
                if (this.mState != 3) {
                    this.transitToStoppedState();
                    this.mState = 3;
                    return;
                }
                return;
            }
            Log.d(PlayerAgent.TAG, "State CLOSED");
            if (this.mState != 4) {
                this.transitToClosedState();
                this.mState = 4;
            }
        }
    }
    
    private void handleSubtitleData(final SubtitleData subtitleData) {
        Log.d(PlayerAgent.TAG, "MEDIA_SUBTITLE_DATA 100");
        new BackgroundTask().execute(new Runnable() {
            @Override
            public void run() {
                Log.d(PlayerAgent.TAG, "Subtitles metadata update started.");
                final TextStyle userSubtitlePreferences = PlayerAgent.this.getUserAgent().getUserSubtitlePreferences();
                final TextStyle subtitleDefaults = PlayerAgent.this.getUserAgent().getSubtitleDefaults();
                while (true) {
                    try {
                        while (true) {
                            final SubtitleParser subtitleParser = new SubtitleParser(PlayerAgent.this.mMedia.getDisplayAspectRatio(), userSubtitlePreferences, subtitleDefaults);
                            while (true) {
                                try {
                                    synchronized (PlayerAgent.this) {
                                        PlayerAgent.this.mSubtitles = subtitleParser;
                                        // monitorexit(this.this$0)
                                        subtitleParser.parse(subtitleData);
                                        Log.d(PlayerAgent.TAG, "Subtitles metadata updated.");
                                        return;
                                    }
                                }
                                catch (Throwable t) {}
                                Log.e(PlayerAgent.TAG, "We failed to parse subtitle metadata", (Throwable)userSubtitlePreferences);
                                PlayerAgent.this.getService().getClientLogging().getErrorLogging().logHandledException(new RuntimeException("We failed to parse subtitle metadata", (Throwable)userSubtitlePreferences));
                                continue;
                            }
                        }
                    }
                    catch (Throwable userSubtitlePreferences) {
                        continue;
                    }
                    break;
                }
            }
        });
    }
    
    private void handleSubtitleUpdate(final int n) {
        while (true) {
            Label_0108: {
                synchronized (this) {
                    if (IMedia.SubtitleOutputMode.EVENTS.equals(this.mSubtitleConfiguration.getMode())) {
                        Log.d(PlayerAgent.TAG, "Subtitle output mode Events, do nothing");
                    }
                    else {
                        if (Log.isLoggable(PlayerAgent.TAG, 3)) {
                            Log.d(PlayerAgent.TAG, "Subtitle output mode XML, send data");
                            Log.d(PlayerAgent.TAG, "Update PTS received " + n);
                        }
                        if (this.mMedia.getCurrentSubtitleTrack() != null) {
                            break Label_0108;
                        }
                        Log.d(PlayerAgent.TAG, "Subtitles are not visible, do not send any update");
                    }
                    return;
                }
            }
            final SubtitleParser mSubtitles = this.mSubtitles;
            if (mSubtitles == null) {
                Log.d(PlayerAgent.TAG, "Subtitle data is not available.");
                return;
            }
            if (!mSubtitles.isReady()) {
                Log.d(PlayerAgent.TAG, "Subtitle data is not ready yet!");
                return;
            }
            if (!this.isPlaying()) {
                Log.d(PlayerAgent.TAG, "Not playing, do NOT send subtitle screen update");
                return;
            }
            if (!this.canUpdatePosition(n)) {
                Log.d(PlayerAgent.TAG, "Can not update position, do NOT send subtitle screen update");
                return;
            }
            final SubtitleScreen subtitlesForPosition = mSubtitles.getSubtitlesForPosition(n);
            final Iterator<PlayerListener> iterator = this.mPlayerListeners.iterator();
            while (iterator.hasNext()) {
                this.getMainHandler().post((Runnable)new Runnable() {
                    final /* synthetic */ PlayerListener val$listener = iterator.next();
                    
                    @Override
                    public void run() {
                        this.val$listener.onSubtitleChange(subtitlesForPosition);
                    }
                });
            }
        }
    }
    
    private void handleUnderflow() {
        Log.w(PlayerAgent.TAG, "MEDIA_PLAYBACK_STALLED 7");
        if (this.mPlayerType == PlayerType.device9) {
            this.prevEndPosition = this.getCurrentPositionMs();
            this.seekedToPosition = this.prevEndPosition;
            this.mInPlayback = false;
        }
        this.mBufferingCompleted = false;
        final Iterator<PlayerListener> iterator = this.mPlayerListeners.iterator();
        while (iterator.hasNext()) {
            this.getMainHandler().post((Runnable)new Runnable() {
                final /* synthetic */ PlayerListener val$listener = iterator.next();
                
                @Override
                public void run() {
                    this.val$listener.onStalled();
                }
            });
        }
    }
    
    private void handleUpdatePts(final int n) {
        if (this.mPlayerType == PlayerType.device9) {
            int n2;
            if (this.seekedToPosition > 1000) {
                n2 = this.seekedToPosition - 1000;
            }
            else {
                n2 = 0;
            }
            if (this.mBufferingCompleted && n > n2 && !this.mInPlayback && (this.prevEndPosition <= this.seekedToPosition || n < this.prevEndPosition)) {
                this.handlePlayback();
                this.mInPlayback = true;
                this.inPlaybackSession = true;
            }
        }
        else {
            this.mInPlayback = true;
            this.inPlaybackSession = true;
        }
        if (!this.splashScreenRemoved) {
            this.muteAudio(false);
            this.splashScreenRemoved = true;
            final Iterator<PlayerListener> iterator = this.mPlayerListeners.iterator();
            while (iterator.hasNext()) {
                this.getMainHandler().post((Runnable)new Runnable() {
                    final /* synthetic */ PlayerListener val$listener = iterator.next();
                    
                    @Override
                    public void run() {
                        this.val$listener.onStarted();
                    }
                });
            }
        }
        if (this.mInPlayback && this.mBifManager == null) {
            final PlayoutMetadata playoutMetadata = this.getPlayoutMetadata();
            if (playoutMetadata != null && playoutMetadata.targetBitRate >= 500) {
                this.startBif();
            }
        }
        this.handleSubtitleUpdate(n);
    }
    
    private void muteAudio(final boolean muted) {
        synchronized (this) {
            if (muted != this.muted) {
                final AudioManager audioManager = (AudioManager)this.getContext().getSystemService("audio");
                if (audioManager != null) {
                    audioManager.setStreamMute(3, muted);
                    this.muted = muted;
                    if (muted) {
                        Log.d(PlayerAgent.TAG, "MUTED");
                    }
                    else {
                        Log.d(PlayerAgent.TAG, "UN-MUTED");
                    }
                }
            }
        }
    }
    
    private void notifyPlayerListeners(final Error error) {
        final Iterator<PlayerListener> iterator = this.mPlayerListeners.iterator();
        while (iterator.hasNext()) {
            this.getMainHandler().post((Runnable)new Runnable() {
                final /* synthetic */ PlayerListener val$listener = iterator.next();
                
                @Override
                public void run() {
                    this.val$listener.onMediaError(error);
                }
            });
        }
    }
    
    private void notifyPlayerListeners(final NccpError nccpError) {
        final Iterator<PlayerListener> iterator = this.mPlayerListeners.iterator();
        while (iterator.hasNext()) {
            this.getMainHandler().post((Runnable)new Runnable() {
                final /* synthetic */ PlayerListener val$listener = iterator.next();
                
                @Override
                public void run() {
                    this.val$listener.onNccpError(nccpError);
                }
            });
        }
    }
    
    private void notifyPlayerListenersOfClose() {
        final Iterator<PlayerListener> iterator = this.mPlayerListeners.iterator();
        while (iterator.hasNext()) {
            this.getMainHandler().post((Runnable)new Runnable() {
                final /* synthetic */ PlayerListener val$listener = iterator.next();
                
                @Override
                public void run() {
                    this.val$listener.playbackClosed();
                }
            });
        }
    }
    
    private void notifyPlayerListenersRestart() {
        final Iterator<PlayerListener> iterator = this.mPlayerListeners.iterator();
        while (iterator.hasNext()) {
            this.getMainHandler().post((Runnable)new Runnable() {
                final /* synthetic */ PlayerListener val$listener = iterator.next();
                
                @Override
                public void run() {
                    this.val$listener.restartPlayback(PlayerAgent.this.mActionId12Error);
                    PlayerAgent.this.mActionId12Error = null;
                }
            });
        }
    }
    
    private void playWithBookmarkCheck() {
        this.seekedToPosition = (int)(Object)Long.valueOf(this.mBookmark);
        final int duration = this.getDuration();
        if (Log.isLoggable(PlayerAgent.TAG, 3)) {
            Log.d(PlayerAgent.TAG, "movie duration = " + duration + ", and bookmark = " + this.seekedToPosition);
        }
        this.mState = 6;
        this.mMedia.play(this.mBookmark);
        this.toPlayAfterStop = false;
    }
    
    private void preparePlayerType(final PlayerType playerType) {
        if (playerType == PlayerType.device10 || playerType == PlayerType.device11) {
            this.mHelper.prepareJPlayer(this.mMedia, this.mSurface, this.mJPlayerListener, this.isPropertyStreamingVideoDrs());
            return;
        }
        if (playerType != PlayerType.device9) {
            this.mHelper.prepare(this.mMedia, this.mSurface, this.getContext());
            return;
        }
        this.mHelper.prepare(this.mMedia, this.mSurfaceHolder, this.getContext());
    }
    
    private void registerReceivers() {
    }
    
    private void release() {
        synchronized (this) {
            Log.d(PlayerAgent.TAG, "release()");
            this.mSurface = null;
            this.mSurfaceHolder = null;
            if (this.mHelper != null) {
                this.mHelper.release();
                this.mHelper = null;
            }
            if (this.mJPlayerListener != null) {
                this.mJPlayerListener = null;
            }
            this.mBookmark = 0L;
            this.preparedCompleted = false;
            this.splashScreenRemoved = false;
            this.seekedToPosition = 0;
            this.mBufferingCompleted = false;
            this.pendingError = null;
            this.muteAudio(false);
            if (this.mBifManager != null) {
                this.mBifManager.release();
                this.mBifManager = null;
            }
        }
    }
    
    private void startBif() {
        synchronized (this) {
            if (this.mBifManager == null && this.mMedia.getTrickplayUrlList() != null) {
                this.mBifManager = new BifManager(this.getContext(), this.mMedia.getTrickplayUrlList(), this.seekedToPosition);
            }
        }
    }
    
    private void transitToClosedState() {
        if (this.mCloseTimeoutTask != null) {
            this.mCloseTimeoutTask.cancel();
        }
        if (this.mTimer != null) {
            this.mTimer.purge();
        }
        this.muteAudio(false);
        this.toCancelOpen = false;
        this.toPlayAfterStop = false;
        if (this.toOpenAfterClose) {
            this.toOpenAfterClose = false;
            this.mState = 5;
            if (this.mStartPlayTimeoutTask != null) {
                this.mStartPlayTimeoutTask.cancel();
            }
            if (this.mTimer != null) {
                this.mTimer.purge();
            }
            this.reloadPlayer();
            this.mMedia.setStreamingQoe(this.getConfigurationAgent().getStreamingQoe());
            this.mMedia.open(this.mMovieId, this.mPlayContext, this.getCurrentNetType());
            return;
        }
        this.release();
        this.notifyPlayerListenersOfClose();
    }
    
    private void transitToOpeningState() {
        Log.d(PlayerAgent.TAG, "MP: Set audio bitrange to 64 Kbps");
        this.mMedia.setAudioBitrateRange(BitrateRangeFactory.getAudioBitrateRange());
        this.mMedia.setThrotteled(false);
        this.mMedia.setNetworkProfile(2);
        this.muteAudio(true);
    }
    
    private void transitToStoppedState() {
        if (this.mState == 0) {
            this.mMedia.setAudioBitrateRange(BitrateRangeFactory.getAudioBitrateRange());
            this.mMedia.setVideoBitrateRanges(this.getConfigurationAgent().getVideoBitrateRange());
            this.mMedia.setThrotteled(false);
            this.mMedia.setNetworkProfile(2);
        }
        if (this.toPlayAfterStop) {
            this.playWithBookmarkCheck();
        }
    }
    
    private void unRegisterReceivers() {
    }
    
    private void updateSubtitleSettings(final boolean b) {
        final SubtitleConfiguration subtitleConfiguration = this.findSubtitleConfiguration();
        if (Log.isLoggable(PlayerAgent.TAG, 3)) {
            Log.d(PlayerAgent.TAG, "Subtitle configuration was " + this.mSubtitleConfiguration);
            Log.d(PlayerAgent.TAG, "Sets subtitle configuration to " + subtitleConfiguration);
        }
        if (this.mSubtitleConfiguration == subtitleConfiguration && !b) {
            if (Log.isLoggable(PlayerAgent.TAG, 3)) {
                Log.d(PlayerAgent.TAG, "Already used subtitle configuration, do nothing ");
            }
            return;
        }
        if (b) {
            Log.d(PlayerAgent.TAG, "Forced set of subtitle configuration");
        }
        this.mMedia.setSubtitleProfile(subtitleConfiguration.getProfile());
        this.mMedia.setSubtitleOutputMode(subtitleConfiguration.getMode());
        this.mSubtitleConfiguration = subtitleConfiguration;
    }
    
    private void updateSubtitleSettingsFromQaLocalOverride(final int n) {
        final SubtitleConfiguration lookup = SubtitleConfiguration.lookup(n);
        if (Log.isLoggable(PlayerAgent.TAG, 3)) {
            Log.d(PlayerAgent.TAG, "Received local override " + n);
            Log.d(PlayerAgent.TAG, "Subtitle configuration was " + this.mSubtitleConfiguration);
            Log.d(PlayerAgent.TAG, "Sets subtitle configuration to " + lookup);
        }
        if (this.mSubtitleConfiguration == lookup) {
            if (Log.isLoggable(PlayerAgent.TAG, 3)) {
                Log.d(PlayerAgent.TAG, "Already used subtitle configuration, do nothing ");
            }
            return;
        }
        this.mMedia.setSubtitleProfile(lookup.getProfile());
        this.mMedia.setSubtitleOutputMode(lookup.getMode());
        this.mSubtitleConfiguration = lookup;
    }
    
    @Override
    public void addPlayerListener(final PlayerListener playerListener) {
        if (!this.mPlayerListeners.contains(playerListener)) {
            this.mPlayerListeners.add(playerListener);
        }
        if (this.mPlayerListeners.size() > 2) {
            Log.e(PlayerAgent.TAG, "Listener count should not be generally greater than 2, count:" + this.mPlayerListeners.size());
        }
    }
    
    @Override
    public boolean canUpdatePosition(final int n) {
        if (this.seeking) {
            Log.d(PlayerAgent.TAG, "canUpdatePosition:: seeking in progress, can not update position");
        }
        else {
            if (n >= this.seekedToPosition) {
                if (Log.isLoggable(PlayerAgent.TAG, 3)) {
                    Log.d(PlayerAgent.TAG, "canUpdatePosition:: pts [" + n + "] >= seekedToPosition [" + this.seekedToPosition + "] , can update position");
                }
                if (!this.validPtsRecieved) {
                    if (this.prevEndPosition > this.seekedToPosition && n >= this.prevEndPosition - 2000) {
                        if (Log.isLoggable(PlayerAgent.TAG, 3)) {
                            Log.d(PlayerAgent.TAG, "canUpdatePosition:: pts [" + n + "] >= prevEndPosition [" + this.prevEndPosition + "] , invlalid PTS");
                            return false;
                        }
                        return false;
                    }
                    else {
                        this.validPtsRecieved = true;
                    }
                }
                return true;
            }
            if (Log.isLoggable(PlayerAgent.TAG, 5)) {
                Log.w(PlayerAgent.TAG, "canUpdatePosition:: pts [" + n + "] < seekedToPosition [" + this.seekedToPosition + "] , can NOT update position");
                return false;
            }
        }
        return false;
    }
    
    @Override
    public void close() {
        Log.d(PlayerAgent.TAG, "close()");
        this.mSurface = null;
        this.mSubtitles = null;
        this.inPlaybackSession = false;
        this.muteAudio(true);
        this.mPlayerExecutor.execute(this.onCloseRunnable);
    }
    
    @Override
    public void destroy() {
        this.unRegisterReceivers();
        if (this.mPlayerExecutor != null) {
            this.mPlayerExecutor.shutdown();
        }
        super.destroy();
        if (Log.isLoggable(PlayerAgent.TAG, 3)) {
            Log.d(PlayerAgent.TAG, "Destroying " + this.getClass().getSimpleName());
        }
    }
    
    @Override
    protected void doInit() {
        this.mNrdp = this.getNrdController().getNrdp();
        if (this.mNrdp == null || !this.mNrdp.isReady()) {
            this.initCompleted(-4);
            Log.e(PlayerAgent.TAG, "NRDP is NOT READY");
            return;
        }
        this.mMedia = this.mNrdp.getMedia();
        this.mMediaEventListener = new GenericMediaEventListener();
        final IMedia.MediaEventEnum[] values = IMedia.MediaEventEnum.values();
        for (int length = values.length, i = 0; i < length; ++i) {
            final IMedia.MediaEventEnum mediaEventEnum = values[i];
            if (Log.isLoggable(PlayerAgent.TAG, 3)) {
                Log.d(PlayerAgent.TAG, "Registering as listener for " + mediaEventEnum.getName());
            }
            this.mMedia.addEventListener(mediaEventEnum.getName(), this.mMediaEventListener);
        }
        this.mPlayerType = PlayerTypeFactory.getCurrentType(this.getContext());
        this.mState = -1;
        this.toCancelOpen = false;
        if (this.mPlayerType == null) {
            Log.e(PlayerAgent.TAG, "This should not happen, player type was null at this point! Use default.");
            this.mPlayerType = PlayerType.device6;
        }
        else if (Log.isLoggable(PlayerAgent.TAG, 3)) {
            Log.d(PlayerAgent.TAG, "Player type is " + this.mPlayerType.getDescription());
        }
        this.mHelper = MediaPlayerHelperFactory.getInstance(this.mPlayerType);
        this.mBitrateCap = BitrateRangeFactory.getBitrateCap(this.getContext());
        if (Log.isLoggable(PlayerAgent.TAG, 3)) {
            Log.d(PlayerAgent.TAG, "Current bitrate cap setting " + this.mBitrateCap);
        }
        Log.d(PlayerAgent.TAG, "MP: Set audio bitrange to 64 Kbps");
        this.mMedia.setAudioBitrateRange(BitrateRangeFactory.getAudioBitrateRange());
        this.mMedia.setStreamingQoe(this.getConfigurationAgent().getStreamingQoe());
        this.mMedia.setThrotteled(false);
        this.mMedia.setNetworkProfile(2);
        Log.d(PlayerAgent.TAG, "MP: Set to Mobile network Profile");
        this.updateSubtitleSettings(true);
        this.mTimer = new Timer("watchdog timer");
        this.registerReceivers();
        this.mPlayerExecutor = Executors.newSingleThreadExecutor();
        this.initCompleted(0);
    }
    
    void excuteOnPlayExecutor(final Runnable runnable) {
        this.mPlayerExecutor.execute(runnable);
    }
    
    @Override
    public AudioSubtitleDefaultOrderInfo[] getAudioSubtitleDefaultOrderInfo() {
        return this.mMedia.getAudioSubtitleDefaultOrderInfo();
    }
    
    @Override
    public AudioSource[] getAudioTrackList() {
        return this.mMedia.getAudioTrackList();
    }
    
    @Override
    public ByteBuffer getBifFrame(final int n) {
        if (this.mBifManager != null) {
            return this.mBifManager.getIndexFrame(n);
        }
        return null;
    }
    
    @Override
    public AudioSource getCurrentAudioTrack() {
        return this.mMedia.getCurrentAudioTrack();
    }
    
    @Override
    public int getCurrentPositionMs() {
        return this.mMedia.getMediaPosition();
    }
    
    @Override
    public int getCurrentProgress() {
        final int currentPosition = this.mMedia.getCurrentPosition();
        int seekedToPosition;
        if (currentPosition < this.seekedToPosition) {
            seekedToPosition = this.seekedToPosition;
        }
        else {
            seekedToPosition = currentPosition;
            if (!this.validPtsRecieved) {
                if (this.prevEndPosition - 2000 > this.seekedToPosition && currentPosition >= this.seekedToPosition + 1500) {
                    if (Log.isLoggable(PlayerAgent.TAG, 3)) {
                        Log.d(PlayerAgent.TAG, "pts [" + currentPosition + "] >= prevEndPosition [" + this.prevEndPosition + "] , invlalid PTS");
                    }
                    return this.seekedToPosition;
                }
                this.validPtsRecieved = true;
                return currentPosition;
            }
        }
        return seekedToPosition;
    }
    
    @Override
    public Subtitle getCurrentSubtitleTrack() {
        return this.mMedia.getCurrentSubtitleTrack();
    }
    
    @Override
    public int getDuration() {
        return this.mMedia.getDuration();
    }
    
    @Override
    public PlayoutMetadata getPlayoutMetadata() {
        return this.mMedia.getPlayoutMetadata();
    }
    
    @Override
    public SubtitleConfiguration getSubtitleConfiguration() {
        return this.mSubtitleConfiguration;
    }
    
    @Override
    public IMedia.SubtitleProfile getSubtitleProfileFromMetadata() {
        final SubtitleParser mSubtitles = this.mSubtitles;
        if (mSubtitles != null) {
            return mSubtitles.getSubtitleProfile();
        }
        return null;
    }
    
    @Override
    public Subtitle[] getSubtitleTrackList() {
        return this.mMedia.getSubtitleTrackList();
    }
    
    @Override
    public int getVideoHeight() {
        return this.mMedia.getVideoHeight();
    }
    
    @Override
    public int getVideoWidth() {
        return this.mMedia.getVideoWidth();
    }
    
    public void handleConnectivityChange(final Intent intent) {
        if (ConnectivityUtils.isNetworkTypeCellular(this.getContext())) {
            this.setVideoStreamingBufferSize(90000);
            return;
        }
        this.setVideoStreamingBufferSize(300000);
    }
    
    @Override
    public boolean isBufferingCompleted() {
        return this.mBufferingCompleted && this.mInPlayback;
    }
    
    @Override
    public boolean isPlaying() {
        return this.mMedia.getState() == 1;
    }
    
    public boolean isPropertyStreamingVideoDrs() {
        return !PlayerTypeFactory.isJPlayerBase(PlayerTypeFactory.getCurrentType(this.getContext())) && AndroidUtils.isPropertyStreamingVideoDrs();
    }
    
    @Override
    public void onConfigRefreshed(final int n) {
        if (n == 0) {
            this.updateSubtitleSettings(false);
        }
    }
    
    @Override
    public void open(final long mMovieId, final PlayContext mPlayContext, final long mBookmark) {
        if (Log.isLoggable(PlayerAgent.TAG, 3)) {
            Log.d(PlayerAgent.TAG, "Open called movieId:" + mMovieId + " trackId:" + mPlayContext.getTrackId() + " bookmark:" + mBookmark);
        }
        this.mMovieId = mMovieId;
        this.mPlayContext = mPlayContext;
        this.mBookmark = mBookmark;
        this.mPlayerExecutor.execute(this.onOpenRunnable);
    }
    
    @Override
    public void pause() {
        this.mMedia.pause();
    }
    
    @Override
    public void play() {
        this.mPlayerExecutor.execute(this.onPlayRunnable);
    }
    
    public void reloadPlayer() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aload_0        
        //     1: monitorenter   
        //     2: getstatic       com/netflix/mediaclient/service/player/PlayerAgent.TAG:Ljava/lang/String;
        //     5: ldc_w           "reloadPlayer if required"
        //     8: invokestatic    com/netflix/mediaclient/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //    11: pop            
        //    12: aload_0        
        //    13: invokevirtual   com/netflix/mediaclient/service/player/PlayerAgent.getContext:()Landroid/content/Context;
        //    16: invokestatic    com/netflix/mediaclient/service/configuration/PlayerTypeFactory.getCurrentType:(Landroid/content/Context;)Lcom/netflix/mediaclient/media/PlayerType;
        //    19: astore_2       
        //    20: aload_0        
        //    21: invokevirtual   com/netflix/mediaclient/service/player/PlayerAgent.getContext:()Landroid/content/Context;
        //    24: invokestatic    com/netflix/mediaclient/service/configuration/BitrateRangeFactory.getBitrateCap:(Landroid/content/Context;)I
        //    27: istore_1       
        //    28: aload_0        
        //    29: aload_2        
        //    30: invokestatic    com/netflix/mediaclient/media/MediaPlayerHelperFactory.getInstance:(Lcom/netflix/mediaclient/media/PlayerType;)Lcom/netflix/mediaclient/media/MediaPlayerHelper;
        //    33: putfield        com/netflix/mediaclient/service/player/PlayerAgent.mHelper:Lcom/netflix/mediaclient/media/MediaPlayerHelper;
        //    36: aload_2        
        //    37: aload_0        
        //    38: getfield        com/netflix/mediaclient/service/player/PlayerAgent.mPlayerType:Lcom/netflix/mediaclient/media/PlayerType;
        //    41: if_acmpne       110
        //    44: iload_1        
        //    45: aload_0        
        //    46: getfield        com/netflix/mediaclient/service/player/PlayerAgent.mBitrateCap:I
        //    49: if_icmpne       110
        //    52: getstatic       com/netflix/mediaclient/service/player/PlayerAgent.TAG:Ljava/lang/String;
        //    55: bipush          6
        //    57: invokestatic    com/netflix/mediaclient/Log.isLoggable:(Ljava/lang/String;I)Z
        //    60: ifeq            102
        //    63: getstatic       com/netflix/mediaclient/service/player/PlayerAgent.TAG:Ljava/lang/String;
        //    66: new             Ljava/lang/StringBuilder;
        //    69: dup            
        //    70: invokespecial   java/lang/StringBuilder.<init>:()V
        //    73: ldc_w           "Player type is not changed! It is still "
        //    76: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    79: aload_0        
        //    80: getfield        com/netflix/mediaclient/service/player/PlayerAgent.mPlayerType:Lcom/netflix/mediaclient/media/PlayerType;
        //    83: invokevirtual   com/netflix/mediaclient/media/PlayerType.getDescription:()Ljava/lang/String;
        //    86: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    89: ldc_w           ". Preparing players!"
        //    92: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    95: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //    98: invokestatic    com/netflix/mediaclient/Log.e:(Ljava/lang/String;Ljava/lang/String;)I
        //   101: pop            
        //   102: aload_0        
        //   103: aload_2        
        //   104: invokespecial   com/netflix/mediaclient/service/player/PlayerAgent.preparePlayerType:(Lcom/netflix/mediaclient/media/PlayerType;)V
        //   107: aload_0        
        //   108: monitorexit    
        //   109: return         
        //   110: aload_0        
        //   111: aload_2        
        //   112: putfield        com/netflix/mediaclient/service/player/PlayerAgent.mPlayerType:Lcom/netflix/mediaclient/media/PlayerType;
        //   115: aload_0        
        //   116: iload_1        
        //   117: putfield        com/netflix/mediaclient/service/player/PlayerAgent.mBitrateCap:I
        //   120: aload_0        
        //   121: getfield        com/netflix/mediaclient/service/player/PlayerAgent.mPlayerType:Lcom/netflix/mediaclient/media/PlayerType;
        //   124: ifnonnull       225
        //   127: getstatic       com/netflix/mediaclient/service/player/PlayerAgent.TAG:Ljava/lang/String;
        //   130: ldc_w           "This should not happen, player type was null at this point! Use default."
        //   133: invokestatic    com/netflix/mediaclient/Log.e:(Ljava/lang/String;Ljava/lang/String;)I
        //   136: pop            
        //   137: aload_0        
        //   138: getstatic       com/netflix/mediaclient/media/PlayerType.device6:Lcom/netflix/mediaclient/media/PlayerType;
        //   141: putfield        com/netflix/mediaclient/service/player/PlayerAgent.mPlayerType:Lcom/netflix/mediaclient/media/PlayerType;
        //   144: aload_0        
        //   145: aload_0        
        //   146: getfield        com/netflix/mediaclient/service/player/PlayerAgent.mPlayerType:Lcom/netflix/mediaclient/media/PlayerType;
        //   149: invokespecial   com/netflix/mediaclient/service/player/PlayerAgent.preparePlayerType:(Lcom/netflix/mediaclient/media/PlayerType;)V
        //   152: aload_0        
        //   153: getfield        com/netflix/mediaclient/service/player/PlayerAgent.mMedia:Lcom/netflix/mediaclient/javabridge/ui/IMedia;
        //   156: aload_0        
        //   157: getfield        com/netflix/mediaclient/service/player/PlayerAgent.mPlayerType:Lcom/netflix/mediaclient/media/PlayerType;
        //   160: aload_0        
        //   161: getfield        com/netflix/mediaclient/service/player/PlayerAgent.mBitrateCap:I
        //   164: invokeinterface com/netflix/mediaclient/javabridge/ui/IMedia.changePlayer:(Lcom/netflix/mediaclient/media/PlayerType;I)V
        //   169: aload_0        
        //   170: getfield        com/netflix/mediaclient/service/player/PlayerAgent.mMedia:Lcom/netflix/mediaclient/javabridge/ui/IMedia;
        //   173: invokestatic    com/netflix/mediaclient/service/configuration/BitrateRangeFactory.getAudioBitrateRange:()Lcom/netflix/mediaclient/media/bitrate/AudioBitrateRange;
        //   176: invokeinterface com/netflix/mediaclient/javabridge/ui/IMedia.setAudioBitrateRange:(Lcom/netflix/mediaclient/media/bitrate/AudioBitrateRange;)V
        //   181: aload_0        
        //   182: getfield        com/netflix/mediaclient/service/player/PlayerAgent.mMedia:Lcom/netflix/mediaclient/javabridge/ui/IMedia;
        //   185: iconst_0       
        //   186: invokeinterface com/netflix/mediaclient/javabridge/ui/IMedia.setThrotteled:(Z)V
        //   191: aload_0        
        //   192: getfield        com/netflix/mediaclient/service/player/PlayerAgent.mMedia:Lcom/netflix/mediaclient/javabridge/ui/IMedia;
        //   195: iconst_2       
        //   196: invokeinterface com/netflix/mediaclient/javabridge/ui/IMedia.setNetworkProfile:(I)V
        //   201: ldc2_w          400
        //   204: invokestatic    java/lang/Thread.sleep:(J)V
        //   207: getstatic       com/netflix/mediaclient/service/player/PlayerAgent.TAG:Ljava/lang/String;
        //   210: ldc_w           "Player changed done"
        //   213: invokestatic    com/netflix/mediaclient/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   216: pop            
        //   217: goto            107
        //   220: astore_2       
        //   221: aload_0        
        //   222: monitorexit    
        //   223: aload_2        
        //   224: athrow         
        //   225: getstatic       com/netflix/mediaclient/service/player/PlayerAgent.TAG:Ljava/lang/String;
        //   228: iconst_3       
        //   229: invokestatic    com/netflix/mediaclient/Log.isLoggable:(Ljava/lang/String;I)Z
        //   232: ifeq            144
        //   235: getstatic       com/netflix/mediaclient/service/player/PlayerAgent.TAG:Ljava/lang/String;
        //   238: new             Ljava/lang/StringBuilder;
        //   241: dup            
        //   242: invokespecial   java/lang/StringBuilder.<init>:()V
        //   245: ldc_w           "Player type is "
        //   248: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   251: aload_0        
        //   252: getfield        com/netflix/mediaclient/service/player/PlayerAgent.mPlayerType:Lcom/netflix/mediaclient/media/PlayerType;
        //   255: invokevirtual   com/netflix/mediaclient/media/PlayerType.getDescription:()Ljava/lang/String;
        //   258: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   261: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   264: invokestatic    com/netflix/mediaclient/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   267: pop            
        //   268: goto            144
        //   271: astore_2       
        //   272: getstatic       com/netflix/mediaclient/service/player/PlayerAgent.TAG:Ljava/lang/String;
        //   275: ldc_w           "ReloadPlayer "
        //   278: aload_2        
        //   279: invokestatic    com/netflix/mediaclient/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //   282: pop            
        //   283: goto            207
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                            
        //  -----  -----  -----  -----  --------------------------------
        //  2      102    220    225    Any
        //  102    107    220    225    Any
        //  110    144    220    225    Any
        //  144    201    220    225    Any
        //  201    207    271    286    Ljava/lang/InterruptedException;
        //  201    207    220    225    Any
        //  207    217    220    225    Any
        //  225    268    220    225    Any
        //  272    283    220    225    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0207:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2592)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:757)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:655)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:532)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:499)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:141)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:130)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:105)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:317)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:238)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:138)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @Override
    public void removePlayerListener(final PlayerListener playerListener) {
        Log.d(PlayerAgent.TAG, "Removed listener: " + this.mPlayerListeners.remove(playerListener));
    }
    
    public void reset() {
        synchronized (this) {
            this.preparedCompleted = false;
            this.splashScreenRemoved = false;
            this.seekedToPosition = 0;
            this.mInPlayback = false;
            this.inPlaybackSession = false;
        }
    }
    
    @Override
    public void seekTo(final int seekedToPosition, final boolean mForcedRebuffer) {
        this.seekedToPosition = seekedToPosition;
        this.mForcedRebuffer = mForcedRebuffer;
        this.mPlayerExecutor.execute(this.onSeekRunnable);
        if (this.mSubtitles != null) {
            this.mSubtitles.seeked();
        }
    }
    
    @Override
    public boolean selectTracks(final AudioSource audioSource, final Subtitle subtitle) {
        synchronized (this) {
            if (Log.isLoggable(PlayerAgent.TAG, 3)) {
                Log.d(PlayerAgent.TAG, "Selected track Audio: " + audioSource);
                Log.d(PlayerAgent.TAG, "Selected track Subtitle: " + subtitle);
            }
            this.mMedia.selectTracks(audioSource, subtitle);
            if (subtitle == null) {
                Log.d(PlayerAgent.TAG, "Removing subtitles");
                this.mSubtitles = null;
            }
            return true;
        }
    }
    
    @Override
    public void setJPlayerListener(final JPlayer.JplayerListener mjPlayerListener) {
        this.mJPlayerListener = mjPlayerListener;
    }
    
    @Override
    public void setSurface(final Surface surface) {
        if (Log.isLoggable(PlayerAgent.TAG, 3)) {
            final String tag = PlayerAgent.TAG;
            final StringBuilder append = new StringBuilder().append("Surface is being set: ");
            String s;
            if (this.mSurface != null) {
                s = "SurfaceExisted";
            }
            else {
                s = "No Surface Existed";
            }
            Log.d(tag, append.append(s).toString());
        }
        this.mSurface = surface;
        this.mMedia.setSurface(surface);
    }
    
    @Override
    public void setSurfaceHolder(final SurfaceHolder mSurfaceHolder) {
        Log.d(PlayerAgent.TAG, "Surface Holder is being set");
        this.mSurfaceHolder = mSurfaceHolder;
    }
    
    public void setVOapi(final long n, final long n2) {
        this.mMedia.setVOapi(n, n2);
    }
    
    void setVideoBitrateRange(final int n, final int n2) {
        if (this.mMedia != null) {}
    }
    
    void setVideoStreamingBufferSize(final int maxVideoBufferSize) {
        if (this.mMedia != null) {
            this.mMedia.setMaxVideoBufferSize(maxVideoBufferSize);
        }
    }
    
    @Override
    public void unpause() {
        this.mMedia.unpause();
    }
    
    private class CloseTimeoutTask extends TimerTask
    {
        CloseTimeoutTask() {
            Log.d(PlayerAgent.TAG, "CloseTimeoutTask created!");
        }
        
        @Override
        public void run() {
            Log.d(PlayerAgent.TAG, "CloseTimeoutTask to unmute audio!");
            PlayerAgent.this.muteAudio(false);
        }
    }
    
    private class GenericMediaEventListener implements EventListener
    {
        @Override
        public void received(final UIEvent uiEvent) {
            Log.d(PlayerAgent.TAG, "Received a media event ");
            if (uiEvent instanceof GenericMediaEvent) {
                PlayerAgent.this.handleGenericMediaEvent((GenericMediaEvent)uiEvent);
                return;
            }
            if (uiEvent instanceof NccpError) {
                PlayerAgent.this.handleError((NccpError)uiEvent);
                return;
            }
            if (uiEvent instanceof GenericMediaEvent) {
                PlayerAgent.this.handleGenericMediaEvent((GenericMediaEvent)uiEvent);
                return;
            }
            if (uiEvent instanceof Buffering) {
                PlayerAgent.this.handleBufferring((Buffering)uiEvent);
                return;
            }
            if (uiEvent instanceof RemoveSubtitle) {
                PlayerAgent.this.handleRemoveSubtitle((RemoveSubtitle)uiEvent);
                return;
            }
            if (uiEvent instanceof ShowSubtitle) {
                PlayerAgent.this.handleShowSubtitle((ShowSubtitle)uiEvent);
                return;
            }
            if (uiEvent instanceof SubtitleData) {
                PlayerAgent.this.handleSubtitleData((SubtitleData)uiEvent);
                return;
            }
            if (uiEvent instanceof AudioTrackChanged) {
                PlayerAgent.this.handleAudioTrackChanged((AudioTrackChanged)uiEvent);
                return;
            }
            if (uiEvent instanceof Statechanged) {
                PlayerAgent.this.handleStatechanged((Statechanged)uiEvent);
                return;
            }
            if (uiEvent instanceof BufferRange) {
                PlayerAgent.this.handleBufferRange((BufferRange)uiEvent);
                return;
            }
            if (uiEvent instanceof UpdatePts) {
                PlayerAgent.this.handleUpdatePts(((UpdatePts)uiEvent).getPts());
                return;
            }
            if (uiEvent instanceof Error) {
                PlayerAgent.this.handleMediaError((Error)uiEvent);
                return;
            }
            if (uiEvent instanceof Warning) {
                PlayerAgent.this.handleMediaWarning((Warning)uiEvent);
                return;
            }
            Log.e(PlayerAgent.TAG, "Uknown event: " + uiEvent.getType());
        }
    }
    
    private class StartPlayTimeoutTask extends TimerTask
    {
        StartPlayTimeoutTask() {
            Log.d(PlayerAgent.TAG, "StartPlayTimeoutTask created!");
        }
        
        @Override
        public void run() {
            Log.d(PlayerAgent.TAG, "StartPlayTimeoutTask to handleFatalError()!");
            PlayerAgent.this.getMainHandler().post((Runnable)new Runnable() {
                @Override
                public void run() {
                    final Iterator<PlayerListener> iterator = PlayerAgent.this.mPlayerListeners.iterator();
                    while (iterator.hasNext()) {
                        iterator.next().onNrdFatalError();
                    }
                }
            });
        }
    }
}