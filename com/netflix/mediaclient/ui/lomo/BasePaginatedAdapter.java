// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.mediaclient.ui.lomo;

import java.util.Iterator;
import com.netflix.mediaclient.servicemgr.UiLocation;
import com.netflix.mediaclient.servicemgr.LoMoUtils;
import com.netflix.mediaclient.servicemgr.VideoType;
import com.netflix.mediaclient.servicemgr.BasicLoMo;
import com.netflix.mediaclient.servicemgr.ServiceManager;
import android.view.View;
import com.netflix.mediaclient.servicemgr.Trackable;
import com.netflix.mediaclient.android.widget.ViewRecycler;
import java.util.ArrayList;
import com.netflix.mediaclient.util.StringUtils;
import com.netflix.mediaclient.util.DataUtil;
import com.netflix.mediaclient.util.MathUtils;
import com.netflix.mediaclient.Log;
import com.netflix.mediaclient.util.DeviceUtils;
import android.content.Context;
import java.util.List;
import android.app.Activity;
import com.netflix.mediaclient.servicemgr.Video;

public abstract class BasePaginatedAdapter<T extends Video>
{
    protected static final boolean DO_NOT_OVERLAP_PAGES = false;
    protected static final boolean OVERLAP_PAGES = true;
    private static final String TAG = "BasePaginatedAdapter";
    private final Activity activity;
    private String currTitle;
    private List<T> data;
    private int listViewPos;
    private final int numItemsPerPage;
    
    public BasePaginatedAdapter(final Context context) {
        this.activity = (Activity)context;
        final int basicScreenOrientation = DeviceUtils.getBasicScreenOrientation(context);
        final int screenSizeCategory = DeviceUtils.getScreenSizeCategory(context);
        this.numItemsPerPage = this.computeNumItemsPerPage(basicScreenOrientation, screenSizeCategory);
        if (Log.isLoggable("BasePaginatedAdapter", 2)) {
            Log.v("BasePaginatedAdapter", this.getClass().getSimpleName() + ", num items: " + this.numItemsPerPage + ", orientation: " + basicScreenOrientation + ", screenSize: " + screenSizeCategory);
        }
    }
    
    private void appendOrUpdate(final List<T> list, final List<T> list2, final int n) {
        for (int i = 0; i < list2.size(); ++i) {
            final Video video = list2.get(i);
            final int n2 = n + i;
            if (n2 < list.size()) {
                list.set(n2, (T)video);
            }
            else {
                list.add((T)video);
            }
        }
    }
    
    private int computeNumPages() {
        return MathUtils.ceiling(this.data.size(), this.numItemsPerPage);
    }
    
    protected static int computeViewPagerWidth(final Context context, final boolean b) {
        if (b) {
            return DeviceUtils.getScreenWidthInPixels(context) - context.getResources().getDimensionPixelOffset(2131361849) * 2;
        }
        return DeviceUtils.getScreenWidthInPixels(context);
    }
    
    private String printLomo() {
        return this.currTitle + ": ";
    }
    
    public void appendData(final List<T> list, final String s, final int n, final int n2) {
        if (list != null) {
            final int size = this.data.size();
            this.appendOrUpdate(this.data, list, n);
            if (Log.isLoggable("BasePaginatedAdapter", 2)) {
                Log.v("BasePaginatedAdapter", this.printLomo() + "appending data starting with item: " + DataUtil.getFirstItemSafely(list) + ", prev size: " + size + ", new size: " + this.data.size());
                if (this.data.size() == size) {
                    Log.w("BasePaginatedAdapter", this.printLomo() + "***** append called but no items added\n");
                }
            }
        }
        if (Log.isLoggable("BasePaginatedAdapter", 2)) {
            Log.v("BasePaginatedAdapter", this.printLomo() + "currTitle: " + this.currTitle + "\ntitle: " + s + ", start: " + n + ", end: " + n2 + ", listViewPos: " + this.listViewPos);
        }
        if (StringUtils.isEmpty(this.currTitle)) {
            if (Log.isLoggable("BasePaginatedAdapter", 2)) {
                Log.v("BasePaginatedAdapter", this.printLomo() + "new title: " + s);
            }
            this.currTitle = s;
        }
        if (!StringUtils.safeEquals(s, this.currTitle)) {
            if (Log.isLoggable("BasePaginatedAdapter", 6)) {
                Log.e("BasePaginatedAdapter", this.printLomo() + "***** title mismatch:" + this.printLomo() + "\n    curr: " + this.currTitle + this.printLomo() + "\n    new: " + s);
            }
            this.currTitle = s;
        }
    }
    
    public void clearData() {
        if (Log.isLoggable("BasePaginatedAdapter", 2)) {
            Log.v("BasePaginatedAdapter", this.printLomo() + "Clearing data...");
        }
        this.data = new ArrayList<T>();
        this.currTitle = "";
        this.listViewPos = -1;
    }
    
    protected abstract int computeNumItemsPerPage(final int p0, final int p1);
    
    protected abstract int computeNumVideosToFetchPerBatch(final Context p0);
    
    protected Activity getActivity() {
        return this.activity;
    }
    
    public int getCount() {
        return this.computeNumPages();
    }
    
    public List<T> getDataForPage(int min) {
        if (min >= this.computeNumPages()) {
            return null;
        }
        min = Math.min(this.numItemsPerPage * min, this.data.size());
        final int min2 = Math.min(this.numItemsPerPage + min, this.data.size());
        if (Log.isLoggable("BasePaginatedAdapter", 2)) {
            Log.v("BasePaginatedAdapter", this.printLomo() + String.format("Getting [%d, %d], data set size: %d", min, min2, this.data.size()));
        }
        return this.data.subList(min, min2);
    }
    
    protected int getListViewPos() {
        return this.listViewPos;
    }
    
    public View getView(final ViewRecycler viewRecycler, final Trackable trackable, final int n) {
        Log.v("BasePaginatedAdapter", this.printLomo() + " Getting page for position: " + n);
        return this.getView(viewRecycler, this.getDataForPage(n), this.numItemsPerPage, n, trackable);
    }
    
    protected abstract View getView(final ViewRecycler p0, final List<T> p1, final int p2, final int p3, final Trackable p4);
    
    public boolean isLastItem(final int n) {
        return n == this.computeNumPages() - 1;
    }
    
    public void restoreFromMemento(final Memento<T> memento) {
        this.data = memento.data;
        this.listViewPos = memento.listViewPos;
        this.currTitle = memento.currTitle;
        if (Log.isLoggable("BasePaginatedAdapter", 2)) {
            Log.v("BasePaginatedAdapter", this.printLomo() + "restored from memento: " + memento);
        }
    }
    
    public Memento<T> saveToMemento() {
        final Memento<T> memento = new Memento<T>(this.data, this.listViewPos, this.currTitle);
        if (Log.isLoggable("BasePaginatedAdapter", 2)) {
            Log.v("BasePaginatedAdapter", this.printLomo() + "saving memento: " + memento);
        }
        return memento;
    }
    
    public void setListViewPos(final int listViewPos) {
        this.listViewPos = listViewPos;
    }
    
    public void trackPresentation(final ServiceManager serviceManager, final BasicLoMo basicLoMo, int n, final Boolean b) {
        final ArrayList<String> list = new ArrayList<String>();
        final List<Video> dataForPage = (List<Video>)this.getDataForPage(n);
        if (dataForPage != null && dataForPage.size() != 0) {
            for (final Video video : dataForPage) {
                if (VideoType.isPresentationTrackingType(video.getType())) {
                    list.add(video.getId());
                }
            }
            n = n * this.numItemsPerPage + LoMoUtils.getClientInjectedVideoCount(basicLoMo, serviceManager.isCurrentProfileFacebookConnected(), n);
            UiLocation uiLocation;
            if (b) {
                uiLocation = UiLocation.GENRE_LOLOMO;
            }
            else {
                uiLocation = UiLocation.HOME_LOLOMO;
            }
            if (list.size() > 0) {
                serviceManager.getClientLogging().getPresentationTracking().reportPresentation(basicLoMo, list, n, uiLocation);
                Log.d("nf_presentation", String.format("t %d, row %d, rank %d,  %s", System.currentTimeMillis(), basicLoMo.getListPos(), n, list.toString()));
            }
        }
    }
    
    static class Memento<T>
    {
        final String currTitle;
        final List<T> data;
        final int listViewPos;
        
        protected Memento(final List<T> data, final int listViewPos, final String currTitle) {
            this.data = data;
            this.listViewPos = listViewPos;
            this.currTitle = currTitle;
        }
        
        @Override
        public String toString() {
            return "title: " + this.currTitle + ", data size: " + this.data.size() + ", listViewPos: " + this.listViewPos;
        }
    }
}