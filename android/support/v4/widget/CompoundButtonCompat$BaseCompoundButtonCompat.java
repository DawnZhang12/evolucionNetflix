// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.widget;

import android.graphics.PorterDuff$Mode;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.widget.CompoundButton;

class CompoundButtonCompat$BaseCompoundButtonCompat implements CompoundButtonCompat$CompoundButtonCompatImpl
{
    @Override
    public Drawable getButtonDrawable(final CompoundButton compoundButton) {
        return CompoundButtonCompatGingerbread.getButtonDrawable(compoundButton);
    }
    
    @Override
    public ColorStateList getButtonTintList(final CompoundButton compoundButton) {
        return CompoundButtonCompatGingerbread.getButtonTintList(compoundButton);
    }
    
    @Override
    public PorterDuff$Mode getButtonTintMode(final CompoundButton compoundButton) {
        return CompoundButtonCompatGingerbread.getButtonTintMode(compoundButton);
    }
    
    @Override
    public void setButtonTintList(final CompoundButton compoundButton, final ColorStateList list) {
        CompoundButtonCompatGingerbread.setButtonTintList(compoundButton, list);
    }
    
    @Override
    public void setButtonTintMode(final CompoundButton compoundButton, final PorterDuff$Mode porterDuff$Mode) {
        CompoundButtonCompatGingerbread.setButtonTintMode(compoundButton, porterDuff$Mode);
    }
}
