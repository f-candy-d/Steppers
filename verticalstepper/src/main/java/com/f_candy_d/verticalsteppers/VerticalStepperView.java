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
import android.support.annotation.Px;
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

        // # Set or Load default attributes

        mActiveThemeColor = ContextCompat.getColor(getContext(), R.color.vertical_stepper_active_primary);
        mInactiveThemeColor = ContextCompat.getColor(getContext(), R.color.vertical_stepper_inactive_primary);
        mActiveCircularLabelIconTint = ContextCompat.getColor(getContext(), R.color.vertical_stepper_active_circular_label_icon_tint);
        mInactiveCircularLabelIconTint = ContextCompat.getColor(getContext(), R.color.vertical_stepper_inactive_circular_label_icon_tint);
        mActiveCircularLabelTextTint = ContextCompat.getColor(getContext(), R.color.vertical_stepper_active_circular_label_text_tint);
        mInactiveCircularLabelTextTint = ContextCompat.getColor(getContext(), R.color.vertical_stepper_inactive_circular_label_text_tint);
        mActiveTitleAppearanceResId = R.style.VerticalStepperActiveTitleAppearance;
        mActiveSubTitleAppearanceResId = R.style.VerticalStepperActiveSubTitleAppearance;
        mInactiveTitleAppearanceResId = R.style.VerticalStepperInactiveTitleAppearance;
        mInactiveSubTitleAppearanceResId = R.style.VerticalStepperInactiveSubTitleAppearance;
        @CircularLabelSize int circularLabelSize = CIRCULAR_LABEL_SIZE_REGULAR;
        @ColorInt int stepConnectionLineColor = ContextCompat.getColor(getContext(), R.color.vertical_stepper_connector_line);
        @Px int stepConnectionLineWidth = getResources().getDimensionPixelSize(R.dimen.vertical_stepper_connection_line_width);
        @DrawableRes int completedIconResId = R.drawable.ic_check;

        // # Load Attributes from xml

        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.VerticalStepperView, defStyle, 0);

        mActiveThemeColor = a.getColor(R.styleable.VerticalStepperView_activeThemeColor, mActiveThemeColor);
        mInactiveThemeColor = a.getColor(R.styleable.VerticalStepperView_inactiveThemeColor, mInactiveThemeColor);
        mActiveCircularLabelIconTint = a.getColor(R.styleable.VerticalStepperView_activeCircularLabelIconTint, mActiveCircularLabelIconTint);
        mInactiveCircularLabelIconTint = a.getColor(R.styleable.VerticalStepperView_inactiveCircularLabelIconTint, mInactiveCircularLabelIconTint);
        mActiveCircularLabelTextTint = a.getColor(R.styleable.VerticalStepperView_activeCircularLabelTextTint, mActiveCircularLabelTextTint);
        mInactiveCircularLabelTextTint = a.getColor(R.styleable.VerticalStepperView_inactiveCircularLabelTextTint, mInactiveCircularLabelTextTint);
        stepConnectionLineColor = a.getColor(R.styleable.VerticalStepperView_stepConnectorColor, stepConnectionLineColor);
        stepConnectionLineWidth = a.getDimensionPixelSize(R.styleable.VerticalStepperView_stepConnectionLineWidth, stepConnectionLineWidth);
        mActiveTitleAppearanceResId = a.getResourceId(R.styleable.VerticalStepperView_activeTitleAppearance, mActiveTitleAppearanceResId);
        mActiveSubTitleAppearanceResId = a.getResourceId(R.styleable.VerticalStepperView_activeSubTitleAppearance, mActiveSubTitleAppearanceResId);
        mInactiveTitleAppearanceResId = a.getResourceId(R.styleable.VerticalStepperView_inactiveTitleAppearance, mInactiveTitleAppearanceResId);
        mInactiveSubTitleAppearanceResId = a.getResourceId(R.styleable.VerticalStepperView_inactiveSubTitleAppearance, mInactiveSubTitleAppearanceResId);
        completedIconResId = a.getResourceId(R.styleable.VerticalStepperView_completedIcon, completedIconResId);

        switch (a.getInt(R.styleable.VerticalStepperView_circularLabelSize, circularLabelSize)) {
            case CIRCULAR_LABEL_SIZE_REGULAR: circularLabelSize = CIRCULAR_LABEL_SIZE_REGULAR; break;
            case CIRCULAR_LABEL_SIZE_SMALL: circularLabelSize = CIRCULAR_LABEL_SIZE_SMALL; break;
        }

        String title = a.getString(R.styleable.VerticalStepperView_title);
        String subTitle = a.getString(R.styleable.VerticalStepperView_subTitle);
        String stepLabel = a.getString(R.styleable.VerticalStepperView_stepLabel);
        boolean expandContentsByDefault = a.getBoolean(R.styleable.VerticalStepperView_expandContentsByDefault, false);
        boolean activateStepByDefault = a.getBoolean(R.styleable.VerticalStepperView_activateStepByDefault, false);
        boolean completeStepByDefault = a.getBoolean(R.styleable.VerticalStepperView_completeStepByDefault, false);
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
        mStepperCircleIconView = findViewById(R.id.stepper_circle_icon);
        mStepperCircleLayout = findViewById(R.id.stepper_circle_layout);
        mContentViewContainer = findViewById(R.id.content_view_container);

        // # Setup TransitionDrawable for Circular Label's background

        mCircularLabelBgDrawableHelper = new TransitionDrawableHelper((TransitionDrawable) mStepperCircleLayout.getBackground());
        mCircularLabelBgDrawableHelper.setFirstLayerId(DRAWABLE_ID_ACTIVE_CIRCULAR_LABEL_BG);
        mCircularLabelBgDrawableHelper.setSecondLayerId(DRAWABLE_ID_INACTIVE_CIRCULAR_LABEL_BG);
        applyCircularLabelColor(mActiveThemeColor, DRAWABLE_ID_ACTIVE_CIRCULAR_LABEL_BG);
        applyCircularLabelColor(mInactiveThemeColor, DRAWABLE_ID_INACTIVE_CIRCULAR_LABEL_BG);

        // # For onDraw() method

        mStepperConnectionLinePaint = new Paint();
        mStepperConnectionLinePaint.setColor(stepConnectionLineColor);
        mStepperConnectionLinePaint.setStrokeWidth(stepConnectionLineWidth);
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
        } else {
            setCollapsedContentView(null);
        }
        if (expandedContentViewResId != 0) {
            setExpandedContentView(expandedContentViewResId);
        } else {
            setExpandedContentView(null);
        }

        setTitle(title);
        setSubTitle(subTitle);
        setStepLabel(stepLabel);
        setCircularLabelSize(circularLabelSize);
        setCompletedIcon(completedIconResId);

        if (expandContentsByDefault) expandContentView(true); else collapseContentView(true);
        if (activateStepByDefault) activateStep(true); else inactivateStep(true);
        if (completeStepByDefault) completeStep(true); else incompleteStep(true);
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
        if (mIsStepActive && !enforceUpdate) return;
        toggleCircularLabelBackgroundColorDrawable(DRAWABLE_ID_ACTIVE_CIRCULAR_LABEL_BG);
        applyCircularLabelIconTint(mActiveCircularLabelIconTint);
        applyCircularLabelTextTint(mActiveCircularLabelTextTint);
        applyTitleAppearance(mActiveTitleAppearanceResId);
        applySubTitleAppearance(mActiveSubTitleAppearanceResId);
        mIsStepActive = true;
    }

    public void inactivateStep(boolean enforceUpdate) {
        if (!mIsStepActive && !enforceUpdate) return;
        toggleCircularLabelBackgroundColorDrawable(DRAWABLE_ID_INACTIVE_CIRCULAR_LABEL_BG);
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
       TransitionManager.beginDelayedTransition(this);

       if ((flags & ANIMATE_EXPAND_CONTENTS) != 0) {
           expandContentView(false);
       }
       if ((flags & ANIMATE_COLLAPSE_CONTENTS) != 0) {
           collapseContentView(false);
       }
       if ((flags & ANIMATE_ACTIVATE_STEP) != 0) {
           activateStep(false);
       }
       if ((flags & ANIMATE_INACTIVATE_STEP) != 0) {
           inactivateStep(false);
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
        if (mIsStepActive) {
            applyCircularLabelColor(activeThemeColor, DRAWABLE_ID_ACTIVE_CIRCULAR_LABEL_BG);
        }
    }

    public void setInactiveThemeColor(int inactiveThemeColor) {
        mInactiveThemeColor = inactiveThemeColor;
        if (!mIsStepActive) {
            applyCircularLabelColor(inactiveThemeColor, DRAWABLE_ID_INACTIVE_CIRCULAR_LABEL_BG);
        }
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
        Drawable bg = mCircularLabelBgDrawableHelper.getDrawableById(drawableIdToApplyColor);
        if (bg == null) {
            throw new IllegalArgumentException(
                    "The second argument must be one of DRAWABLE_ID_ACTIVE_CIRCULAR_LABEL_BG, DRAWABLE_ID_INACTIVE_CIRCULAR_LABEL_BG");
        }
        bg = DrawableCompat.wrap(bg);
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