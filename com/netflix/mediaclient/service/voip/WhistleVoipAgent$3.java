// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.mediaclient.service.voip;

import com.netflix.mediaclient.service.webclient.model.leafs.VoipAuthorizationData;
import org.json.JSONException;
import com.netflix.mediaclient.service.logging.client.model.RootCause;
import org.json.JSONObject;
import com.netflix.mediaclient.service.logging.client.model.DeepErrorElement$Debug;
import com.netflix.mediaclient.service.logging.client.model.DeepErrorElement;
import com.netflix.mediaclient.android.app.Status;
import com.netflix.mediaclient.android.app.CommonStatus;
import com.netflix.mediaclient.util.log.ConsolidatedLoggingUtils;
import com.vailsys.whistleengine.WhistleEngineDelegate$ConnectivityState;
import com.netflix.mediaclient.service.logging.client.model.Error;
import com.netflix.mediaclient.servicemgr.IClientLogging$CompletionReason;
import com.netflix.mediaclient.servicemgr.CustomerServiceLogging$TerminationReason;
import com.netflix.mediaclient.util.log.CustomerServiceLogUtils;
import com.netflix.mediaclient.servicemgr.CustomerServiceLogging$CallQuality;
import android.os.Process;
import android.media.AudioManager;
import java.util.Iterator;
import com.netflix.mediaclient.servicemgr.IVoip$Call;
import com.netflix.mediaclient.service.webclient.model.leafs.VoipConfiguration;
import com.vailsys.whistleengine.WhistleEngineThresholds;
import com.netflix.mediaclient.service.ServiceAgent$ConfigurationAgentInterface;
import com.netflix.mediaclient.servicemgr.IVoip$AuthorizationTokens;
import com.netflix.mediaclient.util.FileUtils;
import com.vailsys.whistleengine.WhistleEngineConfig$TransportMode;
import com.vailsys.whistleengine.WhistleEngineConfig;
import com.netflix.mediaclient.service.user.UserLocaleRepository;
import com.netflix.mediaclient.util.l10n.UserLocale;
import com.netflix.mediaclient.service.NetflixService;
import java.util.Collections;
import java.util.ArrayList;
import com.netflix.mediaclient.service.ServiceAgent$UserAgentInterface;
import android.content.Context;
import android.content.BroadcastReceiver;
import android.media.AudioManager$OnAudioFocusChangeListener;
import com.netflix.mediaclient.servicemgr.IVoip$OutboundCallListener;
import java.util.List;
import com.vailsys.whistleengine.WhistleEngine;
import com.netflix.mediaclient.servicemgr.IVoip$ConnectivityState;
import android.content.ServiceConnection;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.ThreadFactory;
import com.vailsys.whistleengine.WhistleEngineDelegate;
import com.netflix.mediaclient.servicemgr.IVoip;
import com.netflix.mediaclient.service.ServiceAgent;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import com.netflix.mediaclient.Log;
import com.netflix.mediaclient.util.ThreadUtils;

class WhistleVoipAgent$3 implements Runnable
{
    final /* synthetic */ WhistleVoipAgent this$0;
    
    WhistleVoipAgent$3(final WhistleVoipAgent this$0) {
        this.this$0 = this$0;
    }
    
    @Override
    public void run() {
        ThreadUtils.assertNotOnMain();
        setUrgentAudioThreadPriority();
        if (!this.this$0.mDialRequested.get()) {
            Log.d("nf_voip", "No dial request, no need to dial");
            return;
        }
        if (this.this$0.mCurrentCall != null) {
            this.this$0.mDialRequested.set(false);
            Log.e("nf_voip", "Call is already in progress! Terminate it first!");
            return;
        }
        final int access$900 = this.this$0.findLine();
        if (access$900 < 0) {
            Log.e("nf_voip", "No lines available!");
            this.this$0.callFailed(access$900, null, -1);
            this.this$0.getService().getErrorHandler().addError(VoipErrorDialogDescriptorFactory.getHandlerForNoLineAvailable(this.this$0.getContext()));
            return;
        }
        final int dial = this.this$0.mEngine.dial(access$900, VoipUtils.getCustomerServiceNumber(this.this$0.getAppLocale()), VoipUtils.createDialExtra(this.this$0.getContext(), this.this$0.mSharedSessionId));
        if (dial > 0) {
            Log.d("nf_voip", "Whistle engine was able to start dial");
            this.this$0.mCurrentCall = new WhistleVoipAgent$WhistleCall(dial);
            this.this$0.mLockManager.callStarted();
            this.this$0.requestAudioFocus();
            LocalBroadcastManager.getInstance(this.this$0.getContext()).sendBroadcast(new Intent("com.netflix.mediaclient.ui.cs.ACTION_CALL_STARTED"));
            this.this$0.mNotificationManager.showCallingNotification(this.this$0.getService(), this.this$0.getMainHandler());
            return;
        }
        Log.e("nf_voip", "Whistle engine was unable to start dial!");
        this.this$0.getService().getErrorHandler().addError(VoipErrorDialogDescriptorFactory.getHandlerForCallFailed(this.this$0.getContext(), this.this$0.cancelAction));
    }
}
