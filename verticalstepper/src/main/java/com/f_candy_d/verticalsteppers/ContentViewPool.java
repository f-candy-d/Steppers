package com.f_candy_d.verticalsteppers;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.SparseArray;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daichi on 10/15/17.
 */

class ContentViewPool {

    private static final int VIEW_TAG_UID_WITHIN_POOL = 175890472;

    @NonNull private final LocalUID LUID_GENERATOR;
    private final int CONTENT_VIEW_TYPE;
    @NonNull private List<View> mAvailableViews;
    // @key = local-uid, @value = view
    @NonNull private SparseArray<View> mBindedViews;

    ContentViewPool(int contentViewType) {
        LUID_GENERATOR = new LocalUID();
        CONTENT_VIEW_TYPE = contentViewType;
        mAvailableViews = new ArrayList<>();
        mBindedViews = new SparseArray<>();
    }

    /**
     * ContentViewPool consider that a returned view will be attached to a VerticalStepperView.
     */
    @Nullable
    View getAvailableView() {
        View view = popViewFromAvailableViewList();
        if (view != null) {
            putViewToBindedViewList(view);
        }
        return view;
    }

    void recycleView(@NonNull View view) {
        if (view.getParent() != null) {
            throw new IllegalStateException(
                    "The view must be removed from its parent before call recycleView() method");
        }
        if (getContentViewHolderFromView(view).getContentViewType() != CONTENT_VIEW_TYPE) {
            throw new IllegalStateException(
                    "The view's content-view-type must be exactly the same as the ContentViewPool's content-view-type");
        }

        int luid = getLocalUidFromView(view);
        if (luid != LocalUID.INVALID_LOCAL_UID) {
            removeViewFromBindedViewList(luid);
        } else {
            setLocalUIDToView(view);
        }

        pushViewToAvailableViewList(view);
    }

    @Nullable
    private View popViewFromAvailableViewList() {
        return (0 < mAvailableViews.size())
                ? mAvailableViews.remove(mAvailableViews.size() - 1)
                : null;
    }

    private void pushViewToAvailableViewList(@NonNull View view) {
        mAvailableViews.add(view);
    }

    private void putViewToBindedViewList(@NonNull View view) {
        int luid = getLocalUidFromView(view);
        if (luid == LocalUID.INVALID_LOCAL_UID) {
            throw new IllegalStateException("The view does't have a local-uid");
        }
        mBindedViews.put(luid, view);
    }

    private void removeViewFromBindedViewList(int localUid) {
        mBindedViews.remove(localUid);
    }

    private void setLocalUIDToView(@NonNull View view) {
        if (getLocalUidFromView(view) != LocalUID.INVALID_LOCAL_UID) {
            throw new IllegalStateException("The view already has a local uid");
        }
        view.setTag(VIEW_TAG_UID_WITHIN_POOL, LUID_GENERATOR.generate());
    }

    static BaseVerticalStepperAdapter.ContentViewHolder getContentViewHolderFromView(@NonNull View view) {
        BaseVerticalStepperAdapter.ContentViewHolder holder =
                (BaseVerticalStepperAdapter.ContentViewHolder) view.getTag(
                        BaseVerticalStepperAdapter.ContentViewHolder.VIEW_TAG_CONTENT_VIEW_HOLDER);

        if (holder == null) {
            throw new IllegalStateException("The view does't have a ContentViewHolder in its tags");
        }

        return holder;
    }

    private int getLocalUidFromView(@NonNull View view) {
        Integer luid = (Integer) view.getTag(VIEW_TAG_UID_WITHIN_POOL);
        return (luid != null) ? luid : LocalUID.INVALID_LOCAL_UID;
    }

    /**
     * HELPER CLASS TO GENERATE A LOCAL UID
     * ----------------------------------------------------------------------------- */

    private static class LocalUID {

        static final int INVALID_LOCAL_UID = 0;
        private int mUid;

        LocalUID() {
            mUid = INVALID_LOCAL_UID;
        }

        int generate() {
            return ++mUid;
        }
    }
}
