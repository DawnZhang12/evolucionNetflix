// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.mediaclient.ui.kubrick_kids.details;

import com.netflix.mediaclient.servicemgr.interface_.details.SeasonDetails;
import java.util.List;
import com.netflix.mediaclient.util.gfx.AnimationUtils;
import android.support.v7.widget.RecyclerView$Adapter;
import com.netflix.mediaclient.servicemgr.interface_.Video;
import java.util.Collection;
import com.netflix.mediaclient.android.app.Status;
import com.netflix.mediaclient.servicemgr.ServiceManager;
import com.netflix.mediaclient.android.widget.RecyclerViewHeaderAdapter;
import com.netflix.mediaclient.ui.details.SeasonsSpinnerAdapter;
import com.netflix.mediaclient.android.widget.NetflixActionBar;
import com.netflix.mediaclient.ui.details.DetailsPageParallaxScrollListener$IScrollStateChanged;
import android.support.v7.widget.RecyclerView$OnScrollListener;
import com.netflix.mediaclient.servicemgr.interface_.details.ShowDetails;
import com.netflix.mediaclient.servicemgr.ManagerCallback;
import com.netflix.mediaclient.Log;
import android.os.Bundle;
import android.app.Fragment;
import android.view.ViewGroup;
import com.netflix.mediaclient.ui.details.VideoDetailsViewGroup;
import com.netflix.mediaclient.ui.details.DetailsPageParallaxScrollListener;
import com.netflix.mediaclient.ui.details.SeasonsSpinner;
import android.support.v7.widget.RecyclerView;
import com.netflix.mediaclient.ui.kubrick.details.BarkerShowDetailsFrag$HeroSlideshow;
import android.annotation.SuppressLint;
import com.netflix.mediaclient.servicemgr.interface_.Playable;
import com.netflix.mediaclient.ui.details.VideoDetailsViewGroup$DetailsStringProvider;
import com.netflix.mediaclient.ui.experience.BrowseExperience;
import com.netflix.mediaclient.servicemgr.IClientLogging$AssetType;
import com.netflix.mediaclient.android.activity.NetflixActivity;
import com.netflix.mediaclient.servicemgr.interface_.details.VideoDetails;
import com.netflix.mediaclient.util.ViewUtils;
import com.netflix.mediaclient.servicemgr.interface_.details.KidsCharacterDetails;
import com.netflix.mediaclient.util.DeviceUtils;
import android.view.View;
import com.netflix.mediaclient.util.api.Api16Util;
import android.view.View$OnClickListener;
import com.netflix.mediaclient.android.widget.PressedStateHandler$DelayedOnClickListener;
import com.netflix.mediaclient.ui.kids.KidsUtils;
import android.graphics.drawable.Drawable;
import android.content.Context;
import com.netflix.mediaclient.android.widget.PressAnimationFrameLayout;
import android.widget.ImageView;
import com.netflix.mediaclient.android.widget.AdvancedImageView;
import com.netflix.mediaclient.ui.kubrick.details.BarkerVideoDetailsViewGroup;

class BarkerKidsCharacterDetailsFrag$KubrickKidsCharacterDetailsViewGroup extends BarkerVideoDetailsViewGroup
{
    public static final float CHARACTER_IMAGE_SIZE_MULTIPLIER = 0.39999998f;
    public static final float CW_IMAGE_SIZE_MULTIPLIER = 0.6f;
    public static final float CW_PLAYABLE_TITLE_SIZE_MULTIPLIER = 0.36f;
    AdvancedImageView characterView;
    ImageView playView;
    PressAnimationFrameLayout pressableCWImgGroup;
    final /* synthetic */ BarkerKidsCharacterDetailsFrag this$0;
    
    public BarkerKidsCharacterDetailsFrag$KubrickKidsCharacterDetailsViewGroup(final BarkerKidsCharacterDetailsFrag this$0, final Context context) {
        this.this$0 = this$0;
        super(context);
        this.setupViews();
        this.setupPlayButton();
    }
    
    private Drawable getBackgroundResource() {
        switch (this.this$0.kidsColor) {
            default: {
                return this.this$0.getActivity().getResources().getDrawable(2130837578);
            }
            case 2131624055: {
                return this.this$0.getActivity().getResources().getDrawable(2130837582);
            }
            case 2131624048: {
                return this.this$0.getActivity().getResources().getDrawable(2130837578);
            }
            case 2131624050: {
                return this.this$0.getActivity().getResources().getDrawable(2130837579);
            }
            case 2131624052: {
                return this.this$0.getActivity().getResources().getDrawable(2130837581);
            }
            case 2131624051: {
                return this.this$0.getActivity().getResources().getDrawable(2130837580);
            }
        }
    }
    
    private void setupPlayButton() {
        if (this.playView == null) {
            return;
        }
        int imageResource = 0;
        switch (this.this$0.kidsColor) {
            default: {
                imageResource = 2130837592;
                break;
            }
            case 2131624055: {
                imageResource = 2130837596;
                break;
            }
            case 2131624048: {
                imageResource = 2130837591;
                break;
            }
            case 2131624050: {
                imageResource = 2130837593;
                break;
            }
            case 2131624052: {
                imageResource = 2130837595;
                break;
            }
            case 2131624051: {
                imageResource = 2130837594;
                break;
            }
        }
        this.playView.setImageResource(imageResource);
    }
    
    private void setupViews() {
        final int detailsPageContentWidth = KidsUtils.getDetailsPageContentWidth((Context)this.this$0.getActivity());
        this.getHeroImage().setPressedStateHandlerEnabled(false);
        this.getHeroImage2().setPressedStateHandlerEnabled(false);
        this.pressableCWImgGroup.setOnClickListener((View$OnClickListener)new PressedStateHandler$DelayedOnClickListener(this.pressableCWImgGroup.getPressedStateHandler(), this.onCWClickListener));
        Api16Util.setBackgroundDrawableCompat((View)this.getBackgroundImage(), this.getBackgroundResource());
        this.getBackgroundImage().getLayoutParams().width = detailsPageContentWidth;
        int n;
        if (DeviceUtils.isLandscape(this.getContext())) {
            n = (int)(DeviceUtils.getScreenHeightInPixels((Context)this.this$0.getActivity()) * 0.7);
        }
        else {
            n = (int)(detailsPageContentWidth * 0.5625f);
        }
        this.getBackgroundImage().getLayoutParams().height = n;
        this.characterView.getLayoutParams().height = n;
        this.characterView.getLayoutParams().width = (int)(detailsPageContentWidth * 0.39999998f);
        this.getHeroImage().getLayoutParams().width = (int)(n * 1.778f * 0.6f);
        this.getHeroImage().getLayoutParams().height = (int)(n * 0.6f);
        this.basicSupplementalInfo.getLayoutParams().width = (int)(detailsPageContentWidth * 0.36f);
    }
    
    private void updateTitle(final KidsCharacterDetails kidsCharacterDetails) {
        if (kidsCharacterDetails != null && this.title != null && ViewUtils.isVisible((View)this.title)) {
            this.title.setText((CharSequence)kidsCharacterDetails.getCharacterName());
            this.titleImg.setVisibility(8);
        }
    }
    
    @Override
    protected void alignViews() {
    }
    
    @Override
    protected void findViews() {
        super.findViews();
        this.characterView = (AdvancedImageView)this.findViewById(2131689843);
        this.playView = (ImageView)this.findViewById(2131689846);
        this.pressableCWImgGroup = (PressAnimationFrameLayout)this.findViewById(2131689844);
    }
    
    @Override
    protected int getlayoutId() {
        return 2130903134;
    }
    
    @Override
    protected void setupImageClicks(final VideoDetails videoDetails, final NetflixActivity netflixActivity) {
    }
    
    protected void updateBoxart(final KidsCharacterDetails kidsCharacterDetails) {
        if (kidsCharacterDetails == null) {
            return;
        }
        final String storyUrl = kidsCharacterDetails.getStoryUrl();
        NetflixActivity.getImageLoader((Context)this.this$0.getActivity()).showImg(this.horzDispImg, storyUrl, IClientLogging$AssetType.boxArt, String.format(this.getResources().getString(2131230893), this.this$0.kidsCharacterDetails.getTitle()), BrowseExperience.getImageLoaderConfig(), true);
        this.horzDispImg.setTag((Object)storyUrl);
    }
    
    public void updateCharacterDetails(final KidsCharacterDetails kidsCharacterDetails) {
        this.updateBoxart(kidsCharacterDetails);
        this.updateCharacterImage();
        this.updateTitle(kidsCharacterDetails);
    }
    
    protected void updateCharacterImage() {
        if (this.characterView != null) {
            NetflixActivity.getImageLoader((Context)this.this$0.getActivity()).showImg(this.characterView, this.this$0.kidsCharacterDetails.getCharacterImageUrl(), IClientLogging$AssetType.boxArt, String.format(this.getResources().getString(2131230893), this.this$0.kidsCharacterDetails.getTitle()), BrowseExperience.getImageLoaderConfig(), true);
        }
    }
    
    @Override
    public void updateDetails(final VideoDetails videoDetails, final VideoDetailsViewGroup$DetailsStringProvider videoDetailsViewGroup$DetailsStringProvider) {
        super.updateDetails(videoDetails, videoDetailsViewGroup$DetailsStringProvider);
        final Playable playable = videoDetails.getPlayable();
        if (playable != null) {
            this.basicSupplementalInfo.setText((CharSequence)playable.getPlayableTitle());
        }
    }
}
