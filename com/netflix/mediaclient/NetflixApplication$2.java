// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.mediaclient;

import com.netflix.mediaclient.event.UIEvent;
import android.app.Application$ActivityLifecycleCallbacks;
import com.netflix.mediaclient.service.logging.perf.Sessions;
import java.util.Map;
import com.netflix.mediaclient.service.logging.perf.Events;
import com.netflix.mediaclient.service.logging.perf.PerformanceProfiler;
import com.netflix.mediaclient.service.pservice.PServiceWidgetProvider;
import com.netflix.mediaclient.util.AndroidUtils;
import android.content.res.Configuration;
import com.netflix.mediaclient.util.PreferenceUtils;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat$Builder;
import com.netflix.mediaclient.util.IntentUtils;
import com.netflix.mediaclient.util.l10n.LocalizationUtils;
import com.netflix.mediaclient.util.AndroidManifestUtils;
import com.netflix.mediaclient.repository.SecurityRepository;
import com.netflix.mediaclient.ui.launch.LaunchActivity;
import android.content.Intent;
import android.content.Context;
import com.netflix.mediaclient.android.app.UserInputManager;
import com.netflix.mediaclient.util.l10n.UserLocale;
import java.util.concurrent.atomic.AtomicBoolean;
import com.netflix.mediaclient.util.gfx.BitmapLruCache;
import java.util.Timer;
import com.netflix.mediaclient.android.activity.NetflixActivity;
import android.content.BroadcastReceiver;
import com.google.gson.Gson;
import android.app.Application;
import java.util.TimerTask;

class NetflixApplication$2 extends TimerTask
{
    final /* synthetic */ NetflixApplication this$0;
    
    NetflixApplication$2(final NetflixApplication this$0) {
        this.this$0 = this$0;
    }
    
    @Override
    public void run() {
        this.this$0.wasInBackground = true;
    }
}
