package com.f_candy_d.verticalsteppers;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by daichi on 10/14/17.
 */

class ContentViewRecycler {

    static final int CONTENT_VIEW_TYPE_EMPTY_VIEW = -54683202;

    interface Callback {
        View onCreateCollapsedContentView(ViewGroup parent, int contentViewType);
        View onCreateExpandedContentView(ViewGroup parent, int contentViewType);
    }

    // @key=contentViewType, @value=ContentViewPool
    @NonNull private final SparseArray<ContentViewPool> mCollapsedContentViewPools;
    @NonNull private final SparseArray<ContentViewPool> mExpandedContentViewPools;
    @NonNull private Callback mCallback;

    ContentViewRecycler(@NonNull Callback callback) {
        mCollapsedContentViewPools = new SparseArray<>();
        mExpandedContentViewPools = new SparseArray<>();
        mCallback = callback;
    }

    void emplaceContentViewsOfStepperViewIfNeeded(
            @NonNull VerticalStepperView stepperView,
            int nextCollapsedContentViewType,
            int nextExpandedContentViewType) {

        // Emplace collapsed content views if necessary
        View contentView = stepperView.getCollapsedContentView();
        int viewType = (contentView == null)
                ? CONTENT_VIEW_TYPE_EMPTY_VIEW :
                ContentViewPool.getContentViewHolderFromView(contentView).getContentViewType();

        if (viewType != nextCollapsedContentViewType) {
            // Emplace collapsed content views
            recycleView(stepperView.removeCollapsedContentView(), mCollapsedContentViewPools);
            contentView = getAvailableViewForViewType(nextCollapsedContentViewType, mCollapsedContentViewPools);
            if (contentView == null) {
                contentView = mCallback.onCreateCollapsedContentView(stepperView.getContentViewContainer(), nextCollapsedContentViewType);
            }
            stepperView.setCollapsedContentView(contentView);
        }

        // Emplace expanded content views if necessary
        contentView = stepperView.getExpandedContentView();
        viewType = (contentView == null)
                ? CONTENT_VIEW_TYPE_EMPTY_VIEW :
                ContentViewPool.getContentViewHolderFromView(contentView).getContentViewType();

        if (viewType != nextExpandedContentViewType) {
            // Emplace expanded content views
            recycleView(stepperView.removeExpandedContentView(), mExpandedContentViewPools);
            contentView = getAvailableViewForViewType(nextExpandedContentViewType, mExpandedContentViewPools);
            if (contentView == null) {
                contentView = mCallback.onCreateExpandedContentView(stepperView.getContentViewContainer(), nextExpandedContentViewType);
            }
            stepperView.setExpandedContentView(contentView);
        }
    }

    @Nullable
    private View getAvailableViewForViewType(int contentViewType, SparseArray<ContentViewPool> pools) {
        ContentViewPool pool = pools.get(contentViewType, null);
        if (pool == null) return null;
        return pool.getAvailableView();
    }

    private void recycleView(@Nullable View view, SparseArray<ContentViewPool> pools) {
        if (view == null) return;
        BaseVerticalStepperAdapter.ContentViewHolder holder =
                ContentViewPool.getContentViewHolderFromView(view);
        ContentViewPool pool = pools.get(holder.getContentViewType(), null);
        if (pool != null) {
            pool.recycleView(view);
        } else {
            pool = new ContentViewPool(holder.getContentViewType());
            pools.put(holder.getContentViewType(), pool);
            pool.recycleView(view);
        }
    }
}
