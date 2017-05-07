// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.mediaclient.ui.home;

import android.widget.BaseAdapter;
import com.netflix.mediaclient.servicemgr.LoggingManagerCallback;
import android.widget.ListAdapter;
import com.netflix.mediaclient.servicemgr.GenreList;
import java.util.List;
import com.netflix.mediaclient.servicemgr.UserProfile;
import com.netflix.mediaclient.servicemgr.IClientLogging;
import com.netflix.mediaclient.android.activity.NetflixActivity;
import com.netflix.mediaclient.util.gfx.AnimationUtils;
import com.netflix.mediaclient.servicemgr.ManagerCallback;
import com.netflix.mediaclient.Log;
import android.view.ViewGroup;
import android.widget.AdapterView;
import com.netflix.mediaclient.ui.profiles.ProfileSelectionActivity;
import android.content.Intent;
import android.content.Context;
import android.widget.ImageView;
import com.netflix.mediaclient.android.widget.AdvancedImageView;
import android.widget.AdapterView$OnItemClickListener;
import android.view.View$OnClickListener;
import com.netflix.mediaclient.servicemgr.ServiceManager;
import android.widget.ListView;
import com.netflix.mediaclient.android.widget.LoadingAndErrorWrapper;
import android.widget.TextView;
import com.netflix.mediaclient.android.widget.ErrorWrapper;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.app.Activity;
import com.netflix.mediaclient.servicemgr.ManagerStatusListener;

public class SlidingMenuAdapter implements ManagerStatusListener
{
    private static final String TAG = "SlidingMenuAdapter";
    private final Activity activity;
    private GenresListAdapter adapter;
    private final View content;
    private final DrawerLayout drawerLayout;
    private final ErrorWrapper.Callback errorCallback;
    private final TextView home;
    private final LoadingAndErrorWrapper leWrapper;
    private final ListView list;
    private ServiceManager manager;
    private final View$OnClickListener onHomeClickListener;
    private final AdapterView$OnItemClickListener onRowClickListener;
    private final View$OnClickListener onSwitchProfileClickListener;
    private final AdvancedImageView profileImg;
    private final TextView profileName;
    private final View profilesGroup;
    private final ImageView switchProfilesIcon;
    
    public SlidingMenuAdapter(final Activity activity, final DrawerLayout drawerLayout) {
        this.errorCallback = new ErrorWrapper.Callback() {
            @Override
            public void onRetryRequested() {
                SlidingMenuAdapter.this.refresh();
            }
        };
        this.onHomeClickListener = (View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                final Intent startIntent = HomeActivity.createStartIntent((Context)SlidingMenuAdapter.this.activity);
                startIntent.addFlags(67108864);
                SlidingMenuAdapter.this.activity.startActivity(startIntent);
                SlidingMenuAdapter.this.drawerLayout.closeDrawers();
            }
        };
        this.onSwitchProfileClickListener = (View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                if (SlidingMenuAdapter.this.managerNotReady()) {
                    return;
                }
                SlidingMenuAdapter.this.activity.startActivity(ProfileSelectionActivity.createStartIntent(SlidingMenuAdapter.this.activity));
            }
        };
        this.onRowClickListener = (AdapterView$OnItemClickListener)new AdapterView$OnItemClickListener() {
            public void onItemClick(final AdapterView<?> adapterView, final View view, final int n, final long n2) {
                HomeActivity.showGenreList(SlidingMenuAdapter.this.activity, SlidingMenuAdapter.this.adapter.getItem(n - 1));
                SlidingMenuAdapter.this.drawerLayout.closeDrawers();
            }
        };
        this.activity = activity;
        (this.content = drawerLayout.findViewById(2131099753)).setOnClickListener((View$OnClickListener)null);
        this.leWrapper = new LoadingAndErrorWrapper(this.content, this.errorCallback);
        (this.profilesGroup = this.content.findViewById(2131099754)).setOnClickListener(this.onSwitchProfileClickListener);
        this.switchProfilesIcon = (ImageView)this.profilesGroup.findViewById(2131099755);
        this.profileName = (TextView)this.content.findViewById(2131099757);
        (this.profileImg = (AdvancedImageView)this.content.findViewById(2131099756)).setPressedStateHandlerEnabled(false);
        final View inflate = activity.getLayoutInflater().inflate(2130903068, (ViewGroup)null);
        (this.home = (TextView)inflate.findViewById(2131099759)).setText(2131493118);
        this.home.setOnClickListener(this.onHomeClickListener);
        (this.list = (ListView)this.content.findViewById(2131099758)).setFocusable(false);
        this.list.addHeaderView(inflate, (Object)null, false);
        this.drawerLayout = drawerLayout;
        this.fetchGenresDataIfReady();
    }
    
    private void fetchGenresDataIfReady() {
        if (this.managerNotReady()) {
            return;
        }
        if (this.content == null) {
            Log.v("SlidingMenuAdapter", "Content is null - can't fetch data yet");
            return;
        }
        Log.v("SlidingMenuAdapter", "Fetching genres list...");
        this.manager.fetchGenreLists(new FetchGenresCallback());
    }
    
    private boolean managerNotReady() {
        if (this.manager == null || !this.manager.isReady()) {
            Log.v("SlidingMenuAdapter", "Manager is not ready - can't update");
            return true;
        }
        return false;
    }
    
    private void showErrorView() {
        this.leWrapper.showErrorView(true);
        AnimationUtils.hideView((View)this.list, true);
    }
    
    private void showLoadingView() {
        this.leWrapper.showLoadingView(true);
        AnimationUtils.hideView((View)this.list, true);
    }
    
    private void updateProfileInfo() {
        if (this.managerNotReady()) {
            return;
        }
        final UserProfile currentProfile = this.manager.getCurrentProfile();
        if (currentProfile == null) {
            Log.v("SlidingMenuAdapter", "Profile is null - can't update profile info");
            return;
        }
        this.profileName.setText((CharSequence)currentProfile.getFirstName());
        NetflixActivity.getImageLoader((Context)this.activity).showImg(this.profileImg, currentProfile.getAvatarUrl(), IClientLogging.AssetType.profileAvatar, currentProfile.getFirstName(), false, true);
    }
    
    private void updateProfileViewGroupVisibility() {
        if (this.managerNotReady()) {
            return;
        }
        final List<? extends UserProfile> allProfiles = this.manager.getAllProfiles();
        if (allProfiles == null) {
            Log.w("SlidingMenuAdapter", "No profiles found for user!");
            return;
        }
        final View profilesGroup = this.profilesGroup;
        int visibility;
        if (allProfiles.size() > 1) {
            visibility = 0;
        }
        else {
            visibility = 8;
        }
        profilesGroup.setVisibility(visibility);
    }
    
    private void updateProfileViews() {
        this.updateProfileViewGroupVisibility();
        this.updateProfileInfo();
        this.updateSwitchProfileButton();
    }
    
    private void updateSwitchProfileButton() {
        if (this.managerNotReady()) {
            return;
        }
        this.manager.refreshProfileSwitchingStatus();
        final boolean profileSwitchingDisabled = this.manager.isProfileSwitchingDisabled();
        this.profilesGroup.setEnabled(!profileSwitchingDisabled);
        final ImageView switchProfilesIcon = this.switchProfilesIcon;
        int imageResource;
        if (profileSwitchingDisabled) {
            imageResource = 17301535;
        }
        else {
            imageResource = 2130837576;
        }
        switchProfilesIcon.setImageResource(imageResource);
    }
    
    public void onActivityResume() {
        this.updateProfileViews();
    }
    
    @Override
    public void onManagerReady(final ServiceManager manager, final int n) {
        this.manager = manager;
        this.fetchGenresDataIfReady();
        this.updateProfileViews();
    }
    
    @Override
    public void onManagerUnavailable(final ServiceManager serviceManager, final int n) {
        this.manager = null;
    }
    
    public void refresh() {
        this.showLoadingView();
        this.fetchGenresDataIfReady();
    }
    
    void updateGenres(final List<GenreList> list) {
        this.leWrapper.hide(true);
        AnimationUtils.showView((View)this.list, true);
        this.adapter = new GenresListAdapter(list);
        this.list.setAdapter((ListAdapter)this.adapter);
        this.list.setOnItemClickListener(this.onRowClickListener);
    }
    
    private class FetchGenresCallback extends LoggingManagerCallback
    {
        public FetchGenresCallback() {
            super("SlidingMenuAdapter");
        }
        
        @Override
        public void onGenreListsFetched(final List<GenreList> list, final int n) {
            super.onGenreListsFetched(list, n);
            if (n != 0) {
                Log.w("SlidingMenuAdapter", "Invalid status code");
                SlidingMenuAdapter.this.showErrorView();
                return;
            }
            if (list == null) {
                Log.v("SlidingMenuAdapter", "No details in response");
                SlidingMenuAdapter.this.showErrorView();
                return;
            }
            SlidingMenuAdapter.this.updateGenres(list);
        }
    }
    
    private class GenresListAdapter extends BaseAdapter
    {
        private final List<GenreList> genres;
        
        public GenresListAdapter(final List<GenreList> genres) {
            this.genres = genres;
        }
        
        public int getCount() {
            return this.genres.size();
        }
        
        public GenreList getItem(final int n) {
            return this.genres.get(n);
        }
        
        public long getItemId(final int n) {
            return n;
        }
        
        public View getView(final int n, View inflate, final ViewGroup viewGroup) {
            Holder tag;
            if (inflate == null) {
                inflate = SlidingMenuAdapter.this.activity.getLayoutInflater().inflate(2130903068, (ViewGroup)null);
                tag = new Holder((TextView)inflate.findViewById(2131099759));
                inflate.setTag((Object)tag);
            }
            else {
                tag = (Holder)inflate.getTag();
            }
            tag.tv.setText((CharSequence)this.getItem(n).getTitle());
            return inflate;
        }
    }
    
    private static class Holder
    {
        public final TextView tv;
        
        public Holder(final TextView tv) {
            this.tv = tv;
        }
    }
}