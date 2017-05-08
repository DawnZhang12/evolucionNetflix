// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.mediaclient.ui.iko.wordparty.moments;

import android.util.Property;
import android.animation.ValueAnimator$AnimatorUpdateListener;
import com.netflix.mediaclient.ui.iko.wordparty.WPConstants;
import android.animation.PropertyValuesHolder;
import android.animation.TimeInterpolator;
import android.animation.AnimatorSet;
import android.animation.Animator$AnimatorListener;
import android.net.Uri;
import com.netflix.mediaclient.ui.common.MediaPlayerWrapper$PlaybackEventsListener;
import android.text.TextUtils;
import com.netflix.mediaclient.ui.iko.BaseInteractiveMomentsManager$PlaybackCompleteListener;
import com.netflix.mediaclient.util.DeviceUtils;
import com.netflix.mediaclient.servicemgr.ManagerCallback;
import com.netflix.mediaclient.servicemgr.IClientLogging$AssetType;
import com.netflix.mediaclient.android.activity.NetflixActivity;
import com.netflix.mediaclient.util.log.IkoLogUtils;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import android.widget.FrameLayout$LayoutParams;
import android.util.AttributeSet;
import android.content.Context;
import android.view.TextureView;
import android.widget.ImageView;
import android.graphics.drawable.BitmapDrawable;
import com.netflix.mediaclient.ui.iko.wordparty.model.WPInteractiveMomentsModel$WPVideo;
import android.view.animation.Interpolator;
import com.netflix.mediaclient.ui.common.MediaPlayerWrapper;
import android.animation.ObjectAnimator;
import com.netflix.mediaclient.ui.iko.wordparty.model.WPInteractiveMomentsModel$WPAudio;
import android.widget.FrameLayout;
import com.netflix.mediaclient.Log;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;

class WPCardLayout$7 extends AnimatorListenerAdapter
{
    final /* synthetic */ WPCardLayout this$0;
    
    WPCardLayout$7(final WPCardLayout this$0) {
        this.this$0 = this$0;
    }
    
    public void onAnimationEnd(final Animator animator) {
        super.onAnimationEnd(animator);
        if (Log.isLoggable()) {
            Log.d("WPCardLayout", "hideVideo onAnimationEnd");
        }
        this.this$0.videoTextureState = WPCardLayout$VideoTextureState.HIDDEN;
        if (this.this$0.videoTexture != null) {
            this.this$0.videoTexture.setVisibility(8);
        }
        if (this.this$0.videoMaskImageView != null) {
            this.this$0.videoMaskImageView.setVisibility(8);
        }
    }
}
