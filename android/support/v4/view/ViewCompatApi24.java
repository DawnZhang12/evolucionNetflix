// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.view;

import android.view.PointerIcon;
import android.view.View;
import android.annotation.TargetApi;

@TargetApi(24)
class ViewCompatApi24
{
    public static void setPointerIcon(final View view, final Object o) {
        view.setPointerIcon((PointerIcon)o);
    }
}
