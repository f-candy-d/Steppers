package com.f_candy_d.verticalsteppers;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.widget.TextViewCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * TODO: document your custom view class.
 *
 * Add the 'isChecked' attribute
 */
class VerticalStepperView extends RelativeLayout {

    private String mStepLabel;
    private String mTitle;
    private String mSubTitle;

    // Colors
    private int mStepperCircleActiveColor;
    private int mStepperCircleInactiveColor;
    private int mTitleActiveColor;
    private int mTitleInActiveColor;
    private int mSubTitleActiveColor;
    private int mSubTitleInactiveColor;

    // Dimens
    private int mStepperConnectionLineTopMargin;
    private int mStepperConnectionLineBottomMargin;

    // UI
    private TextView mTitleView;
    private TextView mSubTitleView;
    private TextView mStepperCircleTextView;
    private ImageView mStepperCircleImgView;
    private FrameLayout mStepperCircleLayout;
    private FrameLayout mContentViewContainer;
    private View mContentView;

    private Paint mStepperConnectionLinePaint;

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

        // # Colors

        mStepperCircleActiveColor = ContextCompat.getColor(getContext(), R.color.vertical_stepper_circle_active_color);
        mStepperCircleInactiveColor = ContextCompat.getColor(getContext(), R.color.vertical_stepper_circle_inactive_color);
        mTitleActiveColor = ContextCompat.getColor(getContext(), R.color.vertical_stepper_title_active);
        mTitleInActiveColor = ContextCompat.getColor(getContext(), R.color.vertical_stepper_title_inactive);
        mSubTitleActiveColor = ContextCompat.getColor(getContext(), R.color.vertical_stepper_sub_title_active);
        mSubTitleInactiveColor = ContextCompat.getColor(getContext(), R.color.vertical_stepper_sub_title_inactive);

        // # Dimens

        mStepperConnectionLineTopMargin = getResources().getDimensionPixelSize(R.dimen.vertical_stepper_connection_line_top_margin);
        mStepperConnectionLineBottomMargin = getResources().getDimensionPixelSize(R.dimen.vertical_stepper_connection_line_bottom_margin);

        // # Load Attributes

        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.VerticalStepperView, defStyle, 0);

        mTitle = a.getString(R.styleable.VerticalStepperView_title);
        mSubTitle = a.getString(R.styleable.VerticalStepperView_subTitle);
        mStepLabel = a.getString(R.styleable.VerticalStepperView_stepLabel);
        // Load layout's resource id; -> https://stackoverflow.com/questions/25303979/custom-xml-attribute-to-a-layout-reference
        // And id = 0 is a invalid resource id; -> https://stackoverflow.com/questions/5130789/android-resource-ids
        final int contentViewResId = a.getResourceId(R.styleable.VerticalStepperView_contentViewLayout, 0);

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

        // # Inflate a content view if an user specified it in a layout XML file

        if (contentViewResId != 0) {
            setContentView(inflate(getContext(), contentViewResId, null));
        }

        // # Add ripple-effect to myself

        // See -> https://stackoverflow.com/questions/37987732/programatically-set-selectableitembackground-on-android-view
        TypedValue outValue = new TypedValue();
        getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
        this.setBackgroundResource(outValue.resourceId);
        this.setClickable(true);

        // # Connection Line For Stepper Circle

        mStepperConnectionLinePaint = new Paint();
        mStepperConnectionLinePaint.setColor(ContextCompat.getColor(getContext(), R.color.vertical_stepper_connector_line));
        mStepperConnectionLinePaint.setStrokeWidth(getResources().getDimensionPixelSize(R.dimen.vertical_stepper_connection_line_width));

        // # Default values

        mTitleView.setText(mTitle);
        mSubTitleView.setText(mSubTitle);
        mStepperCircleTextView.setText(String.valueOf(mStepLabel));
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
    
    public void setTitle(String title) {
        mTitle = title;
        mTitleView.setText(title);
    }

    public void setSubTitle(String subTitle) {
        mSubTitle = subTitle;
        mSubTitleView.setText(subTitle);
    }

    public void setStepLabel(int number) {
        setStepLabel(String.valueOf(number));
    }

    public void setStepLabel(String text) {
        mStepLabel = text;
        mStepperCircleTextView.setText(text);
    }

    public void setContentView(View view) {
        if (0 < mContentViewContainer.getChildCount()) {
            mContentViewContainer.removeAllViews();
        }

        mContentView = view;
        if (view != null) {
            mContentViewContainer.addView(view);
            mContentViewContainer.setVisibility(VISIBLE);
        } else {
            mContentViewContainer.setVisibility(GONE);
        }
    }

    public View getContentView() {
        return mContentView;
    }

    ViewGroup getContentViewContainer() {
        return mContentViewContainer;
    }

    public void applyStatus(VerticalStepperStatus status, boolean enforceUpdates) {
        setIsActiveUi(status.isActive(), enforceUpdates);
        setIsCompletedUi(status.isCompleted(), enforceUpdates);
    }

    // Default is TRUE
    private boolean mIsActiveUi = true;

    public void setIsActiveUi(boolean isActiveUi, boolean enforceUpdate) {
        if (isActiveUi == mIsActiveUi && !enforceUpdate) return;

        // # Stepper Circle Color

        int color = (isActiveUi) ? mStepperCircleActiveColor : mStepperCircleInactiveColor;

        Drawable bg = DrawableCompat.wrap(mStepperCircleTextView.getBackground());
        DrawableCompat.setTint(bg, color);
        DrawableCompat.setTintMode(bg, PorterDuff.Mode.SRC_IN);

        bg = DrawableCompat.wrap(mStepperCircleImgView.getBackground());
        DrawableCompat.setTint(bg, color);
        DrawableCompat.setTintMode(bg, PorterDuff.Mode.SRC_IN);

        // # Title & Sub-Title Color

//        color = (isActiveUi) ? mTitleActiveColor : mTitleInActiveColor;
//        mTitleView.setTextColor(color);
//        color = (isActiveUi) ? mSubTitleActiveColor : mSubTitleInactiveColor;
//        mSubTitleView.setTextColor(color);
        int appearanceResId = (isActiveUi) ? R.style.VerticalStepperActiveTitleAppearance : R.style.VerticalStepperInactiveTitleAppearance;
        TextViewCompat.setTextAppearance(mTitleView, appearanceResId);
        appearanceResId = (isActiveUi) ? R.style.VerticalStepperActiveSubTitleAppearance : R.style.VerticalStepperInactiveSubTitleAppearance;
        TextViewCompat.setTextAppearance(mSubTitleView, appearanceResId);

        // Update
        mIsActiveUi = isActiveUi;
    }

    // Default is FALSE
    private boolean mIsCompletedUi = false;

    public void setIsCompletedUi(boolean isCompletedUi, boolean enforceUpdate) {
        if (isCompletedUi == mIsCompletedUi && !enforceUpdate) return;

        // TODO; ANIMATE WHEN TOGGLE STEPPER CIRCLES
        if (isCompletedUi) {
//            toggleViewsWithScaleAnimation(mStepperCircleTextView, mStepperCircleImgView);
            mStepperCircleTextView.setVisibility(INVISIBLE);
            mStepperCircleImgView.setVisibility(VISIBLE);
        } else {
//            toggleViewsWithScaleAnimation(mStepperCircleImgView, mStepperCircleTextView);
            mStepperCircleTextView.setVisibility(VISIBLE);
            mStepperCircleImgView.setVisibility(INVISIBLE);
        }

        // Update
        mIsCompletedUi = isCompletedUi;
    }

    private void toggleViewsWithScaleAnimation(final View hiddenView, final View revealView) {
        revealView.setVisibility(INVISIBLE);
        hiddenView.setVisibility(VISIBLE);
        ScaleAnimation scaleUpAnimation = new ScaleAnimation(1f, 0f, 1f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleUpAnimation.setDuration(150);
        scaleUpAnimation.setRepeatCount(0);
        scaleUpAnimation.setFillAfter(true);
        scaleUpAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                revealView.setScaleX(0f);
                revealView.setScaleY(0f);
                hiddenView.setVisibility(INVISIBLE);
                revealView.setVisibility(VISIBLE);
                ScaleAnimation scaleDownAnimation = new ScaleAnimation(0f, 1f, 0f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                scaleDownAnimation.setDuration(150);
                scaleDownAnimation.setRepeatCount(0);
                scaleDownAnimation.setFillAfter(true);
                revealView.startAnimation(scaleDownAnimation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        hiddenView.startAnimation(scaleUpAnimation);
    }
}