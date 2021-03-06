// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.mediaclient.servicemgr;

import com.netflix.model.leafs.advisory.ExpiringContentAdvisory$ContentAction;
import com.netflix.mediaclient.service.pushnotification.MessageData;
import java.util.List;
import com.netflix.model.leafs.social.IrisNotificationSummary;
import java.util.Map;
import com.netflix.mediaclient.servicemgr.interface_.Video;
import com.netflix.falkor.ModelProxy;
import com.netflix.falkor.task.CmpTaskDetails;
import com.netflix.mediaclient.ui.player.PostPlayRequestContext;
import com.netflix.mediaclient.servicemgr.interface_.LoMo;
import java.io.File;
import com.netflix.mediaclient.servicemgr.interface_.VideoType;

public interface IBrowseInterface
{
    void addToQueue(final String p0, final VideoType p1, final int p2, final String p3, final int p4, final int p5);
    
    void dumpCacheToDisk(final File p0);
    
    void endBrowsePlaySession(final long p0, final int p1, final int p2, final int p3);
    
    void fetchActorDetailsAndRelatedForTitle(final String p0, final int p1, final int p2);
    
    void fetchAdvisories(final String p0, final int p1, final int p2);
    
    void fetchCWVideos(final int p0, final int p1, final int p2, final int p3);
    
    void fetchEpisodeDetails(final String p0, final String p1, final int p2, final int p3);
    
    void fetchEpisodes(final String p0, final VideoType p1, final int p2, final int p3, final int p4, final int p5);
    
    void fetchFalkorVideo(final String p0, final int p1, final int p2);
    
    void fetchGenreLists(final int p0, final int p1);
    
    void fetchGenreVideos(final LoMo p0, final int p1, final int p2, final boolean p3, final int p4, final int p5);
    
    void fetchGenres(final String p0, final int p1, final int p2, final int p3, final int p4);
    
    void fetchIQVideos(final LoMo p0, final int p1, final int p2, final boolean p3, final int p4, final int p5);
    
    void fetchInteractiveVideoMoments(final VideoType p0, final String p1, final String p2, final int p3, final int p4, final int p5, final int p6);
    
    void fetchKidsCharacterDetails(final String p0, final int p1, final int p2);
    
    void fetchLoLoMoSummary(final String p0, final int p1, final int p2);
    
    void fetchLoMos(final int p0, final int p1, final int p2, final int p3);
    
    void fetchMovieDetails(final String p0, final String p1, final int p2, final int p3);
    
    void fetchNotifications(final int p0, final int p1, final int p2, final int p3);
    
    void fetchPersonDetail(final String p0, final int p1, final int p2, final String p3);
    
    void fetchPersonRelated(final String p0, final int p1, final int p2);
    
    void fetchPostPlayVideos(final String p0, final VideoType p1, final PostPlayRequestContext p2, final int p3, final int p4);
    
    void fetchPreAppData(final int p0, final int p1);
    
    void fetchScenePosition(final VideoType p0, final String p1, final String p2, final int p3, final int p4);
    
    void fetchSeasonDetails(final String p0, final int p1, final int p2);
    
    void fetchSeasons(final String p0, final int p1, final int p2, final int p3, final int p4);
    
    void fetchShowDetails(final String p0, final String p1, final boolean p2, final int p3, final int p4);
    
    void fetchShowDetailsAndSeasons(final String p0, final String p1, final boolean p2, final boolean p3, final int p4, final int p5);
    
    void fetchSimilarVideosForPerson(final String p0, final int p1, final int p2, final int p3, final String p4);
    
    void fetchSimilarVideosForQuerySuggestion(final String p0, final int p1, final int p2, final int p3, final String p4);
    
    void fetchTask(final CmpTaskDetails p0, final int p1, final int p2);
    
    void fetchVideoSummary(final String p0, final int p1, final int p2);
    
    void fetchVideos(final LoMo p0, final int p1, final int p2, final boolean p3, final boolean p4, final boolean p5, final int p6, final int p7);
    
    void flushCaches();
    
    void flushOnlyMemCache();
    
    void forceFetchFromLocalCache(final boolean p0);
    
    String getLolomoId();
    
    ModelProxy<?> getModelProxy();
    
    void invalidateCachedEpisodes(final String p0, final VideoType p1);
    
    void logBillboardActivity(final Video p0, final BillboardInteractionType p1, final Map<String, String> p2);
    
    void logPostPlayImpression(final String p0, final VideoType p1, final String p2, final int p3, final int p4);
    
    void markNotificationAsRead(final IrisNotificationSummary p0);
    
    void markNotificationsAsRead(final List<IrisNotificationSummary> p0);
    
    void prefetchGenreLoLoMo(final String p0, final int p1, final int p2, final int p3, final int p4, final boolean p5, final boolean p6, final int p7, final int p8);
    
    void prefetchLoLoMo(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final boolean p6, final boolean p7, final boolean p8, final int p9, final int p10);
    
    void prefetchVideoListDetails(final List<? extends Video> p0, final int p1, final int p2);
    
    void refreshCw(final boolean p0);
    
    void refreshEpisodeData(final Asset p0);
    
    void refreshIq();
    
    void refreshIrisNotifications(final boolean p0, final boolean p1, final MessageData p2);
    
    void refreshLolomo();
    
    void removeFromQueue(final String p0, final VideoType p1, final String p2, final int p3, final int p4);
    
    void runPrefetchLolomoJob(final boolean p0);
    
    void searchNetflix(final String p0, final int p1, final int p2);
    
    void setVideoRating(final String p0, final VideoType p1, final int p2, final int p3, final int p4, final int p5);
    
    void updateCachedVideoPosition(final Asset p0);
    
    void updateExpiredContentAdvisoryStatus(final String p0, final ExpiringContentAdvisory$ContentAction p1);
}
