// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.mediaclient.servicemgr;

import com.netflix.mediaclient.media.Language;
import com.netflix.mediaclient.service.player.subtitles.SubtitleScreen;
import com.netflix.mediaclient.media.Watermark;
import com.netflix.mediaclient.event.nrdp.media.NccpError;
import com.netflix.mediaclient.event.nrdp.media.Error;

public interface IPlayer$PlayerListener
{
    boolean isListening();
    
    void onAudioChange(final int p0);
    
    void onBandwidthChange(final int p0);
    
    void onBufferingUpdate(final int p0);
    
    void onCompletion();
    
    void onMediaError(final Error p0);
    
    void onNccpError(final NccpError p0);
    
    void onNrdFatalError();
    
    void onPlaybackError(final IPlayer$PlaybackError p0);
    
    void onPlaying();
    
    void onPrepared(final Watermark p0);
    
    void onSeekComplete();
    
    void onStalled();
    
    void onStarted();
    
    void onSubtitleChange(final SubtitleScreen p0);
    
    void onSubtitleFailed();
    
    void onUpdatePts(final int p0);
    
    void onVideoSizeChanged(final int p0, final int p1);
    
    void playbackClosed();
    
    void restartPlayback(final NccpError p0);
    
    void setLanguage(final Language p0);
}
