// 
// Decompiled by Procyon v0.5.30
// 

package android.support.design.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;

class FloatingActionButtonHoneycombMr1$2 extends AnimatorListenerAdapter
{
    final /* synthetic */ FloatingActionButtonHoneycombMr1 this$0;
    
    FloatingActionButtonHoneycombMr1$2(final FloatingActionButtonHoneycombMr1 this$0) {
        this.this$0 = this$0;
    }
    
    public void onAnimationStart(final Animator animator) {
        this.this$0.mView.setVisibility(0);
    }
}