// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.mediaclient.service.configuration;

import com.netflix.mediaclient.service.webclient.model.leafs.SignInData;
import com.netflix.mediaclient.service.webclient.model.leafs.ConfigData;
import com.netflix.mediaclient.android.app.Status;

public class SimpleConfigurationAgentWebCallback implements ConfigurationAgentWebCallback
{
    @Override
    public void onAllocateABTestCompleted(final Status status) {
    }
    
    @Override
    public void onConfigDataFetched(final ConfigData configData, final Status status) {
    }
    
    @Override
    public void onLoginVerified(final SignInData signInData, final Status status) {
    }
}
