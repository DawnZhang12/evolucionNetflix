// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.media;

import android.view.KeyEvent$DispatcherState;
import android.view.KeyEvent;
import android.os.Build$VERSION;
import android.app.Activity;
import android.view.View;
import java.util.ArrayList;
import android.view.KeyEvent$Callback;
import android.content.Context;
import android.media.AudioManager;

public class TransportMediator extends TransportController
{
    public static final int FLAG_KEY_MEDIA_FAST_FORWARD = 64;
    public static final int FLAG_KEY_MEDIA_NEXT = 128;
    public static final int FLAG_KEY_MEDIA_PAUSE = 16;
    public static final int FLAG_KEY_MEDIA_PLAY = 4;
    public static final int FLAG_KEY_MEDIA_PLAY_PAUSE = 8;
    public static final int FLAG_KEY_MEDIA_PREVIOUS = 1;
    public static final int FLAG_KEY_MEDIA_REWIND = 2;
    public static final int FLAG_KEY_MEDIA_STOP = 32;
    public static final int KEYCODE_MEDIA_PAUSE = 127;
    public static final int KEYCODE_MEDIA_PLAY = 126;
    public static final int KEYCODE_MEDIA_RECORD = 130;
    final AudioManager mAudioManager;
    final TransportPerformer mCallbacks;
    final Context mContext;
    final TransportMediatorJellybeanMR2 mController;
    final Object mDispatcherState;
    final KeyEvent$Callback mKeyEventCallback;
    final ArrayList<TransportStateListener> mListeners;
    final TransportMediatorCallback mTransportKeyCallback;
    final View mView;
    
    public TransportMediator(final Activity activity, final TransportPerformer transportPerformer) {
        this(activity, null, transportPerformer);
    }
    
    private TransportMediator(final Activity activity, View decorView, final TransportPerformer mCallbacks) {
        this.mListeners = new ArrayList<TransportStateListener>();
        this.mTransportKeyCallback = new TransportMediator$1(this);
        this.mKeyEventCallback = (KeyEvent$Callback)new TransportMediator$2(this);
        Object context;
        if (activity != null) {
            context = activity;
        }
        else {
            context = decorView.getContext();
        }
        this.mContext = (Context)context;
        this.mCallbacks = mCallbacks;
        this.mAudioManager = (AudioManager)this.mContext.getSystemService("audio");
        if (activity != null) {
            decorView = activity.getWindow().getDecorView();
        }
        this.mView = decorView;
        this.mDispatcherState = this.mView.getKeyDispatcherState();
        if (Build$VERSION.SDK_INT >= 18) {
            this.mController = new TransportMediatorJellybeanMR2(this.mContext, this.mAudioManager, this.mView, this.mTransportKeyCallback);
            return;
        }
        this.mController = null;
    }
    
    public TransportMediator(final View view, final TransportPerformer transportPerformer) {
        this(null, view, transportPerformer);
    }
    
    private TransportStateListener[] getListeners() {
        if (this.mListeners.size() <= 0) {
            return null;
        }
        final TransportStateListener[] array = new TransportStateListener[this.mListeners.size()];
        this.mListeners.toArray(array);
        return array;
    }
    
    static boolean isMediaKey(final int n) {
        switch (n) {
            default: {
                return false;
            }
            case 79:
            case 85:
            case 86:
            case 87:
            case 88:
            case 89:
            case 90:
            case 91:
            case 126:
            case 127:
            case 130: {
                return true;
            }
        }
    }
    
    private void pushControllerState() {
        if (this.mController != null) {
            this.mController.refreshState(this.mCallbacks.onIsPlaying(), this.mCallbacks.onGetCurrentPosition(), this.mCallbacks.onGetTransportControlFlags());
        }
    }
    
    private void reportPlayingChanged() {
        final TransportStateListener[] listeners = this.getListeners();
        if (listeners != null) {
            for (int length = listeners.length, i = 0; i < length; ++i) {
                listeners[i].onPlayingChanged(this);
            }
        }
    }
    
    private void reportTransportControlsChanged() {
        final TransportStateListener[] listeners = this.getListeners();
        if (listeners != null) {
            for (int length = listeners.length, i = 0; i < length; ++i) {
                listeners[i].onTransportControlsChanged(this);
            }
        }
    }
    
    public void destroy() {
        this.mController.destroy();
    }
    
    public boolean dispatchKeyEvent(final KeyEvent keyEvent) {
        return keyEvent.dispatch(this.mKeyEventCallback, (KeyEvent$DispatcherState)this.mDispatcherState, (Object)this);
    }
    
    @Override
    public int getBufferPercentage() {
        return this.mCallbacks.onGetBufferPercentage();
    }
    
    @Override
    public long getCurrentPosition() {
        return this.mCallbacks.onGetCurrentPosition();
    }
    
    @Override
    public long getDuration() {
        return this.mCallbacks.onGetDuration();
    }
    
    public Object getRemoteControlClient() {
        if (this.mController != null) {
            return this.mController.getRemoteControlClient();
        }
        return null;
    }
    
    @Override
    public int getTransportControlFlags() {
        return this.mCallbacks.onGetTransportControlFlags();
    }
    
    @Override
    public boolean isPlaying() {
        return this.mCallbacks.onIsPlaying();
    }
    
    @Override
    public void pausePlaying() {
        if (this.mController != null) {
            this.mController.pausePlaying();
        }
        this.mCallbacks.onPause();
        this.pushControllerState();
        this.reportPlayingChanged();
    }
    
    public void refreshState() {
        this.pushControllerState();
        this.reportPlayingChanged();
        this.reportTransportControlsChanged();
    }
    
    @Override
    public void registerStateListener(final TransportStateListener transportStateListener) {
        this.mListeners.add(transportStateListener);
    }
    
    @Override
    public void seekTo(final long n) {
        this.mCallbacks.onSeekTo(n);
    }
    
    @Override
    public void startPlaying() {
        if (this.mController != null) {
            this.mController.startPlaying();
        }
        this.mCallbacks.onStart();
        this.pushControllerState();
        this.reportPlayingChanged();
    }
    
    @Override
    public void stopPlaying() {
        if (this.mController != null) {
            this.mController.stopPlaying();
        }
        this.mCallbacks.onStop();
        this.pushControllerState();
        this.reportPlayingChanged();
    }
    
    @Override
    public void unregisterStateListener(final TransportStateListener transportStateListener) {
        this.mListeners.remove(transportStateListener);
    }
}
