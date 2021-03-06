// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.mediaclient.ui.barker_kids.details;

import com.netflix.mediaclient.servicemgr.interface_.details.SeasonDetails;
import java.util.List;
import com.netflix.mediaclient.util.gfx.AnimationUtils;
import android.support.v7.widget.RecyclerView$Adapter;
import com.netflix.mediaclient.ui.details.VideoDetailsViewGroup$DetailsStringProvider;
import com.netflix.mediaclient.servicemgr.interface_.Video;
import java.util.Collection;
import com.netflix.mediaclient.servicemgr.ServiceManager;
import android.view.View;
import com.netflix.mediaclient.android.widget.RecyclerViewHeaderAdapter;
import com.netflix.mediaclient.ui.details.SeasonsSpinnerAdapter;
import com.netflix.mediaclient.android.widget.NetflixActionBar;
import com.netflix.mediaclient.ui.details.DetailsPageParallaxScrollListener$IScrollStateChanged;
import android.support.v7.widget.RecyclerView$OnScrollListener;
import com.netflix.mediaclient.ui.kids.KidsUtils;
import android.content.Context;
import com.netflix.mediaclient.util.DeviceUtils;
import com.netflix.mediaclient.servicemgr.interface_.details.ShowDetails;
import com.netflix.mediaclient.servicemgr.ManagerCallback;
import android.os.Bundle;
import android.app.Fragment;
import com.netflix.mediaclient.ui.details.VideoDetailsViewGroup;
import android.view.ViewGroup;
import android.support.v7.widget.RecyclerView;
import com.netflix.mediaclient.ui.details.DetailsPageParallaxScrollListener;
import com.netflix.mediaclient.android.activity.NetflixActivity;
import com.netflix.mediaclient.ui.details.SeasonsSpinner;
import com.netflix.mediaclient.ui.barker.details.BarkerShowDetailsFrag$HeroSlideshow;
import com.netflix.mediaclient.servicemgr.interface_.details.KidsCharacterDetails;
import android.annotation.SuppressLint;
import com.netflix.mediaclient.android.app.CommonStatus;
import com.netflix.mediaclient.servicemgr.interface_.details.VideoDetails;
import com.netflix.mediaclient.Log;
import com.netflix.mediaclient.android.app.Status;
import com.netflix.mediaclient.servicemgr.interface_.details.MovieDetails;
import com.netflix.mediaclient.servicemgr.LoggingManagerCallback;

class BarkerKidsCharacterDetailsFrag$FetchCharacterDetailsCallback$FetchMovieDataCallback extends LoggingManagerCallback
{
    private final long requestId;
    final /* synthetic */ BarkerKidsCharacterDetailsFrag$FetchCharacterDetailsCallback this$1;
    
    public BarkerKidsCharacterDetailsFrag$FetchCharacterDetailsCallback$FetchMovieDataCallback(final BarkerKidsCharacterDetailsFrag$FetchCharacterDetailsCallback this$1, final long requestId) {
        this.this$1 = this$1;
        super("KidsCharacterDetailsFrag");
        this.requestId = requestId;
    }
    
    @Override
    public void onMovieDetailsFetched(final MovieDetails movieDetails, final Status status) {
        super.onMovieDetailsFetched(movieDetails, status);
        if (this.requestId != this.requestId) {
            Log.v("KidsCharacterDetailsFrag", "Stale response - ignoring");
            return;
        }
        this.this$1.this$0.isLoading = false;
        if (status.isError()) {
            Log.w("KidsCharacterDetailsFrag", "Error status code fetching data - showing errors view");
            this.this$1.this$0.showErrorView();
            return;
        }
        if (movieDetails == null) {
            Log.w("KidsCharacterDetailsFrag", "No details in response!");
            this.this$1.this$0.showErrorView();
            return;
        }
        this.this$1.this$0.renderAsMDP(movieDetails);
        this.this$1.this$0.onLoaded(CommonStatus.OK);
    }
}
