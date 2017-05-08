// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.view;

import android.database.DataSetObserver;
import android.content.res.Resources$NotFoundException;
import android.view.View$MeasureSpec;
import android.util.AttributeSet;
import android.os.Build$VERSION;
import android.graphics.Canvas;
import android.view.accessibility.AccessibilityEvent;
import android.view.KeyEvent;
import android.view.SoundEffectConstants;
import android.view.FocusFinder;
import android.util.Log;
import android.view.ViewGroup$LayoutParams;
import java.util.Collections;
import android.view.MotionEvent;
import android.view.ViewParent;
import android.graphics.Paint;
import android.view.VelocityTracker;
import android.graphics.Rect;
import java.lang.reflect.Method;
import android.widget.Scroller;
import android.os.Parcelable;
import java.util.List;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.EdgeEffectCompat;
import android.view.View;
import java.util.ArrayList;
import android.view.animation.Interpolator;
import java.util.Comparator;
import android.view.ViewGroup;

public class ViewPager extends ViewGroup
{
    private static final Comparator<ViewPager$ItemInfo> COMPARATOR;
    private static final int[] LAYOUT_ATTRS;
    private static final Interpolator sInterpolator;
    private static final ViewPager$ViewPositionComparator sPositionComparator;
    private int mActivePointerId;
    private PagerAdapter mAdapter;
    private ViewPager$OnAdapterChangeListener mAdapterChangeListener;
    private int mBottomPageBounds;
    private boolean mCalledSuper;
    private int mChildHeightMeasureSpec;
    private int mChildWidthMeasureSpec;
    private int mCloseEnough;
    private int mCurItem;
    private int mDecorChildCount;
    private int mDefaultGutterSize;
    private int mDrawingOrder;
    private ArrayList<View> mDrawingOrderedChildren;
    private final Runnable mEndScrollRunnable;
    private int mExpectedAdapterCount;
    private boolean mFakeDragging;
    private boolean mFirstLayout;
    private float mFirstOffset;
    private int mFlingDistance;
    private int mGutterSize;
    private boolean mInLayout;
    private float mInitialMotionX;
    private float mInitialMotionY;
    private ViewPager$OnPageChangeListener mInternalPageChangeListener;
    private boolean mIsBeingDragged;
    private boolean mIsUnableToDrag;
    private final ArrayList<ViewPager$ItemInfo> mItems;
    private float mLastMotionX;
    private float mLastMotionY;
    private float mLastOffset;
    private EdgeEffectCompat mLeftEdge;
    private Drawable mMarginDrawable;
    private int mMaximumVelocity;
    private int mMinimumVelocity;
    private boolean mNeedCalculatePageOffsets;
    private ViewPager$PagerObserver mObserver;
    private int mOffscreenPageLimit;
    private ViewPager$OnPageChangeListener mOnPageChangeListener;
    private List<ViewPager$OnPageChangeListener> mOnPageChangeListeners;
    private int mPageMargin;
    private ViewPager$PageTransformer mPageTransformer;
    private boolean mPopulatePending;
    private Parcelable mRestoredAdapterState;
    private ClassLoader mRestoredClassLoader;
    private int mRestoredCurItem;
    private EdgeEffectCompat mRightEdge;
    private int mScrollState;
    private Scroller mScroller;
    private boolean mScrollingCacheEnabled;
    private Method mSetChildrenDrawingOrderEnabled;
    private final ViewPager$ItemInfo mTempItem;
    private final Rect mTempRect;
    private int mTopPageBounds;
    private int mTouchSlop;
    private VelocityTracker mVelocityTracker;
    
    static {
        LAYOUT_ATTRS = new int[] { 16842931 };
        COMPARATOR = new ViewPager$1();
        sInterpolator = (Interpolator)new ViewPager$2();
        sPositionComparator = new ViewPager$ViewPositionComparator();
    }
    
    private void calculatePageOffsets(ViewPager$ItemInfo viewPager$ItemInfo, int i, ViewPager$ItemInfo viewPager$ItemInfo2) {
        final int count = this.mAdapter.getCount();
        final int clientWidth = this.getClientWidth();
        float n;
        if (clientWidth > 0) {
            n = this.mPageMargin / clientWidth;
        }
        else {
            n = 0.0f;
        }
        if (viewPager$ItemInfo2 != null) {
            final int position = viewPager$ItemInfo2.position;
            if (position < viewPager$ItemInfo.position) {
                float n2 = viewPager$ItemInfo2.offset + viewPager$ItemInfo2.widthFactor + n;
                int j;
                for (int n3 = position + 1, n4 = 0; n3 <= viewPager$ItemInfo.position && n4 < this.mItems.size(); n3 = j + 1) {
                    viewPager$ItemInfo2 = this.mItems.get(n4);
                    float offset;
                    while (true) {
                        j = n3;
                        offset = n2;
                        if (n3 <= viewPager$ItemInfo2.position) {
                            break;
                        }
                        j = n3;
                        offset = n2;
                        if (n4 >= this.mItems.size() - 1) {
                            break;
                        }
                        ++n4;
                        viewPager$ItemInfo2 = this.mItems.get(n4);
                    }
                    while (j < viewPager$ItemInfo2.position) {
                        offset += this.mAdapter.getPageWidth(j) + n;
                        ++j;
                    }
                    viewPager$ItemInfo2.offset = offset;
                    n2 = offset + (viewPager$ItemInfo2.widthFactor + n);
                }
            }
            else if (position > viewPager$ItemInfo.position) {
                int n5 = this.mItems.size() - 1;
                float offset2 = viewPager$ItemInfo2.offset;
                int k;
                for (int n6 = position - 1; n6 >= viewPager$ItemInfo.position && n5 >= 0; n6 = k - 1) {
                    viewPager$ItemInfo2 = this.mItems.get(n5);
                    float n7;
                    while (true) {
                        k = n6;
                        n7 = offset2;
                        if (n6 >= viewPager$ItemInfo2.position) {
                            break;
                        }
                        k = n6;
                        n7 = offset2;
                        if (n5 <= 0) {
                            break;
                        }
                        --n5;
                        viewPager$ItemInfo2 = this.mItems.get(n5);
                    }
                    while (k > viewPager$ItemInfo2.position) {
                        n7 -= this.mAdapter.getPageWidth(k) + n;
                        --k;
                    }
                    offset2 = n7 - (viewPager$ItemInfo2.widthFactor + n);
                    viewPager$ItemInfo2.offset = offset2;
                }
            }
        }
        final int size = this.mItems.size();
        final float offset3 = viewPager$ItemInfo.offset;
        int l = viewPager$ItemInfo.position - 1;
        float offset4;
        if (viewPager$ItemInfo.position == 0) {
            offset4 = viewPager$ItemInfo.offset;
        }
        else {
            offset4 = -3.4028235E38f;
        }
        this.mFirstOffset = offset4;
        float mLastOffset;
        if (viewPager$ItemInfo.position == count - 1) {
            mLastOffset = viewPager$ItemInfo.offset + viewPager$ItemInfo.widthFactor - 1.0f;
        }
        else {
            mLastOffset = Float.MAX_VALUE;
        }
        this.mLastOffset = mLastOffset;
        int n8 = i - 1;
        float n9 = offset3;
        while (n8 >= 0) {
            for (viewPager$ItemInfo2 = this.mItems.get(n8); l > viewPager$ItemInfo2.position; --l) {
                n9 -= this.mAdapter.getPageWidth(l) + n;
            }
            n9 -= viewPager$ItemInfo2.widthFactor + n;
            viewPager$ItemInfo2.offset = n9;
            if (viewPager$ItemInfo2.position == 0) {
                this.mFirstOffset = n9;
            }
            --l;
            --n8;
        }
        float offset5 = viewPager$ItemInfo.offset + viewPager$ItemInfo.widthFactor + n;
        final int n10 = viewPager$ItemInfo.position + 1;
        int n11 = i + 1;
        i = n10;
        while (n11 < size) {
            for (viewPager$ItemInfo = this.mItems.get(n11); i < viewPager$ItemInfo.position; ++i) {
                offset5 += this.mAdapter.getPageWidth(i) + n;
            }
            if (viewPager$ItemInfo.position == count - 1) {
                this.mLastOffset = viewPager$ItemInfo.widthFactor + offset5 - 1.0f;
            }
            viewPager$ItemInfo.offset = offset5;
            offset5 += viewPager$ItemInfo.widthFactor + n;
            ++i;
            ++n11;
        }
        this.mNeedCalculatePageOffsets = false;
    }
    
    private void completeScroll(final boolean b) {
        boolean b2;
        if (this.mScrollState == 2) {
            b2 = true;
        }
        else {
            b2 = false;
        }
        if (b2) {
            this.setScrollingCacheEnabled(false);
            this.mScroller.abortAnimation();
            final int scrollX = this.getScrollX();
            final int scrollY = this.getScrollY();
            final int currX = this.mScroller.getCurrX();
            final int currY = this.mScroller.getCurrY();
            if (scrollX != currX || scrollY != currY) {
                this.scrollTo(currX, currY);
                if (currX != scrollX) {
                    this.pageScrolled(currX);
                }
            }
        }
        this.mPopulatePending = false;
        final int n = 0;
        int n2 = b2 ? 1 : 0;
        for (int i = n; i < this.mItems.size(); ++i) {
            final ViewPager$ItemInfo viewPager$ItemInfo = this.mItems.get(i);
            if (viewPager$ItemInfo.scrolling) {
                viewPager$ItemInfo.scrolling = false;
                n2 = 1;
            }
        }
        if (n2 != 0) {
            if (!b) {
                this.mEndScrollRunnable.run();
                return;
            }
            ViewCompat.postOnAnimation((View)this, this.mEndScrollRunnable);
        }
    }
    
    private int determineTargetPage(int n, final float n2, int max, final int n3) {
        if (Math.abs(n3) > this.mFlingDistance && Math.abs(max) > this.mMinimumVelocity) {
            if (max <= 0) {
                ++n;
            }
        }
        else {
            float n4;
            if (n >= this.mCurItem) {
                n4 = 0.4f;
            }
            else {
                n4 = 0.6f;
            }
            n = (int)(n4 + (n + n2));
        }
        max = n;
        if (this.mItems.size() > 0) {
            max = Math.max(this.mItems.get(0).position, Math.min(n, this.mItems.get(this.mItems.size() - 1).position));
        }
        return max;
    }
    
    private void dispatchOnPageScrolled(final int n, final float n2, final int n3) {
        if (this.mOnPageChangeListener != null) {
            this.mOnPageChangeListener.onPageScrolled(n, n2, n3);
        }
        if (this.mOnPageChangeListeners != null) {
            for (int size = this.mOnPageChangeListeners.size(), i = 0; i < size; ++i) {
                final ViewPager$OnPageChangeListener viewPager$OnPageChangeListener = this.mOnPageChangeListeners.get(i);
                if (viewPager$OnPageChangeListener != null) {
                    viewPager$OnPageChangeListener.onPageScrolled(n, n2, n3);
                }
            }
        }
        if (this.mInternalPageChangeListener != null) {
            this.mInternalPageChangeListener.onPageScrolled(n, n2, n3);
        }
    }
    
    private void dispatchOnPageSelected(final int n) {
        if (this.mOnPageChangeListener != null) {
            this.mOnPageChangeListener.onPageSelected(n);
        }
        if (this.mOnPageChangeListeners != null) {
            for (int size = this.mOnPageChangeListeners.size(), i = 0; i < size; ++i) {
                final ViewPager$OnPageChangeListener viewPager$OnPageChangeListener = this.mOnPageChangeListeners.get(i);
                if (viewPager$OnPageChangeListener != null) {
                    viewPager$OnPageChangeListener.onPageSelected(n);
                }
            }
        }
        if (this.mInternalPageChangeListener != null) {
            this.mInternalPageChangeListener.onPageSelected(n);
        }
    }
    
    private void dispatchOnScrollStateChanged(final int n) {
        if (this.mOnPageChangeListener != null) {
            this.mOnPageChangeListener.onPageScrollStateChanged(n);
        }
        if (this.mOnPageChangeListeners != null) {
            for (int size = this.mOnPageChangeListeners.size(), i = 0; i < size; ++i) {
                final ViewPager$OnPageChangeListener viewPager$OnPageChangeListener = this.mOnPageChangeListeners.get(i);
                if (viewPager$OnPageChangeListener != null) {
                    viewPager$OnPageChangeListener.onPageScrollStateChanged(n);
                }
            }
        }
        if (this.mInternalPageChangeListener != null) {
            this.mInternalPageChangeListener.onPageScrollStateChanged(n);
        }
    }
    
    private void enableLayers(final boolean b) {
        for (int childCount = this.getChildCount(), i = 0; i < childCount; ++i) {
            int n;
            if (b) {
                n = 2;
            }
            else {
                n = 0;
            }
            ViewCompat.setLayerType(this.getChildAt(i), n, null);
        }
    }
    
    private void endDrag() {
        this.mIsBeingDragged = false;
        this.mIsUnableToDrag = false;
        if (this.mVelocityTracker != null) {
            this.mVelocityTracker.recycle();
            this.mVelocityTracker = null;
        }
    }
    
    private Rect getChildRectInPagerCoordinates(Rect rect, final View view) {
        if (rect == null) {
            rect = new Rect();
        }
        if (view == null) {
            rect.set(0, 0, 0, 0);
            return rect;
        }
        rect.left = view.getLeft();
        rect.right = view.getRight();
        rect.top = view.getTop();
        rect.bottom = view.getBottom();
        ViewPager viewPager;
        for (ViewParent viewParent = view.getParent(); viewParent instanceof ViewGroup && viewParent != this; viewParent = viewPager.getParent()) {
            viewPager = (ViewPager)viewParent;
            rect.left += viewPager.getLeft();
            rect.right += viewPager.getRight();
            rect.top += viewPager.getTop();
            rect.bottom += viewPager.getBottom();
        }
        return rect;
    }
    
    private int getClientWidth() {
        return this.getMeasuredWidth() - this.getPaddingLeft() - this.getPaddingRight();
    }
    
    private ViewPager$ItemInfo infoForCurrentScrollPosition() {
        final int clientWidth = this.getClientWidth();
        float n;
        if (clientWidth > 0) {
            n = this.getScrollX() / clientWidth;
        }
        else {
            n = 0.0f;
        }
        float n2;
        if (clientWidth > 0) {
            n2 = this.mPageMargin / clientWidth;
        }
        else {
            n2 = 0.0f;
        }
        float widthFactor = 0.0f;
        float offset = 0.0f;
        int position = -1;
        int n3 = 0;
        int n4 = 1;
        ViewPager$ItemInfo viewPager$ItemInfo = null;
        ViewPager$ItemInfo viewPager$ItemInfo2;
        while (true) {
            viewPager$ItemInfo2 = viewPager$ItemInfo;
            if (n3 >= this.mItems.size()) {
                break;
            }
            ViewPager$ItemInfo mTempItem = this.mItems.get(n3);
            if (n4 == 0 && mTempItem.position != position + 1) {
                mTempItem = this.mTempItem;
                mTempItem.offset = widthFactor + offset + n2;
                mTempItem.position = position + 1;
                mTempItem.widthFactor = this.mAdapter.getPageWidth(mTempItem.position);
                --n3;
            }
            offset = mTempItem.offset;
            final float widthFactor2 = mTempItem.widthFactor;
            if (n4 == 0) {
                viewPager$ItemInfo2 = viewPager$ItemInfo;
                if (n < offset) {
                    break;
                }
            }
            if (n < widthFactor2 + offset + n2 || n3 == this.mItems.size() - 1) {
                viewPager$ItemInfo2 = mTempItem;
                break;
            }
            position = mTempItem.position;
            widthFactor = mTempItem.widthFactor;
            n4 = 0;
            ++n3;
            viewPager$ItemInfo = mTempItem;
        }
        return viewPager$ItemInfo2;
    }
    
    private boolean isGutterDrag(final float n, final float n2) {
        return (n < this.mGutterSize && n2 > 0.0f) || (n > this.getWidth() - this.mGutterSize && n2 < 0.0f);
    }
    
    private void onSecondaryPointerUp(final MotionEvent motionEvent) {
        final int actionIndex = MotionEventCompat.getActionIndex(motionEvent);
        if (MotionEventCompat.getPointerId(motionEvent, actionIndex) == this.mActivePointerId) {
            int n;
            if (actionIndex == 0) {
                n = 1;
            }
            else {
                n = 0;
            }
            this.mLastMotionX = MotionEventCompat.getX(motionEvent, n);
            this.mActivePointerId = MotionEventCompat.getPointerId(motionEvent, n);
            if (this.mVelocityTracker != null) {
                this.mVelocityTracker.clear();
            }
        }
    }
    
    private boolean pageScrolled(int n) {
        boolean b = false;
        if (this.mItems.size() == 0) {
            this.mCalledSuper = false;
            this.onPageScrolled(0, 0.0f, 0);
            if (!this.mCalledSuper) {
                throw new IllegalStateException("onPageScrolled did not call superclass implementation");
            }
        }
        else {
            final ViewPager$ItemInfo infoForCurrentScrollPosition = this.infoForCurrentScrollPosition();
            final int clientWidth = this.getClientWidth();
            final int mPageMargin = this.mPageMargin;
            final float n2 = this.mPageMargin / clientWidth;
            final int position = infoForCurrentScrollPosition.position;
            final float n3 = (n / clientWidth - infoForCurrentScrollPosition.offset) / (infoForCurrentScrollPosition.widthFactor + n2);
            n = (int)((mPageMargin + clientWidth) * n3);
            this.mCalledSuper = false;
            this.onPageScrolled(position, n3, n);
            if (!this.mCalledSuper) {
                throw new IllegalStateException("onPageScrolled did not call superclass implementation");
            }
            b = true;
        }
        return b;
    }
    
    private boolean performDrag(float mLastMotionX) {
        boolean b = true;
        final boolean b2 = false;
        boolean b3 = false;
        final float mLastMotionX2 = this.mLastMotionX;
        this.mLastMotionX = mLastMotionX;
        final float n = this.getScrollX() + (mLastMotionX2 - mLastMotionX);
        final int clientWidth = this.getClientWidth();
        mLastMotionX = clientWidth * this.mFirstOffset;
        final float n2 = clientWidth;
        final float mLastOffset = this.mLastOffset;
        final ViewPager$ItemInfo viewPager$ItemInfo = this.mItems.get(0);
        final ViewPager$ItemInfo viewPager$ItemInfo2 = this.mItems.get(this.mItems.size() - 1);
        int n3;
        if (viewPager$ItemInfo.position != 0) {
            mLastMotionX = viewPager$ItemInfo.offset * clientWidth;
            n3 = 0;
        }
        else {
            n3 = 1;
        }
        float n4;
        if (viewPager$ItemInfo2.position != this.mAdapter.getCount() - 1) {
            n4 = viewPager$ItemInfo2.offset * clientWidth;
            b = false;
        }
        else {
            n4 = n2 * mLastOffset;
        }
        if (n < mLastMotionX) {
            n4 = mLastMotionX;
            if (n3 != 0) {
                b3 = this.mLeftEdge.onPull(Math.abs(mLastMotionX - n) / clientWidth);
                n4 = mLastMotionX;
            }
        }
        else if (n > n4) {
            b3 = b2;
            if (b) {
                b3 = this.mRightEdge.onPull(Math.abs(n - n4) / clientWidth);
            }
        }
        else {
            n4 = n;
        }
        this.mLastMotionX += n4 - (int)n4;
        this.scrollTo((int)n4, this.getScrollY());
        this.pageScrolled((int)n4);
        return b3;
    }
    
    private void recomputeScrollPosition(int n, int n2, int duration, int timePassed) {
        if (n2 > 0 && !this.mItems.isEmpty()) {
            n2 = (n - this.getPaddingLeft() - this.getPaddingRight() + duration) * (this.getScrollX() / (n2 - this.getPaddingLeft() - this.getPaddingRight() + timePassed));
            this.scrollTo(n2, this.getScrollY());
            if (!this.mScroller.isFinished()) {
                duration = this.mScroller.getDuration();
                timePassed = this.mScroller.timePassed();
                this.mScroller.startScroll(n2, 0, (int)(this.infoForPosition(this.mCurItem).offset * n), 0, duration - timePassed);
            }
        }
        else {
            final ViewPager$ItemInfo infoForPosition = this.infoForPosition(this.mCurItem);
            float min;
            if (infoForPosition != null) {
                min = Math.min(infoForPosition.offset, this.mLastOffset);
            }
            else {
                min = 0.0f;
            }
            n = (int)(min * (n - this.getPaddingLeft() - this.getPaddingRight()));
            if (n != this.getScrollX()) {
                this.completeScroll(false);
                this.scrollTo(n, this.getScrollY());
            }
        }
    }
    
    private void removeNonDecorViews() {
        int n;
        for (int i = 0; i < this.getChildCount(); i = n + 1) {
            n = i;
            if (!((ViewPager$LayoutParams)this.getChildAt(i).getLayoutParams()).isDecor) {
                this.removeViewAt(i);
                n = i - 1;
            }
        }
    }
    
    private void requestParentDisallowInterceptTouchEvent(final boolean b) {
        final ViewParent parent = this.getParent();
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(b);
        }
    }
    
    private void scrollToItem(final int n, final boolean b, final int n2, final boolean b2) {
        final ViewPager$ItemInfo infoForPosition = this.infoForPosition(n);
        int n3;
        if (infoForPosition != null) {
            n3 = (int)(Math.max(this.mFirstOffset, Math.min(infoForPosition.offset, this.mLastOffset)) * this.getClientWidth());
        }
        else {
            n3 = 0;
        }
        if (b) {
            this.smoothScrollTo(n3, 0, n2);
            if (b2) {
                this.dispatchOnPageSelected(n);
            }
            return;
        }
        if (b2) {
            this.dispatchOnPageSelected(n);
        }
        this.completeScroll(false);
        this.scrollTo(n3, 0);
        this.pageScrolled(n3);
    }
    
    private void setScrollState(final int mScrollState) {
        if (this.mScrollState == mScrollState) {
            return;
        }
        this.mScrollState = mScrollState;
        if (this.mPageTransformer != null) {
            this.enableLayers(mScrollState != 0);
        }
        this.dispatchOnScrollStateChanged(mScrollState);
    }
    
    private void setScrollingCacheEnabled(final boolean mScrollingCacheEnabled) {
        if (this.mScrollingCacheEnabled != mScrollingCacheEnabled) {
            this.mScrollingCacheEnabled = mScrollingCacheEnabled;
        }
    }
    
    private void sortChildDrawingOrder() {
        if (this.mDrawingOrder != 0) {
            if (this.mDrawingOrderedChildren == null) {
                this.mDrawingOrderedChildren = new ArrayList<View>();
            }
            else {
                this.mDrawingOrderedChildren.clear();
            }
            for (int childCount = this.getChildCount(), i = 0; i < childCount; ++i) {
                this.mDrawingOrderedChildren.add(this.getChildAt(i));
            }
            Collections.sort(this.mDrawingOrderedChildren, ViewPager.sPositionComparator);
        }
    }
    
    public void addFocusables(final ArrayList<View> list, final int n, final int n2) {
        final int size = list.size();
        final int descendantFocusability = this.getDescendantFocusability();
        if (descendantFocusability != 393216) {
            for (int i = 0; i < this.getChildCount(); ++i) {
                final View child = this.getChildAt(i);
                if (child.getVisibility() == 0) {
                    final ViewPager$ItemInfo infoForChild = this.infoForChild(child);
                    if (infoForChild != null && infoForChild.position == this.mCurItem) {
                        child.addFocusables((ArrayList)list, n, n2);
                    }
                }
            }
        }
        if ((descendantFocusability != 262144 || size == list.size()) && this.isFocusable() && ((n2 & 0x1) != 0x1 || !this.isInTouchMode() || this.isFocusableInTouchMode()) && list != null) {
            list.add((View)this);
        }
    }
    
    ViewPager$ItemInfo addNewItem(final int position, final int n) {
        final ViewPager$ItemInfo viewPager$ItemInfo = new ViewPager$ItemInfo();
        viewPager$ItemInfo.position = position;
        viewPager$ItemInfo.object = this.mAdapter.instantiateItem(this, position);
        viewPager$ItemInfo.widthFactor = this.mAdapter.getPageWidth(position);
        if (n < 0 || n >= this.mItems.size()) {
            this.mItems.add(viewPager$ItemInfo);
            return viewPager$ItemInfo;
        }
        this.mItems.add(n, viewPager$ItemInfo);
        return viewPager$ItemInfo;
    }
    
    public void addOnPageChangeListener(final ViewPager$OnPageChangeListener viewPager$OnPageChangeListener) {
        if (this.mOnPageChangeListeners == null) {
            this.mOnPageChangeListeners = new ArrayList<ViewPager$OnPageChangeListener>();
        }
        this.mOnPageChangeListeners.add(viewPager$OnPageChangeListener);
    }
    
    public void addTouchables(final ArrayList<View> list) {
        for (int i = 0; i < this.getChildCount(); ++i) {
            final View child = this.getChildAt(i);
            if (child.getVisibility() == 0) {
                final ViewPager$ItemInfo infoForChild = this.infoForChild(child);
                if (infoForChild != null && infoForChild.position == this.mCurItem) {
                    child.addTouchables((ArrayList)list);
                }
            }
        }
    }
    
    public void addView(final View view, final int n, ViewGroup$LayoutParams generateLayoutParams) {
        if (!this.checkLayoutParams(generateLayoutParams)) {
            generateLayoutParams = this.generateLayoutParams(generateLayoutParams);
        }
        final ViewPager$LayoutParams viewPager$LayoutParams = (ViewPager$LayoutParams)generateLayoutParams;
        viewPager$LayoutParams.isDecor |= (view instanceof ViewPager$Decor);
        if (!this.mInLayout) {
            super.addView(view, n, generateLayoutParams);
            return;
        }
        if (viewPager$LayoutParams != null && viewPager$LayoutParams.isDecor) {
            throw new IllegalStateException("Cannot add pager decor view during layout");
        }
        viewPager$LayoutParams.needsMeasure = true;
        this.addViewInLayout(view, n, generateLayoutParams);
    }
    
    public boolean arrowScroll(final int n) {
        final View focus = this.findFocus();
        View view = null;
        Label_0015: {
            if (focus == this) {
                view = null;
            }
            else {
                Label_0375: {
                    if (focus != null) {
                        ViewParent viewParent = focus.getParent();
                        while (true) {
                            while (viewParent instanceof ViewGroup) {
                                if (viewParent == this) {
                                    final int n2 = 1;
                                    if (n2 == 0) {
                                        final StringBuilder sb = new StringBuilder();
                                        sb.append(focus.getClass().getSimpleName());
                                        for (ViewParent viewParent2 = focus.getParent(); viewParent2 instanceof ViewGroup; viewParent2 = viewParent2.getParent()) {
                                            sb.append(" => ").append(viewParent2.getClass().getSimpleName());
                                        }
                                        Log.e("ViewPager", "arrowScroll tried to find focus based on non-child current focused view " + sb.toString());
                                        view = null;
                                        break Label_0015;
                                    }
                                    break Label_0375;
                                }
                                else {
                                    viewParent = viewParent.getParent();
                                }
                            }
                            final int n2 = 0;
                            continue;
                        }
                    }
                }
                view = focus;
            }
        }
        final View nextFocus = FocusFinder.getInstance().findNextFocus((ViewGroup)this, view, n);
        while (true) {
            Label_0369: {
                boolean b;
                if (nextFocus != null && nextFocus != view) {
                    if (n == 17) {
                        final int left = this.getChildRectInPagerCoordinates(this.mTempRect, nextFocus).left;
                        final int left2 = this.getChildRectInPagerCoordinates(this.mTempRect, view).left;
                        if (view != null && left >= left2) {
                            b = this.pageLeft();
                        }
                        else {
                            b = nextFocus.requestFocus();
                        }
                    }
                    else {
                        if (n != 66) {
                            break Label_0369;
                        }
                        final int left3 = this.getChildRectInPagerCoordinates(this.mTempRect, nextFocus).left;
                        final int left4 = this.getChildRectInPagerCoordinates(this.mTempRect, view).left;
                        if (view != null && left3 <= left4) {
                            b = this.pageRight();
                        }
                        else {
                            b = nextFocus.requestFocus();
                        }
                    }
                }
                else if (n == 17 || n == 1) {
                    b = this.pageLeft();
                }
                else {
                    if (n != 66 && n != 2) {
                        break Label_0369;
                    }
                    b = this.pageRight();
                }
                if (b) {
                    this.playSoundEffect(SoundEffectConstants.getContantForFocusDirection(n));
                }
                return b;
            }
            boolean b = false;
            continue;
        }
    }
    
    protected boolean canScroll(final View view, final boolean b, final int n, final int n2, final int n3) {
        if (view instanceof ViewGroup) {
            final ViewGroup viewGroup = (ViewGroup)view;
            final int scrollX = view.getScrollX();
            final int scrollY = view.getScrollY();
            for (int i = viewGroup.getChildCount() - 1; i >= 0; --i) {
                final View child = viewGroup.getChildAt(i);
                if (n2 + scrollX >= child.getLeft() && n2 + scrollX < child.getRight() && n3 + scrollY >= child.getTop() && n3 + scrollY < child.getBottom() && this.canScroll(child, true, n, n2 + scrollX - child.getLeft(), n3 + scrollY - child.getTop())) {
                    return true;
                }
            }
        }
        Label_0144: {
            break Label_0144;
        }
        if (!b || !ViewCompat.canScrollHorizontally(view, -n)) {
            return false;
        }
        return true;
    }
    
    public boolean canScrollHorizontally(final int n) {
        final boolean b = true;
        boolean b2 = true;
        if (this.mAdapter != null) {
            final int clientWidth = this.getClientWidth();
            final int scrollX = this.getScrollX();
            if (n < 0) {
                if (scrollX <= (int)(clientWidth * this.mFirstOffset)) {
                    b2 = false;
                }
                return b2;
            }
            if (n > 0) {
                return scrollX < (int)(clientWidth * this.mLastOffset) && b;
            }
        }
        return false;
    }
    
    protected boolean checkLayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        return viewGroup$LayoutParams instanceof ViewPager$LayoutParams && super.checkLayoutParams(viewGroup$LayoutParams);
    }
    
    public void computeScroll() {
        if (!this.mScroller.isFinished() && this.mScroller.computeScrollOffset()) {
            final int scrollX = this.getScrollX();
            final int scrollY = this.getScrollY();
            final int currX = this.mScroller.getCurrX();
            final int currY = this.mScroller.getCurrY();
            if (scrollX != currX || scrollY != currY) {
                this.scrollTo(currX, currY);
                if (!this.pageScrolled(currX)) {
                    this.mScroller.abortAnimation();
                    this.scrollTo(0, currY);
                }
            }
            ViewCompat.postInvalidateOnAnimation((View)this);
            return;
        }
        this.completeScroll(true);
    }
    
    void dataSetChanged() {
        final int count = this.mAdapter.getCount();
        this.mExpectedAdapterCount = count;
        int n;
        if (this.mItems.size() < this.mOffscreenPageLimit * 2 + 1 && this.mItems.size() < count) {
            n = 1;
        }
        else {
            n = 0;
        }
        final int mCurItem = this.mCurItem;
        final boolean b = false;
        final int n2 = 0;
        int n3 = n;
        int n4 = mCurItem;
        int n5 = b ? 1 : 0;
        int n9;
        int n10;
        int n15;
        int n16;
        for (int i = n2; i < this.mItems.size(); i = n10 + 1, n5 = n9, n4 = n16, n3 = n15) {
            final ViewPager$ItemInfo viewPager$ItemInfo = this.mItems.get(i);
            final int itemPosition = this.mAdapter.getItemPosition(viewPager$ItemInfo.object);
            int n8;
            int max;
            if (itemPosition == -1) {
                final int n6 = i;
                final int n7 = n5;
                n8 = n3;
                max = n4;
                n9 = n7;
                n10 = n6;
            }
            else if (itemPosition == -2) {
                this.mItems.remove(i);
                n10 = i - 1;
                int n11;
                if ((n11 = n5) == 0) {
                    this.mAdapter.startUpdate(this);
                    n11 = 1;
                }
                this.mAdapter.destroyItem(this, viewPager$ItemInfo.position, viewPager$ItemInfo.object);
                if (this.mCurItem == viewPager$ItemInfo.position) {
                    max = Math.max(0, Math.min(this.mCurItem, count - 1));
                    n9 = n11;
                    n8 = 1;
                }
                else {
                    max = n4;
                    final boolean b2 = true;
                    n9 = n11;
                    n8 = (b2 ? 1 : 0);
                }
            }
            else if (viewPager$ItemInfo.position != itemPosition) {
                if (viewPager$ItemInfo.position == this.mCurItem) {
                    n4 = itemPosition;
                }
                viewPager$ItemInfo.position = itemPosition;
                final int n12 = n4;
                final boolean b3 = true;
                n10 = i;
                n9 = n5;
                max = n12;
                n8 = (b3 ? 1 : 0);
            }
            else {
                final int n13 = n4;
                final int n14 = n3;
                n10 = i;
                n9 = n5;
                max = n13;
                n8 = n14;
            }
            n15 = n8;
            n16 = max;
        }
        if (n5 != 0) {
            this.mAdapter.finishUpdate(this);
        }
        Collections.sort(this.mItems, ViewPager.COMPARATOR);
        if (n3 != 0) {
            for (int childCount = this.getChildCount(), j = 0; j < childCount; ++j) {
                final ViewPager$LayoutParams viewPager$LayoutParams = (ViewPager$LayoutParams)this.getChildAt(j).getLayoutParams();
                if (!viewPager$LayoutParams.isDecor) {
                    viewPager$LayoutParams.widthFactor = 0.0f;
                }
            }
            this.setCurrentItemInternal(n4, false, true);
            this.requestLayout();
        }
    }
    
    public boolean dispatchKeyEvent(final KeyEvent keyEvent) {
        return super.dispatchKeyEvent(keyEvent) || this.executeKeyEvent(keyEvent);
    }
    
    public boolean dispatchPopulateAccessibilityEvent(final AccessibilityEvent accessibilityEvent) {
        final boolean b = false;
        if (accessibilityEvent.getEventType() != 4096) {
            final int childCount = this.getChildCount();
            int n = 0;
            while (true) {
                final boolean dispatchPopulateAccessibilityEvent = b;
                if (n >= childCount) {
                    return dispatchPopulateAccessibilityEvent;
                }
                final View child = this.getChildAt(n);
                if (child.getVisibility() == 0) {
                    final ViewPager$ItemInfo infoForChild = this.infoForChild(child);
                    if (infoForChild != null && infoForChild.position == this.mCurItem && child.dispatchPopulateAccessibilityEvent(accessibilityEvent)) {
                        break;
                    }
                }
                ++n;
            }
            return true;
        }
        return super.dispatchPopulateAccessibilityEvent(accessibilityEvent);
    }
    
    float distanceInfluenceForSnapDuration(final float n) {
        return (float)Math.sin((float)((n - 0.5f) * 0.4712389167638204));
    }
    
    public void draw(final Canvas canvas) {
        super.draw(canvas);
        boolean b = false;
        boolean b2 = false;
        final int overScrollMode = ViewCompat.getOverScrollMode((View)this);
        if (overScrollMode == 0 || (overScrollMode == 1 && this.mAdapter != null && this.mAdapter.getCount() > 1)) {
            if (!this.mLeftEdge.isFinished()) {
                final int save = canvas.save();
                final int n = this.getHeight() - this.getPaddingTop() - this.getPaddingBottom();
                final int width = this.getWidth();
                canvas.rotate(270.0f);
                canvas.translate((float)(-n + this.getPaddingTop()), this.mFirstOffset * width);
                this.mLeftEdge.setSize(n, width);
                b2 = (false | this.mLeftEdge.draw(canvas));
                canvas.restoreToCount(save);
            }
            b = b2;
            if (!this.mRightEdge.isFinished()) {
                final int save2 = canvas.save();
                final int width2 = this.getWidth();
                final int height = this.getHeight();
                final int paddingTop = this.getPaddingTop();
                final int paddingBottom = this.getPaddingBottom();
                canvas.rotate(90.0f);
                canvas.translate((float)(-this.getPaddingTop()), -(this.mLastOffset + 1.0f) * width2);
                this.mRightEdge.setSize(height - paddingTop - paddingBottom, width2);
                b = (b2 | this.mRightEdge.draw(canvas));
                canvas.restoreToCount(save2);
            }
        }
        else {
            this.mLeftEdge.finish();
            this.mRightEdge.finish();
        }
        if (b) {
            ViewCompat.postInvalidateOnAnimation((View)this);
        }
    }
    
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        final Drawable mMarginDrawable = this.mMarginDrawable;
        if (mMarginDrawable != null && mMarginDrawable.isStateful()) {
            mMarginDrawable.setState(this.getDrawableState());
        }
    }
    
    public boolean executeKeyEvent(final KeyEvent keyEvent) {
        if (keyEvent.getAction() == 0) {
            switch (keyEvent.getKeyCode()) {
                case 21: {
                    return this.arrowScroll(17);
                }
                case 22: {
                    return this.arrowScroll(66);
                }
                case 61: {
                    if (Build$VERSION.SDK_INT < 11) {
                        break;
                    }
                    if (KeyEventCompat.hasNoModifiers(keyEvent)) {
                        return this.arrowScroll(2);
                    }
                    if (KeyEventCompat.hasModifiers(keyEvent, 1)) {
                        return this.arrowScroll(1);
                    }
                    break;
                }
            }
        }
        return false;
    }
    
    protected ViewGroup$LayoutParams generateDefaultLayoutParams() {
        return new ViewPager$LayoutParams();
    }
    
    public ViewGroup$LayoutParams generateLayoutParams(final AttributeSet set) {
        return new ViewPager$LayoutParams(this.getContext(), set);
    }
    
    protected ViewGroup$LayoutParams generateLayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        return this.generateDefaultLayoutParams();
    }
    
    public PagerAdapter getAdapter() {
        return this.mAdapter;
    }
    
    protected int getChildDrawingOrder(final int n, final int n2) {
        int n3 = n2;
        if (this.mDrawingOrder == 2) {
            n3 = n - 1 - n2;
        }
        return ((ViewPager$LayoutParams)this.mDrawingOrderedChildren.get(n3).getLayoutParams()).childIndex;
    }
    
    public int getCurrentItem() {
        return this.mCurItem;
    }
    
    public int getOffscreenPageLimit() {
        return this.mOffscreenPageLimit;
    }
    
    public int getPageMargin() {
        return this.mPageMargin;
    }
    
    ViewPager$ItemInfo infoForAnyChild(View view) {
        while (true) {
            final ViewParent parent = view.getParent();
            if (parent == this) {
                return this.infoForChild(view);
            }
            if (parent == null || !(parent instanceof View)) {
                return null;
            }
            view = (View)parent;
        }
    }
    
    ViewPager$ItemInfo infoForChild(final View view) {
        for (int i = 0; i < this.mItems.size(); ++i) {
            final ViewPager$ItemInfo viewPager$ItemInfo = this.mItems.get(i);
            if (this.mAdapter.isViewFromObject(view, viewPager$ItemInfo.object)) {
                return viewPager$ItemInfo;
            }
        }
        return null;
    }
    
    ViewPager$ItemInfo infoForPosition(final int n) {
        for (int i = 0; i < this.mItems.size(); ++i) {
            final ViewPager$ItemInfo viewPager$ItemInfo = this.mItems.get(i);
            if (viewPager$ItemInfo.position == n) {
                return viewPager$ItemInfo;
            }
        }
        return null;
    }
    
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mFirstLayout = true;
    }
    
    protected void onDetachedFromWindow() {
        this.removeCallbacks(this.mEndScrollRunnable);
        super.onDetachedFromWindow();
    }
    
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        if (this.mPageMargin > 0 && this.mMarginDrawable != null && this.mItems.size() > 0 && this.mAdapter != null) {
            final int scrollX = this.getScrollX();
            final int width = this.getWidth();
            final float n = this.mPageMargin / width;
            ViewPager$ItemInfo viewPager$ItemInfo = this.mItems.get(0);
            float offset = viewPager$ItemInfo.offset;
            final int size = this.mItems.size();
            int i = viewPager$ItemInfo.position;
            final int position = this.mItems.get(size - 1).position;
            int n2 = 0;
            while (i < position) {
                while (i > viewPager$ItemInfo.position && n2 < size) {
                    final ArrayList<ViewPager$ItemInfo> mItems = this.mItems;
                    ++n2;
                    viewPager$ItemInfo = mItems.get(n2);
                }
                float n3;
                if (i == viewPager$ItemInfo.position) {
                    n3 = (viewPager$ItemInfo.offset + viewPager$ItemInfo.widthFactor) * width;
                    offset = viewPager$ItemInfo.offset + viewPager$ItemInfo.widthFactor + n;
                }
                else {
                    final float pageWidth = this.mAdapter.getPageWidth(i);
                    n3 = (offset + pageWidth) * width;
                    offset += pageWidth + n;
                }
                if (this.mPageMargin + n3 > scrollX) {
                    this.mMarginDrawable.setBounds((int)n3, this.mTopPageBounds, (int)(this.mPageMargin + n3 + 0.5f), this.mBottomPageBounds);
                    this.mMarginDrawable.draw(canvas);
                }
                if (n3 > scrollX + width) {
                    break;
                }
                ++i;
            }
        }
    }
    
    public boolean onInterceptTouchEvent(final MotionEvent motionEvent) {
        final int n = motionEvent.getAction() & 0xFF;
        if (n != 3 && n != 1) {
            if (n != 0) {
                if (this.mIsBeingDragged) {
                    return true;
                }
                if (this.mIsUnableToDrag) {
                    return false;
                }
            }
            switch (n) {
                case 2: {
                    final int mActivePointerId = this.mActivePointerId;
                    if (mActivePointerId == -1) {
                        break;
                    }
                    final int pointerIndex = MotionEventCompat.findPointerIndex(motionEvent, mActivePointerId);
                    final float x = MotionEventCompat.getX(motionEvent, pointerIndex);
                    final float n2 = x - this.mLastMotionX;
                    final float abs = Math.abs(n2);
                    final float y = MotionEventCompat.getY(motionEvent, pointerIndex);
                    final float abs2 = Math.abs(y - this.mInitialMotionY);
                    if (n2 != 0.0f && !this.isGutterDrag(this.mLastMotionX, n2) && this.canScroll((View)this, false, (int)n2, (int)x, (int)y)) {
                        this.mLastMotionX = x;
                        this.mLastMotionY = y;
                        this.mIsUnableToDrag = true;
                        return false;
                    }
                    if (abs > this.mTouchSlop && 0.5f * abs > abs2) {
                        this.requestParentDisallowInterceptTouchEvent(this.mIsBeingDragged = true);
                        this.setScrollState(1);
                        float mLastMotionX;
                        if (n2 > 0.0f) {
                            mLastMotionX = this.mInitialMotionX + this.mTouchSlop;
                        }
                        else {
                            mLastMotionX = this.mInitialMotionX - this.mTouchSlop;
                        }
                        this.mLastMotionX = mLastMotionX;
                        this.mLastMotionY = y;
                        this.setScrollingCacheEnabled(true);
                    }
                    else if (abs2 > this.mTouchSlop) {
                        this.mIsUnableToDrag = true;
                    }
                    if (this.mIsBeingDragged && this.performDrag(x)) {
                        ViewCompat.postInvalidateOnAnimation((View)this);
                        break;
                    }
                    break;
                }
                case 0: {
                    final float x2 = motionEvent.getX();
                    this.mInitialMotionX = x2;
                    this.mLastMotionX = x2;
                    final float y2 = motionEvent.getY();
                    this.mInitialMotionY = y2;
                    this.mLastMotionY = y2;
                    this.mActivePointerId = MotionEventCompat.getPointerId(motionEvent, 0);
                    this.mIsUnableToDrag = false;
                    this.mScroller.computeScrollOffset();
                    if (this.mScrollState == 2 && Math.abs(this.mScroller.getFinalX() - this.mScroller.getCurrX()) > this.mCloseEnough) {
                        this.mScroller.abortAnimation();
                        this.mPopulatePending = false;
                        this.populate();
                        this.requestParentDisallowInterceptTouchEvent(this.mIsBeingDragged = true);
                        this.setScrollState(1);
                        break;
                    }
                    this.completeScroll(false);
                    this.mIsBeingDragged = false;
                    break;
                }
                case 6: {
                    this.onSecondaryPointerUp(motionEvent);
                    break;
                }
            }
            if (this.mVelocityTracker == null) {
                this.mVelocityTracker = VelocityTracker.obtain();
            }
            this.mVelocityTracker.addMovement(motionEvent);
            return this.mIsBeingDragged;
        }
        this.mIsBeingDragged = false;
        this.mIsUnableToDrag = false;
        this.mActivePointerId = -1;
        if (this.mVelocityTracker != null) {
            this.mVelocityTracker.recycle();
            this.mVelocityTracker = null;
        }
        return false;
    }
    
    protected void onLayout(final boolean b, int paddingTop, int paddingLeft, int paddingBottom, int i) {
        final int childCount = this.getChildCount();
        final int n = paddingBottom - paddingTop;
        final int n2 = i - paddingLeft;
        paddingLeft = this.getPaddingLeft();
        paddingTop = this.getPaddingTop();
        int paddingRight = this.getPaddingRight();
        paddingBottom = this.getPaddingBottom();
        final int scrollX = this.getScrollX();
        int mDecorChildCount = 0;
        int j = 0;
    Label_0256_Outer:
        while (j < childCount) {
            final View child = this.getChildAt(j);
            while (true) {
                Label_0671: {
                    if (child.getVisibility() == 8) {
                        break Label_0671;
                    }
                    final ViewPager$LayoutParams viewPager$LayoutParams = (ViewPager$LayoutParams)child.getLayoutParams();
                    if (!viewPager$LayoutParams.isDecor) {
                        break Label_0671;
                    }
                    i = viewPager$LayoutParams.gravity;
                    final int gravity = viewPager$LayoutParams.gravity;
                    int n3 = 0;
                    switch (i & 0x7) {
                        default: {
                            i = paddingLeft;
                            n3 = paddingLeft;
                            break;
                        }
                        case 3: {
                            final int measuredWidth = child.getMeasuredWidth();
                            i = paddingLeft;
                            n3 = measuredWidth + paddingLeft;
                            break;
                        }
                        case 1: {
                            i = Math.max((n - child.getMeasuredWidth()) / 2, paddingLeft);
                            n3 = paddingLeft;
                            break;
                        }
                        case 5: {
                            final int measuredWidth2 = child.getMeasuredWidth();
                            i = paddingRight + child.getMeasuredWidth();
                            final int n4 = n - paddingRight - measuredWidth2;
                            paddingRight = i;
                            n3 = paddingLeft;
                            i = n4;
                            break;
                        }
                    }
                    switch (gravity & 0x70) {
                        default: {
                            final int n5 = paddingTop;
                            paddingLeft = paddingTop;
                            paddingTop = paddingBottom;
                            paddingBottom = n5;
                            break;
                        }
                        case 48: {
                            final int measuredHeight = child.getMeasuredHeight();
                            paddingLeft = paddingBottom;
                            final int n6 = measuredHeight + paddingTop;
                            paddingBottom = paddingTop;
                            paddingTop = paddingLeft;
                            paddingLeft = n6;
                            break;
                        }
                        case 16: {
                            final int max = Math.max((n2 - child.getMeasuredHeight()) / 2, paddingTop);
                            paddingLeft = paddingTop;
                            paddingTop = paddingBottom;
                            paddingBottom = max;
                            break;
                        }
                        case 80: {
                            final int n7 = n2 - paddingBottom - child.getMeasuredHeight();
                            final int measuredHeight2 = child.getMeasuredHeight();
                            paddingLeft = paddingTop;
                            paddingTop = paddingBottom + measuredHeight2;
                            paddingBottom = n7;
                            break;
                        }
                    }
                    i += scrollX;
                    child.layout(i, paddingBottom, child.getMeasuredWidth() + i, child.getMeasuredHeight() + paddingBottom);
                    final int n8 = mDecorChildCount + 1;
                    i = paddingRight;
                    paddingBottom = n3;
                    final int n9 = paddingTop;
                    paddingTop = n8;
                    ++j;
                    final int n10 = paddingBottom;
                    mDecorChildCount = paddingTop;
                    paddingTop = paddingLeft;
                    paddingBottom = n9;
                    paddingRight = i;
                    paddingLeft = n10;
                    continue Label_0256_Outer;
                }
                i = mDecorChildCount;
                final int n11 = paddingTop;
                final int n12 = paddingLeft;
                paddingTop = i;
                i = paddingRight;
                final int n9 = paddingBottom;
                paddingLeft = n11;
                paddingBottom = n12;
                continue;
            }
        }
        final int n13 = n - paddingLeft - paddingRight;
        View child2;
        ViewPager$LayoutParams viewPager$LayoutParams2;
        ViewPager$ItemInfo infoForChild;
        int n14;
        for (i = 0; i < childCount; ++i) {
            child2 = this.getChildAt(i);
            if (child2.getVisibility() != 8) {
                viewPager$LayoutParams2 = (ViewPager$LayoutParams)child2.getLayoutParams();
                if (!viewPager$LayoutParams2.isDecor) {
                    infoForChild = this.infoForChild(child2);
                    if (infoForChild != null) {
                        n14 = (int)(infoForChild.offset * n13) + paddingLeft;
                        if (viewPager$LayoutParams2.needsMeasure) {
                            viewPager$LayoutParams2.needsMeasure = false;
                            child2.measure(View$MeasureSpec.makeMeasureSpec((int)(viewPager$LayoutParams2.widthFactor * n13), 1073741824), View$MeasureSpec.makeMeasureSpec(n2 - paddingTop - paddingBottom, 1073741824));
                        }
                        child2.layout(n14, paddingTop, child2.getMeasuredWidth() + n14, child2.getMeasuredHeight() + paddingTop);
                    }
                }
            }
        }
        this.mTopPageBounds = paddingTop;
        this.mBottomPageBounds = n2 - paddingBottom;
        this.mDecorChildCount = mDecorChildCount;
        if (this.mFirstLayout) {
            this.scrollToItem(this.mCurItem, false, 0, false);
        }
        this.mFirstLayout = false;
    }
    
    protected void onMeasure(int measuredWidth, int i) {
        this.setMeasuredDimension(getDefaultSize(0, measuredWidth), getDefaultSize(0, i));
        measuredWidth = this.getMeasuredWidth();
        this.mGutterSize = Math.min(measuredWidth / 10, this.mDefaultGutterSize);
        measuredWidth = measuredWidth - this.getPaddingLeft() - this.getPaddingRight();
        i = this.getMeasuredHeight() - this.getPaddingTop() - this.getPaddingBottom();
        int n;
        int n2;
    Label_0318:
        for (int childCount = this.getChildCount(), j = 0; j < childCount; ++j, measuredWidth = n, i = n2) {
            final View child = this.getChildAt(j);
            n = measuredWidth;
            n2 = i;
            if (child.getVisibility() != 8) {
                final ViewPager$LayoutParams viewPager$LayoutParams = (ViewPager$LayoutParams)child.getLayoutParams();
                n = measuredWidth;
                n2 = i;
                if (viewPager$LayoutParams != null) {
                    n = measuredWidth;
                    n2 = i;
                    if (viewPager$LayoutParams.isDecor) {
                        final int n3 = viewPager$LayoutParams.gravity & 0x7;
                        final int n4 = viewPager$LayoutParams.gravity & 0x70;
                        final int n5 = Integer.MIN_VALUE;
                        int n6 = Integer.MIN_VALUE;
                        boolean b;
                        if (n4 == 48 || n4 == 80) {
                            b = true;
                        }
                        else {
                            b = false;
                        }
                        final boolean b2 = n3 == 3 || n3 == 5;
                        int n7;
                        if (b) {
                            n7 = 1073741824;
                        }
                        else {
                            n7 = n5;
                            if (b2) {
                                n6 = 1073741824;
                                n7 = n5;
                            }
                        }
                        int n8;
                        int width;
                        if (viewPager$LayoutParams.width != -2) {
                            n8 = 1073741824;
                            if (viewPager$LayoutParams.width != -1) {
                                width = viewPager$LayoutParams.width;
                            }
                            else {
                                width = measuredWidth;
                            }
                        }
                        else {
                            n8 = n7;
                            width = measuredWidth;
                        }
                        while (true) {
                            Label_0516: {
                                if (viewPager$LayoutParams.height == -2) {
                                    break Label_0516;
                                }
                                n6 = 1073741824;
                                if (viewPager$LayoutParams.height == -1) {
                                    break Label_0516;
                                }
                                final int height = viewPager$LayoutParams.height;
                                n6 = n6;
                                final int n9 = height;
                                child.measure(View$MeasureSpec.makeMeasureSpec(width, n8), View$MeasureSpec.makeMeasureSpec(n9, n6));
                                if (b) {
                                    n2 = i - child.getMeasuredHeight();
                                    n = measuredWidth;
                                    continue Label_0318;
                                }
                                n = measuredWidth;
                                n2 = i;
                                if (b2) {
                                    n = measuredWidth - child.getMeasuredWidth();
                                    n2 = i;
                                }
                                continue Label_0318;
                            }
                            final int n9 = i;
                            continue;
                        }
                    }
                }
            }
        }
        this.mChildWidthMeasureSpec = View$MeasureSpec.makeMeasureSpec(measuredWidth, 1073741824);
        this.mChildHeightMeasureSpec = View$MeasureSpec.makeMeasureSpec(i, 1073741824);
        this.mInLayout = true;
        this.populate();
        this.mInLayout = false;
        int childCount2;
        View child2;
        ViewPager$LayoutParams viewPager$LayoutParams2;
        for (childCount2 = this.getChildCount(), i = 0; i < childCount2; ++i) {
            child2 = this.getChildAt(i);
            if (child2.getVisibility() != 8) {
                viewPager$LayoutParams2 = (ViewPager$LayoutParams)child2.getLayoutParams();
                if (viewPager$LayoutParams2 == null || !viewPager$LayoutParams2.isDecor) {
                    child2.measure(View$MeasureSpec.makeMeasureSpec((int)(viewPager$LayoutParams2.widthFactor * measuredWidth), 1073741824), this.mChildHeightMeasureSpec);
                }
            }
        }
    }
    
    protected void onPageScrolled(int i, float n, int scrollX) {
        if (this.mDecorChildCount > 0) {
            final int scrollX2 = this.getScrollX();
            int paddingLeft = this.getPaddingLeft();
            int paddingRight = this.getPaddingRight();
            final int width = this.getWidth();
            int n2;
            int n3;
            for (int childCount = this.getChildCount(), j = 0; j < childCount; ++j, paddingLeft = n2, paddingRight = n3) {
                final View child = this.getChildAt(j);
                final ViewPager$LayoutParams viewPager$LayoutParams = (ViewPager$LayoutParams)child.getLayoutParams();
                if (!viewPager$LayoutParams.isDecor) {
                    n2 = paddingLeft;
                    n3 = paddingRight;
                }
                else {
                    int max = 0;
                    int n5 = 0;
                    int n6 = 0;
                    switch (viewPager$LayoutParams.gravity & 0x7) {
                        default: {
                            max = paddingLeft;
                            final int n4 = paddingRight;
                            n5 = paddingLeft;
                            n6 = n4;
                            break;
                        }
                        case 3: {
                            final int n7 = child.getWidth() + paddingLeft;
                            max = paddingLeft;
                            n6 = paddingRight;
                            n5 = n7;
                            break;
                        }
                        case 1: {
                            max = Math.max((width - child.getMeasuredWidth()) / 2, paddingLeft);
                            final int n8 = paddingLeft;
                            n6 = paddingRight;
                            n5 = n8;
                            break;
                        }
                        case 5: {
                            max = width - paddingRight - child.getMeasuredWidth();
                            final int measuredWidth = child.getMeasuredWidth();
                            final int n9 = paddingLeft;
                            n6 = paddingRight + measuredWidth;
                            n5 = n9;
                            break;
                        }
                    }
                    final int n10 = max + scrollX2 - child.getLeft();
                    n3 = n6;
                    n2 = n5;
                    if (n10 != 0) {
                        child.offsetLeftAndRight(n10);
                        n3 = n6;
                        n2 = n5;
                    }
                }
            }
        }
        this.dispatchOnPageScrolled(i, n, scrollX);
        if (this.mPageTransformer != null) {
            scrollX = this.getScrollX();
            int childCount2;
            View child2;
            for (childCount2 = this.getChildCount(), i = 0; i < childCount2; ++i) {
                child2 = this.getChildAt(i);
                if (!((ViewPager$LayoutParams)child2.getLayoutParams()).isDecor) {
                    n = (child2.getLeft() - scrollX) / this.getClientWidth();
                    this.mPageTransformer.transformPage(child2, n);
                }
            }
        }
        this.mCalledSuper = true;
    }
    
    protected boolean onRequestFocusInDescendants(final int n, final Rect rect) {
        int n2 = -1;
        int childCount = this.getChildCount();
        int i;
        if ((n & 0x2) != 0x0) {
            n2 = 1;
            i = 0;
        }
        else {
            i = childCount - 1;
            childCount = -1;
        }
        while (i != childCount) {
            final View child = this.getChildAt(i);
            if (child.getVisibility() == 0) {
                final ViewPager$ItemInfo infoForChild = this.infoForChild(child);
                if (infoForChild != null && infoForChild.position == this.mCurItem && child.requestFocus(n, rect)) {
                    return true;
                }
            }
            i += n2;
        }
        return false;
    }
    
    public void onRestoreInstanceState(final Parcelable parcelable) {
        if (!(parcelable instanceof ViewPager$SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        final ViewPager$SavedState viewPager$SavedState = (ViewPager$SavedState)parcelable;
        super.onRestoreInstanceState(viewPager$SavedState.getSuperState());
        if (this.mAdapter != null) {
            this.mAdapter.restoreState(viewPager$SavedState.adapterState, viewPager$SavedState.loader);
            this.setCurrentItemInternal(viewPager$SavedState.position, false, true);
            return;
        }
        this.mRestoredCurItem = viewPager$SavedState.position;
        this.mRestoredAdapterState = viewPager$SavedState.adapterState;
        this.mRestoredClassLoader = viewPager$SavedState.loader;
    }
    
    public Parcelable onSaveInstanceState() {
        final ViewPager$SavedState viewPager$SavedState = new ViewPager$SavedState(super.onSaveInstanceState());
        viewPager$SavedState.position = this.mCurItem;
        if (this.mAdapter != null) {
            viewPager$SavedState.adapterState = this.mAdapter.saveState();
        }
        return (Parcelable)viewPager$SavedState;
    }
    
    protected void onSizeChanged(final int n, final int n2, final int n3, final int n4) {
        super.onSizeChanged(n, n2, n3, n4);
        if (n != n3) {
            this.recomputeScrollPosition(n, n3, this.mPageMargin, this.mPageMargin);
        }
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        final boolean b = false;
        if (this.mFakeDragging) {
            return true;
        }
        if (motionEvent.getAction() == 0 && motionEvent.getEdgeFlags() != 0) {
            return false;
        }
        if (this.mAdapter == null || this.mAdapter.getCount() == 0) {
            return false;
        }
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement(motionEvent);
        boolean b2 = b;
        while (true) {
            switch (motionEvent.getAction() & 0xFF) {
                default: {
                    b2 = b;
                    break Label_0128;
                }
                case 6: {
                    this.onSecondaryPointerUp(motionEvent);
                    this.mLastMotionX = MotionEventCompat.getX(motionEvent, MotionEventCompat.findPointerIndex(motionEvent, this.mActivePointerId));
                    b2 = b;
                    break Label_0128;
                }
                case 5: {
                    final int actionIndex = MotionEventCompat.getActionIndex(motionEvent);
                    this.mLastMotionX = MotionEventCompat.getX(motionEvent, actionIndex);
                    this.mActivePointerId = MotionEventCompat.getPointerId(motionEvent, actionIndex);
                    b2 = b;
                    break Label_0128;
                }
                case 0: {
                    this.mScroller.abortAnimation();
                    this.mPopulatePending = false;
                    this.populate();
                    final float x = motionEvent.getX();
                    this.mInitialMotionX = x;
                    this.mLastMotionX = x;
                    final float y = motionEvent.getY();
                    this.mInitialMotionY = y;
                    this.mLastMotionY = y;
                    this.mActivePointerId = MotionEventCompat.getPointerId(motionEvent, 0);
                    b2 = b;
                }
                case 4: {
                    if (b2) {
                        ViewCompat.postInvalidateOnAnimation((View)this);
                    }
                    return true;
                }
                case 2: {
                    if (!this.mIsBeingDragged) {
                        final int pointerIndex = MotionEventCompat.findPointerIndex(motionEvent, this.mActivePointerId);
                        final float x2 = MotionEventCompat.getX(motionEvent, pointerIndex);
                        final float abs = Math.abs(x2 - this.mLastMotionX);
                        final float y2 = MotionEventCompat.getY(motionEvent, pointerIndex);
                        final float abs2 = Math.abs(y2 - this.mLastMotionY);
                        if (abs > this.mTouchSlop && abs > abs2) {
                            this.requestParentDisallowInterceptTouchEvent(this.mIsBeingDragged = true);
                            float mLastMotionX;
                            if (x2 - this.mInitialMotionX > 0.0f) {
                                mLastMotionX = this.mInitialMotionX + this.mTouchSlop;
                            }
                            else {
                                mLastMotionX = this.mInitialMotionX - this.mTouchSlop;
                            }
                            this.mLastMotionX = mLastMotionX;
                            this.mLastMotionY = y2;
                            this.setScrollState(1);
                            this.setScrollingCacheEnabled(true);
                            final ViewParent parent = this.getParent();
                            if (parent != null) {
                                parent.requestDisallowInterceptTouchEvent(true);
                            }
                        }
                    }
                    b2 = b;
                    if (this.mIsBeingDragged) {
                        b2 = (false | this.performDrag(MotionEventCompat.getX(motionEvent, MotionEventCompat.findPointerIndex(motionEvent, this.mActivePointerId))));
                    }
                    continue;
                }
                case 1: {
                    b2 = b;
                    if (this.mIsBeingDragged) {
                        final VelocityTracker mVelocityTracker = this.mVelocityTracker;
                        mVelocityTracker.computeCurrentVelocity(1000, (float)this.mMaximumVelocity);
                        final int n = (int)VelocityTrackerCompat.getXVelocity(mVelocityTracker, this.mActivePointerId);
                        this.mPopulatePending = true;
                        final int clientWidth = this.getClientWidth();
                        final int scrollX = this.getScrollX();
                        final ViewPager$ItemInfo infoForCurrentScrollPosition = this.infoForCurrentScrollPosition();
                        this.setCurrentItemInternal(this.determineTargetPage(infoForCurrentScrollPosition.position, (scrollX / clientWidth - infoForCurrentScrollPosition.offset) / infoForCurrentScrollPosition.widthFactor, n, (int)(MotionEventCompat.getX(motionEvent, MotionEventCompat.findPointerIndex(motionEvent, this.mActivePointerId)) - this.mInitialMotionX)), true, true, n);
                        this.mActivePointerId = -1;
                        this.endDrag();
                        b2 = (this.mRightEdge.onRelease() | this.mLeftEdge.onRelease());
                    }
                    continue;
                }
                case 3: {
                    b2 = b;
                    if (this.mIsBeingDragged) {
                        this.scrollToItem(this.mCurItem, true, 0, false);
                        this.mActivePointerId = -1;
                        this.endDrag();
                        b2 = (this.mRightEdge.onRelease() | this.mLeftEdge.onRelease());
                    }
                    continue;
                }
            }
            break;
        }
    }
    
    boolean pageLeft() {
        if (this.mCurItem > 0) {
            this.setCurrentItem(this.mCurItem - 1, true);
            return true;
        }
        return false;
    }
    
    boolean pageRight() {
        if (this.mAdapter != null && this.mCurItem < this.mAdapter.getCount() - 1) {
            this.setCurrentItem(this.mCurItem + 1, true);
            return true;
        }
        return false;
    }
    
    void populate() {
        this.populate(this.mCurItem);
    }
    
    void populate(int i) {
        ViewPager$ItemInfo infoForPosition;
        int n2;
        if (this.mCurItem != i) {
            int n;
            if (this.mCurItem < i) {
                n = 66;
            }
            else {
                n = 17;
            }
            infoForPosition = this.infoForPosition(this.mCurItem);
            this.mCurItem = i;
            n2 = n;
        }
        else {
            infoForPosition = null;
            n2 = 2;
        }
        if (this.mAdapter == null) {
            this.sortChildDrawingOrder();
        }
        else {
            if (this.mPopulatePending) {
                this.sortChildDrawingOrder();
                return;
            }
            if (this.getWindowToken() != null) {
                this.mAdapter.startUpdate(this);
                i = this.mOffscreenPageLimit;
                final int max = Math.max(0, this.mCurItem - i);
                final int count = this.mAdapter.getCount();
                final int min = Math.min(count - 1, i + this.mCurItem);
                if (count != this.mExpectedAdapterCount) {
                    try {
                        final String s = this.getResources().getResourceName(this.getId());
                        throw new IllegalStateException("The application's PagerAdapter changed the adapter's contents without calling PagerAdapter#notifyDataSetChanged! Expected adapter item count: " + this.mExpectedAdapterCount + ", found: " + count + " Pager id: " + s + " Pager class: " + this.getClass() + " Problematic adapter: " + this.mAdapter.getClass());
                    }
                    catch (Resources$NotFoundException ex) {
                        final String s = Integer.toHexString(this.getId());
                        throw new IllegalStateException("The application's PagerAdapter changed the adapter's contents without calling PagerAdapter#notifyDataSetChanged! Expected adapter item count: " + this.mExpectedAdapterCount + ", found: " + count + " Pager id: " + s + " Pager class: " + this.getClass() + " Problematic adapter: " + this.mAdapter.getClass());
                    }
                }
                i = 0;
                while (true) {
                    while (i < this.mItems.size()) {
                        final ViewPager$ItemInfo viewPager$ItemInfo = this.mItems.get(i);
                        if (viewPager$ItemInfo.position >= this.mCurItem) {
                            if (viewPager$ItemInfo.position != this.mCurItem) {
                                break;
                            }
                            ViewPager$ItemInfo addNewItem;
                            if (viewPager$ItemInfo == null && count > 0) {
                                addNewItem = this.addNewItem(this.mCurItem, i);
                            }
                            else {
                                addNewItem = viewPager$ItemInfo;
                            }
                            if (addNewItem != null) {
                                int n3 = i - 1;
                                ViewPager$ItemInfo viewPager$ItemInfo2;
                                if (n3 >= 0) {
                                    viewPager$ItemInfo2 = this.mItems.get(n3);
                                }
                                else {
                                    viewPager$ItemInfo2 = null;
                                }
                                final int clientWidth = this.getClientWidth();
                                float n4;
                                if (clientWidth <= 0) {
                                    n4 = 0.0f;
                                }
                                else {
                                    n4 = 2.0f - addNewItem.widthFactor + this.getPaddingLeft() / clientWidth;
                                }
                                final int mCurItem = this.mCurItem;
                                float n5 = 0.0f;
                                int j = mCurItem - 1;
                                int n6 = i;
                                ViewPager$ItemInfo viewPager$ItemInfo3 = viewPager$ItemInfo2;
                                while (j >= 0) {
                                    ViewPager$ItemInfo viewPager$ItemInfo4;
                                    float n7;
                                    int n8;
                                    if (n5 >= n4 && j < max) {
                                        if (viewPager$ItemInfo3 == null) {
                                            break;
                                        }
                                        viewPager$ItemInfo4 = viewPager$ItemInfo3;
                                        i = n3;
                                        n7 = n5;
                                        n8 = n6;
                                        if (j == viewPager$ItemInfo3.position) {
                                            viewPager$ItemInfo4 = viewPager$ItemInfo3;
                                            i = n3;
                                            n7 = n5;
                                            n8 = n6;
                                            if (!viewPager$ItemInfo3.scrolling) {
                                                this.mItems.remove(n3);
                                                this.mAdapter.destroyItem(this, j, viewPager$ItemInfo3.object);
                                                i = n3 - 1;
                                                n8 = n6 - 1;
                                                if (i >= 0) {
                                                    viewPager$ItemInfo4 = this.mItems.get(i);
                                                    n7 = n5;
                                                }
                                                else {
                                                    viewPager$ItemInfo4 = null;
                                                    n7 = n5;
                                                }
                                            }
                                        }
                                    }
                                    else if (viewPager$ItemInfo3 != null && j == viewPager$ItemInfo3.position) {
                                        n7 = n5 + viewPager$ItemInfo3.widthFactor;
                                        i = n3 - 1;
                                        if (i >= 0) {
                                            viewPager$ItemInfo4 = this.mItems.get(i);
                                            n8 = n6;
                                        }
                                        else {
                                            viewPager$ItemInfo4 = null;
                                            n8 = n6;
                                        }
                                    }
                                    else {
                                        n7 = n5 + this.addNewItem(j, n3 + 1).widthFactor;
                                        n8 = n6 + 1;
                                        if (n3 >= 0) {
                                            viewPager$ItemInfo4 = this.mItems.get(n3);
                                            i = n3;
                                        }
                                        else {
                                            viewPager$ItemInfo4 = null;
                                            i = n3;
                                        }
                                    }
                                    --j;
                                    viewPager$ItemInfo3 = viewPager$ItemInfo4;
                                    n3 = i;
                                    n5 = n7;
                                    n6 = n8;
                                }
                                float widthFactor = addNewItem.widthFactor;
                                i = n6 + 1;
                                if (widthFactor < 2.0f) {
                                    ViewPager$ItemInfo viewPager$ItemInfo5;
                                    if (i < this.mItems.size()) {
                                        viewPager$ItemInfo5 = this.mItems.get(i);
                                    }
                                    else {
                                        viewPager$ItemInfo5 = null;
                                    }
                                    float n9;
                                    if (clientWidth <= 0) {
                                        n9 = 0.0f;
                                    }
                                    else {
                                        n9 = this.getPaddingRight() / clientWidth + 2.0f;
                                    }
                                    for (int k = this.mCurItem + 1; k < count; ++k) {
                                        if (widthFactor >= n9 && k > min) {
                                            if (viewPager$ItemInfo5 == null) {
                                                break;
                                            }
                                            if (k == viewPager$ItemInfo5.position && !viewPager$ItemInfo5.scrolling) {
                                                this.mItems.remove(i);
                                                this.mAdapter.destroyItem(this, k, viewPager$ItemInfo5.object);
                                                if (i < this.mItems.size()) {
                                                    viewPager$ItemInfo5 = this.mItems.get(i);
                                                }
                                                else {
                                                    viewPager$ItemInfo5 = null;
                                                }
                                            }
                                        }
                                        else if (viewPager$ItemInfo5 != null && k == viewPager$ItemInfo5.position) {
                                            final float widthFactor2 = viewPager$ItemInfo5.widthFactor;
                                            ++i;
                                            if (i < this.mItems.size()) {
                                                viewPager$ItemInfo5 = this.mItems.get(i);
                                            }
                                            else {
                                                viewPager$ItemInfo5 = null;
                                            }
                                            widthFactor += widthFactor2;
                                        }
                                        else {
                                            final ViewPager$ItemInfo addNewItem2 = this.addNewItem(k, i);
                                            ++i;
                                            final float widthFactor3 = addNewItem2.widthFactor;
                                            if (i < this.mItems.size()) {
                                                viewPager$ItemInfo5 = this.mItems.get(i);
                                            }
                                            else {
                                                viewPager$ItemInfo5 = null;
                                            }
                                            widthFactor += widthFactor3;
                                        }
                                    }
                                }
                                this.calculatePageOffsets(addNewItem, n6, infoForPosition);
                            }
                            final PagerAdapter mAdapter = this.mAdapter;
                            i = this.mCurItem;
                            Object object;
                            if (addNewItem != null) {
                                object = addNewItem.object;
                            }
                            else {
                                object = null;
                            }
                            mAdapter.setPrimaryItem(this, i, object);
                            this.mAdapter.finishUpdate(this);
                            int childCount;
                            View child;
                            ViewPager$LayoutParams viewPager$LayoutParams;
                            ViewPager$ItemInfo infoForChild;
                            for (childCount = this.getChildCount(), i = 0; i < childCount; ++i) {
                                child = this.getChildAt(i);
                                viewPager$LayoutParams = (ViewPager$LayoutParams)child.getLayoutParams();
                                viewPager$LayoutParams.childIndex = i;
                                if (!viewPager$LayoutParams.isDecor && viewPager$LayoutParams.widthFactor == 0.0f) {
                                    infoForChild = this.infoForChild(child);
                                    if (infoForChild != null) {
                                        viewPager$LayoutParams.widthFactor = infoForChild.widthFactor;
                                        viewPager$LayoutParams.position = infoForChild.position;
                                    }
                                }
                            }
                            this.sortChildDrawingOrder();
                            if (!this.hasFocus()) {
                                return;
                            }
                            final View focus = this.findFocus();
                            ViewPager$ItemInfo infoForAnyChild;
                            if (focus != null) {
                                infoForAnyChild = this.infoForAnyChild(focus);
                            }
                            else {
                                infoForAnyChild = null;
                            }
                            if (infoForAnyChild == null || infoForAnyChild.position != this.mCurItem) {
                                View child2;
                                ViewPager$ItemInfo infoForChild2;
                                for (i = 0; i < this.getChildCount(); ++i) {
                                    child2 = this.getChildAt(i);
                                    infoForChild2 = this.infoForChild(child2);
                                    if (infoForChild2 != null && infoForChild2.position == this.mCurItem && child2.requestFocus(n2)) {
                                        break;
                                    }
                                }
                            }
                            return;
                        }
                        else {
                            ++i;
                        }
                    }
                    final ViewPager$ItemInfo viewPager$ItemInfo = null;
                    continue;
                }
            }
        }
    }
    
    public void removeView(final View view) {
        if (this.mInLayout) {
            this.removeViewInLayout(view);
            return;
        }
        super.removeView(view);
    }
    
    public void setAdapter(final PagerAdapter mAdapter) {
        if (this.mAdapter != null) {
            this.mAdapter.unregisterDataSetObserver(this.mObserver);
            this.mAdapter.startUpdate(this);
            for (int i = 0; i < this.mItems.size(); ++i) {
                final ViewPager$ItemInfo viewPager$ItemInfo = this.mItems.get(i);
                this.mAdapter.destroyItem(this, viewPager$ItemInfo.position, viewPager$ItemInfo.object);
            }
            this.mAdapter.finishUpdate(this);
            this.mItems.clear();
            this.removeNonDecorViews();
            this.scrollTo(this.mCurItem = 0, 0);
        }
        final PagerAdapter mAdapter2 = this.mAdapter;
        this.mAdapter = mAdapter;
        this.mExpectedAdapterCount = 0;
        if (this.mAdapter != null) {
            if (this.mObserver == null) {
                this.mObserver = new ViewPager$PagerObserver(this, null);
            }
            this.mAdapter.registerDataSetObserver(this.mObserver);
            this.mPopulatePending = false;
            final boolean mFirstLayout = this.mFirstLayout;
            this.mFirstLayout = true;
            this.mExpectedAdapterCount = this.mAdapter.getCount();
            if (this.mRestoredCurItem >= 0) {
                this.mAdapter.restoreState(this.mRestoredAdapterState, this.mRestoredClassLoader);
                this.setCurrentItemInternal(this.mRestoredCurItem, false, true);
                this.mRestoredCurItem = -1;
                this.mRestoredAdapterState = null;
                this.mRestoredClassLoader = null;
            }
            else if (!mFirstLayout) {
                this.populate();
            }
            else {
                this.requestLayout();
            }
        }
        if (this.mAdapterChangeListener != null && mAdapter2 != mAdapter) {
            this.mAdapterChangeListener.onAdapterChanged(mAdapter2, mAdapter);
        }
    }
    
    void setChildrenDrawingOrderEnabledCompat(final boolean p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: bipush          7
        //     5: if_icmplt       57
        //     8: aload_0        
        //     9: getfield        android/support/v4/view/ViewPager.mSetChildrenDrawingOrderEnabled:Ljava/lang/reflect/Method;
        //    12: ifnonnull       37
        //    15: aload_0        
        //    16: ldc             Landroid/view/ViewGroup;.class
        //    18: ldc_w           "setChildrenDrawingOrderEnabled"
        //    21: iconst_1       
        //    22: anewarray       Ljava/lang/Class;
        //    25: dup            
        //    26: iconst_0       
        //    27: getstatic       java/lang/Boolean.TYPE:Ljava/lang/Class;
        //    30: aastore        
        //    31: invokevirtual   java/lang/Class.getDeclaredMethod:(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
        //    34: putfield        android/support/v4/view/ViewPager.mSetChildrenDrawingOrderEnabled:Ljava/lang/reflect/Method;
        //    37: aload_0        
        //    38: getfield        android/support/v4/view/ViewPager.mSetChildrenDrawingOrderEnabled:Ljava/lang/reflect/Method;
        //    41: aload_0        
        //    42: iconst_1       
        //    43: anewarray       Ljava/lang/Object;
        //    46: dup            
        //    47: iconst_0       
        //    48: iload_1        
        //    49: invokestatic    java/lang/Boolean.valueOf:(Z)Ljava/lang/Boolean;
        //    52: aastore        
        //    53: invokevirtual   java/lang/reflect/Method.invoke:(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
        //    56: pop            
        //    57: return         
        //    58: astore_2       
        //    59: ldc_w           "ViewPager"
        //    62: ldc_w           "Can't find setChildrenDrawingOrderEnabled"
        //    65: aload_2        
        //    66: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //    69: pop            
        //    70: goto            37
        //    73: astore_2       
        //    74: ldc_w           "ViewPager"
        //    77: ldc_w           "Error changing children drawing order"
        //    80: aload_2        
        //    81: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //    84: pop            
        //    85: return         
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                             
        //  -----  -----  -----  -----  ---------------------------------
        //  15     37     58     73     Ljava/lang/NoSuchMethodException;
        //  37     57     73     86     Ljava/lang/Exception;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0037:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2592)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:757)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:655)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:532)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:499)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:141)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:130)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:105)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:317)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:238)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:138)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public void setCurrentItem(final int n) {
        this.mPopulatePending = false;
        this.setCurrentItemInternal(n, !this.mFirstLayout, false);
    }
    
    public void setCurrentItem(final int n, final boolean b) {
        this.setCurrentItemInternal(n, b, this.mPopulatePending = false);
    }
    
    void setCurrentItemInternal(final int n, final boolean b, final boolean b2) {
        this.setCurrentItemInternal(n, b, b2, 0);
    }
    
    void setCurrentItemInternal(int i, final boolean b, final boolean b2, final int n) {
        final boolean b3 = false;
        if (this.mAdapter == null || this.mAdapter.getCount() <= 0) {
            this.setScrollingCacheEnabled(false);
            return;
        }
        if (!b2 && this.mCurItem == i && this.mItems.size() != 0) {
            this.setScrollingCacheEnabled(false);
            return;
        }
        int mCurItem;
        if (i < 0) {
            mCurItem = 0;
        }
        else if ((mCurItem = i) >= this.mAdapter.getCount()) {
            mCurItem = this.mAdapter.getCount() - 1;
        }
        i = this.mOffscreenPageLimit;
        if (mCurItem > this.mCurItem + i || mCurItem < this.mCurItem - i) {
            for (i = 0; i < this.mItems.size(); ++i) {
                this.mItems.get(i).scrolling = true;
            }
        }
        boolean b4 = b3;
        if (this.mCurItem != mCurItem) {
            b4 = true;
        }
        if (this.mFirstLayout) {
            this.mCurItem = mCurItem;
            if (b4) {
                this.dispatchOnPageSelected(mCurItem);
            }
            this.requestLayout();
            return;
        }
        this.populate(mCurItem);
        this.scrollToItem(mCurItem, b, n, b4);
    }
    
    public void setOffscreenPageLimit(final int n) {
        int mOffscreenPageLimit = n;
        if (n < 1) {
            Log.w("ViewPager", "Requested offscreen page limit " + n + " too small; defaulting to " + 1);
            mOffscreenPageLimit = 1;
        }
        if (mOffscreenPageLimit != this.mOffscreenPageLimit) {
            this.mOffscreenPageLimit = mOffscreenPageLimit;
            this.populate();
        }
    }
    
    void setOnAdapterChangeListener(final ViewPager$OnAdapterChangeListener mAdapterChangeListener) {
        this.mAdapterChangeListener = mAdapterChangeListener;
    }
    
    @Deprecated
    public void setOnPageChangeListener(final ViewPager$OnPageChangeListener mOnPageChangeListener) {
        this.mOnPageChangeListener = mOnPageChangeListener;
    }
    
    public void setPageMargin(final int mPageMargin) {
        final int mPageMargin2 = this.mPageMargin;
        this.mPageMargin = mPageMargin;
        final int width = this.getWidth();
        this.recomputeScrollPosition(width, width, mPageMargin, mPageMargin2);
        this.requestLayout();
    }
    
    public void setPageMarginDrawable(final int n) {
        this.setPageMarginDrawable(this.getContext().getResources().getDrawable(n));
    }
    
    public void setPageMarginDrawable(final Drawable mMarginDrawable) {
        this.mMarginDrawable = mMarginDrawable;
        if (mMarginDrawable != null) {
            this.refreshDrawableState();
        }
        this.setWillNotDraw(mMarginDrawable == null);
        this.invalidate();
    }
    
    void smoothScrollTo(int n, int n2, int abs) {
        if (this.getChildCount() == 0) {
            this.setScrollingCacheEnabled(false);
            return;
        }
        final int scrollX = this.getScrollX();
        final int scrollY = this.getScrollY();
        final int n3 = n - scrollX;
        n2 -= scrollY;
        if (n3 == 0 && n2 == 0) {
            this.completeScroll(false);
            this.populate();
            this.setScrollState(0);
            return;
        }
        this.setScrollingCacheEnabled(true);
        this.setScrollState(2);
        n = this.getClientWidth();
        final int n4 = n / 2;
        final float min = Math.min(1.0f, Math.abs(n3) * 1.0f / n);
        final float n5 = n4;
        final float n6 = n4;
        final float distanceInfluenceForSnapDuration = this.distanceInfluenceForSnapDuration(min);
        abs = Math.abs(abs);
        if (abs > 0) {
            n = Math.round(1000.0f * Math.abs((n6 * distanceInfluenceForSnapDuration + n5) / abs)) * 4;
        }
        else {
            n = (int)((Math.abs(n3) / (n * this.mAdapter.getPageWidth(this.mCurItem) + this.mPageMargin) + 1.0f) * 100.0f);
        }
        n = Math.min(n, 600);
        this.mScroller.startScroll(scrollX, scrollY, n3, n2, n);
        ViewCompat.postInvalidateOnAnimation((View)this);
    }
    
    protected boolean verifyDrawable(final Drawable drawable) {
        return super.verifyDrawable(drawable) || drawable == this.mMarginDrawable;
    }
}
