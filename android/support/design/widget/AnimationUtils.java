// 
// Decompiled by Procyon v0.5.30
// 

package android.support.design.widget;

import android.view.animation.DecelerateInterpolator;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.Interpolator;

class AnimationUtils
{
    static final Interpolator DECELERATE_INTERPOLATOR;
    static final Interpolator FAST_OUT_SLOW_IN_INTERPOLATOR;
    static final Interpolator LINEAR_INTERPOLATOR;
    
    static {
        LINEAR_INTERPOLATOR = (Interpolator)new LinearInterpolator();
        FAST_OUT_SLOW_IN_INTERPOLATOR = (Interpolator)new FastOutSlowInInterpolator();
        DECELERATE_INTERPOLATOR = (Interpolator)new DecelerateInterpolator();
    }
    
    static int lerp(final int n, final int n2, final float n3) {
        return Math.round((n2 - n) * n3) + n;
    }
}
