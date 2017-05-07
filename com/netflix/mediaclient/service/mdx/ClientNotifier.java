// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.mediaclient.service.mdx;

import android.content.Intent;
import com.netflix.mediaclient.Log;
import android.content.Context;

public final class ClientNotifier implements NotifierInterface
{
    private static final String TAG = "nf_mdx";
    private Context mContext;
    
    ClientNotifier(final Context mContext) {
        this.mContext = mContext;
        Log.v("nf_mdx", "Creating client notifier");
    }
    
    @Override
    public void audiosub(final String s, final String s2) {
        this.mContext.sendBroadcast(new Intent("com.netflix.mediaclient.intent.action.MDXUPDATE_AUDIOSUB").addCategory("com.netflix.mediaclient.intent.category.MDX").putExtra("uuid", s).putExtra("stringBlob", s2));
        Log.v("nf_mdx", "Intent MDXUPDATE_AUDIOSUB sent");
    }
    
    @Override
    public void capability(final String s, final String s2) {
        this.mContext.sendBroadcast(new Intent("com.netflix.mediaclient.intent.action.MDXUPDATE_CAPABILITY").addCategory("com.netflix.mediaclient.intent.category.MDX").putExtra("uuid", s).putExtra("stringBlob", s2));
        Log.v("nf_mdx", "Intent MDXUPDATE_CAPABILITY sent");
    }
    
    @Override
    public void dialogCancel(final String s, final String s2) {
        this.mContext.sendBroadcast(new Intent("com.netflix.mediaclient.intent.action.MDXUPDATE_DIALOGCANCEL").addCategory("com.netflix.mediaclient.intent.category.MDX").putExtra("uuid", s).putExtra("stringBlob", s2));
        Log.v("nf_mdx", "Intent MDXUPDATE_DIALOGCANCEL sent");
    }
    
    @Override
    public void dialogShow(final String s, final String s2) {
        this.mContext.sendBroadcast(new Intent("com.netflix.mediaclient.intent.action.MDXUPDATE_DIALOGSHOW").addCategory("com.netflix.mediaclient.intent.category.MDX").putExtra("uuid", s).putExtra("stringBlob", s2));
        Log.v("nf_mdx", "Intent MDXUPDATE_DIALOGSHOW sent");
    }
    
    @Override
    public void error(final String s, final int n, final String s2) {
        this.mContext.sendBroadcast(new Intent("com.netflix.mediaclient.intent.action.MDXUPDATE_ERROR").addCategory("com.netflix.mediaclient.intent.category.MDX").putExtra("uuid", s).putExtra("errorCode", n).putExtra("errorDesc", s2));
        Log.v("nf_mdx", "Intent MDXUPDATE_ERROR sent");
    }
    
    @Override
    public void metaData(final String s, final String s2) {
        this.mContext.sendBroadcast(new Intent("com.netflix.mediaclient.intent.action.MDXUPDATE_METADATA").addCategory("com.netflix.mediaclient.intent.category.MDX").putExtra("uuid", s).putExtra("stringBlob", s2));
        Log.v("nf_mdx", "Intent MDXUPDATE_METADATA sent");
    }
    
    @Override
    public void movieMetaData(final String s, final String s2, final String s3, final int n) {
        this.mContext.sendBroadcast(new Intent("com.netflix.mediaclient.intent.action.MDXUPDATE_MOVIEMETADA").addCategory("com.netflix.mediaclient.intent.category.MDX").putExtra("uuid", s).putExtra("catalogId", s2).putExtra("episodeId", s3).putExtra("duration", n));
        Log.v("nf_mdx", "Intent MDXUPDATE_MOVIEDATA sent");
    }
    
    @Override
    public void movieMetaDataAvailable(final String s) {
        this.mContext.sendBroadcast(new Intent("com.netflix.mediaclient.intent.action.MDXUPDATE_MOVIEMETADATA_AVAILABLE").addCategory("com.netflix.mediaclient.intent.category.MDX").putExtra("uuid", s));
        Log.v("nf_mdx", "Intent MOVIEMETADATA_AVAILABLE sent");
    }
    
    @Override
    public void notready() {
        this.mContext.sendBroadcast(new Intent("com.netflix.mediaclient.intent.action.MDXUPDATE_NOTREADY").addCategory("com.netflix.mediaclient.intent.category.MDX"));
        Log.v("nf_mdx", "Intent NOTREADY sent");
    }
    
    @Override
    public void playbackEnd(final String s) {
        this.mContext.sendBroadcast(new Intent("com.netflix.mediaclient.intent.action.MDXUPDATE_PLAYBACKEND").addCategory("com.netflix.mediaclient.intent.category.MDX").putExtra("uuid", s));
        Log.v("nf_mdx", "Intent MDXUPDATE_PLAYBACKEND sent");
    }
    
    @Override
    public void playbackStart(final String s) {
        this.mContext.sendBroadcast(new Intent("com.netflix.mediaclient.intent.action.MDXUPDATE_PLAYBACKSTART").addCategory("com.netflix.mediaclient.intent.category.MDX").putExtra("uuid", s));
        Log.v("nf_mdx", "Intent MDXUPDATE_PLAYBACKSTART sent");
    }
    
    @Override
    public void ready() {
        this.mContext.sendBroadcast(new Intent("com.netflix.mediaclient.intent.action.MDXUPDATE_READY").addCategory("com.netflix.mediaclient.intent.category.MDX"));
        Log.v("nf_mdx", "Intent READY sent");
    }
    
    @Override
    public void simplePlaybackState(final String s, final boolean b, final boolean b2) {
        this.mContext.sendBroadcast(new Intent("com.netflix.mediaclient.intent.action.MDXUPDATE_SIMPLE_PLAYBACKSTATE").addCategory("com.netflix.mediaclient.intent.category.MDX").putExtra("uuid", s).putExtra("paused", b).putExtra("transitioning", b2));
    }
    
    @Override
    public void state(final String s, final String s2, final int n, final int n2) {
        this.mContext.sendBroadcast(new Intent("com.netflix.mediaclient.intent.action.MDXUPDATE_STATE").addCategory("com.netflix.mediaclient.intent.category.MDX").putExtra("uuid", s).putExtra("currentState", s2).putExtra("time", n).putExtra("volume", n2));
        Log.v("nf_mdx", "Intent MDXUPDATE_STATE sent");
    }
    
    @Override
    public void targetList() {
        this.mContext.sendBroadcast(new Intent("com.netflix.mediaclient.intent.action.MDXUPDATE_TARGETLIST").addCategory("com.netflix.mediaclient.intent.category.MDX"));
        Log.v("nf_mdx", "Intent MDXUPDATE_TARGETLISTY sent");
    }
}