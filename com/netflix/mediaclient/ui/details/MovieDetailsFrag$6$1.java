// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.mediaclient.ui.details;

import com.netflix.mediaclient.Log;

class MovieDetailsFrag$6$1 implements Runnable
{
    final /* synthetic */ MovieDetailsFrag$6 this$1;
    
    MovieDetailsFrag$6$1(final MovieDetailsFrag$6 this$1) {
        this.this$1 = this$1;
    }
    
    @Override
    public void run() {
        Log.v("MovieDetailsFrag", "Resetting parallax views");
        this.this$1.this$0.parallaxScroller.setToolbarColor();
    }
}
