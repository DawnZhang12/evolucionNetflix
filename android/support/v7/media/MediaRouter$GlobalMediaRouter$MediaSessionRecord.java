// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v7.media;

import android.os.Bundle;
import android.content.Intent;
import android.content.pm.PackageManager$NameNotFoundException;
import android.view.Display;
import android.content.ContentResolver;
import java.util.List;
import java.util.Collections;
import android.util.Log;
import java.util.Iterator;
import java.util.Locale;
import android.support.v4.app.ActivityManagerCompat;
import android.app.ActivityManager;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import android.support.v4.hardware.display.DisplayManagerCompat;
import android.content.Context;
import android.support.v4.media.VolumeProviderCompat;
import android.support.v4.media.session.MediaSessionCompat;

final class MediaRouter$GlobalMediaRouter$MediaSessionRecord
{
    private int mControlType;
    private int mMaxVolume;
    private final MediaSessionCompat mMsCompat;
    private VolumeProviderCompat mVpCompat;
    final /* synthetic */ MediaRouter$GlobalMediaRouter this$0;
    
    public MediaRouter$GlobalMediaRouter$MediaSessionRecord(final MediaRouter$GlobalMediaRouter this$0, final Object o) {
        this.this$0 = this$0;
        this.mMsCompat = MediaSessionCompat.obtain(o);
    }
    
    public void clearVolumeHandling() {
        this.mMsCompat.setPlaybackToLocal(this.this$0.mPlaybackInfo.playbackStream);
        this.mVpCompat = null;
    }
    
    public void configureVolume(final int n, final int n2, final int currentVolume) {
        if (this.mVpCompat != null && n == this.mControlType && n2 == this.mMaxVolume) {
            this.mVpCompat.setCurrentVolume(currentVolume);
            return;
        }
        this.mVpCompat = new MediaRouter$GlobalMediaRouter$MediaSessionRecord$1(this, n, n2, currentVolume);
        this.mMsCompat.setPlaybackToRemote(this.mVpCompat);
    }
}