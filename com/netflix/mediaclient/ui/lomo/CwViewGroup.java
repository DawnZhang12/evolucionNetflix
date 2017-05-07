// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.mediaclient.ui.lomo;

import com.netflix.mediaclient.Log;
import com.netflix.mediaclient.ui.lolomo.LoLoMoFocusHandler;
import android.view.View;
import android.content.Context;
import com.netflix.mediaclient.servicemgr.CWVideo;

public class CwViewGroup extends VideoViewGroup<CWVideo, CwView>
{
    public CwViewGroup(final Context context) {
        super(context);
    }
    
    @Override
    protected CwView createChildView(final Context context) {
        return new CwView(context);
    }
    
    @Override
    protected boolean shouldApplyPaddingToChildren() {
        return true;
    }
    
    @Override
    protected boolean shouldOverlapPages() {
        return true;
    }
    
    @Override
    protected void updateViewIds(final CwView cwView, int computeViewId, final int n, final int n2) {
        computeViewId = LoLoMoFocusHandler.computeViewId(computeViewId, (n + n2) * 2);
        if (Log.isLoggable("VideoViewGroup", 2)) {
            Log.v("VideoViewGroup", "Setting base view id to: " + computeViewId);
        }
        cwView.setId(computeViewId);
        cwView.setInfoViewId(computeViewId + 1);
    }
}