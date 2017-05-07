// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.mediaclient.service;

import java.util.Set;
import android.os.Process;
import com.netflix.mediaclient.javabridge.ui.ActivationTokens;
import com.netflix.mediaclient.servicemgr.SignUpParams;
import com.netflix.mediaclient.servicemgr.IPushNotification;
import com.netflix.mediaclient.repository.SecurityRepository;
import com.netflix.mediaclient.servicemgr.NrdpComponent;
import com.netflix.mediaclient.servicemgr.IPlayer;
import com.netflix.mediaclient.servicemgr.IMdx;
import com.netflix.mediaclient.util.gfx.ImageLoader;
import com.netflix.mediaclient.service.configuration.esn.EsnProvider;
import com.netflix.mediaclient.servicemgr.IDiagnosis;
import com.netflix.mediaclient.util.DeviceCategory;
import com.netflix.mediaclient.servicemgr.IClientLogging;
import com.netflix.mediaclient.servicemgr.IBrowseInterface;
import com.netflix.mediaclient.servicemgr.model.user.UserProfile;
import com.netflix.mediaclient.service.resfetcher.ResourceFetcherCallback;
import com.netflix.mediaclient.servicemgr.IClientLogging$AssetType;
import com.netflix.mediaclient.servicemgr.ApplicationPerformanceMetricsLogging$Trigger;
import java.util.Iterator;
import android.support.v4.content.LocalBroadcastManager;
import java.io.Serializable;
import android.content.IntentFilter;
import com.netflix.mediaclient.util.ThreadUtils;
import android.content.Context;
import com.netflix.mediaclient.util.ConnectivityUtils;
import com.netflix.mediaclient.util.AndroidUtils;
import com.netflix.mediaclient.util.StringUtils;
import android.content.Intent;
import com.netflix.mediaclient.android.app.CommonStatus;
import com.netflix.mediaclient.service.user.UserAgent;
import com.netflix.mediaclient.service.resfetcher.ResourceFetcher;
import com.netflix.mediaclient.service.pushnotification.PushNotificationAgent;
import com.netflix.mediaclient.service.preapp.PreAppAgent;
import com.netflix.mediaclient.service.player.PlayerAgent;
import android.content.BroadcastReceiver;
import com.netflix.mediaclient.service.mdx.MdxAgent;
import java.util.ArrayList;
import com.netflix.mediaclient.service.falkor.FalkorAgent;
import com.netflix.mediaclient.service.falkor.FalkorAccess;
import com.netflix.mediaclient.service.diagnostics.DiagnosisAgent;
import com.netflix.mediaclient.service.configuration.ConfigurationAgent;
import com.netflix.mediaclient.service.logging.LoggingAgent;
import com.netflix.mediaclient.service.browse.BrowseAgent;
import android.os.IBinder;
import android.os.Handler;
import com.netflix.mediaclient.servicemgr.INetflixService;
import android.app.Service;
import com.netflix.mediaclient.service.webclient.model.leafs.social.SocialNotificationsList;
import com.netflix.mediaclient.service.webclient.model.leafs.social.FriendForRecommendation;
import com.netflix.mediaclient.Log;
import com.netflix.mediaclient.servicemgr.INetflixServiceCallback;
import com.netflix.mediaclient.android.app.Status;
import com.netflix.mediaclient.service.webclient.model.leafs.AvatarInfo;
import java.util.List;
import com.netflix.mediaclient.service.user.UserAgent$UserAgentCallback;

class NetflixService$UserAgentClientCallback implements UserAgent$UserAgentCallback
{
    private final int clientId;
    private final int requestId;
    final /* synthetic */ NetflixService this$0;
    
    NetflixService$UserAgentClientCallback(final NetflixService this$0, final int clientId, final int requestId) {
        this.this$0 = this$0;
        this.clientId = clientId;
        this.requestId = requestId;
    }
    
    @Override
    public void onAvailableAvatarsListFetched(final List<AvatarInfo> list, final Status status) {
        final INetflixServiceCallback netflixServiceCallback = (INetflixServiceCallback)this.this$0.mClientCallbacks.get(this.clientId);
        if (netflixServiceCallback == null) {
            Log.w("NetflixService", "No client callback found for onAvailableAvatarsListFetched");
            return;
        }
        Log.d("NetflixService", "Notified onAvailableAvatarsListFetched");
        netflixServiceCallback.onAvailableAvatarsListFetched(this.requestId, list, status);
    }
    
    @Override
    public void onConnectWithFacebook(final Status status) {
        final INetflixServiceCallback netflixServiceCallback = (INetflixServiceCallback)this.this$0.mClientCallbacks.get(this.clientId);
        if (netflixServiceCallback == null) {
            Log.w("NetflixService", "No client callback found for onConnectWithFacebook");
            return;
        }
        Log.d("NetflixService", "Notified onConnectWithFacebook");
        netflixServiceCallback.onConnectWithFacebookComplete(this.requestId, status);
    }
    
    @Override
    public void onFriendsForRecommendationsListFetched(final List<FriendForRecommendation> list, final Status status) {
        final INetflixServiceCallback netflixServiceCallback = (INetflixServiceCallback)this.this$0.mClientCallbacks.get(this.clientId);
        if (netflixServiceCallback == null) {
            Log.w("NetflixService", "No client callback found for onFriendsForRecommendationsListFetched");
            return;
        }
        Log.d("NetflixService", "Notified onFriendsForRecommendationsListFetched");
        netflixServiceCallback.onFriendsForRecommendationsListFetched(this.requestId, list, status);
    }
    
    @Override
    public void onLoginComplete(final Status status) {
        final INetflixServiceCallback netflixServiceCallback = (INetflixServiceCallback)this.this$0.mClientCallbacks.get(this.clientId);
        if (netflixServiceCallback == null) {
            Log.w("NetflixService", "No client callback found for onLoginComplete");
            return;
        }
        Log.d("NetflixService", "Notified onLoginComplete");
        netflixServiceCallback.onLoginComplete(this.requestId, status);
    }
    
    @Override
    public void onLogoutComplete(final Status status) {
        final INetflixServiceCallback netflixServiceCallback = (INetflixServiceCallback)this.this$0.mClientCallbacks.get(this.clientId);
        if (netflixServiceCallback == null) {
            Log.w("NetflixService", "No client callback found for onLogoutComplete");
            return;
        }
        Log.d("NetflixService", "Notified onLogoutComplete");
        netflixServiceCallback.onLogoutComplete(this.requestId, status);
    }
    
    @Override
    public void onPinVerified(final boolean b, final Status status) {
        final INetflixServiceCallback netflixServiceCallback = (INetflixServiceCallback)this.this$0.mClientCallbacks.get(this.clientId);
        if (netflixServiceCallback == null) {
            Log.w("NetflixService", "No client callback found for onPinVerified");
            return;
        }
        Log.d("NetflixService", "Notified onPinVerified");
        netflixServiceCallback.onPinVerified(this.requestId, b, status);
    }
    
    @Override
    public void onProfilesListUpdateResult(final Status status) {
        final INetflixServiceCallback netflixServiceCallback = (INetflixServiceCallback)this.this$0.mClientCallbacks.get(this.clientId);
        if (netflixServiceCallback == null) {
            Log.w("NetflixService", "No client callback found for onProfilesListUpdateResult");
            return;
        }
        Log.d("NetflixService", "Notified onProfilesListUpdateResult");
        netflixServiceCallback.onProfileListUpdateStatus(this.requestId, status);
    }
    
    @Override
    public void onSocialNotificationsListFetched(final SocialNotificationsList list, final Status status) {
        final INetflixServiceCallback netflixServiceCallback = (INetflixServiceCallback)this.this$0.mClientCallbacks.get(this.clientId);
        if (netflixServiceCallback == null) {
            Log.w("NetflixService", "No client callback found for onSocialNotificationsListFetched");
            return;
        }
        Log.d("NetflixService", "Notified onSocialNotificationsListFetched");
        netflixServiceCallback.onSocialNotificationsListFetched(this.requestId, list, status);
    }
}