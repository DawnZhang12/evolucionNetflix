// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v7.widget;

import android.view.View$MeasureSpec;
import android.widget.ListAdapter;
import android.content.ActivityNotFoundException;
import android.support.v4.view.KeyEventCompat;
import android.text.Editable;
import android.graphics.drawable.Drawable;
import android.text.style.ImageSpan;
import android.text.SpannableStringBuilder;
import android.content.ComponentName;
import android.os.Parcelable;
import android.app.PendingIntent;
import android.util.Log;
import android.database.Cursor;
import android.net.Uri;
import android.content.res.Resources;
import android.support.v7.appcompat.R$dimen;
import android.support.v7.internal.widget.ViewUtils;
import android.annotation.TargetApi;
import android.view.View$OnLayoutChangeListener;
import android.view.ViewTreeObserver$OnGlobalLayoutListener;
import android.support.v7.appcompat.R$id;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.support.v7.appcompat.R$styleable;
import android.support.v7.appcompat.R$attr;
import android.os.Build$VERSION;
import android.content.Intent;
import android.text.TextWatcher;
import android.view.View$OnKeyListener;
import android.support.v4.widget.CursorAdapter;
import android.app.SearchableInfo;
import android.graphics.drawable.Drawable$ConstantState;
import java.util.WeakHashMap;
import android.view.View$OnFocusChangeListener;
import android.widget.AdapterView$OnItemSelectedListener;
import android.widget.AdapterView$OnItemClickListener;
import android.widget.TextView$OnEditorActionListener;
import android.view.View$OnClickListener;
import android.widget.ImageView;
import android.os.Bundle;
import android.support.v7.view.CollapsibleActionView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.view.KeyEvent$DispatcherState;
import android.view.KeyEvent;
import android.graphics.Rect;
import android.text.TextUtils;
import android.support.v7.internal.widget.TintTypedArray;
import android.util.AttributeSet;
import android.content.Context;
import android.support.v7.internal.widget.TintManager;
import android.widget.AutoCompleteTextView;

public class SearchView$SearchAutoComplete extends AutoCompleteTextView
{
    private final int[] POPUP_WINDOW_ATTRS;
    private SearchView mSearchView;
    private int mThreshold;
    private final TintManager mTintManager;
    
    public SearchView$SearchAutoComplete(final Context context) {
        this(context, null);
    }
    
    public SearchView$SearchAutoComplete(final Context context, final AttributeSet set) {
        this(context, set, 16842859);
    }
    
    public SearchView$SearchAutoComplete(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.POPUP_WINDOW_ATTRS = new int[] { 16843126 };
        this.mThreshold = this.getThreshold();
        final TintTypedArray obtainStyledAttributes = TintTypedArray.obtainStyledAttributes(context, set, this.POPUP_WINDOW_ATTRS, n, 0);
        if (obtainStyledAttributes.hasValue(0)) {
            this.setDropDownBackgroundDrawable(obtainStyledAttributes.getDrawable(0));
        }
        obtainStyledAttributes.recycle();
        this.mTintManager = obtainStyledAttributes.getTintManager();
    }
    
    private boolean isEmpty() {
        return TextUtils.getTrimmedLength((CharSequence)this.getText()) == 0;
    }
    
    public boolean enoughToFilter() {
        return this.mThreshold <= 0 || super.enoughToFilter();
    }
    
    protected void onFocusChanged(final boolean b, final int n, final Rect rect) {
        super.onFocusChanged(b, n, rect);
        this.mSearchView.onTextFocusChanged();
    }
    
    public boolean onKeyPreIme(final int n, final KeyEvent keyEvent) {
        if (n == 4) {
            if (keyEvent.getAction() == 0 && keyEvent.getRepeatCount() == 0) {
                final KeyEvent$DispatcherState keyDispatcherState = this.getKeyDispatcherState();
                if (keyDispatcherState != null) {
                    keyDispatcherState.startTracking(keyEvent, (Object)this);
                }
                return true;
            }
            if (keyEvent.getAction() == 1) {
                final KeyEvent$DispatcherState keyDispatcherState2 = this.getKeyDispatcherState();
                if (keyDispatcherState2 != null) {
                    keyDispatcherState2.handleUpEvent(keyEvent);
                }
                if (keyEvent.isTracking() && !keyEvent.isCanceled()) {
                    this.mSearchView.clearFocus();
                    this.mSearchView.setImeVisibility(false);
                    return true;
                }
            }
        }
        return super.onKeyPreIme(n, keyEvent);
    }
    
    public void onWindowFocusChanged(final boolean b) {
        super.onWindowFocusChanged(b);
        if (b && this.mSearchView.hasFocus() && this.getVisibility() == 0) {
            ((InputMethodManager)this.getContext().getSystemService("input_method")).showSoftInput((View)this, 0);
            if (SearchView.isLandscapeMode(this.getContext())) {
                SearchView.HIDDEN_METHOD_INVOKER.ensureImeVisible(this, true);
            }
        }
    }
    
    public void performCompletion() {
    }
    
    protected void replaceText(final CharSequence charSequence) {
    }
    
    public void setDropDownBackgroundResource(final int n) {
        this.setDropDownBackgroundDrawable(this.mTintManager.getDrawable(n));
    }
    
    void setSearchView(final SearchView mSearchView) {
        this.mSearchView = mSearchView;
    }
    
    public void setThreshold(final int n) {
        super.setThreshold(n);
        this.mThreshold = n;
    }
}