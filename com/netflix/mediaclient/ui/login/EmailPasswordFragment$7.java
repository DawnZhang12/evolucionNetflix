// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.mediaclient.ui.login;

import com.netflix.mediaclient.android.app.Status;
import com.netflix.mediaclient.servicemgr.SimpleManagerCallback;

class EmailPasswordFragment$7 extends SimpleManagerCallback
{
    final /* synthetic */ EmailPasswordFragment this$0;
    
    EmailPasswordFragment$7(final EmailPasswordFragment this$0) {
        this.this$0 = this$0;
    }
    
    @Override
    public void onLoginComplete(final Status status) {
        this.this$0.getActivity().runOnUiThread((Runnable)new EmailPasswordFragment$7$1(this, status));
    }
}