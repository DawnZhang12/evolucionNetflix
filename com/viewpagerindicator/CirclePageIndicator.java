// 
// Decompiled by Procyon v0.5.30
// 

package com.viewpagerindicator;

import android.os.Parcel;
import android.os.Parcelable$Creator;
import android.view.View$BaseSavedState;
import android.support.v4.view.MotionEventCompat;
import android.view.MotionEvent;
import android.os.Parcelable;
import android.graphics.Canvas;
import android.view.View$MeasureSpec;
import android.graphics.drawable.Drawable;
import android.content.res.TypedArray;
import android.content.res.Resources;
import android.support.v4.view.ViewConfigurationCompat;
import android.view.ViewConfiguration;
import android.graphics.Paint$Style;
import android.util.AttributeSet;
import android.content.Context;
import android.graphics.Paint;
import com.viewpagerindicator.android.osp.ViewPager;
import android.view.View;

public class CirclePageIndicator extends View implements PageIndicator
{
    private static final int INVALID_POINTER = -1;
    private int mActivePointerId;
    private boolean mCentered;
    private int mCurrentPage;
    private boolean mIsDragging;
    private float mLastMotionX;
    private OnPageChangeListener mListener;
    private int mOrientation;
    private float mPageOffset;
    private final Paint mPaintFill;
    private final Paint mPaintPageFill;
    private final Paint mPaintStroke;
    private float mRadius;
    private int mScrollState;
    private boolean mSnap;
    private int mSnapPage;
    private int mTouchSlop;
    private ViewPager mViewPager;
    
    public CirclePageIndicator(final Context context) {
        this(context, null);
    }
    
    public CirclePageIndicator(final Context context, final AttributeSet set) {
        this(context, set, R.attr.vpiCirclePageIndicatorStyle);
    }
    
    public CirclePageIndicator(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.mPaintPageFill = new Paint(1);
        this.mPaintStroke = new Paint(1);
        this.mPaintFill = new Paint(1);
        this.mLastMotionX = -1.0f;
        this.mActivePointerId = -1;
        if (this.isInEditMode()) {
            return;
        }
        final Resources resources = this.getResources();
        final int color = resources.getColor(R.color.default_circle_indicator_page_color);
        final int color2 = resources.getColor(R.color.default_circle_indicator_fill_color);
        final int integer = resources.getInteger(R.integer.default_circle_indicator_orientation);
        final int color3 = resources.getColor(R.color.default_circle_indicator_stroke_color);
        final float dimension = resources.getDimension(R.dimen.default_circle_indicator_stroke_width);
        final float dimension2 = resources.getDimension(R.dimen.default_circle_indicator_radius);
        final boolean boolean1 = resources.getBoolean(R.bool.default_circle_indicator_centered);
        final boolean boolean2 = resources.getBoolean(R.bool.default_circle_indicator_snap);
        final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, R.styleable.CirclePageIndicator, n, 0);
        this.mCentered = obtainStyledAttributes.getBoolean(2, boolean1);
        this.mOrientation = obtainStyledAttributes.getInt(0, integer);
        this.mPaintPageFill.setStyle(Paint$Style.FILL);
        this.mPaintPageFill.setColor(obtainStyledAttributes.getColor(5, color));
        this.mPaintStroke.setStyle(Paint$Style.STROKE);
        this.mPaintStroke.setColor(obtainStyledAttributes.getColor(8, color3));
        this.mPaintStroke.setStrokeWidth(obtainStyledAttributes.getDimension(3, dimension));
        this.mPaintFill.setStyle(Paint$Style.FILL);
        this.mPaintFill.setColor(obtainStyledAttributes.getColor(4, color2));
        this.mRadius = obtainStyledAttributes.getDimension(6, dimension2);
        this.mSnap = obtainStyledAttributes.getBoolean(7, boolean2);
        final Drawable drawable = obtainStyledAttributes.getDrawable(1);
        if (drawable != null) {
            this.setBackgroundDrawable(drawable);
        }
        obtainStyledAttributes.recycle();
        this.mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(ViewConfiguration.get(context));
    }
    
    private int measureLong(int count) {
        final int mode = View$MeasureSpec.getMode(count);
        final int size = View$MeasureSpec.getSize(count);
        if (mode == 1073741824 || this.mViewPager == null) {
            count = size;
        }
        else {
            count = this.mViewPager.getAdapter().getCount();
            final int n = count = (int)(this.getPaddingLeft() + this.getPaddingRight() + count * 2 * this.mRadius + (count - 1) * this.mRadius + 1.0f);
            if (mode == Integer.MIN_VALUE) {
                return Math.min(n, size);
            }
        }
        return count;
    }
    
    private int measureShort(int n) {
        final int mode = View$MeasureSpec.getMode(n);
        final int size = View$MeasureSpec.getSize(n);
        if (mode == 1073741824) {
            n = size;
        }
        else {
            final int n2 = n = (int)(2.0f * this.mRadius + this.getPaddingTop() + this.getPaddingBottom() + 1.0f);
            if (mode == Integer.MIN_VALUE) {
                return Math.min(n2, size);
            }
        }
        return n;
    }
    
    public int getFillColor() {
        return this.mPaintFill.getColor();
    }
    
    public int getOrientation() {
        return this.mOrientation;
    }
    
    public int getPageColor() {
        return this.mPaintPageFill.getColor();
    }
    
    public float getRadius() {
        return this.mRadius;
    }
    
    public int getStrokeColor() {
        return this.mPaintStroke.getColor();
    }
    
    public float getStrokeWidth() {
        return this.mPaintStroke.getStrokeWidth();
    }
    
    public boolean isCentered() {
        return this.mCentered;
    }
    
    public boolean isSnap() {
        return this.mSnap;
    }
    
    public void notifyDataSetChanged() {
        this.invalidate();
    }
    
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        if (this.mViewPager != null) {
            final int count = this.mViewPager.getAdapter().getCount();
            if (count != 0) {
                if (this.mCurrentPage >= count) {
                    this.setCurrentItem(count - 1);
                    return;
                }
                int n;
                int n2;
                int n3;
                int n4;
                if (this.mOrientation == 0) {
                    n = this.getWidth();
                    n2 = this.getPaddingLeft();
                    n3 = this.getPaddingRight();
                    n4 = this.getPaddingTop();
                }
                else {
                    n = this.getHeight();
                    n2 = this.getPaddingTop();
                    n3 = this.getPaddingBottom();
                    n4 = this.getPaddingLeft();
                }
                final float n5 = this.mRadius * 3.0f;
                float n6 = n4 + this.mRadius;
                float n8;
                final float n7 = n8 = n2 + this.mRadius;
                if (this.mCentered) {
                    n8 = n7 + ((n - n2 - n3) / 2.0f - count * n5 / 2.0f);
                }
                float mRadius;
                final float n9 = mRadius = this.mRadius;
                if (this.mPaintStroke.getStrokeWidth() > 0.0f) {
                    mRadius = n9 - this.mPaintStroke.getStrokeWidth() / 2.0f;
                }
                for (int i = 0; i < count; ++i) {
                    float n10 = n8 + i * n5;
                    float n11;
                    if (this.mOrientation == 0) {
                        n11 = n10;
                        n10 = n6;
                    }
                    else {
                        n11 = n6;
                    }
                    if (this.mPaintPageFill.getAlpha() > 0) {
                        canvas.drawCircle(n11, n10, mRadius, this.mPaintPageFill);
                    }
                    if (mRadius != this.mRadius) {
                        canvas.drawCircle(n11, n10, this.mRadius, this.mPaintStroke);
                    }
                }
                int n12;
                if (this.mSnap) {
                    n12 = this.mSnapPage;
                }
                else {
                    n12 = this.mCurrentPage;
                }
                float n14;
                final float n13 = n14 = n12 * n5;
                if (!this.mSnap) {
                    n14 = n13 + this.mPageOffset * n5;
                }
                float n16;
                if (this.mOrientation == 0) {
                    final float n15 = n8 + n14;
                    n16 = n6;
                    n6 = n15;
                }
                else {
                    n16 = n8 + n14;
                }
                canvas.drawCircle(n6, n16, this.mRadius, this.mPaintFill);
            }
        }
    }
    
    protected void onMeasure(final int n, final int n2) {
        if (this.mOrientation == 0) {
            this.setMeasuredDimension(this.measureLong(n), this.measureShort(n2));
            return;
        }
        this.setMeasuredDimension(this.measureShort(n), this.measureLong(n2));
    }
    
    public void onPageScrollStateChanged(final int mScrollState) {
        this.mScrollState = mScrollState;
        if (this.mListener != null) {
            this.mListener.onPageScrollStateChanged(mScrollState);
        }
    }
    
    public void onPageScrolled(final int mCurrentPage, final float mPageOffset, final int n) {
        this.mCurrentPage = mCurrentPage;
        this.mPageOffset = mPageOffset;
        this.invalidate();
        if (this.mListener != null) {
            this.mListener.onPageScrolled(mCurrentPage, mPageOffset, n);
        }
    }
    
    public void onPageSelected(final int n) {
        if (this.mSnap || this.mScrollState == 0) {
            this.mCurrentPage = n;
            this.mSnapPage = n;
            this.invalidate();
        }
        if (this.mListener != null) {
            this.mListener.onPageSelected(n);
        }
    }
    
    public void onRestoreInstanceState(final Parcelable parcelable) {
        final SavedState savedState = (SavedState)parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        this.mCurrentPage = savedState.currentPage;
        this.mSnapPage = savedState.currentPage;
        this.requestLayout();
    }
    
    public Parcelable onSaveInstanceState() {
        final SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.currentPage = this.mCurrentPage;
        return (Parcelable)savedState;
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        if (super.onTouchEvent(motionEvent)) {
            return true;
        }
        if (this.mViewPager == null || this.mViewPager.getAdapter().getCount() == 0) {
            return false;
        }
        final int n = motionEvent.getAction() & 0xFF;
        switch (n) {
            case 0: {
                this.mActivePointerId = MotionEventCompat.getPointerId(motionEvent, 0);
                this.mLastMotionX = motionEvent.getX();
                break;
            }
            case 2: {
                final float x = MotionEventCompat.getX(motionEvent, MotionEventCompat.findPointerIndex(motionEvent, this.mActivePointerId));
                final float n2 = x - this.mLastMotionX;
                if (!this.mIsDragging && Math.abs(n2) > this.mTouchSlop) {
                    this.mIsDragging = true;
                }
                if (!this.mIsDragging) {
                    break;
                }
                this.mLastMotionX = x;
                if (this.mViewPager.isFakeDragging() || this.mViewPager.beginFakeDrag()) {
                    this.mViewPager.fakeDragBy(n2);
                    break;
                }
                break;
            }
            case 1:
            case 3: {
                if (!this.mIsDragging) {
                    final int count = this.mViewPager.getAdapter().getCount();
                    final int width = this.getWidth();
                    final float n3 = width / 2.0f;
                    final float n4 = width / 6.0f;
                    if (this.mCurrentPage > 0 && motionEvent.getX() < n3 - n4) {
                        if (n != 3) {
                            this.mViewPager.setCurrentItem(this.mCurrentPage - 1);
                        }
                        return true;
                    }
                    if (this.mCurrentPage < count - 1 && motionEvent.getX() > n3 + n4) {
                        if (n != 3) {
                            this.mViewPager.setCurrentItem(this.mCurrentPage + 1);
                        }
                        return true;
                    }
                }
                this.mIsDragging = false;
                this.mActivePointerId = -1;
                if (this.mViewPager.isFakeDragging()) {
                    this.mViewPager.endFakeDrag();
                    break;
                }
                break;
            }
            case 5: {
                final int actionIndex = MotionEventCompat.getActionIndex(motionEvent);
                this.mLastMotionX = MotionEventCompat.getX(motionEvent, actionIndex);
                this.mActivePointerId = MotionEventCompat.getPointerId(motionEvent, actionIndex);
                break;
            }
            case 6: {
                final int actionIndex2 = MotionEventCompat.getActionIndex(motionEvent);
                if (MotionEventCompat.getPointerId(motionEvent, actionIndex2) == this.mActivePointerId) {
                    int n5;
                    if (actionIndex2 == 0) {
                        n5 = 1;
                    }
                    else {
                        n5 = 0;
                    }
                    this.mActivePointerId = MotionEventCompat.getPointerId(motionEvent, n5);
                }
                this.mLastMotionX = MotionEventCompat.getX(motionEvent, MotionEventCompat.findPointerIndex(motionEvent, this.mActivePointerId));
                break;
            }
        }
        return true;
    }
    
    public void setCentered(final boolean mCentered) {
        this.mCentered = mCentered;
        this.invalidate();
    }
    
    public void setCurrentItem(final int mCurrentPage) {
        if (this.mViewPager == null) {
            throw new IllegalStateException("ViewPager has not been bound.");
        }
        this.mCurrentPage = mCurrentPage;
        this.invalidate();
    }
    
    public void setFillColor(final int color) {
        this.mPaintFill.setColor(color);
        this.invalidate();
    }
    
    public void setOnPageChangeListener(final OnPageChangeListener mListener) {
        this.mListener = mListener;
    }
    
    public void setOrientation(final int mOrientation) {
        switch (mOrientation) {
            default: {
                throw new IllegalArgumentException("Orientation must be either HORIZONTAL or VERTICAL.");
            }
            case 0:
            case 1: {
                this.mOrientation = mOrientation;
                this.requestLayout();
            }
        }
    }
    
    public void setPageColor(final int color) {
        this.mPaintPageFill.setColor(color);
        this.invalidate();
    }
    
    public void setRadius(final float mRadius) {
        this.mRadius = mRadius;
        this.invalidate();
    }
    
    public void setSnap(final boolean mSnap) {
        this.mSnap = mSnap;
        this.invalidate();
    }
    
    public void setStrokeColor(final int color) {
        this.mPaintStroke.setColor(color);
        this.invalidate();
    }
    
    public void setStrokeWidth(final float strokeWidth) {
        this.mPaintStroke.setStrokeWidth(strokeWidth);
        this.invalidate();
    }
    
    public void setViewPager(final ViewPager mViewPager) {
        if (this.mViewPager == mViewPager) {
            return;
        }
        if (this.mViewPager != null) {
            this.mViewPager.setOnPageChangeListener(null);
        }
        if (mViewPager.getAdapter() == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }
        (this.mViewPager = mViewPager).setOnPageChangeListener((ViewPager.OnPageChangeListener)this);
        this.invalidate();
    }
    
    public void setViewPager(final ViewPager viewPager, final int currentItem) {
        this.setViewPager(viewPager);
        this.setCurrentItem(currentItem);
    }
    
    static class SavedState extends View$BaseSavedState
    {
        public static final Parcelable$Creator<SavedState> CREATOR;
        int currentPage;
        
        static {
            CREATOR = (Parcelable$Creator)new Parcelable$Creator<SavedState>() {
                public SavedState createFromParcel(final Parcel parcel) {
                    return new SavedState(parcel);
                }
                
                public SavedState[] newArray(final int n) {
                    return new SavedState[n];
                }
            };
        }
        
        private SavedState(final Parcel parcel) {
            super(parcel);
            this.currentPage = parcel.readInt();
        }
        
        public SavedState(final Parcelable parcelable) {
            super(parcelable);
        }
        
        public void writeToParcel(final Parcel parcel, final int n) {
            super.writeToParcel(parcel, n);
            parcel.writeInt(this.currentPage);
        }
    }
}