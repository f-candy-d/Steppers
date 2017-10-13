package com.f_candy_d.verticalsteppers;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
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

/**
 * TODO: document your custom view class.
 *
 * Add the 'isChecked' attribute
 */
class VerticalStepperView extends RelativeLayout {

    private String mStepLabel;
    private String mTitle;
    private String mSubTitle;

    // Style & Status
    private VerticalStepperStyle mStepperStyle;
    private VerticalStepperStatus mStepperStatus;

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

        // # Style & Status

        mStepperStyle = VerticalStepperStyle.createAsDefaultStyle();
        mStepperStatus = VerticalStepperStatus.createAsDefaultStatus();

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
        // # See -> https://stackoverflow.com/questions/37987732/programatically-set-selectableitembackground-on-android-view

        TypedValue outValue = new TypedValue();
        getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
        this.setBackgroundResource(outValue.resourceId);
        this.setClickable(true);

        // # Connection Line For Stepper Circle

        mStepperConnectionLinePaint = new Paint();
        mStepperConnectionLinePaint.setColor(ContextCompat.getColor(getContext(), mStepperStyle.getStepConnectorLineColorRes()));
        mStepperConnectionLinePaint.setStrokeWidth(getResources().getDimensionPixelSize(R.dimen.vertical_stepper_connection_line_width));

        // # Apply default status & style
        applyStyle(mStepperStyle, true);
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

    /**
     * STATUS & STYLE
     * ----------------------------------------------------------------------------- */

    public void applyStatus(@NonNull VerticalStepperStatus status, boolean enforceUpdates) {
        applyActiveStatus(status.isActive(), enforceUpdates);
        applyCompletedStatus(status.isCompleted(), enforceUpdates);
        mStepperStatus.copy(status);
    }

    public void applyStyle(@NonNull VerticalStepperStyle style, boolean enforceUpdates) {
        mStepperStyle.copy(style);
        // icon
        mStepperCircleImgView.setImageResource(style.getCompletedIconRes());
        // Stepper Circle Size
        applyStepperCircleSize(style.getStepperCircleSize());
        // Others
        applyStatus(mStepperStatus, enforceUpdates);
    }

    public void applyActiveStatus(boolean isActive, boolean enforceUpdate) {
        if (isActive == mStepperStatus.isActive() && !enforceUpdate) return;

        // # Stepper Circle Color

        int color = ContextCompat.getColor(getContext(),
                (isActive) ? mStepperStyle.getActiveColorRes() : mStepperStyle.getInactiveColorRes());

        Drawable bg = DrawableCompat.wrap(mStepperCircleTextView.getBackground());
        DrawableCompat.setTint(bg, color);
        DrawableCompat.setTintMode(bg, PorterDuff.Mode.SRC_IN);

        bg = DrawableCompat.wrap(mStepperCircleImgView.getBackground());
        DrawableCompat.setTint(bg, color);
        DrawableCompat.setTintMode(bg, PorterDuff.Mode.SRC_IN);

        // # Title & Sub-Title Appearance

        int appearanceResId = (isActive) ? mStepperStyle.getActiveTitleAppearanceRes() : mStepperStyle.getInactiveTitleAppearanceRes();
        TextViewCompat.setTextAppearance(mTitleView, appearanceResId);
        appearanceResId = (isActive) ? mStepperStyle.getActiveSubTitleAppearanceRes() : mStepperStyle.getInactiveSubTitleAppearanceRes();
        TextViewCompat.setTextAppearance(mSubTitleView, appearanceResId);

        // Update
        mStepperStatus.setActive(isActive);
    }

    public void applyCompletedStatus(boolean isCompleted, boolean enforceUpdate) {
        if (isCompleted == mStepperStatus.isCompleted() && !enforceUpdate) return;

        // TODO; ANIMATE WHEN TOGGLE STEPPER CIRCLES
        if (isCompleted) {
            mStepperCircleTextView.setVisibility(INVISIBLE);
            mStepperCircleImgView.setVisibility(VISIBLE);
        } else {
            mStepperCircleTextView.setVisibility(VISIBLE);
            mStepperCircleImgView.setVisibility(INVISIBLE);
        }

        // Update
        mStepperStatus.setCompleted(isCompleted);
    }

    public void applyStepperCircleSize(@VerticalStepperStyle.StepperCircleSize int size) {
        ViewGroup.LayoutParams params = mStepperCircleLayout.getLayoutParams();
        int textSize;
        int iconPadding;

        if (size == VerticalStepperStyle.STEPPER_CIRCLE_SIZE_REGULAR) {
            params.width = getResources().getDimensionPixelSize(R.dimen.vertical_regular_stepper_circle_size);
            params.height = getResources().getDimensionPixelSize(R.dimen.vertical_regular_stepper_circle_size);
            textSize = getResources().getDimensionPixelSize(R.dimen.vertical_regular_stepper_circle_text_size);
            iconPadding = getResources().getDimensionPixelSize(R.dimen.vertical_regular_stepper_circle_icon_padding);

        } else if (size == VerticalStepperStyle.STEPPER_CIRCLE_SIZE_SMALL) {
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
}