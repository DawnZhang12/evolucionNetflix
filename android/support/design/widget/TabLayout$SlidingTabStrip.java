// 
// Decompiled by Procyon v0.5.30
// 

package android.support.design.widget;

import android.support.v4.view.ViewPager$OnPageChangeListener;
import android.support.v4.view.ViewPager;
import android.support.v4.view.PagerAdapter;
import java.util.Iterator;
import android.view.ViewGroup$LayoutParams;
import android.content.res.TypedArray;
import android.support.design.R$style;
import android.support.design.R$styleable;
import android.util.AttributeSet;
import java.util.ArrayList;
import android.content.res.ColorStateList;
import android.view.View$OnClickListener;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout$LayoutParams;
import android.view.View$MeasureSpec;
import android.graphics.Canvas;
import android.view.View;
import android.support.v4.view.ViewCompat;
import android.content.Context;
import android.graphics.Paint;
import android.widget.LinearLayout;

class TabLayout$SlidingTabStrip extends LinearLayout
{
    private int mIndicatorLeft;
    private int mIndicatorRight;
    private int mSelectedIndicatorHeight;
    private final Paint mSelectedIndicatorPaint;
    private int mSelectedPosition;
    private float mSelectionOffset;
    final /* synthetic */ TabLayout this$0;
    
    TabLayout$SlidingTabStrip(final TabLayout this$0, final Context context) {
        this.this$0 = this$0;
        super(context);
        this.mSelectedPosition = -1;
        this.mIndicatorLeft = -1;
        this.mIndicatorRight = -1;
        this.setWillNotDraw(false);
        this.mSelectedIndicatorPaint = new Paint();
    }
    
    private void setIndicatorPosition(final int mIndicatorLeft, final int mIndicatorRight) {
        if (mIndicatorLeft != this.mIndicatorLeft || mIndicatorRight != this.mIndicatorRight) {
            this.mIndicatorLeft = mIndicatorLeft;
            this.mIndicatorRight = mIndicatorRight;
            ViewCompat.postInvalidateOnAnimation((View)this);
        }
    }
    
    private void updateIndicatorPosition() {
        final View child = this.getChildAt(this.mSelectedPosition);
        int right;
        int n2;
        if (child != null && child.getWidth() > 0) {
            final int left = child.getLeft();
            final int n = right = child.getRight();
            n2 = left;
            if (this.mSelectionOffset > 0.0f) {
                right = n;
                n2 = left;
                if (this.mSelectedPosition < this.getChildCount() - 1) {
                    final View child2 = this.getChildAt(this.mSelectedPosition + 1);
                    n2 = (int)(left * (1.0f - this.mSelectionOffset) + this.mSelectionOffset * child2.getLeft());
                    right = (int)(n * (1.0f - this.mSelectionOffset) + child2.getRight() * this.mSelectionOffset);
                }
            }
        }
        else {
            right = -1;
            n2 = -1;
        }
        this.setIndicatorPosition(n2, right);
    }
    
    void animateIndicatorToPosition(final int n, final int duration) {
        final boolean b = ViewCompat.getLayoutDirection((View)this) == 1;
        final View child = this.getChildAt(n);
        final int left = child.getLeft();
        final int right = child.getRight();
        int mIndicatorLeft;
        int mIndicatorRight;
        if (Math.abs(n - this.mSelectedPosition) <= 1) {
            mIndicatorLeft = this.mIndicatorLeft;
            mIndicatorRight = this.mIndicatorRight;
        }
        else {
            final int access$1300 = this.this$0.dpToPx(24);
            if (n < this.mSelectedPosition) {
                if (b) {
                    mIndicatorRight = (mIndicatorLeft = left - access$1300);
                }
                else {
                    mIndicatorRight = (mIndicatorLeft = right + access$1300);
                }
            }
            else if (b) {
                mIndicatorRight = (mIndicatorLeft = right + access$1300);
            }
            else {
                mIndicatorRight = (mIndicatorLeft = left - access$1300);
            }
        }
        if (mIndicatorLeft != left || mIndicatorRight != right) {
            final ValueAnimatorCompat access$1301 = this.this$0.mIndicatorAnimator = ViewUtils.createAnimator();
            access$1301.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
            access$1301.setDuration(duration);
            access$1301.setFloatValues(0.0f, 1.0f);
            access$1301.setUpdateListener(new TabLayout$SlidingTabStrip$1(this, mIndicatorLeft, left, mIndicatorRight, right));
            access$1301.setListener(new TabLayout$SlidingTabStrip$2(this, n));
            access$1301.start();
        }
    }
    
    boolean childrenNeedLayout() {
        final boolean b = false;
        final int childCount = this.getChildCount();
        int n = 0;
        boolean b2;
        while (true) {
            b2 = b;
            if (n >= childCount) {
                break;
            }
            if (this.getChildAt(n).getWidth() <= 0) {
                b2 = true;
                break;
            }
            ++n;
        }
        return b2;
    }
    
    public void draw(final Canvas canvas) {
        super.draw(canvas);
        if (this.mIndicatorLeft >= 0 && this.mIndicatorRight > this.mIndicatorLeft) {
            canvas.drawRect((float)this.mIndicatorLeft, (float)(this.getHeight() - this.mSelectedIndicatorHeight), (float)this.mIndicatorRight, (float)this.getHeight(), this.mSelectedIndicatorPaint);
        }
    }
    
    float getIndicatorPosition() {
        return this.mSelectedPosition + this.mSelectionOffset;
    }
    
    protected void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
        super.onLayout(b, n, n2, n3, n4);
        this.updateIndicatorPosition();
    }
    
    protected void onMeasure(final int n, final int n2) {
        super.onMeasure(n, n2);
        if (View$MeasureSpec.getMode(n) == 1073741824 && this.this$0.mMode == 1 && this.this$0.mTabGravity == 1) {
            final int childCount = this.getChildCount();
            final int measureSpec = View$MeasureSpec.makeMeasureSpec(0, 0);
            int i = 0;
            int max = 0;
            while (i < childCount) {
                final View child = this.getChildAt(i);
                child.measure(measureSpec, n2);
                max = Math.max(max, child.getMeasuredWidth());
                ++i;
            }
            if (max > 0) {
                if (max * childCount <= this.getMeasuredWidth() - this.this$0.dpToPx(16) * 2) {
                    for (int j = 0; j < childCount; ++j) {
                        final LinearLayout$LayoutParams linearLayout$LayoutParams = (LinearLayout$LayoutParams)this.getChildAt(j).getLayoutParams();
                        linearLayout$LayoutParams.width = max;
                        linearLayout$LayoutParams.weight = 0.0f;
                    }
                }
                else {
                    this.this$0.mTabGravity = 0;
                    this.this$0.updateTabViewsLayoutParams();
                }
                super.onMeasure(n, n2);
            }
        }
    }
    
    void setIndicatorPositionFromTabPosition(final int mSelectedPosition, final float mSelectionOffset) {
        this.mSelectedPosition = mSelectedPosition;
        this.mSelectionOffset = mSelectionOffset;
        this.updateIndicatorPosition();
    }
    
    void setSelectedIndicatorColor(final int color) {
        if (this.mSelectedIndicatorPaint.getColor() != color) {
            this.mSelectedIndicatorPaint.setColor(color);
            ViewCompat.postInvalidateOnAnimation((View)this);
        }
    }
    
    void setSelectedIndicatorHeight(final int mSelectedIndicatorHeight) {
        if (this.mSelectedIndicatorHeight != mSelectedIndicatorHeight) {
            this.mSelectedIndicatorHeight = mSelectedIndicatorHeight;
            ViewCompat.postInvalidateOnAnimation((View)this);
        }
    }
}
