// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.mediaclient.ui.kubrick.lolomo;

import com.netflix.mediaclient.servicemgr.model.LoMoType;
import com.netflix.mediaclient.servicemgr.model.LoMoUtils;
import android.widget.FrameLayout$LayoutParams;
import com.netflix.mediaclient.ui.lolomo.BaseLoLoMoAdapter$RowHolder;
import com.netflix.mediaclient.ui.lolomo.BaseLoLoMoAdapter$LoMoRowContent;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.view.View;
import com.netflix.mediaclient.android.activity.NetflixActivity;
import java.util.Iterator;
import com.netflix.mediaclient.service.webclient.model.leafs.KubrickLoMoDuplicate;
import com.netflix.mediaclient.service.webclient.model.leafs.KubrickLoMoHeroDuplicate;
import java.util.ArrayList;
import android.util.Log;
import com.netflix.mediaclient.util.ThreadUtils;
import com.netflix.mediaclient.servicemgr.model.LoMo;
import java.util.List;
import com.netflix.mediaclient.ui.lolomo.BasePaginatedLoLoMoAdapter;

public class KubrickLolomoUtils
{
    private static final String TAG = "KubrickLolomoUtils";
    
    public static <T extends LoMo> List<T> createDuplicateRows(final BasePaginatedLoLoMoAdapter<?> basePaginatedLoLoMoAdapter, final List<? extends LoMo> list, int n) {
        ThreadUtils.assertOnMain();
        List<T> list2 = (List<T>)list;
        if (basePaginatedLoLoMoAdapter.getCount() < n) {
            Log.v("KubrickLolomoUtils", "Manipulating lomo data to inject Kubrick duplicate rows");
            list2 = new ArrayList<T>(list.size() * 2);
            for (final LoMo loMo : list) {
                if (n > 0 && shouldDuplicateLomo(loMo)) {
                    Log.v("KubrickLolomoUtils", "Adding duplicate lomo for row: " + loMo.getTitle());
                    list2.add((T)new KubrickLoMoHeroDuplicate(loMo));
                    list2.add((T)new KubrickLoMoDuplicate(loMo));
                    --n;
                }
                else {
                    list2.add((T)loMo);
                }
            }
        }
        return list2;
    }
    
    public static BaseLoLoMoAdapter$RowHolder createHolder(final NetflixActivity netflixActivity, final View view, final LinearLayout linearLayout, final TextView textView, final BaseLoLoMoAdapter$LoMoRowContent baseLoLoMoAdapter$LoMoRowContent, final View view2) {
        final TextView textView2 = (TextView)view.findViewById(2131165444);
        ((FrameLayout$LayoutParams)textView2.getLayoutParams()).leftMargin = LoMoUtils.getLomoFragImageOffsetLeftPx(netflixActivity);
        return new KubrickLolomoUtils$KubrickRowHolder((View)linearLayout, textView, baseLoLoMoAdapter$LoMoRowContent, view2, textView2, view.findViewById(2131165439));
    }
    
    public static boolean isDuplicateRow(final LoMo loMo) {
        return loMo instanceof KubrickLoMoDuplicate || loMo instanceof KubrickLoMoHeroDuplicate;
    }
    
    public static boolean shouldDuplicateLomo(final LoMo loMo) {
        final LoMoType type = loMo.getType();
        return type == LoMoType.STANDARD || type == LoMoType.SOCIAL_FRIEND || type == LoMoType.SOCIAL_GROUP || type == LoMoType.SOCIAL_POPULAR;
    }
    
    public static void updateRowViews(final BaseLoLoMoAdapter$RowHolder baseLoLoMoAdapter$RowHolder, final LoMo loMo, int visibility) {
        final int n = 0;
        final KubrickLolomoUtils$KubrickRowHolder kubrickLolomoUtils$KubrickRowHolder = (KubrickLolomoUtils$KubrickRowHolder)baseLoLoMoAdapter$RowHolder;
        if (loMo instanceof KubrickLoMoHeroDuplicate) {
            kubrickLolomoUtils$KubrickRowHolder.kubrickHeroTitle.setText((CharSequence)loMo.getTitle());
            kubrickLolomoUtils$KubrickRowHolder.kubrickHeroTitle.setVisibility(0);
            final View topSpacer = kubrickLolomoUtils$KubrickRowHolder.topSpacer;
            if (visibility > 0) {
                visibility = n;
            }
            else {
                visibility = 8;
            }
            topSpacer.setVisibility(visibility);
            return;
        }
        kubrickLolomoUtils$KubrickRowHolder.kubrickHeroTitle.setVisibility(8);
        kubrickLolomoUtils$KubrickRowHolder.topSpacer.setVisibility(8);
    }
}
