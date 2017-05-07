// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.mediaclient.ui.lolomo;

import com.netflix.mediaclient.util.AndroidUtils;
import android.graphics.drawable.Drawable;
import com.netflix.mediaclient.util.api.Api16Util;
import java.util.Collection;
import com.netflix.mediaclient.ui.lomo.BillboardView;
import com.netflix.mediaclient.android.app.CommonStatus;
import android.widget.AbsListView;
import com.netflix.mediaclient.android.app.Status;
import com.netflix.mediaclient.util.ThreadUtils;
import android.widget.ListView;
import com.netflix.mediaclient.util.ViewUtils;
import android.view.ViewGroup;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import com.netflix.mediaclient.servicemgr.model.LoMoType;
import com.netflix.mediaclient.servicemgr.ManagerCallback;
import android.content.res.Resources;
import com.netflix.mediaclient.servicemgr.model.LoMoUtils;
import android.widget.RelativeLayout$LayoutParams;
import android.widget.TextView;
import android.widget.LinearLayout;
import com.netflix.mediaclient.Log;
import android.view.View;
import java.util.ArrayList;
import com.netflix.mediaclient.android.widget.ObjectRecycler$ViewRecycler;
import com.netflix.mediaclient.servicemgr.ServiceManager;
import com.netflix.mediaclient.android.app.LoadingStatus$LoadingStatusCallback;
import java.util.List;
import com.netflix.mediaclient.android.activity.NetflixActivity;
import android.widget.BaseAdapter;
import com.netflix.mediaclient.servicemgr.model.BasicLoMo;

public abstract class BaseLoLoMoAdapter<T extends BasicLoMo> extends BaseAdapter implements LoLoMoFrag$ILoLoMoAdapter
{
    private static final String TAG = "BaseLoLoMoAdapter";
    protected final NetflixActivity activity;
    protected final LoLoMoFrag frag;
    private boolean hasMoreData;
    private boolean isLoading;
    private int loMoStartIndex;
    private final String lolomoId;
    private long lomoRequestId;
    private boolean lomoRequestPending;
    private final List<T> lomos;
    protected LoadingStatus$LoadingStatusCallback mLoadingStatusCallback;
    protected ServiceManager manager;
    protected final ObjectRecycler$ViewRecycler viewRecycler;
    
    public BaseLoLoMoAdapter(final LoLoMoFrag frag, final String lolomoId) {
        this.isLoading = true;
        this.lomos = new ArrayList<T>(40);
        this.lomoRequestPending = true;
        this.frag = frag;
        this.activity = (NetflixActivity)frag.getActivity();
        this.viewRecycler = frag.getViewRecycler();
        this.lolomoId = lolomoId;
    }
    
    private BaseLoLoMoAdapter$RowHolder createViewsAndHolder(final View view) {
        Log.v("BaseLoLoMoAdapter", "creating views and holder");
        final LinearLayout linearLayout = (LinearLayout)view.findViewById(2131165443);
        linearLayout.setFocusable(false);
        final TextView textView = (TextView)view.findViewById(2131165441);
        final Resources resources = this.activity.getResources();
        int n;
        if (this.activity.isForKids()) {
            n = 2131296358;
        }
        else {
            n = 2131296369;
        }
        textView.setTextColor(resources.getColor(n));
        final BaseLoLoMoAdapter$LoMoRowContent rowContent = this.createRowContent(linearLayout, (View)textView);
        final TextView initTitleView = this.initTitleView(view);
        ((RelativeLayout$LayoutParams)initTitleView.getLayoutParams()).leftMargin = LoMoUtils.getLomoFragImageOffsetLeftPx(this.activity);
        return this.createHolder(view, linearLayout, initTitleView, rowContent, view.findViewById(2131165491));
    }
    
    private void fetchMoreData() {
        this.isLoading = true;
        this.lomoRequestId = System.nanoTime();
        final int n = this.loMoStartIndex + 20 - 1;
        if (Log.isLoggable("BaseLoLoMoAdapter", 2)) {
            Log.v("BaseLoLoMoAdapter", "fetching more data, starting at index: " + this.loMoStartIndex);
            Log.v("BaseLoLoMoAdapter", "fetching from: " + this.loMoStartIndex + " to: " + n + ", id: " + this.lolomoId);
        }
        if (this.manager == null) {
            Log.w("BaseLoLoMoAdapter", "Manager is null - can't refresh data");
            return;
        }
        this.makeFetchRequest(this.lolomoId, this.loMoStartIndex, n, new BaseLoLoMoAdapter$LoMoCallbacks(this, this.lomoRequestId, n - this.loMoStartIndex));
    }
    
    private void hideLoadingAndErrorViews() {
        this.frag.hideLoadingAndErrorViews();
    }
    
    private boolean isRowAfterBillboardOrCwRow(final int n, LoMoType type) {
        if (n == 1) {
            type = this.getItem(0).getType();
            if (type == LoMoType.BILLBOARD || type == LoMoType.CONTINUE_WATCHING) {
                return true;
            }
        }
        return false;
    }
    
    private void sendHomeUpdatedBroadcast() {
        this.activity.sendBroadcast(new Intent("com.netflix.mediaclient.intent.action.HOME_LOLOMO_UPDATED"));
        Log.v("BaseLoLoMoAdapter", "Intent HOME_LOLOMO_UPDATED sent");
    }
    
    private void showErrorView() {
        this.frag.showErrorView();
    }
    
    public boolean areAllItemsEnabled() {
        return false;
    }
    
    protected boolean areRequestsPending() {
        return this.lomoRequestPending;
    }
    
    protected View createDummyView() {
        final View view = new View((Context)this.activity);
        view.setVisibility(8);
        return view;
    }
    
    protected BaseLoLoMoAdapter$RowHolder createHolder(final View view, final LinearLayout linearLayout, final TextView textView, final BaseLoLoMoAdapter$LoMoRowContent baseLoLoMoAdapter$LoMoRowContent, final View view2) {
        return new BaseLoLoMoAdapter$RowHolder((View)linearLayout, textView, baseLoLoMoAdapter$LoMoRowContent, view2);
    }
    
    protected abstract BaseLoLoMoAdapter$LoMoRowContent createRowContent(final LinearLayout p0, final View p1);
    
    public void destroy() {
    }
    
    protected Activity getActivity() {
        return this.activity;
    }
    
    public int getCount() {
        return this.lomos.size();
    }
    
    protected String getGenreId() {
        return this.lolomoId;
    }
    
    public T getItem(final int n) {
        return this.lomos.get(n);
    }
    
    public long getItemId(final int n) {
        return n;
    }
    
    protected ServiceManager getServiceManager() {
        return this.manager;
    }
    
    protected int getShelfVisibility(final T t, final int n) {
        if (this.isRowAfterBillboardOrCwRow(n, t.getType()) && !this.activity.isForKids()) {
            return 0;
        }
        return 8;
    }
    
    public View getView(final int n, View dummyView, final ViewGroup viewGroup) {
        if (this.activity.destroyed()) {
            Log.d("BaseLoLoMoAdapter", "activity is destroyed - can't getView");
            dummyView = this.createDummyView();
        }
        else {
            View inflate;
            if ((inflate = dummyView) == null) {
                inflate = this.activity.getLayoutInflater().inflate(this.getViewLayoutId(), viewGroup, false);
                inflate.setTag((Object)this.createViewsAndHolder(inflate));
            }
            final BasicLoMo item = this.getItem(n);
            if (item == null) {
                Log.w("BaseLoLoMoAdapter", "Trying to show data for null lomo! Position: " + n);
            }
            else {
                this.updateRowViews((BaseLoLoMoAdapter$RowHolder)inflate.getTag(), (T)item, n);
            }
            if (!this.activity.isForKids() || this.activity.isKubrick()) {
                final ListView listView = this.frag.getListView();
                int headerViewsCount;
                if (listView == null) {
                    headerViewsCount = 0;
                }
                else {
                    headerViewsCount = listView.getHeaderViewsCount();
                }
                if (headerViewsCount == 0) {
                    int actionBarHeight;
                    if (n == 0) {
                        actionBarHeight = this.activity.getActionBarHeight();
                    }
                    else {
                        actionBarHeight = 0;
                    }
                    ViewUtils.setPaddingTop(inflate, actionBarHeight);
                }
            }
            dummyView = inflate;
            if (this.hasMoreData) {
                dummyView = inflate;
                if (n == this.getCount() - 1) {
                    this.fetchMoreData();
                    return inflate;
                }
            }
        }
        return dummyView;
    }
    
    protected int getViewLayoutId() {
        return 2130903134;
    }
    
    protected void initLoadingState() {
        ThreadUtils.assertOnMain();
        this.lomos.clear();
        this.lomoRequestId = -2147483648L;
        this.lomoRequestPending = true;
        this.hasMoreData = false;
        this.loMoStartIndex = 0;
        this.notifyDataSetChanged();
    }
    
    protected TextView initTitleView(final View view) {
        if (this.activity.isForKids()) {
            view.findViewById(2131165442).setVisibility(8);
            final TextView textView = (TextView)view.findViewById(2131165492);
            if (textView != null) {
                textView.setVisibility(0);
            }
            return textView;
        }
        return (TextView)view.findViewById(2131165442);
    }
    
    public boolean isEnabled(final int n) {
        return false;
    }
    
    protected abstract boolean isGenreList();
    
    public boolean isLoadingData() {
        return this.isLoading;
    }
    
    protected abstract void makeFetchRequest(final String p0, final int p1, final int p2, final ManagerCallback p3);
    
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        ThreadUtils.assertOnMain();
        if (this.getCount() > 0) {
            this.hideLoadingAndErrorViews();
        }
        else if (!this.areRequestsPending()) {
            this.showErrorView();
        }
    }
    
    protected void onDataLoaded(final Status status) {
        if (this.mLoadingStatusCallback != null) {
            this.mLoadingStatusCallback.onDataLoaded(status);
        }
    }
    
    public void onManagerReady(final ServiceManager manager, final Status status) {
        this.manager = manager;
        this.refreshData();
    }
    
    public void onManagerUnavailable(final ServiceManager serviceManager, final Status status) {
        this.manager = null;
    }
    
    public void onPause() {
    }
    
    public void onResume() {
    }
    
    public void onScroll(final AbsListView absListView, final int n, final int n2, final int n3) {
    }
    
    public void onScrollStateChanged(final AbsListView absListView, final int n) {
    }
    
    public void refreshData() {
        Log.v("BaseLoLoMoAdapter", "Refreshing data");
        this.isLoading = true;
        this.initLoadingState();
        this.fetchMoreData();
    }
    
    public void setLoadingStatusCallback(final LoadingStatus$LoadingStatusCallback mLoadingStatusCallback) {
        if (!this.isLoadingData() && mLoadingStatusCallback != null) {
            mLoadingStatusCallback.onDataLoaded(CommonStatus.OK);
            return;
        }
        this.mLoadingStatusCallback = mLoadingStatusCallback;
    }
    
    protected boolean shouldShowRowTitle(final T t) {
        return t.getType() != LoMoType.BILLBOARD || BillboardView.shouldShowArtworkOnly(this.activity);
    }
    
    protected void updateLoMoData(final List<T> list) {
        this.lomos.addAll((Collection<? extends T>)list);
        this.loMoStartIndex += list.size();
        this.notifyDataSetChanged();
        this.sendHomeUpdatedBroadcast();
    }
    
    protected void updateRowViews(final BaseLoLoMoAdapter$RowHolder baseLoLoMoAdapter$RowHolder, final T t, int dipToPixels) {
        if (Log.isLoggable("BaseLoLoMoAdapter", 2)) {
            Log.v("BaseLoLoMoAdapter", "Updating LoMo row content: " + t.getTitle() + ", type: " + t.getType() + ", pos: " + dipToPixels);
        }
        final TextView title = baseLoLoMoAdapter$RowHolder.title;
        String text;
        if (t.getType() == LoMoType.BILLBOARD) {
            text = this.activity.getString(2131493273);
        }
        else {
            text = t.getTitle();
        }
        title.setText((CharSequence)text);
        final TextView title2 = baseLoLoMoAdapter$RowHolder.title;
        int visibility;
        if (this.shouldShowRowTitle(t)) {
            visibility = 0;
        }
        else {
            visibility = 8;
        }
        title2.setVisibility(visibility);
        if (baseLoLoMoAdapter$RowHolder.shelf != null) {
            baseLoLoMoAdapter$RowHolder.shelf.setVisibility(this.getShelfVisibility(t, dipToPixels));
        }
        baseLoLoMoAdapter$RowHolder.rowContent.refresh(t, dipToPixels);
        if (this.activity.isForKids()) {
            if (t.getType() != LoMoType.CONTINUE_WATCHING) {
                Api16Util.setBackgroundDrawableCompat(baseLoLoMoAdapter$RowHolder.contentGroup, null);
                if (dipToPixels == this.getCount() - 1) {
                    dipToPixels = 1;
                }
                else {
                    dipToPixels = 0;
                }
                final View contentGroup = baseLoLoMoAdapter$RowHolder.contentGroup;
                if (dipToPixels != 0) {
                    dipToPixels = AndroidUtils.dipToPixels((Context)this.activity, 24);
                }
                else {
                    dipToPixels = 0;
                }
                contentGroup.setPadding(0, 0, 0, dipToPixels);
                baseLoLoMoAdapter$RowHolder.title.setTextColor(baseLoLoMoAdapter$RowHolder.defaultTitleColors);
                return;
            }
            baseLoLoMoAdapter$RowHolder.contentGroup.setBackgroundResource(2130837724);
            baseLoLoMoAdapter$RowHolder.contentGroup.setPadding(0, 0, 0, AndroidUtils.dipToPixels((Context)this.activity, 22));
            baseLoLoMoAdapter$RowHolder.title.setTextColor(this.activity.getResources().getColor(2131296360));
        }
    }
}
