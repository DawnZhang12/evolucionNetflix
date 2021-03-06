// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.mediaclient.ui.barker_kids.details;

import android.support.v7.widget.RecyclerView$ItemDecoration;
import com.netflix.mediaclient.util.ItemDecorationUniformPadding;
import com.netflix.mediaclient.android.widget.RecyclerViewHeaderAdapter$IViewCreator;
import android.support.v7.widget.RecyclerView$OnScrollListener;
import com.netflix.mediaclient.ui.details.SeasonsSpinner;
import com.netflix.mediaclient.ui.barker.details.BarkerVideoDetailsViewGroup;
import android.view.View;
import android.content.Context;
import com.netflix.mediaclient.ui.kids.KidsUtils;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import com.netflix.mediaclient.ui.barker.details.BarkerMovieDetailsFrag;

public class BarkerKidsMovieDetailsFrag extends BarkerMovieDetailsFrag
{
    public static BarkerKidsMovieDetailsFrag create(final String s) {
        final BarkerKidsMovieDetailsFrag barkerKidsMovieDetailsFrag = new BarkerKidsMovieDetailsFrag();
        final Bundle arguments = new Bundle();
        arguments.putString("video_id", s);
        barkerKidsMovieDetailsFrag.setArguments(arguments);
        return barkerKidsMovieDetailsFrag;
    }
    
    @Override
    protected int getBackgroundResource() {
        return 2131755278;
    }
    
    @Override
    protected int getLayoutId() {
        return 2130903177;
    }
    
    @Override
    protected int getNumColumns() {
        return this.getActivity().getResources().getInteger(2131558421);
    }
    
    @Override
    protected int getRecyclerViewShadowWidth() {
        return KidsUtils.getDetailsPageContentWidth((Context)this.getActivity()) + (int)this.getResources().getDimension(2131427781) * 2;
    }
    
    @Override
    protected void initDetailsViewGroup(final View view) {
        (this.detailsViewGroup = new BarkerKidsMovieDetailsFrag$BarkerKidsMovieDetailsViewGroup(this, (Context)this.getActivity())).removeActionBarDummyView();
        this.detailsViewGroup.showRelatedTitle();
        ((BarkerVideoDetailsViewGroup)this.detailsViewGroup).hideDataSelector();
    }
    
    @Override
    protected void setupDetailsPageParallaxScrollListener() {
        this.parallaxScroller = new KidsParallax(null, this.recyclerView, new View[] { this.detailsViewGroup.getHeroImage(), ((BarkerVideoDetailsViewGroup)this.detailsViewGroup).getHeroImage2() }, null, null);
        this.recyclerView.setOnScrollListener(this.parallaxScroller);
    }
    
    @Override
    protected void setupRecyclerView() {
        super.setupRecyclerView();
        if (this.recyclerView != null) {
            this.recyclerView.setBackgroundColor(-1);
        }
    }
    
    @Override
    protected void setupRecyclerViewAdapter() {
        super.setupRecyclerViewAdapter();
        this.adapter.setViewCreator(new BarkerKidsMovieDetailsFrag$1(this));
    }
    
    @Override
    protected void setupRecyclerViewItemDecoration() {
        this.recyclerView.addItemDecoration((RecyclerView$ItemDecoration)new ItemDecorationUniformPadding(this.getActivity().getResources().getDimensionPixelOffset(2131427791), this.numColumns));
    }
    
    @Override
    protected void setupRecyclerViewLayoutManager() {
        super.setupRecyclerViewLayoutManager();
        this.recyclerView.getLayoutParams().width = KidsUtils.getDetailsPageContentWidth((Context)this.getActivity());
    }
}
