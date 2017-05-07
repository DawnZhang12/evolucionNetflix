// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.mediaclient.ui.common;

import android.os.Parcelable;
import com.netflix.mediaclient.servicemgr.Trackable;

public interface PlayContext extends Trackable, Parcelable
{
    public static final int DEFAULT_MDX_TRACKID = 13804431;
    public static final PlayContext DFLT_MDX_CONTEXT = new PlayContextImp(null, 13804431, 0, 0);
    public static final PlayContext EMPTY_CONTEXT = new PlayContextImp();
    public static final int NFLX_MDX_3RD_PARTY_LAUNCHER_TRACKID = 13747225;
    public static final PlayContext NFLX_MDX_CONTEXT = new PlayContextImp(null, 13747225, 0, 0);
    
    int getVideoPos();
}