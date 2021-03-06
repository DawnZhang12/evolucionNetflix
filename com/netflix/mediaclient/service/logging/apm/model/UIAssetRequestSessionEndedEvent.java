// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.mediaclient.service.logging.apm.model;

import com.netflix.mediaclient.service.logging.client.model.UIError;
import com.netflix.mediaclient.util.JsonUtils;
import org.json.JSONObject;
import com.netflix.mediaclient.service.logging.client.model.DeviceUniqueId;
import com.netflix.mediaclient.service.logging.client.model.HttpResponse;
import com.netflix.mediaclient.servicemgr.IClientLogging$CompletionReason;
import com.netflix.mediaclient.service.logging.client.model.Error;
import com.netflix.mediaclient.servicemgr.IClientLogging$AssetType;
import com.netflix.mediaclient.service.logging.client.model.SessionEndedEvent;

public final class UIAssetRequestSessionEndedEvent extends SessionEndedEvent
{
    public static final String ASSET_TYPE = "assetType";
    public static final String ERROR = "error";
    public static final String REASON = "reason";
    public static final String RESPONSE = "response";
    private static final String UI_ASSET_NAME = "uiAssetRequest";
    public static final String URL = "url";
    private IClientLogging$AssetType assetType;
    private ConnectionClassCustomData connectionClassCustomData;
    private Error error;
    private IClientLogging$CompletionReason reason;
    private HttpResponse response;
    private String url;
    
    public UIAssetRequestSessionEndedEvent(final long n) {
        super("uiAssetRequest", new DeviceUniqueId(), n);
    }
    
    public UIAssetRequestSessionEndedEvent(JSONObject jsonObject) {
        super(jsonObject);
        jsonObject = JsonUtils.getJSONObject(jsonObject, "data", (JSONObject)null);
        if (jsonObject != null) {
            final String string = JsonUtils.getString(jsonObject, "assetType", (String)null);
            if (string != null) {
                this.assetType = IClientLogging$AssetType.valueOf(string);
            }
            final String string2 = JsonUtils.getString(jsonObject, "reason", (String)null);
            if (string2 != null) {
                this.reason = Enum.valueOf(IClientLogging$CompletionReason.class, string2);
            }
            this.url = JsonUtils.getString(jsonObject, "url", (String)null);
            this.error = UIError.createInstance(JsonUtils.getJSONObject(jsonObject, "error", (JSONObject)null));
            this.response = HttpResponse.createInstance(JsonUtils.getJSONObject(jsonObject, "response", (JSONObject)null));
        }
    }
    
    public IClientLogging$AssetType getAssetType() {
        return this.assetType;
    }
    
    @Override
    protected JSONObject getCustomData() {
        if (this.connectionClassCustomData == null) {
            return null;
        }
        return this.connectionClassCustomData.toJson();
    }
    
    @Override
    protected JSONObject getData() {
        JSONObject data;
        if ((data = super.getData()) == null) {
            data = new JSONObject();
        }
        if (this.url != null) {
            data.put("url", (Object)this.url);
        }
        if (this.error != null) {
            data.put("error", (Object)this.error.toJSONObject());
        }
        if (this.response != null) {
            data.put("response", (Object)this.response.toJSONObject());
        }
        if (this.reason != null) {
            data.put("reason", (Object)this.reason.name());
        }
        if (this.assetType != null) {
            data.put("assetType", (Object)this.assetType.name());
        }
        return data;
    }
    
    public Error getError() {
        return this.error;
    }
    
    public IClientLogging$CompletionReason getReason() {
        return this.reason;
    }
    
    public HttpResponse getResponse() {
        return this.response;
    }
    
    public String getUrl() {
        return this.url;
    }
    
    public void setAssetType(final IClientLogging$AssetType assetType) {
        this.assetType = assetType;
    }
    
    public void setConnectionClassCustomData(final ConnectionClassCustomData connectionClassCustomData) {
        this.connectionClassCustomData = connectionClassCustomData;
    }
    
    public void setError(final Error error) {
        this.error = error;
    }
    
    public void setReason(final IClientLogging$CompletionReason reason) {
        this.reason = reason;
    }
    
    public void setResponse(final HttpResponse response) {
        this.response = response;
    }
    
    public void setUrl(final String url) {
        this.url = url;
    }
}
