// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.mediaclient.servicemgr;

import android.content.Context;

public interface SearchResults extends Trackable
{
    int getNumResults();
    
    int getNumResultsForSection(final int p0);
    
    int getNumSections();
    
    Object getResult(final int p0);
    
    CharSequence getSectionTitle(final Context p0, final int p1);
    
    boolean hasResults();
}