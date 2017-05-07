// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.mediaclient.ui.kids.details;

import com.netflix.mediaclient.android.widget.ErrorWrapper$Callback;
import android.content.Context;
import com.netflix.mediaclient.ui.kids.KidsUtils;
import android.view.LayoutInflater;
import android.widget.ListAdapter;
import com.netflix.mediaclient.util.gfx.AnimationUtils;
import com.netflix.mediaclient.servicemgr.ManagerCallback;
import android.widget.TextView;
import android.view.ViewGroup;
import android.os.Bundle;
import android.app.Fragment;
import com.netflix.mediaclient.servicemgr.ServiceManager;
import android.widget.ListView;
import com.netflix.mediaclient.android.widget.LoadingAndErrorWrapper;
import android.view.View;
import com.netflix.mediaclient.android.fragment.NetflixFrag;
import com.netflix.mediaclient.Log;
import com.netflix.mediaclient.android.app.Status;
import com.netflix.mediaclient.servicemgr.model.details.MovieDetails;
import com.netflix.mediaclient.servicemgr.LoggingManagerCallback;

class KidsMovieDetailsFrag$FetchMovieDetailsCallback extends LoggingManagerCallback
{
    private final long requestId;
    final /* synthetic */ KidsMovieDetailsFrag this$0;
    
    public KidsMovieDetailsFrag$FetchMovieDetailsCallback(final KidsMovieDetailsFrag this$0, final long requestId) {
        this.this$0 = this$0;
        super("KidsMovieDetailsFrag");
        this.requestId = requestId;
    }
    
    @Override
    public void onMovieDetailsFetched(final MovieDetails movieDetails, final Status status) {
        super.onMovieDetailsFetched(movieDetails, status);
        if (this.requestId != this.this$0.requestId) {
            Log.v("KidsMovieDetailsFrag", "Ignoring stale callback");
            return;
        }
        if (status.isError()) {
            Log.w("KidsMovieDetailsFrag", "Invalid status code");
            this.this$0.showErrorView();
            return;
        }
        if (movieDetails == null) {
            Log.v("KidsMovieDetailsFrag", "No details in response");
            this.this$0.showErrorView();
            return;
        }
        this.this$0.updateMovieDetails(movieDetails);
    }
}