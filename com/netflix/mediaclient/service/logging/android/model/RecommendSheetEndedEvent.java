// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.mediaclient.service.logging.android.model;

import org.json.JSONObject;
import com.netflix.mediaclient.service.logging.client.model.UIError;
import com.netflix.mediaclient.servicemgr.IClientLogging$CompletionReason;
import com.netflix.mediaclient.servicemgr.UserActionLogging$CommandName;
import com.netflix.mediaclient.servicemgr.IClientLogging$ModalView;
import com.netflix.mediaclient.service.logging.client.model.DeviceUniqueId;
import com.netflix.mediaclient.service.logging.uiaction.model.BaseUIActionSessionEndedEvent;

public class RecommendSheetEndedEvent extends BaseUIActionSessionEndedEvent
{
    private static final String UIA_NAME = "recommendSheet";
    
    public RecommendSheetEndedEvent(final DeviceUniqueId deviceUniqueId, final long n, final IClientLogging$ModalView clientLogging$ModalView, final UserActionLogging$CommandName userActionLogging$CommandName, final IClientLogging$CompletionReason clientLogging$CompletionReason, final UIError uiError) {
        super("recommendSheet", deviceUniqueId, n, clientLogging$ModalView, userActionLogging$CommandName, clientLogging$CompletionReason, uiError);
    }
    
    public RecommendSheetEndedEvent(final JSONObject jsonObject) {
        super(jsonObject);
    }
    
    @Override
    public boolean isMemberEvent() {
        return true;
    }
}
