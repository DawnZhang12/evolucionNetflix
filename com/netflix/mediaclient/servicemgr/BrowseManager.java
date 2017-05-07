// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.mediaclient.servicemgr;

import com.netflix.mediaclient.service.browse.BrowseAgent;
import com.netflix.mediaclient.servicemgr.model.Video;
import com.netflix.mediaclient.servicemgr.model.LoMo;
import com.netflix.mediaclient.Log;
import com.netflix.mediaclient.util.StringUtils;

public final class BrowseManager implements IBrowseManager
{
    private static final String ERROR_PARAM_WITH_NULL = "Parameter cannot be null";
    private static final String TAG = "ServiceManagerBrowse";
    private IServiceManagerAccess mgr;
    
    public BrowseManager(final IServiceManagerAccess mgr) {
        this.mgr = mgr;
    }
    
    @Override
    public boolean addToQueue(final String s, final int n, final ManagerCallback managerCallback) {
        if (StringUtils.isEmpty(s)) {
            throw new IllegalArgumentException("Parameter cannot be null");
        }
        final int wrappedRequestId = this.mgr.getWrappedRequestId(managerCallback, s);
        if (Log.isLoggable("ServiceManagerBrowse", 3)) {
            Log.d("ServiceManagerBrowse", "addToQueue requestId=" + wrappedRequestId + " id=" + s);
        }
        final INetflixService service = this.mgr.getService();
        if (service != null) {
            service.getBrowse().addToQueue(s, n, this.mgr.getClientId(), wrappedRequestId);
            return true;
        }
        Log.w("ServiceManagerBrowse", "addToQueue:: service is not available");
        return false;
    }
    
    @Override
    public boolean dumpHomeLoLoMosAndVideos(final String s, final String s2) {
        final INetflixService service = this.mgr.getService();
        if (service != null) {
            service.getBrowse().dumpHomeLoLoMosAndVideos(s, s2);
            return true;
        }
        Log.w("ServiceManagerBrowse", "dumpHomeLoLoMosAndVideos:: service is not available");
        return false;
    }
    
    @Override
    public boolean dumpHomeLoLoMosAndVideosToLog() {
        final INetflixService service = this.mgr.getService();
        if (service != null) {
            service.getBrowse().dumpHomeLoLoMosAndVideosToLog();
            return true;
        }
        Log.w("ServiceManagerBrowse", "dumpHomeLoLoMosAndVideos:: service is not available");
        return false;
    }
    
    @Override
    public boolean dumpHomeLoMos() {
        final INetflixService service = this.mgr.getService();
        if (service != null) {
            service.getBrowse().dumpHomeLoMos();
            return true;
        }
        Log.w("ServiceManagerBrowse", " dumpHomeLoMos:: service is not available");
        return false;
    }
    
    @Override
    public boolean fetchCWVideos(final int n, final int n2, final ManagerCallback managerCallback) {
        synchronized (this) {
            final int requestId = this.mgr.getRequestId(managerCallback);
            if (Log.isLoggable("ServiceManagerBrowse", 3)) {
                Log.d("ServiceManagerBrowse", "fetchCWLoMo requestId=" + requestId + " fromVideo=" + n + " toVideo=" + n2);
            }
            final INetflixService service = this.mgr.getService();
            boolean b;
            if (service != null) {
                service.getBrowse().fetchCWVideos(n, n2, this.mgr.getClientId(), requestId);
                b = true;
            }
            else {
                Log.w("ServiceManagerBrowse", "fetchCWVideos:: service is not available");
                b = false;
            }
            return b;
        }
    }
    
    @Override
    public boolean fetchEpisodeDetails(final String s, final ManagerCallback managerCallback) {
        synchronized (this) {
            if (StringUtils.isEmpty(s)) {
                throw new IllegalArgumentException("Parameter cannot be null");
            }
        }
        final int requestId = this.mgr.getRequestId(managerCallback);
        final String s2;
        if (Log.isLoggable("ServiceManagerBrowse", 3)) {
            Log.d("ServiceManagerBrowse", "fetchEpisodeDetails requestId=" + requestId + " episodeId=" + s2);
        }
        final INetflixService service = this.mgr.getService();
        boolean b;
        if (service != null) {
            service.getBrowse().fetchEpisodeDetails(s2, this.mgr.getClientId(), requestId);
            b = true;
        }
        else {
            Log.w("ServiceManagerBrowse", "fetchEpisodeDetails:: service is not available");
            b = false;
        }
        // monitorexit(this)
        return b;
    }
    
    @Override
    public boolean fetchEpisodes(final String s, final int n, final int n2, final ManagerCallback managerCallback) {
        synchronized (this) {
            if (StringUtils.isEmpty(s)) {
                throw new IllegalArgumentException("Parameter cannot be null");
            }
        }
        final int requestId = this.mgr.getRequestId(managerCallback);
        final String s2;
        if (Log.isLoggable("ServiceManagerBrowse", 3)) {
            Log.d("ServiceManagerBrowse", "fetchEpisodes requestId=" + requestId + " id=" + s2 + " fromEpisode=" + n + " toEpisode=" + n2);
        }
        final INetflixService service = this.mgr.getService();
        boolean b;
        if (service != null) {
            service.getBrowse().fetchEpisodes(s2, n, n2, this.mgr.getClientId(), requestId);
            b = true;
        }
        else {
            Log.w("ServiceManagerBrowse", "fetchEpisodes:: service is not available");
            b = false;
        }
        // monitorexit(this)
        return b;
    }
    
    @Override
    public boolean fetchGenreLists(final ManagerCallback managerCallback) {
        synchronized (this) {
            final int requestId = this.mgr.getRequestId(managerCallback);
            if (Log.isLoggable("ServiceManagerBrowse", 3)) {
                Log.d("ServiceManagerBrowse", "fetchGenreLists requestId=" + requestId);
            }
            final INetflixService service = this.mgr.getService();
            boolean b;
            if (service != null) {
                service.getBrowse().fetchGenreLists(this.mgr.getClientId(), requestId);
                b = true;
            }
            else {
                Log.w("ServiceManagerBrowse", "fetchGenreLists:: service is not available");
                b = false;
            }
            return b;
        }
    }
    
    @Override
    public boolean fetchGenreVideos(final LoMo loMo, final int n, final int n2, final ManagerCallback managerCallback) {
        synchronized (this) {
            if (StringUtils.isEmpty(loMo.getId())) {
                throw new IllegalArgumentException("Parameter cannot be null");
            }
        }
        final int requestId = this.mgr.getRequestId(managerCallback);
        final LoMo loMo2;
        if (Log.isLoggable("ServiceManagerBrowse", 3)) {
            Log.d("ServiceManagerBrowse", "fetchGenreVideos requestId=" + requestId + " genreLoMoId=" + loMo2.getId() + " fromVideo=" + n + " toVideo=" + n2);
        }
        final INetflixService service = this.mgr.getService();
        boolean b;
        if (service != null) {
            service.getBrowse().fetchGenreVideos(loMo2, n, n2, this.mgr.getClientId(), requestId);
            b = true;
        }
        else {
            Log.w("ServiceManagerBrowse", "fetchGenreVideos:: service is not available");
            b = false;
        }
        // monitorexit(this)
        return b;
    }
    
    @Override
    public boolean fetchGenres(String stackTraceString, final int n, final int n2, final ManagerCallback managerCallback) {
        while (true) {
            boolean b = false;
            final INetflixService service;
            Label_0099: {
                synchronized (this) {
                    service = this.mgr.getService();
                    if (service == null) {
                        Log.w("ServiceManagerBrowse", "fetchGenres:: service is not available");
                    }
                    else {
                        if (!StringUtils.isEmpty(stackTraceString)) {
                            break Label_0099;
                        }
                        stackTraceString = android.util.Log.getStackTraceString((Throwable)new Exception("Parameter cannot be null"));
                        Log.w("ServiceManagerBrowse", String.format("fetchGenres:: stack:%s", stackTraceString));
                        service.getClientLogging().getErrorLogging().logHandledException(stackTraceString);
                    }
                    return b;
                }
            }
            final int requestId = this.mgr.getRequestId(managerCallback);
            final String s;
            if (Log.isLoggable("ServiceManagerBrowse", 3)) {
                Log.d("ServiceManagerBrowse", "fetchGenres requestId=" + requestId + " id=" + s);
            }
            service.getBrowse().fetchGenres(s, n, n2, this.mgr.getClientId(), requestId);
            b = true;
            return b;
        }
    }
    
    @Override
    public boolean fetchIQVideos(final LoMo loMo, final int n, final int n2, final ManagerCallback managerCallback) {
        synchronized (this) {
            final int requestId = this.mgr.getRequestId(managerCallback);
            if (Log.isLoggable("ServiceManagerBrowse", 3)) {
                Log.d("ServiceManagerBrowse", "fetchIQLoMo requestId=" + requestId + " fromVideo=" + n + " toVideo=" + n2);
            }
            final INetflixService service = this.mgr.getService();
            boolean b;
            if (service != null) {
                service.getBrowse().fetchIQVideos(loMo, n, n2, this.mgr.getClientId(), requestId);
                b = true;
            }
            else {
                Log.w("ServiceManagerBrowse", "fetchIQVideos:: service is not available");
                b = false;
            }
            return b;
        }
    }
    
    @Override
    public boolean fetchKidsCharacterDetails(final String s, final ManagerCallback managerCallback) {
        boolean b = true;
        synchronized (this) {
            if (StringUtils.isEmpty(s)) {
                throw new IllegalArgumentException("Parameter cannot be null");
            }
        }
        final int requestId = this.mgr.getRequestId(managerCallback);
        final String s2;
        if (Log.isLoggable("ServiceManagerBrowse", 3)) {
            Log.d("ServiceManagerBrowse", String.format("fetchKidsCharacterDetails requestId=%d,  characterId=%s", requestId, s2));
        }
        final INetflixService service = this.mgr.getService();
        if (service != null) {
            service.getBrowse().fetchKidsCharacterDetails(s2, this.mgr.getClientId(), requestId);
        }
        else {
            Log.w("ServiceManagerBrowse", "fetchKidsCharacterDetails:: service is not available");
            b = false;
        }
        // monitorexit(this)
        return b;
    }
    
    @Override
    public boolean fetchLoLoMoSummary(final String s, final ManagerCallback managerCallback) {
        synchronized (this) {
            if (StringUtils.isEmpty(s)) {
                throw new IllegalArgumentException("Parameter cannot be null");
            }
        }
        final int requestId = this.mgr.getRequestId(managerCallback);
        final String s2;
        if (Log.isLoggable("ServiceManagerBrowse", 3)) {
            Log.d("ServiceManagerBrowse", "fetchLoLoMoSummary requestId=" + requestId + " category=" + s2);
        }
        final INetflixService service = this.mgr.getService();
        boolean b;
        if (service != null) {
            service.getBrowse().fetchLoLoMoSummary(s2, this.mgr.getClientId(), requestId);
            b = true;
        }
        else {
            Log.w("ServiceManagerBrowse", "fetchLoLoMoSummary:: service is not available");
            b = false;
        }
        // monitorexit(this)
        return b;
    }
    
    @Override
    public boolean fetchLoMos(final int n, final int n2, final ManagerCallback managerCallback) {
        synchronized (this) {
            final int requestId = this.mgr.getRequestId(managerCallback);
            if (Log.isLoggable("ServiceManagerBrowse", 3)) {
                Log.d("ServiceManagerBrowse", "fetchLoLoMo requestId=" + requestId + " fromLoMo=" + n + " toLoMo=" + n2);
            }
            final INetflixService service = this.mgr.getService();
            boolean b;
            if (service != null) {
                service.getBrowse().fetchLoMos(n, n2, this.mgr.getClientId(), requestId);
                b = true;
            }
            else {
                Log.w("ServiceManagerBrowse", "fetchLoMos:: service is not available");
                b = false;
            }
            return b;
        }
    }
    
    @Override
    public boolean fetchMovieDetails(final String s, final ManagerCallback managerCallback) {
        synchronized (this) {
            if (StringUtils.isEmpty(s)) {
                throw new IllegalArgumentException("Parameter cannot be null");
            }
        }
        final String s2;
        final int wrappedRequestId = this.mgr.getWrappedRequestId(managerCallback, s2);
        if (Log.isLoggable("ServiceManagerBrowse", 3)) {
            Log.d("ServiceManagerBrowse", "fetchMovieDetails requestId=" + wrappedRequestId + " movieId=" + s2);
        }
        final INetflixService service = this.mgr.getService();
        boolean b;
        if (service != null) {
            service.getBrowse().fetchMovieDetails(s2, this.mgr.getClientId(), wrappedRequestId);
            b = true;
        }
        else {
            Log.w("ServiceManagerBrowse", "fetchMovieDetails:: service is not available");
            b = false;
        }
        // monitorexit(this)
        return b;
    }
    
    @Override
    public boolean fetchPostPlayVideos(final String s, final ManagerCallback managerCallback) {
        synchronized (this) {
            final int requestId = this.mgr.getRequestId(managerCallback);
            if (Log.isLoggable("ServiceManagerBrowse", 3)) {
                Log.d("ServiceManagerBrowse", "fetchPostPlayVideos requestId=" + requestId + " currentPlayableId=" + s);
            }
            final INetflixService service = this.mgr.getService();
            boolean b;
            if (service != null) {
                service.getBrowse().fetchPostPlayVideos(s, this.mgr.getClientId(), requestId);
                b = true;
            }
            else {
                Log.w("ServiceManagerBrowse", "fetchPostPlayVideos:: service is not available");
                b = false;
            }
            return b;
        }
    }
    
    @Override
    public boolean fetchSeasonDetails(final String s, final ManagerCallback managerCallback) {
        synchronized (this) {
            if (StringUtils.isEmpty(s)) {
                throw new IllegalArgumentException("Parameter cannot be null");
            }
        }
        final int requestId = this.mgr.getRequestId(managerCallback);
        final String s2;
        if (Log.isLoggable("ServiceManagerBrowse", 3)) {
            Log.d("ServiceManagerBrowse", "fetchSeasonDetails requestId=" + requestId + " seasonId=" + s2);
        }
        final INetflixService service = this.mgr.getService();
        boolean b;
        if (service != null) {
            service.getBrowse().fetchSeasonDetails(s2, this.mgr.getClientId(), requestId);
            b = true;
        }
        else {
            Log.w("ServiceManagerBrowse", "fetchSeasonDetails:: service is not available");
            b = false;
        }
        // monitorexit(this)
        return b;
    }
    
    @Override
    public boolean fetchSeasons(final String s, final int n, final int n2, final ManagerCallback managerCallback) {
        synchronized (this) {
            if (StringUtils.isEmpty(s)) {
                throw new IllegalArgumentException("Parameter cannot be null");
            }
        }
        final int requestId = this.mgr.getRequestId(managerCallback);
        final String s2;
        if (Log.isLoggable("ServiceManagerBrowse", 3)) {
            Log.d("ServiceManagerBrowse", "fetchSeasons requestId=" + requestId + " id=" + s2 + " fromSeason=" + n + " toSeason=" + n2);
        }
        final INetflixService service = this.mgr.getService();
        boolean b;
        if (service != null) {
            service.getBrowse().fetchSeasons(s2, n, n2, this.mgr.getClientId(), requestId);
            b = true;
        }
        else {
            Log.w("ServiceManagerBrowse", "fetchSeasons:: service is not available");
            b = false;
        }
        // monitorexit(this)
        return b;
    }
    
    @Override
    public boolean fetchShowDetails(final String s, final String s2, final ManagerCallback managerCallback) {
        synchronized (this) {
            if (StringUtils.isEmpty(s)) {
                throw new IllegalArgumentException("Parameter cannot be null");
            }
        }
        final String s3;
        final int wrappedRequestId = this.mgr.getWrappedRequestId(managerCallback, s3);
        if (Log.isLoggable("ServiceManagerBrowse", 3)) {
            Log.d("ServiceManagerBrowse", "fetchShowDetails requestId=" + wrappedRequestId + " id=" + s3);
        }
        final INetflixService service = this.mgr.getService();
        boolean b;
        if (service != null) {
            service.getBrowse().fetchShowDetails(s3, s2, this.mgr.getClientId(), wrappedRequestId);
            b = true;
        }
        else {
            Log.w("ServiceManagerBrowse", "fetchShowDetails:: service is not available");
            b = false;
        }
        // monitorexit(this)
        return b;
    }
    
    @Override
    public boolean fetchSimilarVideosForPerson(final String s, final int n, final ManagerCallback managerCallback, final String s2) {
        synchronized (this) {
            final int requestId = this.mgr.getRequestId(managerCallback);
            final INetflixService service = this.mgr.getService();
            boolean b;
            if (service != null) {
                service.getBrowse().fetchSimilarVideosForPerson(s, n, this.mgr.getClientId(), requestId, s2);
                b = true;
            }
            else {
                Log.w("ServiceManagerBrowse", "searchNetflix:: service is not available");
                b = false;
            }
            return b;
        }
    }
    
    @Override
    public boolean fetchSimilarVideosForQuerySuggestion(final String s, final int n, final ManagerCallback managerCallback, final String s2) {
        synchronized (this) {
            final int requestId = this.mgr.getRequestId(managerCallback);
            final INetflixService service = this.mgr.getService();
            boolean b;
            if (service != null) {
                service.getBrowse().fetchSimilarVideosForQuerySuggestion(s, n, this.mgr.getClientId(), requestId, s2);
                b = true;
            }
            else {
                Log.w("ServiceManagerBrowse", "searchNetflix:: service is not available");
                b = false;
            }
            return b;
        }
    }
    
    @Override
    public boolean fetchVideos(final LoMo loMo, final int n, final int n2, final ManagerCallback managerCallback) {
        // monitorenter(this)
        while (true) {
            if (loMo != null) {
                try {
                    if (StringUtils.isEmpty(loMo.getId())) {
                        throw new IllegalArgumentException("Parameter cannot be null");
                    }
                }
                finally {
                }
                // monitorexit(this)
                final int requestId = this.mgr.getRequestId(managerCallback);
                final LoMo loMo2;
                if (Log.isLoggable("ServiceManagerBrowse", 3)) {
                    Log.d("ServiceManagerBrowse", "fetchVideos requestId=" + requestId + " loMoId=" + loMo2.getId() + " fromVideo=" + n + " toVideo=" + n2);
                }
                final INetflixService service = this.mgr.getService();
                boolean b;
                if (service != null) {
                    service.getBrowse().fetchVideos(loMo2, n, n2, this.mgr.getClientId(), requestId);
                    b = true;
                }
                else {
                    Log.w("ServiceManagerBrowse", "fetchVideos:: service is not available");
                    b = false;
                }
                // monitorexit(this)
                return b;
            }
            continue;
        }
    }
    
    @Override
    public boolean flushCaches() {
        final INetflixService service = this.mgr.getService();
        if (service != null) {
            service.getBrowse().flushCaches();
            return true;
        }
        Log.w("ServiceManagerBrowse", "flushCaches:: service is not available");
        return false;
    }
    
    @Override
    public boolean hideVideo(final String s, final ManagerCallback managerCallback) {
        if (StringUtils.isEmpty(s)) {
            throw new IllegalArgumentException("Parameter cannot be null");
        }
        final int requestId = this.mgr.getRequestId(managerCallback);
        if (Log.isLoggable("ServiceManagerBrowse", 3)) {
            Log.d("ServiceManagerBrowse", "hideVideo requestId=" + requestId + " id=" + s);
        }
        final INetflixService service = this.mgr.getService();
        if (service != null) {
            service.getBrowse().hideVideo(s, this.mgr.getClientId(), requestId);
            return true;
        }
        Log.w("ServiceManagerBrowse", "hideVideo:: service is not available");
        return false;
    }
    
    @Override
    public void logBillboardActivity(final Video video, final BrowseAgent.BillboardActivityType billboardActivityType) {
        final INetflixService service = this.mgr.getService();
        if (service != null) {
            service.getBrowse().logBillboardActivity(video, billboardActivityType);
            return;
        }
        Log.w("ServiceManagerBrowse", "selectProfile:: service is not available");
    }
    
    @Override
    public boolean prefetchGenreLoLoMo(final String s, final int n, final int n2, final int n3, final int n4, final boolean b, final ManagerCallback managerCallback) {
        synchronized (this) {
            final int requestId = this.mgr.getRequestId(managerCallback);
            if (Log.isLoggable("ServiceManagerBrowse", 3)) {
                Log.d("ServiceManagerBrowse", "prefetchGenreLoLoMo requestId=" + requestId + " genreId=" + s + " fromLoMo=" + n + " toLoMo=" + n2 + " fromVideo=" + n3 + " toVideo=" + n4 + "includeBoxshots=" + b);
            }
            final INetflixService service = this.mgr.getService();
            boolean b2;
            if (service != null) {
                service.getBrowse().prefetchGenreLoLoMo(s, n, n2, n3, n4, b, this.mgr.getClientId(), requestId);
                b2 = true;
            }
            else {
                Log.w("ServiceManagerBrowse", "prefetchGenreLoLoMo:: service is not available");
                b2 = false;
            }
            return b2;
        }
    }
    
    @Override
    public boolean prefetchLoLoMo(final int n, final int n2, final int n3, final int n4, final int n5, final int n6, final boolean b, final boolean b2, final ManagerCallback managerCallback) {
        final int requestId = this.mgr.getRequestId(managerCallback);
        if (Log.isLoggable("ServiceManagerBrowse", 3)) {
            Log.d("ServiceManagerBrowse", "prefetchLoLoMo requestId=" + requestId + " fromLoMo=" + n + " toLoMo=" + n2 + " fromVideo=" + n3 + " toVideo=" + n4 + " fromCWVideo=" + n5 + " toCWVideo=" + n6 + " includeExtraCharacters=" + b + "includeBoxshots=" + b2);
        }
        final INetflixService service = this.mgr.getService();
        if (service != null) {
            service.getBrowse().prefetchLoLoMo(n, n2, n3, n4, n5, n6, b, b2, this.mgr.getClientId(), requestId);
            return true;
        }
        Log.w("ServiceManagerBrowse", "prefetchLoLoMo:: service is not available");
        return false;
    }
    
    @Override
    public boolean removeFromQueue(final String s, final ManagerCallback managerCallback) {
        if (StringUtils.isEmpty(s)) {
            throw new IllegalArgumentException("Parameter cannot be null");
        }
        final int wrappedRequestId = this.mgr.getWrappedRequestId(managerCallback, s);
        if (Log.isLoggable("ServiceManagerBrowse", 3)) {
            Log.d("ServiceManagerBrowse", "removeFromQueue requestId=" + wrappedRequestId + " id=" + s);
        }
        final INetflixService service = this.mgr.getService();
        if (service != null) {
            service.getBrowse().removeFromQueue(s, this.mgr.getClientId(), wrappedRequestId);
            return true;
        }
        Log.w("ServiceManagerBrowse", "removeFromQueue:: service is not available");
        return false;
    }
    
    @Override
    public boolean searchNetflix(final String s, final ManagerCallback managerCallback) {
        synchronized (this) {
            final int requestId = this.mgr.getRequestId(managerCallback);
            if (Log.isLoggable("ServiceManagerBrowse", 3)) {
                Log.d("ServiceManagerBrowse", "searchNetflix requestId=" + requestId);
            }
            final INetflixService service = this.mgr.getService();
            boolean b;
            if (service != null) {
                service.getBrowse().searchNetflix(s, this.mgr.getClientId(), requestId);
                b = true;
            }
            else {
                Log.w("ServiceManagerBrowse", "searchNetflix:: service is not available");
                b = false;
            }
            return b;
        }
    }
    
    @Override
    public boolean setVideoRating(final String s, final int n, final int n2, final ManagerCallback managerCallback) {
        synchronized (this) {
            if (StringUtils.isEmpty(s)) {
                throw new IllegalArgumentException("Parameter cannot be null");
            }
        }
        final int requestId = this.mgr.getRequestId(managerCallback);
        final String s2;
        if (Log.isLoggable("ServiceManagerBrowse", 3)) {
            Log.d("ServiceManagerBrowse", "setVideoRating requestId=" + requestId + " id=" + s2);
        }
        final INetflixService service = this.mgr.getService();
        boolean b;
        if (service != null) {
            service.getBrowse().setVideoRating(s2, n, n2, this.mgr.getClientId(), requestId);
            b = true;
        }
        else {
            Log.w("ServiceManagerBrowse", "setVideoRating:: service is not available");
            b = false;
        }
        // monitorexit(this)
        return b;
    }
}