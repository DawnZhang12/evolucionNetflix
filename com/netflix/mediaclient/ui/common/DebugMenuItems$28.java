// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.mediaclient.ui.common;

import android.view.SubMenu;
import android.support.v4.app.ActivityCompat;
import com.netflix.mediaclient.util.PermissionUtils;
import android.os.Handler;
import android.os.Debug;
import com.netflix.mediaclient.Log;
import com.netflix.mediaclient.android.debug.DebugOverlay;
import com.netflix.mediaclient.ui.home.HomeActivity;
import com.netflix.mediaclient.util.net.CronetHttpURLConnectionFactory;
import android.content.Context;
import com.netflix.mediaclient.util.PreferenceUtils;
import android.view.Menu;
import com.netflix.mediaclient.android.activity.NetflixActivity;
import android.app.Activity;
import com.netflix.mediaclient.ui.barker.details.BarkerRulerActivity;
import android.view.MenuItem;
import android.view.MenuItem$OnMenuItemClickListener;

class DebugMenuItems$28 implements MenuItem$OnMenuItemClickListener
{
    final /* synthetic */ DebugMenuItems this$0;
    
    DebugMenuItems$28(final DebugMenuItems this$0) {
        this.this$0 = this$0;
    }
    
    public boolean onMenuItemClick(final MenuItem menuItem) {
        BarkerRulerActivity.showRuler(this.this$0.activity);
        return true;
    }
}