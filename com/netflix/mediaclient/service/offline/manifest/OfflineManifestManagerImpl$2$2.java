// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.mediaclient.service.offline.manifest;

import com.netflix.mediaclient.service.player.bladerunnerclient.BladeRunnerWebCallback;
import com.netflix.mediaclient.servicemgr.interface_.offline.DownloadVideoQuality;
import com.netflix.mediaclient.android.app.BaseStatus;
import java.util.Iterator;
import com.netflix.mediaclient.util.StringUtils;
import com.netflix.mediaclient.util.LogUtils;
import java.io.File;
import com.netflix.mediaclient.android.app.NetflixImmutableStatus;
import com.netflix.mediaclient.util.FileUtils;
import com.netflix.mediaclient.service.offline.utils.OfflinePathUtils;
import java.util.List;
import com.netflix.mediaclient.android.app.NetflixStatus;
import com.netflix.mediaclient.StatusCode;
import com.netflix.mediaclient.android.app.CommonStatus;
import com.netflix.mediaclient.service.offline.log.OfflineErrorLogblob;
import com.netflix.mediaclient.Log;
import java.util.HashMap;
import android.os.HandlerThread;
import android.os.Handler;
import com.netflix.mediaclient.service.pdslogging.PdsDownloadInterface;
import com.netflix.mediaclient.service.player.manifest.NfManifest;
import java.util.Map;
import com.netflix.mediaclient.servicemgr.IClientLogging;
import com.netflix.mediaclient.service.player.bladerunnerclient.BladeRunnerClient;
import com.netflix.mediaclient.service.pdslogging.DownloadContext;
import org.json.JSONObject;
import com.netflix.mediaclient.android.app.Status;

class OfflineManifestManagerImpl$2$2 implements Runnable
{
    final /* synthetic */ OfflineManifestManagerImpl$2 this$1;
    final /* synthetic */ Status val$status;
    
    OfflineManifestManagerImpl$2$2(final OfflineManifestManagerImpl$2 this$1, final Status val$status) {
        this.this$1 = this$1;
        this.val$status = val$status;
    }
    
    @Override
    public void run() {
        this.this$1.this$0.handleManifestResponse(this.val$status, this.this$1.val$playableId, this.this$1.val$playableDirPath, false, null, this.this$1.val$oxid, this.this$1.val$dxid, null);
    }
}
