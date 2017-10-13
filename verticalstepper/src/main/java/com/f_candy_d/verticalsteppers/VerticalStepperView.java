package com.f_candy_d.verticalsteppers;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.IntDef;
import android.support.annotation.LayoutRes;
import android.support.annotation.StyleRes;
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

class VerticalStepperView extends RelativeLayout {

    // Will be used only in #onDraw()
    // Initialize or load these values in #init()
    private Paint mStepperConnectionLinePaint;
    private int mStepperConnectionLineTopMargin;
    private int mStepperConnectionLineBottomMargin;

    // UI
    private TextView mTitleView;
    private TextView mSubTitleView;
    private TextView mStepperCircleTextView;
    private ImageView mStepperCircleImgView;
    private FrameLayout mStepperCircleLayout;
    private FrameLayout mContentViewContainer;
    private View mExpandedContentView;
    private View mCollapsedContentView;

    // Attributes

    // Theme of stepper
    @ColorInt private int mActiveThemeColor;
    @ColorInt private int mInactiveThemeColor;
    @ColorInt private int mStepConnectorLineColor;

    // Theme of title
    @StyleRes private int mActiveTitleAppearanceResId;
    @StyleRes private int mInactiveTitleAppearanceResId;

    // Theme of sub-title
    @StyleRes private int mActiveSubTitleAppearanceResId;
    @StyleRes private int mInactiveSubTitleAppearanceResId;

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

        // # Set or Load default attributes

        mActiveThemeColor = ContextCompat.getColor(getContext(), R.color.vertical_stepper_active_primary);
        mInactiveThemeColor = ContextCompat.getColor(getContext(), R.color.vertical_stepper_inactive_primary);
        mStepConnectorLineColor = ContextCompat.getColor(getContext(), R.color.vertical_stepper_connector_line);
        mActiveTitleAppearanceResId = R.style.VerticalStepperActiveTitleAppearance;
        mActiveSubTitleAppearanceResId = R.style.VerticalStepperActiveSubTitleAppearance;
        mInactiveTitleAppearanceResId = R.style.VerticalStepperInactiveTitleAppearance;
        mInactiveSubTitleAppearanceResId = R.style.VerticalStepperInactiveSubTitleAppearance;
        @CircularLabelSize int circularLabelSize = CIRCULAR_LABEL_SIZE_REGULAR;
        @ColorInt int circularLabelIconTint = ContextCompat.getColor(getContext(), R.color.vertical_stepper_circle_icon_tint);

        // # Load Attributes from xml

        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.VerticalStepperView, defStyle, 0);

        mActiveThemeColor = a.getColor(R.styleable.VerticalStepperView_activeThemeColor, mActiveThemeColor);
        mInactiveThemeColor = a.getColor(R.styleable.VerticalStepperView_inactiveThemeColor, mInactiveThemeColor);
        mStepConnectorLineColor = a.getColor(R.styleable.VerticalStepperView_stepConnectorColor, mStepConnectorLineColor);
        mActiveTitleAppearanceResId = a.getResourceId(R.styleable.VerticalStepperView_activeTitleAppearance, mActiveTitleAppearanceResId);
        mActiveSubTitleAppearanceResId = a.getResourceId(R.styleable.VerticalStepperView_activeSubTitleAppearance, mActiveSubTitleAppearanceResId);
        mInactiveTitleAppearanceResId = a.getResourceId(R.styleable.VerticalStepperView_inactiveTitleAppearance, mInactiveTitleAppearanceResId);
        mInactiveSubTitleAppearanceResId = a.getResourceId(R.styleable.VerticalStepperView_inactiveSubTitleAppearance, mInactiveSubTitleAppearanceResId);

        switch (a.getInt(R.styleable.VerticalStepperView_circularLabelSize, circularLabelSize)) {
            case CIRCULAR_LABEL_SIZE_REGULAR: circularLabelSize = CIRCULAR_LABEL_SIZE_REGULAR; break;
            case CIRCULAR_LABEL_SIZE_SMALL: circularLabelSize = CIRCULAR_LABEL_SIZE_SMALL; break;
        }

        circularLabelIconTint = a.getColor(R.styleable.VerticalStepperView_circularLabelIconTint, circularLabelIconTint);
        String title = a.getString(R.styleable.VerticalStepperView_title);
        String subTitle = a.getString(R.styleable.VerticalStepperView_subTitle);
        String stepLabel = a.getString(R.styleable.VerticalStepperView_stepLabel);
        // Load layout's resource id; -> https://stackoverflow.com/questions/25303979/custom-xml-attribute-to-a-layout-reference
        // And id = 0 is a invalid resource id; -> https://stackoverflow.com/questions/5130789/android-resource-ids
        final int collapsedContentViewResId = a.getResourceId(R.styleable.VerticalStepperView_collapsedContentViewLayout, 0);
        final int expandedContentViewResId = a.getResourceId(R.styleable.VerticalStepperView_expandedContentViewLayout, 0);

        a.recycle();

        // # UI

        // Load the layout file
        inflate(getContext(), R.layout.vertical_stepper_view, this);

        mTitleView = findViewById(R.id.work_title);
        mSubTitleView = findViewById(R.id.work_sub_title);
        mStepperCircleTextView = findViewById(R.id.stepper_circle_text);
        mStepperCircleImgView = findViewById(R.id.stepper_circle_icon);
        mStepperCircleLayout = findViewById(R.id.stepper_circle_layout);
        mContentViewContainer = findViewById(R.id.content_view_container);

        // # For onDraw() method

        mStepperConnectionLinePaint = new Paint();
        mStepperConnectionLinePaint.setColor(mStepConnectorLineColor);
        mStepperConnectionLinePaint.setStrokeWidth(getResources().getDimensionPixelSize(R.dimen.vertical_stepper_connection_line_width));

        mStepperConnectionLineTopMargin = getResources().getDimensionPixelSize(R.dimen.vertical_stepper_connection_line_top_margin);
        mStepperConnectionLineBottomMargin = getResources().getDimensionPixelSize(R.dimen.vertical_stepper_connection_line_bottom_margin);

        // # Add ripple-effect to myself
        // # See -> https://stackoverflow.com/questions/37987732/programatically-set-selectableitembackground-on-android-view

        TypedValue outValue = new TypedValue();
        getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
        this.setBackgroundResource(outValue.resourceId);
        this.setClickable(true);

        // # Setup view's default status

        //Inflate a content views if an user specify it in a layout XML file
        if (collapsedContentViewResId != 0) {
            setCollapsedContentView(collapsedContentViewResId);
        }
        if (expandedContentViewResId != 0) {
            setExpandedContentView(expandedContentViewResId);
        }

        setTitle(title);
        setSubTitle(subTitle);
        setStepLabel(stepLabel);
        setTitleAppearance(mActiveTitleAppearanceResId);
        setSubTitleAppearance(mActiveSubTitleAppearanceResId);
        setCircularLabelColor(mActiveThemeColor);
        setCircularLabelIconTint(circularLabelIconTint);
        setCircularLabelSize(circularLabelSize);
        expandContentView(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // TODO: consider storing these as member variables to reduce
        // allocations per draw cycle.
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

//        int contentWidth = getWidth() - paddingLeft - paddingRight;
        int contentHeight = getHeight() - paddingTop - paddingBottom;

        // # Connection Line

        int connectionLineX = mStepperCircleLayout.getLeft() + mStepperCircleLayout.getWidth() / 2;
        int connectionLineStartY = mStepperCircleLayout.getBottom() + mStepperConnectionLineTopMargin;
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
        if (mExpandedContentView != null) {
            mContentViewContainer.removeView(mExpandedContentView);
        }

        if (view != null) {
            mContentViewContainer.addView(view);
            mExpandedContentView = view;
            mContentViewContainer.setVisibility(VISIBLE);
        } else {
            mExpandedContentView = null;
            if (mContentViewContainer.getChildCount() == 0) {
                mContentViewContainer.setVisibility(GONE);
            }
        }
    }

    public View setCollapsedContentView(@LayoutRes int layoutId) {
        View view = inflate(getContext(), layoutId, null);
        setCollapsedContentView(view);
        return view;
    }

    public void setCollapsedContentView(View view) {
        if (mCollapsedContentView != null) {
            mContentViewContainer.removeView(mCollapsedContentView);
        }

        if (view != null) {
            mContentViewContainer.addView(view);
            mCollapsedContentView = view;
            mContentViewContainer.setVisibility(VISIBLE);
        } else {
            mCollapsedContentView = null;
            if (mContentViewContainer.getChildCount() == 0) {
                mContentViewContainer.setVisibility(GONE);
            }
        }
    }

    /**
     * STATUS
     * ----------------------------------------------------------------------------- */

    private boolean mIsContentViewExpanded;

    public void expandContentView(boolean enforceUpdate) {
        if (mIsContentViewExpanded && !enforceUpdate) return;
        if (mCollapsedContentView != null) {
            mCollapsedContentView.setVisibility(GONE);
        }
        if (mExpandedContentView != null) {
            mExpandedContentView.setVisibility(VISIBLE);
        }
        mIsContentViewExpanded = true;
    }

    public void collapseContentView(boolean enforceUpdate) {
        if (!mIsContentViewExpanded && !enforceUpdate) return;
        if (mExpandedContentView != null) {
            mExpandedContentView.setVisibility(GONE);
        }
        if (mCollapsedContentView != null) {
            mCollapsedContentView.setVisibility(VISIBLE);
        }
        mIsContentViewExpanded = false;
    }

    /**
     * STYLE
     * ----------------------------------------------------------------------------- */

//    public void applyStatus(@NonNull VerticalStepperStatus status, boolean enforceUpdates) {
//        applyActiveStatus(status.isActive(), enforceUpdates);
//        applyCompletedStatus(status.isCompleted(), enforceUpdates);
//        mStepperStatus.copy(status);
//    }
//
//    public void applyStyle(@NonNull VerticalStepperStyle style, boolean enforceUpdates) {
//        mStepperStyle.copy(style);
//        // icon
//        mStepperCircleImgView.setImageResource(style.getCompletedIconRes());
//        // Stepper Circle Size
//        applyStepperCircleSize(style.getStepperCircleSize());
//        // Others
//        applyStatus(mStepperStatus, enforceUpdates);
//    }

//    public void applyActiveStatus(boolean isActive, boolean enforceUpdate) {
//        if (isActive == mStepperStatus.isActive() && !enforceUpdate) return;
//
//        // # Stepper Circle Color
//
//        int color = ContextCompat.getColor(getContext(),
//                (isActive) ? mStepperStyle.getActiveColorRes() : mStepperStyle.getInactiveColorRes());
//
//        Drawable bg = DrawableCompat.wrap(mStepperCircleTextView.getBackground());
//        DrawableCompat.setTint(bg, color);
//        DrawableCompat.setTintMode(bg, PorterDuff.Mode.SRC_IN);
//
//        bg = DrawableCompat.wrap(mStepperCircleImgView.getBackground());
//        DrawableCompat.setTint(bg, color);
//        DrawableCompat.setTintMode(bg, PorterDuff.Mode.SRC_IN);
//
//        // # Title & Sub-Title Appearance
//
//        int appearanceResId = (isActive) ? mStepperStyle.getActiveTitleAppearanceRes() : mStepperStyle.getInactiveTitleAppearanceRes();
//        TextViewCompat.setTextAppearance(mTitleView, appearanceResId);
//        appearanceResId = (isActive) ? mStepperStyle.getActiveSubTitleAppearanceRes() : mStepperStyle.getInactiveSubTitleAppearanceRes();
//        TextViewCompat.setTextAppearance(mSubTitleView, appearanceResId);
//
//        // Update
//        mStepperStatus.setActive(isActive);
//    }
//
//    public void applyCompletedStatus(boolean isCompleted, boolean enforceUpdate) {
//        if (isCompleted == mStepperStatus.isCompleted() && !enforceUpdate) return;
//
//        // TODO; ANIMATE WHEN TOGGLE STEPPER CIRCLES
//        if (isCompleted) {
//            mStepperCircleTextView.setVisibility(INVISIBLE);
//            mStepperCircleImgView.setVisibility(VISIBLE);
//        } else {
//            mStepperCircleTextView.setVisibility(VISIBLE);
//            mStepperCircleImgView.setVisibility(INVISIBLE);
//        }
//
//        // Update
//        mStepperStatus.setCompleted(isCompleted);
//    }
//
//    public void applyStepperCircleSize(@VerticalStepperStyle.CircularLabelSize int size) {
//        ViewGroup.LayoutParams params = mStepperCircleLayout.getLayoutParams();
//        int textSize;
//        int iconPadding;
//
//        if (size == VerticalStepperStyle.CIRCULAR_LABEL_SIZE_REGULAR) {
//            params.width = getResources().getDimensionPixelSize(R.dimen.vertical_regular_stepper_circle_size);
//            params.height = getResources().getDimensionPixelSize(R.dimen.vertical_regular_stepper_circle_size);
//            textSize = getResources().getDimensionPixelSize(R.dimen.vertical_regular_stepper_circle_text_size);
//            iconPadding = getResources().getDimensionPixelSize(R.dimen.vertical_regular_stepper_circle_icon_padding);
//
//        } else if (size == VerticalStepperStyle.CIRCULAR_LABEL_SIZE_SMALL) {
//            params.width = getResources().getDimensionPixelSize(R.dimen.vertical_small_stepper_circle_size);
//            params.height = getResources().getDimensionPixelSize(R.dimen.vertical_small_stepper_circle_size);
//            textSize = getResources().getDimensionPixelSize(R.dimen.vertical_small_stepper_circle_text_size);
//            iconPadding = getResources().getDimensionPixelSize(R.dimen.vertical_small_stepper_circle_icon_padding);
//
//        } else {
//            throw new IllegalArgumentException();
//        }
//
//        mStepperCircleLayout.setLayoutParams(params);
//        mStepperCircleTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
//        mStepperCircleImgView.setPadding(iconPadding, iconPadding, iconPadding, iconPadding);
//    }

    public void setCircularLabelColor(@ColorInt int color) {
        Drawable bg = DrawableCompat.wrap(mStepperCircleTextView.getBackground());
        DrawableCompat.setTint(bg, color);
        DrawableCompat.setTintMode(bg, PorterDuff.Mode.SRC_IN);

        bg = DrawableCompat.wrap(mStepperCircleImgView.getBackground());
        DrawableCompat.setTint(bg, color);
        DrawableCompat.setTintMode(bg, PorterDuff.Mode.SRC_IN);
    }

    public void setTitleAppearance(@StyleRes int appearanceResId) {
        TextViewCompat.setTextAppearance(mTitleView, appearanceResId);
    }

    public void setSubTitleAppearance(@StyleRes int appearanceResId) {
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
        mStepperCircleImgView.setPadding(iconPadding, iconPadding, iconPadding, iconPadding);
    }

    public void setCircularLabelIconTint(@ColorInt int color) {
        mStepperCircleImgView.setColorFilter(color);
    }
}