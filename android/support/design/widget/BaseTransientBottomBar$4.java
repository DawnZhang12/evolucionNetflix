// 
// Decompiled by Procyon v0.5.30
// 

package android.support.design.widget;

import android.view.View;

class BaseTransientBottomBar$4 implements SwipeDismissBehavior$OnDismissListener
{
    final /* synthetic */ BaseTransientBottomBar this$0;
    
    BaseTransientBottomBar$4(final BaseTransientBottomBar this$0) {
        this.this$0 = this$0;
    }
    
    @Override
    public void onDismiss(final View view) {
        view.setVisibility(8);
        this.this$0.dispatchDismiss(0);
    }
    
    @Override
    public void onDragStateChanged(final int n) {
        switch (n) {
            default: {}
            case 1:
            case 2: {
                SnackbarManager.getInstance().cancelTimeout(this.this$0.mManagerCallback);
            }
            case 0: {
                SnackbarManager.getInstance().restoreTimeout(this.this$0.mManagerCallback);
            }
        }
    }
}
