// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.mediaclient.service.mdx;

import com.netflix.mediaclient.service.mdx.message.controller.PlayerGetCapabilities;
import com.netflix.mediaclient.ui.mdx.MdxTargetCapabilities;
import com.netflix.mediaclient.service.mdx.message.target.PlayerState;
import com.netflix.mediaclient.service.mdx.message.target.PinNotRequired;
import com.netflix.mediaclient.service.mdx.message.target.PinRequired;
import com.netflix.mediaclient.service.mdx.message.target.PlayerStateChanged;
import com.netflix.mediaclient.service.mdx.message.target.PlayerCurrentState;
import org.json.JSONException;
import com.netflix.mediaclient.service.mdx.message.target.HandshakeAccepted;
import com.netflix.mediaclient.ui.experience.BrowseExperience;
import com.netflix.mediaclient.NetflixApplication;
import org.json.JSONObject;
import java.security.InvalidParameterException;
import com.netflix.mediaclient.util.WebApiUtils;
import com.netflix.mediaclient.util.WebApiUtils$VideoIds;
import android.os.Message;
import com.netflix.mediaclient.service.mdx.message.MdxMessage;
import com.netflix.mediaclient.util.StringUtils;
import com.netflix.mediaclient.Log;
import com.netflix.mediaclient.javabridge.ui.mdxcontroller.RemoteDevice;
import android.os.HandlerThread;
import android.os.Handler;
import com.netflix.mediaclient.javabridge.ui.mdxcontroller.MdxController;
import com.netflix.mediaclient.service.mdx.message.controller.Handshake;

class TargetContext$DoHandShake implements Runnable
{
    final /* synthetic */ TargetContext this$0;
    
    TargetContext$DoHandShake(final TargetContext this$0) {
        this.this$0 = this$0;
    }
    
    @Override
    public void run() {
        final Handshake handshake = new Handshake();
        this.this$0.mController.getSession().sendMessage(this.this$0.mSessionId, handshake.messageName(), handshake.messageObject());
    }
}
