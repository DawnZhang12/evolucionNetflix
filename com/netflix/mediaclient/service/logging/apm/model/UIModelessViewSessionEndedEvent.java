// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.mediaclient.service.logging.apm.model;

import com.netflix.mediaclient.util.JsonUtils;
import org.json.JSONObject;
import com.netflix.mediaclient.service.logging.client.model.SessionStartedEvent;
import com.netflix.mediaclient.service.logging.client.model.DeviceUniqueId;
import com.netflix.mediaclient.servicemgr.IClientLogging$ModalView;
import com.netflix.mediaclient.service.logging.client.model.SessionEndedEvent;

public final class UIModelessViewSessionEndedEvent extends SessionEndedEvent
{
    public static final String MODELESS_VIEW = "modelessView";
    private static final String UI_SESSION_NAME = "uiModelessView";
    private IClientLogging$ModalView modelessView;
    
    public UIModelessViewSessionEndedEvent(final DeviceUniqueId deviceUniqueId, final long n, final IClientLogging$ModalView clientLogging$ModalView) {
        super("uiModelessView", deviceUniqueId, n);
        this.modalView = clientLogging$ModalView;
        this.modelessView = clientLogging$ModalView;
    }
    
    public UIModelessViewSessionEndedEvent(final SessionStartedEvent sessionStartedEvent, final long n, final IClientLogging$ModalView clientLogging$ModalView) {
        super(sessionStartedEvent, n);
        this.modalView = clientLogging$ModalView;
        this.modelessView = clientLogging$ModalView;
    }
    
    public UIModelessViewSessionEndedEvent(JSONObject jsonObject) {
        super(jsonObject);
        jsonObject = JsonUtils.getJSONObject(jsonObject, "data", (JSONObject)null);
        if (jsonObject != null) {
            final String string = JsonUtils.getString(jsonObject, "modelessView", (String)null);
            if (string != null) {
                this.modelessView = IClientLogging$ModalView.valueOf(string);
            }
        }
    }
    
    @Override
    protected JSONObject getData() {
        JSONObject data;
        if ((data = super.getData()) == null) {
            data = new JSONObject();
        }
        if (this.modelessView != null) {
            data.put("modelessView", (Object)this.modelessView.name());
        }
        return data;
    }
    
    public IClientLogging$ModalView getModelessView() {
        return this.modelessView;
    }
}
