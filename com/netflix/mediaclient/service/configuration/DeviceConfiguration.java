// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.mediaclient.service.configuration;

import com.netflix.mediaclient.service.webclient.model.leafs.DeviceConfigData;
import com.netflix.mediaclient.util.DeviceCategory;
import com.netflix.mediaclient.util.StringUtils;
import com.netflix.mediaclient.service.webclient.volley.FalkorParseUtils;
import java.util.Iterator;
import java.util.List;
import com.netflix.mediaclient.util.DeviceUtils;
import com.netflix.mediaclient.Log;
import com.netflix.mediaclient.util.PreferenceUtils;
import java.util.HashMap;
import com.netflix.mediaclient.service.net.IpConnectivityPolicy;
import com.netflix.mediaclient.service.webclient.model.leafs.ErrorLoggingSpecification;
import android.content.Context;
import com.netflix.mediaclient.service.webclient.model.leafs.ConsolidatedLoggingSessionSpecification;
import java.util.Map;
import com.netflix.mediaclient.service.webclient.model.leafs.BreadcrumbLoggingSpecification;

public class DeviceConfiguration
{
    private static final int DEFAULT_SAMPLERATE = 8000;
    private static final boolean DISABLE_MDX_DEF = false;
    private static final boolean DISABLE_WEBSOCKET_DEF = true;
    private static final boolean FORCE_DISABLE_VOIP_IN_CODE = false;
    private static final int MAX_SAMPLERATE_48K = 48000;
    private static final int MIN_SAMPLERATE_8K = 8000;
    private static String TAG;
    private String mAlertMsgForMissingLocale;
    private int mAppMinimalVersion;
    private int mAppRecommendedVersion;
    private int mAudioFormat;
    private BreadcrumbLoggingSpecification mBreadcrumbLoggingSpecification;
    private Map<String, ConsolidatedLoggingSessionSpecification> mConsolidatedLoggingSpecification;
    private Context mContext;
    private DeviceRepository mDeviceRepository;
    private ErrorLoggingSpecification mErrorLoggingSpecification;
    private ImagePrefRepository mImagePrefRepository;
    private IpConnectivityPolicy mIpConnectivityPolicy;
    private boolean mIsDisableMdx;
    private boolean mIsDisableWebsocket;
    private boolean mIsVoipEnabledOnDevice;
    private boolean mIsWidevineDisabled;
    private boolean mIsWidevineL1Enabled;
    private boolean mIsWidevineL3Enabled;
    private int mJPlayerErrorRestartCount;
    private boolean mLocalPlaybackEnabled;
    private boolean mMdxRemoteControlLockScreenEnabled;
    private boolean mMdxRemoteControlNotificationEnabled;
    private int mPTAggregationSize;
    private int mRateLimitForGcmBrowseEvents;
    private int mRateLimitForNListChangeEvents;
    private boolean mShouldAlertForMissingLocale;
    private SignUpConfiguration mSignUpConfig;
    private SubtitleConfiguration mSubtitleConfiguration;
    private int mUserSessionDurationInSeconds;
    private int mVideoResolutionOverride;
    private int mVoipSampleRateInHz;
    
    static {
        DeviceConfiguration.TAG = "nf_configuration_device";
    }
    
    public DeviceConfiguration(final Context mContext) {
        this.mSubtitleConfiguration = SubtitleConfiguration.ENHANCED_XML;
        this.mConsolidatedLoggingSpecification = new HashMap<String, ConsolidatedLoggingSessionSpecification>();
        this.mVoipSampleRateInHz = 8000;
        this.mContext = mContext;
        this.mDeviceRepository = new DeviceRepository(this.mContext);
        this.mImagePrefRepository = new ImagePrefRepository(this.mContext);
        this.mSignUpConfig = new SignUpConfiguration(this.mContext);
        this.mSubtitleConfiguration = SubtitleConfiguration.load(this.mContext);
        this.mConsolidatedLoggingSpecification = this.loadConsolidateLoggingSpecification();
        this.mIpConnectivityPolicy = this.loadIpConnectivityPolicy();
        this.mPTAggregationSize = PreferenceUtils.getIntPref(this.mContext, "pt_aggregation_size", -1);
        this.mAppRecommendedVersion = PreferenceUtils.getIntPref(this.mContext, "config_recommended_version", -1);
        this.mAppMinimalVersion = PreferenceUtils.getIntPref(this.mContext, "config_min_version", -1);
        this.mIsDisableMdx = PreferenceUtils.getBooleanPref(this.mContext, "disable_mdx", false);
        this.mIsDisableWebsocket = PreferenceUtils.getBooleanPref(this.mContext, "disable_websocket", true);
        this.mIsWidevineDisabled = PreferenceUtils.getBooleanPref(this.mContext, "disable_widevine", false);
        if (Log.isLoggable()) {
            Log.d(DeviceConfiguration.TAG, "Disable Widevine is " + this.mIsWidevineDisabled);
        }
        if (this.mIsWidevineDisabled) {
            this.mIsWidevineL1Enabled = false;
            this.mIsWidevineL3Enabled = false;
        }
        else {
            this.mIsWidevineL1Enabled = PreferenceUtils.getBooleanPref(this.mContext, "enable_widevine_l1", false);
            this.mIsWidevineL3Enabled = PreferenceUtils.getBooleanPref(this.mContext, "enable_widevine_l3", false);
        }
        this.mIsVoipEnabledOnDevice = PreferenceUtils.getBooleanPref(this.mContext, "enable_voip_on_device", false);
        this.mUserSessionDurationInSeconds = this.loadUserSessionTimeoutDuration();
        this.mBreadcrumbLoggingSpecification = BreadcrumbLoggingSpecification.loadFromPreferences(mContext);
        this.mErrorLoggingSpecification = ErrorLoggingSpecification.loadFromPreferences(mContext);
        this.mVideoResolutionOverride = PreferenceUtils.getIntPref(this.mContext, "video_resolution_override", 0);
        this.mRateLimitForGcmBrowseEvents = PreferenceUtils.getIntPref(this.mContext, "gcm_browse_rate_limit", 0);
        this.mRateLimitForNListChangeEvents = PreferenceUtils.getIntPref(this.mContext, "gcm_tray_change_rate_limit", 0);
        this.mLocalPlaybackEnabled = PreferenceUtils.getBooleanPref(this.mContext, "playback_configuration_local_playback_enabled", DeviceUtils.isLocalPlaybackEnabled());
        this.mMdxRemoteControlLockScreenEnabled = PreferenceUtils.getBooleanPref(this.mContext, "mdx_configuration_remote_lockscreen_enabled", DeviceUtils.isRemoteControlEnabled());
        this.mMdxRemoteControlNotificationEnabled = PreferenceUtils.getBooleanPref(this.mContext, "mdx_configuration_remote_notification_enabled", DeviceUtils.isRemoteControlEnabled());
        this.mJPlayerErrorRestartCount = PreferenceUtils.getIntPref(this.mContext, "jplayer_restart_count", 2);
        this.mShouldAlertForMissingLocale = PreferenceUtils.getBooleanPref(this.mContext, "device_locale_not_supported", false);
        this.mAlertMsgForMissingLocale = PreferenceUtils.getStringPref(this.mContext, "device_locale_not_supported_msg", null);
        this.mVoipSampleRateInHz = PreferenceUtils.getIntPref(this.mContext, "voip_samplerate_hz", 8000);
        if (Log.isLoggable()) {
            Log.d(DeviceConfiguration.TAG, "constructor DeviceConfiguration: Disable mIsVoipEnabledOnDevice " + this.mIsVoipEnabledOnDevice + ", disabledInCode? " + false);
        }
    }
    
    private Map<String, ConsolidatedLoggingSessionSpecification> loadConsolidateLoggingSpecification() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aload_0        
        //     1: getfield        com/netflix/mediaclient/service/configuration/DeviceConfiguration.mContext:Landroid/content/Context;
        //     4: ldc_w           "cl_configuration"
        //     7: aconst_null    
        //     8: invokestatic    com/netflix/mediaclient/util/PreferenceUtils.getStringPref:(Landroid/content/Context;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
        //    11: astore_1       
        //    12: aload_1        
        //    13: invokestatic    com/netflix/mediaclient/util/StringUtils.isEmpty:(Ljava/lang/String;)Z
        //    16: ifeq            37
        //    19: getstatic       com/netflix/mediaclient/service/configuration/DeviceConfiguration.TAG:Ljava/lang/String;
        //    22: ldc_w           "CL specification not found in file system"
        //    25: invokestatic    com/netflix/mediaclient/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //    28: pop            
        //    29: new             Ljava/util/HashMap;
        //    32: dup            
        //    33: invokespecial   java/util/HashMap.<init>:()V
        //    36: areturn        
        //    37: new             Lcom/netflix/mediaclient/service/configuration/DeviceConfiguration$1;
        //    40: dup            
        //    41: aload_0        
        //    42: invokespecial   com/netflix/mediaclient/service/configuration/DeviceConfiguration$1.<init>:(Lcom/netflix/mediaclient/service/configuration/DeviceConfiguration;)V
        //    45: invokevirtual   com/netflix/mediaclient/service/configuration/DeviceConfiguration$1.getType:()Ljava/lang/reflect/Type;
        //    48: astore_2       
        //    49: invokestatic    com/netflix/mediaclient/service/webclient/volley/FalkorParseUtils.getGson:()Lcom/google/gson/Gson;
        //    52: aload_1        
        //    53: aload_2        
        //    54: invokevirtual   com/google/gson/Gson.fromJson:(Ljava/lang/String;Ljava/lang/reflect/Type;)Ljava/lang/Object;
        //    57: checkcast       Ljava/util/List;
        //    60: astore_1       
        //    61: getstatic       com/netflix/mediaclient/service/configuration/DeviceConfiguration.TAG:Ljava/lang/String;
        //    64: ldc_w           "CL specification loaded from file system"
        //    67: invokestatic    com/netflix/mediaclient/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //    70: pop            
        //    71: aload_1        
        //    72: invokestatic    com/netflix/mediaclient/service/configuration/DeviceConfiguration.toMap:(Ljava/util/List;)Ljava/util/Map;
        //    75: areturn        
        //    76: astore_2       
        //    77: aconst_null    
        //    78: astore_1       
        //    79: getstatic       com/netflix/mediaclient/service/configuration/DeviceConfiguration.TAG:Ljava/lang/String;
        //    82: ldc_w           "Failed to load CL specification from file system"
        //    85: aload_2        
        //    86: invokestatic    com/netflix/mediaclient/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //    89: pop            
        //    90: goto            71
        //    93: astore_2       
        //    94: goto            79
        //    Signature:
        //  ()Ljava/util/Map<Ljava/lang/String;Lcom/netflix/mediaclient/service/webclient/model/leafs/ConsolidatedLoggingSessionSpecification;>;
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  37     61     76     79     Ljava/lang/Throwable;
        //  61     71     93     97     Ljava/lang/Throwable;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0071:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2592)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:757)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:655)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:532)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:499)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:141)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:130)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:105)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:317)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:238)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:138)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    private IpConnectivityPolicy loadIpConnectivityPolicy() {
        return IpConnectivityPolicy.from(PreferenceUtils.getIntPref(this.mContext, "ip_connectivity_policy_overide", Integer.MIN_VALUE));
    }
    
    private int loadUserSessionTimeoutDuration() {
        return PreferenceUtils.getIntPref(this.mContext, "apm_user_session_timeout_duration_override", Integer.MIN_VALUE);
    }
    
    private static Map<String, ConsolidatedLoggingSessionSpecification> toMap(final List<ConsolidatedLoggingSessionSpecification> list) {
        final HashMap<String, ConsolidatedLoggingSessionSpecification> hashMap = new HashMap<String, ConsolidatedLoggingSessionSpecification>();
        if (list != null) {
            for (final ConsolidatedLoggingSessionSpecification consolidatedLoggingSessionSpecification : list) {
                if (consolidatedLoggingSessionSpecification != null && consolidatedLoggingSessionSpecification.getSession() != null) {
                    hashMap.put(consolidatedLoggingSessionSpecification.getSession(), consolidatedLoggingSessionSpecification);
                }
            }
        }
        return hashMap;
    }
    
    private void updateAudioFormat(final int mAudioFormat) {
        PreferenceUtils.putIntPref(this.mContext, "supported_audio_format", mAudioFormat);
        this.mAudioFormat = mAudioFormat;
    }
    
    private void updateConsolidatedLoggingSpecification(final List<ConsolidatedLoggingSessionSpecification> list) {
        this.mConsolidatedLoggingSpecification = toMap(list);
        if (list == null || list.size() < 1) {
            PreferenceUtils.removePref(this.mContext, "cl_configuration");
            return;
        }
        PreferenceUtils.putStringPref(this.mContext, "cl_configuration", FalkorParseUtils.getGson().toJson(list));
    }
    
    private void updateDeviceConfigFlag(final boolean b) {
        Log.d(DeviceConfiguration.TAG, "setting DeviceConfig preferences inCache= " + b);
        PreferenceUtils.putBooleanPref(this.mContext, "nf_device_config_cached", b);
    }
    
    private void updateDeviceLocaleSupportAlert(final boolean mShouldAlertForMissingLocale, final String mAlertMsgForMissingLocale) {
        if (Log.isLoggable()) {
            Log.d(DeviceConfiguration.TAG, String.format("nf_loc shouldAlert: %b, alertMsg:%s", mShouldAlertForMissingLocale, mAlertMsgForMissingLocale));
        }
        if (mShouldAlertForMissingLocale != this.mShouldAlertForMissingLocale) {
            PreferenceUtils.putBooleanPref(this.mContext, "device_locale_not_supported", mShouldAlertForMissingLocale);
            this.mShouldAlertForMissingLocale = mShouldAlertForMissingLocale;
        }
        if (!StringUtils.safeEquals(mAlertMsgForMissingLocale, this.mAlertMsgForMissingLocale)) {
            PreferenceUtils.putStringPref(this.mContext, "device_locale_not_supported_msg", mAlertMsgForMissingLocale);
            this.mAlertMsgForMissingLocale = mAlertMsgForMissingLocale;
        }
    }
    
    private void updateDisableMdxFlag(final String s) {
        final boolean mIsDisableMdx = StringUtils.isNotEmpty(s) && Boolean.parseBoolean(s);
        PreferenceUtils.putBooleanPref(this.mContext, "disable_mdx", mIsDisableMdx);
        this.mIsDisableMdx = mIsDisableMdx;
    }
    
    private void updateDisableWebsocketFlag(final String s) {
        final boolean mIsDisableWebsocket = !StringUtils.isNotEmpty(s) || Boolean.parseBoolean(s);
        PreferenceUtils.putBooleanPref(this.mContext, "disable_websocket", mIsDisableWebsocket);
        this.mIsDisableWebsocket = mIsDisableWebsocket;
    }
    
    private void updateLocalPlaybackStatus(final String s) {
        if (StringUtils.isNotEmpty(s)) {
            final boolean boolean1 = Boolean.parseBoolean(s);
            if (Log.isLoggable()) {
                Log.d(DeviceConfiguration.TAG, "Change in local playback status from " + this.mLocalPlaybackEnabled + " to " + boolean1);
            }
            if (this.mLocalPlaybackEnabled != boolean1) {
                PreferenceUtils.putBooleanPref(this.mContext, "playback_configuration_local_playback_enabled", boolean1);
                this.mLocalPlaybackEnabled = boolean1;
            }
        }
        else {
            PreferenceUtils.removePref(this.mContext, "playback_configuration_local_playback_enabled");
            this.mLocalPlaybackEnabled = DeviceUtils.isLocalPlaybackEnabled();
            if (Log.isLoggable()) {
                Log.d(DeviceConfiguration.TAG, "Overide is not found, use default for local playback enabled " + this.mLocalPlaybackEnabled);
            }
        }
    }
    
    private void updateMdxRemoteControlLockScreenStatus(final String s) {
        if (StringUtils.isNotEmpty(s)) {
            final boolean boolean1 = Boolean.parseBoolean(s);
            if (Log.isLoggable()) {
                Log.d(DeviceConfiguration.TAG, "Change in MDX remote control lock screen be used status from " + this.mMdxRemoteControlLockScreenEnabled + " to " + boolean1);
            }
            if (this.mMdxRemoteControlLockScreenEnabled != boolean1) {
                PreferenceUtils.putBooleanPref(this.mContext, "mdx_configuration_remote_lockscreen_enabled", boolean1);
                this.mMdxRemoteControlLockScreenEnabled = boolean1;
            }
        }
        else {
            PreferenceUtils.removePref(this.mContext, "mdx_configuration_remote_lockscreen_enabled");
            this.mMdxRemoteControlLockScreenEnabled = DeviceUtils.isRemoteControlEnabled();
            if (Log.isLoggable()) {
                Log.d(DeviceConfiguration.TAG, "Overide is not found, use default on device for MDX remote control lock screen  " + this.mMdxRemoteControlLockScreenEnabled);
            }
        }
    }
    
    private void updateMdxRemoteControlNotificationStatus(final String s) {
        if (StringUtils.isNotEmpty(s)) {
            final boolean boolean1 = Boolean.parseBoolean(s);
            if (Log.isLoggable()) {
                Log.d(DeviceConfiguration.TAG, "Change in MDX remote control notification be used status from " + this.mMdxRemoteControlNotificationEnabled + " to " + boolean1);
            }
            if (this.mMdxRemoteControlNotificationEnabled != boolean1) {
                PreferenceUtils.putBooleanPref(this.mContext, "mdx_configuration_remote_notification_enabled", boolean1);
                this.mMdxRemoteControlNotificationEnabled = boolean1;
            }
        }
        else {
            PreferenceUtils.removePref(this.mContext, "mdx_configuration_remote_notification_enabled");
            this.mMdxRemoteControlNotificationEnabled = DeviceUtils.isRemoteControlEnabled();
            if (Log.isLoggable()) {
                Log.d(DeviceConfiguration.TAG, "Overide is not found, use default on device for MDX remote control notification " + this.mMdxRemoteControlNotificationEnabled);
            }
        }
    }
    
    private void updateVoipEnabledOnDeviceFlag(final boolean mIsVoipEnabledOnDevice) {
        PreferenceUtils.putBooleanPref(this.mContext, "enable_voip_on_device", mIsVoipEnabledOnDevice);
        this.mIsVoipEnabledOnDevice = mIsVoipEnabledOnDevice;
    }
    
    private void updateVoipSampleRate(final int mVoipSampleRateInHz) {
        if (mVoipSampleRateInHz >= 8000 && mVoipSampleRateInHz <= 48000) {
            if (Log.isLoggable()) {
                Log.d(DeviceConfiguration.TAG, "New sample rate of " + mVoipSampleRateInHz + " Hz is in range of 4KHz - 48KHz");
            }
            PreferenceUtils.putIntPref(this.mContext, "voip_samplerate_hz", mVoipSampleRateInHz);
            this.mVoipSampleRateInHz = mVoipSampleRateInHz;
        }
        else if (Log.isLoggable()) {
            Log.e(DeviceConfiguration.TAG, "New sample rate of " + mVoipSampleRateInHz + " Hz is NOT in range of 4KHz - 48KHz. Not changing existing sample rate...");
        }
    }
    
    private void updateWidevineL1Flag(final boolean mIsWidevineL1Enabled) {
        PreferenceUtils.putBooleanPref(this.mContext, "enable_widevine_l1", mIsWidevineL1Enabled);
        if (this.mIsWidevineDisabled) {
            Log.w(DeviceConfiguration.TAG, "Unable to enable Widevine L1 in runtime because of master Widevine disable");
            return;
        }
        this.mIsWidevineL1Enabled = mIsWidevineL1Enabled;
    }
    
    private void updateWidevineL3Flag(final boolean mIsWidevineL3Enabled) {
        PreferenceUtils.putBooleanPref(this.mContext, "enable_widevine_l3", mIsWidevineL3Enabled);
        if (this.mIsWidevineDisabled) {
            Log.w(DeviceConfiguration.TAG, "Unable to enable Widevine L3 in runtime because of master Widevine disable");
            return;
        }
        this.mIsWidevineL3Enabled = mIsWidevineL3Enabled;
    }
    
    public void clear() {
        this.mDeviceRepository = null;
        this.mImagePrefRepository = null;
        this.mSignUpConfig = null;
    }
    
    public boolean enableWidevineL1() {
        return this.mIsWidevineL1Enabled;
    }
    
    public boolean enableWidevineL3() {
        return this.mIsWidevineL3Enabled;
    }
    
    public String getAlertMsgForMissingLocale() {
        return this.mAlertMsgForMissingLocale;
    }
    
    public int getAppMinimalVersion() {
        return this.mAppMinimalVersion;
    }
    
    public int getAppRecommendedVersion() {
        return this.mAppRecommendedVersion;
    }
    
    public BreadcrumbLoggingSpecification getBreadcrumbLoggingSpecification() {
        return this.mBreadcrumbLoggingSpecification;
    }
    
    public DeviceCategory getCategory() {
        return this.mDeviceRepository.getCategory();
    }
    
    public ConsolidatedLoggingSessionSpecification getConsolidatedLoggingSessionSpecification(final String s) {
        if (StringUtils.isEmpty(s)) {
            return null;
        }
        return this.mConsolidatedLoggingSpecification.get(s);
    }
    
    public ErrorLoggingSpecification getErrorLoggingSpecification() {
        return this.mErrorLoggingSpecification;
    }
    
    public ImagePrefRepository getImageRepository() {
        return this.mImagePrefRepository;
    }
    
    public IpConnectivityPolicy getIpConnectivityPolicy() {
        return this.mIpConnectivityPolicy;
    }
    
    public int getJPlayerStreamErrorRestartCount() {
        return this.mJPlayerErrorRestartCount;
    }
    
    public int getPTAggregationSize() {
        return this.mPTAggregationSize;
    }
    
    public int getRateLimitForGcmBrowseEvents() {
        return this.mRateLimitForGcmBrowseEvents;
    }
    
    public int getRateLimitForNListChangeEvents() {
        return this.mRateLimitForNListChangeEvents;
    }
    
    public SignUpConfiguration getSignUpConfiguration() {
        return this.mSignUpConfig;
    }
    
    public SubtitleConfiguration getSubtitleConfiguration() {
        return this.mSubtitleConfiguration;
    }
    
    public int getUserSessionDurationInSeconds() {
        return this.mUserSessionDurationInSeconds;
    }
    
    public int getVideoResolutionOverride() {
        return this.mVideoResolutionOverride;
    }
    
    public int getVoipSampleRate() {
        return this.mVoipSampleRateInHz;
    }
    
    public int getmAudioFormat() {
        return this.mAudioFormat;
    }
    
    public boolean isDeviceConfigInCache() {
        return PreferenceUtils.getBooleanPref(this.mContext, "nf_device_config_cached", false);
    }
    
    public boolean isDisableMdx() {
        return this.mIsDisableMdx;
    }
    
    public boolean isDisableWebsocket() {
        return this.mIsDisableWebsocket;
    }
    
    public boolean isLocalPlaybackEnabled() {
        return this.mLocalPlaybackEnabled;
    }
    
    public boolean isMdxRemoteControlLockScreenEnabled() {
        return this.mMdxRemoteControlLockScreenEnabled;
    }
    
    public boolean isMdxRemoteControlNotificationEnabled() {
        return this.mMdxRemoteControlNotificationEnabled;
    }
    
    public void persistDeviceConfigOverride(final DeviceConfigData deviceConfigData) {
        final int n = -1;
        if (deviceConfigData != null) {
            if (Log.isLoggable()) {
                Log.d(DeviceConfiguration.TAG, String.format("writing configData to storage %s", deviceConfigData.toString()));
            }
            PlayerTypeFactory.updateDevicePlayerType(this.mContext, deviceConfigData.getPlayerType());
            this.mDeviceRepository.update(this.mContext, deviceConfigData.getDeviceCategory());
            this.mImagePrefRepository.update(this.mContext, deviceConfigData.getImagePref());
            this.mSignUpConfig.update(this.mContext, deviceConfigData.getSignUpEnabled(), deviceConfigData.getSignUpTimeout());
            this.updateDisableWebsocketFlag(deviceConfigData.getWebsocketDisabled());
            this.updateDisableMdxFlag(deviceConfigData.getMdxDisabled());
            this.updateWidevineL1Flag(deviceConfigData.isWidevineL1Enabled());
            this.updateWidevineL3Flag(deviceConfigData.isWidevineL3Enabled());
            this.updateVoipEnabledOnDeviceFlag(deviceConfigData.isVoipEnabledOnDevice());
            this.updateAudioFormat(deviceConfigData.getAudioFormats());
            this.updateConsolidatedLoggingSpecification(deviceConfigData.getConsolidatedloggingSpecification());
            this.mSubtitleConfiguration = SubtitleConfiguration.update(this.mContext, deviceConfigData.getSubtitleConfiguration());
            int int1;
            if (deviceConfigData.getAppMinVresion() != null) {
                int1 = Integer.parseInt(deviceConfigData.getAppMinVresion());
            }
            else {
                int1 = -1;
            }
            this.mAppMinimalVersion = int1;
            PreferenceUtils.putIntPref(this.mContext, "config_min_version", this.mAppMinimalVersion);
            int int2;
            if (deviceConfigData.getAppRecommendedVresion() != null) {
                int2 = Integer.parseInt(deviceConfigData.getAppRecommendedVresion());
            }
            else {
                int2 = -1;
            }
            this.mAppRecommendedVersion = int2;
            PreferenceUtils.putIntPref(this.mContext, "config_recommended_version", this.mAppRecommendedVersion);
            int int3 = n;
            if (StringUtils.isNotEmpty(deviceConfigData.getPTAggregationSize())) {
                int3 = Integer.parseInt(deviceConfigData.getPTAggregationSize());
            }
            this.mPTAggregationSize = int3;
            PreferenceUtils.putIntPref(this.mContext, "pt_aggregation_size", this.mPTAggregationSize);
            final int ipConnectivityPolicy = deviceConfigData.getIpConnectivityPolicy();
            this.mIpConnectivityPolicy = IpConnectivityPolicy.from(ipConnectivityPolicy);
            PreferenceUtils.putIntPref(this.mContext, "ip_connectivity_policy_overide", ipConnectivityPolicy);
            this.mUserSessionDurationInSeconds = deviceConfigData.getUserSessionTimeoutDuration();
            PreferenceUtils.putIntPref(this.mContext, "apm_user_session_timeout_duration_override", this.mUserSessionDurationInSeconds);
            this.mBreadcrumbLoggingSpecification = BreadcrumbLoggingSpecification.saveToPreferences(this.mContext, deviceConfigData.getBreadcrumbLoggingSpecification());
            this.mErrorLoggingSpecification = ErrorLoggingSpecification.saveToPreferences(this.mContext, deviceConfigData.getErrorLoggingSpecification());
            this.mVideoResolutionOverride = deviceConfigData.getVideoResolutionOverride();
            PreferenceUtils.putIntPref(this.mContext, "video_resolution_override", deviceConfigData.getVideoResolutionOverride());
            this.mRateLimitForGcmBrowseEvents = deviceConfigData.getRateLimitForGcmBrowseEvents();
            PreferenceUtils.putIntPref(this.mContext, "gcm_browse_rate_limit", deviceConfigData.getRateLimitForGcmBrowseEvents());
            this.mRateLimitForNListChangeEvents = deviceConfigData.getRateLimitForGcmNListChangeEvents();
            PreferenceUtils.putIntPref(this.mContext, "gcm_tray_change_rate_limit", deviceConfigData.getRateLimitForGcmNListChangeEvents());
            PreferenceUtils.putIntPref(this.mContext, "jplayer_restart_count", deviceConfigData.getJPlayerStreamErrorRestartCount());
            this.updateLocalPlaybackStatus(deviceConfigData.getEnableLocalPlayback());
            this.updateMdxRemoteControlLockScreenStatus(deviceConfigData.getEnableMdxRemoteControlLockScreen());
            this.updateMdxRemoteControlNotificationStatus(deviceConfigData.getEnableMdxRemoteControlNotification());
            this.updateDeviceLocaleSupportAlert(deviceConfigData.shouldAlertForMissingLocale(), deviceConfigData.getAlertMsgForLocaleSupport());
            this.updateVoipSampleRate(deviceConfigData.getVoipSampleRateInHz());
            if (!this.isDeviceConfigInCache()) {
                this.updateDeviceConfigFlag(true);
            }
        }
    }
    
    public boolean shouldAlertForMissingLocale() {
        return this.mShouldAlertForMissingLocale;
    }
    
    public boolean shouldDisableVoip() {
        return !this.mIsVoipEnabledOnDevice;
    }
}
