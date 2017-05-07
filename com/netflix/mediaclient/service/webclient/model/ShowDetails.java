// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.mediaclient.service.webclient.model;

import com.netflix.mediaclient.servicemgr.model.Video;
import com.netflix.mediaclient.service.browse.BrowseAgent;
import com.netflix.mediaclient.servicemgr.model.user.FriendProfile;
import java.util.List;
import com.netflix.mediaclient.servicemgr.model.IconFontGlyph;
import java.io.Serializable;
import com.netflix.mediaclient.Log;
import com.netflix.mediaclient.service.webclient.model.branches.Video$KubrickSummary;
import com.netflix.mediaclient.service.webclient.model.branches.Video$HeroImages;
import com.netflix.mediaclient.service.webclient.model.branches.Video$Bookmark;
import com.netflix.mediaclient.service.webclient.model.branches.Episode$Detail;
import com.netflix.mediaclient.servicemgr.model.details.KubrickShowDetails;
import com.netflix.mediaclient.servicemgr.model.Playable;
import com.netflix.mediaclient.service.webclient.model.branches.Show;

public class ShowDetails extends Show implements Playable, KubrickShowDetails, com.netflix.mediaclient.servicemgr.model.details.ShowDetails
{
    private static final String TAG = "nf_service_browse_showdetails";
    public Episode$Detail currentEpisode;
    public Video$Bookmark currentEpisodeBookmark;
    public Video$HeroImages heroImgs;
    public Video$KubrickSummary kubrickSummary;
    public boolean userConnectedToFacebook;
    
    @Override
    public boolean canBeSharedOnFacebook() {
        if (Log.isLoggable("nf_service_browse_showdetails", 2)) {
            final boolean userConnectedToFacebook = this.userConnectedToFacebook;
            Serializable value;
            if (this.socialEvidence == null) {
                value = "n/a";
            }
            else {
                value = this.socialEvidence.isVideoHidden();
            }
            Log.v("nf_service_browse_showdetails", String.format("userConnectedToFacebook: %s, isVideoHidden(): %s", userConnectedToFacebook, value));
        }
        return this.userConnectedToFacebook && this.socialEvidence != null && !this.socialEvidence.isVideoHidden();
    }
    
    @Override
    public String getActors() {
        if (this.detail == null) {
            return null;
        }
        return this.detail.actors;
    }
    
    @Override
    public String getBifUrl() {
        if (this.currentEpisode == null) {
            return null;
        }
        return this.currentEpisode.bifUrl;
    }
    
    public long getBookmarkCreationTime() {
        if (this.currentEpisodeBookmark == null) {
            return -1L;
        }
        return this.currentEpisodeBookmark.getLastModified();
    }
    
    public int getBookmarkPosition() {
        if (this.currentEpisodeBookmark == null) {
            return 0;
        }
        return this.currentEpisodeBookmark.getBookmarkPosition();
    }
    
    @Override
    public String getCatalogIdUrl() {
        if (this.detail == null) {
            return null;
        }
        return this.detail.restUrl;
    }
    
    @Override
    public String getCertification() {
        if (this.detail == null) {
            return null;
        }
        return this.detail.certification;
    }
    
    @Override
    public String getCreators() {
        if (this.detail == null) {
            return null;
        }
        return this.detail.directors;
    }
    
    @Override
    public String getCurrentEpisodeId() {
        if (this.currentEpisode == null) {
            return null;
        }
        return this.currentEpisode.getId();
    }
    
    @Override
    public int getCurrentEpisodeNumber() {
        if (this.currentEpisode == null) {
            return -1;
        }
        return this.currentEpisode.getEpisodeNumber();
    }
    
    @Override
    public String getCurrentEpisodeTitle() {
        if (this.currentEpisode == null) {
            return null;
        }
        return this.currentEpisode.getTitle();
    }
    
    @Override
    public int getCurrentSeasonNumber() {
        if (this.currentEpisode == null) {
            return -1;
        }
        return this.currentEpisode.getSeasonNumber();
    }
    
    @Override
    public int getEndtime() {
        if (this.currentEpisode == null) {
            return 0;
        }
        return this.currentEpisode.endtime;
    }
    
    @Override
    public int getEpisodeNumber() {
        if (this.currentEpisode == null) {
            return 0;
        }
        return this.currentEpisode.getEpisodeNumber();
    }
    
    @Override
    public IconFontGlyph getEvidenceGlyph() {
        if (this.evidence == null) {
            return null;
        }
        return this.evidence.getIconFontGlyph();
    }
    
    @Override
    public String getEvidenceText() {
        if (this.evidence == null) {
            return null;
        }
        return this.evidence.getText();
    }
    
    @Override
    public List<FriendProfile> getFacebookFriends() {
        if (this.socialEvidence == null) {
            return null;
        }
        return this.socialEvidence.getFacebookFriends();
    }
    
    @Override
    public String getGenres() {
        if (this.detail == null) {
            return null;
        }
        return this.detail.genres;
    }
    
    @Override
    public List<String> getHeroImages() {
        if (this.heroImgs == null) {
            return null;
        }
        return this.heroImgs.heroImgs;
    }
    
    @Override
    public String getHighResolutionLandscapeBoxArtUrl() {
        if (this.detail == null) {
            return null;
        }
        return this.detail.hiResHorzUrl;
    }
    
    @Override
    public String getHighResolutionPortraitBoxArtUrl() {
        if (this.detail == null) {
            return null;
        }
        return this.detail.mdxVertUrl;
    }
    
    @Override
    public String getHorzDispUrl() {
        if (this.detail == null) {
            return null;
        }
        return this.detail.horzDispUrl;
    }
    
    @Override
    public String getKubrickHorzImgUrl() {
        if (this.kubrickSummary == null) {
            return null;
        }
        return this.kubrickSummary.horzDispUrl;
    }
    
    @Override
    public String getKubrickStoryImgUrl() {
        if (this.kubrickSummary == null) {
            return null;
        }
        return this.kubrickSummary.storyImgUrl;
    }
    
    @Override
    public int getLogicalStart() {
        if (this.currentEpisode == null) {
            return 0;
        }
        return this.currentEpisode.logicalStart;
    }
    
    @Override
    public String getNarrative() {
        if (this.kubrickSummary == null) {
            return null;
        }
        return this.kubrickSummary.narrative;
    }
    
    @Override
    public int getNumOfEpisodes() {
        if (this.detail == null) {
            return -1;
        }
        return this.detail.episodeCount;
    }
    
    @Override
    public int getNumOfSeasons() {
        if (this.detail == null) {
            return -1;
        }
        return this.detail.seasonCount;
    }
    
    @Override
    public String getParentId() {
        return this.getId();
    }
    
    @Override
    public String getParentTitle() {
        return this.getTitle();
    }
    
    @Override
    public Playable getPlayable() {
        return this;
    }
    
    @Override
    public int getPlayableBookmarkPosition() {
        final int computePlayPos = BrowseAgent.computePlayPos(this.getBookmarkPosition(), this.getEndtime(), this.getRuntime());
        if (Log.isLoggable("nf_service_browse_showdetails", 3)) {
            Log.d("nf_service_browse_showdetails", String.format("id %s bookmark %d playPos %d endtime %d runtime %d", this.getId(), this.getBookmarkPosition(), computePlayPos, this.getEndtime(), this.getRuntime()));
        }
        return computePlayPos;
    }
    
    @Override
    public long getPlayableBookmarkUpdateTime() {
        return this.getBookmarkCreationTime();
    }
    
    @Override
    public String getPlayableId() {
        return this.getCurrentEpisodeId();
    }
    
    @Override
    public String getPlayableTitle() {
        return this.getCurrentEpisodeTitle();
    }
    
    @Override
    public float getPredictedRating() {
        if (this.detail == null) {
            return -1.0f;
        }
        return this.detail.predictedRating;
    }
    
    @Override
    public String getQuality() {
        if (this.detail == null) {
            return null;
        }
        return this.detail.quality;
    }
    
    @Override
    public int getRuntime() {
        if (this.currentEpisode == null) {
            return 0;
        }
        return this.currentEpisode.runtime;
    }
    
    @Override
    public int getSeasonCount() {
        Integer value;
        if (this.kubrickSummary == null) {
            value = null;
        }
        else {
            value = this.kubrickSummary.seasonCount;
        }
        return value;
    }
    
    @Override
    public int getSeasonNumber() {
        if (this.currentEpisode == null) {
            return 0;
        }
        return this.currentEpisode.getSeasonNumber();
    }
    
    @Override
    public List<Video> getSimilars() {
        return this.similarVideos;
    }
    
    @Override
    public int getSimilarsListPos() {
        return 0;
    }
    
    @Override
    public String getSimilarsRequestId() {
        if (this.similarListSummary != null) {
            return this.similarListSummary.getRequestId();
        }
        return null;
    }
    
    @Override
    public int getSimilarsTrackId() {
        if (this.similarListSummary != null) {
            return this.similarListSummary.getTrackId();
        }
        return 0;
    }
    
    @Override
    public String getStoryDispUrl() {
        if (this.detail == null) {
            return null;
        }
        return this.detail.storyImgDispUrl;
    }
    
    @Override
    public String getStoryUrl() {
        if (this.detail == null) {
            return null;
        }
        return this.detail.storyImgUrl;
    }
    
    @Override
    public String getSynopsis() {
        if (this.detail == null) {
            return null;
        }
        return this.detail.synopsis;
    }
    
    @Override
    public String getTitleImgUrl() {
        if (this.detail == null) {
            return null;
        }
        return this.detail.titleUrl;
    }
    
    @Override
    public String getTvCardUrl() {
        if (this.detail == null) {
            return null;
        }
        return this.detail.tvCardUrl;
    }
    
    @Override
    public float getUserRating() {
        if (this.rating == null) {
            return -1.0f;
        }
        return this.rating.userRating;
    }
    
    @Override
    public int getYear() {
        if (this.detail == null) {
            return 0;
        }
        return this.detail.year;
    }
    
    @Override
    public boolean isAutoPlayEnabled() {
        return this.currentEpisode != null && this.currentEpisode.isAutoPlayEnabled();
    }
    
    @Override
    public boolean isInQueue() {
        return this.inQueue != null && this.inQueue.inQueue;
    }
    
    @Override
    public boolean isNextPlayableEpisode() {
        return this.currentEpisode != null && this.currentEpisode.isNextPlayableEpisode();
    }
    
    @Override
    public boolean isPinProtected() {
        return this.currentEpisode != null && this.currentEpisode.isPinProtected;
    }
    
    @Override
    public boolean isPlayableEpisode() {
        return this.currentEpisode != null;
    }
    
    @Override
    public boolean isVideoHd() {
        return this.detail != null && this.detail.isHdAvailable;
    }
    
    public void setBookmarkPosition(final int bookmarkPosition) {
        if (this.bookmark != null) {
            this.bookmark.setBookmarkPosition(bookmarkPosition);
        }
    }
    
    @Override
    public void setUserRating(final float userRating) {
        this.rating.userRating = userRating;
    }
}
