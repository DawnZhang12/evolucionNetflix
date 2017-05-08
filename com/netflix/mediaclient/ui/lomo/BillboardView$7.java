// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.mediaclient.ui.lomo;

import android.widget.FrameLayout$LayoutParams;
import com.netflix.mediaclient.servicemgr.BillboardInteractionType;
import com.netflix.mediaclient.util.StringUtils;
import com.netflix.mediaclient.servicemgr.interface_.Video;
import com.netflix.mediaclient.servicemgr.ServiceManagerUtils;
import com.netflix.mediaclient.ui.common.PlayContextImp;
import com.netflix.mediaclient.servicemgr.interface_.trackable.Trackable;
import java.util.HashMap;
import com.netflix.mediaclient.util.log.ConsolidatedLoggingUtils;
import com.netflix.mediaclient.servicemgr.interface_.details.VideoDetails;
import android.content.res.Resources;
import android.view.ViewGroup$LayoutParams;
import com.netflix.model.leafs.originals.BillboardDateBadge;
import com.netflix.model.leafs.originals.BillboardAwardsHeadline;
import com.netflix.mediaclient.util.gfx.ImageLoader$StaticImgConfig;
import com.netflix.mediaclient.servicemgr.interface_.VideoType;
import com.netflix.mediaclient.servicemgr.interface_.Playable;
import android.net.Uri;
import com.netflix.mediaclient.ui.common.MediaPlayerWrapper$PlaybackEventsListener;
import com.netflix.model.leafs.originals.BillboardBackground;
import android.view.ViewTreeObserver$OnGlobalLayoutListener;
import com.netflix.mediaclient.service.webclient.model.leafs.ABTestConfig$Cell;
import com.netflix.mediaclient.service.configuration.PersistentConfig;
import android.widget.RelativeLayout$LayoutParams;
import com.netflix.mediaclient.util.ViewUtils;
import com.netflix.mediaclient.util.DeviceUtils;
import android.view.ViewGroup;
import com.netflix.mediaclient.ui.common.PlayContextProvider;
import android.text.TextUtils;
import com.netflix.model.leafs.originals.BillboardSummary;
import android.view.View$OnClickListener;
import java.util.List;
import com.netflix.mediaclient.servicemgr.ServiceManager;
import com.netflix.model.leafs.originals.BillboardCTA;
import java.util.ArrayList;
import com.netflix.mediaclient.servicemgr.ManagerCallback;
import com.netflix.mediaclient.servicemgr.IClientLogging$AssetType;
import com.netflix.mediaclient.android.activity.NetflixActivity;
import android.util.AttributeSet;
import com.netflix.mediaclient.util.log.UIViewLogUtils;
import android.content.Context;
import android.widget.FrameLayout;
import com.netflix.mediaclient.android.widget.TopCropImageView;
import com.netflix.mediaclient.ui.common.PlayContext;
import android.view.TextureView;
import com.netflix.mediaclient.ui.common.MediaPlayerWrapper;
import android.view.View;
import java.util.Map;
import android.widget.TextView;
import com.netflix.mediaclient.android.widget.VideoDetailsClickListener;
import android.widget.Button;
import com.netflix.mediaclient.android.widget.AdvancedImageView;
import com.netflix.mediaclient.servicemgr.AddToListData$StateListener;
import com.netflix.mediaclient.servicemgr.interface_.Billboard;
import android.widget.RelativeLayout;
import com.netflix.mediaclient.Log;

class BillboardView$7 implements Runnable
{
    final /* synthetic */ BillboardView this$0;
    final /* synthetic */ boolean val$hasWindowFocus;
    
    BillboardView$7(final BillboardView this$0, final boolean val$hasWindowFocus) {
        this.this$0 = this$0;
        this.val$hasWindowFocus = val$hasWindowFocus;
    }
    
    @Override
    public void run() {
        if (this.this$0.mediaPlayerWrapper == null) {
            Log.v("BillboardView", "null mediaPlayerWrapper - bailing");
        }
        else {
            if (!this.val$hasWindowFocus) {
                Log.v("BillboardView", "Losing window focus - pausing playback");
                this.this$0.mediaPlayerWrapper.pausePlayback();
                return;
            }
            if (this.this$0.mediaPlayerWrapper.isDonePlaying()) {
                Log.v("BillboardView", "Received focus but media playback complete - skipping resume");
                this.this$0.hideMotionBB();
                return;
            }
            if (!this.this$0.mediaPlayerWrapper.resumePlayback()) {
                Log.v("BillboardView", "Playback not ready yet, but showing motion BB");
                this.this$0.showMotionBB();
            }
            if (this.this$0.myListButton.getVisibility() == 0) {
                Log.v("BillboardView", "Playback ready, updating myList state");
                this.this$0.updateMyListState();
            }
        }
    }
}