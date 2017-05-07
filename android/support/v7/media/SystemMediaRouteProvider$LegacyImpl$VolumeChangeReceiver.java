// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v7.media;

import android.content.res.Resources;
import java.util.Collection;
import android.support.v7.mediarouter.R$string;
import android.media.AudioManager;
import android.content.IntentFilter;
import java.util.ArrayList;
import android.content.Intent;
import android.content.Context;
import android.content.BroadcastReceiver;

final class SystemMediaRouteProvider$LegacyImpl$VolumeChangeReceiver extends BroadcastReceiver
{
    public static final String EXTRA_VOLUME_STREAM_TYPE = "android.media.EXTRA_VOLUME_STREAM_TYPE";
    public static final String EXTRA_VOLUME_STREAM_VALUE = "android.media.EXTRA_VOLUME_STREAM_VALUE";
    public static final String VOLUME_CHANGED_ACTION = "android.media.VOLUME_CHANGED_ACTION";
    final /* synthetic */ SystemMediaRouteProvider$LegacyImpl this$0;
    
    SystemMediaRouteProvider$LegacyImpl$VolumeChangeReceiver(final SystemMediaRouteProvider$LegacyImpl this$0) {
        this.this$0 = this$0;
    }
    
    public void onReceive(final Context context, final Intent intent) {
        if (intent.getAction().equals("android.media.VOLUME_CHANGED_ACTION") && intent.getIntExtra("android.media.EXTRA_VOLUME_STREAM_TYPE", -1) == 3) {
            final int intExtra = intent.getIntExtra("android.media.EXTRA_VOLUME_STREAM_VALUE", -1);
            if (intExtra >= 0 && intExtra != this.this$0.mLastReportedVolume) {
                this.this$0.publishRoutes();
            }
        }
    }
}