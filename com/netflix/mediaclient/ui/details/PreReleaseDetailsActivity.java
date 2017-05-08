// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.mediaclient.ui.details;

import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.app.Fragment;
import android.graphics.Typeface;
import android.support.design.widget.Snackbar$SnackbarLayout;
import android.widget.TextView;
import android.support.design.widget.Snackbar;

public class PreReleaseDetailsActivity extends MovieDetailsActivity
{
    PreReleaseDetailsActivity$MyListReceiver mylistReceiver;
    
    public PreReleaseDetailsActivity() {
        this.mylistReceiver = new PreReleaseDetailsActivity$MyListReceiver(this);
    }
    
    private void showSnackBar() {
        final Snackbar make = Snackbar.make(this.findViewById(16908290), 2131231284, 0);
        make.getView().getLayoutParams().width = -1;
        final TextView textView = (TextView)((Snackbar$SnackbarLayout)make.getView()).findViewById(2131689755);
        textView.setTypeface(Typeface.DEFAULT_BOLD);
        textView.setGravity(1);
        make.show();
    }
    
    @Override
    protected Fragment createPrimaryFrag() {
        return PreReleaseDetailsFrag.create(this.getVideoId(), this.getIntent().getExtras().getBoolean("extra_is_movie", false));
    }
    
    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.registerReceiverWithAutoUnregister(this.mylistReceiver, "com.netflix.mediaclient.mylist.intent.action.ADD");
    }
    
    @Override
    protected void onPostCreate(final Bundle bundle) {
        super.onPostCreate(bundle);
    }
}
