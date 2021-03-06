// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.mediaclient.ui.details;

import java.util.Collection;
import com.netflix.mediaclient.android.app.CommonStatus;
import com.netflix.mediaclient.ui.offline.DownloadButton;
import com.netflix.mediaclient.ui.offline.TutorialHelper;
import com.netflix.android.tooltips.Tooltip;
import com.netflix.mediaclient.servicemgr.interface_.user.UserProfile;
import android.support.v7.widget.GridLayoutManager$SpanSizeLookup;
import android.support.v7.widget.RecyclerView$ItemDecoration;
import com.netflix.mediaclient.util.ItemDecorationUniformPadding;
import android.support.v7.widget.RecyclerView$Adapter;
import com.netflix.mediaclient.android.widget.RecyclerViewHeaderAdapter$IViewCreator;
import com.netflix.mediaclient.android.widget.NetflixActionBar;
import android.support.v7.widget.RecyclerView$OnScrollListener;
import com.netflix.mediaclient.servicemgr.interface_.trackable.Trackable;
import com.netflix.mediaclient.service.webclient.model.leafs.TrackableObject;
import android.transition.Transition$TransitionListener;
import android.transition.Transition;
import com.netflix.mediaclient.util.DataUtil;
import com.netflix.mediaclient.ui.common.SimilarItemsGridViewAdapter;
import com.netflix.mediaclient.android.app.Status;
import android.view.ViewTreeObserver$OnGlobalLayoutListener;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import com.netflix.mediaclient.util.DeviceUtils;
import com.netflix.mediaclient.service.configuration.persistent.Config_Ab7994;
import com.netflix.mediaclient.servicemgr.interface_.Video;
import java.util.List;
import com.netflix.mediaclient.ui.lomo.LomoConfig;
import com.netflix.mediaclient.ui.mdx.MementoMovieDetailsActivity;
import android.support.v7.widget.RecyclerView$LayoutManager;
import com.netflix.mediaclient.servicemgr.ServiceManager;
import com.netflix.mediaclient.servicemgr.ManagerCallback;
import com.netflix.mediaclient.Log;
import android.os.Bundle;
import com.netflix.mediaclient.util.ViewUtils;
import com.netflix.mediaclient.util.StringUtils;
import android.content.Context;
import com.netflix.mediaclient.servicemgr.interface_.details.VideoDetails;
import android.view.View;
import android.support.v7.widget.RecyclerView;
import android.os.Parcelable;
import android.support.v7.widget.GridLayoutManager;
import com.netflix.mediaclient.android.widget.RecyclerViewHeaderAdapter;
import com.netflix.mediaclient.ui.offline.TutorialHelper$Tutorialable;
import com.netflix.mediaclient.servicemgr.interface_.details.MovieDetails;

public class MovieDetailsFrag extends DetailsFrag<MovieDetails> implements ILayoutManager, TutorialHelper$Tutorialable
{
    private static final String EXTRA_LAYOUT_MANAGER_STATE = "layout_manager_state";
    protected static final String EXTRA_VIDEO_ID = "video_id";
    private static final String TAG = "MovieDetailsFrag";
    protected RecyclerViewHeaderAdapter adapter;
    private GridLayoutManager gridLayoutManager;
    protected boolean isLoading;
    private Parcelable layoutManagerSavedState;
    protected int numColumns;
    protected DetailsPageParallaxScrollListener parallaxScroller;
    protected RecyclerView recyclerView;
    protected long requestId;
    private View rootContainer;
    protected String videoId;
    
    public MovieDetailsFrag() {
        this.isLoading = true;
    }
    
    private void addFooter(final VideoDetails videoDetails) {
        if (this.adapter == null) {
            return;
        }
        final CopyrightView create = CopyrightView.create(videoDetails, (Context)this.getActivity());
        if (create != null && StringUtils.isNotEmpty(videoDetails.getCopyright())) {
            create.setGravityAsCenter();
            this.adapter.addFooterView(create.getView());
            create.getView().setVisibility(0);
            return;
        }
        this.adapter.addFooterView(ViewUtils.createActionBarDummyView(this.getNetflixActivity(), this.getResources().getDimensionPixelOffset(2131427907)));
    }
    
    public static MovieDetailsFrag create(final String s) {
        final MovieDetailsFrag movieDetailsFrag = new MovieDetailsFrag();
        final Bundle arguments = new Bundle();
        arguments.putString("video_id", s);
        movieDetailsFrag.setArguments(arguments);
        return movieDetailsFrag;
    }
    
    private void setBackgroundResource(final int backgroundResource) {
        if (this.rootContainer != null) {
            this.rootContainer.setBackgroundResource(backgroundResource);
        }
    }
    
    protected void fetchMovieData() {
        final ServiceManager serviceManager = this.getServiceManager();
        if (serviceManager == null || !serviceManager.isReady()) {
            Log.w("MovieDetailsFrag", "Manager is null/notReady - can't reload data");
            return;
        }
        this.isLoading = true;
        this.requestId = System.nanoTime();
        Log.v("MovieDetailsFrag", "Fetching data for movie ID: " + this.videoId);
        serviceManager.getBrowse().fetchMovieDetails(this.videoId, null, new MovieDetailsFrag$FetchMovieDetailsCallback(this, this.requestId));
    }
    
    protected void findViews(final View view) {
        this.recyclerView = (RecyclerView)this.primaryView;
        this.rootContainer = view.findViewById(2131821073);
    }
    
    @Override
    protected VideoDetailsViewGroup$DetailsStringProvider getDetailsStringProvider(final MovieDetails movieDetails) {
        return new MovieDetailsFrag$6(this, movieDetails);
    }
    
    @Override
    protected int getLayoutId() {
        return 2130903285;
    }
    
    @Override
    public RecyclerView$LayoutManager getLayoutManager() {
        if (this.recyclerView == null) {
            return null;
        }
        return this.recyclerView.getLayoutManager();
    }
    
    protected int getNumColumns() {
        if (this.getActivity() instanceof MementoMovieDetailsActivity) {
            return 4;
        }
        return LomoConfig.computeStandardNumVideosPerPage(this.getNetflixActivity(), false);
    }
    
    @Override
    protected int getPrimaryViewId() {
        return 16908298;
    }
    
    @Override
    protected String getVideoId() {
        return this.videoId;
    }
    
    protected void handlePrefetchDPData(final List<Video> list) {
        DPPrefetchABTestUtils.prefetchDPForSimilars(this.getServiceManager(), list);
    }
    
    @Override
    protected void initDetailsViewGroup(final View view) {
        if (Config_Ab7994.shouldRenderTabs((Context)this.getActivity())) {
            this.detailsViewGroup = new VideoDetailsViewGroup_Ab7994((Context)this.getActivity());
        }
        else {
            this.detailsViewGroup = new VideoDetailsViewGroup((Context)this.getActivity());
        }
        this.detailsViewGroup.removeActionBarDummyView();
        this.detailsViewGroup.showRelatedTitle();
        if (DeviceUtils.isLandscape((Context)this.getActivity()) && DeviceUtils.isTabletByContext((Context)this.getActivity())) {
            this.detailsViewGroup.setPadding(0, this.getNetflixActivity().getActionBarHeight(), 0, 0);
        }
    }
    
    public boolean isLoadingData() {
        return this.isLoading;
    }
    
    public void onCreate(final Bundle bundle) {
        this.videoId = this.getArguments().getString("video_id");
        super.onCreate(bundle);
    }
    
    @Override
    public View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
        final View onCreateView = super.onCreateView(layoutInflater, viewGroup, bundle);
        this.findViews(onCreateView);
        this.numColumns = this.getNumColumns();
        this.initDetailsViewGroup(onCreateView);
        if (bundle != null) {
            final Parcelable parcelable = bundle.getParcelable("layout_manager_state");
            if (parcelable != null) {
                this.setLayoutManagerSavedState(parcelable);
            }
        }
        this.setupRecyclerView();
        this.detailsViewGroup.getViewTreeObserver().addOnGlobalLayoutListener((ViewTreeObserver$OnGlobalLayoutListener)new MovieDetailsFrag$2(this));
        return onCreateView;
    }
    
    public void onManagerReady(final ServiceManager serviceManager, final Status status) {
        super.onManagerReady(serviceManager, status);
        this.fetchMovieData();
    }
    
    public void onManagerUnavailable(final ServiceManager serviceManager, final Status status) {
    }
    
    @Override
    public void onResume() {
        super.onResume();
        SimilarItemsGridViewAdapter.refreshImagesIfNecessary(this.recyclerView);
        if (this.adapter != null) {
            this.adapter.notifyDataSetChanged();
        }
        this.detailsViewGroup.updateDownloadButtonIfExists(this.getVideoDetails());
    }
    
    public void onSaveInstanceState(final Bundle bundle) {
        super.onSaveInstanceState(bundle);
        if (this.gridLayoutManager != null) {
            bundle.putParcelable("layout_manager_state", this.gridLayoutManager.onSaveInstanceState());
        }
    }
    
    @Override
    protected void reloadData() {
        this.fetchMovieData();
    }
    
    protected void restoreLayoutManagerState() {
        if (DataUtil.areAnyNull("MovieDetailsFrag", "can't restore layout manager", new Object[] { this.layoutManagerSavedState, this.recyclerView, this.parallaxScroller })) {
            return;
        }
        Log.v("MovieDetailsFrag", "Restoring layout manager state: %s", this.layoutManagerSavedState);
        this.recyclerView.getLayoutManager().onRestoreInstanceState(this.layoutManagerSavedState);
        this.layoutManagerSavedState = null;
        Log.v("MovieDetailsFrag", "Posting message to reset parallax views");
        this.recyclerView.post((Runnable)new MovieDetailsFrag$7(this));
    }
    
    public void scrollTop() {
        this.recyclerView.post((Runnable)new MovieDetailsFrag$1(this));
    }
    
    public void setExitTransition(final Transition exitTransition) {
        super.setExitTransition(exitTransition);
        this.setBackgroundResource(2131755298);
        if (exitTransition != null) {
            exitTransition.addListener((Transition$TransitionListener)new MovieDetailsFrag$3(this));
        }
    }
    
    @Override
    public void setLayoutManagerSavedState(final Parcelable layoutManagerSavedState) {
        this.layoutManagerSavedState = layoutManagerSavedState;
    }
    
    protected void setTrackId(final MovieDetails movieDetails) {
        this.adapter.setTrackable(new TrackableObject(movieDetails.getSimilarsRequestId(), movieDetails.getSimilarsTrackId(), movieDetails.getSimilarsListPos()));
    }
    
    public void setVideoId(final String videoId) {
        this.videoId = videoId;
    }
    
    protected void setupDetailsPageParallaxScrollListener() {
        if (this.getActivity() != null && this.recyclerView != null) {
            final NetflixActionBar netflixActionBar = this.getNetflixActivity().getNetflixActionBar();
            if (netflixActionBar != null) {
                netflixActionBar.hidelogo();
                if (DeviceUtils.isNotTabletByContext((Context)this.getActivity()) || DeviceUtils.isPortrait((Context)this.getActivity())) {
                    Log.v("MovieDetailsFrag", "Attaching parallax scroll listener to recyclerView");
                    this.parallaxScroller = DetailsPageParallaxScrollListener.createDefault(null, this.recyclerView, new View[] { this.detailsViewGroup.getHeroImage() }, null, null);
                    this.recyclerView.setOnScrollListener(this.parallaxScroller);
                }
            }
        }
    }
    
    protected void setupRecyclerView() {
        if (this.recyclerView == null) {
            return;
        }
        this.recyclerView.setFocusable(false);
        this.recyclerView.setVisibility(4);
        this.setupRecyclerViewItemDecoration();
        this.setupRecyclerViewLayoutManager();
        this.setupRecyclerViewAdapter();
    }
    
    protected void setupRecyclerViewAdapter() {
        (this.adapter = new SimilarItemsGridViewAdapter(true, this.numColumns, new MovieDetailsFrag$4(this))).addHeaderView((View)this.detailsViewGroup);
        this.recyclerView.setAdapter(this.adapter);
    }
    
    protected void setupRecyclerViewItemDecoration() {
        this.recyclerView.addItemDecoration((RecyclerView$ItemDecoration)new ItemDecorationUniformPadding(this.getActivity().getResources().getDimensionPixelOffset(2131427641), this.numColumns));
    }
    
    protected void setupRecyclerViewLayoutManager() {
        (this.gridLayoutManager = new GridLayoutManager((Context)this.getActivity(), this.numColumns)).setSpanSizeLookup(new MovieDetailsFrag$5(this));
        this.recyclerView.setLayoutManager(this.gridLayoutManager);
    }
    
    public Tooltip setupTutorial(final UserProfile userProfile) {
        final DownloadButton downloadButton = this.getVideoDetailsViewGroup().getDownloadButton();
        if (downloadButton == null) {
            return null;
        }
        return TutorialHelper.buildDownloadButtonTutorial(downloadButton.findViewById(2131820966), this.getActivity(), userProfile);
    }
    
    @Override
    protected void showDetailsView(final MovieDetails trackId) {
        super.showDetailsView(trackId);
        this.setTrackId(trackId);
        this.showSimsItems(trackId);
        if (this.recyclerView != null) {
            this.recyclerView.setVisibility(0);
            this.getNetflixActivity().getTutorialHelper().showTutorialForVideoWithScroll((TutorialHelper$Tutorialable)this, (VideoDetails)trackId, this.recyclerView, this.getServiceManager());
        }
        this.addFooter(trackId);
        this.restoreLayoutManagerState();
        this.onLoaded(CommonStatus.OK);
    }
    
    protected void showSimsItems(final MovieDetails movieDetails) {
        if (movieDetails.getSimilars().size() != 0) {
            this.adapter.setItems(movieDetails.getSimilars());
            this.handlePrefetchDPData(movieDetails.getSimilars());
        }
        else if (this.getVideoDetailsViewGroup() != null) {
            this.getVideoDetailsViewGroup().hideRelatedTitle();
        }
    }
    
    protected void updateVolatileDataInView(final MovieDetails movieDetails) {
        Log.v("MovieDetailsFrag", "Volatile data update rating: %d, inQ: %b", movieDetails.getUserThumbRating(), movieDetails.isInQueue());
        this.detailsViewGroup.updateRating(movieDetails);
        this.updateMyListState();
    }
}
