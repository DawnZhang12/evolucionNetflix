// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.widget;

import android.view.animation.Interpolator;
import android.content.Context;
import android.widget.OverScroller;

class ScrollerCompatGingerbread
{
    public static void abortAnimation(final Object o) {
        ((OverScroller)o).abortAnimation();
    }
    
    public static boolean computeScrollOffset(final Object o) {
        return ((OverScroller)o).computeScrollOffset();
    }
    
    public static Object createScroller(final Context context, final Interpolator interpolator) {
        if (interpolator != null) {
            return new OverScroller(context, interpolator);
        }
        return new OverScroller(context);
    }
    
    public static void fling(final Object o, final int n, final int n2, final int n3, final int n4, final int n5, final int n6, final int n7, final int n8) {
        ((OverScroller)o).fling(n, n2, n3, n4, n5, n6, n7, n8);
    }
    
    public static int getCurrX(final Object o) {
        return ((OverScroller)o).getCurrX();
    }
    
    public static int getCurrY(final Object o) {
        return ((OverScroller)o).getCurrY();
    }
    
    public static int getFinalX(final Object o) {
        return ((OverScroller)o).getFinalX();
    }
    
    public static int getFinalY(final Object o) {
        return ((OverScroller)o).getFinalY();
    }
    
    public static boolean isFinished(final Object o) {
        return ((OverScroller)o).isFinished();
    }
    
    public static void startScroll(final Object o, final int n, final int n2, final int n3, final int n4, final int n5) {
        ((OverScroller)o).startScroll(n, n2, n3, n4, n5);
    }
}
