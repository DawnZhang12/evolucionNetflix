// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.mediaclient.service;

import com.netflix.mediaclient.service.webclient.model.leafs.VoipConfiguration;
import com.netflix.mediaclient.media.VideoResolutionRange;
import com.netflix.mediaclient.service.webclient.model.leafs.SubtitleDownloadRetryPolicy;
import com.netflix.mediaclient.service.configuration.SubtitleConfiguration;
import com.netflix.mediaclient.service.webclient.model.leafs.SignInConfigData;
import com.netflix.mediaclient.service.webclient.model.leafs.PreviewContentConfigData;
import com.netflix.mediaclient.service.configuration.PlaybackConfiguration;
import com.netflix.mediaclient.service.webclient.model.leafs.NrmConfigData;
import com.netflix.mediaclient.service.configuration.MdxConfiguration;
import com.netflix.mediaclient.service.configuration.KubrickConfiguration;
import org.json.JSONObject;
import com.netflix.mediaclient.service.net.IpConnectivityPolicy;
import com.netflix.mediaclient.service.configuration.esn.EsnProvider;
import com.netflix.mediaclient.service.webclient.model.leafs.ErrorLoggingSpecification;
import com.netflix.mediaclient.service.configuration.drm.DrmManager;
import com.netflix.mediaclient.util.DeviceCategory;
import com.netflix.mediaclient.media.PlayerType;
import com.netflix.mediaclient.service.webclient.model.leafs.ConsolidatedLoggingSessionSpecification;
import android.util.Pair;
import com.netflix.mediaclient.service.webclient.model.leafs.BreadcrumbLoggingSpecification;
import com.netflix.mediaclient.service.webclient.model.leafs.ABTestConfig$Cell;
import com.netflix.mediaclient.service.webclient.model.leafs.DataSaveConfigData;
import com.netflix.mediaclient.service.webclient.ApiEndpointRegistry;
import com.netflix.mediaclient.service.webclient.model.leafs.ABTestConfig;
import com.netflix.mediaclient.service.configuration.ConfigurationAgentWebCallback;

public interface ServiceAgent$ConfigurationAgentInterface
{
    void clearAccountConfigData();
    
    boolean enableHTTPSAuth();
    
    boolean enableLowBitrateStreams();
    
    void esnMigrationComplete();
    
    void fetchAccountConfigData(final ConfigurationAgentWebCallback p0);
    
    ABTestConfig getABTestConfiguration_6634();
    
    ABTestConfig getABTestConfiguration_6725();
    
    String getAlertMsgForMissingLocale();
    
    ApiEndpointRegistry getApiEndpointRegistry();
    
    int getApmUserSessionDurationInSeconds();
    
    int getAppVersionCode();
    
    DataSaveConfigData getBWSaveConfigData();
    
    ABTestConfig$Cell getBrandLoveSurveyConfig();
    
    BreadcrumbLoggingSpecification getBreadcrumbLoggingSpecification();
    
    ABTestConfig$Cell getCWProgressBarConfig();
    
    Pair<String, byte[]> getCastPrefetchSharedSecret();
    
    ConsolidatedLoggingSessionSpecification getConsolidatedLoggingSessionSpecification(final String p0);
    
    ABTestConfig$Cell getCoppola1Experience();
    
    ABTestConfig$Cell getCoppola2Experience();
    
    PlayerType getCurrentPlayerType();
    
    int getDataRequestTimeout();
    
    DeviceCategory getDeviceCategory();
    
    int getDiskCacheSizeBytes();
    
    ABTestConfig$Cell getDisplayPageRefreshConfig();
    
    DrmManager getDrmManager();
    
    ErrorLoggingSpecification getErrorLoggingSpecification();
    
    EsnProvider getEsnProvider();
    
    long getImageCacheMinimumTtl();
    
    int getImageCacheSizeBytes();
    
    IpConnectivityPolicy getIpConnectivityPolicy();
    
    JSONObject getJPlayerConfig();
    
    int getJPlayerStreamErrorRestartCount();
    
    KubrickConfiguration getKubrickConfiguration();
    
    MdxConfiguration getMdxConfiguration();
    
    ABTestConfig$Cell getMementoConfig();
    
    ABTestConfig$Cell getMotionBBTestConfig();
    
    NrmConfigData getNrmConfigData();
    
    ABTestConfig$Cell getPhoneOrientationConfig();
    
    PlaybackConfiguration getPlaybackConfiguration();
    
    String getPreAppPartnerExperience();
    
    String getPreAppWidgetExperience();
    
    int getPresentationTrackingAggregationSize();
    
    PreviewContentConfigData getPreviewContentConfiguration();
    
    ABTestConfig$Cell getPushNotifOptInConfig();
    
    int getRateLimitForGcmBrowseEvents();
    
    int getRateLimitForNListChangeEvents();
    
    int getResFetcherThreadPoolSize();
    
    int getResourceRequestTimeout();
    
    SignInConfigData getSignInConfigData();
    
    String getSoftwareVersion();
    
    String getStreamingQoe();
    
    SubtitleConfiguration getSubtitleConfiguration();
    
    SubtitleDownloadRetryPolicy getSubtitleDownloadRetryPolicy();
    
    int getVideoBufferSize();
    
    VideoResolutionRange getVideoResolutionRange();
    
    ABTestConfig$Cell getVoiceSearchABTestConfig();
    
    VoipConfiguration getVoipConfiguration();
    
    boolean ignorePreloadForPlayBilling();
    
    boolean isCurrentDrmWidevine();
    
    boolean isDeviceLowMem();
    
    boolean isDisableCastFaststart();
    
    boolean isDisableMcQueenV2();
    
    boolean isDolbyDigitalPlus51Supported();
    
    boolean isDynecomSignInEnabled();
    
    boolean isEsnMigrationRequired();
    
    boolean isLogoutRequired();
    
    boolean isPlayBillingDisabled();
    
    boolean isPreviewContentEnabled();
    
    boolean isWidevineL1Enabled();
    
    boolean isWidevineL3ABTestEnabled();
    
    boolean isWidevineL3Enabled();
    
    boolean shouldAlertForMissingLocale();
    
    boolean shouldDisableVoip();
    
    void userAgentLogoutComplete();
    
    void verifyLoginViaDynecom(final String p0, final String p1, final ConfigurationAgentWebCallback p2);
}
