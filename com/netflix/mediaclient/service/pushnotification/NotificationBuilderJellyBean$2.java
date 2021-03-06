// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.mediaclient.service.pushnotification;

import android.graphics.Bitmap;
import com.netflix.mediaclient.Log;
import android.content.Context;
import android.app.Notification$BigPictureStyle;
import com.netflix.mediaclient.util.gfx.ImageLoader$ImageLoaderListener;

final class NotificationBuilderJellyBean$2 implements ImageLoader$ImageLoaderListener
{
    final /* synthetic */ Notification$BigPictureStyle val$bigPictureStyle;
    final /* synthetic */ Context val$context;
    final /* synthetic */ int val$msgId;
    final /* synthetic */ Payload val$payload;
    
    NotificationBuilderJellyBean$2(final Payload val$payload, final Notification$BigPictureStyle val$bigPictureStyle, final Context val$context, final int val$msgId) {
        this.val$payload = val$payload;
        this.val$bigPictureStyle = val$bigPictureStyle;
        this.val$context = val$context;
        this.val$msgId = val$msgId;
    }
    
    public void onErrorResponse(final String s) {
        if (Log.isLoggable()) {
            Log.e("nf_push", "Failed to downlod " + this.val$payload.largeIcon + ". Reason: " + s);
        }
        NotificationBuilder.sendNotification(this.val$context, this.val$bigPictureStyle.build(), this.val$msgId);
    }
    
    public void onResponse(final Bitmap bitmap, final String s) {
        if (Log.isLoggable()) {
            Log.d("nf_push", "Image is downloaded " + this.val$payload.bigViewPicture + " from " + s);
        }
        if (bitmap != null) {
            this.val$bigPictureStyle.bigPicture(bitmap);
        }
        Log.d("nf_push", "Large icon image set!");
        NotificationBuilder.sendNotification(this.val$context, this.val$bigPictureStyle.build(), this.val$msgId);
    }
}
