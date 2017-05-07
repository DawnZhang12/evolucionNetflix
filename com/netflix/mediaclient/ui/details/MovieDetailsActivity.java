// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.mediaclient.ui.details;

import android.os.Parcel;
import android.os.Parcelable$Creator;
import android.content.Intent;
import java.util.Iterator;
import android.os.Parcelable;
import android.os.Bundle;
import android.app.Fragment;
import com.netflix.mediaclient.android.fragment.NetflixFrag;
import com.netflix.mediaclient.ui.common.PlayContext;
import com.netflix.mediaclient.ui.common.PlayContextImp;
import com.netflix.mediaclient.Log;
import com.netflix.mediaclient.util.StringUtils;
import com.netflix.mediaclient.servicemgr.ServiceManager;
import java.util.ArrayList;
import com.netflix.mediaclient.servicemgr.ManagerStatusListener;

public class MovieDetailsActivity extends DetailsActivity implements ManagerStatusListener
{
    private static final String EXTRA_BACK_STACK = "extra_back_stack";
    private static final String TAG = "MovieDetailsActivity";
    private final ArrayList<BackStackData> backStack;
    private ServiceManager manager;
    
    public MovieDetailsActivity() {
        this.backStack = new ArrayList<BackStackData>();
    }
    
    private void handleNewVideoId() {
        if (StringUtils.isNotEmpty(this.getVideoId())) {
            if (Log.isLoggable("MovieDetailsActivity", 2)) {
                Log.v("MovieDetailsActivity", "Adding curr video to back stack: " + this.getVideoId() + ", " + "TRACK_ID: " + this.getPlayContext().getTrackId());
            }
            this.backStack.add(new BackStackData(this.getVideoId(), this.getPlayContext()));
        }
        this.setVideoId(this.getIntent().getStringExtra("extra_video_id"));
        this.setPlayContext((PlayContext)this.getIntent().getParcelableExtra("extra_playcontext"));
    }
    
    private void showNewDetailsFrag() {
        this.setPrimaryFrag(this.createPrimaryFrag());
        this.getFragmentManager().beginTransaction().replace(2131099750, this.getPrimaryFrag(), "primary").setTransition(4099).commit();
        this.getFragmentManager().executePendingTransactions();
        ((ManagerStatusListener)this.getPrimaryFrag()).onManagerReady(this.manager, 0);
    }
    
    @Override
    protected MovieDetailsFrag createPrimaryFrag() {
        return MovieDetailsFrag.create(this.getVideoId());
    }
    
    @Override
    protected boolean handleBackPressed() {
        Log.v("MovieDetailsActivity", "Back pressed, backStack size: " + this.backStack.size());
        if (this.backStack.size() > 0) {
            final BackStackData backStackData = this.backStack.remove(this.backStack.size() - 1);
            this.setVideoId(backStackData.videoId);
            this.setPlayContext(backStackData.playContext);
            if (Log.isLoggable("MovieDetailsActivity", 2)) {
                Log.v("MovieDetailsActivity", "Video id from back stack: " + this.getVideoId() + ", " + "TRACK_ID: " + this.getPlayContext().getTrackId());
            }
            this.showNewDetailsFrag();
            return true;
        }
        Log.v("MovieDetailsActivity", "No more videos in back stack, finishing...");
        return false;
    }
    
    @Override
    protected void onCreate(final Bundle bundle) {
        if (bundle != null) {
            final Iterator<Parcelable> iterator = bundle.getParcelableArrayList("extra_back_stack").iterator();
            while (iterator.hasNext()) {
                this.backStack.add((BackStackData)iterator.next());
            }
        }
        this.handleNewVideoId();
        super.onCreate(bundle);
    }
    
    @Override
    public void onManagerReady(final ServiceManager manager, final int n) {
        super.onManagerReady(manager, n);
        this.manager = manager;
    }
    
    @Override
    public void onManagerUnavailable(final ServiceManager serviceManager, final int n) {
        super.onManagerUnavailable(serviceManager, n);
        this.manager = null;
    }
    
    protected void onNewIntent(final Intent intent) {
        Log.v("MovieDetailsActivity", "onNewIntent: " + intent);
        super.onNewIntent(intent);
        this.setIntent(intent);
        this.handleNewVideoId();
        this.showNewDetailsFrag();
    }
    
    @Override
    protected void onSaveInstanceState(final Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putParcelableArrayList("extra_back_stack", (ArrayList)this.backStack);
    }
    
    public static class BackStackData implements Parcelable
    {
        public static final Parcelable$Creator<BackStackData> CREATOR;
        private final PlayContext playContext;
        private final String videoId;
        
        static {
            CREATOR = (Parcelable$Creator)new Parcelable$Creator<BackStackData>() {
                public BackStackData createFromParcel(final Parcel parcel) {
                    return new BackStackData(parcel);
                }
                
                public BackStackData[] newArray(final int n) {
                    return new BackStackData[n];
                }
            };
        }
        
        private BackStackData(final Parcel parcel) {
            this.videoId = parcel.readString();
            this.playContext = (PlayContext)parcel.readParcelable(PlayContextImp.class.getClassLoader());
        }
        
        public BackStackData(final String videoId, final PlayContext playContext) {
            this.videoId = videoId;
            this.playContext = playContext;
        }
        
        public int describeContents() {
            return 0;
        }
        
        public void writeToParcel(final Parcel parcel, final int n) {
            parcel.writeString(this.videoId);
            parcel.writeParcelable((Parcelable)this.playContext, n);
        }
    }
}