package com.f_candy_d.verticalsteppers;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.support.annotation.ColorInt;
import android.support.annotation.Dimension;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.transition.TransitionManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.widget.TextViewCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class VerticalStepperView extends RelativeLayout {

    // For TransitionDrawable which is used as Circular-Label's background
    private static final int DRAWABLE_ID_ACTIVE_CIRCULAR_LABEL_BG = 0;
    private static final int DRAWABLE_ID_INACTIVE_CIRCULAR_LABEL_BG = 1;
    private TransitionDrawableHelper mCircularLabelBgDrawableHelper;

    // Will be used only in #onDraw()
    // Initialize or load these values in #init()
    private Paint mStepperConnectionLinePaint;
    private int mStepperConnectionLineTopMargin;
    private int mStepperConnectionLineBottomMargin;

    // UI
    private TextView mTitleView;
    private TextView mSubTitleView;
    private TextView mStepperCircleTextView;
    private ImageView mStepperCircleIconView;
    private FrameLayout mStepperCircleLayout;
    private FrameLayout mContentViewContainer;
    private View mExpandedContentView;
    private View mCollapsedContentView;
    private View mHeaderContentView;
    private View mFooterContentView;

    // Attributes
    // Theme of stepper
    @ColorInt private int mActiveThemeColor;
    @ColorInt private int mInactiveThemeColor;
    // Theme of title
    @StyleRes private int mActiveTitleAppearanceResId;
    @StyleRes private int mInactiveTitleAppearanceResId;
    // Theme of sub-title
    @StyleRes private int mActiveSubTitleAppearanceResId;
    @StyleRes private int mInactiveSubTitleAppearanceResId;
    // THeme of circular-label
    @ColorInt private int mActiveCircularLabelIconTint;
    @ColorInt private int mActiveCircularLabelTextTint;
    @ColorInt private int mInactiveCircularLabelIconTint;
    @ColorInt private int mInactiveCircularLabelTextTint;

    public VerticalStepperView(Context context) {
        super(context);
        init(null, 0);
    }

    public VerticalStepperView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public VerticalStepperView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {

        // # UI

        // Load the layout file
        inflate(getContext(), R.layout.vertical_stepper_view, this);

        mTitleView = findViewById(R.id.work_title);
        mSubTitleView = findViewById(R.id.work_sub_title);
        mStepperCircleTextView = findViewById(R.id.stepper_circle_text);
        mStepperCircleIconView = findViewById(R.id.stepper_circle_icon);
        mStepperCircleLayout = findViewById(R.id.stepper_circle_layout);
        mContentViewContainer = findViewById(R.id.content_view_container);

        // # Setup TransitionDrawable for Circular Label's background

        mCircularLabelBgDrawableHelper = new TransitionDrawableHelper((TransitionDrawable) mStepperCircleLayout.getBackground());
        mCircularLabelBgDrawableHelper.setFirstLayerId(DRAWABLE_ID_ACTIVE_CIRCULAR_LABEL_BG);
        mCircularLabelBgDrawableHelper.setSecondLayerId(DRAWABLE_ID_INACTIVE_CIRCULAR_LABEL_BG);

        // # For onDraw() method

        mStepperConnectionLinePaint = new Paint();
        mStepperConnectionLineTopMargin = getResources().getDimensionPixelSize(R.dimen.vertical_stepper_connection_line_top_margin);
        mStepperConnectionLineBottomMargin = getResources().getDimensionPixelSize(R.dimen.vertical_stepper_connection_line_bottom_margin);

        // # Load default attributes from xml and apply them to UI

        final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.VerticalStepperView, defStyle, 0);

        // Whole

        setActiveThemeColor(a.getColor(R.styleable.VerticalStepperView_activeThemeColor,
                ContextCompat.getColor(getContext(), R.color.vertical_stepper_active_primary)));
        setInactiveThemeColor(a.getColor(R.styleable.VerticalStepperView_inactiveThemeColor,
                ContextCompat.getColor(getContext(), R.color.vertical_stepper_inactive_primary)));

        mStepperConnectionLinePaint.setColor(a.getColor(R.styleable.VerticalStepperView_stepConnectorColor,
                ContextCompat.getColor(getContext(), R.color.vertical_stepper_connector_line)));
        mStepperConnectionLinePaint.setStrokeWidth(a.getDimensionPixelSize(R.styleable.VerticalStepperView_stepConnectionLineWidth,
                getResources().getDimensionPixelSize(R.dimen.vertical_stepper_connection_line_width)));

        // Circular Label

        setActiveCircularLabelIconTint(a.getColor(R.styleable.VerticalStepperView_activeCircularLabelIconTint,
                ContextCompat.getColor(getContext(), R.color.vertical_stepper_active_circular_label_icon_tint)));
        setInactiveCircularLabelIconTint(a.getColor(R.styleable.VerticalStepperView_inactiveCircularLabelIconTint,
                ContextCompat.getColor(getContext(), R.color.vertical_stepper_inactive_circular_label_icon_tint)));

        setActiveCircularLabelTextTint(a.getColor(R.styleable.VerticalStepperView_activeCircularLabelTextTint,
                ContextCompat.getColor(getContext(), R.color.vertical_stepper_active_circular_label_text_tint)));
        setInactiveCircularLabelTextTint(a.getColor(R.styleable.VerticalStepperView_inactiveCircularLabelTextTint,
                ContextCompat.getColor(getContext(), R.color.vertical_stepper_inactive_circular_label_text_tint)));

        setCompletedIcon(a.getResourceId(R.styleable.VerticalStepperView_completedIcon, R.drawable.ic_check));

        switch (a.getInt(R.styleable.VerticalStepperView_circularLabelSize, CIRCULAR_LABEL_SIZE_REGULAR)) {
            case CIRCULAR_LABEL_SIZE_REGULAR: setCircularLabelSize(CIRCULAR_LABEL_SIZE_REGULAR); break;
            case CIRCULAR_LABEL_SIZE_SMALL: setCircularLabelSize(CIRCULAR_LABEL_SIZE_SMALL); break;
        }

        setStepLabel(a.getString(R.styleable.VerticalStepperView_stepLabel));

        // Title

        setActiveTitleAppearanceResId(a.getResourceId(R.styleable.VerticalStepperView_activeTitleAppearance,
                R.style.VerticalStepperActiveTitleAppearance));
        setInactiveTitleAppearanceResId(a.getResourceId(R.styleable.VerticalStepperView_inactiveTitleAppearance,
                R.style.VerticalStepperInactiveTitleAppearance));
        setTitle(a.getString(R.styleable.VerticalStepperView_title));

        // SubTitle

        setActiveSubTitleAppearanceResId(a.getResourceId(R.styleable.VerticalStepperView_activeSubTitleAppearance,
                R.style.VerticalStepperActiveSubTitleAppearance));
        setInactiveSubTitleAppearanceResId(a.getResourceId(R.styleable.VerticalStepperView_inactiveSubTitleAppearance,
                R.style.VerticalStepperInactiveSubTitleAppearance));
        setSubTitle(a.getString(R.styleable.VerticalStepperView_subTitle));

        // Content Views

        // id = 0 is a invalid resource id; -> https://stackoverflow.com/questions/5130789/android-resource-ids
        int contentViewResId = a.getResourceId(R.styleable.VerticalStepperView_collapsedContentViewLayout, 0);
        if (contentViewResId != 0) {
            setCollapsedContentView(contentViewResId);
        } else {
            setCollapsedContentView(null);
        }

        contentViewResId = a.getResourceId(R.styleable.VerticalStepperView_expandedContentViewLayout, 0);
        if (contentViewResId != 0) {
            setExpandedContentView(contentViewResId);
        } else {
            setExpandedContentView(null);
        }

        contentViewResId = a.getResourceId(R.styleable.VerticalStepperView_headerContentViewLayout, 0);
        if (contentViewResId != 0) {
            setHeaderContentView(contentViewResId);
        } else {
            setHeaderContentView(null);
        }

        contentViewResId = a.getResourceId(R.styleable.VerticalStepperView_footerContentViewLayout, 0);
        if (contentViewResId != 0) {
            setFooterContentView(contentViewResId);
        } else {
            setFooterContentView(null);
        }

        // Default status

        if (a.getBoolean(R.styleable.VerticalStepperView_expandContentsByDefault, false)) expandContentView(true); else collapseContentView(true);
        if (a.getBoolean(R.styleable.VerticalStepperView_activateStepByDefault, false)) activateStep(true); else inactivateStep(true);
        if (a.getBoolean(R.styleable.VerticalStepperView_completeStepByDefault, false)) completeStep(true); else incompleteStep(true);

        a.recycle();

        // # Add ripple-effect to myself
        // # See -> https://stackoverflow.com/questions/37987732/programatically-set-selectableitembackground-on-android-view

        TypedValue outValue = new TypedValue();
        getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
        this.setBackgroundResource(outValue.resourceId);
        this.setClickable(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // # Connection Line

        int connectionLineX = mStepperCircleLayout.getLeft() + mStepperCircleLayout.getWidth() / 2;
        int connectionLineStartY = mStepperCircleLayout.getBottom() + mStepperConnectionLineTopMargin;
        int contentHeight = getHeight() - getPaddingTop() - getPaddingBottom();
        int connectionLineEndY = contentHeight - mStepperConnectionLineBottomMargin;

        canvas.drawLine(connectionLineX, connectionLineStartY, connectionLineX, connectionLineEndY, mStepperConnectionLinePaint);
    }

    /**
     * CONTENTS
     * ----------------------------------------------------------------------------- */
    
    public void setTitle(String title) {
        mTitleView.setText(title);
        if (title == null || title.length() == 0) {
            mTitleView.setVisibility(GONE);
        } else {
            mTitleView.setVisibility(VISIBLE);
        }
    }

    public void setSubTitle(String subTitle) {
        mSubTitleView.setText(subTitle);
        if (subTitle == null || subTitle.length() == 0) {
            mSubTitleView.setVisibility(GONE);
        } else {
            mSubTitleView.setVisibility(VISIBLE);
        }
    }

    public void setStepLabel(int number) {
        setStepLabel(String.valueOf(number));
    }

    public void setStepLabel(String text) {
        mStepperCircleTextView.setText(text);
    }

    public View setExpandedContentView(@LayoutRes int layoutId) {
        View view = inflate(getContext(), layoutId, null);
        setExpandedContentView(view);
        return view;
    }

    public void setExpandedContentView(View view) {
        removeExpandedContentView();
        if (view != null) {
            mContentViewContainer.addView(view);
            mExpandedContentView = view;
            // Invalidate
            if (mIsContentViewExpanded) {
                expandContentView(true);
            } else {
                collapseContentView(true);
            }
        }
    }

    public View setCollapsedContentView(@LayoutRes int layoutId) {
        View view = inflate(getContext(), layoutId, null);
        setCollapsedContentView(view);
        return view;
    }

    public void setCollapsedContentView(View view) {
        removeCollapsedContentView();
        if (view != null) {
            mContentViewContainer.addView(view);
            mCollapsedContentView = view;
            // Invalidate
            if (!mIsContentViewExpanded) {
                collapseContentView(true);
            } else {
                expandContentView(true);
            }
        }
    }

    public View removeExpandedContentView() {
        if (mExpandedContentView != null) {
            mContentViewContainer.removeView(mExpandedContentView);
        }
        View removed = mExpandedContentView;
        mExpandedContentView = null;
        if (mContentViewContainer.getChildCount() == 0) {
            mContentViewContainer.setVisibility(GONE);
        }

        return removed;
    }

    public View removeCollapsedContentView() {
        if (mCollapsedContentView != null) {
            mContentViewContainer.removeView(mCollapsedContentView);
        }
        View removed = mCollapsedContentView;
        mCollapsedContentView = null;
        if (mContentViewContainer.getChildCount() == 0) {
            mContentViewContainer.setVisibility(GONE);
        }

        return removed;
    }

    public boolean hasExpandedContentView() {
        return (mExpandedContentView != null);
    }

    public boolean hasCollapsedContentView() {
        return (mCollapsedContentView != null);
    }

    public View setHeaderContentView(@LayoutRes int layoutId) {
        View view = inflate(getContext(), layoutId, null);
        setHeaderContentView(view);
        return view;
    }

    public void setHeaderContentView(View view) {
        swapHeaderContentView(view);
    }

    public View setFooterContentView(@LayoutRes int layoutId) {
        View view = inflate(getContext(), layoutId, null);
        setFooterContentView(view);
        return view;
    }

    public void setFooterContentView(View view) {
        swapFooterContentView(view);
    }

    public View removeHeaderContentView() {
        return swapHeaderContentView(null);
    }

    public View removeFooterContentView() {
        return swapFooterContentView(null);
    }

    public View swapHeaderContentView(View view) {
        ViewGroup container = findViewById(R.id.header_content_container);
        if (mHeaderContentView != null) {
            container.removeView(mHeaderContentView);
        }

        View removed = mHeaderContentView;
        mHeaderContentView = view;
        if (view != null) {
            container.addView(view);
            container.setVisibility(VISIBLE);
        } else {
            container.setVisibility(GONE);
        }

        return removed;
    }

    public View swapFooterContentView(View view) {
        ViewGroup container = findViewById(R.id.footer_content_container);
        if (mFooterContentView != null) {
            container.removeView(mFooterContentView);
        }

        View removed = mFooterContentView;
        mFooterContentView = view;
        if (view != null) {
            container.addView(view);
            container.setVisibility(VISIBLE);
        } else {
            container.setVisibility(GONE);
        }

        return removed;
    }

    public boolean hasHeaderContentView() {
        return (mHeaderContentView != null);
    }

    public boolean hasFooterContentView() {
        return (mFooterContentView != null);
    }

    public View getHeaderContentView() {
        return mHeaderContentView;
    }

    public View getFooterContentView() {
        return mFooterContentView;
    }

    /**
     * This method has package-private visibility
     */
    ViewGroup getContentViewContainer() {
        return mContentViewContainer;
    }

    public View getExpandedContentView() {
        return mExpandedContentView;
    }

    public View getCollapsedContentView() {
        return mCollapsedContentView;
    }

    /**
     * STATUS
     * ----------------------------------------------------------------------------- */

    private boolean mIsContentViewExpanded;

    public void expandContentView(boolean enforceUpdate) {
        if (mIsContentViewExpanded && !enforceUpdate) return;
        mIsContentViewExpanded = true;
        if (mContentViewContainer.getChildCount() == 0) return;
        toggleContentViews(mContentViewContainer, mCollapsedContentView, mExpandedContentView);
    }

    public void collapseContentView(boolean enforceUpdate) {
        if (!mIsContentViewExpanded && !enforceUpdate) return;
        mIsContentViewExpanded = false;
        if (mContentViewContainer.getChildCount() == 0) return;
        toggleContentViews(mContentViewContainer, mExpandedContentView, mCollapsedContentView);
    }

    private void toggleContentViews(@NonNull View container, View willHidden, View willReveal) {
        if (willReveal == null) {
            container.setVisibility(GONE);
        } else {
            if (willHidden != null) {
                willHidden.setVisibility(GONE);
            }
            willReveal.setVisibility(VISIBLE);
            if (container.getVisibility() != VISIBLE) {
                container.setVisibility(VISIBLE);
            }
        }
    }

    public boolean isContentViewExpanded() {
        return mIsContentViewExpanded;
    }

    private boolean mIsStepActive;

    public void activateStep(boolean enforceUpdate) {
        activateStep(enforceUpdate, false);
    }

    public void activateStep(boolean enforceUpdate, boolean animate) {
        if (mIsStepActive && !enforceUpdate) return;
        if (animate) {
            toggleCircularLabelBackgroundColorDrawable(DRAWABLE_ID_ACTIVE_CIRCULAR_LABEL_BG);
        } else {
            setCircularLabelBackgroundColorDrawable(DRAWABLE_ID_ACTIVE_CIRCULAR_LABEL_BG);
        }

        applyCircularLabelIconTint(mActiveCircularLabelIconTint);
        applyCircularLabelTextTint(mActiveCircularLabelTextTint);
        applyTitleAppearance(mActiveTitleAppearanceResId);
        applySubTitleAppearance(mActiveSubTitleAppearanceResId);
        mIsStepActive = true;
    }

    public void inactivateStep(boolean enforceUpdate) {
        inactivateStep(enforceUpdate, false);
    }

    public void inactivateStep(boolean enforceUpdate, boolean animate) {
        if (!mIsStepActive && !enforceUpdate) return;
        if (animate) {
            toggleCircularLabelBackgroundColorDrawable(DRAWABLE_ID_INACTIVE_CIRCULAR_LABEL_BG);
        } else {
            setCircularLabelBackgroundColorDrawable(DRAWABLE_ID_INACTIVE_CIRCULAR_LABEL_BG);
        }
        applyCircularLabelIconTint(mInactiveCircularLabelIconTint);
        applyCircularLabelTextTint(mInactiveCircularLabelTextTint);
        applyTitleAppearance(mInactiveTitleAppearanceResId);
        applySubTitleAppearance(mInactiveSubTitleAppearanceResId);
        mIsStepActive = false;
    }

    private static final int TOGGLE_CIRCULAR_LABEL_BACKGROUND_COLOR_DURATION = 400;

    private void toggleCircularLabelBackgroundColorDrawable(int layerIdToTranslate) {
        mCircularLabelBgDrawableHelper.translateDrawable(layerIdToTranslate,
                TOGGLE_CIRCULAR_LABEL_BACKGROUND_COLOR_DURATION);
    }

    private void setCircularLabelBackgroundColorDrawable(int layerIdToTranslate) {
        mCircularLabelBgDrawableHelper.translateDrawable(layerIdToTranslate, 0);
    }

    public boolean isStepActive() {
        return mIsStepActive;
    }

    private boolean mIsStepCompleted;

    public void completeStep(boolean enforceUpdate) {
        if (mIsStepCompleted && !enforceUpdate) return;
        mStepperCircleTextView.setVisibility(INVISIBLE);
        mStepperCircleIconView.setVisibility(VISIBLE);
        mIsStepCompleted = true;
    }

    public void incompleteStep(boolean enforceUpdate) {
        if (!mIsStepCompleted && !enforceUpdate) return;
        mStepperCircleIconView.setVisibility(INVISIBLE);
        mStepperCircleTextView.setVisibility(VISIBLE);
        mIsStepCompleted = false;
    }

    public boolean isStepCompleted() {
        return mIsStepCompleted;
    }

    public void applyStatusSet(StepperStateSet statusSet, boolean enforceUpdate) {
        if (statusSet.isContentViewExpanded()) {
            expandContentView(enforceUpdate);
        } else {
            collapseContentView(enforceUpdate);
        }

        if (statusSet.isStepActive()) {
            activateStep(enforceUpdate);
        } else {
            inactivateStep(enforceUpdate);
        }

        if (statusSet.isStepCompleted()) {
            completeStep(enforceUpdate);
        } else {
            incompleteStep(enforceUpdate);
        }
    }

    public void applyStatusSetWithAnimation(StepperStateSet statusSet) {
        animateChangingStatus(statusSet.toAnimationFlags());
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef(flag = true,
            value = {ANIMATE_EXPAND_CONTENTS, ANIMATE_COLLAPSE_CONTENTS,
                    ANIMATE_ACTIVATE_STEP, ANIMATE_INACTIVATE_STEP,
                    ANIMATE_COMPLETE_STEP, ANIMATE_INCOMPLETE_STEP})
    public @interface ChangingStatusAnimationFlag {}
    public static final int ANIMATE_EXPAND_CONTENTS = 1;
    public static final int ANIMATE_COLLAPSE_CONTENTS = 1 << 1;
    public static final int ANIMATE_ACTIVATE_STEP = 1 << 2;
    public static final int ANIMATE_INACTIVATE_STEP = 1 << 3;
    public static final int ANIMATE_COMPLETE_STEP = 1 << 4;
    public static final int ANIMATE_INCOMPLETE_STEP = 1 << 5;

   public void animateChangingStatus(@ChangingStatusAnimationFlag int flags) {
       TransitionManager.beginDelayedTransition((ViewGroup) getParent());

       if ((flags & ANIMATE_EXPAND_CONTENTS) != 0) {
           expandContentView(false);
       }
       if ((flags & ANIMATE_COLLAPSE_CONTENTS) != 0) {
           collapseContentView(false);
       }
       if ((flags & ANIMATE_ACTIVATE_STEP) != 0) {
           activateStep(false, true);
       }
       if ((flags & ANIMATE_INACTIVATE_STEP) != 0) {
           inactivateStep(false, true);
       }
       if ((flags & ANIMATE_COMPLETE_STEP) != 0) {
           completeStep(false);
       }
       if ((flags & ANIMATE_INCOMPLETE_STEP) != 0) {
           incompleteStep(false);
       }
   }

    /**
     * STYLE
     * ----------------------------------------------------------------------------- */

    public void setActiveThemeColor(int activeThemeColor) {
        mActiveThemeColor = activeThemeColor;
        applyCircularLabelColor(activeThemeColor, DRAWABLE_ID_ACTIVE_CIRCULAR_LABEL_BG);
    }

    public void setInactiveThemeColor(int inactiveThemeColor) {
        mInactiveThemeColor = inactiveThemeColor;
        applyCircularLabelColor(inactiveThemeColor, DRAWABLE_ID_INACTIVE_CIRCULAR_LABEL_BG);
    }

    public void setActiveTitleAppearanceResId(int activeTitleAppearanceResId) {
        mActiveTitleAppearanceResId = activeTitleAppearanceResId;
        if (mIsStepActive) {
            applyTitleAppearance(activeTitleAppearanceResId);
        }
    }

    public void setInactiveTitleAppearanceResId(int inactiveTitleAppearanceResId) {
        mInactiveTitleAppearanceResId = inactiveTitleAppearanceResId;
        if (mIsStepActive) {
            applyTitleAppearance(inactiveTitleAppearanceResId);
        }
    }

    public void setActiveSubTitleAppearanceResId(int activeSubTitleAppearanceResId) {
        mActiveSubTitleAppearanceResId = activeSubTitleAppearanceResId;
        if (!mIsStepActive) {
            applySubTitleAppearance(activeSubTitleAppearanceResId);
        }
    }

    public void setInactiveSubTitleAppearanceResId(int inactiveSubTitleAppearanceResId) {
        mInactiveSubTitleAppearanceResId = inactiveSubTitleAppearanceResId;
        if (!mIsStepActive) {
            applySubTitleAppearance(inactiveSubTitleAppearanceResId);
        }
    }

    private void applyCircularLabelColor(@ColorInt int color, int drawableIdToApplyColor) {
        Drawable bg = DrawableCompat.wrap(mCircularLabelBgDrawableHelper.getDrawableById(drawableIdToApplyColor));
        DrawableCompat.setTint(bg, color);
        DrawableCompat.setTintMode(bg, PorterDuff.Mode.SRC_IN);
    }

    private void applyTitleAppearance(@StyleRes int appearanceResId) {
        TextViewCompat.setTextAppearance(mTitleView, appearanceResId);
    }

    private void applySubTitleAppearance(@StyleRes int appearanceResId) {
        TextViewCompat.setTextAppearance(mSubTitleView, appearanceResId);
    }

    /**
     * The following constants are defined in res/values/attrs_vertical_stepper_view.xml with enum tag
     */
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({CIRCULAR_LABEL_SIZE_SMALL, CIRCULAR_LABEL_SIZE_REGULAR})
    public @interface CircularLabelSize {}
    public static final int CIRCULAR_LABEL_SIZE_SMALL = 0;
    public static final int CIRCULAR_LABEL_SIZE_REGULAR = 1;

    public void setCircularLabelSize(@CircularLabelSize int size) {
        ViewGroup.LayoutParams params = mStepperCircleLayout.getLayoutParams();
        int textSize;
        int iconPadding;

        if (size == CIRCULAR_LABEL_SIZE_REGULAR) {
            params.width = getResources().getDimensionPixelSize(R.dimen.vertical_regular_stepper_circle_size);
            params.height = getResources().getDimensionPixelSize(R.dimen.vertical_regular_stepper_circle_size);
            textSize = getResources().getDimensionPixelSize(R.dimen.vertical_regular_stepper_circle_text_size);
            iconPadding = getResources().getDimensionPixelSize(R.dimen.vertical_regular_stepper_circle_icon_padding);

        } else if (size == CIRCULAR_LABEL_SIZE_SMALL) {
            params.width = getResources().getDimensionPixelSize(R.dimen.vertical_small_stepper_circle_size);
            params.height = getResources().getDimensionPixelSize(R.dimen.vertical_small_stepper_circle_size);
            textSize = getResources().getDimensionPixelSize(R.dimen.vertical_small_stepper_circle_text_size);
            iconPadding = getResources().getDimensionPixelSize(R.dimen.vertical_small_stepper_circle_icon_padding);

        } else {
            throw new IllegalArgumentException();
        }

        mStepperCircleLayout.setLayoutParams(params);
        mStepperCircleTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        mStepperCircleIconView.setPadding(iconPadding, iconPadding, iconPadding, iconPadding);
    }

    public void setActiveCircularLabelIconTint(@ColorInt int activeCircularLabelIconTint) {
        mActiveCircularLabelIconTint = activeCircularLabelIconTint;
        if (mIsStepActive) {
            applyCircularLabelIconTint(activeCircularLabelIconTint);
        }
    }

    public void setActiveCircularLabelTextTint(@ColorInt int activeCircularLabelTextTint) {
        mActiveCircularLabelTextTint = activeCircularLabelTextTint;
        if (mIsStepActive) {
            applyCircularLabelTextTint(activeCircularLabelTextTint);
        }
    }

    public void setInactiveCircularLabelIconTint(@ColorInt int inactiveCircularLabelIconTint) {
        mInactiveCircularLabelIconTint = inactiveCircularLabelIconTint;
        if (!mIsStepActive) {
            applyCircularLabelIconTint(inactiveCircularLabelIconTint);
        }
    }

    public void setInactiveCircularLabelTextTint(@ColorInt int inactiveCircularLabelTextTint) {
        mInactiveCircularLabelTextTint = inactiveCircularLabelTextTint;
        if (!mIsStepActive) {
            applyCircularLabelTextTint(inactiveCircularLabelTextTint);
        }
    }

    private void applyCircularLabelIconTint(@ColorInt int color) {
        mStepperCircleIconView.setColorFilter(color);
    }

    private void applyCircularLabelTextTint(@ColorInt int color) {
        mStepperCircleTextView.setTextColor(color);
    }

    public void setCompletedIcon(@DrawableRes int iconResId) {
        mStepperCircleIconView.setImageResource(iconResId);
    }

    public void setStepConnectionLineWidth(@Dimension int widthInDip) {
        mStepperConnectionLinePaint.setStrokeWidth(
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, widthInDip, getResources().getDisplayMetrics()));
        invalidate();
    }

    public void setStepperConnectionLineColor(@ColorInt int color) {
        mStepperConnectionLinePaint.setColor(color);
        invalidate();
    }
}