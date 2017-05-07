// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.model.branches;

import com.netflix.mediaclient.servicemgr.model.VideoType;
import android.content.Context;
import com.netflix.mediaclient.service.falkor.Falkor$Creator;
import java.util.HashSet;
import java.util.Set;
import com.netflix.falkor.BranchNode;
import com.netflix.falkor.ModelProxy;
import com.netflix.falkor.Ref;
import com.netflix.falkor.BranchMap;
import com.netflix.model.leafs.Season$Detail;
import com.netflix.mediaclient.servicemgr.model.details.SeasonDetails;
import com.netflix.mediaclient.servicemgr.model.BasicVideo;
import com.netflix.model.BaseFalkorObject;

public class FalkorSeason extends BaseFalkorObject implements BasicVideo, SeasonDetails, FalkorObject
{
    public Season$Detail detail;
    private BranchMap<Ref> episodes;
    
    public FalkorSeason(final ModelProxy<? extends BranchNode> modelProxy) {
        super(modelProxy);
    }
    
    @Override
    public Object get(final String s) {
        switch (s) {
            default: {
                return null;
            }
            case "detail": {
                return this.detail;
            }
            case "episodes": {
                return this.episodes;
            }
        }
    }
    
    @Override
    public int getCurrentEpisodeNumber() {
        if (this.detail == null) {
            return -1;
        }
        return this.detail.currentEpisodeNumber;
    }
    
    @Override
    public String getId() {
        if (this.detail == null) {
            return null;
        }
        return this.detail.id;
    }
    
    @Override
    public Set<String> getKeys() {
        final HashSet<String> set = new HashSet<String>();
        if (this.detail != null) {
            set.add("detail");
        }
        if (this.episodes != null) {
            set.add("episodes");
        }
        return set;
    }
    
    @Override
    public int getNumOfEpisodes() {
        if (this.detail == null) {
            return -1;
        }
        return this.detail.episodeCount;
    }
    
    @Override
    public Object getOrCreate(final String s) {
        final Object value = this.get(s);
        if (value != null) {
            return value;
        }
        switch (s) {
            default: {
                return null;
            }
            case "detail": {
                return this.detail = new Season$Detail();
            }
            case "episodes": {
                return this.episodes = new BranchMap<Ref>(Falkor$Creator.Ref);
            }
        }
    }
    
    @Override
    public int getSeasonNumber() {
        if (this.detail == null) {
            return -1;
        }
        return this.detail.number;
    }
    
    @Override
    public String getSeasonNumberTitle(final Context context) {
        return String.format(context.getString(2131493150), this.getSeasonNumber());
    }
    
    @Override
    public String getTitle() {
        if (this.detail == null) {
            return null;
        }
        return this.detail.getTitle();
    }
    
    @Override
    public VideoType getType() {
        if (this.detail == null) {
            return null;
        }
        return this.detail.getType();
    }
    
    @Override
    public int getYear() {
        if (this.detail == null) {
            return 0;
        }
        return this.detail.year;
    }
    
    @Override
    public void set(final String s, final Object o) {
        if ("detail".equals(s)) {
            this.detail = (Season$Detail)o;
            return;
        }
        if ("episodes".equals(s)) {
            this.episodes = (BranchMap<Ref>)o;
            return;
        }
        throw new IllegalStateException("Can't set key: " + s);
    }
    
    @Override
    public String toString() {
        return this.getTitle();
    }
}