package com.maxiee.heartbeat.common;

/**
 * Created by maxiee on 15-6-13.
 *
 * The cheesesquare project has a bug:
 *  in layouts using CoordinatorLayout, when there is only one CardView,
 *  it will display incorrectly.
 *
 * This class is copy by EmmanuelVinas's Gist which will fix this problem.
 * how-to use:
 *  app:layout_behavior="com.maxiee.attitude.common.FixedHeightScrollingViewBehavior"
 */
import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

public class FixedHeightScrollingViewBehavior extends AppBarLayout.ScrollingViewBehavior{

    public FixedHeightScrollingViewBehavior() {
    }

    public FixedHeightScrollingViewBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public boolean onMeasureChild(CoordinatorLayout parent, View child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
        if(child.getLayoutParams().height == -1) {
            List dependencies = parent.getDependencies(child);
            if(dependencies.isEmpty()) {
                return false;
            }

            AppBarLayout appBar = findFirstAppBarLayout(dependencies);
            if(appBar != null && ViewCompat.isLaidOut(appBar)) {
                if(ViewCompat.getFitsSystemWindows(appBar)) {
                    ViewCompat.setFitsSystemWindows(child, true);
                }

                int scrollRange = appBar.getTotalScrollRange();
                int height = parent.getHeight() - appBar.getMeasuredHeight() + Math.min(scrollRange,  parent.getHeight()- heightUsed);
                int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);
                parent.onMeasureChild(child, parentWidthMeasureSpec, widthUsed, heightMeasureSpec, heightUsed);
                return true;
            }
        }

        return false;
    }

    private static AppBarLayout findFirstAppBarLayout(List<View> views) {
        int i = 0;

        for(int z = views.size(); i < z; ++i) {
            View view = (View)views.get(i);
            if(view instanceof AppBarLayout) {
                return (AppBarLayout)view;
            }
        }

        return null;
    }
}