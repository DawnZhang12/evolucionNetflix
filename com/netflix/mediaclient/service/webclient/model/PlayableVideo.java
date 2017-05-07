// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.mediaclient.service.webclient.model;

import com.netflix.mediaclient.Log;
import com.netflix.mediaclient.service.browse.BrowseAgent;
import com.netflix.mediaclient.servicemgr.model.VideoType;
import com.netflix.mediaclient.service.webclient.model.leafs.SocialEvidence;
import java.util.List;
import com.netflix.mediaclient.service.webclient.model.leafs.TrackableListSummary;
import com.netflix.mediaclient.service.webclient.model.branches.Episode;
import com.netflix.mediaclient.servicemgr.model.Playable;
import com.netflix.mediaclient.servicemgr.model.Video;

public class PlayableVideo implements Video, Playable
{
    private static final String TAG = "nf_model_playablevideo";
    public com.netflix.mediaclient.service.webclient.model.branches.Video.Bookmark bookmark;
    public com.netflix.mediaclient.service.webclient.model.branches.Video.BookmarkStill bookmarkStill;
    public Episode.Detail currentEpisode;
    public com.netflix.mediaclient.service.webclient.model.branches.Video.Bookmark currentEpisodeBookmark;
    public com.netflix.mediaclient.service.webclient.model.branches.Video.Detail detail;
    public com.netflix.mediaclient.service.webclient.model.branches.Video.InQueue inQueue;
    public com.netflix.mediaclient.service.webclient.model.branches.Video.Rating rating;
    public TrackableListSummary similarListSummary;
    public List<Video> similarVideos;
    public SocialEvidence socialEvidence;
    public com.netflix.mediaclient.service.webclient.model.branches.Video.Summary summary;
    public boolean userConnectedToFacebook;
    
    private String getCurrentEpisodeTitle() {
        if (this.currentEpisode == null) {
            return null;
        }
        return this.currentEpisode.getTitle();
    }
    
    @Override
    public boolean canBeSharedOnFacebook() {
        return this.userConnectedToFacebook && this.socialEvidence != null && !this.socialEvidence.isVideoHidden();
    }
    
    public String getBaseUrl() {
        String baseUrl;
        if (this.detail == null) {
            baseUrl = null;
        }
        else {
            baseUrl = this.detail.baseUrl;
        }
        if (this.currentEpisode == null) {
            return baseUrl;
        }
        return this.currentEpisode.baseUrl;
    }
    
    public long getBookmarkLastUpdateTime() {
        long n = -1L;
        if (this.bookmark != null) {
            n = this.bookmark.getLastModified();
        }
        if (this.currentEpisodeBookmark != null) {
            n = this.currentEpisodeBookmark.getLastModified();
        }
        return n;
    }
    
    public int getBookmarkPosition() {
        int n = 0;
        if (this.bookmark != null) {
            n = this.bookmark.getBookmarkPosition();
        }
        if (this.currentEpisodeBookmark != null) {
            n = this.currentEpisodeBookmark.getBookmarkPosition();
        }
        return n;
    }
    
    @Override
    public String getBoxshotURL() {
        if (this.summary == null) {
            return null;
        }
        return this.summary.getBoxshotURL();
    }
    
    public String getCurrentEpisodeId() {
        if (this.currentEpisode == null) {
            return null;
        }
        return this.currentEpisode.getId();
    }
    
    public int getCurrentEpisodeNumber() {
        if (this.currentEpisode == null) {
            return -1;
        }
        return this.currentEpisode.getEpisodeNumber();
    }
    
    public int getCurrentSeasonNumber() {
        if (this.currentEpisode == null) {
            return -1;
        }
        return this.currentEpisode.getSeasonNumber();
    }
    
    @Override
    public int getEndtime() {
        if (this.detail == null) {
            return 0;
        }
        return this.detail.endtime;
    }
    
    @Override
    public int getEpisodeNumber() {
        if (this.currentEpisode == null) {
            return -1;
        }
        return this.currentEpisode.getEpisodeNumber();
    }
    
    @Override
    public VideoType getErrorType() {
        if (this.summary == null) {
            return null;
        }
        return this.summary.getErrorType();
    }
    
    @Override
    public String getHorzDispUrl() {
        if (this.detail == null) {
            return null;
        }
        return this.detail.horzDispUrl;
    }
    
    @Override
    public String getId() {
        if (this.summary == null) {
            return null;
        }
        return this.summary.getId();
    }
    
    public String getNextEpisodeId() {
        if (this.currentEpisode == null) {
            return null;
        }
        return this.currentEpisode.getNextEpisodeId();
    }
    
    @Override
    public String getParentId() {
        return this.getId();
    }
    
    @Override
    public String getParentTitle() {
        if (VideoType.MOVIE.equals(this.getType())) {
            return null;
        }
        return this.getTitle();
    }
    
    @Override
    public int getPlayableBookmarkPosition() {
        final int computePlayPos = BrowseAgent.computePlayPos(this.getBookmarkPosition(), this.getEndtime(), this.getRuntime());
        if (Log.isLoggable("nf_model_playablevideo", 3)) {
            Log.d("nf_model_playablevideo", String.format("id %s bookmark %d playPos %d endtime %d runtime %d", this.getId(), this.getBookmarkPosition(), computePlayPos, this.getEndtime(), this.getRuntime()));
        }
        return computePlayPos;
    }
    
    @Override
    public long getPlayableBookmarkUpdateTime() {
        return this.getBookmarkLastUpdateTime();
    }
    
    @Override
    public String getPlayableId() {
        if (VideoType.MOVIE.equals(this.getType())) {
            return this.getId();
        }
        return this.getCurrentEpisodeId();
    }
    
    @Override
    public String getPlayableTitle() {
        if (VideoType.MOVIE.equals(this.getType())) {
            return this.getTitle();
        }
        return this.getCurrentEpisodeTitle();
    }
    
    @Override
    public int getRuntime() {
        if (this.detail == null) {
            return 0;
        }
        return this.detail.runtime;
    }
    
    @Override
    public int getSeasonNumber() {
        if (this.currentEpisode == null) {
            return -1;
        }
        return this.currentEpisode.getSeasonNumber();
    }
    
    @Override
    public String getSquareUrl() {
        if (this.summary == null) {
            return null;
        }
        return this.summary.getSquareUrl();
    }
    
    @Override
    public String getTitle() {
        if (this.summary == null) {
            return null;
        }
        return this.summary.getTitle();
    }
    
    @Override
    public String getTvCardUrl() {
        if (this.detail == null) {
            return null;
        }
        return this.detail.tvCardUrl;
    }
    
    @Override
    public VideoType getType() {
        if (this.summary == null) {
            return null;
        }
        return this.summary.getType();
    }
    
    @Override
    public boolean isAutoPlayEnabled() {
        if (VideoType.MOVIE.equals(this.getType())) {
            if (this.detail != null) {
                return this.detail.isAutoPlayEnabled;
            }
        }
        else if (this.currentEpisode != null) {
            return this.currentEpisode.isAutoPlayEnabled();
        }
        return false;
    }
    
    @Override
    public boolean isNextPlayableEpisode() {
        return VideoType.SHOW.equals(this.getType()) && this.currentEpisode != null && this.currentEpisode.isNextPlayableEpisode();
    }
    
    @Override
    public boolean isPinProtected() {
        if (VideoType.MOVIE.equals(this.getType())) {
            if (this.detail != null) {
                return this.detail.isPinProtected;
            }
        }
        else if (this.currentEpisode != null) {
            return this.currentEpisode.isPinProtected;
        }
        return false;
    }
    
    @Override
    public boolean isPlayableEpisode() {
        return this.currentEpisode != null;
    }
    
    @Override
    public boolean isUserConnectedToFacebook() {
        return this.userConnectedToFacebook;
    }
}