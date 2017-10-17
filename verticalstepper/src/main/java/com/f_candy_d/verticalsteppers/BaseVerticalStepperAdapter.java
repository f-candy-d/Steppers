package com.f_candy_d.verticalsteppers;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by daichi on 10/14/17.
 *
 * T is a class type of RecyclerView's view-holder (RecyclerView.ViewHolder).
 * C is a class type of collapsed content view's view-holder (BaseVerticalStepperAdapter.ContentViewHolder).
 * E is a class type of expanded content view's view-holder (BaseVerticalStepperAdapter.ContentViewHolder).
 */

abstract public class BaseVerticalStepperAdapter<
        T extends RecyclerView.ViewHolder,
        C extends BaseVerticalStepperAdapter.ContentViewHolder,
        E extends BaseVerticalStepperAdapter.ContentViewHolder>
        extends RecyclerView.Adapter<T>
        implements ContentViewRecycler.Callback {

    private ContentViewRecycler mContentViewRecycler;

    public BaseVerticalStepperAdapter() {
        mContentViewRecycler = new ContentViewRecycler(this);
    }

    @Override
    public void onBindViewHolder(T holder, int position) {
        if (holder instanceof BaseViewHolder) {
            VerticalStepperView stepperView = ((BaseViewHolder) holder).getStepperView();
            mContentViewRecycler.emplaceContentViewsOfStepperViewIfNeeded(
                    stepperView,
                    getCollapsedContentViewType(position),
                    getExpandedContentViewType(position));

            View contentView = stepperView.getCollapsedContentView();
            if (contentView != null) {
                onBindCollapsedViewHolder((C) ContentViewPool.getContentViewHolderFromView(contentView), position);
            }

            contentView = stepperView.getExpandedContentView();
            if (contentView != null) {
                onBindExpandedViewHolder((E) ContentViewPool.getContentViewHolderFromView(contentView), position);
            }
        }
    }

    @Override
    public View onCreateCollapsedContentView(ViewGroup parent, int contentViewType) {
        if (contentViewType != CONTENT_VIEW_TYPE_EMPTY_VIEW) {
            ContentViewHolder holder = onCreateCollapsedContentViewHolder(parent, contentViewType);
            holder.setContentViewType(contentViewType);
            return holder.getContentView();
        }
        return null;
    }

    @Override
    public View onCreateExpandedContentView(ViewGroup parent, int contentViewType) {
        if (contentViewType != CONTENT_VIEW_TYPE_EMPTY_VIEW) {
            ContentViewHolder holder = onCreateExpandedContentViewHolder(parent, contentViewType);
            holder.setContentViewType(contentViewType);
            return holder.getContentView();
        }
        return null;
    }

    protected void onBindCollapsedViewHolder(C holder, int position) {}
    protected void onBindExpandedViewHolder(E holder, int position) {}

    // 'contentViewType' would never be the save as 'CONTENT_VIEW_TYPE_EMPTY_VIEW'
    @NonNull abstract
    protected ContentViewHolder onCreateCollapsedContentViewHolder(ViewGroup parent, int contentViewType);
    @NonNull abstract
    protected ContentViewHolder onCreateExpandedContentViewHolder(ViewGroup parent, int contentViewType);

    // Use this constant variable as a view type of 'no content view'
    public static final int CONTENT_VIEW_TYPE_EMPTY_VIEW = ContentViewRecycler.CONTENT_VIEW_TYPE_EMPTY_VIEW;

    /**
     * Use the following 2 methods if you want to use multiple types of content views
     */

    protected int getCollapsedContentViewType(int position) {
        return 0;
    }

    protected int getExpandedContentViewType(int position) {
        return 0;
    }

    /**
     * BASE VIEW HOLDER
     * ----------------------------------------------------------------------------- */

    abstract public static class BaseViewHolder extends RecyclerView.ViewHolder {

        public BaseViewHolder(View view) {
            super(view);
        }

        @NonNull
        abstract public VerticalStepperView getStepperView();

        @Nullable
        public <T extends ContentViewHolder> T getCollapsedContentViewHolder() {
            View contentView = getStepperView().getCollapsedContentView();
            if (contentView == null) return null;
            return (T) ContentViewPool.getContentViewHolderFromView(contentView);
        }

        @Nullable
        public <T extends ContentViewHolder> T getExpandedContentViewHolder() {
            View contentView = getStepperView().getExpandedContentView();
            if (contentView == null) return null;
            return (T) ContentViewPool.getContentViewHolderFromView(contentView);
        }
    }

    /**
     * CONTENT VIEW HOLDER
     * ----------------------------------------------------------------------------- */

    abstract public static class ContentViewHolder {

        // Use this as a key of View#setTag(), to set a content-view-holder as a view's tag
        static final int VIEW_TAG_CONTENT_VIEW_HOLDER = 744311751;

        private int mContentViewType;
        private View mContentView;

        public ContentViewHolder(View view) {
            mContentView = view;
            view.setTag(VIEW_TAG_CONTENT_VIEW_HOLDER, this);
        }

        public int getContentViewType() {
            return mContentViewType;
        }

        void setContentViewType(int contentViewType) {
            mContentViewType = contentViewType;
        }

        public View getContentView() {
            return mContentView;
        }
    }
}