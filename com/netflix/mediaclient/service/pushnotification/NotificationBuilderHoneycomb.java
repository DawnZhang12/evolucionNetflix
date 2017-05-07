// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.mediaclient.service.pushnotification;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import com.netflix.mediaclient.Log;
import com.netflix.mediaclient.servicemgr.IClientLogging;
import com.netflix.mediaclient.util.StringUtils;
import com.netflix.mediaclient.util.gfx.ImageLoader;
import com.netflix.mediaclient.servicemgr.ErrorLogging;
import android.app.Notification$Builder;
import android.content.Context;
import android.annotation.TargetApi;

@TargetApi(11)
public class NotificationBuilderHoneycomb extends NotificationBuilder
{
    public static void createNotification(final Context context, final Payload payload, final ImageLoader imageLoader, final int n, final ErrorLogging errorLogging) {
        final long when = payload.getWhen();
        final String title = payload.getTitle(context.getString(2131492897));
        final String ticker = payload.getTicker(title);
        final Notification$Builder notification$Builder = new Notification$Builder(context);
        notification$Builder.setContentIntent(NotificationBuilder.getNotificationOpenedIntent(context, payload));
        notification$Builder.setDeleteIntent(NotificationBuilder.getNotificationCanceledIntent(context, payload));
        notification$Builder.setTicker((CharSequence)ticker);
        notification$Builder.setAutoCancel(true);
        notification$Builder.setContentTitle((CharSequence)title);
        notification$Builder.setContentText((CharSequence)payload.text);
        notification$Builder.setSmallIcon(2130837629);
        notification$Builder.setWhen(when);
        while (true) {
            if (NotificationBuilder.isSoundEnabled(context)) {
                try {
                    notification$Builder.setSound(NotificationBuilder.getSound(payload.sound), 5);
                    if (!StringUtils.isEmpty(payload.largeIcon) && imageLoader != null) {
                        imageLoader.getImg(payload.largeIcon, IClientLogging.AssetType.boxArt, 0, 0, (ImageLoader.ImageLoaderListener)new ImageLoader.ImageLoaderListener() {
                            @Override
                            public void onErrorResponse(final String s) {
                                if (Log.isLoggable("nf_push", 6)) {
                                    Log.e("nf_push", "Failed to downlod " + payload.largeIcon + ". Reason: " + s);
                                }
                            }
                            
                            @Override
                            public void onResponse(final Bitmap largeIcon, final String s) {
                                if (Log.isLoggable("nf_push", 3)) {
                                    Log.d("nf_push", "Image is downloaded " + payload.largeIcon + " from " + s);
                                }
                                if (largeIcon != null) {
                                    notification$Builder.setLargeIcon(largeIcon);
                                }
                                send(context, notification$Builder, n, errorLogging);
                                Log.d("nf_push", "Image set!");
                            }
                        });
                        return;
                    }
                }
                catch (Throwable t) {
                    Log.e("nf_push", "Failed to get notification sound URL!", t);
                    continue;
                }
                Log.d("nf_push", "Icon was not set");
                send(context, notification$Builder, n, errorLogging);
                return;
            }
            continue;
        }
    }
    
    @SuppressLint({ "NewApi" })
    private static void send(final Context context, final Notification$Builder notification$Builder, final int n, final ErrorLogging errorLogging) {
        NotificationBuilder.sendNotification(context, notification$Builder.getNotification(), n, errorLogging);
    }
}